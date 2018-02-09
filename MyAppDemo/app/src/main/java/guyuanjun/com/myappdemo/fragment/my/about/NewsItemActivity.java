package guyuanjun.com.myappdemo.fragment.my.about;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import guyuanjun.com.myappdemo.R;

public class NewsItemActivity extends AppCompatActivity {

    private static final String TAG = "NewsItemActivity";
    private Context mContext = NewsItemActivity.this;

    private int position;
    private TextView news_content;
    private ImageView back_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);

        initView();
        listener();
        initData();
    }

    private void initView(){
        news_content = (TextView)findViewById(R.id.news_content);
        back_news = (ImageView)findViewById(R.id.back_news);
    }

    private void initData(){
        getData();
        news_content.setText("about_news_item "+position);
    }

    private void getData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("about_news_item");
        position = bundle.getInt("position", 0);
    }

    private void listener(){
        back_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, AboutNewsActivity.class);
                startActivity(intent);
            }
        });
    }
}
