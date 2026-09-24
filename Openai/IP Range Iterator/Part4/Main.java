import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        var res = solution.solution("192.168.1.0/30", 2);
        System.out.println(res); // { 4, 192.168.1.0, 192.168.1.3, [ 192.168.1.0, 192.168.1.1 ], [ 192.168.1.2, 192.168.1.3 ] }

        res = solution.solution("10.0.0.5/32", 3);
        System.out.println(res); // { 1, 10.0.0.5, 10.0.0.5, [ 10.0.0.5 ], [ 10.0.0.5 ] }

        res = solution.solution("0.0.0.0/0", 1);
        System.out.println(res); // { 4294967296, 0.0.0.0, 255.255.255.255, [ 0.0.0.0 ], [ 255.255.255.255 ] }

        res = solution.solution("1.2.3.4/24", 0);
        System.out.println(res); // { 256, 1.2.3.0, 1.2.3.255, [  ], [  ] }
    }
}

class Solution {
    public IpBlock solution(String cidr, int k) {
        if (cidr == null || cidr.length() == 0) return null;
        String[] arr = cidr.split("/");
        int maskIp = 0;
        String[] masks = arr[0].split("\\.");
        for (String mask : masks) {
            maskIp = (maskIp << 8) | Integer.parseInt(mask);
        }
        int d = 32 - Integer.parseInt(arr[1]);
        // 要注意这个边界问题，java里比特操作里 << 会模除32，所以 -1 << 32 就是 -1 << 0
        if (d == 32) {
            return startIpToBlock(0, -1, 1L << d, k);
        }
        int startIp = maskIp & (-1 << d);
        int endIp = startIp | (-1 ^ (-1 << d));
        return startIpToBlock(startIp, endIp, 1L << d, k);
    }

    private IpBlock startIpToBlock(int startIp, int endIp, long n, int k) {
        if (k == 0) {
            return new IpBlock(n, ipToStr(startIp), ipToStr(endIp), new String[0], new String[0]);
        }
        k = (int)(Math.min(n, (long)k));
        String[] firstK = startIpToArray(startIp, k);
        String[] lastK = startIpToArray(endIp - k + 1, k);
        return new IpBlock(n, firstK[0], lastK[k - 1], firstK, lastK);
    }

    private String[] startIpToArray(int startIp, int k) {
        String[] res = new String[k];
        for (int i = 0; i < k; i++) {
            res[i] = ipToStr(startIp + i);
        }
        return res;
    }

    private String ipToStr(int ip) {
        String[] res = new String[4];
        for (int i = 3; i >= 0; i--) {
            res[i] = String.valueOf(ip & 255);
            ip = ip >> 8;
        }
        return String.join(".", res);
    }

}

class IpBlock {
    long count;
    String startIp;
    String endIp;
    String[] firstK;
    String[] lastK;

    public IpBlock(long count, String startIp, String endIp, String[] firstK, String[] lastK) {
        this.count = count;
        this.startIp = startIp;
        this.endIp = endIp;
        this.firstK = firstK;
        this.lastK = lastK;
    }

    public String toString() {
        return String.format("{ %d, %s, %s, [ %s ], [ %s ] }", count, startIp, endIp, String.join(", ", firstK), String.join(", ", lastK));
    }
}