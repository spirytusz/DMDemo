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

import com.zspirytus.dmdemo.Interface.getSnobyAccountResponse;
import com.zspirytus.dmdemo.JavaSource.Manager.ActivityManager;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.PhotoUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.util.ArrayList;

public class LoginActivity extends BaseActivity {

    private static final String isRememberKey = "isRemember";
    private static final String mAccountKey = "Account";
    private static final String mPwdKey = "PassWord";
    private static final String mAvatarKey = "Avatar";
    private  static final String mSnoKey = "Sno";
    private static final String isExitKey = "isExit";
    private  static final String AccountInfoFileName = "accountInfo";
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
        pref = getSharedPreferences(AccountInfoFileName,MODE_PRIVATE);
        LoadPane();
        RestoreEditArea();
    }

    /**
     * if exists account info,skip this activity and start next activity
     */
    private void skip(){
        SharedPreferences pref = getSharedPreferences(AccountInfoFileName,MODE_PRIVATE);
        String sno = pref.getString(mSnoKey,"");
        if(!sno.equals("")){
            boolean isManager = sno.indexOf("5281") != -1;
            if(!isManager){
                MainActivity.StartThisActivity(this,sno);
            }
            else
                AdminActivity.StartThisActivity(this,sno);
        }

    }

    /**
     * init Pane
     */
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
                        Bitmap bitmap = PhotoUtil.getBitmapbyString(pref.getString(mAvatarKey,""));
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
                if(!isEmpty(mAccount,mPwd)){
                    if(mAccountValue.equals("admin") && mPwdValue.equals("admin")){
                        AdminActivity.StartThisActivity(activity,WebServiceConnector.PARAMTYPE_ENO);
                    }
                    StartNextActivity();
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

    /**
     * set StatusBar Background Color
     */
    private void setStatusBarBackgroundColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * if exists account info,restore the EditText
     */
    private void RestoreEditArea(){
        SharedPreferences pref = getSharedPreferences(AccountInfoFileName,Context.MODE_PRIVATE);
        boolean isRemember = pref.getBoolean(isRememberKey,false);
        if(isRemember){
            mCheckBox.setChecked(true);
            mAccount.setText(pref.getString(mAccountKey,""));
            mPwd.setText(pref.getString(mPwdKey,""));
            if(!pref.getString(mAvatarKey,"").equals("")){
                mAvatar.setImageBitmap(PhotoUtil.getBitmapbyString(pref.getString(mAvatarKey,"")));
            }
        }
    }

    /**
     * if it is new account,clear old one
     */
    private void clear(){
        SharedPreferences pref = getSharedPreferences(AccountInfoFileName,Context.MODE_PRIVATE);
        String oldAccount = pref.getString(mAccountKey,"");
        String newAccount = mAccount.getText().toString();
        boolean isNewAccount = !oldAccount.equals(newAccount);
        if(isNewAccount){
            pref.edit().clear().commit();
        }
    }

    /**
     *  If checkBox is checked,remember account and pwd;
     *  else clear the remembered account and pwd
     * @param response Sno or Eno
     */
    private void RememberItOrClearIt(ArrayList<String> response){
        if(mCheckBox.isChecked()){
            // clear oldAccountInfo
            clear();
            //save newAccountInfo
            String account = mAccountValue;
            String pwd = mPwdValue;
            SharedPreferences.Editor editor = getSharedPreferences(AccountInfoFileName,Context.MODE_PRIVATE).edit();
            editor.putString(mSnoKey,response.get(0));
            editor.putString(mAccountKey,account);
            editor.putString(mPwdKey,pwd);
            editor.putBoolean(isRememberKey,true);
            editor.apply();
        } else {
            SharedPreferences.Editor editor = getSharedPreferences(AccountInfoFileName,MODE_PRIVATE).edit();
            editor.putBoolean(isRememberKey,false);
            editor.apply();
        }
    }

    /**
     * Request WebService and start next activity according to response
     */
    private void StartNextActivity(){
            MyAsyncTask<getSnobyAccountResponse> myAsyncTask = new MyAsyncTask<getSnobyAccountResponse>(this, WebServiceConnector.METHOD_GETNO);
            myAsyncTask.setListener(new getSnobyAccountResponse() {
                @Override
                public void getSno(ArrayList<String> result) {
                    if(result != null) {
                        if(result.size() > 0 && !result.get(0).equals("-1")){
                            //save AccountInfo
                            RememberItOrClearIt(result);
                            boolean isManager = result.get(0).indexOf("5281") != -1;
                            if(!isManager)
                                MainActivity.StartThisActivity(activity,result.get(0));
                            else
                                AdminActivity.StartThisActivity(activity,result.get(0));
                        } else
                            Toast.makeText(activity,"账号密码错误...",Toast.LENGTH_SHORT).show();
                    } else {
                        DialogUtil.showNegativeTipsDialog(activity);
                    }
                }
            });
            myAsyncTask.execute(getParamType(),getInput());
    }

    /**
     * get WebService Method Params Name
     * @return Params Name
     */
    private ArrayList<String> getParamType(){
        ArrayList<String> paramType = new ArrayList<>();
        paramType.clear();
        paramType.add(WebServiceConnector.PARAMTYPE_ACCOUNT);
        paramType.add(WebServiceConnector.PARAMTYPE_PWD);
        return paramType;
    }

    /**
     * get WebService Method Params
     * @return Params
     */
    private ArrayList<String> getInput(){
        ArrayList<String> input = new ArrayList<>();
        input.clear();
        input.add(mAccountValue);
        input.add(mPwdValue);
        return input;
    }

    /**
     *  is et1 or et1 empty?
     * @param et1 EditText et1
     * @param et2 EditText et2
     * @return is et1 or et1 empty
     */
    private boolean isEmpty(EditText et1,EditText et2){
        return et1.getText().toString().isEmpty()||et2.getText().toString().isEmpty();
    }

    /**
     * Start This Activity
     * @param context Context
     */
    public static void StartThisActivity(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        intent.putExtra(isExitKey,true);
        context.startActivity(intent);
    }
}
