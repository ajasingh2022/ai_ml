package com.capgemini.sesp.ast.android.ui.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;

/**
 * This broadcast receiver would be used to check 
 * network connectivity and would impact the SESP application flow
 * accordingly.
 * 
 * @author Capgemini
 * @since 29th October, 2014
 *
 */
public class NetworkStatusMonitorReceiver extends BroadcastReceiver{
	
	/* 
	 * As this broadcast receiver is locally initialized and de-initialized
	 * by the parent activity hence the parent reference does not need
	 * to be kept inside a soft ref
	 * 	 * 
	 */
	private transient NetworkStatusCallbackListener listener = null;
	
	public NetworkStatusMonitorReceiver(final NetworkStatusCallbackListener callback) {
		super();
		this.listener = callback;
	}


	 public NetworkStatusMonitorReceiver(){
		super();
	}
	
	/**
	 * This method would be called from the android OS automatically
	 * when there is a change in network connectivity status
	 * 
	 * As per the intent filter defined for this receiver, 
	 * its interested for only MOBILE DATA and WIFI network types 
	 * 
	 * 
	 * @param context {@link Context}
	 * @param intent {@link Intent}
	 */
	@Override
	public void onReceive(final Context context, final Intent intent) {
		AndroidUtilsAstSep.checkServerConnection(this.listener);
	}


}
