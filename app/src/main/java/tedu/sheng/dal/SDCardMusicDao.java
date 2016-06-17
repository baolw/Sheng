package tedu.sheng.dal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;
import tedu.sheng.entity.MineSong;


public class SDCardMusicDao implements IDao<MineSong> {

	@Override
	public List<MineSong> getData() {
		List<MineSong> musics = new ArrayList<MineSong>();

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			if (dir.exists()) {
				File[] files = dir.listFiles();
				if (files != null && files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						if (files[i].isFile()) {
							if (files[i].getName().toLowerCase(Locale.CHINA).endsWith(".mp3")) {
								MineSong music = new MineSong();
								music.setPath(files[i].getAbsolutePath());
								music.setName(files[i].getName().substring(0, files[i].getName().length() - 4));
								Log.v("tedu", "" + music);
								musics.add(music);
							}
						}
					}
				} else {
					Log.w("tedu", "Music文件夹下没有有效的文件");
				}
			} else {
				Log.w("tedu", "Music文件夹不存在");
			}
		} else {
			Log.w("tedu", "无法访问存储设备");
		}

		return musics;
	}

}
