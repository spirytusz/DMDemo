package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.zspirytus.dmdemo.R;

/**
 * Created by 97890 on 2017/9/17.
 */

public class RepairFragment extends Fragment {

    private static final String TAG = "RepairFragment";

    private View view;
    private EditText mStudentId;
    private EditText mStudnetName;
    private TextView mRepairArea;
    private EditText mRepairPlace;
    private TextView mRepairCategory;
    private EditText mRepairDetail;
    private TextView mRepairDate;
    private TextView mRepairTime;
    private EditText mRepairContact;
    private TextView mRepairPhoto;
    private Button mRepairSend;

    private SetUploadPicPath listener;


    private String outputDirectory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.layout_repairfragment,container,false);
        Toast.makeText(getActivity(),TAG, Toast.LENGTH_SHORT).show();
        LoadPane(view);
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (SetUploadPicPath)((Activity)context);
    }

    public void LoadPane(View v){
        final String[] mArea = {
                getActivity().getString(R.string.Public_Area).toString(),
                getActivity().getString(R.string.Living_Area).toString()
        };
        final String[] mCategory = {
                getActivity().getString(R.string.Electrician).toString(),
                getActivity().getString(R.string.Hydraulic).toString(),
                getActivity().getString(R.string.Woodworking).toString(),
                getActivity().getString(R.string.Construction).toString(),
                getActivity().getString(R.string.Equipment).toString(),
                getActivity().getString(R.string.Other).toString()
        };
        mStudentId = v.findViewById(R.id.repair_edittext_id);
        mStudnetName = v.findViewById(R.id.repair_edittext_name);
        mRepairArea = v.findViewById(R.id.repair_select_area);
        mRepairArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());// 自定义对话框
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());// 自定义对话框
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
        mRepairDate = v.findViewById(R.id.repair_select_date);
        mRepairDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(getActivity(),
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = ""+ (monthOfYear + 1);
                                String day = ""+dayOfMonth;
                                if(monthOfYear<10)
                                    month = "0" + month;
                                if(dayOfMonth<10)
                                    day = "0" + day;
                                mRepairDate.setText( year + " - " + month + " - " + day );
                            }
                        }
                        // 设置初始日期
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mRepairTime = v.findViewById(R.id.repair_select_time);
        mRepairTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                // 创建一个TimePickerDialog实例，并把它显示出来
                new TimePickerDialog(getActivity(),
                        // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String hour = "" + hourOfDay;
                                String minutes = "" + minute;
                                if(hourOfDay < 10)
                                    hour = "0" + hour;
                                if(minute<10)
                                    minutes = "0" + minutes;
                                mRepairTime.setText( hour + " : "  + minutes );
                            }
                        }
                        // 设置初始时间
                        , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        // true表示采用24小时制
                        true).show();
            }
        });
        mRepairContact = v.findViewById(R.id.repair_edittext_contact);
        mRepairPhoto = v.findViewById(R.id.repair_select_photo);
        mRepairPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getString(R.string.By));
                //    指定下拉列表的显示数据
                final String[] mSelectItem = {
                        getActivity().getString(R.string.By_Camera),
                        getActivity().getString(R.string.By_Album),
                        getActivity().getString(R.string.Cancel)
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

