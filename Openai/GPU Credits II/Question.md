# OpenAI Screening Interview: GPU Credits II

## Problem
You are implementing a time-based GPU credit ledger. Credits are granted as named pools with an amount and a half-open validity window `[start, end)`. Usage permanently consumes those pools (like spending from a wallet), rather than applying only at a single timestamp.

Events may arrive out of order. Queries must reconstruct the credit history by event timestamp, not by the order the calls were received.

Implement:

- `grantCredit(id, amount, start, end)` — grant `amount` units of credit `id`, active for `t ∈ [start, end)`.
- `subtract(amount, timestamp)` — permanently deduct `amount` units at `timestamp` from grants that are active at that time.
- `getBalance(timestamp)` — return remaining active credits at `timestamp`. If recorded usage exceeds registered available credits, return `-1` (or throw).

Consumption uses **Earliest Deadline First (EDF)**: when multiple grants are active, deduct from the grant that expires soonest, so longer-lived credits are preserved.

## Example 1
Standard chronological events.

```
grantCredit("a", 10, 10, 40)
grantCredit("b", 5, 20, 60)
getBalance(15)  → 10
getBalance(25)  → 15
subtract(3, 30)
getBalance(35)  → 12
getBalance(45)  → 5
```

Explanation:

- `"a"` is active on `[10, 40)` with 10 units; `"b"` is active on `[20, 60)` with 5 units.
- At `t = 15` only `"a"` is active (`10`). At `t = 25` both are active (`10 + 5 = 15`).
- `subtract(3, 30)` consumes 3 units from `"a"` because `"a"` expires sooner (`40` vs `60`). Remaining: `"a"` has 7, `"b"` has 5.
- At `t = 35` both remain (`7 + 5 = 12`). At `t = 45`, `"a"` has expired, so only `"b"` remains (`5`). The 3 units spent at `t = 30` stay gone.

## Example 2
Out-of-order events (a usage log arrives before the grant).

```
subtract(4, 25)
getBalance(25)           → -1
grantCredit("c", 10, 10, 50)
getBalance(25)           → 6
```

Explanation:

- The subtraction is recorded at `t = 25` before any grant exists, so the query reports insufficient credits.
- After `"c"` is registered on `[10, 50)`, history is resolved by timestamp: at `t = 25` the grant of 10 was valid and 4 were used, so the balance is `6`.

## Example 3
Permanent ledger vs point-in-time (GPU Credits I).

```
grantCredit("a", 5, 10, 50)
subtract(2, 20)
getBalance(20)  → 3
getBalance(25)  → 3
```

In GPU Credits I, a subtraction only affects that exact timestamp, so `getBalance(25)` would still be `5`. In GPU Credits II, the 2 units are permanently gone from the pool, so later queries in the same window still see `3`.

## Suggested Approach
Store grants as intervals with remaining amounts, and store subtract events by timestamp.

On each query, replay events in timestamp order up to the query time. Keep a priority queue of currently active grants ordered by expiration. When a subtract is applied, consume from the earliest-expiring grant that is still active at the subtract timestamp (skip grants that have already expired).

Because events can arrive out of order, do not apply grants and subtracts as a running live balance in processing order; always reconstruct by event time.

A difference array / sweep of `+amount` at `start` and `-amount` at `end` is enough for GPU Credits I (point-in-time usage). It is not enough here, because consumption is persistent and grant-specific (EDF).

## Time & Space Complexity
Time: `O(N log N)` per query if you replay grants and subtracts with a heap; `O(N)` per query with a linear scan of active grants.

Space: `O(N)` for grants and subtract events.
