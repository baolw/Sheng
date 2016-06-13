package com.tedu.music.entity;

import java.io.Serializable;

/**
 * @author pjy
 *描述音乐下载路径相关
 * {
                "show_link": "http://zhangmenshiting.baidu.com/data2/music/df5629a02e11eaa5b5ff683e24cc2751/264917711/264917711.mp3?xcode=b3617ad54beb94c7ee71546b0f38c3ae",
                "down_type": 0,
                "original": 0,
                "free": 1,
                "replay_gain": "0.930000",
                "song_file_id": 264917711,
                "file_size": 1702254,
                "file_extension": "mp3",
                "file_duration": 212,
                "can_see": 1,
                "can_load": true,
                "preload": 40,
                "file_bitrate": 64,
                "file_link": "http://yinyueshiting.baidu.com/data2/music/df5629a02e11eaa5b5ff683e24cc2751/264917711/264917711.mp3?xcode=b3617ad54beb94c7ee71546b0f38c3ae",
                "is_udition_url": 0,
                "hash": "b6968b65181dc6942a2970dcc40ec4d51716de32"
            },
 *
 */
public class SongUrl implements Serializable{
	private  String show_link;
	private  String file_size;
	private  String file_bitrate;
	public String getShow_link() {
		return show_link;
	}
	public void setShow_link(String show_link) {
		this.show_link = show_link;
	}
	public String getFile_size() {
		return file_size;
	}
	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}
	public String getFile_bitrate() {
		return file_bitrate;
	}
	public void setFile_bitrate(String file_bitrate) {
		this.file_bitrate = file_bitrate;
	}
	
	
}
