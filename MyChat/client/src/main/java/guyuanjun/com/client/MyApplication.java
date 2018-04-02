package guyuanjun.com.client;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import guyuanjun.com.client.presenter.MyBroadcastReceiver;
import guyuanjun.com.client.view.StatusCode;

/**
 * Created by HP on 2018-4-2.
 */

public class MyApplication extends Application {

    private static volatile MyApplication application = null;
    //private MyBroadcastReceiver myBroadcastReceiver;

    public static MyApplication getApplication() {
        if (application == null){
            synchronized (MyApplication.class){
                if (application == null){
                    application = new MyApplication();
                }
            }
        }
        return application;
    }

//    private Context mContext;
//
//    public Handler appHandler = new Handler(Looper.getMainLooper()){
//        @Override
//        public void handleMessage(Message msg) {
//            //super.handleMessage(msg);
//            switch (msg.what){
//                case StatusCode.CONNECT_NETWORK_FAIL:
//                   // if (getApplication() != null && getApplication().getApplicationContext()!=null)
//                    Toast.makeText(mContext, "网络连接成功", Toast.LENGTH_SHORT).show();
//                    break;
//                case StatusCode.CONNECT_NETWORK_SUCCESS:
//                    //if (getApplication() != null && getApplication().getApplicationContext()!=null)
//                    Toast.makeText(mContext, "网络已经断开", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    };

    @Override
    public void onCreate() {
        super.onCreate();
        //myBroadcastReceiver = new MyBroadcastReceiver();
        //mContext = this.getApplicationContext();
        //registerNetworkListener(getApplication().getApplicationContext());
    }

//    public void registerNetworkListener(Context context) {
//        System.out.println("------- 注册广播");
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
//        filter.addAction("android.net.wifi.STATE_CHANGE");
//        context.registerReceiver(myBroadcastReceiver, filter);
//    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.out.println("------- 退出 onTerminate");
        //unregisterNetworkListener(getApplication().getApplicationContext());
    }

//    public void unregisterNetworkListener(Context context) {
//        System.out.println("------- 退出广播");
//        context.unregisterReceiver(myBroadcastReceiver);
//    }
}
