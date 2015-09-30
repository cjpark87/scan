package kr.ac.kaist.nmsl.scan.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.*;

import kr.ac.kaist.nmsl.scan.Constants;
import kr.ac.kaist.nmsl.scan.util.FileUtil;



public class GPSService extends Service {
    private static final int GPS_SERVICE_INTERVAL = 15000;
    private static final float GPS_SERVICE_DISTANCE = 1.0f;
    private static final String GPS_SERVICE_NAME = "GPS";

    PowerManager.WakeLock wakeLock;
    Context mContext;

    private LocationManager locationManager;

    public GPSService() {
        mContext = this;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);

        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        super.onStartCommand(intent, flags, id);

        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_SERVICE_INTERVAL, GPS_SERVICE_DISTANCE, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, GPS_SERVICE_INTERVAL, GPS_SERVICE_DISTANCE, listener);

        return START_STICKY;
    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
           if (location == null)
                return;

            JSONObject value = new JSONObject();
            try {
                value.put("provider", location.getProvider());
                value.put("accuracy", location.getAccuracy());
                value.put("latitude", location.getLatitude());
                value.put("longitude", location.getLongitude());
                value.put("altitude", location.getAltitude());
                value.put("bearing", location.getBearing());
                value.put("speed", location.getSpeed());
            } catch (Exception e) {
                Log.e(Constants.DEBUG_TAG, e.getMessage());
            }

            FileUtil.writeData(mContext, new Date(), GPS_SERVICE_NAME, value);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onDestroy() {
        if(wakeLock.isHeld()) {
            wakeLock.release();
        }

        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
