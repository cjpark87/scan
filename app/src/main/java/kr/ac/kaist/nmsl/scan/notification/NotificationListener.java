package kr.ac.kaist.nmsl.scan.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Set;

import kr.ac.kaist.nmsl.scan.Constants;
import kr.ac.kaist.nmsl.scan.R;
import kr.ac.kaist.nmsl.scan.util.SharedPreferenceUtil;

public class NotificationListener extends NotificationListenerService {
    Context context;

    // Sets an ID for the notification
    private int mNotificationId = 001;
    private NotificationManager mNotifyMgr;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Log.d(Constants.DEBUG_TAG, "Creating NotificationListener");

        NotificationCompat.Builder notiBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_menu_view)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.running))
                        .setOngoing(true);

        // Gets an instance of the NotificationManager service
        mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, notiBuilder.build());
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

    @Override
    public void onDestroy() {
        mNotifyMgr.cancel(mNotificationId);
        super.onDestroy();
    }
}
