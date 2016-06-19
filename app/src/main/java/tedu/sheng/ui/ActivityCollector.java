package tedu.sheng.ui;

import android.app.Activity;
import android.os.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qll on 2016/5/31.
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        // 结束掉所有的Activity
        for (Activity activity : activities) {
            activity.finish();
        }

        // 结束进程
        Process.killProcess(Process.myPid());

        //长连接关了
        //线程while(isRunning) isRunning=false
    }
}
