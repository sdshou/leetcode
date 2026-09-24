# Spreading Contagion on a Grid
Part 3: Time-Limited (Dynamic) Immunity

## Source

- [prachub](https://prachub.com/coding-questions/spreading-contagion-on-a-grid)

## Problem

Same grid encoding and 4-directional, one-ring-per-minute spread as Parts 1 and 2. Now you are also given an integer x, and immunity is acquired dynamically over time.

A unit develops lasting immunity if it stays healthy for at least x consecutive minutes from the start. Precisely:

- A healthy unit that has not yet been infected after x minutes of spreading have been applied becomes permanently immune and can never be infected afterward, even if an infected neighbor later appears next to it.
- Infections in minutes 1 through x proceed normally on units that are not yet immune.
- Tie-break: infection that lands at minute x takes effect; immunity is granted only to units still healthy after minute x's infections are applied.

Return the number of units still healthy once the system reaches a steady state (no further infections are possible). This includes both units that became permanently immune at minute x and units that were never reachable at all.

When x == 0, every healthy unit is immune from the start, so the answer is the total number of healthy units.

**Key insight**: any unit still healthy right after minute x becomes immune, so after that boundary no healthy non-immune cell exists for the contagion to spread into. The answer is therefore simply the number of healthy cells remaining after running the multi-source BFS for at most x minutes.



## Examples

```
cells = [[2,1,1,1,1]], x = 2  ->  2   (after minute 2: [2,2,2,1,1]; the 2 right cells become immune)
cells = [[2,1,1]],     x = 5  ->  0   (whole row infected within 2 minutes, before x)
cells = [[2,1],[0,1]], x = 1  ->  1   (bottom-right unit is still healthy after minute 1, so it is immune)
```


## Constraints

- 1 <= m, n and m * n <= 10^4
- Each cells[i][j] is 0, 1, or 2
- 0 <= x <= m * n
- When x == 0, every healthy unit is immune from the start