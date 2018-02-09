package guyuanjun.com.myappdemo.fragment.my.store.mobile;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.OrderDetailAdapter;
import guyuanjun.com.myappdemo.bean.MobileInfo;
import guyuanjun.com.myappdemo.db.MobileInfoDaoOpe;
import guyuanjun.com.myappdemo.utils.MyUtils;

public class OrderDetailActivity extends AppCompatActivity {
    private static final String TAG = "OrderDetailActivity";
    private Context mContext = OrderDetailActivity.this;

    private RecyclerView order_list;
    private EditText phone;
    private EditText order_id;
    private Button query_order;
    private ProgressBar progress;
    private LinearLayout empty_layout;

    private OrderDetailAdapter adapter;
    private List<MobileInfo> datas;

    private final int LOAD_FAIL = 0x00;
    private final int LOAD_OK = 0x01;
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what){
                case LOAD_FAIL:
                    Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                    break;
                case LOAD_OK:
                    if (datas.isEmpty()){
                        empty_layout.setVisibility(View.VISIBLE);
                    }else {
                        adapter.notifyDataSetChanged();
                        Toast.makeText(mContext, "加载完成", Toast.LENGTH_SHORT).show();
                    }
                    progress.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        initData();
        listener();
    }

    private void initView(){
        order_list = (RecyclerView)findViewById(R.id.order_list);
        phone = (EditText)findViewById(R.id.phone);
        order_id = (EditText)findViewById(R.id.order_id);
        query_order = (Button)findViewById(R.id.query_order);
        progress = (ProgressBar)findViewById(R.id.progress);
        empty_layout = (LinearLayout)findViewById(R.id.empty_layout);
    }

    private void initData(){
        adapter = new OrderDetailAdapter(mContext, datas);
        order_list.setLayoutManager(new LinearLayoutManager(mContext));
        order_list.setAdapter(adapter);
    }

    private void loadData(final String phone_str){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    datas = MobileInfoDaoOpe.getInstance().query(mContext, phone_str);
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(LOAD_OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(LOAD_FAIL);
                }
            }
        }).start();
    }
    private void loadData(final String phone_str, final String order_id_str){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    datas = MobileInfoDaoOpe.getInstance().query(mContext, phone_str, order_id_str);
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(LOAD_OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(LOAD_FAIL);
                }
            }
        }).start();
    }

    private void listener(){
        query_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_str = phone.getText().toString().trim();
                if (MyUtils.isMobileNO(phone_str)){
                    String order_id_str = order_id.getText().toString().trim();
                    progress.setVisibility(View.VISIBLE);
                    if (order_id_str != null && !order_id_str.equals("")){
                        loadData(phone_str, order_id_str);
                    }else {
                        loadData(phone_str);
                    }
                }else {
                    Toast.makeText(mContext, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
