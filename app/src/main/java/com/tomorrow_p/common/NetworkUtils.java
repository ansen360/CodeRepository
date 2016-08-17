package com.tomorrow_p.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtils {

    public static Boolean isWifiConnected(Context context) {
        if (context == null) {
            return false;
        }
        Object object = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (object == null) {
            return false;
        }
        ConnectivityManager connectMgr = (ConnectivityManager) object;
        Assert.notNull(connectMgr);

        NetworkInfo wifiNetInfo = connectMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Assert.notNull(wifiNetInfo);
        return wifiNetInfo.isConnected();
    }

    public static boolean isWifiConnectedOrConnecting(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Assert.notNull(connManager);

        NetworkInfo wifi = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Assert.notNull(wifi);

        return wifi.isConnectedOrConnecting();
    }

    public static int getNetworkType(Context ctx) {
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info.getType();
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getWifiInfoMacAddress(Context ctx) {
        try {
            WifiManager wifi = (WifiManager) ctx
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isMobileNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    public static String getWifiInfoBSSID(Context ctx) {
        try {
            WifiManager wm = (WifiManager) ctx
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wm.getConnectionInfo();
            return info.getBSSID();
        } catch (Exception e) {
            return null;
        }
    }
}
