package com.capgemini.sesp.ast.android.ui.activity.near_by_installations;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.skvader.rsp.ast_sep.common.to.custom.NearByWorkOrder;


import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.END;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class NearByInstallationsActivity extends AppCompatActivity implements OnMapReadyCallback{

    public static String RANGE = "RANGE";
    private GoogleMap mMap;
    public  List<NearByWorkOrder> nearByWorkOrders = new ArrayList<>();
    private List<NearByInstallationClusterItem> nearByInstallationClusterItems = null ;
    private List<NearByWorkOrder> selectedNearByWorkOrders = new ArrayList<>();
    private Context mContext;
    private InstallationDetailsAdapter installationDetailsAdapter;
    private RecyclerView recyclerView;
    private ClusterManager<NearByInstallationClusterItem> clusterManager;
    private NearByInstallationClusterRenderer nearByInstallationClusterRenderer ;
    private Location lastKnownlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_installations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void addMarkersOnMap() throws Exception {
        mContext = getApplicationContext();
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        nearByInstallationClusterItems = new ArrayList<>();
        if (mMap!=null && nearByWorkOrders != null){
            for (NearByWorkOrder nearByWorkOrder: nearByWorkOrders){
                NearByInstallationClusterItem nearByInstallationClusterItem =
                        new NearByInstallationClusterItem(nearByWorkOrder);
                nearByInstallationClusterItems.add(nearByInstallationClusterItem);
                clusterManager.addItem(nearByInstallationClusterItem);
                latLngBoundsBuilder.include(nearByInstallationClusterItem.getPosition());
            }
        }

        LatLng currentLatLng = new LatLng(lastKnownlocation.getLatitude(),
                lastKnownlocation.getLongitude());
        latLngBoundsBuilder.include(currentLatLng);
        LatLngBounds bounds = latLngBoundsBuilder.build();
        int padding = 10; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);

        nearByInstallationClusterRenderer = new NearByInstallationClusterRenderer(this,mMap,
                clusterManager);
        clusterManager.setRenderer(nearByInstallationClusterRenderer);

        clusterManager.setAlgorithm(new NonHierarchicalDistanceBasedAlgorithm<>());


        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<NearByInstallationClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<NearByInstallationClusterItem> cluster) {
                ArrayList arrayList = new ArrayList<NearByWorkOrder>();
                for (NearByInstallationClusterItem nearByInstallationClusterItem:cluster.getItems()){
                    arrayList.add(nearByInstallationClusterItem.nearByWorkOrder);
                }
                showSelectionDetails(arrayList);
                return false;
            }
        });

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<NearByInstallationClusterItem>() {
            @Override
            public boolean onClusterItemClick(NearByInstallationClusterItem nearByInstallationClusterItem) {
                ArrayList arrayList = new ArrayList<NearByWorkOrder>();
                arrayList.add(nearByInstallationClusterItem.nearByWorkOrder);
                showSelectionDetails(arrayList);
                return false;
            }
        });
        mMap.setOnMarkerClickListener(clusterManager);
        clusterManager.cluster();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Add marker in current location
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        // Add a marker in Sydney and move the camera
        clusterManager = new ClusterManager<>(this, mMap);
        new FetchNearByWorkOrders().execute();

    }


    private  class FetchNearByWorkOrders extends AsyncTask<Void,Void,List<NearByWorkOrder>>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPostExecute(List<NearByWorkOrder> nearByWorkOrders) {
            try {
                NearByInstallationsActivity.this.nearByWorkOrders = nearByWorkOrders;
                addMarkersOnMap();
                progressDialog.dismiss();
                if (nearByWorkOrders == null ||nearByWorkOrders.size() == 0)
                    throw new Exception("No result Found");
            } catch (Exception e) {
                try {
                    NearByInstallationsActivity.this.finish();
                }catch (Exception e1){
                    SespLogHandler.writeLog("FetchNearByWorkOrders:onPostExecute()",e1);
                }

            }
        }

        @Override
        protected List<NearByWorkOrder> doInBackground( Void... v) {

            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

             lastKnownlocation = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            double latitude = lastKnownlocation.getLatitude();
            double longitude = lastKnownlocation.getLongitude();

            try {
                return AndroidUtilsAstSep.getDelegate().findNearbyWorkOrders(latitude,longitude,
                        Double.parseDouble(getIntent().getExtras().get(RANGE).toString()));
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(NearByInstallationsActivity.this);
            progressDialog.setTitle(getResources().getString(R.string.loading));
            progressDialog.setMessage(getResources().getString(R.string.please_wait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

    private void showSelectionDetails(ArrayList<NearByWorkOrder> nearByWorkOrders) {

        //configure Dialogue

        View view = getLayoutInflater().inflate(R.layout.dialog_cluster_details, null);

        final Dialog dialog = new Dialog(this,R.style.SespAppThemeNAB);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WRAP_CONTENT);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels*0.7f);

        RecyclerView lv = (RecyclerView) view.findViewById(R.id.custom_list);
        lv.setMinimumWidth(widthLcl);
        lv.setLayoutManager(new LinearLayoutManager(this));
        InstallationDetailsAdapter installationDetailsAdapter = new InstallationDetailsAdapter(NearByInstallationsActivity.this,
                nearByWorkOrders);
        lv.setAdapter(installationDetailsAdapter);

        TextView totalTime = view.findViewById(R.id.totalTime);

        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(MATCH_PARENT,
                WRAP_CONTENT);

        totalTime.setLayoutParams(layoutParams);
        totalTime.setText("                                                              ");

        totalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        totalTime.setGravity(END);
        totalTime.setCompoundDrawablesWithIntrinsicBounds(null,null,
                getDrawable(R.drawable.ic_cancel_24dp),null);

        Button buttonSelect = view.findViewById(R.id.buttonSelect);
        buttonSelect.setVisibility(View.GONE);

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setVisibility(View.GONE);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
        installationDetailsAdapter.notifyDataSetChanged();
    }
}

