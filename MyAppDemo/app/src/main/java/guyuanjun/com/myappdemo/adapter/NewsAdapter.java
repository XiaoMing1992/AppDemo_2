package guyuanjun.com.myappdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.item.GridViewAdapter;
import guyuanjun.com.myappdemo.bean.BeanConstant;
import guyuanjun.com.myappdemo.bean.CommentInfo;
import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.db.CommentInfoDaoOpe;
import guyuanjun.com.myappdemo.picture.MyPhotoActivity;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2017-3-16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private final String TAG = "NewsAdapter";

    private List<NewsItemInfo> mDatas;
    private Context mContext;

    public NewsAdapter(Context context, List<NewsItemInfo> datas) {
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
        return mDatas == null ? 0: mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return mDatas == null ? 0: position;
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsAdapterViewHolder holder, final int position) {
        holder.title.setText(mDatas.get(position).getTitle());
        holder.where.setText(mDatas.get(position).getWhere());
        //获取评论数
        List<CommentInfo> commentInfoList = CommentInfoDaoOpe.getInstance().queryByItemId(mContext, mDatas.get(position).getId());
        holder.comment.setText("" + commentInfoList.size()+"评论");
        //计算时间
        String timeStr = computeTime(mDatas.get(position).getTime());
        holder.time.setText(timeStr);
        //根据类型来显示内容
        int flag = mDatas.get(position).getFlag_type();
        Log.d(TAG, "flag = "+flag);
//        if (flag == BeanConstant.WORD_TYPE){               //文字类型
//            holder.content.setVisibility(View.VISIBLE);
//            holder.content.setText(mDatas.get(position).getContent());
//        }else if (flag == BeanConstant.IMG_TYPE){          //图片类型
//            if (Constant.mCanGetBitmapFromNetWork) {
//                holder.img.setVisibility(View.VISIBLE);
//                holder.img.setImageBitmap(Utils.getInstance().getPicture(mDatas.get(position).getImg_path()));
//            }
//        }else if (flag == BeanConstant.WORD_IMG_TYPE){     //文字+图片类型
//
//            holder.content.setVisibility(View.VISIBLE);
//            holder.content.setText(mDatas.get(position).getContent());
//
//            if (Constant.mCanGetBitmapFromNetWork) {
//                holder.img.setVisibility(View.VISIBLE);
//                holder.img.setImageBitmap(Utils.getInstance().getPicture(mDatas.get(position).getImg_path()));
//            }
//        }

        if (mDatas.get(position).getContent() != null && !mDatas.get(position).getContent().isEmpty()) {
            //显示内容
            holder.content_layout.setVisibility(View.VISIBLE);
            holder.content.setText(mDatas.get(position).getContent());
        }
        if (Constant.mCanGetBitmapFromNetWork) {
            //holder.img.setVisibility(View.VISIBLE);
            //holder.img.setImageBitmap(Utils.getInstance().getPicture(mDatas.get(position).getImg_path()));
            LogUtil.d(TAG, "NewsAdapter getImg_path_list : "+mDatas.get(position).getImg_path_list());
            GridViewAdapter gridViewAdapter = new GridViewAdapter(mContext, mDatas.get(position).getImg_path_list());
            holder.pic_gridview.setAdapter(gridViewAdapter);
            if (mDatas.get(position).getImg_path_list() == null || mDatas.get(position).getImg_path_list().isEmpty()){
                holder.pic_gridview.setVisibility(View.GONE);
            }
            holder.pic_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int gridview_position, long id) {
                    LogUtil.d(TAG, "position = "+gridview_position);
                    Intent intent = new Intent(mContext, MyPhotoActivity.class);
                    intent.putExtra("id", gridview_position);
                    String[] imgs = null;
                    if (mDatas.get(position).getImg_path_list() != null) {
                        imgs = mDatas.get(position).getImg_path_list().split(GridViewAdapter.SPLIT_STR);
                        LogUtil.d(TAG, "img_path_list : "+mDatas.get(position).getImg_path_list());
                    }
                    intent.putExtra("img_path_list", imgs);
                    mContext.startActivity(intent);
                }
            });
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

    public static class NewsAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView where;
        TextView comment;
        TextView time;
        TextView content;
        //ImageView img;
        GridView pic_gridview;
        LinearLayout content_layout;

        public NewsAdapterViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            where = (TextView) itemView.findViewById(R.id.where);
            comment = (TextView) itemView.findViewById(R.id.comment);
            content = (TextView) itemView.findViewById(R.id.content);
            time = (TextView) itemView.findViewById(R.id.time);
            //img = (ImageView) itemView.findViewById(R.id.img);
            pic_gridview = (GridView)itemView.findViewById(R.id.pic_gridview);//显示图片集
            content_layout = (LinearLayout)itemView.findViewById(R.id.content_layout);
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
