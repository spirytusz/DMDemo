package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_backlatefragment, container, false);
        LoadPane(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParentActivity = (Activity) context;
    }

    private void LoadPane(View view) {
        mReturnTime = view.findViewById(R.id.blf_returnTime);
        mReturnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.TimePicker(mParentActivity, mReturnTime);
            }
        });
        mReason = view.findViewById(R.id.blf_reason);
        mButton = view.findViewById(R.id.blf_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getmReturnTime();
                if (!isInputLegal()) {
                    DialogUtil.showNegativeTipsDialog(mParentActivity);
                    return;
                }
                SendMessage();
            }
        });
    }

    private void SendMessage() {
        MyAsyncTask<getBooleanTypeResponse> myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(mParentActivity, WebServiceConnector.METHOD_NEWRETURNLATELY);
        myAsyncTask.setListener(new getBooleanTypeResponse() {
            @Override
            public void showDialog(ArrayList<String> result) {
                final boolean isSuccess = result.get(0).equals("true");
                DialogUtil.showResultDialog(mParentActivity, isSuccess);
                if (isSuccess) {
                    mReturnTime.setText("");
                    mReason.setText("");
                }
            }
        });
        myAsyncTask.execute(getParamType(), getInput());
    }

    private ArrayList<String> getParamType() {
        ArrayList<String> param = new ArrayList<>();
        param.clear();
        param.add(WebServiceConnector.PARAMTYPE_SNO);
        param.add(WebServiceConnector.PARAMTYPE_RNO);
        param.add(WebServiceConnector.PARAMTYPE_RETURNTIME);
        param.add(WebServiceConnector.PARAMTYPE_REASON);
        return param;
    }

    private ArrayList<String> getInput() {
        ArrayList<String> input = new ArrayList<>();
        input.clear();
        Bundle bundle = getArguments();
        String Sno = bundle.getString(mSnoKey);
        String Rno = getRno();
        String returnTime = mReturnTime.getText().toString();
        String reason = mReason.getText().toString();
        if (
                Sno != null && !Sno.equals("")
                        && Rno != null && !Rno.equals("")
                        && returnTime != null && !returnTime.equals("")
                        && reason != null && !reason.equals("")) {
            input.add(Sno);
            input.add(Rno);
            input.add(returnTime);
            input.add(reason);
        } else {
            return null;
        }
        return input;
    }

    private boolean isInputLegal() {
        if (mReason.getText().toString().equals(""))
            return false;
        ArrayList<Integer> inputTime = getmReturnTime();
        Calendar c = Calendar.getInstance();
        int nowMinute = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
        int inputMinute = inputTime.get(0) * 60 + inputTime.get(1);
        if (nowMinute >= inputMinute)
            return false;
        return true;
    }

    private ArrayList<Integer> getmReturnTime() {
        char[] c = mReturnTime.getText().toString().toCharArray();
        String hour = c[0] + "" + c[1] + "";
        String minute = c[3] + "" + c[4] + "";
        ArrayList<Integer> time = new ArrayList<>();
        time.clear();
        time.add(Integer.parseInt(hour));
        time.add(Integer.parseInt(minute));
        for(int i = 0;i<time.size();i++)
            Log.d("","Input Time Test:\t"+time.get(i));
        return time;
    }

    private String getRno() {
        SimpleDateFormat formator = new SimpleDateFormat("ddHHmmssSSS");
        Date curDate = new Date(System.currentTimeMillis());
        String Rno = formator.format(curDate);
        return Rno;
    }

    public static BackLateFragment GetThisFragment(String Sno) {
        BackLateFragment blf = new BackLateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(mSnoKey, Sno);
        blf.setArguments(bundle);
        return blf;
    }
}
