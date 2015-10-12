package kr.ac.kaist.nmsl.scan.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by cjpark on 2015-10-07.
 */
public class ServiceUtil {

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
