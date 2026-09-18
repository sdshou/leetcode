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
        System.out.println(system.getBalance(35)); // 5
        System.out.println(system.getBalance(40)); // 3
        System.out.println(system.getBalance(50)); // 0
    }
}

class CreditSystem {
    private Map<Integer, Integer> grants;
    private Map<Integer, Integer> subs;
    /**
     * GPU Credit Management System.
     *
     * grantCredit(id, amount, start, end): Add a credit grant
     * getBalance(timestamp): Get total available credits at timestamp
     * subtract(amount, timestamp): Subtract credits at timestamp
     *
     * Credits are available from start (inclusive) to end (exclusive).
     */
    public CreditSystem() {
        this.grants = new TreeMap<>();
        this.subs = new HashMap<>();
    }

    /**
     * Add a credit grant valid from start to end.
     * @param id grant identifier
     * @param amount credit amount
     * @param start start timestamp (inclusive)
     * @param end end timestamp (exclusive)
     */
    public void grantCredit(String id, int amount, int start, int end) {
        this.grants.put(start, amount);
        this.grants.put(end, -amount);
    }

    /**
     * Return total available credits at the given timestamp.
     * @param timestamp the query timestamp
     * @return available credit balance
     * if usage exceeds available credits at that time, then return -1
     */
    public int getBalance(int timestamp) {
        int res = 0;
        for (Map.Entry<Integer, Integer> grant : grants.entrySet()) {
            if (grant.getKey() > timestamp) break;
            res += grant.getValue();
        }
        res -= this.subs.getOrDefault(timestamp, 0);
        // if the balance is negative, then return -1
        return Math.max(res, -1);
    }

    /**
     * Subtract credits at the given timestamp.
     * @param amount amount to subtract
     * @param timestamp the timestamp
     */
    public void subtract(int amount, int timestamp) {
        this.subs.put(timestamp, this.subs.getOrDefault(timestamp, 0) + amount);
    }
}
