package com.code.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.code.common.Logger;
import com.code.common.ThreadPoolManager;
import com.code.common.ToastUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Ansen on 2017/4/8 11:36.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.ansen.socket
 * @Description: TODO
 */
public class UDPServer extends Service {

    private static final String TAG = "ansen";
    private boolean mRun = true;
    private DatagramSocket mDatagramSocket;
    private static final int mPort = 12345;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ThreadPoolManager.getThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //  1.创建接收端Socket对象,指定端口
                    mDatagramSocket = new DatagramSocket(mPort);
                    ToastUtils.show("The UDP server is running");
                    while (mRun) {
                        //  2.创建数据包
                        byte[] bys = new byte[1024];
                        DatagramPacket dp = new DatagramPacket(bys, bys.length);
                        //  3.接收数据
                        mDatagramSocket.receive(dp);
                        //  4.解析数据
                        String ip = dp.getAddress().getHostAddress();
                        String s = new String(dp.getData(), 0, dp.getLength());
                        Logger.d(TAG, "from " + ip + " : " + s);
                        ToastUtils.show(s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.d(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRun = false;
        if (mDatagramSocket != null) {
            mDatagramSocket.close();
        }
    }
}
