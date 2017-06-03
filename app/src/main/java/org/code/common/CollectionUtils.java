package org.code.common;

public class CollectionUtils {
	/**
	 * 数组是否包含元素
	 * @param arr
	 * @param str
	 * @return
	 */
	public static boolean isArrayContains(String[] arr, String str) {
		if (arr == null || arr.length == 0) {
			return false;
		}
		for (int i = 0; i < arr.length; i++) {
			String curStr = arr[i];
			if (curStr.equals(str)) {
				return true;
			}
		}
		return false;
	}
}
