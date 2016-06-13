package com.tedu.music.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.tedu.music.entity.Song;
import com.tedu.music.entity.SongInfo;
import com.tedu.music.entity.SongLrc;
import com.tedu.music.entity.SongSerch;
import com.tedu.music.entity.SongUrl;
import com.tedu.music.ui.R;
import com.tedu.music.url.HostURL;
import com.tedu.music.util.BitmapLruCache;
import com.tedu.music.util.HttpUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

/**
 * @author pjy 处理业务逻辑
 */
public class MusicModel {
	private RequestQueue mQu;// 请求队列
	private ImageCache cach;

	private Context context;// 声明全局上下文对象

	ImageLoader loader;

	public MusicModel() {
	}

	public MusicModel(Context context) {
		super();
		this.mQu = Volley.newRequestQueue(context);
		cach = new BitmapLruCache();
		loader = new ImageLoader(mQu, cach);

		this.context = context;
	}

	/**
	 * @param offset
	 * @param size
	 * @return 返回歌曲别表
	 * 
	 */
	public List<Song> getSong(int offset, int size) {
		List<Song> list = new ArrayList<Song>();
		// 网络访问获取流
		String path = HostURL.getHot(offset, size);
		InputStream is = HttpUtils.getIs(path);
		// 转换成字符串
		String str = HttpUtils.getStr(is);
		// json解析
		try {
			JSONObject obj = new JSONObject(str);

			JSONArray song_list = obj.getJSONArray("song_list");
			for (int i = 0; i < song_list.length(); i++) {
				JSONObject so = song_list.getJSONObject(i);
				Song s = new Song();
				s.setArtist_name(so.isNull("artist_name") ? "" : so.getString("artist_name"));
				s.setLrclink(so.isNull("lrclink") ? "" : so.getString("lrclink"));
				s.setPic_big(so.isNull("pic_big") ? "" : so.getString("pic_big"));
				s.setPic_small(so.isNull("pic_small") ? "" : so.getString("pic_small"));
				s.setSong_id(so.isNull("song_id") ? "" : so.getString("song_id"));
				s.setTitle(so.isNull("title") ? "" : so.getString("title"));
				list.add(s);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * @return 获取歌曲的其他信息
	 */
	public Song getSongInfo(Song s) {
		// 歌曲下载地址集合
		List<SongUrl> list = new ArrayList<SongUrl>();
		String path = HostURL.getSong(s.getSong_id());

		InputStream is = HttpUtils.getIs(path);
		String str = HttpUtils.getStr(is);
		// 地址解析过程
		try {
			JSONObject obj = new JSONObject(str);
			JSONObject songurl = obj.getJSONObject("songurl");
			JSONArray url = songurl.getJSONArray("url");
			for (int i = 0; i < url.length(); i++) {
				JSONObject su = url.getJSONObject(i);
				SongUrl u = new SongUrl();
				u.setFile_bitrate(su.isNull("file_bitrate") ? "" : su.getString("file_bitrate"));
				u.setFile_size(su.isNull("file_size") ? "" : su.getString("file_size"));
				u.setShow_link(su.isNull("show_link") ? "" : su.getString("show_link"));
				// 添加到地址集合
				list.add(u);
			}
			// 添加到歌曲对象里
			s.setUrls(list);

			// 解析歌曲信息
			JSONObject in = obj.getJSONObject("songinfo");
			SongInfo info = new SongInfo();
			info.setAlbum_1000_1000(in.isNull("album_1000_1000") ? "" : in.getString("album_1000_1000"));
			info.setAlbum_500_500(in.isNull("album_500_500") ? "" : in.getString("album_500_500"));
			info.setArtist_1000_1000(in.isNull("artist_1000_1000") ? "" : in.getString("artist_1000_1000"));
			info.setArtist_480_800(in.isNull("artist_480_800") ? "" : in.getString("artist_480_800"));
			info.setArtist_640_1136(in.isNull("artist_640_1136") ? "" : in.getString("artist_640_1136"));
			s.setInfo(info);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s;

	}

	/**
	 * 处理adpter图片的加载
	 * 
	 */
	public void displayImg(ImageView img, final String url) {
		// 取得消息队列对象
		// 取得imageloader对象
		// 加载图片
		loader.get(url, ImageLoader.getImageListener(img, R.drawable.icon_task_browser, R.drawable.icon_shake_fail));
	}

	/**
	 * 处理加载单张图片 imgrequest 方式
	 * 
	 */
	public void displaySingle(String url, final ImageView img) {
		// 创建请求对象
		ImageRequest imr = new ImageRequest(url, new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap arg0) {
				if (arg0 != null) {
					img.setImageBitmap(arg0);
				}
			}
		}, 60, 60, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		});
		mQu.add(imr);

	}

	/**
	 * @param url
	 *            下载歌词
	 */
	public List<SongLrc> getLrc(String url) {
		InputStream is = null;
		// 先从缓存中获取--->文件流==网络上获取的流一致
		is = loadLrc(url);// 从文件中获取输入流
		try {
			if (is == null || is.available() < 1) {
				is = HttpUtils.getIs(url);
				System.out.println("证明网络加载");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 声明歌词集合存储所有的歌词
		List<SongLrc> lrcLines = new ArrayList<SongLrc>();
		// 对输入流进行包装
		// [02:45.72]远远的无关的人不经意逃避着
		// 一句歌词的样式
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line = br.readLine();
			while (line != null) {
				// 取歌词
				// 如果歌词没有
				// 循环终止
				// 找点"."的下标
				int point = line.indexOf(".");
				if (point == -1) {
					break;
				}

				// 取时间
				String time = line.substring(line.indexOf("[") + 1, line.indexOf("."));
				// 取内容
				String content = line.substring(line.indexOf("]") + 1);
				// 创建歌词对象并封装内容
				SongLrc lrc = new SongLrc();
				lrc.setData(line);
				lrc.setContent(content);
				lrc.setTime(time);
				// 将歌词添加到歌词列表集合
				lrcLines.add(lrc);

				// 对file 做写入的操作

				line = br.readLine();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 将歌词保存到文件目录
		saveLrc(url, lrcLines);
		return lrcLines;

	}

	/**
	 * @param context
	 *            歌词缓存
	 * 
	 */
	public void saveLrc(String url, List<SongLrc> lrcLines) {
		// 确定一个文件名
		// 取歌曲歌词路径
		// http://musicdata.baidu.com/data2/lrc/1aec249cd8134a79a853f801a3d07b6e/265391690/265391690.lrc
		// String path=s.getLrclink();
		String name = url.substring(url.lastIndexOf("/") + 1);
		// 准备好文件
		File f = new File(context.getCacheDir(), name);
		// 往文件里写东西
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		} else {
			try {
				f.createNewFile();
				// 往文件里写数据
				// PrintWriter 输出流 (打印流)-->单流
				PrintWriter pa = new PrintWriter(f);
				// 写数据 分析(从网上读什么样就写什么样)
				for (int i = 0; i < lrcLines.size(); i++) {
					// 取得一句歌词
					SongLrc lrc = lrcLines.get(i);
					// 从歌词中获取网络的原始数据
					String data = lrc.getData();
					// 用流写入
					pa.write(data);
					pa.write("\r\n");
					pa.flush();
				}
				pa.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 当下载歌词之前可以调用
	 * 
	 * 将缓存的歌词取出 return 返回本地存储文件的输入流
	 */
	public InputStream loadLrc(String url) {
		FileInputStream fis = null;// 如果文件存在将文件转换成输入流
		// 如果要从缓存中取出先要获取该文件
		// 文件名处理
		// 先去下载路径
		// String path=s.getLrclink();
		// 确定文件名
		String name = url.substring(url.lastIndexOf("/") + 1);
		// 找出文件
		File f = new File(context.getCacheDir(), name);
		if (f.exists()) {
			// 如果文件存在 转换成输入流
			try {
				fis = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return fis;
	}

	// 音乐搜索的过程
	/**
	 * @param name
	 *            搜索的名字
	 * retun  搜索对象的集合
	 * 
	 */
	public List<SongSerch>  Sreach(String name) {
		//准备搜索对象集合存储搜索数据
		List<SongSerch> data=new ArrayList<SongSerch>();
		// 获取搜索地址
		String path = HostURL.getSerach(name);
		// 获取输入流
		InputStream is = HttpUtils.getIs(path);
		// 获取字符串
		String str = HttpUtils.getStr(is);
		// json解析
		// 看外边符号
		// 创建object
		try {
			JSONObject obj = new JSONObject(str);
			// 获取json数组
			JSONArray song_list = obj.getJSONArray("song_list");
			// 循环从数组中取出数据
			for (int i = 0; i < song_list.length(); i++) {
				// 获取搜索歌曲的一个jsonobject对象
				JSONObject serach = song_list.getJSONObject(i);
				// 取值封装
				String title = (serach.isNull("title") ? "" : serach.getString("title"));
				String author=(serach.isNull("author") ? "" : serach.getString("author"));
				String song_id=(serach.isNull("song_id") ? "" : serach.getString("song_id"));
				// 创建搜索对象
				SongSerch song_srach = new SongSerch(title, song_id, author);
				data.add(song_srach);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}
	
	/**
	 * @return 判断音乐是否已经下载
	 * 下载返回true
	 * 没下载返回false
	 * 
	 * 
	 */
	public boolean  isHave(Song song,int version){
	File root=Environment. //获得公共目录的music目录
			getExternalStoragePublicDirectory
			(Environment.DIRECTORY_MUSIC);
	//文件的命名 规则
	//_version+品质+歌名.mp3
	//获取下载的songUrl
	SongUrl downUrl=song.getUrls().get(version);
	
	String name="+"+version+ //版本
			downUrl.getFile_bitrate()+//品质
			song.getTitle()//歌名
			+".mp3";//后缀
	File sdk_Song=new File(root, name);
	
		if(sdk_Song.exists()){
			return true;
		}else{
			return  false;
		}
	}
	

}
