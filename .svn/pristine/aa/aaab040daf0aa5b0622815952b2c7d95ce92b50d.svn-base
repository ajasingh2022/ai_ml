package com.capgemini.sesp.ast.android.ui.activity.common;

/**
 * Created by pramalli on 10/5/2016.
 */

import android.app.Activity;
import android.util.ArrayMap;

import java.util.Map;

/**
 * Map will be populated with name-class map of activities.
 * Can be easily overridden by BST where customization of default activities is
 * Class to override default activities from BST layer
 */
public final class ActivityFactory {
    private transient static final ActivityFactory INSTANCE = new ActivityFactory();
    //Map to hold  ActivityName-ActivityClass entries

    private Map<String,Class<? extends Activity>> activityClasses = new ArrayMap<String, Class<? extends Activity>>();

    /**
     * Return pre initialized instance
     * @return - AcitivityFactory
     */
    public static ActivityFactory getInstance() {
        return INSTANCE;
    }

    public  void addActivity(String activityKey,Class<? extends Activity> activityClass){
        activityClasses.put(activityKey,activityClass);
    }
    public  Class<? extends Activity> getActivityClass(String activityKey){
        return activityClasses.get(activityKey);
    }

}
