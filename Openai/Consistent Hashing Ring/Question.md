# **Consistent Hashing Ring with Virtual Nodes for Shard Rebalancing**

`hard` (actually not that hard)

## Source

- [prachub](https://prachub.com/coding-questions/consistent-hashing-ring-with-virtual-nodes-for-shard-rebalancing)

## Problem

Implement a consistent hashing ring with virtual nodes over the integer interval [0, 2^32) (positions wrap: the successor of 2^32-1 is 0). Because the console runs one function, drive the ring through a command list: you are given `operations` (method names) and `args` (the arguments for each), and you return one result per operation.

Support these methods:

- `"ShardRing"` with args [vnodes]: create an empty ring where every shard contributes exactly `vnodes` virtual nodes (1 <= vnodes <= 1000). Return `None`.
- `"add_shard"` with args [shard_id]: add a shard; its virtual node i sits at fnv1a_32(shard_id + "#" + str(i)) for i in 0..vnodes-1. Return `None`.
- `"remove_shard"` with args [shard_id]: remove the shard and all of its virtual nodes. Return `None`.
- `"get_shard"` with args [key]: return the owning shard id, i.e. the first virtual node clockwise (increasing position) starting at `fnv1a_32(key)`; if the key hash exceeds every position, wrap to the smallest position. Return `""` if the ring has no shards.

Hashing (32-bit FNV-1a of the UTF-8 bytes): 

```
hash = 2166136261; 
for each byte b: 
    hash ^= b; 
    hash = (hash * 16777619) mod 2^32.
```

Tie-breaking: if virtual nodes share a position, order them by shard_id ascending, then replica index i ascending; `get_shard` returns the first such node clockwise. `get_shard` must be `O(log V)` (binary search over sorted positions).

## Examples

**Example 1**

```
Input: (["ShardRing", "get_shard"], [[3], ["anykey"]])

Output: [None, ""]

Notes: Empty ring: get_shard has no virtual nodes to route to, so it returns the empty string.
```

**Example 2**

```
Input: (["ShardRing", "add_shard", "get_shard", "get_shard", "get_shard"], [[5], ["A"], ["k1"], ["k2"], ["user:42"]])

Output: [None, None, "A", "A", "A"]

Notes: 
With only shard A on the ring, every key routes clockwise to one of A's 5 virtual nodes, so all keys are owned by A.
```



### **Constraints**

- 1 <= vnodes <= 1000 (fixed for the ring's lifetime)
- Up to 10^5 total operations across add_shard, remove_shard, get_shard
- shard_id and key are non-empty ASCII strings up to 64 characters
- The same shard_id is never added twice without being removed first; remove targets a present shard
- All hash arithmetic is unsigned 32-bit (mod 2^32); ring positions lie in `[0, 2^32)`

