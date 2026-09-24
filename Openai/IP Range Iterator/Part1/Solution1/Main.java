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
        int[] ip = ipToNum(startIp);
        for (int i = 0; i < n - 1; i++) {
            int carry = 1;
            for (int j = 3; j >= 0; j--) {
                ip[j] += carry;
                if (ip[j] > 255) {
                    ip[j] = 0;
                    carry = 1;
                } else {
                    carry = 0;
                }
            }
            if (carry == 1) break;
            // to string
            res.add(ipToStr(ip));
        }
        return res;
    }

    private int[] ipToNum(String ip) {
        String[] ipStr = ip.split("\\.");
        int[] ipNum = new int[4];
        for (int i = 0; i < 4; i++) {
            ipNum[i] = Integer.parseInt(ipStr[i]);
        }
        return ipNum;
    }

    private String ipToStr(int[] ip) {
        StringBuilder sb = new StringBuilder();
        for (int p : ip) {
            sb.append(p).append(".");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}