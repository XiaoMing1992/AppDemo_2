package guyuanjun.com.myappdemo.fragment.my.store.music;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2018-4-8.
 *
 * @Description: 音频文件帮助类
 */

public class AudioUtils {

    /**
     * 获取sd卡所有的音乐文件
     *
     * @return
     * @throws Exception
     */
    public static List<Song> getAllSongs(Context context) {

        List<Song> songs = null;

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.YEAR,
                        MediaStore.Audio.Media.MIME_TYPE,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[]{"audio/mpeg", "audio/x-ms-wma"}, null);

        songs = new ArrayList<Song>();

        if (cursor.moveToFirst()) {

            Song song = null;

            do {
                song = new Song();
                // 文件名
                song.setFileName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                // 歌曲名
                song.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                // 时长
                song.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                // 歌手名
                song.setSinger(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                // 专辑名
                song.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                // 年代
                if (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)) != null) {
                    song.setYear(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
                } else {
                    song.setYear("未知");
                }
                // 歌曲格式
                if ("audio/mpeg".equals(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)).trim())) {
                    song.setType("mp3");
                } else if ("audio/x-ms-wma".equals(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)).trim())) {
                    song.setType("wma");
                }
                // 文件大小
                if (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)) != null) {
                    float size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)) / 1024f / 1024f;
                    song.setSize((size + "").substring(0, 4) + "M");
                } else {
                    song.setSize("未知");
                }
                // 文件路径
                if (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)) != null) {
                    song.setFileUrl(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                }
                songs.add(song);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return songs;
    }
}
