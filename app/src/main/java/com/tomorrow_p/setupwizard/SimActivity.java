package com.tomorrow_p.setupwizard;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tomorrow_p.R;


public class SimActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SimActivity";
    protected ActionBar mActionBar;
    protected static final String SHOW_SKIP = "show_skip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setupwizard_activity_sim);
        initActionBar();
        Button mContinue = (Button) findViewById(R.id.btn_continue);
        Button mShutdown = (Button) findViewById(R.id.btn_shutdown);
        mShutdown.setOnClickListener(this);
        mContinue.setOnClickListener(this);
    }

    private void initActionBar() {
        mActionBar = getActionBar();
        mActionBar.setElevation(0);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.setupwizard_actionbar, null);
        TextView actionBarTitle = (TextView) customView.findViewById(R.id.title);
        customView.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startActivity = new Intent();
                startActivity.setComponent(new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSetupActivity"));
                startActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startActivity);
                finish();
            }
        });
        actionBarTitle.setText(getResources().getString(R.string.setupwizard_check_sim));
        mActionBar.setCustomView(customView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_continue:
                try {
                    Intent startActivity = new Intent("com.qucii.usercenter.register.RegCheckMobileActivity");
                    startActivity.putExtra(SHOW_SKIP, true);
                    startActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startActivity);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    startActivity(new Intent(this, OpenActivity.class));
                }
                finish();
                break;
            case R.id.btn_shutdown:
                try {
                    Intent shutdown = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
                    shutdown.putExtra("android.intent.action.EXTRA_KEY_CONFIRM", false);
                    shutdown.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(shutdown);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
