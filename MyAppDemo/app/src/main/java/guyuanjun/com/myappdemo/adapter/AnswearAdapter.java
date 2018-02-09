package guyuanjun.com.myappdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.bean.AnswearInfo;
import guyuanjun.com.myappdemo.utils.LogUtil;

/**
 * Created by HP on 2017-6-20.
 */

public class AnswearAdapter  extends RecyclerView.Adapter<AnswearAdapter.AdapterViewHolder> {
    private final String TAG = "AnswearAdapter";

    private List<AnswearInfo>mDatas;

    public AnswearAdapter(){

    }

    public AnswearAdapter(List<AnswearInfo>datas){
        mDatas = datas;
    }

    //点击
    private AnswearAdapter.OnItemClickListener mOnItemClickListener;
    //长按
    private AnswearAdapter.OnItemLongClickListener mOnItemLongClickListener;

    //点击
    public void setmOnItemClickListener(AnswearAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //长按
    public void setmOnItemLongClickListener(AnswearAdapter.OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public AnswearAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_answear_item, parent, false);
        return new AnswearAdapter.AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnswearAdapter.AdapterViewHolder holder, int position) {
        LogUtil.d(TAG, "Host_nickname = "+mDatas.get(position).getHost_nickname());
        LogUtil.d(TAG, "Answear_nickname = "+mDatas.get(position).getAnswear_nickname());
        LogUtil.d(TAG, "Answear_content = "+mDatas.get(position).getAnswear_content());

        holder.answear1.setText(mDatas.get(position).getHost_nickname());
        if (mDatas.get(position).getAnswear_nickname() == null){
            holder.tip.setVisibility(View.GONE);
            holder.answear2.setVisibility(View.GONE);
        }else {
            holder.tip.setVisibility(View.VISIBLE);
            holder.answear2.setText(mDatas.get(position).getAnswear_nickname());
        }
        holder.answear_content.setText(mDatas.get(position).getAnswear_content());



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

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView answear1;
        TextView answear2;
        TextView answear_content;
        TextView tip;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            answear1 = (TextView) itemView.findViewById(R.id.answear1);
            answear2 = (TextView) itemView.findViewById(R.id.answear2);
            answear_content = (TextView) itemView.findViewById(R.id.answear_content);
            tip = (TextView)itemView.findViewById(R.id.tip);
        }
    }

    //点击监听接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //长按监听接口
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}
