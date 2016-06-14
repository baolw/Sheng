package tedu.sheng.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import tedu.sheng.R;
import tedu.sheng.entity.Song;

public class MusicAdapter extends BaseAdapter {
    private List<Song> songs = new ArrayList<Song>();
    private Context context;
    //private MusicModel model;


    public MusicAdapter(List<Song> songs, Context context) {
        super();
        this.songs = songs;
        this.context = context;
       // this.model = model;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hot, null);
            holder = new Holder();
            holder.ivDown= (ImageView) convertView.findViewById(R.id.iv_item_down);
            holder.tvNum= (TextView) convertView.findViewById(R.id.tv_item_num);
            holder.tvSong = (TextView) convertView.findViewById(R.id.tv_item_song);
            holder.tvSinger = (TextView) convertView.findViewById(R.id.tv_item_singer);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Song song = songs.get(position);

        //model.displayImg(holder.img, song.getPic_small());

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
        holder.tvSong.setText(song.getTitle());
        holder.tvSinger.setText(song.getArtist_name());
        holder.ivDown.setTag("ivDown"+position);
        return convertView;
    }

    class Holder {
        ImageView ivDown;
        TextView tvSong;
        TextView tvSinger;
        TextView tvNum;
    }

}
