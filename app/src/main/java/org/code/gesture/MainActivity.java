package org.code.gesture;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MenuItem;

import org.code.R;

import java.io.IOException;

public class MainActivity extends Activity {
    public ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(1);
        }
//        initPermisson();
        mContentResolver = getContentResolver();
        initDatabase();
        getFragmentManager().beginTransaction().replace(android.R.id.content, new GesturePreference()).commit();
    }

    private void initPermisson() {
//        if (!Settings.System.canWrite(this)) {    SDK23
            Uri selfPackageUri = Uri.parse("package:"+ getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    selfPackageUri);
            startActivity(intent);
//        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}