import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        var res = solution.solution("192.168.1.0/30");
        System.out.println(res); // { 192.168.1.0, 192.168.1.3, [ 192.168.1.0, 192.168.1.1, 192.168.1.2, 192.168.1.3 ] }

        res = solution.solution("172.16.5.9/29");
        System.out.println(res); // { 172.16.5.8, 172.16.5.15, [ 172.16.5.8, 172.16.5.9, 172.16.5.10, 172.16.5.11, 172.16.5.12, 172.16.5.13, 172.16.5.14, 172.16.5.15 ] }

        res = solution.solution("10.0.0.5/32");
        System.out.println(res); // { 10.0.0.5, 10.0.0.5, [ 10.0.0.5 ] }

        res = solution.solution("0.0.0.0/31");
        System.out.println(res); // { 0.0.0.0, 0.0.0.1, [ 0.0.0.0, 0.0.0.1 ] }
    }
}

class Solution {
    public IpBlock solution(String cidr) {
        if (cidr == null || cidr.length() == 0) return null;
        String[] arr = cidr.split("/");
        int maskIp = 0;
        String[] masks = arr[0].split("\\.");
        for (String mask : masks) {
            maskIp = (maskIp << 8) | Integer.parseInt(mask);
        }
        int d = 32 - Integer.parseInt(arr[1]);
        int startIp = maskIp & (-1 << d);
        return startIpToBlock(startIp, 1 << d);
    }

    private IpBlock startIpToBlock(int startIp, int n) {
        String[] all = new String[n];
        for (int i = 0; i < n; i++) {
            all[i] = ipToStr(startIp + i);
        }
        return new IpBlock(all[0], all[n - 1], all);
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
    String startIp;
    String endIp;
    String[] allAddresses;

    public IpBlock(String startIp, String endIp, String[] allAddresses) {
        this.startIp = startIp;
        this.endIp = endIp;
        this.allAddresses = allAddresses;
    }

    public String toString() {
        return String.format("{ %s, %s, [ %s ] }", startIp, endIp, String.join(", ", allAddresses));
    }
}