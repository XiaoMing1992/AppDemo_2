package guyuanjun.com.myappdemo;

/**
 * Created by Administrator on 2017/9/10 0010.
 */

public class Solution {
    public String addBinary(String a, String b) {
        String sum = "";
        char temp = '0';
        if (a.length() < b.length()) {
            String temp_str = a;
            a = b;
            b = temp_str;
        }
        int len_a = a.length(), len_b = b.length();

        for (int i = len_a - 1; i >= 0; i--) {
            if (i <= (len_a - 1 - len_b)) {
                if (len_a > len_b) {
                    if (temp == '1') {
                        if (a.charAt(i) == '0') {
                            sum += "1";
                            temp = '0';
                            if (i > 0) {
                                for(int m=i-1; m>=0; m--)
                                    sum += a.charAt(m);
                            }
                            break;
                        } else {
                            sum += "0";
                            temp = '1';
                            continue;
                        }
                    } else {
                        temp = '0';
                        //System.out.println(sum);
                        for(int m=i; m>=0; m--)
                            sum += a.charAt(m);
                        //System.out.println(sum);
                        break;
                    }
                }
            }

            if (a.charAt(i) == '1' && b.charAt(i-(len_a-len_b)) == '1') {
                if (temp == '1')
                    sum += "1";
                else
                    sum += "0";
                temp = '1';
            } else if (a.charAt(i) == '0' && b.charAt(i-(len_a-len_b)) == '0') {
                if (temp == '1')
                    sum += "1";
                else
                    sum += "0";
                temp = '0';
            } else if ((a.charAt(i) == '0' && b.charAt(i-(len_a-len_b)) == '1')
                    || (a.charAt(i) == '1' && b.charAt(i-(len_a-len_b)) == '0')) {
                if (temp == '1')
                    sum += "0";
                else{
                    sum += "1";
                    temp = '0';
                }
            }
        }


        if (temp == '1')
            sum += "1";

        sum = sum.substring(0, sum.lastIndexOf('1') + 1);
        String sumStr = "";
        for (int i = sum.length() - 1; i >= 0; i--) {
            sumStr += sum.charAt(i);
        }
        if(sumStr.isEmpty())
            sumStr = "0";
        return sumStr;
    }
}
