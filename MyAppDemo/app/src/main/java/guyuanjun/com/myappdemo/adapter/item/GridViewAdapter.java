package guyuanjun.com.myappdemo.adapter.item;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.utils.LogUtil;

/**
 * Created by HP on 2017-11-23.
 */

public class GridViewAdapter extends BaseAdapter{
    private final String TAG = GridViewAdapter.class.getSimpleName();
    private Context mContext;
    private List<String> path_list = new ArrayList<>();
    public static final String SPLIT_STR = ";";

    public GridViewAdapter(Context context){
        this.mContext = context;
    }

    public GridViewAdapter(Context context, String img_path_list){
        this.mContext = context;
        if (img_path_list != null) {
            String[] imgs = img_path_list.split(SPLIT_STR);
            for (int i=0;i<imgs.length;i++){
                this.path_list.add(imgs[i]);
            }
            LogUtil.d(TAG, "img_path_list : "+img_path_list);
            LogUtil.d(TAG, "path_list : "+path_list.toString());
        }
    }

    @Override
    public int getCount() {
        return path_list != null ? path_list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return path_list != null ? path_list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_adapter, parent, false);
            holder = new ViewHolder();
            holder.img = (ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.img.setImageBitmap(BitmapFactory.decodeFile(path_list.get(position)));
        return convertView;
    }

    class ViewHolder{
        ImageView img;
    }
}
