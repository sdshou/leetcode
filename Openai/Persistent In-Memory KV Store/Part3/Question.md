# **Persistent In-Memory KV Store**

# **Part 3: Add an Append-Only Mutation Log with Replay and Compaction**

## Source

- [prachub](https://prachub.com/coding-questions/implement-persistent-kv-store-serialization)

**Note**: airbnb also interviewed with this question(Part1 - Part3)

## Problem

Implement a persistent key-value store that keeps two persisted artifacts on a fake disk:

1. a full snapshot of the store
2. an append-only log of later mutations

The store starts empty. Every `put` and `delete` must update the in-memory map and also append a mutation record to the persisted log. A `restart` simulates process shutdown and startup: throw away the in-memory state, reload the snapshot, and replay the log in order.

A full rewrite after every mutation is too expensive, so snapshot compaction should happen only when the pending log becomes large enough. In this problem, define the compaction policy as follows: if the number of pending log records becomes greater than or equal to `compact_threshold` immediately after a mutation, write a fresh snapshot of the current store and clear the log.

Keys and values are either `bytes` or `str`, and the serialization format must be binary-safe.

`status` operation returns {len(store), pending_log_records, compaction_count}

## Examples

**Example 1**

```
Input: 
(3, [('put', 'a', '1'), ('put', 'b', '2'), ('restart',), ('get', 'a'), ('get', 'b'), ('status',)])

Output: ['1', '2', (2, 2, 0)]
Notes: With threshold 3, two mutations stay only in the log; restart must replay them correctly.
```

**Example 2**

```
Input: 
(2, [('put', 'x', '1'), ('status',), ('put', 'y', '2'), ('status',), ('restart',), ('get', 'x'), ('get', 'y'), ('status',)])

Output: [(1, 1, 0), (2, 0, 1), '1', '2', (2, 0, 1)]
Notes: The second mutation reaches the threshold, so the log is compacted into a snapshot and cleared.
```



## Constraints

- `1 <= compact_threshold <= 10^4`
- `0 <= len(operations) <= 10^4`
- Keys and values are only of type `bytes` or `str`
- Total size of snapshot plus log at any moment is at most `10^6` bytes

