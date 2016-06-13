package com.tedu.music.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author pjy
 * 用于描述歌曲
 *  "artist_id": "776",
            "language": "国语",
            "pic_big": "http://qukufile2.qianqian.com/data2/pic/cc53c09a7695871d78324cbbe85d37d9/264917229/264917229.jpg",
            "pic_small": "http://qukufile2.qianqian.com/data2/pic/801861c9d48c4d24f3f7dcdba49545bc/264917234/264917234.jpg",
            "country": "内地",
            "area": "0",
            "publishtime": "2016-04-21",
            "album_no": "1",
            "lrclink": "http://musicdata.baidu.com/data2/lrc/3b51cbe42d3e3c9f2c85ba038a86e6cf/265247000/265247000.lrc",
            "copy_type": "1",
            "hot": "590069",
            "all_artist_ting_uid": "1383",
            "resource_type": "0",
            "is_new": "1",
            "rank_change": "0",
            "rank": "1",
            "all_artist_id": "776",
            "style": "影视原声",
            "del_status": "0",
            "relate_status": "0",
            "toneid": "0",
            "all_rate": "64,128,256,320,flac",
            "sound_effect": "0",
            "file_duration": 0,
            "has_mv_mobile": 0,
            "versions": "影视原声",
            "bitrate_fee": "{\"0\":\"0|0\",\"1\":\"0|0\"}",
            "song_id": "264917602",
            "title": "等我遇见你",
            "ting_uid": "1383",
            "author": "李健",
            "album_id": "264917612",
            "album_title": "等我遇见你",
            "is_first_publish": 0,
            "havehigh": 2,
            "charge": 0,
            "has_mv": 0,
            "learn": 0,
            "song_source": "web",
            "piao_id": "0",
            "korean_bb_song": "0",
            "resource_type_ext": "0",
            "mv_provider": "0000000000",
            "artist_name": "李健"
        },
       
 * 
 * 
 * 
 * 
 */
public class Song  implements Serializable{
	
	private SongInfo info;//歌曲信息
	public SongInfo getInfo() {
		return info;
	}
	public void setInfo(SongInfo info) {
		this.info = info;
	}
	private List<SongUrl> urls;//所有歌曲的下载地址
	public List<SongUrl> getUrls() {
		return urls;
	}
	public void setUrls(List<SongUrl> urls) {
		this.urls = urls;
	}
	private String pic_small;//小图
	private String pic_big;//大图
	private String lrclink;//歌词
	private String song_id;//歌曲id
	private String title;//歌名
	private String artist_name;//歌手
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


}
