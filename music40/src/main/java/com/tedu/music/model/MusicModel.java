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
 * @author pjy ����ҵ���߼�
 */
public class MusicModel {
	private RequestQueue mQu;// �������
	private ImageCache cach;

	private Context context;// ����ȫ�������Ķ���

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
	 * @return ���ظ������
	 * 
	 */
	public List<Song> getSong(int offset, int size) {
		List<Song> list = new ArrayList<Song>();
		// ������ʻ�ȡ��
		String path = HostURL.getHot(offset, size);
		InputStream is = HttpUtils.getIs(path);
		// ת�����ַ���
		String str = HttpUtils.getStr(is);
		// json����
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
	 * @return ��ȡ������������Ϣ
	 */
	public Song getSongInfo(Song s) {
		// �������ص�ַ����
		List<SongUrl> list = new ArrayList<SongUrl>();
		String path = HostURL.getSong(s.getSong_id());

		InputStream is = HttpUtils.getIs(path);
		String str = HttpUtils.getStr(is);
		// ��ַ��������
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
				// ��ӵ���ַ����
				list.add(u);
			}
			// ��ӵ�����������
			s.setUrls(list);

			// ����������Ϣ
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
	 * ����adpterͼƬ�ļ���
	 * 
	 */
	public void displayImg(ImageView img, final String url) {
		// ȡ����Ϣ���ж���
		// ȡ��imageloader����
		// ����ͼƬ
		loader.get(url, ImageLoader.getImageListener(img, R.drawable.icon_task_browser, R.drawable.icon_shake_fail));
	}

	/**
	 * ������ص���ͼƬ imgrequest ��ʽ
	 * 
	 */
	public void displaySingle(String url, final ImageView img) {
		// �����������
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
	 *            ���ظ��
	 */
	public List<SongLrc> getLrc(String url) {
		InputStream is = null;
		// �ȴӻ����л�ȡ--->�ļ���==�����ϻ�ȡ����һ��
		is = loadLrc(url);// ���ļ��л�ȡ������
		try {
			if (is == null || is.available() < 1) {
				is = HttpUtils.getIs(url);
				System.out.println("֤���������");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// ������ʼ��ϴ洢���еĸ��
		List<SongLrc> lrcLines = new ArrayList<SongLrc>();
		// �����������а�װ
		// [02:45.72]ԶԶ���޹ص��˲������ӱ���
		// һ���ʵ���ʽ
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line = br.readLine();
			while (line != null) {
				// ȡ���
				// ������û��
				// ѭ����ֹ
				// �ҵ�"."���±�
				int point = line.indexOf(".");
				if (point == -1) {
					break;
				}

				// ȡʱ��
				String time = line.substring(line.indexOf("[") + 1, line.indexOf("."));
				// ȡ����
				String content = line.substring(line.indexOf("]") + 1);
				// ������ʶ��󲢷�װ����
				SongLrc lrc = new SongLrc();
				lrc.setData(line);
				lrc.setContent(content);
				lrc.setTime(time);
				// �������ӵ�����б���
				lrcLines.add(lrc);

				// ��file ��д��Ĳ���

				line = br.readLine();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ����ʱ��浽�ļ�Ŀ¼
		saveLrc(url, lrcLines);
		return lrcLines;

	}

	/**
	 * @param context
	 *            ��ʻ���
	 * 
	 */
	public void saveLrc(String url, List<SongLrc> lrcLines) {
		// ȷ��һ���ļ���
		// ȡ�������·��
		// http://musicdata.baidu.com/data2/lrc/1aec249cd8134a79a853f801a3d07b6e/265391690/265391690.lrc
		// String path=s.getLrclink();
		String name = url.substring(url.lastIndexOf("/") + 1);
		// ׼�����ļ�
		File f = new File(context.getCacheDir(), name);
		// ���ļ���д����
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		} else {
			try {
				f.createNewFile();
				// ���ļ���д����
				// PrintWriter ����� (��ӡ��)-->����
				PrintWriter pa = new PrintWriter(f);
				// д���� ����(�����϶�ʲô����дʲô��)
				for (int i = 0; i < lrcLines.size(); i++) {
					// ȡ��һ����
					SongLrc lrc = lrcLines.get(i);
					// �Ӹ���л�ȡ�����ԭʼ����
					String data = lrc.getData();
					// ����д��
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
	 * �����ظ��֮ǰ���Ե���
	 * 
	 * ������ĸ��ȡ�� return ���ر��ش洢�ļ���������
	 */
	public InputStream loadLrc(String url) {
		FileInputStream fis = null;// ����ļ����ڽ��ļ�ת����������
		// ���Ҫ�ӻ�����ȡ����Ҫ��ȡ���ļ�
		// �ļ�������
		// ��ȥ����·��
		// String path=s.getLrclink();
		// ȷ���ļ���
		String name = url.substring(url.lastIndexOf("/") + 1);
		// �ҳ��ļ�
		File f = new File(context.getCacheDir(), name);
		if (f.exists()) {
			// ����ļ����� ת����������
			try {
				fis = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return fis;
	}

	// ���������Ĺ���
	/**
	 * @param name
	 *            ����������
	 * retun  ��������ļ���
	 * 
	 */
	public List<SongSerch>  Sreach(String name) {
		//׼���������󼯺ϴ洢��������
		List<SongSerch> data=new ArrayList<SongSerch>();
		// ��ȡ������ַ
		String path = HostURL.getSerach(name);
		// ��ȡ������
		InputStream is = HttpUtils.getIs(path);
		// ��ȡ�ַ���
		String str = HttpUtils.getStr(is);
		// json����
		// ����߷���
		// ����object
		try {
			JSONObject obj = new JSONObject(str);
			// ��ȡjson����
			JSONArray song_list = obj.getJSONArray("song_list");
			// ѭ����������ȡ������
			for (int i = 0; i < song_list.length(); i++) {
				// ��ȡ����������һ��jsonobject����
				JSONObject serach = song_list.getJSONObject(i);
				// ȡֵ��װ
				String title = (serach.isNull("title") ? "" : serach.getString("title"));
				String author=(serach.isNull("author") ? "" : serach.getString("author"));
				String song_id=(serach.isNull("song_id") ? "" : serach.getString("song_id"));
				// ������������
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
	 * @return �ж������Ƿ��Ѿ�����
	 * ���ط���true
	 * û���ط���false
	 * 
	 * 
	 */
	public boolean  isHave(Song song,int version){
	File root=Environment. //��ù���Ŀ¼��musicĿ¼
			getExternalStoragePublicDirectory
			(Environment.DIRECTORY_MUSIC);
	//�ļ������� ����
	//_version+Ʒ��+����.mp3
	//��ȡ���ص�songUrl
	SongUrl downUrl=song.getUrls().get(version);
	
	String name="+"+version+ //�汾
			downUrl.getFile_bitrate()+//Ʒ��
			song.getTitle()//����
			+".mp3";//��׺
	File sdk_Song=new File(root, name);
	
		if(sdk_Song.exists()){
			return true;
		}else{
			return  false;
		}
	}
	

}
