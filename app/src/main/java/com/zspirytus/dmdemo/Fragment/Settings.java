package com.zspirytus.dmdemo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zspirytus.dmdemo.Activity.LoginActivity;
import com.zspirytus.dmdemo.R;

import java.io.File;


/**
 * Created by 97890 on 2017/9/9.
 */

public class Settings extends Fragment {
    private static final String TAG = "Settings";

    private View view;
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.layout_settingsfragment,container,false);
        LoadPane(view);
        return view;
    }

    public void LoadPane(View v){
        String[] mListViewItem = {
                getActivity().getString(R.string.Switch_Account).toString(),
                getActivity().getString(R.string.Delete_Account_Info).toString()
        };
        listView = v.findViewById(R.id.settingsfragment_listview);
        ArrayAdapter<String> mListViewAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mListViewItem);
        listView.setAdapter(mListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = ((TextView)view).getText().toString();
                switch (text){
                    case "Switch Account":
                        getActivity().finish();
                        LoginActivity.StartThisActivity(getActivity());
                        break;
                    case "Delete Account Info":
                        File file = new File("/data/data"+getActivity().getPackageName()+"/shared_prefs/data.xml");
                        if(file.exists())
                            file.delete();
                }
            }
        });
    }
}

