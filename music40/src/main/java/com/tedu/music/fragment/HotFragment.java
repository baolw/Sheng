package com.tedu.music.fragment;

import java.util.ArrayList;
import java.util.List;

import com.tedu.music.adapter.MusicAdapter;
import com.tedu.music.entity.Song;
import com.tedu.music.model.MusicModel;
import com.tedu.music.service.MusicService.IMusicPlayer;
import com.tedu.music.ui.MainActivity;
import com.tedu.music.ui.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HotFragment extends Fragment{
	private MusicModel model;
	private List<Song> list=new ArrayList<Song>();
	private  ListView mLv;
	private MusicAdapter adapter;
	private IMusicPlayer player;//���Ŷ���
	//���ڽ��յ�ǰ�������ֵ��±�
	private int index;
	//�����½�����ڽ��������������
	private List<Song> new_data=new ArrayList<Song>();
	
	
	//��������������player����ֵ
	public void setPlay(IMusicPlayer play){
		this.player=play;
	}
	
	
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 0:
				adapter=new MusicAdapter(list, getActivity(),model);
				mLv.setAdapter(adapter);
				break;
			case 1:
				Song s=(Song) msg.obj;
				//ִ�в���ҵ��
				//�����ַ�Ͳ���
				if(s!=null){
				player.playCrrentMusic(s,list,index);
				}
				break;
			case 2://ִ�з�ҳ�������ݵĸ���
				//��msg�л�ȡ������
				List<Song> list_song=(List<Song>) msg.obj;
				//����������ӵ�ԭʼ���ݼ�����
				list.addAll(list_song);
				//֪ͨ������ˢ��
				adapter.notifyDataSetChanged();
			break;
				
			}
		};
	};
	@Override//��䲼��
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v=inflater.inflate(R.layout.music_list, null);
		model=new MusicModel(getActivity());
		mLv=(ListView) v.findViewById(R.id.listView1);
		getlist();
		listen();
		return v;
	}
	
	//�������������ȡ����
	public void getlist(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			list=model.getSong(0, 20);
			//�����ݷ���message ����
			//��handler �����ݵ�һ�μ���
			handler.sendEmptyMessage(0);
			
			}
		}).start();
	}
	
	//��Ӽ�����
	public void listen(){
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				//�����ȡ������������Ϣ
				index=position;
				new Thread( new Runnable() {
					public void run() {
						//list.get(position) ��ȡ�ĸ���û�в��ŵ�ַ��
						Song s=model.getSongInfo(list.get(position));
						Message msg=handler.obtainMessage(1,s);
						handler.sendMessage(msg);
					}
				}).start(); 
				
				
			}
		});
	
		//listview��ӹ�������
		mLv.setOnScrollListener(new OnScrollListener() {
			//�����������ͱ���-->��������Ƿ񻬶���ĩβ
			boolean atbottom=false;
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//����״̬
				//SCROLL_STATE_FLING
				//SCROLL_STATE_IDLE
				//SCROLL_STATE_TOUCH_SCROLL
			   switch (scrollState) {
			   case OnScrollListener.SCROLL_STATE_FLING:
				
				break;
				//��listview ���е�ʱ���������
			   case OnScrollListener.SCROLL_STATE_IDLE:
				   if(atbottom){
					  // ���������������;
					   new Thread(new Runnable() {
						public void run() {
							new_data=model.getSong(list.size(), 20);
							//����hanlder �����ݴ��ݵ�UI����и���
							Message msg=handler.obtainMessage(2, new_data);
							//send����
							handler.sendMessage(msg);
							System.out.println("��֤�����Ƿ����"+new_data.size());
						}
					}).start();

					   //�ж�����ص����ݼ����Ƿ��������
					   if(new_data.size()<1){
						   //û�����ݵ�����ʾ
						   Toast.makeText(getActivity(), "�Ѿ�û�пɼ��ص�������", Toast.LENGTH_SHORT).show();
						   
					   }
				   }
				   
				   break;
			   case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				   
				   break;
				
				
				

			}
				
				
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				//����ж��Ѿ����� listviewĩβ
				if(firstVisibleItem+
				visibleItemCount==totalItemCount){
					atbottom=true;//֪ͨliextview���и���
				}else{
					atbottom=false;
				}
				
				
				
				
			}
		});
		
	}
	

}


















