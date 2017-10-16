package com.zspirytus.dmdemo.JavaSource;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 97890 on 2017/9/16.
 */

public class ActivityManager {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        if(!activities.contains(activity))
            activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        if(activities.contains(activity))
            activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
