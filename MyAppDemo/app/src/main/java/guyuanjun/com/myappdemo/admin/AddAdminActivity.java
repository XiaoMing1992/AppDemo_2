package guyuanjun.com.myappdemo.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.AdminInfo;
import guyuanjun.com.myappdemo.db.AdminInfoDaoOpe;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.MyUtils;
import guyuanjun.com.myappdemo.utils.ToastUtil;

public class AddAdminActivity extends AppCompatActivity {
    private final String TAG = AddAdminActivity.class.getSimpleName();

    private Context mContext;

    private Button submit;
    private EditText name;
    private EditText password;
    private EditText confirm;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        mContext = AddAdminActivity.this;
        initView();
    }

    private void initView() {
        submit = (Button) findViewById(R.id.submit);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        confirm = (EditText) findViewById(R.id.confirm);
        email = (EditText) findViewById(R.id.email);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password_str = password.getText().toString().trim();
                String confirm_str = confirm.getText().toString().trim();
                String email_str = email.getText().toString().trim();
                String name_str = name.getText().toString().trim();

                if (name_str.equals("") || password_str.equals("")
                        || email_str.equals("") || confirm_str.equals("")) {
                    ToastUtil.show(mContext, "不能为空");
                    return;
                }

                if (!password_str.equals(confirm_str)) {
                    ToastUtil.show(mContext, "两次密码输入不正确");
                } else {
                    if (MyUtils.isEmail(email_str)) {
                        LogUtil.d(TAG, "name = " + name_str + "     password = " + password_str + "     email = " + email_str);
                        //把要添加的管理员的信息写入数据库
                        AdminInfo adminInfo = new AdminInfo();
                        adminInfo.setAdminname(name_str);
                        password_str = Utils.encodeMD5(password_str);
                        LogUtil.d(TAG,"add admin --> password_str = "+password_str);
                        adminInfo.setPassword(password_str);
                        adminInfo.setEmail(email_str);

                        try {
                            AdminInfoDaoOpe.getInstance().insertData(mContext, adminInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show(mContext, "添加管理员失败");
                            return;
                        }
                        ToastUtil.show(mContext, "成功添加管理员");
                        finish();
                    } else {
                        ToastUtil.show(mContext, "邮件格式不正确");
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(mContext, AdminHomeActivity.class);
        intent.putExtra("fragment_type", 0);
        startActivity(intent);
    }
}
