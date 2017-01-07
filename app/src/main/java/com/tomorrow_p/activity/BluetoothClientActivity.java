package com.tomorrow_p.activity;

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

import com.tomorrow_p.R;
import com.tomorrow_p.common.ToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static com.tomorrow_p.common.ToastUtils.show;


/**
 * Created by Ansen on 2016/12/23 16:36.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/1031307403/
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

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {    //判断是否有蓝牙
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) { //判断蓝牙 是否开启
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        }

    }


    private void createBond(BluetoothDevice bluetoothDevice) {
        try {
            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
            createBondMethod.invoke(bluetoothDevice);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    // 与设备解除配对
    public boolean removeBond(BluetoothDevice bluetoothDevice) throws Exception {
        Method removeBondMethod = BluetoothDevice.class.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(bluetoothDevice);
        return returnValue.booleanValue();
    }

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
            Toast.makeText(this, "没有连接", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            OutputStream os = mBluetoothSocket.getOutputStream();
            os.write(mMsg.getText().toString().getBytes());
            os.flush();
            ToastUtils.show("客户端:发送信息成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice bluetoothDevice = mDevices.get(position);
        if (BluetoothDevice.BOND_NONE == bluetoothDevice.getBondState()) {
            mBluetoothDevice = bluetoothDevice;
            createBond(mDevices.get(position));
        } else {
            try {
                show("客户端:开始连接:");
                ClientThread clientConnectThread = new ClientThread(bluetoothDevice);
                clientConnectThread.start();
            } catch (Exception e) {
                e.printStackTrace();
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
            textView.setPadding(10, 10, 10, 10);
            textView.setTextSize(18);
            textView.setText(bluetoothDevice.getName());
            if (BluetoothDevice.BOND_NONE == bluetoothDevice.getBondState()) {
                textView.setTextColor(Color.RED);
            } else {
                textView.setTextColor(Color.GREEN);
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
                        show("客户端:开始连接:");
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
                //连接
                show("客户端:开始连接...");
                mBluetoothSocket.connect();
                show("客户端:连接成功");
                //启动接受数据
                show("客户端:启动接受数据");
                ReadThread mreadThread = new ReadThread(mBluetoothSocket);
                mreadThread.start();
            } catch (IOException e) {
                show("客户端:连接服务端异常！断开连接重新试一试");
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
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream is = null;
            try {
                is = bluetoothSocket.getInputStream();
                ToastUtils.show("客户端:获得输入流");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            while (true) {
                try {
                    if ((bytes = is.read(buffer)) > 0) {
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            buf_data[i] = buffer[i];
                        }
                        String s = new String(buf_data);
                        ToastUtils.show("客户端:读取数据了" + s);
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
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            //蓝牙已经开启
            show("蓝牙已开启");
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
