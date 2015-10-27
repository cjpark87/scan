package kr.ac.kaist.nmsl.scan.mic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.*;

import kr.ac.kaist.nmsl.scan.Constants;
import kr.ac.kaist.nmsl.scan.util.FileUtil;

public class MicrophoneService extends Service {
    private static final int NEW_FILE_INTERVAL = 10 * 1000;
    private static final int INITIAL_DELAY = 100;

    private MediaRecorder mRecorder;
    private String mSensorTypeName = "MIC";
    private File mRecordFile;
    //private Timer mTimer;
    //private TimerTask mTimerTask;

    public MicrophoneService() {
    }

    @Override
    public void onCreate() {
//        mTimer = new Timer();
//        mTimerTask = new TimerTask() {
//            @Override
//            public void run() {
//                if (mRecordFile != null && mRecorder != null) {
//                    endRecording();
//                }
//                 initializeRecorder();
                // startRecording();
//            }
//        };
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //mTimer.scheduleAtFixedRate(mTimerTask, INITIAL_DELAY, NEW_FILE_INTERVAL);
        initializeRecorder();
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        endRecording();

        //mTimer.cancel();

        super.onDestroy();
    }

    private void initializeRecorder() {
        mRecordFile = new File(FileUtil.getDataFilename(getApplicationContext(), new Date(), mSensorTypeName, "mp3"));
        mRecorder = new MediaRecorder();

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mRecordFile.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    }

    private void startRecording() {
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            Log.e(Constants.DEBUG_TAG, e.getMessage());
        }

        mRecorder.start();
    }

    private void endRecording() {
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;

        FileUtil.refreshDataFile(this, mRecordFile);

        mRecordFile = null;
    }
}
