package com.zspirytus.dmdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.zspirytus.dmdemo.Interface.getRLDetailsInfoResponse;
import com.zspirytus.dmdemo.Interface.getRLInfoResponse;
import com.zspirytus.dmdemo.Interface.getRepBasInfoResponse;
import com.zspirytus.dmdemo.Interface.getRepDetailsInfoResponse;
import com.zspirytus.dmdemo.Interface.getRepPicResponse;
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
    private ListView listView;
    private ArrayList<String> pictures = null;
    private ArrayList<String> texts = null;

    private MyAsyncTask<getRepBasInfoResponse> myAsyncTaskForRep;
    private MyAsyncTask<getRepPicResponse> myAsyncTaskForRepPic;
    private MyAsyncTask<getSLSInfoResponse> myAsyncTaskForSLS;
    private MyAsyncTask<getRLInfoResponse> myAsyncTaskForRL;

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

    @Override
    public void onDestroy(){
        ActivityManager.removeActivity(this);
        cancelTask();
        super.onDestroy();
    }

    private void cancelTask(){
        if(myAsyncTaskForRep != null && myAsyncTaskForRep.getStatus() == AsyncTask.Status.RUNNING){
            myAsyncTaskForRep.cancel(true);
        }
        if(myAsyncTaskForRepPic != null && myAsyncTaskForRepPic.getStatus() == AsyncTask.Status.RUNNING){
            myAsyncTaskForRepPic.cancel(true);
        }
        if(myAsyncTaskForSLS != null && myAsyncTaskForSLS.getStatus() == AsyncTask.Status.RUNNING){
            myAsyncTaskForSLS.cancel(true);
        }
        if(myAsyncTaskForRL != null && myAsyncTaskForRL.getStatus() == AsyncTask.Status.RUNNING){
            myAsyncTaskForRL.cancel(true);
        }
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
                finish();
            }
        });
        toolbar.setTitle(title);
        toolbar.inflateMenu(R.menu.blank);
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
        myAsyncTaskForRep
                = new MyAsyncTask<getRepBasInfoResponse>(this, WebServiceConnector.METHOD_GETREPAIRBASICINFOBYSNO);
        myAsyncTaskForRep.setListener(new getRepBasInfoResponse() {
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
                getRepPicResponse();
                setRepInfoText(result);
            }
        });
        myAsyncTaskForRep.execute(getParamType(),getInput());
    }

    private void getRepPicResponse(){
        myAsyncTaskForRepPic = new MyAsyncTask<getRepPicResponse>(this,WebServiceConnector.METHOD_GETREPBASICINFOBMP);
        myAsyncTaskForRepPic.setListener(new getRepPicResponse() {
            @Override
            public void getResult(ArrayList<String> result) {
                setRepInfoPic(result);
                RefreshUI(getRepInfoText());
            }
        });
        myAsyncTaskForRepPic.execute(getParamType(),getInput());
    }

    private void getSLSResponse(){
        myAsyncTaskForSLS
                = new MyAsyncTask<getSLSInfoResponse>(this, WebServiceConnector.METHOD_GETSLSBASICINFO);
        myAsyncTaskForSLS.setListener(new getSLSInfoResponse() {
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
        myAsyncTaskForSLS.execute(getParamType(),getInput());
    }

    private void getRLResponse(){
        myAsyncTaskForRL
                = new MyAsyncTask<getRLInfoResponse>(this, WebServiceConnector.METHOD_GETRETURNLATELYBASICINFO);
        myAsyncTaskForRL.setListener(new getRLInfoResponse() {
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
        myAsyncTaskForRL.execute(getParamType(),getInput());
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
                String primaryKey = (String)map.get("title");
                DetailsInfoActivity.StartThisActivity(activity,primaryKey,type);
            }
        });
    }

    private void setRepInfoText(ArrayList<String> texts){
        this.texts = texts;
    }

    private ArrayList<String> getRepInfoText(){
        return texts;
    }

    private void setRepInfoPic(ArrayList<String> pictures){
        this.pictures = pictures;
    }

    private ArrayList<String> getRepInfoPic(){
        return pictures;
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
                    for(int i = 0;i<length;i+=2){
                        rsf = new RSFListViewItem();
                        if(pictures != null){
                            if(pictures.get(i/2).equals("123"))
                                rsf.setmBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_background_blank));
                            else
                                rsf.setmBitmap(PhotoUtil.convertStringToIcon(pictures.get(i/2)));
                        } else {
                            rsf.setmBitmap(PhotoUtil.convertStringToIcon(""));
                        }
                        rsf.setmTitle(result.get(i));
                        rsf.setmTime(DateUtil.FormatDate(result.get(i+1),"yyyy/MM/dd"));
                        list.add(rsf);
                    }
                    break;
                case 1:
                    for(int i = 0;i<length;i+=3){
                        rsf = new RSFListViewItem();
                        rsf.setmBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_background_blank));
                        rsf.setmTitle(result.get(i));
                        rsf.setmTime(DateUtil.FormatDate(result.get(i+1),"yyyy/MM/dd")+"\t"+DateUtil.FormatDate(result.get(i+2),"yyyy/MM/dd"));
                        list.add(rsf);
                    }
                    break;
                case 2:
                    for(int i = 0;i<length;i+=2){
                        rsf = new RSFListViewItem();
                        rsf.setmBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_background_blank));
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
