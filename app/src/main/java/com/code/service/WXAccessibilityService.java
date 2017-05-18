package com.code.service;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.code.common.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ansen on 2017/1/7 10:31.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.service
 * @Description: AccessibilityService可以获取当前页面的信息(view的节点), 并且可以模拟点击事件
 */
public class WXAccessibilityService extends AccessibilityService {

    private static final String TAG = "ansen";
    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock mKeyguardLock;
    //唤醒屏幕相关
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private List<AccessibilityNodeInfo> mNodeInfos;
    private int lastCount;
    private boolean isChatUI = false;
    private boolean isNotifyEnter = false;


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mNodeInfos = new ArrayList<>();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        switch (eventType) {
            case 2048:  //监听视图变化事件
                if (isChatUI && "android.widget.TextView".equals(className)) {
                    getIncreasePacket();
                    Logger.i(TAG, "来消息");
                }

                Logger.d("2048", "2048 " + event.getClassName().toString());
                break;
            // 监听有通知栏消息的事件
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        Logger.d(TAG, "content: " + content);
                        if (content.contains("[微信红包]")) {
                            //打开通知栏消息
                            if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                                Notification notification = (Notification) event.getParcelableData();
                                PendingIntent pendingIntent = notification.contentIntent;
                                try {
                                    pendingIntent.send();
                                    isNotifyEnter = true;
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;

            // 监听窗口发生变化的事件(判断是否进入微信红包消息界面)
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Logger.i(TAG, "className: " + className);
                if ("com.tencent.mm.ui.LauncherUI".equals(className)) { //聊天页面
                    if (isNotifyEnter) {
                        getLastPacket();
                        Logger.i(TAG, "通知栏: 开");
                    }
                    isNotifyEnter = false;
                    isChatUI = true;
                } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(className)) { //红包页面
                    isChatUI = false;
                    //拆红包
                    executeClick("com.tencent.mm:id/bg7");
//                    executeClick("com.tencent.mm:id/bga");   //返回
                    Logger.i(TAG, "拆");
                } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(className)) { //红包已打开页面
                    isChatUI = false;
                    executeClick("com.tencent.mm:id/ge");   //返回
                    Logger.i(TAG, "关");
                } else {
                    isChatUI = false;
                }
                break;
        }


    }

    private void getIncreasePacket() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        recycle(rootNode);
        if (mNodeInfos.size() > 0) {
            if (lastCount < mNodeInfos.size()) {    //有新消息
                mNodeInfos.get(mNodeInfos.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Logger.i(TAG, "新红包");
            }
            lastCount = mNodeInfos.size();
        }
        mNodeInfos.clear();
    }

    private void getLastPacket() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        recycle(rootNode);
        if (mNodeInfos.size() > 0) {
            mNodeInfos.get(mNodeInfos.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            lastCount = mNodeInfos.size();
            mNodeInfos.clear();
        }
    }


    /**
     * 打印一个节点的结构
     */
    public void recycle(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            if (info.getText() != null) {
                if ("领取红包".equals(info.getText().toString())) {
                    //找到一个可以点击的View,模拟点击指定事件
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    AccessibilityNodeInfo parent = info.getParent();
                    while (parent != null) {
                        if (parent.isClickable()) {
                            mNodeInfos.add(parent);
                            break;
                        }
                        parent = parent.getParent();
                    }

                }
            }

        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    recycle(info.getChild(i));
                }
            }
        }
    }


    /**
     * 查找对应控件,模拟点击事件
     */
    private void executeClick(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            // 通过节点View的Text内容来查找
//            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("开");
//            通过节点View在xml布局中的id名称
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            for (AccessibilityNodeInfo n : list) {
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

    }

    private void wakeUp(boolean b) {
        if (b) {
            //获取电源管理器对象
            mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

            //点亮屏幕
            mWakeLock.acquire();

            //得到键盘锁管理器对象
            mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            mKeyguardLock = mKeyguardManager.newKeyguardLock("unLock");

            //解锁
            mKeyguardLock.disableKeyguard();
        } else {
            //锁屏
            mKeyguardLock.reenableKeyguard();

            //释放wakeLock，关灯
            mWakeLock.release();
        }

    }

    @Override
    public void onInterrupt() {

    }
}
