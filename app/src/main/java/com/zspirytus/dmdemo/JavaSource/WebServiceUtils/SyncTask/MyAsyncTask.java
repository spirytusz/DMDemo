package com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask;

import android.os.AsyncTask;

import com.zspirytus.dmdemo.Interface.getStudentBasicInfoResponse;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;

import java.util.ArrayList;

/**
 * Created by ZSpirytus on 2017/11/4.
 */

public class MyAsyncTask extends AsyncTask<ArrayList<String>, Integer, ArrayList<String>> {

    private static final String TAG = "MyAsyncTask";
    private final String methodName;

    public MyAsyncTask(String methodName){
        this.methodName = methodName;
    }

    public getStudentBasicInfoResponse response;

    public void setListener(getStudentBasicInfoResponse response){
        this.response = response;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        ArrayList<String> paramType = params[0];
        ArrayList<String> param = params[1];
        return WebServiceConnector.executingMethod(methodName, paramType,param);
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        response.getResult(result);
    }
}
