package org.code.common;

import java.io.InputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * UI工具类
 * @version 1.0
 */
public class UiUtils {
	/**
	 * 设置view的显示状态
	 */
	public static void setVisibilitySafe(View view, int visibility) {
		if (view != null && view.getVisibility() != visibility) {
			view.setVisibility(visibility);
		}
	}

	/**
	 * 设置view的显示状态
	 */
	public static void setVisibilitySafe(View parent, int id, int visibility) {
		if (parent != null) {
			setVisibilitySafe(parent.findViewById(id), visibility);
		}
	}

	public static void setPressedSafe(View view, boolean pressed) {
		if (view != null && view.isPressed() != pressed) {
			view.setPressed(pressed);
		}
	}

	public static void setEnabledSafe(View parent, int id, boolean enabled) {
		if (parent != null) {
			View view = parent.findViewById(id);
			if (view != null) {
				view.setEnabled(enabled);
			}
		}
	}

	public static void setOnClickListenerSafe(View parent, int id, OnClickListener l) {
		if (parent != null) {
			View view = parent.findViewById(id);
			if (view != null) {
				view.setOnClickListener(l);
			}
		}
	}

	public static void requestFocus(View view) {
		if (view != null) {
			view.setFocusable(true);
			view.setFocusableInTouchMode(true);
			view.requestFocus();
		}
	}

	public static boolean isEditTextEmpty(EditText edit) {
		return edit.getText() == null || edit.getText().toString().trim().length() <= 0;
	}

	public static boolean hideInputMethod(Activity activity) {
		return hideInputMethod(activity, activity.getWindow().getDecorView().getWindowToken());
	}

	public static boolean hideInputMethod(Dialog dialog) {
		return hideInputMethod(dialog.getContext(), dialog.getWindow().getDecorView().getWindowToken());
	}

	public static boolean hideInputMethod(Context context, IBinder iBinder) {
		InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		return im.hideSoftInputFromWindow(iBinder, 0);
	}

	public static void checkBackgroudThread() {
		if (Looper.getMainLooper() == Looper.myLooper()) {
			throw new IllegalStateException("It must run in backgroud thread.");
		}
	}

	public static void cancelAsyncTask(AsyncTask<?, ?, ?> task) {
		if (task != null) {
			task.cancel(true);
		}
	}

	public static void clearBitmapInImageView(ImageView v) {
		if (v != null) {
			clearBitmapInDrawable(v.getDrawable());
		}
	}

	public static void clearBackgroundBitmapInView(View v) {
		if (v != null) {
			clearBitmapInDrawable(v.getBackground());
		}
	}

	public static void clearBitmapInDrawable(Drawable d) {
		if (d != null && d instanceof BitmapDrawable) {
			Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
			if (bitmap != null) {
				Logger.v("luochun", bitmap.toString());
				bitmap.recycle();
			}
		}
	}

	public static Bitmap decodeResourceBitmap(Context context, int resId) {
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is);
	}
}
