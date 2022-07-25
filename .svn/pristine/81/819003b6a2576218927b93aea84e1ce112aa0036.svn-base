package com.capgemini.sesp.ast.android.ui.activity.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;

/**
 * This is the component that is responsible for actual device administration.
 * It becomes the receiver when a policy is applied. A policy can be applied by
 * hardcoding it in the sesp_device_admin or through an EMM
 */
public class SESPDeviceAdministrationReceiver extends DeviceAdminReceiver {
    static final String TAG =  "SESPDeviceAdminReceiver";


    /** Called when this application is approved to be a device administrator. */
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, R.string.device_admin_enabled,
                Toast.LENGTH_LONG).show();
    }

    /** Called when this application is no longer the device administrator. */
    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, R.string.device_admin_disabled,
                Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDisabled");
    }
}
