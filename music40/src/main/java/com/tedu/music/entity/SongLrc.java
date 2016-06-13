package com.tedu.music.entity;

/**
 * @author pjy
 *
 *	描述歌词(一句)
 */
public class SongLrc {
	private String time;//时间
	private String content;//内容
	private String data;//存储网络上原始数据
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}
