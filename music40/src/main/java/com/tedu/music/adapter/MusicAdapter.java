package com.tedu.music.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tedu.music.entity.Song;
import com.tedu.music.model.MusicModel;
import com.tedu.music.ui.R;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter{
	private List<Song> data=new ArrayList<Song>();
	private Context context;
	private MusicModel model;
	
	/**
	 * @param data
	 * @param context
	 * 构造器
	 */
	public MusicAdapter(List<Song> data, Context context,MusicModel model) {
		super();
		this.data = data;
		this.context = context;
		this.model=model;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.list_item, null);
			holder=new Holder();
			holder.img=(ImageView) convertView.findViewById(R.id.artist_pic);
			holder.s_Name=(TextView) convertView.findViewById(R.id.song_name);
			holder.s_artist=(TextView) convertView.findViewById(R.id.artist_name);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		Song song=data.get(position);
		//数据填充
		//holder.img model.displayimg();
		model.displayImg(holder.img, song.getPic_small());;
		holder.s_Name.setText(song.getTitle());
		holder.s_artist.setText(song.getArtist_name());
		return convertView;
	}
	
	class Holder {
		ImageView img;
		TextView s_Name,s_artist;
	}

}
