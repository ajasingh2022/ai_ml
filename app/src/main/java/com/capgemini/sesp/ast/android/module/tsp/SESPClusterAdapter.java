package com.capgemini.sesp.ast.android.module.tsp;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.capgemini.sesp.ast.android.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Adapter which can be used to customize behaviour of InfoWindowAdapter
 */
public class SESPClusterAdapter implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;
    private SESPCluster selectedCluster;
    MapsActivity mapsActivity;
    ImageView select,info;

    SESPClusterAdapter(Activity context) {
        mapsActivity = (MapsActivity)context;
        myContentsView = context.getLayoutInflater().inflate(
                R.layout.cluster_info_window, null);
        selectedCluster = ((MapsActivity)context).mSelectedCluster;
        select = myContentsView.findViewById(R.id.select);
        info = myContentsView.findViewById(R.id.info);

    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (mapsActivity.mSelectedCluster.isSelected()) {
            ((ImageView) myContentsView.findViewById(R.id.select)).setImageDrawable(mapsActivity.getResources().getDrawable(R.drawable.ic_yes_selected));
        }

        return myContentsView;
    }
}

