package com.tomorrow_p.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.tomorrow_p.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ansen on 2015/07/18 10:22.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/1031307403/
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.activity
 * @Description: TODO
 */

public class CameraActivity extends Activity {
    private static final String TAG = "ansen";
    private View mRelativeLayout;
    private Camera mCamera;
    private Camera.Parameters mParameters = null;
    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
    private SurfaceHolder mHolder;

    Bundle bundle = null; // 声明一个Bundle对象，用来存储数据

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//拍照过程屏幕一直处于高亮

        setContentView(R.layout.activity_camera);

        mRelativeLayout = findViewById(R.id.relativeLayout);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, 144); //设置Surface分辨率
        surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        surfaceView.getHolder().addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数
    }

    public void takePhoto(View v) {
        if (mCamera != null) {
            // 获取到拍照的图片数据后回调PictureCallback,PictureCallback可以对相片进行保存或传入网络
            mCamera.takePicture(null, null, new MyPictureCallback());
        }
    }

    public void change(View v) {
        //切换前后摄像头
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//开始预览
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//开始预览
                    cameraPosition = 1;
                    break;
                }
            }

        }
    }

    /**
     * 图片被点击触发的时间
     *
     * @param v
     */
    public void imageClick(View v) {
        if (bundle == null) {
            Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, CameraPreviewImageActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private final class MyPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                bundle = new Bundle();
                bundle.putByteArray("bytes", data); //将图片字节数据保存在bundle当中，实现数据交换
                saveToSDCard(data); // 保存图片到sd卡中
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//                camera.startPreview(); // 拍完照后，重新开始预览

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveToSDCard(byte[] data) throws IOException {
        Log.d(TAG, "saveToSDCard");
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String filename = format.format(date) + ".jpg";
        File fileFolder = new File(Environment.getExternalStorageDirectory() + "/ansen/");// Environment.getRootDirectory()
        if (!fileFolder.exists()) {
            fileFolder.mkdir();
        }
        File jpgFile = new File(fileFolder, filename);
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
        outputStream.write(data);
        outputStream.close();
        mCamera.startPreview(); // 拍完照后，重新开始预览
        if (false) {
            Bitmap b = byteToBitmap(data);
            // 获取手机屏幕的宽高
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            int windowWidth = windowManager.getDefaultDisplay().getWidth();
            int windowHight = windowManager.getDefaultDisplay().getHeight();
            Bitmap bitmap = Bitmap.createBitmap(b, 0, 0, windowWidth, windowHight);
            // 图片压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();

        }
    }

    /**
     * 把图片byte流转换成bitmap
     */
    private Bitmap byteToBitmap(byte[] data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int i = 0;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                break;
            }
            i += 1;
        }
        return b;
    }

    /**
     * 显示surfaceView 数据的接口
     */
    private class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Log.d(TAG, "surfaceChanged");
            mParameters = mCamera.getParameters(); // 获取各项参数
            mParameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            mParameters.setPreviewSize(width, height); // 设置预览大小
            mParameters.setPreviewFrameRate(5);  //设置每秒显示4帧
            mParameters.setPictureSize(width, height); // 设置保存的图片尺寸
            mParameters.setJpegQuality(80); // 设置照片质量
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "surfaceCreated");
            mHolder = holder;// SurfaceHolder是系统提供用来设置surfaceView的对象
            try {
                mCamera = Camera.open(); // 打开摄像头
                mCamera.setPreviewDisplay(holder); //通过surfaceview显示取景画面
                mCamera.setDisplayOrientation(getPreviewDegree(CameraActivity.this));// 设置相机的方向
                mCamera.startPreview(); // 开始预览
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "surfaceCreated >>  Exception: " + e.getMessage());
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed");
            if (mCamera != null) {
                mCamera.release(); // 释放照相机
                mCamera = null;
            }
        }
    }

    /**
     * 点击手机屏幕是，显示两个按钮
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRelativeLayout.setVisibility(ViewGroup.VISIBLE); // 设置视图可见
                break;
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_CAMERA:
                if (mCamera != null && event.getRepeatCount() == 0) {
                    // 获取到拍照的图片数据后回调PictureCallback,PictureCallback可以对相片进行保存或传入网络
                    mCamera.takePicture(null, null, new MyPictureCallback());
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    // 用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        int degree = 0;
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Log.d(TAG, "rotation : " + rotation);
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }
}