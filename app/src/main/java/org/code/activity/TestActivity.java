package org.code.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import org.code.R;
import org.code.common.ImageUtils;
import org.code.common.ScrollableViewUtil;
import org.code.common.ToastUtils;

/**
 * Created by Ansen on 2017/3/23 11:43.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.activity
 * @Description: TODO
 */
public class TestActivity extends Activity {

    ScrollView mScrollView;
    ImageView testImage;
    int a = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        testImage = (ImageView) findViewById(R.id.testImage);

        findViewById(R.id.scroll_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollableViewUtil scrollableView = new ScrollableViewUtil();
                scrollableView.start(mScrollView, testImage, new ScrollableViewUtil.OnScrollFinishedListener() {
                    @Override
                    public void onFinish(Bitmap bitmap) {
                        ImageUtils.saveImage(TestActivity.this, bitmap, "4444_" + a + ".png");
                        a++;
                    }
                });
                ToastUtils.show("sss");
            }
        });
    }

}
