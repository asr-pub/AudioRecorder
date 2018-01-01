package me.zhengken.audiorecorder;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Copyright  : zhengken@live.com
 * Created by zhengken on 2017/9/22.
 * ClassName    : RecordService
 * Description  :
 */

public class RecordService extends Service {

    private static final String TAG = RecordService.class.getSimpleName();

    private static final int CHANNEL_IN_MONO = 1;

    private MediaRecorder mRecorder = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        if (mRecorder != null) {
            stopRecording();
        }

        super.onDestroy();
    }

    private void startRecording() {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(CHANNEL_IN_MONO);
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioEncodingBitRate(192000);
        mRecorder.setOutputFile(getFileName());

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private String getFileName() {

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);

        return Environment.getExternalStorageDirectory().getPath()
                + "/AudioRecorder/"
                + getString(R.string.file_name_prefix)
                + " "
                + sdf.format(date)
                + ".mp4";

    }

}
