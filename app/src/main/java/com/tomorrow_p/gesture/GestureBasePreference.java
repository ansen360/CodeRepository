package com.tomorrow_p.gesture;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Switch;

public class GestureBasePreference extends PreferenceFragment
	implements OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
	public Switch mSwitch;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onResume() {
		mSwitch = new Switch(getActivity());
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, 
        		ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.END);
        layoutParams.setMargins(0, 0, 70, 0);
        actionBar.setCustomView(mSwitch, layoutParams);
		super.onResume();
	}
	
	public void setVisible(boolean visible) {
		if (mSwitch == null) {
			return;
		}
		if (visible) {
			mSwitch.setVisibility(View.VISIBLE);
		} else {
			mSwitch.setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onPreferenceChange(Preference arg0, Object arg1) {
		ContentResolver mContentResolver = getActivity().getContentResolver();
		boolean isChecked = ((Boolean) arg1).booleanValue();
		//Save the settings into system with key in xml
		Settings.System.putInt(mContentResolver, arg0.getKey(), isChecked ? 1 : 0);
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
