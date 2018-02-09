package guyuanjun.com.myappdemo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.AttentionInfo;
import guyuanjun.com.myappdemo.bean.FanInfo;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.db.AttentionInfoDaoOpe;
import guyuanjun.com.myappdemo.db.FanInfoDaoOpe;
import guyuanjun.com.myappdemo.db.UserInfoDaoOpe;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2017-5-13.
 */

public class FanAdapter extends RecyclerView.Adapter<FanAdapter.AdapterViewHolder> {
    private final String TAG = "MyAttentionAdapter";

    private List<FanInfo> mDatas;
    private Context mContext;

    public FanAdapter(Context context, List<FanInfo> datas) {
        mDatas = datas;
        mContext = context;
    }

    //点击
    private FanAdapter.OnItemClickListener mOnItemClickListener;
    //长按
    private FanAdapter.OnItemLongClickListener mOnItemLongClickListener;

    //点击
    public void setmOnItemClickListener(FanAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //长按
    public void setmOnItemLongClickListener(FanAdapter.OnItemLongClickListener mOnItemLongClickListener) {
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
    public FanAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_fan_item, parent, false);
        return new FanAdapter.AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FanAdapter.AdapterViewHolder holder, final int position) {
        if (mDatas != null) {
            //final List<FanInfo> infos = FanInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getHost_id(), mDatas.get(position).getFan_id());
            List<UserInfo> infos = UserInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getFan_id());
            if (infos != null && infos.size() != 0) {
                //List<UserInfo> userInfos = UserInfoDaoOpe.getInstance().query(mContext, infos.get(0).getFan_id());
                //用户名
                holder.name.setText(infos.get(0).getUsername());
                String head_path = infos.get(0).getHead_path();
                if (head_path == null) {
                    holder.head_icon.setImageResource(R.drawable.head_icon);
                } else
                    holder.head_icon.setImageBitmap(BitmapFactory.decodeFile(head_path));

                //显示粉丝数
                List<FanInfo> fan_info = FanInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getFan_id());
                if (fan_info != null) {
                    holder.fan_num.setText(Utils.getInstance().formatNum(fan_info.size()));
                }else {
                    holder.fan_num.setText("0");
                }
                //显示关注数
                List<AttentionInfo> attention_info = AttentionInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getFan_id());
                if (attention_info != null) {
                    holder.attention_num.setText(Utils.getInstance().formatNum(attention_info.size()));
                }else {
                    holder.attention_num.setText("0");
                }

                List<AttentionInfo> attentionInfos = AttentionInfoDaoOpe.getInstance().query(mContext, mDatas.get(position).getHost_id(),
                        mDatas.get(position).getFan_id());

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
                                AttentionInfo attentionInfo = new AttentionInfo(mDatas.get(position).getHost_id(),
                                        mDatas.get(position).getFan_id(), new Date(), true);
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

        ImageView head_icon;
        TextView name;
        TextView fan_num;
        TextView attention;
        TextView attention_num;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            attention = (TextView) itemView.findViewById(R.id.attention);
            head_icon = (ImageView) itemView.findViewById(R.id.head_icon);
            attention_num = (TextView) itemView.findViewById(R.id.attention_num);
            name = (TextView) itemView.findViewById(R.id.name);
            fan_num = (TextView) itemView.findViewById(R.id.fan_num);
        }
    }

    public void setmDatas(final List<FanInfo> datas) {
        this.mDatas = datas;
    }

    public List<FanInfo> getmDatas() {
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
