package tedu.sheng.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import tedu.sheng.app.MyApplication;
import tedu.sheng.entity.Song;
import tedu.sheng.model.MusicModel;
import tedu.sheng.util.Consts;


public class MusicService extends Service implements Consts {


    //本地的音乐播放
    private BroadcastReceiver receiver;
    private Intent broadcasetIntent = new Intent();


    //网络歌曲集合
    private List<Song> netSongs;
    //当前需要播放的音乐信息
    private Song currentSong;
    private int currenMusicIndex;
    //更新播放的线程
    private UpdateProgressThread update = null;

    //暂停的位置
    private int pausePosition;
    private MediaPlayer player;


    private boolean looper;
    private MyApplication app;


    //播放歌曲的下标,即当前是列表中的第几首
    private int index;

    private MusicModel model;


    @Override
    public void onCreate() {
        super.onCreate();
        app = (MyApplication) getApplication();
        netSongs = app.getNetSongs();

        model=new MusicModel();



        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }


        });




        //looper = true;
        update = new UpdateProgressThread();
        update.start();

        receiver = new InnerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY_OR_PAUSE);
        filter.addAction(ACTION_NEXT);
        filter.addAction(ACTION_PLAY_FROM_PROGRESS);
        filter.addAction(ACTION_PLAY_POSITION);
        filter.addAction(ACTION_PREVIOUS);
        filter.addAction(ACTION_PLAY_POSITION_NET);
        registerReceiver(receiver, filter);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_PLAY_OR_PAUSE.equals(action)) {
                Log.e("错误检查","接收");
                if (player.isPlaying()) {
                    pause();
                } else {
                    play();
                }
            } else if (ACTION_NEXT.equals(action)) {

                 next();
            } else if (ACTION_PREVIOUS.equals(action)) {
                // previous();
            } else if (ACTION_PLAY_POSITION.equals(action)) {
                int position = intent.getIntExtra(EXTRA_MUSIC_INDEX, 0);
                currentSong = (Song) intent.getSerializableExtra(EXTRA_CURRENT_MUSIC);
                currenMusicIndex=intent.getIntExtra(EXTRA_MUSIC_INDEX,0);
                play(position);
            } else if (ACTION_PLAY_FROM_PROGRESS.equals(action)) {
                int progress = intent.getIntExtra(EXTRA_PROGRESS, 0);
                //playFromProgress(progress);
            } else if (ACTION_PLAY_POSITION_NET.equals(action)) {
                int position = intent.getIntExtra(EXTRA_MUSIC_INDEX, 0);
                currentSong = (Song) intent.getSerializableExtra(EXTRA_CURRENT_MUSIC);
                play(position);

            }
        }



    }

    //下一首
    private void next() {
        pausePosition=0;
        play();
    }


    //暂停
    private void pause() {
        player.pause();
        pausePosition=player.getCurrentPosition();
Log.e("错误检查","pause");
        broadcasetIntent.setAction(ACTION_SET_AS_PAUSE_STATE);
        sendBroadcast(broadcasetIntent);
        startUpdateProgressThread();
    }

    private void play() {
        try {
            player.reset();
            player.setDataSource(currentSong.getUrls().get(0).getShow_link());
            player.prepare();
            player.seekTo(pausePosition);
            player.start();


        } catch (IOException e) {
            e.printStackTrace();
        }

        //将播放歌曲信息同步到activity界面中
        broadcasetIntent.setAction(ACTION_SET_AS_PLAY_STATE);
        broadcasetIntent.putExtra(EXTRA_MUSIC_INDEX, index);
        sendBroadcast(broadcasetIntent);

        startUpdateProgressThread();




    }

    private void play(int position) {
        index = position;
        pausePosition = 0;
        play();
    }


    public interface IMusicPlayer extends Serializable {
        //播放暂停
        void playOrPause();

        //播放歌曲
        void playCurrentMusic(Song song, List<Song> data, int index);

        //控制进度
        void seekTo(int position);
    }

    //开始线程
    public void startUpdateProgressThread(){
        if(update==null){
            update=new UpdateProgressThread();
            update.setIsRunning(true);
            update.start();
        }
    }

    //结束线程
    public void stopUpdateProgressThread(){
        if(update!=null){
            update=null;
            update.setIsRunning(false);
        }
    }

    class UpdateProgressThread extends Thread {
        private boolean isRunning;
        public void setIsRunning(boolean isRunning){
            this.isRunning=isRunning;
        }
        @Override
        public void run() {
            super.run();

            while (looper) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (player.isPlaying()) {

                    int dura = player.getDuration();

                    int current = player.getCurrentPosition();

                    Intent in = new Intent("update");
                    in.putExtra("duration", dura);
                    in.putExtra("current", current);
                    sendBroadcast(in);
                }


            }
        }
    }

    @Override
    public void onDestroy() {
        player.reset();
        player.release();
        looper = false;
        stopThread();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void stopThread() {
        update = null;
    }


}
