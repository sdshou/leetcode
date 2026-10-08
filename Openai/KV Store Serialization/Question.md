# **Implement KV Store Serialization**

## Source

- [prachub](https://prachub.com/coding-questions/implement-kv-store-serialization)

## Problem

Implement a **serializer and deserializer** for an in-memory key-value store using a custom binary format.

## **What to implement**

Write a single function `solution(operation, data)` that dispatches on the string `operation`:

- `solution('encode', store)` → returns the serialized `bytes`.
- `solution('decode', buffer)` → returns the reconstructed **store** (a `dict`).

Any other `operation` value must raise an error.

## **The store**

`store` is a nested Python `dict` whose **keys are UTF-8 strings** and whose values are one of:

- **int64** — a Python `int` in signed 64-bit range
- **float64** — a Python `float`
- **bool**
- **UTF-8 string**
- another **nested dict** following the same rules

Because `bool` is a subclass of `int` in Python, treat `True`/`False` as **bool**, not as integers.

## **Binary layout**

Every encoded buffer has three parts:

1. **Header** (9 bytes)
  - 4 bytes magic: `b'KVSB'`
  - 1 byte version: `1`
  - 4 bytes: little-endian **unsigned** body length
2. **Body**
  - the root **map payload** (see below)
3. **Footer** (4 bytes)
  - 4 bytes: little-endian **unsigned** checksum

**Checksum rule:** the checksum is the unsigned 32-bit sum of **every byte before the checksum field** (i.e. header + body), taken modulo `2^32`.

### **Map payload format**

- 4 bytes: little-endian **unsigned** entry count
- then each entry, emitted in the **dict's existing iteration order** (do **not** sort keys)



### **Entry format**

- 4 bytes: little-endian unsigned **key length**
- the key bytes (UTF-8)
- 1 byte: **type tag**
- 4 bytes: little-endian unsigned **value length**
- the value bytes



### **Type tags**


| **Tag** | **Type**     | **Value payload**                         |
| ------- | ------------ | ----------------------------------------- |
| `1`     | int64        | exactly 8 bytes, little-endian **signed** |
| `2`     | float64      | exactly 8 bytes, little-endian IEEE-754   |
| `3`     | bool         | exactly 1 byte: `0` or `1`                |
| `4`     | UTF-8 string | raw UTF-8 bytes (any length)              |
| `5`     | nested map   | another full map payload                  |


```
All multi-byte integers in this format (body length, entry count, key length, value length, checksum) are little-endian unsigned, except the int64 value payload (tag 1), which is little-endian signed.
```



## **Decoder validation**

The decoder must reject malformed input by raising an error. It must validate:

- the magic bytes and the version (**reject unsupported versions**);
- the total buffer length is exactly `9 + body_length + 4`;
- the checksum matches the recomputed sum;
- every length prefix stays within bounds (no truncated keys, tags, or values);
- keys and string values are valid UTF-8;
- per-type payload sizes (int64/float64 = 8 bytes, bool = 1 byte and value is `0` or `1`);
- the type tag is known (**reject unknown tags**);
- each nested map payload is consumed exactly.



## **Constraints**

- All keys are strings, all strings are UTF-8, and all integers fit in signed 64-bit range.
- Serialize entries in the dict's existing iteration order; do not sort keys.
- The total serialized size is at most `10^6` bytes, and nesting depth is at most `100`.
- Within a single map, keys are unique.



## **Round-trip guarantee**

Encoding and decoding are inverses: `solution('decode', solution('encode', store))` reproduces the original store, with each field read back in the same order it was written.

## Examples

**Example 1**

```
Input: ('encode', {})

Output: b'KVSB\x01\x04\x00\x00\x00\x00\x00\x00\x00;\x01\x00\x00'

Notes: An empty map body is just a 4-byte entry count of 0. The checksum is the sum of the header and body bytes modulo 2^32.
```

**Example 2**

```
Input: ('decode', b'KVSB\x01\x04\x00\x00\x00\x00\x00\x00\x00;\x01\x00\x00')

Output: {}

Notes: This is the valid encoding of an empty root map, so decoding returns an empty dict.
```



### **Constraints**

- All keys are strings, all strings are UTF-8, and all integers fit in signed 64-bit range.
- Serialize entries in the dict's existing iteration order; do not sort keys.
- The total serialized size is at most 10^6 bytes, and nesting depth is at most 100.
- Within a map, keys are unique.

