package com.code.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.code.R;

/**
 * Created by Ansen on 2016/12/20 09:49.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.activity
 * @Description: TODO
 */
public class WarrantyActivity extends Activity implements View.OnClickListener {

    private LinearLayout mBtns;
    private TextView registSuccess, registInfo;
    private RegistReceiver mRregistReceiver;
    private static final String REQUEST_ACTION="com.homecare.WarrantyRequest";
    private static final String RESPONSE_ACTION="com.homecare.WarrantyResponse";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warranty);
        registInfo = (TextView) findViewById(R.id.regist_info);
        registSuccess = (TextView) findViewById(R.id.regist_success);
        mBtns = (LinearLayout) findViewById(R.id.ll_btn);
        Button regist = (Button) findViewById(R.id.btn_regist);
        Button cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);
        regist.setOnClickListener(this);
        mRregistReceiver = new RegistReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RESPONSE_ACTION);
        registerReceiver(mRregistReceiver, intentFilter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_regist:
//                sendBroadcast(new Intent(REQUEST_ACTION));
                showDialog();
                break;
            case R.id.btn_cancel:
                registSuccess.setVisibility(View.GONE);
                registInfo.setText(getResources().getString(R.string.warranty_regist_info));
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRregistReceiver);
    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.dialog_warranty, null);
        dialog.show();
        dialog.getWindow().setContentView(view);
    }

    private class RegistReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status= intent.getIntExtra("warranty_status",0);
            if(status==0){
                showDialog();
            }else{  //  success
                registSuccess.setVisibility(View.VISIBLE);
//                mBtns.setVisibility(View.GONE);
                registInfo.setText(getResources().getString(R.string.warranty_regist_info_success));
            }
        }
    }
}
