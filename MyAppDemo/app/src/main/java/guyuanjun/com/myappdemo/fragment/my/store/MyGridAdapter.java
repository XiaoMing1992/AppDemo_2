package guyuanjun.com.myappdemo.fragment.my.store;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import guyuanjun.com.myappdemo.R;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams
 */
public class MyGridAdapter extends BaseAdapter {
	private Context mContext;

	private int[] icon = { R.drawable.seller, R.drawable.movie,
			R.drawable.tv, R.drawable.music,R.drawable.game,
			R.drawable.yaoqingyoujiang, R.drawable.my_order, R.drawable.shoujichongzhi};
	private String[] iconName = { "特卖", "电影", "电视剧", "音乐", "游戏", "邀请有奖", "我的订单", "手机充值" };

	public MyGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return iconName.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_my_store, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.text);
		ImageView iv = BaseViewHolder.get(convertView, R.id.img);
		iv.setImageResource(icon[position]);

		tv.setText(iconName[position]);
		return convertView;
	}

}
