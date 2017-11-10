package com.zspirytus.dmdemo.JavaSource.WebServiceUtils;

import android.os.AsyncTask;

import com.zspirytus.dmdemo.Interface.getStudentBasicInfoResponse;

import java.util.ArrayList;

/**
 * Created by ZSpirytus on 2017/11/4.
 */

public class GetStudentBasicInfoBySno extends AsyncTask<ArrayList<String>, Integer, ArrayList<String>> {

    private static final String TAG = "GetStudentBasicInfoBySn";

    public getStudentBasicInfoResponse response;

    public void setListener(getStudentBasicInfoResponse response){
        this.response = response;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        ArrayList<String> paramType = params[0];
        ArrayList<String> param = params[1];
        return WebServiceConnector.executingMethod(WebServiceConnector.METHOD_GETBASICINFOBYSNO, paramType,param);
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        response.getResult(result);
    }
}
