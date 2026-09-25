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
        boolean[] visited = new boolean[n];
        int count = 0;
        
        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;
            count++;
            visited[i] = true;
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(i);
            // expend
            while (!queue.isEmpty()) {
                var cur = queue.poll();
                for (int j = 0; j < n; j++) {
                    if (!visited[j] && isConnected[cur][j] == 1) {
                        visited[j] = true;
                        queue.offer(j);
                    }
                }
            }
        }
        return count;
    }
}