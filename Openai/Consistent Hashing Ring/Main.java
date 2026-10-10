import java.nio.charset.StandardCharsets;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        List<String> operations = List.of("ShardRing", "get_shard");
        List<List<Object>> dataArgs = List.of(List.of(3), List.of("anykey"));
        List<String> res = solution.solution(operations, dataArgs);
        System.out.println(res);

        Solution solution1 = new Solution();
        List<String> operations1 = List.of("ShardRing", "add_shard", "get_shard", "get_shard", "get_shard");
        List<List<Object>> dataArgs1 = List.of(List.of(5), List.of("A"), List.of("k1"), List.of("k2"), List.of("user:42"));
        List<String> res1 = solution1.solution(operations1, dataArgs1);
        System.out.println(res1);
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
        TreeMap<Long, String> ring;

        ShardRing(int vnodes) {
            this.vnodes = vnodes;
            // keep virtual-node positions sorted for O(log V) lookup
            this.ring = new TreeMap<>();
        }

        // place vnodes virtual nodes at fnv1a32(shardId + "#" + i)
        void addShard(String shardId) {
            for (int i = 0; i < this.vnodes; i++) {
                long idx = Solution.fnv1a32(String.format("%s#%d", shardId, i));
                this.ring.put(idx, shardId);
            }
        }

        // drop every virtual node belonging to shardId
        void removeShard(String shardId) {
            for (int i = 0; i < this.vnodes; i++) {
                long idx = Solution.fnv1a32(String.format("%s#%d", shardId, i));
                this.ring.remove(idx);
            }
        }

        // first virtual node clockwise from fnv1a32(key); "" if empty
        String getShard(String key) {
            if (this.ring.isEmpty()) return "";
            long idx = Solution.fnv1a32(key);
            Long shardKey = this.ring.ceilingKey(idx);
            if (shardKey == null) {
                shardKey = this.ring.ceilingKey(0L);
            }
            return this.ring.get(shardKey);
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
