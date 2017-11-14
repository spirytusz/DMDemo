package com.zspirytus.dmdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zspirytus.dmdemo.Fragment.ELSFragment;
import com.zspirytus.dmdemo.Fragment.MyInfoFragment;
import com.zspirytus.dmdemo.Fragment.RepairFragment;
import com.zspirytus.dmdemo.Fragment.Settings;
import com.zspirytus.dmdemo.Interface.SetMyInfoAvatar;
import com.zspirytus.dmdemo.Interface.SetUploadPicPath;
import com.zspirytus.dmdemo.Interface.getStudentBasicInfoResponse;
import com.zspirytus.dmdemo.JavaSource.ActivityManager;
import com.zspirytus.dmdemo.JavaSource.FragmentCollector;
import com.zspirytus.dmdemo.JavaSource.PhotoUtils;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;
import com.zspirytus.dmdemo.Reproduction.CircleImageView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        SetMyInfoAvatar,
        SetUploadPicPath {

    private static final String TAG = "MainActivity";
    private static final String mAccountKey = "Account";
    private static final String mAvatarKey = "Avatar";
    private static final String hasCustomAvatar = "CustomAvatar";
    private static final String myInfoFragmentStatusKey = "myInfoFragmentStatusKey";
    private static final String myRepairFragmentStatusKey = "myRepairFragmentStatusKey";
    private static final String myELSFragmentStatusKey = "myELSFragmentStatusKey";
    private static final String mySettingsFragmentStatusKey = "mySettingsFragmentStatusKey";
    private static final int REQ_CAMERA = 0x01;
    private static final int REQ_ALBUM = 0x02;
    private static final int REQ_CUT = 0x04;
    private static final int REQ_PERMISSION_FOR_CAMERA = 0x10;
    private static final int REQ_PERMISSION_FOR_ALBUM = 0x20;
    private static final int BY_CAMERA = 1;
    private static final int BY_ALBUM = 2;

    private FragmentManager mFragmentManager;
    private MyInfoFragment mMInfoFragment;
    private RepairFragment mRepairFragment;
    private ELSFragment mELSchool;
    private Settings mSettings;
    private TextView account;
    private CircleImageView mAvatar;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private ProgressBar mProgressBar;

    private String mAccountVaule;
    private File picName;
    private Uri picUri;
    private boolean isAlbum = false;
    private boolean isRepairPicDir = false;

    private CircleImageView cimg;
    private TextView repairPicDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager.addActivity(this);
        LoadPane();
        RestoreAvatar();
        if( getSupportFragmentManager().findFragmentByTag(MyInfoFragment.class.getName()) == null)
        {
            Log.d(TAG,"TEST FRAGMENT1");
            getInform(WebServiceConnector.PARAMTYPE_SNO,"15251102203");
        } else {
            mMInfoFragment = (MyInfoFragment) getSupportFragmentManager().findFragmentByTag(MyInfoFragment.class.getName());
            FragmentCollector.HideAllFragment(mFragmentManager.beginTransaction());
        }
        if(getSupportFragmentManager().findFragmentByTag(RepairFragment.class.getName()) != null){
            mRepairFragment = (RepairFragment) getSupportFragmentManager().findFragmentByTag(RepairFragment.class.getName());
        }
        if(getSupportFragmentManager().findFragmentByTag(ELSFragment.class.getName()) != null){
            mELSchool = (ELSFragment) getSupportFragmentManager().findFragmentByTag(ELSFragment.class.getName());
        }
        if(getSupportFragmentManager().findFragmentByTag(Settings.class.getName()) != null){
            mSettings = (Settings) getSupportFragmentManager().findFragmentByTag(Settings.class.getName());
        }
    }

    /*private void restoreFragmentStatus(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if((mMInfoFragment = (MyInfoFragment) mFragmentManager.findFragmentByTag(MyInfoFragment.class.getName())) == null){
            mMInfoFragment = new MyInfoFragment();
            FragmentCollector.addFragment(mMInfoFragment);
            ft.add(R.id.fragment_container, mMInfoFragment,mMInfoFragment.getClass().getName());
        }
        if((mRepairFragment = (RepairFragment) mFragmentManager.findFragmentByTag(RepairFragment.class.getName())) == null){
            mRepairFragment = new RepairFragment();
            FragmentCollector.addFragment(mRepairFragment);
            ft.add(R.id.fragment_container, mRepairFragment,mRepairFragment.getClass().getName());
        }
        if((mELSchool = (ELSFragment) mFragmentManager.findFragmentByTag(ELSFragment.class.getName())) == null){
            mELSchool = new ELSFragment();
            FragmentCollector.addFragment(mELSchool);
            ft.add(R.id.fragment_container, mELSchool,mELSchool.getClass().getName());
        }
        if((mSettings = (Settings) mFragmentManager.findFragmentByTag(Settings.class.getName())) == null){
            mSettings = new Settings();
            FragmentCollector.addFragment(mSettings);
            ft.add(R.id.fragment_container, mSettings,mSettings.getClass().getName());
        }
        ft.commitAllowingStateLoss();
    }*/

    private void getInform(String paramType,String param){
        MyAsyncTask gs = new MyAsyncTask(WebServiceConnector.METHOD_GETBASICINFOBYSNO);
        gs.setListener(new getStudentBasicInfoResponse() {
            @Override
            public void getResult(ArrayList<String> result) {
                setDefaultFragment(mFragmentManager,result);
            }
        });
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        list1.clear();
        list2.clear();
        list1.add(paramType);
        list2.add(param);
        gs.execute(list1,list2);
    }

    @Override
    public void setUploadPicPath(TextView textView, int by) {
        repairPicDir = textView;
        isRepairPicDir = true;
        if (by == BY_CAMERA)
            picUri = PhotoUtils.applyPermissionForCamera(this);
        else if (by == BY_ALBUM)
            PhotoUtils.applyPermissionForAlbum(this);
    }

    public void RestoreAvatar() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String avatar = pref.getString(mAvatarKey, "");
        if (!avatar.equals(""))
            mAvatar.setImageBitmap(PhotoUtils.getBitmapbyString(avatar));
    }

    private void LoadPane() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.GONE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFragmentManager = getSupportFragmentManager();
        mAccountVaule = getIntent().getStringExtra(mAccountKey);
        View headerView = navigationView.getHeaderView(0);
        account = headerView.findViewById(R.id.side_account);
        account.setText(mAccountVaule);
        mAvatar = headerView.findViewById(R.id.imageView);
        mAvatar.setImageResource(R.drawable.ic_account_circle_black_24dp);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION_FOR_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    picUri = PhotoUtils.useCamera(this);
                else {
                    // 没有获取到权限，重新请求
                    if (true) {

                    }
                    Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQ_PERMISSION_FOR_ALBUM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    PhotoUtils.selectFromAlbum(this);
                else {
                    // 没有获取到权限，重新请求
                    if (true) {

                    }
                    Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void setAvatar(CircleImageView img, int by) {
        if (by == BY_CAMERA)
            picUri = PhotoUtils.applyPermissionForCamera(this);
        else if (by == BY_ALBUM) {
            PhotoUtils.applyPermissionForAlbum(this);
            isAlbum = true;
        }
        cimg = img;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CAMERA:
                    if (isRepairPicDir) {
                        repairPicDir.setText(PhotoUtils.picName.getAbsolutePath());
                        break;
                    }
                    PhotoUtils.cropPicture(this, picUri);
                    break;
                case REQ_ALBUM:
                    picUri = data.getData();
                    PhotoUtils.cropPicture(this, picUri);
                    break;
                case REQ_CUT:
                    Bitmap bitmap = BitmapFactory.decodeFile(PhotoUtils.cropPicName.getAbsolutePath());
                    String a = PhotoUtils.convertIconToString(bitmap);
                    PhotoUtils.saveAvatar(this, bitmap);
                    cimg.setImageBitmap(bitmap);
                    mAvatar.setImageBitmap(bitmap);
                    if (isAlbum)
                        PhotoUtils.cropPicName.delete();
                    else {
                        PhotoUtils.picName.delete();
                        PhotoUtils.cropPicName.delete();
                    }
                    break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (id == R.id.nav_account) {
            FragmentCollector.HideAllFragment(ft);
            ft.show(mMInfoFragment);
        } else if (id == R.id.nav_repair) {
            if (mRepairFragment == null) {
                mRepairFragment = new RepairFragment();
                FragmentCollector.addFragment(mRepairFragment);
                ft.add(R.id.fragment_container, mRepairFragment,mRepairFragment.getClass().getName());
            }
            FragmentCollector.HideAllFragment(ft);
            ft.show(mRepairFragment);
        } else if (id == R.id.nav_els) {
            if (mELSchool == null) {
                mELSchool = new ELSFragment();
                FragmentCollector.addFragment(mELSchool);
                ft.add(R.id.fragment_container, mELSchool,mELSchool.getClass().getName());
            }
            FragmentCollector.HideAllFragment(ft);
            ft.show(mELSchool);
        } else if (id == R.id.nav_settings) {
            if (mSettings == null) {
                mSettings = new Settings();
                FragmentCollector.addFragment(mSettings);
                ft.add(R.id.fragment_container, mSettings,mSettings.getClass().getName());
            }
            FragmentCollector.HideAllFragment(ft);
            ft.show(mSettings);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_feedback) {

        }
        ft.commitAllowingStateLoss();
        Log.d("","MyInfoFragmentHiddenStatus:"+mMInfoFragment.isHidden());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initFragment(){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        mRepairFragment = new RepairFragment();
        FragmentCollector.addFragment(mRepairFragment);
        ft.add(R.id.fragment_container, mRepairFragment);
        mELSchool = new ELSFragment();
        FragmentCollector.addFragment(mELSchool);
        ft.add(R.id.fragment_container, mELSchool);
        mSettings = new Settings();
        FragmentCollector.addFragment(mSettings);
        ft.add(R.id.fragment_container, mSettings);
        ft.commitAllowingStateLoss();
    }

    public void setDefaultFragment(FragmentManager fm,ArrayList<String> strList) {
        mMInfoFragment = MyInfoFragment.GetThisFragment(strList);
        FragmentCollector.addFragment(mMInfoFragment);
        fm.beginTransaction().add(R.id.fragment_container, mMInfoFragment, mMInfoFragment.getClass().getName()).show(mMInfoFragment).commit();
    }

    public static void StartThisActivity(Context context, String account) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(mAccountKey, account);
        ((Activity) context).finish();
        context.startActivity(intent);
    }

}
