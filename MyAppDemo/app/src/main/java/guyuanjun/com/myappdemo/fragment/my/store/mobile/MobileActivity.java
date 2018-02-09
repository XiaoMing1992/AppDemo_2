package guyuanjun.com.myappdemo.fragment.my.store.mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.IEvent;
import guyuanjun.com.myappdemo.adapter.PayMethodAdapter;
import guyuanjun.com.myappdemo.bean.MobileInfo;
import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.db.MobileInfoDaoOpe;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.MyUtils;
import guyuanjun.com.myappdemo.utils.PrefUtils;

public class MobileActivity extends AppCompatActivity {
    private static final String TAG = MobileActivity.class.getSimpleName();
    private Context mContext = MobileActivity.this;

    private EditText phone;
    private GridView gridView;
    private Button zhangdan;

    private SimpleAdapter adapter;
    private List<Map<String, Object>> datas;
    private String[] normal_price = new String[]{"10元", "20元", "30元", "50元", "100元", "200元", "300元", "500元"};
    private String[] sell_price = new String[]{"售价:9.98元", "售价:19.96元", "售价:29.94元", "售价:49.90元", "售价:99.80元",
            "售价:199.60元", "售价:299.40元", "售价:499.00元"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        initView();
        initData();
        listener();
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        gridView = (GridView) findViewById(R.id.gridview);
        zhangdan = (Button) findViewById(R.id.zhangdan);
    }

    private void initData() {
        loadData();
        adapter = new SimpleAdapter(mContext, datas, R.layout.activity_mobile_item,
                new String[]{"normal_price", "sell_price"}, new int[]{R.id.normal_price, R.id.sell_price});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //获取某个item的内容
                Map<String, Object> item = (HashMap<String, Object>) adapterView.getItemAtPosition(position);
                //记录所选Item的sell_price
                String sell_price = (String) item.get("sell_price");
                String normal_price = (String) item.get("normal_price");
                showPopwindow(normal_price.substring(3, normal_price.length()), sell_price.substring(3, sell_price.length()));
            }
        });
    }

    private void loadData() {
        datas = new ArrayList<>();
        Map<String, Object> data = null;
        for (int i = 0; i < normal_price.length; i++) {
            data = new HashMap<>();
            data.put("normal_price", normal_price[i]);
            data.put("sell_price", sell_price[i]);
            datas.add(data);
        }
    }

    private void listener() {
        zhangdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, OrderDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private TextView pay_method;

    /**
     * 显示popupWindow
     */
    private void showPopwindow(final String normal_price, final String sell_price) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chongzhi_detail, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.AnimationBottomFade);
        // 在底部显示
        window.showAtLocation(findViewById(R.id.gridview),
                Gravity.BOTTOM, 0, 0);

        ImageView close = (ImageView) view.findViewById(R.id.close);
        TextView order = (TextView) view.findViewById(R.id.order);
        pay_method = (TextView) view.findViewById(R.id.pay_method);
        final TextView money = (TextView) view.findViewById(R.id.money);
        Button submit = (Button) view.findViewById(R.id.submit);
        RelativeLayout pay_method_layout = (RelativeLayout) view.findViewById(R.id.pay_method_layout);

        order.setText("中国移动充值 订单号是00000001");
        pay_method.setText("余额宝");
        money.setText(sell_price);

        pay_method_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopwindow2();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_num = phone.getText().toString().trim();
                if (MyUtils.isMobileNO(phone_num)) {
                    if (ShoujiHuaFei.telCheck(phone_num, Integer.valueOf(normal_price)) == 0) {
                        Log.d(TAG, "手机号" + phone_num + "可以充值");

                        String orderid = "2014123115121";
                        String result = ShoujiHuaFei.onlineOrder(phone_num, Integer.valueOf(normal_price), orderid);
                        if (result != null) {
                            int error_code = JSONObject.fromObject(result).getInt("error_code");
                            if (error_code == 0) {
                                Toast.makeText(mContext, "充值中", Toast.LENGTH_SHORT).show();
                            } else if (error_code == 1) {
                                Toast.makeText(mContext, "充值成功", Toast.LENGTH_SHORT).show();
                                JSONObject result_object = JSONObject.fromObject(result).getJSONObject("result");

                                Date time = new Date();
                                //MobileInfo mobileInfo = new MobileInfo(phone_num, orderid, time);
                                MobileInfo mobileInfo = new MobileInfo(time);
                                mobileInfo.setPhone(result_object.getString("game_userid"));
                                mobileInfo.setOrder_id(result_object.getString("uorderid"));
                                mobileInfo.setState(result_object.getInt("game_state"));
                                mobileInfo.setUordercash("" + result_object.get("ordercash"));
                                mobileInfo.setSporder_id(result_object.getString("sporder_id"));
                                MobileInfoDaoOpe.getInstance().insertData(mContext, mobileInfo);

                            } else if (error_code == 9) {
                                Toast.makeText(mContext, "充值撤销", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "充值失败", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d(TAG, "手机号" + phone_num + "不可以充值");
                    }
                } else {
                    Toast.makeText(mContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                }
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }

            }
        });
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });

    }

    private List<String> methods;
    private int pre_position = 0;

    /**
     * 显示popupWindow
     */
    private void showPopwindow2() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.select_pay_method, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.AnimationRightFade);
        // 在底部显示
        window.showAtLocation(findViewById(R.id.gridview),
                Gravity.BOTTOM, 0, 0);

        ImageView close = (ImageView) view.findViewById(R.id.close);
        ListView listView = (ListView) view.findViewById(R.id.mehtod_list);
        PayMethodAdapter adapter = new PayMethodAdapter(mContext, getMethods());
        listView.setAdapter(adapter);

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        if (window != null && window.isShowing()) {
                            window.dismiss();
                        }
                        break;
                }
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //LogUtil.d(TAG, "position = "+position+" content = "+methods.get(position));
                adapterView.getChildAt(pre_position).findViewById(R.id.select).setVisibility(View.INVISIBLE);
                pre_position = position;
                view.findViewById(R.id.select).setVisibility(View.VISIBLE);
                pay_method.setText(((TextView) view.findViewById(R.id.pay_method)).getText());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(0);
                    }
                }, 1000);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }
            }
        });
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });


    }

    private List<String> getMethods() {
        methods = new ArrayList<>();
        methods.add("余额宝");
        methods.add("银行卡1");
        methods.add("银行卡2");
        methods.add("银行卡3");
        methods.add("银行卡4");
        return methods;
    }
}
