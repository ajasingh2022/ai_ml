package com.capgemini.sesp.ast.android.module.tsp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.skvader.rsp.cft.common.to.cft.table.SystemParameterTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.ApplicationAstSep.context;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<WorkOrder>,
        ClusterManager.OnClusterInfoWindowClickListener<WorkOrder>,
        ClusterManager.OnClusterItemClickListener<WorkOrder>,
        ClusterManager.OnClusterItemInfoWindowClickListener<WorkOrder> {

    public GoogleMap mMap;
    public double zoomLevel;
    ArrayList<WorkOrder> workOrders;
    private ClusterManager<WorkOrder> mClusterManager;
    SESPClusteringAlgorithm SESPClusteringAlgorithm;
    ArrayList<LatLng> mWayPoints = new ArrayList<>();
    SESPClusterRenderer sespClusterRenderer = null;
    static float mDensity;
    Polyline polyline = null;
    WorkOrder mSelectedOrder;
    SESPCluster mSelectedCluster;
    Location mMyLocation;
    TextView infoView;
    BottomSheetBehavior bottomSheetBehavior;
    ArrayList<WorkOrder> returnValues;

    ArrayList<ArrayList> polylines = null;// index 0 ->from index 1-->To index 2 -->road index 3->Polyline


    ArrayList<WorkOrder> allSelectedWos, wosWIthSLA, wosWithTR;
    private double mRange;

    FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        polylines = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        infoView = findViewById(R.id.zoomInfo);

        FloatingActionButton buttonRoute = findViewById(R.id.findRoute);
        buttonRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    launchGMap(returnValues.get(0).latLng);
                } catch (Exception e) {
                    Toast.makeText(MapsActivity.this, "Not Optimized Yet", Toast.LENGTH_LONG).show();
                }

            }
        });

        mapFragment.getMapAsync(this);
        mDensity = context.getResources().getDisplayMetrics().density;
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet));


        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //bottomSheetHeading.setText("Collapse Me");
                } else {
                    //bottomSheetHeading.setText("Expand Me");
                }

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createCustomizeDialog();

    }


    private void showRoads(ArrayList<WorkOrder> returnValues, Compute compute) {

        for (int i = 0; i < returnValues.size(); i++) {
            LatLng workOrderC = null, workOrderN = null;
            if (i == 0) {
                workOrderC = new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude());
            } else {
                workOrderC = returnValues.get(i - 1).latLng;
            }
            workOrderN = returnValues.get(i).latLng;
            TspUtil tspUtil = new TspUtil();
            tspUtil.findRouteBetween(workOrderC, workOrderN);
        }
    }

    private void ShowWorkOrderSequence(ArrayList<WorkOrder> sequenceWorkOrders) {

        if (sequenceWorkOrders.size() > 0) {
            RecyclerView recyclerView = findViewById(R.id.woSequence);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(MapsActivity.this));
            WorkOrderSequenceAdater workOrderSequenceAdater = new WorkOrderSequenceAdater(MapsActivity.this, sequenceWorkOrders);
            recyclerView.setAdapter(workOrderSequenceAdater);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Result!");
            WebView msgView = new WebView(this);
            String msg = getResources().getString(R.string.no_optimization_done_msg);

            msg = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>"+msg+ "</body></html>";
                    msgView.loadData(msg, "text/html", "UTF-8");

            builder.setView(msgView)
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            findViewById(R.id.woSequence).setVisibility(View.GONE);
        }

    }

    private void createCustomizeDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") final View alertLayout = inflater.inflate(R.layout.samle, null);

        EditText eReminderTime = alertLayout.findViewById(R.id.timeEnd);
        eReminderTime.setText(SESPPreferenceUtil.getPreferenceString("END_TIME"));
        eReminderTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MapsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        eReminderTime.setText((selectedHour > 9 ? selectedHour : "0" + selectedHour) + ":"
                                + (selectedMinute > 9 ? selectedMinute : "0" + selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        builder.setPositiveButton("OK", null);


        builder.setView(alertLayout);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //validate
                        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        String[] hour_minute = eReminderTime.getText().toString().split(":");

                        try {
                            if (Integer.parseInt(hour_minute[0]) > hour) {

                                SESPPreferenceUtil.savePreference("END_TIME", eReminderTime.getText().toString());
                                //valid
                                dialog.dismiss();
                            } else {
                                throw new IllegalArgumentException();
                            }
                        } catch (Exception e) {
                            alertDialog
                                    .getWindow()
                                    .getDecorView()
                                    .animate()
                                    .translationX(16f)
                                    .setInterpolator(new CycleInterpolator(7f));

                            Toast.makeText(context, "Make valid Selection", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        //noinspection ConstantConditions
        alertDialog.show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        new AsyncTask<Object, Object, Object>() {

            ProgressDialog progressDialog;

            @Override
            protected Object doInBackground(Object[] objects) {

                mRange = Double.parseDouble(ObjectCache.getIdObject(SystemParameterTO.class,
                        AndroidUtilsAstSep.CONSTANTS().SYSTEM_PARAMETER__CLUSTER_DIAMETER).getValue());
                workOrders = (ArrayList<WorkOrder>) TspUtil.popWosToList();
                return null;
            }

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(MapsActivity.this);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Map View Loading,Please wait");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {

                }
                doClustering();
                customizeMapView();
                fixZoom();
            }
        }.execute();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mMyLocation = location;

            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void customizeMapView() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMyLocationEnabled(true);
        int padding = 100;
        mMap.setPadding(padding, padding, padding, padding);

    }

    private void getMyLocation() {
        LatLng latLng = new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        mMap.animateCamera(cameraUpdate);
    }


    private void doClustering() {

        mClusterManager = new ClusterManager<WorkOrder>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(workOrders.get(0).latLng));
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);

        sespClusterRenderer = new SESPClusterRenderer(this, mMap, mClusterManager);
        sespClusterRenderer.setOnClusterClickListener(this);

        mClusterManager.setRenderer(sespClusterRenderer);
        mClusterManager.addItems(workOrders);
        SESPClusteringAlgorithm = new SESPClusteringAlgorithm<WorkOrder>(this, mRange);
        mClusterManager.setAlgorithm(SESPClusteringAlgorithm);


        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<WorkOrder>() {
                    @Override
                    public boolean onClusterClick(Cluster<WorkOrder> cluster) {
                        //mSelectedCluster = (SESPCluster) cluster;
                        //selectCluster(mSelectedCluster);
                        showClusterDetails((SESPCluster<WorkOrder>) cluster);
                        return false;
                    }
                });

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<WorkOrder>() {
            @Override
            public boolean onClusterItemClick(WorkOrder workOrder) {
                SESPCluster sespCluster = new SESPCluster<>(workOrder.latLng);
                sespCluster.add(workOrder);
                showClusterDetails(sespCluster);
                return false;
            }
        });

        mClusterManager.setOnClusterInfoWindowClickListener(new ClusterManager.OnClusterInfoWindowClickListener<WorkOrder>() {
            @Override
            public void onClusterInfoWindowClick(Cluster<WorkOrder> cluster) {
                showClusterDetails((SESPCluster<WorkOrder>) cluster);
            }
        });

        mClusterManager
                .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<WorkOrder>() {
                    @Override
                    public boolean onClusterItemClick(WorkOrder item) {

                        ArrayList arrayList = new ArrayList<WorkOrder>();
                        arrayList.add(item);
                        showSelectionDetails(arrayList);
                        mSelectedOrder = item;
                        return false;
                    }
                });

        SESPClusteringAlgorithm.formClusters();

        TspUtil.mMapsActivity = this;
        TspUtil.mContext = this.getApplicationContext();
        TspUtil.mMap = mMap;
        //Compute.fillDistancematrixAmongClusters(new ArrayList<>(SESPClusteringAlgorithm.clustersAtHigh));

    }

    private void showClusterDetails(SESPCluster<WorkOrder> cluster) {

        //configure Dialogue
        final Dialog dialog = new Dialog(this);

        dialog.setTitle("Cluster Details");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels * 0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels * 0.7f);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = widthLcl;
        lp.height = heightLcl;
        dialog.getWindow().setAttributes(lp);

        float timeForCluster = Compute.timeTakenForCluster(cluster);
        View view = getLayoutInflater().inflate(R.layout.dialog_cluster_details, null);

        RecyclerView lv = (RecyclerView) view.findViewById(R.id.custom_list);
        lv.setMinimumWidth(widthLcl);
        lv.setLayoutManager(new LinearLayoutManager(MapsActivity.this));
        CustomListAdapterDialog clad = new CustomListAdapterDialog(MapsActivity.this, (ArrayList<WorkOrder>) cluster.getItems());
        lv.setAdapter(clad);

        TextView totalTime = view.findViewById(R.id.totalTime);

        totalTime.setText("Time estimation for completing the cluster " + String.valueOf(timeForCluster) + " " + getResources().getString(R.string.minute));

        Button buttonSelect = view.findViewById(R.id.buttonSelect);
        buttonSelect.setText("Select");
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCluster(cluster);
                Compute.sortOnSLA((List<WorkOrder>) cluster.getItems());
                Compute.sortOnTimeReservationEnd((List<WorkOrder>) cluster.getItems());
                popTable(cluster);
                dialog.dismiss();
            }
        });
        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (cluster.isSelected()) {
            buttonSelect.setText("Unselect");
        }


        dialog.setContentView(view);

        dialog.show();
        clad.notifyDataSetChanged();

    }


    private void fixZoom() {

        LatLngBounds.Builder bc = new LatLngBounds.Builder();

        for (WorkOrder workOrder : workOrders) {
            bc.include(workOrder.latLng);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 10));
    }


    public boolean selectCluster(Cluster<WorkOrder> cluster) {

        SESPCluster sespCluster = (SESPCluster) cluster;

        if (!sespCluster.isSelected()) {
            sespCluster.setSelected(true);
            mWayPoints.add(sespCluster.getPosition());
        } else {
            sespCluster.setSelected(false);
            mWayPoints.remove(sespCluster.getPosition());
        }

        sespClusterRenderer.forced = true;
        mClusterManager.cluster();
        return false;
    }

    private void popTable(SESPCluster<WorkOrder> cluster) {

        LayoutInflater layoutInflater = this.getLayoutInflater();

        if (allSelectedWos == null) {
            allSelectedWos = new ArrayList<>();
        }
        if (wosWithTR == null) {
            wosWithTR = new ArrayList<>();
        }
        if (wosWIthSLA == null) {
            wosWIthSLA = new ArrayList<>();
        }

        if (cluster.isSelected()) {
            allSelectedWos.addAll(cluster.getItems());
            for (WorkOrder workOrder : cluster.getItems()) {
                if (!(workOrder.workorderLiteTO.getTimeReservationEnd() == null)) {
                    wosWithTR.add(workOrder);
                }
                if (!workOrder.sla.contains("null")) {
                    wosWIthSLA.add(workOrder);
                }
            }
        } else {
            allSelectedWos.removeAll(cluster.getItems());
            ArrayList sla = new ArrayList();
            ArrayList tr = new ArrayList();
            for (WorkOrder workOrder : cluster.getItems()) {
                if (!(workOrder.workorderLiteTO.getTimeReservationEnd() == null)) {
                    tr.add(workOrder);
                }
                if (!workOrder.sla.contains("null")) {
                    sla.add(workOrder);
                }
            }
            wosWIthSLA.removeAll(sla);
            wosWithTR.removeAll(tr);
        }

        TableLayout tableLayout = findViewById(R.id.selectedWosTable);

        if (allSelectedWos.size() > 0) {
            tableLayout.setVisibility(View.VISIBLE);
            TextView totalCount = tableLayout.findViewById(R.id.totalCount);
            totalCount.setText(String.valueOf(allSelectedWos.size()));

            TextView slaCount = tableLayout.findViewById(R.id.slaCount);
            slaCount.setText(String.valueOf(wosWIthSLA.size()));

            TextView trCount = tableLayout.findViewById(R.id.trCount);
            trCount.setText(String.valueOf(wosWithTR.size()));

            TableRow tableRowAllWO = tableLayout.findViewById(R.id.allwosSelected);
            tableRowAllWO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectionDetails(allSelectedWos);
                }
            });

            TableRow tableRowSLAWO = tableLayout.findViewById(R.id.slaWos);
            tableRowSLAWO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectionDetails(wosWIthSLA);
                }
            });

            TableRow tableRowTRWO = tableLayout.findViewById(R.id.trWos);
            tableRowTRWO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectionDetails(wosWithTR);
                }
            });


        } else {
            tableLayout.setVisibility(View.GONE);
        }

    }

    private void showSelectionDetails(ArrayList<WorkOrder> workOrders) {

        //configure Dialogue
        final Dialog dialog = new Dialog(this);

        dialog.setTitle("Details");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels * 0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels * 0.7f);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = widthLcl;
        lp.height = heightLcl;
        dialog.getWindow().setAttributes(lp);

        View view = getLayoutInflater().inflate(R.layout.dialog_cluster_details, null);

        RecyclerView lv = (RecyclerView) view.findViewById(R.id.custom_list);
        lv.setMinimumWidth(widthLcl);
        lv.setLayoutManager(new LinearLayoutManager(MapsActivity.this));
        CustomListAdapterDialog clad = new CustomListAdapterDialog(MapsActivity.this, (ArrayList<WorkOrder>) workOrders);
        lv.setAdapter(clad);

        TextView totalTime = view.findViewById(R.id.totalTime);
        totalTime.setVisibility(View.GONE);

        Button buttonSelect = view.findViewById(R.id.buttonSelect);
        buttonSelect.setText("Dismiss");
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setVisibility(View.GONE);

        dialog.setContentView(view);

        dialog.show();
        clad.notifyDataSetChanged();

    }


    @Override
    public boolean onClusterItemClick(WorkOrder workOrder) {
        return false;
    }


    @Override
    public void onClusterInfoWindowClick(Cluster<WorkOrder> cluster) {

    }

    @Override
    public void onClusterItemInfoWindowClick(WorkOrder workOrder) {

        ArrayList arrayList = new ArrayList<WorkOrder>();
        arrayList.add(workOrder);
        showSelectionDetails(arrayList);

    }

    @Override
    public boolean onClusterClick(Cluster<WorkOrder> cluster) {
        return false;
    }

    public void updateRoad(WorkOrder workOrder, boolean selected) {


        for (ArrayList arrayList : polylines) {

            if (arrayList.get(1).equals(workOrder.latLng)) {
                ((Polyline) arrayList.get(3)).remove();
                TspUtil tspUtit = new TspUtil();
                arrayList.add(3, tspUtit.drawPolyline((List<List<HashMap<String, String>>>) arrayList.get(2), selected));
                break;
            }

        }

    }

    public class CustomListAdapterDialog extends RecyclerView.Adapter<CustomListAdapterDialog.ViewHolder> {

        private ArrayList<WorkOrder> listData;

        private LayoutInflater layoutInflater;

        int EXPANDED = 0;
        int COLLAPSED = 1;

        int selectedPosition = -1;

        public CustomListAdapterDialog(Context context, ArrayList<WorkOrder> listData) {
            this.listData = listData;
            layoutInflater = LayoutInflater.from(context);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View listItem = layoutInflater.inflate(R.layout.dialogue_list_row, parent, false);
            LinearLayout linearLayout = listItem.findViewById(R.id.expandedView);


            if (viewType == EXPANDED) {
                linearLayout.setVisibility(View.VISIBLE);
            }

            if (viewType == COLLAPSED) {
                linearLayout.setVisibility(View.GONE);
            }

            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(CustomListAdapterDialog.ViewHolder holder, int position) {

            if (position == selectedPosition) {
                holder.exandableView.setVisibility(View.VISIBLE);
                holder.casiId.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
            } else {
                holder.casiId.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
                holder.exandableView.setVisibility(View.GONE);
            }


            WorkOrder workOrder = listData.get(position);
            if (workOrder.workorderLiteTO.getTimeReservationStart() == null) {
                holder.timeReservation.setText("N/A");
            } else {
                holder.timeReservation.setText(String.valueOf(workOrder.workorderLiteTO.getTimeReservationStart()) + "-"
                        + String.valueOf(workOrder.workorderLiteTO.getTimeReservationEnd()));
            }
            holder.sla.setText(workOrder.sla.contains("null") ? "N/A" : workOrder.sla);
            holder.casiId.setText(String.valueOf(workOrder.workorderLiteTO.getId()));
            holder.casiId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.exandableView.getVisibility() == View.VISIBLE) {
                        holder.exandableView.setVisibility(View.GONE);
                        selectedPosition = -1;
                    } else {
                        holder.exandableView.setVisibility(View.VISIBLE);
                        selectedPosition = position;
                    }
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            if (selectedPosition == position) {
                return EXPANDED;
            } else
                return COLLAPSED;
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView casiId;
            TextView sla;
            TextView timeReservation;
            LinearLayout exandableView;

            public ViewHolder(View itemView) {
                super(itemView);
                casiId = itemView.findViewById(R.id.wo_id_text);
                sla = itemView.findViewById(R.id.sla);
                timeReservation = itemView.findViewById(R.id.time_reservation);
                exandableView = itemView.findViewById(R.id.expandedView);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.optimize_route) {
            {
                returnValues = null;
                if (!(mWayPoints.size() > 0)) {
                    Toast.makeText(MapsActivity.this, "Select at least 1 cluster", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    Toast.makeText(MapsActivity.this, "Finding Route", Toast.LENGTH_SHORT).show();
                    Compute compute = new Compute();
                    DownloadCallBack downloadCallBack = new DownloadCallBack() {
                        @Override
                        public void DownloadCallBackListener(String returnValue) {

                        }

                        @Override
                        public void ComputeCallBack(ArrayList<WorkOrder> returnValues) {

                            Collections.sort(returnValues, new Comparator<WorkOrder>() {
                                @Override
                                public int compare(WorkOrder o1, WorkOrder o2) {
                                    return o1.getEtc().compareTo(o2.getEtc());
                                }
                            });
                            MapsActivity.this.returnValues = returnValues;
                            ShowWorkOrderSequence(returnValues);
                            ShowNotConsideredWorkOrders();
                            if (polylines != null) {
                                for (ArrayList arrayList : polylines) {
                                    ((Polyline) arrayList.get(3)).remove();
                                }
                            }
                            polylines.clear();
                            showRoads(returnValues, compute);

                        }

                    };
                    compute.sequenceWorkOrders(new ArrayList<>(SESPClusteringAlgorithm.clustersAtHigh), downloadCallBack);

                }
            }
        } else if (item.getItemId() == R.id.legend) {
            showLegendDetails();

        }
        return true;
    }

    private void showLegendDetails() {

        //configure Dialogue
        final Dialog dialog = new Dialog(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels * 0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels * 1f);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = widthLcl;
        lp.height = heightLcl;
        dialog.getWindow().setAttributes(lp);

        View view = getLayoutInflater().inflate(R.layout.dialog_legend_details, null);

        RecyclerView lv = (RecyclerView) view.findViewById(R.id.legend_color);
        lv.setMinimumWidth(widthLcl);
        lv.setLayoutManager(new LinearLayoutManager(MapsActivity.this));
        LegendAdapter clad = new LegendAdapter(MapsActivity.this);
        lv.setAdapter(clad);

        TextView title = view.findViewById(R.id.title);

        ((TextView) (view.findViewById(R.id.title))).setText("Legend Description");

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
        clad.notifyDataSetChanged();


    }

    private void ShowNotConsideredWorkOrders() {

        ArrayList<SESPCluster> allWos = new ArrayList<SESPCluster>(SESPClusteringAlgorithm.clustersAtHigh);
        ArrayList<WorkOrder> workOrders = new ArrayList<>();

        for (SESPCluster clusterItem : allWos) {

            if (((SESPCluster) clusterItem).isSelected()) {
                workOrders.addAll((ArrayList<WorkOrder>) ((SESPCluster) clusterItem).getItems());
            }
        }

        RecyclerView lv = (RecyclerView) findViewById(R.id.excludedwos);
        lv.setLayoutManager(new LinearLayoutManager(MapsActivity.this));

        workOrders.removeAll(returnValues);

        CustomListAdapterDialog clad = new CustomListAdapterDialog(MapsActivity.this, workOrders);
        lv.setAdapter(clad);
        clad.notifyDataSetChanged();

    }

    public void launchGMap(LatLng dest_address) {
        final Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?"
                        + "saddr=" + mMyLocation.getLatitude() + "," + mMyLocation.getLongitude() + "&daddr="
                        + dest_address.latitude + "," + dest_address.longitude));

        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

        startActivity(intent);
    }
}

