package com.tomorrow_p.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class GsonUtils {
    private static Gson gson = null;

    private GsonUtils() {
    }

    public static Gson getInstance() {
        if (gson == null) {
            synchronized (GsonUtils.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

    public static Map<String, Object> parseJson2Map(String json) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        map = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
        }.getType());
        return map;
    }

    public static String parseObj2Json(Object object) throws Exception {
        if (object == null) {
            return "";
        }
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(object, object.getClass());
        return json;
    }

    /**
     * JSON转成指定对象
     *
     * @param json
     */
    public static <T> T parseJson2Obj(String json, TypeToken<T> typeToken) throws Exception {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new StringReader(json));
        return gson.fromJson(reader, typeToken.getType());
    }
}
