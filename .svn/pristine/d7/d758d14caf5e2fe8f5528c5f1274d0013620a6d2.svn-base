package com.capgemini.sesp.ast.android.ui.activity.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.HelpDocumentThread;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.PermissionUtil;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteManager;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteType;
import com.capgemini.sesp.ast.android.module.versionmanagement.SespStdVersionManager;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.common.LanguageSelectionActivity;
import com.capgemini.sesp.ast.android.ui.activity.document_download.DocumentList;
import com.capgemini.sesp.ast.android.ui.activity.receiver.NetworkStatusMonitorReceiver;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.service.AndroidStatisticsSyncService;
import com.capgemini.sesp.ast.android.ui.service.DownloadTypeDataIntentService;
import com.capgemini.sesp.ast.android.ui.service.DownloadWOIntentService;
import com.capgemini.sesp.ast.android.ui.service.SESPCommonSynchService;
import com.skvader.rsp.cft.common.util.AndroidAuthenticationTO;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.UserLoginCustomTO;

import java.util.List;
import java.util.TimeZone;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * This activity class is last modified on 23rd July, 2014
 * for SESPSTD-3182, the modified class now implements
 * the OnClickListener
 *
 * @author Capgemini
 */

@SuppressLint("InflateParams")
public class LoginActivity extends AppCompatActivity implements NetworkStatusCallbackListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private transient EditText usernameET = null;
    private transient EditText passwordET = null;
    DocumentList lastLoginUserName = new DocumentList();
    //The "x" and "y" position of the "Show Button" on screen.
    Point p;


    private static final int SESP_DEVICE_PERMISSION_REQUEST = 100;
    private static final String TAG = LoginActivity.class.getSimpleName();

    private int noOfOrdersToBeSynced = 0;

    /*
     * Local broadcast receiver used to
     * receive network connectivity information asynchronously
     */
    private transient BroadcastReceiver networkStatusReceiver = null;

    /*
     * The button object is changed to ImageView
     * to allow the image to be auto sized as per the original
     * dimensions
     */
    private transient ImageButton localeFlag = null;
    private transient CheckBox forceOfflineCheckBox = null;
    private LoginThread loginThread;
    private transient Button forgotPasswordButton = null;
    private transient TextView userLockedTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("LoginActivity", "onCreate called");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        localeFlag = findViewById(R.id.languageButton);
        localeFlag.setVisibility(View.VISIBLE);
        final Drawable flagIcon = LanguageHelper.getFlagIcon(LanguageHelper.getLanguageCode());
        Bitmap bitmap = ((BitmapDrawable) flagIcon).getBitmap();
        //To specify dimension in dps
        final float scale = this.getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (24 * scale + 0.5f);//24dp is converted to equal pixels
        // Scale it to 24 x 24 ie Button Size
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, pixels, pixels, true));
        localeFlag.setBackground(d);

        ImageButton btn_show = findViewById(R.id.aboutButton);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                showAboutAlert();

            }
        });

        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        userLockedTextView=findViewById(R.id.userLockedTextView);
        forceOfflineCheckBox = findViewById(R.id.forceOfflineCheckBox);

        forgotPasswordButton = (Button)findViewById(R.id.forgotPasswordFlatButton);

        //Request for device permission
        requestForPermission();

    }

    private void requestForPermission() {
        if (!PermissionUtil.checkPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.BLUETOOTH_ADMIN)) {
            PermissionUtil.requestPermissions(this, SESP_DEVICE_PERMISSION_REQUEST,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.CAMERA,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.BLUETOOTH_ADMIN);
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
     * The method would be invoked by android
     * framework when the user clicks on any menu item
     * <p/>
     * Provide the screen shows the hardware menu button
     * For newer devices the technique is different.
     *
     * @param view android.view.MenuItem
     * @return {@link Boolean}
     */


    public void languageButtonClick(View view) {
        try {
            Intent intent = new Intent(LoginActivity.this, LanguageSelectionActivity.class);
            intent.putExtra(ActivityConstants.CALLING_ACTIVITY,
                    ActivityConstants.ACTIVITY_LOGIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            writeLog(TAG + " :languageButtonClick()", e);
        }
    }


    public void settingsButtonClick(final View view) {
        showSettings();
    }


    /**
     * Method added to avoid
     * code duplication for showing status activity
     */
    public void showSettings() {
        try {
            AndroidUtilsAstSep.launchExplicitActivity(this, LoginSettingsActivity.class, 0);
        } catch (Exception e) {
            writeLog(TAG + " :showSettings()", e);
        }
    }


    public void forceOfflineCheckBoxClicked(View view) {
        try {

            if (forceOfflineCheckBox.isChecked()) {
                final String offlineUsername = DatabaseHandler.createDatabaseHandler().getLastLoginUserName();

                if (TextUtils.isEmpty(offlineUsername)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_offline_login_not_possible), Toast.LENGTH_LONG).show();
                } else {
                    usernameET.setText(offlineUsername);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :forceOfflineCheckBoxClicked()", e);
        }
    }
    public void forgotPasswordButtonClicked(View view) {
        SharedPreferences userLockedPreference = AndroidUtilsAstSep.getSharedPreferences(ConstantsAstSep.SharedPreferenceType.PERSISTENT);
        if (userLockedPreference.getBoolean(ConstantsAstSep.SharedPreferenceKeys.USER_LOCKED, false)) {
            forgotPasswordButton.setEnabled(false);
            forgotPasswordButton.setTextColor(getResources().getColor(R.color.disabled_red));
            userLockedTextView.setVisibility(View.VISIBLE);
        } else {

            Intent intent = new Intent(LoginActivity.this,
                    ForgotPasswordActivity.class);
            userLockedTextView.setVisibility(View.GONE);
            intent.putExtra(ActivityConstants.CALLING_ACTIVITY,
                    ActivityConstants.ACTIVITY_LOGIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LoginActivity", "onResume called");
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        this.networkStatusReceiver = new NetworkStatusMonitorReceiver(this);
        // Register broadcast receiver here
        registerReceiver(this.networkStatusReceiver, filter);

        LanguageHelper.reloadIfLanguageChanged(this);

        usernameET.setText(DatabaseHandler.createDatabaseHandler().getLastLoginUserName());
        passwordET.setText(null); // Always reset password!

        if (loginThread != null && loginThread.isCancelled()) {

            if (loginThread.isSuccessful()) {
                loginThread.onPostExecute(true, "");
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // De-register the broadcast receiver here
        if (this.networkStatusReceiver != null) {
            unregisterReceiver(this.networkStatusReceiver);
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (loginThread != null) {
            loginThread.cancel(true);
        }
    }


    public void loginButtonClicked(final View view) {
        /* Validation before sending request to web service */
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        try {
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, R.string.error_missing_username, Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, R.string.error_missing_password, Toast.LENGTH_SHORT).show();
                return;
            }
            hideSoftKeyBoard();

            loginThread = new LoginThread(this, username.toLowerCase(), password);
            loginThread.start();
            new HelpDocumentThread().execute();
        } catch (Exception e) {
            writeLog(TAG + " :loginButtonClicked()", e);
        }
    }

    private void hideSoftKeyBoard() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final View passwordView = findViewById(R.id.passwordEditText);
        final View usernameView = findViewById(R.id.usernameEditText);
        try {
            inputMethodManager.hideSoftInputFromWindow(passwordView.getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(usernameView.getWindowToken(), 0);
        } catch (Exception e) {
            writeLog(TAG + "  :hideSoftKeyBoard()", e);
        }
    }

    private class LoginThread extends GuiWorker<String> {
        String username = null;
        String password = null;

        public LoginThread(Activity ownerActivity, String userName, String passWord) {
            super(ownerActivity, ActivityFactory.getInstance().getActivityClass(ActivityConstants.MAIN_MENU_ACTIVITY));
            username = userName;
            password = passWord;
        }

        @Override
        protected String runInBackground() throws Exception {
            boolean successfulLogin = false;

            //Return the runinbackground method if cancelled
            if (isCancelled()) {
                return null;
            }

            setMessage(R.string.logging_in_user);
            SESPPreferenceUtil.setForcedOfflineStatus(forceOfflineCheckBox.isChecked());
            String errorMessage = null;
            if (forceOfflineCheckBox.isChecked()) {

                successfulLogin = SessionState.getInstance().logonInOfflineMode(username, password);
                if (!successfulLogin) {
                    errorMessage = getString(R.string.error_wrong_username_or_password);
                } else {
                    SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(ConstantsAstSep.SharedPreferenceType.PERSISTENT);
                    SharedPreferences.Editor pe = p.edit();
                    pe.putBoolean(ConstantsAstSep.SharedPreferenceKeys.DOWNLOAD_FINISHED, true);
                    pe.apply();
                    ExecuteManager.getInstance().execute(ExecuteType.BST_TYPE_DATA_DOWNLOADED);
                }
            } else {
                /* Login user */
                String previousUserName = DatabaseHandler.createDatabaseHandler().getLastLoginUserName();
                lastLoginUserName.setLastLoginUser(previousUserName);
                SessionState.getInstance().registerCurrentUserCredentials(username, password);
                //Return the runinbackground method if cancelled
                if (isCancelled()) {
                    return null;
                }

                AndroidAuthenticationTO androidAuthenticationTO = new AndroidAuthenticationTO();
                //Set Android device IP address.

                androidAuthenticationTO.setIpAddress(AndroidUtilsAstSep.getLocalIpAddress());
                androidAuthenticationTO.setOperatingSystem("Android:" + Build.VERSION.RELEASE);
                // Set Device Id
                androidAuthenticationTO.setDeviceSerialNumber(Build.SERIAL != Build.UNKNOWN ? Build.SERIAL : Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));


               /* *//* HELP Document:-   *//*
                List<HelpDocBean> helpDocBean = null;
                SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.ISO8601_FORMAT_HELP_DOCUMENT.getPattern());

                if (previousUserName == null) {
                    helpDocBean = AndroidUtilsAstSep.getDelegate().getAllHelpDocuments( null);
                } else {
                    helpDocBean = AndroidUtilsAstSep.getDelegate().getAllHelpDocuments( sdf.format(new Date()).toString());
                    Log.i("HELP", new Date().toString());
                }
                if (!helpDocBean.isEmpty()) {
                    DatabaseHandler.createDatabaseHandler().insertOrUpdateHelpDocuemnt(helpDocBean);
                }*/

                //Return the runinbackground method if cancelled
                if (isCancelled()) {
                    return null;
                }
                String infoMessage = null;

                final UserLoginCustomTO userLoginCustomTO = AndroidUtilsAstSep.getDelegate().login(androidAuthenticationTO);


                //Return the runinbackground method if cancelled
                if (isCancelled()) {
                    return null;
                }

                SessionState.getInstance().registerCurrentUser(userLoginCustomTO, password, true);
                /* Check if password is about to expire */
                Long timeLeft = userLoginCustomTO.getPasswordExpirationTime();

                if (timeLeft != null) {
                    infoMessage = getString(R.string.password_will_expire_in_x, getTimeLeft(timeLeft)) + "\n" + getString(R.string.please_change_password);
                }
                //Return the runinbackground method if cancelled
                if (isCancelled()) {
                    return null;
                }
                /* Download type data */
                // setMessage(R.string.downloading_types);
                //   CacheController.downloadCachedData();
                /* Set user's time zone */
                //Modified to set User Time zone in UserLoginCustomTO itself instead of setting it here
                SessionState.getInstance().setUsersTimeZone(TimeZone.getTimeZone(userLoginCustomTO.getSimpleTimezoneCode()));

                Log.d(TAG, "Android Statistics sync Service");
                startService(new Intent(this.ctx, AndroidStatisticsSyncService.class));

                /* Synch offline work orders on available network connection*/

                Intent intent1 = new Intent(getApplicationContext(), DownloadTypeDataIntentService.class);
                startService(intent1);

                Intent intent2 = new Intent(getApplicationContext(), DownloadWOIntentService.class);
                startService(intent2);                

                Intent newlogError = new Intent(ctx, SendErrorLogsToDB.class);
                startService(newlogError);

                /* Login successful, send stored exceptions (if any) in the background */
                Intent logError = new Intent(ctx, SESPCommonSynchService.class);
                logError.setAction(ConstantsAstSep.CustomActionConstants.LOG_ERROR);
                startService(logError);

                return infoMessage;
            }
            if (!successfulLogin) {
                throw new Exception(errorMessage);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final boolean successful, final String infoToUser) {
            if (infoToUser != null) {
                Builder infoDialog = GuiController.showInfoDialog(LoginActivity.this, infoToUser);
                infoDialog.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoginThread.super.onPostExecute(successful, infoToUser);
                                dialog.dismiss(); // Method deprecated, and should not use -1. But it
                                // works :)
                            }
                        });
                infoDialog.show();
            } else {
                LoginThread.super.onPostExecute(successful, infoToUser);
            }
        }
    }

    private String getTimeLeft(Long timeLeftInMilliSec) {
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        try {
            days = timeLeftInMilliSec / (24 * 60 * 60 * 1000);
            hours = timeLeftInMilliSec / (60 * 60 * 1000) - days * 24;
            minutes = timeLeftInMilliSec / (60 * 1000) - days * 24 - hours * 60;
            seconds = (timeLeftInMilliSec - minutes * 60 * 1000L) / 1000;

        } catch (Exception e) {
            writeLog(TAG + "  : getTimeLeft() ", e);
        }
        if (days > 0) {
            return getString(R.string.x_days_and_y_hours, days, hours);
        } else if (hours > 0) {
            return getString(R.string.x_hours_and_y_minutes, hours, minutes);
        } else {
            return getString(R.string.x_minutes_and_y_seconds, minutes, seconds);
        }

    }


    /**
     * This callback method is executed by the local broadcast receiver
     * that receives information when the network status is changed
     *
     * @param isConnected {@link Boolean}
     */
    @Override
    public void networkStatusChanged(final boolean isConnected) {
        try {
            final TextView infoTextView = findViewById(R.id.infoTextView);
            infoTextView.setText(isConnected ? R.string.network_available : R.string.network_unavailable);
            infoTextView.setTextColor(getResources().getColor(isConnected ? R.color.colorPrimary : R.color.colorHighlight));
            forceOfflineCheckBox.setEnabled(isConnected);
            forceOfflineCheckBox.setChecked(!isConnected);
            forceOfflineCheckBoxClicked(null);
        } catch (Exception e) {
            writeLog(TAG + "  : networkStatusChanged() ", e);
        }
    }

    /**
     * This callback method is executed after the successful online login
     * that synchronizes all the work orders performed online
     */
    private void syncOrders() {

        Log.d("LogInActivity", "syncOrders() START");
        //Synchronizing work orders with server
        try {
            List<Long> caseIdsToBeSynched = DatabaseHandler.createDatabaseHandler().getCaseIdsForSync();
            noOfOrdersToBeSynced = caseIdsToBeSynched.size();
            for (Long caseId : caseIdsToBeSynched) {
                Log.d("LogoutActivity", "Syncing work order, case Id : " + caseId);
                AndroidUtilsAstSep.suggestSaveWorkorder(caseId, null);
            }

            Log.d("LoginActivity", "syncOrders() END");
        } catch (Exception e) {
            writeLog(TAG + "  : syncOrders() ", e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LoginActivity", "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LoginActivity", "onDestroy called");
    }


    //**************************************** Alert Dialogue ************************************************************

    public void showAboutAlert() {
        try {
            AlertDialog alertDialog = new Builder(LoginActivity.this).create();
            alertDialog.setTitle("About");
            alertDialog.setIcon(R.drawable.ic_about_24dp);

            final String businessVersion = SespStdVersionManager.getCurrentBusinessVersion();
            final String frameworkVersion = SespStdVersionManager.getCurrentFrameworkVersion();

            alertDialog.setMessage("Business Version  :  " + businessVersion + "\n\n" + "Framework Version  :  " + frameworkVersion);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();
        } catch (Exception e) {
            writeLog(TAG + "  : showAboutAlert() ", e);
        }
    }


}