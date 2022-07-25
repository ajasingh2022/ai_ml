package com.capgemini.sesp.ast.android.ui.service;

import android.os.AsyncTask;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SecurityUtil;

import java.net.HttpURLConnection;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by samdasgu on 6/14/2016.
 */
public class CheckServerConnectionService extends AsyncTask<Void, Void, Boolean> {

    private AsyncResponse<Boolean> delegate = null;
    private final String TAG = "CheckServerConnectionService";
    private static final String DEFAULT_CONNECTION_TIMEOUT = "10";

    public CheckServerConnectionService(AsyncResponse<Boolean> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Boolean isConnectionAvailable) {
        delegate.processFinish(isConnectionAvailable);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean isConnectionAvailable = false;
        try {
            if (AndroidUtilsAstSep.isNetworkAvailable()) {
                int responseCode = 0;
                final int maxConnectionTimeout = Integer.parseInt(ApplicationAstSep.getPropertyValue(ConstantsAstSep.PropertyConstants.KEY_CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT)) * 1000;
                try {
                    HttpURLConnection connection = SecurityUtil.getURLConnection(CommunicationHelper.getServerAddress(),
                            CommunicationHelper.getSslCertName(),
                            CommunicationHelper.getSslPort());
                    connection.setConnectTimeout(maxConnectionTimeout);
                    connection.setReadTimeout(maxConnectionTimeout);
                    responseCode = connection.getResponseCode();
                    connection.disconnect();
                } catch (Exception e) {
                    writeLog(TAG + " :doInBackground()  :Error on connecting to network ", e);
                }
                isConnectionAvailable = responseCode == HttpURLConnection.HTTP_OK;
                Log.i(TAG, "Server connection status = " + isConnectionAvailable);
            }
        } catch (Exception e) {
            writeLog(TAG + " : doInBackground()", e);
        }
        return isConnectionAvailable;
    }


}
