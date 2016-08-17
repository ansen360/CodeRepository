package com.tomorrow_p.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {
	/**
	 * @param phoneNo
	 *            加星号过的电话号码
	 */
	public static String dealPhoneNo(String phoneNo) {
		return phoneNo.substring(0, 3) + "****" + phoneNo.substring(7);
	}

	/**
	 *  通过正则表达式判断是否为手机号
	 * @param phoneString
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneString) {
		String format = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		return isMatch(format, phoneString);
	}

	/**
	 *  字符串正则校验
	 * @param regex
	 *            正则表达式
	 * @param string
	 *            需要检验的字符串
	 * @return
	 */
	public static boolean isMatch(String regex, String string) {

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(string);
		return matcher.matches();
	}
}
