package tedu.sheng.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import tedu.sheng.R;
import tedu.sheng.adapter.MineAdapter;

/**
 * Created by Administrator on 2016/6/13.
 */
public class MineFragment extends Fragment {

    private ListView lvMine;
    private MineAdapter adapter;

    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_mine,null);
        lvMine= (ListView) v.findViewById(R.id.lv_mine);
        setAdapter();
        setListener();

        return v;
    }

    private void setAdapter() {
        adapter=new MineAdapter()
    }

    private void setListener() {
        lvMine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
