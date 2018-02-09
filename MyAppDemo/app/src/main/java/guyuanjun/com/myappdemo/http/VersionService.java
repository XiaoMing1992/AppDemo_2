package guyuanjun.com.myappdemo.http;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.MyUtils;

/**
 * Created by HP on 2017-6-2.
 */

public class VersionService extends Service {
    private final String TAG = "VersionService";
    //private Context mContext;
    public VersionService() {
    }

/*    public VersionService(Context context) {
        mContext = context;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        //System.out.println("Service is Created.");
        LogUtil.d(TAG, "Service is Created.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        String url = intent.getStringExtra("download_url");
        download(VersionService.this, url);
        flags = START_FLAG_REDELIVERY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service is Destroyed.");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

     private void download(final Context mContext, final String url){
        Log.d("download", "download url"+ url);
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
                //progressBean.setProgress(progress);
                //EventBus.getDefault().post(progressBean);
                //progressBar.setProgress(progress);
                Log.d("download", "download progress "+ progress);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                //progressBean.setCompleted(true);
                // EventBus.getDefault().post(progressBean);
                //PreferencesHelper.saveInt(PreferencesHelper.DOWNLOADED_VERSION, mNewestVersion);
                MyUtils.installApk(mContext, path);
                Log.d("download", "download completed ");
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                //LogUtil.d(TAG, "download paused ");
                Log.d("download", "download paused ");
                int progress = soFarBytes * 100 / totalBytes;
                //progressBar.setProgress(progress);
            }

            @Override
            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                Log.d("download", "retryingTimes = "+retryingTimes);
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                Log.d("download", "FileDownloader error: " + e.toString());
                //Trace.Debug("FileDownloader error: " + e.toString());
                //progressBean.setError(true);
               // progressBean.setErrorType(ProgressBean.ERROR_DOWNLOAD);
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
    }
}
