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

    /*@Override
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
    }*/

    @Override
    public void onDestroy(){
        ActivityManager.removeActivity(this);
        super.onDestroy();
    }

}
