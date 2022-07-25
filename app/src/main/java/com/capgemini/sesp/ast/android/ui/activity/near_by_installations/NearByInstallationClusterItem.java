package com.capgemini.sesp.ast.android.ui.activity.near_by_installations;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.skvader.rsp.ast_sep.common.to.custom.NearByWorkOrder;

public class NearByInstallationClusterItem implements ClusterItem {

    NearByWorkOrder nearByWorkOrder;

    public NearByInstallationClusterItem(NearByWorkOrder nearByWorkOrder) {
        this.nearByWorkOrder = nearByWorkOrder;
    }

    public NearByWorkOrder getNearByWorkOrder() {
        return nearByWorkOrder;
    }

    public void setNearByWorkOrder(NearByWorkOrder nearByWorkOrder) {
        this.nearByWorkOrder = nearByWorkOrder;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(nearByWorkOrder.getLatitude(),nearByWorkOrder.getLongitude());
    }

    @Override
    public String getTitle() {
        return nearByWorkOrder.getIdCase().toString();
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
