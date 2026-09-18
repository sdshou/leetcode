# OpenAI Screening Interview: GPU Credits

## Problem
You are implementing a system that tracks GPU credit grants and usage over time. Each grant has an amount and a validity window, and usage events may arrive out of order. Your goal is to process grants, revocations, and balance queries at arbitrary timestamps.

## Example
Input:
```
["CreditSystem", "grantCredit", "getBalance", "grantCredit", "subtract", "subtract", "getBalance", "getBalance", "getBalance", "getBalance", "getBalance", "getBalance"]
[[], ["a", 3, 10, 60], [10], ["b", 2, 20, 40], [1, 30], [3, 50], [10], [20], [30], [35], [40], [50]]
```

Output:
```
[null, null, 3, null, null, null, 3, 5, 4, 5, 3, 0]
```

Explanation:

Credit “a” is active on `[10, 59]` with 3 units.

Credit “b” is active on `[20, 39]` with 2 units.

Subtractions apply only at the exact timestamp.

Queries return the remaining active credits at that time.

## Suggested Approach
Store grant intervals and subtract events keyed by timestamp.

Use a structure such as a difference array, segment tree, or Fenwick tree to accumulate active credits for any timestamp.

For each query, compute the sum of active grants covering the timestamp and subtract any recorded usage. Return -1 if usage exceeds available credits.

## Time & Space Complexity
Time: O(N) per query in a simple structure; O(log N) with interval indexing.

Space: O(N) for grants and timestamp-specific subtract operations.