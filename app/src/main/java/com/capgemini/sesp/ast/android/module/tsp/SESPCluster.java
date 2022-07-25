package com.capgemini.sesp.ast.android.module.tsp;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A cluster whose center is updated upon creation.
 */
public class SESPCluster<T extends ClusterItem> implements Cluster<T> {

    private Marker selectedMarker = null;
    private boolean isSelected = false;
    private LatLng mCenter;
    private final List<T> mItems = new ArrayList<T>();
    double radius=0;
    boolean containsTR = false;
    boolean containsSLA = false;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }



    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public SESPCluster(LatLng center) {
        mCenter = center;
    }

    public boolean add(T t) {
        return mItems.add(t);
    }

    @Override
    public LatLng getPosition() {
        return mCenter;
    }

    public void setPosition(LatLng mCenter) {
        this.mCenter = mCenter;
    }

    public boolean remove(T t) {
        return mItems.remove(t);
    }

    public void removeAll(){
        mItems.clear();
    }

    @Override
    public Collection<T> getItems() {
        return mItems;
    }

    @Override
    public int getSize() {
        return mItems.size();
    }

    @Override
    public String toString() {
        return "SESPCluster{" +
                "mCenter=" + mCenter +
                ", mItems.size=" + mItems.size() +
                '}';
    }

    @Override
    public int hashCode() {
        return mCenter.hashCode() + mItems.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof SESPCluster<?>)) {
            return false;
        }

        return ((SESPCluster<?>) other).mCenter.equals(mCenter)
                && ((SESPCluster<?>) other).mItems.equals(mItems);
    }


    public Marker getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(Marker selectedMarker) {
        this.selectedMarker = selectedMarker;
    }
}