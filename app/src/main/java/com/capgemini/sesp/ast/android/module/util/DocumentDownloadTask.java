package com.capgemini.sesp.ast.android.module.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.document_download.DocumentList;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderSummaryActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by svadera on 4/25/2018.
 */
public class DocumentDownloadTask extends AsyncTask<String, Integer, String> {
    private Context context;
    private PowerManager.WakeLock mWakeLock;
    ProgressDialog mProgressDialog;
    List<String> selectedURL;

    public DocumentDownloadTask(Context context, List selectedURL) {
        this.context = context;
        this.selectedURL = selectedURL;

    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        DocumentDirectoryOperation directoryOperation = new DocumentDirectoryOperation(context);
        Log.i("DocumentDocwnloadTask", "in doInBackground");
        try {
            for (int i = 0; i < selectedURL.size(); i++) {
                String combinedURL = sUrl[0].concat(selectedURL.get(i).toString());
                int index = combinedURL.lastIndexOf("/");
                String subString = combinedURL.substring(index + 1, combinedURL.length());
                URL url = new URL(combinedURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(directoryOperation.getDocumentType(DocumentList.getDocType()) + "/" + subString);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            }

        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
            if (connection != null)
                connection.disconnect();
        }
        Log.d("Download", "Done");
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getResources().getString(R.string.downloading_documents));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (result != null) {
            Toast.makeText(context, context.getResources().getString(R.string.error_downloading) + result, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(context,   ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.ORDER_SUMMARY_ACTIVITY));
            context.startActivity(intent);
        }
    }
}

