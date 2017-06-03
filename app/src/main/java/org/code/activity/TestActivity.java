package org.code.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.code.R;
import org.code.view.WheelView;

import java.util.ArrayList;

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

    WheelView mWheelView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mWheelView = (WheelView) findViewById(R.id.wheelView);
        mWheelView.setData(getData1());

    }

    @Override
    protected void onResume() {
        super.onResume();


    }
    private ArrayList<String> getData1() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            data.add(String.valueOf(((char) (97 + i))));
        }
        return data;
    }
}
