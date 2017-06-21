package org.code;

import android.app.Application;

import org.code.common.ToastUtils;

/**
 * Created by Ansen on 2016/9/23 11:32.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p
 * @Description: TODO
 */
public class CodeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);

    }
}
