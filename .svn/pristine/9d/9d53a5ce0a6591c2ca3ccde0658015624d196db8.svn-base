package com.capgemini.sesp.ast.android.module.util.update;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.SecurityUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by aditam on 2/23/2016.
 */
public class UpdateApkTask extends AsyncTask<String, Void, Boolean> {
    private Context context;
    private APkDOwnloadListener mApkListener;

    private ProgressDialog progressDialog = null;


    public interface APkDOwnloadListener {
        void onApkDownloaded();

        void onApkFailed(String message);
    }

    public void setContext(Context contextf, APkDOwnloadListener listener) {
        context = contextf;
        mApkListener = listener;
    }


    @Override
    protected final void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        // Load the custom title view
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customTitleLayout = inflater.inflate(R.layout.progress_bar_custom_layout, null);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCustomTitle(customTitleLayout);
        ((TextView) customTitleLayout.findViewById(R.id.progressVwId)).setText(R.string.loading);
        //progressDialog.setTitle(R.string.loading);
        progressDialog.setMessage(context.getString(R.string.downloading));
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... arg0) {
        try {
            final HttpURLConnection urlConnection = SecurityUtil.getURLConnection(arg0[0]);
            String username = arg0[1];
            String password = arg0[2];
            String userCredentials = username + ":" + password;
            byte[] encodedBytes = Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP);
            String authorization = "Basic " + new String(encodedBytes);
            urlConnection.setRequestProperty("Authorization", authorization);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            //For generic downlaod path using Environment constants for download directory
            File outputFile = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "\\sesp_update.apk");

            if (outputFile.exists()) {
                outputFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = urlConnection.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();


        } catch (Exception e) {
            writeLog("UpdateApkTask  :doInBackground() :Update error! " , e);
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        progressDialog.dismiss();
        if (mApkListener != null) {
            if (isSuccess) {
                mApkListener.onApkDownloaded();
            } else {
                mApkListener.onApkFailed(context.getString(R.string.failed_download));
            }
        }
    }
}

