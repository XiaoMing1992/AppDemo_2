package guyuanjun.com.myappdemo.fragment.my.history.history_fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.LookupHistoryAdapter;
import guyuanjun.com.myappdemo.adapter.SpacesItemDecoration;
import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.db.NewsItemInfoDaoOpe;
import guyuanjun.com.myappdemo.fragment.news.ItemViewer;
import guyuanjun.com.myappdemo.http.HttpHelper;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LoadCode;
import guyuanjun.com.myappdemo.utils.LogUtil;

/**
 * Created by HP on 2017-4-24.
 */

public class LookupHistoryFragment extends Fragment {
    private final String TAG = LookupHistoryFragment.class.getSimpleName();

    private RecyclerView lookup_list;
    private List<NewsItemInfo> newsItemInfoList;
    private LookupHistoryAdapter adapter = null;

    private View myLayout;
    private LinearLayout loading_layout;
    private LinearLayout fail_layout;
    private LinearLayout refresh_layout;

    private final String URL = "https://www.baidu.com/";

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LoadCode.FAIL:
                    loading_layout.setVisibility(View.GONE);
                    //fail_layout.setVisibility(View.VISIBLE);
                    refresh_layout.setVisibility(View.VISIBLE);
                    break;
                case LoadCode.SUCCESS:
                    loading_layout.setVisibility(View.GONE);
                    if (newsItemInfoList.isEmpty()) {
                        refresh_layout.setVisibility(View.VISIBLE);
                    }else{
                        adapter = new LookupHistoryAdapter(getActivity(), newsItemInfoList);
                        lookup_list.setAdapter(adapter);
                        listener();
                    }
                    break;
                case LoadCode.LOADING:
                    loading_layout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myLayout = inflater.inflate(R.layout.lookup_history, container, false);
        initView();
        initLayout();
        //网络请求数据
        initData(URL);
        return myLayout;
    }

    private void listener(){
        adapter.setmOnItemClickListener(new LookupHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "点击列表项第 " + position + " 项", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(getActivity(), ItemViewer.class);
                intent.putExtra("item_id", newsItemInfoList.get(position).getId());
                intent.putExtra("intent_type", Constant.TYPE_NEWS);
                startActivity(intent);

            }
        });

        //长按监听
        adapter.setmOnItemLongClickListener(new LookupHistoryAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyDataSetChanged();
                        newsItemInfoList.remove(position);
                        Toast.makeText(getActivity(), "删除列表项", Toast.LENGTH_SHORT).show();
                    }
                });
                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //Dialog 显示
                builder.create().show();
            }
        });
    }

    private void initLayout() {
        /*
        * 布局管理器
        * LinearLayoutManager(listview展现形式)
        * GridLayoutManager(gridview展现形式)
        * StaggeredGridLayoutManager(瀑布流展现形式)
        * */
        lookup_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置item之间的间隔
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(20);
        lookup_list.addItemDecoration(spacesItemDecoration);

        refresh_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.d(TAG, "--> refresh_layout is clicked");
                loading_layout.setVisibility(View.VISIBLE);
                refresh_layout.setVisibility(View.GONE);
                //重新网络请求数据
                initData(URL);
                //adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        loading_layout = (LinearLayout) myLayout.findViewById(R.id.loading_layout);
        fail_layout = (LinearLayout) myLayout.findViewById(R.id.fail_layout);
        refresh_layout = (LinearLayout) myLayout.findViewById(R.id.refresh_layout);
        lookup_list = (RecyclerView) myLayout.findViewById(R.id.lookup_history_list);
    }

    private void initData(String url) {
        mHandler.sendEmptyMessage(LoadCode.LOADING);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                //打印请求返回结果
                Log.e("volley", result);
                jieJson(testDatas());

                Message msg = new Message();
                msg.what = LoadCode.SUCCESS;
                mHandler.sendMessageDelayed(msg, 300);

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
        HttpHelper.getInstance(getActivity()).addToRequestQueue(stringRequest);

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
        Type typeOfT = new TypeToken<List<NewsItemInfo>>() {
        }.getType();
        //进行解析
        newsItemInfoList = gson.fromJson(res, typeOfT); //这种方式调用notifyDataSetChanged之后listview不刷新
    }

    private String testDatas() {
        List<NewsItemInfo> newsItemInfoList2 = NewsItemInfoDaoOpe.getInstance().queryAll(getActivity());
        //Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.icon);

        for (int i = 0; i < newsItemInfoList2.size(); i++) {
            Log.d("testDatas", "" + newsItemInfoList2.get(i).getImg_path());
        }

        Gson gson = new Gson();
        Type typeOfT = new TypeToken<List<NewsItemInfo>>() {
        }.getType();
        String json = gson.toJson(newsItemInfoList2, typeOfT);
        //System.out.println("json:"+json);

        return json;
    }
}
