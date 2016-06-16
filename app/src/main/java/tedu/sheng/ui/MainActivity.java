package tedu.sheng.ui;



import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tedu.sheng.R;
import tedu.sheng.adapter.MyFragmentPagerAdapter;
import tedu.sheng.app.MyApplication;
import tedu.sheng.entity.Song;
import tedu.sheng.fragment.BankFragment;
import tedu.sheng.fragment.MineFragment;
import tedu.sheng.fragment.NewsFragment;
import tedu.sheng.model.MusicModel;
import tedu.sheng.service.MusicService;
import tedu.sheng.util.Consts;

public class MainActivity extends FragmentActivity implements Consts{

    //管理父ViewPager
    private List<Fragment> fragments;
    private BankFragment bankFragment;
    private NewsFragment newsFragment;
    private MineFragment mineFragment;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private ViewPager vpNav;


    private ImageView ivMenu;
    private ImageView ivSearch;
    private CircleImageView civPhoto;
    private ImageView ivPlayOrPause;
    private TextView tvSong;
    private TextView tvSinger;

    // RadioGroup相关组件声明
    private RadioGroup rgSup;
    private RadioButton rbSupBank;
    private RadioButton rbSupNews;
    private RadioButton rbSupMine;

    private List<Song> songs=new ArrayList<>();

    private MyApplication app;
    private BroadcastReceiver musicReceiver;
    private Song currentSong;
    private MusicModel model;
    private Intent broadIntent=new Intent();

    private boolean isRunning=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }*/
        setContentView(R.layout.activity_main);
        app= (MyApplication) getApplication();

        model=new MusicModel(this);
        //开启服务
        Intent intent=new Intent(MainActivity.this,MusicService.class);
        startService(intent);
        //注册广播
        registBroadcast();
        init();
        setFragment();
        setListener();

    }

    private void registBroadcast() {
        musicReceiver=new musicReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ACTION_SET_AS_PLAY_STATE);
        filter.addAction(ACTION_SET_AS_PAUSE_STATE);
        filter.addAction(ACTION_UPDATE_PROGRESS);
        registerReceiver(musicReceiver,filter);
    }

    class musicReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            RotateAnimation  rotateAnimation=null;
            String action=intent.getAction();

            if(ACTION_SET_AS_PLAY_STATE.equals(action)){

                isRunning=true;
                currentSong=app.getCurrentSong();
                ivPlayOrPause.setImageResource(R.mipmap.pause);
                tvSong.setText(currentSong.getTitle());
                tvSinger.setText(currentSong.getArtist_name());

                if(rotateAnimation==null) {
                    model.displaySingle(currentSong.getInfo().getAlbum_500_500(), civPhoto);
                    rotateAnimation = new RotateAnimation(0, 360, civPhoto.getWidth() / 2, civPhoto.getHeight() / 2);
                    rotateAnimation.setDuration(2000);
                    rotateAnimation.setRepeatCount(Animation.INFINITE);
                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    civPhoto.setAnimation(rotateAnimation);
                }else{
                    civPhoto.setAnimation(rotateAnimation);
                }

            }else if(ACTION_SET_AS_PAUSE_STATE.equals(action)){
                isRunning=false;
                civPhoto.clearAnimation();
                ivPlayOrPause.setImageResource(R.mipmap.play);
            }
        }
    }


    private void setListener() {
        vpNav.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        rbSupBank.setChecked(true);
                        break;
                    case 1:
                        rbSupNews.setChecked(true);
                        break;
                    case 2:
                        rbSupMine.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rgSup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_sup_bank:
                        vpNav.setCurrentItem(0);
                        rbSupBank.setTextSize(18);
                        rbSupNews.setTextSize(16);
                        rbSupMine.setTextSize(16);
                        break;
                     case R.id.rb_sup_news:
                        vpNav.setCurrentItem(1);
                        rbSupNews.setTextSize(18);
                         rbSupBank.setTextSize(16);
                         rbSupMine.setTextSize(16);
                         break;
                     case R.id.rb_sup_mine:
                        vpNav.setCurrentItem(2);
                        rbSupMine.setTextSize(18);
                         rbSupBank.setTextSize(16);
                         rbSupNews.setTextSize(16);
                         break;
                }
            }
        });

        ivPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentSong!=null){
                    if(currentSong!=null) {
                        broadIntent.setAction(ACTION_PLAY_OR_PAUSE);
                        sendBroadcast(broadIntent);
                    }
                }
            }
        });

        civPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent=new Intent(MainActivity.this,PlayActivity.class);
                playIntent.putExtra(EXTRA_CURRENT_MUSIC,currentSong);

                startActivity(playIntent);
            }
        });

    }

    private void setFragment() {

        fragments=new ArrayList<>();

        bankFragment=new BankFragment();
        fragments.add(bankFragment);

        newsFragment=new NewsFragment();
        fragments.add(newsFragment);

        mineFragment=new MineFragment();
        fragments.add(mineFragment);

        myFragmentPagerAdapter=new MyFragmentPagerAdapter(fragments,getSupportFragmentManager());
        vpNav.setAdapter(myFragmentPagerAdapter);
        vpNav.setOffscreenPageLimit(3);
    }


    private void init() {
        ivMenu= (ImageView) findViewById(R.id.iv_menu);
        ivSearch= (ImageView) findViewById(R.id.iv_search);
        civPhoto= (CircleImageView) findViewById(R.id.civ_photo);
        ivPlayOrPause= (ImageView) findViewById(R.id.iv_playorpause);
        tvSong= (TextView) findViewById(R.id.tv_song);
        tvSinger= (TextView) findViewById(R.id.tv_singer);
        vpNav= (ViewPager) findViewById(R.id.vp_nav);
        rgSup= (RadioGroup) findViewById(R.id.rg_sup);
        rbSupBank= (RadioButton) findViewById(R.id.rb_sup_bank);
        rbSupNews= (RadioButton) findViewById(R.id.rb_sup_news);
        rbSupMine= (RadioButton) findViewById(R.id.rb_sup_mine);
    }



}
