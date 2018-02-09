package guyuanjun.com.myappdemo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.AttentionInfo;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.bean.WeitoutiaoInfo;
import guyuanjun.com.myappdemo.db.AttentionInfoDaoOpe;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.db.WeitoutiaoInfoDaoOpe;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2017-5-12.
 */

public class MyAttentionAdapter extends RecyclerView.Adapter<MyAttentionAdapter.AdapterViewHolder> {
    private final String TAG = "MyAttentionAdapter";

    private List<AttentionInfo> mDatas;
    private Context mContext;

    public MyAttentionAdapter(Context context, List<AttentionInfo> datas) {
        mDatas = datas;
        mContext = context;
    }

    //点击
    private MyAttentionAdapter.OnItemClickListener mOnItemClickListener;
    //长按
    private MyAttentionAdapter.OnItemLongClickListener mOnItemLongClickListener;

    //点击
    public void setmOnItemClickListener(MyAttentionAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //长按
    public void setmOnItemLongClickListener(MyAttentionAdapter.OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? -1 : mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return mDatas == null ? -1 : position;
    }

    @Override
    public MyAttentionAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_attention_item, parent, false);
        return new MyAttentionAdapter.AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyAttentionAdapter.AdapterViewHolder holder, final int position) {
        if (mDatas != null) {
            final List<WeitoutiaoInfo> infos = WeitoutiaoInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getUser_id(), mDatas.get(position).getItem_id());
            if (infos != null && infos.size() != 0) {
                //显示内容
                holder.content01.setText(infos.get(0).getContent());
                //用户名
                holder.name.setText(infos.get(0).getUsername());
                //计算时间
                String timeStr = Utils.getInstance().computeTime(infos.get(0).getTime());
                holder.time.setText(timeStr);
                //显示头像
                List<UserInfo> uses = UserInfoDaoOpe.getInstance().query(mContext, infos.get(0).getUser_id());
                if (uses.size() == 0)
                    holder.head_icon.setImageResource(R.drawable.head_icon);
                else {
                    String head_path = uses.get(0).getHead_path();
                    if (head_path == null) {
                        holder.head_icon.setImageResource(R.drawable.head_icon);
                    } else
                        holder.head_icon.setImageBitmap(BitmapFactory.decodeFile(head_path));
                }
                //显示图片
                String img_path = infos.get(0).getImg_path();
                holder.picture.setImageBitmap(BitmapFactory.decodeFile(img_path));

                holder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int resultCode = AttentionInfoDaoOpe.getInstance().deleteDataByKey(mContext, mDatas.get(position).getId());
                        if (resultCode == 200) {
                            Toast.makeText(mContext, "取消关注成功 id=" + mDatas.get(position).getId(), Toast.LENGTH_SHORT).show();
                            cancelAttention(position);
                        } else {
                            Toast.makeText(mContext, "取消关注失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //显示阅读量
                holder.read_num.setText(Utils.getInstance().formatNum(infos.get(0).getRead_num()) + "阅读量");
                //显示赞数
                holder.praise_num.setText(Utils.getInstance().formatNum(infos.get(0).getPraise_num()) + "赞");
                //显示评论数
                holder.comment_num.setText(Utils.getInstance().formatNum(infos.get(0).getComment_num()) + "评论");

                //点击赞
                boolean has_attention = infos.get(0).getHas_praised();
                if (has_attention) {
                    holder.img_praise.setImageResource(R.drawable.has_praised);
                    holder.tv_praise.setTextColor(R.color.blue);
                }

                holder.praise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean has_attention = infos.get(0).getHas_praised();
                        if (has_attention) {
                            Toast.makeText(mContext, "已赞", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        infos.get(0).setPraise_num(infos.get(0).getPraise_num() + 1);
                        int resultCode = WeitoutiaoInfoDaoOpe.getInstance().save(mContext, infos.get(0));
                        if (resultCode == 200) {
                            holder.img_praise.setImageResource(R.drawable.has_praised);
                            holder.tv_praise.setTextColor(R.color.blue);
                            //显示赞数
                            holder.praise_num.setText(Utils.getInstance().formatNum(infos.get(0).getPraise_num()) + "赞");
                        }
                    }
                });


                //点击评论
                holder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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
            }
        }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView content01;
        ImageView head_icon;
        TextView name;
        TextView time;
        TextView cancel;
        TextView read_num;
        TextView praise_num;
        TextView comment_num;
        ImageView picture;

        ImageView img_praise;
        TextView tv_praise;
        ImageView img_comment;
        TextView tv_comment;
        RelativeLayout praise;
        RelativeLayout comment;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            content01 = (TextView) itemView.findViewById(R.id.content01);
            head_icon = (ImageView) itemView.findViewById(R.id.head_icon);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            cancel = (TextView) itemView.findViewById(R.id.cancel);
            read_num = (TextView) itemView.findViewById(R.id.read_num);
            praise_num = (TextView) itemView.findViewById(R.id.praise_num);
            comment_num = (TextView) itemView.findViewById(R.id.comment_num);

            tv_praise = (TextView) itemView.findViewById(R.id.tv_praise);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            img_praise = (ImageView) itemView.findViewById(R.id.img_praise);
            img_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            praise = (RelativeLayout) itemView.findViewById(R.id.praise);
            comment = (RelativeLayout) itemView.findViewById(R.id.comment);
        }
    }

    public void setmDatas(final List<AttentionInfo> datas) {
        this.mDatas = datas;
    }

    public List<AttentionInfo> getmDatas() {
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

    private void cancelAttention(final int position) {
        Bundle bundle = new Bundle();
        bundle.putString("event", "cancel_attention");
        bundle.putInt("position", position);
        EventBus.getDefault().post(bundle);
        //EventBus.getDefault().post("cancel_attention");
    }

    private void changePraise() {
        EventBus.getDefault().post("change_praise");
    }

    private void changeReadNum() {
        EventBus.getDefault().post("change_read_num");
    }

    private void changeComment() {
        EventBus.getDefault().post("change_comment");
    }
}