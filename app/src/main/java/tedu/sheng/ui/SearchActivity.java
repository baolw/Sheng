package tedu.sheng.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tedu.sheng.R;
import tedu.sheng.adapter.SearchAdapter;
import tedu.sheng.entity.SongSearch;
import tedu.sheng.model.MusicModel;
import tedu.sheng.util.Consts;

public class SearchActivity extends Activity implements Consts{


    private EditText etSearch;
    private Button btSearch;
    private ListView lvSearch;


    List<SongSearch> data=new ArrayList<SongSearch>();

    MusicModel model;

    private SearchAdapter adapter;

   private Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    List<SongSearch> searchData=(List<SongSearch>) msg.obj;
                    data.clear();
                    data.addAll(searchData);
                    adapter.notifyDataSetChanged();
                    break;

            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        model=new MusicModel(this);
        init();
        setadapter();
        setListener();
    }

    private void setListener() {

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=etSearch.getText().toString();
                if(name.length()>0){
                    new Thread(){
                        @Override
                        public void run() {
                            List<SongSearch> searchs=model.search(name);

                            Message msg=handler.obtainMessage(0,searchs);
                           handler.sendMessage(msg);
                        }
                    }.start();
                }
            }
        });

    }

    private void setadapter() {
        adapter=new SearchAdapter(data,this);
        lvSearch.setAdapter(adapter);
    }

    private void init() {
        etSearch= (EditText) findViewById(R.id.et_search);
        btSearch= (Button) findViewById(R.id.bt_search);
        lvSearch= (ListView) findViewById(R.id.lv_search);
    }
}
