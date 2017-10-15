package com.zspirytus.dmdemo.JavaSource;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


import com.zspirytus.dmdemo.Activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 97890 on 2017/9/17.
 */

public class ObtainPermission extends BaseActivity {
    private static final String[] NEED_PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET

    };
    boolean mShowRequestPermission = true;//用户是否禁止权限
    private Context mContext;

    /**
     * @param context 通过构造方法传递的上下文
     */
    public ObtainPermission(Context context){
        mContext = context;
    }

    /**
     * 判断哪些权限未授予
     */
    public List<String> hasNotGrant(){
        List<String> mPermissionList = new ArrayList<>();
        mPermissionList.clear();
        for (int i = 0; i < NEED_PERMISSION.length; i++) {
            if (ContextCompat.checkSelfPermission(mContext, NEED_PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(NEED_PERMISSION[i]);
            }
        }
        return mPermissionList;
    }

    /**
     * 授予权限
     */
    public void GrantPermission(){
        List<String> hasNotGrant = hasNotGrant();
        if(!hasNotGrant.isEmpty()){
            String[] permissions = hasNotGrant.toArray(new String[hasNotGrant.size()]);
            ActivityCompat.requestPermissions((Activity) mContext,permissions,1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permissions[i]);
                        if (showRequestPermission) {
                            GrantPermission();
                            return;
                        } else {
                            mShowRequestPermission = false;//已经禁止
                        }
                    }
                }
                break;

            default:
                break;
        }
    }
}
