package guyuanjun.com.myappdemo.fragment.my.store;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.dialog.LoginByPhone;
import guyuanjun.com.myappdemo.fragment.my.store.bill.BillActivity;
import guyuanjun.com.myappdemo.fragment.my.store.game.GameActivity;
import guyuanjun.com.myappdemo.fragment.my.store.invitation.InvitationActivity;
import guyuanjun.com.myappdemo.fragment.my.store.mobile.MobileActivity;
import guyuanjun.com.myappdemo.fragment.my.store.movie.MovieActivity;
import guyuanjun.com.myappdemo.fragment.my.store.music.MusicActivity;
import guyuanjun.com.myappdemo.fragment.my.store.sale.SaleActivity;
import guyuanjun.com.myappdemo.fragment.my.store.tv.TVActivity;
import guyuanjun.com.myappdemo.utils.Utils;

public class MyStore extends AppCompatActivity {

    private static final String TAG = "MyStore";
    private Context mContext = MyStore.this;

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);
        initView();
        initData();
    }

    private void initView(){
        gridView = (GridView)findViewById(R.id.gridview);
    }

    private void initData(){
        gridView.setAdapter(new MyGridAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
/*                //获取某个item的内容
                Map<String, Object> item= (HashMap<String, Object>) adapterView.getItemAtPosition(position);
                //显示所选Item的ItemText
                setTitle((String)item.get("ItemText"));*/
                if (position == 0) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, SaleActivity.class);
                    startActivity(intent);
                }else if (position == 1) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, MovieActivity.class);
                    startActivity(intent);
                }else if (position == 2) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, TVActivity.class);
                    startActivity(intent);
                }else if (position == 3) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, MusicActivity.class);
                    startActivity(intent);
                }else if (position == 4) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, GameActivity.class);
                    startActivity(intent);
                } else if (position == 5){
                    if (!hasLoaded(mContext)) //没有登录
                        return;
                    Intent intent = new Intent();
                    intent.setClass(mContext, InvitationActivity.class);
                    startActivity(intent);
                } else if (position == 6){
                    if (!hasLoaded(mContext)) //没有登录
                        return;
                    Intent intent = new Intent();
                    intent.setClass(mContext, BillActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent();
                    intent.setClass(mContext, MobileActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean hasLoaded(Context context) {
        String sessionId = Utils.getInstance().getSessionFromLocal(context);
        if (sessionId == null) {
            Log.d(TAG, "sessionId = " + sessionId);
            LoginByPhone loginByPhone = new LoginByPhone(context);
            loginByPhone.show();
            return false;
        }
        return true;
    }
}
