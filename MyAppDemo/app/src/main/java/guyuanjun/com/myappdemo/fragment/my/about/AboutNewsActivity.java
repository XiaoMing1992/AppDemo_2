package guyuanjun.com.myappdemo.fragment.my.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.CompanyNewsAdapter;
import guyuanjun.com.myappdemo.adapter.MyDecoration;
import guyuanjun.com.myappdemo.model.CompanyNewsItemInfo;

public class AboutNewsActivity extends AppCompatActivity {

    private final String TAG = "Company";

    private ImageView back_news;
    private Context mContext = AboutNewsActivity.this;
    private RecyclerView company_news_list;
    private CompanyNewsAdapter adapter = null;
    private ProgressBar progress;

    private final int LOADDATA_FAIL = 0x00;
    private final int LOADDATA_OK = 0x01;
    List<CompanyNewsItemInfo> data;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOADDATA_FAIL:
                    Toast.makeText(mContext, "数据加载失败", Toast.LENGTH_SHORT).show();
                    break;
                case LOADDATA_OK:
                    progress.setVisibility(View.GONE);
                    initData();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_news);

        initView();
        //initData();

        data = loadData();
        clickListener();
    }

    private void initView(){
        back_news = (ImageView)findViewById(R.id.back_news);
        company_news_list = (RecyclerView)findViewById(R.id.company_news_list);
        progress = (ProgressBar)findViewById(R.id.progress);
    }

    private void initData(){
        company_news_list.setLayoutManager(new LinearLayoutManager(mContext));

/*        List<CompanyNewsItemInfo> data = new ArrayList<CompanyNewsItemInfo>();
        data.add(new CompanyNewsItemInfo("1111111111111111","","","2017-2-01"));
        data.add(new CompanyNewsItemInfo("2222222222222222","","","2017-2-02"));
        data.add(new CompanyNewsItemInfo("3333333333333333","","","2017-2-03"));*/

        adapter = new CompanyNewsAdapter(data);
        company_news_list.setAdapter(adapter);

        //这句就是添加我们自定义的分隔线
        company_news_list.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));

/*        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(1);
        company_news_list.addItemDecoration(spacesItemDecoration);*/
        adapter.setmOnItemClickListener(new CompanyNewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, "点击列表项第 "+position+" 项", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(mContext, NewsItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                intent.putExtra("about_news_item", bundle);
                startActivity(intent);
            }
        });
    }

    private void clickListener(){
        back_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<CompanyNewsItemInfo> loadData(){
        final List<CompanyNewsItemInfo> data = new ArrayList<CompanyNewsItemInfo>();
        progress.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                data.add(new CompanyNewsItemInfo("1111111111111111","","","2017-2-01"));
                data.add(new CompanyNewsItemInfo("2222222222222222","","","2017-2-02"));
                data.add(new CompanyNewsItemInfo("3333333333333333","","","2017-2-03"));
                data.add(new CompanyNewsItemInfo("1111111111111111","","","2017-2-01"));
                data.add(new CompanyNewsItemInfo("2222222222222222","","","2017-2-02"));
                data.add(new CompanyNewsItemInfo("3333333333333333","","","2017-2-03"));
                data.add(new CompanyNewsItemInfo("1111111111111111","","","2017-2-01"));
                data.add(new CompanyNewsItemInfo("2222222222222222","","","2017-2-02"));
                data.add(new CompanyNewsItemInfo("3333333333333333","","","2017-2-03"));
                data.add(new CompanyNewsItemInfo("1111111111111111","","","2017-2-01"));
                data.add(new CompanyNewsItemInfo("2222222222222222","","","2017-2-02"));
                data.add(new CompanyNewsItemInfo("3333333333333333","","","2017-2-03"));
                data.add(new CompanyNewsItemInfo("1111111111111111","","","2017-2-01"));
                data.add(new CompanyNewsItemInfo("2222222222222222","","","2017-2-02"));
                data.add(new CompanyNewsItemInfo("3333333333333333","","","2017-2-03"));

                Message msg = new Message();
                msg.what = LOADDATA_OK;
                mHandler.sendMessage(msg);
            }
        }).start();
       return data;
    }
}
