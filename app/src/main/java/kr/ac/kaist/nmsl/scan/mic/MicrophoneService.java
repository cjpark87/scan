package kr.ac.kaist.nmsl.scan.mic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.Date;

import kr.ac.kaist.nmsl.scan.Constants;
import kr.ac.kaist.nmsl.scan.util.FileUtil;

public class MicrophoneService extends Service {
    private MediaRecorder mRecorder;
    private String mSensorTypeName = "MIC";

    public MicrophoneService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        File recordFile = new File(FileUtil.getDataFilename(this, new Date(), mSensorTypeName, "3gp"));
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(recordFile.getAbsolutePath());
        mRecorder.setAudioSamplingRate(44100);
        MediaScannerConnection.scanFile(this, new String[]{recordFile.getAbsolutePath()}, null, null);
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
        super.onDestroy();
    }
}
