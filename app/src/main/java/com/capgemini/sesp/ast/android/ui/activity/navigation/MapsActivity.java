package com.capgemini.sesp.ast.android.ui.activity.navigation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.BuildConfig;
import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.LocationCaptureInterface;
import com.capgemini.sesp.ast.android.module.communication.LocationStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.cft.common.util.Utils;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

//import org.osmdroid.DefaultResourceProxyImpl;
//import org.osmdroid.ResourceProxy;

/**
 * This activity will open the open street maps which displays the location based on the coordinates provided by work order
 *
 * @author Umesh
 * @version 1.0
 */

@SuppressLint("InflateParams")
public class MapsActivity extends AppCompatActivity implements LocationStatusCallbackListener, LocationCaptureInterface, View.OnClickListener {


    private MapOverlay mMapOverlay;
    private double mLatitude = 0.0d;
    private double mLongitude = 0.0d;
    private boolean mLocationChange = false;
    private MapView mMap;

    private transient AlertDialog dialog = null;
    private Button navigateBtn;
    private GPSConnectCountDownTimer gpsConnectCountDownTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setTitle(R.string.title_activity_map_navigation);

        //Check if we are getting latitude and longitude from calling activity
        if (getIntent().getExtras() != null) {

            mLatitude = getIntent().getExtras().getDouble("customerLatitude");
            mLongitude = getIntent().getExtras().getDouble("customerLongitude");
        }

        navigateBtn = findViewById(R.id.navigate_btn);
        navigateBtn.setOnClickListener(this);
        //Navigate feature is not needed in Maps activity as this is just to display the Map
        navigateBtn.setVisibility(View.GONE);

        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(getString(R.string.title_activity_maps));

        //Displaying maps.
        mMap = findViewById(R.id.map);
        if (AndroidUtilsAstSep.getTileSourceURL() != null) {
            OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
            ITileSource SESPMapTiles = new XYTileSource("SespTiles", 0, 18, 256, ".png", AndroidUtilsAstSep.getTileSourceURL());
            //OnlineTileSourceBase SESPMapTiles = new XYTileSource("SespTiles", 0, 18, 256, ".png", new String[]{"http://a.tile.openstreetmap.org/", "http://b.tile.openstreetmap.org/", "http://c.tile.openstreetmap.org/"});
            Log.v("TileSource", "Setting SESP Map Tile Source");
               /* TileSourceFactory.addTileSource(SESPMapTiles);
                TileSourceFactory.getTileSources();
                map.setTileSource(TileSourceFactory.getTileSource("SespTiles"));*/
            mMap.setTileSource(SESPMapTiles);
        } else {
            mMap.setTileSource(TileSourceFactory.MAPNIK);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Calling only once. If the location is found then no need to call service again
        if (!mLocationChange) {

            //If lat and lang are not received from calling activity then find out current location other wise show the map
            if (mLatitude == 0.0d && mLongitude == 0.0d) {
                fetchCurrentLocation();
            } else
                showMap();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        unregForLocationChangeInfo(this, null);
    }

    private void showMap() {

        mMap.setBuiltInZoomControls(true);
        mMap.setMultiTouchControls(true);
        mMap.setUseDataConnection(true);

        IMapController mapController = mMap.getController();
        mapController.setZoom(18);
        GeoPoint startPoint = new GeoPoint(mLatitude, mLongitude);
        mapController.setCenter(startPoint);

        //Drawing the marker on the map to point the required position

        Drawable marker = getResources().getDrawable(R.drawable.drawable_locationpin);
        marker.setBounds(0, 20, 20, 0);
        //ResourceProxy resourceProxy = new DefaultResourceProxyImpl(this);
        //mMapOverlay = new MapOverlay(marker, resourceProxy);
        mMapOverlay = new MapOverlay(marker);
        mMap.getOverlays().add(mMapOverlay);
        mMapOverlay.addItem(startPoint, "StartPoint", "Location");

    }

    /**
     * If the gps is off, dialog will be displayed to turn on
     */
    private void fetchCurrentLocation() {

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
                String connectionTimeOut = null;
                try {
                    connectionTimeOut = AndroidUtilsAstSep.getProperty("connectionTimeOut", getApplicationContext());
                } catch (IOException iox) {
                    writeLog("MapsActivity  : fetchCurrentLocation()", iox);
                }


                gpsConnectCountDownTimer = new GPSConnectCountDownTimer(Integer.parseInt(connectionTimeOut) * 1000, 1000);
                gpsConnectCountDownTimer.start();

                regForLocationChangeInfo(this, LocationManager.GPS_PROVIDER);
            }

        } else {

            alertView = inflater.inflate(R.layout.gps_not_enabled_layout, null);

            if (alertView != null) {
                //final Button okButton = (Button) alertView.findViewById(R.id.okButton);
                //final Button turnGpsNoButton = (Button) alertView.findViewById(R.id.turnGpsNoButton);
                final Switch turnGpsYesNoSwitch = alertView.findViewById(R.id.turnGpsYesNoSwitch);
/*
                if (okButton != null) {
                    okButton.setOnClickListener(this);
                }

                if (turnGpsNoButton != null) {
                    turnGpsNoButton.setOnClickListener(this);
                }
*/

                if (turnGpsYesNoSwitch != null) {

                    turnGpsYesNoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {
                                dialog.dismiss();
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            } else {
                                dialog.dismiss();
                                gpsConnectCountDownTimer.cancel();
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
/*
        if (v != null && v.getId() == R.id.okButton && dialog != null) {
            dialog.dismiss();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
*/
        if (v != null && v.getId() == R.id.cancelButton && dialog != null) {
            try {
                dialog.dismiss();
                gpsConnectCountDownTimer.cancel();
            } catch (Exception e) {
                writeLog("MapsActivity  : onClick()", e);
            }
            MapsActivity.this.finish();   //If user clicks "cancel" button in dialog then finishing the activity
        } else if (v != null && (v.getId() == R.id.navigate_btn)) {
            AndroidUtilsAstSep.navigateTo(this, String.valueOf(mLatitude), String.valueOf(mLongitude));
        } else if (v != null && (v.getId() == R.id.gpsOkButton)) {
            dialog.dismiss();
            MapsActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
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
            AndroidUtilsAstSep.requestLocationByProviders(this, this, shouldRequestNewLoc);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null) {

                if ((mLatitude == 0.0d && mLongitude == 0.0d)) {

                    mLocationChange = true;

                    gpsConnectCountDownTimer.cancel();

                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    // Disable GPS tracking immediately to save power
                    unregForLocationChangeInfo(this, null);

                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();

                    //Display map with lat and long
                    showMap();
                }

            } else {
                Toast.makeText(MapsActivity.this, this.getString(R.string.no_location), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            writeLog("MapsActivity  : onLocationChanged()", e);
        }
    }

    /**
     * Display alert message if GPS is not connected within certain time (as defined in rsp.properties file)
     */
    public void showGPSNotConnectionMsg() {

        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertView = inflater.inflate(R.layout.gps_connection_fail_msg_layout, null);

        if (alertView != null) {

            final Button gpsOkButton = alertView.findViewById(R.id.gpsOkButton);
            if (gpsOkButton != null) {
                gpsOkButton.setOnClickListener(this);
            }
            dialog = GuiController.getCustomAlertDialog(this, alertView, null, null).create();
            dialog.show();

        }
    }

    /**
     * Define count down timer class
     */
    public class GPSConnectCountDownTimer extends CountDownTimer {

        public GPSConnectCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
            showGPSNotConnectionMsg();
        }

    }

}
