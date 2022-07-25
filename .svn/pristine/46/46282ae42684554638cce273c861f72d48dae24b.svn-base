package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.communication.LocationStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.GPSThread;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.activity.flow.SESPFlowActivity;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.to.cft.table.SystemCoordinateSystemTTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by yhegde on 2/19/2018.
 */

@SuppressLint({"ValidFragment", "InflateParams"})
public class RegisterCordsFragment
        extends CustomFragment implements View.OnClickListener, LocationStatusCallbackListener, View.OnTouchListener {

    private transient AlertDialog dialog = null;
    private transient Location retrievedLoc = null;
    private transient final String GPS_COORD_TYPE = "WGS84";


    private transient Button gpsButton = null;
    private transient Button existingButton = null;
    private transient Button useSavedButton = null;

    private transient Spinner cordsystemspinner = null;
    private transient CordSysTypeAdapter adapter = null;
    private transient TextView newCordXTv = null;
    private transient TextView newCordYTv = null;

    private transient TextView existingCordSysTypTv = null;
    private transient TextView existingCordXValTv = null;
    private transient TextView existingCordYValTv = null;

    private transient TextView existingCoordXLb = null;
    private transient TextView existingCoordYLb = null;
    private transient TextInputLayout newCoordXLb = null;
    private transient TextInputLayout newCoordYLb = null;

    transient static final String EXISTING_COORD_NAME = "EXISTING_COORD_NAME";
    protected transient static final String EXISTING_X_COORD = "EXISTING_X_COORD";
    protected transient static final String EXISTING_Y_COORD = "EXISTING_Y_COORD";
    transient static final String EXISTING_X_COORD_LABEL = "EXISTING_X_COORD_LABEL";
    transient static final String EXISTING_Y_COORD_LABEL = "EXISTING_Y_COORD_LABEL";
    protected transient static final String NEW_COORD_ID = "NEW_COORD_ID";
    public transient static final String NEW_X_COORD = "NEW_X_COORD";
    protected transient static final String NEW_Y_COORD = "NEW_Y_COORD";
    private transient static final String GPS_BUTTON_SELECTED = "GPS_BUTTON_SELECTED";
    transient static final String EXISTING_BUTTON_SELECTED = "EXISTING_BUTTON_SELECTED";
    transient static final String USE_SAVED_BUTTON_SELECTED = "USE_SAVED_BUTTON_SELECTED";
    private transient final String YES = "YES";
    private transient final String NO = "NO";
    private static String TAG = RegisterCordsFragment.class.getSimpleName();
    transient boolean noProduct = true;
    String gpsXCOORD;
    String gpsYCOORD;

    /**
     * Drawables
     */
    private transient Drawable roundedCornerButtonPositiveEnabled = null;

    private transient Map<String, SystemCoordinateSystemTTO> cordTypeMap
            = new ArrayMap<String, SystemCoordinateSystemTTO>();

    private transient Map<String, Integer> cordPositionMap
            = new ArrayMap<String, Integer>();

    private transient List<SystemCoordinateSystemTTO> sysCordTypLs = null;

    public RegisterCordsFragment() {
        super();
    }

    public RegisterCordsFragment(String stepIdentifier) {
        super(stepIdentifier);
    }


    @Override
    public void applyChangesToModifiableWO() {
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        WoInstTO woInstTO = null;
        Long idSystemCoordsSystemT = null;
        String xCoordinate = null;
        String yCoordinate = null;
        try {
            if (wo != null
                    && wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoInst() != null
                    && wo.getWorkorderCustomTO().getWoInst().getWoInstTO() != null) {
                woInstTO = wo.getWorkorderCustomTO().getWoInst().getWoInstTO();
            }
            if (woInstTO != null) {
                if (stepfragmentValueArray.get(NEW_COORD_ID) != null && !"-1".equals(stepfragmentValueArray.get(NEW_COORD_ID))) {
                    woInstTO.setIdSystemCoordSystemTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    woInstTO.setIdSystemCoordSystemTD(Long.valueOf(String.valueOf(stepfragmentValueArray.get(NEW_COORD_ID))));
                    idSystemCoordsSystemT =Long.valueOf(String.valueOf(stepfragmentValueArray.get(NEW_COORD_ID)));
                }
                if (stepfragmentValueArray.get(NEW_X_COORD) != null) {
                    woInstTO.setXCoordinateV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    woInstTO.setXCoordinateD(String.valueOf(stepfragmentValueArray.get(NEW_X_COORD)));
                    xCoordinate = String.valueOf(stepfragmentValueArray.get(NEW_X_COORD));
                }
                if (stepfragmentValueArray.get(NEW_Y_COORD) != null) {
                    woInstTO.setYCoordinateV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    woInstTO.setYCoordinateD(String.valueOf(stepfragmentValueArray.get(NEW_Y_COORD)));
                    yCoordinate = String.valueOf(stepfragmentValueArray.get(NEW_Y_COORD));
                }
                if (YES.equals(stepfragmentValueArray.get(GPS_BUTTON_SELECTED)))  {
                    new GPSThread(idSystemCoordsSystemT, xCoordinate, yCoordinate).execute();
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }
    }


    public String evaluateNextPage() {
        return String.valueOf(noProduct);

    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_register_cords, null);

        initializeViewComponents();
        setupListeners();
        populateData();
        return fragmentView;
    }


    private void setupListeners() {
        // Register for GPS button
        if (gpsButton != null) {
            gpsButton.setOnClickListener(this);
            gpsButton.setOnTouchListener(this);
        }

        if (existingButton != null) {
            existingButton.setOnClickListener(this);
            existingButton.setOnTouchListener(this);
        }

        if (useSavedButton != null) {
            useSavedButton.setOnClickListener(this);
            useSavedButton.setOnTouchListener(this);
        }

        newCordXTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                stepfragmentValueArray.put(NEW_X_COORD, s.toString());
            }
        });

        newCordYTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                stepfragmentValueArray.put(NEW_Y_COORD, s.toString());
            }
        });

        cordsystemspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                SystemCoordinateSystemTTO systemCoordinateSystemTTO = (SystemCoordinateSystemTTO) adapter.getItem(position);
                if (systemCoordinateSystemTTO != null) {
                    if (systemCoordinateSystemTTO.getId() != null) {
                        stepfragmentValueArray.put(NEW_COORD_ID, String.valueOf(systemCoordinateSystemTTO.getId().longValue()));
                        newCoordXLb.setHint(systemCoordinateSystemTTO.getXCoordName());
                        newCoordYLb.setHint(systemCoordinateSystemTTO.getYCoordName());
                        newCordXTv.setHint(systemCoordinateSystemTTO.getXCoordName());
                        newCordYTv.setHint(systemCoordinateSystemTTO.getYCoordName());
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }


    /**
     * Initialize view lookup objects
     */

    private void initializeViewComponents() {
        existingCordSysTypTv = fragmentView.findViewById(R.id.existingCordTv);
        existingCordXValTv = fragmentView.findViewById(R.id.existingCordXTv);
        existingCordYValTv = fragmentView.findViewById(R.id.existingCordYTv);

        gpsButton = fragmentView.findViewById(R.id.gpsButton);
        existingButton = fragmentView.findViewById(R.id.existingButton);
        useSavedButton = fragmentView.findViewById(R.id.useSavedButton);

        newCordXTv = fragmentView.findViewById(R.id.newCordXTv);
        newCordYTv = fragmentView.findViewById(R.id.newCordYTv);

        cordsystemspinner = fragmentView.findViewById(R.id.cordsystemspinner);

            /*roundedCornerButtonPositiveEnabled = getResources().getDrawable(R.drawable.rounded_corner_button_positive_enabled);*/

        existingCoordXLb = fragmentView.findViewById(R.id.existingCordXLb);
        existingCoordYLb = fragmentView.findViewById(R.id.existingCordYLb);
        newCoordXLb = fragmentView.findViewById(R.id.newCordXLb);
        newCoordYLb = fragmentView.findViewById(R.id.newCordYLb);

         gpsXCOORD = SESPPreferenceUtil.getPreferenceString("GPS_X_COORD_TEMP");
         gpsYCOORD = SESPPreferenceUtil.getPreferenceString("GPS_Y_COORD_TEMP");


    }

    private void resetFields() {
        newCordXTv.setText("");
        newCordYTv.setText("");
        cordsystemspinner.setSelection(0);
    }

    /**
     * Populate the data based on existing workorder
     */

    private void populateData() {
        try {

            if(gpsXCOORD == null || gpsXCOORD.equals("")|| gpsYCOORD == null || gpsYCOORD.equals(""))
            {
                useSavedButton.setEnabled(false);
              //  useSavedButton.setTextColor(getResources().getColor(R.color.white));
            }
            else
            {
                useSavedButton.setEnabled(true);
              //  useSavedButton.setTextColor(getResources().getColor(R.color.black));
            }
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (WorkorderUtils.isMeterRegisterExists(wo) || WorkorderUtils.isCollectionProductExist(wo)) {
                noProduct = false;
            }

            if (true) {
                WoInstTO woInstTO = null;
                if (wo != null
                        && wo.getWorkorderCustomTO() != null
                        && wo.getWorkorderCustomTO().getWoInst() != null
                        && wo.getWorkorderCustomTO().getWoInst().getWoInstTO() != null) {
                    woInstTO = wo.getWorkorderCustomTO().getWoInst().getWoInstTO();
                }

                if (woInstTO != null) {
                    SystemCoordinateSystemTTO coordSys = null;
                    if ((CONSTANTS().SYSTEM_BOOLEAN_T__TRUE).equals(woInstTO.getIdSystemCoordSystemTV())) {
                        coordSys = ObjectCache.getType(SystemCoordinateSystemTTO.class, woInstTO.getIdSystemCoordSystemTD());
                    } else {
                        try {
                            coordSys = ObjectCache.getType(SystemCoordinateSystemTTO.class, woInstTO.getIdSystemCoordSystemTO());
                        } catch (Exception e) {
                            writeLog(TAG + " : populateData() :Need a Check", e);
                        }

                    }
                    if (coordSys != null) {
                        stepfragmentValueArray.put(EXISTING_COORD_NAME, coordSys.getName());
                        stepfragmentValueArray.put(EXISTING_X_COORD_LABEL, coordSys.getXCoordName());
                        stepfragmentValueArray.put(EXISTING_Y_COORD_LABEL, coordSys.getYCoordName());
                    }
                    String existingXCordinate = null;
                    if ((CONSTANTS().SYSTEM_BOOLEAN_T__TRUE).equals(woInstTO.getXCoordinateV())) {
                        existingXCordinate = woInstTO.getXCoordinateD();
                    } else {
                        existingXCordinate = woInstTO.getXCoordinateO();
                    }
                    stepfragmentValueArray.put(EXISTING_X_COORD, existingXCordinate);
                    String existingYCordinate = null;
                    if ((CONSTANTS().SYSTEM_BOOLEAN_T__TRUE).equals(woInstTO.getYCoordinateV())) {
                        existingYCordinate = woInstTO.getYCoordinateD();
                    } else {
                        existingYCordinate = woInstTO.getYCoordinateO();
                    }
                    stepfragmentValueArray.put(EXISTING_Y_COORD, existingYCordinate);
                }
            }

            if (YES.equals(stepfragmentValueArray.get(GPS_BUTTON_SELECTED))) {

                //existingButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                existingButton.setEnabled(true);
                //useSavedButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
              //  useSavedButton.setEnabled(false);
            } else if (YES.equals(stepfragmentValueArray.get(EXISTING_BUTTON_SELECTED))) {
                //existingButton.setBackgroundDrawable(roundedCornerButtonPositiveEnabled);
                //useSavedButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
             //   useSavedButton.setEnabled(false);
                //gpsButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                gpsButton.setEnabled(true);
            } else if (YES.equals(stepfragmentValueArray.get(USE_SAVED_BUTTON_SELECTED))) {
                //useSavedButton.setBackgroundDrawable(roundedCornerButtonPositiveEnabled);
                //gpsButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                gpsButton.setEnabled(true);
                //existingButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                existingButton.setEnabled(true);
            }
            if (stepfragmentValueArray.get(EXISTING_COORD_NAME) != null) {
                existingCordSysTypTv.setText((CharSequence) stepfragmentValueArray.get(EXISTING_COORD_NAME));
                if (stepfragmentValueArray.get(EXISTING_X_COORD_LABEL) != null) {
                    existingCoordXLb.setText((CharSequence) stepfragmentValueArray.get(EXISTING_X_COORD_LABEL));
                }
                if (stepfragmentValueArray.get(EXISTING_Y_COORD_LABEL) != null) {
                    existingCoordYLb.setText((CharSequence) stepfragmentValueArray.get(EXISTING_Y_COORD_LABEL));
                }
            }
            if (stepfragmentValueArray.get(EXISTING_X_COORD) != null) {
                existingCordXValTv.setText((CharSequence) stepfragmentValueArray.get(EXISTING_X_COORD));
            }
            if (stepfragmentValueArray.get(EXISTING_Y_COORD) != null) {
                existingCordYValTv.setText((CharSequence) stepfragmentValueArray.get(EXISTING_Y_COORD));
            }
            if (cordsystemspinner != null) {
                sysCordTypLs = TypeDataUtil.filterUserSettableAndSystemEnabled(ObjectCache.getAllTypes(SystemCoordinateSystemTTO.class));
                if (sysCordTypLs != null && !sysCordTypLs.isEmpty()) {
                    convertToMap(sysCordTypLs);
                    final SystemCoordinateSystemTTO fillerCordSystemTto = sysCordTypLs.get(0);
                    if (fillerCordSystemTto != null && !Utils.safeToString(fillerCordSystemTto.getCode()).equals("")) {
                        sysCordTypLs.add(0, AndroidUtilsAstSep.instantiateEmptyTypeObject(SystemCoordinateSystemTTO.class));
                    }
                    adapter = new CordSysTypeAdapter(this, sysCordTypLs);
                    cordsystemspinner.setAdapter(adapter);
                    if (stepfragmentValueArray.get(NEW_COORD_ID) != null && !"-1".equals(stepfragmentValueArray.get(NEW_COORD_ID))) {
                        SystemCoordinateSystemTTO coordinateSystemTTO = ObjectCache.getType(SystemCoordinateSystemTTO.class, Long.valueOf(String.valueOf(stepfragmentValueArray.get(NEW_COORD_ID))));
                        if (coordinateSystemTTO != null) {
                            cordsystemspinner.setSelection(adapter.selectCordSystemType(coordinateSystemTTO));
                            newCoordXLb.setHint(coordinateSystemTTO.getXCoordName());
                            newCoordYLb.setHint(coordinateSystemTTO.getYCoordName());
                            newCordXTv.setHint(coordinateSystemTTO.getXCoordName());
                            newCordYTv.setHint(coordinateSystemTTO.getYCoordName());
                        }
                    }
                }
            }
            if (stepfragmentValueArray.get(NEW_X_COORD) != null) {
                newCordXTv.setText((CharSequence) stepfragmentValueArray.get(NEW_X_COORD));
            }
            if (stepfragmentValueArray.get(NEW_Y_COORD) != null) {
                newCordYTv.setText((CharSequence) stepfragmentValueArray.get(NEW_Y_COORD));
            }
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }
    }

    private void convertToMap(final List<SystemCoordinateSystemTTO> cordSystemTypeTto) {
        if (cordSystemTypeTto != null && !cordSystemTypeTto.isEmpty()) {
            for (final SystemCoordinateSystemTTO tto : cordSystemTypeTto) {
                if (tto != null && tto.getCode() != null) {
                    cordTypeMap.put(tto.getCode().toUpperCase(Locale.getDefault()), tto);
                }
            }
        }
    }

    public boolean validateUserInput() {
        boolean status = false;
        try {
            final SystemCoordinateSystemTTO selectedCordSysType = (SystemCoordinateSystemTTO) cordsystemspinner.getSelectedItem();
            if (selectedCordSysType != null
                    && selectedCordSysType.getName() != null
                    && !selectedCordSysType.getName().trim().equalsIgnoreCase("")) {
                status = true;
            }
            if (Utils.safeToString(newCordXTv.getText()).isEmpty()) {
                status = status & false;
            } else {
                status = status & true;
            }
            if (Utils.safeToString(newCordYTv.getText()).isEmpty()) {
                status = status & false;
            } else {
                status = status & true;
            }
            if (status)
                applyChangesToModifiableWO();
        } catch (Exception e) {
            writeLog(TAG + "  : validateUserInput() ", e);
        }
        return status;
    }


    public void showPromptUserAction() {

			/*
             *  If status is false then it is coming from flow engine stack rebuild
			 *  and does not require user attention
			 */

        final View alertView
                = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.prompt_user_response_layout, null);
        if (alertView != null) {
            final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
            okButton.setOnClickListener(this);

            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                    alertView, null, null);
            dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * Internal method that either starts GPS tracking
     * or asks for user to turn on GPS if not already enabled
     */

    private void processGPS() {
        try {
            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertView = null;

            // Check if GPS is enabled from system settings
            if (AndroidUtilsAstSep.isGPSEnabled()) {
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
                    dialog = GuiController.getCustomAlertDialog(getActivity(), alertView, null, null).create();
                    dialog.setCancelable(false);
                    dialog.show();

                    try {
                        ((SESPFlowActivity) getActivity()).regForLocationChangeInfo(this, LocationManager.GPS_PROVIDER);
                    } catch (final Exception e1) {
                        writeLog(TAG + " : processGPS()", e1);
                    }
                }

            } else {
                // Inflate the view first to attach the listener

                alertView = inflater.inflate(R.layout.gps_not_enabled_layout, null);

                if (alertView != null) {
                    //final Button okButton = (Button) alertView.findViewById(R.id.okButton);
                    final Button cancelButton = alertView.findViewById(R.id.cancelButton);         //std ui modification
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

                                cancelButton.performClick();

                            }

                        }
                    });


					/*
					 *  Show user friendly request to enable GPS
					 *  Dont hack and auto enable GPS without user's knowledge
					 */
                    dialog = GuiController.getCustomAlertDialog(getActivity(), alertView, null, null).create();
                    dialog.show();
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : processGPS() ", e);
        }
    }


    private void populateGpsMessages() {
        try {
            if (retrievedLoc != null) {
                if (newCordXTv != null) {
                    newCordXTv.setText(String.valueOf(retrievedLoc.getLatitude()));
                    stepfragmentValueArray.put(NEW_X_COORD, newCordXTv.getText().toString());
                }

                if (newCordYTv != null) {
                    newCordYTv.setText(String.valueOf(retrievedLoc.getLongitude()));
                    stepfragmentValueArray.put(NEW_Y_COORD, newCordYTv.getText().toString());
                }

                // Change the spinner Content
                // Coordinate system type will always be WGS84 in case the cords are taken from GPS lock
                if (cordTypeMap != null
                        && cordTypeMap.get(GPS_COORD_TYPE) != null
                        && adapter != null) {
                    cordsystemspinner.setSelection(adapter.selectCordSystemType(cordTypeMap.get(GPS_COORD_TYPE)));
                    stepfragmentValueArray.put(NEW_COORD_ID, String.valueOf(cordTypeMap.get(GPS_COORD_TYPE).getId().longValue()));
                    SystemCoordinateSystemTTO coordinateSystemTTO = cordTypeMap.get(GPS_COORD_TYPE);
                    newCoordXLb.setHint(coordinateSystemTTO.getXCoordName());
                    newCoordYLb.setHint(coordinateSystemTTO.getYCoordName());
                    newCordXTv.setHint(coordinateSystemTTO.getXCoordName());
                    newCordYTv.setHint(coordinateSystemTTO.getYCoordName());
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : processGPSMessages() ", e);
        }
    }


    @Override
    public void onClick(View v) {
        try {
            if (v != null && v.getId() == R.id.gpsButton) {
                // GPS button clicked
                stepfragmentValueArray.put(GPS_BUTTON_SELECTED, YES);
                stepfragmentValueArray.put(EXISTING_BUTTON_SELECTED, NO);
                stepfragmentValueArray.put(USE_SAVED_BUTTON_SELECTED, NO);
                // gpsButton.setBackgroundDrawable(roundedCornerButtonPositiveEnabled);
                // existingButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                existingButton.setEnabled(true);
                // useSavedButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
              //  useSavedButton.setEnabled(true);
                resetFields();
                processGPS();

            } else if (v != null && v.getId() == R.id.existingButton) {
                stepfragmentValueArray.put(EXISTING_BUTTON_SELECTED, YES);
                stepfragmentValueArray.put(GPS_BUTTON_SELECTED, NO);
                stepfragmentValueArray.put(USE_SAVED_BUTTON_SELECTED, NO);
                //existingButton.setBackgroundDrawable(roundedCornerButtonPositiveEnabled);
                // gpsButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                gpsButton.setEnabled(true);
                //useSavedButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
              //  useSavedButton.setEnabled(true);
                // Copy the co-ordinate and system type to new co-ordinates
                final SystemCoordinateSystemTTO coordinateSystemTTO = cordTypeMap.get(existingCordSysTypTv.getText().toString().toUpperCase(Locale.getDefault()));
                if (coordinateSystemTTO != null && sysCordTypLs != null && sysCordTypLs.indexOf(coordinateSystemTTO) != -1) {
                    cordsystemspinner.setSelection(sysCordTypLs.indexOf(coordinateSystemTTO));
                    stepfragmentValueArray.put(NEW_COORD_ID, String.valueOf(coordinateSystemTTO.getId().longValue()));
                    newCoordXLb.setHint(coordinateSystemTTO.getXCoordName());
                    newCoordYLb.setHint(coordinateSystemTTO.getYCoordName());
                    newCordXTv.setHint(coordinateSystemTTO.getXCoordName());
                    newCordYTv.setHint(coordinateSystemTTO.getYCoordName());
                }
                newCordXTv.setText(existingCordXValTv.getText());
                newCordYTv.setText(existingCordYValTv.getText());
                stepfragmentValueArray.put(NEW_X_COORD, newCordXTv.getText().toString());
                stepfragmentValueArray.put(NEW_Y_COORD, newCordYTv.getText().toString());
                //cordsystemspinner.setEnabled(true);
            } else if (v != null && v.getId() == R.id.okButton
                    && dialog != null) {
                // Time to dismiss the alert
                dialog.dismiss();
                // Navigate to turn on GPS
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } else if (v != null && (v.getId() == R.id.cancelButton)
                    && dialog != null) {
                // Time to dismiss the alert
                dialog.dismiss();
                // References should be with interface not implementation class
                ((SESPFlowActivity) getActivity()).unregForLocationChangeInfo(this, LocationManager.GPS_PROVIDER);
				/*if(animator!=null){
					animator.end();
					animator=null;
				}*/
            } else if (v != null && v.getId() == R.id.useButton) {
                // Stop gps
                ((SESPFlowActivity) getActivity()).unregForLocationChangeInfo(this, LocationManager.GPS_PROVIDER);
                dialog.dismiss();
                populateGpsMessages();
                // Use gps retrived co-ordinates

            } else if (v != null && dialog != null
                    && (v.getId() == R.id.cancelButton || v.getId() == R.id.okButtonYesNoPage)) {
                dialog.dismiss();
                dialog = null;
            } else if (v != null && v.getId() == R.id.useSavedButton) {
                stepfragmentValueArray.put(USE_SAVED_BUTTON_SELECTED, YES);
                stepfragmentValueArray.put(GPS_BUTTON_SELECTED, NO);
                stepfragmentValueArray.put(EXISTING_BUTTON_SELECTED, NO);
               // String gpsXCOORD = SESPPreferenceUtil.getPreferenceString("GPS_X_COORD_TEMP");
              //  String gpsYCOORD = SESPPreferenceUtil.getPreferenceString("GPS_Y_COORD_TEMP");
                if (gpsXCOORD == null || gpsXCOORD.equals("") || gpsYCOORD == null || gpsYCOORD.equals(""))
                    return;
                //useSavedButton.setBackgroundDrawable(roundedCornerButtonPositiveEnabled);


                // gpsButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                gpsButton.setEnabled(true);

                //existingButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                existingButton.setEnabled(true);


                if (cordTypeMap != null && cordTypeMap.get(GPS_COORD_TYPE) != null && adapter != null) {
                    cordsystemspinner.setSelection(adapter.selectCordSystemType(cordTypeMap.get(GPS_COORD_TYPE)));
                    stepfragmentValueArray.put(NEW_COORD_ID, String.valueOf(cordTypeMap.get(GPS_COORD_TYPE).getId().longValue()));
                    SystemCoordinateSystemTTO coordinateSystemTTO = cordTypeMap.get(GPS_COORD_TYPE);
                    newCoordXLb.setHint(coordinateSystemTTO.getXCoordName());
                    newCoordYLb.setHint(coordinateSystemTTO.getYCoordName());
                    newCordXTv.setHint(coordinateSystemTTO.getXCoordName());
                    newCordYTv.setHint(coordinateSystemTTO.getYCoordName());

                }
                newCordXTv.setText(gpsXCOORD);
                newCordYTv.setText(gpsYCOORD);
                stepfragmentValueArray.put(NEW_X_COORD, newCordXTv.getText().toString());
                stepfragmentValueArray.put(NEW_Y_COORD, newCordYTv.getText().toString());
            }
        } catch (Exception e) {
            writeLog(TAG + "  : onClick() ", e);
        }
    }

    @Override
    public void onLocationChanged(final Location location) {
			/*
			 *  The flow activity has found a location lock
			 *  Now do the business logic
			 */
			try{
        if (location != null) {
            // Disable GPS tracking immediately to save power
            ((SESPFlowActivity) getActivity()).unregForLocationChangeInfo(this, LocationManager.GPS_PROVIDER);

            retrievedLoc = location;
            // Dismiss the popup first
            if (dialog != null) {
                dialog.dismiss();
            }

            // Next show user that new cords have been found
            final View newCordView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.new_gps_cord_show_layout, null);

            final Button useButton = newCordView.findViewById(R.id.useButton);
            final Button cancelButton = newCordView.findViewById(R.id.cancelButton);
            if (useButton != null) {
                useButton.setOnClickListener(this);
            }

            if (cancelButton != null) {
                cancelButton.setOnClickListener(this);
            }

            // Populate the retrieved co-ordinates
            final TextView xView = newCordView.findViewById(R.id.xValue);
            final TextView yView = newCordView.findViewById(R.id.yValue);
            final TextView xLabel = newCordView.findViewById(R.id.xLabel);
            final TextView yLabel = newCordView.findViewById(R.id.yLabel);

            SystemCoordinateSystemTTO coordinateSystemTTO = cordTypeMap.get(GPS_COORD_TYPE);
            xLabel.setText(coordinateSystemTTO.getXCoordName());
            yLabel.setText(coordinateSystemTTO.getYCoordName());

            if (xView != null) {
                xView.setText(String.valueOf(location.getLatitude()));
            }

            if (yView != null) {
                yView.setText(String.valueOf(location.getLongitude()));
            }

            dialog = GuiController.getCustomAlertDialog(getActivity(), newCordView, null, null).create();
            // User must answer this
            dialog.setCancelable(false);
            dialog.show();
        }
            } catch (Exception e) {
                writeLog(TAG + "  : onLocationChanged() ", e);
            }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            v.performClick();
        }
        return true;
    }

    public void updateCordTypPosMap(final String code, final int position) {
        cordPositionMap.put(code, position);
    }

    @SuppressLint("InflateParams")
    static class CordSysTypeAdapter extends BaseAdapter {

        private transient List<SystemCoordinateSystemTTO> sysCordSysTypeTTO = null;
        private transient SoftReference<RegisterCordsFragment> fragmentRef = null;

        public CordSysTypeAdapter(final RegisterCordsFragment fragment,
                                  final List<SystemCoordinateSystemTTO> sysCordSysTypeTTO) {
            this.sysCordSysTypeTTO = sysCordSysTypeTTO;
            if (fragment != null) {
                this.fragmentRef = new SoftReference<RegisterCordsFragment>(fragment);
            }
        }

        @Override
        public int getCount() {
            int count = 0;
            try{
            if (this.sysCordSysTypeTTO != null) {
                count = this.sysCordSysTypeTTO.size();
            }
            } catch (Exception e) {
                writeLog(TAG + "  : getCount() ", e);
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            Object item = null;
            try {
                if (this.sysCordSysTypeTTO != null
                        && this.sysCordSysTypeTTO.size() > position) {
                    item = this.sysCordSysTypeTTO.get(position);
                }
            } catch (Exception e) {
                writeLog(TAG + "  : getItem() ", e);
            }
            return item;
        }

        public int selectCordSystemType(final SystemCoordinateSystemTTO tto) {

            int position = 0;
            try {
                if (tto != null && sysCordSysTypeTTO != null
                        && !sysCordSysTypeTTO.isEmpty()
                        && sysCordSysTypeTTO.contains(tto)) {
                    position = sysCordSysTypeTTO.indexOf(tto);
                }
            } catch (Exception e) {
                writeLog(TAG + "  : selectCordSystemType() ", e);
            }
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView vw = null;
            try {
                if (this.fragmentRef != null && this.fragmentRef.get() != null) {
                    final Context context = this.fragmentRef.get().getActivity();
                    vw = (TextView) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                            .inflate(android.R.layout.simple_spinner_item, parent, false);

                    vw.setEllipsize(null);
                    if (vw != null) {
                        final SystemCoordinateSystemTTO sysCordsTto = (SystemCoordinateSystemTTO) getItem(position);
                        if (sysCordsTto != null) {
                            vw.setText(sysCordsTto.getName());
                            vw.setTag(sysCordsTto);
                            Log.d("CordSysTypeAdapter", "Setting view tag for coordinate system type spinner=" + sysCordsTto.getName());
                            this.fragmentRef.get().updateCordTypPosMap(sysCordsTto.getCode(), position);
                        }
                    }
                }
            } catch (Exception e) {
                writeLog(TAG + "  : getView() ", e);
            }
            return vw;

        }
    }
}
