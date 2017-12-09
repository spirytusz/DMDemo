package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zspirytus.dmdemo.Activity.BaseActivity;
import com.zspirytus.dmdemo.Interface.SetMyInfoAvatar;
import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.JavaSource.Utils.DateUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.Manager.FragmentCollector;
import com.zspirytus.dmdemo.JavaSource.Utils.PhotoUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 97890 on 2017/9/17.
 */

public class RepairFragment extends Fragment {

    private Activity mParentActivity;

    private static final String TAG = "RepairFragment";
    private static final String mSnoKey = "Sno";

    private View view;
    private TextView mRepairArea;
    private EditText mRepairPlace;
    private TextView mRepairType;
    private EditText mRepairDetail;
    private EditText mRepairContact;
    private TextView mRepairPhoto;
    private Button mRepairSend;

    private SetMyInfoAvatar listener;

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
        listener = (SetMyInfoAvatar)((Activity)context);
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
                DialogUtil.showListDialog(mParentActivity,mArea,mRepairArea);
            }
        });
        mRepairPlace = v.findViewById(R.id.repair_edittext_place);
        mRepairType = v.findViewById(R.id.repairType);
        mRepairType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showListDialog(mParentActivity,mCategory,mRepairType);
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
                    BaseActivity.CloseKeyBoard(mParentActivity);
                }
            }
        });
        myAsyncTask.execute(getParamType(),getInput());
    }

    private ArrayList<String> getInput(){
        String Sno = getArguments().getString(mSnoKey);
        String repairNo = DateUtil.getNowDate("ddHHmmssSSS");
        String area = mRepairArea.getText().toString();
        String place = mRepairPlace.getText().toString();
        String type = mRepairType.getText().toString();
        String detail = mRepairDetail.getText().toString();
        String contact = mRepairContact.getText().toString();
        String dir = mRepairPhoto.getText().toString();
        File photoFile;
        if(!dir.equals(getString(R.string.Add_Your_Photo))){
            photoFile = new File(dir);
            if(photoFile.length() > PhotoUtil.UPLOAD_MAXSIZE){
                photoFile = PhotoUtil.saveCompressFile(photoFile,70);
            }
        }
        else
             photoFile = null;
        ArrayList<String> input = new ArrayList<String>();
        input.clear();
        input.add(Sno);
        input.add(repairNo);
        input.add(area);
        input.add(place);
        input.add(type);
        input.add(detail);
        input.add(contact);
        if(photoFile!=null){
            String photo = PhotoUtil.convertFileToString(photoFile);
            input.add(photo);
        }
        else
            input.add("");
        input.add(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
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

