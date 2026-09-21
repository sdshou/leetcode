# OpenAI Screening Interview: Implement in-memory DB querying

## Part 1: Project Selected Columns from an In-Memory Table
### Problem
Implement relational projection over an in-memory table.

You are given a table as a list of rows, where each row is a dictionary mapping string column names to values. Given a list of column names to keep, return a new table that contains only those columns for every row.

### Function
```
def solution(rows, selected_columns):
    ...
```
- `rows` — a list of dictionaries. Each dictionary represents one row, with string keys (column names).
- `selected_columns` — a list of strings naming the columns to project, in the order they should appear in the output.

### What to return
A new list of dictionaries — one output row per input row — where each output row contains exactly the columns named in selected_columns:

Keep the rows in their original order.
In each output row, include only the keys in selected_columns, in that same order. Drop any other columns present in the source row.
If a requested column is missing from a source row, still include that key in the output with value None.
Do not modify the input rows (build fresh dictionaries; the source rows are read-only).



## Example
Input:
```
rows = [{"id": "1", "name": "Ada", "birthday": "1815-12-10"}, {"id": "2", "name": "Charles", "birthday": "1791-12-26"}], selected_columns = ["id", "name"] 
```
Output:
```
[{"id": "1", "name": "Ada"}, {"id": "2", "name": "Charles"}] (the birthday column is dropped).
```

Input:
```
rows = [{"id": "1"}, {"name": "Ada"}], selected_columns = ["id", "name"] 
```
Output:
```
[{"id": "1", "name": None}, {"id": None, "name": "Ada"}] (missing columns become None).
```

### Edge cases

If `rows` is empty, return [].
If `selected_columns` is empty, return one empty dictionary `{}` for each input row (e.g. two rows → `[{}, {}]`).

### Constraints
`0 <= len(rows) <= 10000`
`0 <= len(selected_columns) <= 100`
Each row is a dictionary with string keys.
Do not modify the input rows in place.

## Time & Space Complexity
Time: O(N) per query in a simple structure; O(log N) with interval indexing.

Space: O(N) for grants and timestamp-specific subtract operations.