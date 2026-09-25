import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] isConnected = new int[][]{{1, 0}, {0, 1}};
        int res = solution.countFriendGroups(isConnected);
        System.out.println(res); // 2

        res = solution.countFriendGroups(new int[][]{{1}});
        System.out.println(res); // 1

        isConnected = new int[][]{{1, 0, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 0}, {1, 0, 0, 1}};
        res = solution.countFriendGroups(isConnected);
        System.out.println(res); // 2: {0, 3}, {1, 2}
    }
}

class Solution {
    public int countFriendGroups(int[][] isConnected) {
        if (isConnected == null || isConnected.length == 0) return 0;
        int n = isConnected.length;
        int count = n * 3;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (isConnected[i][j] == 1) count--;
            }
        }
        return count / 2;
    }
}