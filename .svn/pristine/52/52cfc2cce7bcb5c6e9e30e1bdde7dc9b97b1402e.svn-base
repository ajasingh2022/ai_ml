package com.capgemini.sesp.ast.android.module.tsp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.capgemini.sesp.ast.android.module.tsp.TspUtil.mMapsActivity;

/**
 * Task specific for downloading data from Uri
 */
public  class DownloadTask extends AsyncTask<String, Void, String> {

    DownloadCallBack downloadCallBack;
    private ProgressDialog progressDialog;


    // Downloading data in non-ui thread
    @Override
    protected String doInBackground(String... url) {

        // For storing data from web service
        String data = "";

        try{
            // Fetching the data from web service
            data = downloadUrl(url[0]);

        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return data;
    }

    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(mMapsActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Downloading Data");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        downloadCallBack.DownloadCallBackListener(result);
        try {
            progressDialog.dismiss();
        }catch (Exception e){
            //
        }
    }

    private static String downloadUrl(String strUrl) throws IOException {
        String data = null;
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("URL Exception", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}