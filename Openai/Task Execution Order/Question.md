# Lexicographically Smallest Task Execution Order

## Source
- [prachub](https://prachub.com/coding-questions/order-tasks-with-dependencies-using-a-deterministic-topological-sort)

## Problem
You are scheduling n tasks numbered from 0 through n - 1. Some tasks cannot begin until other tasks have finished.

Each element of dependencies is a pair [task, prerequisite], which means that prerequisite must occur before task. The dependency graph is guaranteed to be acyclic, so at least one valid execution order always exists.

Return a list that contains every task ID from 0 through n - 1 exactly once, ordered so that each task appears after all of its prerequisites. Several valid orders may exist; return the lexicographically smallest one. Lexicographic comparison scans the two orders position by position and prefers the order whose integer value is smaller at the first position where they differ. Return an empty list when n is zero.

Example 1: Input: n = 4, dependencies = `[[2, 0], [2, 1], [3, 1]]` Output: `[0, 1, 2, 3]` Explanation: Task 2 must come after tasks 0 and 1, and task 3 must come after task 1. Tasks 0 and 1 are free at the start, and 0 is the smaller of them; after both are placed, tasks 2 and 3 are free and 2 is smaller.

Example 2: Input: n = 4, dependencies = `[[0, 2], [1, 2]]` Output: `[2, 0, 1, 3]` Explanation: Tasks 0 and 1 both require task 2, so only tasks 2 and 3 are free at the start. Taking the smaller free task, 2, releases 0 and 1, and the remaining free tasks are then taken in increasing order.

All task IDs fit comfortably in a 32-bit signed integer; no value in the input or the output exceeds 2^31 - 1.


## Constraints
- 0 <= n <= 100000.
- 0 <= len(dependencies) <= 200000.
- Every referenced task ID is in [0, n).
- There are no repeated dependency pairs or self-dependencies.
- The graph is guaranteed to be acyclic.
- Each dependency is a pair [task, prerequisite] meaning prerequisite must occur before task.
- All values fit in a 32-bit signed integer; no value exceeds 2^31 - 1.