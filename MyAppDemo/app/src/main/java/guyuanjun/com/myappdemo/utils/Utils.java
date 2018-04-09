package guyuanjun.com.myappdemo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import android.util.Xml;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.model.AppVersion;
import guyuanjun.com.myappdemo.model.Pictureinfos;

import static android.content.Context.MODE_PRIVATE;
import static guyuanjun.com.myappdemo.utils.Constant.PACKET_NAME;

/**
 * Created by HP on 2017-2-14.
 */

public class Utils {
    private static volatile Utils instance = null;

    private Utils() {

    }

    public static Utils getInstance() {
        if (instance == null) {
            synchronized (Utils.class) {
                if (instance == null) {
                    instance = new Utils();
                }
            }
        }
        return instance;
    }

    public List<Pictureinfos> readXML(InputStream inputstream) {
        if (inputstream == null)
            return null;

        List<Pictureinfos> pictureinfos = null;
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(inputstream, "UTF-8");
            int eventCode = parser.getEventType();
            Pictureinfos pictureinfo = null;

            while (eventCode != XmlPullParser.END_DOCUMENT) {
                switch (eventCode) {
                    case XmlPullParser.START_DOCUMENT:
                        pictureinfos = new ArrayList<Pictureinfos>();
                        break;
                    case XmlPullParser.START_TAG:
                        if ("data".equals(parser.getName())) {
                            pictureinfo = new Pictureinfos();
                            //pictureinfo.setId(parser.getAttributeValue(0));
                        } else if (pictureinfo != null) {
                            if ("order".equals(parser.getName())) {
                                pictureinfo.setOrder(Integer.parseInt(parser.nextText()));
                            } else if ("image".equals(parser.getName())) {
                                pictureinfo.setImageUrl(parser.nextText());
                            } else if ("update_time".equals(parser.getName())) {
                                pictureinfo.setUpdateTime(Long.parseLong(parser.nextText()));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("data".equals(parser.getName()) && pictureinfo != null) {
                            pictureinfos.add(pictureinfo);
                            pictureinfo = null;
                        }
                        break;
                    default:
                        break;
                }
                eventCode = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            pictureinfos = null;
        } catch (IOException e) {
            e.printStackTrace();
            pictureinfos = null;
        }

        return pictureinfos;
    }

    public AppVersion readVersionXML(InputStream inputstream) {
        if (inputstream == null)
            return null;

        AppVersion appVersion = null;
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(inputstream, "UTF-8");
            int eventCode = parser.getEventType();

            while (eventCode != XmlPullParser.END_DOCUMENT) {
                switch (eventCode) {
                    case XmlPullParser.START_DOCUMENT:
                        //appVersion = new AppVersion();
                        break;
                    case XmlPullParser.START_TAG:
                        if ("data".equals(parser.getName())) {
                            appVersion = new AppVersion();
                            //pictureinfo = new Pictureinfos();
                            //pictureinfo.setId(parser.getAttributeValue(0));
                        } else if (appVersion != null) {
                            if ("version_code".equals(parser.getName())) {
                                appVersion.setVersionCode(Integer.parseInt(parser.nextText()));
                            } else if ("version_name".equals(parser.getName())) {
                                appVersion.setVersionName(parser.nextText());
                            } else if ("download_url".equals(parser.getName())) {
                                appVersion.setDownloadUrl(parser.nextText());
                            } else if ("update_time".equals(parser.getName())) {
                                appVersion.setUpdateTime(Long.parseLong(parser.nextText()));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("data".equals(parser.getName()) && appVersion != null) {
                            //pictureinfos.add(pictureinfo);
                            //pictureinfo = null;
                        }
                        break;
                    default:
                        break;
                }
                eventCode = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            //pictureinfos = null;
            appVersion = null;
        } catch (IOException e) {
            e.printStackTrace();
            appVersion = null;
            //pictureinfos = null;
        }

        return appVersion;
    }

    public InputStream getInputStream(String fileName) {
        FileInputStream is = null;
        try {
            File file = new File(fileName);
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return is;
    }

    public InputStream getHttpInputStream(String url_str) {
        InputStream is = null;
        try {
            URL url = new URL(url_str);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public AppVersion getVersionInfo(String url_str) {
        return readVersionXML(getHttpInputStream(url_str));
    }

    public boolean saveFile(String path, String fileName) {
        try {
            File foder = new File(path);
            if (!foder.exists()) {
                foder.mkdirs();
            }
            File myCaptureFile = new File(path, fileName);
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            //Bitmap bm = null;
            //bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Bitmap getPicture(String path) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false; //获取缩略图，如果设置为true，则获取原始图片
        options.inSampleSize = 2;
        try {
            bm = BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            e.printStackTrace();
            bm = null;
        }
        return bm;
    }

    public int compareTimeStamp(long timeStamp1, long timeStamp2) {
        if (timeStamp1 < timeStamp2)
            return -1;
        else if (timeStamp1 == timeStamp2)
            return 0;
        else
            return 1;
    }

    public void downloadPicture(Context context, String url, int loadingRes, int errorRes, ImageView imageView) {
        Log.d("Picasso", "--- Picasso ---");
        Picasso.with(context).load(url)
                .fit()
                .placeholder(loadingRes)
                .error(errorRes)
                .into(imageView);
    }

    public String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public String getSessionIdFromServer(final String requrl) {
        String sessionid = null;
        try {
            URL url = new URL(requrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            // 取得sessionid.
            String cookieval = con.getHeaderField("set-cookie");
            if (cookieval != null) {
                sessionid = cookieval.substring(0, cookieval.indexOf(";"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionid;
    }

    public void sendSessionIdToServer(final String requrl, final String sessionid) {
        try {
            URL url = new URL(requrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //注意，把存在本地的cookie值加在请求头上
            if (sessionid != null) {
                con.setRequestProperty("cookie", sessionid);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSessionToLocal(Context context, String cookieString) {
        SharedPreferences preference = context.getSharedPreferences("cookie", MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        //然后保存在本地
        editor.putString("jsessionid", cookieString);
        editor.commit();
    }

    public String getSessionFromLocal(Context context) {
        String cookieString = null;
        SharedPreferences preference = context.getSharedPreferences("cookie", MODE_PRIVATE);
        cookieString = preference.getString("jsessionid", null);
        return cookieString;
    }

    public long daysBetweenDate(Date start, Date end) {
        long time_start = start.getTime(); //获得毫秒型日期
        long time_end = end.getTime();//获得毫秒型日期
        long betweenDate = (time_end - time_start) / (1000 * 60 * 60 * 24); //计算间隔多少天，则除以毫秒到天的转换公式
        return betweenDate;
    }

    public long hoursBetweenDate(Date start, Date end) {
        long time_start = start.getTime(); //获得毫秒型日期
        long time_end = end.getTime();//获得毫秒型日期
        long betweenDate = (time_end - time_start) / (1000 * 60 * 60); //计算间隔多少小时，则除以毫秒到小时的转换公式
        return betweenDate;
    }

    public long minutesBetweenDate(Date start, Date end) {
        long time_start = start.getTime(); //获得毫秒型日期
        long time_end = end.getTime();//获得毫秒型日期
        long betweenDate = (time_end - time_start) / (1000 * 60); //计算间隔多少分钟，则除以毫秒到分钟的转换公式
        return betweenDate;
    }

    public void download() {

    }

    public String formatTime(long time) {
        time = time / 1000;
        String strHour = "" + (time / 3600);
        String strMinute = "" + time % 3600 / 60;
        String strSecond = "" + time % 3600 % 60;

        strHour = strHour.length() < 2 ? "0" + strHour : strHour;
        strMinute = strMinute.length() < 2 ? "0" + strMinute : strMinute;
        strSecond = strSecond.length() < 2 ? "0" + strSecond : strSecond;

        String strRsult = "";
        if (!strHour.equals("00")) {
            strRsult += strHour + ":";
        }
        if (!strMinute.equals("00")) {
            strRsult += strMinute + ":";
        }

        strRsult = strRsult.trim().length() == 0 ? "00:" : strRsult;
        strRsult += strSecond;

        return strRsult;
    }

    public String computeTime(Date time) {
        String timeStr = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeStr = format.format(time);
        return timeStr;
    }

    public String formatNum(int read_num) {
        String str = "";
        double num = read_num;
        if (read_num >= 1000) {
            str += num / 1000 + "万";
        } else {
            str += read_num;
        }
        return str;
    }

    public boolean has_login(Context context) {
        String sessionId = Utils.getInstance().getSessionFromLocal(context);
        if (sessionId == null) {
            return false;
        }
        return true;
    }

    /**
     * 判断SD卡是否存在
     */
    public boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建目录
     */
    public boolean createPath(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteAllFiles(File root) {
        try {
            File files[] = root.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) { // 判断是否为文件夹
                        deleteAllFiles(f);
                        try {
                            Log.d("delete", f.getAbsolutePath());
                            f.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (f.exists()) { // 判断是否存在
                            try {
                                Log.d("delete", f.getAbsolutePath());
                                f.delete();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean chooseWifi(Context context) {
        return dialog(context);
    }

    private boolean _continue = true;

    private boolean dialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("没有开启WiFi，确认继续吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                _continue = true;
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                _continue = false;
            }
        });
        builder.create().show();

        return _continue;
    }


    /**
     * 生成一个18位随机的字符串.生成规则如下:前面14位由日期时间生成,生成形式为"yyyyMMddHHmmss",
     * <p>
     * 后4位由伪随机数(0-9999,不足4位则补0)构成
     *
     * @return
     */

    public String randName() {
        String result = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        result += sdf.format(date);

        Double rand = Math.random() * 10000;
        if (rand < 10)
            result += "000" + rand.toString().substring(0, 1);
        else if (rand < 100)
            result += "00" + rand.toString().substring(0, 2);
        else if (rand < 1000)
            result += "0" + rand.toString().substring(0, 3);
        else
            result += rand.toString().substring(0, 4);
        return result;
    }

    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(PACKET_NAME, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "1.0.0";
        }
        return versionName;
    }

    public int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(PACKET_NAME, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionCode = 0;
        }
        return versionCode;
    }

    /**
     * 比较两个基本整数的大小
     * @param num1
     * @param num2
     * @return 如果num1 == num2，返回0；如果num1 > num2，返回1；如果num1 < num2，返回-1
     */
    public int compareNum(int num1, int num2) {
        if (num1 == num2) return 0;
        else if (num1 > num2) return 1;
        else return -1;
    }

    //根据传入的开始和结束日期来计算时间
    public String computeTime(Date start, Date end){
        String timeStr = "";
        long day = 0;
        long hour = 0;
        long minute = 0;
        long diff = end.getTime() - start.getTime();

        if (diff >= 1000 * 60 * 60 * 24){
            day = Utils.getInstance().daysBetweenDate(start, end);
            timeStr += day+"天";
        }
        if ((diff - day * 1000 * 60 * 60 * 24) >= 1000 * 60 * 60){
            hour = Utils.getInstance().hoursBetweenDate(start, end);
            timeStr += hour+"小时";
        }
        //if ((diff - day * 1000 * 60 * 60 * 24 - hour * 1000 * 60 * 60) >= 1000 * 60){
            minute = (diff - day * 1000 * 60 * 60 * 24 - hour * 1000 * 60 * 60)/(1000*60);
            timeStr += minute+"分钟";
        //}
        return timeStr;
    }

    //根据传入的开始和结束日期来计算时间
    public String computeTime(long time){
        String timeStr = "";
        long day = 0;
        long hour = 0;
        long minute = 0;

        if (time >= 1000 * 60 * 60 * 24){
            day = time/(1000 * 60 * 60 * 24);
            timeStr += day+"天";
        }
        if ((time - day * 1000 * 60 * 60 * 24) >= 1000 * 60 * 60){
            hour = (time - day * 1000 * 60 * 60 * 24)/(1000 * 60 * 60);
            timeStr += hour+"小时";
        }
        //if ((diff - day * 1000 * 60 * 60 * 24 - hour * 1000 * 60 * 60) >= 1000 * 60){
        minute = (time - day * 1000 * 60 * 60 * 24 - hour * 1000 * 60 * 60)/(1000*60);
        timeStr += minute+"分钟";
        //}
        return timeStr;
    }

    public String generateVerifyCode(){
        String verifyCode = "";
        for (int i=0;i<4;i++){
            verifyCode = verifyCode+""+(int)(Math.random()*10);
        }
        return verifyCode;
    }
}

