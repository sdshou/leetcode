# Spreading Contagion on a Grid
Part 2: Count the Immune Units

## Source

- [prachub](https://prachub.com/coding-questions/spreading-contagion-on-a-grid)

## Problem

Same grid encoding as Part 1: 0 is empty, 1 is a healthy unit, 2 is an infected unit; infection spreads 4-directionally, one ring per minute.

Some healthy units can never become infected, no matter how much time passes — they are unreachable from every initial infection source. Call such a unit immune.

Return the count of immune units in the grid (healthy units the contagion can never reach). Empty locations (0) and infected locations (2) are not units and must not be counted.

## Examples

```
cells = [[2,1,1],[0,1,1],[1,0,1]]  ->  1
```
The bottom-left healthy unit is isolated from the contagion and is the only immune unit.


## Constraints

- 1 <= m, n and m * n <= 10^4
- Each cells[i][j] is 0, 1, or 2

