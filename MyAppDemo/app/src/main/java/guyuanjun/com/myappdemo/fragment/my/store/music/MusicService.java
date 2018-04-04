package guyuanjun.com.myappdemo.fragment.my.store.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by HP on 2018-4-4.
 */

public class MusicService extends Service {

    private MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MusicService onCreate");

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("MusicService onDestroy");
        stop();
    }

    private void initMediaPlayer(String word_url, String song_url) {
        if (mPlayer.isPlaying())
            mPlayer.reset();
    }

    public void reset(){
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.seekTo(0);
            return;
        }
        //play(0);
    }

    public void stop(){
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
