package tedu.sheng.entity;

import java.io.Serializable;
import java.util.List;

public class Song implements Serializable {

    private SongInfo info;
    private String pic_small;
    private String pic_big;
    private String lrclink;
    private String song_id;
    private String title;
    private String artist_name;
    private List<SongUrl> urls;


    public SongInfo getInfo() {
        return info;
    }

    public void setInfo(SongInfo info) {
        this.info = info;
    }

    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getLrclink() {
        return lrclink;
    }

    public void setLrclink(String lrclink) {
        this.lrclink = lrclink;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public List<SongUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<SongUrl> urls) {
        this.urls = urls;
    }
}
