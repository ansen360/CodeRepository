package com.tomorrow_p.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.tomorrow_p.R;
import com.tomorrow_p.common.Logger;
import com.tomorrow_p.common.encrypt.DES;
import com.tomorrow_p.common.encrypt.DES3;
import com.tomorrow_p.common.encrypt.RSA;

public class EncryptTestActivity extends Activity {

    private static final String TAG = "Encrypt";
    private StringBuilder mStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlog);
        TextView log = (TextView) findViewById(R.id.log);
        mStringBuilder = new StringBuilder();
        testDes();
        testDes3();
        testRsa();
        log.setText(mStringBuilder.toString());

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
        Logger.d(TAG, "加密后: " + en);
        Logger.d(TAG, "解密后: " + de);
        mStringBuilder.append("加密后: " + en + "\n");
        mStringBuilder.append("解密后: " + de + "\n");
    }
}
