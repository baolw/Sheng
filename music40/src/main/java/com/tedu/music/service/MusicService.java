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
 *         �ṩ���ֲ��ŵ���ط���
 */
public class MusicService extends Service {
	private MediaPlayer play;
	private boolean looper;

	
	private  UpdateThread upate=null;
	@Override //���ݵĳ�ʼ��
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
	 * bind��
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
				play.reset();//����һ��
				play.setDataSource(song.getUrls().get(0).getShow_link());//����Դ
				play.prepare();//׼������
				play.start();
				//֤�����ֿ�ʼ���� ���͹㲥
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

		@Override //�����ĳ��λ�ò���
		public void seekTo(int position) {
			play.seekTo(position);

		} 
	}

	/**
	 * @author pjy
	 *����һЩ���� ����UI����
	 *
	 */
	public interface IMusicPlayer extends Serializable{
		//������ͣ
		void playOrPuse();
		//���Ÿ���
		void playCrrentMusic(Song song,List<Song> data,int index);
		//���ƽ���
		void seekTo(int position);
	}
	/**
	 * @author pjy
	 *���¹㲥��Ϣ
	 *
	 */
	class UpdateThread extends Thread {
		@Override
		public void run() {
			super.run();
			//�����ڵĸ��㲥������Ϣ
			while (looper) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(play.isPlaying()){
					//������Ϣ
					//���ֳ���
				int dura=play.getDuration();
					//���ֽ���
				int current=play.getCurrentPosition();
					//����
					//�������ֽ��ȵĹ㲥
				Intent in=new Intent("update");
				in.putExtra("duration", dura);
				in.putExtra("current", current);
				sendBroadcast(in);
				}
				
				
			}
		}
	}
	@Override //��������ٷ���
	public void onDestroy() {
		play.reset();
		play.release();//�ͷ�mediaplay
		looper=false; //ѭ����ֹ
		stopThread();
	}
	/**
	 * ֹͣ�����߳�
	 */
	public void stopThread() {
		upate=null;
	}

	
}
