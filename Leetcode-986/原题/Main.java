import java.util.*;


public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] firstList = {{0, 2}, {5, 10}, {13, 23}, {24, 25}};
        int[][] secondList = {{1, 5}, {8, 12}, {15, 24}, {25, 26}};
        int[][] result = solution.intervalIntersection(firstList, secondList);
        System.out.println(Arrays.deepToString(result));
    }
}

class Solution {
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        int i = 0, j = 0;
        int len1 = firstList.length;
        int len2 = secondList.length;
        List<int[]> results = new ArrayList<>();
        while (i < len1 && j < len2) {
            //compare
            int left = Math.max(firstList[i][0], secondList[j][0]);
            int right = Math.min(firstList[i][1], secondList[j][1]);
            if (left <= right) {
                results.add(new int[]{left, right});
            }
            if (firstList[i][1] <= secondList[j][1]) i++;
            else j++;
        }
        return results.toArray(new int[0][]);
    }
}