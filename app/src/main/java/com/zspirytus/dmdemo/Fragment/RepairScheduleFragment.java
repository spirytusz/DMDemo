package com.zspirytus.dmdemo.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zspirytus.dmdemo.JavaSource.RSFListViewItem;
import com.zspirytus.dmdemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 97890 on 2017/9/20.
 */

public class RepairScheduleFragment extends Fragment{

    private View view;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.layout_repairschedulefragment,container,false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        listView = view.findViewById(R.id.repairschedule_listview);
        //SimpleAdapter adapter = getAdapter();
        //listView.setAdapter(adapter);
        return view;
    }

    private SimpleAdapter getAdapter(){
        List<RSFListViewItem> list = getListViewItemList();
        List<Map<String,Object>> result = getListDate(list);
        SimpleAdapter adapter=new SimpleAdapter(
                getActivity(),
                result,
                R.layout.layout_repairschedulefragment_lisviewlayout,
                new String[]{"bmp","title","time"},
                new int[]{R.id.rsf_ls_imageview,R.id.rfs_ls_textviewtitle,R.id.rsf_ls_textviewtime}
        );
        adapter.setViewBinder(new SimpleAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if( (view instanceof ImageView) & (data instanceof Bitmap) ) {
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap)data);
                    return true;
                }
                return false;
            }
        });
        return adapter;
    }

    /**
     *
     * @return 加入了RSFListViewItem的List<RSFListViewItem> list
     */
    private List<RSFListViewItem> getListViewItemList(){
        List<RSFListViewItem> list = new ArrayList<RSFListViewItem>();
        list.clear();
        File root = Environment.getExternalStorageDirectory();
        File folder = new File(root.getAbsolutePath()+"/tbs");
        String[] file = folder.list();
        RSFListViewItem[] rsf = new RSFListViewItem[file.length];
        for(int i=0;i<file.length;i++){
            rsf[i] = new RSFListViewItem();
            Uri uri = Uri.parse(file[i]);
            Bitmap bitmap = BitmapFactory.decodeFile(folder.getPath()+"/"+file[i]);
            rsf[i].setmBitmap(bitmap);
            rsf[i].setmTitle("title"+(i+1));
            rsf[i].setmTime(Long.toString(System.currentTimeMillis()));
            list.add(rsf[i]);
        }
        return list;
    }

    /**
     *
     * @param list    List<RSFListViewItem>
     * @return        List<Map<String, Object>> result
     */
    private List<Map<String,Object>> getListDate(List<RSFListViewItem> list){
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(RSFListViewItem rsf:list){
            Map<String, Object> map = new HashMap<String, Object>();
            Bitmap bmp=rsf.getmBitmap();
            map.put("bmp", bmp);
            String title=rsf.getmTitle();
            map.put("title", title);
            String time = rsf.getmTime();
            map.put("time", time);
            result.add(map);
        }
        return result;
    }

    public interface RSFCallBack{
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l);
    }

}
