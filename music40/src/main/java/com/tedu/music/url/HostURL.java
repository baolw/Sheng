package com.tedu.music.url;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author pjy
 * 9. 4）新歌榜  
10. http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=1&offset=0&size=50  
11. 5）热歌榜  
12.http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=2&offset=0&size=50  注意这个和上边的区别，type=1  
13. 6）Billboard  
 *
 */
public class HostURL {
	/**
	 * @param offset
	 * @param size
	 * @return 热歌表列表
	 */
	public static String  getHot(int offset,int size){
	String path="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=1&offset="+offset+"&size="+size;
	return path;
	}
	/**
	 * @param songid
	 * @return
	 * 获取具体的一首歌
	 */
	public static String  getSong(String songid){
		String path="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.song.getInfos&format=json&songid="+songid+"&ts=1408284347323&e=JoN56kTXnnbEpd9MVczkYJCSx%2FE1mkLx%2BPMIkTcOEu4%3D&nw=2&ucf=1&res=1";
		return path;
	}
	/**
	 * @param name
	 * @return 返回 搜索歌曲地址
	 */
	public  static String getSerach(String name){
		String path=null;
		try {
			path ="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&query="+URLEncoder.encode(name, "utf-8")+"&page_no=1&page_size=30";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		return path;
	}
	
	
	
	

}
