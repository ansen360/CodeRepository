package com.tomorrow_p.gesture;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;

import com.tomorrow_p.R;

public class MainActivity extends Activity {
    public ContentResolver mContentResolver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentResolver = getContentResolver();
        initDatabase();
        getFragmentManager().beginTransaction().replace(android.R.id.content, new GesturePreference()).commit();
    }
    
    public void initDatabase() {
    	// Is run first time, if true, init default database, 1 for true 0 for false.
        boolean firstRun = Utils.getConfig(this, Utils.IS_FIRST_RUN_KEY, true);
        if (firstRun) {
            Resources resources = getResources();
            String[] gestures = resources.getStringArray(R.array.gestures);
            String[] packages = resources.getStringArray(R.array.gesture_packages);
            String[] classes = resources.getStringArray(R.array.gesture_classes);
            for (int i = 0; i < packages.length; i++) {
                Utils.insert(mContentResolver, gestures[i], packages[i], classes[i], 0);
            }
            Utils.setConfig(this, Utils.IS_FIRST_RUN_KEY, false);
        }
    }

    public Fragment getVisibleFragment() {
        Fragment fragment = getFragmentManager().findFragmentById(android.R.id.content);
        return fragment;
    }
    
    @Override
    public void onBackPressed() {
    	if (getVisibleFragment() instanceof AppListPreference) {
    		((AppListPreference) getVisibleFragment()).backPressed();
		}
    	super.onBackPressed();
    }
}