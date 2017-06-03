package org.code.common;

import android.text.Html;
import android.text.Spannable;

public class TextViewUtils {
	public static Spannable changeColor(String beforeText, String centerText, String endText, String color) {
		return (Spannable) Html.fromHtml(beforeText + "<font color=\"#" + color + "\">" + centerText + "</font>" + endText);
	}

	public static Spannable changeColor(String centerText, String endText, String color) {
		return (Spannable) Html.fromHtml("<font color=\"#" + color + "\">" + centerText + "</font>" + endText);
	}

	public static Spannable changeColor(String centerText, String color) {
		return (Spannable) Html.fromHtml("<font color=\"#" + color + "\">" + centerText + "</font>");
	}
}
