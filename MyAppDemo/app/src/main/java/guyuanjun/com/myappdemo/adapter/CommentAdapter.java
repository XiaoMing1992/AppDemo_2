package guyuanjun.com.myappdemo.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.AnswearInfo;
import guyuanjun.com.myappdemo.bean.CommentInfo;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.AnswearInfoDaoOpe;
import guyuanjun.com.myappdemo.db.CommentInfoDaoOpe;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.model.NewsItemInfo;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.PrefUtils;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2017-4-17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentAdapterViewHolder> {
    private final String TAG = "CommentAdapter";
    private Context mContext;

    private List<CommentInfo> mDatas;
    private RecyclerView commentView;
    private long clickPosition = -1;

    public CommentAdapter(Context context, List<CommentInfo> datas) {
        mDatas = datas;
        mContext = context;
    }

    //点击
    private CommentAdapter.OnItemClickListener mOnItemClickListener;
    //长按
    private CommentAdapter.OnItemLongClickListener mOnItemLongClickListener;

    //点击
    public void setmOnItemClickListener(CommentAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //长按
    public void setmOnItemLongClickListener(CommentAdapter.OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CommentAdapter.CommentAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.CommentAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.CommentAdapterViewHolder holder, final int position) {
        holder.praise_num.setText(""+mDatas.get(position).getPraise_num());
        holder.comment.setText(mDatas.get(position).getComment());
        holder.time.setText(computeTime(mDatas.get(position).getComment_time()));

        final List<UserInfo> userInfos = UserInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getUser_id());
        holder.head_icon.setImageBitmap(Utils.getInstance().getPicture(userInfos.get(0).getHead_path()));
        holder.user_name.setText(userInfos.get(0).getUsername());

        //加载回复信息
        LogUtil.d(TAG, "user id="+mDatas.get(position).getUser_id()+"   item id="+mDatas.get(position).getId());
        final List<AnswearInfo> answearInfoList = AnswearInfoDaoOpe.getInstance().queryByItemId(mContext, mDatas.get(position).getId());
        LogUtil.d(TAG, "answearInfoList size="+answearInfoList.size());
        final AnswearAdapter answearAdapter = new AnswearAdapter(answearInfoList);
        holder.comment_many_to_many.setLayoutManager(new LinearLayoutManager(mContext));
        holder.comment_many_to_many.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL, 5, R.color.blue));
        holder.comment_many_to_many.setAdapter(answearAdapter);


        answearAdapter.setmOnItemClickListener(new AnswearAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
/*                commentView.findViewById(R.id.answear_layout2).setVisibility(View.VISIBLE);
                EditText WW = (EditText)commentView.findViewById(R.id.et_answear_content2);
                WW.setHint("回复"+answearInfoList.get(position).getHost_nickname());*/

                holder.answear_layout.setVisibility(View.VISIBLE);
               holder.et_answear_content.setHint("回复"+answearInfoList.get(position).getHost_nickname());

                LogUtil.d(TAG, "position = "+position+" click id ="+answearInfoList.get(position).getHost_id());
                LogUtil.d(TAG, "position = "+position+" click username ="+answearInfoList.get(position).getHost_nickname());
                clickPosition = position;
            }
        });

        //if (holder.answear_layout.getVisibility() == View.VISIBLE){
            holder.btn_answear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String content = holder.et_answear_content.getText().toString();

                    LogUtil.d(TAG, "insert AnswearInfo content="+content);

                    AnswearInfo answearInfo = new AnswearInfo();
                    answearInfo.setItem_id(mDatas.get(position).getId());

/*                    answearInfo.setAnswear_id(mDatas.get(position).getUser_id());
                    answearInfo.setAnswear_nickname(userInfos.get(0).getUsername());*/
                    if (clickPosition >=0) {
                        LogUtil.d(TAG, "clickPosition = " + clickPosition + " Answear_id =" + answearInfoList.get((int) clickPosition).getHost_id());
                        LogUtil.d(TAG, "clickPosition = " + clickPosition + " Answear_nickname =" + answearInfoList.get((int) clickPosition).getHost_nickname());
                        answearInfo.setAnswear_id(answearInfoList.get((int) clickPosition).getHost_id());
                        answearInfo.setAnswear_nickname(answearInfoList.get((int) clickPosition).getHost_nickname());
                    }else{
                        answearInfo.setAnswear_id(null);
                        answearInfo.setAnswear_nickname(null);
                    }

                    answearInfo.setHost_id(PrefUtils.getLong(Constant.SAVE_USER_INFO_NAME, Constant.USER_ID_KEY, mContext));
                    answearInfo.setHost_nickname(PrefUtils.getString(Constant.SAVE_USER_INFO_NAME, Constant.USER_NAME_KEY, mContext));
                    answearInfo.setAnswear_content(content);
                    int result = AnswearInfoDaoOpe.getInstance().insertData(mContext, answearInfo);
                    LogUtil.d(TAG, "insert AnswearInfo result="+result);

                    holder.et_answear_content.setText("");
                    holder.answear_layout.setVisibility(View.GONE);

                    answearInfoList.add(answearInfo);
                    answearAdapter.notifyItemInserted(answearInfoList.size()-1);
                }
            });
        //}

        if (mDatas.get(position).getHas_praise()) {
            holder.praise.setImageResource(R.drawable.has_praised);
        }
        final int _position = position;
        holder.praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<CommentInfo> commentInfoList = CommentInfoDaoOpe.getInstance().query(mContext,
                        mDatas.get(_position).getUser_id(), mDatas.get(_position).getItem_id());

                if (mDatas.get(_position).getHas_praise()) {  //取消点赞
                    holder.praise.setImageResource(R.drawable.not_praise);
                    commentInfoList.get(0).setHas_praise(false);
                    commentInfoList.get(0).setPraise_num(commentInfoList.get(0).getPraise_num()-1);
                }else { //点赞
                    holder.praise.setImageResource(R.drawable.has_praised);
                    commentInfoList.get(0).setHas_praise(true);
                    commentInfoList.get(0).setPraise_num(commentInfoList.get(0).getPraise_num()+1);
                }
                CommentInfoDaoOpe.getInstance().save(mContext, commentInfoList.get(0));//保存更新

                holder.praise_num.setText(""+commentInfoList.get(0).getPraise_num());
            }
        });

        //判断是否设置了监听器
        //单击判断
        if (mOnItemClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView, position); // 2
                }
            });
        }
        //长按判断
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.answear_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    public static class CommentAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView user_name;
        TextView praise_num;
        TextView comment;
        TextView time;
        ImageView head_icon;
        ImageView praise;
        RecyclerView comment_many_to_many;
        //处理回复信息
        RelativeLayout answear_layout;
        EditText et_answear_content;
        Button btn_answear;

        public CommentAdapterViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            praise_num = (TextView) itemView.findViewById(R.id.praise_num);
            comment = (TextView) itemView.findViewById(R.id.comment);
            time = (TextView) itemView.findViewById(R.id.time);
            head_icon = (ImageView) itemView.findViewById(R.id.head_icon);
            praise = (ImageView) itemView.findViewById(R.id.praise);

            comment_many_to_many = (RecyclerView)itemView.findViewById(R.id.comment_many_to_many);
            answear_layout = (RelativeLayout)itemView.findViewById(R.id.answear_layout);
            et_answear_content = (EditText)itemView.findViewById(R.id.et_answear_content);
            btn_answear = (Button)itemView.findViewById(R.id.btn_answear);
        }
    }

    public void setmDatas(final List<CommentInfo> datas){
        this.mDatas = datas;
    }

    public List<CommentInfo> getmDatas(){
        return this.mDatas;
    }

    //点击监听接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //长按监听接口
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private String computeTime(Date start){
        Date end = new Date();
        String timeStr = "";
        if ((end.getTime() - start.getTime()) > 1000 * 60 * 60 * 24){
            timeStr += Utils.getInstance().daysBetweenDate(start, end)+"天前";
        }else if ((end.getTime() - start.getTime()) > 1000 * 60 * 60){
            timeStr += Utils.getInstance().hoursBetweenDate(start, end)+"小时前";
        }else {
            timeStr += Utils.getInstance().minutesBetweenDate(start, end)+"分钟前";
        }
        return timeStr;
    }

    public void setCommentView(RecyclerView commentView){
        this.commentView = commentView;
    }


}
