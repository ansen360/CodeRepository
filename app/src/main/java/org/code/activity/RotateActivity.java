package org.code.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.code.R;
import org.code.view.RotateImageView;

/**
 * Created by Ansen on 2017/5/22 15:22.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.code.activity
 * @Description: TODO
 */
public class RotateActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_rotate);
        RotateImageView image = (RotateImageView) findViewById(R.id.riv);

    }

}
