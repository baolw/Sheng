package tedu.sheng.ui;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import tedu.sheng.R;
import tedu.sheng.app.MyApplication;
import tedu.sheng.entity.Song;
import tedu.sheng.model.MusicModel;
import tedu.sheng.util.BitmapUtils;
import tedu.sheng.util.CommonUtils;
import tedu.sheng.util.Consts;
import tedu.sheng.util.HttpUtils;

/**
 * Created by Administrator on 2016/6/15.
 */
public class PlayActivity extends Activity implements Consts {


    private CircleImageView civPlayPhoto;
    private ImageView ivPlayBack;
    private TextView tvPlaySong;
    private TextView tvPlaySinger;
    private TextView tvPlayLrc;
    private TextView tvPlayProgress;
    private TextView tvPlayTotal;
    private SeekBar sbPlayProgress;
    private ImageView ivPlayPre;
    private ImageView ivPlayPlay;
    private ImageView ivPlayNext;

    private Song currentSong;
    private MusicModel model;
    RotateAnimation rotateAnimation;
    private Intent broadIntent = new Intent();


    private MyApplication app;
    private int index;

    private boolean isTrackingTouch;

    InnerReceiver receiver;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

civPlayPhoto.clearAnimation();
                    app.setCurrentSong(currentSong);
                    broadIntent.setAction(ACTION_NEXT);
                    broadIntent.putExtra(EXTRA_CURRENT_MUSIC, currentSong);
                    sendBroadcast(broadIntent);

                    break;
                case 1:
                    civPlayPhoto.clearAnimation();
                    app.setCurrentSong(currentSong);
                    broadIntent.setAction(ACTION_PREVIOUS);
                    broadIntent.putExtra(EXTRA_CURRENT_MUSIC, currentSong);
                    sendBroadcast(broadIntent);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        if (savedInstanceState == null) {
            app = (MyApplication) getApplication();
            model = new MusicModel(this);
            currentSong = app.getCurrentSong();
            index = app.getPosition();
            receiver = new InnerReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_SET_AS_PLAY_STATE2);
            filter.addAction(ACTION_SET_AS_PAUSE_STATE2);
            filter.addAction(ACTION_UPDATE_PROGRESS);
            registerReceiver(receiver, filter);
            init();
            setViews();
            setListener();

        }
    }


    private void setViews() {


        if(currentSong!=null) {
            tvPlaySong.setText(currentSong.getTitle());
            tvPlaySinger.setText(currentSong.getArtist_name());

            String path="";
            if("".equals(currentSong.getInfo().getAlbum_500_500())){
                path=currentSong.getInfo().getAlbum_500_500();
            }else if ("".equals(currentSong.getInfo().getAlbum_1000_1000())){
                path=currentSong.getInfo().getAlbum_1000_1000();
            }else if("".equals(currentSong.getInfo().getArtist_480_800())){
                path=currentSong.getInfo().getArtist_480_800();
            }else if("".equals(currentSong.getInfo().getArtist_640_1136())){
                path=currentSong.getInfo().getArtist_480_800();
            }else if("".equals(currentSong.getInfo().getArtist_1000_1000())){
                path=currentSong.getInfo().getArtist_480_800();
            }
            model.displaySingle(path, civPlayPhoto, 260, 260);

            model.displayblur(path, ivPlayBack);

            if (app.getIsRunning()) {
                ivPlayPlay.setImageResource(R.mipmap.pause);

                if(rotateAnimation==null) {
                    rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(10000);
                    rotateAnimation.setRepeatCount(Animation.INFINITE);
                    rotateAnimation.setFillAfter(true);
                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    civPlayPhoto.setAnimation(rotateAnimation);
                }
            } else {

                ivPlayPlay.setImageResource(R.mipmap.play_big);
                if(rotateAnimation!=null) {
                    civPlayPhoto.clearAnimation();
                    rotateAnimation=null;
                }
                //civPlayPhoto.clearAnimation();
            }
        }

    }


    class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_SET_AS_PAUSE_STATE2.equals(action)) {
                setViews();



            } else if (ACTION_SET_AS_PLAY_STATE2.equals(action)) {
                setViews();


            }else if(ACTION_UPDATE_PROGRESS.equals(action)){
                int currentPosition=intent.getIntExtra(EXTRA_CURRENT_POSITION, 0);
                int duration=intent.getIntExtra(EXTRA_DURATION,0);
                sbPlayProgress.setMax(duration);
                if(!isTrackingTouch){
                    sbPlayProgress.setProgress(currentPosition);
                }
                tvPlayProgress.setText(CommonUtils.getFormattedTime(currentPosition));
                tvPlayTotal.setText(CommonUtils.getFormattedTime(duration));

            }
        }


    }

    private void setListener() {
        ivPlayPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSong != null) {
                    broadIntent.setAction(ACTION_PLAY_OR_PAUSE);
                    sendBroadcast(broadIntent);
                }
            }
        });
        ivPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread() {
                    @Override
                    public void run() {

                        index++;
                        if (index >= app.getNetSongs().size()) {
                            index = 0;
                        }

                        currentSong = model.getSongInfo(app.getNetSongs().get(index));

                        handler.sendEmptyMessage(0);
                    }
                }.start();

            }
        });
        ivPlayPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        index--;
                        if (index <= 0) {
                            index = app.getNetSongs().size() - 1;
                        }
                        currentSong = model.getSongInfo(app.getNetSongs().get(index));
                        handler.sendEmptyMessage(1);
                    }
                }.start();

            }
        });
         sbPlayProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {
                 isTrackingTouch=true;

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {

                 isTrackingTouch=false;
                 broadIntent.setAction(ACTION_PLAY_FROM_PROGRESS);
                 broadIntent.putExtra(EXTRA_PROGRESS,sbPlayProgress.getProgress());
                 sendBroadcast(broadIntent);
             }
         });
    }

    private void init() {

        civPlayPhoto = (CircleImageView) findViewById(R.id.civ_play_photo);
        ivPlayBack = (ImageView) findViewById(R.id.iv_play_back);
        tvPlaySong = (TextView) findViewById(R.id.tv_play_song);
        tvPlaySinger = (TextView) findViewById(R.id.tv_play_singer);
        tvPlayLrc = (TextView) findViewById(R.id.tv_play_lrc);
        tvPlayProgress = (TextView) findViewById(R.id.tv_play_progress);
        tvPlayTotal = (TextView) findViewById(R.id.tv_play_total);
        sbPlayProgress = (SeekBar) findViewById(R.id.sb_play_progress);
        ivPlayPre = (ImageView) findViewById(R.id.iv_play_pre);
        ivPlayPlay = (ImageView) findViewById(R.id.iv_play_play);
        ivPlayNext = (ImageView) findViewById(R.id.iv_play_next);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }


}
