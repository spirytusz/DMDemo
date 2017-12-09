package com.zspirytus.dmdemo.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.preference.DialogPreference;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.JavaSource.Utils.DateUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.PhotoUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.util.ArrayList;

public class DetailsInfoActivity extends AppCompatActivity {

    private static final String TAG = "DetailsInfoActivity";
    private static final String infoKey = "info";
    private static final String typeKey = "type";

    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_info);
        Log.e(TAG,TAG+"\tStart!");
        LoadPane();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                break;
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
        LoadInfo();
    }

    private void LoadInfo(){
        ImageView imageView = (ImageView) findViewById(R.id.detailsinfoactivity_imageview);
        TextView textView = (TextView) findViewById(R.id.detailsinfoactivity_textview);
        ListView listView = (ListView) findViewById(R.id.detailsiinfoactivity_listview);
        ArrayList<String> info = getIntent().getStringArrayListExtra(infoKey);
        Log.d(TAG,TAG+"1234567\t"+info.size());
        imageView.setImageBitmap(PhotoUtil.convertStringToIcon(info.get(0)));
        textView.setText(info.get(1));
        String[] listViewItem = new String[info.size()-2];
        for(int i = 2;i<info.size();i++)
            listViewItem[i-2] = info.get(i);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listViewItem
        );
        listView.setAdapter(adapter);
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
                        doTask(paramType,requestParams);
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
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText reason = dialogView.findViewById(R.id.sub_reason);
                        TextView startTime = dialogView.findViewById(R.id.sub_start_time);
                        TextView endTime = dialogView.findViewById(R.id.sub_end_time);
                        TextView rno = (TextView) findViewById(R.id.detailsinfoactivity_textview);
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
                            Log.d(TAG,"requestParamss:\t"+requestParams.get(x));
                        doTask(paramType,requestParams);
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
                DialogUtil.DatePicker(activity,time);
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
                        else
                            requestParams.add(timeStr);
                        if (reasonStr.equals(""))
                            requestParams.add(WebServiceConnector.SQL_REASON);
                        else
                            requestParams.add(reasonStr);
                        for (int x = 0;x<requestParams.size();x++)
                            Log.d(TAG,"requestParamsss:\t"+requestParams.get(x));
                        doTask(paramType,requestParams);
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

    public void doTask(ArrayList<String> paramType,ArrayList<String> request){
        MyAsyncTask<getBooleanTypeResponse> myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(this, WebServiceConnector.METHOD_UPDATEREP);
        myAsyncTask.setListener(new getBooleanTypeResponse() {
            @Override
            public void showDialog(ArrayList<String> result) {
                if(result == null||result.size() == 0){
                    DialogUtil.showNegativeTipsDialog(activity,"响应失败");
                    return;
                }
                boolean isSuccess = result.get(0).replaceAll("\r|\n|\t","").equals("true");
                DialogUtil.showResultDialog(activity,isSuccess);
            }
        });
        myAsyncTask.execute(paramType,request);
    }

    public static void StartThisActivity(Context context, ArrayList<String> info, int type){
        Intent intent = new Intent(context,DetailsInfoActivity.class);
        intent.putExtra(infoKey,info);
        intent.putExtra(typeKey,type);
        context.startActivity(intent);
    }
}
