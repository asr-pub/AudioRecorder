package me.zhengken.audiorecorder.popupwindows;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DimenRes;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import me.zhengken.audiorecorder.R;
import me.zhengken.audiorecorder.model.Record;

/**
 * Copyright  : zhengken@live.com
 * Created by zhengken on 2017/11/20.
 * ClassName    : PlaybackWindow
 * Description  :
 */

public class PlaybackWindow extends PopupWindow implements View.OnClickListener, BubbleSeekBar.OnProgressChangedListener, MediaPlayer.OnCompletionListener, PopupWindow.OnDismissListener {

    private static final String TAG = "PlaybackWindow";

    private static final int MSG_SEEK_BAR_REFRESH = 0;

    private Context mContext;

    private View mView;

    private Record mRecord;

    private TextView mTitle;
    private TextView mTimeInfo;
    private ImageView mClose;
    private BubbleSeekBar mSeekBar;
    private Button mPlay;
    private Button mPlayMode;

    private String TIME_FORMAT = "%s / %s";

    private MediaPlayer mMediaPlayer;

    private Handler handler;

    public PlaybackWindow(Context context, Record record) {

        mContext = context;
        mRecord = record;
        init();
    }

    public void showPopupWindow() {
        if (!isShowing()) {
            showAtLocation(mView, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_play:
                boolean isPlaying = mMediaPlayer.isPlaying();
                if (isPlaying) {
                    mPlay.setText("播放");
                    mMediaPlayer.pause();
                    handler.removeMessages(MSG_SEEK_BAR_REFRESH);
                } else {
                    mPlay.setText("暂停");
                    mMediaPlayer.start();
                    handler.sendEmptyMessageDelayed(MSG_SEEK_BAR_REFRESH, 1000);
                }
                break;
            case R.id.btn_play_mode:
                boolean isSingleLoop = mMediaPlayer.isLooping();
                if (isSingleLoop) {
                    mMediaPlayer.setLooping(false);
                    mPlayMode.setText("开启循环播放");
                } else {
                    mMediaPlayer.setLooping(true);
                    mPlayMode.setText("关闭循环播放");
                }
        }
    }

    @Override
    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i, float v) {

    }

    @Override
    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int i, float v) {

    }

    @Override
    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int i, float v) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        mPlay.setText("播放");
        mSeekBar.setProgress(0);
        mTimeInfo.setText(String.format(TIME_FORMAT, secondToTime(0), mRecord.getFormattedDuration()));
        handler.removeMessages(MSG_SEEK_BAR_REFRESH);
    }

    private void initHandler(Context context) {
        handler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SEEK_BAR_REFRESH:
                        if (mMediaPlayer != null) {
                            float position = mMediaPlayer.getCurrentPosition() / 1000;
                            mSeekBar.setProgress(position);
                            sendEmptyMessageDelayed(MSG_SEEK_BAR_REFRESH, 1000);
                            mTimeInfo.setText(String.format(TIME_FORMAT, secondToTime((int) position), mRecord.getFormattedDuration()));
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void init() {
        findView();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);

        mTitle.setText(mRecord.getName());
        mTimeInfo.setText(String.format(TIME_FORMAT, secondToTime(0), mRecord.getFormattedDuration()));
        mClose.setOnClickListener(this);
        mSeekBar.getConfigBuilder().min(0).max(mRecord.getDuration() / 1000).build();

        setContentView(mView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(false);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.color.white));
        setOnDismissListener(this);

        mPlay.setOnClickListener(this);
        mPlayMode.setOnClickListener(this);

        initHandler(mContext);

        try {
            mMediaPlayer.setDataSource("/sdcard/AudioRecorder/" + mRecord.getName() + ".mp4");
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void findView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.playback, null);

        mTitle = (TextView) mView.findViewById(R.id.tv_title);
        mTimeInfo = (TextView) mView.findViewById(R.id.txt_time_info);
        mClose = (ImageView) mView.findViewById(R.id.iv_close);
        mSeekBar = (BubbleSeekBar) mView.findViewById(R.id.bsb_seek_bar);
        mPlay = (Button) mView.findViewById(R.id.btn_play);
        mPlayMode = (Button) mView.findViewById(R.id.btn_play_mode);
    }

    private String secondToTime(int second) {
        if (second == 0) {
            return "00:00:00";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        return formatter.format(second * 1000);
    }

    @Override
    public void onDismiss() {
        Log.d(TAG, "onDismiss: dismiss");
    }
}
