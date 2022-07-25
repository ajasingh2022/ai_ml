package com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_taking;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.MaterialLogisticsActivity;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.ast_sep.common.to.ast.table.StocktakingTO;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class StockTakingFetchActiveThread extends GuiWorker<StocktakingTO> {

	private static final String TAG = StockTakingFetchActiveThread.class.getSimpleName();
	private Activity ownerActivity;
	
	public StockTakingFetchActiveThread(Activity ownerActivity) {
		super(ownerActivity);
		this.ownerActivity = ownerActivity;
	}

	@Override
    protected StocktakingTO runInBackground() throws Exception {
		setMessage(R.string.fetching_active_stock_taking);
		return AndroidUtilsAstSep.getDelegate().getActiveStockTaking(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK));
		
    }
	
	@Override
	protected void onPostExecute(boolean successful, StocktakingTO result) {
		try{
		if(successful) {
			if (result == null) {
				Log.d(TAG, "No Active Stocktaking found for the stocksite");
				Builder errorDialog = GuiController.showErrorDialog((Activity) ctx, ctx.getString(R.string.no_active_stocktaking_available));
				errorDialog.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								if(StockTakingFetchActiveThread.this.ownerActivity instanceof StockTakingActivity) {
									Intent materiaLogisticslIntent = new Intent(ctx, MaterialLogisticsActivity.class);
									ctx.startActivity(materiaLogisticslIntent);
								}
							}
						});
				errorDialog.show();

			} else{
				Intent goToIntent = new Intent(ctx, StockTakingActivity.class);
				goToIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				goToIntent.putExtra("StocktakingTO", result);
				ctx.startActivity(goToIntent);
			}
		}
		} catch (Exception e) {
			writeLog("StockTakingFetchActiveThread  : onPostExecute() ", e);
		}
	}

}
