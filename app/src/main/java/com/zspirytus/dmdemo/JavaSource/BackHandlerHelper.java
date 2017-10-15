package com.zspirytus.dmdemo.JavaSource;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.zspirytus.dmdemo.Interface.FragmentBackHandler;

import java.util.List;

public class BackHandlerHelper {

    /**
     * 将back事件分发给 FragmentManager 中管理的子Fragment，如果该 FragmentManager 中的所有Fragment都
     * 没有处理back事件，则尝试 FragmentManager.popBackStack()
     *
     * @return 如果处理了back键则返回 <b>true</b>
     * @see #handleBackPress(Fragment)
     * @see #handleBackPress(FragmentActivity)
     */
    public static boolean handleBackPress(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments == null) return false;
        /*for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment child = fragments.get(i);
            Boolean isFragmentBackHandled = isFragmentBackHandled(child);
            Log.d("isFragmentBackHandled",Boolean.toString(isFragmentBackHandled == false));
            if (isFragmentBackHandled) {
                return true;
            }
        }*/

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }

    public static boolean handleBackPress(Fragment fragment) {
        return handleBackPress(fragment.getChildFragmentManager());
    }

    public static boolean handleBackPress(FragmentActivity fragmentActivity) {
        return handleBackPress(fragmentActivity.getSupportFragmentManager());
    }

    public static boolean handleBackPress(AppCompatActivity appCompatActivity){
        return handleBackPress(appCompatActivity.getSupportFragmentManager());
    }

    /**
     * 判断Fragment是否处理了Back键
     *
     * @return 如果处理了back键则返回 <b>true</b>
     */
    public static boolean isFragmentBackHandled(Fragment fragment) {
        Log.d("fragment_test",Boolean.toString(fragment != null));
        Log.d("fragment_visible",Boolean.toString(fragment.isVisible()));
        Log.d("fragment_instanceof",Boolean.toString(fragment instanceof FragmentBackHandler));
        Log.d("New Line","\n\n");
        return fragment != null
                && fragment.isVisible()
                && fragment.getUserVisibleHint() //for ViewPager
                && fragment instanceof FragmentBackHandler
                && ((FragmentBackHandler) fragment).onBackPressed();
    }
}