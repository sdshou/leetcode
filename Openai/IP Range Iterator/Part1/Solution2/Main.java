import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        List<String> res = solution.solution("192.168.0.1", 3);
        System.out.println(res); // [192.168.0.1, 192.168.0.2, 192.168.0.3]

        res = solution.solution("192.168.0.255", 3);
        System.out.println(res); // [192.168.0.255, 192.168.1.0, 192.168.1.1]

        res = solution.solution("255.255.255.255", 3);
        System.out.println(res); // [255.255.255.255]

        res = solution.solution("0.0.0.0", 0);
        System.out.println(res); // []
    }
}

class Solution {
    public List<String> solution(String startIp, int n) {
        List<String> res = new ArrayList<>();
        if (n <= 0) return res;
        res.add(startIp);
        String[] ipStr = startIp.split("\\.");
        int ip = 0;
        for (String str : ipStr) {
            ip = (ip << 8) | Integer.parseInt(str);
        }
        for (int i = 1; i < n; i++) {
            ip += 1;
            if (ip == 0) break;
            res.add(ipToStr(ip));
        }
        
        return res;
    }

    private String ipToStr(int ip) {
        String[] strs = new String[4];
        for (int i = 3; i >= 0; i--) {
            strs[i] = String.valueOf(ip & 255);
            ip = ip >> 8;
        }
        return String.join(".", strs);
    }
}