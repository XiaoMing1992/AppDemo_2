package guyuanjun.com.myappdemo.fragment.my.fan;

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
import guyuanjun.com.myappdemo.adapter.FanAdapter;
import guyuanjun.com.myappdemo.adapter.MyAttentionAdapter;
import guyuanjun.com.myappdemo.bean.FanInfo;
import guyuanjun.com.myappdemo.db.AttentionInfoDaoOpe;
import guyuanjun.com.myappdemo.db.FanInfoDaoOpe;

public class MyFan extends AppCompatActivity {
    private static final String TAG = "MyFan";
    private Context mContext = MyFan.this;

    private RecyclerView fan_list;
    private List<FanInfo> infoList;
    private FanAdapter adapter = null;

    private ProgressBar progress;

    private long user_id;

    private final int LOADDATA_FAIL = 0x00;
    private final int LOADDATA_OK = 0x01;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOADDATA_FAIL:
                    Toast.makeText(mContext, "数据加载失败", Toast.LENGTH_SHORT).show();
                    break;
                case LOADDATA_OK:
                    progress.setVisibility(View.GONE);
                    Toast.makeText(mContext, "数据加载完成", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_fan);
        initView();
        initData();
    }

    private void initView(){
        fan_list = (RecyclerView)findViewById(R.id.recycler);
        progress = (ProgressBar)findViewById(R.id.progress);
    }

    private void initData(){
        user_id = getIntent().getLongExtra("user_id", 0);
        if (user_id != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.VISIBLE);
                    infoList = FanInfoDaoOpe.getInstance().query(mContext, user_id);
                    if (infoList != null){
                        adapter = new FanAdapter(mContext, infoList);
                        fan_list.setLayoutManager(new LinearLayoutManager(mContext));
                        fan_list.setAdapter(adapter);
                        mHandler.sendEmptyMessage(LOADDATA_OK);
                    }else {
                        mHandler.sendEmptyMessage(LOADDATA_FAIL);
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
                Log.d(TAG, "cancel_attention position="+position);
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
        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
