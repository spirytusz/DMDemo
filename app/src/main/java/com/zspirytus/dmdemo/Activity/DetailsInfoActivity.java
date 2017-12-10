package com.zspirytus.dmdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.Interface.getRLDetailsInfoResponse;
import com.zspirytus.dmdemo.Interface.getRepDetailsInfoResponse;
import com.zspirytus.dmdemo.Interface.getSLSDetailsInfoResponse;
import com.zspirytus.dmdemo.JavaSource.Utils.DateUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.PhotoUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.util.ArrayList;

public class DetailsInfoActivity extends AppCompatActivity {

    private static final String TAG = "DetailsInfoActivity";
    private static final String PrimaryKey = "primaryKey";
    private static final String typeKey = "type";
    private static final String doNothing = "null";
    private ArrayList<String> resultMemory;


    private final Activity activity = this;

    private boolean flagOfLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_info);
        Log.e(TAG,TAG+"\tStart!");
        LoadPane();
        GetMessage();
    }

    @Override
    public void onDestroy(){
        Intent intent = getIntent();
        intent.putExtra(doNothing,doNothing);
        setResult(0,intent);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(flagOfLoaded){
            switch (item.getItemId()){
                case R.id.dlf_update:
                    switch(getIntent().getIntExtra(typeKey,-1)){
                        case 0:
                            UpdateForRep();
                            break;
                        case 1:
                            UpdateForELS();
                            break;
                        case 2:
                            UpdateForRL();
                            break;
                        default:
                            break;
                    }
                    break;
                case R.id.dlf_delete:
                    Delete();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadPane(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.dia_toolbar);
        toolbar.setTitle("详细信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void LoadInfo(ArrayList<String> result){
        resultMemory = result;
        ImageView imageView = (ImageView) findViewById(R.id.detailsinfoactivity_imageview);
        TextView textView = (TextView) findViewById(R.id.detailsinfoactivity_textview);
        ListView listView = (ListView) findViewById(R.id.detailsiinfoactivity_listview);
        imageView.setImageBitmap(PhotoUtil.convertStringToIcon(result.get(0)));
        textView.setText(result.get(1));
        String[] listViewItem = new String[result.size()-2];
        for(int i = 2;i<result.size();i++)
            listViewItem[i-2] = result.get(i);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listViewItem
        );
        listView.setAdapter(adapter);
        flagOfLoaded = true;
    }

    private void GetMessage(){
        Intent intent = getIntent();
        String primaryKey = intent.getStringExtra(PrimaryKey);
        int type = intent.getIntExtra(typeKey,-1);
        doTask(primaryKey,type);
    }

    private void doTask(final String primaryKey,int type){
        ArrayList<String> paramType = new ArrayList<>();
        ArrayList<String> param = new ArrayList<>();
        paramType.add(WebServiceConnector.PARAMTYPE_RNO);
        param.add(primaryKey);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.detailinfo_progressbar);
        switch (type){
            case 0:
                MyAsyncTask<getRepDetailsInfoResponse> myAsyncTask
                        = new MyAsyncTask<getRepDetailsInfoResponse>(this,WebServiceConnector.METHOD_GETREPAIRDETAILSINFO,progressBar);
                myAsyncTask.setListener(new getRepDetailsInfoResponse() {
                    @Override
                    public void getResult(ArrayList<String> result) {
                        ArrayList<String> formatResult = new ArrayList<String>();
                        for(int i = 0;i<result.size();i++)
                            formatResult.add(result.get(i));
                        //Refresh UI
                        LoadInfo(formatResult);
                    }
                });
                myAsyncTask.execute(paramType,param);
                break;
            case 1:
                MyAsyncTask<getSLSDetailsInfoResponse> myAsyncTask1
                        = new MyAsyncTask<getSLSDetailsInfoResponse>(this,WebServiceConnector.METHOD_GETSLSDETAILSINFO,progressBar);
                myAsyncTask1.setListener(new getSLSDetailsInfoResponse() {
                    @Override
                    public void getResult(ArrayList<String> result) {
                        if (result == null){
                            DialogUtil.showNegativeTipsDialog(activity);
                            return;
                        }
                        ArrayList<String> formatResult = new ArrayList<String>();
                        formatResult.add(PhotoUtil.convertIconToString(BitmapFactory.decodeResource(getResources(),R.drawable.ic_directions_run_black_48dp)));
                        formatResult.add(result.get(0));
                        formatResult.add(DateUtil.FormatDate(result.get(1),"yyyy/MM/dd")+" - "+DateUtil.FormatDate(result.get(2),"yyyy/MM/dd"));
                        formatResult.add(result.get(3));
                        formatResult.add(result.get(4));
                        //Refresh UI
                        LoadInfo(formatResult);
                    }
                });
                myAsyncTask1.execute(paramType,param);
                break;
            case 2:
                MyAsyncTask<getRLDetailsInfoResponse> myAsyncTask2
                        = new MyAsyncTask<getRLDetailsInfoResponse>(this,WebServiceConnector.METHOD_GETRLDETAILSINFO,progressBar);
                myAsyncTask2.setListener(new getRLDetailsInfoResponse() {
                    @Override
                    public void getResult(ArrayList<String> result) {
                        if (result == null || result.size() == 0){
                            DialogUtil.showNegativeTipsDialog(activity);
                            return;
                        }
                        ArrayList<String> formatResult = new ArrayList<String>();
                        formatResult.add(PhotoUtil.convertIconToString(BitmapFactory.decodeResource(getResources(),R.drawable.ic_av_timer_black_custom_48dp)));
                        for(int i = 0;i<result.size();i++)
                            formatResult.add(result.get(i));
                        //Refresh UI
                        LoadInfo(formatResult);
                    }
                });
                myAsyncTask2.execute(paramType,param);
                break;
        }
    }

    public void UpdateForRep(){
        final String[] mCategory = {
                getString(R.string.Electrician).toString(),
                getString(R.string.Hydraulic).toString(),
                getString(R.string.Woodworking).toString(),
                getString(R.string.Construction).toString(),
                getString(R.string.Equipment).toString(),
                getString(R.string.Other).toString()
        };
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_updatedialogforrep,null);
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        customizeDialog.setView(dialogView);
        final TextView dialogTextView = (TextView) dialogView.findViewById(R.id.sub_repairType);
        final EditText repairPlace = (EditText) dialogView.findViewById(R.id.sub_edittext_place);
        final EditText detail = (EditText)dialogView.findViewById(R.id.sub_edittext_detail);
        final EditText contact = (EditText)dialogView.findViewById(R.id.sub_edittext_contact);
        dialogTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showListDialog(activity,mCategory,dialogTextView);
            }
        });
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView rno = (TextView) findViewById(R.id.detailsinfoactivity_textview);
                        String placeText = repairPlace.getText().toString();
                        String detailText = detail.getText().toString();
                        String contactText = contact.getText().toString();
                        String typeText = dialogTextView.getText().toString();
                        ArrayList<String> paramType = new ArrayList<String>();
                        ArrayList<String> requestParams = new ArrayList<String>();
                        paramType.add(WebServiceConnector.PARAMTYPE_REPAIRNO);
                        paramType.add(WebServiceConnector.PARAMTYPE_REPAIRPLACE);
                        paramType.add(WebServiceConnector.PARAMTYPE_REPAIRTYPE);
                        paramType.add(WebServiceConnector.PARAMTYPE_DETAIL);
                        paramType.add(WebServiceConnector.PARAMTYPE_CONTACT);
                        requestParams.add(rno.getText().toString());
                        if(placeText.equals(""))
                            requestParams.add(WebServiceConnector.SQL_REPAIRPLACE);
                        else
                            requestParams.add(placeText);
                        if(typeText.equals(""))
                            requestParams.add(WebServiceConnector.SQL_REPAIRTYPE);
                        else
                            requestParams.add(typeText);
                        if (detailText.equals(""))
                            requestParams.add(WebServiceConnector.SQL_DETAIL);
                        else
                            requestParams.add(detailText);
                        if (contactText.equals(""))
                            requestParams.add(WebServiceConnector.SQL_CONTACT);
                        else
                            requestParams.add(contactText);
                        Update(paramType,requestParams,0);
                    }
                });
        customizeDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        customizeDialog.setCancelable(false);
        customizeDialog.show();
    }

    public void UpdateForELS(){
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_updatedialogforels,null);
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        customizeDialog.setView(dialogView);
        final EditText reason = dialogView.findViewById(R.id.sub_reason);
        final TextView startTime = dialogView.findViewById(R.id.sub_start_time);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.DatePicker(activity,startTime);
            }
        });
        final TextView endTime = dialogView.findViewById(R.id.sub_end_time);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.DatePicker(activity,endTime);
            }
        });
        final TextView rno = (TextView) findViewById(R.id.detailsinfoactivity_textview);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String startTimeStr = startTime.getText().toString();
                        String endTimeStr = endTime.getText().toString();
                        String reasonStr = reason.getText().toString();
                        ArrayList<String> paramType = new ArrayList<String>();
                        paramType.add(WebServiceConnector.PARAMTYPE_SLSNO);
                        paramType.add(WebServiceConnector.PARAMTYPE_LEAVEDATE);
                        paramType.add(WebServiceConnector.PARAMTYPE_BACKDATE);
                        paramType.add(WebServiceConnector.PARAMTYPE_REASON);
                        ArrayList<String> requestParams = new ArrayList<String>();
                        requestParams.add(rno.getText().toString());
                        if (startTimeStr.equals(""))
                            requestParams.add(WebServiceConnector.SQL_LEAVEDATE);
                        else
                            requestParams.add(startTimeStr);
                        if (endTimeStr.equals(""))
                            requestParams.add(WebServiceConnector.SQL_BACKDATE);
                        else
                            requestParams.add(endTimeStr);
                        if (reasonStr.equals(""))
                            requestParams.add(WebServiceConnector.SQL_REASON);
                        else
                            requestParams.add(reasonStr);
                        for(int x = 0;x<requestParams.size();x++)
                            Log.d(TAG,"requestParam1:\t"+requestParams.get(x));
                        Update(paramType,requestParams,1);
                    }
                });
        customizeDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        customizeDialog.setCancelable(false);
        customizeDialog.show();
    }

    public void UpdateForRL(){
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_updatedialogforrl,null);
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        customizeDialog.setView(dialogView);
        final TextView time = (TextView)dialogView.findViewById(R.id.sub_returnTime);
        final EditText reason = (EditText) dialogView.findViewById(R.id.sub_reason1);
        final TextView rno = (TextView) findViewById(R.id.detailsinfoactivity_textview);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.TimePicker(activity,time);
            }
        });
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String timeStr = time.getText().toString();
                        String reasonStr = reason.getText().toString();
                        ArrayList<String> paramType = new ArrayList<String>();
                        paramType.add(WebServiceConnector.PARAMTYPE_RNO);
                        paramType.add(WebServiceConnector.PARAMTYPE_RETURNTIME);
                        paramType.add(WebServiceConnector.PARAMTYPE_REASON);
                        ArrayList<String> requestParams = new ArrayList<String>();
                        requestParams.add(rno.getText().toString());
                        if (timeStr.equals(""))
                            requestParams.add(WebServiceConnector.SQL_RETURNTIME);
                        else {
                            timeStr = DateUtil.getNowYear_int()
                                    + "-" + DateUtil.getNowMonth_int()
                                    + "-" + DateUtil.getNowDay_int()
                                    + " " + timeStr
                                    + ":00";
                            if (DateUtil.isNextDay(timeStr,"yyyy-MM-dd HH:mm:ss"))
                                timeStr = DateUtil.AddOneDay(timeStr, 1, "yyyy-MM-dd HH:mm:ss");
                            requestParams.add(timeStr);
                        }
                        if (reasonStr.equals(""))
                            requestParams.add(WebServiceConnector.SQL_REASON);
                        else
                            requestParams.add(reasonStr);
                        for (int x = 0;x<requestParams.size();x++)
                            Log.d(TAG,"requestParam2:\t"+requestParams.get(x));
                        Update(paramType,requestParams,2);
                    }
                });
        customizeDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        customizeDialog.setCancelable(false);
        customizeDialog.show();
    }

    public void Update(ArrayList<String> paramType,ArrayList<String> request,int type){
        MyAsyncTask<getBooleanTypeResponse> myAsyncTask = null;
        switch(type){
            case 0:
                myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(this, WebServiceConnector.METHOD_UPDATEREP);
                break;
            case 1:
                myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(this, WebServiceConnector.METHOD_UPDATEELS);
                break;
            case 2:
                myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(this, WebServiceConnector.METHOD_UPDATERL);
                break;
        }
        myAsyncTask.setListener(new getBooleanTypeResponse() {
            @Override
            public void showDialog(ArrayList<String> result) {
                if(result == null||result.size() == 0){
                    DialogUtil.showNegativeTipsDialog(activity,"响应失败");
                    return;
                }
                boolean isSuccess = result.get(0).replaceAll("\r|\n|\t","").equals("true");
                AlertDialog.Builder dialog = DialogUtil.getResultDialog(activity,isSuccess);
                dialog.setPositiveButton("好",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GetMessage();
                            }
                        });
                dialog.show();
            }
        });
        myAsyncTask.execute(paramType,request);
    }

    /*private void showDeleteTipsDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("提示");
        dialog.setMessage("删除");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog.show();
    }*/

    private void Delete(){
        Intent intent = getIntent();
        String primaryKey = intent.getStringExtra(PrimaryKey);
        int type = intent.getIntExtra(typeKey,-1);
        Delete(primaryKey,type);
    }

    private void Delete(final String primaryKey,final int type){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("提示");
        dialog.setMessage("删除记录: "+primaryKey+" 吗?");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doDelete(primaryKey,type);
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog.show();
    }

    private void doDelete(final String primaryKey,final int type){
        MyAsyncTask<getBooleanTypeResponse> myAsyncTask = null;
        ArrayList<String> paramType = new ArrayList<>();
        ArrayList<String> requestParams = new ArrayList<>();
        paramType.add(WebServiceConnector.PARAMTYPE_DELETENO);
        requestParams.add(primaryKey);
        switch (type){
            case 0:
                myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(activity,WebServiceConnector.METHOD_DELETEREP);
                break;
            case 1:
                myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(activity,WebServiceConnector.METHOD_DELETEELS);
                break;
            case 2:
                myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(activity,WebServiceConnector.METHOD_DELETERL);
                break;
            default:
                break;
        }
        myAsyncTask.setListener(new getBooleanTypeResponse() {
            @Override
            public void showDialog(ArrayList<String> result) {
                Log.d(TAG,"isSuccess?\t"+result.get(0));
                if(result == null||result.size() == 0){
                    DialogUtil.showNegativeTipsDialog(activity,"响应失败");
                    return;
                }
                boolean isSuccess = result.get(0).replaceAll("\r|\n|\t","").equals("true");
                Log.d(TAG,"isSuccess?\t"+Boolean.toString(isSuccess));
                AlertDialog.Builder dialog = DialogUtil.getDialog(activity,"成功");
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activity.finish();
                            }
                        });
                dialog.show();
            }
        });
        myAsyncTask.execute(paramType,requestParams);
    }

    public static void StartThisActivity(Context context, String primaryKey, int type){
        Intent intent = new Intent(context,DetailsInfoActivity.class);
        intent.putExtra(PrimaryKey,primaryKey);
        intent.putExtra(typeKey,type);
        context.startActivity(intent);
    }
}
