package guyuanjun.com.myappdemo.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtils {

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static void executeInThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    /**
     * 判断内容是否只是为数字或者字母
     * @param content
     * @return
     */
    public static boolean isDigitOrLetter(final String content){
        if (content == null || content.isEmpty()) return false;
        for (int i=0; i<content.length(); i++){
            if (!((content.charAt(i)>='a'&&content.charAt(i)<='z')
                    ||(content.charAt(i)>='A'&&content.charAt(i)<='Z')
                    ||(content.charAt(i)>='0'&&content.charAt(i)<='9'))){
                return false;
            }
        }
        return true;
    }

    /**
     * 正则表达式判断手机号
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //判断是否全是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 发送验证码
     * @param phone 手机号
     * @return
     */
    public static boolean sendVerifyCode(String phone){

        return false;
    }


    /**
     * 将java.util.Date 格式转换为字符串格式'yyyy-MM-dd HH:mm:ss'(24小时制)<br>
     * 如Sat May 11 17:24:21 CST 2002 to '2002-05-11 17:24:21'<br>
     * @param time Date 日期<br>
     * @return String   字符串<br>
     */
    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);
        return ctime;
    }

    /**
     * 将字符串格式'yyyy-MM-dd HH:mm:ss'(24小时制)转换为java.util.Date 格式<br>
     * 如Sat May 11 17:24:21 CST 2002 to '2002-05-11 17:24:21'<br>
     * @param time  字符串<br>
     * @return  Date 日期<br>
     */
    public static Date StringTodate(String time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date ctime = null;
        try {
            ctime = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ctime;
    }


    public static int compare_date(String DATE1, String DATE2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static void installApk(Context context, String storePath) {
        LogUtil.d("installApk", "install Apk path : " + storePath);
        File apkFile = new File(storePath);
        if (!apkFile.exists()) {
            return;
        }
        String command = "chmod 777 " + storePath;
        LogUtil.d("installApk", "command: " + command);
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(command);
        } catch (IOException e) {
            LogUtil.d("installApk", "chmod failed");
            e.printStackTrace();
        }
        LogUtil.d("installApk", "apkFile toString : " + apkFile.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        try {
            context.startActivity(intent);
        } catch (Throwable e) {
            LogUtil.d("installApk", "installApk catch throwable " + e.toString());
        }
    }

    public static String getApkDownloadPath(Context context) {
        LogUtil.d("installApk", "getApkDownloadPath : " + context.getFilesDir() + "/" + Constant.APK_NAME);
        return context.getFilesDir() + "/" + Constant.APK_NAME;
    }

    public static String getCachePath(Context context) {
        LogUtil.d("cache", "getCachePath : " + context.getCacheDir().getAbsolutePath());
        return context.getCacheDir().getAbsolutePath();
    }

}
