package org.code.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Ansen on 2017/5/22 14:10.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.code.view
 * @Description: TODO
 */
public class RotateImageView extends AppCompatImageView {

    private float mDirection;
    private float mTargetDirection;
    private final float MAX_ROATE_DEGREE = 1.0f;
    private Drawable mImage;
    private AccelerateInterpolator mInterpolator;
    private SensorManager mSensorManager;
    private Sensor mOrientationSensor;

    public RotateImageView(Context context) {
        this(context, null);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        mInterpolator = new AccelerateInterpolator();
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        mSensorManager.registerListener(mOrientationListener, mOrientationSensor, SensorManager.SENSOR_DELAY_GAME);
        this.postDelayed(mRunnable, 20);

        mImage = getDrawable();
        mImage.setBounds(0, 0, getWidth(), getHeight());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(mDirection, getWidth() / 2, getHeight() / 2);
        mImage.draw(canvas);
        canvas.restore();
    }

    private void updateDirection() {
        invalidate();
    }

    protected Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mDirection != mTargetDirection) {

                // calculate the rotate orientation
                float to = mTargetDirection;
                if (to - mDirection > 180) {    //逆时针
                    to -= 360;
                } else if (to - mDirection < -180) {
                    to += 360;
                }

                // limit the max speed to MAX_ROTATE_DEGREE
                float distance = to - mDirection;
                mDirection = normalizeDegree(mDirection + (distance * mInterpolator.getInterpolation(0.3f)));
                updateDirection();
            }

            postDelayed(mRunnable, 20);
        }
    };
    private SensorEventListener mOrientationListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float value0 = event.values[0];  //方位,手机绕Z抽旋转的角度;0表示北
            float value1 = event.values[1];  //倾斜度,手机倾斜的程度
            float value2 = event.values[2];  //手机沿着Y轴的滚动角度
            float direction = value0 * -1.0f;
            mTargetDirection = normalizeDegree(direction);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.GONE) {
            mSensorManager.unregisterListener(mOrientationListener);
        }
    }
}
