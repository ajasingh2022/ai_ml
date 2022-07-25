package com.capgemini.sesp.ast.android.ui.activity.material_logistics.register_new_broken_device;

import android.app.Activity;
import android.app.AlertDialog.Builder;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.ast_sep.common.to.custom.UnitChangeStatusCustomTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class RegisterNewBrokenDeviceSaveThread extends GuiWorker<Void> {
	
	private List<BrokenNewDeviceItem> items;
	
	public RegisterNewBrokenDeviceSaveThread(Activity ownerActivity, List<BrokenNewDeviceItem> items) {
		super(ownerActivity);
		this.items = items;
	}

	@Override
    protected Void runInBackground() throws Exception {
		setMessage(R.string.saving);
		
		UnitChangeStatusCustomTO unitChangeStatusCustomTO;
		
		for (BrokenNewDeviceItem item : items) {
			unitChangeStatusCustomTO = new UnitChangeStatusCustomTO();
			unitChangeStatusCustomTO.setIdentifier(item.getIdentifier());
			unitChangeStatusCustomTO.setIdUnitIdentifierT(item.getIdUnitIdentifierT());
			unitChangeStatusCustomTO.setIdUnitStatusT(AndroidUtilsAstSep.CONSTANTS().UNIT_STATUS_T__BROKEN_NEW_EQUIPMENT);
			unitChangeStatusCustomTO.setSignature("");
			unitChangeStatusCustomTO.setTimestamp(new Date());
			unitChangeStatusCustomTO.setIdUnitT(item.getIdUnitT());
			unitChangeStatusCustomTO.setIdUnitModelT(item.getIdUnitModelT());
			
			List<Long> idUnitStatusReasonTTOs = new ArrayList<Long>();
			idUnitStatusReasonTTOs.add(item.getUnitStatusReasonTTO().getId());
			unitChangeStatusCustomTO.setIdUnitStatusReasonTTOs(idUnitStatusReasonTTOs);

			AndroidUtilsAstSep.getDelegate().changeUnitStatusAndSetReason(unitChangeStatusCustomTO);
		}
		return null;
    }
	
	@Override
	protected void onPostExecute(boolean successful, Void result) {
		try{
		if(successful) {
			Builder builder = GuiController.showInfoDialog((Activity) ctx, ctx.getString(R.string.saved));
			builder.show();
			RegisterNewBrokenDeviceActivity list = (RegisterNewBrokenDeviceActivity) ctx;
			list.removeAllItems();
		}
		} catch (Exception e) {
			writeLog( "RegisterNewBrokenDeviceSaveThread: onPostExecute()", e);
		}
	}

}
