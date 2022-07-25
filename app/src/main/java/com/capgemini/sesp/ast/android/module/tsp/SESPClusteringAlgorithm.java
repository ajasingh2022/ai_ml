package com.capgemini.sesp.ast.android.module.tsp;

import android.graphics.Color;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.algo.Algorithm;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Algorithm implementaion for doing clustering
 * @param <T>
 */

public class SESPClusteringAlgorithm<T extends ClusterItem> implements Algorithm<T> {

    private final Set<T> mItems = Collections.synchronizedSet(new HashSet<T>());;//800;//in Meters
    HashSet<Cluster<T>> clustersAtLow, clustersAtHigh;
    ArrayList<Polygon> circles = null;
    private HashMap<LatLng,Integer> mPotentialCenters = null;
    MapsActivity mMap;
    double mRange;
    boolean mBoundsDrawn = false;

    public SESPClusteringAlgorithm(MapsActivity mMap, double mRange) {
        this.mMap = mMap;
        this.mRange = mRange;
        formClusters();

    }

    public void formClusters() {

        clustersAtHigh = new HashSet<>();
        clustersAtLow = new HashSet<>();
        synchronized (mItems) {

            ArrayList <T> mItemsList = sortMitemsToList();

            for (int i=0;i<5;i++) {

                for (Cluster cluster : clustersAtHigh) {
                    SESPCluster sespCluster= ((SESPCluster) cluster);
                    sespCluster.radius = 0;
                    sespCluster.removeAll();

                }


                for (T item : mItemsList) {
                    addToCluster(item, clustersAtHigh);
                }
            }

            for (Cluster<T> sespCluster:clustersAtHigh){
                setSLAandTRflags((SESPCluster<WorkOrder>) sespCluster);

            }


            for (T item : mItemsList) {
                SESPCluster sespCluster = new SESPCluster<>(item.getPosition());
                sespCluster.add(item);
                clustersAtLow.add(sespCluster);
            }

        }

    }

    private void setSLAandTRflags(SESPCluster<WorkOrder> sespCluster) {

        for (WorkOrder workOrder:sespCluster.getItems()){
            if (!workOrder.sla.contains("null")){
                sespCluster.containsSLA = true;
            }

            if (!(workOrder.workorderLiteTO.getTimeReservationEnd() == null)){
                sespCluster.containsTR = true;
            }

        }


    }

    private ArrayList<T> sortMitemsToList() {

        ArrayList<T> arrayList = new ArrayList(mItems);
        Comparator comparator = new MitemComparator();
        Collections.sort(arrayList, comparator);
        return arrayList;
    }

    @Override
    public boolean addItem(T t) {
        mItems.add(t);

        return true;
    }

    @Override
    public boolean addItems(Collection<T> collection) {
        mItems.addAll(collection);

        return true;
    }

    @Override
    public void clearItems() {
        mItems.clear();
    }

    @Override
    public boolean removeItem(T t) {
        mItems.remove(t);
        return true;
    }

    @Override
    public boolean updateItem(T item) {
        boolean result;
        synchronized (mItems) {
            result = removeItem(item);
            if (result) {
                // Only add the item if it was removed (to help prevent accidental duplicates on map)
                result = addItem(item);
            }
        }
        return result;
    }

    @Override
    public boolean removeItems(Collection<T> items) {
        mItems.removeAll(items);
        return true;
    }

    @Override
    public Set<? extends Cluster<T>> getClusters(float zoom) {

                if (zoom > 13f) {
                    mMap.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.infoView.setText("Zoom out to select clusters for routing");
                        }
                    });
                    drawBoundings(true);
                } else {
                    mMap.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.infoView.setText("Zoom in to see all work order locations");
                        }
                    });
                    drawBoundings(false);
                }

        mMap.zoomLevel = zoom;
        return clustersAtHigh;



    }

    private static long getCoord(long numCells, double x, double y) {
        return (long) (numCells * Math.floor(x) + Math.floor(y));
    }

    private void addToCluster(T item, HashSet<Cluster<T>> clusters) {

        if (mPotentialCenters == null) {
            mPotentialCenters = new HashMap<>();
        }
        LatLng clusterCenter = findNearestClusterCenter(item, mPotentialCenters);
        SESPCluster cluster = findClusterFromCenter(clusterCenter,clusters);

        if (cluster != null) {
            double distance = findDistanceBwn(cluster.getPosition(), item);
            if (distance < mRange) {
                mPotentialCenters.remove(cluster.getPosition());
                cluster.add(item);
                if (distance>cluster.radius)
                    cluster.radius = distance;
                cluster.setPosition(getCenterofCluster(cluster));
                mPotentialCenters.put(cluster.getPosition(),cluster.getSize());

            } else {
                SESPCluster newCluster = new SESPCluster<>(item.getPosition());
                newCluster.add(item);
                clusters.add(newCluster);
                mPotentialCenters.put(newCluster.getPosition(),newCluster.getSize());
            }
        } else {
            SESPCluster newCluster = new SESPCluster<>(item.getPosition());
            newCluster.add(item);
            clusters.add(newCluster);
            mPotentialCenters.put(newCluster.getPosition(),newCluster.getSize());
        }

    }

    private SESPCluster findClusterFromCenter(LatLng clusterCenter, HashSet<Cluster<T>> clusters) {

        for (Cluster sespCluster:clusters){
            if (sespCluster.getPosition() == clusterCenter) {
                return (SESPCluster) sespCluster;
            }
        }

        return null;
    }


    private LatLng getCenterofCluster(SESPCluster polygonPointsList) {
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < polygonPointsList.getSize(); i++) {
            ClusterItem clusterItem = (ClusterItem) ((ArrayList) polygonPointsList.getItems()).get(i);
            builder.include(clusterItem.getPosition());
        }
        LatLngBounds bounds = builder.build();
        centerLatLng = bounds.getCenter();

        return centerLatLng;
    }

    private double findDistanceBwn(LatLng latLng, T item) {

        double lat2, lat1, lon2, lon1, el1, el2;
        el1 = 0;
        el2 = 0;

        lat1 = latLng.latitude;
        lat2 = item.getPosition().latitude;

        lon1 = latLng.longitude;
        lon2 = item.getPosition().longitude;


        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);


    }

    private LatLng findNearestClusterCenter(T item, HashMap<LatLng,Integer> mPotentialCenters) {

        LatLng nearest = null;
        LatLng newNearest = null;

        Iterator keySetIterator = mPotentialCenters.keySet().iterator();

        while (keySetIterator.hasNext())
        {
            LatLng key = (LatLng) keySetIterator.next();

            double newDistance = findDistanceBwn(key, item);
            if (newDistance < mRange) {
                newNearest = key;
                if (nearest == null)
                    nearest = newNearest;

                else
                if (mPotentialCenters.get(newNearest) > mPotentialCenters.get(nearest))
                    nearest = newNearest;
            }

        }
        return nearest;
    }

    private Polygon drawCircle(SESPCluster sespCluster) {

        double minLat = Double.MAX_VALUE,minLang = Double.MAX_VALUE;
        double maxLat = Double.MAX_VALUE * -1 ,maxLang=Double.MAX_VALUE* -1;
        float size = 10;

        ArrayList<LatLng> allLatlng = new ArrayList<>();
        if (sespCluster.isSelected())
            size = 100;

        for (WorkOrder clusterItem:(ArrayList<WorkOrder>)sespCluster.getItems())
        {
            if (minLang > clusterItem.latLng.longitude)
                minLang =clusterItem.latLng.longitude;
            if (minLat > clusterItem.latLng.latitude)
                minLat = clusterItem.latLng.latitude;
            if (maxLang < clusterItem.latLng.longitude)
                maxLang = clusterItem.latLng.longitude;
            if (maxLat < clusterItem.latLng.latitude)
                maxLat = clusterItem.latLng.latitude;
            //allLatlng.add(clusterItem.latLng);

        }

        float hue = (300.0F - size) * (300.0F - size) / 90000.0F * 220.0F;

        PolygonOptions polygonOptions = new PolygonOptions()
                .add(new LatLng(maxLat,maxLang))
                .add(new LatLng(maxLat,minLang))
                .add(new LatLng(minLat,minLang))
                .add(new LatLng(minLat,maxLang))
                .strokeColor(Color.parseColor("#FFA366")).fillColor(Color.HSVToColor(0x80,new float[]{hue, 1.0F, 0.6F}));

        Polygon polygon = mMap.mMap.addPolygon(polygonOptions);

        return polygon;
    }

    public void drawBoundings(boolean yesNo)
    {
        if (circles!=null ) {
            if (mBoundsDrawn && !yesNo) {
                mMap.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBoundsDrawn = false;
                        if (circles != null) {
                            for (Polygon circle : circles) {
                                circle.remove();
                            }

                        }
                    }
                });
            }
        }
        else
            circles = new ArrayList<>();

        if (yesNo && !mBoundsDrawn){
            mBoundsDrawn = true;
            for(Cluster cluster:clustersAtHigh) {
                if (cluster.getSize() >1) {
                    mMap.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            circles.add(drawCircle((SESPCluster) cluster));
                        }
                    });
                }
            }
    }



}
    @Override
    public Collection<T> getItems() {
        return mItems;
    }

    @Override
    public void setMaxDistanceBetweenClusteredItems(int maxDistance) {
        mRange = maxDistance;

    }

    @Override
    public int getMaxDistanceBetweenClusteredItems() {
        return (int) mRange;
    }

    @Override
    public void lock() {

    }

    @Override
    public void unlock() {

    }

    private class MitemComparator implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            return  Double.compare(distanceFromInfinityToPoint(o1),distanceFromInfinityToPoint(o2));
        }

        private double distanceFromInfinityToPoint(T o1) {
            return findDistanceBwn(new LatLng(100000,100000),o1);
        }
    }
}
