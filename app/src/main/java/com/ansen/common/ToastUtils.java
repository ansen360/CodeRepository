package com.ansen.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ansen.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Ansen on 2015/9/23.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.common
 * @Description: TODO
 */
public class ToastUtils {
    private static final String TAG = "ToastUtils";
    private static int GRAVITY = Gravity.CENTER;
    private static Context mContext;
    private static WindowManager wm;
    private static List<View> views = new ArrayList<View>();

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String text = (String) msg.obj;
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
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

    public static void show(int res) {
        String text = mContext.getResources().getString(res);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        } else {
            Message message = new Message();
            message.obj = text;
            mHandler.sendMessage(message);
        }
    }

    public static void showLong(Context context, String message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    public static void showShort(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, int textId) {
        show(context, textId, Toast.LENGTH_LONG);
    }

    public static void showShort(Context context, int textId) {
        show(context, textId, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(GRAVITY, 80, 80);
        toast.show();
    }

    public static void show(Context context, int textId, int duration) {
        Toast toast = Toast.makeText(context, textId, duration);
        toast.setGravity(GRAVITY, 80, 80);
        toast.show();
    }

    public static void showSuccess(Context context, int textId) {
//        showIconToast(context, textId, R.drawable.ic_success, R.color.holo_blue);
    }

    public static void showFailure(Context context, int textId) {
//        showIconToast(context, textId, R.drawable.ic_failure, R.color.warn);
    }

    public static void showIconToast(Context context, int textId, int iconId, int colorId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.common_toast, null);
        ((TextView) layout).setText(textId);
        ((TextView) layout).setTextColor(context.getResources().getColor(colorId));
        ((TextView) layout).setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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
