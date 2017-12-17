package com.zspirytus.dmdemo.JavaSource.Utils;

/**
 * Created by ZSpirytus on 2017/12/14.
 */

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

public class NetWorkUtil {

    /**
     * @param context context
     * @return 网络是否连接可用
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connManager.getActiveNetworkInfo();

        if (networkinfo != null) {
            return networkinfo.isConnected();
        }

        return false;
    }

    /**
     * @param context context
     * @return wifi是否连接可用
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi != null) {
            return mWifi.isConnected();
        }

        return false;
    }

    /**
     * 当wifi不能访问网络时，mobile才会起作用
     * @param context context
     * @return GPRS是否连接可用
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobile = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mMobile != null) {
            return mMobile.isConnected();
        }
        return false;
    }

    /**
     * GPRS网络开关 反射ConnectivityManager中hide的方法setMobileDataEnabled 可以开启和关闭GPRS网络
     * @param context context
     * @param isEnable
     * @throws Exception
     */
    public static void toggleGprs(Context context,boolean isEnable) throws Exception {
        ConnectivityManager connManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> cmClass = connManager.getClass();
        Class<?>[] argClasses = new Class[1];
        argClasses[0] = boolean.class;

        // 反射ConnectivityManager中hide的方法setMobileDataEnabled，可以开启和关闭GPRS网络
        Method method = cmClass.getMethod("setMobileDataEnabled", argClasses);
        method.invoke(connManager, isEnable);
    }

    /**
     * WIFI网络开关
     * @param context context
     * @param enabled
     * @return 设置是否success
     */
    public static boolean toggleWiFi(Context context,boolean enabled) {
        ConnectivityManager connManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wm = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        return wm.setWifiEnabled(enabled);

    }

    /**
     * @param context context
     * @return 是否处于飞行模式
     */
    public static boolean isAirplaneModeOn(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 返回值是1时表示处于飞行模式
        int modeIdx = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
        boolean isEnabled = (modeIdx == 1);
        return isEnabled;
    }
    /**
     * 飞行模式开关
     * @param context context
     * @param setAirPlane
     */
    public static void toggleAirplaneMode(Context context,boolean setAirPlane) {
        ConnectivityManager connManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, setAirPlane ? 1 : 0);
        // 广播飞行模式信号的改变，让相应的程序可以处理。
        // 不发送广播时，在非飞行模式下，Android 2.2.1上测试关闭了Wifi,不关闭正常的通话网络(如GMS/GPRS等)。
        // 不发送广播时，在飞行模式下，Android 2.2.1上测试无法关闭飞行模式。
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        // intent.putExtra("Sponsor", "Sodino");
        // 2.3及以后，需设置此状态，否则会一直处于与运营商断连的情况
        intent.putExtra("state", setAirPlane);
        context.sendBroadcast(intent);
    }
}