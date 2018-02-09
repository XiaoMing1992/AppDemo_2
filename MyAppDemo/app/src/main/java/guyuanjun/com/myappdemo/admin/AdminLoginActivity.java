package guyuanjun.com.myappdemo.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.AdminInfo;
import guyuanjun.com.myappdemo.db.AdminInfoDaoOpe;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.PrefUtils;
import guyuanjun.com.myappdemo.utils.ToastUtil;

public class AdminLoginActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;

    private ImageView back_login;
    private EditText admin_name;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        mContext = AdminLoginActivity.this;
        initView();
        clickListener();
    }

    private void initView(){
        back_login = (ImageView)findViewById(R.id.back_login);
        admin_name = (EditText)findViewById(R.id.admin_name);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.btn_login);
    }

    private void clickListener(){
        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminLoginActivity.this.finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_str = admin_name.getText().toString().trim();
                String password_str = password.getText().toString().trim();
                if (name_str.isEmpty()){
                    ToastUtil.show(mContext, "账号不能为空");
                    return;
                }
                if (password_str.isEmpty()){
                    ToastUtil.show(mContext, "密码不能为空");
                    return;
                }

//                if(name_str.equals("admin")&&password_str.equals("admin")){
//                    //Utils.saveAdminSession(mContext, "admin");
//                    Utils.set(Constant.ADMIN_SESSION_FILE, Constant.ADMIN_SESSION_ID, "admin",mContext);
//                    //Utils.set(Constant.SAVE_ADMIN_INFO_NAME, Constant.ADMIN_NAME_KEY, adminInfos.get(0).getAdminname(),mContext);
//                    //Utils.set(Constant.SAVE_ADMIN_INFO_NAME, Constant.ADMIN_ID_KEY, adminInfos.get(0).getId(),mContext);
//
//                    Intent intent2 = new Intent();
//                    intent2.setClass(mContext, AdminHomeActivity.class);
//                    startActivity(intent2);
//                }

                AdminInfo adminInfo = new AdminInfo();
                adminInfo.setAdminname("admin");
                adminInfo.setEmail("1422297148@qq.com");
                adminInfo.setPassword(Utils.encodeMD5("admin")); //
                adminInfo.setNickname("admin");
                AdminInfoDaoOpe.getInstance().insertData(mContext, adminInfo);

                //判断
                password_str = Utils.encodeMD5(password_str);
                LogUtil.d(TAG,"login admin --> password_str = "+password_str);
                List<AdminInfo> adminInfos = AdminInfoDaoOpe.getInstance().query(mContext, name_str, password_str);
                if (adminInfos.size() == 0){
                    ToastUtil.show(mContext, "账号或者密码有错");
                    return;
                }
                //Utils.saveAdminSession(mContext, "admin");
                Utils.set(Constant.ADMIN_SESSION_FILE, Constant.ADMIN_SESSION_ID, "admin",mContext);
                Utils.set(Constant.SAVE_ADMIN_INFO_NAME, Constant.ADMIN_NAME_KEY, adminInfos.get(0).getAdminname(),mContext);
                Utils.set(Constant.SAVE_ADMIN_INFO_NAME, Constant.ADMIN_ID_KEY, adminInfos.get(0).getId(),mContext);

                Intent intent = new Intent();
                intent.setClass(mContext, AdminHomeActivity.class);
                startActivity(intent);
                AdminLoginActivity.this.finish();
            }
        });
    }

}
