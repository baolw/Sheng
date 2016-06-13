package com.tedu.music.ui;

import java.util.ArrayList;
import java.util.List;

import com.tedu.music.adapter.SerachAdapter;
import com.tedu.music.entity.SongSerch;
import com.tedu.music.model.MusicModel;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author pjy
 *显示搜索相关信息
 *
 */
public class SerachActivity extends Activity {
	//组件声明
	private  ListView serachLv;
	private  EditText serachEdit;
	private  Button  serachBtn;
	
	//声明数据源集合
	List<SongSerch> data=new ArrayList<SongSerch>();
	
	//声明模型对象
	MusicModel model;
	//准备适配器
	private SerachAdapter adapter;
	//准备handler
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			//根据消息参数处理UI更新
			switch (msg.what) {
			case 0:
				//检查数据源是否正常获取
				//获取数据源
				List<SongSerch> serachs=(List<SongSerch>) msg.obj;
				//避免数据源重复清空原有数据
				data.clear();
				//给数据源添加新数据
				data.addAll(serachs);
				//通知适配器数据源已经更新
				// 更新listview
				adapter.notifyDataSetChanged();
				break;

			
			}
			
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//绑定布局
		setContentView(R.layout.activity_serach);
		
		//组件初始化
		serachLv=(ListView) findViewById(R.id.serach_lv);
		serachEdit=(EditText) findViewById(R.id.serach_edit);
		serachBtn= (Button) findViewById(R.id.serach_btn);
		
		//给model 初始化
		model=new MusicModel(this);
		
		
		//先适配器初始化
		adapter=new SerachAdapter(data, this);
		//适配器和listview绑定
		serachLv.setAdapter(adapter);
		
		//组件添加监听事件
		//按钮监听
		serachBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取搜索的内容
				final String  name=serachEdit.getText().toString().trim();
						
				if(name.length()>0){
					
				
				//启动线程获取数据
				new Thread(new Runnable() {
					public void run() {
						//获得数据源
						
						List<SongSerch> getDate=model.Sreach(name);
						//给listvie 绑数据
						//给handler发送空消息-->通知handler进行更新操作
						Message msg=handler.obtainMessage(0,getDate);
						
						handler.sendMessage(msg);
					}
				}).start();
				}else{
					Toast.makeText(SerachActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
				}
				
				
				
			}
		});
		//listview 监听
	}
	

}
