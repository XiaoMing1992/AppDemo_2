package guyuanjun.com.myappdemo.fragment.my.store.music;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.utils.Utils;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener{

    private WordView mWordView;
    private List mTimeList;
    private MediaPlayer mPlayer;

    private TextView song_name;
    private TextView singer_name;
    private TextView cur_pro;
    private TextView total_pro;
    private SeekBar progress;
    private ImageView previous;
    private ImageView play;
    private ImageView next;

    private  LrcHandle lrcHandler;

    final Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
           switch (msg.what){
               case 1:
                   if (mPlayer.isPlaying())
                   mWordView.invalidate();

                   //cur_pro.setText(mPlayer.getCurrentPosition());
                   //cur_pro.invalidate();

                   progress.setProgress(mPlayer.getCurrentPosition());
                   progress.invalidate();
                   break;

               case 2:
                   cur_pro.invalidate();
                   break;
           }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(1);
        }
    };

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        initView();
/*        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                finish();
            }
        });*/

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


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
        initMediaPlayer("/storage/extSdCard/music/2728244.lrc", "/storage/extSdCard/music/21751125560064.mp3");

        initData();

        playListener();
    }

    private void initView(){
        mWordView = (WordView) findViewById(R.id.text);

        song_name = (TextView)findViewById(R.id.song_name);
        singer_name = (TextView)findViewById(R.id.singer_name);
        cur_pro = (TextView)findViewById(R.id.cur_pro);
        total_pro = (TextView)findViewById(R.id.total_pro);

        progress = (SeekBar) findViewById(R.id.progress);

        previous = (ImageView) findViewById(R.id.previous);
        play = (ImageView) findViewById(R.id.play);
        next = (ImageView) findViewById(R.id.next);
    }

    private void initData(){
        total_pro.setText(Utils.getInstance().formatTime(mPlayer.getDuration()));
        cur_pro.setText("00:00");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    public void playListener(){
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayer !=null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                    play.setImageResource(R.drawable.stop);
                }else {
                    if (mPlayer !=null) {
                        mPlayer.start();
                        play.setImageResource(R.drawable.play);

                        new Thread(new Runnable() {
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
                        }).start();

/*                        long delay = 1000;
                        handler.postDelayed(runnable, delay);*/
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        try {

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayer.release();
                    mPlayer = null;
                }
            });

            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mPlayer.release();
                    mPlayer = null;
                    return false;
                }
            });

            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    progress.setMax(mPlayer.getDuration());
                   // mPlayer.start();
                    mPlayer.seekTo(mPlayer.getCurrentPosition());

/*                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isPlaying = true;
                            while(isPlaying){
                                progress.setProgress(mPlayer.getCurrentPosition());
                                SystemClock.sleep(500);
                            }
                        }
                    });*/
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.seekTo(progress);
                    cur_pro.setText(Utils.getInstance().formatTime(mPlayer.getCurrentPosition()));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.seekTo(progress);
                }
            }
        });
    }

    private void initMediaPlayer(String word_url, String song_url){
        if (mPlayer.isPlaying())
            mPlayer.reset();


        lrcHandler = new LrcHandle();
        try {
            //lrcHandler.readLRC("/storage/extSdCard/music/2728244.lrc");
            lrcHandler.readLRC(word_url);
            mTimeList = lrcHandler.getTime();

            //File file = new File("/storage/extSdCard/music/21751125560064.mp3");
            File file = new File(song_url);
            FileInputStream fis = new FileInputStream(file);

            mPlayer.setDataSource(fis.getFD());
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }


    }

    public void pause(){
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            play.setImageResource(R.drawable.stop);
        }
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
        stop();
    }
}
