package org.code.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public final class Logger {

    public static boolean DEBUG = true;
    private static final String TAG = "PUSH Logger";

    private Logger() {
    }

    public static void v(String tag, String message) {
        if (DEBUG)
            Log.v(tag, message);
    }

    public static void v(String message) {
        if (DEBUG)
            Log.v(TAG, message);
    }

    public static void d(String tag, String message) {
        if (DEBUG)
            Log.d(tag, message);
    }

    public static void d(String message) {
        if (DEBUG)
            Log.d(TAG, message);
    }

    public static void i(String tag, String message) {
        if (DEBUG)
            Log.i(tag, message);
    }

    public static void i(String message) {
        if (DEBUG)
            Log.i(TAG, message);
    }

    public static void w(String tag, String message) {
        if (DEBUG)
            Log.w(tag, message);
    }

    public static void w(String message) {
        if (DEBUG)
            Log.w(TAG, message);
    }

    public static void e(String tag, String message) {
        if (DEBUG)
            Log.e(tag, message);
    }

    public static void e(String message) {
        if (DEBUG)
            Log.e(TAG, message);
    }

    public static void e(String msg, Throwable e) {
        e(TAG, msg, e);
    }

    public static void e(String tag, String msg, Throwable e) {
        if (DEBUG) {
            Log.e(tag, msg, e);
        }
    }

    public void write2File(String log, String path) {
        if (path != null && Logger.DEBUG) {
            FileWriter writer = null;
            try {
                writer = new FileWriter(path, true);
                if (writer != null) {
                    Calendar calendar = Calendar.getInstance();
                    String curDate = String.format("%04d-%02d-%02d %02d:%02d:%02d ", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
                    writer.write(curDate + log + "\r\n");
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null)
                        writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static final String DEBUG_FLAG = "DEBUG_FLAG";

    private static final String RECEIVER_PERMISSION = "common.debug.broadcast.permission";

    private static final String LOG_CONTENT = "LOG_CONTENT";

    /**
     * 配合common-debug使用,将Log输出到 /mnt/sdcard/main_log.txt文件
     */
    public static void printLog(Context context, String packageName, String log) {
        if (Settings.Global.getInt(context.getContentResolver(), DEBUG_FLAG, 0) == 0) {
            Intent intent = new Intent("intent.action.log.enqueue");
            intent.setComponent(new ComponentName("com.common.debug", "com.common.debug.LogReceiver"));
            intent.putExtra(LOG_CONTENT, packageName + " " + log);
//            sendBroadcast(intent, RECEIVER_PERMISSION);
            context.sendBroadcast(intent);
        }
    }
}
