package com.tedu.music.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tedu.music.entity.Song;
import com.tedu.music.entity.SongUrl;
import com.tedu.music.ui.R;
import com.tedu.music.util.HttpUtils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

/**
 * @author pjy
 *	用于执行 下载的耗时操作
 *
 */
public class MusicDownLoadService extends IntentService{
	//	添加一个无参的构造
	public MusicDownLoadService(){
		super("name");
	}
	private static int NOTIFSCTION_ID=10;
	
	/**
	 * @param name
	 * 有参构造器
	 */
	public MusicDownLoadService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//取得下载信息
		//歌
		Song song=(Song) intent.getSerializableExtra("song");
		//版本
		int version=intent.getIntExtra("version", 0);
		//获得下载根目录
		File root=Environment.
				getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		
		//判断root目录是否存在
		if(!root.exists()){
			root.mkdirs();
		}
		//下载地址对象
		SongUrl down_url=song.getUrls().get(version);
		//组合歌名_version+品质+歌名.mp3
		String name="_"+version+
				down_url.getFile_bitrate()+
				song.getTitle()+
				".mp3";
		//创建保存到sdk里的文件
		File f=new File(root, name);
		//1获取输入流
		InputStream is=HttpUtils.getIs(down_url.getShow_link());
		//下载操作
		//声明一定长度的字节数组
		byte [] buffer=new byte[1024*200];
		//存储所有的下载字节
		int all_byte=0;
		//文件大小
		int song_size= Integer.parseInt(down_url.getFile_size());
		//让通知显示的百分比
		String count="";
		try {
			int length=is.read(buffer);//保存在数组中读取文件长度
			all_byte=length;
			FileOutputStream fos=new FileOutputStream(f);
			while (length!=-1) {
				fos.write(buffer, 0, length);
				fos.flush();
				length=is.read(buffer);
				all_byte+=length;
				//百分比求法
			count="已经下载:"+Math.floor(all_byte*100.0/song_size)+"%";
				if(all_byte==song_size){
					count="下载完毕了";
				}
				senNotifaction(count);
				
			}
			
			//流的关闭
			is.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		//通知的发送
		

		
	}
	
	/**
	 * 来给系统发送通知
	 */
	/**
	 * @param count 下载的百分比
	 */
	public void  senNotifaction(String count){
		//通知管理对象
		NotificationManager manager=(NotificationManager) 
				getSystemService
				(Context.NOTIFICATION_SERVICE);

		//通知构建器
		//Notification.Builder api向下兼容等级要求为11以上
		Notification.Builder builder=new 
				Notification.Builder
				(getApplicationContext());
		//通知属性处理
		builder.setContentTitle("音乐下载");//标题
		builder.setContentText(count);//内容
		builder.setSmallIcon//图标
		(R.drawable.btn_social_pic_save_normal);
		//生成通知
		Notification no=builder.build();
		//发送通知
		manager.notify(NOTIFSCTION_ID, no);

	}
	
	
}
