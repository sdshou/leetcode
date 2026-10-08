import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        HexFormat hex = HexFormat.of().withPrefix("\\x");
        //test little-endian signed for long type data
        System.out.println("-35L to bytes(little-endian signed):" + hex.formatHex(Handlers.encodeLong(-35L))); // \xdd\xff\xff\xff\xff\xff\xff\xff
        System.out.println("--------------------------");

        Solution solution = new Solution();
        var res = solution.solution("encode", new HashMap<String, Object>());
        byte[] bytesData = (byte[])res;
        StringBuilder sb = new StringBuilder();
        for (byte b : bytesData) {
            if (b <= 126 && b >= 33) {
                sb.append((char)b);
            } else {
                sb.append("\\x").append(hex.toHexDigits(b));
            }
        }
        System.out.println(sb.toString()); // KVSB\x01\x04\x00\x00\x00\x00\x00\x00\x00;\x01\x00\x00
        System.out.println(new String(bytesData, 0, 4, StandardCharsets.UTF_8) + hex.formatHex(bytesData, 4, bytesData.length)); // 
        // KVSB\x01\x04\x00\x00\x00\x00\x00\x00\x00\x3b\x01\x00\x00

        // "KVSB\\x01\\x04\\x00\\x00\\x00\\x00\\x00\\x00\\x00;\\x01\\x00\\x00"
        bytesData = new byte[]{'k', 'v', 'S', 'B', 0x01, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, ';', 0x01, 0x00, 0x00};
        res = solution.solution("decode", bytesData);
        System.out.println(res); // {}

        System.out.println("--------------------------");

        Map<String, Object> educationMap = new HashMap<>();
        educationMap.put("highestDegree", "Master");
        educationMap.put("hasBachelor", true);
        educationMap.put("yearsOfBachelor", 4L);
        educationMap.put("tuitionFee", 12042.76D);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Shaodi Shou");
        map.put("education", educationMap);
        map.put("age", 35L);
        map.put("male", true);
        map.put("deposit", 87655637282.76D); // doubleToLongBits: 4770553005602947727L, byte[]: \x8f\xc2"\xe1\xaeh4B
        var res1 = solution.solution("encode", map);
        byte[] bytesData1 = (byte[])res1;
        StringBuilder sb1 = new StringBuilder();
        for (byte b : bytesData1) {
            if (b <= 126 && b >= 33) {
                sb1.append((char)b);
            } else {
                sb1.append("\\x").append(hex.toHexDigits(b));
            }
        }
        System.out.println(sb1.toString());
        res1 = solution.solution("decode", bytesData1);
        System.out.println("map: " + res1);
        map = (Map<String, Object>) res1;
        System.out.printf("deposit: %.2fD%n", map.get("deposit")); // deposit: 87655637282.76D
        educationMap = (Map<String, Object>)(map.get("education"));
        System.out.printf("education::tuitionFee: %.2fD%n", educationMap.get("tuitionFee")); // education::tuitionFee: 12042.76D
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
            if (data instanceof Map<?, ?> m) {
                return encode((Map<String, Object>)m);
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
            switch (val) {
                case Long l -> {
                    tag = 1;
                    valBytes = Handlers.encodeLong(l);
                }
                case Double d -> {
                    tag = 2;
                    valBytes = Handlers.encodeDouble(d);
                }
                case Boolean b -> {
                    tag = 3;
                    valBytes = new byte[]{Handlers.encodeBoolean(b)};
                }
                case String s -> {
                    tag = 4;
                    valBytes = Handlers.encodeString(s);
                }
                case Map<?, ?> m -> {
                    tag = 5;
                    valBytes = encodeMapPayload((Map<String, Object>)m);
                }
                default -> valBytes = new byte[0];
            }
            byte[] entryBytes = new byte[4 + keyBytes.length + 5 + valBytes.length];
            int start = 0;
            System.arraycopy(Handlers.encodeInt(keyBytes.length), 0, entryBytes, start, 4);
            start += 4;
            System.arraycopy(keyBytes, 0, entryBytes, start, keyBytes.length);
            start += keyBytes.length;
            entryBytes[start] = (byte)tag;
            start++;
            System.arraycopy(Handlers.encodeInt(valBytes.length), 0, entryBytes, start, 4);
            start += 4;
            System.arraycopy(valBytes, 0, entryBytes, start, valBytes.length);
            size += entryBytes.length;
            res.add(entryBytes);
        }
        return mergeBytes(res, size);
    }

    private Map<String, Object> decode(byte[] data) {
        int bodyLength = Handlers.decodeToInt(data, 5);
        return decodeToMapPayload(data, 9);
    }

    private Map<String, Object> decodeToMapPayload(byte[] data, int start) {
        Map<String, Object> res = new HashMap<>();
        int entryCount = Handlers.decodeToInt(data, start);
        start += 4;
        for (int i = 0; i < entryCount; i++) {
            int keyLength = Handlers.decodeToInt(data, start);
            start += 4;
            String key = Handlers.decodeToString(data, start, keyLength);
            start += keyLength;
            int tag = (int)(data[start]);
            start++;
            int valLength = Handlers.decodeToInt(data, start);
            start += 4;
            Object val;
            switch (tag) {
                case 1 -> {
                    val = Handlers.decodeToLong(data, start);
                }
                case 2 -> {
                    val = Handlers.decodeToDouble(data, start);
                }
                case 3 -> {
                    val = Handlers.decodeToBoolean(data, start);
                }
                case 4 -> {
                    val = Handlers.decodeToString(data, start, valLength);
                }
                case 5 -> {
                    val = decodeToMapPayload(data, start);
                }
                default -> val = null;
            }
            start += valLength;
            res.put(key, val);
        }
        return res;
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

    public static byte[] encodeLong(long num) {
        byte[] res = new byte[8];
        for (int i = 0; i < 8; i++) {
            res[i] = (byte)num;
            num = num >>> 8;
        }
        return res;
    }

    public static byte[] encodeDouble(double num) {
        return encodeLong(Double.doubleToLongBits(num));
    }

    public static byte encodeBoolean(boolean data) {
        return data ? (byte)1 : (byte)0;
    }

    public static byte[] encodeString(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }

    public static int decodeToInt(byte[] data, int start) {
        int res = 0;
        for (int i = 3; i >= 0; i--) {
            res = (res << 8) | (data[start + i] & 0xFF);
        }
        return res;
    }
    
    public static long decodeToLong(byte[] data, int start) {
        long res = 0;
        for (int i = 7; i >= 0; i--) {
            res = (res << 8) | (data[start + i] & 0xFF);
            
        }
        return res;
    }

    public static double decodeToDouble(byte[] data, int start) {
        return Double.longBitsToDouble(decodeToLong(data, start));
    }

    public static boolean decodeToBoolean(byte[] data, int start) {
        return (data[start] & 0x01) != 0;
    }

    public static String decodeToString(byte[] data, int start, int length) {
        return new String(data, start, length, StandardCharsets.UTF_8);
    }
}