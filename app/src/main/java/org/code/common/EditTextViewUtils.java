package org.code.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.widget.EditText;

public class EditTextViewUtils {
	/**
	 * 自定义键盘显示关标
	 * @param editText
	 */
	public static void showOffLabel(EditText editText) {
		Class<EditText> cls = EditText.class;
		try {
			Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
			setShowSoftInputOnFocus.setAccessible(false);
			setShowSoftInputOnFocus.invoke(editText, false);
			setShowSoftInputOnFocus.invoke(editText, false);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
