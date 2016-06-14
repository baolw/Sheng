package tedu.sheng.url;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class HostURL {

	//热歌路径
	public static String  getHot(int offset,int size){
		String path="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=1&offset="+offset+"&size="+size;
		return path;
	}
	//新歌路径
	public static String  getNew(int offset,int size){
		String path="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=1&offset="+offset+"&size="+size;
		return path;
	}
	//英文路径
	public static String  getEn(int offset,int size){
		String path="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=8&offset="+offset+"&size="+size;
		return path;
	}
	//歌手路径

	public static String  getSinger(int offset,int size){
		String path="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.artist.get72HotArtist&format=json&order=1&offset="+offset+"&size="+size;
		return path;
	}

	//电台路径
	public static String  getRadio(int offset,int size){
		String path="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.radio.getCategoryList&format=json";
		return path;
	}





	//取歌路径
	public static String  getSong(String songid){
		String path="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.song.getInfos&format=json&songid="+songid+"&ts=1408284347323&e=JoN56kTXnnbEpd9MVczkYJCSx%2FE1mkLx%2BPMIkTcOEu4%3D&nw=2&ucf=1&res=1";
		return path;
	}


	//查歌路径
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
