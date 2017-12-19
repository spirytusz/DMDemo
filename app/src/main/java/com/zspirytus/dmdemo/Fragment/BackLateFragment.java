package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zspirytus.dmdemo.Activity.BaseActivity;
import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.JavaSource.Utils.DateUtil;
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

    /**
     * init Pane
     * @param view rootView
     */
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
                if (!isInputLegal()) {
                    DialogUtil.showNegativeTipsDialog(mParentActivity);
                    return;
                }
                SendMessage();
            }
        });
    }

    /**
     * Send Message to WebService
     */
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
                    BaseActivity.CloseKeyBoard(mParentActivity);
                }
            }
        });
        myAsyncTask.execute(getParamType(), getInput());
    }

    /**
     * get WebService Method Params Name
     * @return Params Name
     */
    private ArrayList<String> getParamType() {
        ArrayList<String> param = new ArrayList<>();
        param.clear();
        param.add(WebServiceConnector.PARAMTYPE_SNO);
        param.add(WebServiceConnector.PARAMTYPE_RNO);
        param.add(WebServiceConnector.PARAMTYPE_RETURNTIME);
        param.add(WebServiceConnector.PARAMTYPE_REASON);
        return param;
    }

    /**
     * get WebService Method Params
     * @return Params
     */
    private ArrayList<String> getInput() {
        ArrayList<String> input = new ArrayList<>();
        input.clear();
        Bundle bundle = getArguments();
        String Sno = bundle.getString(mSnoKey);
        String Rno = getRno();
        String returnTime = DateUtil.getNowYear_int()
                + "-" + DateUtil.getNowMonth_int()
                + "-" + DateUtil.getNowDay_int()
                + " " + mReturnTime.getText().toString()
                + ":00";
        if (DateUtil.isNextDay(mReturnTime.getText().toString(),"HH:mm"))
            returnTime = DateUtil.AddOneDay(returnTime, 1, "yyyy-MM-dd HH:mm:ss");
        String reason = mReason.getText().toString();
        input.add(Sno);
        input.add(Rno);
        input.add(returnTime);
        input.add(reason);
        return input;
    }

    /**
     * is Input legal
     * @return true or false
     */
    private boolean isInputLegal() {
        if (mReason.getText().toString().equals("") || mReturnTime.getText().toString().equals(getString(R.string.Return_Time)))
            return false;
        return true;
    }

    /**
     * Generated Rno randomly
     * @return Rno
     */
    private String getRno() {
        SimpleDateFormat format = new SimpleDateFormat("ddHHmmssSSS");
        Date curDate = new Date(System.currentTimeMillis());
        String Rno = format.format(curDate);
        return Rno;
    }

    /**
     * set Args and get this Fragment
     * @param Sno Sno
     * @return this Fragment
     */
    public static BackLateFragment GetThisFragment(String Sno) {
        BackLateFragment blf = new BackLateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(mSnoKey, Sno);
        blf.setArguments(bundle);
        return blf;
    }

}
