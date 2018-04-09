package guyuanjun.com.myappdemo.fragment.my.store.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.utils.Utils;

/**
 * Created by HP on 2018-4-8.
 */

public class MusicAdapter extends BaseAdapter {

    private List<Song> mSongs = null;
    private Context mContext;

    public MusicAdapter(Context context, List<Song> songs){
        this.mSongs = songs;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return this.mSongs == null ? 0 : this.mSongs.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mSongs == null ? null:this.mSongs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int postion, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.music_item, parent, false);
            holder = new ViewHolder();
            holder.song_name = (TextView)convertView.findViewById(R.id.song_name);
            holder.song_duration = (TextView)convertView.findViewById(R.id.song_duration);
            holder.song_singer = (TextView)convertView.findViewById(R.id.song_singer);
            holder.song_path = (TextView)convertView.findViewById(R.id.song_path);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (mSongs != null) {
            holder.song_duration.setText(Utils.getInstance().formatTime(mSongs.get(postion).getDuration()));
            holder.song_name.setText(mSongs.get(postion).getTitle());
            holder.song_path.setText(mSongs.get(postion).getFileUrl());
            holder.song_singer.setText(mSongs.get(postion).getSinger());
        }
        return convertView;
    }

    class ViewHolder{
        TextView song_name;
        TextView song_singer;
        TextView song_duration;
        TextView song_path;
    }
}
