package tedu.sheng.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import tedu.sheng.R;
import tedu.sheng.entity.Song;
import tedu.sheng.model.MusicModel;
import tedu.sheng.service.MusicDownLoadService;

public class MusicAdapter extends BaseAdapter {
    private List<Song> songs = new ArrayList<Song>();
    private Context context;
    //private MusicModel model;

    private MusicModel model;
    Song currentSong;
    public MusicAdapter(List<Song> songs, Context context) {
        super();
        this.songs = songs;
        this.context = context;
       // this.model = model;
        model=new MusicModel();
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


        holder.ivDown.setOnClickListener(new View.OnClickListener() {








            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        currentSong=model.getSongInfo(songs.get(position));
                    }
                }.start();
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                String items[]=new String[]{"极速版","普通版","超清版"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String msg="";
                        switch (which){
                            case 0:
                                msg="你选择了极速版";
                                break;
                            case 1:
                                msg="你选择了普通版";
                                break;
                            case 2:
                                msg="你选择了高清版";
                                break;
                        }
                        if(model.isHave(currentSong,which)){
                            Toast.makeText(context, "文件已经存在", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            Intent down_intent=new Intent(context, MusicDownLoadService.class);
                            down_intent.putExtra("song", currentSong);
                            down_intent.putExtra("version", which);
                            context.startService(down_intent);
                        }
                    }
                });
                builder.create().show();
            }
        });
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
