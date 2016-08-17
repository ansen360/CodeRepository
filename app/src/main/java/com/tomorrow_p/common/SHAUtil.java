package com.tomorrow_p.common;

import java.security.MessageDigest;

/**
 * 提供SHA加密
 */
public class SHAUtil {

	// 进行SHA-1加密
	public static String SHAEncode(String message) {
		String resultString = null;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(message.getBytes());
			resultString = bytes2HexString(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}

	// 将Bytes数据转换成16进制字符串格式
	public static String bytes2HexString(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

}
