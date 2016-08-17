package com.tomorrow_p;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tomorrow_p.common.encrypt.EncryptTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void encrypt(View v){
        startActivity(new Intent(this, EncryptTestActivity.class));
    }
}
