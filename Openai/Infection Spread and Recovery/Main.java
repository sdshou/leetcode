import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        List<String> grid = List.of("SIS", "SMS", "SSS");
        List<String> res = solution.simulateInfection(grid, 2, 1);
        System.out.println(res); // [III, SMS, SSS]

        res = solution.simulateInfection(List.of("SIS", "SMS", "SSS"), 2, 2);
        System.out.println(res); // [IRI, IMI, SSS]
    }
}

class Solution {
    private int[][] deltas = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    public List<String> simulateInfection(List<String> grid, int recoveryDays, int days) {
        char[][] matrix = toCharGrid(grid);
        int row = matrix.length, col = matrix[0].length;
        int[][] recovery = new int[row][col];
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 'I') {
                    queue.offer(new int[]{i , j});
                    recovery[i][j] = recoveryDays;
                }
            }
        }
        while (!queue.isEmpty() && days > 0) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cur = queue.poll();
                for (int[] delta : deltas) {
                    int x = cur[0] + delta[0];
                    int y = cur[1] + delta[1];
                    if (x >= 0 && x < row && y >= 0 && y < row && matrix[x][y] == 'S') {
                        matrix[x][y] = 'I';
                        recovery[x][y] = recoveryDays;
                        queue.offer(new int[]{x, y});
                    }
                }
                recovery[cur[0]][cur[1]]--;
                if (recovery[cur[0]][cur[1]] == 0) {
                    matrix[cur[0]][cur[1]] = 'R';
                } else {
                    queue.offer(cur);
                }
            }
            days--;
        }
        return toStringGrid(matrix);
    }

    private char[][] toCharGrid(List<String> grid) {
        int row = grid.size(), col = grid.get(0).length();
        char[][] res = new char[row][];
        int i = 0;
        for (String r : grid) {
            res[i++] = r.toCharArray();
        }
        return res;
    }

    private List<String> toStringGrid(char[][] matrix) {
        List<String> res = new ArrayList<>();
        for (char[] row : matrix) {
            res.add(new String(row));
        }
        return res;
    }
}