package com.umeng.uniapp.util;
import java.util.Map;

public class JSONUtils {

    public static Map<String, Object> fastJsonObject2Map(com.alibaba.fastjson.JSONObject fastJson) {
        if (fastJson == null) {
            return null;
        }
        try {
            Map<String, Object> map = com.alibaba.fastjson.JSONObject.parseObject(fastJson.toString());
            return map;
        } catch (Throwable ignore) {

        }
        return null;
    }
}
