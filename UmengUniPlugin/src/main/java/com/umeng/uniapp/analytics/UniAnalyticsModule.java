/*
 * Copyright 2015－2021 Umeng+ Data Inc.
 *
 */

package com.umeng.uniapp.analytics;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.spm.SpmAgent;
import com.umeng.uniapp.util.JSONUtils;

import java.util.Map;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniDestroyableModule;

public class UniAnalyticsModule extends UniDestroyableModule {

    public static final String VERSION = "1.0.0";

    private static final String MODULE_NAME = "UniAnalyticsModule";
    private static final String LOG_TAG = "MobclickAgent";

    @UniJSMethod()
    public void setCustomDomain(String primaryDomain, String standbyDomain) {
        try {
            UMConfigure.setCustomDomain(primaryDomain, standbyDomain);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void setLogEnabled(int enable) {
        try {
            if (enable == 1) {
                UMConfigure.setLogEnabled(true);
            } else {
                UMConfigure.setLogEnabled(false);
            }
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void preInit(String appkey, String channel) {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            UMConfigure.preInit(appContext, appkey, channel);
        } catch (Throwable e) {

        }
    }

//    @UniJSMethod()
//    public void init2() {
//        try {
//            //Log.i(LOG_TAG, "--->>>*** ");
//        } catch (Throwable e) {
//
//        }
//    }

    @UniJSMethod()
    public void init(String appkey, String channel, int deviceType, String pushSecret) {
        try {
            Log.i(MODULE_NAME, "Umeng uni-app(Android) plugin version: " + VERSION);
            Context appContext = UniAnalyticsProxy.getAppContext();
            UMConfigure.init(appContext, appkey, channel, deviceType, pushSecret);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void onKillProcess() {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            MobclickAgent.onKillProcess(appContext);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void registerGlobalProperties(String globalProperties) {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            JSONObject gp = JSONObject.parseObject(globalProperties);
            if (gp != null && gp instanceof JSONObject) {
                Map<String, Object> args = JSONUtils.fastJsonObject2Map(gp);
                MobclickAgent.registerGlobalProperties(appContext, args);
            }
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void unregisterGlobalProperty(String propertyName) {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            MobclickAgent.unregisterGlobalProperty(appContext, propertyName);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod(uiThread = false)
    public String getGlobalProperties() {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            String result = MobclickAgent.getGlobalProperties(appContext);
            return result;
        } catch (Throwable e) {

        }
        return null;
    }

    @UniJSMethod()
    public void clearGlobalProperties() {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            MobclickAgent.clearGlobalProperties(appContext);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void setAutoPageEnabled(int mode) {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            if (mode == 1) {
                MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL);
            } else {
                MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
            }
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void onPageStart(String viewName) {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            MobclickAgent.onPageStart(viewName);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void onPageEnd(String viewName) {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            MobclickAgent.onPageEnd(viewName);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void skipMe(String viewName) {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            Activity currentActivity = UniAnalyticsProxy.getCurrentActivity();
            MobclickAgent.skipMe(currentActivity, viewName);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void disableActivityPageCollection() {
        try {
            MobclickAgent.disableActivityPageCollection();
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void onProfileSignIn(String ID) {
        try {
            MobclickAgent.onProfileSignIn(ID);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void onProfileSignIn(String ID, String Provider) {
        try {
            MobclickAgent.onProfileSignIn(Provider, ID);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void onProfileSignOff() {
        try {
            MobclickAgent.onProfileSignOff();
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void onEventObject(String eventID, String parameters) {
        try {
//            Log.i(LOG_TAG, "--->>> onEventObject called. eventID = " + eventID);
//            Log.i(LOG_TAG, "--->>> onEventObject called. args = " + parameters);
            JSONObject args = null;
            if (!TextUtils.isEmpty(parameters)) {
                args = JSONObject.parseObject(parameters);
            }
            if (args != null && args instanceof JSONObject) {

                Context appContext = UniAnalyticsProxy.getAppContext();
                Map<String, Object> map = JSONUtils.fastJsonObject2Map(args);
                for (Object value : map.values()) {
                    Log.i(LOG_TAG, "--->>> onEventObject map value = " + value);
                }
                MobclickAgent.onEventObject(appContext, eventID, map);
            }
        } catch (Throwable e) {
        }
    }

    @UniJSMethod()
    public void setProcessEvent(int enable) {
        try {
            if (enable == 1) {
                UMConfigure.setProcessEvent(true);
            } else {
                UMConfigure.setProcessEvent(false);
            }
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void setPageProperty(String pageName, String properties) {
        try {
            JSONObject args = JSONObject.parseObject(properties);
            if (args != null && args instanceof JSONObject) {
                Context appContext = UniAnalyticsProxy.getAppContext();
                Map<String, Object> map = JSONUtils.fastJsonObject2Map(args);
                SpmAgent.setPageProperty(appContext, pageName, map);
            }
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void updateCurSpm(String spm) {
        try {
            Context appContext = UniAnalyticsProxy.getAppContext();
            SpmAgent.updateCurSpm(appContext, spm);
        } catch (Throwable e) {

        }
    }

    @UniJSMethod()
    public void updateNextPageProperties(String properties) {
        try {
            JSONObject args = JSONObject.parseObject(properties);
            if (args != null && args instanceof JSONObject) {
                Context appContext = UniAnalyticsProxy.getAppContext();
                Map<String, Object> map = JSONUtils.fastJsonObject2Map(args);
                SpmAgent.updateNextPageProperties(map);
            }
        } catch (Throwable e) {

        }
    }

    @Override
    public void destroy() {
    }
}
