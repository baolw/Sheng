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
 *  装配搜索信息
 */
public class SerachAdapter extends BaseAdapter{
	//准备数据源
	private List<SongSerch> data=new ArrayList<SongSerch>();
	//上下文对象
	private Context context;
	
	
	
	/**
	 * @param data
	 * @param context
	 * 构造器
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
			//给convertView赋值
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_serach_item, null);
			//实例化holder对象
			holder=new Holder();
			holder.title=(TextView) convertView.findViewById(R.id.serach_item_title_txt);
			holder.author=(TextView) convertView.findViewById(R.id.serach_item_author_txt);
			convertView.setTag(holder);
		}else{
			//convertView界面已经创建
			holder=(Holder) convertView.getTag();		
		}
		//数据填充
		SongSerch serach=data.get(position);
		holder.title.setText("歌名:"+serach.getTitle());
		holder.author.setText("作者:"+serach.getAuthor());
		return convertView;
	}
	/**
	 * @author pjy
	 * 复用使用
	 */
	
	class Holder{
		TextView title,author;
	}

}
