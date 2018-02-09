package guyuanjun.com.myappdemo.utils;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.bean.WeitoutiaoInfo;
import guyuanjun.com.myappdemo.db.NewsItemInfoDaoOpe;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.db.WeitoutiaoInfoDaoOpe;

/**
 * Created by HP on 2017-4-8.
 */

public class UploadUtil {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码

    /**
     * 上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    public static int uploadFile(File file, String RequestURL) {
        int res = 0;
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            if (file != null) {
                /**
                 * 当文件不为空时执行上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名
                 */

                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[4*1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                res = conn.getResponseCode();
                Log.e(TAG, "response code:" + res);
                if (res == 200) {
                    Log.e(TAG, "request success");
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    Log.e(TAG, "result : " + result);
                } else {
                    Log.e(TAG, "request error");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int uploadToLocal(Context context, File file, long id){
        File fileCache = context.getCacheDir();
        Log.d(TAG,"fileCache "+fileCache);

        if (fileCache != null && fileCache.exists()){
            String name = Utils.getInstance().hashKeyFormUrl(file.getName());
            File picFile = new File(fileCache, name);
            if (!picFile.exists()) {
                try {
                    picFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileInputStream input = null;
            FileOutputStream out = null;
            try {
                input = new FileInputStream(file);
                out = new FileOutputStream(picFile);
                byte[] bytes = new byte[4*1024];
                int len = 0;
                while ((len = input.read(bytes))!=-1){
                    out.write(bytes,0, len);
                }
                out.flush();

                List<UserInfo> uses = UserInfoDaoOpe.getInstance().query(context, id);
                for (int i=0; i<uses.size(); i++){
                    Log.d(TAG, "head icon path 1=: "+picFile.getAbsolutePath());
                    uses.get(i).setHead_path(picFile.getAbsolutePath());
                    UserInfoDaoOpe.getInstance().save(context, uses.get(i));//保存更新
                    Log.d(TAG, "head icon path 2=: "+uses.get(i).getHead_path());
                }

                return 200;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (input != null){
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null){
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else
            Log.d(TAG,"fail");
        return 505;
    }

    public static int uploadToServer(Context context, File file, NewsItemInfo newsItemInfo){
        File picturePath = new File(Constant.SAVE_PICTURE_URL);
        if (!picturePath.exists())
            picturePath.mkdir();
        Log.d(TAG,"picturePath "+picturePath.getAbsolutePath());

        if (picturePath != null && picturePath.exists()){
            String name = Utils.getInstance().hashKeyFormUrl(file.getName());
            File picFile = new File(picturePath, name);
            if (!picFile.exists()) {
                try {
                    picFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileInputStream input = null;
            FileOutputStream out = null;
            try {
                input = new FileInputStream(file);
                out = new FileOutputStream(picFile);
                byte[] bytes = new byte[4*1024];
                int len = 0;
                while ((len = input.read(bytes))!=-1){
                    out.write(bytes,0, len);
                }
                out.flush();

                //NewsItemInfo newsItemInfo = new NewsItemInfo(user_id, _title, _where, date, flag);
                newsItemInfo.setImg_path(picFile.getAbsolutePath());
                NewsItemInfoDaoOpe.getInstance().insertData(context, newsItemInfo);

/*                List<NewsItemInfo> news = NewsItemInfoDaoOpe.getInstance().query(context, id);
                for (int i=0; i<news.size(); i++){
                    Log.d(TAG, "picture path 1=: "+picFile.getAbsolutePath());
                    news.get(i).setImg_path(picFile.getAbsolutePath());
                    NewsItemInfoDaoOpe.getInstance().save(context, news.get(i)); //保存修改
                    Log.d(TAG, "picture path 2=: "+news.get(i).getImg_path());
                }*/

                return 200;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (input != null){
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null){
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else
            Log.d(TAG,"fail");
        return 505;
    }

    public static int uploadToWeitoutiaoServer(Context context, File file, long id){
        File picturePath = new File(Constant.SAVE_PICTURE_URL);
        if (!picturePath.exists())
            picturePath.mkdir();
        Log.d(TAG,"picturePath "+picturePath.getAbsolutePath());

        if (picturePath != null && picturePath.exists()){
            String name = Utils.getInstance().hashKeyFormUrl(file.getName());
            File picFile = new File(picturePath, name);
            if (!picFile.exists()) {
                try {
                    picFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileInputStream input = null;
            FileOutputStream out = null;
            try {
                input = new FileInputStream(file);
                out = new FileOutputStream(picFile);
                byte[] bytes = new byte[4*1024];
                int len = 0;
                while ((len = input.read(bytes))!=-1){
                    out.write(bytes,0, len);
                }
                out.flush();



                return 200;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (input != null){
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null){
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else
            Log.d(TAG,"fail");
        return 505;
    }
}