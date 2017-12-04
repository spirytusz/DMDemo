package com.zspirytus.dmdemo.JavaSource.Manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZSpirytus on 2017/10/16.
 */

public class FragmentCollector {

    public static List<Fragment> fragments = new ArrayList<>();

    public static void addFragment(Fragment fragment){
        if(!fragments.contains(fragment)) {
            fragments.add(fragment);
        }
    }

    public static void removeFragment(Fragment fragment){
        if(fragments.contains(fragment)){
            fragments.remove(fragment);
        }
    }

    public static void HideAllFragment(FragmentTransaction ft){
        for(Fragment fragment : fragments){
            ft.hide(fragment);
        }
    }

    public static final int getFragmentNum(){
        return fragments.size();
    }
}
