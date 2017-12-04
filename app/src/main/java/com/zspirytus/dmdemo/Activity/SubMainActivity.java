package com.zspirytus.dmdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.zspirytus.dmdemo.Interface.getRLInfoResponse;
import com.zspirytus.dmdemo.Interface.getRepBasInfoResponse;
import com.zspirytus.dmdemo.Interface.getSLSInfoResponse;
import com.zspirytus.dmdemo.JavaSource.ActivityManager;
import com.zspirytus.dmdemo.JavaSource.RSFListViewItem;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.PhotoUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.io.File;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_schedule);
        ActivityManager.addActivity(this);
        getArgs();
        LoadPane();
        GetMessage();
    }

    private void getArgs(){
        Intent intent = getIntent();
        i = intent.getIntExtra(typeKey,-1);
        Log.d(TAG,"Adapters Length:\t"+i);
        title = intent.getStringExtra(titleKey);
        Sno = intent.getStringExtra(mSnoKey);
    }

    private void LoadPane(){
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(title);
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
        MyAsyncTask<getRepBasInfoResponse> myAsyncTask = new MyAsyncTask<getRepBasInfoResponse>(this, WebServiceConnector.METHOD_GETREPAIRBASICINFOBYSNO,mProgressBar);
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
        MyAsyncTask<getSLSInfoResponse> myAsyncTask = new MyAsyncTask<getSLSInfoResponse>(this, WebServiceConnector.METHOD_GETSLSBASICINFO,mProgressBar);
        myAsyncTask.setListener(new getSLSInfoResponse() {
            @Override
            public void getResult(ArrayList<String> result) {
                if(result == null||result.size() == 0){
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
        MyAsyncTask<getRLInfoResponse> myAsyncTask = new MyAsyncTask<getRLInfoResponse>(this, WebServiceConnector.METHOD_GETRETURNLATELYBASICINFO,mProgressBar);
        myAsyncTask.setListener(new getRLInfoResponse() {
            @Override
            public void getResult(ArrayList<String> result) {
                if(result == null||result.size() == 0){
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
        listView.setAdapter(getAdapter(result));
    }

    @Override
    public void onDestroy(){
        ActivityManager.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        Log.d("","result size:\t"+result.size());
        if(result.size()>0) {
            List<RSFListViewItem> list = new ArrayList<>();
            list.clear();
            int length = result.size();
            RSFListViewItem rsf;
            switch(i){
                case 0:
                    for(int i = 0;i<length;i+=3){
                        rsf = new RSFListViewItem();
                        rsf.setmBitmap(PhotoUtil.convertStringToIcon(result.get(i)));
                        rsf.setmTitle(result.get(i+1));
                        rsf.setmTime(result.get(i+2));
                        list.add(rsf);
                    }
                    break;
                case 1:
                    Log.d(TAG,"Adapterss Length:\t"+length);
                    for(int i = 0;i<length;i+=3){
                        rsf = new RSFListViewItem();
                        rsf.setmBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_directions_run_black_24dp));
                        rsf.setmTitle(result.get(i+1));
                        rsf.setmTime(result.get(i+2));
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
