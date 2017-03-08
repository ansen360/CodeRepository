package com.tomorrow_p.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tomorrow_p.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

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
public class BluetoothServerActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter;
    private StringBuilder mStringBuilder;
    private BluetoothSocket mBluetoothSocket;
    ;
    private TextView mLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlog);
        mLog = (TextView) findViewById(R.id.log);
        mStringBuilder = new StringBuilder();
        initBluetooth();
        ServerThread startServerThread = new ServerThread();
        startServerThread.start();

    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {    //判断是否有蓝牙
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) { //判断蓝牙 是否开启
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        }
        //使本机蓝牙在300秒内可被搜索
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * 开启服务器
     */
    private class ServerThread extends Thread {
        public void run() {
            try {
                BluetoothServerSocket mserverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("btspp",
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));   //UUID被用于唯一标识一个服务,如文件传输服务，串口服务、打印机服务等
                show("服务端:等待连接");

                mBluetoothSocket = mserverSocket.accept();
                show("服务端:连接成功");

                ReadThread mreadThread = new ReadThread(mBluetoothSocket);
                mreadThread.start();
                show("服务端:启动接受数据");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取数据
     */
    private class ReadThread extends Thread {
        private BluetoothSocket bluetoothSocket;

        public ReadThread(BluetoothSocket bluetoothSocket) {
            this.bluetoothSocket = bluetoothSocket;
        }

        public void run() {

            try {
                byte[] buffer = new byte[1024];
                int bytes;
                InputStream is = null;
                is = bluetoothSocket.getInputStream();

                while (true) {
                    try {
                        if ((bytes = is.read(buffer)) > 0) {
                            byte[] buf_data = new byte[bytes];
                            for (int i = 0; i < bytes; i++) {
                                buf_data[i] = buffer[i];
                            }
                            String s = new String(buf_data);
                            show("服务端:读取数据了~~" + s);
                        }
                    } catch (IOException e) {
                        try {
                            is.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void show(String msg) {
        mStringBuilder.append(msg + "\n");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLog.setText(mStringBuilder.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothSocket != null) {
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
