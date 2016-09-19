package com.tomorrow_p.gesture;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.SwitchPreference;

import com.tomorrow_p.R;

public class GesturePreference extends GestureBasePreference {
    private Preference mSleepGesture;

    private SwitchPreference mSwitchFM;
    private SwitchPreference mSwitchPhoto, mSwitchMusic;
    private SwitchPreference mAdjustVolume, mDblclickSleep, mScreenshot;
    private SwitchPreference mFlipMute;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        init();
    }

	private void init() {
        addPreferencesFromResource(R.xml.gesture_settings);
        mSleepGesture = (Preference) findPreference("gestures_sleep");
        mSleepGesture.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				getActivity().getFragmentManager().beginTransaction().replace(android.R.id.content, 
						new GestureSleepPreference()).addToBackStack(null).commit();
				return false;
			}
		});
        
        /**
         * Key air_switch_fm, air_switch_photo, air_switch_music etc
         * is the key save in Settings.System, see GestureBasePreference
         */
        //Air control
        //mSwitchFM = ((SwitchPreference) findPreference("air_switch_fm"));
        mSwitchPhoto = ((SwitchPreference) findPreference("air_switch_photo"));
        mSwitchMusic = ((SwitchPreference) findPreference("air_switch_music"));
        //mSwitchFM.setOnPreferenceChangeListener(this);
        mSwitchPhoto.setOnPreferenceChangeListener(this);
        mSwitchMusic.setOnPreferenceChangeListener(this);

        //Quick Settings
        mScreenshot = ((SwitchPreference) findPreference("three_finger_screenshot"));
        mAdjustVolume = ((SwitchPreference) findPreference("two_finger_adjust_volume"));
        mDblclickSleep = ((SwitchPreference) findPreference("dblclick_gotosleep"));
        mScreenshot.setOnPreferenceChangeListener(this);
        mAdjustVolume.setOnPreferenceChangeListener(this);
        mDblclickSleep.setOnPreferenceChangeListener(this);

        //Smart phone
        mFlipMute = ((SwitchPreference) findPreference("flip_mute"));
        mFlipMute.setOnPreferenceChangeListener(this);
    }
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().setTitle(getActivity().getResources().getString(R.string.smart_induction));
		setVisible(false);
	}
}
