package com.zspirytus.dmdemo.JavaSource.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


/**
 * Created by ZSpirytus on 2017/11/27.
 */

public class DialogUtil {

    private static final String SUCCESS = "成功";
    private static final String FAILED = "失败";
    private static final String TIP = "提示";
    private static final String OK = "好";
    private static final String ILLEGAL = "输入值为空或违反约束";

    public static void TimePicker(Activity activity, final TextView textView){
        int hour;
        if(is24((Context)activity)){
            hour = DateUtil.getNowHour_int();
        } else {
            hour = DateUtil.getNowHour_int()%12;
        }
        TimePickerDialog dialog = new TimePickerDialog(activity,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String mHour = "";
                        String mMinute = "";
                        if(hourOfDay<10){
                            mHour = "0" + hourOfDay;
                        } else {
                            mHour = "" + hourOfDay;
                        }
                        if(minute<10){
                            mMinute = "0" + minute;
                        } else {
                            mMinute = "" + minute;
                        }
                        textView.setText(mHour+":"+mMinute);
                    }
                },
                // 设置初始时间
                hour, DateUtil.getNowMinute_int(),true);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void DatePicker(Activity activity,final TextView textView){
        DatePickerDialog dialog = new DatePickerDialog(activity,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month = "" + (monthOfYear + 1);
                        String day = "" + dayOfMonth;
                        if (monthOfYear < 10)
                            month = "0" + month;
                        if (dayOfMonth < 10)
                            day = "0" + day;
                        textView.setText(year + "-" + month + "-" + day);
                    }
                },
                // 设置初始日期
                DateUtil.getNowYear_int(), DateUtil.getNowMonth_int() - 1, DateUtil.getNowDay_int());
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void showResultDialog(Activity activity,final boolean isSuccess){
        String FormatResult = "";
        if(isSuccess){
            FormatResult = SUCCESS;
        } else {
            FormatResult = FAILED;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(TIP);
        dialog.setMessage(FormatResult);
        dialog.setPositiveButton(OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    public static void showNegativeTipsDialog(Activity activity){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(TIP);
        dialog.setMessage(ILLEGAL);
        dialog.setPositiveButton(OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    public static void showNegativeTipsDialog(Activity activity,String errorMessage){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(TIP);
        dialog.setMessage(errorMessage);
        dialog.setPositiveButton(OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    public static void showListDialog(Activity activity,final String[] list,final TextView textView){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                textView.setText(list[which]);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private static boolean is24(Context context){
        return DateFormat.is24HourFormat(context);
    }
}
