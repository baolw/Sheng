package tedu.sheng.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tedu.sheng.R;
import tedu.sheng.adapter.MusicAdapter;
import tedu.sheng.app.MyApplication;
import tedu.sheng.entity.Song;
import tedu.sheng.model.MusicModel;
import tedu.sheng.util.Consts;

/**
 * Created by Administrator on 2016/6/13.
 */
public class BankHotFragment extends Fragment implements Consts{
    View v;
    private ListView lvHot;
    private MusicModel model;
    private int index;
    private List<Song> songs=new ArrayList<>();
    private List<Song> newSongs=new ArrayList<>();
    private MusicAdapter adapter;

    private boolean atBottom=false;

    private MyApplication app;

    //发送广播播放
    private Intent intent=new Intent();
    //处理ui更新，及播放歌曲
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    adapter=new MusicAdapter(songs,getActivity());
                    lvHot.setAdapter(adapter);
                    break;
                case 1:
                    songs.addAll(newSongs);
                    adapter.notifyDataSetChanged();
                    app.setNetSongs(songs);
                    break;
                case 2:
                    Song s= (Song) msg.obj;
                    app.setCurrentSong(s);
                    if(s!=null){
                        intent.setAction(ACTION_PLAY_POSITION_NET);
                        intent.putExtra(EXTRA_MUSIC_INDEX,index);
                        intent.putExtra(EXTRA_CURRENT_MUSIC,s);
                        getActivity().sendBroadcast(intent);
                    }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_bank_hot,null);
        app= (MyApplication) getActivity().getApplication();
        lvHot= (ListView) v.findViewById(R.id.lv_hot);

        model=new MusicModel();
        getSongs();
        setListener();
        return v;
    }

    //列表监听，滑动监听
    private void setListener() {
        lvHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(app.getCurrentSong()==null) {
                    intent.setAction(ACTION_START_MUSIC_TRAVERL);
                    getActivity().sendBroadcast(intent);
                }
                index=position;
                app.setPosition(index);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Song s=model.getSongInfo(songs.get(position));
                        Message msg=handler.obtainMessage(2,s);
                        handler.sendMessage(msg);

                    }
                }).start();
            }
        });

        lvHot.setOnScrollListener(new AbsListView.OnScrollListener() {
           private boolean haveDate=true;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState){
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        if(atBottom){

                            new Thread(){
                                @Override
                                public void run() {
                                    newSongs=model.getSongs(songs.size(),20);
                                    if(newSongs.size()<1){
                                        haveDate=false;
                                    }
                                    Message msg=handler.obtainMessage(1,newSongs);
                                    handler.sendMessage(msg);
                                }
                            }.start();
                            Log.e("歌曲数量",newSongs.size()+"");
                            if (!haveDate) {
                                Toast.makeText(getActivity(), "已经到底啦！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if((firstVisibleItem+visibleItemCount)==totalItemCount){
                    atBottom=true;
                }else{
                    atBottom=false;
                }
            }
        });
    }

    //网络访问获取播放列表
    private void getSongs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                songs=model.getSongs(0,20);
                handler.sendEmptyMessage(0);
                app.setNetSongs(songs);
            }
        }).start();
    }
}
