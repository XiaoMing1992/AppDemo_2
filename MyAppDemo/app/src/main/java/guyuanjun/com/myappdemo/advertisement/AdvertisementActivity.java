package guyuanjun.com.myappdemo.advertisement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import guyuanjun.com.myappdemo.MainActivity;
import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.event.AdverEvent;
import guyuanjun.com.myappdemo.fragment.news.ItemViewer;
import guyuanjun.com.myappdemo.http.HttpHelper;
import guyuanjun.com.myappdemo.index.RoleActivity;
import guyuanjun.com.myappdemo.user.LoginActivity;
import guyuanjun.com.myappdemo.user.RegisterFirst;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LoadCode;
import guyuanjun.com.myappdemo.utils.Utils;
import guyuanjun.com.myappdemo.vedio_module.util.NetWorkUtil;

public class AdvertisementActivity extends AppCompatActivity {
    private TextView skip;
    //private Button open;
    private ImageView advertisement;

    private Context mContext = AdvertisementActivity.this;
    private HttpHelper mHttpHelper;

    private final int UPDATE_TIME = 0x01;
    private final int TIME_OVER = 0x02;

    private int advertiseTime;
    private final int DELAY_TIME = 1000;
    private final int TOTAL_ADVERTIMENT_TIME = 5; //广告时间为5秒

    private boolean isSkip = false;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TIME:
                    skip.setText(getResources().getString(R.string.skip) + " " + advertiseTime + "s");
                    break;
                case TIME_OVER:
                   /* if (NetWorkUtil.isWifiConnected(mContext)){
                        boolean _continue = Utils.getInstance().chooseWifi(mContext);
                        if (!_continue){
                            Process.killProcess(Process.myPid());
                        }else {
                            Intent toMainActivity = new Intent();
                            toMainActivity.setClass(mContext, MainActivity.class);
                            startActivity(toMainActivity);
                            AdvertisementActivity.this.finish();
                        }
                    }else {
                        Intent toMainActivity = new Intent();
                        toMainActivity.setClass(mContext, MainActivity.class);
                        startActivity(toMainActivity);
                        AdvertisementActivity.this.finish();
                    }*/

                    Intent toMainActivity = new Intent();
                    //toMainActivity.setClass(mContext, MainActivity.class);
                    toMainActivity.setClass(mContext, RoleActivity.class);
                    startActivity(toMainActivity);
                    AdvertisementActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertisement);
        intiView();

        //更新广告背景
        initBackground();
        //监听跳过按钮
        clickSkip();
        //监听立即体验按钮
        clickOpen();
        //计算广告时间
        computeTime();
    }

    /**
     * 初始化控件
     */
    private void intiView() {
        skip = (TextView) findViewById(R.id.skip);
        //open = (Button) findViewById(R.id.open);
        advertisement = (ImageView)findViewById(R.id.advertisement);
    }

    private void clickSkip() {
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSkip = true;
                Intent toMainActivity = new Intent();
                //toMainActivity.setClass(mContext, MainActivity.class);
                toMainActivity.setClass(mContext, RoleActivity.class);
                startActivity(toMainActivity);
                AdvertisementActivity.this.finish();
            }
        });
    }

    private void clickOpen() {
//        open.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isSkip = true;
//                Intent toMain2Activity = new Intent();
//                toMain2Activity.setClass(mContext, RegisterFirst.class);
//                startActivity(toMain2Activity);
//                AdvertisementActivity.this.finish();
//            }
//        });

        advertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSkip = true;
                Intent toMain2Activity = new Intent();
                //toMain2Activity.setClass(mContext, RegisterFirst.class);
                toMain2Activity.setClass(mContext, ItemViewer.class);
                toMain2Activity.putExtra("item_id", 1);
                toMain2Activity.putExtra("intent_type", Constant.TYPE_NEWS);
                startActivity(toMain2Activity);
                AdvertisementActivity.this.finish();
            }
        });
    }

    private void computeTime() {
        advertiseTime = TOTAL_ADVERTIMENT_TIME;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    do {
                        Message msg = new Message();
                        msg.what = UPDATE_TIME;
                        mHandler.sendMessage(msg);

                        Thread.currentThread().sleep(DELAY_TIME);
                        advertiseTime -= 1;
                    } while (advertiseTime >= 1 && advertiseTime <= TOTAL_ADVERTIMENT_TIME);

                    if (advertiseTime < 1 && !isSkip){
                        Message msg = new Message();
                        msg.what = TIME_OVER;
                        mHandler.sendMessage(msg);
                        System.out.println("here");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initData(String url) {
//        mHandler.sendEmptyMessage(LoadCode.LOADING);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                //打印请求返回结果
                Log.e("volley", result);
                //jieJson(testDatas());
                fromJson(result);
                if(adverEvent.getRetBean().getRet_code().equals("1")){
                    String img_path = adverEvent.getData().getImg_path();
                    if (img_path != null && !img_path.isEmpty()){
                        loadPicture(img_path);
                    }
                }

//                Message msg = new Message();
//                msg.what = LoadCode.SUCCESS;
//                mHandler.sendMessageDelayed(msg, 300);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyerror", "erro2");

//                Message msg = new Message();
//                msg.what = LoadCode.FAIL;
//                mHandler.sendMessage(msg);
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

    private AdverEvent adverEvent = null;
    private void fromJson(final String res) {
        //创建一个Gson对象
        Gson gson = new Gson();
        adverEvent = gson.fromJson(res, AdverEvent.class);
    }

    private String URL = null;
    private void initBackground(){
        mHttpHelper = HttpHelper.getInstance(mContext);

        loadPicture("http://b.hiphotos.baidu.com/zhidao/pic/item/a6efce1b9d16fdfafee0cfb5b68f8c5495ee7bd8.jpg");
    }

    private void loadPicture(final String url){
        try {
/*            boolean needDownload = true;
            //boolean needDownload = downloadNewestPicture(TestApplication.mApp.pictureinfos.get(i));
            mHttpHelper.loadAdvertisement(advertisement, R.drawable.image_default, R.drawable.image_default,
                    "http://b.hiphotos.baidu.com/zhidao/pic/item/a6efce1b9d16fdfafee0cfb5b68f8c5495ee7bd8.jpg"
                    *//*TestApplication.mApp.pictureinfos.get(i).getImageUrl()*//*,needDownload);
            //Log.i(TAG, "易互动主页图地址: " + TestApplication.mApp.pictureinfos.get(i).getImageUrl());*/

            Utils.getInstance().downloadPicture(mContext, url,
                    R.drawable.image_default, R.drawable.image_default,advertisement);
        } catch (OutOfMemoryError error) {
            //Log.e(TAG, "OutOfMemoryError: " + TestApplication.mApp.pictureinfos.get(i).getImageUrl());
            advertisement.setImageResource(R.drawable.image_default);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent toMainActivity = new Intent();
        toMainActivity.setClass(mContext, MainActivity.class);
        startActivity(toMainActivity);
        AdvertisementActivity.this.finish();
    }
}

