package guyuanjun.com.myappdemo.fragment.my.collect;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.NewsAdapter;
import guyuanjun.com.myappdemo.adapter.SpacesItemDecoration;
import guyuanjun.com.myappdemo.db.NewsItemInfoDaoOpe;
import guyuanjun.com.myappdemo.dialog.LoginDialog;
import guyuanjun.com.myappdemo.fragment.news.ItemViewer;
import guyuanjun.com.myappdemo.http.HttpHelper;
import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.user.LoginActivity;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LoadCode;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.Utils;

public class MyCollect extends AppCompatActivity {

    private static final String TAG = MyCollect.class.getSimpleName();
    private Context mContext = MyCollect.this;

    private RecyclerView collectView;
    private List<NewsItemInfo> newsItemInfoList;
    private NewsAdapter adapter = null;

    private LinearLayout not_login;
    private LinearLayout empty;
    private LinearLayout loading_layout;
    //private LinearLayout fail_layout;
    private LinearLayout refresh_layout;
    private Button login;

    private final String URL = "https://www.baidu.com/";

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LoadCode.FAIL:
                    loading_layout.setVisibility(View.GONE);
                    refresh_layout.setVisibility(View.VISIBLE);
                    break;

                case LoadCode.SUCCESS:
                    loading_layout.setVisibility(View.GONE);
                    if (newsItemInfoList.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                    }else{
                        adapter = new NewsAdapter(mContext, newsItemInfoList);
                        collectView.setAdapter(adapter);
                        listener();
                    }
                    Toast.makeText(mContext, "数据加载完成"+newsItemInfoList.size(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_collect);
        initView();
        layoutListener();
        //网络请求数据
        initData(URL);
    }

    private void initView(){
        collectView = (RecyclerView)findViewById(R.id.collect_list);
        not_login = (LinearLayout)findViewById(R.id.not_login);
        login = (Button) findViewById(R.id.login);
        empty = (LinearLayout)findViewById(R.id.empty);
        loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        //fail_layout = (LinearLayout) findViewById(R.id.fail_layout);
        refresh_layout = (LinearLayout) findViewById(R.id.refresh_layout);

        /*
        * 布局管理器
        * LinearLayoutManager(listview展现形式)
        * GridLayoutManager(gridview展现形式)
        * StaggeredGridLayoutManager(瀑布流展现形式)
        * */
        collectView.setLayoutManager(new LinearLayoutManager(mContext));

        //设置item之间的间隔
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(1);
        collectView.addItemDecoration(spacesItemDecoration);

//        adapter = new NewsAdapter(mContext, newsItemInfoList);
//        collectView.setAdapter(adapter);
    }

    private void initData(String url) {
        String sessionId = Utils.getInstance().getSessionFromLocal(mContext);
        if (sessionId == null) {
            Log.d(TAG,"sessionId = "+sessionId);
            collectView.setVisibility(View.GONE);
            not_login.setVisibility(View.VISIBLE);
            LoginDialog loginDialog = new LoginDialog(mContext);
            loginDialog.show();
            return;
        }

        loading_layout.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                //打印请求返回结果
                Log.e("volley", result);
                jieJson(testDatas());

                Message msg = new Message();
                msg.what = LoadCode.SUCCESS;
                mHandler.sendMessage(msg);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyerror", "erro2");

                Message msg = new Message();
                msg.what = LoadCode.FAIL;
                mHandler.sendMessage(msg);
            }
        });

        //将StringRequest对象添加进RequestQueue请求队列中
        HttpHelper.getInstance(mContext).addToRequestQueue(stringRequest);

/*        //封装的okhttp工具类的get异步
        OkHttp.getAsync("http://japi.juhe.cn/joke/content/list.from?key= 874ed931559ba07aade103eee279bb37 &page="+page+"&pagesize=10&sort=asc&time=1418745237", new OkHttp.DataCallBack() {
            //请求失败所做处理（工具类已处理）
            @Override
            public void requestFailure(Request request, IOException e) {
            }
            //请求成功所做处理
            @Override
            public void requestSuccess(String result) throws Exception {
                //解析数据
                jieJson(result);
            }
        });*/

    }

    private void jieJson(String res) {
        //创建一个Gson对象
        Gson gson = new Gson();
        Type typeOfT = new TypeToken<List<NewsItemInfo>>(){}.getType();
        //进行解析
        newsItemInfoList = gson.fromJson(res, typeOfT);
    }

    private String testDatas(){
        List<NewsItemInfo> newsItemInfoList = NewsItemInfoDaoOpe.getInstance().queryAll(mContext);
        //Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.icon);

        Gson gson = new Gson();
        Type typeOfT = new TypeToken<List<NewsItemInfo>>(){}.getType();
        String json = gson.toJson(newsItemInfoList, typeOfT);
        LogUtil.d("testDatas", "json:"+json);
        return json;
    }

    private void layoutListener(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });

        refresh_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.d(TAG, "--> refresh_layout is clicked");
                loading_layout.setVisibility(View.VISIBLE);
                refresh_layout.setVisibility(View.GONE);
                //重新网络请求数据
                initData(URL);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void listener(){
        adapter.setmOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, "点击列表项第 "+position+" 项", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(mContext, ItemViewer.class);
                intent.putExtra("item_id", newsItemInfoList.get(position).getId());
                intent.putExtra("intent_type", Constant.TYPE_NEWS);
                startActivity(intent);

            }
        });
    }
}
