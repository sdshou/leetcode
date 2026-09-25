# **Count Connected Friend Groups**

## Source

- [prachub](https://prachub.com/coding-questions/count-connected-friend-groups)

Similar questions:
- Leetcode: [547. Number of Provinces](https://leetcode.com/problems/number-of-provinces/description/)
- Leetcode: [2685. Count the Number of Complete Components](https://leetcode.com/problems/count-the-number-of-complete-components/description/)

## Problem

Implement count_friend_groups(is_connected). The input is a symmetric n by n zero-one adjacency matrix with ones on its diagonal. People are in the same group when a path of direct connections joins them. Return the number of connected components without mutating the matrix.

## Examples

### **Examples**

**Example 1**

```
Input ([[1]],)

Output 1

Notes: One person forms one group.
```

**Example 2**

```
Input ([[1, 0], [0, 1]],)

Output 2

Notes: Two isolated people form two groups.
```



### **Constraints**

- 1 <= n <= 200
- The matrix is n by n, symmetric, contains only 0 and 1, and has a one on every diagonal entry.
- Do not mutate the input matrix.

