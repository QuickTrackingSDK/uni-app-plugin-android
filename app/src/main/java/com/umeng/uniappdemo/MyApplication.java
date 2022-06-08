package com.umeng.uniappdemo;
import android.util.Log;

import io.dcloud.application.DCloudApplication;

public class MyApplication extends DCloudApplication {
    public static final String TAG = "UMENG_DEMO";
    @Override
    public void onCreate(   ) {
        super.onCreate();
        Log.i(TAG, "--->>> native Application: onCreate enter.");
    }
}
