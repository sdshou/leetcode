# Simulate Infection Spread and Recovery

## Source

- [prachub](https://prachub.com/coding-questions/implement-ipv4-iterators-and-cidr-expansion)

## Problem

Return the first **n** IPv4 addresses produced by iterating *backward* from a given ending address.

An IPv4 address can be treated as a single unsigned 32-bit integer formed from its four octets (`a.b.c.d` → `(a << 24) | (b << 16) | (c << 8) | d`). Iterating backward means repeatedly moving to the **previous** address, i.e. decrementing that integer by one. Borrows cross octet boundaries automatically — for example, the address before `10.0.0.0` is `9.255.255.255`.

## **What to implement**

```python
def solution(end_ip, n):
    ...

```

Given:

- `end_ip` — a string holding a valid IPv4 address (the address to start from).
- `n` — an integer count of addresses to produce.

Return a **list of IPv4 address strings**, starting with `end_ip` itself and continuing downward, one address lower each step.

## **Rules**

- The result is **inclusive of** `end_ip`: the first element of the list is `end_ip`.
- Each subsequent element is the next-lower IPv4 address (the 32-bit integer value minus one).
- Produce up to `n` addresses in total.
- **Floor at** `0.0.0.0`**:** never go below `0.0.0.0`. If the descending sequence reaches `0.0.0.0` before `n` addresses have been produced, stop there. In that case the returned list contains fewer than `n` elements (it ends with `0.0.0.0`).
- If `n <= 0`, return an empty list.\

## **Examples**

- `solution("192.168.0.255", 3)` → `["192.168.0.255", "192.168.0.254", "192.168.0.253"]`
- `solution("10.0.0.0", 2)` → `["10.0.0.0", "9.255.255.255"]` (borrow across octets)
- `solution("1.0.0.0", 3)` → `["1.0.0.0", "0.255.255.255", "0.255.255.254"]`
- `solution("0.0.0.0", 5)` → `["0.0.0.0"]` (already at the floor, so only one address is returned)

## **Constraints**

- `end_ip` is a valid IPv4 address.
- `0 <= n <= 100000`
- Do not produce any address smaller than `0.0.0.0`.

