package tedu.sheng.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import tedu.sheng.R;
import tedu.sheng.app.MyApplication;
import tedu.sheng.entity.Song;
import tedu.sheng.model.MusicModel;
import tedu.sheng.util.Consts;

/**
 * Created by Administrator on 2016/6/15.
 */
public class PlayActivity extends Activity implements Consts{


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
    private Intent broadIntent=new Intent();


    private MyApplication app;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        app= (MyApplication) getApplication();
        model=new MusicModel(this);
        currentSong=app.getCurrentSong();
        index=app.getPosition();

        init();
        setViews();
        setListener();

    }

    private void setViews() {
        if(currentSong!=null){
            tvPlaySong.setText(currentSong.getTitle());
            tvPlaySinger.setText(currentSong.getArtist_name());
            model.displaySingle(currentSong.getInfo().getAlbum_1000_1000(),civPlayPhoto);
            if(rotateAnimation==null) {
                Log.e("宽高错误","kuang="+civPlayPhoto.getWidth() / 2+"gao="+civPlayPhoto.getHeight()/2);

                int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                civPlayPhoto.measure(w,h);
                rotateAnimation = new RotateAnimation(0, 360, civPlayPhoto.getMeasuredWidth()/ 2, civPlayPhoto.getMeasuredHeight()/2);
                rotateAnimation.setDuration(2000);
                rotateAnimation.setRepeatCount(Animation.INFINITE);
                rotateAnimation.setInterpolator(new LinearInterpolator());
                civPlayPhoto.setAnimation(rotateAnimation);
            }
            model.displaySingle(currentSong.getInfo().getAlbum_1000_1000(),ivPlayBack);

        }
    }

    private void setListener() {
        ivPlayPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentSong!=null) {
                    broadIntent.setAction(ACTION_PLAY_OR_PAUSE);
                    sendBroadcast(broadIntent);
                }
            }
        });
        ivPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentSong!=null){
                    currentSong=model.getSongInfo(app.getNetSongs().get(index));
                    app.setCurrentSong(currentSong);

                    broadIntent.setAction(ACTION_NEXT);
                    sendBroadcast(broadIntent);

                }
            }
        });
        ivPlayPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentSong!=null){
                    broadIntent.setAction(ACTION_PREVIOUS);
                    sendBroadcast(broadIntent);
                }
            }
        });

    }

    private void init() {

        civPlayPhoto = (CircleImageView) findViewById(R.id.civ_play_photo);
        ivPlayBack = (ImageView) findViewById(R.id.iv_play_back);
        tvPlaySong= (TextView) findViewById(R.id.tv_play_song);
        tvPlaySinger= (TextView) findViewById(R.id.tv_play_singer);
        tvPlayLrc= (TextView) findViewById(R.id.tv_play_lrc);
        tvPlayProgress= (TextView) findViewById(R.id.tv_play_progress);
        tvPlayTotal= (TextView) findViewById(R.id.tv_play_total);
        sbPlayProgress= (SeekBar) findViewById(R.id.sb_play_progress);
        ivPlayPre= (ImageView) findViewById(R.id.iv_play_pre);
        ivPlayPlay= (ImageView) findViewById(R.id.iv_play_play);
        ivPlayNext= (ImageView) findViewById(R.id.iv_play_next);

    }
}
