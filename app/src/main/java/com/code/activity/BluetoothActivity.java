package com.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.code.R;

/**
 * Created by Ansen on 2017/1/5 10:23.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.activity
 * @Description: TODO
 */
public class BluetoothActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Button client = (Button) findViewById(R.id.bluetooth_client);
        Button server = (Button) findViewById(R.id.bluetooth_server);
        client.setOnClickListener(this);
        server.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth_client:
                startActivity(new Intent(this, BluetoothClientActivity.class));
                break;
            case R.id.bluetooth_server:
                startActivity(new Intent(this, BluetoothServerActivity.class));
                break;
        }
    }
}
