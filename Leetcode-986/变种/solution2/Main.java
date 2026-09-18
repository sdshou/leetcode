import java.util.*;


public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] firstList = {{0, 2}, {6, 10}};
        int[][] secondList = {{1, 5}, {8, 12}};
        int[][] result = solution.intervalIntersection(firstList, secondList);
        System.out.println(Arrays.deepToString(result));
    }
}

//LC986变种: 要求merge所有的intervals，Meta电面面试题(2023.10.24)
// Solution 2: using PriorityQueue
class Solution {
    private int i = 0, j = 0;
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        int len1 = firstList.length;
        int len2 = secondList.length;
        List<int[]> results = new ArrayList<>();
        Queue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        for (int[] a : firstList) pq.offer(a);
        for (int[] b : secondList) pq.offer(b);
        int[] ref = pq.poll();
        while (!pq.isEmpty()) {
            int[] next = pq.poll();
            if (next[0] <= ref[1]) {
                ref[1] = Math.max(ref[1], next[1]);
            } else {
                results.add(ref);
                ref = next;
            }
        }
        results.add(ref);
        return results.toArray(new int[0][]);
    }
}