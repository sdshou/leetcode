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
    private Map<Integer, Credit> grants = new TreeMap<>();
    private Map<Integer, Integer> subs = new HashMap<>();

    // the 'end' is exclusive
    public void grantCredit(int credit, int start, int end) {
        this.grants.put(start, new Credit(credit, start, end));
    }

    public int getBalance(int timestamp) {
        int amount = 0;
        for (Map.Entry<Integer, Credit> grant : grants.entrySet()) {
            if (grant.getKey() > timestamp) break;
            Credit c = grant.getValue();
            if (c.end > timestamp) {
                amount += c.credit;
            }
        }
        amount -= this.subs.getOrDefault(timestamp, 0);
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