package guyuanjun.com.myappdemo.utils;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by HP on 2017-2-14.
 */

public class HttpDownload {

    private static volatile HttpDownload instance = null;
    private HttpDownload() {
    }

    public static HttpDownload getInstance(){
        if (instance == null){
            synchronized (HttpDownload.class){
                if (instance == null)
                {
                    instance = new HttpDownload();
                }
            }
        }
        return instance;
    }

    public boolean downloadFile(String urlStr, String directory, String fileName) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        FileOutputStream output = null;

        File directoryFile = new File(directory);
        if (!directoryFile.exists())
            directoryFile.mkdir();

        String pathName = directory + fileName;//文件存储路径

        File file = new File(pathName);
        if (!file.exists()) {
            try {
                file.createNewFile();//新建文件
            } catch (IOException e) {
                System.out.println("fail");
                e.printStackTrace();
            }
        } else {
            try {
                file.delete();
                System.out.println("delete over!!!!!!!!!!!!!!!!!");
                file.createNewFile();//新建文件
            } catch (IOException e) {
                System.out.println("fail");
                e.printStackTrace();
            }
        }

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            Log.d("HttpDownloadCode",""+conn.getResponseCode());

            if (conn.getResponseCode() == 200) {
                try {
                    InputStream input = conn.getInputStream();
                    //BufferedInputStream bis = new BufferedInputStream(input);
                    output = new FileOutputStream(file);
                    //BufferedOutputStream bos = new BufferedOutputStream(output);

                    //读取大文件
                    byte[] buffer = new byte[4 * 1024];
                    int len = 0;
                    while ((len = input.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }
                    output.flush();
                    input.close();

                    return true;

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("FileHttpDownload", "fail");
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        Log.d("FileHttpDownload", "success");
                    } catch (IOException e) {
                        Log.d("FileHttpDownload", "fail");
                        e.printStackTrace();
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public InputStream download(String urlStr, String directory, String fileName) {
       // FileOutputStream output = null;
        //String directory = "/data/data/" + mContext.getPackageName() + "/" + "pictures" + "/"; //目录
        File directoryFile = new File(directory);
        if (!directoryFile.exists())
            directoryFile.mkdir();

        String pathName = directory + fileName;//文件存储路径

        File file = new File(pathName);
        if (!file.exists()) {
            try {
                file.createNewFile();//新建文件
            } catch (IOException e) {
                System.out.println("fail");
                e.printStackTrace();
                return null;
            }
        } /*else {
            try {
                file.delete();
                file.createNewFile();//新建文件
            } catch (IOException e) {
                System.out.println("fail");
                e.printStackTrace();
            }
        }*/

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {
                try {
                    InputStream input = conn.getInputStream();
                    return input;
/*                    //BufferedInputStream bis = new BufferedInputStream(input);
                    output = new FileOutputStream(file);
                    //BufferedOutputStream bos = new BufferedOutputStream(output);

                    //读取大文件
                    byte[] buffer = new byte[4 * 1024];
                    int len = 0;
                    while ((len = input.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }
                    input.close();
                    output.flush();*/
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Log.d("PictureHttpDownload", "success");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

/*    private void download(){
        Log.d("download", "download");
        //if (dataList != null && dataList.size()!=0){
        //data = dataList.get(0);
        //if (data.getVersion_code() > Utils.getVersionCode(mContext)){
        String apkUrl = "http://ftp-apk.pconline.com.cn/a2318ae3f4ce2e39aac1bd605095dac8/pub/download/201010/pconline1493186630135.apk";
        final String path = MyUtils.getApkDownloadPath(mContext);
        Log.d("download", " path = "+path);

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        FileDownloadListener listener = new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                Log.d("download", " pending ");
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                int progress = soFarBytes * 100 / totalBytes;
                progressBean.setProgress(progress);
                //EventBus.getDefault().post(progressBean);
                progressBar.setProgress(progress);
                Log.d("download", "download progress "+ progress);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                progressBean.setCompleted(true);
                // EventBus.getDefault().post(progressBean);
                //PreferencesHelper.saveInt(PreferencesHelper.DOWNLOADED_VERSION, mNewestVersion);
                MyUtils.installApk(mContext, path);
                Log.d("download", "download completed ");
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                LogUtil.d(TAG, "download paused ");
                int progress = soFarBytes * 100 / totalBytes;
                progressBar.setProgress(progress);
            }

            @Override
            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                Log.d("download", "retryingTimes = "+retryingTimes);
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                Log.d("download", "FileDownloader error: " + e.toString());
                //Trace.Debug("FileDownloader error: " + e.toString());
                progressBean.setError(true);
                progressBean.setErrorType(ProgressBean.ERROR_DOWNLOAD);
                //EventBus.getDefault().post(progressBean);
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                Log.d("download", " warn ");
            }
        };

        FileDownloader.getImpl().create(apkUrl)
                .setPath(path)
                .setListener(listener).start();
    }*/
}
