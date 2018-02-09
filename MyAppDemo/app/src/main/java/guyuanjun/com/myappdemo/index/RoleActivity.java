package guyuanjun.com.myappdemo.index;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import guyuanjun.com.myappdemo.MainActivity;
import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.admin.AdminHomeActivity;
import guyuanjun.com.myappdemo.admin.AdminLoginActivity;
import guyuanjun.com.myappdemo.admin.Utils;
import guyuanjun.com.myappdemo.utils.ToastUtil;

public class RoleActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;

    private RelativeLayout user;
    private RelativeLayout admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        initView();
        mContext = RoleActivity.this;
    }

    private void initView(){
        user = (RelativeLayout)findViewById(R.id.user);
        admin = (RelativeLayout)findViewById(R.id.admin);
        user.setOnClickListener(this);
        admin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user:
                Intent user_intent = new Intent();
                user_intent.setClass(this, MainActivity.class);
                startActivity(user_intent);
                RoleActivity.this.finish();
                break;

            case R.id.admin:
                Intent admin_intent = new Intent();
                if (!Utils.isLogin(mContext)) {
                    admin_intent.setClass(this, AdminLoginActivity.class);
                }else{
                    admin_intent.setClass(this, AdminHomeActivity.class);
                }
                startActivity(admin_intent);
                RoleActivity.this.finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Process.killProcess(Process.myPid());
    }
}
