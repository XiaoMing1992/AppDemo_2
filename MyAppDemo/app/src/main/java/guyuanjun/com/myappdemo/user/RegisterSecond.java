package guyuanjun.com.myappdemo.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.fragment.my.about.AboutActivity;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.Utils;

import static guyuanjun.com.myappdemo.utils.MyUtils.isEmail;

public class RegisterSecond extends AppCompatActivity {

    private static final String TAG = "RegisterSecond";
    private Context mContext = RegisterSecond.this;

    private EditText user_name;
    private EditText password;
    private EditText again_pw;
    private EditText email;
    private Button register;
    //private ImageView back;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_second);
        initView();
        clickListener();

        //获取传递过来的电话
        getPhone();
    }

    private void initView() {
        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        again_pw = (EditText) findViewById(R.id.again_password);
        email = (EditText) findViewById(R.id.email);
        register = (Button) findViewById(R.id.btn_register);
        //back = (ImageView)findViewById(R.id.back_register);
    }

    private void clickListener() {
        register.setEnabled(true);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _user_name = user_name.getText().toString().trim();
                String _password = password.getText().toString().trim();
                String _again_pw = again_pw.getText().toString().trim();
                String _email = email.getText().toString().trim();

/*                phone.setText("");
                verify_code.setText("");*/
                Toast.makeText(mContext, "注册提交， 用户名是 " + _user_name + ", 密码是 " + _password
                        + ", 确认密码是 " + _again_pw + ", 邮箱是 " + _email + ", 电话号码是 " + phone, Toast.LENGTH_SHORT).show();

                if (!_password.equals(_again_pw)){
                    Toast.makeText(mContext, "两次密码不一样", Toast.LENGTH_SHORT).show();
                }else if (!isEmail(_email)){
                    Toast.makeText(mContext, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                }else { //正确
                    if (UserInfoDaoOpe.getInstance().queryByUsername(mContext, _user_name).size() != 0){
                        Toast.makeText(mContext, "用户名已经存在", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    _password = Utils.getInstance().hashKeyFormUrl(_password);
                    LogUtil.d(TAG,"user register --> _password = "+_password);
                    UserInfo userInfo = new UserInfo(_user_name, phone, _password,_email);
                    userInfo.setInvitationCode(getinvitationCode()); //生成邀请码
                    UserInfoDaoOpe.getInstance().insertData(mContext, userInfo);
                    Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();

                    List<UserInfo> lists = UserInfoDaoOpe.getInstance().queryAll(mContext);
                    for(int i=0;i<lists.size();i++){
                        Log.d("register", "用户名 "+lists.get(i).getUsername()+" 密码 "+lists.get(i).getPassword()
                                +" 电话号码 "+lists.get(i).getPhone()+" 邮箱 "+lists.get(i).getEmail());
                    }

                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginActivity.class);
                    startActivity(intent);
                }

/*                Intent intent = new Intent();
                intent.setClass(mContext, RegisterSecond.class);
                intent.putExtra("phone", _phone);
                startActivity(intent);*/
            }
        });

/*        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, RegisterFirst.class);
                startActivity(intent);
            }
        });*/
    }

    private void getPhone() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
    }

    private String getinvitationCode(){
        return Utils.randName();
    }
}
