package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zspirytus.dmdemo.Activity.BaseActivity;
import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.Manager.FragmentCollector;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;

import java.util.ArrayList;


/**
 * Created by 97890 on 2017/9/17.
 */

public class ELSFragment extends Fragment {

    private Activity mParentActivity;

    private static final String TAG = "ELSchool";
    private static final String FRAGMENT_HIDDEN_STATUS = "FRAGMENT_HIDDEN_STATUS";
    private static final String mSnoKey = "Sno";

    private View view;
    private TextView mStartTime;
    private TextView mEndTime;
    private EditText mReason;
    private Button mElsButton;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_elsfragment, container, false);
        LoadPane(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParentActivity = (Activity) context;
    }

    @Override
    public void onDestroyView() {
        FragmentCollector.removeFragment(this);
        super.onDestroyView();
    }

    public void LoadPane(View v) {
        mStartTime = v.findViewById(R.id.els_start_time);
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.DatePicker(mParentActivity, mStartTime);
            }
        });
        mEndTime = v.findViewById(R.id.els_end_time);
        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.DatePicker(mParentActivity, mEndTime);
            }
        });
        mReason = v.findViewById(R.id.els_reason);
        mElsButton = v.findViewById(R.id.els_button);
        mElsButton.setOnClickListener(new View.OnClickListener() {
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

    public void SendMessage() {
        MyAsyncTask<getBooleanTypeResponse> myAsyncTask = new MyAsyncTask<getBooleanTypeResponse>(mParentActivity, WebServiceConnector.METHOD_NEWSTUDENTLEAVINGSCHOOL);
        myAsyncTask.setListener(new getBooleanTypeResponse() {
            @Override
            public void showDialog(ArrayList<String> result) {
                final boolean isSuccess = result.get(0).equals("true");
                DialogUtil.showResultDialog(mParentActivity, isSuccess);
                if (isSuccess) {
                    mStartTime.setText("");
                    mEndTime.setText("");
                    mReason.setText("");
                    BaseActivity.CloseKeyBoard(mParentActivity);
                }
            }
        });
        myAsyncTask.execute(getParamType(), getInput());
    }

    private ArrayList<Integer> getDateFromTextView(TextView textView) {
        char[] c = textView.getText().toString().toCharArray();
        String y = c[0] + "" + c[1] + "" + c[2] + "" + c[3];
        String m = c[7] + "" + c[8];
        String d = c[12] + "" + c[13];
        Log.d("","format test:\t"+y+"\t"+m+"\t"+d);
        int year = Integer.parseInt(y);
        int month = Integer.parseInt(m);
        int day = Integer.parseInt(d);
        ArrayList<Integer> end = new ArrayList<>();
        end.clear();
        end.add(year);
        end.add(month);
        end.add(day);
        return end;
    }

    private boolean isInputLegal() {
        String start = mStartTime.getText().toString();
        String end = mEndTime.getText().toString();
        String reason = mReason.getText().toString();
        if (start.equals(getString(R.string.Start_Time)) || end.equals(getString(R.string.End_Time)) || reason.equals(""))
            return false;
        Calendar c = Calendar.getInstance();
        ArrayList<Integer> startDate = getDateFromTextView(mStartTime);
        ArrayList<Integer> endDate = getDateFromTextView(mEndTime);
        ArrayList<Integer> nowDate = new ArrayList<>();
        nowDate.clear();
        nowDate.add(c.get(Calendar.YEAR));
        nowDate.add(c.get(Calendar.MONTH));
        nowDate.add(c.get(Calendar.DAY_OF_MONTH));
        return isDateOneBigger(startDate, nowDate) && isDateOneBigger(endDate, startDate);
    }

    private boolean isDateOneBigger(ArrayList<Integer> date1, ArrayList<Integer> date2) {
        for (int i = 0; i < date1.size(); i++)
            if (date1.get(i) > date2.get(i))
                return true;
        return false;
    }

    private ArrayList<String> getParamType() {
        ArrayList<String> paramType = new ArrayList<>();
        paramType.clear();
        paramType.add(WebServiceConnector.PARAMTYPE_SNO);
        paramType.add(WebServiceConnector.PARAMTYPE_LEAVEDATE);
        paramType.add(WebServiceConnector.PARAMTYPE_BACKDATE);
        paramType.add(WebServiceConnector.PARAMTYPE_REASON);
        return paramType;
    }

    private ArrayList<String> getInput() {
        ArrayList<Integer> start = getDateFromTextView(mStartTime);
        ArrayList<Integer> end = getDateFromTextView(mEndTime);
        ArrayList<String> input = new ArrayList<>();
        input.clear();
        String Sno = getArguments().getString(mSnoKey);
        input.add(Sno);
        input.add(start.get(0) + "-" + start.get(1) + "-" + start.get(2));
        input.add(end.get(0) + "-" + end.get(1) + "-" + end.get(2));
        input.add(mReason.getText().toString());
        return input;
    }

    public static ELSFragment GetThisFragment(String Sno) {
        ELSFragment els = new ELSFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(mSnoKey, Sno);
        els.setArguments(bundle);
        return els;
    }
}
