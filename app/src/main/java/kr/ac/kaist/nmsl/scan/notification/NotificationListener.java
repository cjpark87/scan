package kr.ac.kaist.nmsl.scan.notification;

import android.content.Context;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Set;

import kr.ac.kaist.nmsl.scan.Constants;
import kr.ac.kaist.nmsl.scan.util.SharedPreferenceUtil;

public class NotificationListener extends NotificationListenerService {
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Log.d(Constants.DEBUG_TAG, "Creating NotificationListener");
    }

    private boolean isWhiteListedPackage(String packageName) {
        // Possible bottleneck?
        Set<String> whiteListedPackages = SharedPreferenceUtil.getWhiteListedPackages(context);
        return whiteListedPackages.contains(packageName);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        if (!isWhiteListedPackage(pack)) {
            Log.d(Constants.DEBUG_TAG, "Not white listed package: " + pack);
            return;
        }

        Log.i(Constants.DEBUG_TAG, pack);
        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(Constants.DEBUG_TAG, "Notification Removed");
    }
}
