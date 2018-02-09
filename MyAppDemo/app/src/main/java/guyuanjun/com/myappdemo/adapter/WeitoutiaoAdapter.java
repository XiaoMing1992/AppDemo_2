package guyuanjun.com.myappdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.adapter.item.GridViewAdapter;
import guyuanjun.com.myappdemo.bean.AttentionInfo;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.bean.WeitoutiaoInfo;
import guyuanjun.com.myappdemo.db.AttentionInfoDaoOpe;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.db.WeitoutiaoInfoDaoOpe;
import guyuanjun.com.myappdemo.picture.MyPhotoActivity;
import guyuanjun.com.myappdemo.utils.Constant;
import guyuanjun.com.myappdemo.utils.LogUtil;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2017-5-5.
 */

public class WeitoutiaoAdapter extends RecyclerView.Adapter<WeitoutiaoAdapter.AdapterViewHolder> {
    private final String TAG = "WeitoutiaoAdapter";

    private List<WeitoutiaoInfo> mDatas;
    private Context mContext;

    public WeitoutiaoAdapter(Context context, List<WeitoutiaoInfo> datas) {
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
        return mDatas == null ? 0 : position;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weitoutiao_layout_item, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder holder, final int position) {
        if (mDatas != null) {
            if (mDatas.get(position).getContent() != null && !mDatas.get(position).getContent().isEmpty()) {
                //显示内容
                holder.content_layout.setVisibility(View.VISIBLE);
                holder.content01.setText(mDatas.get(position).getContent());
            }

            //用户名
            holder.name.setText(mDatas.get(position).getUsername());
            //计算时间
            String timeStr = Utils.getInstance().computeTime(mDatas.get(position).getTime());
            holder.time.setText(timeStr);
            //显示头像
            List<UserInfo> uses = UserInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getUser_id());
            if (uses.size() == 0)
                holder.head_icon.setImageResource(R.drawable.head_icon);
            else {
                String head_path = uses.get(0).getHead_path();
                if (head_path == null) {
                    holder.head_icon.setImageResource(R.drawable.head_icon);
                } else
                    holder.head_icon.setImageBitmap(BitmapFactory.decodeFile(head_path));
            }

            if (Constant.mCanGetBitmapFromNetWork) { //
                //显示图片
//                String img_path = mDatas.get(position).getImg_path();
//                holder.picture.setImageBitmap(BitmapFactory.decodeFile(img_path));
                LogUtil.d(TAG, "WeitoutiaoAdapter getImg_path_list : "+mDatas.get(position).getImg_path_list());
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

            //监听 关注 点击事件
            List<AttentionInfo> attentionInfos = AttentionInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getUser_id(),
                    mDatas.get(position).getId());
            if (attentionInfos != null) {
                if (attentionInfos.size() != 0) {
                    boolean has_attention = attentionInfos.get(0).getHas_attentioned();
                    if (has_attention) {
                        holder.attention.setText("已关注");
                    }
                } else {
                    holder.attention.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AttentionInfo attentionInfo = new AttentionInfo(mDatas.get(position).getUser_id(),
                                    mDatas.get(position).getId(), new Date(), true);
                            int resultCode = AttentionInfoDaoOpe.getInstance().insertData(mContext, attentionInfo);
                            if (resultCode == 200) {
                                holder.attention.setText("已关注");
                                changeAttention();
                            } else {
                                Toast.makeText(mContext, "关注失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            //显示阅读量
            holder.read_num.setText(Utils.getInstance().formatNum(mDatas.get(position).getRead_num()) + "阅读量");
            //显示赞数
            holder.praise_num.setText(Utils.getInstance().formatNum(mDatas.get(position).getPraise_num()) + "赞");
            //显示评论数
            holder.comment_num.setText(Utils.getInstance().formatNum(mDatas.get(position).getComment_num()) + "评论");

            //点击赞
            List<WeitoutiaoInfo> weitoutiaoInfos = WeitoutiaoInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getUser_id(),
                    mDatas.get(position).getId());
            if (weitoutiaoInfos != null) {
                if (weitoutiaoInfos.size() != 0) {
                    boolean has_attention = weitoutiaoInfos.get(0).getHas_praised();
                    if (has_attention) {
                        holder.img_praise.setImageResource(R.drawable.has_praised);
                        holder.tv_praise.setTextColor(R.color.blue);
                    }
                }
            }
            holder.praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<WeitoutiaoInfo> weitoutiaoInfos = WeitoutiaoInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getUser_id(),
                            mDatas.get(position).getId());
                    if (weitoutiaoInfos != null) {
                        if (weitoutiaoInfos.size() != 0) {
                            boolean has_attention = weitoutiaoInfos.get(0).getHas_praised();
                            if (has_attention) {
                                Toast.makeText(mContext, "已赞", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        List<WeitoutiaoInfo> infos = WeitoutiaoInfoDaoOpe.getInstance().queryByItemId(mContext,
                                mDatas.get(position).getId());
                        if (infos != null) {
                            if (infos.size() != 0) {
                                infos.get(0).setPraise_num(infos.get(0).getPraise_num() + 1);
                                int resultCode = WeitoutiaoInfoDaoOpe.getInstance().save(mContext, infos.get(0));
                                if (resultCode == 200) {
                                    holder.img_praise.setImageResource(R.drawable.has_praised);
                                    holder.tv_praise.setTextColor(R.color.blue);
                                    //显示赞数
                                    holder.praise_num.setText(Utils.getInstance().formatNum(infos.get(0).getPraise_num()) + "赞");
                                }
                            }
                        }
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

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView content01;
        ImageView head_icon;
        TextView name;
        TextView time;
        TextView attention;
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
        GridView pic_gridview;
        LinearLayout content_layout;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            content01 = (TextView) itemView.findViewById(R.id.content01);
            head_icon = (ImageView) itemView.findViewById(R.id.head_icon);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            attention = (TextView) itemView.findViewById(R.id.attention);
            read_num = (TextView) itemView.findViewById(R.id.read_num);
            praise_num = (TextView) itemView.findViewById(R.id.praise_num);
            comment_num = (TextView) itemView.findViewById(R.id.comment_num);

            tv_praise = (TextView) itemView.findViewById(R.id.tv_praise);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            img_praise = (ImageView) itemView.findViewById(R.id.img_praise);
            img_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            praise = (RelativeLayout) itemView.findViewById(R.id.praise);
            comment = (RelativeLayout) itemView.findViewById(R.id.comment);

            pic_gridview = (GridView)itemView.findViewById(R.id.pic_gridview);
            content_layout = (LinearLayout)itemView.findViewById(R.id.content_layout);
        }
    }

    public void setmDatas(final List<WeitoutiaoInfo> datas) {
        this.mDatas = datas;
    }

    public List<WeitoutiaoInfo> getmDatas() {
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

    private void changeAttention() {
        EventBus.getDefault().post("change_attention");
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


