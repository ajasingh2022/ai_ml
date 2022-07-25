package com.capgemini.sesp.ast.android.ui.activity.navigation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.communication.LocationCaptureInterface;
import com.capgemini.sesp.ast.android.module.communication.LocationStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GPSThread;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.ui.activity.dashboard.MainMenuItem;
import com.capgemini.sesp.ast.android.ui.activity.near_by_installations.NearByInstallationsActivity;
import com.capgemini.sesp.ast.android.ui.adapter.MainMenuAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.cft.common.to.cft.table.SystemCoordinateSystemTTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static android.location.LocationManager.GPS_PROVIDER;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Activity to show main menu of the application.
 *
 * @author Capgemini
 * @since 04th April, 2016
 */

public class NavigationListActivity extends AppCompatActivity implements LocationStatusCallbackListener, LocationCaptureInterface, OnClickListener {//, OnTouchListener, OnClickListener {

    public ListView listView = null;
    private transient AlertDialog dialog = null;
    private transient Location retrievedLoc = null;
    private transient final String GPS_COORD_TYPE = "WGS84";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_list);

        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);

        /*
         * Setting up custom action bar view
         */
        final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
        ImageButton help_btn = vw.findViewById(R.id.menu_help);
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new HelpDialog(NavigationListActivity.this, ConstantsAstSep.HelpDocumentConstant.NAVIGATION);
                dialog.show();
            }
        });

        // -- Customizing the action bar ends -----


        getSupportActionBar().setCustomView(vw);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(R.string.title_activity_navigation_menu);

        //    getSupportActionBar().setTitle(R.string.title_activity_navigation_menu);


        listView= findViewById(R.id.listNV);
        listView.setAdapter(new MainMenuAdapter(this, createItems()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onSelectionOfListItem(listView,view,position,id);


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregForLocationChangeInfo(this, GPS_PROVIDER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        processGPS();
    }


    /**
     * List which returns items from pre defined array
     *
     * @return items
     */
    private List<MainMenuItem> createItems() {

        int[] itemIconIds = new int[]{
                R.drawable.gps,
                //    R.drawable.drawable_locationpin,
                R.drawable.ic_save,
                R.drawable.ic_near_me_24dp

        };
        String[] itemNames = getResources().getStringArray(R.array.items_navigation_menu);

        List<MainMenuItem> items = new ArrayList<MainMenuItem>();
        for (int i = 0; i < itemNames.length; i++) {
            Integer iconId = null;
            if (i < itemIconIds.length) {
                iconId = itemIconIds[i];
            }
            items.add(new MainMenuItem(itemNames[i], iconId));
        }
        return items;
    }


    protected void onSelectionOfListItem(ListView l, View v, int position, long id) {

        if (position == ConstantsAstSep.NavigationMenuItems.MAP_GPS) {
            Intent goToIntent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(goToIntent);
        } else if (position == ConstantsAstSep.NavigationMenuItems.SAVE_X_Y) {
            if (AndroidUtilsAstSep.isGPSEnabled() && retrievedLoc != null)
            saveXY();
            else
                processGPS();
        } else if (position == ConstantsAstSep.NavigationMenuItems.NEAR_BY_INSTALLATIONS) {
            if (AndroidUtilsAstSep.isGPSEnabled()  && retrievedLoc != null)
            showNearByInstallation();
            else
                processGPS();
        }
    }

    private void saveXY(){
        final View newCordView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.new_gps_cord_show_layout, null);

        final Button useButton = newCordView.findViewById(R.id.useButton);
        final Button cancelButton = newCordView.findViewById(R.id.cancelButton);
        if (useButton != null) {
            useButton.setText(R.string.save);
            useButton.setOnClickListener(this);
        }

        if (cancelButton != null) {
            cancelButton.setOnClickListener(this);
        }

        // Populate the retrieved co-ordinates
        final TextView xView = newCordView.findViewById(R.id.xValue);
        final TextView yView = newCordView.findViewById(R.id.yValue);

        Location location = retrievedLoc;
        if (xView != null) {
            xView.setText(String.valueOf(location.getLatitude()));
        }

        if (yView != null) {
            yView.setText(String.valueOf(location.getLongitude()));
        }

        dialog = GuiController.getCustomAlertDialog(this, newCordView, null, null).create();
        // User must answer this
        dialog.setCancelable(false);
        dialog.show();

    }

    private void showNearByInstallation() {

        final EditText input = new EditText(NavigationListActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationListActivity.this);
        builder.setTitle(getResources().getString(R.string.enterRadius));
        builder.setView(input);
        builder.setPositiveButton(getString(R.string.ok),null);
        builder.setNegativeButton(getString(R.string.cancel),null);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button buttonn =
                        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                buttonn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //validate
                        try {
                            if (Double.parseDouble((String) input.getText().toString()) > 0) {
                                alertDialog.dismiss();
                                Intent intent = new Intent(NavigationListActivity.this,
                                        NearByInstallationsActivity.class);
                                intent.putExtra(NearByInstallationsActivity.RANGE,
                                        input.getText().toString());
                                startActivity(intent);


                            }
                            else
                                throw new Exception();

                        } catch (Exception e) {

                            alertDialog
                                    .getWindow()
                                    .getDecorView()
                                    .animate()
                                    .translationX(16f)
                                    .setInterpolator(new CycleInterpolator(7f));

                            Toast.makeText(NavigationListActivity.this,
                                    getString(R.string.wrong_radius_value),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });

        alertDialog.show();

    }


    private void processGPS() {
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertView = null;

        // Check if GPS is enabled from system settings
        if (AndroidUtilsAstSep.isGPSEnabled()) {
            try {
                regForLocationChangeInfo(this, GPS_PROVIDER);
            } catch (final Exception e1) {
                writeLog("NavigationListActivity : processGPS()", e1);
            }

            /*
             *  Show pop up containing animation of GPS locking
             *  and start tracking in background
             */

            alertView = inflater.inflate(R.layout.animate_gps_tracking_layout, null);
            if (alertView != null) {
                final Button cancelButton = alertView.findViewById(R.id.cancelButton);
                if (cancelButton != null) {
                    cancelButton.setOnClickListener(this);
                }
                /*
                 *  Start animation of receiving GPS Signal
                 *  In background start GPS tracking
                 */
                dialog = GuiController.getCustomAlertDialog(this, alertView, null, null).create();
                dialog.setCancelable(false);
                dialog.show();

            }

        } else {
            // Inflate the view first to attach the listener

            alertView = inflater.inflate(R.layout.gps_not_enabled_layout, null);

            if (alertView != null) {

                final Switch turnGpsYesNoSwitch = alertView.findViewById(R.id.turnGpsYesNoSwitch);

                if (turnGpsYesNoSwitch != null) {

                    turnGpsYesNoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {
                                // Time to dismiss the alert
                                dialog.dismiss();
                                // Navigate to turn on GPS
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            } else {

                                //std ui modification ---> It will invoke the cancel button click event indirectly
                                Button cancelButton = findViewById(R.id.cancelButton);
                                cancelButton.performClick();

                            }

                        }
                    });

                }


                /*
                 *  Show user friendly request to enable GPS
                 *  Dont hack and auto enable GPS without user's knowledge
                 */
                dialog = GuiController.getCustomAlertDialog(this, alertView, null, null).create();
                dialog.show();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v != null && v.getId() == R.id.okButton
                && dialog != null) {
            // Time to dismiss the alert
            dialog.dismiss();
            // Navigate to turn on GPS
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        } else if (v != null && (v.getId() == R.id.cancelButton)
                && dialog != null) {
            // Time to dismiss the alert
            dialog.dismiss();

        } else if (v != null && v.getId() == R.id.useButton) {
            dialog.dismiss();
            SaveXYLocInPreference();
            // Use gps retrived co-ordinates

        }

    }

    private Long getGpsCoordType() {
        List<SystemCoordinateSystemTTO> sysCordTypLs = TypeDataUtil.filterUserSettableAndSystemEnabled(ObjectCache.getAllTypes(SystemCoordinateSystemTTO.class));
        Long value = null;
        if (sysCordTypLs != null && !sysCordTypLs.isEmpty()) {
            for (final SystemCoordinateSystemTTO tto : sysCordTypLs) {
                if (tto != null && tto.getCode().equals(GPS_COORD_TYPE)) {
                    value = tto.getId();
                }
            }
        }
        return value;
    }

    private void SaveXYLocInPreference() {
        try {
            if (retrievedLoc != null) {
                SESPPreferenceUtil.savePreference("GPS_X_COORD_TEMP", String.valueOf(retrievedLoc.getLatitude()));
                SESPPreferenceUtil.savePreference("GPS_Y_COORD_TEMP", String.valueOf(retrievedLoc.getLongitude()));
                SESPPreferenceUtil.savePreference("GPS_COORD_SYS_TYPE", GPS_COORD_TYPE);
                if (getGpsCoordType() != null)
                    new GPSThread(getGpsCoordType(), String.valueOf(retrievedLoc.getLatitude()), String.valueOf(retrievedLoc.getLongitude())).execute();
            }
        } catch (final Exception e1) {
            writeLog("NavigationListActivity : SaveXYLocInPreference()", e1);
        }


    }


    @Override
    public void onLocationChanged(final Location location) {
        /*
         *  The flow activity has found a location lock
         *  Now do the business logic
         */
        if (location != null) {
            retrievedLoc = location;
            // Dismiss the popup first
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void regForLocationChangeInfo(final LocationStatusCallbackListener callBack, final String provider) {

        if (callBack != null && !Utils.safeToString(provider).equals("")) {
            boolean shouldRequestNewLoc = false;
            AndroidUtilsAstSep.requestLocationByProviders(this, this, shouldRequestNewLoc);
        }
    }

    /**
     * Reverse of regForLocationChangeInfo method
     * <p>
     * Each implementing entity (activity/fragment) should unregister
     * at onStop/onPause
     *
     * @param callBack {@link LocationStatusCallbackListener}
     * @param provider {@link String} Provider constant
     */
    public void unregForLocationChangeInfo(final LocationStatusCallbackListener callBack, final String provider) {

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
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
}
