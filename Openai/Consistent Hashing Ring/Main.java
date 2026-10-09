import java.nio.charset.StandardCharsets;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        
    }
}

class Solution {
    static long fnv1a32(String s) {
        long h = 2166136261L;
        for (byte b : s.getBytes(StandardCharsets.UTF_8)) {
            h ^= (b & 0xFF);
            h = (h * 16777619L) & 0xFFFFFFFFL;
        }
        return h;
    }

    class ShardRing {
        int vnodes;
        ShardRing(int vnodes) {
            this.vnodes = vnodes;
            // TODO: keep virtual-node positions sorted for O(log V) lookup
        }
        void addShard(String shardId) {
            // TODO: place vnodes virtual nodes at fnv1a32(shardId + "#" + i)
        }
        void removeShard(String shardId) {
            // TODO: drop every virtual node belonging to shardId
        }
        String getShard(String key) {
            // TODO: first virtual node clockwise from fnv1a32(key); "" if empty
            return "";
        }
    }

    List<String> solution(List<String> operations, List<List<Object>> args) {
        List<String> results = new ArrayList<>();
        ShardRing ring = null;
        for (int idx = 0; idx < operations.size(); idx++) {
            String op = operations.get(idx);
            List<Object> a = args.get(idx);
            if (op.equals("ShardRing")) {
                ring = new ShardRing(((Number) a.get(0)).intValue());
                results.add(null);
            } else if (op.equals("add_shard")) {
                ring.addShard((String) a.get(0));
                results.add(null);
            } else if (op.equals("remove_shard")) {
                ring.removeShard((String) a.get(0));
                results.add(null);
            } else if (op.equals("get_shard")) {
                results.add(ring.getShard((String) a.get(0)));
            }
        }
        return results;
    }
}
