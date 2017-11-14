package com.zspirytus.dmdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zspirytus.dmdemo.JavaSource.ActivityManager;
import com.zspirytus.dmdemo.R;

public class SubLoginActivity extends AppCompatActivity {

    private static final String viewKey = "viewKey";
    private static final int REGISTER = 1;
    private static final int MODIFYPWD = 2;
    private EditText mSno;
    private EditText mAccount;
    private EditText mPwd;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        Intent intent = getIntent();
        int view = intent.getIntExtra(viewKey,0);
        mySetContentView(view);
    }

    @Override
    protected void onDestroy(){
        ActivityManager.removeActivity(this);
        super.onDestroy();
    }

    private void mySetContentView(int view){
        switch(view){
            case REGISTER:
                setContentView(R.layout.activity_register);
                initRegView();
                break;
            case MODIFYPWD:
                setContentView(R.layout.activity_modifypwd);
                initModifyPwdView();
                break;
        }
    }

    private void initRegView(){
        mSno = (EditText)findViewById(R.id.edittext_reg_sno);
        mAccount = (EditText)findViewById(R.id.edittext_reg_account);
        mPwd = (EditText)findViewById(R.id.edittext_reg_pwd);
        button = (Button)findViewById(R.id.button_reg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.New_Member);
    }

    private void initModifyPwdView(){
        mAccount = (EditText)findViewById(R.id.edittext_mPwd_account);
        mPwd = (EditText)findViewById(R.id.edittext_mPwd_pwd);
        button = (Button)findViewById(R.id.button_mPwd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.Modify_Pwd);
    }

    public static void StartThisActivity(Context context,int view){
        Intent intent = new Intent(context, SubLoginActivity.class);
        intent.putExtra(viewKey, view);
        context.startActivity(intent);
    }
}
