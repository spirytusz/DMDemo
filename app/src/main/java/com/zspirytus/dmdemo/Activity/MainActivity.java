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
import android.view.KeyEvent;
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

import com.zspirytus.dmdemo.Fragment.AboutFragment;
import com.zspirytus.dmdemo.Fragment.BackLateFragment;
import com.zspirytus.dmdemo.Fragment.ELSFragment;
import com.zspirytus.dmdemo.Fragment.MyInfoFragment;
import com.zspirytus.dmdemo.Fragment.RepairFragment;
import com.zspirytus.dmdemo.Fragment.Settings;
import com.zspirytus.dmdemo.Interface.SetMyInfoAvatar;
import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.Interface.getStudentBasicInfoResponse;
import com.zspirytus.dmdemo.JavaSource.Manager.ActivityManager;
import com.zspirytus.dmdemo.JavaSource.Manager.FragmentCollector;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.PhotoUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;
import com.zspirytus.dmdemo.Reproduction.CircleImageView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        SetMyInfoAvatar {

    public CircleImageView mAvatar;

    private static final String TAG = "MainActivity";
    private static final String mSnoKey = "Sno";
    private static final String mAvatarKey = "Avatar";
    private static final int REQ_CAMERA = 0x01;
    private static final int REQ_ALBUM = 0x02;
    private static final int REQ_CUT = 0x04;
    private static final int REQ_PERMISSION_FOR_CAMERA = 0x10;
    private static final int REQ_PERMISSION_FOR_ALBUM = 0x20;
    private static final int BY_CAMERA = 1;
    private static final int BY_ALBUM = 2;

    private final Activity activity = this;

    private FragmentManager mFragmentManager;
    private MyInfoFragment mMInfoFragment;
    private RepairFragment mRepairFragment;
    private ELSFragment mELSchool;
    private Settings mSettings;
    private BackLateFragment mBackLateFragment;
    private AboutFragment mAboutFragment;
    private TextView mSno;
    private TextView mName;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private ProgressBar mProgressBar;

    private String mSnoVaule;
    private Uri picUri;
    private boolean isAlbum = false;
    private boolean isRepairPicDir = false;

    private CircleImageView cimg;
    private TextView repairPicDir;
    private long mPreviousPressBackTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager.addActivity(this);
        LoadPane();
        RestoreAvatar();
        if( getSupportFragmentManager().findFragmentByTag(MyInfoFragment.class.getName()) == null)
        {
            ArrayList<String> list = MyInfoFragment.getStudentInfobyLocalFile(this,mSnoVaule);
            if(true)
                getInform(WebServiceConnector.PARAMTYPE_SNO,mSnoVaule);
            else {
                if(mSnoVaule != null && mSnoVaule.equals("")){

                }
                setDefaultFragment(getSupportFragmentManager(),list);
                mName.setText(list.get(1));
            }
        }

    }

    private void getInform(String paramType,String param){
        MyAsyncTask<getStudentBasicInfoResponse> gs = new MyAsyncTask<getStudentBasicInfoResponse>(this,WebServiceConnector.METHOD_GETBASICINFOBYSNO,mProgressBar);
        gs.setListener(new getStudentBasicInfoResponse() {
            @Override
            public void getResult(ArrayList<String> result) {
                result = MyInfoFragment.FormatList(result);
                setDefaultFragment(mFragmentManager,result);
                mName.setText(result.get(1));
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

    public void RestoreAvatar() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String avatar = pref.getString(mAvatarKey, "");
        if (!avatar.equals(""))
            mAvatar.setImageBitmap(PhotoUtil.getBitmapbyString(avatar));
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
        mSnoVaule = getIntent().getStringExtra(mSnoKey);
        View headerView = navigationView.getHeaderView(0);
        mSno = headerView.findViewById(R.id.side_sno);
        mSno.setText(mSnoVaule);
        mName  = headerView.findViewById(R.id.side_name);
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
                    picUri = PhotoUtil.useCamera(this);
                else {
                    // 没有获取到权限，重新请求
                    if (true) {

                    }
                    Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQ_PERMISSION_FOR_ALBUM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    PhotoUtil.selectFromAlbum(this);
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
            picUri = PhotoUtil.applyPermissionForCamera(this);
        else if (by == BY_ALBUM) {
            PhotoUtil.applyPermissionForAlbum(this);
            isAlbum = true;
        }
        cimg = img;
    }

    @Override
    public void setUploadPicPath(TextView textView, int by) {
        repairPicDir = textView;
        isRepairPicDir = true;
        if (by == BY_CAMERA)
            picUri = PhotoUtil.applyPermissionForCamera(this);
        else if (by == BY_ALBUM)
            PhotoUtil.applyPermissionForAlbum(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CAMERA:
                    //拍照回调
                    if(picUri == null)
                        return;
                    if (isRepairPicDir) {
                        //是否为报修照片
                        PhotoUtil.saveCompressFile(PhotoUtil.picName,PhotoUtil.REPAIRPHOTO_QUALITY);
                        repairPicDir.setText(PhotoUtil.compressFileName.getAbsolutePath());
                        break;
                    }
                    PhotoUtil.cropPicture(this, picUri);
                    break;
                case REQ_ALBUM:
                    //相册回调
                    picUri = data.getData();
                    if(isRepairPicDir){
                        repairPicDir.setText(PhotoUtil.getRealFilePath(this,picUri));
                        break;
                    }
                    PhotoUtil.cropPicture(this, picUri);
                    break;
                case REQ_CUT:
                    //压缩图片文件
                    if(!isRepairPicDir){
                        //不是报修图片，设置头像
                        PhotoUtil.saveCompressFile(PhotoUtil.cropPicName,PhotoUtil.AVATAR_QUALITY);
                        Bitmap bitmap = BitmapFactory.decodeFile(PhotoUtil.cropPicName.getAbsolutePath());
                        cimg.setImageBitmap(bitmap);
                        mAvatar.setImageBitmap(bitmap);
                        UpdateAvatar();
                    } else {
                        PhotoUtil.saveCompressFile(PhotoUtil.cropPicName,PhotoUtil.REPAIRPHOTO_QUALITY);
                        repairPicDir.setText(PhotoUtil.compressFileName.getAbsolutePath());
                    }
                    //删除临时文件
                    if(PhotoUtil.picName.exists())
                        PhotoUtil.picName.delete();
                    if(PhotoUtil.cropPicName.exists())
                        PhotoUtil.cropPicName.delete();
                    /*if (isAlbum) {
                        if(PhotoUtil.cropPicName.exists())
                            PhotoUtil.cropPicName.delete();
                        if(PhotoUtil.compressFileName.exists())
                            PhotoUtil.compressFileName.delete();
                    }
                    else if(!isRepairPicDir){
                        if(PhotoUtil.picName.exists())
                            PhotoUtil.picName.delete();
                        if(PhotoUtil.cropPicName.exists())
                            PhotoUtil.cropPicName.delete();
                        if(PhotoUtil.compressFileName.exists())
                            PhotoUtil.compressFileName.delete();
                    }*/
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long mLastPressBackTime = System.currentTimeMillis();
            if(mLastPressBackTime - mPreviousPressBackTime > 1000){
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                mPreviousPressBackTime = mLastPressBackTime;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            Log.d("","itemName:"+"account");
        } else if (id == R.id.nav_repair) {
            if (mRepairFragment == null) {
                mRepairFragment = RepairFragment.GetThisFragment(mSnoVaule);
                FragmentCollector.addFragment(mRepairFragment);
                ft.add(R.id.fragment_container, mRepairFragment,mRepairFragment.getClass().getName());
            }
            FragmentCollector.HideAllFragment(ft);
            ft.show(mRepairFragment);
            Log.d("","itemName:"+"repair");
        } else if (id == R.id.nav_els) {
            if (mELSchool == null) {
                mELSchool = ELSFragment.GetThisFragment(mSnoVaule);
                FragmentCollector.addFragment(mELSchool);
                ft.add(R.id.fragment_container, mELSchool,mELSchool.getClass().getName());
            }
            FragmentCollector.HideAllFragment(ft);
            ft.show(mELSchool);
            Log.d("","itemName:"+"elschool");
        } else if (id == R.id.nav_settings) {
            if (mSettings == null) {
                mSettings = new Settings();
                FragmentCollector.addFragment(mSettings);
                ft.add(R.id.fragment_container, mSettings,mSettings.getClass().getName());
            }
            FragmentCollector.HideAllFragment(ft);
            ft.show(mSettings);
            Log.d("","itemName:"+"settings");
        } else if (id == R.id.nav_about) {
            if(mAboutFragment == null){
                mAboutFragment = new AboutFragment();
                FragmentCollector.addFragment(mAboutFragment);
                ft.add(R.id.fragment_container,mAboutFragment);
            }
            FragmentCollector.HideAllFragment(ft);
            ft.show(mAboutFragment);
            Log.d("","itemName:"+"about");
        } else if (id == R.id.nav_backLate) {
            if(mBackLateFragment == null){
                mBackLateFragment = BackLateFragment.GetThisFragment(mSnoVaule);
                FragmentCollector.addFragment(mBackLateFragment);
                ft.add(R.id.fragment_container,mBackLateFragment);
            }
            FragmentCollector.HideAllFragment(ft);
            ft.show(mBackLateFragment);
            Log.d("","itemName:"+"backlate");
        }
        ft.commitAllowingStateLoss();
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
        mBackLateFragment = new BackLateFragment();
        FragmentCollector.addFragment(mBackLateFragment);
        ft.add(R.id.fragment_container,mBackLateFragment);
        mAboutFragment = new AboutFragment();
        FragmentCollector.addFragment(mAboutFragment);
        ft.add(R.id.fragment_container,mAboutFragment);
        ft.commitAllowingStateLoss();
    }

    public void setDefaultFragment(FragmentManager fm,ArrayList<String> strList) {
        mMInfoFragment = MyInfoFragment.GetThisFragment(this,strList);
        FragmentCollector.addFragment(mMInfoFragment);
        fm.beginTransaction().add(R.id.fragment_container, mMInfoFragment, mMInfoFragment.getClass().getName()).show(mMInfoFragment).commit();
    }

    private void UpdateAvatar(){
        MyAsyncTask<getBooleanTypeResponse> myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(this,WebServiceConnector.METHOD_UPDATEAVATAR);
        myAsyncTask.setListener(new getBooleanTypeResponse() {
            @Override
            public void showDialog(ArrayList<String> result) {
                if(result == null){
                    DialogUtil.showNegativeTipsDialog(activity,"请求失败！");
                    return;
                }
                if(result.size() > 0 && result.get(0).equals("true")){
                    Toast.makeText(activity,"修改成功！",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(activity,"修改失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        myAsyncTask.execute(getParamType(),getInput());
    }

    private ArrayList<String> getParamType(){
        ArrayList<String> paramType = new ArrayList<>();
        paramType.clear();
        paramType.add(WebServiceConnector.PARAMTYPE_SNO);
        paramType.add(WebServiceConnector.PARAMTYPE_PHOTO);
        return paramType;
    }

    private ArrayList<String> getInput(){
        ArrayList<String> input = new ArrayList<>();
        input.clear();
        String sno = getIntent().getStringExtra(mSnoKey);
        String photo = PhotoUtil.convertFileToString(PhotoUtil.compressFileName);
        input.add(sno);
        input.add(photo);
        return input;
    }

    public static void StartThisActivity(Context context, String Sno) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(mSnoKey, Sno);
        ((Activity) context).finish();
        context.startActivity(intent);
    }

}
