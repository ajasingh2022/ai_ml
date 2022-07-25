package com.capgemini.sesp.ast.android.ui.activity.printer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.driver.iface.LabelPrinter;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.cft.common.util.Utils;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class PrintScannedIdActivity extends AppCompatActivity {

    private static final int SCAN_REQUEST = 100;
    private EditText gsrnValue;
    private Button scanIdentifierButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_print_scanned_id);

        // Hiding the logo as requested
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);

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
                    final Dialog dialog = new HelpDialog(PrintScannedIdActivity.this, ConstantsAstSep.HelpDocumentConstant.COMMUNICATION);
                    dialog.show();
                }
            });

        // -- Customizing the action bar ends -----

        final View localeFlag = vw.findViewById(R.id.save_btn);
        if(localeFlag!=null){
            localeFlag.setVisibility(View.INVISIBLE);
        }

        getSupportActionBar().setCustomView(vw, layout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(R.string.title_activity_print_scanned_id);

        gsrnValue = findViewById(R.id.gsrnValue);
        scanIdentifierButton = findViewById(R.id.scanIdentifierButton);
        } catch (Exception e) {
            writeLog("PrintScannedIdActivity : onCreate()", e);
        }
    }

    public void printScannedId(View view) {
        try{
        String gsrnValueText = gsrnValue.getText().toString();
        if(Utils.isNotEmpty(gsrnValueText)) {
            AndroidUtilsAstSep.turnOnOffBluetooth(true);
            final LabelPrinter labelPrinter = ApplicationAstSep.getDriverManager().getLabelPrinter();

            new PrintBarcodeThread(this, labelPrinter, gsrnValueText).start();

        } else {
            final View alertView
                    = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.prompt_user_response_layout, null);
            final Dialog dialog = GuiController.getCustomAlertDialog(this,
                    alertView, getString(R.string.gsrn_cannot_blank), "ERROR")
                    .create();
            if (alertView != null) {
                final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
        } catch (Exception e) {
            writeLog("PrintScannedIdActivity : printScannedId()", e);
        }
    }

    public void scanButtonClicked(View view) {
        AndroidUtilsAstSep.scanBarCode(this, SCAN_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        if (requestCode == SCAN_REQUEST && resultCode == Activity.RESULT_OK) {
            gsrnValue.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), false));
            AndroidUtilsAstSep.showHideSoftKeyBoard(this, false);
        }
    }

    private class PrintBarcodeThread extends GuiWorker<String> {
        LabelPrinter labelPrinter = null;
        String gsrnValueText = null;
        public PrintBarcodeThread(Activity ownerActivity,LabelPrinter labelPrinter, String gsrnValueText) {
            super(ownerActivity, PrintScannedIdActivity.class);
            this.labelPrinter = labelPrinter;
            this.gsrnValueText = gsrnValueText;
        }
        @Override
        protected String runInBackground() throws Exception {
            setMessage(R.string.check_printer_status);
            String infoMessage = null;
            try {
                String deviceStatus = labelPrinter.checkDeviceStatus(SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.BT_MAC_ADDRESS));
                if("Bluetooth not supported on device.".equals(deviceStatus)) {
                    infoMessage = getString(R.string.bluetooth_not_supported_on_device);
                } else if("Bluetooth on device not enabled.".equals(deviceStatus)) {
                    infoMessage = getString(R.string.bluetooth_on_device_not_enabled);
                } else {
                    setMessage(R.string.opening_connection);
                }

                if(labelPrinter.openConnection()) {
                    setMessage(R.string.sending_data_for_printing);
                    Thread.sleep(1500);
                    try {
                        labelPrinter.printBarCode("SESP", gsrnValueText, "");
                        infoMessage = getString(R.string.data_sent_to_printer);
                    } catch (Exception e) {
                        infoMessage = getString(R.string.error_in_printing);
                    }
                } else {
                    infoMessage = getString(R.string.printer_connected_fail);
                }
            } catch (Exception ex) {
                writeLog("PrintScannedIdActivity : runInBackground() ", ex);
                infoMessage = getString(R.string.printer_connected_fail);
            } finally {
                labelPrinter.closeConnection();
            }
            return infoMessage;
        }

        @Override
        protected void onPostExecute(final boolean successful, final String infoToUser) {
            try{
            if (infoToUser != null) {
                AlertDialog.Builder infoDialog = GuiController.showInfoDialog(PrintScannedIdActivity.this, infoToUser);
                infoDialog.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                infoDialog.show();
            }
            } catch (Exception e) {
                writeLog("PrintScannedIdActivity : onPostExecute()", e);
            }
        }
    }



}
