package com.capgemini.sesp.ast.android.ui.activity.order;



import com.capgemini.sesp.ast.android.ui.adapter.ParentListItem;

import java.util.List;

public class OrderSummary implements ParentListItem {
    private String instCode;
    private List<OrderSummaryItems> mOrderSummaryItems;

    private String timeReservation ;
    private String started  ;
    private String assigned ;
    private String key ;
    private String priority;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTimeReservation() {
        return timeReservation;
    }

    public void setTimeReservation(String timeReservation) {
        this.timeReservation = timeReservation;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public OrderSummary(String instCode, List<OrderSummaryItems> mOrderSummaryItems, String timeReservation, String started, String assigned, String key,String priority) {
        this.instCode = instCode;
        this.mOrderSummaryItems = mOrderSummaryItems;
        this.timeReservation = timeReservation;
        this.started = started;
        this.assigned = assigned;
        this.key = key;
        this.priority=priority;
    }

    public String getName() {
        return instCode;
    }

    @Override
    public List<?> getChildItemList() {
        return mOrderSummaryItems;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
