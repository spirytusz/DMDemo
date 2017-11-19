package com.zspirytus.dmdemo.JavaSource;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

import com.zspirytus.dmdemo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ZSpirytus on 2017/10/15.
 */

public class PhotoUtils {

    public static final File picName = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/dmdemo/" + "temp.jpg");
    public static final File cropPicName = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/dmdemo/crop.jpg");
    private static final int REQ_CAMERA = 0x01;
    private static final int REQ_ALBUM = 0x02;
    private static final int REQ_CUT = 0x04;
    private static final int REQ_PERMISSION_FOR_CAMERA = 0x10;
    private static final int REQ_PERMISSION_FOR_ALBUM = 0x20;

    /** 申请权限并打开相机
     *
     * @param activity 调用该方法的活动
     * @return 权限已赋予，返回照片Uri；权限未赋予，返回null
     */
    public static Uri applyPermissionForCamera(final Activity activity){
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        if(ContextCompat.checkSelfPermission(activity, permissions[0]) == PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(activity, permissions[1]) == PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(activity, permissions[2]) == PackageManager.PERMISSION_GRANTED
                ){
            Uri uri = useCamera(activity);
            return uri;
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setTitle(activity.getString(R.string.Need_Permission));
            dialog.setMessage(activity.getString(R.string.The_Application_Need_the_Permission));
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String[] permissions = {
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    };
                    activity.requestPermissions(permissions,REQ_PERMISSION_FOR_CAMERA);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }
        return null;
    }

    /** 申请权限并打开相册
     *
     * @param activity 调用该方法的活动
     */
    public static void applyPermissionForAlbum(final Activity activity){
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        if(ContextCompat.checkSelfPermission(activity, permissions[0]) == PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(activity, permissions[1]) == PackageManager.PERMISSION_GRANTED
                ){
            selectFromAlbum(activity);
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setTitle(activity.getString(R.string.Need_Permission));
            dialog.setMessage(activity.getString(R.string.The_Application_Need_the_Permission));
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String[] permissions = {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    };
                    activity.requestPermissions(permissions,REQ_PERMISSION_FOR_ALBUM);
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

    /**  打开相机拍照拍照
     *
     * @param activity 调用该方法的活动
     * @return 返回拍照所得的照片的Uri
     */
    public static Uri useCamera(Activity activity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        picName.getParentFile().mkdirs();
        Uri picUri = FileProvider.getUriForFile(activity, "com.zspirytus.dmdemo.Activity.MainActivity.fileprovider", picName);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        activity.startActivityForResult(intent, REQ_CAMERA);
        return picUri;
    }

    /**  打开相册选择图片
     *
     * @param activity 调用该方法的活动
     */
    public static void selectFromAlbum(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, REQ_ALBUM);
    }

    /**  截取图片
     *
     * @param activity 调用该方法的活动
     * @param picUri 需要裁剪的图片的Uri
     * @return 裁剪所得照片的Uri
     */
    public static Uri cropPicture(Activity activity,Uri picUri) {
        if(!cropPicName.exists())
            cropPicName.getParentFile().mkdirs();
        Uri cropPicUri = Uri.fromFile(cropPicName);
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
        activity.startActivityForResult(intent, REQ_CUT);
        return cropPicUri;
    }

    /** String to Bitmap
     *
     * @param str 转换成String的图片
     * @return 图片Bitmap
     */
    public static Bitmap getBitmapbyString(String str){
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

    /** Bitmap to String
     *
     * @param bitmap 需要转String的图片
     * @return String型的图片
     */
    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }

    /**  String to Bitmap
     *
     * @param str  String型图片
     * @return     Bitmap型图片
     */
    public static Bitmap convertStringToIcon(String str)
    {
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(str, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**  压缩图片
     *
     * @param bitmap 需要压缩的图片
     * @param photoSize   图片大小
     * @return 压缩完成的图片
     */
    public static Bitmap CompressBitmap(Bitmap bitmap,int photoSize){
        int maxSize = 1024*384;
        Log.d("","Size Test:\t"+(bitmap.getByteCount()/1024)+"KB");
        if(photoSize <= maxSize)
            return bitmap;
        float inSampleSize = (float)maxSize/(float) photoSize;
        Matrix matrix = new Matrix();
        matrix.setScale(inSampleSize,inSampleSize);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return newBitmap;
    }

    /** 保存Bitmap图片到手机
     *
     * @param file 保存路径
     * @param bm 需要保存的图片
     */
    public static void saveNewBitmap(File file,Bitmap bm){
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Uri saveCompressBitmap(Context context,Uri bitmapUri){
        try{
            InputStream input = context.getContentResolver().openInputStream(bitmapUri);
            Log.d("","inputStream Test:\t"+Boolean.toString(input == null));
            Log.d("","inputStream Test:\t"+input);
            input.close();
            Bitmap oldBitmap = BitmapFactory.decodeStream(input);
            Bitmap newBitmap = CompressBitmap(oldBitmap,2);
            saveNewBitmap(picName,newBitmap);
            return FileProvider.getUriForFile(context, "com.zspirytus.dmdemo.Activity.MainActivity.fileprovider", picName);
        }catch (FileNotFoundException fne){
            fne.printStackTrace();
            return null;
        } catch (IOException ie){
            ie.printStackTrace();
            return null;
        }
    }

    /** 保存头像到SharedPreferences
     *
     * @param context 上下文
     * @param bitmap 头像
     */
    public static void saveAvatar(Context context, Bitmap bitmap){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("Avatar",PhotoUtils.convertIconToString(bitmap));
        editor.apply();
    }
}
