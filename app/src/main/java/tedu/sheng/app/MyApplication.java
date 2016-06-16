package tedu.sheng.app;

import android.app.Application;
import android.util.Log;

import java.util.List;

import tedu.sheng.entity.Song;

/**
 * Created by Administrator on 2016/6/15.
 */
public class MyApplication extends Application{
    //不包含地址
    private List<Song> netSongs;
    private List<Song> localSongs;
    //当前播放的歌曲
    private Song currentSong;
//是否在播放
    private boolean isRunning;


    private int position;
    @Override
    public void onCreate() {
        super.onCreate();
        //setLocalSongs();

    }



    public boolean getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean running) {
        isRunning = running;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Song> getNetSongs(){
        return netSongs;
    }

    public void setNetSongs(List<Song> netSongs) {

        this.netSongs=netSongs;
    }

    public List<Song> getLocalSongs() {
        return localSongs;
    }

    public void setLocalSongs() {
        this.localSongs = localSongs;
    }

    //当前播放的歌曲
    public void setCurrentSong(Song currentSong){
        this.currentSong=currentSong;
    }
    public Song getCurrentSong(){
        Log.e("app里","get");
        return currentSong;
    }
}
