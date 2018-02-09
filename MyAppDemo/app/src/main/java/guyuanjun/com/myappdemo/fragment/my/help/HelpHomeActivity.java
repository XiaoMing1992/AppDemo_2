package guyuanjun.com.myappdemo.fragment.my.help;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import guyuanjun.com.myappdemo.MainActivity;
import guyuanjun.com.myappdemo.R;

import static guyuanjun.com.myappdemo.utils.Constant.MY_FRAGMENT;

public class HelpHomeActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";
    private Context mContext = HelpHomeActivity.this;

    private ImageView back_help_home;

    private RelativeLayout help;
    private RelativeLayout feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_home);

        initView();
        clickListener();
    }

    private void initView(){
        back_help_home = (ImageView)findViewById(R.id.back_help_home);
        help = (RelativeLayout)findViewById(R.id.help);
        feedback = (RelativeLayout)findViewById(R.id.feedback);
    }

    private void clickListener(){
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, HelpActivity.class);
                startActivity(intent);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        back_help_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, MainActivity.class);
                intent.putExtra("fragment_type", MY_FRAGMENT);
                startActivity(intent);
            }
        });
    }
}
