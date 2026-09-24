# Simulate Infection Spread and Recovery

## Source

- [prachub](https://prachub.com/coding-questions/implement-ipv4-iterators-and-cidr-expansion)

## Problem

Summarize a CIDR block without expanding every address it contains.

Some CIDR blocks (for example `/8` or `/0`) cover billions of addresses, so materializing the whole range is not an option. Your task is to produce a **compact summary** of the block using only its endpoints and a small sample of addresses from each end.

## **Function**

```
solution(cidr, k)

```

- `cidr` — a string in `"A.B.C.D/prefix"` form (a dotted-quad IPv4 address followed by a prefix length), e.g. `"192.168.1.0/30"`.
- `k` — an integer controlling how many addresses to sample from each end (`0 <= k <= 1000`).

## **What to compute**

A CIDR block of prefix length `p` always describes a **contiguous** range of `2^(32 - p)` IPv4 addresses. Derive the following:

1. **count** — the total number of addresses in the block.
2. **start IP** — the network (lowest) address. **Clear the host bits** of the given IP first: any host bits set in the input are masked off, so `"1.2.3.4/24"` has start `"1.2.3.0"`.
3. **end IP** — the highest address in the block.
4. **first k addresses** — the first `k` addresses, in ascending order starting from the start IP.
5. **last k addresses** — the last `k` addresses, in ascending order ending at the end IP.

## **Return value**

Return a tuple of five values, in this exact order:

```
(count, start_ip, end_ip, first_k, last_k)

```

- `count` is an integer.
- `start_ip` and `end_ip` are dotted-quad strings (e.g. `"192.168.1.0"`).
- `first_k` and `last_k` are lists of dotted-quad strings.

## **Rules and edge cases**

- `k = 0`**:** both `first_k` and `last_k` are **empty lists** `[]`. (More generally, any `k <= 0` yields two empty lists.)
- **Block no larger than** `k`**:** if the block contains at most `k` addresses, then `first_k` and `last_k` are **identical** — each is the **entire block** (every address, in ascending order).
- A `/32` block contains a single address, so its start and end IPs are equal and each sample list (for `k >= 1`) holds just that one address.
- A `/0` block contains all `4294967296` IPv4 addresses; handle it with the same arithmetic — do **not** enumerate the full range.

## **Examples**


| `cidr`             | `k` | **Result**                                                                                          |
| ------------------ | --- | --------------------------------------------------------------------------------------------------- |
| `"192.168.1.0/30"` | `2` | `(4, "192.168.1.0", "192.168.1.3", ["192.168.1.0", "192.168.1.1"], ["192.168.1.2", "192.168.1.3"])` |
| `"10.0.0.5/32"`    | `3` | `(1, "10.0.0.5", "10.0.0.5", ["10.0.0.5"], ["10.0.0.5"])`                                           |
| `"0.0.0.0/0"`      | `1` | `(4294967296, "0.0.0.0", "255.255.255.255", ["0.0.0.0"], ["255.255.255.255"])`                      |
| `"1.2.3.4/24"`     | `0` | `(256, "1.2.3.0", "1.2.3.255", [], [])`                                                             |


## **Constraints**

- `cidr` is valid and has a prefix length in the range `0` to `32`.
- `0 <= k <= 1000`.
- The block may be as large as `/0`, so expanding the full range is **not allowed** — generate at most about `2k` addresses regardless of block size.

