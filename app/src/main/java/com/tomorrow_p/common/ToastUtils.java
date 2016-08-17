package com.tomorrow_p.common;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ToastUtils {
    private static final String TAG = ToastUtils.class.getSimpleName();
    private static WindowManager wm;
    private static List<View> views = new ArrayList<View>();

    public static void debugToast(String msg) {
        if (Logger.DEBUG) {

        }
    }

    /**
     * 显示可点击自定义土司
     */
    public static void showClickableView(View view, Context context,
                                         WindowManager.LayoutParams params) {
        Logger.d(TAG, "view.size()======>" + views.size());
        if (wm == null) {
            wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
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
