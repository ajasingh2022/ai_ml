package com.capgemini.sesp.ast.android.ui.activity.navigation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.LocationCaptureInterface;
import com.capgemini.sesp.ast.android.module.communication.LocationStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.cft.common.util.Utils;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

//import org.osmdroid.bonuspack.overlays.Marker;
//import org.osmdroid.bonuspack.overlays.Polyline;

/**
 * Created by umeshm on 3/1/2016.
 */
public class MapNavigation extends AppCompatActivity implements LocationStatusCallbackListener, LocationCaptureInterface, View.OnClickListener {

    private double mDestinationLongitude;
    private double mDestinationLatitude;
    private double mCurrentLocLatitude = 0.0d;
    private double mCurrentLocLongitude = 0.0d;

    private boolean mLocationChange = false;

    private MapView map;
    private RoadManager roadManager;

    private transient AlertDialog dialog = null;
    private Button navigateBtn;
        static  String TAG = MapNavigation.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        map = findViewById(R.id.map);
        navigateBtn = findViewById(R.id.navigate_btn);
        navigateBtn.setOnClickListener(this);
        if (getIntent().getExtras() != null) {

            TextView txtTitleBar = findViewById(R.id.title_text);
            txtTitleBar.setText(getString(R.string.title_activity_map_navigation));


            //TODO : Commenting the customer location. Server is responding with there is no route possible between current location(INDIA)
            // and customer location. so for demo purpose hardcoding kolkata location

            mDestinationLatitude = getIntent().getExtras().getDouble("customerLatitude");
            mDestinationLongitude = getIntent().getExtras().getDouble("customerLongitude");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Calling only once. If the location is found then no need to call service again
        if (!mLocationChange) {

            //If customer location is not received then should not display the route
            if (mDestinationLatitude != 0.0d && mDestinationLongitude != 0.0d) {
                //Navigate Button is enabled for proper working
                navigateBtn.setEnabled(true);
                processGPS();
            } else {
                //Navigate Button is disabled because otherwise clicking navigate will create a problem
                navigateBtn.setEnabled(false);
                Toast.makeText(this, this.getString(R.string.customer_location_not_received), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
        unregForLocationChangeInfo(this, null);
    }

    /**
     * If the gps is off, dialog will be displayed to turn on
     */
    private void processGPS() {

        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertView = null;

        // Check if GPS is enabled from system settings
        if (AndroidUtilsAstSep.isGPSEnabled()) {

            alertView = inflater.inflate(R.layout.animate_gps_tracking_layout, null);
            if (alertView != null) {

                final Button cancelButton = alertView.findViewById(R.id.cancelButton);
                if (cancelButton != null) {
                    cancelButton.setOnClickListener(this);
                }
                dialog = GuiController.getCustomAlertDialog(this, alertView, null, null).create();
                dialog.show();

                regForLocationChangeInfo(this, LocationManager.GPS_PROVIDER);
            }

        } else {

            alertView = inflater.inflate(R.layout.gps_not_enabled_layout, null);

            if (alertView != null) {
                //final Button okButton = (Button) alertView.findViewById(R.id.okButton);
                //final Button turnGpsNoButton = (Button) alertView.findViewById(R.id.turnGpsNoButton);

                final Switch turnGpsYesNoSwitch = alertView.findViewById(R.id.turnGpsYesNoSwitch);

/*                if (okButton != null) {                                                               std ui modifications
                    okButton.setOnClickListener(this);
                }

                if (turnGpsNoButton != null) {
                    turnGpsNoButton.setOnClickListener(this);
                }*/

                if (turnGpsYesNoSwitch != null) {

                    turnGpsYesNoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {
                                dialog.dismiss();
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                            else {
                                dialog.dismiss();
                                MapNavigation.this.finish();
                            }

                        }
                    });

                }

                dialog = GuiController.getCustomAlertDialog(this, alertView, null, null).create();
                dialog.show();
            }
        }
    }

    /**
     * Handling click events
     *
     * @param v
     */
    public void onClick(View v) {
        /*if (v != null && v.getId() == R.id.okButton && dialog != null) {
            dialog.dismiss();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));*/
        if (v != null && v.getId() == R.id.cancelButton && dialog != null) {
            dialog.dismiss();
            MapNavigation.this.finish();   //If user clicks "cancel" button in dialog then finishing the activity
        }else if(v != null && (v.getId() == R.id.navigate_btn)){
          //  AndroidUtilsAstSep.navigateTo(this, String.valueOf(mDestinationLatitude), String.valueOf(mDestinationLongitude));
            if(!AndroidUtilsAstSep.isEmulator()) {
                              AndroidUtilsAstSep.navigateTo(this, String.valueOf(mDestinationLatitude), String.valueOf(mDestinationLongitude));
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * Register for location change
     *
     * @param callBack {@link LocationStatusCallbackListener}
     * @param provider {@link String} Provider constant
     * @throws Exception
     */
    @Override
    public void regForLocationChangeInfo(LocationStatusCallbackListener callBack, String provider) {

        if (callBack != null && !Utils.safeToString(provider).equals("")) {

            boolean shouldRequestNewLoc = false;
            AndroidUtilsAstSep.requestLocationByProviders(MapNavigation.this, this, shouldRequestNewLoc);
        }
    }

    /**
     * Unregister for location change
     *
     * @param callBack {@link LocationStatusCallbackListener}
     * @param provider {@link String} Provider constant
     */
    @Override
    public void unregForLocationChangeInfo(LocationStatusCallbackListener callBack, String provider) {

        // Stop the listener. Provider will not be used in this case.

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }

    /**
     * Location change listener. If the location found then this method will be called automatically
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {

            if ((mCurrentLocLatitude == 0.0d && mCurrentLocLongitude == 0.0d)) {

                mLocationChange = true;

                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                // Disable GPS tracking immediately to save power
                unregForLocationChangeInfo(this, null);

                mCurrentLocLatitude = location.getLatitude();
                mCurrentLocLongitude = location.getLongitude();

                //Start Async Task
                new UpdateRoadTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

        } else {
            Toast.makeText(MapNavigation.this, this.getString(R.string.no_location), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //No Implementation is required
    }

    @Override
    public void onProviderEnabled(String s) {
        //No Implementation is required
    }

    @Override
    public void onProviderDisabled(String s) {
        //No Implementation is required
    }


    /**
     * Async Task for drawing the route between technician current location and customer location
     */
    private class UpdateRoadTask extends AsyncTask<Void, Void, Road> {

        private final Context context;
        private ProgressDialog progressDialog = null;

        public UpdateRoadTask(Context context) {
            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(context.getString(R.string.route));
            progressDialog.show();
            }catch(Exception e) {
                writeLog(TAG +"  : onPreExecute() " ,e);
            }

        }

        @Override
        protected Road doInBackground(Void... voids) {
            Road road = null;
            roadManager = new OSRMRoadManager(context);
            ArrayList<GeoPoint> routePoints = new ArrayList<GeoPoint>();

            GeoPoint startPoint = new GeoPoint(mCurrentLocLatitude, mCurrentLocLongitude);
            try {
                routePoints.add(startPoint);
                Log.d("MapNavigation", "Current Latitude :: " + mCurrentLocLatitude);
                Log.d("MapNavigation", "Current Longitude :: " + mCurrentLocLongitude);

                GeoPoint endPoint = new GeoPoint(mDestinationLatitude, mDestinationLongitude);
                routePoints.add(endPoint);
                Log.d("MapNavigation", "Destination Latitude :: " + mDestinationLatitude);
                Log.d("MapNavigation", "Destination Longitude :: " + mDestinationLongitude);

                road = roadManager.getRoad(routePoints);
            }catch(Exception e) {
                writeLog(TAG +"  : doInBackground() " ,e);
            }

            return road;
        }

        @Override
        protected void onPostExecute(Road road) {
        try{
            progressDialog.dismiss();
            if (road.mStatus == Road.STATUS_OK) {

                map.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // This is required, otherwise mapview is crashing. Known issue in osmbonuspack
                map.setBuiltInZoomControls(true);
                map.setMultiTouchControls(true);
                map.setUseDataConnection(true);
                IMapController mapController = map.getController();
                mapController.setZoom(15);
                mapController.setCenter(new GeoPoint(mCurrentLocLatitude, mCurrentLocLongitude));

                // Using the Marker overlay for starting point
                Marker startMarker = new Marker(map);
                startMarker.setPosition(new GeoPoint(mCurrentLocLatitude, mCurrentLocLongitude));
                startMarker.setIcon(getResources().getDrawable(R.drawable.drawable_locationpin).mutate());
                startMarker.setTitle("Start point");
                startMarker.setOnMarkerDragListener(new OnMarkerDragListenerDrawer());
                map.getOverlays().add(startMarker);

                // Showing the Route steps on the map
                // Filling the bubbles
                FolderOverlay roadMarkers = new FolderOverlay();
                map.getOverlays().add(roadMarkers);
                Drawable nodeIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_node, null);
                for (int i=0; i<road.mNodes.size(); i++){
                    RoadNode node = road.mNodes.get(i);
                    Marker nodeMarker = new Marker(map);
                    nodeMarker.setPosition(node.mLocation);
                    nodeMarker.setIcon(nodeIcon);

                    nodeMarker.setTitle("Step " + i);
                    nodeMarker.setSnippet(node.mInstructions);
                    nodeMarker.setSubDescription(Road.getLengthDurationText(getApplicationContext(), node.mLength, node.mDuration));
                    Drawable iconContinue = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_continue_forward, null);
                    nodeMarker.setImage(iconContinue);
                    roadMarkers.add(nodeMarker);
                }


                //Using the Marker overlay for end point
                Marker endMarker = new Marker(map);
                endMarker.setPosition(new GeoPoint(mDestinationLatitude, mDestinationLongitude));
                endMarker.setTitle("End point");
                endMarker.setIcon(getResources().getDrawable(R.drawable.drawable_destination).mutate());
                endMarker.setOnMarkerDragListener(new OnMarkerDragListenerDrawer());
                map.getOverlays().add(endMarker);

                Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                map.getOverlays().add(roadOverlay);
                map.invalidate();

            } else if (road.mStatus == Road.STATUS_TECHNICAL_ISSUE) {
                Toast.makeText(context,context.getString(R.string.technical_issue_route), Toast.LENGTH_SHORT).show();
            } else if (road.mStatus > Road.STATUS_TECHNICAL_ISSUE) {
                Toast.makeText(context, context.getString(R.string.no_route), Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(road);
        }catch(Exception e) {
            writeLog(TAG +"  : onPostExecute() " ,e);
        }
        }
    }

    //0. Using the Marker and Polyline overlays - advanced options
    private class OnMarkerDragListenerDrawer implements Marker.OnMarkerDragListener {
        ArrayList<GeoPoint> mTrace;
        Polyline mPolyline;

        OnMarkerDragListenerDrawer() {
            try{
            mTrace = new ArrayList<GeoPoint>(100);
            mPolyline = new Polyline(map.getContext());
            mPolyline.setColor(0xAA0000FF);
            mPolyline.setWidth(2.0f);
            mPolyline.setGeodesic(true);
            map.getOverlays().add(mPolyline);
            }catch(Exception e) {
                writeLog(TAG +"  : OnMarkerDragListenerDrawer() " ,e);
            }
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            //mTrace.add(marker.getPosition());
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            try{
            mTrace.add(marker.getPosition());
            mPolyline.setPoints(mTrace);
            map.invalidate();
            }catch(Exception e) {
                writeLog(TAG +"  : onMarkerDragEnd() " ,e);
            }
        }

        @Override
        public void onMarkerDragStart(Marker marker) {

        }
    }
}
