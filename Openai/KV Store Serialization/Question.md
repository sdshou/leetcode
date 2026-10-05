# **Persistent In-Memory KV Store**

# **Part 1: Serialize and Deserialize a Persistent In-Memory KV Store**

## Source

- [prachub](https://prachub.com/coding-questions/implement-persistent-kv-store-serialization)

**Note**: airbnb also interviewed with this question(Part1 - Part3)

## Problem

Implement a persistent in-memory key-value store using a fake disk inside the function.

The store supports `put`, `get`, `delete`, `serialize(path)`, and `deserialize(path)`.

Each key and value is either a Python `bytes` object or a `str`. Your persistence format must be binary-safe: do not rely on delimiters that may appear inside the data. When `deserialize(path)` is called, the current in-memory store must become exactly the state that was previously written by `serialize(path)`.

Because an online judge should not depend on real file I/O, treat `path` as a label in an internal dictionary that represents disk.

## Examples

**Example 1**

```
Input: 
([('put', 'a', '1'), ('get', 'a'), ('serialize', 'snap1'), ('put', 'a', '2'), ('deserialize', 'snap1'), ('get', 'a')],)

Output: ['1', '1']
```

**Example 2**

```
Input: 
([('put', b'a|b', b'v:1'), ('serialize', 'p'), ('delete', b'a|b'), ('get', b'a|b'), ('deserialize', 'p'), ('get', b'a|b')],)

Output: [None, b'v:1']
```



## Constraints

- `0 <= len(operations) <= 10^4`
- Keys and values are only of type `bytes` or `str`
- Total size of all live keys and values at any moment is at most `10^6` bytes
- Every `deserialize(path)` refers to a path that was previously serialized

