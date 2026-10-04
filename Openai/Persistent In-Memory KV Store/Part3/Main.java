import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        byte[] key1 = "a".getBytes(StandardCharsets.UTF_8);
        byte[] key2 = "b".getBytes(StandardCharsets.UTF_8);
        Object[][] operations = new Object[][]{
            {"put", key1, "12345".getBytes(StandardCharsets.UTF_8)}, 
            {"put", key2, "67890".getBytes(StandardCharsets.UTF_8)}, 
            {"serialize", "p"}, {"segment_count", "p"}, {"delete", key1}, 
            {"deserialize", "p"}, {"get", key1}, {"get", key2}
        };
        var res = solution.solution(15, operations);
        if (res instanceof List<?> r) {
            byte[] v1 = (byte[]) r.get(1);
            byte[] v2 = (byte[]) r.get(2);
            var str = String.format("[%d, %s, %s]", r.get(0), new String(v1, StandardCharsets.UTF_8), new String(v2, StandardCharsets.UTF_8));
            System.out.println(str);
        }

        key1 = "aa".getBytes(StandardCharsets.UTF_8);
        key2 = "bb".getBytes(StandardCharsets.UTF_8);
        byte[] val1 = "xx".getBytes(StandardCharsets.UTF_8);
        byte[] val2 = "yy".getBytes(StandardCharsets.UTF_8);
        var operations2 = new Object[][]{
            {"put", key1, val1}, {"put", key2, val2}, {"serialize", "snap"}, 
            {"segment_count", "snap"}, {"reorder", "snap", new int[]{2, 0, 1}}, 
            {"delete", key2}, {"deserialize", "snap"}, 
            {"get", key1}, {"get", key2}
        };
        res = solution.solution(20, operations2);
        if (res instanceof List<?> r) {
            byte[] v1 = (byte[]) r.get(1);
            byte[] v2 = (byte[]) r.get(2);
            var str = String.format("[%d, %s, %s]", r.get(0), new String(v1, StandardCharsets.UTF_8), new String(v2, StandardCharsets.UTF_8));
            System.out.println(str);
        } // [None, v:1]
    }
}

class Solution {
    private Map<DataItem, DataItem> store = new HashMap<>();
    private DiskDataManager diskManager = new DiskDataManager();
    public Object solution(Object compact_threshold, Object operations) {
        int maxSize = (int)compact_threshold;
        var outputs = new LinkedList<Object>();
        if (!(operations instanceof Object[])) return outputs;

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
                case "put": {
                    Operation putOp = new Operation(0, new DataItem(operation[1]), new DataItem(operation[2]));
                    store.put(putOp.key, putOp.val);
                    diskManager.appendLog(putOp.serialize());
                    tryCompact();
                    break;
                }
                case "delete": {
                    Operation deleteOp = new Operation(0, new DataItem(operation[1]), null);
                    store.remove(deleteOp.key);
                    diskManager.appendLog(deleteOp.serialize());
                    tryCompact();
                    break;
                }
                case "restart": {
                    byte[][] segments = disk.get(((String)(operation[1])));
                    this.store = deserialize(diskManager.snapshot);
                    replay(diskManager.logs);
                    break;
                }
                case "status": {
                    int[] status = new int[]{this.store.size(), diskManager.logs.size(), diskManager.compactionCount}
                    outputs.add(status);
                    break;
                }
            }
        }
        return outputs;
    }

    private void tryCompact() {
        if (!diskManager.shouldCompact()) return;
        diskManager.compact(this.serialize());
    }

    private byte[] serialize() {
        List<byte[]> chunks = new ArrayList<>();
        int size = 0;
        for (Map.Entry<DataItem, DataItem> entry : store.entrySet()) {
            var key = entry.getKey().encodeToBytes();
            chunks.add(encodeNum(key.length));
            chunks.add(key);
            var val = entry.getValue().encodeToBytes();
            chunks.add(encodeNum(val.length));
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
            keyLength = decodeNum(storeBytes, start);
            start += 4;
            var key = DataItem.decodeFromBytes(storeBytes, start);
            start += keyLength;
            valLength = decodeNum(storeBytes, start);
            start += 4;
            var val = DataItem.decodeFromBytes(storeBytes, start);
            start += valLength;
            localstore.put(key, val);
        }
        return localstore;
    }

    private void replay(List<byte[]> logs) {
        for (byte[] log : logs) {
            Operation op = Operation.deserialize(log);
            if (op.op == 0) {
                this.store.put(op.key, op.val);
            } else if (op.op == 1) {
                this.store.remove(op.key);
            }
        }
    }

    private byte[] encodeNum(int length) {
        byte[] res = new byte[4];
        res[0] = (byte)(length >>> 24);
        res[1] = (byte)(length >>> 16);
        res[2] = (byte)(length >>> 8);
        res[3] = (byte)(length);
        return res;
    }

    private int decodeNum(byte[] data, int start) {
        int num = 0;
        for (int i = start; i < start + 4; i++) {
            num = (num << 8) | (data[i] & 0xFF);
        }
        return num;
    }
}

class DiskDataManager {
    int compactionCount;
    byte[] snapshot;
    List<byte[]> logs;

    public DiskDataManager() {
        this.compactionCount = 0;
        this.snapshot = null;
        this.logs = new ArrayList<>();
    }

    public void appendLog(byte[] log) {
        this.logs.add(log);
    }

    public void shouldCompact(int compactThreshold) {
        return logs.size() >= compactThreshold;
    }

    public void compact(byte[] snapshot) {
        this.compactionCount++;
        this.snapshot = snapshot;
        this.logs.clear();
    }
}

class Operation {
    int op; // 0: put, 1: delete
    DataItem key;
    DataItem val;

    public Operation(int op, DataItem key, DataItem val) {
        this.op = op;
        this.key = key;
        this.val = val;
    }

    public byte[] serialize() {
        // byte[op, key, val]
        var keyBytes = this.key.encodeToBytes();
        var valBytes = this.val.encodeToBytes();
        byte[] res = new byte[1 + keyBytes.length + valBytes.length];
        res[0] = op & 0xFF;
        system.arraycopy(keyBytes, 0, res, 1, keyBytes.length);
        system.arraycopy(valBytes, 0, res, 1 + keyBytes.length, valBytes.length);
        return res;
    }

    public static Operation deserialize(byte[] log) {
        int op = (int)(log[0]);
        int length = 0;
        for (int i = 1; i < 5; i++) {
            length = (length << 8) | (log[i] & 0xFF);
        }
        var key = DataItem.decodeFromBytes(log, 1);
        var val = DataItem.decodeFromBytes(log, 5 + length);
        return new Operation(op, key, val);
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