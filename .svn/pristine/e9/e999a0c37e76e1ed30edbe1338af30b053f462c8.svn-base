package com.capgemini.sesp.ast.android.module.util;

import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.to.BasicDataTO;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control.UnitStatusTypeInformationType;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusTTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by samdasgu on 3/11/2017.
 */
public class MaterialLogisticsUtils {

    private static List<UnitStatusTypeInformationType> unitStatusTypeInformationTypes;

    /**
     * Fetch Unit Status Type Information
     * @return List<UnitStatusTypeInformationType>
     */
    public static List<UnitStatusTypeInformationType> createUnitStatusTypeInformationTypes() {
        try{
        if(Utils.isNotEmpty(unitStatusTypeInformationTypes)) {
            return unitStatusTypeInformationTypes;
        } else {
            List<UnitStatusTTO> unitStatusTTOs = ObjectCache.getAllTypes(UnitStatusTTO.class);
            List<UnitStatusReasonTTO> unitStatusReasonTTOs = ObjectCache.getAllTypes(UnitStatusReasonTTO.class);

            unitStatusTypeInformationTypes = new ArrayList<UnitStatusTypeInformationType>();
            for (UnitStatusTTO unitStatusTTO : unitStatusTTOs) {
                UnitStatusTypeInformationType unitStatusTypeInformationType = new UnitStatusTypeInformationType();
                BasicDataTO unitStatusType = new BasicDataTO();
                unitStatusType.setId(unitStatusTTO.getId());
                unitStatusType.setCode(unitStatusTTO.getCode());
                unitStatusType.setName(unitStatusTTO.getName());
                unitStatusType.setInfo(unitStatusTTO.getInfo());
                unitStatusTypeInformationType.setUnitStatusType(unitStatusType);

                List<UnitStatusReasonTCTO> unitStatusReasonTCTOs = new ArrayList<UnitStatusReasonTCTO>();
                for (UnitStatusReasonTCTO unitStatusReasonTCTO : ObjectCache.getAllTypes(UnitStatusReasonTCTO.class)) {
                    if (unitStatusTTO.getId().equals(unitStatusReasonTCTO.getIdUnitStatusT())) {
                        unitStatusReasonTCTOs.add(unitStatusReasonTCTO);
                    }
                }
                BasicDataTO[] unitStatusReasonTypes = new BasicDataTO[unitStatusReasonTCTOs.size()];

                int j = 0;
                for (UnitStatusReasonTCTO unitStatusReasonTCTO : unitStatusReasonTCTOs) {
                    for (UnitStatusReasonTTO temp : unitStatusReasonTTOs) {
                        if (temp.getId().equals(unitStatusReasonTCTO.getIdUnitStatusReasonT())) {
                            BasicDataTO unitStatusReasonType = new BasicDataTO();
                            unitStatusReasonType.setId(temp.getId());
                            unitStatusReasonType.setCode(temp.getCode());
                            unitStatusReasonType.setName(temp.getName());
                            unitStatusReasonType.setInfo(temp.getInfo());
                            unitStatusReasonTypes[j] = unitStatusReasonType;
                            j++;
                            break;
                        }
                    }
                }
                unitStatusTypeInformationType.setAvailableUnitStatusReasonTypes(unitStatusReasonTypes);
                unitStatusTypeInformationTypes.add(unitStatusTypeInformationType);
            }
        }
        } catch (Exception e) {
                writeLog("MaterialLogisticsUtils : createUnitStatusTypeInformationTypes()", e);
            }
            return unitStatusTypeInformationTypes;

    }
}
