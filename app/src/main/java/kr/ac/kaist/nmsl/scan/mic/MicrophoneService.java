package kr.ac.kaist.nmsl.scan.mic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.Date;

import kr.ac.kaist.nmsl.scan.Constants;
import kr.ac.kaist.nmsl.scan.util.FileUtil;

public class MicrophoneService extends Service {
    private MediaRecorder mRecorder;
    private String mSensorTypeName = "MIC";
    private File mRecordFile;

    public MicrophoneService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRecordFile = new File(FileUtil.getDataFilename(this, new Date(), mSensorTypeName, "3gp"));
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mRecordFile.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            Log.e(Constants.DEBUG_TAG, e.getMessage());
        }

        mRecorder.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;

        FileUtil.refreshDataFile(this, mRecordFile);

        super.onDestroy();
    }
}
