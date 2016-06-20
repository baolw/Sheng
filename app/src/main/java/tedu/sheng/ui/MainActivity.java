package tedu.sheng.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tencent.tauth.Tencent;

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
import tedu.sheng.util.StatusBarCompat;

public class MainActivity extends FragmentActivity implements Consts{

    //管理父ViewPager
    private List<Fragment> fragments;
    private BankFragment bankFragment;
    private NewsFragment newsFragment;
    private MineFragment mineFragment;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private ViewPager vpNav;


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
    private MyApplication app;
    private BroadcastReceiver musicReceiver;
    private Song currentSong;
    private MusicModel model;
    private Intent broadIntent=new Intent();
    private LinearLayout llSongSinger;


    private TextView tvWelcome;
    private TextView tvLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
            StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimary));
        }


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
        filter.addAction(ACTION_START_MUSIC_TRAVERL);
        registerReceiver(musicReceiver,filter);
    }

    class musicReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            RotateAnimation  rotateAnimation=null;
            String action=intent.getAction();


            if(ACTION_SET_AS_PLAY_STATE.equals(action)) {
                if (app.getIsNetWork()) {

                    currentSong = app.getCurrentSong();
                    ivPlayOrPause.setImageResource(R.mipmap.pause);
                    tvSong.setText(currentSong.getTitle());
                    tvSinger.setText(currentSong.getArtist_name());

                    if("".equals(currentSong.getInfo().getAlbum_500_500())){
                        civPhoto.setImageResource(R.mipmap.album);
                    }else{

                        model.displaySingle(currentSong.getInfo().getAlbum_500_500(), civPhoto, 60, 60);
                    }
                    if (rotateAnimation == null) {

                        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotateAnimation.setDuration(10000);
                        rotateAnimation.setRepeatCount(Animation.INFINITE);
                        rotateAnimation.setInterpolator(new LinearInterpolator());
                        civPhoto.setAnimation(rotateAnimation);
                    } else {
                        civPhoto.setAnimation(rotateAnimation);
                    }

                }
                    else{
                    if(app.getLocalSongs().get(app.getCurrentIndex()).getAlbumArt() == null) {
                        civPhoto.setImageResource(R.mipmap.album);
                    } else {
                        Bitmap bm= BitmapFactory.decodeFile(app.getLocalSongs().get(app.getCurrentIndex()).getAlbumArt());
                        civPhoto.setImageBitmap(bm);
                    }
                    ivPlayOrPause.setImageResource(R.mipmap.pause);
                        tvSong.setText(app.getLocalSongs().get(app.getCurrentIndex()).getName());
                        tvSinger.setText(app.getLocalSongs().get(app.getCurrentIndex()).getArtist());
                    rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(10000);
                    rotateAnimation.setRepeatCount(Animation.INFINITE);
                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    civPhoto.setAnimation(rotateAnimation);
                    }

                }

                else if (ACTION_SET_AS_PAUSE_STATE.equals(action)) {

                    civPhoto.clearAnimation();
                    ivPlayOrPause.setImageResource(R.mipmap.play);
                } else if (ACTION_START_MUSIC_TRAVERL.equals(action)) {
                    llSongSinger.setVisibility(View.VISIBLE);
                    civPhoto.setVisibility(View.VISIBLE);
                    tvWelcome.setVisibility(View.GONE);

                }else if(ACTION_QQ_IMAGE.equals(action)){
                    tvLogin.setVisibility(View.GONE);
                civPhoto.setVisibility(View.VISIBLE);
                Bitmap bitmap=intent.getParcelableExtra(EXTRA_QQ_IMAGE);
                civPhoto.setImageBitmap(bitmap);
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
                        rbSupNews.setChecked(true);
                        break;
                    case 1:
                        rbSupMine.setChecked(true);
                        break;
                    case 2:
                        rbSupBank.setChecked(true);
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
                    case R.id.rb_sup_news:
                        vpNav.setCurrentItem(0);
                        rbSupNews.setTextSize(18);
                        rbSupBank.setTextSize(16);
                        rbSupMine.setTextSize(16);

                        break;
                     case R.id.rb_sup_mine:
                        vpNav.setCurrentItem(1);

                         rbSupMine.setTextSize(18);
                         rbSupBank.setTextSize(16);
                         rbSupNews.setTextSize(16);
                         break;
                     case R.id.rb_sup_bank:
                         vpNav.setCurrentItem(2);
                         rbSupBank.setTextSize(18);
                         rbSupNews.setTextSize(16);
                         rbSupMine.setTextSize(16);
                         break;
                }
            }
        });

        ivPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.getIsNetWork()) {
                    if (currentSong != null) {
                        if (currentSong != null) {
                            broadIntent.setAction(ACTION_PLAY_OR_PAUSE);
                            sendBroadcast(broadIntent);
                        }
                    }
                }else{
                    if(app.getCurrentIndex()!=-1){
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

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setFragment() {

        fragments=new ArrayList<>();


        newsFragment=new NewsFragment();
        fragments.add(newsFragment);

        mineFragment=new MineFragment();
        fragments.add(mineFragment);

        bankFragment=new BankFragment();
        fragments.add(bankFragment);

        myFragmentPagerAdapter=new MyFragmentPagerAdapter(fragments,getSupportFragmentManager());
        vpNav.setAdapter(myFragmentPagerAdapter);
        vpNav.setOffscreenPageLimit(3);
    }


    private void init() {
        tvLogin= (TextView) findViewById(R.id.tv_Login);
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
        llSongSinger= (LinearLayout) findViewById(R.id.ll_song_singer);
        tvWelcome= (TextView) findViewById(R.id.tv_welcome);

    }
    // //处理就是点击返回键的业务
    @Override
    public void onBackPressed() {
        // 隐藏界面显示状态如果可见
        // 加一个动画让隐藏界面从最大向下平移直至不可见
        // 隐藏界面隐藏掉
        // 创建一个新的任务栈回到启动界面

            Intent in = new Intent(Intent.ACTION_MAIN);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.addCategory(Intent.CATEGORY_HOME);
            startActivity(in);
        }

    @Override
    protected void onDestroy() {


        if(musicReceiver!=null){
            unregisterReceiver(musicReceiver);
            musicReceiver=null;
        }
        super.onDestroy();

    }
}
