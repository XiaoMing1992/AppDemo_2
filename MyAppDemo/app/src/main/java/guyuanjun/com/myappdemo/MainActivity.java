package guyuanjun.com.myappdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import guyuanjun.com.myappdemo.fragment.VedioFragment;
import guyuanjun.com.myappdemo.fragment.WeitoutiaoFragment;
import guyuanjun.com.myappdemo.fragment.MyFragment;
import guyuanjun.com.myappdemo.fragment.HomeFragment;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.PrefUtils;
import guyuanjun.com.myappdemo.utils.Utils;
import guyuanjun.com.myappdemo.vedio_module.util.NetWorkUtil;

/**
 * a、比如：我在FragmentA中的EditText填了一些数据，当切换到FragmentB时，如果希望会到A还能看到数据，
 * 则适合你的就是hide和show；也就是说，希望保留用户操作的面板，你可以使用hide和show，当然了不
 * 要使劲在那new实例，进行下非null判断。
 * b、再比如：我不希望保留用户操作，你可以使用remove()，然后add()；或者使用replace()这个和remove,
 * add是相同的效果。
 * c、remove和detach有一点细微的区别，在不考虑回退栈的情况下，remove会销毁整个Fragment实例，而detach则
 * 只是销毁其视图结构，实例并不会被销毁。那么二者怎么取舍使用呢？如果你的当前Activity一直存在，
 * 那么在不希望保留用户操作的时候，你可以优先使用detach。
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = MainActivity.this;
    int flag = 0;

    /**
     * 用来计算退出所需要点击返回键次数
     */
    private long firstTime = 0;

    /**
     * 用于展示消息的Fragment
     */
    private WeitoutiaoFragment weitoutiaoFragment;

    /**
     * 用于展示视频的Fragment
     */
    private VedioFragment vedioFragment;

    /**
     * 用于展示动态的Fragment
     */
    private HomeFragment homeFragment;

    /**
     * 用于展示设置的Fragment
     */
    private MyFragment myFragment;

    /**
     * 微头条界面布局
     */
    private View weitoutiaoLayout;

    /**
     * 联系人界面布局
     */
    private View vedioLayout;

    /**
     * 主界面布局
     */
    private View homeLayout;

    /**
     * 我的界面布局
     */
    private View myLayout;

    /**
     * 在Tab布局上显示消息图标的控件
     */
    private ImageView weitoutiaoImage;

    /**
     * 在Tab布局上显示视频图标的控件
     */
    private ImageView vedioImage;

    /**
     * 在Tab布局上显示动态图标的控件
     */
    private ImageView homeImage;

    /**
     * 在Tab布局上显示设置图标的控件
     */
    private ImageView myImage;

    /**
     * 在Tab布局上显示消息图标的控件
     */
    private TextView weitoutiaoText;

    /**
     * 在Tab布局上显示联系人图标的控件
     */
    private TextView vedioText;

    /**
     * 在Tab布局上显示动态图标的控件
     */
    private TextView homeText;

    /**
     * 在Tab布局上显示设置图标的控件
     */
    private TextView myText;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    //开启一个Fragment事务
    FragmentTransaction fragmentTransaction;

    private final int LOADDATA_FAIL = 0x00;
    private final int LOADDATA_OK = 0x01;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADDATA_FAIL:

                    break;
                case LOADDATA_OK:

                    fragmentTransaction.commit();
                    break;
            }
        }
    };

    private int tabId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置主题
        setThemeType();
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            if (savedInstanceState.getInt("fragment_id", -1) == -1) {
                tabId = 0;
            } else {
                tabId = savedInstanceState.getInt("fragment_id", -1);
            }
            Log.d("fragment_id", "" + savedInstanceState.getInt("fragment_id", -1));
        }

        EventBus.getDefault().register(this); //注册

        //初始化布局
        initViews();
        fragmentManager = getFragmentManager();

        if (savedInstanceState != null)
            setTabSelection(tabId);
        else {
            Intent intent = getIntent();
            int id = intent.getIntExtra("fragment_type", 0);
            //第一次启动时选中第0个tab
            setTabSelection(id);
            tabId = id;
        }


    }

    private final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    private void initViews() {
        weitoutiaoLayout = findViewById(R.id.weitoutiao_layout);
        vedioLayout = findViewById(R.id.vedio_layout);
        homeLayout = findViewById(R.id.home_layout);
        myLayout = findViewById(R.id.my_layout);

        weitoutiaoImage = (ImageView) findViewById(R.id.weitoutiao_img);
        vedioImage = (ImageView) findViewById(R.id.vedio_img);
        homeImage = (ImageView) findViewById(R.id.home_image);
        myImage = (ImageView) findViewById(R.id.my_image);

        weitoutiaoText = (TextView) findViewById(R.id.weitoutiao_tv);
        vedioText = (TextView) findViewById(R.id.vedio_tv);
        homeText = (TextView) findViewById(R.id.home_text);
        myText = (TextView) findViewById(R.id.my_text);

        weitoutiaoLayout.setOnClickListener(this);
        vedioLayout.setOnClickListener(this);
        homeLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);

        boolean flow_mode = PrefUtils.getBoolean("setting", "flow_mode", mContext); //是否为省流量模式
        if (flow_mode) {
            if (NetWorkUtil.isWifiConnected(mContext)) {
                Constant.mCanGetBitmapFromNetWork = true;
                //Constant.mCanGetBitmapFromNetWork = Utils.getInstance().chooseWifi(mContext);
                Log.d("NetWork", "" + Constant.mCanGetBitmapFromNetWork);
            } else {
                Constant.mCanGetBitmapFromNetWork = false;
            }
            Toast.makeText(mContext, "当前处于省流量模式", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_layout:
                tabId = 0;
                //当点击了消息tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.vedio_layout:
                tabId = 1;
                //当点击了视频tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.weitoutiao_layout:
                tabId = 2;
                //当点击了动态tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.my_layout:
                tabId = 3;
                //当点击了我的tab时，选中第4个tab
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置
     */
    private void setTabSelection(int index) {
        //每次选中之前先清除掉上次的选中状态
        clearSelection();

        fragmentTransaction = fragmentManager.beginTransaction();
        //先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
                //当点击了动态tab时，改变控件的图片和文字颜色
                homeImage.setImageResource(R.drawable.home_selected);
                homeText.setTextColor(Color.WHITE);
                if (homeFragment == null) {
                    //如果newsFragment为空，则创建一个并添加到界面上
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            homeFragment = new HomeFragment();
                            fragmentTransaction.add(R.id.content, homeFragment);
                            mHandler.sendEmptyMessage(LOADDATA_OK);
                        }
                    }).start();
                } else {
                    //如果newsFragment不为空，则直接显示出来
                    fragmentTransaction.show(homeFragment);
                    fragmentTransaction.commit();
                }
                break;

            case 1:
                //当点击了视频tab时，改变控件的图片和文字颜色
                vedioImage.setImageResource(R.drawable.vedio_selected);
                vedioText.setTextColor(Color.WHITE);
                if (vedioFragment == null) {
                    //如果contactsFragment为空，则创建一个并添加到界面上
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            vedioFragment = new VedioFragment();
                            fragmentTransaction.add(R.id.content, vedioFragment);
                            mHandler.sendEmptyMessage(LOADDATA_OK);
                        }
                    }).start();
                } else {
                    //如果contactsFragment不为空，则直接显示出来
                    fragmentTransaction.show(vedioFragment);
                    fragmentTransaction.commit();
                }
                break;

            case 2:
                //当点击了消息tab时，改变控件的图片和文字颜色
                weitoutiaoImage.setImageResource(R.drawable.weitoutiao_selected);
                weitoutiaoText.setTextColor(Color.WHITE);
                if (weitoutiaoFragment == null) {
                    //如果MessageFragment为空，则创建一个并添加到界面上
                    weitoutiaoFragment = new WeitoutiaoFragment();
                    fragmentTransaction.add(R.id.content, weitoutiaoFragment);
                } else {
                    //如果MessageFragment不为空，则直接显示出来
                    fragmentTransaction.show(weitoutiaoFragment);
                }
                fragmentTransaction.commit();
                break;

            case 3:
            default:
                //当点击了设置tab时，改变控件的图片和文字颜色
                myImage.setImageResource(R.drawable.my_selected);
                myText.setTextColor(Color.WHITE);
                if (myFragment == null) {
                    //如果settingFragment为空，则创建一个并添加到界面上
                    myFragment = new MyFragment();
                    fragmentTransaction.add(R.id.content, myFragment);
                } else {
                    //如果settingFragment不为空，则直接显示出来
                    fragmentTransaction.show(myFragment);
                }
                fragmentTransaction.commit();
                break;
        }

    }

    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection() {
        weitoutiaoImage.setImageResource(R.drawable.weitoutiao_unselected);
        weitoutiaoText.setTextColor(Color.parseColor("#82858b"));
        vedioImage.setImageResource(R.drawable.vedio_unselected);
        vedioText.setTextColor(Color.parseColor("#82858b"));
        homeImage.setImageResource(R.drawable.home_unselected);
        homeText.setTextColor(Color.parseColor("#82858b"));
        myImage.setImageResource(R.drawable.my_unselected);
        myText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都设置为隐藏状态。
     *
     * @param fragmentTransaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (weitoutiaoFragment != null) {
            fragmentTransaction.hide(weitoutiaoFragment);
        }
        if (vedioFragment != null) {
            fragmentTransaction.hide(vedioFragment);
        }
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (myFragment != null) {
            fragmentTransaction.hide(myFragment);
        }
    }

    private void recordTime(){
        long end_time = System.currentTimeMillis();
        long total_time = PrefUtils.getLong(Constant.USER_READ_TIME_FILE_NAME, Constant.USER_READ_TIME_VALUE,
                mContext)+(end_time - Constant.USER_READ_TIME_TEMP);
        PrefUtils.set(Constant.USER_READ_TIME_FILE_NAME, Constant.USER_READ_TIME_VALUE,
                total_time, mContext);
        Constant.USER_READ_TIME_TEMP = end_time; //这个很重要，更新时间
        LogUtil.d("MainApplication", "=== total_time ="+total_time);
        System.out.println("MainApplication === total_time ="+total_time);
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
            firstTime = secondTime;
        } else {
            recordTime();
            Process.killProcess(Process.myPid());

            //System.exit(0);
/*            int pid = android.os.Process.myPid();
            Log.d("process", "pid = "+pid);
            String command = "kill -9 "+ pid;
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
        }
    }

    private void setThemeType() {
        String theme_type = PrefUtils.getString("setting", "theme_type", mContext);
        if (theme_type == null) {
            setTheme(R.style.SwitchTheme2);
        } else {
            if (theme_type.equals("夜间模式"))
                setTheme(R.style.SwitchTheme1);
            else
                setTheme(R.style.SwitchTheme2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if ("change_theme".equals(event)) { //主题发生了改变
            Log.d("ShowMsgActivity", "onEventMainThread收到了消息：" + event);

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(weitoutiaoFragment);
            //fragmentTransaction.commit();

            //fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(vedioFragment);
            //fragmentTransaction.commit();

            //fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(homeFragment);
            //fragmentTransaction.commit();

            //fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(myFragment);
            fragmentTransaction.commit();
            //hideFragments(fragmentTransaction);

            MainActivity.this.recreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("fragment_id", tabId);
        super.onSaveInstanceState(outState);
    }
}

