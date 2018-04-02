package guyuanjun.com.client.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import guyuanjun.com.client.MyApplication;
import guyuanjun.com.client.Utils;
import guyuanjun.com.client.view.StatusCode;

/**
 * Created by HP on 2018-4-2.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = MyBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                        Log.i(TAG, "当前WiFi连接可用 ");
                        System.out.println("当前WiFi连接可用 ");
                        Utils.setWifi(true);
                        Utils.setMobile(false);
                        Utils.setConnect(true);

                        //MyApplication.getApplication().appHandler.sendEmptyMessage(StatusCode.CONNECT_NETWORK_SUCCESS);
                        PresenterComp.handler.sendEmptyMessage(StatusCode.CONNECT_NETWORK_SUCCESS);
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        Log.i(TAG, "当前移动网络连接可用 ");
                        System.out.println("当前移动网络连接可用 ");
                        Utils.setWifi(false);
                        Utils.setMobile(true);
                        Utils.setConnect(true);
                        PresenterComp.handler.sendEmptyMessage(StatusCode.CONNECT_NETWORK_SUCCESS);
                        break;
                    default:
                        Log.i(TAG, "当前无网络连接可用 ");
                        System.out.println("当前无网络连接可用 ");
                        Utils.setWifi(false);
                        Utils.setMobile(false);
                        Utils.setConnect(false);
                        PresenterComp.handler.sendEmptyMessage(StatusCode.CONNECT_NETWORK_FAIL);
                        break;
                }
            } else {
                Log.i(TAG, "当前无网络连接可用 ");
                System.out.println("当前无网络连接可用 ");
                Utils.setWifi(false);
                Utils.setMobile(false);
                Utils.setConnect(false);
                PresenterComp.handler.sendEmptyMessage(StatusCode.CONNECT_NETWORK_FAIL);
            }
        } else {
            Log.i(TAG, "当前无网络连接可用 ");
            System.out.println("当前无网络连接可用 ");
            Utils.setWifi(false);
            Utils.setMobile(false);
            Utils.setConnect(false);
            PresenterComp.handler.sendEmptyMessage(StatusCode.CONNECT_NETWORK_FAIL);
        }
    }
}
