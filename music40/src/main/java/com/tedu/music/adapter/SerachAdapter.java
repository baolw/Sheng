package com.tedu.music.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tedu.music.entity.SongSerch;
import com.tedu.music.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author pjy
 *  װ��������Ϣ
 */
public class SerachAdapter extends BaseAdapter{
	//׼������Դ
	private List<SongSerch> data=new ArrayList<SongSerch>();
	//�����Ķ���
	private Context context;
	
	
	
	/**
	 * @param data
	 * @param context
	 * ������
	 */
	public SerachAdapter(List<SongSerch> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder=null;
		if(convertView==null){
			//��convertView��ֵ
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_serach_item, null);
			//ʵ����holder����
			holder=new Holder();
			holder.title=(TextView) convertView.findViewById(R.id.serach_item_title_txt);
			holder.author=(TextView) convertView.findViewById(R.id.serach_item_author_txt);
			convertView.setTag(holder);
		}else{
			//convertView�����Ѿ�����
			holder=(Holder) convertView.getTag();		
		}
		//�������
		SongSerch serach=data.get(position);
		holder.title.setText("����:"+serach.getTitle());
		holder.author.setText("����:"+serach.getAuthor());
		return convertView;
	}
	/**
	 * @author pjy
	 * ����ʹ��
	 */
	
	class Holder{
		TextView title,author;
	}

}
