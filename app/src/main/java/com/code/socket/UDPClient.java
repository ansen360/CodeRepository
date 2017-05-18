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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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
public class UDPClient extends Activity implements View.OnClickListener {
    private static final String TAG = "ansen";
    private EditText mEditText;
    private Button mSend, mOpen, mClose;
    private DatagramSocket mDatagramSocket;
    private String mIPAddress;
    private static final int mPort = 12345;

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
                startService(new Intent(this, UDPServer.class));

                break;
            case R.id.close_server:
                stopService(new Intent(this, UDPServer.class));
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
                                // 1.创建发送端Socket对象
                                if (mDatagramSocket == null)
                                    mDatagramSocket = new DatagramSocket();
                                // 2.创建数据并打包
                                byte[] bytes = mEditText.getText().toString().getBytes();
                                DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(mIPAddress), mPort);

                                // 3.发送数据
                                mDatagramSocket.send(dp);
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
        if (mDatagramSocket != null) {
            mDatagramSocket.close();
        }
    }
}
