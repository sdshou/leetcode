import java.util.*;


public class Main {
    public static void main(String[] args) {
        CreditSystem system = new CreditSystem();
        system.grantCredit(3, 10, 60);
        System.out.println(system.getBalance(10)); // 3
        system.grantCredit(2, 20, 40);
        system.subtract(1, 30);
        system.subtract(3, 50);
        System.out.println(system.getBalance(10)); // 3
        System.out.println(system.getBalance(20)); // 5
        System.out.println(system.getBalance(30)); // 4
        System.out.println(system.getBalance(35)); // 4
        System.out.println(system.getBalance(40)); // 3
        System.out.println(system.getBalance(50)); // 0
        System.out.println(system.getBalance(55)); // 0
    }
}

class CreditSystem {
    private Map<Integer, Credit> grants = new TreeMap<>();
    private Map<Integer, Integer> subs = new TreeMap<>();

    // the 'end' is exclusive
    public void grantCredit(int credit, int start, int end) {
        this.grants.put(start, new Credit(credit, start, end));
    }

    public int getBalance(int timestamp) {
        int left = timestamp;
        Queue<Credit> pq = new PriorityQueue<>((a, b) -> a.end - b.end);
        for (Map.Entry<Integer, Credit> grant : grants.entrySet()) {
            if (grant.getKey() > timestamp) break;
            Credit c = grant.getValue();
            if (c.end > timestamp) {
                pq.offer(new Credit(c.credit, c.start, c.end));
                left = Math.min(left, c.start);
            }
        }
        for (Map.Entry<Integer, Integer> sub : subs.entrySet()) {
            var subTime = sub.getKey();
            if (subTime > timestamp) break;
            if (subTime >= left) {
                var subVal = sub.getValue();
                while (!pq.isEmpty()) {
                    Credit head = pq.peek();
                    if (head.start > subTime) continue;
                    if (head.credit > subVal) {
                        head.credit -= subVal;
                        break;
                    }
                    pq.poll();
                    subVal -= head.credit;
                }
            }
        }
        int amount = 0;
        while (!pq.isEmpty()) {
            amount += pq.poll().credit;
        }
        return amount;
    }

    public void subtract(int credit, int timestamp) {
        subs.put(timestamp, subs.getOrDefault(timestamp, 0) + credit);
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