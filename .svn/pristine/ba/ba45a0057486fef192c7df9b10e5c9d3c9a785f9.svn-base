package com.capgemini.sesp.ast.android.ui.activity.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.communication.SmsListener;
import com.capgemini.sesp.ast.android.module.communication.SmsReceiver;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.PermissionUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.ui.activity.receiver.NetworkStatusMonitorReceiver;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.skvader.rsp.cft.common.authentication.EncryptionUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by svadera on 11/21/2018.
 */
@SuppressLint("InflateParams")
public class ForgotPasswordActivity extends AppCompatActivity implements NetworkStatusCallbackListener, ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private transient EditText editText1 = null;
    private transient EditText editText2 = null;
    private transient EditText editText3 = null;
    private EditText countryZipcodeEditText = null;
    private TextView infoTextView = null;
    private TextView errorResultTextView = null;
    private Button raisedButton = null;
    private Button flatButton = null;
    private ImageView networkStateIcon;
    private LinearLayout otpLinearLayout = null;
    private LinearLayout commonLinearLayout = null;
    private Button otpRaisedButton = null;
    private TextInputLayout editTextInpuLayout1 = null;
    private TextInputLayout editTextInpuLayout2 = null;
    private Spinner countryCodeSpinner = null;
    private static final int SESP_DEVICE_PERMISSION_REQUEST = 100;
    private String mobileNumberOrEmailIdValue;
    public static boolean permissionGranted = false;
    public String username;
    public long maxResendAttempts = 0;
    public long otpExpiryTime = 0;
    private String otpString = null;
    private int resendCount = 0;
    private final long minimumValueForTimer = 1000;
    String countryZipCode = "";
    String countryCodeSpinnerValues[];
    /*
     * Local broadcast receiver used to
     * receive network connectivity information asynchronously
     */
    private transient BroadcastReceiver networkStatusReceiver = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);

        /*
         * Setting up custom action bar view
         */
        final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
        networkStateIcon = (ImageView) vw.findViewById(R.id.network_state_iv);

        ImageButton help_btn = vw.findViewById(R.id.menu_help);
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new HelpDialog(ForgotPasswordActivity.this, ConstantsAstSep.HelpDocumentConstant.PASSWORD_RESET);
                dialog.show();
            }
        });
        // -- Customizing the action bar ends -----
        actionBar.setCustomView(vw);
        actionBar.setDisplayShowCustomEnabled(true);

        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(getString(R.string.app_name));

        otpLinearLayout = (LinearLayout) findViewById(R.id.otpLayout);
        commonLinearLayout = (LinearLayout) findViewById(R.id.commonLinearLayout);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        infoTextView = (TextView) findViewById(R.id.infoTextView);
        errorResultTextView = (TextView) findViewById(R.id.errorResultTextView);
        countryZipcodeEditText=(EditText)findViewById(R.id.countryZipcodeEdittext);
        raisedButton = (Button) findViewById(R.id.raisedButton);
        flatButton = (Button) findViewById(R.id.flatButton);
        otpRaisedButton = (Button) findViewById(R.id.otpRaisedButton);
        raisedButton.setOnClickListener(this);
        flatButton.setOnClickListener(this);
        otpRaisedButton.setOnClickListener(this);
        editTextInpuLayout1 = findViewById(R.id.editTextInputLayout1);
        editTextInpuLayout2 = findViewById(R.id.editTextInputLayout2);

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                otpString = parseSMSMessage(messageText);
                Log.d("Text", messageText);
                if (otpString != null)
                    editText3.setText(otpString);
                else
                    editText3.setText(" ");
            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable mobileNumberOrEmailIdText) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNumberOrEmailIdTextValue = s.toString().trim();
                if (raisedButton.getText().toString().equals(getResources().getString(R.string.continue2))) {
                    if (Patterns.PHONE.matcher(mobileNumberOrEmailIdTextValue).matches()) {
                        if (mobileNumberOrEmailIdTextValue.matches("([+|0].*?)|((.)*(-)(.)*)"))
                            GuIUtil.toggleTheButton(raisedButton, false);
                        else
                            GuIUtil.toggleTheButton(raisedButton, true);
                    } else if (Patterns.EMAIL_ADDRESS.matcher(mobileNumberOrEmailIdTextValue.trim()).matches())
                        GuIUtil.toggleTheButton(raisedButton, true);
                    else
                        GuIUtil.toggleTheButton(raisedButton, false);
                }
            }
        });

        countryCodeSpinnerValues = getSpinnerValues();
        countryCodeSpinner = (Spinner) findViewById(R.id.country_code_spinner);
        countryCodeSpinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter countryCodeArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countryCodeSpinnerValues);
        countryCodeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        countryCodeSpinner.setAdapter(countryCodeArrayAdapter);


        try {
            maxResendAttempts = Long.parseLong(AndroidUtilsAstSep.getProperty("max_otp_request_attempts", getApplicationContext()));
            otpExpiryTime = Long.parseLong(AndroidUtilsAstSep.getProperty("otp_expiry_time", getApplicationContext()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if (position > 0) {
            countryZipcodeEditText.setText(getCountryZipCode(countryCodeSpinnerValues[position]));
        } else
            countryZipcodeEditText.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public String[] getSpinnerValues() {
        String arrayValues[] = this.getResources().getStringArray(R.array.country_zip_codes);
        String spinnerValues[] = new String[arrayValues.length];
        spinnerValues[0] = arrayValues[0];
        for (int i = 1; i < arrayValues.length; i++) {
            String[] g = arrayValues[i].split(",");
            spinnerValues[i] = g[1];
        }
        return spinnerValues;
    }

    @Override
    protected void onResume() {
        Log.d("LoginActivity", "onResume called");
        super.onResume();
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        this.networkStatusReceiver = new NetworkStatusMonitorReceiver(this);
        // Register broadcast receiver here
        registerReceiver(this.networkStatusReceiver, filter);
        LanguageHelper.reloadIfLanguageChanged(this);

    }

    // for parsing otp from received SMS
    public String parseSMSMessage(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    //Checks whether to receive and read the sms
    private void requestForPermission() {
        if (!PermissionUtil.checkPermissions(this,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS)) {
            PermissionUtil.requestPermissions(this, SESP_DEVICE_PERMISSION_REQUEST,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS
            );
        } else {
            permissionGranted = true;
            generateOtpButtonClicked(null);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SESP_DEVICE_PERMISSION_REQUEST) {
            // for each permission check if the user granted/denied them
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        permissionGrantDialog();
                        break;
                    } else {
                        requestForPermission();
                        break;
                    }
                }
                //permission granted at beginning itself
                else {
                    permissionGranted = true;
                    generateOtpButtonClicked(null);
                    break;
                }
            }
        }
    }

    // when Never ask again checkbox is clicked then dialog box will appear to ask permission from the user to move the app control to application settings in mobile
    public void permissionGrantDialog() {
        AlertDialog.Builder permissionDialogBuilder = GuiController.showConfirmCancelDialog(ForgotPasswordActivity.this,
                getResources().getString(R.string.permission_required_question),
                getResources().getString(R.string.permission_grant_explanation)
        );
        permissionDialogBuilder.setPositiveButton(getResources().getString(R.string.continue2), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, SESP_DEVICE_PERMISSION_REQUEST);
                    }
                }
        );
        permissionDialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }
        );
        permissionDialogBuilder.show();
    }

    public void generateOtpButtonClicked(final View view) {
        username = editText1.getText().toString();
        mobileNumberOrEmailIdValue = editText2.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(mobileNumberOrEmailIdValue)) {
            Toast.makeText(getApplicationContext(), R.string.one_or_more_mandatory_field_is_missing, Toast.LENGTH_LONG).show();
        } else {
            if (!permissionGranted)
                requestForPermission();
            else if (Patterns.PHONE.matcher(mobileNumberOrEmailIdValue.trim()).matches()) {
                if (TextUtils.isEmpty(countryZipcodeEditText.getText().toString()))
                    Toast.makeText(getApplicationContext(), R.string.one_or_more_mandatory_field_is_missing, Toast.LENGTH_LONG).show();
                else {
                    if (mobileNumberOrEmailIdValue.startsWith(countryZipCode.substring(1))) {
                        guiErrorDialog(getString(R.string.invalid_mobile_number));
                    } else {
                        mobileNumberOrEmailIdValue = mobileNumberOrEmailIdValue.replaceAll("[^0-9]", "");
                        mobileNumberOrEmailIdValue = countryZipCode + "%%" + mobileNumberOrEmailIdValue;
                        checkUserServiceCall();
                    }
                }
            } else if (Patterns.EMAIL_ADDRESS.matcher(mobileNumberOrEmailIdValue.trim()).matches()) {
                checkUserServiceCall();
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_validation_of_mobile_bumber_or_email_id, Toast.LENGTH_LONG).show();
            }
        }
        hideSoftKeyBoard();
    }

    public String getCountryZipCode(String countryId) {
        String[] rl = this.getResources().getStringArray(R.array.country_zip_codes);
        for (int i = 1; i <= rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(countryId.trim())) {
                countryZipCode = g[0];
                break;
            }
        }
        return countryZipCode;
    }

    public void checkUserServiceCall() {
        new GuiWorker<String>(this) {
            @Override
            protected String runInBackground() throws Exception {
                return AndroidUtilsAstSep.getDelegateForForgotPassword().checkUser(username, mobileNumberOrEmailIdValue);
            }

            @Override
            protected void onPostExecute(boolean successful, String response) {
                if (successful) {
                    switch (response) {
                        case ConstantsAstSep.ForgotPasswordCode.USERNAME_INVALID:
                            guiErrorDialog(getString(R.string.invalid_username));
                            break;
                        case ConstantsAstSep.ForgotPasswordCode.MOBILE_NUMBER_OR_EMAIL_INVALID:
                            if (Patterns.PHONE.matcher(editText2.getText().toString().trim()).matches())
                                guiErrorDialog(getString(R.string.invalid_mobile_number));
                            else
                                guiErrorDialog(getString(R.string.invalid_email_id));
                            break;
                        case ConstantsAstSep.ForgotPasswordCode.OTP_GENERATION_FAILED:
                            guiErrorDialog(getString(R.string.otp_generation_failed));
                            break;
                        case ConstantsAstSep.ForgotPasswordCode.OTP_SEND_FAILED:
                            guiErrorDialog(getString(R.string.otp_send_failed));
                            break;
                        case ConstantsAstSep.ForgotPasswordCode.LOCKED_USER:
                            guiErrorDialog(getString(R.string.locked_user));
                            break;
                        case ConstantsAstSep.ForgotPasswordCode.SUCCESSFULL:
                            countDownTimer();
                            editText1.setEnabled(false);
                            editText2.setEnabled(false);
                            countryCodeSpinner.setEnabled(false);
                            raisedButton.setVisibility(View.GONE);
                            otpLinearLayout.setVisibility(View.VISIBLE);
                            break;
                        default:
                            guiErrorDialog(getString(R.string.network_error));
                    }
                }
            }
        }.start();

    }

    public void guiErrorDialog(String errorString) {
        GuiController.showErrorDialog(ForgotPasswordActivity.this, getString(R.string.error), errorString).show();
    }

    // Here validation is performed for the otp entered
    public void otpContinueClicked(final View view) {
        String OtpText = editText3.getText().toString();
        if (TextUtils.isEmpty(OtpText)) {
            Toast.makeText(this, R.string.error_otp_missing, Toast.LENGTH_LONG).show();
            return;
        } else {
            new GuiWorker<String>(this) {
                @Override
                protected String runInBackground() throws Exception {
                    return AndroidUtilsAstSep.getDelegateForForgotPassword().validateOTP(username, editText3.getText().toString());
                }

                @Override
                protected void onPostExecute(boolean successful, String response) {
                    if (successful) {
                        if (response != null)
                            if (response.equals(ConstantsAstSep.ForgotPasswordCode.OTP_VALIDATION_FAILED)) {
                                errorResultTextView.setVisibility(View.VISIBLE);
                                errorResultTextView.setText(getResources().getString(R.string.otp_differs));
                            } else {
                                setVisibilityAndValuesForPasswordChange();
                            }
                    }
                }
            }.start();
        }

        hideSoftKeyBoard();
    }

    public void resendCodeButtonClicked(final View view) {

        if (resendCount < maxResendAttempts) {
            resendCount++;
            countDownTimer();
            checkUserServiceCall();
        } else {
            new GuiWorker<Boolean>(this) {
                @Override
                protected Boolean runInBackground() throws Exception {
                    return AndroidUtilsAstSep.getDelegateForForgotPassword().lockUser(username);
                }

                @Override
                protected void onPostExecute(boolean successful, Boolean response) {
                    if (successful) {
                        if (response) {
                            errorResultTextView.setVisibility(View.VISIBLE);
                            editText3.setEnabled(false);
                            otpRaisedButton.setEnabled(false);
                            errorResultTextView.setText(getResources().getString(R.string.error_maximum_limit_reached));
                            flatButton.setTextColor(getResources().getColor(R.color.colorDisable));
                            otpRaisedButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                            SharedPreferences lockedUserSharedPreference = AndroidUtilsAstSep.getSharedPreferences(ConstantsAstSep.SharedPreferenceType.PERSISTENT);
                            SharedPreferences.Editor lockedUserSharedPreferenceEditor = lockedUserSharedPreference.edit();
                            lockedUserSharedPreferenceEditor.putBoolean(ConstantsAstSep.SharedPreferenceKeys.USER_LOCKED, true);
                            lockedUserSharedPreferenceEditor.apply();
                        }
                    }
                }

            }.start();
        }
    }

    // count down timer for enabling and disabling the resend code button
    public void countDownTimer() {

        new CountDownTimer(otpExpiryTime, minimumValueForTimer) {
            TextView textTimer = (TextView) findViewById(R.id.countDownTimerTextView);

            public void onTick(long millisUntilFinished) {
                flatButton.setTextColor(getResources().getColor(R.color.colorDisable));
                flatButton.setEnabled(false);
                textTimer.setText(millisUntilFinished / minimumValueForTimer + getResources().getString(R.string.sec));
            }

            public void onFinish() {
                textTimer.setText("");
                flatButton.setTextColor(getResources().getColor(R.color.colorAccentDark));
                flatButton.setEnabled(true);

            }
        }.start();
    }

    public void saveChangesButtonClicked(final View view) {
        String newPasswordText = editText1.getText().toString();
        String confirmNewPasswordText = editText2.getText().toString();

        if (TextUtils.isEmpty(newPasswordText) || TextUtils.isEmpty(confirmNewPasswordText)) {
            Toast.makeText(this, R.string.one_or_more_mandatory_field_is_missing, Toast.LENGTH_SHORT).show();
            return;
        } else if (!newPasswordText.equals(confirmNewPasswordText)) {
            errorResultTextView.setVisibility(View.VISIBLE);
            errorResultTextView.setText(getResources().getString(R.string.passwords_differ_please_try_again));
        } else {
            new GuiWorker<String>(this) {
                @Override
                protected String runInBackground() throws Exception {
                    return AndroidUtilsAstSep.getDelegateForForgotPassword().resetPassword(username, EncryptionUtils.base64Encode(editText1.getText().toString()));
                }

                @Override
                protected void onPostExecute(boolean successful, String response) {
                    if (successful) {
                        switch (response) {
                            case ConstantsAstSep.ForgotPasswordCode.ALREADY_LOCKED_USER:
                                guiErrorDialog(getString(R.string.locked_user));
                                break;
                            case ConstantsAstSep.ForgotPasswordCode.PASSWORD_RESET_FAILED:
                                AlertDialog.Builder passwordChangeUnsuccessfulDialog = GuiController.showErrorDialog((Activity) ctx, getResources().getString(R.string.password_change_unsuccessful));
                                passwordChangeUnsuccessfulDialog.setPositiveButton(R.string.try_again,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                editText1.setText("");
                                                editText2.setText("");
                                            }
                                        });
                                passwordChangeUnsuccessfulDialog.setNegativeButton(R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                signInButtonClicked(view);
                                            }
                                        });
                                passwordChangeUnsuccessfulDialog.setTitle(getResources().getString(R.string.password_change_unsuccess));
                                passwordChangeUnsuccessfulDialog.setCancelable(false);
                                passwordChangeUnsuccessfulDialog.show();
                                break;
                            case ConstantsAstSep.ForgotPasswordCode.PASSWORD_RESET_SUCCESS:
                                AlertDialog.Builder passwordChangeSuccessfulinfoDialog = GuiController.showInfoDialog((Activity) ctx, getResources().getString(R.string.your_password_has_been_changed) + "\n" + getResources().getString(R.string.to_log_on_use_the_changed_credentials));
                                passwordChangeSuccessfulinfoDialog.setPositiveButton(R.string.sign_in,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                signInButtonClicked(view);
                                            }
                                        });
                                passwordChangeSuccessfulinfoDialog.setTitle(getResources().getString(R.string.password_change_success));
                                passwordChangeSuccessfulinfoDialog.setCancelable(false);
                                passwordChangeSuccessfulinfoDialog.show();
                                break;
                            case ConstantsAstSep.ForgotPasswordCode.MISMATCH_PASSWORD_REQUIREMENTS:
                                guiErrorDialog(getString(R.string.not_matching_password_requirements));
                                break;
                            case ConstantsAstSep.ForgotPasswordCode.PASSWORD_RESET_NOT_SUPPORTED:
                                guiErrorDialog(getString(R.string.password_reset_not_supported));
                                break;
                            default:
                                guiErrorDialog(getString(R.string.network_error));
                        }
                    }
                }
            }.start();

            hideSoftKeyBoard();
        }

    }

    private void setVisibilityAndValuesForPasswordChange() {
        commonLinearLayout.setVisibility(View.VISIBLE);
        raisedButton.setVisibility(View.VISIBLE);
        otpLinearLayout.setVisibility(View.GONE);
        errorResultTextView.setVisibility(View.GONE);
        countryZipcodeEditText.setVisibility(View.GONE);
        countryCodeSpinner.setVisibility(View.GONE);

        editText1.setEnabled(true);
        editText2.setEnabled(true);

        GuIUtil.toggleTheButton(raisedButton, true);

        infoTextView.setText(getResources().getString(R.string.we_ll_ask_for_this_password_whenever_you_sign_in));
        editTextInpuLayout1.setHint(getResources().getString(R.string.enter_new_password));
        editTextInpuLayout2.setHint(getResources().getString(R.string.re_enter_new_password));
        raisedButton.setText(getResources().getString(R.string.save_changes));
        editText1.setText("");
        editText2.setText("");

        editText1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void signInButtonClicked(final View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this,
                LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void hideSoftKeyBoard() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final View editText1 = findViewById(R.id.editText1);
        final View editText2 = findViewById(R.id.editText2);
        final View editText3 = findViewById(R.id.editText3);
        inputMethodManager.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(editText3.getWindowToken(), 0);
    }


    @Override
    public void networkStatusChanged(boolean isConnected) {
        Log.d("ForgotPasswordActivity", "networkStatusChanged called, value = " + isConnected);
        networkStateIcon.setVisibility(View.VISIBLE);
        SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
        if (isConnected) {
            networkStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_4_bar_white, null));
        } else {
            networkStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_null_black, null));
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
    protected void onStop() {
        super.onStop();
        Log.d("ForgotPasswordActivity", "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ForgotPasswordActivity", "onDestroy called");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.raisedButton) {
            if (raisedButton.getText().toString().equals(getResources().getString(R.string.continue2)))
                generateOtpButtonClicked(v);
            else
                saveChangesButtonClicked(v);
        }
        if (v.getId() == R.id.flatButton) {
            resendCodeButtonClicked(v);
        }
        if (v.getId() == R.id.otpRaisedButton) {
            otpContinueClicked(v);
        }
    }

}
