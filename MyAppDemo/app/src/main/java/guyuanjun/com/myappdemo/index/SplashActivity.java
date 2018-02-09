package guyuanjun.com.myappdemo.index;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import guyuanjun.com.myappdemo.MainActivity;
import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.advertisement.AdvertisementActivity;
import guyuanjun.com.myappdemo.utils.Constant;


/**
 * 功能：使用ViewPager实现初次进入应用时的引导页
 * <p>
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入引导activity；否，则进入MainActivity
 * (3)5s后执行(2)操作
 *
 * @author sz082093
 */

public class SplashActivity extends AppCompatActivity {



    //*************************************************
    // Handler:跳转至不同页面
    //*************************************************
    private final static int SWITCH_ADVERTISEMENTCTIVITY = 1000;
    private final static int SWITCH_GUIDACTIVITY = 1001;

    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SWITCH_ADVERTISEMENTCTIVITY:
                    Intent mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this, AdvertisementActivity.class);
                    SplashActivity.this.startActivity(mIntent);
                    SplashActivity.this.finish();
                    break;

                case SWITCH_GUIDACTIVITY:
                    mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this, GuideActivity.class);
                    SplashActivity.this.startActivity(mIntent);
                    SplashActivity.this.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //ManageActvity.getInstance().addActivity(SplashActivity.this);//添加Activity

        boolean mFirst = isFirstEnter(SplashActivity.this, SplashActivity.this.getClass().getName());
        Log.d("mFirst",String.valueOf(mFirst));
        if (mFirst)
            mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY, 0);
        else
            mHandler.sendEmptyMessageDelayed(SWITCH_ADVERTISEMENTCTIVITY, 0);
    }

    private boolean isFirstEnter(Context context, String className) {
        if (context == null || className == null || "".equalsIgnoreCase(className)) return false;
        String mResultStr = context.getSharedPreferences(Constant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(Constant.KEY_GUIDE_ACTIVITY, "");//取得所有类名
        if (mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)
                && event.getRepeatCount() == 0) {

            //ManageActvity.getInstance().closeActivity();//关掉activity
            Process.killProcess(Process.myPid());

            //SplashActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}