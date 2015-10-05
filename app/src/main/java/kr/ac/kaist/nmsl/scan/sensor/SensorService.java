package kr.ac.kaist.nmsl.scan.sensor;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.util.*;

import kr.ac.kaist.nmsl.scan.Constants;
import kr.ac.kaist.nmsl.scan.util.FileUtil;

public class SensorService extends Service implements SensorEventListener {
    protected String mSensorTypeName;
    protected int mSensorType;
    protected int mDataCount;
    protected int mInterval;

    protected SensorManager mSensorManager;
    protected Sensor mSensor;

    public SensorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void writeData(SensorEvent event) {
        JSONObject value = new JSONObject();

        try {
            if (mDataCount == 0) {
                value.put(mSensorTypeName, true);
            } else {
                for (int i = 0; i < mDataCount; i++) {

                        switch (i) {
                            case 0:
                                value.put("x" + i, event.values[i]);
                                break;
                            case 1:
                                value.put("y" + i, event.values[i]);
                                break;
                            case 2:
                                value.put("z" + i, event.values[i]);
                                break;
                            default:
                                value.put("event" + i, event.values[i]);
                                break;
                        }

                }
            }
        } catch (Exception e) {
            Log.e(Constants.DEBUG_TAG, e.getMessage());
        }

        FileUtil.writeData(this, new Date(), mSensorTypeName, value);
    }
}