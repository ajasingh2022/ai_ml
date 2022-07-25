package com.capgemini.sesp.ast.android.module.tsp;

import android.app.Activity;
import android.view.View;

import com.capgemini.sesp.ast.android.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class SESPItemAdapter  implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        private WorkOrder selectedWorkOrder;

        SESPItemAdapter(Activity activity) {
            myContentsView = activity.getLayoutInflater().inflate(
                    R.layout.wo_info_window, null);
            selectedWorkOrder = ((MapsActivity)activity).mSelectedOrder;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return myContentsView;
        }

}
