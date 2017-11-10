package com.zspirytus.dmdemo.JavaSource.WebServiceUtils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ZSpirytus on 2017/11/4.
 */

public class GetStudentBasicInfoBySno extends AsyncTask<String, Integer, ArrayList<String>> {

    private static final String TAG = "GetStudentBasicInfoBySn";
    ArrayList<String> result;

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> paramType = new ArrayList<String>();
        ArrayList<String> param = new ArrayList<String>();
        paramType.clear();
        param.clear();
        paramType.add(params[0]);
        param.add(params[1]);
        return WebServiceConnector.executingMethod(WebServiceConnector.METHOD_GETBASICINFOBYSNO, paramType,param);
    }

    @Override
    protected void onProgressUpdate(Integer...values){

    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);
        this.result = result;
    }

    public ArrayList<String> getResult(){
        return result;
    }
}
