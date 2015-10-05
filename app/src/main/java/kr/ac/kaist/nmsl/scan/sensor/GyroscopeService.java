package kr.ac.kaist.nmsl.scan.sensor;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.IBinder;

public class GyroscopeService extends SensorService {
    public GyroscopeService() {
        mDataCount = 3; //x,y,z
        mSensorType = Sensor.TYPE_GYROSCOPE;
        mSensorTypeName = "GYROSCOPE";
        mInterval = 100000000; //10 seconds in us
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        writeData(event);
    }
}
