package com.tomorrow_p.setupwizard;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.tomorrow_p.R;

public class OpenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getActionBar().hide();
        setContentView(R.layout.setupwizard_activity_open);
        Button user = (Button) findViewById(R.id.btn_user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                finish();
            }
        });
    }

    private void init() {
        // Add a persistent setting to allow other apps to know the device has been provisioned.
        Settings.Global.putInt(getContentResolver(), Settings.Global.DEVICE_PROVISIONED, 1);
//        Settings.Secure.putInt(getContentResolver(), Settings.Secure.USER_SETUP_COMPLETE, 1);

        // remove this activity from the package manager.
        PackageManager pm = getPackageManager();
        ComponentName name = new ComponentName(this, DefaultActivity.class);
        pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
