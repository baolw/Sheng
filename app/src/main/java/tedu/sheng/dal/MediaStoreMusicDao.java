package tedu.sheng.dal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tedu.sheng.entity.MineSong;


class MediaStoreMusicDao implements IDao<MineSong> {

    private Context context;
    public MediaStoreMusicDao(Context context) {
        super();
        this.context = context;
    }

    @Override
    public List<MineSong> getData() {
        List<MineSong> musics = new ArrayList<MineSong>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                "_id",
                "title",
                "_data",
                "_size",
                "duration",
                "album",
                "artist",
                "album_artist",
                "album_key"
        };
        Cursor c = cr.query(uri, projection, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                Log.i("tedu", "读取歌曲信息");
                for (; !c.isAfterLast(); c.moveToNext()) {
                    MineSong music = new MineSong();
                    music.setId(c.getLong(0));
                    music.setName(c.getString(1));
                    music.setPath(c.getString(2));
                    music.setSize(c.getInt(3));
                    music.setDuration(c.getInt(4));
                    music.setAlbum(c.getString(5));
                    music.setArtist(c.getString(6));
                    music.setAlbumArtist(c.getString(7));
                    music.setAlbumKey(c.getString(8));
                    music.setAlbumArt(getAlbumArtByKey(music.getAlbumKey()));
                    Log.d("tedu", "" + music);
                    musics.add(music);
                }
                Log.i("tedu", "读取歌曲信息完成");
            }
            c.close();
            c = null;
        }
        return musics;
    }


    private String getAlbumArtByKey(String albumKey) {
        if (albumKey == null) {
            return null;
        }

        String albumArt = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        String[] projection = {"album_art"};
        String selection = "album_key=?";
        String[] selectionArgs = {albumKey};
        Cursor c = cr.query(uri, projection, selection, selectionArgs, null);

        if (c != null) {
            if (c.moveToFirst()) {
                albumArt = c.getString(0);
            }
            c.close();
            c = null;
        }

        return albumArt;
    }

}
