# Simulate Infection Spread and Recovery

## Source

- [prachub](https://prachub.com/coding-questions/implement-ipv4-iterators-and-cidr-expansion)

## Problem

Generate the first n IPv4 addresses produced by iterating forward from a starting address, treating each address as a 32-bit counter that increments by one.

Implement:

```
solution(start_ip, n)
```

- `start_ip` — a string holding a valid IPv4 address in dotted-decimal form (four octets 0–255 separated by ., e.g. "192.168.0.1").
- `n` — an integer count of addresses to produce.
Return a list of strings: the address sequence beginning at start_ip (inclusive) and stepping forward one address at a time.



## **How forward iteration works**

Interpret the four octets as a single 32-bit number (`a·2²⁴ + b·2¹⁶ + c·2⁸ + d`). The **next** address is that number plus one, decoded back into dotted-decimal. This means an octet rolls over into the next when it passes `255`:

- `192.168.0.255` → `192.168.1.0`

So the returned list is `start_ip`, `start_ip + 1`, `start_ip + 2`, …, up to `n` addresses total.

## **Rules**

- **Inclusive start:** the first element of the result is always `start_ip` (when `n` produces at least one address).
- **Stop at the ceiling:** `255.255.255.255` is the largest valid address. If the forward sequence reaches it before `n` addresses have been produced, **stop there** — never produce an address greater than `255.255.255.255`. The result may therefore contain **fewer than** `n` entries.
- **Non-positive** `n`**:** if `n <= 0` (in particular `n == 0`), return an empty list `[]`.

## Examples


| `start_ip`          | `n` | **Output**                                        |
| ------------------- | --- | ------------------------------------------------- |
| `"192.168.0.1"`     | `3` | `["192.168.0.1", "192.168.0.2", "192.168.0.3"]`   |
| `"192.168.0.255"`   | `3` | `["192.168.0.255", "192.168.1.0", "192.168.1.1"]` |
| `"255.255.255.255"` | `3` | `["255.255.255.255"]`                             |
| `"0.0.0.0"`         | `0` | `[]`                                              |


## **Constraints**

- `start_ip` is a valid IPv4 address.
- `0 <= n <= 100000`
- Do not produce any address greater than `255.255.255.255`.

