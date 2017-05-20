package com.code.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.code.R;

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

/**
 * 在蓝牙3.0及一下版本中，UUID被用于唯一标识一个服务，比如文件传输服务，串口服务、打印机服务等，如下： 
 * #蓝牙串口服务 
 * SerialPortServiceClass_UUID = '{00001101-0000-1000-8000-00805F9B34FB}' 
 * LANAccessUsingPPPServiceClass_UUID = '{00001102-0000-1000-8000-00805F9B34FB}' 
 * #拨号网络服务 
 * DialupNetworkingServiceClass_UUID = '{00001103-0000-1000-8000-00805F9B34FB}' 
 * #信息同步服务 
 * IrMCSyncServiceClass_UUID = '{00001104-0000-1000-8000-00805F9B34FB}' 
 * SDP_OBEXObjectPushServiceClass_UUID = '{00001105-0000-1000-8000-00805F9B34FB}' 
 * #文件传输服务 
 * OBEXFileTransferServiceClass_UUID = '{00001106-0000-1000-8000-00805F9B34FB}' 
 * IrMCSyncCommandServiceClass_UUID = '{00001107-0000-1000-8000-00805F9B34FB}' 
 * 蓝牙的连接有主从设备，提供服务的可以认为是从设备。主设备通过UUID访问从设备提供具有相同UUID的服务，从而建立客服端—服务器（C/S）模式。 
 */
public class BluetoothServerActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter;
    private StringBuilder mStringBuilder;
    private BluetoothServerSocket mBluetoothServerSocket;
    private TextView mLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlog);
        mLog = (TextView) findViewById(R.id.log);
        mStringBuilder = new StringBuilder();
        initBluetooth();
        new ServerThread().start();

    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        }
        // 设置蓝牙在300秒内可被搜索
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
                mBluetoothServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("ansen",
                        // UUID被用于唯一标识一个服务,如文件传输服务，串口服务、打印机服务等
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                show("服务器 等待连接...");

                BluetoothSocket socket1 = mBluetoothServerSocket.accept();
                show(socket1.getRemoteDevice().getName() + " 连接成功");
                new ReadThread(socket1).start();

                BluetoothSocket socket2 = mBluetoothServerSocket.accept();
                show(socket2.getRemoteDevice().getName() + " 连接成功");
                new ReadThread(socket2).start();

                BluetoothSocket socket3 = mBluetoothServerSocket.accept();
                show(socket3.getRemoteDevice().getName() + " 连接成功");
                new ReadThread(socket3).start();


            } catch (IOException e) {
                show(e.getMessage());
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
                            show(bluetoothSocket.getRemoteDevice().getName() + " " + s);
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
            } catch (IOException e) {
                show(e.getMessage());
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
        if (mBluetoothServerSocket != null) {
            try {
                mBluetoothServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
