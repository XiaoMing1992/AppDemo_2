package guyuanjun.com.myappdemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.dreamtobe.filedownloader.OkHttp3Connection;
import cn.jpush.android.api.JPushInterface;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.CrashHandler;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.PrefUtils;
import guyuanjun.com.myappdemo.utils.TencentCloud;
import okhttp3.OkHttpClient;

import com.tencent.stat.StatCrashCallback;
import com.tencent.stat.StatCrashReporter;

/**
 * Created by HP on 2017-3-17.
 */

public class MainApplication extends Application {
    private final String TAG = MainApplication.class.getSimpleName();

    public static MainApplication myApplication;
    public static Application getContext() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());

        System.out.println("=== onCreate MainApplication onCreate ===");
        LogUtil.d("MainApplication", "=== onCreate MainApplication onCreate ===");

        long start_time = System.currentTimeMillis();
        Constant.USER_READ_TIME_TEMP = start_time;
        System.out.println("=== onCreate MainApplication USER_READ_TIME_TEMP ==="+Constant.USER_READ_TIME_TEMP);
        setReadTime();//设置阅读时间

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        // Enable the okHttp3 connection with the customized okHttp client builder.
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(20_000, TimeUnit.MILLISECONDS); // customize the value of the connect timeout.
        builder.readTimeout(20_000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(20_000, TimeUnit.MILLISECONDS);
        FileDownloader.init(getApplicationContext(), new DownloadMgrInitialParams.InitCustomMaker()
                .connectionCreator(new OkHttp3Connection.Creator(builder)));

        //在这里设置异常处理
        CrashHandler crashHandler = CrashHandler.getsInstance();
        crashHandler.init(this);

        initTencentCloud();
        //初始化阿里百川热更新
        initALiBaiChuan();

        new Thread(runnable).start();
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                try {
                    if (MyLifecycleHandler.isApplicationInForeground()) { //应用在前台
                        System.out.println("MainApplication ApplicationInForeground()");
                        recordTime();
                    }else{
                        long start_time = System.currentTimeMillis();
                        Constant.USER_READ_TIME_TEMP = start_time;
                        System.out.println("=== onCreate MainApplication USER_READ_TIME_TEMP ==="+Constant.USER_READ_TIME_TEMP);
                    }
                    Thread.sleep(1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void setReadTime() {
        long check_time = PrefUtils.getLong(Constant.USER_READ_TIME_FILE_NAME, Constant.USER_READ_TIME_START,
                this);
        LogUtil.d("setReadTime", "USER_READ_TIME_TEMP = "+Constant.USER_READ_TIME_TEMP);
        LogUtil.d("setReadTime", "check_time = "+check_time);
        LogUtil.d("setReadTime", "USER_READ_TIME_TEMP/(1000 * 60 * 60 * 24) = "+Constant.USER_READ_TIME_TEMP/(1000 * 60 * 60 * 24));
        LogUtil.d("setReadTime", "check_time/(1000 * 60 * 60 * 24) = "+check_time/(1000 * 60 * 60 * 24));

        Date date1 = new Date(Constant.USER_READ_TIME_TEMP);
        Date date2 = new Date(check_time);

        if (date1.getDay() > date2.getDay()){
        //if ((Constant.USER_READ_TIME_TEMP - check_time) / (1000 * 60 * 60 * 24) > 0) {
            //开始记录阅读时间
            PrefUtils.set(Constant.USER_READ_TIME_FILE_NAME, Constant.USER_READ_TIME_START,
                    Constant.USER_READ_TIME_TEMP, this);
            PrefUtils.set(Constant.USER_READ_TIME_FILE_NAME, Constant.USER_READ_TIME_VALUE,
                    0L, this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        LogUtil.d("MainApplication", "=== attachBaseContext MainApplication onCreate ===");
    }

    private void initTencentCloud(){
        // androidManifest.xml指定本activity最先启动
        // 因此，MTA的初始化工作需要在本onCreate中进行
        // 在startStatService之前调用StatConfig配置类接口，使得MTA配置及时生
        // 初始化并启动MTA
        try {
            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
            StatService.startStatService(this, TencentCloud.APP_KEY,
                    com.tencent.stat.common.StatConstants.VERSION);
            LogUtil.d("MTA","MTA初始化成功");

            // 这个是开启Mta的统计功能
            StatService.registerActivityLifecycleCallbacks(this);

            StatCrashReporter crashReporter = StatCrashReporter.getStatCrashReporter(this);
            // 开启异常时的实时上报
            crashReporter.setEnableInstantReporting(true);
            // 开启java异常捕获
            crashReporter.setJavaCrashHandlerStatus(true);
            // 开启Native c/c++，即so的异常捕获
            // 请根据需要添加，记得so文件
            crashReporter.setJniNativeCrashStatus(true);

            // crash时的回调，业务可根据需要自选决定是否添加
            crashReporter.addCrashCallback(new StatCrashCallback() {
                @Override
                public void onJniNativeCrash(String tombstoneString) {
                    // native dump内容，包含异常信号、进程、线程、寄存器、堆栈等信息
                    // 具体请参考：Android原生的tombstone文件格式
                    LogUtil.d(TAG, "MTA StatCrashCallback onJniNativeCrash:\n" + tombstoneString);
                }

                @Override
                public void onJavaCrash(Thread thread, Throwable ex) {
                    //thread:crash线程信息
                    // ex:crash堆栈
                    LogUtil.d(TAG, "MTA StatCrashCallback onJavaCrash:\n"+ex);
                }
            });
        } catch (MtaSDkException e) {
            // MTA初始化失败
            LogUtil.d("MTA","MTA初始化失败"+e);
        }
    }

    @Override
    public void onTerminate() {
        LogUtil.d("MainApplication", "=== onTerminate MainApplication onTerminate ===");
        runnable = null;
        //recordTime();
        System.out.println("=== onTerminate MainApplication onTerminate ===");
        super.onTerminate();
    }

    private void recordTime(){
        long end_time = System.currentTimeMillis();
        long total_time = PrefUtils.getLong(Constant.USER_READ_TIME_FILE_NAME, Constant.USER_READ_TIME_VALUE,
                this)+(end_time - Constant.USER_READ_TIME_TEMP);
        PrefUtils.set(Constant.USER_READ_TIME_FILE_NAME, Constant.USER_READ_TIME_VALUE,
                total_time, this);
        Constant.USER_READ_TIME_TEMP = end_time; //这个很重要，更新时间

        LogUtil.d("MainApplication", "=== total_time ="+total_time);
        System.out.println("MainApplication recordTime === total_time ="+total_time);
    }

    private void initALiBaiChuan(){
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }

//        HotFixManager.getInstance().setContext(this)
//                .setAppVersion(appVersion) //版本号
//                .setAppId("24745610-1") //开发者平台创建应用的appId
//                .setAesKey(null)//如果对补丁进行了Aes加密，这里就要填上，具体见开发文档
//                .setSupportHotpatch(true)
//                .setEnableDebug(true)
//                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
//                    @Override
//                    public void onload(final int mode, final int code, final String info, final int handlePatchVersion) {
//                        // 补丁加载回调通知
//                        if (code == PatchStatusCode.CODE_SUCCESS_LOAD) {
//                            // TODO: 10/24/16 表明补丁加载成功
//                            LogUtil.d("hofix","补丁加载成功"+code);
//                        } else if (code == PatchStatusCode.CODE_ERROR_NEEDRESTART) {
//                            // TODO: 10/24/16 表明新补丁生效需要重启. 业务方可自行实现逻辑, 提示用户或者强制重启, 建议: 用户可以监听进入后台事件, 然后应用自杀
//                            LogUtil.d("hofix","补丁生效需要重启"+code);
//                        } else if (code == PatchStatusCode.CODE_ERROR_INNERENGINEFAIL) {
//                            // 内部引擎加载异常, 推荐此时清空本地补丁, 但是不清空本地版本号, 防止失败补丁重复加载
//                            //HotFixManager.getInstance().cleanPatches(false);
//                            LogUtil.d("hofix","内部引擎加载异常"+code);
//                        } else {
//                            // TODO: 10/25/16 其它错误信息, 查看PatchStatusCode类说明
//                            LogUtil.d("hofix","补丁code"+code);
//                        }
//
//                    }
//                }).initialize();

        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            LogUtil.d("hofix","补丁加载成功"+code);
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                            LogUtil.d("hofix","补丁生效需要重启"+code);
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            SophixManager.getInstance().cleanPatches();
                            LogUtil.d("hofix","内部引擎加载异常"+code);
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            LogUtil.d("hofix","补丁code"+code);
                        }
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
