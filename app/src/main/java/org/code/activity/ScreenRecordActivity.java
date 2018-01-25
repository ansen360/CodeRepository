package org.code.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import org.code.common.ToastUtils;
import org.code.service.ScreenRecordService;

/**
 * Created by Ansen on 2018/1/17 16:31.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: org.code.activity
 * @Description: TODO
 */
public class ScreenRecordActivity extends Activity {

    private final int REQUEST_CODE = 0x001;
    private int mDensityDpi, mWidthPixels, mHeightPixels;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
        }

        initData();
        Button button = new Button(this);
        setContentView(button);
        button.setText("启动录屏服务");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaProjectionManager manager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                Intent intent = new Intent(manager.createScreenCaptureIntent());
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void initData() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensityDpi = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
        mWidthPixels = dm.widthPixels;
        mHeightPixels = dm.heightPixels;
    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(ScreenRecordActivity.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivity(intent);
                    ToastUtils.show("勾选允许后,再次点击");
                    return;
                }
            }

            // 启动屏幕录制服务
            /*bindService(new Intent(this, ScreenRecordService.class), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    mMyBinder = (ScreenRecordService.MyBinder) iBinder;
                    mMyBinder.initData(resultCode, data, mDensityDpi, mWidthPixels, mHeightPixels);

                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {    // 正常情况下不被调用,当Service异外销毁时时调用
                    mMyBinder = null;
                }

            }, BIND_AUTO_CREATE);   // BIND_AUTO_CREATE: 如果服务不存在才创建*/

            Intent service = new Intent(this, ScreenRecordService.class);
            service.putExtra("resultCode", resultCode);
            service.putExtra("data", data);
            service.putExtra("mWidthPixels", mWidthPixels);
            service.putExtra("mHeightPixels", mHeightPixels);
            service.putExtra("mDensityDpi", mDensityDpi);
            startService(service);

        }
    }
}
