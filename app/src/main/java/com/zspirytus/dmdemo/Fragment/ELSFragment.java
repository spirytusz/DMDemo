package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.Toast;

import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.JavaSource.FragmentCollector;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by 97890 on 2017/9/17.
 */

public class ELSFragment extends Fragment {

    private Activity mParentActivity;

    private static final String TAG = "ELSchool";
    private static final String FRAGMENT_HIDDEN_STATUS = "FRAGMENT_HIDDEN_STATUS";

    private View view;
    private TextView mStartTime;
    private TextView mEndTime;
    private EditText mReason;
    private Button mElsButton;
    private String mStartYear;
    private String mStartMonth;
    private String mStartDay;
    private String mEndYear;
    private String mEndMonth;
    private String mEndDay;

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
        view = inflater.inflate(R.layout.layout_elsfragment,container,false);
        LoadPane(view);
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParentActivity = (Activity) context;
    }

    @Override
    public void onDestroyView(){
        FragmentCollector.removeFragment(this);
        super.onDestroyView();
    }

    public void LoadPane(View v){
        mStartTime = v.findViewById(R.id.els_start_time);
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(mParentActivity,
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
                                mStartTime.setText( year + " - " + month + " - " + day );
                                getStartDate(year+"",month,day);
                            }
                        }
                        // 设置初始日期
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mEndTime = v.findViewById(R.id.els_end_time);
        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(mParentActivity,
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
                                mEndTime.setText( year + " - " + month + " - " + day );
                                getEndDate(year+"",month,day);
                            }
                        }
                        // 设置初始日期
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mReason = v.findViewById(R.id.els_reason);
        mElsButton = v.findViewById(R.id.els_button);
        mElsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage("15251102224");
            }
        });
    }

    public void SendMessage(String sno){
        MyAsyncTask<getBooleanTypeResponse> myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(mParentActivity,WebServiceConnector.METHOD_NEWSTUDENTLEAVINGSCHOOL);
        myAsyncTask.setListener(new getBooleanTypeResponse() {
            @Override
            public void showDialog(ArrayList<String> result) {
                String FormatResult = "";
                if(result.get(0).equals("true"))
                    FormatResult = "成功";
                else
                    FormatResult = "失败";
                AlertDialog.Builder dialog = new AlertDialog.Builder(mParentActivity);
                dialog.setTitle("提示");
                dialog.setMessage(FormatResult);
                dialog.setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                dialog.show();
            }
        });
        myAsyncTask.execute(getParmType(),getInput(sno));
    }

    private ArrayList<String> getEndDate(){
        ArrayList<String> end = new ArrayList<>();
        end.clear();
        end.add(mEndYear);
        end.add(mEndMonth);
        end.add(mEndDay);
        return  end;
    }

    private void getEndDate(String year,String month,String day){
        mEndYear = year;
        mEndMonth = month;
        mEndDay = day;
    }

    private ArrayList<String> getStartDate(){
        ArrayList<String> start = new ArrayList<>();
        start.clear();
        start.add(mStartYear);
        start.add(mStartMonth);
        start.add(mStartDay);
        return start;
    }

    private void getStartDate(String year,String month,String day){
        mStartYear = year;
        mStartMonth = month;
        mStartDay = day;
    }

    private boolean isInputlegal(ArrayList<String> start,ArrayList<String> end){
        boolean islegal = false;
        Calendar c = Calendar.getInstance();
        int nowYear = c.get(Calendar.YEAR);
        int nowMonth = c.get(Calendar.MONTH);
        int nowDay = c.get(Calendar.DAY_OF_MONTH);
        int startYear = Integer.parseInt(start.get(0));
        int startMonth = Integer.parseInt(start.get(1));
        int startDay = Integer.parseInt(start.get(3));
        int endYear = Integer.parseInt(end.get(0));
        int endMonth = Integer.parseInt(end.get(1));
        int endDay = Integer.parseInt(end.get(3));
        return islegal;
    }

    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    private ArrayList<String> getParmType(){
        ArrayList<String> paramType = new ArrayList<>();
        paramType.clear();
        paramType.add(WebServiceConnector.PARAMTYPE_SNO);
        paramType.add(WebServiceConnector.PARAMTYPE_LEAVEDATE);
        paramType.add(WebServiceConnector.PARAMTYPE_BACKDATE);
        paramType.add(WebServiceConnector.PARAMTYPE_REASON);
        return paramType;
    }

    private ArrayList<String> getInput(String sno){
        ArrayList<String> input = new ArrayList<>();
        input.clear();
        input.add(sno);
        input.add(mStartYear+"-"+mStartMonth+"-"+mStartDay);
        input.add(mEndYear+"-"+mEndMonth+"-"+mEndDay);
        input.add(mReason.getText().toString());
        return input;
    }
}
