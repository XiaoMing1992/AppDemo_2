package guyuanjun.com.myappdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.MobileInfo;
import guyuanjun.com.myappdemo.db.MobileInfoDaoOpe;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2017-5-3.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.AdapterViewHolder> {
    private final String TAG = "OrderDetailAdapter";

    private List<MobileInfo> mDatas;
    private Context mContext;

    public OrderDetailAdapter(Context context, List<MobileInfo> datas) {
        mDatas = datas;
        mContext = context;
    }

    //点击
    private OrderDetailAdapter.OnItemClickListener mOnItemClickListener;
    //长按
    private OrderDetailAdapter.OnItemLongClickListener mOnItemLongClickListener;

    //点击
    public void setmOnItemClickListener(OrderDetailAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //长按
    public void setmOnItemLongClickListener(OrderDetailAdapter.OnItemLongClickListener mOnItemLongClickListener) {
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
    public OrderDetailAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);
        return new OrderDetailAdapter.AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderDetailAdapter.AdapterViewHolder holder, int position) {
        if (mDatas != null) {
            holder.sporder_id.setText(mDatas.get(position).getSporder_id());
            //计算充了多少钱
            holder.uordercash.setText(mDatas.get(position).getUordercash());
            //计算时间
            String timeStr = Utils.getInstance().computeTime(mDatas.get(position).getChongzhi_time());
            holder.time.setText(timeStr);
            //显示充值状态
            int state = mDatas.get(position).getState();
            holder.game_state.setText(getState(state));

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

        TextView sporder_id;
        TextView uordercash;
        TextView game_state;
        TextView time;


        public AdapterViewHolder(View itemView) {
            super(itemView);
            sporder_id = (TextView) itemView.findViewById(R.id.sporder_id);
            uordercash = (TextView) itemView.findViewById(R.id.uordercash);
            game_state = (TextView) itemView.findViewById(R.id.game_state);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }

    public void setmDatas(final List<MobileInfo> datas){
        this.mDatas = datas;
    }

    public List<MobileInfo> getmDatas(){
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

    private String getState(int state){
        String state_str = null;
        switch (state){
            case 0:
                state_str = "充值中";
                break;
            case 1:
                state_str = "充值成功";
                break;
            case 9:
                state_str = "充值失败";
                break;
            default:
                state_str = "未进行充值活动";
                break;
        }
        return state_str;
    }
}

