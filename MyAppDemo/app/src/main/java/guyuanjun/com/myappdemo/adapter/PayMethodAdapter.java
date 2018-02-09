package guyuanjun.com.myappdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.utils.LogUtil;

/**
 * Created by HP on 2017-8-21.
 */

public class PayMethodAdapter extends BaseAdapter{
    private Context mContext;
    private List<String> methods;
    private LayoutInflater inflater;

    public PayMethodAdapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public PayMethodAdapter(Context context, List<String> methods){
        mContext = context;
        this.methods = methods;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return methods == null ? 0:methods.size();
    }

    @Override
    public Object getItem(int position) {
        return methods == null ? null:methods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView != null){
            holder = (ViewHolder)convertView.getTag();
        }else {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.select_pay_method_item, null);
            holder.item_layout = (LinearLayout)convertView.findViewById(R.id.item_layout);
            holder.pay_method = (TextView)convertView.findViewById(R.id.pay_method);
            holder.select = (ImageView)convertView.findViewById(R.id.select);
            convertView.setTag(holder);
        }

        holder.pay_method.setText(methods.get(position));
/*        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.d("item_layout", "position = ");
            }
        });*/
/*        holder.item_layout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    holder.select.setVisibility(View.VISIBLE);
                }else {
                    holder.select.setVisibility(View.INVISIBLE);
                }
            }
        });*/
        return convertView;
    }

    static class ViewHolder{
        LinearLayout item_layout;
        TextView pay_method;
        ImageView select;
    }
}
