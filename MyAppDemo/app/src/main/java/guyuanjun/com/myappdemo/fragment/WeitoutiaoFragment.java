package guyuanjun.com.myappdemo.fragment;

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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.WeitoutiaoAdapter;
import guyuanjun.com.myappdemo.bean.WeitoutiaoInfo;
import guyuanjun.com.myappdemo.db.WeitoutiaoInfoDaoOpe;
import guyuanjun.com.myappdemo.fragment.news.ItemViewer;
import guyuanjun.com.myappdemo.http.HttpHelper;
import guyuanjun.com.myappdemo.utils.ConfigCacheUtil;
import guyuanjun.com.myappdemo.adapter.RecyclerViewDivider;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LoadCode;
import guyuanjun.com.myappdemo.utils.LogUtil;

/**
 * Created by HP on 2016-9-1.
 */
public class WeitoutiaoFragment extends Fragment {
    private final String TAG = WeitoutiaoFragment.class.getSimpleName();

    private LinearLayout loading_layout;
    private LinearLayout fail_layout;
    private LinearLayout refresh_layout;

    private RecyclerView recycler;
    private WeitoutiaoAdapter adapter;
    private List<WeitoutiaoInfo> datas = null;

    //private final int LOADDATA_FAIL = 0x00;
    //private final int LOADDATA_OK = 0x01;

    private final int ADD_READNUM_FAIL = 0x02;
    private final int ADD_READNUM_OK = 0x03;

    private final String URL = "http://www.163.com/";

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
                    if (datas.isEmpty()) {
                        refresh_layout.setVisibility(View.VISIBLE);
                    }else{
                        adapter = new WeitoutiaoAdapter(getActivity(), datas);
                        recycler.setAdapter(adapter);
                        listener();
                    }
                    break;
                case ADD_READNUM_FAIL:
                    Toast.makeText(getActivity(), "增加阅读量失败", Toast.LENGTH_SHORT).show();
                    break;
                case ADD_READNUM_OK:
                    Toast.makeText(getActivity(), "增加阅读量完成", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.weitoutiao_layout, container, false);
        recycler = (RecyclerView) layout.findViewById(R.id.recycler);
        loading_layout = (LinearLayout) layout.findViewById(R.id.loading_layout);
        fail_layout = (LinearLayout) layout.findViewById(R.id.fail_layout);
        refresh_layout = (LinearLayout) layout.findViewById(R.id.refresh_layout);

                /*
        * 布局管理器
        * LinearLayoutManager(listview展现形式)
        * GridLayoutManager(gridview展现形式)
        * StaggeredGridLayoutManager(瀑布流展现形式)
        * */
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));

        layoutListener();

        //网络请求数据
        initData(URL);
        //jieJson();

        EventBus.getDefault().register(this);
        return layout;
    }

    private void initData(final String url) {
        loading_layout.setVisibility(View.VISIBLE);

        String res_content = ConfigCacheUtil.getUrlCache(url, ConfigCacheUtil.ConfigCacheModel.CONFIG_CACHE_MODEL_SHORT);
        if (res_content != null){
            Log.e("cache", res_content);
            //jieJson(testDatas());

            loadData(testDatas());

            Message msg = new Message();
            msg.what = LoadCode.SUCCESS;
            mHandler.sendMessageDelayed(msg, 500);
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String result) {
                //打印请求返回结果
                Log.e("volley", result);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConfigCacheUtil.setUrlCache(result, url);//存储缓存
                    }
                }).start();

                //jieJson(testDatas());

                loadData(testDatas());
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

    private void loadData(final String res) {
        //创建一个Gson对象
        Gson gson = new Gson();
        Type typeOfT = new TypeToken<List<WeitoutiaoInfo>>() {
        }.getType();
        //进行解析
        datas = gson.fromJson(res, typeOfT);
    }

    private void listener() {
        adapter.setmOnItemClickListener(new WeitoutiaoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                Toast.makeText(getActivity(), "点击列表项第 " + position + " 项", Toast.LENGTH_SHORT).show();

                //addReadNum(datas.get(position).getId());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<WeitoutiaoInfo> weitoutiaoInfos = WeitoutiaoInfoDaoOpe.getInstance().queryByItemId(getActivity(), datas.get(position).getId());
                        if (weitoutiaoInfos != null && weitoutiaoInfos.size() != 0) {
                            weitoutiaoInfos.get(0).setRead_num(weitoutiaoInfos.get(0).getRead_num() + 1);
                            int resultCode = WeitoutiaoInfoDaoOpe.getInstance().save(getActivity(), weitoutiaoInfos.get(0));
                            if (resultCode == 200) {
                                mHandler.sendEmptyMessage(ADD_READNUM_OK);
                            } else
                                mHandler.sendEmptyMessage(ADD_READNUM_FAIL);
                        } else
                            mHandler.sendEmptyMessage(ADD_READNUM_FAIL);
                    }
                }).start();

                //((TextView)view.findViewById(R.id.read_num)).setText(Utils.getInstance().formatNum(weitoutiaoInfos.get(0).getRead_num()) + "阅读量");

                //显示阅读量
                //holder.read_num.setText(Utils.getInstance().formatNum(weitoutiaoInfos.get(0).getRead_num()) + "阅读量");

/*                List<WeitoutiaoInfo> weitoutiaoInfos = WeitoutiaoInfoDaoOpe.getInstance().query(getActivity(), mDatas.get(position).getUser_id(),
                        datas.get(position).getId());*/

                Intent intent = new Intent();
                intent.setClass(getActivity(), ItemViewer.class);
                intent.putExtra("item_id", datas.get(position).getId());
                intent.putExtra("intent_type", Constant.TYPE_WEITOUTIAO);
                startActivity(intent);

            }
        });

        //长按监听
        adapter.setmOnItemLongClickListener(new WeitoutiaoAdapter.OnItemLongClickListener() {
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
                        datas.remove(position);
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

    private void layoutListener(){
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

    private String testDatas() {

        List<WeitoutiaoInfo> infos = null;
        try {
            infos = WeitoutiaoInfoDaoOpe.getInstance().queryAll(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        for (int i = 0; i < infos.size(); i++) {
            Log.d("testDatas", "" + infos.get(i).getImg_path());
        }

        Gson gson = new Gson();
        Type typeOfT = new TypeToken<List<WeitoutiaoInfo>>() {
        }.getType();
        String json = gson.toJson(infos, typeOfT);
        //System.out.println("json:"+json);

        return json;
    }

    private void addReadNum(long item_id) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if ("change_attention".equals(event)) { //关注发生了改变
            //Log.d(TAG, "change_attention");
        }
    }

    @Override
    public void onDestroy() {
        //Log.d(TAG, "onDestroy");
        mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
