package guyuanjun.com.myappdemo.fragment.my.history;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.fragment.my.history.history_fragment.LookupHistoryFragment;
import guyuanjun.com.myappdemo.fragment.my.history.history_fragment.PushHistoryFragment;
import guyuanjun.com.myappdemo.utils.LoadCode;

import com.sevenheaven.segmentcontrol.SegmentControl;

public class HistoryActivity extends AppCompatActivity{
    private static final String TAG = "HistoryActivity";
    private Context mContext = HistoryActivity.this;

    private SegmentControl mSegmentHorzontal;

    private View pushLayout;
    private View lookupLayout;

    /**
     * 推送历史
     */
    private PushHistoryFragment pushHistoryFragment = null;

    /**
     * 浏览历史
     */
    private LookupHistoryFragment lookupHistoryFragment = null;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    //开启一个Fragment事务
    FragmentTransaction fragmentTransaction;

    //private final int LOADDATA_FAIL = 0x00;
    //private final int LOADDATA_OK = 0x01;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LoadCode.FAIL:

                    break;
                case LoadCode.SUCCESS:
                    //fragmentTransaction.add(R.id.content, newsFragment);
                    fragmentTransaction.commit();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        initData();
    }

    private void initView(){
        mSegmentHorzontal = (SegmentControl)findViewById(R.id.segment_control);
        mSegmentHorzontal.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                Log.i(TAG, "onSegmentControlClick: index = " + index);
                setTabSelection(index);
            }
        });
    }

    private void initData(){
        fragmentManager = getFragmentManager();
        Intent intent = getIntent();
        final int id = intent.getIntExtra("fragment_type", 0);
        //第一次启动时选中第0个tab
        setTabSelection(id);
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *             每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置
     */
    private void setTabSelection(int index){
        //每次选中之前先清除掉上次的选中状态
        clearSelection();

        fragmentTransaction = fragmentManager.beginTransaction();
        //先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragmentTransaction);
        switch(index){
            case 1:
                //当点击了推送历史tab时
                if (pushHistoryFragment == null){
                    //如果PushHistoryFragment为空，则创建一个并添加到界面上
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            pushHistoryFragment = new PushHistoryFragment();
                            fragmentTransaction.add(R.id.content,pushHistoryFragment);
                            mHandler.sendEmptyMessage(LoadCode.SUCCESS);
                        }
                    }).start();

                }else{
                    //如果PushHistoryFragment不为空，则直接显示出来
                    fragmentTransaction.show(pushHistoryFragment);
                    fragmentTransaction.commit();
                }
                break;

            //当点击了浏览历史tab时
            case 0:
            default:
                if (lookupHistoryFragment == null){
                    //如果LookupHistoryFragment为空，则创建一个并添加到界面上
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            lookupHistoryFragment = new LookupHistoryFragment();
                            fragmentTransaction.add(R.id.content, lookupHistoryFragment);
                            mHandler.sendEmptyMessage(LoadCode.SUCCESS);
                        }
                    }).start();
                }else{
                    //如果LookupHistoryFragment不为空，则直接显示出来
                    fragmentTransaction.show(lookupHistoryFragment);
                    fragmentTransaction.commit();
                }
                break;
        }

    }

    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection(){

    }

    /**
     * 将所有的Fragment都设置为隐藏状态。
     *
     * @param fragmentTransaction
     *              用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction fragmentTransaction)
    {
        if(lookupHistoryFragment !=null){
            fragmentTransaction.hide(lookupHistoryFragment);
        }
        if (pushHistoryFragment !=null){
            fragmentTransaction.hide(pushHistoryFragment);
        }
    }

}

