package guyuanjun.com.myappdemo.fragment.my.store.music;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.utils.Utils;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    private WordView mWordView;
    private List mTimeList;

    private TextView song_name;
    private TextView singer_name;
    private TextView cur_pro;
    private TextView total_pro;
    private SeekBar progress;
    private ImageView previous;
    private ImageView play;
    private ImageView next;
    private ImageView music_foreground;

    private LrcHandle lrcHandler;

    private ListView song_list;
    private MusicAdapter musicAdapter;
    private List<Song> songs;
    private static int currentPosition = 0;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
//                   if (mPlayer.isPlaying())
//                   mWordView.invalidate();

                    //cur_pro.setText(mPlayer.getCurrentPosition());
                    //cur_pro.invalidate();

/*                   progress.setProgress(mPlayer.getCurrentPosition());
                   progress.invalidate();*/

                    System.out.println("MusicService.getCurrentPosition() = "
                            + MusicService.getCurrentPosition()+" MusicService.getDuration() = "
                            +MusicService.getDuration());
                    progress.setProgress(MusicService.getCurrentPosition());
                    //progress.invalidate();

                    cur_pro.setText(Utils.getInstance().formatTime(MusicService.getCurrentPosition()));

                    if (MusicService.isPlay()) {
                        //if (!animator.isRunning()) {
                            //if (animator.isPaused())
                             //   animator.resume();//恢复动画
                            //else
                        if (animator.isPaused()) {
                            animator.start(); //开始动画
                            play.setImageResource(R.drawable.play);
                        }
                        //}
                    }
                    break;

                case 0x02:
                    //if (!MusicService.isPlay()) {
                        if (animator.isRunning()) {
                            animator.pause();
                            play.setImageResource(R.drawable.stop);
                        }
                    //}
                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {

                if (MusicService.getCurrentPosition() > 0){
                    if (!MusicService.isPlay()) {
                        handler.sendEmptyMessage(0x02);
                    }else{
                        handler.sendEmptyMessage(0x01);
                    }

                }else{
                    if (MusicService.isPlay()) {
                        handler.sendEmptyMessage(0x01);
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //handler.postDelayed(this, 1000);
        }
    };

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        initView();

/*        new Thread(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                while (mPlayer.isPlaying()) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            mWordView.invalidate();
                        }
                    });
                    try {
                        Thread.sleep(((int)mTimeList.get(i + 1) - (int)mTimeList.get(i)));
                    } catch (InterruptedException e) {
                    }
                    i++;
                    if (i == mTimeList.size() - 1) {
                        mPlayer.stop();
                        break;
                    }
                }
            }
        }).start();*/
        //initMediaPlayer("/storage/extSdCard/music/2728244.lrc", "/storage/extSdCard/music/21751125560064.mp3");

        initData();
        progressListener();

        //playListener();
        //handler.postDelayed(runnable, 1000);
        //handler.post(runnable);

        new Thread(runnable).start();
    }

    private void initView() {
        //mWordView = (WordView) findViewById(R.id.text);
        music_foreground = (ImageView) findViewById(R.id.music_foreground);
        song_name = (TextView) findViewById(R.id.song_name);
        singer_name = (TextView) findViewById(R.id.singer_name);
        cur_pro = (TextView) findViewById(R.id.cur_pro);
        total_pro = (TextView) findViewById(R.id.total_pro);

        progress = (SeekBar) findViewById(R.id.progress);

        previous = (ImageView) findViewById(R.id.previous);
        play = (ImageView) findViewById(R.id.play);
        next = (ImageView) findViewById(R.id.next);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        song_list = (ListView) findViewById(R.id.song_list);
    }

    private void initData() {
        Animator(); //设置属性动画

        songs = AudioUtils.getAllSongs(this);
        musicAdapter = new MusicAdapter(this, songs);
        song_list.setAdapter(musicAdapter);
        song_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (songs != null && songs.size() != 0) {
                    currentPosition = position;
                    mSongUrl = songs.get(position).getFileUrl();
                    setSong();
                    System.out.println("music position = " + position + " mSongUrl = " + mSongUrl);
                    startService();
                }
            }
        });
        if (songs != null && songs.size() != 0) {
            //currentPosition = 0;
            System.out.println("init currentPosition = "+currentPosition);
            mSongUrl = songs.get(currentPosition).getFileUrl();
            setSong();
        }
    }


    private void setSong() {
        cur_pro.setText("00:00");
        total_pro.setText(Utils.getInstance().formatTime(songs.get(currentPosition).getDuration()));
        song_name.setText(songs.get(currentPosition).getTitle());
        singer_name.setText(songs.get(currentPosition).getSinger());
        progress.setMax(songs.get(currentPosition).getDuration());

        if (MusicService.isPlay()) {
            if (animator.isPaused())
                animator.start(); //开始动画
            play.setImageResource(R.drawable.play);
        } else {
            if (animator.isRunning())
                animator.pause();//暂停动画
            play.setImageResource(R.drawable.stop);
        }


        //System.out.println("MusicService.getDuration() = "+MusicService.getDuration());

        //handler.removeCallbacks(runnable);
        //new Thread(runnable).start();
    }

    private ObjectAnimator animator;

    private void Animator() {
        animator = ObjectAnimator.ofFloat(music_foreground, "rotation", 0f, 360.0f);
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
    }

    private void nextSong() {
        currentPosition = (currentPosition + 1) % songs.size();
        mSongUrl = songs.get(currentPosition).getFileUrl();
        System.out.println("music currentPosition = " + currentPosition + " mSongUrl = " + mSongUrl);
        startService();
        setSong();
    }

    private void previousSong() {
        if (currentPosition - 1 < 0)
            currentPosition = songs.size() + currentPosition - 1;
        else
            currentPosition = (currentPosition - 1) % songs.size();
        mSongUrl = songs.get(currentPosition).getFileUrl();
        System.out.println("music currentPosition = " + currentPosition + " mSongUrl = " + mSongUrl);
        startService();
        setSong();
    }

    private void stopService() {
        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);
        stopService(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                System.out.println("---- click play" + " mSongUrl = " + mSongUrl);
                if (MusicService.isPlay()) {
                    MusicService.pause();
                    if (animator.isRunning())
                        animator.pause();//暂停动画
                    play.setImageResource(R.drawable.stop);
                } else {
                    //startService();
                    MusicService.start();
                    if (animator.isPaused())
                        animator.resume();//恢复动画
                    else
                        animator.start(); //开始动画
                    play.setImageResource(R.drawable.play);
                }
                break;

            case R.id.next:
                nextSong();
                break;

            case R.id.previous:
                previousSong();
                break;
        }
    }

    private void progressListener() {
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("onProgressChanged progress = " + progress);
                //MusicService.setSeekBar(progress);
                //cur_pro.setText(Utils.getInstance().formatTime(MusicService.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                MusicService.setSeekBar(progress);
                cur_pro.setText(Utils.getInstance().formatTime(MusicService.getCurrentPosition()));
                System.out.println("onStopTrackingTouch progress = " + progress);
            }
        });
    }

/*    public MusicService musicService;
    private MusicService.MyBinder mBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //Log.w(TAG, "in MyServiceConnection onServiceConnected");
            System.out.println("in MyServiceConnection onServiceConnected");
            mBinder = (MusicService.MyBinder) iBinder;
            musicService = mBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            System.out.println("in MyServiceConnection onServiceDisconnected");
        }
    };*/


    public void playListener() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*                        new Thread(new Runnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                while (mPlayer.isPlaying()) {
                                    //handler.post(runnable);
                                    handler.sendEmptyMessage(1);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                    }
                                    i++;
                                    if (mPlayer == null) {
                                        mPlayer.stop();
                                        break;
                                    }
                                }
                            }
                        }).start();*/


/*                        long delay = 1000;
                        handler.postDelayed(runnable, delay);*/

            }
        });

    }

    private void initMediaPlayer(String word_url, String song_url) {
/*        if (mPlayer.isPlaying())
            mPlayer.reset();*/


        lrcHandler = new LrcHandle();
        try {
            //lrcHandler.readLRC("/storage/extSdCard/music/2728244.lrc");
            lrcHandler.readLRC(word_url);
            mTimeList = lrcHandler.getTime();

/*            //File file = new File("/storage/extSdCard/music/21751125560064.mp3");
            File file = new File(song_url);
            FileInputStream fis = new FileInputStream(file);

            mPlayer.setDataSource(fis.getFD());
            mPlayer.prepare();*/
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(this);
    }

    private String mSongUrl;

    private void startService() {
        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);
        intent.putExtra("songUrl", mSongUrl);
        startService(intent);
    }

}
