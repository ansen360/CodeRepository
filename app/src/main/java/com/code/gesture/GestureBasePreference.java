package com.code.gesture;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

public class GestureBasePreference extends PreferenceFragment
        implements OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    protected Activity mActivity;
    protected ContentResolver mContentResolver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContentResolver = getActivity().getContentResolver();

    }


    @Override
    public boolean onPreferenceChange(Preference arg0, Object arg1) {
        ContentResolver mContentResolver = mActivity.getContentResolver();
        boolean isChecked = ((Boolean) arg1).booleanValue();
        //Save the settings into system with key in xml
//        Settings.System.putInt(mContentResolver, arg0.getKey(), isChecked ? 1 : 0);
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public void updateTitle(int titleRes) {
        mActivity.setTitle(mActivity.getResources().getString(titleRes));
    }
}
