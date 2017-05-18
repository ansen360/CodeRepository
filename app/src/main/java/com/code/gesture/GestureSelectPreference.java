package com.code.gesture;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.preference.Preference;

import com.code.R;

public class GestureSelectPreference extends GestureBasePreference {
    private Preference mAwake;
    private Preference mOpenApp;
    private Preference mUnlockLockScreen;
    private String mGesture;
    private ContentResolver mContentResolver;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.gesture_select);

        mOpenApp = findPreference("sleep_open_app");
        mUnlockLockScreen = findPreference("sleep_unlock_lock_screen");
        mAwake = findPreference("sleep_awake");
        mOpenApp.setOnPreferenceClickListener(this);
        mUnlockLockScreen.setOnPreferenceClickListener(this);
        mAwake.setOnPreferenceClickListener(this);

        mContentResolver = getActivity().getContentResolver();
        mGesture = getArguments().getString("gesture");
    }
    
    @Override
	public void onResume() {
    	super.onResume();
        updateTitle(R.string.gesture_select_actions);
	}
    
    @Override
    public boolean onPreferenceClick(Preference paramPreference) {
        if (paramPreference == mOpenApp) {
            Bundle bundle = new Bundle();
            bundle.putString("gesture", mGesture);
            AppListPreference appListFragment = new AppListPreference();
            appListFragment.setArguments(bundle);
            //Do not need to addToBackStack cause we don't need to go back here.
            getFragmentManager().beginTransaction().replace(android.R.id.content,
                    appListFragment).addToBackStack("SELECT").commit();
        }
        else if (paramPreference == mUnlockLockScreen) {
            ContentValues values = new ContentValues();
            values.put("pakname", "unlock");
            Utils.updateByGesture(mContentResolver, values, mGesture);
            //Directly go back to GestureSleepPreference.
            getActivity().getFragmentManager().popBackStack();
        }
        else if (paramPreference == mAwake) {
            ContentValues values = new ContentValues();
            values.put("pakname", "awake");
            Utils.updateByGesture(mContentResolver, values, mGesture);

            getActivity().getFragmentManager().popBackStack();
        }
        return false;
    }
}
