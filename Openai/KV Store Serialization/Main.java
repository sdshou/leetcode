import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        String[][] operations = new String[][]{
            {"put", "a", "1"}, {"get", "a"}, {"serialize", "snap1"}, 
            {"put", "a", "2"}, {"deserialize", "snap1"}, {"get", "a"}
        };
        var res = solution.solution(operations);
        System.out.println(res); // [1, 1]

        byte[] key = "a|b".getBytes(StandardCharsets.UTF_8);
        byte[] val = "v:1".getBytes(StandardCharsets.UTF_8);
        var operations2 = new Object[][]{
            {"put", key, val}, {"serialize", "p"}, {"delete", key},
            {"get", key}, {"deserialize", "p"}, {"get", key}
        };
        res = solution.solution(operations2);
        if (res instanceof List<?> r) {
            byte[] v1 = (byte[]) r.get(1);
            var str = String.format("[%s, %s]", r.get(0), new String(v1, StandardCharsets.UTF_8));
            System.out.println(str);
        } // [None, v:1]
    }
}

public class Solution {
    // `data` and the return value are genuinely heterogeneous: which type occupies
    // each slot is decided by `operation`, so neither can be narrowed past Object.
    //   solution("encode", Map<String,Object> store) -> String  (the serialized buffer)
    //   solution("decode", String buffer)            -> Map<String,Object> (the store)
    // A buffer is carried as a String holding ONE CHAR PER BYTE (each char in 0..255) --
    // the harness marshals Python `bytes` that way; it is NOT UTF-8 text.
    // Store values are Long (tag 1) | Double (tag 2) | Boolean (tag 3) | String (tag 4)
    // | Map<String,Object> (tag 5, nested).
    public Object solution(String operation, Object data) {
        if ("encode".equals(operation)) {
            if (data instanceof Map<?, ?>) {
                var store = (Map<String, Object>)data;
                return encode(store);
            }
        } else if ("decode".equals(operation)) {
            if (data instanceof byte[]) {
                var storeBytes = (byte[])data;
                return decode(storeBytes);
            }
        }
        return null;
    }

    private byte[] encode(Map<String, Object> store) {
        byte[] header = {'K', 'V', 'S', 'B', (byte)1, 0, 0, 0, 0};
        byte[] body = encodeMapPayload(store);
        byte[] bodyLen = Handlers.encodeInt(body.length);
        System.arraycopy(bodyLen, 0, header, 5, 4);
        byte[] footer = getChecksum(header, body);
        byte[] res = new byte[9 + body.length + 4];
        int start = 0;
        System.arraycopy(header, 0, res, start, 9);
        start += 9;
        System.arraycopy(body, 0, res, start, body.length);
        start += body.length;
        System.arraycopy(footer, 0, res, start, 4);
        return res;
    }

    private byte[] encodeMapPayload(Map<String, Object> store) {
        List<byte[]> res = new ArrayList<>();
        int count = store.size();
        res.add(Handlers.encodeInt(count));
        int size = 4;
        for (Map.Entry<String, Object> entry : store.entrySet()) {
            byte[] keyBytes = entry.getKey().getBytes(StandardCharsets.UTF_8);
            Object val = entry.getValue();
            int tag = 0;
            byte[] valBytes;
            switch val.getClass() {
                case Long.class : {
                    tag = 1;
                    valBytes = Handlers.encodeLong((long)val);
                    break;
                }
                case Double.class : {
                    tag = 2;
                    valBytes = Handlers.encodeDouble((double)val);
                    break;
                }
                case Boolean.class : {
                    tag = 3;
                    valBytes = Handlers.encodeBoolean((boolean)val);
                    break;
                }
                case String.class : {
                    tag = 4;
                    valBytes = Handlers.encodeString((String)val);
                    break;
                }
                case Map.class : {
                    tag = 5;
                    valBytes = encodeMapPayload((Map<String, Object>)val);
                    break;
                }
            }
            byte[] entry = new byte[4 + keyBytes.length + 5 + valBytes.length];
            int start = 0;
            System.arraycopy(Handlers.encodeInt(keyBytes.length), 0, entry, start, 4);
            start += 4;
            System.arraycopy(keyBytes, 0, entry, start, keyBytes.length);
            start += keyBytes.length;
            entry[start] = (byte)tag;
            start++;
            System.arraycopy(Handlers.encodeInt(valBytes.length), 0, entry, start, 4);
            start += 4;
            System.arraycopy(valBytes, 0, entry, start, valBytes.length);
            size += entry.length;
            res.add(entry);
        }
        return mergeBytes(res, size);
    }

    private Map<String, Object> decode(byte[] data) {

    }

    private byte[] getChecksum(byte[] header, byte[] body) {
        long sum = 0;
        for (byte b : header) {
            sum += (int)(b & 0xFF);
        }
        for (byte b : body) {
            sum += (int)(b & 0xFF);
        }
        return Handlers.encodeInt((int)sum);
    }

    private byte[] mergeBytes(List<byte[]> data, int size) {
        byte[] res = new byte[size];
        int start = 0;
        for (byte[] item: data) {
            System.arraycopy(item, 0, res, start, item.length);
            start += item.length;
        }
        return res;
    }
}


class Handlers {
    public static byte[] encodeInt(int num) {
        byte[] res = new byte[4];
        for (int i = 0; i < 4; i++) {
            res[i] = (byte)num;
            num = num >>> 8;
        }
        return res;
    }

    public static byte[] encodeLong() {

    }

    public static byte[] encodeDouble() {

    }

    public static byte[] encodeBoolean() {
        
    }

    public static byte[] encodeString() {
        
    }

    public static int decodeToInt(byte[] data, int start) {
        int res = 0;
        for (int i = 3; i >= 0; i--) {
            res = (res << 8) | data[start + i];
        }
        return res;
    }
}