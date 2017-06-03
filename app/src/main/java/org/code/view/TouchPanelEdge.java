/*
 * Copyright (c) 2011-2014, Qualcomm Technologies, Inc. All Rights Reserved.
 * Qualcomm Technologies Proprietary and Confidential.
 */
package org.code.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class TouchPanelEdge extends Activity {

    private static final String TAG = "TouchPanelEdge";
    private ArrayList<EdgePoint> mArrayList;
    private int mHightPix = 0, mWidthPix = 0, mRadius = 20, mStep = 0;
    private float mCurrenX = 0, mCurrenY = 0;
    // If points is too more, it will be hard to touch edge points.
    private final int X_MAX_POINTS = 16;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // get panel size
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mHightPix = mDisplayMetrics.heightPixels;
        mWidthPix = mDisplayMetrics.widthPixels;
        // It must be common divisor of width and hight
        mStep = getStep(mWidthPix, mHightPix);
        mRadius = mStep / 2;
        Log.d(TAG, "mRadius: " + mRadius);
        setContentView(new Panel(this));
    }


    private int getStep(int hightPix, int widthPix) {
        int MIN_STEP = widthPix / X_MAX_POINTS;
        int step = MIN_STEP;
        for (int i = MIN_STEP; i < widthPix / 5; i++) {
            if (hightPix % i == 0 && widthPix % i == 0)
                return i;
        }
        return step;
    }

    public ArrayList<EdgePoint> initPoint() {
        ArrayList<EdgePoint> list = new ArrayList<EdgePoint>();
        for (int x = mRadius; x < mWidthPix + mRadius; x += mStep) {    // X轴从第一个球半径开始,累积增加一个球的直径,由于是从半径初始化,判断条件需要加上球的半径
            for (int y = mRadius; y < mHightPix + mRadius + mRadius; y += mStep) {  // Y轴同上,判断条件需要再加上一个球的半径,因为NavigateBar原因
                if (x > mRadius && x < mWidthPix - mRadius && y > mRadius && y < mHightPix)  // X轴处于第一个球和最后一个球的中间,Y轴同理,满足这两个条件就continue
                    continue;
                list.add(new EdgePoint(x, y, false));
            }
        }
        return list;
    }


    public List getSlashPoint() {
        ArrayList<EdgePoint> list = new ArrayList<EdgePoint>();
        int x = 0;
        int y = 0;
        mHightPix += mRadius;
        Double mSlash = Math.sqrt((double) mHightPix * mHightPix + (double) mWidthPix * mWidthPix);
        int mSlashint = mSlash.intValue();
        for (int z = mRadius; z < mSlashint + mRadius; z += mStep) {
            x = z / 2-90;
            y = (z * 5) / 6;
            list.add(new EdgePoint(x, y, false));
            getLeftSlashPoint(list,x, y);
        }
        return list;
    }

    private void getLeftSlashPoint(List list,int x, int y) {
        int x1 = 1080 - x;
        list.add(new EdgePoint(x1, y, false));
    }

    private List getCrossData() {
        ArrayList<EdgePoint> list = new ArrayList<>();
        for (int y = mRadius; y < mHightPix + mRadius; y += mStep) {
            list.add(new EdgePoint(mWidthPix / 2, y, false));
        }
        for (int x = mRadius; x < mWidthPix + mRadius; x += mStep) {
            if (x == mRadius || x == mWidthPix / 2 || x == mWidthPix - mRadius)
                continue;
            list.add(new EdgePoint(x, mHightPix / 2 + 60, false));
        }
        return list;
    }

    class Panel extends View {
        public static final int TOUCH_TRACE_NUM = 30;
        public static final int PRESSURE = 500;
        private TouchData[] mTouchData = new TouchData[TOUCH_TRACE_NUM];
        private int traceCounter = 0;
        private Paint mPaint = new Paint();

        public class TouchData {
            public float x;
            public float y;
            public float r;
        }

        public Panel(Context context) {
            super(context);
            mArrayList = initPoint();
            mArrayList.addAll(getCrossData());
            mPaint.setARGB(100, 100, 100, 100);
            for (int i = 0; i < TOUCH_TRACE_NUM; i++) {
                mTouchData[i] = new TouchData();
            }
        }

        private int getNext(int c) {
            int temp = c + 1;
            return temp < TOUCH_TRACE_NUM ? temp : 0;
        }

        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mPaint.setColor(Color.LTGRAY);
            mPaint.setTextSize(25);
            canvas.drawText("W: " + mCurrenX, mWidthPix / 2 - 20, mHightPix / 2 - 10, mPaint);
            canvas.drawText("H: " + mCurrenY, mWidthPix / 2 - 20, mHightPix / 2 + 10, mPaint);
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(mRadius);
            for (int i = 0; i < mArrayList.size(); i++) {
                EdgePoint point = mArrayList.get(i);
                canvas.drawCircle(point.x, point.y, mPaint.getStrokeWidth(), mPaint);
            }
            for (int i = 0; i < TOUCH_TRACE_NUM; i++) {
                TouchData td = mTouchData[i];
                mPaint.setColor(Color.BLUE);
                if (td.r > 0) {
                    canvas.drawCircle(td.x, td.y, 2, mPaint);
                }
            }
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            final int eventAction = event.getAction();
            mCurrenX = event.getRawX();
            mCurrenY = event.getRawY();
            if ((eventAction == MotionEvent.ACTION_MOVE) || (eventAction == MotionEvent.ACTION_UP)) {
                for (int i = 0; i < mArrayList.size(); i++) {
                    EdgePoint point = mArrayList.get(i);
                    if (!point.isChecked && ((mCurrenX >= (point.x - mRadius)) && (mCurrenX <= (point.x + mRadius))) && // 滑动X轴在处于球坐标的直径范围内
                            ((mCurrenY >= (point.y - mRadius)) && (mCurrenY <= (point.y + mRadius)))) {
                        mArrayList.remove(i);
                        break;
                    }
                }
                if (mArrayList.isEmpty()) {
//                    ((Activity) TouchPanelEdge.this).setResult(RESULT_OK);
                    finish();
                }
                TouchData tData = mTouchData[traceCounter];
                tData.x = event.getX();
                tData.y = event.getY();
                tData.r = event.getPressure() * PRESSURE;
                traceCounter = getNext(traceCounter);
                invalidate();
            }
            return true;
        }
    }

    public class EdgePoint {
        int x;
        int y;
        boolean isChecked = false;

        public EdgePoint(int x, int y, boolean isCheck) {
            this.x = x;
            this.y = y;
            this.isChecked = isCheck;
        }
    }
}
