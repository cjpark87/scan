package kr.ac.kaist.nmsl.scan.sensor;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.IBinder;

public class OrientationService extends SensorService {
    public OrientationService() {
        mDataCount = 3; //x,y,z
        mSensorType = Sensor.TYPE_ORIENTATION;
        mSensorTypeName = "ORIENTATION";
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
