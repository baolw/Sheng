package tedu.sheng.util;

public interface Consts {


	String ACTION_PLAY_OR_PAUSE = "tedu.intent.action.PLAY_OR_PAUSE";
	String ACTION_PREVIOUS = "tedu.intent.action.PREVIOUS";
	String ACTION_NEXT = "tedu.intent.action.NEXT";
	String ACTION_PLAY_POSITION = "tedu.intent.action.PLAY_POSITION";
	String ACTION_PLAY_FROM_PROGRESS = "tedu.intent.action.PLAY_FROM_PROGRESS";
	String ACTION_SET_AS_PLAY_STATE = "tedu.intent.action.SET_AS_PLAY_STATE";
	String ACTION_SET_AS_PLAY_STATE2 = "tedu.intent.action.SET_AS_PLAY_STATE2";
	String ACTION_SET_AS_PAUSE_STATE = "tedu.intent.action.SET_AS_PAUSE_STATE";
	String ACTION_SET_AS_PAUSE_STATE2 = "tedu.intent.action.SET_AS_PAUSE_STATE2";

	String ACTION_UPDATE_PROGRESS = "tedu.intent.action.UPDATE_PROGRESS";


	String ACTION_PLAY_POSITION_NET="tedu.intent.action.PLAY_POSITION_NET";

	//开启音乐之旅
	String ACTION_START_MUSIC_TRAVERL="tedu.intent.action.START_MUSIC_TRAVERL";

	//需要播放的歌曲信息
	String EXTRA_CURRENT_MUSIC="tedu.intent.action.CURRENT_MUSIC";

	//歌曲的索引
	String EXTRA_MUSIC_INDEX = "position";
	//歌曲播放到的当前位置时间
	String EXTRA_CURRENT_POSITION = "current_position";
	//歌曲总事件
	String EXTRA_DURATION="duration";
	//多少进度开始播放
	String EXTRA_PROGRESS = "progress";
}
