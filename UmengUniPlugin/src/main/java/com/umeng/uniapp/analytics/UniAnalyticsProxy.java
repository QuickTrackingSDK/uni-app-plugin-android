package com.umeng.uniapp.analytics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.debug.UMRTLog;

import io.dcloud.feature.uniapp.UniAppHookProxy;

public class UniAnalyticsProxy implements UniAppHookProxy {
    private static final String LOG_TAG = "MobclickAgent";
    private static Context appContext = null;

    private static Object lock = new Object();
    private static Activity currentActivity = null;

    Application.ActivityLifecycleCallbacks callback = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            synchronized (lock) {
                currentActivity = activity;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            synchronized (lock) {
                currentActivity = null;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            currentActivity = null;
        }
    };

    private String getCfgStrValueFromXml(Context context, String cfgKey) {
        String result = "";
        try {
            PackageManager manager = context.getPackageManager();
            ApplicationInfo info = manager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            if (info != null) {
                result = info.metaData.getString(cfgKey);
                if (result == null) {
                    Log.e(LOG_TAG, "配置项: " + cfgKey + "未正确配置。请检查工程AndroidManifest.xml文件!");
                } else {
                    if (TextUtils.isEmpty(result)) {
                        Log.i(LOG_TAG, "配置项: " + cfgKey + "; 值: 空字符串");
                    } else {
                        Log.i(LOG_TAG, "配置项: " + cfgKey + "; 值：" + result);
                    }
                }
            }
        } catch (Throwable ignore) {

        }
        return result;
    }

    /**
     * Returns the value associated with the given key, or 0 if
     * no mapping of the desired type exists for the given key.
     * @param context
     * @param cfgKey
     * @return
     */
    private int getCfgIntValueFromXml(Context context, String cfgKey) {
        int result = 0;
        try {
            PackageManager manager = context.getPackageManager();
            ApplicationInfo info = manager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            if (info != null) {
                result = info.metaData.getInt(cfgKey);
                Log.i(LOG_TAG, "配置项: " + cfgKey + "; 值: " + result);
            }
        } catch (Throwable ignore) {

        }
        return result;
    }

    @Override
    public void onCreate(Application application) {
        Log.i(LOG_TAG, "--->>> UniAnalyticsProxy: onCreate() enter...");
        appContext = application.getApplicationContext();
        application.registerActivityLifecycleCallbacks(callback);

        int logFlag = getCfgIntValueFromXml(appContext, "UMENG_LOG");
        if (logFlag == 1) {
            UMConfigure.setLogEnabled(true);
        } else {
            // 默认不输出SDK调试日志
            UMConfigure.setLogEnabled(false);
        }
        String primaryUrl = getCfgStrValueFromXml(appContext, "UMENG_URL");
        String standbyUrl = getCfgStrValueFromXml(appContext, "UMENG_URL2");
        String appkey = getCfgStrValueFromXml(appContext, "UMENG_APPKEY");
        String realAppkey = appkey;
        if (appkey.startsWith("##")) {
            realAppkey = appkey.substring(2);
        }
        String channel = getCfgStrValueFromXml(appContext, "UMENG_CHANNEL");
        UMConfigure.setCustomDomain(primaryUrl, standbyUrl);
        int collectionMode = getCfgIntValueFromXml(appContext, "UMENG_MODE");
        if (collectionMode == 1) {
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL);
        } else {
            // 默认使用自动采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
            int ignoreActivityPath = getCfgIntValueFromXml(appContext, "UMENG_NOACTIVITYPATH");
            if (ignoreActivityPath == 1) {
                MobclickAgent.disableActivityPageCollection();
            }
        }
        Log.i("MobclickAgent", "uniapp插件 preInit传入appkey参数：" + realAppkey);
        UMConfigure.preInit(appContext, realAppkey, channel);
	Log.i(LOG_TAG, "preInit预初始化函数调用完毕。");
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static Activity getCurrentActivity() {
        synchronized (lock) {
            return currentActivity;
        }
    }

    @Override
    public void onSubProcessCreate(Application application) {
        //子进程初始化回调
    }
}
