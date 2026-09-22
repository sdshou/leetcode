import java.util.*;


public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        List<Map<String, Object>> example1 = new ArrayList<>(List.of(
            new HashMap<>(Map.of("id", "1", "name", "Charles")),
            new HashMap<>(Map.of("id", "2", "name", "Ada")),
            new HashMap<>(Map.of("id", "3", "name", "Bob"))
        ));
        var res1 = solution.solution(example1, List.of("name"), List.of(true));
        System.out.println(res1); // [{name=Ada, id=2}, {name=Bob, id=3}, {name=Charles, id=1}]

        List<Map<String, Object>> example2 = new ArrayList<>(List.of(
            new HashMap<>(Map.of("id", "1", "age", 30)),
            new HashMap<>(Map.of("id", "2", "age", 20)),
            new HashMap<>(Map.of("id", "3", "age", 40))
        ));
        
        var res2 = solution.solution(example2, List.of("age"), List.of(false));
        System.out.println(res2); // [{age=40, id=3}, {age=30, id=1}, {age=20, id=2}]

        List<Map<String, Object>> example3 = new ArrayList<>(List.of(
            new HashMap<>(Map.of("id", "1", "age", 36, "name", "Ada")),
            new HashMap<>(Map.of("id", "2", "age", 28, "name", "Bob")),
            new HashMap<>(Map.of("id", "3", "age", 40, "name", "Ada"))
        ));
        
        var res3 = solution.solution(example3, List.of("name", "age"), List.of(true, false));
        System.out.println(res3); // [{name=Ada, age=40, id=3}, {name=Ada, age=36, id=1}, {name=Bob, age=28, id=2}]
    }
}

class Solution {
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> solution(
        List<Map<String, Object>> rows, List<String> orderColumns, List<Boolean> ascendingFlags) {
        if (rows == null || rows.size() == 0 || orderColumns == null || orderColumns.size() == 0) return rows;
        Collections.sort(rows, (a, b) -> {
            int len = orderColumns.size();
            for (int i = 0; i < len; i++) {
                String col = orderColumns.get(i);
                Boolean flag = ascendingFlags.get(i);
                
                Object valA = a.get(col); //assume always not null
                Object valB = b.get(col); //assume always not null
                int ans = ((Comparable<Object>) valA).compareTo(valB);
                if (ans == 0) continue;
                return flag ? ans : -ans;
            }
            return 0;
        });
        return rows;
    }
}
