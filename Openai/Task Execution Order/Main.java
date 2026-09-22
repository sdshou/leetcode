import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] dependencies = new int[][]{{2, 0}, {2, 1}, {3, 1}};
        int[] res = solution.orderTasks(4, dependencies);
        for (int r : res) {
            System.out.printf("%d,", r);
        }
        System.out.println("");

        res = solution.orderTasks(4, new int[][]{{0, 2}, {1, 2}});
        for (int r : res) {
            System.out.printf("%d,", r);
        }
        System.out.println("");

        res = solution.orderTasks(1, new int[][]{});
        for (int r : res) {
            System.out.printf("%d,", r);
        }
        System.out.println("");

        res = solution.orderTasks(14, new int[][]{{0, 2}, {1, 12}});
        for (int r : res) {
            System.out.printf("%d,", r);
        }
        System.out.println("");

        // 有环
        res = solution.orderTasks(3, new int[][]{{0, 1}, {1, 2}, {2, 0}});
        for (int r : res) {
            System.out.printf("%d,", r);
        }
        System.out.println("");
    }
}

class Solution {
    int[] orderTasks(int n, int[][] dependencies) {
        if (n <= 0) return new int[0];
        Map<Integer, List<Integer>> maps = new HashMap<>();
        int[] indegrees = new int[n];
        for (int[] pair : dependencies) {
            indegrees[pair[0]]++;
            List<Integer> relations = maps.getOrDefault(pair[1], new ArrayList<>());
            relations.add(pair[0]);
            maps.put(pair[1], relations);
        }
        Queue<Integer> pq = new PriorityQueue<>((a, b) -> {
            char[] charA = a.toString().toCharArray();
            char[] charB = b.toString().toCharArray();
            int i = 0, j = 0;
            while (i < charA.length && j < charB.length) {
                if (charA[i] < charB[j]) return -1;
                else if (charA[i] > charB[j]) return 1;
                i++;j++;
            }
            return i == charA.length ? -1 : 1;
        });
        for (int i = 0; i < n; i++) {
            if (indegrees[i] == 0) {
                pq.offer(i);
            }
        }
        int[] res = new int[n];
        int pos = 0;
        while (!pq.isEmpty()) {
            Integer cur = pq.poll();
            res[pos++] = cur;
            List<Integer> relations = maps.get(cur);
            if (relations == null) continue;
            for (Integer r : relations) {
                indegrees[r]--;
                if (indegrees[r] == 0) pq.offer(r);
            }
        }
        if (pos < n) return new int[0];
        return res;
    }
}