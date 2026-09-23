# Simulate Infection Spread and Recovery

## Source

- [prachub](https://prachub.com/coding-questions/simulate-infection-spread-with-immunity-and-recovery)

## Problem

Simulate a rectangular grid for a fixed number of days. S is susceptible, I infected, M immune, R recovered, and . empty. Every initial or newly infected person is infectious for exactly recoveryDays full simulation days. During a day, all people infected at its start infect orthogonally adjacent susceptible cells; spread and end-of-day recoveries are simultaneous. A newly infected cell first spreads the next day, and a source still spreads on its final infectious day before becoming R. Return the final grid after days days.



## Examples

```
(["SIS","SMS","SSS"], 2, 1)  ->  ['III', 'SMS', 'SSS'] 
(["SIS","SMS","SSS"], 2, 2)  ->  ['IRI', 'IMI', 'SSS']
```


## Constraints

- rows and columns are positive and rows * columns <= 200,000.
- Grid characters are S, I, M, R, or . and rows have equal length.
- 1 <= recoveryDays <= 200,000 and 0 <= days <= 200,000.
- Infection uses four-direction adjacency and all daily changes are simultaneous.
