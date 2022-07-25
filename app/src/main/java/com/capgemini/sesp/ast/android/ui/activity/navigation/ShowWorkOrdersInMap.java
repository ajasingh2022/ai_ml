package com.capgemini.sesp.ast.android.ui.activity.navigation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.capgemini.sesp.ast.android.BuildConfig;
import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.WorkorderCache;
import com.capgemini.sesp.ast.android.module.communication.LocationCaptureInterface;
import com.capgemini.sesp.ast.android.module.communication.LocationStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderListActivity;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


public class ShowWorkOrdersInMap extends OrderListActivity implements LocationStatusCallbackListener, LocationCaptureInterface, View.OnClickListener {


    private double mLatitude = 0.0d;
    private double mLongitude = 0.0d;
    private boolean mLocationChange = false;
    private MapView mMap;

    private ArrayList<String> selectedMarkers = null;
    private transient AlertDialog dialog = null;
    private GPSConnectCountDownTimer gpsConnectCountDownTimer = null;
    private ProgressDialog progressDialog;
    private ArrayList<WorkorderCustomWrapperTO> workorderCustomWrapperTOS;
    private RelativeLayout listPopup;
    ImageButton closeList;
    private FrameLayout transperentLayout;
    private GeoPoint currentLocation;
    private Marker currentLocationMarker;
    static String TAG = ShowWorkOrdersInMap.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invalidateOptionsMenu();
        supportInvalidateOptionsMenu();
        setContentView(R.layout.activity_show_work_orders_in_map);
        super.getSupportActionBar().hide();
        getSupportActionBar().setTitle(R.string.title_activity_map_navigation);
        fetchDBandSetMap(getIntent());
        TextView txtTitleBar = (TextView) findViewById(R.id.title_text);
        txtTitleBar.setText(getString(R.string.title_activity_maps));


        transperentLayout = (FrameLayout) findViewById(R.id.transperent_layout);
        transperentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeList();
            }
        });
        transperentLayout.setVisibility(View.GONE);


        if (workorderList == null)
            workorderList = new ArrayList<WorkorderLiteTO>();
        else
            workorderList.removeAll(workorderList);

        //Displaying maps.
        mMap = (MapView) findViewById(R.id.map);
        listPopup = (RelativeLayout) findViewById(R.id.list_popup);
        closeList = (ImageButton) findViewById(R.id.close_list);
        listPopup.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerview);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        populateData(this.getWorkorderList(), getItemTitles());

        closeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeList();
            }
        });


        selectedMarkers = new ArrayList<>();


        if (AndroidUtilsAstSep.getTileSourceURL() != null) {
            OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
            ITileSource SESPMapTiles = new XYTileSource("SespTiles", 0, 18, 256, ".png", AndroidUtilsAstSep.getTileSourceURL());
            //OnlineTileSourceBase SESPMapTiles = new XYTileSource("SespTiles", 0, 18, 256, ".png", new String[]{"http://a.tile.openstreetmap.org/", "http://b.tile.openstreetmap.org/", "http://c.tile.openstreetmap.org/"});
            Log.v("TileSource", "Setting SESP Map Tile Source");
            mMap.setTileSource(SESPMapTiles);
        } else {
            mMap.setTileSource(TileSourceFactory.MAPNIK);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            gpsConnectCountDownTimer.cancel();
        }catch (Exception e)
        {
            //
        }
    }

    private void closeList() {
        workorderList.removeAll(workorderList);
        selectedMarkers.removeAll(selectedMarkers);
        listPopup.setVisibility(View.GONE);
        transperentLayout.setVisibility(View.GONE);
        mMap.setClickable(true);

    }

    private void showProgressDialog() {
        try {
            progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
            // Load the custom title view
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customTitleLayout = inflater.inflate(R.layout.progress_bar_custom_layout, null);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCustomTitle(customTitleLayout);
            ((TextView) customTitleLayout.findViewById(R.id.progressVwId)).setText(R.string.loading);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        } catch (Exception e) {
            writeLog(TAG + " :showProgressDialog()  ", e);
        }
    }

    private void fetchDBandSetMap(final Intent intent) {
        try {
            //Start an async task to fetch Db contents and set data in onPostexecute method
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    showProgressDialog();
                }

                @Override
                protected Void doInBackground(Void... voids) {


                    ArrayList<String> idList = (ArrayList<String>) getIntent().getExtras().get("WO_LIST");

                    workorderCustomWrapperTOS = new ArrayList<>();

                    for (String s : idList) {
                        WorkorderCustomWrapperTO wo = WorkorderCache.getWorkorderByCaseId(Long.parseLong(s), ApplicationAstSep.workOrderClass);
                        workorderCustomWrapperTOS.add(wo);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    try {
                        progressDialog.dismiss();
                        setUpView();
                    }catch (Exception e)
                    {
                        Log.d(this.getClass().getSimpleName(),"Activity could be dead alread");
                    }

                }
            }.execute();
        } catch (Exception e) {
            writeLog(TAG + " :fetchDBandSetMap()  ", e);
        }
    }

    private void setUpView() {
        try {
            mMap.setBuiltInZoomControls(true);
            mMap.setMultiTouchControls(true);
            mMap.setUseDataConnection(true);

            IMapController mapController = mMap.getController();
            mapController.setZoom(5);
            currentLocation = new GeoPoint(mLatitude, mLongitude);

            final DatabaseHandler dbHandler = DatabaseHandler.createDatabaseHandler();

            Drawable locationPin;
            if (getIntent().getLongExtra("CASE_T", -12345) < 0) {
                locationPin = getResources().getDrawable(R.drawable.ic_place_red_24dp);
            } else {
                locationPin = getResources().getDrawable(R.drawable.ic_place_pink_24dp).mutate();

            }


            //Drawing the marker on the map to point the required position

            Double minLat = Double.valueOf(Integer.MAX_VALUE);
            Double maxLat = Double.valueOf(Integer.MIN_VALUE);
            Double minLong = Double.valueOf(Integer.MAX_VALUE);
            Double maxLong = Double.valueOf(Integer.MIN_VALUE);
            for (final WorkorderCustomWrapperTO workorderCustomWrapperTO : workorderCustomWrapperTOS) {
                if (workorderCustomWrapperTO.getWgs84Latitude() != null && workorderCustomWrapperTO.getWgs86Longitude() != null) {

                    Double mCurrentLocLatitude = Double.valueOf(workorderCustomWrapperTO.getWgs84Latitude());
                    Double mCurrentLocLongitude = Double.valueOf(workorderCustomWrapperTO.getWgs86Longitude());

                    if (mCurrentLocLatitude > maxLat)
                        maxLat = mCurrentLocLatitude;
                    if (mCurrentLocLatitude < minLat)
                        minLat = mCurrentLocLatitude;
                    if (mCurrentLocLongitude > maxLong)
                        maxLong = mCurrentLocLongitude;
                    if (mCurrentLocLongitude < minLong)
                        minLong = mCurrentLocLongitude;

                    Drawable locationPinTmp = locationPin.getConstantState().newDrawable().mutate();
                    Marker startMarker = new Marker(mMap);
                    startMarker.setPosition(new GeoPoint(mCurrentLocLatitude, mCurrentLocLongitude));
                    if (workorderCustomWrapperTO.getTimeReservationStart() != null) {
                        locationPinTmp = getResources().getDrawable(R.drawable.ic_person_pin_circle_red_24dp);
                        locationPinTmp.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    }
                    startMarker.setIcon(locationPinTmp);
                    startMarker.setTitle(workorderCustomWrapperTO.getIdCase().toString());
                    startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker, MapView mapView) {

                            if (selectedMarkers == null)
                                selectedMarkers = new ArrayList<>();
                            selectedMarkers.add(marker.getTitle());
                            mMap.setClickable(false);
                            workorderList.add(dbHandler.generateWorkorderLiteTo(workorderCustomWrapperTO));
                            populateData(workorderList, getItemTitles());

                            try {
                                getWorkOrderAddressAdapter().notifyDataSetChanged();
                                getWorkOrderCustomerAdapter().notifyDataSetChanged();
                                getWorkOrderMeterAdapter().notifyDataSetChanged();
                                getWorkOrderKeyAdapter().notifyDataSetChanged();
                                getWorkOrderSLAAdapter().notifyDataSetChanged();
                                getWorkOrderTimeReservationAdapter().notifyDataSetChanged();
                            } catch (Exception e) {
                                writeLog(TAG + " :setUpView()  ", e);
                            }

                            if (listPopup.getVisibility() != View.VISIBLE) {
                                transperentLayout.setVisibility(View.VISIBLE);
                                listPopup.setVisibility(View.VISIBLE);
                            }
                            //doSomething();
                            return false;

                        }
                    });
                    mMap.getOverlays().add(startMarker);

                }
            }

            if (currentLocation.getLatitude() > maxLat)
                maxLat = currentLocation.getLatitude();
            if (currentLocation.getLatitude() < minLat)
                minLat = currentLocation.getLatitude();
            if (currentLocation.getLongitude() > maxLong)
                maxLong = currentLocation.getLongitude();
            if (currentLocation.getLongitude() < minLong)
                minLong = currentLocation.getLongitude();


            BoundingBox boundingBox = new BoundingBox(maxLat, maxLong, minLat, minLong);
            mMap.zoomToBoundingBox(boundingBox, true);

            Drawable markerr = getResources().getDrawable(org.osmdroid.bonuspack.R.drawable.marker_cluster);
            Bitmap bitmap = ((BitmapDrawable) markerr).getBitmap();
            // Scale it to 50 x 50
            Drawable marker = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 25, 25, true));
            // Set your new, scaled drawable "d"


            currentLocationMarker = new Marker(mMap);
            currentLocationMarker.setPosition(currentLocation);
            currentLocationMarker.setIcon(marker);
            currentLocationMarker.setTitle("Current_Location");
            currentLocationMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    return false;
                }
            });
            mMap.getOverlays().add(currentLocationMarker);
        } catch (Exception e) {
            writeLog(TAG + " :showProgressDialog()  ", e);
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

    }

    /**
     * If the gps is off, dialog will be displayed to turn on
     */
    private void fetchCurrentLocation() {
        try {
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
                    } catch (Exception iox) {
                        writeLog(TAG + "  : fetchCurrentLocation()", iox);
                    }


                    gpsConnectCountDownTimer = new GPSConnectCountDownTimer(Integer.parseInt(connectionTimeOut) * 1000, 1000);
                    gpsConnectCountDownTimer.start();

                    regForLocationChangeInfo(this, LocationManager.GPS_PROVIDER);
                }

            } else {

                alertView = inflater.inflate(R.layout.gps_not_enabled_layout, null);

                if (alertView != null) {
                    final Switch turnGpsYesNoSwitch = alertView.findViewById(R.id.turnGpsYesNoSwitch);
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
        } catch (Exception e) {
            writeLog(TAG + " :fetchCurrentLocation()  ", e);
        }
    }


    /**
     * Handling click events
     *
     * @param v
     */
    public void onClick(View v) {
        try {
            if (v != null && v.getId() == R.id.cancelButton && dialog != null) {
                dialog.dismiss();
                this.finish();   //If user clicks "cancel" button in dialog then finishing the activity
            } else if (v != null && (v.getId() == R.id.navigate_btn)) {
                AndroidUtilsAstSep.navigateTo(this, String.valueOf(mLatitude), String.valueOf(mLongitude));
            } else if (v != null && (v.getId() == R.id.gpsOkButton)) {
                if (dialog != null)
                    dialog.dismiss();
                this.finish();
            }
        } catch (Exception e) {
            writeLog(TAG + " :onClick()  ", e);
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
        try {
            // Stop the listener. Provider will not be used in this case.
            final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeUpdates(this);
        } catch (Exception e) {
            writeLog(TAG + " :unregForLocationChangeInfo()  ", e);
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
        try {
            if (callBack != null && !Utils.safeToString(provider).equals("")) {
                boolean shouldRequestNewLoc = false;
                AndroidUtilsAstSep.requestLocationByProviders(this, this, shouldRequestNewLoc);
            }
        } catch (Exception e) {
            writeLog(TAG + " :regForLocationChangeInfo()  ", e);
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
            try {
                dialog.dismiss();
            }catch (Exception e)
            {
                //Can be ignored
            }

            gpsConnectCountDownTimer.cancel();
        }catch (Exception e)
        {
            Log.d(this.getClass().getSimpleName(),"It could already been canceled");
        }
        try {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            currentLocation = new GeoPoint(mLatitude, mLongitude);

            if (mMap != null && currentLocationMarker != null && currentLocation != null) {
                mMap.getOverlays().remove(currentLocationMarker);
                currentLocationMarker.setPosition(currentLocation);
                mMap.getOverlays().add(currentLocationMarker);

            }


            if (location != null) {

                if ((mLatitude == 0.0d && mLongitude == 0.0d)) {

                    mLocationChange = true;
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
                Toast.makeText(this, this.getString(R.string.no_location), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            writeLog(TAG + " :onLocationChanged()  ", e);
        }
    }

    /**
     * Display alert message if GPS is not connected within certain time (as defined in rsp.properties file)
     */
    public void showGPSNotConnectionMsg() {
        try {
            final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertView = inflater.inflate(R.layout.gps_connection_fail_msg_layout, null);

            if (alertView != null) {

                final Button gpsOkButton = (Button) alertView.findViewById(R.id.gpsOkButton);
                if (gpsOkButton != null) {
                    gpsOkButton.setOnClickListener(this);
                }
                dialog = GuiController.getCustomAlertDialog(this, alertView, null, null).create();
                dialog.show();

            }
        } catch (Exception e) {
            writeLog(TAG + " :showGPSNotConnectionMsg()  ", e);
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
            try {
                if (dialog != null)
                    dialog.dismiss();
                dialog = null;
                showGPSNotConnectionMsg();
            } catch (Exception e) {
                writeLog(TAG + " :onFinish()  ", e);
            }
        }

    }

}
