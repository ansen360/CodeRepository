package com.tomorrow_p.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class BrowserUtils {
	/**
	 * 访问网站
	 */
	public static void onInternet(Context mContext, String info) {
		/* 调用系统浏览器 */
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(info);
		intent.setData(content_url);
		mContext.startActivity(intent);
	}
}
