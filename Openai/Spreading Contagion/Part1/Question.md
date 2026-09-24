# Spreading Contagion on a Grid
Part 1: Time to Full Infection

## Source

- [prachub](https://prachub.com/coding-questions/spreading-contagion-on-a-grid)

## Problem

You are given an m x n integer grid cells. Each entry is one of:

0 — an empty location (no unit present)
1 — a healthy unit
2 — an infected unit
Time advances in discrete minutes. Every minute, any healthy unit (1) that is 4-directionally adjacent (up, down, left, right) to an infected unit (2) becomes infected. All infections in a given minute happen simultaneously.

Return the minimum number of minutes that must elapse until no healthy unit remains. If it is impossible for every healthy unit to become infected (some healthy unit can never be reached by the contagion), return -1.

## Examples

```
cells = [[2,1,1],[1,1,0],[0,1,1]]  ->  4
cells = [[2,1,1],[0,1,1],[1,0,1]]  ->  -1   (bottom-left unit is unreachable)
cells = [[0,2]]                    ->  0    (no healthy units at minute 0)
```



## Constraints

- 1 <= m, n and m * n <= 10^4
- Each cells[i][j] is 0, 1, or 2

