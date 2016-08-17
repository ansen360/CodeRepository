package com.tomorrow_p.common;

import android.app.Activity;
import android.content.Intent;

/**
 * 邮件相关类
 */
public class EMailUtils {
	/**
	 * 调用手机中email应用发送邮件
	 * @param context
	 * @param receivers
	 * @param copys
	 * @param title
	 * @param content
	 */
	public static void sendEmail(Activity context, String[] receivers, String[] copys, String title, String content) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		//接收者
		intent.putExtra(Intent.EXTRA_EMAIL, receivers);
		//抄送者
		intent.putExtra(Intent.EXTRA_CC, copys);
		//邮件Title
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		//邮件内容
		intent.putExtra(Intent.EXTRA_TEXT, content);
		context.startActivity(Intent.createChooser(intent, null));
	}

}
