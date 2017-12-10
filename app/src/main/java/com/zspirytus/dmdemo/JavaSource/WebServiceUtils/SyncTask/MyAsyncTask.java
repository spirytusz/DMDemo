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
import com.zspirytus.dmdemo.Interface.getRLBasicInfoByManager;
import com.zspirytus.dmdemo.Interface.getRLDetailsInfoResponse;
import com.zspirytus.dmdemo.Interface.getRLInfoResponse;
import com.zspirytus.dmdemo.Interface.getRepBasInfoResponse;
import com.zspirytus.dmdemo.Interface.getRepBasicInfoByManager;
import com.zspirytus.dmdemo.Interface.getRepDetailsInfoResponse;
import com.zspirytus.dmdemo.Interface.getRepPicResponse;
import com.zspirytus.dmdemo.Interface.getSLSBasicInfoByManager;
import com.zspirytus.dmdemo.Interface.getSLSDetailsInfoResponse;
import com.zspirytus.dmdemo.Interface.getSLSInfoResponse;
import com.zspirytus.dmdemo.Interface.getSnobyAccountResponse;
import com.zspirytus.dmdemo.Interface.getStudentBasicInfoResponse;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;

import java.util.ArrayList;

/**
 * Created by ZSpirytus on 2017/11/4.
 */

public class MyAsyncTask<T> extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

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
        else if(!methodName.equals(WebServiceConnector.METHOD_GETREPBASICINFOBMP)){
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
    public void onProgressUpdate(Void...param)
    {
        //Task被取消了，不再继续执行后面的代码
        if(isCancelled())
            return;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        getTaskResult(result);
        if(mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
        else if(!methodName.equals(WebServiceConnector.METHOD_GETREPBASICINFOBMP)){
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
            case WebServiceConnector.METHOD_GETSLSBASICINFO:
                ((getSLSInfoResponse)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_GETRETURNLATELYBASICINFO:
                ((getRLInfoResponse)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_GETREPAIRDETAILSINFO:
                ((getRepDetailsInfoResponse)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_GETRLDETAILSINFO:
                ((getRLDetailsInfoResponse)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_GETSLSDETAILSINFO:
                ((getSLSDetailsInfoResponse)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_GETREPBASICINFOBMP:
                ((getRepPicResponse)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_GETREPBASICBYMANAGER:
                ((getRepBasicInfoByManager)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_GETSLSBASICBYMANAGER:
                ((getSLSBasicInfoByManager)response).getResult(result);
                break;
            case WebServiceConnector.METHOD_GETRLBASICBYMANAGER:
                ((getRLBasicInfoByManager)response).getResult(result);
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
            case WebServiceConnector.METHOD_UPDATEREP:
                ((getBooleanTypeResponse)response).showDialog(result);
                break;
            case WebServiceConnector.METHOD_UPDATEELS:
                ((getBooleanTypeResponse)response).showDialog(result);
                break;
            case WebServiceConnector.METHOD_UPDATERL:
                ((getBooleanTypeResponse)response).showDialog(result);
                break;
            case WebServiceConnector.METHOD_DELETEREP:
                ((getBooleanTypeResponse)response).showDialog(result);
                break;
            case WebServiceConnector.METHOD_DELETEELS:
                ((getBooleanTypeResponse)response).showDialog(result);
                break;
            case WebServiceConnector.METHOD_DELETERL:
                ((getBooleanTypeResponse)response).showDialog(result);
                break;
        }
    }

    private boolean shouldShowProgressDialog(){
        return !methodName.equals(WebServiceConnector.METHOD_GETREPBASICINFOBMP) ||
                !methodName.equals(WebServiceConnector.METHOD_GETREPAIRDETAILSINFO) ||
                !methodName.equals(WebServiceConnector.METHOD_GETRLDETAILSINFO) ||
                !methodName.equals(WebServiceConnector.METHOD_GETSLSDETAILSINFO);
    }
}
