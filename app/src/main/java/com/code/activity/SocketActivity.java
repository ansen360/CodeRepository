package com.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Ansen on 2017/3/27 15:53.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.ansen.activity
 * @Description: TODO
 */
public class SocketActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        Button client = new Button(this);
        client.setText("client");
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocketActivity.this,SocketClientActivity.class));
            }
        });
        linearLayout.addView(client);
        Button server = new Button(this);
        server.setText("server");
        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(SocketActivity.this,SocketServerService.class));
            }
        });
        linearLayout.addView(server);
        setContentView(linearLayout);

    }
}
