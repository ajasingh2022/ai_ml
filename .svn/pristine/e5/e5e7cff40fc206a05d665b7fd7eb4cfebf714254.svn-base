package com.capgemini.sesp.ast.android.module.util;

import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * UTILITY CLASS FOR HANDLING WoInfoCustomTO object of the work order
 * Created by : samdasgu
 * Date : 4/27/2017
 */
public class WoInfoCustomUtil {

    /**
     * Gets all info posts for specified idInfoT as a concatenated string including previously saved infos (info with id set).
     *
     * @param workorderTO
     * @param idInfoT     INFO_T constant
     * @return
     */
    public static String getInfo(WorkorderCustomWrapperTO workorderTO, Long idInfoT) {
        return getInfo(workorderTO, idInfoT, false);
    }

    /**
     * Gets all info posts for specified idInfoSourceType as a concatenated string with the option to include only unsaved infos.
     *
     * @param workorderTO
     * @param idInfoT                INFO_T constant
     * @param includeOnlyUnsavedInfo false=all info will be included. true=only unsaved (info with no id) will also be included.
     * @return
     */
    public static String getInfo(WorkorderCustomWrapperTO workorderTO, Long idInfoT, boolean includeOnlyUnsavedInfo) {
        StringBuffer sb = new StringBuffer();
        try {
            List<WoInfoCustomTO> woInfoCustomToList = workorderTO.getWorkorderCustomTO().getInfoList();
            if (Utils.isNotEmpty(woInfoCustomToList)) {
                for (WoInfoCustomTO woInfoCustomTO : woInfoCustomToList) {
                    if (woInfoCustomTO.getInfo().getIdInfoT().equals(idInfoT)) {
                        boolean alreadySaved = woInfoCustomTO.getInfo().getId() != null;
                        if (!(includeOnlyUnsavedInfo && alreadySaved)) {
                            sb.append(woInfoCustomTO.getInfo().getText());
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog("WoInfoCustomUtil  :getInfo() ", e);
        }
        return sb.toString();
    }

    /**
     * All infos of the type woInfoCustomTO.getIdInfoT() will be removed from the workorderTO,
     * the one woInfoCustomTO instance will take their place.
     *
     * @param workorderTO
     * @param woInfoCustomTO
     */
    public static void deleteAndAddWoInfoCustomMTO(WorkorderCustomWrapperTO workorderTO, WoInfoCustomTO woInfoCustomTO) {

        try {
            Long deleteIdInfoT = woInfoCustomTO.getInfo().getIdInfoT();
            List<WoInfoCustomTO> woInfoCustomTOs = workorderTO.getWorkorderCustomTO().getInfoList();
            if (Utils.isNotEmpty(woInfoCustomTOs)) {
                Iterator<WoInfoCustomTO> woInfoCustomTOIterator = woInfoCustomTOs.iterator();
                while (woInfoCustomTOIterator.hasNext()) {
                    WoInfoCustomTO infoCustomTO = woInfoCustomTOIterator.next();
                    if (deleteIdInfoT.equals(infoCustomTO.getInfo().getIdInfoT())) {
                        woInfoCustomTOIterator.remove();
                    }
                }
            }
            addWoInfoCustomMTO(workorderTO, woInfoCustomTO);
        } catch (Exception e) {
            writeLog("WoInfoCustomUtil  :deleteAndAddWoInfoCustomMTO() ", e);
        }
    }

    /**
     * Adds a woInfoCustomTO to the workorderTO infoList, the array of woInfoCustomTOs will be created if it does not exist
     *
     * @param workorderTO
     * @param woInfoCustomTO
     */
    public static void addWoInfoCustomMTO(WorkorderCustomWrapperTO workorderTO, WoInfoCustomTO woInfoCustomTO) {

        try {
            List<WoInfoCustomTO> woInfoCustomTOs = workorderTO.getWorkorderCustomTO().getInfoList();
            if (woInfoCustomTOs == null) {
                woInfoCustomTOs = new ArrayList<WoInfoCustomTO>();
            }
            woInfoCustomTOs.add(woInfoCustomTO);
            workorderTO.getWorkorderCustomTO().setInfoList(woInfoCustomTOs);
        } catch (Exception e) {
            writeLog("WoInfoCustomUtil  :addWoInfoCustomMTO() ", e);
        }
    }

    /**
     * Removes all unsaved infos (id is null) that has the specified idInfoT and date from a workorderTO's infoList
     *
     * @param workorderTO
     * @param idInfoT
     * @param date
     */
    public static void removeInfoWithTimestamp(WorkorderCustomWrapperTO workorderTO, Long idInfoT, Date date) {
        try {
            List<WoInfoCustomTO> woInfoCustomMTOs = workorderTO.getWorkorderCustomTO().getInfoList();
            if (woInfoCustomMTOs != null && date != null) {
                Iterator<WoInfoCustomTO> woInfoCustomTOIterator = woInfoCustomMTOs.iterator();
                while (woInfoCustomTOIterator.hasNext()) {
                    WoInfoCustomTO woInfoCustomTO = woInfoCustomTOIterator.next();
                    if (idInfoT.equals(woInfoCustomTO.getInfo().getIdInfoT())
                            && woInfoCustomTO.getInfo().getId() == null
                            && woInfoCustomTO.getInfo().getTimestamp().equals(date)) {
                        woInfoCustomTOIterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            writeLog("WoInfoCustomUtil:removeInfoWithTimestamp() ", e);
        }
    }
}
