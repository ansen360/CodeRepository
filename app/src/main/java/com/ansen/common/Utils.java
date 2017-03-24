package com.ansen.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static String getAppVersion(Context ctx) {
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return info.versionName + "-" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isAppRunning(Context context, String packageName) {
        if (packageName == null)
            return false;

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName)
                    || info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }

        return false;
    }

}
