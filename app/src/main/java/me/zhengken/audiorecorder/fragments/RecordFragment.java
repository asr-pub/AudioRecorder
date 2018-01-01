package me.zhengken.audiorecorder.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.objectbox.Box;
import me.zhengken.audiorecorder.App;
import me.zhengken.audiorecorder.R;
import me.zhengken.audiorecorder.RecordService;
import me.zhengken.audiorecorder.events.UpdateRecordsEvent;
import me.zhengken.audiorecorder.model.Record;

/**
 * Copyright  : zhengken@live.com
 * Created by zhengken on 2017/9/3.
 * ClassName    : RecordFragment
 * Description  :
 */

public class RecordFragment extends Fragment implements View.OnClickListener, Chronometer.OnChronometerTickListener {

    private static final String TAG = RecordFragment.class.getSimpleName();

    private static final String ARG_POSITION = "position";

    private Box<Record> mRecordsBox;

    private ImageView mStartButton;
    private ImageView mStopButton;

    private Chronometer mChronometer;

    private long mStartTime;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);

    public static RecordFragment newInstance(int position) {
        RecordFragment fragment = new RecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecordsBox = ((App) getActivity().getApplication()).getBoxStore().boxFor(Record.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record, container, false);

        mStartButton = (ImageView) view.findViewById(R.id.btn_start_record);
        mStopButton = (ImageView) view.findViewById(R.id.btn_stop_record);
        mChronometer = (Chronometer) view.findViewById(R.id.chronometer);
        mChronometer.setOnChronometerTickListener(this);

        mStopButton.setEnabled(false);
        mStartButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_record:
                onStartRecord();
                break;
            case R.id.btn_stop_record:
                onStopRecord();
                break;
            default:
                break;
        }
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {

        int time = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);

        int h = time / 3600;
        int m = (time % 3600) / 60;
        int s = (time % 3600) % 60;

        String formatTime = String.format(Locale.CHINA, "%02d:%02d:%02d", h, m, s);

        chronometer.setText(formatTime);
    }

    private void onStartRecord() {

        Intent intent = new Intent(getActivity(), RecordService.class);

        mStartButton.setEnabled(false);
        mStartButton.setImageResource(R.mipmap.ic_start_disable);
        mStopButton.setEnabled(true);
        mStopButton.setImageResource(R.drawable.selector_btn_stop_active);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();

        mStartTime = System.currentTimeMillis();

        getActivity().startService(intent);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void onPauseRecord() {
    }

    private void onResumeRecord() {
    }

    private void onStopRecord() {

        Intent intent = new Intent(getActivity(), RecordService.class);

        mChronometer.stop();
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mStartButton.setEnabled(true);
        mStartButton.setImageResource(R.drawable.selector_btn_start);
        mStopButton.setEnabled(false);
        mStopButton.setImageResource(R.mipmap.ic_stop_disable);

        long duration = System.currentTimeMillis() - mStartTime;
        Date date = new Date(mStartTime);
        String name = getString(R.string.file_name_prefix) + " " + sdf.format(date);
        mRecordsBox.put(new Record(name, duration, date));

        EventBus.getDefault().post(new UpdateRecordsEvent());

        getActivity().stopService(intent);
    }


}
