package org.code.gesture;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.code.R;

import java.util.ArrayList;
import java.util.List;

public class GestureSleepPreference extends GestureBasePreference implements
        GestureSwitchPreference.OnGestureClickListener, GestureSwitchPreference.OnGestureCheckedChangedListener {

    private final String KEY_GESTURE_SLEEP = "gestures_sleep";
    private PreferenceScreen mPreferenceScreen;
    private GestureSwitchPreference mSleepUpGesture, mSleepDownGesture, mSleepLeftGesture,
            mSleepRightGesture, mSleepVGesture, mSleepMGesture, mSleepWGesture, mSleepDGesture;
    private String summary;
    private Switch mSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        initView();
    }

    private void initActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            mSwitch = new Switch(mActivity);
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
//                    Settings.System.putInt(mContentResolver, KEY_GESTURE_SLEEP, isChecked ? 1 : 0);
                    final Intent i = new Intent("com.qucii.openclose.sleepgesture");
                    i.putExtra("openClose", isChecked);
                    getActivity().sendBroadcast(i);
                    mPreferenceScreen.setEnabled(isChecked);
                }
            });
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.END);
            layoutParams.setMargins(0, 0, 70, 0);
            actionBar.setCustomView(mSwitch, layoutParams);
        }

    }

    private void initView() {
        addPreferencesFromResource(R.xml.gesture_sleep_settings);
        mPreferenceScreen = (PreferenceScreen) findPreference("main_preference_key");
        mSleepUpGesture = (GestureSwitchPreference) findPreference("sleep_up_gesture");
        mSleepDownGesture = (GestureSwitchPreference) findPreference("sleep_down_gesture");
        mSleepLeftGesture = (GestureSwitchPreference) findPreference("sleep_left_gesture");
        mSleepRightGesture = (GestureSwitchPreference) findPreference("sleep_right_gesture");
        mSleepVGesture = (GestureSwitchPreference) findPreference("sleep_v_gesture");
        mSleepMGesture = (GestureSwitchPreference) findPreference("sleep_m_gesture");
        mSleepWGesture = (GestureSwitchPreference) findPreference("sleep_w_gesture");
        mSleepDGesture = (GestureSwitchPreference) findPreference("sleep_d_gesture");

        mSleepUpGesture.setOnGestureClickListener(this);
        mSleepDownGesture.setOnGestureClickListener(this);
        mSleepLeftGesture.setOnGestureClickListener(this);
        mSleepRightGesture.setOnGestureClickListener(this);
        mSleepVGesture.setOnGestureClickListener(this);
        mSleepMGesture.setOnGestureClickListener(this);
        mSleepWGesture.setOnGestureClickListener(this);
        mSleepDGesture.setOnGestureClickListener(this);
        mSleepUpGesture.setOnGestureClickListener(this);

        mSleepUpGesture.setOnGestureCheckedChangedListener(this);
        mSleepDownGesture.setOnGestureCheckedChangedListener(this);
        mSleepLeftGesture.setOnGestureCheckedChangedListener(this);
        mSleepRightGesture.setOnGestureCheckedChangedListener(this);
        mSleepVGesture.setOnGestureCheckedChangedListener(this);
        mSleepMGesture.setOnGestureCheckedChangedListener(this);
        mSleepWGesture.setOnGestureCheckedChangedListener(this);
        mSleepDGesture.setOnGestureCheckedChangedListener(this);
        mSleepUpGesture.setOnGestureCheckedChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle(R.string.gesture_sleep);
        boolean isCheck = Settings.System.getInt(mContentResolver, KEY_GESTURE_SLEEP, 0) == 1;
        mPreferenceScreen.setEnabled(isCheck);
        if (mSwitch != null)
            mSwitch.setChecked(isCheck);
        initSummary();
    }

    @Override
    public void onClick(View view, String key) {
        Bundle bundle = new Bundle();
        bundle.putString("gesture", key);
        GestureSelectPreference selectFragment = new GestureSelectPreference();
        selectFragment.setArguments(bundle);
        //Need to addToBackStack, then we can go back here after GestureSlectPreference and AppListPreference removed.
        getFragmentManager().beginTransaction().replace
                (android.R.id.content, selectFragment).addToBackStack("GESTURE1").commit();
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
        mSleepVGesture.setSummary(getSummary("sleep_v_gesture"));
        mSleepMGesture.setSummary(getSummary("sleep_m_gesture"));
        mSleepWGesture.setSummary(getSummary("sleep_w_gesture"));
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
                boolean isInstalled = isAppInstalled(packageManager, packageName);
                if (!isInstalled) {
                    summary = getString(R.string.app_is_not_installed);
                    return summary;
                }
                summary = packageManager.getActivityInfo(new ComponentName(packageName, className), 0)
                        .loadLabel(packageManager).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return summary;
    }

    private boolean isAppInstalled(PackageManager packageManager, String packageName) {
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pname = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                pname.add(pinfo.get(i).packageName);
            }
        }
        return pname.contains(packageName);
    }
}
