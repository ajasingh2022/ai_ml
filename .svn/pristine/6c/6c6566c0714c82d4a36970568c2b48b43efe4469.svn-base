package com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.UnitItem;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.ast_sep.common.to.ast.table.StockTO;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class MaterialControlInfoFetchThread extends GuiWorker<UnitInformationTO> {

    private String identifier;
    private UnitItem unitItem;

    public MaterialControlInfoFetchThread(Activity ownerActivity, String identifier) {
        super(ownerActivity, MaterialControlRegisterUnitActivity.class);
        this.identifier = identifier;
    }

    public MaterialControlInfoFetchThread(Activity ownerActivity, String identifier, UnitItem unitItem) {
        super(ownerActivity, MaterialControlRegisterUnitActivity.class);
        this.identifier = identifier;
        this.unitItem = unitItem;
    }

    @Override
    protected UnitInformationTO runInBackground() throws Exception {
        // setMessage(R.string.fetching_Unit_info);

        Long idIdentifierT = SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER);
        Long idStockSite = SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK);
        List<StockTO> stockTOs = ObjectCache.getAllIdObjects(StockTO.class);
        Long idDomain = null;

            for (StockTO stockTO : stockTOs) {
                if (stockTO.getId().equals(idStockSite)) {
                    idDomain = stockTO.getIdDomain();
                    break;
                }
            }

        if (Utils.isNotEmpty(unitItem)) {
            if (Utils.isNotEmpty(unitItem.getIdUnitT())) {
                return AndroidUtilsAstSep.getDelegate().getDeviceInfoForDomainWithUnitType(identifier, idIdentifierT, idDomain,
                        Utils.isNotEmpty(unitItem.getIdUnitT()) ? unitItem.getIdUnitT() : null, Utils.isNotEmpty(unitItem.getIdUnitModelT()) ? unitItem.getIdUnitModelT() : null);
            } else {

                return AndroidUtilsAstSep.getDelegate().getDeviceInfoForDomain(identifier, idIdentifierT, idDomain);
            }
        } else {
            return AndroidUtilsAstSep.getDelegate().getDeviceInfoForDomain(identifier, idIdentifierT, idDomain);
        }

    }

    @Override
    protected void onPostExecute(boolean successful, UnitInformationTO unitInformationTO) {
        try{
        if (successful) {
            Long idStock = null;
            if (unitInformationTO.getStockCode() != null && unitInformationTO.getStockEnd() == null) {
                idStock = Long.valueOf(unitInformationTO.getStockCode());
            }

            if (unitInformationTO.getUnitStatusTTO() == null
                    || (!unitInformationTO.getUnitStatusTTO().getId().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_STATUS_T__BROKEN_NEW_EQUIPMENT)
                    && !unitInformationTO.getUnitStatusTTO().getId().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_STATUS_T__DISMANTLED))) {
                Builder builder = GuiController.showErrorDialog((Activity) ctx, ctx.getString(R.string.error_invalid_unit_status));
                builder.show();
            } else if (idStock != null &&
                    !idStock.equals(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK))) {

                Builder builder = GuiController.showErrorDialog((Activity) ctx, ctx.getString(R.string.error_invalid_stock_SITE));
                builder.show();
            } else {
                Intent goToIntent = new Intent(ctx, ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.MATERIAL_CONTROL_REGISTER_UNIT_ACTIVITY));
                goToIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                goToIntent.putExtra("UnitInformationTO", unitInformationTO);
                goToIntent.putExtra("UnitId", identifier);
                ctx.startActivity(goToIntent);
            }
        } else {
            String errorMessage;

            if (getException() != null) {
                errorMessage = (Utils.safeToString(getException().getMessage()).isEmpty() ? getException().getClass().getSimpleName() : getException().getMessage());
            } else if (isCancelled()) {
                errorMessage = ctx.getString(R.string.action_cancelled);
            } else {
                errorMessage = ctx.getString(R.string.network_error);
            }
            // commented no need to show url and port number if network is not available
            Builder builder = GuiController.showErrorDialog((Activity) ctx, errorMessage);
            builder.show();
        }
        } catch (Exception e) {
            writeLog("MaterialControlInfoFetchThread: onPostExecute()", e);
        }
    }

}
