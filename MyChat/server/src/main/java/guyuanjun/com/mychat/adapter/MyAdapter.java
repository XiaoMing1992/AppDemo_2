package guyuanjun.com.mychat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import guyuanjun.com.mychat.R;
import guyuanjun.com.mychat.model.MyMessage;

/**
 * Created by HP on 2018-3-1.
 */

public class MyAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    //private List<Map<String, ?>> mData;
    private List<MyMessage> mData;

    public MyAdapter(Context context, final List<MyMessage> data) {
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item, parent, false);
            holder = new Holder();
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();

        }
        String msg = mData.get(position).getMsg();
        //int id = ((Integer) mData.get(position).get("id")).intValue();
        int id = 0;
        if (id == 0) {
            holder.left_tv = (TextView) convertView.findViewById(R.id.left_tv);
            holder.left_tv.setVisibility(View.VISIBLE);
            if (holder.right_tv != null) holder.right_tv.setVisibility(View.GONE);

            holder.left_tv.setText(msg);
        } else if (id == 1) {
            holder.right_tv = (TextView) convertView.findViewById(R.id.right_tv);
            holder.right_tv.setVisibility(View.VISIBLE);
            if (holder.left_tv != null) holder.left_tv.setVisibility(View.GONE);
            holder.right_tv.setText(msg);
        }

        String timeStr = mData.get(position).getTime();
        holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
        holder.time_tv.setVisibility(View.VISIBLE);
        holder.time_tv.setText(timeStr);

        return convertView;
    }

    class Holder {
        TextView left_tv;
        TextView right_tv;
        TextView time_tv;
    }
}
