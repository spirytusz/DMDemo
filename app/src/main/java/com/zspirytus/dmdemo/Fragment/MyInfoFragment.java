package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.zspirytus.dmdemo.Activity.MainActivity;
import com.zspirytus.dmdemo.Activity.SubMainActivity;
import com.zspirytus.dmdemo.Interface.SetMyInfoAvatar;
import com.zspirytus.dmdemo.Interface.getAvatarResponse;
import com.zspirytus.dmdemo.JavaSource.FragmentCollector;
import com.zspirytus.dmdemo.JavaSource.InfoItem;
import com.zspirytus.dmdemo.JavaSource.Utils.PhotoUtil;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.SyncTask.MyAsyncTask;
import com.zspirytus.dmdemo.JavaSource.WebServiceUtils.WebServiceConnector;
import com.zspirytus.dmdemo.R;
import com.zspirytus.dmdemo.Reproduction.CircleImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by 97890 on 2017/9/9.
 */

public class MyInfoFragment extends Fragment{

    private Activity mParentActivity;

    private static final String mAvatarKey = "Avatar";
    private static final String FRAGMENT_HIDDEN_STATUS = "FRAGMENT_HIDDEN_STATUS";
    private static final String SNO = "Sno";
    private static final String SNAME = "Sname";
    private static final String SCOLLEGE = "Scollege";
    private static final String SDEPT = "Sdept";
    private static final String INFO_FILENAME = "StudentInfo";

    private SetMyInfoAvatar setAvatar;

    private ListView mInfo;
    private ListView mSchedule;
    private CircleImageView mAvatar;
    private ArrayList<String> inform;
    private ProgressBar mProgressBar;

    public static MyInfoFragment GetThisFragment(Context context,Object...obj){
        MyInfoFragment mFragment = new MyInfoFragment();
        saveStudentInfo(context,(ArrayList<String>) obj[0]);
        Bundle bundle = new Bundle();
        bundle.putSerializable("obj", (Serializable) (ArrayList<String>) obj[0]);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    public static ArrayList<String> FormatList(ArrayList<String> oldList){
        ArrayList<String> list = new ArrayList<String>();
        list.clear();
        String[] oldListToString = new String[5];
        for(int i =0;i<5;i++){
            oldListToString[i] = oldList.get(i);
        }
        for(int i = 0;i<3;i++)
            list.add(oldListToString[i]);
        list.add(oldListToString[3]+oldListToString[4]+"班");
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_myinfofragment,container,false);
        Bundle bundle = getArguments();
        inform = (ArrayList<String>) bundle.getSerializable("obj");
        LoadPane(view);
        return  view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParentActivity = (Activity) context;
        setAvatar = (SetMyInfoAvatar)((Activity)context);
    }

    @Override
    public void onDestroyView(){
        FragmentCollector.removeFragment(this);
        super.onDestroyView();
    }

    public void RestoreAvatar(){
        SharedPreferences pref = mParentActivity.getSharedPreferences("data",Context.MODE_PRIVATE);
        String avatar = pref.getString(mAvatarKey,"");
        if(!avatar.equals(""))
            mAvatar.setImageBitmap(PhotoUtil.getBitmapbyString(avatar));
    }

    private void RestoreAvatarFromDatabase(){
        MyAsyncTask<getAvatarResponse> myAsyncTask = new MyAsyncTask<getAvatarResponse>(mParentActivity,WebServiceConnector.METHOD_GETAVATAR,mProgressBar);
        myAsyncTask.setListener(new getAvatarResponse() {
            @Override
            public void getAvatar(ArrayList<String> result) {
                if(result.size() > 0){
                    Bitmap bitmap = PhotoUtil.convertStringToIcon(result.get(0));
                    mAvatar.setImageBitmap(bitmap);
                    ((MainActivity)mParentActivity).mAvatar.setImageBitmap(bitmap);
                } else if(result.get(0).equals("")){
                    mAvatar.setImageResource(R.drawable.ic_account_circle_black_24dp);
                } else {
                    Toast.makeText(mParentActivity,"从数据库获取头像失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        myAsyncTask.execute(getParamType(),getInput());
    }

    private ArrayList<String> getParamType(){
        ArrayList<String> paramType = new ArrayList<>();
        paramType.clear();
        paramType.add(WebServiceConnector.PARAMTYPE_SNO);
        return paramType;
    }

    private ArrayList<String> getInput(){
        ArrayList<String> input = new ArrayList<>();
        input.clear();
        Bundle bundle = getArguments();
        ArrayList<String> list = (ArrayList<String>)bundle.getSerializable("obj");
        input.add(list.get(0));
        return input;
    }

    /**
     *  保存学生信息，减少不必要的访问数据库次数
     * @param context 上下文
     * @param list     封装成ArrayList<String>的查询数据库获得的结果
     */
    private static void saveStudentInfo(Context context,ArrayList<String> list){
        SharedPreferences.Editor editor = context.getSharedPreferences(INFO_FILENAME,Context.MODE_PRIVATE).edit();
        editor.putString(SNO,list.get(0));
        Log.d("","list contain:\t"+list.get(0));
        editor.putString(SNAME,list.get(1));
        Log.d("","list contain:\t"+list.get(1));
        editor.putString(SCOLLEGE,list.get(2));
        Log.d("","list contain:\t"+list.get(2));
        editor.putString(SDEPT,list.get(3));
        Log.d("","list contain:\t"+list.get(3));
        editor.apply();
    }

    /**
     *   获取保存了的学生信息
     * @param activity  上下文
     * @return            保存的学生信息
     */
    public static ArrayList<String> getStudentInfobyLocalFile(Activity activity,String Sno){
        SharedPreferences pref = activity.getSharedPreferences(INFO_FILENAME,Context.MODE_PRIVATE);
        String sno = pref.getString(SNO,"");
        String sname = pref.getString(SNAME,"");
        String scollege = pref.getString(SCOLLEGE,"");
        String sdept = pref.getString(SDEPT,"");
        if(sno.equals("") || sname.equals("") || scollege.equals("") || sdept.equals(""))
            return null;
        if(!sno.equals(Sno))
            return null;
        Log.d("","sno equals Sno?"+Boolean.toString(sno.equals(Sno)));
        Log.d("","sno equals Sno?\t"+sno+"\t"+Sno);
        ArrayList<String> list = new ArrayList<String>();
        list.clear();
        list.add(sno);
        list.add(sname);
        list.add(scollege);
        list.add(sdept);
        return list;
    }

    private SimpleAdapter getAdapter(){
        List<InfoItem> list = getListViewItemList();
        List<Map<String,String>> result = getListData(list);
        SimpleAdapter adapter=new SimpleAdapter(
                mParentActivity,
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
                getString(R.string.Student_College),
                getString(R.string.Student_Class)
        };
        List<InfoItem> list = new ArrayList<InfoItem>();
        list.clear();
        InfoItem[] info = new InfoItem[str.length];
        for(int i=0;i<str.length;i++){
            info[i] = new InfoItem();
            info[i].setRowName(str[i]);
            info[i].setRowElement(inform.get(i));
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
        mProgressBar = v.findViewById(R.id.myinfofragment_progressbar);
        mProgressBar.setVisibility(View.GONE);
        String[] mInfoItem = {
                getString(R.string.Student_Name).toString(),
                getString(R.string.Student_ID).toString(),
                getString(R.string.Student_Department).toString(),
                getString(R.string.Student_Class).toString()
        };
        List<String> mInfoList = new ArrayList<>(Arrays.asList(mInfoItem));
        String[] mScheduleItem = {
                getString(R.string.Repair).toString(),
                getString(R.string.Enrollment).toString(),
                getString(R.string.Back_late).toString()
        };
        List<String> mScheduleList = new ArrayList<>(Arrays.asList(mScheduleItem));
        mAvatar = v.findViewById(R.id.mAvatar);
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);
                builder.setTitle(getString(R.string.Modify_Your_Avatar));
                //    指定下拉列表的显示数据
                final String[] mSelectItem = {
                        getString(R.string.By_Camera),
                        getString(R.string.By_Album),
                        getString(R.string.Cancel)
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
        RestoreAvatarFromDatabase();
        mInfo = v.findViewById(R.id.layout_minfo_lisview_top);
        mSchedule = v.findViewById(R.id.layout_minfo_lisview_bottom);
        ArrayAdapter<String> mInfoAdapter = new ArrayAdapter<String>(mParentActivity,android.R.layout.simple_list_item_1,mInfoItem);
        mInfo.setAdapter(getAdapter());
        final ArrayAdapter<String> mScheduleAdapter = new ArrayAdapter<String>(mParentActivity,android.R.layout.simple_list_item_1,mScheduleList);
        mSchedule.setAdapter(mScheduleAdapter);
        mSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String Sno = ((ArrayList<String>)getArguments().getSerializable("obj")).get(0);
                switch (i){
                    case 0:
                        SubMainActivity.StartThisActivity(mParentActivity,getString(R.string.Repair),i,Sno);
                        break;
                    case 1:
                        SubMainActivity.StartThisActivity(mParentActivity,getString(R.string.Enrollment),i,Sno);
                        break;
                    case 2:
                        SubMainActivity.StartThisActivity(mParentActivity,getString(R.string.Back_late),i,Sno);
                        break;
                }
            }
        });
    }
}