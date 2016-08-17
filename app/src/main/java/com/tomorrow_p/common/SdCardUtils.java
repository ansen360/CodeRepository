package com.tomorrow_p.common;

import android.os.Environment;
import android.os.StatFs;

public class SdCardUtils {
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return
	 */
	public int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		// (availableBlocks * blockSize)/1024 KIB 单位
		// (availableBlocks * blockSize)/1024 /1024 MIB单位
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / (1024 * 1024);
		return (int) sdFreeMB;
	}
}
