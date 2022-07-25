package com.capgemini.sesp.ast.android.module.util;

import android.app.Activity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.CacheController;

/**
 * Created by samdasgu on 10/12/2017.
 */
public class ReloadTypeDataThread extends GuiWorker<Void> {


    public ReloadTypeDataThread(Activity ownerActivity, Class<? extends Activity> nextActivityClass, String message, Integer launchIntentFlag) {
        super(ownerActivity, nextActivityClass, message, launchIntentFlag);
    }

    @Override
    protected Void runInBackground() throws Exception {
        /* Reload Type Data */
        setMessage(R.string.refreshing_type_data);
        CacheController.downloadCachedData();
        return null;
    }

}
