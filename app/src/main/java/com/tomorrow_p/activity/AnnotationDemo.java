package com.tomorrow_p.activity;

import android.app.Activity;

/**
 * Created by Ansen on 2017/3/8 18:24.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.annotation
 * @Description: TODO
 */
public class AnnotationDemo extends Activity{
    public @interface Name {
        String value() default "";
    }
}
