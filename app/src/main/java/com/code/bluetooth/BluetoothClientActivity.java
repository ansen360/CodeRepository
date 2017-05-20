package com.code.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.code.R;
import com.code.common.ToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static com.code.common.ToastUtils.show;


/**
 * Created by Ansen on 2016/12/23 16:36.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.activity
 * @Description: TODO
 */

public class BluetoothClientActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String TAG = "ansen";
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> mDevices = new ArrayList();
    private DeviceAdapter mDeviceAdapter;
    private ListView mListView;
    private EditText mMsg;
    private Button mSend;
    private BluetoothSocket mBluetoothSocket;
    private BluetoothDevice mBluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_bluetooth);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
        }
        mListView = (ListView) findViewById(R.id.list);
        mMsg = (EditText) findViewById(R.id.bluetooth_msg);
        mSend = (Button) findViewById(R.id.bluetooth_send);
        mListView.setOnItemClickListener(this);
        mSend.setOnClickListener(this);
        mDeviceAdapter = new DeviceAdapter();
        mListView.setAdapter(mDeviceAdapter);

        initBluetooth();
        registerBTReceiver();
        mBluetoothAdapter.startDiscovery();

    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {    //是否支持蓝牙
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            // 打开蓝牙方式一,直接打开
            mBluetoothAdapter.enable();
            // 打开蓝牙方式二,调用对话框打开: onActivityResult()提供打开成功的回调
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent, 0);
        }

    }

    private void registerBTReceiver() {
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND); // 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);    // 获取蓝牙设备的连接状态
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intent);
    }

    /**
     * 配对设备
     */
    private void createBond(BluetoothDevice bluetoothDevice) {
        try {
            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
            createBondMethod.invoke(bluetoothDevice);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * 与设备解除配对
     */
    public boolean removeBond(BluetoothDevice bluetoothDevice) throws Exception {
        Method removeBondMethod = BluetoothDevice.class.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(bluetoothDevice);
        return returnValue.booleanValue();
    }

    /**
     * 获取配对过的设备
     */
    private Set<BluetoothDevice> getBondedDevices() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            Log.d(TAG, device.getName() + " " + device.getAddress());
        }
        return pairedDevices;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth_send:
                sendMessage();
                break;
        }
    }

    /**
     * 发送数据
     */
    public void sendMessage() {
        if (mBluetoothSocket == null) {
            Toast.makeText(this, "请连接蓝牙设备", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            OutputStream os = mBluetoothSocket.getOutputStream();
            os.write(mMsg.getText().toString().getBytes());
            os.flush();
            ToastUtils.show("客户端: 发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice bluetoothDevice = mDevices.get(position);
        // 蓝牙配对
        if (BluetoothDevice.BOND_NONE == bluetoothDevice.getBondState()) {
            mBluetoothDevice = bluetoothDevice;
            createBond(mDevices.get(position));
            show("设备配对..");
        } else {// 蓝牙配对过,直接连接
            try {
                show("连接设备..");
                ClientThread clientConnectThread = new ClientThread(bluetoothDevice);
                clientConnectThread.start();
            } catch (Exception e) {
                show(e.getMessage());
            }
        }
    }

    private class DeviceAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mDevices.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BluetoothDevice bluetoothDevice = mDevices.get(position);
            TextView textView = new TextView(BluetoothClientActivity.this);
            textView.setPadding(30, 30, 30, 30);
            textView.setTextSize(18);
            if (TextUtils.isEmpty(bluetoothDevice.getName())) {
                textView.setText(bluetoothDevice.getAddress());
            } else {
                textView.setText(bluetoothDevice.getName());
            }
            if (BluetoothDevice.BOND_NONE == bluetoothDevice.getBondState()) {
                textView.setTextColor(getResources().getColor(R.color.colorWarning));
            } else {
                textView.setTextColor(getResources().getColor(R.color.colorBlue));
            }
            return textView;
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDevices.add(bluetoothDevice);

            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                // 获取蓝牙设备的连接状态
                if (mBluetoothDevice == null) {
                    return;
                }
                int connectState = mBluetoothDevice.getBondState();
                // 已配对
                if (connectState == BluetoothDevice.BOND_BONDED) {
                    try {
                        show("连接中...");
                        ClientThread clientConnectThread = new ClientThread(mBluetoothDevice);
                        clientConnectThread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {  //搜索结束
                mDeviceAdapter.notifyDataSetChanged();
            }

        }
    };

    /**
     * 开启客户端
     */
    private class ClientThread extends Thread {
        private BluetoothDevice bluetoothDevice;

        public ClientThread(BluetoothDevice bluetoothDevice) {
            this.bluetoothDevice = bluetoothDevice;
        }

        public void run() {
            try {
                //创建一个Socket连接：只需要服务器在注册时的UUID号
                mBluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));   //UUID被用于唯一标识一个服务,如文件传输服务，串口服务、打印机服务等
                mBluetoothSocket.connect();
                show("连接成功");
                //启动接受数据
                new ReadThread(mBluetoothSocket).start();
                ;
            } catch (IOException e) {
                show("连接服务端异常！断开连接重新试一试");
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
                InputStream is = bluetoothSocket.getInputStream();

                while (true) {
                    if ((bytes = is.read(buffer)) > 0) {
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            buf_data[i] = buffer[i];
                        }
                        String s = new String(buf_data);
                        ToastUtils.show("服务器: " + s);
                    }
                    is.close();
                    break;
                }
            } catch (Exception e) {
                ToastUtils.show(e.getMessage());
            }
        }
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
