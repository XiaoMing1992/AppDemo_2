package guyuanjun.com.myappdemo.utils;

import android.util.Log;

/**
 * Developer : xiongwenwei@aliyun.com
 * Create Time :
 * Function：
 */
public class LogUtil {

    public static void i(String str) {
        Log.i("默认标记", str);
    }

    public static void i(String tag, String str) {
        Log.i(tag, str);
    }

    public static void d(String tag, String str) {
        Log.d(tag, str);
    }

    public static void iFor(int forNumber, String str) {
        for (int i = 0; i < forNumber; i++) {
            Log.i("默认标记", str);
        }
    }
}
