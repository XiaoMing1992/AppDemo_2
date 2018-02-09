package guyuanjun.com.myappdemo.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;
import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;

public class ForgetpwSecond extends AppCompatActivity {
    private static final String TAG = "ForgetpwSecond";
    private Context mContext = ForgetpwSecond.this;

    private EditText new_pw;
    private EditText again_pw;
    private Button modify;

    private String phone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpw_second);
        initView();
        initData();
        listener();
    }

    private void initView() {
        new_pw = (EditText) findViewById(R.id.new_pw);
        again_pw = (EditText)findViewById(R.id.again_pw);
        modify = (Button)findViewById(R.id.modify);
    }

    private void initData(){
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
    }

    private void listener(){
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _new_pw = new_pw.getText().toString().trim();
                String _again_pw = again_pw.getText().toString().trim();
                if (!_new_pw.equals(_again_pw)){
                    Toast.makeText(mContext, "两次密码不一样", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        List<UserInfo> lists = UserInfoDaoOpe.getInstance().query(mContext, phone);
                        if (lists.size() != 0){
                            lists.get(0).setPassword(_new_pw);
                            Toast.makeText(mContext, "修改密码成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext, "不存在该用户，修改密码失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "修改密码失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
