package org.code.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.TrafficStats;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.code.R;
import org.code.common.Logger;
import org.code.common.NetworkUtils;
import org.code.common.ScreenUtils;
import org.code.common.SizeUtils;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by Ansen on 2017/6/27 16:41.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: org.code.service
 * @Description: TODO
 */
public class NetSpeedService extends Service {

    private static final String TAG = "NetSpeedService";

    public LinearLayout mFloatLayout;
    public WindowManager mWindowManager;
    public WindowManager.LayoutParams wmParams;
    private TextView mFloatView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        getNetSpeed();
    }

    private void initView() {
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 设置window type为TYPE_SYSTEM_ALERT
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式,效果为背景透明
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;// 默认位置：左上角
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.x = (ScreenUtils.getScreenWidth(getApplicationContext()) - wmParams.width) / 2;// 设置x、y初始值，相对于gravity
        wmParams.y = 10;
        // 浮动窗口布局
        mFloatLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.service_netspeed, null);
        mWindowManager.addView(mFloatLayout, wmParams);// 添加mFloatLayout
        mFloatView = (TextView) mFloatLayout.findViewById(R.id.speed);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth() / 2;
                Logger.i(TAG, "RawX" + event.getRawX() + "  X" + event.getX());
                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() / 2 - 25;// 减25为状态栏的高度
                Logger.i(TAG, "RawY" + event.getRawY() + "  Y" + event.getY());
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);// 刷新
                return false;
            }
        });
    }

    private void setText(final double speed) {
        mFloatView.post(new Runnable() {
            @Override
            public void run() {
                mFloatView.setText(speed + "kb/s");
            }
        });
    }

    private static Timer mTimer;
    private int updateHz = 1;   // 更新频率（每几秒更新一次,至少1秒）
    private int times = 1;
    private long rxBytes;

    /**
     * 获取当前网速(需要实现NetSpeed接口获取网速)
     */
    public void getNetSpeed() {
        rxBytes = TrafficStats.getTotalRxBytes();    //获取当前下载流量总和
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (times == updateHz) {
                    setText(calculateNetSpeed());
                    times = 1;
                } else {
                    times++;
                }
            }
        }, 1000, 1000);
    }

    /**
     * 计算网速
     */
    private double calculateNetSpeed() {
        long curRxBytes = TrafficStats.getTotalRxBytes();    //获取当前下载流量总和
        if (rxBytes == 0)
            rxBytes = curRxBytes;
        long bytes = curRxBytes - rxBytes;
        rxBytes = curRxBytes;
        //int kb = (int) Math.floor(bytes / 1024 + 0.5);
        double kb = (double) bytes / (double) 1024;
        BigDecimal bd = new BigDecimal(kb);

        return bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
