package guyuanjun.com.myappdemo.user;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.fragment.my.about.AboutActivity;
import guyuanjun.com.myappdemo.fragment.my.help.HelpHomeActivity;
import guyuanjun.com.myappdemo.utils.Utils;

import static guyuanjun.com.myappdemo.utils.MyUtils.isMobileNO;

public class RegisterFirst extends AppCompatActivity {
    private static final String TAG = RegisterFirst.class.getSimpleName();
    private Context mContext = RegisterFirst.this;

    private EditText phone;
    private EditText verify_code;
    private Button next;
    private Button send;
    private ImageView back;

    private final int TOTAL_TIME = 60;  //60秒后重发
    private int TIME = 0;//当前秒数
    private final int RESEND_TIP_SHOW = 0x00;
    private final int RESEND_TIP_CLOSE = 0x01;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case RESEND_TIP_SHOW:
                    send.setEnabled(false);
                    send.setText("" + TIME + "秒后重发");
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
            while (i >= 0) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_first);
        initView();
        clickListener();
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        verify_code = (EditText) findViewById(R.id.verify_code);
        next = (Button) findViewById(R.id.btn_next);
        send = (Button) findViewById(R.id.send);
        back = (ImageView) findViewById(R.id.back_register);
    }

    private void clickListener() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _phone = phone.getText().toString().trim();
                mVerifyCode = Utils.getInstance().generateVerifyCode();
                Toast.makeText(mContext, "发送验证码" + mVerifyCode + "  到手机号  " + _phone, Toast.LENGTH_SHORT).show();
                if (isMobileNO(_phone)) {
                    //next.setEnabled(true);
                    new Thread(runnable).start();
                } else {
                    Toast.makeText(mContext, "手机号 " + _phone + " 不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _phone = phone.getText().toString().trim();
                final String _verify_code = verify_code.getText().toString().trim();
/*                phone.setText("");
                verify_code.setText("");*/
                if (_verify_code.equals(mVerifyCode)) {
                    if (isMobileNO(_phone)) {
                        if (UserInfoDaoOpe.getInstance().query(mContext, _phone).size() != 0) {
                            Toast.makeText(mContext, "手机号 " + _phone + " 已经被注册", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(mContext, "验证成功， 手机号是 " + _phone + ", 验证码是 " + _verify_code, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setClass(mContext, RegisterSecond.class);
                        intent.putExtra("phone", _phone);
                        startActivity(intent);

                    } else {
                        Toast.makeText(mContext, "手机号 " + _phone + " 不正确", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, AboutActivity.class);
                startActivity(intent);
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11)
                    next.setEnabled(true);
                else
                    next.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
