package me.zhengken.audiorecorder.fragments;

import android.support.v4.app.Fragment;

/**
 * Copyright  : zhengken@live.com
 * Created by zhengken on 2017/9/3.
 * ClassName    : FragmentFactory
 * Description  :
 */

public class FragmentFactory {

    public static Fragment getFragment(int position) {
        switch (position) {
            case 0:
                return RecordFragment.newInstance(position);
            case 1:
                return RecordFileFragment.newInstance(position);
            default:
                throw new IllegalArgumentException();
        }
    }

}
