package com.zspirytus.dmdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.zspirytus.dmdemo.Fragment.RLManagerFragment;
import com.zspirytus.dmdemo.Fragment.RepManagerFragment;
import com.zspirytus.dmdemo.Fragment.SLSManagerFragment;
import com.zspirytus.dmdemo.Interface.getRLBasicInfoByManager;
import com.zspirytus.dmdemo.Interface.getRepBasicInfoByManager;
import com.zspirytus.dmdemo.Interface.getSLSBasicInfoByManager;
import com.zspirytus.dmdemo.JavaSource.Manager.FragmentCollector;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.util.ArrayList;

public class AdminActivity extends BaseActivity {

    private static final String TAG = "AdminActivity";
    private static final String mEnoKey = "Eno";

    private ProgressBar mProgressBar;
    private FragmentManager mFragmentManager;
    private RepManagerFragment mRepManagerFragment;
    private SLSManagerFragment mSLSManagerFragment;
    private RLManagerFragment mRLManagerFragment;

    private boolean flagOfLoaded = false;
    private int inThisFragment = R.id.navigation_rep;

    private MyAsyncTask<getRepBasicInfoByManager> myAsyncTask0;
    private MyAsyncTask<getSLSBasicInfoByManager> myAsyncTask1;
    private MyAsyncTask<getRLBasicInfoByManager> myAsyncTask2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            FragmentCollector.HideAllFragment(ft);
            switch (item.getItemId()) {
                case R.id.navigation_rep:
                    if (mRepManagerFragment == null) {
                        getInfo(R.id.navigation_rep,ft);
                        return true;
                    }
                    FragmentCollector.HideAllFragment(ft);
                    ft.show(mRepManagerFragment);
                    ft.commitAllowingStateLoss();
                    inThisFragment = R.id.navigation_rep;
                    return true;
                case R.id.navigation_sls:
                    if (mSLSManagerFragment == null) {
                        getInfo(R.id.navigation_sls,ft);
                        return true;
                    }
                    FragmentCollector.HideAllFragment(ft);
                    ft.show(mSLSManagerFragment);
                    ft.commitAllowingStateLoss();
                    inThisFragment = R.id.navigation_sls;
                    return true;
                case R.id.navigation_rl:
                    if (mRLManagerFragment == null) {
                        getInfo(R.id.navigation_rl,ft);
                        return true;
                    }
                    FragmentCollector.addFragment(mRLManagerFragment);
                    ft.show(mRLManagerFragment);
                    ft.commitAllowingStateLoss();
                    inThisFragment = R.id.navigation_rl;
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        LoadPane();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(flagOfLoaded && item.getItemId() == R.id.refresh){
            //do refresh
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            getInfo(inThisFragment,ft);
        } else if(item.getItemId() == R.id.backToLogin){
            this.finish();
            LoginActivity.StartThisActivity(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        cancelTask();
        super.onDestroy();
    }

    /**
     * stop doing AsyncTask
     */
    private void cancelTask(){
        if(myAsyncTask0 != null && myAsyncTask0.getStatus() == AsyncTask.Status.RUNNING)
            myAsyncTask0.cancel(true);
        if(myAsyncTask1 != null && myAsyncTask1.getStatus() == AsyncTask.Status.RUNNING)
            myAsyncTask1.cancel(true);
        if(myAsyncTask2 != null && myAsyncTask2.getStatus() == AsyncTask.Status.RUNNING)
            myAsyncTask2.cancel(true);
    }

    /**
     * init Pane
     */
    private void LoadPane() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.dia_toolbar);
        toolbar.setTitle(getIntent().getStringExtra(mEnoKey));
        setSupportActionBar(toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.admin_progressbar);
        mProgressBar.setVisibility(View.GONE);
        mFragmentManager = getSupportFragmentManager();
        setDefaultFragment();
    }

    /**
     * set Default Fragment
     */
    private void setDefaultFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        getInfo(R.id.navigation_rep,ft);
    }

    /**
     *  get Basic Info and get adapter of listView
     * @param fragmentId in which fragment
     * @param ft         fragmentTransaction
     */
    private void getInfo(int fragmentId,final FragmentTransaction ft) {
        switch (fragmentId) {
            case R.id.navigation_rep:
                myAsyncTask0
                        = new MyAsyncTask<getRepBasicInfoByManager>(this, WebServiceConnector.METHOD_GETREPBASICBYMANAGER, mProgressBar);
                myAsyncTask0.setListener(new getRepBasicInfoByManager() {
                    @Override
                    public void getResult(ArrayList<String> result) {
                        initAndShowRepFragment(ft,result);
                        flagOfLoaded = true;
                    }
                });
                myAsyncTask0.execute(getParamType(), getInput());
                break;
            case R.id.navigation_sls:
                myAsyncTask1
                        = new MyAsyncTask<getSLSBasicInfoByManager>(this, WebServiceConnector.METHOD_GETSLSBASICBYMANAGER, mProgressBar);
                myAsyncTask1.setListener(new getSLSBasicInfoByManager() {
                    @Override
                    public void getResult(ArrayList<String> result) {
                        initAndShowSLSFragment(ft,result);
                        flagOfLoaded = true;
                    }
                });
                myAsyncTask1.execute(getParamType(), getInput());
                break;
            case R.id.navigation_rl:
                myAsyncTask2
                        = new MyAsyncTask<getRLBasicInfoByManager>(this, WebServiceConnector.METHOD_GETRLBASICBYMANAGER,mProgressBar);
                myAsyncTask2.setListener(new getRLBasicInfoByManager() {
                    @Override
                    public void getResult(ArrayList<String> result) {
                        initAndShowRLFragment(ft,result);
                        flagOfLoaded = true;
                    }
                });
                myAsyncTask2.execute(getParamType(), getInput());
                break;
        }

    }

    /**
     *  get WebService Method Params Name
     * @return Params Name
     */
    private ArrayList<String> getParamType() {
        ArrayList<String> paramType = new ArrayList<>();
        paramType.add(WebServiceConnector.PARAMTYPE_ENO);
        return paramType;
    }

    /**
     *  get WebService Method Params
     * @return Params
     */
    private ArrayList<String> getInput() {
        ArrayList<String> input = new ArrayList<>();
        input.add(getIntent().getStringExtra(mEnoKey));
        return input;
    }

    /**
     * init RepManagerFragment and show it
     * @param ft Fragment Transaction
     * @param result the response of WebService Method
     */
    private void initAndShowRepFragment(FragmentTransaction ft,ArrayList<String> result){
        if (mRepManagerFragment == null) {
            mRepManagerFragment = RepManagerFragment.GetThisFragment(result);
            FragmentCollector.addFragment(mRepManagerFragment);
            ft.add(R.id.admin_fragment_container, mRepManagerFragment);
        }
        FragmentCollector.HideAllFragment(ft);
        ft.show(mRepManagerFragment);
        ft.commitAllowingStateLoss();
    }

    /**
     *  init SLSManagerFragment and show it
     * @param ft Fragment Transaction
     * @param result the response of WebService Method
     */
    private void initAndShowSLSFragment(FragmentTransaction ft,ArrayList<String> result){
        if (mSLSManagerFragment == null) {
            mSLSManagerFragment = SLSManagerFragment.GetThisFragment(result);
            FragmentCollector.addFragment(mSLSManagerFragment);
            ft.add(R.id.admin_fragment_container, mSLSManagerFragment);
        }
        FragmentCollector.HideAllFragment(ft);
        ft.show(mSLSManagerFragment);
        ft.commitAllowingStateLoss();
    }

    /**
     *  init RLManagerFragment and show it
     * @param ft Fragment Transaction
     * @param result the response of WebService Method
     */
    private void initAndShowRLFragment(FragmentTransaction ft,ArrayList<String> result){
        if (mRLManagerFragment == null) {
            mRLManagerFragment = RLManagerFragment.GetThisFragment(result);
            FragmentCollector.addFragment(mRLManagerFragment);
            ft.add(R.id.admin_fragment_container, mRLManagerFragment);
        }
        FragmentCollector.addFragment(mRLManagerFragment);
        ft.show(mRLManagerFragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * Start This Activity
     * @param context Context
     * @param Eno Administrator No
     */
    public static void StartThisActivity(Context context, String Eno) {
        Intent intent = new Intent(context, AdminActivity.class);
        intent.putExtra(mEnoKey, Eno);
        ((Activity) context).finish();
        context.startActivity(intent);
    }

}
