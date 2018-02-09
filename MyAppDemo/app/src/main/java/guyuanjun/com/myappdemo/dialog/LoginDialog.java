package guyuanjun.com.myappdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.user.QQAuthActivity;
import guyuanjun.com.myappdemo.user.weixin.wxapi.WXEntryActivity;

/**
 * Created by HP on 2017-4-20.
 */

public class LoginDialog extends Dialog {
    private Context mContext;
    private ImageView close;
    private RelativeLayout mobile_login;
    private RelativeLayout weixin_login;
    private RelativeLayout qq_login;
    private TextView other;

    public LoginDialog(Context context) {
        super(context, R.style.MyDialogStyle);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_method);
        initView();
        listener();
    }

    private void initView(){
        close = (ImageView)findViewById(R.id.close);
        mobile_login = (RelativeLayout)findViewById(R.id.mobile_login);
        weixin_login = (RelativeLayout)findViewById(R.id.weixin_login);
        qq_login = (RelativeLayout)findViewById(R.id.qq_login);
        other = (TextView)findViewById(R.id.other);
    }

    private void listener(){
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginDialog.this.isShowing())
                    LoginDialog.this.dismiss();
            }
        });

        mobile_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "手机登录", Toast.LENGTH_SHORT).show();
                if (LoginDialog.this.isShowing())
                    LoginDialog.this.dismiss();

                LoginByPhone loginByPhone = new LoginByPhone(mContext);
                loginByPhone.show();
            }
        });


        weixin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "微信登录", Toast.LENGTH_SHORT).show();
                if (LoginDialog.this.isShowing())
                    LoginDialog.this.dismiss();

                Intent intent = new Intent();
                intent.setClass(mContext, WXEntryActivity.class);
                mContext.startActivity(intent);
            }
        });


        qq_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "qq登录", Toast.LENGTH_SHORT).show();
                if (LoginDialog.this.isShowing())
                    LoginDialog.this.dismiss();

                Intent intent = new Intent();
                intent.setClass(mContext, QQAuthActivity.class);
                mContext.startActivity(intent);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "其他方式登录", Toast.LENGTH_SHORT).show();
                if (LoginDialog.this.isShowing())
                    LoginDialog.this.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (( keyCode == KeyEvent.KEYCODE_BACK/* || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER */) && event.getAction() == KeyEvent.ACTION_DOWN){ // 监控/拦截/屏蔽返回键
            if (LoginDialog.this.isShowing())
                LoginDialog.this.dismiss();
            return true;
        }
        return false;
    }

}
