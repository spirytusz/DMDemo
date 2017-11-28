package com.zspirytus.dmdemo.JavaSource.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by ZSpirytus on 2017/11/27.
 */

public class DialogUtil {

    public static void TimePicker(Activity activity, final TextView textView){
        Calendar c = Calendar.getInstance();
        // 直接创建一个TimePickerDialog对话框实例，并将它显示出来
        new TimePickerDialog(activity,
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
                c.get(Calendar.HOUR), c.get(Calendar.MINUTE),true).show();
    }

    public static void DatePicker(Activity activity,final TextView textView){
        Calendar c = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity,
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
                        textView.setText(year + " - " + month + " - " + day);
                    }
                }
                // 设置初始日期
                , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void showResultDialog(Activity activity,final boolean isSuccess){
        String FormatResult = "";
        if(isSuccess){
            FormatResult = "成功";
        } else {
            FormatResult = "失败";
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("提示");
        dialog.setMessage(FormatResult);
        dialog.setPositiveButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    public static void showNegativeTipsDialog(Activity activity){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("提示");
        dialog.setMessage("输入值为空或违反约束");
        dialog.setPositiveButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }
}
