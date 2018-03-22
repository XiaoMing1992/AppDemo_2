package guyuanjun.com.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import guyuanjun.com.client.R;


/**
 * Created by HP on 2018-3-1.
 */

public class MyAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Map<String, ?>> mData;

    public MyAdapter(Context context, final List<Map<String, ?>> data) {
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
        String content = (String) mData.get(position).get("content");
        int id = ((Integer) mData.get(position).get("id")).intValue();
        if (id == 0) {
            holder.left_tv = (TextView) convertView.findViewById(R.id.left_tv);
            holder.left_tv.setVisibility(View.VISIBLE);
            if (holder.right_tv != null) holder.right_tv.setVisibility(View.GONE);

            holder.left_tv.setText(content);
        } else if (id == 1) {
            holder.right_tv = (TextView) convertView.findViewById(R.id.right_tv);
            holder.right_tv.setVisibility(View.VISIBLE);
            if (holder.left_tv != null) holder.left_tv.setVisibility(View.GONE);
            holder.right_tv.setText(content);
        }
        return convertView;
    }

    class Holder {
        TextView left_tv;
        TextView right_tv;
    }
}
