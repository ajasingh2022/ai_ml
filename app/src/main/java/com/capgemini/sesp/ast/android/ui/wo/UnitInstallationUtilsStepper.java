package com.capgemini.sesp.ast.android.ui.wo;

import android.content.Intent;
import android.os.Bundle;

import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class UnitInstallationUtilsStepper {

    private static final String TAG = UnitInstallationUtilsStepper.class.getSimpleName();

    public static String evaluateUnitInstallationNextPage(UnitModelCustomTO unitModelCustomTO, WoUnitCustomTO existingCMUnit,
                                                          Intent intent, Bundle bundle) {
        String returnValue = null;
        try {
            if (unitModelCustomTO != null) {
                if (unitModelCustomTO.getAllowCombinedUnits() != null &&
                        unitModelCustomTO.getAllowCombinedUnits().longValue() == CONSTANTS().SYSTEM_BOOLEAN_T__FALSE) {
                    //Unit Model is not Combined
                    if (!bundle.getBoolean(ConstantsAstSep.FlowPageConstants.SIM_CARD_INSTALLED)
                            && unitModelCustomTO.getRequireSimcard() != null
                            && unitModelCustomTO.getRequireSimcard().longValue() == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) {
                        // Condition 1: New sim card is must : REQUIRE_SIM = 1
                        bundle.putBoolean(ConstantsAstSep.FlowPageConstants.ENFORCE_NEW_SIM, true);
                        returnValue = ConstantsAstSep.PageNavConstants.NEXT_PAGE_SIM_CARD;

                    } else {
                        final Long allowSimCard = unitModelCustomTO.getAllowSimcard();
                        if (!bundle.getBoolean(ConstantsAstSep.FlowPageConstants.SIM_CARD_INSTALLED) &&
                                allowSimCard != null &&
                                allowSimCard.longValue() == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) {
                            // Condition 2: New Sim card is option : REQUIRE_SIM = 0 and ALLOW_SIM = 1
                            bundle.putBoolean(ConstantsAstSep.FlowPageConstants.ENFORCE_NEW_SIM, false);
                            returnValue = ConstantsAstSep.PageNavConstants.NEXT_PAGE_SIM_CARD;


                        } else { // Condition 3:  Sim card not supported by unit model

                            Long requireCM = unitModelCustomTO.getRequireCm();
                            Long allowCM = unitModelCustomTO.getAllowCm();

                            if (!bundle.getBoolean(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED) &&
                                    requireCM != null && requireCM.longValue() == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE &&
                                    !unitModelCustomTO.getIdUnitT().equals(CONSTANTS().UNIT_T__CM)
                                //&& existingCMUnit != null
                                    ) { // Condition 4: Option Built in CM unchecked and Require CM = true
                                bundle.putBoolean(ConstantsAstSep.FlowPageConstants.ENFORCE_NEW_CM, true);
                                returnValue = ConstantsAstSep.PageNavConstants.NEXT_PAGE_CM_MODULE;

                            } else if (!bundle.getBoolean(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED) &&
                                    allowCM != null && allowCM.longValue() == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE &&
                                    // existingCMUnit != null &&
                                    !unitModelCustomTO.getIdUnitT().equals(CONSTANTS().UNIT_T__CM)) { // Condition 5: Option Built in CM unchecked and Require CM = true

                                bundle.putBoolean(ConstantsAstSep.FlowPageConstants.ENFORCE_NEW_CM, false);
                                returnValue = ConstantsAstSep.PageNavConstants.NEXT_PAGE_CM_MODULE;

                            } else { // Condition 6: Option Built in CM checked or Require CM is false or Allow CM is false

                                returnValue = ConstantsAstSep.PageNavConstants.NEXT_PAGE_UNIT_VERIFICATION;

                            }
                        }
                    }
                } else {
                    //Unit Model is Combined -- Installation is finished
                    //return ConstantsAstSep.PageNavConstants.NEXT_PAGE_UNIT_VERIFICATION;
                    returnValue = ConstantsAstSep.PageNavConstants.NEXT_PAGE_UNIT_VERIFICATION;
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : evaluateUnitInstallationNextPage() ", e);
        }
        return returnValue;
    }

    public static WoUnitCustomTO getUnit(final WorkorderCustomWrapperTO wo, final Long idWoUnitT,
                                         final Long idWoUnitStatusT) {
        WoUnitCustomTO res = null;
        try {
            if (wo.getWorkorderCustomTO().getWoUnits() != null
                    && !wo.getWorkorderCustomTO().getWoUnits().isEmpty()) {
                for (WoUnitCustomTO unit : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (unit.getIdWoUnitT().equals(idWoUnitT)) {
                        if (idWoUnitStatusT.equals(TypeDataUtil.getOrgOrDiv(unit.getIdWoUnitStatusTO(), unit.getIdWoUnitStatusTD(), unit.getIdWoUnitStatusTV()))) {
                            res = unit;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getUnit() ", e);
        }

        return res;
    }
}