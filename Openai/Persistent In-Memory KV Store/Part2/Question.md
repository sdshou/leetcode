# **Persistent In-Memory KV Store**

# **Part 2: Persist the KV Store Across Multiple Segments**

## Source

- [prachub](https://prachub.com/coding-questions/implement-persistent-kv-store-serialization)

**Note**: airbnb also interviewed with this question(Part1 - Part3)

## Problem

Extend the persistent key-value store so a snapshot can be split across multiple segment files.

Implement the same store operations as in Part 1, but now `serialize(prefix)` must save the snapshot as a list of segment blobs, where each individual segment has size at most `max_segment_size` bytes.

Segments may later be returned in a different order, so every segment must contain enough metadata to reconstruct the original snapshot correctly. Use a binary-safe encoding for keys and values; do not rely on delimiters.

For testing, the fake disk stores a list of segment blobs under each `prefix`.

## Examples

**Example 1**

```
Input: 
(15, [('put', b'a', b'12345'), ('put', b'b', b'67890'), ('serialize', 'p'), ('segment_count', 'p'), ('delete', b'a'), ('deserialize', 'p'), ('get', b'a'), ('get', b'b')])

Output: [6, b'12345', b'67890']
Notes: The snapshot spans multiple segments when the per-segment size limit is small.
```

**Example 2**

```
Input: 
(20, [('put', b'aa', b'xx'), ('put', b'bb', b'yy'), ('serialize', 'snap'), ('segment_count', 'snap'), ('reorder', 'snap', [2, 0, 1]), ('delete', b'aa'), ('deserialize', 'snap'), ('get', b'aa'), ('get', b'bb')])

Output: [3, b'xx', b'yy']
Notes: Even after reordering the saved segments, deserialization must rebuild the correct store.
```



## Constraints

- `9 <= max_segment_size <= 10^6`
- `0 <= len(operations) <= 10^4`
- Total serialized snapshot size at any moment is at most `10^6` bytes
- Every `deserialize(prefix)` and `segment_count(prefix)` refers to a prefix that was previously serialized
- Every `reorder(prefix, order)` uses a valid permutation of the existing segment indices

