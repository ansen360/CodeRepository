package com.tomorrow_p.common;

import android.view.View;
import android.view.animation.AlphaAnimation;

public class AnimationUtil {
	public static void showAlphaAnimation(View v, long durationMillis) {
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		animation.setDuration(durationMillis);
		v.startAnimation(animation);
	}

	public static void hideAlphaAnimation(View v, long durationMillis) {
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		animation.setDuration(durationMillis);
		v.startAnimation(animation);
	}
}
