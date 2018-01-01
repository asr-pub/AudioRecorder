package me.zhengken.audiorecorder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;
import me.zhengken.audiorecorder.App;
import me.zhengken.audiorecorder.R;
import me.zhengken.audiorecorder.adapters.RecordsAdapter;
import me.zhengken.audiorecorder.events.UpdateRecordsEvent;
import me.zhengken.audiorecorder.model.Record;
import me.zhengken.audiorecorder.model.Record_;

/**
 * Copyright  : zhengken@live.com
 * Created by zhengken on 2017/9/3.
 * ClassName    : RecordFragment
 * Description  :
 */

public class RecordFileFragment extends Fragment {

    private static final String TAG = "RecordFileFragment";
    private static final String ARG_POSITION = "position";

    private RecordsAdapter mAdapter;
    private Query<Record> mRecordsQuery;
    private Box<Record> mRecordsBox;

    public static RecordFileFragment newInstance(int position) {
        RecordFileFragment fragment = new RecordFileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        mRecordsBox = ((App) getActivity().getApplication()).getBoxStore().boxFor(Record.class);
        mRecordsQuery = mRecordsBox.query().orderDesc(Record_.date).build();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAdapter = new RecordsAdapter(getContext());

        View view = inflater.inflate(R.layout.fragment_record_file, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecords(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void updateRecords(UpdateRecordsEvent event) {
        List<Record> records = mRecordsQuery.find();
        mAdapter.setRecords(records);
    }
}
