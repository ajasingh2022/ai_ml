package com.capgemini.sesp.ast.android.module.util;

import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataValueTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoAdditionalDataCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class AdditionalDataUtils {

    public static List<WoAdditionalDataTTO> filterControl(String typeOfControl) {

        List<WoAdditionalDataTTO> woAdditionalDataTTOS = ObjectCache.getAllTypes(WoAdditionalDataTTO.class);
        List<WoAdditionalDataTTO> list = new ArrayList<>();

        if (woAdditionalDataTTOS != null) {
            for (WoAdditionalDataTTO wo : woAdditionalDataTTOS) {
                try {
                    if (wo.getInfo() != null) {
                        String[] str = wo.getInfo().toString().split(";");
                        if (str.length == 2 && wo != null) {
                            if (str[0].equalsIgnoreCase("SOLAR_INST")) {
                                if (str[1].equalsIgnoreCase(typeOfControl)) {
                                    list.add(wo);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    writeLog("SolarCheckListFragment  : filterControl() ", e);
                }
            }
        }
        return list;
    }


    public static String getStringValueOfCodeFromAdditionalData(String code) {
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        List<WoAdditionalDataTTO> woAdditionalDataTTOS = ObjectCache.getAllTypes(WoAdditionalDataTTO.class);
        String codeValue = "";
        final List<WoAdditionalDataCustomTO> woAdditionalDataCustomTO = wo.getWorkorderCustomTO().getAdditionalDataList();

        for (WoAdditionalDataTTO woAdditionalDataTTO : woAdditionalDataTTOS) {
            if (woAdditionalDataTTO.getCode().equalsIgnoreCase(code)) {
                for (WoAdditionalDataCustomTO woAdditionalDataCustomTO1 : woAdditionalDataCustomTO) {
                    if (wo.getIdCase().equals(woAdditionalDataCustomTO1.getAdditionalData().getIdCase())) {
                        if (woAdditionalDataCustomTO1.getAdditionalData().getIdWoAdditionalDataT().equals(woAdditionalDataTTO.getId())) {
                            for (WoAdditionalDataValueTO woAdditionalDataValueTO : woAdditionalDataCustomTO1.getAdditionalDataValue()) {
                                codeValue = woAdditionalDataValueTO.getAdditionalDataValueO();
                                break;
                            }
                        }
                    }
                }
            }
        }
        return codeValue;
    }

    public static int getValueOfCodeFromAdditionalData(String code) {
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        List<WoAdditionalDataTTO> woAdditionalDataTTOS = ObjectCache.getAllTypes(WoAdditionalDataTTO.class);
        int codeId = 0;
        final List<WoAdditionalDataCustomTO> woAdditionalDataCustomTO = wo.getWorkorderCustomTO().getAdditionalDataList();

        for (WoAdditionalDataTTO woAdditionalDataTTO : woAdditionalDataTTOS) {
            if (woAdditionalDataTTO.getCode().equalsIgnoreCase(code)) {
                for (WoAdditionalDataCustomTO woAdditionalDataCustomTO1 : woAdditionalDataCustomTO) {
                    if (wo.getIdCase().equals(woAdditionalDataCustomTO1.getAdditionalData().getIdCase())) {
                        if (woAdditionalDataCustomTO1.getAdditionalData().getIdWoAdditionalDataT().equals(woAdditionalDataTTO.getId())) {
                            for (WoAdditionalDataValueTO woAdditionalDataValueTO : woAdditionalDataCustomTO1.getAdditionalDataValue()) {
                                codeId = Integer.parseInt(woAdditionalDataValueTO.getAdditionalDataValueO());
                                break;
                            }
                        }
                    }
                }
            }
        }
        return codeId;
    }

    public static Long getIDFromCodeInAdditionalData(String code) {
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        List<WoAdditionalDataTTO> woAdditionalDataTTOS = ObjectCache.getAllTypes(WoAdditionalDataTTO.class);
        Long codeId = null;
        final List<WoAdditionalDataCustomTO> woAdditionalDataCustomTO = wo.getWorkorderCustomTO().getAdditionalDataList();

        for (WoAdditionalDataTTO woAdditionalDataTTO : woAdditionalDataTTOS) {
            if (woAdditionalDataTTO.getCode().equalsIgnoreCase(code)) {
                for (WoAdditionalDataCustomTO woAdditionalDataCustomTO1 : woAdditionalDataCustomTO) {
                    if (wo.getIdCase().equals(woAdditionalDataCustomTO1.getAdditionalData().getIdCase())) {
                        if (woAdditionalDataCustomTO1.getAdditionalData().getIdWoAdditionalDataT().equals(woAdditionalDataTTO.getId())) {
                            codeId = woAdditionalDataTTO.getId();
                        }
                    }
                }
            }
        }
        return codeId;
    }
}