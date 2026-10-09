## Explain concept



In this problem's vocabulary:

- **Shard** = a physical/logical server (a node in the cluster, e.g. a machine or a storage partition that actually holds data).
- **Virtual node (vnode)** = a "clone" of that shard on the ring, created purely by hashing `shard-id + replica index`.

So "every shard contributes exactly `vnodes` virtual nodes" means: when you `add_shard("db1")` on a ring built with `vnodes=100`, you must place exactly 100 points on the ring for `db1` — typically at positions `hash("db1#0"), hash("db1#1"), ..., hash("db1#99")` — all owned by that one server.

### Why bother with vnodes?

With only 1 point per server (plain consistent hashing):

- **Bad distribution**: 3 servers hashed onto a circle of 2³² positions can easily end up owning arcs like 70% / 25% / 5% of the keyspace — one server is way hotter than the others.
- **Uneven load on removal**: when a server dies, its *entire arc* goes to the single next node on the ring, doubling that node's load.

With e.g. 100 vnodes per server:

- The much larger number of points spreads evenly over the ring statistically, so each shard ends up owning roughly `1/N` of the keyspace.
- When a shard is removed, its 100 scattered arcs are absorbed by ~all other shards (each picks up the few arcs where it happens to be the successor), so the load migrates *evenly* across the whole cluster instead of hammering one neighbor.
- Adding a shard similarly steals a little from everyone rather than half of one neighbor's range.

So in your implementation:

- The ring holds `numShards × vnodes` positions total (sorted structure keyed by hash → shard id).
- A key lookup is once: hash the key, find the first vnode position ≥ key's hash (wrapping around), and return the shard that owns that vnode.
- `add_shard`/`remove_shard` must insert/delete exactly `vnodes` points, and (important for grading) the hash function must be deterministic across the shard's replicas, e.g. `hash(shard_id + "#" + i)` for `i` in `0..vnodes-1`.

This is exactly how Amazon Dynamo, Cassandra, and libketama (memcached clients) do it — "vnode = replica of a server on the hash ring."





## What is FNV-1a 32-bit hash

Explain FNV-1a concisely.

**FNV-1a** (Fowler–Noll–Vo, version "1a") is a classic non-cryptographic hash function — fast, simple, and designed purely to spread strings evenly across a fixed range (perfect for hash tables and consistent-hashing rings).

### The algorithm (32-bit version)

```

h = 2166136261          (offset basis, = 0x811C9DC5)

for each byte b of the input:

    h = h XOR b         ("1a" = XOR first, then multiply)

    h = h * 16777619    (FNV prime, = 0x01000193, mod 2^32)

return h                (a value in [0, 2^32))

```

That's the entire hash — 4 lines of code. That's exactly what [[Main.java](http://Main.java)'s `fnv1a32`]([file:///Users/shaodi/projects/leetcode/Openai/Consistent](file:///Users/shaodi/projects/leetcode/Openai/Consistent) Hashing Ring/[Main.java](http://Main.java)#L12-L19) implements.

### Key ideas

1. **Offset basis (2166136261)**: the hash starts from a carefully chosen non-zero constant instead of 0, so short inputs and inputs starting with zero bytes still mix well.

2. **XOR each byte in, then multiply by an odd prime**: the "1a" variant does XOR-*before*-multiply (FNV-1 does it the other way; 1a has better avalanche properties). The multiplication by `16777619` scrambles the byte's bits across the whole 32-bit space, so changing one byte of the input wildly changes the output. This gives near-uniform distribution, which is exactly what keeps vnodes spread evenly around the ring.

3. **mod 2^32**: the `& 0xFFFFFFFFL` truncates the product to 32 bits, keeping the result in `[0, 2^32)` — matching the problem's ring interval.

### Why it's used here (and in real systems)

- **Deterministic everywhere**: unlike Java's `String.hashCode()` (which is only guaranteed within the JVM) or language-dependent hashes, FNV-1a produces the same number for the same string on every machine and language. That's why the problem can fix expected test outputs.

- **Fast**: one XOR + one multiply per byte, no lookup tables.

- **Good enough distribution**: real consistent-hashing libraries (e.g. libketama uses MD5; others use CRC32 or FNV) just need uniformity, not cryptographic security.

It's *not* suitable for passwords or security — it's trivially forgeable — but for "where does this key land on the ring," it's ideal.