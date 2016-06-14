package tedu.sheng.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import tedu.sheng.entity.Song;


public class MusicService extends Service {
	private MediaPlayer play;
	private boolean looper;


	
	private  UpdateThread upate=null;
	@Override
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
		public void playCurrentMusic(Song song, List<Song> data, int index) {
			
			try {
				play.reset();
				play.setDataSource(song.getUrls().get(0).getShow_link());
				play.prepare();
				play.start();

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

		@Override
		public void seekTo(int position) {
			play.seekTo(position);

		} 
	}


	public interface IMusicPlayer extends Serializable{

		void playOrPuse();

		void playCurrentMusic(Song song, List<Song> data, int index);

		void seekTo(int position);
	}

	class UpdateThread extends Thread {
		@Override
		public void run() {
			super.run();

			while (looper) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(play.isPlaying()){

				int dura=play.getDuration();

				int current=play.getCurrentPosition();

				Intent in=new Intent("update");
				in.putExtra("duration", dura);
				in.putExtra("current", current);
				sendBroadcast(in);
				}
				
				
			}
		}
	}
	@Override
	public void onDestroy() {
		play.reset();
		play.release();
		looper=false;
		stopThread();
	}

	public void stopThread() {
		upate=null;
	}

	
}
