import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] grid = new int[][]{{2,1,1}, {1,1,0}, {0,1,1}};
        int res = solution.minutesToInfect(grid);
        System.out.println(res);

        res = solution.minutesToInfect(new int[][]{{2,1,1}, {0,1,1}, {1,0,1}});
        System.out.println(res);

        res = solution.minutesToInfect(new int[][]{{0, 2}});
        System.out.println(res);
    }
}

class Solution {
    private int[][] deltas = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    public int minutesToInfect(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int row = grid.length, col = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        int time = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) {
                    queue.offer(new int[]{i, j});
                }
            }
        }
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cur = queue.poll();
                for (int[] delta : deltas) {
                    int x = cur[0] + delta[0];
                    int y = cur[1] + delta[1];
                    if (x >= 0 && x < row && y >= 0 && y < col && grid[x][y] == 1) {
                        grid[x][y] = 2;
                        queue.offer(new int[]{x, y});
                    }
                }
            }
            if (!queue.isEmpty()) time++;
        }
        
        // check healthy
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) return -1;
            }
        }
        
        return time;
    }
}