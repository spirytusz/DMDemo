package com.zspirytus.dmdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zspirytus.dmdemo.JavaSource.ActivityManager;
import com.zspirytus.dmdemo.JavaSource.PhotoUtils;
import com.zspirytus.dmdemo.R;

import java.io.File;

public class LoginActivity extends BaseActivity {

    private static final String isRememberKey = "isRemember";
    private static final String mAccountKey = "Account";
    private static final String mPwdKey = "PassWord";
    private static final String mAvatarKey = "Avatar";
    private static final String isExitKey = "isExit";
    private final Activity activity = this;

    private ImageView mAvatar;
    private EditText mAccount;
    private EditText mPwd;
    private ImageView isVisibleButton;
    private CheckBox mCheckBox;
    private Button mButton;
    private TextView mForget;
    private TextView mRegistration;
    private SharedPreferences pref;
    private String mAccountValue;
    private String mPwdValue;

    private boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Boolean isExit = getIntent().getBooleanExtra(isExitKey,false);
        if(!isExit)
            skip();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityManager.addActivity(this);
        pref = getSharedPreferences("data",MODE_PRIVATE);
        LoadPane();
        RestoreEditArea();
    }

    private void skip(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String account = pref.getString(mAccountKey,"");
        String pwd = pref.getString(mPwdKey,"");
        boolean isLegal = check(account,pwd);
        if(isLegal)
            MainActivity.StartThisActivity(this,account);
    }

    private boolean check(String account,String pwd){
        if(account.equals("")||pwd.equals(""))
            return false;
        return  true;
    }

    private void LoadPane(){
        final ActionBar actionbar = this.getSupportActionBar();
        actionbar.hide();
        setStatusBarBackgroundColor();
        mAvatar = (ImageView) findViewById(R.id.mAvatar);
        mAccount = (EditText) findViewById(R.id.edittext_account);
        mAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String account = pref.getString(mAccountKey,"");
                if(!mAccount.getText().toString().equals(account)){
                    mAvatar.setImageResource(R.drawable.ic_account_circle_black_24dp);
                }
                else if(!account.equals("")){
                    if(!pref.getString(mAvatarKey,"").equals("")){
                        Bitmap bitmap = PhotoUtils.getBitmapbyString(pref.getString(mAvatarKey,""));
                        mAvatar.setImageBitmap(bitmap);
                    }
                }
            }
        });
        mPwd = (EditText) findViewById(R.id.edittext_pwd);
        isVisibleButton = (ImageView) findViewById(R.id.pwd_visible);
        isVisibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isVisible){
                    mPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isVisibleButton.setImageResource(R.drawable.ic_visibility_black_48dp);
                    isVisible = true;
                }else{
                    mPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isVisibleButton.setImageResource(R.drawable.ic_visibility_off_black_48dp);
                    isVisible = false;
                }
            }
        });
        mCheckBox = (CheckBox) findViewById(R.id.remember_pwd);
        mButton = (Button) findViewById(R.id.button_login);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAccountValue = mAccount.getText().toString();
                mPwdValue = mPwd.getText().toString();
                if(Check(mAccountValue,mPwdValue)&&!isEmpty(mAccount,mPwd)){
                    if(mCheckBox.isChecked()){
                        RememberIt();
                    }else{
                        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                        editor.putBoolean(isRememberKey,false);
                        editor.apply();
                    }
                    MainActivity.StartThisActivity(activity,mAccountValue);
                }

            }
        });
        mForget = (TextView) findViewById(R.id.forget_pwd);
        mForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubLoginActivity.StartThisActivity(activity,2);
            }
        });
        mRegistration = (TextView) findViewById(R.id.new_member);
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubLoginActivity.StartThisActivity(activity,1);
            }
        });
    }

    private void setStatusBarBackgroundColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void RestoreEditArea(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        boolean isRemember = pref.getBoolean(isRememberKey,false);
        if(isRemember){
            mCheckBox.setChecked(true);
            mAccount.setText(pref.getString(mAccountKey,""));
            mPwd.setText(pref.getString(mPwdKey,""));
            if(!pref.getString(mAvatarKey,"").equals("")){
                mAvatar.setImageBitmap(PhotoUtils.getBitmapbyString(pref.getString(mAvatarKey,"")));
            }
        }
    }

    public void clear(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String oldAccount = pref.getString(mAccountKey,"");
        String newAccount = mAccount.getText().toString();
        boolean isNewAccount = !oldAccount.equals(newAccount);
        if(isNewAccount){
            pref.edit().clear().commit();
        }
    }

    public void RememberIt(){
        clear();
        String account = mAccount.getText().toString();
        String pwd = mPwd.getText().toString();
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString(mAccountKey,account);
        editor.putString(mPwdKey,pwd);
        editor.putBoolean(isRememberKey,true);
        editor.apply();
    }

    public boolean Check(String Account,String Pwd){
        return true;
    }

    public boolean isEmpty(EditText et1,EditText et2){
        return et1.getText().toString().isEmpty()||et2.getText().toString().isEmpty();
    }

    public static void StartThisActivity(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        intent.putExtra(isExitKey,true);
        context.startActivity(intent);
    }
}
