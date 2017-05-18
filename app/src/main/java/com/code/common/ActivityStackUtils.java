package com.code.common;

import android.app.Activity;

import java.util.Stack;

public class ActivityStackUtils {
	private static Stack<Activity> activityStack;
	private static ActivityStackUtils instance;

	private ActivityStackUtils() {
	}

	public static ActivityStackUtils getScreenManager() {
		if (instance == null) {
			instance = new ActivityStackUtils();
		}
		return instance;
	}

	public void popActivity() {
		Activity activity = activityStack.lastElement();
		if (activity != null) {
			activity.finish();
			activity = null;
		}
	}

	public void popActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	public Activity currentActivity() {
		if (!activityStack.empty()) {
			Activity activity = activityStack.lastElement();
			return activity;
		} else {
			return null;
		}
	}

	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	public void popAllActivityExceptOne(Class cls) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity);
		}
	}
}