package tedu.sheng.dal;

import android.content.Context;
import tedu.sheng.entity.MineSong;

public class MusicDaoFactory {
	private MusicDaoFactory() {
		super();
	}

	public static IDao<MineSong> newInstance(Context context) {
		return new MediaStoreMusicDao(context);
	}
}
