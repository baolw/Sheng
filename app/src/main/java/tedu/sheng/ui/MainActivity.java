package tedu.sheng.ui;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tedu.sheng.R;
import tedu.sheng.adapter.MyFragmentPagerAdapter;
import tedu.sheng.entity.Song;
import tedu.sheng.fragment.BankFragment;
import tedu.sheng.fragment.MineFragment;
import tedu.sheng.fragment.NewsFragment;

public class MainActivity extends FragmentActivity {

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

    private List<Song> songs=new ArrayList<Song>();

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
        init();
        setFragment();
        setListener();


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

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setFragment() {
        fragments=new ArrayList<Fragment>();
        bankFragment=new BankFragment();
        fragments.add(bankFragment);
        newsFragment=new NewsFragment();
        fragments.add(newsFragment);
        mineFragment=new MineFragment();
        fragments.add(mineFragment);
        myFragmentPagerAdapter=new MyFragmentPagerAdapter(fragments,getSupportFragmentManager());
        vpNav.setAdapter(myFragmentPagerAdapter);
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
