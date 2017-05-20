package com.code;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.code.bluetooth.BluetoothActivity;
import com.code.activity.CameraActivity;
import com.code.activity.DateFormatActivity;
import com.code.activity.DialogActivity;
import com.code.activity.EncryptTestActivity;
import com.code.activity.JNIActivity;
import com.code.activity.NotificationActivity;
import com.code.activity.RedPacketActivity;
import com.code.activity.TabLayoutActivity;
import com.code.activity.TestActivity;
import com.code.activity.WarrantyActivity;
import com.code.common.GPSManager;
import com.code.ipc.AIDLClient;
import com.code.ipc.MessengerClient;
import com.code.socket.TCPClient;
import com.code.socket.UDPClient;
import com.code.view.FlowButton;
import com.code.view.drawoutline.DrawOutlineActivity;
import com.code.view.flowlayout.FlowLayout;

public class MainActivity extends AppCompatActivity {

    private FlowLayout mFlowLayout;
    private MainActivity mContext;
    private static final int[] ICONS = new int[]{R.mipmap.icon1, R.mipmap.icon2, R.mipmap.icon3,
            R.mipmap.icon4, R.mipmap.icon5, R.mipmap.icon6, R.mipmap.icon7, R.mipmap.icon8,
            R.mipmap.icon9, R.mipmap.icon10, R.mipmap.icon11, R.mipmap.icon12};
    private static int ICON_INDEX = 0;

    private void nextIndex() {
        ICON_INDEX = (++ICON_INDEX) % ICONS.length;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FrameLayout rootLayout = (FrameLayout) findViewById(R.id.root_layout);
        mFlowLayout = (FlowLayout) findViewById(R.id.flowlayout);
        mContext = this;
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIndex();
                rootLayout.setBackgroundResource(ICONS[ICON_INDEX % ICONS.length]);
            }
        });

        mFlowLayout.addView(new FlowButton(this, "TEST", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TestActivity.class));
            }
        }));
        mFlowLayout.addView(new FlowButton(this, "Encrypt", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, EncryptTestActivity.class));
            }
        }));

        mFlowLayout.addView(new FlowButton(this, "Date Format", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DateFormatActivity.class));
            }
        }));

        /*mFlowLayout.addView(new FlowButton(this, "Boot the wizard", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DefaultActivity.class));
            }
        }));

        mFlowLayout.addView(new FlowButton(this, "gesture", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, com.ansen.gesture.MainActivity.class));
            }
        }));*/

        mFlowLayout.addView(new FlowButton(this, "DrawFace", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DrawOutlineActivity.class));
            }
        }));
        mFlowLayout.addView(new FlowButton(this, "Test", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        }));

        mFlowLayout.addView(new FlowButton(this, "Dialog", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DialogActivity.class));
            }
        }));

        mFlowLayout.addView(new FlowButton(this, "Camera", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CameraActivity.class));
            }
        }));

        mFlowLayout.addView(new FlowButton(this, "Warranty", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, WarrantyActivity.class));
            }
        }));

        mFlowLayout.addView(new FlowButton(this, "Bluetooth", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, BluetoothActivity.class));
            }
        }));

        mFlowLayout.addView(new FlowButton(this, "Notification", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, NotificationActivity.class));
            }
        }));

        mFlowLayout.addView(new FlowButton(this, "RedPacket", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RedPacketActivity.class));
            }
        }));
        mFlowLayout.addView(new FlowButton(this, "TabLayout", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TabLayoutActivity.class));
            }
        }));
//        mFlowLayout.addView(new FlowButton(this, "GuideActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, GuideActivity.class));
//            }
//        }));
//        mFlowLayout.addView(new FlowButton(this, "Socket", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, SocketActivity.class));
//            }
//        }));
        mFlowLayout.addView(new FlowButton(this, "IPC-Messenger", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MessengerClient.class));
            }
        }));
        mFlowLayout.addView(new FlowButton(this, "IPC-AIDL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AIDLClient.class));
            }
        }));
        mFlowLayout.addView(new FlowButton(this, "UDP-Socket", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, UDPClient.class));
            }
        }));
        mFlowLayout.addView(new FlowButton(this, "TCP-Socket", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TCPClient.class));
            }
        }));
        mFlowLayout.addView(new FlowButton(this, "JNI", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, JNIActivity.class));
            }
        }));
        ImmersiveMode();
    }

    /**
     * TODO: test
     */
    private void test() {
        GPSManager gpsManager = new GPSManager(this);
        gpsManager.isOpen();
        gpsManager.getGPSConfi(this);


    }


    private void ImmersiveMode() {
//        transparentStatusBar();
        transparentNavigationBar();
//        hideStatusAndNavigationBar();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    /**
     * 隐藏状态栏和导航栏
     */
    private void hideStatusAndNavigationBar() {
        View decorView = getWindow().getDecorView();    //获取当前界面的DecorView
        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;  // 设置全屏(隐藏导航栏)
//        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;  // 设置全屏(隐藏状态栏)
        decorView.setSystemUiVisibility(option);
    }

    /**
     * 设置全屏(透明状态栏)
     */
    private void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 透明导航栏
     */
    private void transparentNavigationBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {    // 沉浸式模式
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }
}
