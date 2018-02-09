package guyuanjun.com.myappdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.model.CompanyNewsItemInfo;

/**
 * Created by HP on 2017-3-19.
 */

public class CompanyNewsAdapter extends RecyclerView.Adapter<CompanyNewsAdapter.CompanyNewsAdapterViewHolder> {
    private List<CompanyNewsItemInfo> mDatas;

    public CompanyNewsAdapter(List<CompanyNewsItemInfo> datas) {
        mDatas = datas;
    }

    //点击
    private CompanyNewsAdapter.OnItemClickListener mOnItemClickListener;
    //长按
    private CompanyNewsAdapter.OnItemLongClickListener mOnItemLongClickListener;

    //点击
    public void setmOnItemClickListener(CompanyNewsAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //长按
    public void setmOnItemLongClickListener(CompanyNewsAdapter.OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CompanyNewsAdapter.CompanyNewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_news_item, parent, false);
        return new CompanyNewsAdapter.CompanyNewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CompanyNewsAdapter.CompanyNewsAdapterViewHolder holder, int position) {
        holder.title.setText(mDatas.get(position).getTitle());
/*        holder.where.setText(mDatas.get(position).getWhere());
        holder.comment.setText(mDatas.get(position).getComment());*/
        holder.time.setText(mDatas.get(position).getTime());;

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

    public static class CompanyNewsAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView time;

        public CompanyNewsAdapterViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
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
