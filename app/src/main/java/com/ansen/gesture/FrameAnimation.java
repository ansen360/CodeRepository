package com.ansen.gesture;

import android.widget.ImageView;

public class FrameAnimation {

	private OnFinishListenner mOnFinishListenner;
	private ImageView mImageView;
	private int[] mFrameRess;
	private int[] mDurations;
	private int mDuration;
	private int mDelayBreak;
	private int mLastFrame;

	public FrameAnimation(ImageView iv, int[] frameRess, int duration) {
		this.mImageView = iv;
		this.mFrameRess = frameRess;
		this.mDuration = duration;
		this.mLastFrame = frameRess.length - 1;
		play(0);
	}
	public FrameAnimation(ImageView iv, int[] frameRess, int[] durations) {
		this.mImageView = iv;
		this.mFrameRess = frameRess;
		this.mDurations = durations;
		this.mLastFrame = frameRess.length - 1;
		playByDurations(0);
	}

	public FrameAnimation(ImageView iv, int[] frameRess, int[] durations,
			int breakDelay) {
		this.mImageView = iv;
		this.mFrameRess = frameRess;
		this.mDurations = durations;
		this.mDelayBreak = breakDelay;
		this.mLastFrame = frameRess.length - 1;
		playAndDelayBreak(0);
	}

	private void playAndDelayBreak(final int i) {
		mImageView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mImageView.setBackgroundResource(mFrameRess[i]);
				if (i == mLastFrame) {
					playAndDelayBreak(0);
				} else {
					playAndDelayBreak(i + 1);
				}
			}
		}, i == mLastFrame && mDelayBreak > 0 ? mDelayBreak : mDurations[i]);

	}

	private void playByDurations(final int i) {
		mImageView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mImageView.setBackgroundResource(mFrameRess[i]);
				if (i == mLastFrame) {
					playByDurations(0);
				} else {
					playByDurations(i + 1);
				}
			}
		}, mDurations[i]);

	}
	private void play(final int i) {
		mImageView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mImageView.setBackgroundResource(mFrameRess[i]);
				if (i == mLastFrame) {
					if(mOnFinishListenner!=null){
						mOnFinishListenner.onFinish();
					}else {
						play(0);
					}
				} else {
					play(i + 1);
				}
			}
		}, mDuration);
	}
	public interface OnFinishListenner{
		public void onFinish();
	}
	public void setOnFinishListenner(OnFinishListenner l){
		this.mOnFinishListenner=l;
	}
}
