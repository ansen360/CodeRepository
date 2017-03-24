package com.ansen.common;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * 震动工具类
 * @where
 */
public class ShockUtil {

	/**
	 * 时长震动
	 * @param activity
	 * @param milliseconds 震动的时长，单位是毫秒
	 */
	public static void Vibrate(final Context context, long milliseconds, boolean isVibrate) {
		if (isVibrate) {
			Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(milliseconds);
		}
	}

	public static void Vibrate1(final Context context, long milliseconds, boolean soundOnorOff) {
		if (soundOnorOff) {
			Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(milliseconds);
		}
	}

	/**
	 * 自定义震动模式
	 * 
	 * @param activity
	 * @param pattern 数组中数字的含义依次是静止的时长，震动时长，静止时长，震动时长。 单位是毫秒
	 * @param isRepeat 是否反复震动，如果是true，反复震动，如果是false，只震动一次
	 */
	public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}
}
