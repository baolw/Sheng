package tedu.sheng.app;

import android.app.Application;
import android.util.Log;

import java.util.List;

import tedu.sheng.dal.MusicDaoFactory;
import tedu.sheng.entity.MineSong;
import tedu.sheng.entity.Song;
import tedu.sheng.entity.SongLrc;
import tedu.sheng.entity.SongSearch;

/**
 * Created by Administrator on 2016/6/15.
 */
public class MyApplication extends Application{
    //网络歌曲集合不包含地址
    private List<Song> netSongs;
//本地歌曲集合
    private List<MineSong> localSongs;
    //当前播放的歌曲，网络歌曲
    private Song currentSong;
//是否在播放
    private boolean isRunning;

    //搜索歌曲，搜索歌曲的歌
    private SongSearch songSearch;

    public SongSearch getSongSearch() {
        return songSearch;
    }

    public void setSongSearch(SongSearch songSearch) {
        this.songSearch = songSearch;
    }
//歌词集合
    private List<SongLrc> songLrcs;
    //当前是否为播放网络歌曲
    private boolean isNetWork=true;

//当前播放歌曲的下标
    private int currentIndex=-1;


    //当前播放到的时间位置
   private int progress=-1;
    //当前播放歌曲的总时间
    private int duration=-1;
    //设置歌词
    private String content;

//是否在触摸seekbar
    private boolean isTrackingTouch;

    @Override
    public void onCreate() {
        super.onCreate();
        setLocalSongs();

    }

    public boolean getIsTrackingTouch() {
        return isTrackingTouch;
    }

    public void setIsTrackingTouch(boolean trackingTouch) {
        isTrackingTouch = trackingTouch;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public boolean getIsNetWork() {
        return isNetWork;
    }

    public void setIsNetWork(boolean netWork) {
        isNetWork = netWork;
    }

    public List<MineSong> getLocalSongs() {
        return localSongs;
    }

    public void setLocalSongs() {
        this.localSongs = MusicDaoFactory.newInstance(this).getData();
    }

    public List<SongLrc> getSongLrcs() {
        return songLrcs;
    }

    public void setSongLrcs(List<SongLrc> songLrcs) {
        this.songLrcs = songLrcs;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean running) {
        isRunning = running;
    }



    public List<Song> getNetSongs(){
        return netSongs;
    }

    public void setNetSongs(List<Song> netSongs) {

        this.netSongs=netSongs;
    }


    //当前播放的歌曲下标
    public void setCurrentSong(Song currentSong){
        this.currentSong=currentSong;
    }
    public Song getCurrentSong(){
        Log.e("app里","get");
        return currentSong;
    }
}
