package guyuanjun.com.myappdemo.admin;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.admin.fragment.AdminCenterFragment;
import guyuanjun.com.myappdemo.admin.fragment.HomeFragment;
import guyuanjun.com.myappdemo.utils.ToastUtil;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = this.getClass().getSimpleName();
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    //开启一个Fragment事务
    FragmentTransaction fragmentTransaction;

    private int tabId = 0;

    private ImageView homeImage;
    private TextView homeText;
    private View homeLayout;
    private ImageView myImage;
    private TextView myText;
    private View myLayout;
    private HomeFragment homeFragment;
    private AdminCenterFragment adminCenterFragment;

    /**
     * 用来计算退出所需要点击返回键次数
     */
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_home);
        if (savedInstanceState != null) {
            if (savedInstanceState.getInt("fragment_id", -1) == -1) {
                tabId = 0;
            } else {
                tabId = savedInstanceState.getInt("fragment_id", -1);
            }
            Log.d("fragment_id", ""+savedInstanceState.getInt("fragment_id", -1));
        }


        initView();
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

    private void initView(){
        homeImage = (ImageView)findViewById(R.id.home_image);
        homeText = (TextView)findViewById(R.id.home_text);
        homeLayout = findViewById(R.id.home_layout);
        homeLayout.setOnClickListener(this);

        myImage = (ImageView)findViewById(R.id.my_image);
        myText = (TextView)findViewById(R.id.my_text);
        myLayout = findViewById(R.id.my_layout);
        myLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_layout:
                tabId = 0;
                //当点击了主页tab时，选中第1个tab
                setTabSelection(0);
                break;

            case R.id.my_layout:
                tabId = 3;
                //当点击了主页tab时，选中第4个tab
                setTabSelection(3);
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *             每个tab页对应的下标。0表示主页
     */
    private void setTabSelection(int index){
        //每次选中之前先清除掉上次的选中状态
        clearSelection();

        fragmentTransaction = fragmentManager.beginTransaction();
        //先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragmentTransaction);
        switch(index){
            case 3:
                //当点击了设置tab时，改变控件的图片和文字颜色
                myImage.setImageResource(R.drawable.my_selected);
                myText.setTextColor(Color.WHITE);
                if (adminCenterFragment == null){
                    //如果settingFragment为空，则创建一个并添加到界面上
                    adminCenterFragment = new AdminCenterFragment();
                    fragmentTransaction.add(R.id.content, adminCenterFragment);
                }else{
                    //如果settingFragment不为空，则直接显示出来
                    fragmentTransaction.show(adminCenterFragment);
                }
                fragmentTransaction.commit();
                break;

            case 0:
            default:
                //当点击了设置tab时，改变控件的图片和文字颜色
                homeImage.setImageResource(R.drawable.home_selected);
                homeText.setTextColor(Color.WHITE);
                if (homeFragment == null){
                    //如果settingFragment为空，则创建一个并添加到界面上
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.content, homeFragment);
                }else{
                    //如果settingFragment不为空，则直接显示出来
                    fragmentTransaction.show(homeFragment);
                }
                fragmentTransaction.commit();
                break;
        }
    }

    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection(){
        homeImage.setImageResource(R.drawable.home_unselected);
        homeText.setTextColor(Color.parseColor("#82858b"));

    }

    /**
     * 将所有的Fragment都设置为隐藏状态。
     *
     * @param fragmentTransaction
     *              用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction fragmentTransaction)
    {
        if (homeFragment != null){
            fragmentTransaction.hide(homeFragment);
        }
        if (adminCenterFragment != null){
            fragmentTransaction.hide(adminCenterFragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("fragment_id", tabId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtil.show(this,"再按一次退出");
            //Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
            firstTime = secondTime;
        } else {
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
}
