package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zspirytus.dmdemo.Activity.LoginActivity;
import com.zspirytus.dmdemo.JavaSource.ActivityManager;
import com.zspirytus.dmdemo.JavaSource.FragmentCollector;
import com.zspirytus.dmdemo.R;


/**
 * Created by 97890 on 2017/9/9.
 */

public class Settings extends Fragment {
    private Activity mParentActivity;

    private static final String TAG = "Settings";
    private static final String FRAGMENT_HIDDEN_STATUS = "FRAGMENT_HIDDEN_STATUS";

    private View view;
    private ListView listView;

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

    public void LoadPane(View v){
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
                        LoginActivity.StartThisActivity(mParentActivity);
                        break;
                    case 1:
                        SharedPreferences pref = mParentActivity.getSharedPreferences("data", Context.MODE_PRIVATE);
                        pref.edit().clear().commit();
                        Toast.makeText(mParentActivity,getString(R.string.Delete_Successfully),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

