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
 *��ʾ���������Ϣ
 *
 */
public class SerachActivity extends Activity {
	//�������
	private  ListView serachLv;
	private  EditText serachEdit;
	private  Button  serachBtn;
	
	//��������Դ����
	List<SongSerch> data=new ArrayList<SongSerch>();
	
	//����ģ�Ͷ���
	MusicModel model;
	//׼��������
	private SerachAdapter adapter;
	//׼��handler
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			//������Ϣ��������UI����
			switch (msg.what) {
			case 0:
				//�������Դ�Ƿ�������ȡ
				//��ȡ����Դ
				List<SongSerch> serachs=(List<SongSerch>) msg.obj;
				//��������Դ�ظ����ԭ������
				data.clear();
				//������Դ���������
				data.addAll(serachs);
				//֪ͨ����������Դ�Ѿ�����
				// ����listview
				adapter.notifyDataSetChanged();
				break;

			
			}
			
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//�󶨲���
		setContentView(R.layout.activity_serach);
		
		//�����ʼ��
		serachLv=(ListView) findViewById(R.id.serach_lv);
		serachEdit=(EditText) findViewById(R.id.serach_edit);
		serachBtn= (Button) findViewById(R.id.serach_btn);
		
		//��model ��ʼ��
		model=new MusicModel(this);
		
		
		//����������ʼ��
		adapter=new SerachAdapter(data, this);
		//��������listview��
		serachLv.setAdapter(adapter);
		
		//�����Ӽ����¼�
		//��ť����
		serachBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//��ȡ����������
				final String  name=serachEdit.getText().toString().trim();
						
				if(name.length()>0){
					
				
				//�����̻߳�ȡ����
				new Thread(new Runnable() {
					public void run() {
						//�������Դ
						
						List<SongSerch> getDate=model.Sreach(name);
						//��listvie ������
						//��handler���Ϳ���Ϣ-->֪ͨhandler���и��²���
						Message msg=handler.obtainMessage(0,getDate);
						
						handler.sendMessage(msg);
					}
				}).start();
				}else{
					Toast.makeText(SerachActivity.this, "��������������", Toast.LENGTH_SHORT).show();
				}
				
				
				
			}
		});
		//listview ����
	}
	

}
