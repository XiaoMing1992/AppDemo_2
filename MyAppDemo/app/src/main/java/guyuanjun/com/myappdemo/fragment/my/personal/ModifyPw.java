package guyuanjun.com.myappdemo.fragment.my.personal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;

public class ModifyPw extends AppCompatActivity {

    private static final String TAG = "ModifyPw";
    private Context mContext = ModifyPw.this;

    private Button sure;
    private EditText old_pw;
    private EditText new_pw;
    private EditText again_pw;

    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_modify_pw);
        initView();
        initData();
        listener();
    }

    private void initView() {
        sure = (Button) findViewById(R.id.sure);
        old_pw = (EditText) findViewById(R.id.old_pw);
        new_pw = (EditText) findViewById(R.id.new_pw);
        again_pw = (EditText) findViewById(R.id.again_pw);
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getLongExtra("user_id",1);
    }

    private void listener(){
        final long id = 1;

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _old_pw = old_pw.getText().toString().trim();
                String _new_pw = new_pw.getText().toString().trim();
                String _again_pw = again_pw.getText().toString().trim();

                Log.d(TAG, "_old_pw:" + _old_pw);
                Log.d(TAG, "_new_pw:" + _new_pw);
                Log.d(TAG, "_again_pw:" + _again_pw);

                List<UserInfo> users = UserInfoDaoOpe.getInstance().query(mContext, id);
                for (int i = 0; i < users.size(); i++) {
                    if (_old_pw.equals(users.get(i).getPassword())) {
                        Log.d(TAG, "old password:" + users.get(i).getPassword());
                        if (_new_pw.equals(_again_pw)) {
                            users.get(i).setPassword(_new_pw);
                            UserInfoDaoOpe.getInstance().save(mContext, users.get(i)); //保存更新
                            Log.d(TAG, "new password:" + users.get(i).getPassword());
                            Toast.makeText(mContext, "修改密码成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext, "新密码和确认密码不一样", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(mContext, "旧密码输入错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent = new Intent();
            intent.setClass(mContext, PersonalActivity.class);
            intent.putExtra("user_id", id);
            startActivity(intent);
            this.finish();
            return true;
        }
        return false;
    }
}
