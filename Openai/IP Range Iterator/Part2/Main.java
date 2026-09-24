import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        List<String> res = solution.solution("192.168.0.255", 3);
        System.out.println(res); // [192.168.0.255, 192.168.0.254, 192.168.0.253]

        res = solution.solution("10.0.0.0", 2);
        System.out.println(res); // [10.0.0.0, 9.255.255.255]

        res = solution.solution("1.0.0.0", 3);
        System.out.println(res); // [1.0.0.0, 0.255.255.255, 0.255.255.254]

        res = solution.solution("0.0.0.0", 5);
        System.out.println(res); // [0.0.0.0]
    }
}

class Solution {
    public List<String> solution(String endIp, int n) {
        List<String> res = new ArrayList<>();
        if (n <= 0) return res;
        res.add(endIp);
        String[] starts = endIp.split("\\.");
        int ip = 0;
        for (String start : starts) {
            ip = (ip << 8) | (Integer.parseInt(start));
        }
        for (int i = 1; i < n; i++) {
            if (ip == 0) break;
            ip--;
            res.add(ipToStr(ip));
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