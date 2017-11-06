package com.zspirytus.dmdemo.JavaSource.WebServiceUtils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ZSpirytus on 2017/11/4.
 */

public class GetStudentBasicInfoBySno extends AsyncTask<String, Integer, InputStream> {

    private static final String TAG = "GetStudentBasicInfoBySn";
    InputStream is;

    @Override
    protected InputStream doInBackground(String... params) {
        ArrayList<String> param = new ArrayList<String>();
        ArrayList<String> paramType = new ArrayList<String>();
        param.clear();
        paramType.clear();
        param.add("15251102222");
        paramType.add("sno");
        param.add("ZSpirytus");
        paramType.add("account");
        param.add("zkw012300");
        paramType.add("pwd");
        return WebServiceConnector.executingMethod(WebServiceConnector.METHOD_REG, paramType, param);
    }

    @Override
    //此方法可以在主线程改变UI
    protected void onPostExecute(InputStream result) {
        is = result;
        BufferedReader reader = new BufferedReader(new InputStreamReader(result));
        StringBuilder str = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                str.append(line + "\n");
                }
            } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public InputStream getResult(){
        return is;
    }
}
