package com.zspirytus.dmdemo.JavaSource;

import android.graphics.Bitmap;

/**
 * Created by 97890 on 2017/9/20.
 */

public class RSFListViewItem {

    public Bitmap mBitmap;
    public String mTitle;
    public String mTime;

    public RSFListViewItem(Bitmap bitmap, String title, String time){
        mBitmap = bitmap;
        mTitle = title;
        mTime = time;
    }

    public RSFListViewItem(){}

    public void setmBitmap(Bitmap bitmap){
        mBitmap = bitmap;
    }

    public void setmTitle(String title){
        mTitle = title;
    }

    public void setmTime(String time){
        mTime = time;
    }

    public Bitmap getmBitmap(){
        return mBitmap;
    }

    public String getmTitle(){
        return mTitle;
    }

    public String getmTime(){
        return mTime;
    }
}
