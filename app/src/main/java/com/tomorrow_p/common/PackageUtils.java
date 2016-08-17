package com.tomorrow_p.common;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class PackageUtils {
	protected static final String TAG = PackageUtils.class.getSimpleName();

	/**
	 * 安装apk应用
	 */
	public static void installAPK(Context context, File apkFile) {
		if (apkFile.isFile()) {
			String fileName = apkFile.getName();
			String postfix = fileName.substring(fileName.length() - 4, fileName.length());
			if (postfix.toLowerCase().equals(".apk")) {
				String cmd = "chmod 755 " + apkFile.getAbsolutePath();
				try {
					Runtime.getRuntime().exec(cmd);
				} catch (Exception e) {
					Logger.e(TAG, e.getLocalizedMessage());
				}
				Uri uri = Uri.fromFile(apkFile);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(uri, "application/vnd.android.package-archive");
				context.startActivity(intent);
			}
		} else if (apkFile.isDirectory()) {
			File[] files = apkFile.listFiles();
			int fileCount = files.length;
			for (int i = 0; i < fileCount; i++) {
				installAPK(context, files[i]);
			}
		}
	}

	/**
	 * 安装apk应用
	 */
	public static void installDirApk(Context context, String filePath) {
		File file = new File(filePath);
		installAPK(context, file);
	}

	/**
	 * 卸载apk文件
	 */
	public static void uninstallPackage(Context context, Uri packageUri) {
		Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
		context.startActivity(intent);
	}

	/**
	 * 根据包得到应用信息
	 */
	public static ApplicationInfo getApplicationInfoByName(Context context, String packageName) {
		if (null == packageName || "".equals(packageName)) {
			return null;
		}
		try {
			return context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
		} catch (NameNotFoundException e) {
			Logger.e("EspanceUtils", packageName + " NameNotFoundException");
			return null;
		}
	}

	/**
	 *  通过报名获取包信息
	 */
	public static PackageInfo getPackageInfoByName(Context context, String packageName) {
		if (null == packageName || "".equals(packageName)) {
			return null;
		}
		try {
			return context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
		} catch (NameNotFoundException e) {
			Logger.e(TAG, e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * 判断apk包是否安装
	 */
	public static boolean isApkIntalled(Context context, String packageName) {
		if (null == getApplicationInfoByName(context, packageName)) {
			return false;
		} else {
			return true;
		}
	}
}
