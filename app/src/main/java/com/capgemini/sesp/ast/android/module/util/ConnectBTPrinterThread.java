package com.capgemini.sesp.ast.android.module.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.driver.iface.LabelPrinter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by aditam on 5/30/2017.
 */
public class ConnectBTPrinterThread extends GuiWorker<String> {
    LabelPrinter labelPrinter = null;
    private String btPrinterMacAddress;

    public ConnectBTPrinterThread(Activity ownerActivity,LabelPrinter labelPrinter, String btPrinterMacAddress) {
        super(ownerActivity,null);
        this.btPrinterMacAddress = btPrinterMacAddress;
        this.labelPrinter = labelPrinter;
    }
    @Override
    protected String runInBackground() throws Exception {
        setMessage(R.string.check_printer_status);
        String infoMessage = null;
        try {
            String deviceStatus = labelPrinter.checkDeviceStatus(btPrinterMacAddress);
            if("Bluetooth not supported on device.".equals(deviceStatus)) {
                infoMessage = ctx.getString(R.string.bluetooth_not_supported_on_device);
            } else if("Bluetooth on device not enabled.".equals(deviceStatus)) {
                infoMessage = ctx.getString(R.string.bluetooth_on_device_not_enabled);
            }

            if(labelPrinter.openConnection()) {
                infoMessage = ctx.getString(R.string.printer_connected_success);
                labelPrinter.closeConnection();
            } else {
                infoMessage = ctx.getString(R.string.printer_connected_fail);
            }

        } catch (Exception ex) {
            writeLog("ConnectBTPrinterThread  :PrintMenuActivity()", ex);
            labelPrinter.closeConnection();
            infoMessage = ctx.getString(R.string.printer_connected_fail);
        }
        return infoMessage;
    }

    @Override
    protected void onPostExecute(final boolean successful, final String infoToUser) {
        try {
            if (infoToUser != null) {
                AlertDialog.Builder infoDialog = GuiController.showInfoDialog((Activity) ctx, infoToUser);
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
            writeLog("ConnectBTPrinterThread" + " :onPostExecute()", e);
        }
    }
}