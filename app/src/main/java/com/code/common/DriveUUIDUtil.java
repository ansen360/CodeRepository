package com.code.common;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.UUID;

public class DriveUUIDUtil {

	/**
	 * 得到设备<a href="http://www.it165.net/pro/ydad/" target="_blank"
	 * class="keylink">Android</a>ID，需要设备添加 Google账户
	 */
	public static String getAndroidID(Context context) {
		String androidID = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
		return androidID;
	}

	/**
	 * 得到设备IMEI值
	 */
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 通过IMEI获得uuid
	 */
	public static String getDriverUUID(Context context){
//		String uuid ="";
//		uuid = UUID.nameUUIDFromBytes(getIMEI(context).getBytes()).toString();
//		return uuid;
		String macAddress = getMacAddress();
		if(TextUtils.isEmpty(macAddress)){
			macAddress = getMacAddress(context);
		}
		return macAddress + ":F";
		
	}
	
	/**
	 * 获取mac地址
	 */
	private static String getMacAddress(Context context){
		String address ="";
		try {
			if(context == null){
//				Utils.errorRemind("DriveUUIDUtils", "wifi not open context is null",null);
//				LogsReaderWriter.writeLogsToFile("context == null=-===================================");
//				context = LauncherApplication.getContext();
			}
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			address = info.getMacAddress().replace(":", "-").toUpperCase();
		} catch (Exception e) {
			if(e != null){
//				LogsReaderWriter.writeLogsToFile(e.getMessage());
			}
//			Utils.errorRemind("DriveUUIDUtils", "wifi not open", e);
		}
	
		return address;
	}
	
	private static String getMacAddress(){
	    String macSerial = null;
	    String str = "";

	    try 
	    {
	        Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
	        InputStreamReader ir = new InputStreamReader(pp.getInputStream());
	        LineNumberReader input = new LineNumberReader(ir);

	        for (; null != str;) 
	        {
	            str = input.readLine();
	            if (str != null)
	            {
	                macSerial = str.trim();// 去空格
	                macSerial = macSerial.replace(":", "-").toUpperCase();
	                break;
	            }
	        }
	    } catch (Exception ex) {
	        // 赋予默认值
	        ex.printStackTrace();
	    }
	    return macSerial;
	}
	
	/**
	 * 得到设备序列号
	 */
	public static String getSimSerialNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimSerialNumber();
	}

	/**
	 * 得到设备唯一识别码
	 *
	 * @param context
	 * @return
	 */
	public static String getUniqueNumber(Context context) {
		String androidID = getAndroidID(context);
		String imei = getIMEI(context);
		String simSerialNumber = getSimSerialNumber(context);
		UUID uuid = new UUID(androidID.hashCode(),((long) imei.hashCode() << 32) | simSerialNumber.hashCode());
		return uuid.toString();
	}

	public static boolean isRightMPUUID(String uuid){
		if(!TextUtils.isEmpty(uuid) && uuid.length() == 19 && uuid.lastIndexOf(":F") == 17){
			return true;
		}
		return false;
	}
}
