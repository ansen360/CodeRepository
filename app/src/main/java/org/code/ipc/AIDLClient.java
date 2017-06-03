package org.code.ipc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.code.common.ToastUtils;

/**
 * Created by Ansen on 2017/3/31 17:35.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.ansen.ipc
 * @Description: TODO
 */
public class AIDLClient extends Activity {
    private static final String TAG = "ansen";
    private Connection mConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  1.在同一个项目不同进程中,直接启动服务
        Intent intent = new Intent(this, AIDLService.class);

//          2.在另一个项目中启动该服务,需要配置intent-filter,然后通过action启动
//        Intent intent = new Intent("com.aidl.service");
//        intent.setPackage("com.ansen");

        mConnection = new Connection();
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    private class Connection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IAidlInterface iAidlInterface = IAidlInterface.Stub.asInterface(service);
            Log.d(TAG, "aidl 连接成功");
            ToastUtils.show("aidl 连接成功");
            try {
                iAidlInterface.test();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "aidl 连接失败");
            ToastUtils.show("aidl 连接失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
