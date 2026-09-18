## 方法一

每个grant其实就是个interval，用一种数据结构把它们存起来，然后对左端点sort，找到跟这个时间戳能戳中的所有interval，然后把credit都加起来。Subtract can stay a HashMap lookup, since it only applies at that exact timestamp.
- 怎么sort：
    - 如果用数组存，那么sort就是 nlogn
    - 所以得用treemap之类的，每次插入都是logn的时间： `Map<Integer, Credit> grants = new TreeMap<>();`, key是interval的左端点，value是额外创建的数据结构Credit，包含(start, end, credit)
    - 需要for循环去累加treemap里符合条件的interval

## 方法二

还有一种存法，比较像扫描线的思路，把每个grant的interval的左右端点拆开来，左端点就是+credit，右端点就是-credit（因为expire了）

```
The idea
A grant of +3 on [10, 60) (end exclusive) means:

at 10: balance goes up by 3
at 60: balance goes down by 3
```

- 具体实现：
    - 也是得用treemap，每次插入都是logn的时间：`TreeMap<Integer, Integer> delta = new TreeMap<>();`，key是时间戳（左端点 或 右端点），value是credit（+credit 或 -credit）
    - 也需要for循环去累加treemap里所有小于时间戳的entry的值