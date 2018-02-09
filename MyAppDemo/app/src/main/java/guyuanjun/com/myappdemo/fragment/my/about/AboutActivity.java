package guyuanjun.com.myappdemo.fragment.my.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import guyuanjun.com.myappdemo.MainActivity;
import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.fragment.MyFragment;
import guyuanjun.com.myappdemo.http.VersionService;
import guyuanjun.com.myappdemo.model.AppVersion;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.ToastUtil;
import guyuanjun.com.myappdemo.utils.Utils;

import static guyuanjun.com.myappdemo.utils.Constant.MY_FRAGMENT;
import static guyuanjun.com.myappdemo.utils.Constant.PACKET_NAME;

public class AboutActivity extends AppCompatActivity {
    private static final String TAG = "AboutActivity";
    private Context mContext = AboutActivity.this;

    private ImageView back_about;
    private TextView tv_version;
    private ProgressBar check;
    private ImageView new_version;
    private TextView tip_update;

    private RelativeLayout company;
    private RelativeLayout news;
    private RelativeLayout contact;
    private RelativeLayout check_version;

    private final int START_CHECK_VERSION = 0x00;
    private final int STOP_CHECK_VERSION = 0x01;
    private final int HAS_NEW_VERSION = 0x02;
    private final int HAS_NOT_NEW_VERSION = 0x03;
    private boolean has_new_version = false;

    private AppVersion appVersion;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_CHECK_VERSION:
                    check.setVisibility(View.VISIBLE);
                    break;

                case STOP_CHECK_VERSION:
                    check.setVisibility(View.GONE);
                    break;

                case HAS_NEW_VERSION:
                    check.setVisibility(View.GONE);
                    has_new_version = true;
                    new_version.setVisibility(View.VISIBLE);
                    tip_update.setVisibility(View.VISIBLE);
                    tip_update.setText(appVersion.getVersionName());
                    break;

                case HAS_NOT_NEW_VERSION:
                    check.setVisibility(View.GONE);
                    has_new_version = false;
                    new_version.setVisibility(View.GONE);
                    tip_update.setVisibility(View.GONE);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_about);

        initView();
        clickListener();
        //初始化数据
        initData();
    }

    private void initView() {
        back_about = (ImageView) findViewById(R.id.back_about);
        tv_version = (TextView) findViewById(R.id.version);
        check = (ProgressBar) findViewById(R.id.check_version);
        new_version = (ImageView)findViewById(R.id.new_version);
        tip_update = (TextView)findViewById(R.id.tip_update);

        company = (RelativeLayout) findViewById(R.id.company);
        news = (RelativeLayout) findViewById(R.id.news_dynamic);
        contact = (RelativeLayout) findViewById(R.id.contact);
        check_version = (RelativeLayout) findViewById(R.id.update);
    }

    private void initData() {
        tv_version.setText(Utils.getInstance().getVersionName(mContext));
    }

    private void clickListener() {
        back_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutActivity.this, "here", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(AboutActivity.this, MainActivity.class);
                intent.putExtra("fragment_type", MY_FRAGMENT);
                startActivity(intent);
            }
        });

        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, Company.class);
                startActivity(intent);
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, AboutNewsActivity.class);
                startActivity(intent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, ContactActivity.class);
                startActivity(intent);
            }
        });

        check_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check.getVisibility() == View.VISIBLE) {
                    return;
                }

                if (has_new_version) {
                    ToastUtil.show(mContext, getResources().getString(R.string.download_version));
                    Intent intent = new Intent();
                    intent.setClass(mContext, VersionService.class);
                    intent.putExtra("download_url", appVersion.getDownloadUrl());
                    startActivity(intent);
                }else {
                    handler.sendEmptyMessage(START_CHECK_VERSION);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            appVersion = Utils.getInstance().getVersionInfo(Constant.VERSION_ADDR);
                            if (appVersion != null) {
                                if (Utils.getInstance().compareNum(appVersion.getVersionCode(),
                                        Utils.getInstance().getVersionCode(mContext)) == 1) { //有新版本
                                    handler.sendEmptyMessageDelayed(HAS_NEW_VERSION, 1000);
                                } else {
                                    handler.sendEmptyMessageDelayed(HAS_NOT_NEW_VERSION, 1000);
                                }
                            } else {
                                handler.sendEmptyMessageDelayed(HAS_NOT_NEW_VERSION, 1000);
                            }
                            //handler.sendEmptyMessage(STOP_CHECK_VERSION);
                        }

                    }).start();
                }
            }
        });
    }
}
