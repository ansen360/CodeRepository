package com.ansen.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ansen.common.Logger;
import com.ansen.common.ThreadPoolManager;
import com.ansen.common.ToastUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
public class TCPServer extends Service {
    private static final String TAG = "ansen";
    private ServerSocket mServerSocket;
    private static final int mPort = 12346;

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
                    //  1.创建服务器Socket对象
                    mServerSocket = new ServerSocket(mPort);
                    ToastUtils.show("The TCP server is running");
                    //  2.监听客户端
                    Socket accept = mServerSocket.accept();//阻塞

                    //  3.获取输入流,读取数据
                    BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                    PrintWriter pw = new PrintWriter(accept.getOutputStream(), true);
                    String line = null;
                    while ((line = br.readLine()) != null) {//阻塞
                        Logger.d(TAG, line.toString());
                        ToastUtils.show(line.toString());
                        pw.println("received");
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
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
