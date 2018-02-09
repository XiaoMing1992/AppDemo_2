package guyuanjun.com.myappdemo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.AttentionInfoDaoOpe;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.dialog.LoginByPhone;
import guyuanjun.com.myappdemo.dialog.LoginDialog;
import guyuanjun.com.myappdemo.fragment.my.about.AboutActivity;
import guyuanjun.com.myappdemo.fragment.my.attention.MyAttention;
import guyuanjun.com.myappdemo.fragment.my.collect.MyCollect;
import guyuanjun.com.myappdemo.fragment.my.fan.MyFan;
import guyuanjun.com.myappdemo.fragment.my.help.HelpHomeActivity;
import guyuanjun.com.myappdemo.fragment.my.history.HistoryActivity;
import guyuanjun.com.myappdemo.fragment.my.personal.PersonalActivity;
import guyuanjun.com.myappdemo.fragment.my.set.SettingActivity;
import guyuanjun.com.myappdemo.fragment.my.store.MyStore;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.PrefUtils;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2016-9-1.
 */
public class MyFragment extends Fragment {

    private final String TAG = MyFragment.class.getSimpleName();

    public View myLayout;

    public LinearLayout history;
    public LinearLayout my_collect;
    public LinearLayout theme;

    public RelativeLayout person_info;
    public RelativeLayout my_setting;
    public RelativeLayout my_attention;
    public RelativeLayout my_fan;
    public RelativeLayout my_store;
    public RelativeLayout my_push;
    public RelativeLayout about;
    public RelativeLayout help;

    private TextView read_time;
    private ImageView head_icon;
    private ImageView tip_fan;
    private ImageView img_theme;
    private TextView nickname;
    private TextView theme_title;
    private TextView tip_attention;
    private TextView name_fan;
    private List<UserInfo> uses;

    private long id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myLayout = inflater.inflate(R.layout.my_layout, container, false);
        initView();
        initData();
        listener();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this); //注册
        return myLayout;
    }

    public void initView() {
        history = (LinearLayout) myLayout.findViewById(R.id.history);
        my_collect = (LinearLayout) myLayout.findViewById(R.id.my_collect);
        theme = (LinearLayout) myLayout.findViewById(R.id.theme);
        theme_title = (TextView) myLayout.findViewById(R.id.theme_title);

        person_info = (RelativeLayout) myLayout.findViewById(R.id.person_info);
        my_setting = (RelativeLayout) myLayout.findViewById(R.id.my_setting);
        my_attention = (RelativeLayout) myLayout.findViewById(R.id.my_attention);
        my_fan = (RelativeLayout) myLayout.findViewById(R.id.my_fan);
        my_push = (RelativeLayout) myLayout.findViewById(R.id.my_push);
        my_store = (RelativeLayout) myLayout.findViewById(R.id.my_store);
        help = (RelativeLayout) myLayout.findViewById(R.id.help);
        about = (RelativeLayout) myLayout.findViewById(R.id.about);

        read_time = (TextView) myLayout.findViewById(R.id.read_time);
        head_icon = (ImageView) myLayout.findViewById(R.id.head_icon);
        img_theme = (ImageView) myLayout.findViewById(R.id.img_theme);
        nickname = (TextView) myLayout.findViewById(R.id.nickname);
        tip_attention = (TextView) myLayout.findViewById(R.id.tip_attention);
        name_fan = (TextView) myLayout.findViewById(R.id.name_fan);
        tip_fan = (ImageView) myLayout.findViewById(R.id.tip_fan);
    }

    private static final int REFRESH = 0x00;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH:
                    computeReadTime();
                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                try {
                    mHandler.sendEmptyMessage(REFRESH);
                    Thread.sleep(1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void refreshTime(){
        new Thread(runnable).start();
    }

    private void computeReadTime(){
        //recordTime();
        //计算阅读时间
        //long now_time = System.currentTimeMillis();
        long total_time = PrefUtils.getLong(Constant.USER_READ_TIME_FILE_NAME, Constant.USER_READ_TIME_VALUE,
                getActivity());
        System.out.println("MainApplication computeReadTime === total_time ="+total_time);
        LogUtil.d(TAG, "total_time="+total_time);
        String time_str = Utils.getInstance().computeTime(total_time);
        read_time.setText("今日阅读"+time_str);
    }

    private void recordTime(){
        long end_time = System.currentTimeMillis();
        long total_time = PrefUtils.getLong(Constant.USER_READ_TIME_FILE_NAME, Constant.USER_READ_TIME_VALUE,
                getActivity())+(end_time - Constant.USER_READ_TIME_TEMP);
        PrefUtils.set(Constant.USER_READ_TIME_FILE_NAME, Constant.USER_READ_TIME_VALUE,
                total_time, getActivity());
        Constant.USER_READ_TIME_TEMP = end_time; //这个很重要，更新时间

        LogUtil.d("MainApplication", "=== total_time ="+total_time);
        System.out.println("MainApplication recordTime === total_time ="+total_time);
    }

    private void loadData() {
        id = PrefUtils.getLong(Constant.SAVE_USER_INFO_NAME, Constant.USER_ID_KEY, getActivity());
        LogUtil.d(TAG, "user id = " + id);

        uses = UserInfoDaoOpe.getInstance().query(getActivity(), id);
        for (int i = 0; i < uses.size(); i++) {
            Log.d(TAG, "head icon path =: " + uses.get(i).getHead_path());
            Log.d(TAG, "nickname =: " + uses.get(i).getNickname());
            try {
                Bitmap bitmap = Utils.getInstance().getPicture(uses.get(i).getHead_path());
                if (bitmap != null)
                    head_icon.setImageBitmap(bitmap);
                if (uses.get(i).getNickname() != null)
                    nickname.setText(uses.get(i).getNickname());
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        //加载关注的数量
        getAttention();
    }

    private void lostData() {
        head_icon.setImageResource(R.drawable.head_icon);
        nickname.setText("未登陆");
    }

    public void initData() {
        if (Utils.getInstance().has_login(getActivity())) {
            loadData();
        } else {
            lostData();
        }

        if (PrefUtils.getString("setting", "theme_type", getActivity()) == null) {
            theme_title.setText("白天模式");
            img_theme.setImageResource(R.drawable.day_theme);
            PrefUtils.set("setting", "theme_type", "白天模式", getActivity());
        } else {
            String type = PrefUtils.getString("setting", "theme_type", getActivity());
            theme_title.setText(type);
            if (type.equals("夜间模式"))
                img_theme.setImageResource(R.drawable.night_theme);
            else {
                img_theme.setImageResource(R.drawable.day_theme);
            }
        }
        refreshTime(); //更新阅读时间
        //computeReadTime();
    }

    @Override
    public void onResume() {
        super.onResume();
        //recordTime();
        //computeReadTime();
    }

    @Override
    public void onDetach() {
        mHandler.removeCallbacks(runnable);
        mHandler = null;
        System.out.println("=== onDetach MainApplication MyFragment ===");
        super.onDetach();
    }

    public void listener() {

        /**
         *
         */
        person_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断有没有已经登录
                if (!hasLoaded(getActivity())) {
                    return;
                }
                //如果已经登录，则直接跳转
                Intent intent = new Intent();
                intent.setClass(getActivity(), PersonalActivity.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });

        /**
         * 历史
         */
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 关注
         */
        my_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.getInstance().has_login(getActivity())) {
                    LoginDialog loginDialog = new LoginDialog(getActivity());
                    loginDialog.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyAttention.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });

        /**
         * 主题模式
         */
        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopwindow();
            }
        });

        /**
         * 粉丝
         */
        my_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.getInstance().has_login(getActivity())) {
                    LoginDialog loginDialog = new LoginDialog(getActivity());
                    loginDialog.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyFan.class);
                startActivity(intent);
            }
        });

        /**
         * 我的收藏
         */
        my_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.getInstance().has_login(getActivity())) {
                    LoginDialog loginDialog = new LoginDialog(getActivity());
                    loginDialog.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyCollect.class);
                startActivity(intent);
            }
        });

        /**
         * 消息通知
         */
        my_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                String sessionId = Utils.getInstance().getSessionFromLocal(getActivity());
                if (sessionId == null) {
                    Log.d(TAG, "sessionId = " + sessionId);
                    LoginDialog loginDialog = new LoginDialog(getActivity());
                    loginDialog.show();
                    return;
                }*/
                if (!Utils.getInstance().has_login(getActivity())) {
                    LoginDialog loginDialog = new LoginDialog(getActivity());
                    loginDialog.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 应用商城
         */
        my_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyStore.class);
                startActivity(intent);
            }
        });

        /**
         * 设置
         */
        my_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 关于我们
         */
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 帮助反馈
         */
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), HelpHomeActivity.class);
                startActivity(intent);

            }
        });
    }

    private boolean hasLoaded(Context context) {
        String sessionId = Utils.getInstance().getSessionFromLocal(context);
        if (sessionId == null) {
            Log.d(TAG, "sessionId = " + sessionId);
            LoginByPhone loginByPhone = new LoginByPhone(getActivity());
            loginByPhone.show();

//            WindowManager windowManager = getActivity().getWindowManager();
//            Display display = windowManager.getDefaultDisplay();
//            WindowManager.LayoutParams lp = loginByPhone.getWindow()
//                    .getAttributes();
//            lp.width = (int) (display.getWidth()); // 设置宽度
//            lp.height = (int) display.getHeight();
//            loginByPhone.getWindow().setAttributes(lp);
            return false;
        }
        return true;
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.select_theme, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.AnimationBottomFade);
        // 在底部显示
        window.showAtLocation(getActivity().findViewById(R.id.theme),
                Gravity.BOTTOM, 0, 0);

        LinearLayout night_mode = (LinearLayout) view.findViewById(R.id.night_mode);
        LinearLayout light_mode = (LinearLayout) view.findViewById(R.id.light_mode);
        LinearLayout cancel = (LinearLayout) view.findViewById(R.id.cancel);

        night_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PrefUtils.getString("setting", "theme_type", getActivity()) != null
                        && PrefUtils.getString("setting", "theme_type", getActivity()).equals("夜间模式")) {

                } else {
                    theme_title.setText("夜间模式");
                    img_theme.setImageResource(R.drawable.night_theme);
                    PrefUtils.set("setting", "theme_type", "夜间模式", getActivity());
                    changeTheme(); //通知改变主题
                }

                if (window != null && window.isShowing()) {
                    window.dismiss();
                }
            }
        });

        light_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PrefUtils.getString("setting", "theme_type", getActivity()) != null
                        && PrefUtils.getString("setting", "theme_type", getActivity()).equals("白天模式")) {

                } else {
                    theme_title.setText("白天模式");
                    img_theme.setImageResource(R.drawable.day_theme);
                    PrefUtils.set("setting", "theme_type", "白天模式", getActivity());
                    changeTheme();//通知改变主题
                }

                if (window != null && window.isShowing()) {
                    window.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }
            }
        });
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });

    }

    private void changeTheme() {
        EventBus.getDefault().post("change_theme");
    }

    //加载关注的数量
    private void getAttention() {
        int num = AttentionInfoDaoOpe.getInstance().queryAll(getActivity()) == null
                ? 0 : AttentionInfoDaoOpe.getInstance().queryAll(getActivity()).size();
        tip_attention.setText("" + num);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解注册EventBus
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if ("change_attention".equals(event)) { //关注发生了改变
            getAttention();
            Log.d(TAG, "change_attention");
        } else if ("logout".equals(event)) { //登录状态发生了改变
            changeLoginState(LOGOUT);
            Log.d(TAG, "logout");
        } else if ("login".equals(event)) { //登录状态发生了改变
            changeLoginState(LOGIN);
            Log.d(TAG, "login");
        }else if ("modify_head_nickname".equals(event)) { //修改头像和昵称
            modifyHeadAndNickName();
            Log.d(TAG, "modify_head_nickname");
        }
    }

    private void modifyHeadAndNickName(){
            id = PrefUtils.getLong(Constant.SAVE_USER_INFO_NAME, Constant.USER_ID_KEY, getActivity());
            LogUtil.d(TAG, "user id = " + id);

            uses = UserInfoDaoOpe.getInstance().query(getActivity(), id);
            for (int i = 0; i < uses.size(); i++) {
                Log.d(TAG, "head icon path =: " + uses.get(i).getHead_path());
                Log.d(TAG, "nickname =: " + uses.get(i).getNickname());
                try {
                    Bitmap bitmap = Utils.getInstance().getPicture(uses.get(i).getHead_path());
                    if (bitmap != null)
                        head_icon.setImageBitmap(bitmap);
                    if (uses.get(i).getNickname() != null)
                        nickname.setText(uses.get(i).getNickname());
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            }
    }

    private final int LOGIN = 0x00;
    private final int LOGOUT = 0x01;

    private void changeLoginState(int state) {
        switch (state) {
            case LOGIN:
                loadData();
                break;

            case LOGOUT:
                lostData();
                break;
        }
    }
}