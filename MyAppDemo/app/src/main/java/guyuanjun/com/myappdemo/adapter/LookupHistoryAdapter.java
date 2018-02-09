package guyuanjun.com.myappdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.CommentInfo;
import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.db.CommentInfoDaoOpe;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2017-4-25.
 */

public class LookupHistoryAdapter extends RecyclerView.Adapter<LookupHistoryAdapter.AdapterViewHolder> {
    private final String TAG = "LookupHistoryAdapter";

    private List<NewsItemInfo> mDatas;
    private Context mContext;

    public LookupHistoryAdapter(Context context, List<NewsItemInfo> datas) {
        mDatas = datas;
        mContext = context;
    }

    //点击
    private OnItemClickListener mOnItemClickListener;
    //长按
    private OnItemLongClickListener mOnItemLongClickListener;

    //点击
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //长按
    public void setmOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
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
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lookup_history_item, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder holder, int position) {
        holder.title.setText(mDatas.get(position).getTitle());
        holder.where.setText(mDatas.get(position).getWhere());
        //获取评论数
        List<CommentInfo> commentInfoList = CommentInfoDaoOpe.getInstance().queryByItemId(mContext, mDatas.get(position).getId());
        holder.comment_num.setText("" + commentInfoList.size()+"评论");
        //获取赞数
        holder.praise_num.setText("" + mDatas.get(position).getPraise_num()+"赞");
        //计算时间
        String timeStr = computeTime(mDatas.get(position).getTime());
        holder.time.setText(timeStr);
        //根据类型来显示内容
        int flag = mDatas.get(position).getFlag_type();
        Log.d(TAG, "flag = "+flag);
        if (flag == 0){
/*            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText(mDatas.get(position).getContent());*/
        }else if (flag == 1){
            if (Constant.mCanGetBitmapFromNetWork) { //
                holder.img.setVisibility(View.VISIBLE);
                holder.img.setImageBitmap(Utils.getInstance().getPicture(mDatas.get(position).getImg_path()));
            }
        }else if (flag == 2){

        }

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
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView where;
        TextView comment_num;
        TextView time;
        TextView praise_num;
        ImageView img;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            where = (TextView) itemView.findViewById(R.id.where);
            comment_num = (TextView) itemView.findViewById(R.id.comment_num);
            praise_num = (TextView) itemView.findViewById(R.id.praise_num);
            time = (TextView) itemView.findViewById(R.id.time);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

    public void setmDatas(final List<NewsItemInfo> datas){
        this.mDatas = datas;
    }

    public List<NewsItemInfo> getmDatas(){
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
}
