package com.capgemini.sesp.ast.android.ui.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.capgemini.sesp.ast.android.module.cache.CacheController;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 7/13/2018.
 */
public class DownloadWOIntentService extends IntentService {

    private int noOfOrdersToBeSynced = 0;
    public static final String ACTION = "com.capgemini.sesp.ast.android.ui.service.DownloadWOIntentService";
    private static final String TAG = DownloadWOIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownloadWOIntentService() {
        super(DownloadWOIntentService.class.getName());
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.i("DownloadWOService", "Download work order Started");
            syncOrders();
            CacheController.downloadWorkorders();
            Intent in = new Intent(ACTION);
            in.putExtra("resultCode", Activity.RESULT_OK);
            LocalBroadcastManager.getInstance(this).sendBroadcast(in);
        } catch (Exception e) {
            writeLog(TAG + ":onHandleIntent", e);
        }
    }

    private void syncOrders() {

        Log.d("DownloadWOIntentService", "syncOrders() START");
        //Synchronizing work orders with server
        try {
            List<Long> caseIdsToBeSynched = DatabaseHandler.createDatabaseHandler().getCaseIdsForSync();
            noOfOrdersToBeSynced = caseIdsToBeSynched.size();
            for (Long caseId : caseIdsToBeSynched) {
                Log.d("DownloadWOIntentService", "Syncing work order, case Id : " + caseId);
                AndroidUtilsAstSep.suggestSaveWorkorder(caseId, null);
            }

            Log.d("DownloadWOIntentService", "syncOrders() END");
        } catch (Exception e) {
            writeLog(TAG + "  : syncOrders() ", e);
        }
    }

}
