package com.capgemini.sesp.ast.android.ui.activity.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.BluetoothService;
import com.capgemini.sesp.ast.android.module.communication.BluetoothServiceCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.ui.activity.receiver.BluetoothSearchReceiver;
import com.capgemini.sesp.ast.android.ui.fragments.StandardBluetoothHandlerFragment;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

//import android.annotation.SuppressLint;


/**
 * This activity would act like a plug an play activity
 * which provides standard functionality and user interface
 * for interacting with bluetooth functionlity and bluetooth search and pairing.
 * <p>
 * <P><b>
 * This activity does not provide functionality to pair with a device
 * and specialized bluetooth profile based communication.
 * </b></p>
 *
 * @author nirmchak
 * @version 1.0
 * @since 12th March, 2015
 */
@SuppressLint("InflateParams")
public class StandardBluetoothHandlerActivity extends AppCompatActivity implements BluetoothService {

    /*
     * Local broadcast receiver for bluetooth searching 
     * of local devices
     */
    private static final String TAG = StandardBluetoothHandlerFragment.class.getSimpleName();
    private transient BroadcastReceiver bluetoothSearchReceiver = null;
    private transient BluetoothServiceCallbackListener btCallbackListener = null;
    private transient BluetoothAdapter btAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_bluetooth_handler);

    }

    @Override
    protected void onResume() {
        super.onResume();
        btAdapter = enableDisableBluetooth(true);
    }

    public void onPause() {
        super.onPause();
        stopLocalBluetoothDeviceSearch();
    }



    /**
     * Utility method to be called from any flow page
     * based on requirement
     *
     * @param enable {@link Boolean}
     */

    @Override
    public BluetoothAdapter enableDisableBluetooth(final boolean enable) {
        final Boolean currentBtStatus = AndroidUtilsAstSep.getBluetoothStatus();
        BluetoothAdapter adapter = null;
        try {
            if (currentBtStatus != null ) {
                adapter = AndroidUtilsAstSep.turnOnOffBluetooth(enable);
            }
        } catch (Exception e) {
            writeLog(TAG + " :enableDisableBluetooth()", e);
        }
        return adapter;
    }

    @Override
    public void searchLocalBluetoothDevices(final BluetoothServiceCallbackListener listener) {
        // Build the intent for blue-tooth discovery broadcast receiver
        try {
            if (this.bluetoothSearchReceiver == null && listener != null) {

                this.btCallbackListener = listener;

                final IntentFilter filter = new IntentFilter();
                filter.addAction(BluetoothDevice.ACTION_FOUND);

                this.bluetoothSearchReceiver = new BluetoothSearchReceiver(btCallbackListener);
                registerReceiver(this.bluetoothSearchReceiver, filter);

                AndroidUtilsAstSep.searchBluetoothDevices(btAdapter);
            }
        } catch ( Exception e1) {
            writeLog(TAG + " : searchLocalBluetoothDevices", e1);
        }
        // Else the broadcast receiver is already running ignore the call
    }

    @Override
    public void stopLocalBluetoothDeviceSearch() {
        try {
            if (this.bluetoothSearchReceiver != null) {
                unregisterReceiver(this.bluetoothSearchReceiver);
                this.bluetoothSearchReceiver = null;
            }
        } catch (final Exception e1) {
            writeLog(TAG + " : StandardFlowActivity-stopLocalBluetoothDeviceSearch()", e1);
        }
    }
}
