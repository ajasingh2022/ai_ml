package com.capgemini.sesp.ast.android.module.cache;

import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO.SortByInstCode;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.OrderListKeys;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.Collections;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class WorkorderCache {
    private static final String TAG = WorkorderCache.class.getSimpleName();
    private static List<WorkorderLiteTO> workorderLiteTOs = null;

    public static void clear() {
        workorderLiteTOs = null;
        try {
            DatabaseHandler.createDatabaseHandler().purgeWorkorderCache();
        } catch (Exception e) {
            writeLog(TAG + " :clear() ", e);
        }
        /*DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
    	db.delete(WorkorderCustomWrapperTO.class);
    	db.close();*/
    }

    public static List<WorkorderLiteTO> getWorkordersLite(boolean update) {
        //return getWorkordersLite(false);
        List<WorkorderLiteTO> tos = null;
        try {
            if (workorderLiteTOs != null && !update) {
                tos = workorderLiteTOs;
            } else {
                tos = DatabaseHandler.createDatabaseHandler().getAllWorkorderLiteTos();
            }
            Collections.sort(tos, new SortByInstCode(false,	OrderListKeys.TIME_RESERVATION));
        } catch (Exception e) {
            writeLog(TAG + " :getWorkordersLite() ", e);
        }
        return tos;
    }


    public static List<WorkorderLiteTO> getWorkOrdersByType(Long idCaseT) {
         List<WorkorderLiteTO> wos = null;
        try {
            wos = DatabaseHandler.createDatabaseHandler().getWorkOrdersByType(idCaseT);
            Collections.sort(wos, new SortByInstCode(false, OrderListKeys.TIME_RESERVATION));
        } catch (Exception e) {
            writeLog(TAG + " :getWorkOrdersByType() ", e);
        }
        return wos;
    }

    public static List<WorkorderLiteTO> getWorkOrdersByTimeReserved(Long idCaseT) {
         List<WorkorderLiteTO> wos = null;
        try{
             wos =  DatabaseHandler.createDatabaseHandler().getTimeReservedWorkOrdersByType(idCaseT);
        } catch (Exception e) {
            writeLog(TAG + " :getWorkOrdersByTimeReserved() ", e);
        }
        return wos;
    }

    public static List<WorkorderLiteTO> getPlannedWorkOrdersByType(Long ppId, Long idCaseT, boolean timeReserved) {
        List<WorkorderLiteTO> wos = null;
        try{
        wos =  DatabaseHandler.createDatabaseHandler().getPlannedWorkOrdersByType(ppId, idCaseT, timeReserved);
        } catch (Exception e) {
            writeLog(TAG + " :getPlannedWorkOrdersByType() ", e);
        }
        return wos;
    }

    public static <T extends WorkorderCustomWrapperTO> void saveWorkorderToCache(T customWrapperTo) {
         try{
        DatabaseHandler.createDatabaseHandler().saveWOLiteTO(customWrapperTo);
         } catch (Exception e) {
             writeLog(TAG + " :saveWorkorderToCache() ", e);
         }
    }

    public static <T extends WorkorderCustomWrapperTO> T getWorkorderByCaseId(Long caseId, Class<T> clazz) {

        return DatabaseHandler.createDatabaseHandler().getSpecificWorkorder(caseId, clazz);
    }
}
