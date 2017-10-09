package org.code.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import org.code.MainActivity;
import org.code.R;
import org.code.common.ToastUtils;
import org.code.service.ScreenshotService;

/**
 * Created by Ansen on 2017/9/14 17:15.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: org.code.activity
 * @Description: TODO
 */
public class ScreenshotActivity extends Activity implements View.OnClickListener {


    private Button startCut;
    private Button stopCut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);

        startCut = (Button) findViewById(R.id.start_cut);
        stopCut = (Button) findViewById(R.id.stop_cut);
        startCut.setOnClickListener(this);
        stopCut.setOnClickListener(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:

                if (resultCode == RESULT_OK && data != null) {

                    Intent intent = new Intent(this, ScreenshotService.class);
                    intent.putExtra("resultCode", resultCode);
                    intent.putExtra("data", data);
                    startService(intent);

                }

                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_cut:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(ScreenshotActivity.this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent);
                        return;
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //5.0 之后才允许使用屏幕截图

                    MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                    startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), 1); // 弹窗是否允许截取屏幕

                }
                break;
            case R.id.stop_cut:
                stopService(new Intent(this, ScreenshotService.class));
                break;


        }
    }
}
