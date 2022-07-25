package com.capgemini.sesp.ast.android.module.util;

import com.capgemini.sesp.ast.android.module.communication.WorkorderAdditionalProcessingCallbackListener;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.Date;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by samdasgu on 4/6/2017.
 */
public class WorkOrderEventReasonResetProcessor implements WorkorderAdditionalProcessingCallbackListener<WorkorderCustomWrapperTO> {

    @Override
    public void process(WorkorderCustomWrapperTO workOrder, Object... parameters) {
        try {
            WoEventCustomTO woEventCustomTO = WorkorderUtils.getOrCreateWoEventCustomTO(workOrder, AndroidUtilsAstSep.CONSTANTS().WO_EVENT_T__FIELD_VISIT, AndroidUtilsAstSep.CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA);
            if (workOrder.getWorkorderCustomTO().getWoEvents() != null
                    && woEventCustomTO != null && woEventCustomTO.getResultReasons() != null) {
                woEventCustomTO.getResultReasons().clear();
                woEventCustomTO.getWoEventTO().setId(null);
                woEventCustomTO.getWoEventTO().setIdWoEventResultT(null);
                woEventCustomTO.getWoEventTO().setEventTimestamp(new Date());
            }
        } catch (Exception e) {
            writeLog("WorkOrderEventReasonResetProcessor  :process() ", e);
        }
    }
}
