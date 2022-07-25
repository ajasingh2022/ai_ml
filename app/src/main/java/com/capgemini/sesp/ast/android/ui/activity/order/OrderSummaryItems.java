package com.capgemini.sesp.ast.android.ui.activity.order;

import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;

public class OrderSummaryItems {

    private String mName;
    private String mInfo;

    private WorkorderLiteTO workOrder;


    public OrderSummaryItems(String name) {
        mName = name;
    }
    public OrderSummaryItems(String name, String mInfo) {

        mName = name;
        this.mInfo = mInfo;
    }


    public void setmName(String mName) {
        this.mName = mName;
    }

    public WorkorderLiteTO getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkorderLiteTO workOrder) {
        this.workOrder = workOrder;
    }

    public String getmName() {
        return mName;
    }


    public String getmInfo() {
        return mInfo;
    }

    public void setmInfo(String mInfo) {
        this.mInfo = mInfo;
    }

}
