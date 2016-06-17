package tedu.sheng.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import tedu.sheng.R;
import tedu.sheng.adapter.LocalAdapter;
import tedu.sheng.app.MyApplication;
import tedu.sheng.util.Consts;


public class MineFragment extends Fragment implements Consts{

    private ListView lvMine;
    private LocalAdapter adapter;

    private MyApplication app;

    private Intent intent=new Intent();

    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_mine,null);
        lvMine= (ListView) v.findViewById(R.id.lv_mine);
        app= (MyApplication) getActivity().getApplication();

        setAdapter();
        setListener();
        return v;
    }



    private void setAdapter() {
        adapter=new LocalAdapter(app.getLocalSongs(),getActivity());
        lvMine.setAdapter(adapter);

    }

    private void setListener() {
        lvMine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                app.setCurrentIndex(position);
                if(app.getCurrentIndex()!=-1){
                    intent.setAction(ACTION_START_MUSIC_TRAVERL);
                    getActivity().sendBroadcast(intent);}
                app.setIsNetWork(false);
                intent.setAction(ACTION_PLAY_POSITION);
                intent.putExtra(EXTRA_MUSIC_INDEX, position);
                getActivity().sendBroadcast(intent);
            }
        });
    }
}
