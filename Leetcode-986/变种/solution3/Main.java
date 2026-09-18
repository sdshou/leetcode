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
// Solution 2: using PriorityQueue， but don't copy all in, use O(1) extra space
class Solution {
    private int i = 0, j = 0;
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        int len1 = firstList.length;
        int len2 = secondList.length;
        List<int[]> results = new ArrayList<>();
        Queue<Interval> pq = new PriorityQueue<>((a, b) -> a.current[0] - b.current[0]);
        pq.offer(new Interval(firstList));
        pq.offer(new Interval(secondList));
        Interval item = pq.poll();
        int[] ref =item.current;
        if (item.next()) pq.offer(item);
        while (!pq.isEmpty()) {
            item = pq.poll();
            int[] next = item.current;
            if (next[0] <= ref[1]) {
                ref[1] = Math.max(ref[1], next[1]);
            } else {
                results.add(ref);
                ref = next;
            }
            if (item.next()) pq.offer(item);
        }
        results.add(ref);
        return results.toArray(new int[0][]);
    }
}

class Interval {
    int[] current;
    int index;
    int[][] list;
    public Interval(int[][] list) {
        this.current = list[0];
        this.index = 0;
        this.list = list;
    }
    
    public boolean next() {
        this.index++;
        if (this.index < this.list.length) {
            this.current = this.list[this.index];
            return true;
        } 
        return false;
    }
}