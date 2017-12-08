package com.zspirytus.dmdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zspirytus.dmdemo.Fragment.DetailsInfoFragment;
import com.zspirytus.dmdemo.Interface.getRLDetailsInfoResponse;
import com.zspirytus.dmdemo.Interface.getRLInfoResponse;
import com.zspirytus.dmdemo.Interface.getRepBasInfoResponse;
import com.zspirytus.dmdemo.Interface.getRepDetailsInfoResponse;
import com.zspirytus.dmdemo.Interface.getSLSDetailsInfoResponse;
import com.zspirytus.dmdemo.Interface.getSLSInfoResponse;
import com.zspirytus.dmdemo.JavaSource.Manager.ActivityManager;
import com.zspirytus.dmdemo.JavaSource.ListViewModule.RSFListViewItem;
import com.zspirytus.dmdemo.JavaSource.Utils.DateUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.PhotoUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubMainActivity extends AppCompatActivity {

    private static final String TAG = "SubMainActivity";
    private static final String titleKey = "titleKey";
    private static final String typeKey = "typeKey";
    private static final String mSnoKey = "Sno";
    private final Activity activity = this;

    private int i;
    private String title;
    private String Sno;
    private ProgressBar mProgressBar;
    private ListView listView;
    private DetailsInfoFragment dif;

    private boolean isNextListView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_schedule);
        ActivityManager.addActivity(this);
        getArgs();
        LoadPane();
        GetMessage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e(TAG,TAG+"OptionMenu");
        getMenuInflater().inflate(R.menu.blank, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(isNextListView){
                //listView.setAdapter(mBasicInfoAdapter);
                setIsNextListView(false);
                //listView.setVisibility(View.GONE);
                //mFrameLayout.setVisibility(View.VISIBLE);
                hideFragment();
            } else {
                finish();
            }
        } else if(keyCode == R.id.dlf_delete){
            Toast.makeText(this,"delete",Toast.LENGTH_SHORT).show();
        } else if (keyCode == R.id.dlf_update){
            Toast.makeText(this,"update",Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onBackPressed() {

        if (isNextListView) {
            //listView.setAdapter(mBasicInfoAdapter);
            setIsNextListView(false);
            //listView.setVisibility(View.GONE);
            //mFrameLayout.setVisibility(View.VISIBLE);
            hideFragment();
        } else {
            super.onBackPressed();
        }
    }

    private void getArgs(){
        Intent intent = getIntent();
        i = intent.getIntExtra(typeKey,-1);
        title = intent.getStringExtra(titleKey);
        Sno = intent.getStringExtra(mSnoKey);
    }

    private void LoadPane(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"isNextListView back:"+Boolean.toString(isNextListView));
                if(isNextListView){
                    setIsNextListView(false);
                    hideFragment();
                } else {
                    finish();
                }
            }
        });
        toolbar.setTitle(title);
        toolbar.inflateMenu(R.menu.blank);
        mProgressBar = (ProgressBar) findViewById(R.id.submainactivity_progressbar);
        listView = (ListView)findViewById(R.id.submainactivity_listview);
    }

    private void GetMessage(){
        switch (i){
            case 0:
                getRepResponse();
                break;
            case 1:
               getSLSResponse();
                break;
            case 2:
                getRLResponse();
        }
    }

    private void getRepResponse(){
        MyAsyncTask<getRepBasInfoResponse> myAsyncTask
                = new MyAsyncTask<getRepBasInfoResponse>(this, WebServiceConnector.METHOD_GETREPAIRBASICINFOBYSNO,mProgressBar);
        myAsyncTask.setListener(new getRepBasInfoResponse() {
            @Override
            public void getResult(ArrayList<String> result) {
                if(result == null){
                    DialogUtil.showNegativeTipsDialog(activity,"响应失败");
                    finish();
                    return;
                } else if(result.size() == 0){
                    listView.setVisibility(listView.GONE);
                    TextView textView = (TextView) findViewById(R.id.submainactivity_textview);
                    textView.setText("空");
                    return;
                }
                RefreshUI(result);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        myAsyncTask.execute(getParamType(),getInput());
    }

    private void getSLSResponse(){
        MyAsyncTask<getSLSInfoResponse> myAsyncTask
                = new MyAsyncTask<getSLSInfoResponse>(this, WebServiceConnector.METHOD_GETSLSBASICINFO,mProgressBar);
        myAsyncTask.setListener(new getSLSInfoResponse() {
            @Override
            public void getResult(ArrayList<String> result) {
                if(result == null){
                    DialogUtil.showNegativeTipsDialog(activity,"响应失败");
                    finish();
                    return;
                } else if(result.size() == 0){
                    listView.setVisibility(listView.GONE);
                    TextView textView = (TextView) findViewById(R.id.submainactivity_textview);
                    textView.setText("空");
                    return;
                }
                RefreshUI(result);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        myAsyncTask.execute(getParamType(),getInput());
    }

    private void getRLResponse(){
        MyAsyncTask<getRLInfoResponse> myAsyncTask
                = new MyAsyncTask<getRLInfoResponse>(this, WebServiceConnector.METHOD_GETRETURNLATELYBASICINFO,mProgressBar);
        myAsyncTask.setListener(new getRLInfoResponse() {
            @Override
            public void getResult(ArrayList<String> result) {
                if(result == null){
                    DialogUtil.showNegativeTipsDialog(activity,"响应失败");
                    finish();
                    return;
                } else if(result.size() == 0){
                    listView.setVisibility(listView.GONE);
                    TextView textView = (TextView) findViewById(R.id.submainactivity_textview);
                    textView.setText("空");
                    return;
                }
                RefreshUI(result);
            }
        });
        myAsyncTask.execute(getParamType(),getInput());
    }

    private ArrayList<String> getParamType(){
        ArrayList<String> paramType = new ArrayList<>();
        paramType.clear();
        paramType.add(WebServiceConnector.PARAMTYPE_SNO);
        return paramType;
    }

    private ArrayList<String> getInput(){
        ArrayList<String> input = new ArrayList<>();
        input.clear();
        input.add(Sno);
        return input;
    }

    private void RefreshUI(ArrayList<String> result){
        final int type = i;
        listView = (ListView) findViewById(R.id.submainactivity_listview);
        listView.setAdapter(getAdapter(result));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,Object> map=(HashMap<String,Object>)adapterView.getItemAtPosition(i);
                doTask(map,type);
            }
        });
    }

    private void showFragment(ArrayList<String> strings){
        TextView textView = (TextView) findViewById(R.id.submainactivity_textview);
        textView.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        toolbar.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(dif == null){
            dif = DetailsInfoFragment.GetThisFragment(strings);
            ft.add(R.id.sub_fragment_container,dif);
        }
        ft.show(dif);
        ft.commitAllowingStateLoss();
    }

    private void hideFragment(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.submainactivity_textview);
        textView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(dif);
        ft.remove(dif);
        dif = null;
        ft.commitAllowingStateLoss();
    }

    private void setIsNextListView(boolean isNextListView){
        this.isNextListView = isNextListView;
    }

    @Override
    public void onDestroy(){
        ActivityManager.removeActivity(this);
        super.onDestroy();
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG,"isNextListView back:"+Boolean.toString(isNextListView));
                if(isNextListView){
                    listView.setAdapter(mBasicInfoAdapter);
                    setIsNextListView(false);
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void doTask(final HashMap<String,Object> map,int type){
        final ListView listView = (ListView) findViewById(R.id.submainactivity_listview);
        ArrayList<String> paramType = new ArrayList<>();
        ArrayList<String> param = new ArrayList<>();
        paramType.add(WebServiceConnector.PARAMTYPE_RNO);
        param.add((String)map.get("title"));
        switch (type){
            case 0:
                MyAsyncTask<getRepDetailsInfoResponse> myAsyncTask
                        = new MyAsyncTask<getRepDetailsInfoResponse>(this,WebServiceConnector.METHOD_GETREPAIRDETAILSINFO,mProgressBar);
                myAsyncTask.setListener(new getRepDetailsInfoResponse() {
                    @Override
                    public void getResult(ArrayList<String> result) {
                        /*String formatResult = "";
                        for(String item:result){
                            formatResult = formatResult + item + "\n";
                        }
                        //Toast.makeText(activity,formatResult,Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                SubMainActivity.this,
                                android.R.layout.simple_list_item_1,
                                (String[])result.toArray(new String[result.size()])
                        );
                        listView.setAdapter(adapter);*/
                        String bmp = PhotoUtil.convertIconToString((Bitmap)map.get("bmp"));
                        ArrayList<String> formatResult = new ArrayList<String>();
                        formatResult.add(bmp);
                        for(int i = 0;i<result.size();i++)
                            formatResult.add(result.get(i));
                        showFragment(formatResult);
                        setIsNextListView(true);
                        Log.d(TAG,"isNextListView:"+Boolean.toString(isNextListView));
                    }
                });
                myAsyncTask.execute(paramType,param);
                break;
            case 1:
                MyAsyncTask<getSLSDetailsInfoResponse> myAsyncTask1
                        = new MyAsyncTask<getSLSDetailsInfoResponse>(this,WebServiceConnector.METHOD_GETSLSDETAILSINFO,mProgressBar);
                myAsyncTask1.setListener(new getSLSDetailsInfoResponse() {
                    @Override
                    public void getResult(ArrayList<String> result) {
                        /*String formatResult = "";
                        for(String item:result){
                            formatResult = formatResult + item + "\n";
                        }
                        //Toast.makeText(activity,formatResult,Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                SubMainActivity.this,
                                android.R.layout.simple_list_item_1,
                                (String[])result.toArray(new String[result.size()])
                        );
                        listView.setAdapter(adapter);*/
                        showFragment(result);
                        setIsNextListView(true);
                    }
                });
                myAsyncTask1.execute(paramType,param);
                break;
            case 2:
                MyAsyncTask<getRLDetailsInfoResponse> myAsyncTask2
                        = new MyAsyncTask<getRLDetailsInfoResponse>(this,WebServiceConnector.METHOD_GETRLDETAILSINFO,mProgressBar);
                myAsyncTask2.setListener(new getRLDetailsInfoResponse() {
                    @Override
                    public void getResult(ArrayList<String> result) {
                        /*String formatResult = "";
                        for(String item:result){
                            formatResult = formatResult + item + "\n";
                        }
                        //Toast.makeText(activity,formatResult,Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                SubMainActivity.this,
                                android.R.layout.simple_list_item_1,
                                (String[])result.toArray(new String[result.size()])
                        );
                        listView.setAdapter(adapter);*/
                        showFragment(result);
                        setIsNextListView(true);
                    }
                });
                myAsyncTask2.execute(paramType,param);
                break;
        }
    }

    private SimpleAdapter getAdapter(ArrayList<String> response){
        List<RSFListViewItem> list = getListViewItemList(response);
        List<Map<String,Object>> result = getListDate(list);
        SimpleAdapter adapter=new SimpleAdapter(
                this,
                result,
                R.layout.layout_repairschedulefragment_lisviewlayout,
                new String[]{"bmp","title","time"},
                new int[]{R.id.rsf_ls_imageview,R.id.rfs_ls_textviewtitle,R.id.rsf_ls_textviewtime}
        );
        adapter.setViewBinder(new SimpleAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if( (view instanceof ImageView) & (data instanceof Bitmap) ) {
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap)data);
                    return true;
                }
                return false;
            }
        });
        return adapter;
    }

    /**
     *
     * @return 加入了RSFListViewItem的List<RSFListViewItem> list
     */
    private List<RSFListViewItem> getListViewItemList(ArrayList<String> result){
        if(result.size()>0) {
            List<RSFListViewItem> list = new ArrayList<>();
            list.clear();
            int length = result.size();
            RSFListViewItem rsf;
            switch(i){
                case 0:
                    for(int i = 0;i<length;i+=3){
                        rsf = new RSFListViewItem();
                        if(!result.get(i).equals("null"))
                            rsf.setmBitmap(PhotoUtil.convertStringToIcon(result.get(i)));
                        else
                            rsf.setmBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_build_black_48dp));
                        rsf.setmTitle(result.get(i+1));
                        rsf.setmTime(DateUtil.FormatDate(result.get(i+2),"yyyy/MM/dd"));
                        list.add(rsf);
                    }
                    break;
                case 1:
                    for(int i = 0;i<length;i+=3){
                        rsf = new RSFListViewItem();
                        rsf.setmBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_directions_run_black_24dp));
                        rsf.setmTitle(result.get(i));
                        rsf.setmTime(DateUtil.FormatDate(result.get(i+1),"yyyy/MM/dd")+"\t"+DateUtil.FormatDate(result.get(i+2),"yyyy/MM/dd"));
                        list.add(rsf);
                    }
                    break;
                case 2:
                    for(int i = 0;i<length;i+=2){
                        rsf = new RSFListViewItem();
                        rsf.setmBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_av_timer_black_48dp));
                        rsf.setmTitle(result.get(i));
                        rsf.setmTime(result.get(i+1));
                        list.add(rsf);
                    }
                    break;
            }
            return list;
        }
        return null;
    }

    /**
     *
     * @param list    List<RSFListViewItem>
     * @return        List<Map<String, Object>> result
     */
    private List<Map<String,Object>> getListDate(List<RSFListViewItem> list){
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(RSFListViewItem rsf:list){
            Map<String, Object> map = new HashMap<String, Object>();
            Bitmap bmp=rsf.getmBitmap();
            map.put("bmp", bmp);
            String title=rsf.getmTitle();
            map.put("title", title);
            String time = rsf.getmTime();
            map.put("time", time);
            result.add(map);
        }
        return result;
    }

    public static void StartThisActivity(Context context,final String title,final int i,final String Sno) {
        Intent intent = new Intent(context, SubMainActivity.class);
        intent.putExtra(titleKey,title);
        intent.putExtra(typeKey,i);
        intent.putExtra(mSnoKey,Sno);
        context.startActivity(intent);
    }

}
