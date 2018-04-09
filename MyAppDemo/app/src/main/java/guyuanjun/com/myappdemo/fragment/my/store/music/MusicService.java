package guyuanjun.com.myappdemo.fragment.my.store.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by HP on 2018-4-4.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener {

    private static MediaPlayer mPlayer;
    private static String mSongUrl = null;

    private MyBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MusicService onCreate");
        init();//初始化
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mSongUrl = intent.getStringExtra("songUrl");
            if (mSongUrl != null && mPlayer != null) {
                System.out.println("mSongUrl : " + mSongUrl);
                prepare();
                start();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("MusicService onBind");
/*        if (intent != null) {
            mSongUrl = intent.getStringExtra("songUrl");
            if (mSongUrl != null) {
                System.out.println("mSongUrl : "+mSongUrl);
                prepare();
                start();
            }
        }*/
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("MusicService onDestroy");
        stop();
    }

    private void init() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }

//    private void initMediaPlayer(String word_url, String song_url) {
//
//        if (mPlayer.isPlaying())
//            mPlayer.reset();
//    }

    public void reset() {
        if (mPlayer != null/* && mPlayer.isPlaying()*/) {
            mPlayer.seekTo(0);
            mPlayer.reset();
        }
        //play(0);
    }

    public void prepare() {
        System.out.println("prepare : " + mSongUrl);
        try {
            reset();
            File file = new File(mSongUrl);
            FileInputStream fis = new FileInputStream(file);

            mPlayer.setDataSource(fis.getFD());
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pause() {
        System.out.println("pause : " + mSongUrl);
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    public static void start() {
        System.out.println("start : " + mSongUrl);
        if (mPlayer != null) {

            mPlayer.start();
        }
    }

    public void release() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying())
                mPlayer.stop();
            mPlayer.release();
            System.out.println("release : " + mSongUrl);
        }
    }

    public void stop() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying())
                mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            System.out.println("stop : " + mSongUrl);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        System.out.println("MusicService onPrepared");
        //prepare();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        System.out.println("MusicService onSeekComplete");
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        System.out.println("MusicService onCompletion");
        release();
        //stop();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        stop();
        return false;
    }

    public static boolean isPlay() {
        if (mPlayer != null && mPlayer.isPlaying()) return true;
        else return false;
    }

    public static int getDuration() {
        if (mPlayer != null)
            return mPlayer.getDuration();
        else return 0;
    }

    public static int getCurrentPosition() {
        if (mPlayer != null)
            return mPlayer.getCurrentPosition();
        else return 0;
    }

    public static void setSeekBar(int progress){
        if (mPlayer != null){
            mPlayer.seekTo(progress);
        }
    }

}
