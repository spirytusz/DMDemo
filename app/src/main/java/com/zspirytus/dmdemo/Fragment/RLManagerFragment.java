package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.zspirytus.dmdemo.Activity.DetailsInfoActivity;
import com.zspirytus.dmdemo.JavaSource.ListViewModule.RSFListViewItem;
import com.zspirytus.dmdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZSpirytus on 2017/12/10.
 */

public class RLManagerFragment extends Fragment {

    private static final String TAG = "RLManagerFragment";
    private static final String mResultKey = "result";

    private ListView listView;
    private Activity mParentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_rlmanagerfragment,container,false);
        LoadPane(view);
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParentActivity = (Activity)context;
    }

    private void LoadPane(View view){
        listView = view.findViewById(R.id.rlmanager_listview);
        ArrayList<String> result = getArguments().getStringArrayList(mResultKey);
        if(result.size() > 0){
            listView.setAdapter(getAdapter(result));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    HashMap<String,Object> map=(HashMap<String,Object>)adapterView.getItemAtPosition(i);
                    String rno = (String)map.get("title");
                    //Toast.makeText(mParentActivity,rno,Toast.LENGTH_SHORT).show();
                    DetailsInfoActivity.StartThisActivity(mParentActivity,rno,2);
                }
            });
        } else {
            listView.setVisibility(View.GONE);
        }
    }

    private SimpleAdapter getAdapter(ArrayList<String> response){
        List<RSFListViewItem> list = getListViewItemList(response);
        List<Map<String,Object>> result = getListDate(list);
        SimpleAdapter adapter=new SimpleAdapter(
                mParentActivity,
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
                } else if((view instanceof ProgressBar)){
                    ProgressBar progressBar = (ProgressBar) view;
                    progressBar.setVisibility(View.GONE);
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
    private List<RSFListViewItem> getListViewItemList(ArrayList<String> result){
        if(result.size()>0) {
            List<RSFListViewItem> list = new ArrayList<>();
            list.clear();
            int length = result.size();
            RSFListViewItem rsf;
            for(int i = 0;i<length;i+=2){
                rsf = new RSFListViewItem();
                rsf.setmBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_av_timer_black_custom_48dp));
                rsf.setmTitle(result.get(i));
                rsf.setmTime(result.get(i+1));
                list.add(rsf);
            }
            return list;
        }
        return null;
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

    public static RLManagerFragment GetThisFragment(ArrayList<String> result){
        RLManagerFragment rmf = new RLManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(mResultKey,result);
        rmf.setArguments(bundle);
        return rmf;
    }
}
