package com.tinymonster.strangerdiary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public class NetUtils {
    /**
     * 网络是否已经连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED)
                    return true;
            }
        }
        return false;
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = null;
            if (cm != null) {
                info = cm.getActiveNetworkInfo();
            }
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     *
     * @param context
     * @return
     */
    public static boolean isWiFi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo info = null;
        info = cm.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI && info.getState() == NetworkInfo.State.CONNECTED;
    }

}
