/**
 * 
 */
package com.capgemini.sesp.ast.android.ui.notification;

import android.app.AlertDialog;
import android.content.Context;

import java.lang.ref.SoftReference;

/**
 * @author nirmchak
 *
 */
public abstract class AbstractNotificationDialog extends AlertDialog {
	
	protected transient SoftReference<Context> context = null;

	protected AbstractNotificationDialog(Context context) {
		super(context);
		if(context!=null){
			this.context = new SoftReference<Context>(context);
		}
	}

}