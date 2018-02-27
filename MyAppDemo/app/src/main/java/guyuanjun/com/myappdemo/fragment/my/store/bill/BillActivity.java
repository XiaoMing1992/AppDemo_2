package guyuanjun.com.myappdemo.fragment.my.store.bill;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.client.methods.RequestBuilder;
import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.utils.LoadCode;
import guyuanjun.com.myappdemo.utils.LogUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BillActivity extends AppCompatActivity  implements View.OnClickListener{
    private final String TAG = BillActivity.class.getSimpleName();

    private LinearLayout refresh_layout;
    private OkHttpClient client;
    private Request request;
    private Response response;

    private final String URL = "https://www.baidu.com/";

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LoadCode.SUCCESS:
//                    if (response.isSuccessful()){
//                        Toast.makeText(BillActivity.this, response.body().string(), Toast.LENGTH_LONG).show();
//                        LogUtil.d(TAG, "cache response: "+response.cacheResponse());
//                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initView();
        initData();
    }

    private void initView(){
        refresh_layout = (LinearLayout)findViewById(R.id.refresh_layout);
        refresh_layout.setOnClickListener(this);
    }

    private void initData(){
        client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        request = new Request.Builder()
                .url(URL)
                .build();
        load();
    }

    private void load(){
          client.newCall(request).enqueue(new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                  //Toast.makeText(BillActivity.this, "!!!", Toast.LENGTH_LONG).show();
                  System.out.println("cache response: "+"!!!");
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                  //Toast.makeText(BillActivity.this, response.body().toString(), Toast.LENGTH_LONG).show();
                  //LogUtil.d(TAG, "cache response: "+response.cacheResponse());
                  System.out.println("cache response: "+response.cacheResponse()+" network response:"+ response.networkResponse());
                  System.out.println(response.body().string());
              }
          });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    response = client.newCall(request).execute();
//                    mHandler.sendEmptyMessage(LoadCode.SUCCESS);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refresh_layout:
                request = new Request.Builder()
                        .url(URL)
                        //.addHeader("Cache-Control", "max-age=0")
                        .build();
                load();
//                if (response.isSuccessful()){
//                    //Toast.makeText(this, response.body().toString(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(this, "cache response: "+response.cacheResponse(), Toast.LENGTH_SHORT).show();
//                }
                //Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
