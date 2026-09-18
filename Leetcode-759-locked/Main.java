import java.util.*;


public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        List<List<Interval>> schedule = new ArrayList<>();
        schedule.add(new ArrayList<>(Arrays.asList(new Interval(1, 2), new Interval(5, 6))));
        schedule.add(new ArrayList<>(Arrays.asList(new Interval(1, 3))));
        schedule.add(new ArrayList<>(Arrays.asList(new Interval(4, 10))));
        List<Interval> result = solution.employeeFreeTime(schedule);
        result.forEach(a -> System.out.printf("[%d, %d]\n", a.start, a.end));

        schedule = new ArrayList<>();
        schedule.add(new ArrayList<>(Arrays.asList(new Interval(1, 3), new Interval(6, 7))));
        schedule.add(new ArrayList<>(Arrays.asList(new Interval(2, 4))));
        schedule.add(new ArrayList<>(Arrays.asList(new Interval(2, 5), new Interval(9, 12))));
        result = solution.employeeFreeTime(schedule);
        result.forEach(a -> System.out.printf("[%d, %d]\n", a.start, a.end));
    }
}


// Definition for an Interval.
class Interval {
    public int start;
    public int end;

    public Interval() {}

    public Interval(int _start, int _end) {
        start = _start;
        end = _end;
    }
};

// use priority queue, 双指针谁小移谁
// 还有一种方法是用treemap：https://github.com/grandyang/leetcode/issues/759
class Solution {
    public List<Interval> employeeFreeTime(List<List<Interval>> schedule) {
        List<Interval> results = new ArrayList<>();
        if (schedule == null || schedule.size() == 0) return results;
        // queue item: int[2]{employee_id, employee_interval_id}
        // schedule[a[0]][a[1]], employee[b[0]][b[1]]
        Queue<int[]> pq = new PriorityQueue<>((a, b) -> {
            Interval ia = schedule.get(a[0]).get(a[1]);
            Interval ib = schedule.get(b[0]).get(b[1]);
            return ia.start - ib.start;
        });
        for (int i = 0; i < schedule.size(); i++) {
            pq.offer(new int[]{i, 0});
        }
        Interval ref = null;
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            if (cur[1] + 1 < schedule.get(cur[0]).size()) {
                pq.offer(new int[]{cur[0], cur[1] + 1});
            }
            Interval current = schedule.get(cur[0]).get(cur[1]);
            if (ref == null) {
                ref = current;
            } else {
                if (current.start <= ref.end) {
                    ref.end = Math.max(current.end, ref.end);
                } else {
                    results.add(new Interval(ref.end, current.start));
                    ref = current;
                }
            }
        }
        return results;
    }
}