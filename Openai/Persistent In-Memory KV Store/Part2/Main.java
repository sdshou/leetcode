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

class Solution {
    private Map<DataItem, DataItem> store = new HashMap<>();
    private Map<String, byte[][]> disk = new HashMap<>();
    public Object solution(Object max_segment_size, Object operations) {
        var outputs = new LinkedList<Object>();
        if (!operations instanceof Object[]) return outputs;

        for (Object op : (Object[])operations) {
            if (!(op instanceof Object[])) continue;
            var operation = (Object[])op;
            switch ((String)(operation[0])) {
                case "get": {
                    var val = store.get(new DataItem(operation[1]));
                    if (val == null) {
                        outputs.add("None");
                    } else {
                        outputs.add(val.data);
                    }
                    break;
                }
                case "put": store.put(new DataItem(operation[1]), new DataItem(operation[2])); break;
                case "delete": store.remove(new DataItem(operation[1])); break;
                case "serialize": {
                    disk.put(((String)operation[1]), serialize());
                }
                case "deserialize": {
                    byte[] storeBytes = disk.get(((String)(operation[1])));
                    this.store = deserialize(storeBytes);
                }
            }
        }
        return outputs;
    }

    private byte[][] serializeWithSegment(int maxSize) {
        int segSize = maxSize - 4;
        byte[] allBytes = serialize();
        int count = (allBytes.length + segSize - 1) / segSize;
        byte[][] segments = new byte[count][];
        int start = 0, end = start + segSize;
        for (int i = 0; i < count - 1; i++) {
            byte[] chunk = new byte[maxSize];
            byte[] head = encodeLength(i);
            System.arraycopy(head, 0, chunk, 0, 4);
            System.arraycopy(allBytes, start, chunk, 4, segSize);
            segments[i] = chunk;
            start += segSize;
        }
        // handle last chunk
        int lastChunkSize = allBytes.length - start;
        byte[] chunk = new byte[lastChunkSize];
        byte[] head = encodeLength(count - 1);
        System.arraycopy(head, 0, chunk, 0, 4);
        System.arraycopy(allBytes, start, chunk, 4, lastChunkSize);
        segments[count - 1] = chunk;
        return segments;
    }

    private byte[] serialize() {
        List<byte[]> chunks = new ArrayList<>();
        int size = 0;
        for (Map.Entry<DataItem, DataItem> entry : store.entrySet()) {
            var key = entry.getKey().encodeToBytes();
            chunks.add(encodeLength(key.length));
            chunks.add(key);
            var val = entry.getValue().encodeToBytes();
            chunks.add(encodeLength(val.length));
            chunks.add(val);
            size += 8 + key.length + val.length;
        }
        byte[] res = new byte[size];
        int start = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, res, start, chunk.length);
            start += chunk.length;
        }
        return res;
    }

    private Map<DataItem, DataItem> deserialize(byte[] storeBytes) {
        int start = 0;
        Map<DataItem, DataItem> localstore = new HashMap<>();
        while (start < storeBytes.length) {
            int keyLength = 0, valLength = 0;
            for (int i = 0; i < 4; i++) {
                keyLength = (keyLength << 8) | (storeBytes[start + i] & 0xFF);
            }
            start += 4;
            var key = DataItem.decodeFromBytes(storeBytes, start);
            start += keyLength;
            for (int i = 0; i < 4; i++) {
                valLength = (valLength << 8) | (storeBytes[start + i] & 0xFF);
            }
            start += 4;
            var val = DataItem.decodeFromBytes(storeBytes, start);
            start += valLength;
            localstore.put(key, val);
        }
        return localstore;
    }

    private byte[] encodeLength(int length) {
        byte[] res = new byte[4];
        res[0] = (byte)(length >>> 24);
        res[1] = (byte)(length >>> 16);
        res[2] = (byte)(length >>> 8);
        res[3] = (byte)(length);
        return res;
    }
}

class DataItem {
    int type;
    Object data;

    public DataItem(Object data) {
        if (data instanceof String) {
            this.type = 0;
        } else {
            this.type = 1;
        }
        this.data = data;
    }

    public byte[] encodeToBytes() {
        byte[] temp;
        if (type == 0) {
            temp = ((String)data).getBytes(StandardCharsets.UTF_8);
        } else {
            temp = (byte[])this.data;
        }

        int length = temp.length;
        byte[] res = new byte[5 + length];
        res[0] = (byte)this.type;
        res[1] = (byte)(length >>> 24);
        res[2] = (byte)(length >>> 16);
        res[3] = (byte)(length >>> 8);
        res[4] = (byte)(length);
        
        for (int i = 0; i < length; i++) {
            res[i + 5] = temp[i];
        }
        return res;
    }

    public static DataItem decodeFromBytes(byte[] data, int start) {
        int type = data[start];
        int length = 0;
        for (int i = start + 1; i < start + 5; i++) {
            length = (length << 8) | (data[i] & 0xFF);
        }
        int begin = 5 + start;
        if (type == 0) {
            String res = new String(data, begin, length, StandardCharsets.UTF_8);
            return new DataItem(res);
        }
        return new DataItem(Arrays.copyOfRange(data, begin, begin + length));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DataItem other = (DataItem)obj;
        if (this.type != other.type) return false;
        if (this.type == 0) {
            return this.data.equals(other.data);
        }
        return Arrays.equals((byte[])this.data, (byte[])other.data);
    }

    @Override
    public int hashCode() {
        if (this.type == 0) return this.data.hashCode();
        return Arrays.hashCode((byte[])data);
    }
}