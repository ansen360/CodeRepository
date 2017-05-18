package com.code.setupwizard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.code.R;

import java.lang.ref.WeakReference;

/**
 * Created by Ansen on 2017/3/18 15:23.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.setupwizard
 * @Description: 透明的activity
 */
public class GuideActivity extends Activity implements Animation.AnimationListener {

    private ImageView iv_text1, iv_hand, iv_guide_press, handS, handS2, mArrow;
    private RelativeLayout mRLOne, mRLTwo;
    private FrameLayout mFLPoint;
    private AnimationHandler mHandler;
    public static final int ANIMATION_ONE = 0;
    public static final int ANIMATION_TWO = 1;
    public static final int ANIMATION_THREE = 2;
    public static final int ANIMATION_FOUR = 3;
    public static final int ANIMATION_FIVE = 4;
    public static final int ANIMATION_SIX = 5;
    private int mAnimation = ANIMATION_ONE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_guide);
        iv_hand = (ImageView) findViewById(R.id.iv_hand);
        iv_text1 = (ImageView) findViewById(R.id.iv_text1);
        iv_guide_press = (ImageView) findViewById(R.id.iv_guide_press);
        handS = (ImageView) findViewById(R.id.iv_hand_s);
        handS2 = (ImageView) findViewById(R.id.iv_hand_s2);
        mArrow = (ImageView) findViewById(R.id.iv_arrow);
        mRLOne = (RelativeLayout) findViewById(R.id.rl_one);
        mFLPoint = (FrameLayout) findViewById(R.id.fl_point);
        mRLTwo = (RelativeLayout) findViewById(R.id.rl_two);
        mHandler = new AnimationHandler(this);
        animationOne();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (mAnimation) {
                case ANIMATION_ONE:
                    hideOne();
                    animationTwo();
                    mAnimation = ANIMATION_TWO;
                    break;
                case ANIMATION_TWO:
                    animationThree();
                    mAnimation = ANIMATION_THREE;
                    break;
                case ANIMATION_THREE:
                    mAnimation = ANIMATION_FOUR;
                    mHandler.removeCallbacks(mRunnable1);
                    animationFour();
                    break;
                case ANIMATION_FOUR:
                    mAnimation = ANIMATION_FIVE;
                    mHandler.removeCallbacks(mRunnable2);
                    iv_guide_press.clearAnimation();
                    animationFive();
                    break;
                case ANIMATION_FIVE:
                    mAnimation = ANIMATION_SIX;
                    mHandler.removeCallbacks(mRunnable3);
                    hideTwo();
                    animationEnd();
                    break;
                case ANIMATION_SIX:
                    finish();
                    break;

            }
        }

        return super.onTouchEvent(event);
    }

    private void hideOne() {
        mRLOne.setVisibility(View.GONE);
        mFLPoint.setVisibility(View.VISIBLE);
        iv_hand.setVisibility(View.VISIBLE);
        iv_text1.setVisibility(View.VISIBLE);
        handS.clearAnimation();
    }

    private void hideTwo() {
        mFLPoint.setVisibility(View.GONE);
        iv_text1.setVisibility(View.GONE);
        iv_hand.clearAnimation();
        iv_hand.setVisibility(View.INVISIBLE);
        mRLTwo.setVisibility(View.VISIBLE);
    }

    private void animationOne() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -110);
        translateAnimation.setDuration(2000);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setFillAfter(true);
        handS.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(this);

    }

    private void animationTwo() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, -110, 0, 0);
        translateAnimation.setDuration(2000);
        translateAnimation.setFillAfter(true);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        iv_hand.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(this);
    }

    private void animationThree() {
        iv_text1.setImageResource(R.mipmap.ic_guide_text2);
        animationTwo();
    }

    private void animationFour() {
        iv_text1.setImageResource(R.mipmap.ic_guide_text3);
        animationTwo();
    }

    private void animationFive() {
        iv_text1.setImageResource(R.mipmap.ic_guide_text4);
        animationTwo();
    }

    private void animationEnd() {

        AlphaAnimation animationAphaP = new AlphaAnimation(0, 1);
        animationAphaP.setDuration(800);
        animationAphaP.setFillAfter(true);
        mArrow.startAnimation(animationAphaP);
        animationAphaP.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        switch (mAnimation) {
            case ANIMATION_FOUR:
                Log.d("ansen", "ANIMATION_FOUR");
                mHandler.sendEmptyMessageDelayed(ANIMATION_FOUR, 1200);
                break;
            case ANIMATION_FIVE:
                Log.d("ansen", "ANIMATION_FOUR");
                mHandler.sendEmptyMessageDelayed(ANIMATION_FOUR, 1200);
                break;

        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        switch (mAnimation) {
            case ANIMATION_ONE:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationOne();
                        Log.d("ansen", "animationOne");
                    }
                }, 1500);
                break;
            case ANIMATION_TWO:
                Log.d("ansen", "ANIMATION_TWO");
                animationTwo();

                break;
            case ANIMATION_THREE:
                Log.d("ansen", "ANIMATION_THREE");
                mHandler.postDelayed(mRunnable1, 1500);
                break;
            case ANIMATION_FOUR:
                Log.d("ansen", "ANIMATION_FOUR");
                mHandler.postDelayed(mRunnable2, 1500);
                break;
            case ANIMATION_FIVE:
                mHandler.postDelayed(mRunnable3, 2500);
                break;
            case ANIMATION_SIX:
                mHandler.sendEmptyMessage(ANIMATION_FIVE);
                Log.d("ansen", "ANIMATION_FOUR");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationEnd();
                    }
                }, 2000);
                break;

        }

    }
    private Runnable mRunnable1=new Runnable() {
        @Override
        public void run() {
            animationThree();
        }
    };
    private Runnable mRunnable2=new Runnable() {
        @Override
        public void run() {
            iv_guide_press.clearAnimation();
            animationFour();
        }
    };
    private Runnable mRunnable3=new Runnable() {
        @Override
        public void run() {
            iv_guide_press.clearAnimation();
            animationFive();
        }
    };

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    private static class AnimationHandler extends Handler {
        private WeakReference<GuideActivity> mActivity;

        public AnimationHandler(GuideActivity context) {
            mActivity = new WeakReference<GuideActivity>(context);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity.get() == null) {
                return;
            }
            GuideActivity activity = mActivity.get();
            switch (msg.what) {
                case ANIMATION_FOUR:
                    AlphaAnimation animationAphaP = new AlphaAnimation(0, 1);
                    animationAphaP.setDuration(800);
                    animationAphaP.setFillAfter(true);
                    activity.iv_guide_press.startAnimation(animationAphaP);
                    break;
                case ANIMATION_FIVE:
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -110);
                    translateAnimation.setDuration(2000);
                    translateAnimation.setFillAfter(true);
                    translateAnimation.setRepeatMode(Animation.REVERSE);
                    activity.handS2.startAnimation(translateAnimation);
                    break;
            }


        }
    }


}
