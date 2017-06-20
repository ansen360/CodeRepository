package org.code.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.code.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Ansen on 2015/9/23.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.common
 * @Description: 使用方法: 在程序入口(Application的onCreate)调用init函数初始化即可
 */
public class ToastUtils {

    private static final String TAG = "ToastUtils";

    private static final int COLOR_TEXT = Color.parseColor("#FFFFFF");

    private static final int COLOR_SUCCESS = Color.parseColor("#388E3C");
    private static final int COLOR_INFO = Color.parseColor("#3F51B5");
    private static final int COLOR_WARNING = Color.parseColor("#FFA900");
    private static final int COLOR_ERROR = Color.parseColor("#D50000");

    private static Context mContext;

    private static WindowManager wm;

    private static List<View> views = new ArrayList<View>();


    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String text = (String) msg.obj;
            show(text);
        }
    };

    public static void init(Context context) {
        mContext = context;
    }

    public static void show(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        } else {
            Message message = new Message();
            message.obj = text;
            mHandler.sendMessage(message);
        }
    }

    public static void show(int textRes) {
        String text = mContext.getResources().getString(textRes);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        } else {
            Message message = new Message();
            message.obj = text;
            mHandler.sendMessage(message);
        }
    }

    /**
     * 设置文本Toast的背景
     */
    public static void show(CharSequence text, int tintColor) {
        toast(text, COLOR_TEXT, 0, tintColor);
    }

    /**
     * 设置文本Toast的背景
     */
    public static void show(int textRes, int tintColor) {
        toast(textRes, COLOR_TEXT, 0, tintColor);
    }

    public static void showSuccess(CharSequence text) {
        toast(text, COLOR_TEXT, R.drawable.ic_toast_success, COLOR_SUCCESS);
    }

    public static void showSuccess(int textRes) {
        toast(textRes, COLOR_TEXT, R.drawable.ic_toast_success, COLOR_SUCCESS);
    }

    public static void showInfo(CharSequence text) {
        toast(text, COLOR_TEXT, R.drawable.ic_toast_info, COLOR_INFO);
    }

    public static void showInfo(int textRes) {
        toast(textRes, COLOR_TEXT, R.drawable.ic_toast_info, COLOR_INFO);
    }

    public static void showWarning(CharSequence text) {
        toast(text, COLOR_TEXT, R.drawable.ic_toast_warning, COLOR_WARNING);
    }

    public static void showWarning(int textId) {
        toast(textId, COLOR_TEXT, R.drawable.ic_toast_warning, COLOR_WARNING);
    }

    public static void showError(CharSequence text) {
        toast(text, COLOR_TEXT, R.drawable.ic_toast_error, COLOR_ERROR);
    }

    public static void showError(int textRes) {
        toast(textRes, COLOR_TEXT, R.drawable.ic_toast_error, COLOR_ERROR);
    }

    public static void showIcon(CharSequence text, int iconRes) {
        toast(text, COLOR_TEXT, iconRes, COLOR_INFO);
    }

    public static void showIcon(int textId, int iconRes) {
        toast(textId, COLOR_TEXT, iconRes, COLOR_INFO);
    }

    /**
     * @param textRes   需要显示文本
     * @param textColor 文本的颜色,如果不用,设置0
     * @param iconRes   左侧图标,如果不用,设置0
     * @param tintColor 色调,如果不用,设置0
     */
    public static void toast(int textRes, @ColorInt int textColor, int iconRes, @ColorInt int tintColor) {

        View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.common_toast, null);
        ImageView toastIcon = (ImageView) view.findViewById(R.id.toast_icon);
        TextView toastText = (TextView) view.findViewById(R.id.toast_text);
        if (textColor != 0) {
            toastText.setTextColor(textColor);
        }
        toastText.setText(textRes);
        toastText.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));

        if (iconRes == 0) {
            toastIcon.setVisibility(View.GONE);
        } else {
            toastIcon.setImageResource(iconRes);
        }

        setTint(view, tintColor);
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.BOTTOM, 0, 200);

        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * @param text      需要显示文本
     * @param textColor 文本的颜色,如果不用,设置0
     * @param iconRes   左侧图标,如果不用,设置0
     * @param tintColor 色调,如果不用,设置0
     */
    public static void toast(@NonNull CharSequence text, @ColorInt int textColor, int iconRes, @ColorInt int tintColor) {

        View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.common_toast, null);
        ImageView toastIcon = (ImageView) view.findViewById(R.id.toast_icon);
        TextView toastText = (TextView) view.findViewById(R.id.toast_text);
        if (textColor != 0) {
            toastText.setTextColor(textColor);
        }
        toastText.setText(text);
        toastText.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));

        if (iconRes == 0) {
            toastIcon.setVisibility(View.GONE);
        } else {
            toastIcon.setImageResource(iconRes);
        }

        setTint(view, tintColor);
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.BOTTOM, 0, 200);

        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    private static void setTint(View view, @ColorInt int tintColor) {
        NinePatchDrawable drawable = (NinePatchDrawable) mContext.getDrawable(R.mipmap.bg_toast);
        if (tintColor != 0) {
            drawable.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        }
        view.setBackground(drawable);
    }

    /**
     * 显示可点击自定义土司
     */
    public static void showClickableView(View view, Context context, WindowManager.LayoutParams params) {
        Logger.d(TAG, "view.size()======>" + views.size());
        if (wm == null) {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        wm.addView(view, params);
        views.add(view);
    }

    /**
     * 隐藏自定义土司
     */
    public static void hide(View view) {
        if (!views.contains(view)) {
            return;
        }
        if (wm != null) {
            wm.removeView(view);
            Iterator<View> iterator = views.iterator();
            while (iterator.hasNext()) {
                View next = iterator.next();
                if (next.equals(view)) {
                    iterator.remove();
                    break;
                }
            }
        }
        if (views.size() == 0) {
            wm = null;
        }
    }
}
