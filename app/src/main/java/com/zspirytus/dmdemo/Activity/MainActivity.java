package com.zspirytus.dmdemo.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.zspirytus.dmdemo.Fragment.ELSFragment;
import com.zspirytus.dmdemo.Fragment.MyInfoFragment;
import com.zspirytus.dmdemo.Fragment.RepairFragment;
import com.zspirytus.dmdemo.Fragment.RepairScheduleFragment;
import com.zspirytus.dmdemo.Fragment.Settings;
import com.zspirytus.dmdemo.Interface.SetMyInfoAvatar;
import com.zspirytus.dmdemo.Interface.SetUploadPicPath;
import com.zspirytus.dmdemo.JavaSource.ActivityManager;
import com.zspirytus.dmdemo.R;
import com.zspirytus.dmdemo.Reproduction.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends BaseActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        SetMyInfoAvatar,
        SetUploadPicPath{

    private static final String TAG = "MainActivity";
    private static final String mAccountKey = "Account";
    private static final String mAvatarKey = "Avatar";
    private static final String hasCustomAvatar = "CustomAvatar";
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
    private RepairScheduleFragment mRSF;
    private TextView account;
    private CircleImageView mAvatar;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    private String mAccountVaule;
    private File picName;
    private File cropPicName;
    private Uri picUri;
    private Uri cropPicUri;
    private boolean isAlbum = false;
    private boolean isRepairPicDir = false;

    private Bitmap bitmap;
    private CircleImageView cimg;
    private TextView repairPicDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager.addActivity(this);
        LoadPane();
        setResult(RESULT_OK);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void setUploadPicPath(TextView textView,int by){
        repairPicDir = textView;
        isRepairPicDir = true;
        if(by == BY_CAMERA)
            applyPermissionForCamera();
        else if(by == BY_ALBUM)
            applyPermissionForAlbum();
    }

    private void setStatusBarBackgroundColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void RestoreAvatar(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        if(pref.getBoolean(hasCustomAvatar,false))
            mAvatar.setImageBitmap(getBitmapbyString(pref.getString(mAvatarKey,"")));
    }

    public Bitmap getBitmapbyString(String str){
        Bitmap bitmap = null;
        try{
            byte[] bitmapArray;
            bitmapArray = Base64.decode(str,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray,0,bitmapArray.length);
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    private void LoadPane() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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
        setDefaultFragment(mFragmentManager);
        RestoreAvatar();
    }

    public void useCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        picName = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/dmdemo/" + "temp.jpg");
        picName.getParentFile().mkdirs();
        picUri = FileProvider.getUriForFile(this, "com.zspirytus.dmdemo.Activity.MainActivity.fileprovider", picName);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        startActivityForResult(intent, REQ_CAMERA);
    }

    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQ_ALBUM);
    }

    private void cropPicture() {
        cropPicName = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/dmdemo/crop.jpg");
        if(!cropPicName.exists())
            cropPicName.getParentFile().mkdirs();
        cropPicUri = Uri.fromFile(cropPicName);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(picUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropPicUri);
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQ_CUT);
    }

    public String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    public void applyPermissionForCamera(){
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        if(ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this, permissions[2]) == PackageManager.PERMISSION_GRANTED
                ){
            useCamera();
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle(getString(R.string.Need_Permission));
            dialog.setMessage(getString(R.string.The_Application_Need_the_Permission));
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String[] permissions = {
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    };
                    requestPermissions(permissions,REQ_PERMISSION_FOR_CAMERA);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }
    }

    private void applyPermissionForAlbum(){
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        if(ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED
                ){
            selectFromAlbum();
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle(getString(R.string.Need_Permission));
            dialog.setMessage(getString(R.string.The_Application_Need_the_Permission));
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String[] permissions = {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    };
                    requestPermissions(permissions,REQ_PERMISSION_FOR_ALBUM);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQ_PERMISSION_FOR_CAMERA:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    useCamera();
                else{
                    // 没有获取到权限，重新请求
                    if(true){

                    }
                    Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQ_PERMISSION_FOR_ALBUM:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    selectFromAlbum();
                else{
                    // 没有获取到权限，重新请求
                    if(true){

                    }
                    Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void setAvatar(CircleImageView img,int by){
        if(by == BY_CAMERA)
            applyPermissionForCamera();
        else if (by == BY_ALBUM){
            applyPermissionForAlbum();
            isAlbum = true;
        }
        cimg = img;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch (requestCode){
                case REQ_CAMERA:
                    if(isRepairPicDir){
                        repairPicDir.setText(picName.getAbsolutePath());
                        break;
                    }
                    cropPicture();
                    break;
                case REQ_ALBUM:
                    picUri = data.getData();
                    cropPicture();
                    break;
                case REQ_CUT:
                    Bitmap bitmap = BitmapFactory.decodeFile(cropPicName.getAbsolutePath());
                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString(mAvatarKey,convertIconToString(bitmap));
                    editor.putBoolean(hasCustomAvatar,true);
                    editor.apply();
                    cimg.setImageBitmap(bitmap);
                    mAvatar.setImageBitmap(bitmap);
                    if(isAlbum)
                        cropPicName.delete();
                    else{
                        picName.delete();
                        cropPicName.delete();
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
        }else{
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
            if (mMInfoFragment == null) {
                mMInfoFragment = new MyInfoFragment();
                ft.add(R.id.fragment_container, mMInfoFragment);
            }
            HideAllFragment(ft);
            ft.show(mMInfoFragment);
        } else if (id == R.id.nav_repair) {
            if (mRepairFragment == null) {
                mRepairFragment = new RepairFragment();
                ft.add(R.id.fragment_container, mRepairFragment);
            }
            HideAllFragment(ft);
            ft.show(mRepairFragment);
        } else if (id == R.id.nav_els) {
            if (mELSchool == null) {
                mELSchool = new ELSFragment();
                ft.add(R.id.fragment_container, mELSchool);
            }
            HideAllFragment(ft);
            ft.show(mELSchool);
        } else if (id == R.id.nav_settings) {
            if (mSettings == null) {
                mSettings = new Settings();
                ft.add(R.id.fragment_container, mSettings);
            }
            HideAllFragment(ft);
            ft.show(mSettings);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_feedback) {

        }
        ft.commitAllowingStateLoss();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void HideAllFragment(FragmentTransaction ft) {
        if (mMInfoFragment != null)
            ft.hide(mMInfoFragment);
        if (mRepairFragment != null)
            ft.hide(mRepairFragment);
        if (mELSchool != null)
            ft.hide(mELSchool);
        if (mSettings != null)
            ft.hide(mSettings);
    }

    public Bitmap CompressBitMap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float widthScale = 196f / width;
        float heightScale = 196f / height;
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        Bitmap mNewBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return mNewBitmap;
    }

    public void setDefaultFragment(FragmentManager fm) {
        mMInfoFragment = new MyInfoFragment();
        fm.beginTransaction().add(R.id.fragment_container, mMInfoFragment).show(mMInfoFragment).commit();
    }

    public static void StartThisActivity(Context context, String account) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(mAccountKey, account);
        ((Activity) context).finish();
        context.startActivity(intent);
    }

}
