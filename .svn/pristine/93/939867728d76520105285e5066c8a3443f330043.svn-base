/**
 * 
 */
package com.capgemini.sesp.ast.android.ui.notification;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.capgemini.sesp.ast.android.R;

/**
 * @author nirmchak
 *
 */
public class WOGenericNotification extends AbstractNotificationDialog {

	public WOGenericNotification(final Context context) {
		super(context);
	}
	
	@Override
	public void onCreate(final Bundle bundle){
		super.onCreate(bundle);
		setMessage("If you return to main menu you will loose");		
		if(super.context!=null && super.context.get()!=null){
			final Context ctx = this.context.get();
			final Resources resources = ctx.getResources();
			setTitle(resources.getString(R.string.warning));
		
			this.setButton(DialogInterface.BUTTON_POSITIVE, resources.getString(R.string.ok), new OnClickListener() {
				
				@Override
				public void onClick(final DialogInterface dialog, final int which) {
					
				}
			});
			
			this.setButton(DialogInterface.BUTTON_NEGATIVE, resources.getString(R.string.no), new OnClickListener() {
				
				@Override
				public void onClick(final DialogInterface dialog, final int which) {
					
				}
			});
		}
	}
	
}
