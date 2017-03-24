package com.ansen.gesture;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.ansen.R;

import java.util.ArrayList;

public class AnimationActivity extends Activity {
    private static final String TAG = "zolen";
    private String mGesture;
    private String mPackageName;
    private String mClassName;
    private static final String LYT_SLEEP_UP = "lyt_sleep_up";
    private static final String LYT_SLEEP_DOWN = "lyt_sleep_down";
    private static final String LYT_SLEEP_LEFT = "lyt_sleep_left";
    private static final String LYT_SLEEP_RIGHT = "lyt_sleep_right";
    private static final String LYT_SLEEP_E = "lyt_sleep_e";
    private static final String LYT_SLEEP_M = "lyt_sleep_m";
    private static final String LYT_SLEEP_C = "lyt_sleep_c";
    private static final String LYT_SLEEP_W = "lyt_sleep_w";
    private static final String LYT_SLEEP_V = "lyt_sleep_v";
    private static final String LYT_SLEEP_O = "lyt_sleep_o";

    ArrayList<Integer> image;
    private ImageView mImageView;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.gesture_activity_animation);
        mImageView = (ImageView) findViewById(R.id.image);
        mGesture = getIntent().getExtras().getString("gesture");
        mPackageName = getIntent().getExtras().getString("packageName");
        mClassName = getIntent().getExtras().getString("className");
        Log.d(TAG, "mGesture: " + mGesture + " mPackageName: " + mPackageName + " mClassName: " + mClassName);
        startAnimation();
    }

    private void startAnimation() {
        TypedArray typedArray = null;
        if (LYT_SLEEP_UP.equals(mGesture)) {//lyt_sleep_up
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_up);
        } else if (LYT_SLEEP_DOWN.equals(mGesture)) {//lyt_sleep_down
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_down);
        } else if (LYT_SLEEP_LEFT.equals(mGesture)) {//lyt_sleep_left
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_left);
        } else if (LYT_SLEEP_RIGHT.equals(mGesture)) { //lyt_sleep_right
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_right);
        } else if (LYT_SLEEP_E.equals(mGesture)) {//lyt_sleep_e
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_e);
        } else if (LYT_SLEEP_M.equals(mGesture)) { //lyt_sleep_m
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_m);
        } else if (LYT_SLEEP_C.equals(mGesture)) {//lyt_sleep_c
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_c);
        } else if (LYT_SLEEP_W.equals(mGesture)) { //lyt_sleep_w
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_w);
        } else if (LYT_SLEEP_O.equals(mGesture)) {//lyt_sleep_o
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_o);
        } else if (LYT_SLEEP_V.equals(mGesture)) {//lyt_sleep_v
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_v);
        } else if (LYT_SLEEP_UP.equals(mGesture)) {
            typedArray = getResources().obtainTypedArray(R.array.gesture_lyt_sleep_d);
        }
        int[] icon = new int[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            icon[i] = typedArray.getResourceId(i, 0);
        }
        FrameAnimation frameAnimation = new FrameAnimation(mImageView, icon, 20);
        frameAnimation.setOnFinishListenner(new FrameAnimation.OnFinishListenner() {
            @Override
            public void onFinish() {
                finish();
                openApp();
            }
        });
    }

    private void openApp() {
        Intent intent = new Intent();
        //intent.setFlags(268435456);
        intent.setClassName(mPackageName, mClassName);
        startActivity(intent);
    }
}
