import java.util.*;


public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        List<Map<String, Object>> example1 = new ArrayList<>(List.of(
            new HashMap<>(Map.of("id", "1", "name", "Ada")),
            new HashMap<>(Map.of("id", "2", "name", "Charles")),
            new HashMap<>(Map.of("id", "3", "name", "Ada"))
        ));
        var res1 = solution.solution(example1, "name", List.of("Ada", "Charles", "Eve"));
        System.out.println(res1); // [[0, 2], [1], []]

        List<Map<String, Object>> example2 = new ArrayList<>(List.of(
            new HashMap<>(Map.of("id", "1")),
            new HashMap<>(Map.of("id", "2", "city", "Paris")),
            new HashMap<>(Map.of("id", "3", "city", "Paris"))
        ));
        
        var res2 = solution.solution(example2, "city", List.of("Paris", "London"));
        System.out.println(res2); // [[1, 2], []]

        List<Map<String, Object>> example3 = new ArrayList<>();
        var res3 = solution.solution(example3, "name", List.of("Ada"));
        System.out.println(res3); // []

        List<Map<String, Object>> example4 = new ArrayList<>(List.of(
            new HashMap<>(Map.of("id", "1", "age", 36)),
            new HashMap<>(Map.of("id", "2", "age", 28)),
            new HashMap<>(Map.of("id", "3", "age", 36)),
            new HashMap<>(Map.of("id", "4", "age", 40))
        ));
        
        var res4 = solution.solution(example4, "age", List.of(40, 36));
        System.out.println(res4); // [[3], [0, 2]]
    }
}

class Solution {
    List<List<Integer>> solution(
        List<Map<String, Object>> rows, String indexColumn, List<Object> lookupValues) {
        List<List<Integer>> res = new ArrayList<>();
        if (rows == null || rows.size() == 0 || lookupValues == null || lookupValues.size() == 0) return res;
        Map<Object, List<Integer>> index = buildIndex(rows, indexColumn);
        for (Object obj : lookupValues) {
            if (index.containsKey(obj)) {
                res.add(index.get(obj));
            } else {
                res.add(new ArrayList<>());
            }
        }
        return res;
    }

    private Map<Object, List<Integer>> buildIndex(List<Map<String, Object>> rows, String indexColumn) {
        Map<Object, List<Integer>> index = new HashMap<>();
        int i = 0;
        for (Map<String, Object> row : rows) {
            Object val = row.get(indexColumn);
            if (val != null) {
                List<Integer> pos = index.getOrDefault(val, new ArrayList<>());
                pos.add(i);
                index.put(val, pos);
            }
            i++;
        }
        return index;
    }
}
