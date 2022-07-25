package com.capgemini.sesp.ast.android.module.tsp;

import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.Date;

public class WorkOrder implements ClusterItem {


    WorkorderLiteTO workorderLiteTO;
    LatLng latLng;
    String sla;
    boolean selected;
    Date etc;
    Long travelTime;


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public WorkOrder() {
    }

    public WorkOrder(LatLng latLng, String caseId) {
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return String.valueOf(workorderLiteTO.getId());
    }

    @Override
    public String getSnippet() {
        return String.valueOf(workorderLiteTO.getId());
    }

    public Date getEtc() {
        return etc;
    }

    public void setEtc(Date etc) {
        this.etc = etc;
    }

    public Long getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Long travelTime) {
        this.travelTime = travelTime;
    }

    public WorkorderLiteTO getWorkorderLiteTO() {
        return workorderLiteTO;
    }

    public void setWorkorderLiteTO(WorkorderLiteTO workorderLiteTO) {
        this.workorderLiteTO = workorderLiteTO;
    }
}
