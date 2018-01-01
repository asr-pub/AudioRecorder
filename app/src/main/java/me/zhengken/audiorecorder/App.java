package me.zhengken.audiorecorder;

import android.app.Application;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import me.zhengken.audiorecorder.model.MyObjectBox;

/**
 * Created by zhengken on 2017/11/5.
 */

public class App extends Application {

    private BoxStore mBoxStore;

    @Override
    public void onCreate() {
        super.onCreate();

        mBoxStore = MyObjectBox.builder().androidContext(App.this).build();
        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(mBoxStore).start(this);
        }
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }
}
