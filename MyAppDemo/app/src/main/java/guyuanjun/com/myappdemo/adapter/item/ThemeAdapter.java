package guyuanjun.com.myappdemo.adapter.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import guyuanjun.com.myappdemo.R;

/**
 * Created by HP on 2017-3-26.
 */

public class ThemeAdapter extends BaseAdapter{

    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private List<String> data = null;

    public ThemeAdapter(Context context, List<String> data){
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0: data.size();
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null: data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.theme_lv_item, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.theme_type = (TextView) convertView.findViewById(R.id.theme_type);
            /**绑定ViewHolder对象*/
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        }
        /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
        holder.theme_type.setText(data.get(position));

        return null;
    }


     private class ViewHolder{
        private TextView theme_type;
    }
}