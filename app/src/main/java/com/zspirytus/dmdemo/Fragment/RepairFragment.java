package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.zspirytus.dmdemo.Interface.SetMyInfoAvatar;
import com.zspirytus.dmdemo.Interface.SetUploadPicPath;
import com.zspirytus.dmdemo.JavaSource.FragmentCollector;
import com.zspirytus.dmdemo.R;

/**
 * Created by 97890 on 2017/9/17.
 */

public class RepairFragment extends Fragment {

    private Activity mParentActivity;

    private static final String TAG = "RepairFragment";
    private static final String FRAGMENT_HIDDEN_STATUS = "FRAGMENT_HIDDEN_STATUS";

    private View view;
    private TextView mRepairArea;
    private EditText mRepairPlace;
    private TextView mRepairCategory;
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
        mRepairCategory = v.findViewById(R.id.repair_select_category);
        mRepairCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);// 自定义对话框
                builder.setSingleChoiceItems(mCategory, 0, new DialogInterface.OnClickListener() {// 2默认的选中

                    @Override
                    public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                        // showToast(which+"");
                        //setmCategorySelected(mCategory[which]);
                        mRepairCategory.setText(mCategory[which]);
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

            }
        });
    }

}

