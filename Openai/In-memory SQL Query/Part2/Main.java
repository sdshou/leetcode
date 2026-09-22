import java.util.*;


public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        List<Map<String, Object>> example1 = new ArrayList<>(List.of(
            new HashMap<>(Map.of("id", "1", "age", 36)),
            new HashMap<>(Map.of("id", "2", "age", 28)),
            new HashMap<>(Map.of("id", "3", "age", 36))
        ));
        List<Condition> condition1 = new ArrayList<>(List.of(new Condition("age", "=", 36)));
        var res1 = solution.solution(example1, condition1);
        System.out.println(res1); // [{age=36, id=1}, {age=36, id=3}]

        List<Map<String, Object>> example2 = new ArrayList<>(List.of(
            new HashMap<>(Map.of("id", "1", "age", 36, "name", "Ada")),
            new HashMap<>(Map.of("id", "2", "age", 28, "name", "Bob")),
            new HashMap<>(Map.of("id", "3", "age", 40, "name", "Ada"))
        ));
        List<Condition> condition2 = new ArrayList<>(List.of(
            new Condition("name", "=", "Ada"),
            new Condition("age", ">", 36)
        ));
        var res2 = solution.solution(example2, condition2);
        System.out.println(res2); // [{name=Ada, age=40, id=3}]

        List<Map<String, Object>> example3 = new ArrayList<>(List.of(
            new HashMap<>(Map.of("id", "1")),
            new HashMap<>(Map.of("id", "2", "name", "Ada"))
        ));
        List<Condition> condition3 = new ArrayList<>(List.of(
            new Condition("name", "=", "Ada")
        ));
        var res3 = solution.solution(example3, condition3);
        System.out.println(res3); // [{name=Ada, id=2}]
    }
}

class Solution {
    List<Map<String, Object>> solution(
        List<Map<String, Object>> rows, List<Condition> conditions) {
        List<Map<String, Object>> res = new ArrayList<>();
        if (rows == null || rows.size() == 0) return res;
        if (conditions == null || conditions.size() == 0) return rows;
        for (Map<String, Object> row : rows) {
            boolean isMatch = true;
            for (Condition condition : conditions) {
                if (!condition.isMatch(row.get(condition.column))) {
                    isMatch = false;
                    break;
                }
            }
            if (isMatch) {
                res.add(row);
            }
        }
        return res;
    }
}

class Condition {
    String column;
    String operator;
    Object value; // assume always not null

    public Condition(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public boolean isMatch(Object val) {
        if (val == null) return false;
        switch (operator) {
            case "=": {
                return val.equals(this.value);
            }
            case "!=": {
                return !val.equals(this.value);
            }
            case "<": {
                return ((Comparable<Object>) val).compareTo(this.value) < 0;
            }
            case "<=": {
                return ((Comparable<Object>) val).compareTo(this.value) <= 0;
            }
            case ">": {
                return ((Comparable<Object>) val).compareTo(this.value) > 0;
            }
            case ">=": {
                return ((Comparable<Object>) val).compareTo(this.value) >= 0;
            }
        }
        return false;
    }
}