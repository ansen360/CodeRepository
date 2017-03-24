package com.ansen.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import com.ansen.R;

/**
 * Created by Ansen on 2015/07/18 10:23.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.activity
 * @Description: TODO
 */

public class CameraPreviewImageActivity extends Activity {
    private ImageView ivPic = null; // 显示图片控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview_image);
        ivPic = (ImageView) findViewById(R.id.imageView);
        setImageBitmap(getImageFormBundle());
    }

    public void setImageBitmap(byte[] bytes) {
        Bitmap cameraBitmap = byte2Bitmap();
        // 根据拍摄的方向旋转图像（纵向拍摄时要需要将图像选择90度)
        Matrix matrix = new Matrix();
        matrix.setRotate(CameraActivity.getPreviewDegree(this));
        cameraBitmap = Bitmap
                .createBitmap(cameraBitmap, 0, 0, cameraBitmap.getWidth(),
                        cameraBitmap.getHeight(), matrix, true);
        ivPic.setImageBitmap(cameraBitmap);
    }

    public byte[] getImageFormBundle() {
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        byte[] bytes = data.getByteArray("bytes");
        return bytes;
    }

    /**
     * 将字节数组的图形数据转换为Bitmap
     */
    private Bitmap byte2Bitmap() {
        byte[] data = getImageFormBundle();
        // 将byte数组转换成Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }
}