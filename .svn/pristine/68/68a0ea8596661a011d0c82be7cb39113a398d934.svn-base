package com.capgemini.sesp.ast.android.ui.activity.login;

import android.Manifest;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.activity.receiver.SESPDeviceAdministrationReceiver;
import com.capgemini.sesp.ast.android.ui.layout.FingerprintHelper;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Lockdown screen and device administration
 */
public class SESPDeviceAdministration extends AppCompatActivity implements FingerprintHelper.FingerprintHelperListener {

    static final String TAG = "SESPDeviceAdmin";
    static final int ACTIVATION_REQUEST = 1; // identifies our request id
    DevicePolicyManager devicePolicyManager;
    ComponentName deviceAdmin;
    final String APP_LAUNCH_ACTION = "com.capgemini.app.START";
    private transient EditText pinET = null;
    private FingerprintHelper fingerprintHelper;
    private FingerprintManager fingerprintManager;
    private Context context;
    public static boolean resultForAuthentication = false;
    private TextView fingerprintTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.lock_screen);
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceAdmin = new ComponentName(this, SESPDeviceAdministrationReceiver.class);
        fingerprintTextView=(TextView)findViewById(R.id.fingerprint_tv);
        pinET = findViewById(R.id.pinText);
        } catch (Exception e) {
            writeLog("SESPDeviceAdministration  : onCreate() ", e);
        }
    }

    public void pinButtonClicked(final View view) {
        /**
         * Lockdown PIN verification can be implemented here.
         */
        try{
        String pin = pinET.getText().toString();

        if((!TextUtils.isEmpty(pin)) && pin.equals("SESP2016")){
            resultForAuthentication = true;
            Intent intent=new Intent();
            // if pin correct then launch app
            intent.setAction(APP_LAUNCH_ACTION);
            // Verify that the intent will resolve to an activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }else{
                Log.d(TAG,"Can not initialize the startup task");
                 }
        }else{
            //Handling based on business.
            finish();
            return;
        }
        } catch (Exception e) {
            writeLog("SESPDeviceAdministration  : pinButtonClicked() ", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        //Check for the fingerprint permission and listen for fingerprint
        //add additional checks along with this condition based on your logic
        if (checkFingerprintSettings(this)) {
            //Fingerprint is available, update the UI to ask user for Fingerprint auth
            //start listening for Fingerprint
            fingerprintHelper = new FingerprintHelper(this);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            fingerprintHelper.startAuth(fingerprintManager, null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        super.onPause();
        if (fingerprintHelper != null)
            fingerprintHelper.cancel();
    }

    @Override
    public void authenticationFailed(String error) {
        if (!resultForAuthentication){
            resultForAuthentication = false;
            GuiController.showErrorDialog(this,getResources().getString(R.string.invalid_fingerprint_message)).setTitle(getResources().getString(R.string.authentication_failure)).show();
        }
        return;
    }
    @Override
    public void authenticationSuccess(FingerprintManager.AuthenticationResult result) {
        resultForAuthentication = true;
        Intent intent=new Intent();
        // if fingerprint correct then launch app
        intent.setAction(APP_LAUNCH_ACTION);
        // Verify that the intent will resolve to an activity
         if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            }else{
            Log.d(TAG,"Can not initialize the startup task");
           }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkFingerprintSettings(Context context) {
        final String TAG = "FP-Check";

        //Check for android version, FingerPrint is not available below Marshmallow
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d(TAG, "This Android version does not support fingerprint authentication.");
            fingerprintTextView.setVisibility(View.GONE);
            return false;
        }

        //Check whether the security option for phone is set.
        //ie. LockScreen is enabled or not.
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (!keyguardManager.isKeyguardSecure()) {
                Log.d(TAG, "User hasn't enabled Lock Screen");
                fingerprintTextView.setVisibility(View.GONE);
                return false;
            }
        }

        //check if user have registered any fingerprints
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            Log.d(TAG, "User hasn't registered any fingerprints");
            fingerprintTextView.setVisibility(View.GONE);
            return false;
        }

        //check for app permissions
        //Make sure you have mentioned the permission on AndroidManifest.xml
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "User hasn't granted permission to use Fingerprint");
            fingerprintTextView.setVisibility(View.GONE);
            return false;
        }
        Log.d(TAG, "Fingerprint authentication is set.");
        return true;
    }
}
