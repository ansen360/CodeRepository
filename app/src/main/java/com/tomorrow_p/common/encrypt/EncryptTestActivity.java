package com.tomorrow_p.common.encrypt;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.tomorrow_p.R;

import java.util.Map;

public class EncryptTestActivity extends Activity {

    private static final String TAG = "ansen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);
        testDes();
        testDes3();
        testRsa();

    }

    private void testDes3() {
        try {
            String en = DES3.encode("我是DES3对称加密的内容");
            String de = DES3.decode(en);
            log(en, de);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testRsa() {
//        try {
//            Map<String, Object> map = RSA.genKeyPair();
//            String publicKey = RSA.getPublicKey(map);
//            String privateKey = RSA.getPrivateKey(map);
//            log(publicKey, privateKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String en = RSA.encryptWithBase64("我是非对称加密的内容");
        String de = RSA.decryptWithBase64(en);
        log(en, de);
    }

    private void testDes() {
//        DES.randomKey = DES.genRandomKey(15);
        DES.randomKey = "95880288244730611960574122434059469100235892702736860872901247123456";
        String en = DES.encryptWithBase64("我是对称加密的内容");
        String de = DES.decryptWithBase64(en);
        log(en, de);
    }

    private void log(String en, String de) {
        Log.d(TAG, "加密后: " + en);
        Log.d(TAG, "解密后: " + de);
    }
}
