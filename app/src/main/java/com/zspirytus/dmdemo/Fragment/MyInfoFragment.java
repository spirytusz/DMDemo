package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.zspirytus.dmdemo.Activity.RepairScheduleActivity;
import com.zspirytus.dmdemo.Interface.SetMyInfoAvatar;
import com.zspirytus.dmdemo.JavaSource.FragmentCollector;
import com.zspirytus.dmdemo.JavaSource.InfoItem;
import com.zspirytus.dmdemo.JavaSource.PhotoUtils;
import com.zspirytus.dmdemo.R;
import com.zspirytus.dmdemo.Reproduction.CircleImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by 97890 on 2017/9/9.
 */

public class MyInfoFragment extends Fragment{

    private static final String mAvatarKey = "Avatar";
    private static final String FRAGMENT_HIDDEN_STATUS = "FRAGMENT_HIDDEN_STATUS";

    private SetMyInfoAvatar setAvatar;

    private ListView mInfo;
    private ListView mSchedule;
    private CircleImageView mAvatar;

    private String[] item;

    @Override
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_myinfofragment,container,false);
        LoadPane(view);
        RestoreAvatar();
        return  view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        setAvatar = (SetMyInfoAvatar)((Activity)context);
    }

    @Override
    public void onDestroyView(){
        FragmentCollector.removeFragment(this);
        super.onDestroyView();
    }

    public void RestoreAvatar(){
        SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        String avatar = pref.getString(mAvatarKey,"");
        if(!avatar.equals(""))
            mAvatar.setImageBitmap(PhotoUtils.getBitmapbyString(avatar));
    }

    private SimpleAdapter getAdapter(){
        List<InfoItem> list = getListViewItemList();
        List<Map<String,String>> result = getListData(list);
        SimpleAdapter adapter=new SimpleAdapter(
                getActivity(),
                result,
                R.layout.layout_infolistview_item,
                new String[]{"rowName","rowElement"},
                new int[]{R.id.rowname,R.id.rowElement}
        );
        return adapter;
    }

    /**
     *
     * @return 加入了InfoItem的List<InfoItem> list
     */
    private List<InfoItem> getListViewItemList(){
        String[] str = {
                getString(R.string.Student_Name),
                getString(R.string.Student_ID),
                getString(R.string.Student_Department),
                getString(R.string.Student_Class)
        };
        item = new String[str.length];
        List<InfoItem> list = new ArrayList<InfoItem>();
        list.clear();
        InfoItem[] info = new InfoItem[str.length];
        for(int i=0;i<str.length;i++){
            info[i] = new InfoItem();
            info[i].setRowName(str[i]);
            info[i].setRowElement(item[i]);
            list.add(info[i]);
        }
        return list;
    }

    /**
     *
     * @param list    List<InfoItem>
     * @return        List<Map<String, String>> result
     */
    private List<Map<String, String>> getListData(List<InfoItem> list){
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for(InfoItem info:list){
            Map<String, String> map = new HashMap<String, String>();
            String rowName = info.getRowName();
            String rowElement = info.getRowElement();
            map.put("rowName", rowName);
            map.put("rowElement", rowElement);
            result.add(map);
        }
        return result;
    }

    public void LoadPane(View v){
        String[] mInfoItem = {
                getActivity().getString(R.string.Student_Name).toString(),
                getActivity().getString(R.string.Student_ID).toString(),
                getActivity().getString(R.string.Student_Department).toString(),
                getActivity().getString(R.string.Student_Class).toString()
        };
        List<String> mInfoList = new ArrayList<>(Arrays.asList(mInfoItem));
        String[] mScheduleItem = {
                getActivity().getString(R.string.Repair).toString(),
                getActivity().getString(R.string.Enrollment).toString(),
                getString(R.string.Back_late).toString()
        };
        List<String> mScheduleList = new ArrayList<>(Arrays.asList(mScheduleItem));
        mAvatar = v.findViewById(R.id.mAvatar);
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getString(R.string.Modify_Your_Avatar));
                //    指定下拉列表的显示数据
                final String[] mSelectItem = {
                        getActivity().getString(R.string.By_Camera),
                        getActivity().getString(R.string.By_Album),
                        getActivity().getString(R.string.Cancel)
                };
                //    设置一个下拉的列表选择项
                builder.setItems(mSelectItem, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which){
                            case 0:
                                setAvatar.setAvatar(mAvatar,1);
                                break;
                            case 1:
                                setAvatar.setAvatar(mAvatar,2);
                                break;
                            case 2:
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        mInfo = v.findViewById(R.id.layout_minfo_lisview_top);
        mSchedule = v.findViewById(R.id.layout_minfo_lisview_bottom);
        ArrayAdapter<String> mInfoAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mInfoItem);
        mInfo.setAdapter(getAdapter());
        final ArrayAdapter<String> mScheduleAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mScheduleList);
        mSchedule.setAdapter(mScheduleAdapter);
        mSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = ((TextView)view).getText().toString();
                switch (text){
                    case "Repair":
                        RepairScheduleActivity.StartThisActivity(getActivity());
                        break;
                    case "Enrollment of Leaving School":
                        break;
                }
            }
        });
    }
}


