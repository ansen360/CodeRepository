package org.code.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class DeviceUtils {
	protected static final String TAG = DeviceUtils.class.getSimpleName();

	// 移动  
	private static final int CHINA_MOBILE = 1;
	// 联通 
	private static final int UNICOM = 2;
	// 电信 
	private static final int TELECOMMUNICATIONS = 3;
	// 失败  
	private static final int ERROR = 0;

	/**
	 * 手机唯一标识
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		return uniqueId;
	}

	/**
	 * 手机MAC地址
	 * @param context
	 * @return
	 */
	public static String getMacAddressInfo(Context context) {
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * TelephonyManager对象
	 * @param context
	 * @return
	 */
	private static TelephonyManager getTelphoneManager(Context context) {
		return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * DeviceId
	 * @param context
	 * @return
	 */
	public static String getDeviceID(Context context) {
		return getTelphoneManager(context).getDeviceId();
	}

	/**
	 * IMSI号
	 * @param context
	 * @return
	 */
	public static String getImis(Context context) {
		return getTelphoneManager(context).getSubscriberId();
	}

	/**
	 *  厂商信息
	 * @return
	 */
	public static String getProductInfo() {
		return android.os.Build.MODEL;
	}

	/**
	 * release版本
	 * @return
	 */
	public static String getReleaseVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * SDK_INT 版本
	 * @return
	 */
	public static int getSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 手机号码
	 * @param context
	 * @return
	 */
	public static String getPhoneNum(Context context) {
		return getTelphoneManager(context).getLine1Number();
	}

	/**
	 * 当前运营商
	 * @param context
	 * @return 返回0 表示失败 1表示为中国移动 2为中国联通 3为中国电信
	 */
	public static int getProviderName(Context context) {
		String IMSI = getImis(context);
		if (IMSI == null) {
			return ERROR;
		}
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			return CHINA_MOBILE;
		} else if (IMSI.startsWith("46001")) {
			return UNICOM;
		} else if (IMSI.startsWith("46003")) {
			return TELECOMMUNICATIONS;
		}
		return ERROR;
	}

	/**
	 * 手机CPU名字
	 * @return
	 */
	public static String getCpuName() {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			// 读取文件CPU信息
			fileReader = new FileReader("/pro/cpuinfo");
			bufferedReader = new BufferedReader(fileReader);
			String string = bufferedReader.readLine();
			String[] strings = string.split(":\\s+", 2);
			return strings[1];
		} catch (FileNotFoundException e) {
			Logger.e(TAG, e.getLocalizedMessage());
		} catch (IOException e) {
			Logger.e(TAG, e.getLocalizedMessage());
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					Logger.e(TAG, e.getLocalizedMessage());
				}
			}
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					Logger.e(TAG, e.getLocalizedMessage());
				}
			}
		}
		return null;
	}

	/**
	 * 检查程序是否运行
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppRunning(Context context, String packageName) {
		boolean isAppRunning = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
				isAppRunning = true;
				// find it, break
				break;
			}
		}
		return isAppRunning;
	}

	/**
	 * 是否在最前面
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isTopActivity(Context context, String packageName) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			System.out.println("---------------包名-----------" + tasksInfo.get(0).topActivity.getPackageName());
			// 应用程序位于堆栈的顶层
			if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}
