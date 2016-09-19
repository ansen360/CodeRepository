package com.tomorrow_p.gesture;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.tomorrow_p.R;

public class GestureSleepPreference extends GestureBasePreference implements
        GestureSwitchPreference.OnGestureClickListener, GestureSwitchPreference.OnGestureCheckedChangedListener {
	
	private final String KEY_GESTURE_SLEEP = "gestures_sleep";
	private PreferenceScreen mPreferenceScreen;
    private GestureSwitchPreference mSleepUpGesture,mSleepDownGesture,mSleepLeftGesture,
    		mSleepRightGesture,mSleepEGesture,mSleepMGesture,mSleepCGesture,mSleepDGesture;
    private ContentResolver mContentResolver;
    private String summary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentResolver = getActivity().getContentResolver();
        initView();
    }
    
    @Override
	public void onResume() {
    	super.onResume();
		getActivity().setTitle(getActivity().getResources().getString(R.string.gesture_sleep));
		boolean isCheck = Settings.System.getInt(mContentResolver, KEY_GESTURE_SLEEP, 0) == 1;
        mPreferenceScreen.setEnabled(isCheck);
        setVisible(true);
        if (mSwitch != null) {
            mSwitch.setChecked(isCheck);
            mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			@Override
    			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
    				Settings.System.putInt(mContentResolver, KEY_GESTURE_SLEEP, isChecked ? 1 : 0);
				    final Intent i = new Intent("com.qucii.openclose.sleepgesture");
    			    i.putExtra("openClose", isChecked);
				    getActivity().sendBroadcast(i);
    				mPreferenceScreen.setEnabled(isChecked);
    			}
    		});
		}
		initSummary();
	}

    private void initView() {
        addPreferencesFromResource(R.xml.gesture_sleep_settings);
        mPreferenceScreen = (PreferenceScreen) findPreference("main_preference_key");
        mSleepUpGesture = (GestureSwitchPreference) findPreference("sleep_up_gesture");
        mSleepDownGesture = (GestureSwitchPreference) findPreference("sleep_down_gesture");
        mSleepLeftGesture = (GestureSwitchPreference) findPreference("sleep_left_gesture");
        mSleepRightGesture = (GestureSwitchPreference) findPreference("sleep_right_gesture");
        mSleepEGesture = (GestureSwitchPreference) findPreference("sleep_e_gesture");
        mSleepMGesture = (GestureSwitchPreference) findPreference("sleep_m_gesture");
        mSleepCGesture = (GestureSwitchPreference) findPreference("sleep_c_gesture");
        mSleepDGesture = (GestureSwitchPreference) findPreference("sleep_d_gesture");

        mSleepUpGesture.setOnGestureClickListener(this);
        mSleepDownGesture.setOnGestureClickListener(this);
        mSleepLeftGesture.setOnGestureClickListener(this);
        mSleepRightGesture.setOnGestureClickListener(this);
        mSleepEGesture.setOnGestureClickListener(this);
        mSleepMGesture.setOnGestureClickListener(this);
        mSleepCGesture.setOnGestureClickListener(this);
        mSleepDGesture.setOnGestureClickListener(this);
        mSleepUpGesture.setOnGestureClickListener(this);
        
        mSleepUpGesture.setOnGestureCheckedChangedListener(this);
        mSleepDownGesture.setOnGestureCheckedChangedListener(this);
        mSleepLeftGesture.setOnGestureCheckedChangedListener(this);
        mSleepRightGesture.setOnGestureCheckedChangedListener(this);
        mSleepEGesture.setOnGestureCheckedChangedListener(this);
        mSleepMGesture.setOnGestureCheckedChangedListener(this);
        mSleepCGesture.setOnGestureCheckedChangedListener(this);
        mSleepDGesture.setOnGestureCheckedChangedListener(this);
        mSleepUpGesture.setOnGestureCheckedChangedListener(this);
    }
  
    @Override
	public void onClick(View view, String key) {
		Bundle bundle = new Bundle();
        bundle.putString("gesture", key);
        GestureSelectPreference selectFragment = new GestureSelectPreference();
        selectFragment.setArguments(bundle);
        //Need to addToBackStack, then we can go back here after GestureSlectPreference and AppListPreference removed.
        getFragmentManager().beginTransaction().replace
        		(android.R.id.content, selectFragment).addToBackStack(null).commit();
	}

    @Override
	public void OnChecked(String key, boolean isChecked) {
		ContentValues values = new ContentValues();
		if (isChecked) {
			values.put("onoff", Integer.valueOf("1"));
		} else {
			values.put("onoff", Integer.valueOf("0"));
		}
		Utils.updateByGesture(mContentResolver, values, key);
	}
	
    public void initSummary() {
        mSleepUpGesture.setSummary(getSummary("sleep_up_gesture"));
        mSleepDownGesture.setSummary(getSummary("sleep_down_gesture"));
        mSleepLeftGesture.setSummary(getSummary("sleep_left_gesture"));
        mSleepRightGesture.setSummary(getSummary("sleep_right_gesture"));
        mSleepEGesture.setSummary(getSummary("sleep_e_gesture"));
        mSleepMGesture.setSummary(getSummary("sleep_m_gesture"));
        mSleepCGesture.setSummary(getSummary("sleep_c_gesture"));
        mSleepDGesture.setSummary(getSummary("sleep_d_gesture"));
    }

    private String getSummary(String prekey) {
        String[] values = Utils.qureyByGesture(mContentResolver, prekey);
        String packageName = values[0];
        String className = values[1];
        try {
            if (packageName.equals("awake")) {
                summary = getString(R.string.sleep_awake);
            } else if (packageName.equals("unlock")) {
                summary = getString(R.string.sleep_unlock_lock_screen);
            } else {
                PackageManager packageManager = getActivity().getPackageManager();
                summary = packageManager.getActivityInfo(new ComponentName(packageName, className), 0)
                		.loadLabel(packageManager).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return summary;
    }
}
