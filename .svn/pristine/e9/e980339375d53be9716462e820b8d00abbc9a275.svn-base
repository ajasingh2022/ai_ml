package com.capgemini.sesp.ast.android.ui.activity.login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.PermissionUtil;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.update.UpdateApkTask;
import com.capgemini.sesp.ast.android.module.util.update.UpdateApkTask.APkDOwnloadListener;
import com.capgemini.sesp.ast.android.module.versionmanagement.SespStdVersionManager;
import com.capgemini.sesp.ast.android.ui.activity.common.LanguageSelectionActivity;

import java.io.File;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Display a splash screen for 2 seconds
 */
public class StartupActivity extends AppCompatActivity implements APkDOwnloadListener, NetworkStatusCallbackListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int DISPLAY_TIME = 2000;
    private static final int START_NEXT_ACTIVITY = 1;
    private static final String AUTO_UPDATE_YES = "yes";

    private static final int START_UPDATE_ACTIVITY = 2;
    private static final int SESP_DEVICE_PERMISSION_REQUEST = 100;
    private static final String TAG = StartupActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        /* Global error handling. Why Set here. */
        AndroidUtilsAstSep.loadConfigProperties();
        //Check for app update , else continue with normal flow

        // Set the language selected by User in the previous run , default is en.
        if (LanguageHelper.isLanguageSet()) {
            LanguageHelper.setLanguage(LanguageHelper.getLanguageCode());
        }
        //Request for device permission
        requestForPermission();
        } catch (Exception e) {
            writeLog(TAG + "  : onCreate() ", e);
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Class<?> nextActivity;

            if (!LanguageHelper.isLanguageSet()) {
                nextActivity = LanguageSelectionActivity.class;
            } else {
                nextActivity = LoginActivity.class;
            }
            Intent intent = new Intent();
            intent.setClass(StartupActivity.this, nextActivity);
            intent.putExtra(ActivityConstants.CALLING_ACTIVITY,
                    ActivityConstants.ACTIVITY_SPLASH);
            startActivity(intent);
            finish();
        }
    };

    public void onBackPressed() {
        if (mHandler != null) {
            mHandler.removeMessages(START_NEXT_ACTIVITY);
            mHandler.removeMessages(START_UPDATE_ACTIVITY);
        }
        super.onBackPressed();
    }

    private void requestForPermission() {
        if (!PermissionUtil.checkPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN)) {
            PermissionUtil.requestPermissions(this, SESP_DEVICE_PERMISSION_REQUEST,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN);
        } else {
            AndroidUtilsAstSep.checkServerConnection(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SESP_DEVICE_PERMISSION_REQUEST:
                MET_SCAN_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Do nothing
                    Log.d(TAG, "All permissions granted");
                    AndroidUtilsAstSep.checkServerConnection(this);
                } else {
                    //Exiting the application without proceeding further
                    Log.d(TAG, "One or more permission has been denied");
                    finish();
                    System.exit(0);
                }
                return;
        }
    }


    /**
     * Checks if update app available else continue the normal login
     */
    private void checkForUpdateElseContinue() {
        try{
        //if (AndroidUtilsAstSep.isNetworkAvailable()) {
            new AsyncTask<Void, Boolean, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    if (AUTO_UPDATE_YES.equalsIgnoreCase(SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.AUTO_UPDATE))) {
                        if (SespStdVersionManager.isUpdateAvailable()) {
                            Log.d("StartupActivity", "Update available");
                            //Set the load static type data and constant flag to true to reload the type data from fresh
                            SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(ConstantsAstSep.SharedPreferenceType.PERSISTENT);
                            SharedPreferences.Editor pe = p.edit();
                            pe.putBoolean("LOAD_STATIC_TYPE_DATA_AND_CONSTANTS", true);
                            pe.apply();
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean shouldUpdate) {
                    if (shouldUpdate) {
                        showUpdateApkDialog();
                    } else {
                        deleteOldApk();
                        mHandler.sendEmptyMessageDelayed(START_NEXT_ACTIVITY, DISPLAY_TIME);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        /*} else {
            mHandler.sendEmptyMessageDelayed(START_NEXT_ACTIVITY, DISPLAY_TIME);
        }*/
        } catch (Exception e) {
            writeLog(TAG + "  : checkForUpdateElseContinue() ", e);
        }
    }

    /**
     * Deletes the old apk file in a background thread
     */
    private void deleteOldApk() {
        try{
        new Thread(new Runnable() {
            @Override
            public void run() {
                String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "\\";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, "sesp_update.apk");
                if (outputFile.exists()) {
                    outputFile.delete();
                }
            }
        }).start();
        } catch (Exception e) {
            writeLog(TAG + "  : deleteOldApk() ", e);
        }

    }

    /**
     * Display update apk dialog to enter username and password
     */
    private void showUpdateApkDialog() {
        try{
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.update_app));
        builder.setMessage(getString(R.string.update_available));
        builder.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View dialogBodyView = inflater.inflate(R.layout.update_app_credential_layout, null);
        builder.setView(dialogBodyView);
        final EditText passwordEt = dialogBodyView.findViewById(R.id.passwordEditText);
        final EditText userNameEt = dialogBodyView.findViewById(R.id.usernameEditText);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do nothing as we cannot control the dissmis of dialog here
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Overriding the handler immediately after show is probably a better approach than OnShowListener
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(passwordEt.getText()) && !TextUtils.isEmpty(userNameEt.getText())) {
                    UpdateApkTask apktask = new UpdateApkTask();
                    apktask.setContext(StartupActivity.this, StartupActivity.this);
                    apktask.execute(SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.DOWNLOAD_URL), userNameEt.getText().toString(), passwordEt.getText().toString());
                    alertDialog.dismiss();
                } else {
                    passwordEt.setError("");
                    userNameEt.setError("");
                    Toast.makeText(StartupActivity.this, getString(R.string.fields_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        } catch (Exception e) {
            writeLog(TAG + "  : showUpdateApkDialog() ", e);
        }
    }

    @Override
    public void onApkDownloaded() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {

            String downloadPath = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "\\sesp_update.apk";
            Log.d("StartupActivity", "Download folder location :: " + downloadPath);
            intent.setDataAndType(Uri.fromFile(new File(downloadPath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
            startActivity(intent);
            finish();
        }catch(Exception e)
        {
            writeLog(TAG + ": onApkDownloaded()", e);
        }
    }

    @Override
    public void onApkFailed(String message) {
        try{
        Toast.makeText(StartupActivity.this, message, Toast.LENGTH_SHORT).show();
        checkForUpdateElseContinue();
        } catch (Exception e) {
            writeLog(TAG + "  : onApkFailed() ", e);
        }
    }


    @Override
    public void networkStatusChanged(boolean isConnected) {
        try{
        if(isConnected) {
            checkForUpdateElseContinue();
        } else {
            mHandler.sendEmptyMessageDelayed(START_NEXT_ACTIVITY, DISPLAY_TIME);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : networkStatusChanged() ", e);
        }
    }
}
