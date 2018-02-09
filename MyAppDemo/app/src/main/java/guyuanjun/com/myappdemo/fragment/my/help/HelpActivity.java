package guyuanjun.com.myappdemo.fragment.my.help;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import guyuanjun.com.myappdemo.R;

public class HelpActivity extends AppCompatActivity {

    private static final String TAG = "HelpActivity";
    private Context mContext = HelpActivity.this;

    private ImageView back_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initView();
        clickListener();
    }

    private void initView() {
        back_help = (ImageView) findViewById(R.id.back_help);
    }

    private void clickListener(){
        back_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, HelpHomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
