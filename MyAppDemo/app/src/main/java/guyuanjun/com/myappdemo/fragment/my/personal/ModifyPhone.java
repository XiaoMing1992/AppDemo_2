package guyuanjun.com.myappdemo.fragment.my.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import guyuanjun.com.myappdemo.utils.Utils;

public class ModifyPhone extends AppCompatActivity {
    private static final String TAG = ModifyPhone.class.getSimpleName();
    private Context mContext = ModifyPhone.this;

    private Button sure;
    private Button send;
    private EditText phone;
    private EditText verifyCode;
    private TextView old_phone;

    private String oldPhone;

    private long id;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_modify_phone);
        initView();
        initData();
        listener();
    }

    private void initView() {
        sure = (Button) findViewById(R.id.sure);
        send = (Button) findViewById(R.id.send);
        phone = (EditText) findViewById(R.id.phone);
        verifyCode = (EditText) findViewById(R.id.verify_code);
        old_phone = (TextView) findViewById(R.id.old_phone);
    }

    private void initData() {
        Intent intent = getIntent();
        oldPhone = intent.getStringExtra("old_phone");
        old_phone.setText(oldPhone);
        //id = intent.getLongExtra("user_id",1);
        id = PrefUtils.getLong(Constant.SAVE_USER_INFO_NAME, Constant.USER_ID_KEY, mContext);
        LogUtil.d(TAG, "user id = " + id);
    }

    private void listener() {

        final String verify_code = "123456";

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _new_phone = phone.getText().toString().trim();
                String _verify_code = verifyCode.getText().toString().trim();
                Log.d(TAG, "_new_phone:" + _new_phone);
                Log.d(TAG, "_verify_code:" + _verify_code);

                if (MyUtils.isMobileNO(_new_phone)) {
                    if (!_new_phone.equals(oldPhone)) {
                        if (_verify_code.equals(verify_code)) {
                            List<UserInfo> users = UserInfoDaoOpe.getInstance().query(mContext, id);
                            for (int i = 0; i < users.size(); i++) {
                                users.get(i).setPhone(_new_phone);
                                UserInfoDaoOpe.getInstance().save(mContext, users.get(i)); //保存更新
                                Log.d(TAG, "new phone:" + users.get(i).getPhone());

                                EventBus.getDefault().post("modify_phone"); //修改电话号码
                                Toast.makeText(mContext, "修改手机号成功", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "输入的验证码不对", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "该手机号与旧手机号一样", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "手机号格式不对", Toast.LENGTH_SHORT).show();
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _new_phone = phone.getText().toString().trim();
                Log.d(TAG, "_new_phone:" + _new_phone);
                if (MyUtils.isMobileNO(_new_phone)) {
                    //sure.setEnabled(true);
                    new Thread(runnable).start();
                }else{
                    Toast.makeText(mContext, "手机号格式不对", Toast.LENGTH_SHORT).show();
                }
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11)
                    sure.setEnabled(true);
                else
                    sure.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
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
