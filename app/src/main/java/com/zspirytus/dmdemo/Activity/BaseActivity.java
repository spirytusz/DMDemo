/**
 * Base Activity, all activity extended this activity
 */
package com.zspirytus.dmdemo.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.zspirytus.dmdemo.JavaSource.Manager.ActivityManager;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.NetWorkUtil;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by ZSpirytus on 2017/9/28.
 */

public class BaseActivity extends AppCompatActivity{

    private Context context = this;

    private MyNetWorkConnectBroadCaster myNetWorkConnectBroadCaster;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
    }

    public static void CloseKeyBoard(Activity activity){
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        myNetWorkConnectBroadCaster
                = new MyNetWorkConnectBroadCaster();
        registerReceiver(myNetWorkConnectBroadCaster,intentFilter);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(myNetWorkConnectBroadCaster != null)
            unregisterReceiver(myNetWorkConnectBroadCaster);
        myNetWorkConnectBroadCaster = null;
    }

    @Override
    public void onDestroy(){
        ActivityManager.removeActivity(this);
        super.onDestroy();
    }

    public static void setMobileDataState(Context context, boolean enabled) {
        TelephonyManager mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        ConnectivityManager mConnectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // if api level >= L, use ConnectivityManager, else use TelephonyManager
        Object object = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP?       mTelephonyManager : mConnectivityManager;
        String methodName = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP? "setDataEnabled" : "setMobileDataEnabled";
        Method setMobileDataEnable;
        try {
            setMobileDataEnable = mConnectivityManager.getClass().getMethod("setDataEnabled", boolean.class);
            setMobileDataEnable.invoke(object, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getMobileDataState(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        ConnectivityManager mConnectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // if api level >= L, use ConnectivityManager, else use TelephonyManager
        Object object = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP? mConnectivityManager:mTelephonyManager;
        String methodName = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP? "getDataEnabled" : "getMobileDataEnabled";
        try {
            Method getMobileDataEnable = mConnectivityManager.getClass().getMethod("getDataEnabled", null);
            return (Boolean) getMobileDataEnable.invoke(object, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    class MyNetWorkConnectBroadCaster extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent){
            boolean isNetWorkAvailable = NetWorkUtil.isMobileConnected(context) || NetWorkUtil.isWifiConnected(context);
            if(!isNetWorkAvailable){
                AlertDialog.Builder dialog = DialogUtil.getDialog((Activity)context,"网络连接未打开,请打开网络");
                dialog.setPositiveButton("好",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                dialog.show();
            }
        }

    }
}
