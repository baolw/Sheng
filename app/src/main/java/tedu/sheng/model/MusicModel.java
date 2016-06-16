package tedu.sheng.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import tedu.sheng.R;
import tedu.sheng.entity.Song;
import tedu.sheng.entity.SongInfo;
import tedu.sheng.entity.SongLrc;
import tedu.sheng.entity.SongSerch;
import tedu.sheng.entity.SongUrl;
import tedu.sheng.url.HostURL;
import tedu.sheng.util.BitmapLruCache;
import tedu.sheng.util.BitmapUtils;
import tedu.sheng.util.HttpUtils;

public class MusicModel {
    private RequestQueue mQu;
    private ImageLoader.ImageCache cach;

    private Context context;

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


    public List<Song> getSongs(int offset, int size) {
        List<Song> songs = new ArrayList<Song>();
        String path = HostURL.getHot(offset, size);
        InputStream is = HttpUtils.getIs(path);
        String str = HttpUtils.getStr(is);
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
                songs.add(s);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return songs;
    }

    public Song getSongInfo(Song s) {
        List<SongUrl> list = new ArrayList<SongUrl>();
        String path = HostURL.getSong(s.getSong_id());

        InputStream is = HttpUtils.getIs(path);
        String str = HttpUtils.getStr(is);
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
                list.add(u);
            }
            s.setUrls(list);

            JSONObject in = obj.getJSONObject("songinfo");
            SongInfo info = new SongInfo();
            info.setAlbum_1000_1000(in.isNull("album_1000_1000") ? "" : in.getString("album_1000_1000"));
            info.setAlbum_500_500(in.isNull("album_500_500") ? "" : in.getString("album_500_500"));
            info.setArtist_1000_1000(in.isNull("artist_1000_1000") ? "" : in.getString("artist_1000_1000"));
            info.setArtist_480_800(in.isNull("artist_480_800") ? "" : in.getString("artist_480_800"));
            info.setArtist_640_1136(in.isNull("artist_640_1136") ? "" : in.getString("artist_640_1136"));
            s.setInfo(info);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return s;

    }


    public void displayImg(ImageView img, final String url) {

        //loader.get(url, ImageLoader.getImageListener(img, R.drawable.icon_task_browser, R.drawable.icon_shake_fail));
    }


    public void displaySingle(String url, final ImageView img,final int width,final int height) {
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap arg0) {
                if (arg0 != null) {
                    img.setImageBitmap(arg0);
                }
            }
        }, width, height, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        });
        mQu.add(imageRequest);

    }



    public void displayblur(String url, final ImageView img) {
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap arg0) {
                if (arg0 != null) {
                    Bitmap blur = BitmapUtils.createBlurBitmap(arg0, 10);
                    img.setImageBitmap(blur);
                }
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        });
        mQu.add(imageRequest);

    }

    public List<SongLrc> getLrc(String url) {
        InputStream is = null;
        is = loadLrc(url);
        try {
            if (is == null || is.available() < 1) {
                is = HttpUtils.getIs(url);
                System.out.println("֤���������");
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        List<SongLrc> lrcLines = new ArrayList<SongLrc>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line = br.readLine();
            while (line != null) {

                int point = line.indexOf(".");
                if (point == -1) {
                    break;
                }


                String time = line.substring(line.indexOf("[") + 1, line.indexOf("."));

                String content = line.substring(line.indexOf("]") + 1);
                SongLrc lrc = new SongLrc();
                lrc.setData(line);
                lrc.setContent(content);
                lrc.setTime(time);
                lrcLines.add(lrc);


                line = br.readLine();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveLrc(url, lrcLines);
        return lrcLines;

    }


    public void saveLrc(String url, List<SongLrc> lrcLines) {

        // http://musicdata.baidu.com/data2/lrc/1aec249cd8134a79a853f801a3d07b6e/265391690/265391690.lrc
        // String path=s.getLrclink();
        String name = url.substring(url.lastIndexOf("/") + 1);
        File f = new File(context.getCacheDir(), name);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        } else {
            try {
                f.createNewFile();
                PrintWriter pa = new PrintWriter(f);
                for (int i = 0; i < lrcLines.size(); i++) {
                    SongLrc lrc = lrcLines.get(i);
                    String data = lrc.getData();
                    pa.write(data);
                    pa.write("\r\n");
                    pa.flush();
                }
                pa.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public InputStream loadLrc(String url) {
        FileInputStream fis = null;

        String name = url.substring(url.lastIndexOf("/") + 1);
        File f = new File(context.getCacheDir(), name);
        if (f.exists()) {
            try {
                fis = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return fis;
    }


    public List<SongSerch> Sreach(String name) {
        List<SongSerch> data = new ArrayList<SongSerch>();
        String path = HostURL.getSerach(name);
        InputStream is = HttpUtils.getIs(path);
        String str = HttpUtils.getStr(is);

        try {
            JSONObject obj = new JSONObject(str);
            JSONArray song_list = obj.getJSONArray("song_list");
            for (int i = 0; i < song_list.length(); i++) {
                JSONObject serach = song_list.getJSONObject(i);
                String title = (serach.isNull("title") ? "" : serach.getString("title"));
                String author = (serach.isNull("author") ? "" : serach.getString("author"));
                String song_id = (serach.isNull("song_id") ? "" : serach.getString("song_id"));
                SongSerch song_srach = new SongSerch(title, song_id, author);
                data.add(song_srach);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }


    public boolean isHave(Song song, int version) {
        File root = Environment.
                getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_MUSIC);
        SongUrl downUrl = song.getUrls().get(version);

        String name = "+" + version +
                downUrl.getFile_bitrate() +
                song.getTitle()
                + ".mp3";
        File sdk_Song = new File(root, name);

        if (sdk_Song.exists()) {
            return true;
        } else {
            return false;
        }
    }


}
