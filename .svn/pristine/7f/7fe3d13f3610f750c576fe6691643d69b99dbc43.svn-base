package com.capgemini.sesp.ast.android.ui.activity.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.communication.BluetoothServiceCallbackListener;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * 
 * This broadcast receiver would be called when the device
 * has discovered a local bluetooth device. Based on the provided listener
 * interface the callback method would be executed and the corresponding
 * activity/flow page can execute the business logic. 
 * 
 * 
 * @author Capgemini
 * @since 23rd December, 2014
 * @version 1.0
 *
 */

public class BluetoothSearchReceiver extends BroadcastReceiver {
	
	private transient BluetoothServiceCallbackListener listener = null;
	
	public BluetoothSearchReceiver(final BluetoothServiceCallbackListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	public void onReceive(final Context context,final Intent intent) {
		try{
		Log.d("BluetoothSearchReceiver-onReceive","Bluetooth Search Receiver onReceive called");
		
		final String action = intent.getAction();   
		Log.d("BluetoothSearchReceiver-onReceive","Action="+action);
		
		if (BluetoothDevice.ACTION_FOUND.equals(action)) { 
			//  A device is discovered using blue-tooth search
			final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);             
			// Add the name and address to an array adapter to show in a ListView
			Log.d("BluetoothSearchReceiver-onReceive","Bluetooth device found");
			Log.d("BluetoothSearchReceiver-onReceive","Device name="+device.getName());
			Log.d("BluetoothSearchReceiver-onReceive","Device address="+device.getAddress());
			
			if(listener!=null){
				listener.onBluetoothDevicesFound(device);
			}
		}
		} catch (Exception e) {
			writeLog("BluetoothSearchReceiver : onReceive()", e);
		}
	}
}
