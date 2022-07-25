/**
 * @copyright Capgemini
 */
package com.capgemini.sesp.ast.android.module.dataloader;

import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderSummaryActivity;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderSummaryCategory;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * This async task reads the number of workorders for the current
 * logged in user
 * 
 * @author Capgemini
 * @since 12th March, 2015
 * @version 1.o
 *
 */
public class WorkorderLtoCategoryLoader extends GuiWorker<List<OrderSummaryCategory>> {
	
	/*
	 * The activity references, keep it as soft-reference so that 
	 * it allows the parent activity to be garbage collected
	 */
	private transient SoftReference<OrderSummaryActivity> activityRef = null;

	public WorkorderLtoCategoryLoader(final OrderSummaryActivity ownerActivity) {
		super(ownerActivity);
		activityRef = new SoftReference<OrderSummaryActivity>(ownerActivity);
	}

	@Override
	protected List<OrderSummaryCategory> runInBackground() throws Exception {
		return DatabaseHandler.createDatabaseHandler().getOrderSummaryCatInfo();
	}

	public void onPostExecute(boolean successful, final List<OrderSummaryCategory> result) {
		if(result!=null && activityRef!=null && activityRef.get()!=null){
			activityRef.get().onLoadFinished(result);
		}
	}
}
