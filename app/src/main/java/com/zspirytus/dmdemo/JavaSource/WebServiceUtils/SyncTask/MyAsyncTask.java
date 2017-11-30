package com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.zspirytus.dmdemo.Interface.getAvatarResponse;
import com.zspirytus.dmdemo.Interface.getBooleanTypeResponse;
import com.zspirytus.dmdemo.Interface.getModRegResponse;
import com.zspirytus.dmdemo.Interface.getRepBasInfoResponse;
import com.zspirytus.dmdemo.Interface.getSnobyAccountResponse;
import com.zspirytus.dmdemo.Interface.getStudentBasicInfoResponse;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;

import java.util.ArrayList;

/**
 * Created by ZSpirytus on 2017/11/4.
 */

public class MyAsyncTask<T> extends AsyncTask<ArrayList<String>, Integer, ArrayList<String>> {

    public T response;

    private static final String TAG = "MyAsyncTask";
    private final String methodName;
    private ProgressBar mProgressBar;
    private Context context;
    private ProgressDialog mProgressDialog;

    public MyAsyncTask(Context context,String methodName, ProgressBar mProgressBar){
        this.methodName = methodName;
        this.mProgressBar = mProgressBar;
        this.context = context;
    }

    public MyAsyncTask(Context context, String methodName){
        this.methodName = methodName;
        this.context = context;
        mProgressBar = null;
    }

    public void setListener(T response){
        this.response = response;
    }

    @Override
    protected void onPreExecute(){
        if(mProgressBar != null)
            mProgressBar.setVisibility(View.VISIBLE);
        else {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("Send");
            mProgressDialog.setMessage("Sending...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        ArrayList<String> paramType = params[0];
        ArrayList<String> param = params[1];
        ArrayList<String> result = WebServiceConnector.executingMethod(methodName, paramType,param);
        Log.d(TAG,"AsyncTask doInBackground Test:\t"+Boolean.toString(result == null));
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        getTaskResult(result);
        if(mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
        else {
            mProgressDialog.dismiss();
        }
    }

    private void getTaskResult(ArrayList<String> result){
        switch (methodName){
            case WebServiceConnector.METHOD_GETBASICINFOBYSNO:
                ((getStudentBasicInfoResponse)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_GETSNOBYACCOUNT:
                ((getSnobyAccountResponse)response).getSno(result);
                break;
            case WebServiceConnector.METHOD_MODIFYPWD:
                ((getModRegResponse)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_REGISTERACCOUNT:
                ((getModRegResponse)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_GETAVATAR:
                ((getAvatarResponse)response).getAvatar(result);
                break;
            case WebServiceConnector.METHOD_GETREPAIRBASICINFOBYSNO:
                ((getRepBasInfoResponse)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_NEWRETURNLATELY:
                ((getBooleanTypeResponse)response).showDialog(result);
                break;
            case WebServiceConnector.METHOD_NEWREPAIRREPORT:
                ((getBooleanTypeResponse)response).showDialog(result);
                break;
            case  WebServiceConnector.METHOD_NEWSTUDENTLEAVINGSCHOOL:
                ((getBooleanTypeResponse)response).showDialog(result);
                break;
            case WebServiceConnector.METHOD_UPDATEAVATAR:
                ((getBooleanTypeResponse)response).showDialog(result);
                break;
        }
        Log.d(TAG,"onPostExecute Test:\t"+methodName);
        if(result != null)
            Log.d(TAG,"onPostExecute's result Test:\t"+result.size());
    }
}
