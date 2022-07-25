/**
 * 
 */
package com.capgemini.sesp.ast.android.module.communication;

import android.bluetooth.BluetoothAdapter;

/**
 * @author Capgemini
 *
 */
public interface BluetoothService {
	
	BluetoothAdapter enableDisableBluetooth(boolean enable) throws InterruptedException;
	void searchLocalBluetoothDevices(BluetoothServiceCallbackListener listener);
	void stopLocalBluetoothDeviceSearch();
}
