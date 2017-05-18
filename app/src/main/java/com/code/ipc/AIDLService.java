package com.code.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.code.common.ToastUtils;

/**
 * Created by Ansen on 2017/3/31 17:28.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.ansen.ipc
 * @Description: TODO
 */
public class AIDLService extends Service {

    private Binder mBinder = new IAidlInterface.Stub() {
        @Override
        public void test() throws RemoteException {
            Log.d("ansen", "test");
            ToastUtils.show("test");
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.show("AIDLService 启动");
    }
}
