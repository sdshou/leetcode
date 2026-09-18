import java.util.*;


public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] intervals = new int[][]{{1,3}, {2,6}, {8,10}, {15,18}};
        int[][] res = solution.merge(intervals);
        System.out.println(Arrays.deepToString(res));
    }
}


class Solution {
    public int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return null;
        List<int[]> res = new ArrayList<>();
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        int[] ref = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= ref[1]) {
                ref[1] = Math.max(intervals[i][1], ref[1]);
            } else {
                res.add(ref);
                ref = intervals[i];
            }
        }
        res.add(ref);
        return res.toArray(new int[0][]);
    }
}