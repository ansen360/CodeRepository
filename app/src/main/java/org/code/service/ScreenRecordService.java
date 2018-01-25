package org.code.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.code.R;
import org.code.activity.ScreenRecordActivity;
import org.code.common.Logger;
import org.code.common.ScreenUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ansen on 2018/1/17 17:12.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: org.code.service
 * @Description: TODO
 */
public class ScreenRecordService extends Service {

    private static final String TAG = "ScreenRecordService";
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private LinearLayout mFloatLayout;
    private MediaProjection mMediaProjection;
    private MediaRecorder mMediaRecorder;
    private VirtualDisplay mVirtualDisplay;
    private boolean isScreenRecord;
    private int mScreenDensity, mWidthPixels, mHeightPixels;

    @Override
    public IBinder onBind(Intent intent) {
//        return new MyBinder();
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int resultCode = intent.getIntExtra("resultCode", -1);
        Intent data = intent.getParcelableExtra("data");
        mWidthPixels = intent.getIntExtra("mWidthPixels", 720);
        mHeightPixels = intent.getIntExtra("mHeightPixels", 1080);
        mScreenDensity = intent.getIntExtra("mDensityDpi", 1);
        Log.d(TAG, "initData  -->  mScreenDensity: " + mScreenDensity + "  mWidthPixels: " + mWidthPixels + "  mHeightPixels: " + mHeightPixels);
        mMediaProjection = ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE))
                .getMediaProjection(resultCode, data);

        return super.onStartCommand(intent, flags, startId);
    }


    private void startRecorder() {
        Log.d(TAG, "startRecord");

        isScreenRecord = true;

        createMediaRecorder();

        createVirtualDisplay(); // 在mediaRecorder.prepare() 之后调用，否则报错 failed to get surface

        mMediaRecorder.start();
    }

    private void createVirtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay(TAG, mWidthPixels, mHeightPixels, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(), null, null);
    }

    private void createMediaRecorder() {
        // 生成文件名
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String curTime = formatter.format(new Date(System.currentTimeMillis()));

        mMediaRecorder = new MediaRecorder();

        //设置视频源: DEFAULT,Surface,Camera
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);

        //设置视频输出格式: amr_nb，amr_wb,default,mpeg_4,raw_amr,three_gpp
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        //设置视频编码格式: default, H263, H264, MPEG_4_SP
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

//        //设置音频源
//        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//
//        //设置音频编码格式: default，AAC，AMR_NB，AMR_WB
//        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        //设置视频尺寸大小
        mMediaRecorder.setVideoSize(mWidthPixels, mHeightPixels);

        //设置视频编码的码率
        mMediaRecorder.setVideoEncodingBitRate(5 * mWidthPixels * mHeightPixels);   // mWidthPixels * mHeightPixels

        //设置视频编码的帧率
        mMediaRecorder.setVideoFrameRate(60);   // 30

        //设置视频输出路径
        mMediaRecorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Ansen_" + curTime + ".mp4");

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, "VideoSize: " + mWidthPixels + " X " + mHeightPixels + "  VideoEncodingBitRate: "
                + (5 * mWidthPixels * mHeightPixels) + "  +VideoFrameRate: " + "60");
    }

    private void initView() {
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 设置window type为TYPE_SYSTEM_ALERT
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式,效果为背景透明
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;// 默认位置：右下角
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.x = (ScreenUtils.getScreenWidth(getApplicationContext()) - wmParams.width) / 2;// 设置x、y初始值，相对于gravity
        wmParams.y = 10;
        // 浮动窗口布局
        mFloatLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.screen_recorder, null);
        mWindowManager.addView(mFloatLayout, wmParams);
        final LinearLayout screenrecord = (LinearLayout) mFloatLayout.findViewById(R.id.screenrecord);
        final TextView title = (TextView) mFloatLayout.findViewById(R.id.screenrecord_title);
        final TextView close = (TextView) mFloatLayout.findViewById(R.id.screenrecord_close);
        final ImageView image = (ImageView) mFloatLayout.findViewById(R.id.play_stop);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        // 设置监听浮动窗口的触摸移动
        View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - mFloatLayout.getMeasuredWidth() / 2;
                Log.i(TAG, "RawX" + event.getRawX() + "  X" + event.getX());
                wmParams.y = (int) event.getRawY() - mFloatLayout.getMeasuredHeight() / 2 - 25;// 减25为状态栏的高度
                Log.i(TAG, "RawY" + event.getRawY() + "  Y" + event.getY());
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);// 刷新
                return false;
            }
        };
        mFloatLayout.setOnTouchListener(mOnTouchListener);
        screenrecord.setOnTouchListener(mOnTouchListener);
        screenrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isScreenRecord) {
                    image.setImageResource(R.mipmap.screen_record_play);
                    title.setText("开始");
                    stopRecord();
                } else {
                    image.setImageResource(R.mipmap.screen_record_stop);
                    title.setText("停止");
                    startRecorder();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "stopSelf");
                ScreenRecordService.this.stopSelf();

            }
        });
    }

    private void stopRecord() {
        Log.d(TAG, "stopRecord");
        isScreenRecord = false;

        mMediaRecorder.stop();
        mMediaRecorder.reset();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (isScreenRecord) {
            stopRecord();
        }

        mVirtualDisplay.release();
        mMediaProjection.stop();

        mWindowManager.removeView(mFloatLayout);
    }
}
