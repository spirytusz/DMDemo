package com.zspirytus.dmdemo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zspirytus.dmdemo.JavaSource.Utils.DialogUtil;
import com.zspirytus.dmdemo.JavaSource.Utils.PhotoUtil;
import com.zspirytus.dmdemo.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZSpirytus on 2017/12/7.
 */

public class DetailsInfoFragment extends Fragment {

    private static final String TAG = "DetailsInfoFragment";
    private static final String infoKey = "info";

    private Activity mParentActivity;

    private ImageView imageView;
    private TextView textView;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_detailsinfofragment,container,false);
        setHasOptionsMenu(true);
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
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e(TAG,TAG+"OptionMenu");
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dlf_update:
                Toast.makeText(mParentActivity,"不填代表不修改",Toast.LENGTH_SHORT).show();
                UpdateDialogForRepair();
                break;
            case R.id.dlf_delete:
                Toast.makeText(mParentActivity,"delete", Toast.LENGTH_SHORT).show();
                //hideFragment(view);
                mParentActivity.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UpdateDialogForRepair(){
        final String[] mCategory = {
                getString(R.string.Electrician).toString(),
                getString(R.string.Hydraulic).toString(),
                getString(R.string.Woodworking).toString(),
                getString(R.string.Construction).toString(),
                getString(R.string.Equipment).toString(),
                getString(R.string.Other).toString()
        };
        final View dialogView = LayoutInflater.from(mParentActivity)
                .inflate(R.layout.layout_updatedialogforrep,null);
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(mParentActivity);
        final TextView dialogTextView = dialogView.findViewById(R.id.sub_repairType);
        dialogTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);
                builder.setSingleChoiceItems(mCategory, 0, new DialogInterface.OnClickListener() {// 2默认的选中

                    @Override
                    public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                        dialogTextView.setText(mCategory[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();*/
                DialogUtil.showListDialog(mParentActivity,mCategory,dialogTextView);
            }
        });
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText repairPlace = dialogView.findViewById(R.id.sub_edittext_place);
                        EditText detail = dialogView.findViewById(R.id.sub_edittext_detail);
                        EditText contact = dialogView.findViewById(R.id.sub_edittext_contact);
                        String placeText = repairPlace.getText().toString();
                        String detailText = detail.getText().toString();
                        String contactText = contact.getText().toString();
                        String typeText = dialogTextView.getText().toString();
                        ArrayList<String> requestParams = new ArrayList<String>();
                        requestParams.add(placeText);
                        requestParams.add(typeText);
                        requestParams.add(detailText);
                        requestParams.add(contactText);
                    }
                });
        customizeDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        customizeDialog.setCancelable(false);
        customizeDialog.show();
    }

    private void UpdateForELS(){

    }

    private void LoadPane(View view){
        Toolbar toolbar = view.findViewById(R.id.dlf_toolbar);
        ((AppCompatActivity) mParentActivity).setSupportActionBar(toolbar);
        toolbar.setTitle("详细信息");
        imageView = view.findViewById(R.id.detailsinfofragment_imageview);
        textView = view.findViewById(R.id.detailsinfofragment_textview);
        listView = view.findViewById(R.id.detailsiinfofragment_listview);
        ArrayList<String> result = (ArrayList<String>)getArguments().getSerializable(infoKey);
        imageView.setImageBitmap(PhotoUtil.convertStringToIcon(result.get(0)));
        textView.setText(result.get(1));
        String[] listViewItem = new String[result.size()-2];
        for(int i =2;i<result.size();i++)
            listViewItem[i-2] = result.get(i);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                mParentActivity,
                android.R.layout.simple_list_item_1,
                listViewItem
        );
        listView.setAdapter(adapter);
    }

    public static DetailsInfoFragment GetThisFragment(ArrayList<String> info){
        DetailsInfoFragment dif = new DetailsInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(infoKey,(Serializable)info);
        dif.setArguments(bundle);
        return dif;
    }

}
