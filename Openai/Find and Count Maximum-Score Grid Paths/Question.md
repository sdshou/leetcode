# **Lexicographically Smallest Task Execution Order**

## Source

- [prachub](https://prachub.com/coding-questions/find-and-count-maximum-score-grid-paths)

## Problem

Find the maximum-scoring path from a fixed top-row cell to the last row of a grid using diagonal, downward, and limited two-row jump moves. Return the score, number of optimal paths modulo the limit, and lexicographically smallest optimal coordinate sequence.

Starting in the top row of an integer grid, move one row diagonally or vertically, or jump two rows vertically up to max_jumps times. Return the maximum last-row path score, the lexicographically smallest maximizing coordinate path, and the number of maximizing paths modulo 1,000,000,007.

## Examples



### **Examples**

**Example 1**

```
Input ([[5, 1]], 0, 0)

Output {"max_score": 5, "path": [[0, 0]], "count": 1}

Notes: A one-row grid has exactly its starting path.
```

**Example 2**

```
Input ([[1, 1], [2, 2]], 0, 0)

Output {"max_score": 3, "path": [[0, 0], [1, 0]], "count": 2}

Notes: Two equal last-row choices tie; the smaller column is representative.
```



### **Constraints**

- The grid is nonempty and rectangular.
- The start column is in bounds and max_jumps is nonnegative.
- Every valid path ends on the last row.
- A two-row jump stays in the same column.
- Distinct coordinate sequences count as distinct paths.

