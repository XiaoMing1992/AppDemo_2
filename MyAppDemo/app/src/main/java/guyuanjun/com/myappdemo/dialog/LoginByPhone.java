package guyuanjun.com.myappdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.user.ForgetpwFirst;
import guyuanjun.com.myappdemo.user.LoginActivity;
import guyuanjun.com.myappdemo.user.QQAuthActivity;
import guyuanjun.com.myappdemo.user.RegisterFirst;
import guyuanjun.com.myappdemo.user.weixin.wxapi.WXEntryActivity;
import guyuanjun.com.myappdemo.utils.MyUtils;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2017-4-20.
 */

public class LoginByPhone extends Dialog {
    private Context mContext;
    private ImageView close;
    private ImageView weixin_login;
    private ImageView qq_login;
    private EditText phone;
    private EditText verify_code;
    private Button send;
    private Button login;
    private TextView normal_login;
    private TextView go_register;
    private TextView forget_pw;

    private final int TOTAL_TIME = 60;  //60秒后重发
    private int TIME = 0;//当前秒数
    private final int RESEND_TIP_SHOW = 0x00;
    private final int RESEND_TIP_CLOSE = 0x01;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case RESEND_TIP_SHOW:
                    send.setEnabled(false);
                    send.setText(""+TIME+"秒后重发");
                    break;
                case RESEND_TIP_CLOSE:
                    send.setEnabled(true);
                    send.setText("发送验证码");
                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int i = TOTAL_TIME;
            while (i>=0){
                mHandler.sendEmptyMessage(RESEND_TIP_SHOW);
                TIME = i;
                try {
                    Thread.sleep(1000);
                    i--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mHandler.sendEmptyMessage(RESEND_TIP_CLOSE);
        }
    };

    private String mVerifyCode;

    public LoginByPhone(Context context) {
        super(context, R.style.MyDialogStyle);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_by_phone);
        initView();
        listener();
    }

    private void initView() {
        close = (ImageView) findViewById(R.id.close);
        weixin_login = (ImageView) findViewById(R.id.weixin_login);
        qq_login = (ImageView) findViewById(R.id.qq_login);
        phone = (EditText) findViewById(R.id.phone);
        verify_code = (EditText) findViewById(R.id.verify_code);
        send = (Button) findViewById(R.id.send);
        login = (Button) findViewById(R.id.login);
        normal_login = (TextView) findViewById(R.id.normal_login);

        go_register = (TextView) findViewById(R.id.go_register);
        go_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        go_register.getPaint().setAntiAlias(true);//抗锯齿

        forget_pw = (TextView) findViewById(R.id.forget_pw);
        forget_pw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        forget_pw.getPaint().setAntiAlias(true);//抗锯齿

    }

    private void listener() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginByPhone.this.isShowing())
                    LoginByPhone.this.dismiss();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _phone = phone.getText().toString().trim();
                mVerifyCode = Utils.getInstance().generateVerifyCode();
                if (MyUtils.isMobileNO(_phone)) {
                    Toast.makeText(mContext, "发送验证码"+mVerifyCode+"到手机号" + _phone, Toast.LENGTH_SHORT).show();
                    //login.setEnabled(true);
                    new Thread(runnable).start();
                }else {
                    Toast.makeText(mContext, "手机号格式不对", Toast.LENGTH_SHORT).show();
                    //login.setEnabled(false);
                }
            }
        });

        go_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginByPhone.this.isShowing())
                    LoginByPhone.this.dismiss();
                Intent intent = new Intent();
                intent.setClass(mContext, RegisterFirst.class);
                mContext.startActivity(intent);
            }
        });

        forget_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginByPhone.this.isShowing())
                    LoginByPhone.this.dismiss();
                Intent intent = new Intent();
                intent.setClass(mContext, ForgetpwFirst.class);
                mContext.startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String _verify_code = verify_code.getText().toString().trim();
                if (_verify_code.equals(mVerifyCode)) {
                    String _phone = phone.getText().toString().trim();
                    if (!MyUtils.isMobileNO(_phone)) {
                        Toast.makeText(mContext, "手机号格式不对", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<UserInfo> UserInfoList = UserInfoDaoOpe.getInstance().query(mContext, _phone);
                    if (UserInfoList.size() > 0) {
                        Utils.getInstance().saveSessionToLocal(mContext, "JSESSIONID=AD5F5C9EEB16C71EC3725DBF209F6178");
                        Toast.makeText(mContext, "手机登陆成功， 手机号是 " + _phone, Toast.LENGTH_SHORT).show();
                        if (LoginByPhone.this.isShowing())
                            LoginByPhone.this.dismiss();
                    }
                    else
                        Toast.makeText(mContext, "手机登陆失败， 手机号是 " + _phone, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });


        weixin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "微信登录", Toast.LENGTH_SHORT).show();
                if (LoginByPhone.this.isShowing())
                    LoginByPhone.this.dismiss();

                Intent intent = new Intent();
                intent.setClass(mContext, WXEntryActivity.class);
                mContext.startActivity(intent);
            }
        });


        qq_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "qq登录", Toast.LENGTH_SHORT).show();
                if (LoginByPhone.this.isShowing())
                    LoginByPhone.this.dismiss();

                Intent intent = new Intent();
                intent.setClass(mContext, QQAuthActivity.class);
                mContext.startActivity(intent);
            }
        });

        normal_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "账号密码登录", Toast.LENGTH_SHORT).show();
                if (LoginByPhone.this.isShowing())
                    LoginByPhone.this.dismiss();

                Intent intent = new Intent();
                intent.setClass(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11)
                    login.setEnabled(true);
                else
                    login.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK/* || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER */) && event.getAction() == KeyEvent.ACTION_DOWN) { // 监控/拦截/屏蔽返回键
            if (LoginByPhone.this.isShowing())
                LoginByPhone.this.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void show() {
        super.show();

        //WindowManager windowManager = getWindowManager();
        //Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //lp.width = (int) (display.getWidth()); // 设置宽度
        //lp.height = (int) display.getHeight();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mHandler.removeCallbacks(runnable);
    }
}

