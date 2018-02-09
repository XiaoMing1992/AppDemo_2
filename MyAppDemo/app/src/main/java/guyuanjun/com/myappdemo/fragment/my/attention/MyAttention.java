package guyuanjun.com.myappdemo.fragment.my.attention;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.MyAttentionAdapter;
import guyuanjun.com.myappdemo.bean.AttentionInfo;
import guyuanjun.com.myappdemo.db.AttentionInfoDaoOpe;
import guyuanjun.com.myappdemo.adapter.RecyclerViewDivider;
import guyuanjun.com.myappdemo.utils.LoadCode;


public class MyAttention extends AppCompatActivity {
    private static final String TAG = MyAttention.class.getSimpleName();
    private Context mContext = MyAttention.this;

    private RecyclerView attention_list;
    private List<AttentionInfo> infoList;
    private MyAttentionAdapter adapter = null;

    private ProgressBar progress;

    private long user_id;

    //private final int LOADDATA_FAIL = 0x00;
    //private final int LOADDATA_OK = 0x01;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LoadCode.FAIL:
                    Toast.makeText(mContext, "数据加载失败", Toast.LENGTH_SHORT).show();
                    break;
                case LoadCode.SUCCESS:
                    progress.setVisibility(View.GONE);
                    Toast.makeText(mContext, "数据加载完成", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_attention);
        initView();
        initData();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this); //注册
    }

    private void initView() {
        attention_list = (RecyclerView) findViewById(R.id.recycler);
        progress = (ProgressBar) findViewById(R.id.progress);
    }

    private void initData() {
        user_id = getIntent().getLongExtra("user_id", 0);
        if (user_id != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.VISIBLE);
                    infoList = AttentionInfoDaoOpe.getInstance().query(mContext, user_id);
                    if (infoList != null) {
                        adapter = new MyAttentionAdapter(mContext, infoList);
                        attention_list.setLayoutManager(new LinearLayoutManager(mContext));
                        //添加默认分割线：高度为2px，颜色为灰色
                        attention_list.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL));
                        attention_list.setAdapter(adapter);

                        mHandler.sendEmptyMessage(LoadCode.SUCCESS);
                    } else {
                        mHandler.sendEmptyMessage(LoadCode.FAIL);
                    }
                }
            }).start();
        }
    }

    public void onEventMainThread(Bundle event) {
        String _event = event.getString("event", null);
        if (_event != null) {
            if ("cancel_attention".equals(_event)) { // 取消关注
                int position = event.getInt("position", -1);
                Log.d(TAG, "cancel_attention position=" + position);
                if (position != -1) {
                    adapter.notifyDataSetChanged();
                    infoList.remove(position);
                    adapter.notifyItemRemoved(position);
                    EventBus.getDefault().post("change_attention");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        //Log.d(TAG, "onDestroy");
        mHandler.removeCallbacksAndMessages(null);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
