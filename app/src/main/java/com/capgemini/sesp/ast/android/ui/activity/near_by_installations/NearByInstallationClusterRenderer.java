package com.capgemini.sesp.ast.android.ui.activity.near_by_installations;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.VectorDrawable;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.tsp.TspUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


public class NearByInstallationClusterRenderer extends DefaultClusterRenderer implements GoogleMap.OnCameraMoveListener {
    private final GoogleMap mMap;
    private final ClusterManager mClusterManager;
    private Context mContext;
    private float currentZoom;

    @Override
    protected void onBeforeClusterItemRendered(ClusterItem item, MarkerOptions markerOptions) {
        float sampler = 120;//Green;
            if(((NearByInstallationClusterItem) item).nearByWorkOrder.getLocked() != null)
                sampler = 120;
            else
                sampler = 0;
        VectorDrawable vectorDrawable = (VectorDrawable) mContext.getDrawable(R.drawable.ic_navigation);
        vectorDrawable.setTint(Color.HSVToColor(new float[]{sampler, 1.0F, 0.6F}));
        vectorDrawable.setAlpha(0xFF);
        markerOptions.icon(TspUtil.generateBitmap(vectorDrawable));
        markerOptions.zIndex(10);
        super.onBeforeClusterItemRendered(item, markerOptions);


    }

    public NearByInstallationClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
        mMap = map;
        mClusterManager = clusterManager;
        mMap.setOnCameraMoveListener(this);
        setMinClusterSize(2);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {

        return  super.shouldRenderAsCluster(cluster);

    }


    @Override
    public void onCameraMove() {
        currentZoom = mMap.getCameraPosition().zoom;
        mClusterManager.cluster();
    }
}
