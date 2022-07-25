package com.capgemini.sesp.ast.android.ui.activity.navigation;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

//import org.osmdroid.ResourceProxy;

/**
 * This is the overlay class which extends Itemized overlay class.
 * @author Umesh
 * @version 1.0
 */
public class MapOverlay extends ItemizedOverlay<OverlayItem> {


    private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();

    public MapOverlay(Drawable pDefaultMarker) {
        super(pDefaultMarker);
    }


    /*public MapOverlay(Drawable pDefaultMarker,
                             ResourceProxy pResourceProxy) {
        super(pDefaultMarker, pResourceProxy);
        // TODO Auto-generated constructor stub
    }*/

    /**
     * Adding the Geo point to overlay Item list
     * @param p
     * @param title
     * @param snippet
     */
    public void addItem(GeoPoint p, String title, String snippet){
        OverlayItem newItem = new OverlayItem(title, snippet, p);
        overlayItemList.add(newItem);
        populate();
    }

    @Override
    public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected OverlayItem createItem(int arg0) {
        // TODO Auto-generated method stub
        return overlayItemList.get(arg0);
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return overlayItemList.size();
    }

}
