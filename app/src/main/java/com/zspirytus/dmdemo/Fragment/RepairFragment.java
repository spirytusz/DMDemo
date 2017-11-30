package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zspirytus.dmdemo.Interface.SetUploadPicPath;
import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.FragmentCollector;
import com.zspirytus.dmdemo.JavaSource.Utils.PhotoUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 97890 on 2017/9/17.
 */

public class RepairFragment extends Fragment {

    private Activity mParentActivity;

    private static final String TAG = "RepairFragment";
    private static final String FRAGMENT_HIDDEN_STATUS = "FRAGMENT_HIDDEN_STATUS";
    private static final String mSnoKey = "Sno";

    private View view;
    private TextView mRepairArea;
    private EditText mRepairPlace;
    private TextView mRepairType;
    private EditText mRepairDetail;
    private EditText mRepairContact;
    private TextView mRepairPhoto;
    private Button mRepairSend;

    private SetUploadPicPath listener;


    private String outputDirectory;

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(FRAGMENT_HIDDEN_STATUS);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putBoolean(FRAGMENT_HIDDEN_STATUS,isHidden());
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.layout_repairfragment,container,false);
        LoadPane(view);
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParentActivity = (Activity) context;
        listener = (SetUploadPicPath)((Activity)context);
    }

    @Override
    public void onDestroyView(){
        FragmentCollector.removeFragment(this);
        super.onDestroyView();
    }

    public void LoadPane(View v){
        final String[] mArea = {
                getString(R.string.Public_Area).toString(),
                getString(R.string.Living_Area).toString()
        };
        final String[] mCategory = {
                getString(R.string.Electrician).toString(),
                getString(R.string.Hydraulic).toString(),
                getString(R.string.Woodworking).toString(),
                getString(R.string.Construction).toString(),
                getString(R.string.Equipment).toString(),
                getString(R.string.Other).toString()
        };
        mRepairArea = v.findViewById(R.id.repair_select_area);
        mRepairArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);// 自定义对话框
                builder.setSingleChoiceItems(mArea, 0, new DialogInterface.OnClickListener() {// 2默认的选中

                    @Override
                    public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                        // showToast(which+"");
                        //setmAreaSelected(mArea[which]);
                        mRepairArea.setText(mArea[which]);
                        dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
                    }
                });
                builder.show();// 让弹出框显示
            }
        });
        mRepairPlace = v.findViewById(R.id.repair_edittext_place);
        mRepairType = v.findViewById(R.id.repairType);
        mRepairType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);// 自定义对话框
                builder.setSingleChoiceItems(mCategory, 0, new DialogInterface.OnClickListener() {// 2默认的选中

                    @Override
                    public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                        // showToast(which+"");
                        //setmCategorySelected(mCategory[which]);
                        mRepairType.setText(mCategory[which]);
                        dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
                    }
                });
                builder.show();// 让弹出框显示
            }
        });
        mRepairDetail = v.findViewById(R.id.repair_edittext_detail);
        mRepairContact = v.findViewById(R.id.repair_edittext_contact);
        mRepairPhoto = v.findViewById(R.id.repair_select_photo);
        mRepairPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);
                builder.setTitle(mParentActivity.getString(R.string.By));
                //    指定下拉列表的显示数据
                final String[] mSelectItem = {
                        getString(R.string.By_Camera),
                        getString(R.string.By_Album),
                        getString(R.string.Cancel)
                };
                //    设置一个下拉的列表选择项
                builder.setItems(mSelectItem, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which){
                            case 0:
                                listener.setUploadPicPath(mRepairPhoto,1);
                                break;

                            case 1:
                                listener.setUploadPicPath(mRepairPhoto,2);
                                break;

                            case 2:
                                break;

                            default:
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        mRepairSend = v.findViewById(R.id.button_repair);
        mRepairSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isInputLegal()){
                    DialogUtil.showNegativeTipsDialog(mParentActivity);
                    return;
                }
                SendMessage();
            }
        });
    }

    private void SendMessage(){
        MyAsyncTask<getBooleanTypeResponse> myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(mParentActivity,WebServiceConnector.METHOD_NEWREPAIRREPORT);
        myAsyncTask.setListener(new getBooleanTypeResponse() {
            @Override
            public void showDialog(ArrayList<String> result) {
                if(result == null||result.size() == 0){
                    DialogUtil.showNegativeTipsDialog(mParentActivity,"响应失败");
                    return;
                }
                boolean isSuccess = result.get(0).replaceAll("\r|\n|\t","").equals("true");
                DialogUtil.showResultDialog(mParentActivity,isSuccess);
                if(isSuccess){
                    mRepairArea.setText("");
                    mRepairPlace.setText("");
                    mRepairType.setText("");
                    mRepairDetail.setText("");
                    mRepairContact.setText("");
                    mRepairPhoto.setText("");
                }
            }
        });
        myAsyncTask.execute(getParamType(),getInput());
    }

    private ArrayList<String> getInput(){
        SimpleDateFormat formator = new SimpleDateFormat("ddHHmmssSSS");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String Sno = getArguments().getString(mSnoKey);
        String repairNo = formator.format(curDate);
        String area = mRepairArea.getText().toString();
        String place = mRepairPlace.getText().toString();
        String type = mRepairType.getText().toString();
        String detail = mRepairDetail.getText().toString();
        String contact = mRepairContact.getText().toString();
        String dir = mRepairPhoto.getText().toString();
        Log.d(TAG,"InputTest:\t Sno    "+Sno);
        Log.d(TAG,"InputTest:\t repairNo    "+repairNo);
        File photoFile;
        if(!dir.equals(getString(R.string.Add_Your_Photo))){
            photoFile = new File(dir);
        }
        else
             photoFile = null;
        ArrayList<String> input = new ArrayList<String>();
        input.clear();
        input.add(Sno);
        input.add(repairNo);
        if(area.equals(getString(R.string.Public_Area))){
            input.add("0");
            Log.d(TAG,"InputTest:\t area    "+"0");
        }
        else{
            input.add("1");
            Log.d(TAG,"InputTest:\t area    "+"1");
        }
        input.add(place);
        Log.d(TAG,"InputTest:\t place    "+place);
        switch (type){
            case "电工类":
                input.add("0x000");
                Log.d(TAG,"InputTest:\t type    "+"0x000");
                break;
            case "水工类":
                input.add("0x001");
                Log.d(TAG,"InputTest:\t type    "+"0x001");
                break;
            case "木工类":
                input.add("0x010");
                Log.d(TAG,"InputTest:\t type    "+"0x010");
                break;
            case "土建类":
                input.add("0x011");
                Log.d(TAG,"InputTest:\t type    "+"0x011");
                break;
            case "设备类":
                input.add("0x100");
                Log.d(TAG,"InputTest:\t type    "+"0x100");
                break;
            case "杂项":
                input.add("0x101");
                Log.d(TAG,"InputTest:\t type    "+"0x101");
                break;
        }
        input.add(detail);
        input.add(contact);
        Log.d(TAG,"InputTest:\t detail    "+detail);
        Log.d(TAG,"InputTest:\t contact    "+contact);
        Log.d(TAG,"InputTest:\t dir    "+dir);
        if(photoFile!=null){
            String photo = PhotoUtil.convertFileToString(photoFile);
            input.add(photo);
            Log.d("","String length:\t"+photo.length()/1024+"KB");
        }
        else
            input.add("000");
        input.add("2017-12-01 13:50:04");
        Log.d(TAG,"InputTest:\t reportDate" + "2017-12-01 13:50:04");
        return input;
    }

    private ArrayList<String> getParamType(){
        ArrayList<String> paraType = new ArrayList<String>();
        paraType.clear();
        paraType.add(WebServiceConnector.PARAMTYPE_SNO);
        paraType.add(WebServiceConnector.PARAMTYPE_REPAIRNO);
        paraType.add(WebServiceConnector.PARAMTYPE_REPAIRAREA);
        paraType.add(WebServiceConnector.PARAMTYPE_REPAIRPLACE);
        paraType.add(WebServiceConnector.PARAMTYPE_REPAIRTYPE);
        paraType.add(WebServiceConnector.PARAMTYPE_DETAIL);
        paraType.add(WebServiceConnector.PARAMTYPE_CONTACT);
        paraType.add(WebServiceConnector.PARAMTYPE_PHOTO);
        paraType.add(WebServiceConnector.PARAMTYPE_REPORTDATE);
        return paraType;
    }

    private boolean isInputLegal(){
        String area = mRepairArea.getText().toString();
        String place = mRepairPlace.getText().toString();
        String type = mRepairType.getText().toString();
        String detail = mRepairDetail.getText().toString();
        String contact = mRepairContact.getText().toString();
        if(area.equals("") || place.equals("") || type.equals("") || detail.equals("") || contact.equals("")){
            return false;
        }
        return true;
    }
    public static RepairFragment GetThisFragment(String Sno){
        RepairFragment rf = new RepairFragment();
        Bundle bundle = new Bundle();
        bundle.putString(mSnoKey,Sno);
        rf.setArguments(bundle);
        return  rf;
    }
}

