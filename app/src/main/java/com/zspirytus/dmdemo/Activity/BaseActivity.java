package com.zspirytus.dmdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ZSpirytus on 2017/9/28.
 */

public class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public static void CloseKeyBoard(Activity activity){
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
