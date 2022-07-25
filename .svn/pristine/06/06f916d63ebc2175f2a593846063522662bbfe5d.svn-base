package com.capgemini.sesp.ast.android.ui.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.exception.SespExceptionHandler;
import com.skvader.rsp.cft.common.to.cft.table.ErrorTO;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * IntentService to handle Synchronisation with SESP servers asynchronously
 * This Service will synch error,workorders, events
 * <p/>
 */
public class SESPCommonSynchService extends IntentService {

    private transient boolean connectionAvailable = false;

    public SESPCommonSynchService() {

        super("SESPCommonSynchService");
        try {
            CheckServerConnectionService checkServerConnectionService = new CheckServerConnectionService(new AsyncResponse<Boolean>() {
                @Override
                public void processFinish(Boolean output) {
                    connectionAvailable = output;
                }
            });
            checkServerConnectionService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            writeLog("SESPCommonSynchService : SESPCommonSynchService() : ", e);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if (intent != null) {
                final String action = intent.getAction();
                if (connectionAvailable)
                    if (ConstantsAstSep.CustomActionConstants.LOG_ERROR.equals(action)) {
                        sendErrorLogs();
                    }
            }
        } catch (Exception e) {
            writeLog("SESPCommonSynchService : onHandleIntent() : ", e);
        }
    }

    /**
     * Synchronise error with Server.
     */
    private void sendErrorLogs() {
        Log.d("SESPCommonSynchService", " Sending error files");
        List<String> stacktraceFileNames = new ArrayList<String>();
        try {
            for (String file : fileList()) {
                Log.d("SESPCommonSynchService", " File name :: " + file);
                if (file.endsWith(SespExceptionHandler.STACKTRACE_FILE_ENDING)) {
                    stacktraceFileNames.add(file);
                }
            }
        } catch (Exception e) {
            writeLog("SESPCommonSynchService : sendErrorLogs() : ", e);
        }
        try {
            for (String stacktraceFileName : stacktraceFileNames) {
                String line;
                StringBuilder fileContent = new StringBuilder();
                InputStream is = openFileInput(stacktraceFileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append("\n");
                }
                is.close();
                ErrorTO errorTO = CommunicationHelper.JSONMAPPER.readValue(fileContent.toString(), ErrorTO.class);
                AndroidUtilsAstSep.getDelegate().logError(errorTO);
                deleteFile(stacktraceFileName);
            }
       /* } catch (FileNotFoundException e) {
            writeLog("SESPCommonSynchService : sendErrorLogs() : ",e);*/
        } catch (Exception e) {
            writeLog("SESPCommonSynchService : sendErrorLogs() : ", e);
        }
    }

}
