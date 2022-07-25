package com.capgemini.sesp.ast.android.ui.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.exception.SespExceptionHandler;
import com.capgemini.sesp.ast.android.ui.service.SESPCommonSynchService;
import com.skvader.rsp.cft.common.to.cft.table.ErrorTO;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Broadcast receiver to receive synchronization actions.
 * After receiving an synchronization action, the broadcast receiver calls
 * an IntentService to process
 */

public class SESPSynchronizeActionReceiver extends BroadcastReceiver {

    private static final String TAG = SESPSynchronizeActionReceiver.class.getSimpleName();

    public SESPSynchronizeActionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (ConstantsAstSep.CustomActionConstants.LOG_ERROR
                    .equalsIgnoreCase(intent.getAction())) {
                Intent logError = new Intent(context, SESPCommonSynchService.class);
                sendErrorLogs(context);

            }
        } catch (Exception e) {
            writeLog(TAG + " : onReceive()", e);
        }
    }

    private void sendErrorLogs(Context ctx) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                List<String> stacktraceFileNames = new ArrayList<String>();
                for (String file : ctx.fileList()) {
                    if (file.endsWith(SespExceptionHandler.STACKTRACE_FILE_ENDING)) {
                        stacktraceFileNames.add(file);
                    }
                }
                try {
                    for (String stacktraceFileName : stacktraceFileNames) {
                        String line;
                        StringBuilder fileContent = new StringBuilder();
                        InputStream is = ctx.openFileInput(stacktraceFileName);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        while ((line = reader.readLine()) != null) {
                            fileContent.append(line).append("\n");
                        }
                        is.close();
                        ErrorTO errorTO = CommunicationHelper.JSONMAPPER.readValue(fileContent.toString(), ErrorTO.class);
                        //Double check on network status
                        if (networkInfo.isConnected()) {
                            AndroidUtilsAstSep.getDelegate().logError(errorTO);
                            ctx.deleteFile(stacktraceFileName);
                        }
                    }
           /* } catch (FileNotFoundException e) {
                writeLog(TAG + " : sendErrorLogs()", e); */
                } catch (Exception e) {
                    writeLog(TAG + " : sendErrorLogs()", e);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : sendErrorLogs()", e);
        }
    }
}
