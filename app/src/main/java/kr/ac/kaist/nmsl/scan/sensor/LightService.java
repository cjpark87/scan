package kr.ac.kaist.nmsl.scan.sensor;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.IBinder;

public class LightService extends SensorService {
    public LightService() {
        mDataCount = 1; //x,y,z
        mSensorType = Sensor.TYPE_LIGHT;
        mSensorTypeName = "LIGHT";
        mInterval = 10000000; //10 seconds in us
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
