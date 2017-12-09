package com.zspirytus.dmdemo.JavaSource.Utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ZSpirytus on 2017/12/6.
 */

public class DateUtil {

    private static final String YEAR = "yyyy";
    private static final String MONTH = "MM";
    private static final String DAY = "dd";
    private static final String HOUR = "HH";
    private static final String MINUTES = "mm";
    private static final String DATE = YEAR+"-"+MONTH+"-"+DAY;
    private static final String TIME = HOUR+":"+MINUTES+":ss";


    public static String getNowYear_str(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(YEAR);
        return mFormat.format(date);
    }

    public static int getNowYear_int(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(YEAR);
        return Integer.parseInt(mFormat.format(date));
    }

    public static String getNowMonth_str(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(MONTH);
        return mFormat.format(date);
    }

    public static int getNowMonth_int(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(MONTH);
        return Integer.parseInt(mFormat.format(date));
    }

    public static String getNowDay_str(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(DAY);
        return mFormat.format(date);
    }

    public static int getNowDay_int(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(DAY);
        return Integer.parseInt(mFormat.format(date));
    }

    public static String getNowHour_str(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(HOUR);
        return mFormat.format(date);
    }

    public static int getNowHour_int(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(HOUR);
        return Integer.parseInt(mFormat.format(date));
    }

    public static String getNowMinute_str(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(MINUTES);
        return mFormat.format(date);
    }

    public static int getNowMinute_int(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(MINUTES);
        return Integer.parseInt(mFormat.format(date));
    }

    public static String getNowDate(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(DATE);
        return mFormat.format(date);
    }

    public static String getNowDate(String format){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(format);
        return mFormat.format(date);
    }

    public static String getNowTime(){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(TIME);
        return mFormat.format(date);
    }

    public static String getNowTime(String format){
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat(format);
        return mFormat.format(date);
    }

    public static boolean isDateOneBigger(String date1,String date2){
        SimpleDateFormat format = new SimpleDateFormat(DATE);
        Date mDate1;
        Date mDate2;
        try{
            mDate1 = format.parse(date1);
            mDate2 = format.parse(date2);
            if(mDate1.getTime()>mDate2.getTime())
                return true;
            else
                return false;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    public static String AddOneDay(String day,int Num,String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() + (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    public static boolean isNextDay(String time,String format) {
        SimpleDateFormat mFormat = new SimpleDateFormat(format);
        String now = DateUtil.getNowTime(format);
        try {
            Date nowTime = mFormat.parse(now);
            Date inputTime = mFormat.parse(time);
            if (inputTime.getTime() < nowTime.getTime())
                return true;
            else
                return false;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String FormatDate(String date,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date d = new Date(date);
        return simpleDateFormat.format(d);
    }

}
