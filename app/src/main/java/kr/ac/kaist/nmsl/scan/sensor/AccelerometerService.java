package kr.ac.kaist.nmsl.scan.sensor;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.IBinder;

public class AccelerometerService extends SensorService {


    public AccelerometerService() {
        mDataCount = 3; //x,y,z
        mSensorType = Sensor.TYPE_ACCELEROMETER;
        mSensorTypeName = "ACCELEROMETER";
        mInterval = 100000000; //10 seconds in us
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, mInterval);
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       writeData(event);
    }
}
