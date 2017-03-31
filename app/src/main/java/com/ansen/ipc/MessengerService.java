package com.ansen.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ansen.common.Logger;
import com.ansen.common.ToastUtils;

/**
 * Created by Ansen on 2017/3/31 15:11.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.ansen.ipc
 * @Description: 通过Messenger的方式实现IPC通讯的服务端
 */
public class MessengerService extends Service {
    private static final String TAG = "ansen";

    private final static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Logger.d(TAG, "收到client的消息");
                ToastUtils.show("收到client的消息");
            }
            //获取客户端传过来的Messenger(送信者到了)
            Messenger messenger = msg.replyTo;
            //创建Message(让送信者顺便带回去一个信)
            Message message = Message.obtain();
            message.what = 2;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    private final static Messenger mMessenger = new Messenger(mHandler);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "服务启动");
        ToastUtils.show("服务启动");
    }
}
