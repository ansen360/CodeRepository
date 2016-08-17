package com.tomorrow_p.common;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class FloatUtils {
	/**还就是要保留2位小数点
	 * @param price
	 * @return
	 */
	public static String format(String price) {
		if ("0".equals(price)) {
			return "0.00";
		}
		Float f = (float) (Integer.parseInt(price) / 100.00);
		DecimalFormat formater = new DecimalFormat("##.00");
		return formater.format(f);
	}

	/**
	 * @param 需要验证的参数
	 * @return 是否是浮点数
	 */
	public static boolean isFloat(String string) {
		String regex = "^[-+]??(\\d++[.]\\d*?|[.]\\d+?)([eE][-+]??\\d++)?$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(string).matches();
	}

	/**
	 * 删除float没用的0
	 * @param f 11.1-->11.1  11.0-->11
	 * @return
	 */
	public static String DeleteFloatZero(Float f) {
		String mStr = f.toString();
		if (mStr.indexOf(".0") != -1) {
			int index = mStr.indexOf(".0");
			mStr = mStr.substring(0, index);
		}
		return mStr;
	}
}
