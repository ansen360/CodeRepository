package com.tomorrow_p.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @项目名: code
 * @包名: com.tomorrow_p.common
 * @类名: SpUtils
 * @创建者: senPan
 * @创建时间: 2015-7-1 下午5:51:23
 * @描述: SharedPreferences的工具类
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class SpUtils {
    private static String SP_NAME = "config";

    public static SharedPreferences getSp(Context context) {
        SharedPreferences sp = null;
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp;
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getSp(context);
        Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getBoolean(key, defValue);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = getSp(context);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getString(key, defValue);
    }

}
