# Simulate Infection Spread and Recovery

## Source

- [prachub](https://prachub.com/coding-questions/implement-ipv4-iterators-and-cidr-expansion)

## Problem

Expand a **CIDR block** into the full list of IPv4 addresses it contains.

You are given a single string `cidr` in the form `base_ip/prefix_len` (for example, `"192.168.1.0/30"`). Treat each IPv4 address as a **32-bit unsigned integer** and use **bitwise masking** to compute the block's boundaries, then enumerate every address in the inclusive range.

## **What to implement**

```
def solution(cidr):
    ...

```

Given the CIDR string, do the following:

1. **Parse** `cidr` into the base IP and the integer prefix length.
2. **Compute the network start address** by clearing the host bits of the base IP (mask off the lowest `32 - prefix_len` bits). The base IP may **not** already be aligned to the network boundary — if it isn't, snap it *down* to the network address.
3. **Compute the highest address** in the block by setting all of those host bits to `1`.
4. **Enumerate** every IPv4 address from the start address to the highest address, **inclusive**, in ascending order.

## **Input**

- `cidr` — a string `"A.B.C.D/prefix_len"`, where `A.B.C.D` is a dotted-quad IPv4 address and `prefix_len` is an integer.

## **Output**

Return a **tuple of three values** `(start_ip, end_ip, all_addresses)`:

- `start_ip` — the network start address, as a dotted-quad string.
- `end_ip` — the highest address in the block, as a dotted-quad string.
- `all_addresses` — a **list** of every IPv4 address in the inclusive range `[start_ip, end_ip]`, each as a dotted-quad string, in ascending order.

All host addresses are included — do **not** exclude the network or broadcast addresses.

## **Examples**


| **Input**          | **Output**                                                                                     |
| ------------------ | ---------------------------------------------------------------------------------------------- |
| `"192.168.1.0/30"` | `("192.168.1.0", "192.168.1.3", ["192.168.1.0", "192.168.1.1", "192.168.1.2", "192.168.1.3"])` |
| `"172.16.5.9/29"`  | `("172.16.5.8", "172.16.5.15", ["172.16.5.8", "172.16.5.9", ..., "172.16.5.15"])`              |
| `"10.0.0.5/32"`    | `("10.0.0.5", "10.0.0.5", ["10.0.0.5"])`                                                       |
| `"0.0.0.0/31"`     | `("0.0.0.0", "0.0.0.1", ["0.0.0.0", "0.0.0.1"])`                                               |


Note in the second example that the base IP `172.16.5.9` is not aligned to the `/29` boundary, so the start address snaps down to `172.16.5.8`.

## **Constraints**

- `cidr` is valid and has a prefix length in the range **0 to 32**.
- For this problem, the expanded range size will be at most **4096 addresses**.
- Use **bitwise masking** to compute the network and end addresses.

