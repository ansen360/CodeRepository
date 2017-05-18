package com.code.socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.code.R;
import com.code.common.Logger;
import com.code.common.NetworkUtils;
import com.code.common.ThreadPoolManager;
import com.code.common.ToastUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Ansen on 2017/4/8 11:35.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.ansen.socket
 * @Description: TODO
 */
public class TCPClient extends Activity implements View.OnClickListener {
    private static final String TAG = "ansen";
    private EditText mEditText;
    private Button mSend, mOpen, mClose;
    private Socket mSocket;
    private String mIPAddress;
    private static final int mPort = 12346;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);
        mEditText = (EditText) findViewById(R.id.et_socket);
        mSend = (Button) findViewById(R.id.socket_send);
        mOpen = (Button) findViewById(R.id.open_server);
        mClose = (Button) findViewById(R.id.close_server);
        mSend.setOnClickListener(this);
        mOpen.setOnClickListener(this);
        mClose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_server:
                startService(new Intent(this, TCPServer.class));

                break;
            case R.id.close_server:
                stopService(new Intent(this, TCPServer.class));
                break;
            case R.id.socket_send:
                // 在wifi状态下获取手机IP地址
                if (NetworkUtils.NETWORK_WIFI == NetworkUtils.getNetWorkType(this)) {
                    mIPAddress = NetworkUtils.getIpAddress(this);
                    Logger.d(TAG, "ip: " + mIPAddress);

                    ThreadPoolManager.getThreadPool().submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (mSocket == null)
                                    // 1.创建发送端Socket对象
                                    mSocket = new Socket(mIPAddress, mPort);
                                // 2.获取输出流,写数据
                                OutputStream os = mSocket.getOutputStream();
                                PrintWriter pw = new PrintWriter(os, true);
                                String text = mEditText.getText().toString();
                                pw.println(text);

                                // 获取服务器的反馈
                                BufferedReader brServer = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                                String s = brServer.readLine();//阻塞
                                ToastUtils.show("response: " + s);
                                Logger.d(TAG, "response: " + s);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 4.释放资源
        if (mSocket != null) {
            try {
                mSocket.close();
                mSocket=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
