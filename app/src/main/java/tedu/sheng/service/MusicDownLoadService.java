package tedu.sheng.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import tedu.sheng.R;
import tedu.sheng.entity.Song;
import tedu.sheng.entity.SongUrl;
import tedu.sheng.util.HttpUtils;


public class MusicDownLoadService extends IntentService{
	Song song;
	public MusicDownLoadService(){
		super("name");
	}
	private static int NOTIFSCTION_ID=10;
	

	public MusicDownLoadService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.e("teduceshxiazai","测试下载功能");
		song=(Song) intent.getSerializableExtra("song");

		int version=intent.getIntExtra("version", 0);

		File root=Environment.
				getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		

		if(!root.exists()){
			root.mkdirs();
		}

		SongUrl down_url=song.getUrls().get(version);

		String name="_"+version+
				down_url.getFile_bitrate()+
				song.getTitle()+
				".mp3";
		File f=new File(root, name);
		InputStream is= HttpUtils.getIs(down_url.getShow_link());
		byte [] buffer=new byte[1024*200];
		int all_byte=0;
		int song_size= Integer.parseInt(down_url.getFile_size());
		String count="";
		try {
			int length=is.read(buffer);
			all_byte=length;
			FileOutputStream fos=new FileOutputStream(f);
			while (length!=-1) {
				fos.write(buffer, 0, length);
				fos.flush();
				length=is.read(buffer);
				if(length!=-1) {
					all_byte += length;
				}
				Log.e("tedu下载数据量","song_size-->"+song_size+"--<>--all_byte-->"+all_byte);
				count="已经下载"+Math.floor(all_byte*100.0/song_size)+"%";
				if(all_byte==song_size){
					count="下载成功";
				}
				sendNotifaction(count);
				
			}
			
			is.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		

		
	}
	

	public void  sendNotifaction(String count){
		NotificationManager manager=(NotificationManager)
				getSystemService
				(Context.NOTIFICATION_SERVICE);

		Notification.Builder builder=new
				Notification.Builder
				(getApplicationContext());

		builder.setContentTitle("正在下载-->"+song.getTitle());
		builder.setContentText(count);
		builder.setSmallIcon
		(R.mipmap.download);
		Notification no=builder.build();
		manager.notify(NOTIFSCTION_ID, no);

	}
	
	
}
