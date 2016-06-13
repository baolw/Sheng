package com.tedu.music.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.tedu.music.entity.Song;
import com.tedu.music.ui.CircleImageView;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

/**
 * @author pjy
 *
 *         提供音乐播放的相关服务
 */
public class MusicService extends Service {
	private MediaPlayer play;
	private boolean looper;

	
	private  UpdateThread upate=null;
	@Override //数据的初始化
	public void onCreate() {
		super.onCreate();
		play=new MediaPlayer();
		looper=true;
		upate=new UpdateThread();
		upate.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new myBind();
	}
	
	/**
	 * @author pjy
	 * bind类
	 */
	class myBind extends Binder implements IMusicPlayer{

		@Override
		public void playOrPuse() {
			if(play.isPlaying()){
				play.pause();
			}else{
				play.start();
			}
			
		}

		@Override
		public void playCrrentMusic(Song song,List<Song> data, int index) {
			
			try {
				play.reset();//重置一下
				play.setDataSource(song.getUrls().get(0).getShow_link());//绑定资源
				play.prepare();//准备播放
				play.start();
				//证明音乐开始播放 发送广播
				Intent in=new Intent("start");
				in.putExtra("song", song);
				in.putExtra("song_list", (Serializable)data);
				in.putExtra("index", index);
				sendBroadcast(in);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override //处理从某个位置播放
		public void seekTo(int position) {
			play.seekTo(position);

		} 
	}

	/**
	 * @author pjy
	 *放置一些方法 给主UI调用
	 *
	 */
	public interface IMusicPlayer extends Serializable{
		//播放暂停
		void playOrPuse();
		//播放歌曲
		void playCrrentMusic(Song song,List<Song> data,int index);
		//控制进度
		void seekTo(int position);
	}
	/**
	 * @author pjy
	 *跟新广播信息
	 *
	 */
	class UpdateThread extends Thread {
		@Override
		public void run() {
			super.run();
			//不定期的给广播发送信息
			while (looper) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(play.isPlaying()){
					//跟新信息
					//音乐长度
				int dura=play.getDuration();
					//音乐进度
				int current=play.getCurrentPosition();
					//待定
					//跟新音乐进度的广播
				Intent in=new Intent("update");
				in.putExtra("duration", dura);
				in.putExtra("current", current);
				sendBroadcast(in);
				}
				
				
			}
		}
	}
	@Override //服务的销毁方法
	public void onDestroy() {
		play.reset();
		play.release();//释放mediaplay
		looper=false; //循环终止
		stopThread();
	}
	/**
	 * 停止工作线程
	 */
	public void stopThread() {
		upate=null;
	}

	
}
