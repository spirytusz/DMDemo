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

import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ZSpirytus on 2017/11/26.
 */

public class BackLateFragment extends Fragment {

    private static final String TAG = "BackLateFragment";
    private static final String mSnoKey = "Sno";
    private Activity mParentActivity;

    private TextView mReturnTime;
    private EditText mReason;
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_backlatefragment,container,false);
        LoadPane(view);
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParentActivity = (Activity) context;
    }

    private void LoadPane(View view){
        mReturnTime = view.findViewById(R.id.blf_returnTime);
        mReturnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.TimePicker(mParentActivity,mReturnTime);
            }
        });
        mReason = view.findViewById(R.id.blf_reason);
        mButton = view.findViewById(R.id.blf_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAsyncTask<getBooleanTypeResponse> myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(mParentActivity,WebServiceConnector.METHOD_NEWRETURNLATELY);
                myAsyncTask.setListener(new getBooleanTypeResponse() {
                    @Override
                    public void showDialog(ArrayList<String> result) {
                        final boolean isSuccess = result.get(0).equals("true");
                        String FormatResult = "";
                        if (isSuccess)
                            FormatResult = "成功";
                        else
                            FormatResult = "失败";
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mParentActivity);
                        dialog.setTitle("提示");
                        dialog.setMessage(FormatResult);
                        dialog.setPositiveButton("好", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(isSuccess){
                                    mReturnTime.setText("");
                                    mReason.setText("");
                                }
                            }
                        });
                        dialog.show();
                    }
                });
                myAsyncTask.execute(getParamType(),getInput());
            }
        });
    }

    private ArrayList<String> getParamType(){
        ArrayList<String> param = new ArrayList<>();
        param.clear();
        param.add(WebServiceConnector.PARAMTYPE_SNO);
        param.add(WebServiceConnector.PARAMTYPE_RNO);
        param.add(WebServiceConnector.PARAMTYPE_RETURNTIME);
        param.add(WebServiceConnector.PARAMTYPE_REASON);
        return param;
    }

    private ArrayList<String> getInput(){
        ArrayList<String> input = new ArrayList<>();
        input.clear();
        Bundle bundle = getArguments();
        String Sno = bundle.getString(mSnoKey);
        String Rno = getRno();
        String returnTime = mReturnTime.getText().toString();
        String reason = mReason.getText().toString();
        if(
            Sno != null && !Sno.equals("")
            && Rno != null && !Rno.equals("")
            && returnTime != null && !returnTime.equals("")
            && reason != null && !reason.equals("")){
            input.add(Sno);
            input.add(Rno);
            input.add(returnTime);
            input.add(reason);
        } else {
            return null;
        }
        return input;
    }

    private String getRno(){
        SimpleDateFormat formator = new SimpleDateFormat("ddHHmmssSSS");
        Date curDate =  new Date(System.currentTimeMillis());
        String Rno = formator.format(curDate);
        return Rno;
    }

    public static BackLateFragment GetThisFragment(String Sno){
        BackLateFragment blf = new BackLateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(mSnoKey,Sno);
        blf.setArguments(bundle);
        return blf;
    }
}
