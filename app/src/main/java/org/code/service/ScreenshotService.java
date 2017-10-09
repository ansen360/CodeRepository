package org.code.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.code.R;
import org.code.common.FileUtils;
import org.code.common.ImageUtils;
import org.code.common.Logger;
import org.code.common.ScreenUtils;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ansen on 2017/9/15 10:55.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: org.code.service
 * @Description: TODO
 */
public class ScreenshotService extends Service {

    private static final String TAG = "ScreenshotService";

    // 创建浮动窗口设置布局参数的对象
    public WindowManager mWindowManager;
    public WindowManager.LayoutParams wmParams;
    private ImageView mImageView;

    private MediaProjectionManager mMediaProjectionManager;
    private int mWindowHeight, mWindowWidth, mScreenDensity;
    ImageReader mImageReader;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getScreenInfo();
        int resultCode = intent.getIntExtra("resultCode", 1);
        Intent data = intent.getParcelableExtra("data");
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        MediaProjection mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        mImageReader = ImageReader.newInstance(mWindowWidth, mWindowHeight, 0x1, 2);
        mMediaProjection.createVirtualDisplay("ScreenCapture", mWindowWidth, mWindowHeight, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
    }

    private void startCapture() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        String data = simpleDateFormat.format(new Date());
        Image image = mImageReader.acquireLatestImage();
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        ImageUtils.saveImage(getApplicationContext(), bitmap, data + ".png");

        image.close();
    }

    private void initView() {
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 设置window
        // type为TYPE_SYSTEM_ALERT
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;// 默认位置：左上角
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.x = (ScreenUtils.getScreenWidth(getApplicationContext()) - wmParams.width) / 2;// 设置x、y初始值，相对于gravity
        wmParams.y = 10;
        // 获取浮动窗口视图所在布局
        mImageView = new ImageView(this);
        mImageView.setImageResource(R.mipmap.ic_screentshot);
        mWindowManager.addView(mImageView, wmParams);
        mImageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 设置监听浮动窗口的触摸移动
        mImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - mImageView.getMeasuredWidth() / 2;
                Logger.i(TAG, "RawX" + event.getRawX() + "  X" + event.getX());
                wmParams.y = (int) event.getRawY() - mImageView.getMeasuredHeight() / 2 - 25;// 减25为状态栏的高度
                Logger.i(TAG, "RawY" + event.getRawY() + "  Y" + event.getY());
                mWindowManager.updateViewLayout(mImageView, wmParams);  // 刷新
                return false;
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCapture();
                Logger.d(TAG, "startCapture");
            }
        });
    }

    /**
     * 获取屏幕相关数据
     */
    private void getScreenInfo() {
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mWindowWidth = metrics.widthPixels;
        mWindowHeight = metrics.heightPixels;
        mScreenDensity = metrics.densityDpi;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ansen", "onDestory");
        mWindowManager.removeView(mImageView);
    }
}
