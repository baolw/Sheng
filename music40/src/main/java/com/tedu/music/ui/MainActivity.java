package com.tedu.music.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tedu.music.entity.Song;
import com.tedu.music.entity.SongInfo;
import com.tedu.music.entity.SongLrc;
import com.tedu.music.fragment.HotFragment;
import com.tedu.music.fragment.NewFragment;
import com.tedu.music.model.MusicModel;
import com.tedu.music.service.MusicDownLoadService;
import com.tedu.music.service.MusicService;
import com.tedu.music.service.MusicService.IMusicPlayer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author pjy
 *
 */
public class MainActivity extends FragmentActivity {
	// 用于存储切换的碎片
	private List<Fragment> list = new ArrayList<Fragment>();
	// 准备适配器
	private FragmentPagerAdapter adapter;
	private ViewPager mVp;
	private RadioGroup mRg;
	private ServiceConnection conn;
	private IMusicPlayer musicplay;// 给主ui准备的操作音乐的的对象
	private musicBroad broad;// 跟新音乐的广播

	// 声明小条里的组件
	private CircleImageView mCir;// 圆形头像
	private TextView bottom_name;// 放歌手信息
	MusicModel model;
	// 处理隐藏布局组件的声明
	private LinearLayout mHit;// 隐藏布局
	private ImageView hBackground, hAlbum, hPlay, hNext, hPre;
	private TextView hSongName, hSongLrc, hCount, hCurrent;
	private SeekBar hSk;
	//声明音乐集合用于接收音乐列表
	private List<Song> song_list=new ArrayList<Song>();
	//接收的音乐对象声明为全局变量以便于切歌操作
	Song haveUrl;//有地址歌曲
	Song s;//有地址歌曲
	//用于接收当前音乐的下标
	int index;
	//准备歌词集合
	List<SongLrc> lrcLines=new ArrayList<SongLrc>();
	
	
	/**
	 * 接收获得参数信息进行播放
	 */
	private Handler hander=new Handler(){
		public void handleMessage(android.os.Message msg) {
			//消息参数进行匹配
			switch (msg.what) {
			case 0://匹配播放操作
				//取得信息 index song
				int new_index=msg.arg1;
				Song new_song=(Song) msg.obj;
				//播放歌曲
				musicplay.playCrrentMusic(haveUrl, song_list, new_index);
				break;
			}
			
			
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		model = new MusicModel(this);// 这种方法还需优化---
		init();
		hitLin();
		setAdapter();
		Linten();
		bindSer();// day7-绑定服务
		registBroad();// day7-动态注册广播

	}

	/**
	 * 处理初始化相关问题
	 */
	public void init() {
		HotFragment hot = new HotFragment();
		NewFragment ne = new NewFragment();
		// 数据的添加
		list.add(hot);
		list.add(ne);
		mVp = (ViewPager) findViewById(R.id.vp);
		mRg = (RadioGroup) findViewById(R.id.rg);

		// 小条组件初始化
		mCir = (CircleImageView) findViewById(R.id.main_cir_img);
		bottom_name = (TextView) findViewById(R.id.main_name);

	}

	/**
	 * 处理适配器绑定
	 */
	public void setAdapter() {
		// 通过内部类形式获取适配器
		adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return list.get(arg0);
			}
		};

		mVp.setAdapter(adapter);
	}

	/**
	 * 处理监听事件
	 */
	public void Linten() {
		// radio按钮监听

		mRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.rb_hot:
					mVp.setCurrentItem(0);
					break;
				case R.id.rb_new:
					mVp.setCurrentItem(1);
					break;
				}
			}
		});

		// viewpager添加监听
		mVp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:// 切的热歌
					mRg.check(R.id.rb_hot);
					break;
				case 2:// 切的新歌
					mRg.check(R.id.rb_new);
					break;

				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			} // hushi

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// hushi
			}
		});

	}

	// 采用继承类方式获得适配器
	// class myFr extends FragmentPagerAdapter{
	//
	// public myFr(FragmentManager fm) {
	// super(fm);
	// // TODO Auto-generated constructor stub
	// }
	//
	// @Override
	// public Fragment getItem(int arg0) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public int getCount() {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// }

	// 创绑定服务的方法
	/**
	 * 创绑定服务的方法
	 */
	public void bindSer() {
		Intent service = new Intent(MainActivity.this, MusicService.class);
		//
		// conn对象初始化
		// conn=new ServiceConnection() {
		//
		// @Override
		// public void onServiceDisconnected(ComponentName name) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onServiceConnected(ComponentName name, IBinder service) {
		// // TODO Auto-generated method stub
		//
		// }
		// };

		bindService(service, conn = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// service异常断开的时候
			}

			@Override // 服务政策启动一个方法
			public void onServiceConnected(ComponentName name, IBinder service) {
				musicplay = (IMusicPlayer) service;
				HotFragment hot = (HotFragment) list.get(0);
				hot.setPlay(musicplay);

			}
		}, BIND_AUTO_CREATE);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);// 服务解除绑定
		unregisterReceiver(broad);// 解绑广播

	}

	//
	/**
	 * 广播的动态注册
	 */
	public void registBroad() {
		// 动态注册广播方法
		// receiver广播对象, filter 过滤器
		broad = new musicBroad();
		// 创建过滤器
		IntentFilter filter = new IntentFilter();
		filter.addAction("start");// 播放
		filter.addAction("update");// 更新
		registerReceiver(broad, filter);

	}

	// 准备广播接收器 -->用于接收service 发过来的相关请求
	class musicBroad extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals("start")) {
				// 音乐开始播放
				s = (Song) intent.getSerializableExtra("song");
				// 加载图片到圆形头像
				String path = s.getPic_small();
				// 把歌手名显示在头像后
				model.displaySingle(path, mCir);
				bottom_name.setText(s.getTitle());
				// 头像加旋转动画
				// 创建一个旋转动画从0到360 旋转一周,以图片本身中心为轴
				RotateAnimation anim = new RotateAnimation(0, 360, mCir.getWidth() / 2, mCir.getHeight() / 2);
				// 设置动画属性
				// 持续时间
				anim.setDuration(2000);
				// 是否重复
				anim.setRepeatCount(Animation.INFINITE);
				// 是否添加了匀速
				anim.setInterpolator(new LinearInterpolator());
				// 启动动画
				mCir.setAnimation(anim);
				
				//隐藏界面的赋值
				//背景图
				String bg_path="";
				SongInfo info=s.getInfo();
				if(!"".equals(info.getAlbum_500_500())){
					bg_path=info.getAlbum_500_500();
					
				}else if(!"".equals(info.getAlbum_1000_1000())){
					bg_path=info.getAlbum_1000_1000();
				}else{
					bg_path=s.getPic_big();
				}
				
				//给隐藏背景绑定图片
				model.displaySingle(bg_path, hBackground);
				//歌曲图
				String artist_path="";
				if(!"".equals(info.getArtist_480_800())){
					artist_path=info.getArtist_480_800();
					
				}else if(!"".equals(info.getArtist_640_1136())){
					
					artist_path=info.getArtist_640_1136();
				}else if(!"".equals(info.getArtist_1000_1000())){
					artist_path=info.getAlbum_1000_1000();
					
				}else{
					artist_path=s.getPic_small();
				}
				model.displaySingle(artist_path, hAlbum);
				hSongName.setText(s.getTitle());
				
				//服务端给我传回来的音乐集合-->切歌服务
				song_list=(List<Song>) intent.getSerializableExtra("song_list");
				index=intent.getIntExtra("index", 0);
			
			  // 这里做歌词的下载
				new Thread(new Runnable() {
					public void run() {
					lrcLines=model.getLrc(s.getLrclink());
					}
				}).start();
			
			} else if (action.equals("update")) {
				// 跟新音乐信息
				//给拖动条赋值hSk
				int max=intent.getIntExtra("duration", 0);
				hSk.setMax(max);//进度最大值
				int progress=intent.getIntExtra("current", 0);
				hSk.setProgress(progress);//当前进度
				SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
				String time=sdf.format(new Date(progress));
				hCurrent.setText(time);
				String count=sdf.format(new Date(max));
				hCount.setText(count);
				

				//hSongLrc歌词绑定
				for (int i = 0; i <lrcLines.size() ;i++) {
					//取出每句歌词对象
					SongLrc lrc=lrcLines.get(i);
					String content=lrc.getContent();//歌词
					String lrc_time=lrc.getTime();//时间
					//何时更新歌词
					//当歌词播放进度时间
					//和歌词时间匹配上就可以跟新
					if(time.equals(lrc_time)){
						hSongLrc.setText(content);
					}
				}
				
			
				
				
				
				
				
			}

		}
	}

	/**
	 * 处理隐藏布局的组件问题
	 */
	public void hitLin() {
		mHit = (LinearLayout) findViewById(R.id.main_lin_hit);
		// 背景
		hBackground = (ImageView) findViewById(R.id.hit_bg_img);
		// 专辑图
		hAlbum = (ImageView) findViewById(R.id.imageView1);
		// 控制图
		hPlay = (ImageView) findViewById(R.id.hit_play_img);
		// 上一首
		hPre = (ImageView) findViewById(R.id.hit_pre_img);
		;
		hNext = (ImageView) findViewById(R.id.hit_next_img);
		// 文本组件

		// 歌曲名
		hSongName = (TextView) findViewById(R.id.hit_song_name);

		// 播放时间
		hCurrent = (TextView) findViewById(R.id.hit_playtime_txt);
		// 音乐时长
		hCount = (TextView) findViewById(R.id.hit_duction_txt);
		// 歌词
		hSongLrc = (TextView) findViewById(R.id.hit_lrc_txt);
		hSk=(SeekBar) findViewById(R.id.hit_ctrol_seek);
		
	}

	// 点击的监听事件
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.main_cir_img://底部头像
			// 弹出黄色界面
			// 界面显示出来
			mHit.setVisibility(View.VISIBLE);
			// 加入一个从下进入的画
			// TranslateAnimation 平移的动画
			TranslateAnimation anim = new TranslateAnimation(0, 0, mHit.getHeight(), 0);
			anim.setDuration(400);
			mHit.setAnimation(anim);
			break;
		case R.id.hit_play_img://控制音乐播放与暂停
			musicplay.playOrPuse();
			break;
		case R.id.hit_pre_img://切上一首歌
			//得到上一首歌的下标
			index=index<=0?0:index-1;
			//调用线程 歌曲这个歌曲加载播放信息
			new Thread(new Runnable() {
				public void run() {
					//获得不带列表的歌曲
				 Song noUrl=song_list.get(index);
					//返回加载完信息的歌曲
					 haveUrl=model.getSongInfo(noUrl);
					//将获取完整信息的歌曲相关参数
					//发送到handler 进行播放操作
					//需要的参数 song index song_list(可以不传)
//					System.out.println();
					Message msg=hander.obtainMessage(0, index, 0, haveUrl);
					hander.sendMessage(msg);
					
				}
			}).start();
			
			
			break;
		case R.id.hit_next_img://切下一首歌
			index=index>=(song_list.size()-1)?song_list.size():index+1;
			new Thread(new Runnable() {
				public void run() {
					 Song noUrl=song_list.get(index);
					 haveUrl=model.getSongInfo(noUrl);
					Message msg=hander.obtainMessage(0,index,0,s);
					hander.sendMessage(msg);
				}
			}).start();
			 
		
			break;
			case R.id.main_serach_btn:
			
			
				//意图做界面跳转
				Intent intent=new Intent(MainActivity.this, SerachActivity.class);
				//直接跳转
				startActivity(intent);
				//注意 检查跳转界面是否注册
	
			break;
			case R.id.main_download_btn:
				//获取下载信息传递给服务
				//创建启动服务的意图
				//给用户提供友好界面进行选择
				//对话框的形式
				getDialog();
				
				
				break;
			

		}
		

	}

	// //处理就是点击返回键的业务
	@Override
	public void onBackPressed() {
		// 隐藏界面显示状态如果可见
		// 加一个动画让隐藏界面从最大向下平移直至不可见
		// 隐藏界面隐藏掉
		// 创建一个新的任务栈回到启动界面
		if (mHit.getVisibility() == View.VISIBLE) {
			TranslateAnimation anim = new TranslateAnimation(0, 0, 0, mHit.getHeight());
			anim.setDuration(400);
			mHit.setAnimation(anim);
			mHit.setVisibility(View.INVISIBLE);
		} else {
			// 黄色布局是可见的状态 开辟一个新的任务栈回到主页
			Intent in = new Intent(Intent.ACTION_MAIN);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			in.addCategory(Intent.CATEGORY_HOME);
			startActivity(in);
		}
	}
	
	/**
	 * 处理创建对话框的逻辑
	 */
	public void getDialog(){
		//创建对话框构建器
		AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
		//给对话框设置
		String [] items={"极速","普通","高清"};
		builder.setItems(items, new OnClickListener() {
			
			@Override
			public void onClick(
			DialogInterface dialog,
			 int which//点击了哪一个
			 ) {
				String msg="用于提示选择的版本";
			 switch (which) {
			case 0://极速下载
				msg="你选择了极速板";
				break;
			case 1://普通下载
				msg="你选择了普通板";
				break;
			case 2://高清的下载
				msg="你选择了高清板";
				break;
			}
			//选择版本后先验证是否已经下载
			 if(model.isHave(s, which)){
			 //已经提示
				 Toast.makeText(MainActivity.this, "文件已经存在", Toast.LENGTH_SHORT).show();
				}else{
				//未下载
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
				//真正启动下载服务
				Intent down_inten=new Intent(MainActivity.this, MusicDownLoadService.class);
				//参数的设置 音乐对象, 版本
				down_inten.putExtra("song", s);//传音乐
				down_inten.putExtra("wersion", which);//传音乐
				startService(down_inten);
				}
			}
		});
		//创建对话框并显示
		builder.create().show();
		
		
		
		
	}

}
