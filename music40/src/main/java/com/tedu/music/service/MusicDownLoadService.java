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
 *	����ִ�� ���صĺ�ʱ����
 *
 */
public class MusicDownLoadService extends IntentService{
	//	���һ���޲εĹ���
	public MusicDownLoadService(){
		super("name");
	}
	private static int NOTIFSCTION_ID=10;
	
	/**
	 * @param name
	 * �вι�����
	 */
	public MusicDownLoadService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//ȡ��������Ϣ
		//��
		Song song=(Song) intent.getSerializableExtra("song");
		//�汾
		int version=intent.getIntExtra("version", 0);
		//������ظ�Ŀ¼
		File root=Environment.
				getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		
		//�ж�rootĿ¼�Ƿ����
		if(!root.exists()){
			root.mkdirs();
		}
		//���ص�ַ����
		SongUrl down_url=song.getUrls().get(version);
		//��ϸ���_version+Ʒ��+����.mp3
		String name="_"+version+
				down_url.getFile_bitrate()+
				song.getTitle()+
				".mp3";
		//�������浽sdk����ļ�
		File f=new File(root, name);
		//1��ȡ������
		InputStream is=HttpUtils.getIs(down_url.getShow_link());
		//���ز���
		//����һ�����ȵ��ֽ�����
		byte [] buffer=new byte[1024*200];
		//�洢���е������ֽ�
		int all_byte=0;
		//�ļ���С
		int song_size= Integer.parseInt(down_url.getFile_size());
		//��֪ͨ��ʾ�İٷֱ�
		String count="";
		try {
			int length=is.read(buffer);//�����������ж�ȡ�ļ�����
			all_byte=length;
			FileOutputStream fos=new FileOutputStream(f);
			while (length!=-1) {
				fos.write(buffer, 0, length);
				fos.flush();
				length=is.read(buffer);
				all_byte+=length;
				//�ٷֱ���
			count="�Ѿ�����:"+Math.floor(all_byte*100.0/song_size)+"%";
				if(all_byte==song_size){
					count="���������";
				}
				senNotifaction(count);
				
			}
			
			//���Ĺر�
			is.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		//֪ͨ�ķ���
		

		
	}
	
	/**
	 * ����ϵͳ����֪ͨ
	 */
	/**
	 * @param count ���صİٷֱ�
	 */
	public void  senNotifaction(String count){
		//֪ͨ�������
		NotificationManager manager=(NotificationManager) 
				getSystemService
				(Context.NOTIFICATION_SERVICE);

		//֪ͨ������
		//Notification.Builder api���¼��ݵȼ�Ҫ��Ϊ11����
		Notification.Builder builder=new 
				Notification.Builder
				(getApplicationContext());
		//֪ͨ���Դ���
		builder.setContentTitle("��������");//����
		builder.setContentText(count);//����
		builder.setSmallIcon//ͼ��
		(R.drawable.btn_social_pic_save_normal);
		//����֪ͨ
		Notification no=builder.build();
		//����֪ͨ
		manager.notify(NOTIFSCTION_ID, no);

	}
	
	
}
