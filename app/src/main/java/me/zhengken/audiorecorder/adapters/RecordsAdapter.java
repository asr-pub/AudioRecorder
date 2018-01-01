package me.zhengken.audiorecorder.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import io.objectbox.Box;
import me.zhengken.audiorecorder.R;
import me.zhengken.audiorecorder.model.Record;
import me.zhengken.audiorecorder.model.Record_;
import me.zhengken.audiorecorder.popupwindows.PlaybackWindow;

/**
 * Copyright  : zhengken@live.com
 * Created by zhengken on 2017/9/24.
 * ClassName    : RecordsAdapter
 * Description  :
 */
public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.MyViewHolder> {

    private static final String TAG = "RecordsAdapter";

    private List<Record> mRecordsList = null;

    private Context mContext;

    public RecordsAdapter(Context context) {

        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_file_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String detailedInfo = String.format("%s 录制于 %s"
                , mRecordsList.get(position).getFormattedDuration()
                , mRecordsList.get(position).getFormattedDate());
        holder.mFileName.setText(mRecordsList.get(position).getName());
        holder.mDetailedInfo.setText(detailedInfo);

        holder.mClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlaybackWindow(mRecordsList.get(position));
            }
        });

        holder.mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: position = " + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecordsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mClickArea;
        private TextView mFileName;
        private TextView mDetailedInfo;

        private TextView mOptions;

        public MyViewHolder(View itemView) {
            super(itemView);

            mClickArea = (LinearLayout) itemView.findViewById(R.id.ll_click_area);
            mFileName = (TextView) itemView.findViewById(R.id.txt_file_name);
            mDetailedInfo = (TextView) itemView.findViewById(R.id.txt_detail);
            mOptions = (TextView) itemView.findViewById(R.id.txt_options);

            mOptions.setTypeface(null, Typeface.BOLD);
        }
    }

    public void setRecords(List<Record> recordList) {

        mRecordsList = recordList;
        notifyDataSetChanged();
    }

    private void showPlaybackWindow(Record record) {

        PlaybackWindow playbackWindow = new PlaybackWindow(mContext, record);

        playbackWindow.showPopupWindow();

    }
}
