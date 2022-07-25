package com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.util.Log;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.StringUtil;
import com.capgemini.sesp.ast.android.module.util.to.BasicDataTO;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.ast_sep.common.to.custom.UnitChangeStatusCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisterMaterialControlUnitSaveThread extends GuiWorker<Void> {

	private UnitInformationTO unitInformationTO;
	private String strUnitId;
	BasicDataTO actionType;
	Activity ownerActivity;

	private final transient String TAG = getClass().getSimpleName();

	public RegisterMaterialControlUnitSaveThread(Activity ownerActivity, String strUnitId, UnitInformationTO unitInformationTO, BasicDataTO actionType) {
		super(ownerActivity);
		this.unitInformationTO = unitInformationTO;
		this.actionType = actionType;
		this.strUnitId = strUnitId;
		this.ownerActivity = ownerActivity;
	}

	@Override
	protected Void runInBackground() throws Exception {
		setMessage(R.string.saving);
		UnitChangeStatusCustomTO unitChangeStatusCustomTO;

		unitChangeStatusCustomTO = new UnitChangeStatusCustomTO();
		unitChangeStatusCustomTO.setIdentifier(this.strUnitId);
		unitChangeStatusCustomTO.setIdUnitIdentifierT(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER));
		unitChangeStatusCustomTO.setIdUnitStatusT(actionType.getId());
		unitChangeStatusCustomTO.setIdStock(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK));
		unitChangeStatusCustomTO.setSignature("");
		unitChangeStatusCustomTO.setTimestamp(new Date());
		unitChangeStatusCustomTO.setUnitType(unitInformationTO.getUnitTTO().getCode());
		unitChangeStatusCustomTO.setUnitModelType(unitInformationTO.getUnitModelTO().getCode());
		unitChangeStatusCustomTO.setIdUnitT(unitInformationTO.getUnitTTO().getId());
		unitChangeStatusCustomTO.setIdUnitModelT(unitInformationTO.getIdUnitModel());
		List<Long> idUnitStatusReasonTTOs = new ArrayList<Long>();
		idUnitStatusReasonTTOs.add(this.unitInformationTO.getUnitStatusReasonTTO().get(0).getId());
		unitChangeStatusCustomTO.setIdUnitStatusReasonTTOs(idUnitStatusReasonTTOs);

		if (actionType.getCode().equals(ConstantsAstSep.StockhandlingConstants.CONTROL)) {
			unitChangeStatusCustomTO.setPalletCode(unitInformationTO.getPalletCode());
			AndroidUtilsAstSep.getDelegate().changeUnitStatusSetReasonAndPallet(unitChangeStatusCustomTO);
		} else {
			AndroidUtilsAstSep.getDelegate().changeUnitStatusAndSetReason(unitChangeStatusCustomTO);
		}
		return null;
	}

	@Override
	protected void onPostExecute(boolean successful, Void result) {
		if (successful) {
			Builder builder = GuiController.showInfoDialog((Activity) ctx, ctx.getString(R.string.unit_registration_success));
			builder.show();
			if (!actionType.getCode().equals(ConstantsAstSep.StockhandlingConstants.MOUNTABLE)
					&& !actionType.getCode().equals(ConstantsAstSep.StockhandlingConstants.CONTROL)
					&& StringUtil.isNotEmpty(unitInformationTO.getPalletCode())
					&& !ObjectCache.materialControlPalletList.contains(unitInformationTO.getPalletCode())) {
				ObjectCache.materialControlPalletList.add(unitInformationTO.getPalletCode());
			}
			goToInputActivity();

		}
	}

	public void goToInputActivity() {
		Log.d(TAG, "=to toScrapping button clicked===");
		Intent goToIntent = new Intent(ownerActivity, ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.MATERIAL_CONTROL_INPUT_ACTIVITY));
		ownerActivity.startActivity(goToIntent);
		ownerActivity.finish();
	}

}
