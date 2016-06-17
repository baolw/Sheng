package tedu.sheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tedu.sheng.R;
import tedu.sheng.entity.MineSong;

public class LocalAdapter extends BaseAdapter {
    private List<MineSong> songs = new ArrayList<MineSong>();
    private Context context;

    public LocalAdapter(List<MineSong> songs, Context context) {
        super();
        this.songs = songs;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_local, null);
            holder = new Holder();
            holder.tvSong = (TextView) convertView.findViewById(R.id.tv_item_song);
            holder.tvSinger = (TextView) convertView.findViewById(R.id.tv_item_singer);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        MineSong song = songs.get(position);

        holder.tvSong.setText(song.getName());
        holder.tvSinger.setText(song.getArtist());
        return convertView;
    }

    class Holder {
        TextView tvSong;
        TextView tvSinger;
    }

    


}
