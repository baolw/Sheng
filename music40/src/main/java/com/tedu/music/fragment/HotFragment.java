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
	private IMusicPlayer player;//播放对象
	//用于接收当前播放音乐的下标
	private int index;
	//声明新结合用于接收新请求的数据
	private List<Song> new_data=new ArrayList<Song>();
	
	
	//创建公共方法给player对象赋值
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
				//执行播放业务
				//查完地址就播放
				if(s!=null){
				player.playCrrentMusic(s,list,index);
				}
				break;
			case 2://执行分页加载数据的更新
				//从msg中获取新数据
				List<Song> list_song=(List<Song>) msg.obj;
				//将新数据添加到原始数据集合内
				list.addAll(list_song);
				//通知适配器刷新
				adapter.notifyDataSetChanged();
			break;
				
			}
		};
	};
	@Override//填充布局
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v=inflater.inflate(R.layout.music_list, null);
		model=new MusicModel(getActivity());
		mLv=(ListView) v.findViewById(R.id.listView1);
		getlist();
		listen();
		return v;
	}
	
	//发送网络请求获取数据
	public void getlist(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			list=model.getSong(0, 20);
			//把数据发送message 出来
			//让handler 做数据第一次加载
			handler.sendEmptyMessage(0);
			
			}
		}).start();
	}
	
	//添加监听器
	public void listen(){
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				//点击获取歌曲的其他信息
				index=position;
				new Thread( new Runnable() {
					public void run() {
						//list.get(position) 获取的歌曲没有播放地址的
						Song s=model.getSongInfo(list.get(position));
						Message msg=handler.obtainMessage(1,s);
						handler.sendMessage(msg);
					}
				}).start(); 
				
				
			}
		});
	
		//listview添加滚动监听
		mLv.setOnScrollListener(new OnScrollListener() {
			//声明布尔类型变量-->监控我们是否滑动到末尾
			boolean atbottom=false;
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//三种状态
				//SCROLL_STATE_FLING
				//SCROLL_STATE_IDLE
				//SCROLL_STATE_TOUCH_SCROLL
			   switch (scrollState) {
			   case OnScrollListener.SCROLL_STATE_FLING:
				
				break;
				//让listview 空闲的时候更新数据
			   case OnScrollListener.SCROLL_STATE_IDLE:
				   if(atbottom){
					  // 向服务器发送请求;
					   new Thread(new Runnable() {
						public void run() {
							new_data=model.getSong(list.size(), 20);
							//借用hanlder 将数据传递到UI层进行更新
							Message msg=handler.obtainMessage(2, new_data);
							//send发送
							handler.sendMessage(msg);
							System.out.println("验证数据是否叠加"+new_data.size());
						}
					}).start();

					   //判断请求回的数据集合是否存在数据
					   if(new_data.size()<1){
						   //没有数据弹出提示
						   Toast.makeText(getActivity(), "已经没有可加载的数据了", Toast.LENGTH_SHORT).show();
						   
					   }
				   }
				   
				   break;
			   case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				   
				   break;
				
				
				

			}
				
				
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				//如何判断已经滚动 listview末尾
				if(firstVisibleItem+
				visibleItemCount==totalItemCount){
					atbottom=true;//通知liextview空闲更新
				}else{
					atbottom=false;
				}
				
				
				
				
			}
		});
		
	}
	

}


















