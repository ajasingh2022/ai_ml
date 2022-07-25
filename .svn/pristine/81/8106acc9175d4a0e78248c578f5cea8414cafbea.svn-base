package com.capgemini.sesp.ast.android.ui.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.capgemini.sesp.ast.android.module.cache.CacheController;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 7/13/2018.
 */
public class DownloadTypeDataIntentService extends IntentService {
    public static final String ACTION = "com.capgemini.sesp.ast.android.ui.service.DownloadTypeDataIntentService";
    private static final String TAG = DownloadTypeDataIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownloadTypeDataIntentService() {
        super(DownloadTypeDataIntentService.class.getName());
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent){
        try {
            Log.i("DownloadTypeDataIntentService", "Download Type Data Started");
            CacheController.downloadCachedData();
            Intent in = new Intent(ACTION);
            in.putExtra("resultCode", Activity.RESULT_OK);
            LocalBroadcastManager.getInstance(this).sendBroadcast(in);
        } catch (Exception e) {
            writeLog(TAG + ":onHandleIntent", e);
        }
    }

}
