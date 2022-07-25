package com.capgemini.sesp.ast.android.module.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.CacheController;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;

public class DownloadWOThread extends GuiWorker<Void> {
	
	public DownloadWOThread(final Activity ownerActivity, 
			final Class<? extends Activity> nextActivityClass, final Integer flag) {
		super(ownerActivity, nextActivityClass, null);
	}
	
	public DownloadWOThread(final Activity ownerActivity, 
			final Class<? extends Activity> nextActivityClass) {
		super(ownerActivity, nextActivityClass, null);
	}

	@Override
	protected Void runInBackground() throws Exception {
		/* Download work orders */
		setMessage(R.string.downloading_workorders);
		CacheController.downloadWorkorders();
		return null;
	}
	
	@Override
	protected void onPostExecute(boolean successful, Void v) {
		if (successful){
			if(nextActivityClass != null) {
				Intent goToIntent = new Intent(ctx, nextActivityClass);
	    		ctx.startActivity(goToIntent);
			}
		} else {
			Builder continueQuestion = GuiController.showConfirmCancelDialog((Activity) ctx, ctx.getString(R.string.offline_login), 
					ctx.getString(R.string.unable_to_download_typedata_continue_anyway));
			continueQuestion.setPositiveButton(R.string.yes,
	                 new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	Intent goToIntent = new Intent(ctx, nextActivityClass);
    	    		ctx.startActivity(goToIntent);
                }
            });
			continueQuestion.show();
		}
	}
	
	
	
}
