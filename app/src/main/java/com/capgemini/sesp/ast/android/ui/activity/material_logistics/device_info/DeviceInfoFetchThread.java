package com.capgemini.sesp.ast.android.ui.activity.material_logistics.device_info;

import android.app.Activity;
import android.content.Intent;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.UnitItem;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;
import com.skvader.rsp.cft.common.util.Utils;

public class DeviceInfoFetchThread extends GuiWorker<UnitInformationTO> {

	private String identifier;
	private UnitItem unitItem;

	public DeviceInfoFetchThread(Activity ownerActivity, String identifier) {
		super(ownerActivity, ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.DEVICE_INFO_SHOW_ACTIVITY));
		this.identifier = identifier;
	}

	public DeviceInfoFetchThread(Activity ownerActivity, String identifier, UnitItem unitItem) {
		super(ownerActivity, ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.DEVICE_INFO_SHOW_ACTIVITY));
		this.identifier = identifier;
		this.unitItem = unitItem;
	}

	@Override
	protected UnitInformationTO runInBackground() throws Exception {
		setMessage(R.string.fetching_device);
		Long idIdentifierT = SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER);
		if(Utils.isNotEmpty(unitItem)) {
			if (Utils.isNotEmpty(unitItem.getIdUnitT())) {
				return AndroidUtilsAstSep.getDelegate().getDeviceInfoWithUnitType(identifier, idIdentifierT, Utils.isNotEmpty(unitItem.getIdUnitT()) ? unitItem.getIdUnitT() : null, Utils.isNotEmpty(unitItem.getIdUnitModelT()) ? unitItem.getIdUnitModelT() : null);
			} else {
				return AndroidUtilsAstSep.getDelegate().getDeviceInfo(identifier, idIdentifierT);
			}
		}else{
			return AndroidUtilsAstSep.getDelegate().getDeviceInfo(identifier, idIdentifierT);
		}
	}

	@Override
	protected void onPostExecute(boolean successful, UnitInformationTO unitInformationTO) {
		if(successful) {
			Intent goToIntent = new Intent(ctx, ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.DEVICE_INFO_SHOW_ACTIVITY));
			goToIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			goToIntent.putExtra("UnitInformationTO", unitInformationTO);
			ctx.startActivity(goToIntent);
		}
	}

}
