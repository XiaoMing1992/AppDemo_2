package guyuanjun.com.myappdemo.fragment.my.personal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import guyuanjun.com.myappdemo.MainActivity;
import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.dialog.LoginDialog;
import guyuanjun.com.myappdemo.fragment.my.about.AboutActivity;
import guyuanjun.com.myappdemo.fragment.my.about.AboutNewsActivity;
import guyuanjun.com.myappdemo.fragment.my.about.Company;
import guyuanjun.com.myappdemo.fragment.my.about.ContactActivity;
import guyuanjun.com.myappdemo.user.LoginActivity;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.PrefUtils;
import guyuanjun.com.myappdemo.utils.Utils;

import static guyuanjun.com.myappdemo.utils.Constant.MY_FRAGMENT;

public class PersonalActivity extends AppCompatActivity {

    private static final String TAG = PersonalActivity.class.getSimpleName();
    private Context mContext = PersonalActivity.this;

    private TextView tv_phone;
    private TextView tv_email;
    private TextView tv_nickname;
    private ImageView head_icon;
    private Button edit;
    private Button logout;

    private RelativeLayout modify_phone;
    private RelativeLayout modify_password;
    private RelativeLayout modify_email;

    private LinearLayout my_look;
    private LinearLayout my_attention;
    private LinearLayout my_fan;

    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        initView();
        clickListener();
        //初始化数据
        initData();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this); //注册
    }

    private void initView() {
        edit = (Button) findViewById(R.id.edit);
        logout = (Button) findViewById(R.id.btn_logout);

        tv_phone = (TextView) findViewById(R.id.phone);
        tv_email = (TextView) findViewById(R.id.email);
        tv_nickname = (TextView) findViewById(R.id.nickname);
        head_icon = (ImageView) findViewById(R.id.head_icon);

        modify_phone = (RelativeLayout) findViewById(R.id.modify_phone);
        modify_password = (RelativeLayout) findViewById(R.id.modify_password);
        modify_email = (RelativeLayout) findViewById(R.id.modify_email);

        my_look = (LinearLayout) findViewById(R.id.my_look);
        my_attention = (LinearLayout) findViewById(R.id.my_attention);
        my_fan = (LinearLayout) findViewById(R.id.my_fan);
    }

    private void initData() {
        if (!Utils.getInstance().has_login(mContext)) {
            LoginDialog loginDialog = new LoginDialog(mContext);
            loginDialog.show();
            return;
        }
        loadData();
    }

    private void loadData(){
        //Intent intent = getIntent();
        //id = intent.getLongExtra("user_id", -1);
        id = PrefUtils.getLong(Constant.SAVE_USER_INFO_NAME, Constant.USER_ID_KEY, mContext);
        LogUtil.d(TAG, "user id = " + id);

        String head_icon_path = null;
        String nickname = null;
        String phone = "还没有呢";
        String email = "还没有呢";


        List<UserInfo> users = UserInfoDaoOpe.getInstance().query(mContext, id);
        for (int i = 0; i < users.size(); i++) {
            Log.d(TAG, "new phone:" + users.get(i).getPhone());
            phone = users.get(i).getPhone();
            email = users.get(i).getEmail();
            head_icon_path = users.get(i).getHead_path();
            nickname = users.get(i).getNickname();
        }

/*        Bundle bundle = intent.getBundleExtra("person");
        String nickname = bundle.getString("nickname","xiaoming");
        String head_icon_path = bundle.getString("head_icon_path",null);
        String phone = bundle.getString("phone","还没有呢");
        String email = bundle.getString("email","还没有呢");*/
        if (nickname != null)
            tv_nickname.setText(nickname);
        if(phone.equals("还没有呢"))
            tv_phone.setText(phone);
        else
            tv_phone.setText(phone.substring(0,3)+"******"+phone.substring(9,11));
        tv_email.setText(email);

        if (head_icon_path != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(head_icon_path);
                if (bitmap != null)
                    head_icon.setImageBitmap(bitmap);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
    }

    private void clickListener() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PrefUtils.remove("user", "userId", mContext); //删除会话
                PrefUtils.remove("cookie", "jsessionid", mContext); //删除会话

                PrefUtils.remove(Constant.SAVE_USER_INFO_NAME, "user_name", mContext);
                PrefUtils.remove(Constant.SAVE_USER_INFO_NAME, "user_id", mContext);

                Toast.makeText(mContext, "退出成功", Toast.LENGTH_LONG).show();

                EventBus.getDefault().post("logout"); //改变登录状态

                Intent intent = new Intent();
                intent.setClass(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });

        modify_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, ModifyPhone.class);
                intent.putExtra("old_phone", tv_phone.getText().toString().trim());
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });

        modify_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, ModifyPw.class);
                startActivity(intent);
            }
        });

        modify_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, ModifyEmail.class);
                intent.putExtra("old_email", tv_email.getText().toString().trim());
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, EditPersonalActivity.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });
    }

    private void modifyHeadAndNickName(){
//        Intent intent = getIntent();
//        id = intent.getLongExtra("user_id", -1);
        String head_icon_path = null;
        String nickname = null;

        List<UserInfo> users = UserInfoDaoOpe.getInstance().query(mContext, id);
        for (int i = 0; i < users.size(); i++) {
            head_icon_path = users.get(i).getHead_path();
            nickname = users.get(i).getNickname();
            Log.d(TAG, "head_icon_path:" + head_icon_path+"   nickname:" + nickname);
        }

        if (nickname != null)
            tv_nickname.setText(nickname);
        if (head_icon_path != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(head_icon_path);
                if (bitmap != null)
                    head_icon.setImageBitmap(bitmap);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }

    }

    private void modifyPhone(){
//        Intent intent = getIntent();
//        id = intent.getLongExtra("user_id", -1);
        String phone = "还没有呢";

        List<UserInfo> users = UserInfoDaoOpe.getInstance().query(mContext, id);
        for (int i = 0; i < users.size(); i++) {
            phone = users.get(i).getPhone();
            Log.d(TAG, "phone:" + phone);
        }

        if(phone.equals("还没有呢"))
            tv_phone.setText(phone);
        else
            tv_phone.setText(phone.substring(0,3)+"******"+phone.substring(9,11));
    }

    private void modifyEmail(){
//        Intent intent = getIntent();
//        id = intent.getLongExtra("user_id", -1);
        String email = "还没有呢";

        List<UserInfo> users = UserInfoDaoOpe.getInstance().query(mContext, id);
        for (int i = 0; i < users.size(); i++) {
            email = users.get(i).getEmail();
            Log.d(TAG, "email:" + email);
        }
        tv_email.setText(email);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if ("modify_head_nickname".equals(event)) { //修改头像和昵称
            modifyHeadAndNickName();
            Log.d(TAG, "modify_head_nickname");
        } else if ("modify_phone".equals(event)) { //修改电话号码
            modifyPhone();
            Log.d(TAG, "modify_phone");
        } else if ("modify_email".equals(event)) { //修改邮件
            modifyEmail();
            Log.d(TAG, "modify_email");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setClass(mContext, MainActivity.class);
            intent.putExtra("fragment_type", MY_FRAGMENT);
            startActivity(intent);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
