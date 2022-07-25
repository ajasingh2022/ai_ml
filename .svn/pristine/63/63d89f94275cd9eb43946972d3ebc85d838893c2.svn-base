package com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_balance;

import android.app.Activity;
import android.content.Intent;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.skvader.rsp.ast_sep.common.to.custom.StockBalanceTO;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class StockBalanceFetchThread extends GuiWorker<StockBalanceTO> {
	
	public StockBalanceFetchThread(Activity ownerActivity) {
		super(ownerActivity, StockBalanceActivity.class);
	}

	@Override
    protected StockBalanceTO runInBackground() throws Exception {
		setMessage(R.string.fetching_stock_balance);
		return AndroidUtilsAstSep.getDelegate().getStockBalance(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK));
    }
	
	@Override
	protected void onPostExecute(boolean successful, StockBalanceTO stockBalanceTO) {
		try{
		if(successful) {
			Intent goToIntent = new Intent(ctx, StockBalanceActivity.class);
			goToIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			goToIntent.putExtra("StockBalanceTO", stockBalanceTO);
    		ctx.startActivity(goToIntent);
		}
		} catch (Exception e){
			writeLog("StockBalanceFetchThread :onPostExecute()",e );
		}
	}

}
