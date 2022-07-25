package com.capgemini.sesp.ast.android.ui.activity.USBSerialCommunication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.ZeraUtils;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.sesp.zera.utils.UsbDeviceListener;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by aditam on 3/17/2017.
 */
public class UsbSerialCommunicationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, UsbDeviceListener {

    protected EditText zeraComportConfigET;
    protected Button testZeraCommunicationBTN;
    private Spinner deviceTypeSpinner;
    private String deviceType;
    private ZeraUtils zeraUtils;
    private TextView connectionStatusTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usb_serial_communication_test_layout);
        /*
         * Setting up custom action bar view
         */
        final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final LayoutInflater layoutManager = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
        ImageButton help_btn = vw.findViewById(R.id.menu_help);
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new HelpDialog(UsbSerialCommunicationActivity.this, ConstantsAstSep.HelpDocumentConstant.COMMUNICATION);
                dialog.show();
            }
        });

        // -- Customizing the action bar ends -----

        getSupportActionBar().setCustomView(vw, layout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        initViews();


    }

    private void initViews() {
        try{
        zeraComportConfigET = findViewById(R.id.zeraComportConfigET);
        testZeraCommunicationBTN = findViewById(R.id.testZeraCommunicationBTN);
        connectionStatusTv = findViewById(R.id.connection_status_tv);
        deviceTypeSpinner = findViewById(R.id.device_type_spinner);
        deviceTypeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> deviceAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.zera_device_type));
        deviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deviceTypeSpinner.setAdapter(deviceAdapter);
        testZeraCommunicationBTN.setOnClickListener(this);
        } catch (Exception e) {
            writeLog("UsbSerialCommunicationActivity  : initViews() ", e);
        }
    }

    private void enableSendButton() {
        if(SESPPreferenceUtil.getPreferenceBoolean("USB_PERMISSION_GRANTED")){
            testZeraCommunicationBTN.setVisibility(View.VISIBLE);
        }
    }

    private void displayResponse(String data) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                deviceType = ZeraUtils.ZERA_MT_3000;
                initZera();
                break;
            case 2:
                deviceType = ZeraUtils.ZERA_MT_310;
                initZera();
                break;
            default:
                //do nothing
                break;
        }
    }

    private void initZera() {
       // zeraUtils = new ZeraUtils(this, deviceType, this);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.testZeraCommunicationBTN) {
            zeraUtils.connectZeraDevice();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        enableSendButton();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Usb listenr callbacks************************************************************
     */

    @Override
    public void onDeviceAttached() {
        Toast.makeText(this, "onDeviceAttached", Toast.LENGTH_LONG).show();
        connectionStatusTv.setText(getString(R.string.device_connected));
    }

    @Override
    public void onDeviceDetached() {
        Toast.makeText(this, "onDeviceDetached", Toast.LENGTH_LONG).show();
        connectionStatusTv.setText(getString(R.string.please_connect_the_device));
    }

    @Override
    public void onResponseReceived(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
        zeraComportConfigET.setText(data);
    }

    @Override
    public void onResponseReceived(byte[] data) {

    }

    @Override
    public void onStatusChange(String status) {
        Toast.makeText(this, "onStatusChange", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onUsbReady(String message) {
        Toast.makeText(this, getString(R.string.on_usb_ready), Toast.LENGTH_LONG).show();
        zeraUtils.executeCommandZeraMT310("AAV");
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(this, "onError " + errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionGranted(boolean isGranted) {
        try{
        SESPPreferenceUtil.savePreference("USB_PERMISSION_GRANTED", isGranted);
        if (isGranted) {
            enableSendButton();
        }
        } catch (Exception e) {
            writeLog("UsbSerialCommunicationActivity  : onPermissionGranted() ", e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    /**
     * Usb listenr callbacks end ************************************************************
     */
}
