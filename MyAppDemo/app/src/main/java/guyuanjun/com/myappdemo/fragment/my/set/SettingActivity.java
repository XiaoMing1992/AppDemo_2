package guyuanjun.com.myappdemo.fragment.my.set;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import guyuanjun.com.myappdemo.MainActivity;
import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.item.ThemeAdapter;
import guyuanjun.com.myappdemo.user.LoginActivity;
import guyuanjun.com.myappdemo.user.RegisterFirst;
import guyuanjun.com.myappdemo.user.RegisterSecond;
import guyuanjun.com.myappdemo.utils.PrefUtils;
import guyuanjun.com.myappdemo.utils.Utils;

import static guyuanjun.com.myappdemo.utils.Constant.MY_FRAGMENT;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";
    private Context mContext = SettingActivity.this;

    //private TextView tv_theme;

    private ImageView back;
    private ToggleButton flow_mode;
    private ToggleButton push_msg;
    private ToggleButton clear_cache;
    //private RelativeLayout theme;
    private ListView theme_lv;


    private boolean flag_flow_mode;
    private boolean flag_push_msg;
    private boolean flag_clear_cache;

    private PopupWindow popupWindow;
    private PopupWindow window;

    private List<String> themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        clickListener();
        initData();
    }

    private void initView(){
        //tv_theme = (TextView)findViewById(R.id.tv_theme);
        //theme = (RelativeLayout)findViewById(R.id.theme);
        back = (ImageView)findViewById(R.id.back_setting);
        flow_mode = (ToggleButton) findViewById(R.id.flow_mode);
        push_msg = (ToggleButton) findViewById(R.id.push_msg);
        clear_cache = (ToggleButton) findViewById(R.id.clear_cache);

    }

    private void clickListener(){
/*        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // initPopupWindow();
                showPopwindow();
            }
        });*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, MainActivity.class);
                intent.putExtra("fragment_type", MY_FRAGMENT);
                startActivity(intent);
            }
        });

        flow_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("flow_mode",  String.valueOf(b));
                PrefUtils.set("setting", "flow_mode", b, mContext);
            }
        });

        push_msg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("push_msg",  String.valueOf(b));
                PrefUtils.set("setting", "push_msg", b, mContext);
            }
        });

        clear_cache.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("clear_cache",  String.valueOf(b));
                PrefUtils.set("setting", "clear_cache", b, mContext);
                if (!b){
                    test();
                }else {
                    clearCache();
                }
            }
        });
    }

    private void initData(){
        flag_flow_mode = PrefUtils.getBoolean("setting", "flow_mode", mContext);
        flag_push_msg = PrefUtils.getBoolean("setting", "push_msg", mContext);
        flag_clear_cache = PrefUtils.getBoolean("setting", "clear_cache", mContext);

        flow_mode.setChecked(flag_flow_mode);
        push_msg.setChecked(flag_push_msg);
        clear_cache.setChecked(flag_clear_cache);

/*        if (PrefUtils.getString("setting", "theme_type", mContext) == null) {
            tv_theme.setText("夜间模式");
            PrefUtils.set("setting", "theme_type", "夜间模式", mContext);
        }else {
            tv_theme.setText(PrefUtils.getString("setting", "theme_type", mContext));
        }*/
    }

    private void getThemeData(){
        themes = new ArrayList<String>();
        themes.add("夜间模式");
        themes.add("白天模式");
    }

    class popupDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    protected void initPopupWindow(){
        getThemeData();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupWindowView = inflater.from(mContext).inflate(R.layout.select_theme, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        //theme_lv = (ListView)findViewById(R.id.theme_lv);
        ThemeAdapter adapter = new ThemeAdapter(mContext, themes);
        theme_lv.setAdapter(adapter);
        theme_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if( popupWindow!=null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }

                //tv_theme.setText(themes.get(position));
                PrefUtils.set("setting", "theme_type", themes.get(position), mContext);
            }
        });

        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);

        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);

        //显示位置
        popupWindow.showAtLocation(LayoutInflater.from(mContext).inflate(R.layout.activity_setting, null), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);


        //设置背景半透明
        backgroundAlpha(0.5f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());

        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if( popupWindow!=null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow=null;
                    return true;
                }
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.select_theme, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
       final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.AnimationBottomFade);
        // 在底部显示
        window.showAtLocation(SettingActivity.this.findViewById(R.id.theme),
                Gravity.BOTTOM, 0, 0);

        LinearLayout night_mode = (LinearLayout)view.findViewById(R.id.night_mode);
        LinearLayout light_mode = (LinearLayout)view.findViewById(R.id.light_mode);
        LinearLayout  cancel = (LinearLayout)view.findViewById(R.id.cancel);

        night_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tv_theme.setText("夜间模式");
                PrefUtils.set("setting", "theme_type", "夜间模式", mContext);
                if( window != null &&window.isShowing()) {
                    window.dismiss();
                }
                changeTheme(); //通知改变主题
            }
        });

        light_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tv_theme.setText("白天模式");
                PrefUtils.set("setting", "theme_type", "白天模式", mContext);
                if( window != null &&window.isShowing()) {
                    window.dismiss();
                }
                changeTheme();//通知改变主题
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( window != null &&window.isShowing()) {
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

    private void changeTheme(){
        EventBus.getDefault().post("change_theme");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent = new Intent();
            intent.setClass(mContext, MainActivity.class);
            intent.putExtra("fragment_type", MY_FRAGMENT);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void clearCache(){
        boolean clear = PrefUtils.getBoolean("setting", "clear_cache", mContext);
        if (clear){
            File file = mContext.getCacheDir();
            if (file.exists()){
                Log.d("cache", file.getAbsolutePath());
                boolean a = Utils.getInstance().deleteAllFiles(file);
                Log.d("cache", ""+a);
            }
        }
    }

    private void test(){
        try {
            File file = mContext.getCacheDir();
            File file1 = new File(file,"/data/");
            if (!file1.exists()){
                file1.mkdir();
            }
            File file2 = new File(file1,"test.txt");
            if (!file2.exists()){
                file2.createNewFile();
            }

            File file3 = new File(file,"my.txt");
            if (!file3.exists()){
                file3.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
