package com.capgemini.sesp.ast.android.ui.activity.order;

import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.CaseTCustomTO;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.CASE_TYPE_NULL;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class OrderSummaryCategory implements Comparable<OrderSummaryCategory> {
    private Long idCaseT;
    private int numberOfWos;
    private int numberTimeReservation;
    private boolean timeReservationWarning = false;

    public Long getIdCaseT() {
        return idCaseT;
    }

    public void setIdCaseT(Long idCaseT) {
        this.idCaseT = idCaseT;
    }

    public int getCount() {
        return numberOfWos;
    }

    public void setNumberOfWoCount(final int count) {
        this.numberOfWos = count;
    }

    public int getTimeReservationCount() {
        return numberTimeReservation;
    }

    public void setTimeReservationCount(final int count) {
        this.numberTimeReservation = count;
    }

    public boolean getTimeReservationWarning() {
        return timeReservationWarning;
    }


    public void setTimeReservationWarning(final boolean flag) {
        this.timeReservationWarning = flag;
    }

    String c1 = null;
    String c2 = null;
    int value = 0;

    @Override
    public int compareTo(OrderSummaryCategory another) {
        if (CASE_TYPE_NULL.equals(this.idCaseT)) {
            return (CASE_TYPE_NULL.equals(another.idCaseT) ? 0 : -999999);
        } else {
            try {
                c1 = ObjectCache.getIdObjectName(CaseTCustomTO.class, this.idCaseT);
                c2 = ObjectCache.getIdObjectName(CaseTCustomTO.class, another.idCaseT);
                 value = c1.compareTo(c2);

            } catch (Exception e) {
                writeLog("OrderSummaryCategory : compareTo()", e);
            }

            return value ;
        }
    }

}
