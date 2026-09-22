import java.util.*;


public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        List<Map<String, Object>> rows = new ArrayList<>(List.of(
            new HashMap<>(Map.of("id", "1", "name", "Ada", "birthday", "1815-12-10")),
            new HashMap<>(Map.of("id", "2", "name", "Charles", "birthday", "1791-12-26"))
        ));
        List<String> selectedColumns = new ArrayList<>(List.of("id", "name"));
        var res = solution.solution(rows, selectedColumns);
        System.out.println(res); // [{name=Ada, id=1}, {name=Charles, id=2}]
        res = solution.solution(rows, new ArrayList<>(List.of("id", "address")));
        System.out.println(res); // [{address=null, id=1}, {address=null, id=2}]
    }
}

class Solution {
    List<Map<String, Object>> solution(
        List<Map<String, Object>> rows, List<String> selectedColumns) {
        List<Map<String, Object>> res = new ArrayList<>();
        if (rows == null || rows.size() == 0 || selectedColumns == null || selectedColumns.size() == 0) return res;
        for (Map<String, Object> row : rows) {
            Map<String, Object> ans = new HashMap<>();
            for (String col : selectedColumns) {
                ans.put(col, row.get(col));
            }
            res.add(ans);
        }
        return res;
    }
}