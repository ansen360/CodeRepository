package org.code.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

/**
 * Created by Ansen on 2015/7/1.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.common
 * @Description: TODO
 */
public class SpUtils {

    private static String SP_NAME = "QuciiSDK";
    public static final String SP_DEF_STRING_VALUE = "";

    private SpUtils() {
    }

    public static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * put boolean preferences
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getSp(context);
        Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * get boolean preferences, default is false
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = getSp(context);
        return sp.getBoolean(key, false);
    }

    /**
     * get boolean preferences
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = getSp(context);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = getSp(context);
        return sp.getString(key, SP_DEF_STRING_VALUE);
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getString(key, defValue);
    }

    /**
     * put int preferences
     */
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences settings = getSp(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * get int preferences
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * get int preferences
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = getSp(context);
        return settings.getInt(key, defaultValue);
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static long getLong(Context context, String key) {
        SharedPreferences sp = getSp(context);
        return sp.getLong(key, 0000);
    }

    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getLong(key, defValue);
    }

    /**
     * put float preferences
     */
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences settings = getSp(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * get float preferences
     */
    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    /**
     * get float preferences
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = getSp(context);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * 获取SP中所有键值对
     *
     * @return Map对象
     */
    public Map<String, ?> getAll(Context context) {
        return getSp(context).getAll();
    }

    /**
     * 从SP中移除该key
     *
     * @param key 键
     */
    public void remove(Context context, String key) {
        getSp(context).edit().remove(key).apply();
    }

    /**
     * 判断SP中是否存在该key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public boolean contains(Context context, String key) {
        return getSp(context).contains(key);
    }

    /**
     * 清除SP中所有数据
     */
    public void clear(Context context) {
        getSp(context).edit().clear().apply();
    }

    public static void addOnChangeListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences settings = getSp(context);
        settings.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void removeOnChangeListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences settings = getSp(context);
        settings.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
