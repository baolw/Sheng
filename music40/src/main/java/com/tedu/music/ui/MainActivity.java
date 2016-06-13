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
	// ���ڴ洢�л�����Ƭ
	private List<Fragment> list = new ArrayList<Fragment>();
	// ׼��������
	private FragmentPagerAdapter adapter;
	private ViewPager mVp;
	private RadioGroup mRg;
	private ServiceConnection conn;
	private IMusicPlayer musicplay;// ����ui׼���Ĳ������ֵĵĶ���
	private musicBroad broad;// �������ֵĹ㲥

	// ����С��������
	private CircleImageView mCir;// Բ��ͷ��
	private TextView bottom_name;// �Ÿ�����Ϣ
	MusicModel model;
	// �������ز������������
	private LinearLayout mHit;// ���ز���
	private ImageView hBackground, hAlbum, hPlay, hNext, hPre;
	private TextView hSongName, hSongLrc, hCount, hCurrent;
	private SeekBar hSk;
	//�������ּ������ڽ��������б�
	private List<Song> song_list=new ArrayList<Song>();
	//���յ����ֶ�������Ϊȫ�ֱ����Ա����и����
	Song haveUrl;//�е�ַ����
	Song s;//�е�ַ����
	//���ڽ��յ�ǰ���ֵ��±�
	int index;
	//׼����ʼ���
	List<SongLrc> lrcLines=new ArrayList<SongLrc>();
	
	
	/**
	 * ���ջ�ò�����Ϣ���в���
	 */
	private Handler hander=new Handler(){
		public void handleMessage(android.os.Message msg) {
			//��Ϣ��������ƥ��
			switch (msg.what) {
			case 0://ƥ�䲥�Ų���
				//ȡ����Ϣ index song
				int new_index=msg.arg1;
				Song new_song=(Song) msg.obj;
				//���Ÿ���
				musicplay.playCrrentMusic(haveUrl, song_list, new_index);
				break;
			}
			
			
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		model = new MusicModel(this);// ���ַ��������Ż�---
		init();
		hitLin();
		setAdapter();
		Linten();
		bindSer();// day7-�󶨷���
		registBroad();// day7-��̬ע��㲥

	}

	/**
	 * �����ʼ���������
	 */
	public void init() {
		HotFragment hot = new HotFragment();
		NewFragment ne = new NewFragment();
		// ���ݵ����
		list.add(hot);
		list.add(ne);
		mVp = (ViewPager) findViewById(R.id.vp);
		mRg = (RadioGroup) findViewById(R.id.rg);

		// С�������ʼ��
		mCir = (CircleImageView) findViewById(R.id.main_cir_img);
		bottom_name = (TextView) findViewById(R.id.main_name);

	}

	/**
	 * ������������
	 */
	public void setAdapter() {
		// ͨ���ڲ�����ʽ��ȡ������
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
	 * ��������¼�
	 */
	public void Linten() {
		// radio��ť����

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

		// viewpager��Ӽ���
		mVp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:// �е��ȸ�
					mRg.check(R.id.rb_hot);
					break;
				case 2:// �е��¸�
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

	// ���ü̳��෽ʽ���������
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

	// ���󶨷���ķ���
	/**
	 * ���󶨷���ķ���
	 */
	public void bindSer() {
		Intent service = new Intent(MainActivity.this, MusicService.class);
		//
		// conn�����ʼ��
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
				// service�쳣�Ͽ���ʱ��
			}

			@Override // ������������һ������
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
		unbindService(conn);// ��������
		unregisterReceiver(broad);// ���㲥

	}

	//
	/**
	 * �㲥�Ķ�̬ע��
	 */
	public void registBroad() {
		// ��̬ע��㲥����
		// receiver�㲥����, filter ������
		broad = new musicBroad();
		// ����������
		IntentFilter filter = new IntentFilter();
		filter.addAction("start");// ����
		filter.addAction("update");// ����
		registerReceiver(broad, filter);

	}

	// ׼���㲥������ -->���ڽ���service ���������������
	class musicBroad extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals("start")) {
				// ���ֿ�ʼ����
				s = (Song) intent.getSerializableExtra("song");
				// ����ͼƬ��Բ��ͷ��
				String path = s.getPic_small();
				// �Ѹ�������ʾ��ͷ���
				model.displaySingle(path, mCir);
				bottom_name.setText(s.getTitle());
				// ͷ�����ת����
				// ����һ����ת������0��360 ��תһ��,��ͼƬ��������Ϊ��
				RotateAnimation anim = new RotateAnimation(0, 360, mCir.getWidth() / 2, mCir.getHeight() / 2);
				// ���ö�������
				// ����ʱ��
				anim.setDuration(2000);
				// �Ƿ��ظ�
				anim.setRepeatCount(Animation.INFINITE);
				// �Ƿ����������
				anim.setInterpolator(new LinearInterpolator());
				// ��������
				mCir.setAnimation(anim);
				
				//���ؽ���ĸ�ֵ
				//����ͼ
				String bg_path="";
				SongInfo info=s.getInfo();
				if(!"".equals(info.getAlbum_500_500())){
					bg_path=info.getAlbum_500_500();
					
				}else if(!"".equals(info.getAlbum_1000_1000())){
					bg_path=info.getAlbum_1000_1000();
				}else{
					bg_path=s.getPic_big();
				}
				
				//�����ر�����ͼƬ
				model.displaySingle(bg_path, hBackground);
				//����ͼ
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
				
				//����˸��Ҵ����������ּ���-->�и����
				song_list=(List<Song>) intent.getSerializableExtra("song_list");
				index=intent.getIntExtra("index", 0);
			
			  // ��������ʵ�����
				new Thread(new Runnable() {
					public void run() {
					lrcLines=model.getLrc(s.getLrclink());
					}
				}).start();
			
			} else if (action.equals("update")) {
				// ����������Ϣ
				//���϶�����ֵhSk
				int max=intent.getIntExtra("duration", 0);
				hSk.setMax(max);//�������ֵ
				int progress=intent.getIntExtra("current", 0);
				hSk.setProgress(progress);//��ǰ����
				SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
				String time=sdf.format(new Date(progress));
				hCurrent.setText(time);
				String count=sdf.format(new Date(max));
				hCount.setText(count);
				

				//hSongLrc��ʰ�
				for (int i = 0; i <lrcLines.size() ;i++) {
					//ȡ��ÿ���ʶ���
					SongLrc lrc=lrcLines.get(i);
					String content=lrc.getContent();//���
					String lrc_time=lrc.getTime();//ʱ��
					//��ʱ���¸��
					//����ʲ��Ž���ʱ��
					//�͸��ʱ��ƥ���ϾͿ��Ը���
					if(time.equals(lrc_time)){
						hSongLrc.setText(content);
					}
				}
				
			
				
				
				
				
				
			}

		}
	}

	/**
	 * �������ز��ֵ��������
	 */
	public void hitLin() {
		mHit = (LinearLayout) findViewById(R.id.main_lin_hit);
		// ����
		hBackground = (ImageView) findViewById(R.id.hit_bg_img);
		// ר��ͼ
		hAlbum = (ImageView) findViewById(R.id.imageView1);
		// ����ͼ
		hPlay = (ImageView) findViewById(R.id.hit_play_img);
		// ��һ��
		hPre = (ImageView) findViewById(R.id.hit_pre_img);
		;
		hNext = (ImageView) findViewById(R.id.hit_next_img);
		// �ı����

		// ������
		hSongName = (TextView) findViewById(R.id.hit_song_name);

		// ����ʱ��
		hCurrent = (TextView) findViewById(R.id.hit_playtime_txt);
		// ����ʱ��
		hCount = (TextView) findViewById(R.id.hit_duction_txt);
		// ���
		hSongLrc = (TextView) findViewById(R.id.hit_lrc_txt);
		hSk=(SeekBar) findViewById(R.id.hit_ctrol_seek);
		
	}

	// ����ļ����¼�
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.main_cir_img://�ײ�ͷ��
			// ������ɫ����
			// ������ʾ����
			mHit.setVisibility(View.VISIBLE);
			// ����һ�����½���Ļ�
			// TranslateAnimation ƽ�ƵĶ���
			TranslateAnimation anim = new TranslateAnimation(0, 0, mHit.getHeight(), 0);
			anim.setDuration(400);
			mHit.setAnimation(anim);
			break;
		case R.id.hit_play_img://�������ֲ�������ͣ
			musicplay.playOrPuse();
			break;
		case R.id.hit_pre_img://����һ�׸�
			//�õ���һ�׸���±�
			index=index<=0?0:index-1;
			//�����߳� ��������������ز�����Ϣ
			new Thread(new Runnable() {
				public void run() {
					//��ò����б�ĸ���
				 Song noUrl=song_list.get(index);
					//���ؼ�������Ϣ�ĸ���
					 haveUrl=model.getSongInfo(noUrl);
					//����ȡ������Ϣ�ĸ�����ز���
					//���͵�handler ���в��Ų���
					//��Ҫ�Ĳ��� song index song_list(���Բ���)
//					System.out.println();
					Message msg=hander.obtainMessage(0, index, 0, haveUrl);
					hander.sendMessage(msg);
					
				}
			}).start();
			
			
			break;
		case R.id.hit_next_img://����һ�׸�
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
			
			
				//��ͼ��������ת
				Intent intent=new Intent(MainActivity.this, SerachActivity.class);
				//ֱ����ת
				startActivity(intent);
				//ע�� �����ת�����Ƿ�ע��
	
			break;
			case R.id.main_download_btn:
				//��ȡ������Ϣ���ݸ�����
				//���������������ͼ
				//���û��ṩ�Ѻý������ѡ��
				//�Ի������ʽ
				getDialog();
				
				
				break;
			

		}
		

	}

	// //������ǵ�����ؼ���ҵ��
	@Override
	public void onBackPressed() {
		// ���ؽ�����ʾ״̬����ɼ�
		// ��һ�����������ؽ�����������ƽ��ֱ�����ɼ�
		// ���ؽ������ص�
		// ����һ���µ�����ջ�ص���������
		if (mHit.getVisibility() == View.VISIBLE) {
			TranslateAnimation anim = new TranslateAnimation(0, 0, 0, mHit.getHeight());
			anim.setDuration(400);
			mHit.setAnimation(anim);
			mHit.setVisibility(View.INVISIBLE);
		} else {
			// ��ɫ�����ǿɼ���״̬ ����һ���µ�����ջ�ص���ҳ
			Intent in = new Intent(Intent.ACTION_MAIN);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			in.addCategory(Intent.CATEGORY_HOME);
			startActivity(in);
		}
	}
	
	/**
	 * �������Ի�����߼�
	 */
	public void getDialog(){
		//�����Ի��򹹽���
		AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
		//���Ի�������
		String [] items={"����","��ͨ","����"};
		builder.setItems(items, new OnClickListener() {
			
			@Override
			public void onClick(
			DialogInterface dialog,
			 int which//�������һ��
			 ) {
				String msg="������ʾѡ��İ汾";
			 switch (which) {
			case 0://��������
				msg="��ѡ���˼��ٰ�";
				break;
			case 1://��ͨ����
				msg="��ѡ������ͨ��";
				break;
			case 2://���������
				msg="��ѡ���˸����";
				break;
			}
			//ѡ��汾������֤�Ƿ��Ѿ�����
			 if(model.isHave(s, which)){
			 //�Ѿ���ʾ
				 Toast.makeText(MainActivity.this, "�ļ��Ѿ�����", Toast.LENGTH_SHORT).show();
				}else{
				//δ����
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
				//�����������ط���
				Intent down_inten=new Intent(MainActivity.this, MusicDownLoadService.class);
				//���������� ���ֶ���, �汾
				down_inten.putExtra("song", s);//������
				down_inten.putExtra("wersion", which);//������
				startService(down_inten);
				}
			}
		});
		//�����Ի�����ʾ
		builder.create().show();
		
		
		
		
	}

}
