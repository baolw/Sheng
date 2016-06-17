package tedu.sheng.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import tedu.sheng.app.MyApplication;
import tedu.sheng.entity.MineSong;
import tedu.sheng.entity.Song;
import tedu.sheng.entity.SongLrc;
import tedu.sheng.entity.SongUrl;
import tedu.sheng.model.MusicModel;
import tedu.sheng.util.CommonUtils;
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


    private MyApplication app;


    //播放歌曲的下标,即当前是列表中的第几首

    private MusicModel model;



    //本地音乐集合
    private List<MineSong> musics;
    @Override
    public void onCreate() {
        super.onCreate();
        app = (MyApplication) getApplication();
        netSongs = app.getNetSongs();
        musics=app.getLocalSongs();
        model=new MusicModel(getApplication());



        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                currenMusicIndex++;
                if(currenMusicIndex>=app.getNetSongs().size()){
                    currenMusicIndex=0;
                }
                currentSong=model.getSongInfo(app.getNetSongs().get(currenMusicIndex));
                app.setCurrentIndex(currenMusicIndex);
                app.setCurrentSong(currentSong);
                next();
            }
        });

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

                if(app.getIsNetWork()) {
                    currentSong = (Song) intent.getSerializableExtra(EXTRA_CURRENT_MUSIC);
                }else{
                    currenMusicIndex=intent.getIntExtra(EXTRA_CURRENT_POSITION,0);
                }
                 next();
            } else if (ACTION_PREVIOUS.equals(action)) {
                if(app.getIsNetWork()){
                    currentSong= (Song) intent.getSerializableExtra(EXTRA_CURRENT_MUSIC);
                }else{
                    currenMusicIndex=intent.getIntExtra(EXTRA_CURRENT_POSITION,0);
                }
                previous();
            } else if (ACTION_PLAY_POSITION.equals(action)) {
                int position = intent.getIntExtra(EXTRA_MUSIC_INDEX, 0);
                if(app.getIsNetWork()){
                    currentSong = (Song) intent.getSerializableExtra(EXTRA_CURRENT_MUSIC);
                }else{

                }
                play(position);
            } else if (ACTION_PLAY_FROM_PROGRESS.equals(action)) {
                int progress = intent.getIntExtra(EXTRA_PROGRESS, 0);
                playFromProgress(progress);
            } else if (ACTION_PLAY_POSITION_NET.equals(action)) {
                int position = intent.getIntExtra(EXTRA_MUSIC_INDEX, 0);
                currentSong = (Song) intent.getSerializableExtra(EXTRA_CURRENT_MUSIC);
                play(position);

            }
        }



    }
    private void playFromProgress(int progress) {
        pausePosition=progress;
        play();
    }



    //下一首
    private void next() {

        pausePosition=0;
        play();


    }private void previous() {
        pausePosition=0;
        play();

    }


    //暂停
    private void pause() {

        app.setIsRunning(false);
        player.pause();
        pausePosition=player.getCurrentPosition();
        broadcasetIntent.setAction(ACTION_SET_AS_PAUSE_STATE);
        sendBroadcast(broadcasetIntent);
        stopUpdateProgressThread();

        broadcasetIntent.setAction(ACTION_SET_AS_PAUSE_STATE2);
        sendBroadcast(broadcasetIntent);

    }


    public String getPath(int version) {
        String path = "";
        if (model.isHave(currentSong, 0)) {
            File root = Environment.
                    getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_MUSIC);
            SongUrl downUrl = currentSong.getUrls().get(0);

            String name = "_0" +
                    downUrl.getFile_bitrate() +
                    currentSong.getTitle()
                    + ".mp3";
            File sdk_Song = new File(root, name);
            path = sdk_Song.getAbsolutePath();
        } else {
            path = currentSong.getUrls().get(0).getShow_link();

        }
        return path;

    }
    private void play() {

        app.setIsRunning(true);

        String path="";
        if(app.getIsNetWork()) {
            if (getPath(0) != null) {
                path = getPath(0);
            } else if (getPath(1) != null) {
                path = getPath(1);
            } else if (getPath(2) != null) {
                path = getPath(2);
            }
        }else{
            path=musics.get(currenMusicIndex).getPath();
        }


        try {
            player.reset();
            player.setDataSource(path);
            player.prepare();
            player.seekTo(pausePosition);
            player.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //将播放歌曲信息同步到activity界面中
        broadcasetIntent.setAction(ACTION_SET_AS_PLAY_STATE);
        sendBroadcast(broadcasetIntent);
        broadcasetIntent.setAction(ACTION_SET_AS_PLAY_STATE2);
        sendBroadcast(broadcasetIntent);
        if(app.getIsNetWork()) {
            new Thread() {
                @Override
                public void run() {
                    app.setSongLrcs(model.getLrc(currentSong.getLrclink()));
                }
            }.start();
        }

        startUpdateProgressThread();



    }

    private void play(int position) {
        currenMusicIndex = position;
        pausePosition = 0;
        play();
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
            update.setIsRunning(false);
            update=null;
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

            while (isRunning) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }



                    if (player.isPlaying()) {

                    app.setProgress(player.getCurrentPosition());
                        app.setDuration(player.getDuration());

                        if (app.getIsNetWork()) {
                            List<SongLrc> songLrcs = app.getSongLrcs();
                            for (int i = 0; i < songLrcs.size(); i++) {
                                SongLrc lrc = songLrcs.get(i);
                                String content = lrc.getContent();
                                app.setContent(content);
                            }

                        }



                        broadcasetIntent.setAction(ACTION_UPDATE_PROGRESS);
                        broadcasetIntent.putExtra(EXTRA_CURRENT_POSITION,player.getCurrentPosition());
                        broadcasetIntent.putExtra(EXTRA_DURATION,player.getDuration());
                        sendBroadcast(broadcasetIntent);
                    }
                }

        }
    }

    @Override
    public void onDestroy() {
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
        stopUpdateProgressThread();
        if(player!=null){
            player.release();
            player=null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




}
