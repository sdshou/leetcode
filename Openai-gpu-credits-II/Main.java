import java.util.*;


public class Main {
    public static void main(String[] args) {
        CreditSystem system = new CreditSystem();
        system.grantCredit("a", 3, 10, 60);
        System.out.println(system.getBalance(10)); // 3
        system.grantCredit("b", 2, 20, 40);
        system.subtract(1, 30);
        system.subtract(1, 40);
        system.subtract(3, 50);
        System.out.println(system.getBalance(10)); // 3
        System.out.println(system.getBalance(20)); // 5
        System.out.println(system.getBalance(30)); // 4
        System.out.println(system.getBalance(35)); // 4
        System.out.println(system.getBalance(40)); // 2
        System.out.println(system.getBalance(50)); // 0
        System.out.println(system.getBalance(55)); // 0
    }
}

class CreditSystem {
    private Set<Event> events = new TreeSet<>((a, b) -> {
        if (a.start == b.start) {
            if (a.type == b.type && a.id != null) {
                return a.id.compareTo(b.id);
            }
            return a.type - b.type;
        }
        return a.start - b.start;
    });

    private Map<Integer, Event> subs = new HashMap<>();

    // the 'end' is exclusive
    // O(logn)
    public void grantCredit(String id, int credit, int start, int end) {
        this.events.add(new Event(id, 0, start, end, credit));
    }

    // O(klogk + klogk + n)
    public int getBalance(int timestamp) {
        // 可能只要放 [end, credit] 即可
        Queue<Credit> pq = new PriorityQueue<>((a, b) -> a.end - b.end);
        // 必须从最左边开始扫，不然会漏掉解（corner case）
        for (Event event : events) {
            if (event.start > timestamp) break;
            switch (event.type) {
                case 0: {
                     // O(logk)
                    pq.offer(new Credit(event.credit, event.start, event.end));
                    break;
                }
                case 1: {
                    int subval = event.credit;
                    int subtime = event.start;
                    while (!pq.isEmpty()) {
                        Credit cur = pq.peek();
                        if (cur.end <= subtime) {
                            pq.poll();
                            continue;
                        }
                        if (cur.credit > subval) {
                            cur.credit -= subval;
                            break;
                        } else {
                            pq.poll();
                            subval -= cur.credit;
                        }
                    }
                }
            }
        }
        
        int amount = 0;
        while (!pq.isEmpty()) {
            Credit c = pq.poll();
            if (c.end <= timestamp) continue;
            amount += c.credit;
        }
        return amount;
    }

    // O(logn)
    public void subtract(int credit, int timestamp) {
        if (this.subs.containsKey(timestamp)) {
            Event event = this.subs.get(timestamp); // O(1)
            this.events.remove(event); // O(logn)
            event.credit += credit;
            this.events.add(event); // O(logn)
        } else {
            Event event = new Event(1, timestamp, timestamp, credit);
            this.subs.put(timestamp, event);
            this.events.add(event);
        }
    }
}

class Event {
    int type;// 0: start, 1: end; 2: sub
    int start;
    int end;
    int credit;
    String id;

    public Event(String id, int type, int start, int end, int credit) {
        this.id = id;
        this.type = type;
        this.start = start;
        this.end = end;
        this.credit = credit;
    }

    public Event(int type, int start, int end, int credit) {
        this(null, type, start, end, credit);
    }
}

class Credit {
    int credit;
    int start;
    int end;

    public Credit(int credit, int start, int end) {
        this.credit = credit;
        this.start = start;
        this.end = end;
    }
}