package guyuanjun.com.myappdemo.fragment.news;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.CommentAdapter;
import guyuanjun.com.myappdemo.adapter.item.GridViewAdapter;
import guyuanjun.com.myappdemo.bean.AnswearInfo;
import guyuanjun.com.myappdemo.bean.BeanConstant;
import guyuanjun.com.myappdemo.bean.CollectInfo;
import guyuanjun.com.myappdemo.bean.CommentInfo;
import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.bean.WeitoutiaoInfo;
import guyuanjun.com.myappdemo.db.AnswearInfoDaoOpe;
import guyuanjun.com.myappdemo.db.CollectInfoDaoOpe;
import guyuanjun.com.myappdemo.db.CommentInfoDaoOpe;
import guyuanjun.com.myappdemo.db.NewsItemInfoDaoOpe;
import guyuanjun.com.myappdemo.db.WeitoutiaoInfoDaoOpe;
import guyuanjun.com.myappdemo.picture.MyPhotoActivity;
import guyuanjun.com.myappdemo.user.LoginActivity;
import guyuanjun.com.myappdemo.adapter.RecyclerViewDivider;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LoadCode;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.PrefUtils;
import guyuanjun.com.myappdemo.utils.Utils;

public class ItemViewer extends AppCompatActivity {
    private static final String TAG = ItemViewer.class.getSimpleName();
    private Context mContext = ItemViewer.this;

    private TextView where;
    private TextView tv_content;
    //private ImageView news_img;
    private GridView pic_gridview;
    private TextView comment_num;
    private Button submit;
    private EditText comment;
    private EditText comment_area;
    private ImageView talk;
    private ImageView collect;
    private ImageView forward;
    private RelativeLayout bottom;
    private RelativeLayout layout_comment;
    private LinearLayout empty_layout;
    private LinearLayout loading_layout;

    //处理回复信息
    private RelativeLayout answear_layout;
    private EditText et_answear_content;
    private Button btn_answear;

    private long user_id;
    private long item_id;
    private List<CollectInfo> collectInfoList;
    private List<CommentInfo> commentInfoList;
    private RecyclerView comment_list;
    private CommentAdapter adapter;

    private boolean flag = false;
    private String where_str = null; //来源
    private String content_str = null; //文字内容
    private String img_path_str = null; //图片路径

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case LoadCode.SUCCESS:
                    loading_layout.setVisibility(View.GONE);
                    LogUtil.d(TAG, "load SUCCESS");
                    //计算评论数
                    computeCommentNum();

                    if (commentInfoList != null && commentInfoList.isEmpty()) {
                        empty_layout.setVisibility(View.VISIBLE);
                    } else {
                        adapter = new CommentAdapter(mContext, commentInfoList);
                        comment_list.setAdapter(adapter);
                    }
                    //comment_list.setVisibility(View.VISIBLE);
                    //adapter.notifyDataSetChanged();
                    //adapter.notify();

                    break;
                case LoadCode.FAIL:
                    loading_layout.setVisibility(View.GONE);
                    break;
                case LoadCode.LOADING:
                    loading_layout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_news_item);
        initView();
        initData();
        listener();
    }

    private void initView() {
        where = (TextView) findViewById(R.id.where);
        tv_content = (TextView) findViewById(R.id.tv_content);
        //news_img = (ImageView) findViewById(R.id.news_img);
        pic_gridview = (GridView) findViewById(R.id.pic_gridview);//显示图片集
        comment_num = (TextView) findViewById(R.id.comment_num);
        comment = (EditText) findViewById(R.id.comment);
        comment_area = (EditText) findViewById(R.id.comment_area);
        talk = (ImageView) findViewById(R.id.talk);
        collect = (ImageView) findViewById(R.id.collect);
        forward = (ImageView) findViewById(R.id.forward);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        layout_comment = (RelativeLayout) findViewById(R.id.layout_comment);
        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);
        loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        submit = (Button) findViewById(R.id.submit);
        comment_list = (RecyclerView) findViewById(R.id.comment_list);

        //处理回复信息
        answear_layout = (RelativeLayout) findViewById(R.id.answear_layout2);
        et_answear_content = (EditText) findViewById(R.id.et_answear_content2);
        btn_answear = (Button) findViewById(R.id.btn_answear2);
    }

    private void initData() {
        Log.d(TAG, " item_id = " + item_id + ", user_id = " + user_id);
        //loading_layout.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        item_id = intent.getLongExtra("item_id", 0);
        String type = intent.getStringExtra("intent_type");
        //NewsItemInfo newsItemInfo = (NewsItemInfo) intent.getSerializableExtra("NewsItemInfo");

        //user_id = intent.getLongExtra("user_id", 0);
        //Log.d(TAG, " item_id = "+item_id+", user_id = "+user_id);

        String sessionId = Utils.getInstance().getSessionFromLocal(mContext);
        if (sessionId != null) {
            user_id = PrefUtils.getLong(Constant.SAVE_USER_INFO_NAME, Constant.USER_ID_KEY, mContext);
        }

        Log.d(TAG, " item_id = " + item_id + ", user_id = " + user_id);


        if (type.equals(Constant.TYPE_NEWS)) {
            final List<NewsItemInfo> newsItemInfos = NewsItemInfoDaoOpe.getInstance().queryByItemId(mContext, item_id);
            if (!newsItemInfos.isEmpty()) {
                where_str = newsItemInfos.get(0).getWhere();
                content_str = newsItemInfos.get(0).getContent();
                img_path_str = newsItemInfos.get(0).getImg_path_list();
            }
        }else if (type.equals(Constant.TYPE_WEITOUTIAO)){
            final List<WeitoutiaoInfo> weitoutiaoInfos = WeitoutiaoInfoDaoOpe.getInstance().queryByItemId(mContext, item_id);
            if (!weitoutiaoInfos.isEmpty()) {
                where_str = weitoutiaoInfos.get(0).getWhere();
                content_str = weitoutiaoInfos.get(0).getContent();
                img_path_str = weitoutiaoInfos.get(0).getImg_path_list();
            }
        }

        //if(!newsItemInfos.isEmpty()) {
        if (where_str != null && !where_str.isEmpty())
            where.setText(where_str);
        if (content_str == null || content_str.isEmpty()) {
            //tv_content.setVisibility(View.GONE);
        } else {
            tv_content.setText(content_str);
            tv_content.setVisibility(View.VISIBLE);
        }

//            if (newsItemInfos.get(0).getFlag_type() == BeanConstant.WORD_TYPE) {
//                tv_content.setText(newsItemInfos.get(0).getContent());
//                tv_content.setVisibility(View.VISIBLE);
//            } else if (newsItemInfos.get(0).getFlag_type() == BeanConstant.IMG_TYPE) {
//                news_img.setImageBitmap(Utils.getInstance().getPicture(newsItemInfos.get(0).getImg_path()));
//                news_img.setVisibility(View.VISIBLE);
//            } else if (newsItemInfos.get(0).getFlag_type() == BeanConstant.WORD_IMG_TYPE) {
//                tv_content.setText(newsItemInfos.get(0).getContent());
//                tv_content.setVisibility(View.VISIBLE);
//                news_img.setImageBitmap(Utils.getInstance().getPicture(newsItemInfos.get(0).getImg_path()));
//                news_img.setVisibility(View.VISIBLE);
//            }

        if (Constant.mCanGetBitmapFromNetWork) {
            //holder.img.setVisibility(View.VISIBLE);
            //holder.img.setImageBitmap(Utils.getInstance().getPicture(mDatas.get(position).getImg_path()));
            LogUtil.d(TAG, "NewsAdapter getImg_path_list : " + img_path_str);
            GridViewAdapter gridViewAdapter = new GridViewAdapter(mContext, img_path_str);
            pic_gridview.setAdapter(gridViewAdapter);
            if (img_path_str == null || img_path_str.isEmpty()) {
                pic_gridview.setVisibility(View.GONE);
            }
            pic_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int gridview_position, long id) {
                    LogUtil.d(TAG, "position = " + gridview_position);
                    Intent intent = new Intent(mContext, MyPhotoActivity.class);
                    intent.putExtra("id", gridview_position);
                    String[] imgs = null;
                    if (img_path_str != null) {
                        imgs = img_path_str.split(GridViewAdapter.SPLIT_STR);
                        LogUtil.d(TAG, "img_path_list : " + img_path_str);
                    }
                    intent.putExtra("img_path_list", imgs);
                    mContext.startActivity(intent);
                }
            });
        }
        //}

        collectInfoList = CollectInfoDaoOpe.getInstance().query(mContext, user_id, item_id);
        for (int i = 0; i < collectInfoList.size(); i++) {
            flag = collectInfoList.get(i).getHas_collected();
        }
        if (flag) {
            collect.setImageResource(R.drawable.has_collected);
        }

//        commentInfoList = CommentInfoDaoOpe.getInstance().queryByItemId(mContext, item_id);
//        LogUtil.d(TAG, "load SUCCESS");
//        //计算评论数
//        computeCommentNum();


        adapter = new CommentAdapter(mContext, commentInfoList);
        comment_list.setLayoutManager(new LinearLayoutManager(mContext));
        //添加默认分割线：高度为2px，颜色为灰色
        //comment_list.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL));
        comment_list.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL, 5, R.color.blue));
        //comment_list.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        //为RecyclerView添加默认动画效果，测试不写也可以
        comment_list.setItemAnimator(new DefaultItemAnimator());
        comment_list.setAdapter(adapter);

        adapter.setCommentView(comment_list);
        adapter.setmOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //在此处处理 回复 列表
                Toast.makeText(mContext, "postion = " + position, Toast.LENGTH_SHORT).show();


            }
        });

        //加载评论内容
        loadData();
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Looper.prepare();
                mHandler.sendEmptyMessage(LoadCode.LOADING);
                commentInfoList = CommentInfoDaoOpe.getInstance().queryByItemId(mContext, item_id);
                mHandler.sendEmptyMessageDelayed(LoadCode.SUCCESS, 1000);
                //Looper.loop();
            }
        }).start();

    }

    private void listener() {
/*        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom.setVisibility(View.INVISIBLE);
                layout_comment.setVisibility(View.VISIBLE);
            }
        });*/
        comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    bottom.setVisibility(View.GONE);
                    layout_comment.setVisibility(View.VISIBLE);
                    comment_area.setFocusable(true);
                    comment_area.setText("");
                }
            }
        });

        comment_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sessionId = Utils.getInstance().getSessionFromLocal(mContext);
                if (sessionId == null) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                String _comment = comment_area.getText().toString().trim();
                Log.d(TAG, "_comment = " + _comment);
                Date comment_time = new Date();
                Log.d(TAG, "comment_time = " + comment_time);

                CommentInfo commentInfo = new CommentInfo(user_id, item_id, _comment, comment_time);
                CommentInfoDaoOpe.getInstance().insertData(mContext, commentInfo);


                //更新评论列表
                //loadData();
                //adapter.notifyDataSetChanged();
                //comment_list.
                //loadData();
                commentInfoList.add(commentInfo);
                Log.d(TAG, "commentInfoList.size() = " + (commentInfoList.size()));
                adapter.notifyItemInserted(commentInfoList.size() - 1);
                //adapter.notifyItemRangeChanged(commentInfoList.size()-1, adapter.getItemCount());
                Log.d(TAG, "commentInfoList.size()-1 = " + (commentInfoList.size() - 1));
                comment_list.scrollToPosition(commentInfoList.size() - 1);
                adapter.notifyDataSetChanged();

/*                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                },400);*/

                //更新评论数
                computeCommentNum();
                comment_area.setText("");

                bottom.setVisibility(View.VISIBLE);
                layout_comment.setVisibility(View.GONE);
                Toast.makeText(mContext, "评论成功!", Toast.LENGTH_SHORT).show();
            }
        });

        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_id == 0) { //没有登录
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginActivity.class);
                    startActivity(intent);
                } else { //已经登录

                    if (flag) {
                        collectInfoList = CollectInfoDaoOpe.getInstance().query(mContext, user_id, item_id);
                        for (int i = 0; i < collectInfoList.size(); i++) {
                            CollectInfoDaoOpe.getInstance().deleteDataByKey(mContext, collectInfoList.get(i).getId());
                        }
                        //CollectInfo collectInfo = new CollectInfo(user_id, item_id);
                        //CollectInfoDaoOpe.getInstance().deleteData(mContext, collectInfo);

                        collect.setImageResource(R.drawable.not_collect);
                        Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();
                        flag = false;
                    } else {
                        Date collect_time = new Date();
                        Log.d(TAG, "collect_time = " + collect_time);
                        CollectInfo collectInfo = new CollectInfo(user_id, item_id, collect_time, true);
                        CollectInfoDaoOpe.getInstance().insertData(mContext, collectInfo);
                        collect.setImageResource(R.drawable.has_collected);
                        Log.d(TAG, "CollectInfoDao collect_time = " + collectInfo.getCollect_time());
                        Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareContent();
            }
        });


    }

    private void shareContent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setType("image/*");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    private void computeCommentNum() {
        //计算评论数
        //commentInfoList = CommentInfoDaoOpe.getInstance().queryByItemId(mContext, item_id);
        if (commentInfoList.size() == 0)
            comment_num.setVisibility(View.INVISIBLE);
        else {
            comment_num.setVisibility(View.VISIBLE);
            comment_num.setText("" + commentInfoList.size());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (layout_comment.getVisibility() == View.VISIBLE) {
                layout_comment.setVisibility(View.GONE);
                bottom.setVisibility(View.VISIBLE);
                return true;
            } else if (bottom.getVisibility() == View.VISIBLE) {
                finish();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        loadData();
        super.onStart();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}
