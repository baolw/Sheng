package com.tedu.music.entity;

import java.io.Serializable;

/**
 * @author pjy
 *描述歌曲信息
 * "songinfo": {
        "resource_type_ext": "0",
        "pic_huge": "http://musicdata.baidu.com/data2/pic/2ffd34c9ff6e3724519404ac031ff05b/264917614/264917614.jpg",
        "resource_type": "0",
        "del_status": "0",
        "album_1000_1000": "http://musicdata.baidu.com/data2/pic/2ffd34c9ff6e3724519404ac031ff05b/264917614/264917614.jpg",
        "pic_singer": "",
        "album_500_500": "http://musicdata.baidu.com/data2/pic/c7a45665e8a58495b63f6128d96bdea5/264917615/264917615.jpg",
        "havehigh": 2,
        "piao_id": "0",
        "song_source": "web",
        "korean_bb_song": "0",
        "compose": "",
        "toneid": "0",
        "area": "0",
        "original_rate": "",
        "bitrate": "64,128,256,320,1587",
        "artist_500_500": "http://musicdata.baidu.com/data2/pic/173e033ad3300c6333143c2cd4bc4842/246871664/246871664.jpg",
        "multiterminal_copytype": "1110,1101,1011,0111",
        "has_mv": 0,
        "file_duration": "0",
        "album_title": "等我遇见你",
        "sound_effect": "0",
        "title": "等我遇见你",
        "high_rate": "320",
        "pic_radio": "http://musicdata.baidu.com/data2/pic/fbbd41a8484e70976e4a9507779cb4d3/264917223/264917223.jpg",
        "is_first_publish": 0,
        "hot": "590069",
        "language": "国语",
        "lrclink": "http://musicdata.baidu.com/data2/lrc/3b51cbe42d3e3c9f2c85ba038a86e6cf/265247000/265247000.lrc",
        "distribution": "0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,1111110000,1111110000,0000000000",
        "relate_status": "0",
        "learn": 0,
        "play_type": 0,
        "pic_big": "http://musicdata.baidu.com/data2/pic/cc53c09a7695871d78324cbbe85d37d9/264917229/264917229.jpg",
        "pic_premium": "http://musicdata.baidu.com/data2/pic/c7a45665e8a58495b63f6128d96bdea5/264917615/264917615.jpg",
        "artist_480_800": "http://musicdata.baidu.com/data2/pic/105445077/105445077.jpg",
        "aliasname": "",
        "country": "内地",
        "artist_id": "776",
        "album_id": "264917612",
        "original": 0,
        "compress_status": "0",
        "versions": "影视原声",
        "expire": 36000,
        "ting_uid": "1383",
        "artist_1000_1000": "http://musicdata.baidu.com/data2/pic/246871652/246871652.jpg",
        "all_artist_id": "776",
        "artist_640_1136": "http://musicdata.baidu.com/data2/pic/105445078/105445078.jpg",
        "publishtime": "2016-04-21",
        "charge": 0,
        "copy_type": "0",
        "songwriting": "",
        "share_url": "http://music.baidu.com/song/264917602",
        "author": "李健",
        "has_mv_mobile": 0,
        "all_rate": "64,128,256,320,flac",
        "pic_small": "http://musicdata.baidu.com/data2/pic/801861c9d48c4d24f3f7dcdba49545bc/264917234/264917234.jpg",
        "album_no": "1",
        "song_id": "264917602",
        "is_charge": "0"
    }
 *
 */
public class SongInfo implements Serializable{
	public String getAlbum_1000_1000() {
		return album_1000_1000;
	}
	public void setAlbum_1000_1000(String album_1000_1000) {
		this.album_1000_1000 = album_1000_1000;
	}
	public String getAlbum_500_500() {
		return album_500_500;
	}
	public void setAlbum_500_500(String album_500_500) {
		this.album_500_500 = album_500_500;
	}
	public String getArtist_480_800() {
		return artist_480_800;
	}
	public void setArtist_480_800(String artist_480_800) {
		this.artist_480_800 = artist_480_800;
	}
	public String getArtist_1000_1000() {
		return artist_1000_1000;
	}
	public void setArtist_1000_1000(String artist_1000_1000) {
		this.artist_1000_1000 = artist_1000_1000;
	}
	public String getArtist_640_1136() {
		return artist_640_1136;
	}
	public void setArtist_640_1136(String artist_640_1136) {
		this.artist_640_1136 = artist_640_1136;
	}
	private String album_1000_1000;
	private String album_500_500;
	private String artist_480_800;
	private String artist_1000_1000;
	private String artist_640_1136;
	
	

}
