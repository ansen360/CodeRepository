package com.tomorrow_p.common;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Locale;

public class SettingsUtils {
    private static final String TAG = SettingsUtils.class.getSimpleName();

    // 停止自动亮度调节
    public static void stopAutoBrightness(Activity activity) {

        if (activity == null)
            return;
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    // 开启亮度自动调节
    public static void startAutoBrightness(Activity activity) {

        if (activity == null) {
            return;
        }
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    /**
     * @param language 简体中文="zh",英语="en"
     */
    public static void setLanguage(Context context, String language) {
        Resources resources = context.getResources();
        Configuration config = new Configuration();
        DisplayMetrics dm = resources.getDisplayMetrics();

        config.setLocale(checkLocal(language));
        resources.updateConfiguration(config, dm);
    }

    public static Locale checkLocal(String language) {
        if (Locale.US.getLanguage().equals(language)) {
            return Locale.US;
        } else if (Locale.CHINA.getLanguage().equals(language)) {
            return Locale.CHINA;
        }
        return Locale.US;
    }

    public static void updateLanguage(Context context, String language) {
        try {

            Object objIActMag, objActMagNative;
            Class clzIActMag = Class.forName("android.app.IActivityManager");
            Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
            Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
            // IActivityManager iActMag = ActivityManagerNative.getDefault();
            objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
            // Configuration config = iActMag.getConfiguration();
            Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
            Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
            config.locale = checkLocal(language);
            // iActMag.updateConfiguration(config);
            // 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
            // 会重新调用 onCreate();
            Class[] clzParams = {Configuration.class};
            Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod(
                    "updateConfiguration", clzParams);
            mtdIActMag$updateConfiguration.invoke(objIActMag, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            SystemClock.setCurrentTimeMillis(when);
        }
    }

    public static void setTime(int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            SystemClock.setCurrentTimeMillis(when);
        }
    }

    public static void reflashTime(Context context) {
        Logger.d(TAG, "刷新时间");
        try {
            Settings.Global.putInt(context.getContentResolver(), Settings.Global.AUTO_TIME, 0);
            Settings.Global.putInt(context.getContentResolver(), Settings.Global.AUTO_TIME, 1);
        } catch (Exception e) {
            Logger.e(TAG, "权限有限" + e.getMessage());
        }
    }

}
