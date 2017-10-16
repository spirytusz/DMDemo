package com.zspirytus.dmdemo.Fragment;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zspirytus.dmdemo.JavaSource.FragmentCollector;
import com.zspirytus.dmdemo.R;


/**
 * Created by 97890 on 2017/9/17.
 */

public class ELSFragment extends Fragment {

    private static final String TAG = "ELSchool";

    private View view;
    private EditText mStudentId;
    private EditText mStudentName;
    private TextView mStartTime;
    private TextView mEndTime;
    private EditText mReason;
    private Button mElsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.layout_elsfragment,container,false);
        LoadPane(view);
        return view;
    }

    @Override
    public void onDestroyView(){
        FragmentCollector.removeFragment(this);
        super.onDestroyView();
    }

    public void LoadPane(View v){
        mStudentId = v.findViewById(R.id.edittext_studId);
        mStudentName = v.findViewById(R.id.edittext_studName);
        mStartTime = v.findViewById(R.id.els_start_time);
        mStartTime.setOnClickListener(new View.OnClickListener() {
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
                                mStartTime.setText( year + " - " + month + " - " + day );
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
                                mEndTime.setText( year + " - " + month + " - " + day );
                            }
                        }
                        // 设置初始日期
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mElsButton = v.findViewById(R.id.els_button);
        mElsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage(mStudentId.getText().toString(),mStudentName.getText().toString(),mStartTime.getText().toString(),mEndTime.getText().toString());
            }
        });
    }

    public void SendMessage(String stdID,String stdName,String start,String end){
        Toast.makeText(getActivity(),stdID+"\n"+stdName+"\n"+start+"\n"+end,Toast.LENGTH_SHORT).show();
    }
}
