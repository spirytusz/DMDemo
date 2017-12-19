package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zspirytus.dmdemo.Activity.LoginActivity;
import com.zspirytus.dmdemo.JavaSource.Manager.ActivityManager;
import com.zspirytus.dmdemo.JavaSource.Manager.FragmentCollector;
import com.zspirytus.dmdemo.R;


/**
 * Created by 97890 on 2017/9/9.
 */

public class Settings extends Fragment {
    private Activity mParentActivity;

    private View view;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.layout_settingsfragment,container,false);
        LoadPane(view);
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParentActivity = (Activity) context;
    }

    @Override
    public void onDestroyView(){
        FragmentCollector.removeFragment(this);
        super.onDestroyView();
    }

    /**
     * init Pane
     * @param v rootView
     */
    private void LoadPane(View v){
        String[] mListViewItem = {
                getString(R.string.Switch_Account).toString(),
                getString(R.string.Delete_Account_Info).toString()
        };
        listView = v.findViewById(R.id.settingsfragment_listview);
        ArrayAdapter<String> mListViewAdapter = new ArrayAdapter<String>(mParentActivity,android.R.layout.simple_list_item_1,mListViewItem);
        listView.setAdapter(mListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = ((TextView)view).getText().toString();
                switch (i){
                    case 0:
                        ActivityManager.finishAll();
                        clearAccountInfo();
                        LoginActivity.StartThisActivity(mParentActivity);
                        break;
                    case 1:
                        clearAccountInfo();
                        Toast.makeText(mParentActivity,getString(R.string.Delete_Successfully),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * clear all account info
     */
    private void clearAccountInfo(){
        SharedPreferences pref1 = mParentActivity.getSharedPreferences("data", Context.MODE_PRIVATE);
        pref1.edit().clear().commit();
        SharedPreferences pref2 = mParentActivity.getSharedPreferences("StudentInfo", Context.MODE_PRIVATE);
        pref2.edit().clear().commit();
        SharedPreferences pref3 = mParentActivity.getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
        pref3.edit().clear().commit();
    }
}

