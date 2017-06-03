package org.code.ipc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import org.code.common.Logger;
import org.code.common.ToastUtils;

/**
 * Created by Ansen on 2017/3/31 15:15.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.ansen.ipc
 * @Description: 通过Messenger的方式实现IPC通讯的客户端
 */
public class MessengerClient extends Activity {
    private static final String TAG = "ansen";
    private static Messenger mMessenger;
    private Connection mConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.创建Messenger对象
        mMessenger = new Messenger(mHandler);
        //2.绑定服务
        Intent intent = new Intent(this, MessengerService.class);
        mConnection = new Connection();
        bindService(intent, mConnection, BIND_AUTO_CREATE);

    }

    private final static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                Logger.d(TAG, "来自Messenger service的消息");
                ToastUtils.show("来自Messenger service的消息");
            }
        }
    };

    /**
     * 服务连接状态的监听
     */
    private class Connection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            //通过binder创建Messenger (送信者)
            Messenger messenger = new Messenger(service);
            //创建Message(信)
            Message message = Message.obtain();
            //客户端的Messenger传给服务端(送信者去送信)
            message.replyTo = mMessenger;
            message.what = 1;
            try {
                Logger.d(TAG, "连接成功,client发送消息");
                ToastUtils.show("连接成功,client发送消息");
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
