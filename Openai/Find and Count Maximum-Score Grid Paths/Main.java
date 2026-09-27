import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] board = new int[][]{{5, 1}};
        var res = solution.bestGridPaths(board, 0, 0);
        System.out.println(res);

        res = solution.bestGridPaths(new int[][]{{1, 1}, {2, 2}}, 0, 0);
        System.out.println(res);

        res = solution.bestGridPaths(new int[][]{{-7}}, 0, 3);
        System.out.println(res);
    }
}

class Solution {
    private int[][] deltas = new int[][]{{1, -1}, {1, 0}, {1, 1}};

    public Paths bestGridPaths(int[][] board, int startCol, int maxJumps) {
        Paths res = new Paths(board[0][0] - 1, new ArrayList<>(), 0);
        dfs(board, 0, startCol, maxJumps, new Paths(), res);
        res.count = res.count % 1000000007;
        return res;
    }

    public void dfs(int[][] board, int row, int col, int maxJumps, Paths temp, Paths res) {
        int height = board.length, width = board[0].length;
        // process current
        temp.maxScore += board[row][col];
        temp.paths.add(new int[]{row, col});
        // success
        if (row == height - 1) {
            if (temp.maxScore > res.maxScore) {
                res.maxScore = temp.maxScore;
                res.paths = new ArrayList<>(temp.paths);
                res.count = 1;
            } else if (temp.maxScore == res.maxScore) {
                res.count++;
            }
        }
        //expend
        for (int[] delta : deltas) {
            int x = row + delta[0];
            int y = col + delta[1];
            if (x >= 0 && x < height && y >= 0 && y < width) {
                dfs(board, x, y, maxJumps, temp, res);
            }
        }
        if (maxJumps > 0) {
            int x = row + 2;
            int y = col;
            if (x >= 0 && x < height && y >= 0 && y < width) {
                dfs(board, x, y, maxJumps - 1, temp, res);
            }
        }
        // revert
        temp.maxScore -= board[row][col];
        temp.paths.remove(temp.paths.size() - 1);
    }
}

class Paths {
    int maxScore;
    List<int[]> paths;
    int count;

    public Paths() {
        this.maxScore = 0;
        this.paths = new ArrayList<>();
        this.count = 0;
    }

    public Paths(int maxScore, List<int[]> paths, int count) {
        this.maxScore = maxScore;
        this.paths = paths;
        this.count = count;
    }

    public String toString() {
        List<String> sb = new ArrayList<>();
        for (int[] p : paths) {
            sb.add(String.format("[%d, %d]", p[0], p[1]));
        }
        String pathStr = String.join(",", sb);
        return String.format("{ maxScore: %d, path: [ %s ], count: %d}", this.maxScore, pathStr, this.count);
    }
}