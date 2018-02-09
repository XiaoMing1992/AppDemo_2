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

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.MyUtils;
import guyuanjun.com.myappdemo.utils.PrefUtils;

public class ModifyEmail extends AppCompatActivity {

    private static final String TAG = "ModifyEmail";
    private Context mContext = ModifyEmail.this;

    private Button sure;
    private EditText email;
    private TextView old_email;

    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_modify_email);
        initView();
        initData();
        listener();
    }

    private void initView() {
        sure = (Button) findViewById(R.id.sure);
        email = (EditText) findViewById(R.id.new_email);
        old_email = (TextView) findViewById(R.id.old_email);
    }

    private void initData() {
        Intent intent = getIntent();
        String _old_email = intent.getStringExtra("old_email");
        old_email.setText(_old_email);
        //id = intent.getLongExtra("user_id",1);
        id = PrefUtils.getLong(Constant.SAVE_USER_INFO_NAME, Constant.USER_ID_KEY, mContext);
        LogUtil.d(TAG, "user id = " + id);
    }

    private void listener() {

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _new_email = email.getText().toString().trim();
                Log.d(TAG, "_new_email:" + _new_email);

                if (MyUtils.isEmail(_new_email)) {
                    if (!_new_email.equals(old_email.getText().toString().trim())) {
                        List<UserInfo> users = UserInfoDaoOpe.getInstance().query(mContext, id);
                        for (int i = 0; i < users.size(); i++) {
                            users.get(i).setEmail(_new_email);
                            UserInfoDaoOpe.getInstance().save(mContext, users.get(i)); //保存更新
                            Log.d(TAG, "new email:" + users.get(i).getEmail());

                            EventBus.getDefault().post("modify_email"); //修改邮件
                            Toast.makeText(mContext, "修改邮箱成功", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "该邮箱与旧邮箱一样", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "邮箱格式不对", Toast.LENGTH_SHORT).show();
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
