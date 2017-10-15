package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zspirytus.dmdemo.Activity.LoginActivity;
import com.zspirytus.dmdemo.Activity.RepairScheduleActivity;
import com.zspirytus.dmdemo.Interface.SetMyInfoAvatar;
import com.zspirytus.dmdemo.R;
import com.zspirytus.dmdemo.Reproduction.CircleImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by 97890 on 2017/9/9.
 */

public class MyInfoFragment extends Fragment{

    private static final String mAvatarKey = "Avatar";
    private static final String hasCustomAvatar = "CustomAvatar";

    private SetMyInfoAvatar setAvatar;

    private ListView mInfo;
    private ListView mSchedule;
    private CircleImageView mAvatar;
    private AppCompatActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_myinfofragment,container,false);
        LoadPane(view);
        return  view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        setAvatar = (SetMyInfoAvatar)((Activity)context);
    }

    public void RestoreEditArea(){
        SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        if(pref.getBoolean(hasCustomAvatar,false))
            mAvatar.setImageBitmap(getBitmapbyString(pref.getString(mAvatarKey,"")));
    }

    public Bitmap getBitmapbyString(String str){
        Bitmap bitmap = null;
        try{
            byte[] bitmapArray;
            bitmapArray = Base64.decode(str,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray,0,bitmapArray.length);
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
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
                getActivity().getString(R.string.Enrollment).toString()
        };
        List<String> mScheduleList = new ArrayList<>(Arrays.asList(mScheduleItem));
        activity = (AppCompatActivity) getActivity();
        mAvatar = v.findViewById(R.id.mAvatar);
        RestoreEditArea();
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
        mInfo.setAdapter(mInfoAdapter);
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
                        Log.d("lkjk","Enrollment of Leaving School");
                        break;
                }
            }
        });
    }

}


