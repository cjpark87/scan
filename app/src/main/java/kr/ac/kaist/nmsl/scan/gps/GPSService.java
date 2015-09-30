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
import android.widget.Toast;

import kr.ac.kaist.nmsl.scan.Constants;

public class GPSService extends Service {
    PowerManager.WakeLock wakeLock;

    private LocationManager locationManager;

    public GPSService() {
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

        Toast.makeText(getApplicationContext(), "SCAN GPS: Service Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    @Deprecated
    public int onStartCommand(Intent intent, int flags, int id) {
        super.onStartCommand(intent, flags, id);

        Toast.makeText(getApplicationContext(), "SCAN GPS: Service Started", Toast.LENGTH_SHORT).show();

        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.GPS_SERVICE_INTERVAL, Constants.GPS_SERVICE_DISTANCE, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.GPS_SERVICE_INTERVAL, Constants.GPS_SERVICE_DISTANCE, listener);

        return START_STICKY;
    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
           if (location == null)
                return;

            Toast.makeText(getApplicationContext(), "SCAN GPS: " + location.getLatitude() + ", " + location.getLongitude() + " (" + location.getProvider() + ", " + location.getAccuracy() + ")", Toast.LENGTH_SHORT).show();
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
