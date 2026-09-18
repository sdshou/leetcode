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
class Solution {
    private int i = 0, j = 0;
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        int len1 = firstList.length;
        int len2 = secondList.length;
        List<int[]> results = new ArrayList<>();
        int[] ref = null;
        while (true) {
            int[] next = getNext(firstList, secondList);
            if (next == null) break;
            if (ref == null) {
                ref = next;
            } else {
                if (ref[1] >= next[0]) {
                    ref[1] = Math.max(ref[1], next[1]);
                } else {
                    results.add(ref);
                    ref = next;
                }
            }
        }
        if (ref != null) results.add(ref);
        return results.toArray(new int[0][]);
    }

    private int[] getNext(int[][] firstList, int[][] secondList) {
        int[] a = null;
        int[] b = null;
        if (i < firstList.length) {
            a = firstList[i];
        }
        if (j < secondList.length) {
            b = secondList[j];
        }
        
        if (a == null) {
            j++;
            return b;
        }
        if (b == null) {
            i++;
            return a;
        }
        if (a[0] <= b[0]) {
            i++;
            return a;
        }
        if (a[0] > b[0]) {
            j++;
            return b;
        }
        return null;
    }
}