package com.ansen.activity;

import android.app.Activity;
import android.os.Bundle;

import com.ansen.common.ToastUtils;

/**
 * Created by Ansen on 2017/4/11 18:56.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.ansen.activity
 * @Description: TODO
 */
public class JNIActivity extends Activity {

    static {
        System.loadLibrary("hello");
    }

    public native String hello();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ToastUtils.show(hello() + " ");
    }

}