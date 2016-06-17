package tedu.sheng.adapter;

import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import tedu.sheng.R;
import tedu.sheng.entity.SongSearch;


public class SearchAdapter extends BaseAdapter{
	private List<SongSearch> data=new ArrayList<SongSearch>();
	private Context context;

	public SearchAdapter(List<SongSearch> data, Context context) {
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
		ViewHolder holder;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_search, null);
			holder=new ViewHolder();
			holder.title=(TextView) convertView.findViewById(R.id.tv_item_search_song);
			holder.author=(TextView) convertView.findViewById(R.id.tv_item_search_singer);
			holder.tvNum= (TextView) convertView.findViewById(R.id.tv_item_search_num);
			holder.ivSearchDown= (ImageView) convertView.findViewById(R.id.iv_item_search_down);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		SongSearch search=data.get(position);
		if (position<3){
			holder.tvNum.setTextColor(context.getResources().getColor(R.color.red));
		}else{
			holder.tvNum.setTextColor(context.getResources().getColor(R.color.gray_light));

		}
		if(position<9){
			holder.tvNum.setText("0"+(position+1));

		}else{
			holder.tvNum.setText((position+1)+"");
		}
		holder.title.setText(search.getTitle());
		holder.author.setText(search.getAuthor());

		return convertView;
	}
	
	
	class ViewHolder{
		TextView title,author;
		TextView tvNum;
		ImageView ivSearchDown;
	}

}
