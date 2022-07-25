package com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AdditionalDataUtils;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.BatteryInverterVerfication;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.adapter.UnitModelAdapter;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.CustomFragment;
import com.google.gson.Gson;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelIdentifierTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.isNetworkAvailable;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


@SuppressLint("ValidFragment")
public class PanelInstallationFragement extends CustomFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    protected static final int PANEL_SCAN_REQUEST = 100;
    protected transient Spinner solarPanelModel = null;
    protected transient UnitModelAdapter adapter = null;
    private static String TAG = PanelInstallationFragement.class.getSimpleName();
    protected transient ImageButton scanIdentifierButton = null;
    protected transient ImageButton clearPropValueBtn = null;
    Button batteryInverterVerificationBtn = null;
    protected transient TextView techPlanTv = null;
    TextView msgText = null;
    String userSelectedSolarPanelModel = null;
    private ImageButton mismatchUnitIdBtn;
    private ImageButton leftBtn;
    private ImageButton rightBtn;
    private int count = 0;
    private int noOfsolarPanelcount;
    private TextView mismatchValueTxtv;
    private EditText mismatchUnitId;
    Gson gson = new Gson();
    private ArrayMap<Integer, WoUnitCustomTO> noOfUnitsArrayMap = new ArrayMap<Integer, WoUnitCustomTO>();
    protected static final String MISMATCH_VALUE_LIST = "MISMATCH_VALUE_LIST";
    protected static final String USER_SELECTED_UNIT_MODEL_ID = "USER_SELECTED_UNIT_MODEL_ID";
    protected static final String VERFICATION_BUTTON_STATUS = "VERFICATION_BUTTON_STATUS";
    BatteryInverterVerfication batteryInverterVerfication = new BatteryInverterVerfication();
    ArrayMap<String, String> verificationStatus = new ArrayMap<>();

    public PanelInstallationFragement(String stepId) {
        super(stepId);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        try {
            fragmentView = inflater.inflate(R.layout.fragment_panel_installation_fragement, null);
            initViews();
            initializeUI();
            populateData();

        } catch (Exception e) {
            writeLog("PanelInstallationFragement  : onCreateView() ", e);

        }
        return fragmentView;
    }

    protected void initViews() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            //If the device does not have built-in camera, cam scanner button will be hidden.
            if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                mismatchUnitIdBtn = fragmentView.findViewById(R.id.mismatchUnitIdBtn);
                mismatchUnitIdBtn.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            writeLog(TAG + " : initViews()", e);
        }
    }

    /**
     * Lookup and initialize all the view widgets
     */
    protected void initializeUI() {
        try {
            final View parentView = fragmentView;
            if (parentView != null) {
                techPlanTv = parentView.findViewById(R.id.techPlanTv);
                solarPanelModel = parentView.findViewById(R.id.existingSolarModel);
                clearPropValueBtn = parentView.findViewById(R.id.clearPropValueBtn);
                scanIdentifierButton = parentView.findViewById(R.id.scanIdentifierButton);
                mismatchUnitId = parentView.findViewById(R.id.mismatchUnitIdEt);
                mismatchUnitIdBtn = parentView.findViewById(R.id.mismatchUnitIdBtn);
                batteryInverterVerificationBtn = parentView.findViewById(R.id.batteryInverterVerificationBtn);
                msgText = parentView.findViewById(R.id.msgText);
                mismatchUnitIdBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AndroidUtilsAstSep.scanBarCode(PanelInstallationFragement.this, PANEL_SCAN_REQUEST);
                    }
                });

                leftBtn = parentView.findViewById(R.id.leftArrow);
                rightBtn = parentView.findViewById(R.id.rightArrow);
                mismatchValueTxtv = parentView.findViewById(R.id.mismatchValueTxtv);
                mismatchUnitId.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        verificationStatus.remove(VERFICATION_BUTTON_STATUS);
                    }
                });
                leftBtn.setOnClickListener(this);
                rightBtn.setOnClickListener(this);
                batteryInverterVerificationBtn.setOnClickListener(this);
                msgText.setVisibility(View.VISIBLE);
                msgText.setText("You can continue without verification since the app is in offline mode");
                msgText.setTextColor(getResources().getColor(R.color.colorAccent));
                if(SessionState.getInstance().isLoggedInOnline())
                {
                    msgText.setText(getResources().getString(R.string.mandatory_to_perform));
                    msgText.setTextColor(getResources().getColor(R.color.colorHighlight));
                }

            }
        } catch (Exception e) {
            writeLog(TAG + "  : initializeUI() ", e);
        }
    }

    /**
     * Populate UI components with data
     */
    protected void populateData() {
        try {
         //   batteryInverterVerfication.networkStatusChanged(isNetworkAvailable());
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            techPlanTv.setText(WorkorderUtils.getTechnologyPlanName(wo));
            List<UnitModelCustomTO> unitCMModelCustomTOs = UnitInstallationUtils.getUnitModelsOfType(CONSTANTS().UNIT_T__SOLAR_PANEL);
            GuIUtil.setUpSpinner(getContext(), solarPanelModel, unitCMModelCustomTOs, true, this);

            if (stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID) != null) {
                if (GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID)))) {
                    Long selectedUnitModelId = Long.parseLong((String) stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID));
                    solarPanelModel.setSelection(GuIUtil.getPositionOfItemInSpinner(solarPanelModel, selectedUnitModelId));
                }
            }
            try {
                userSelectedSolarPanelModel = stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID).toString();
            } catch (Exception e) {
                writeLog(TAG + " : populateData()", e);
            }
            noOfsolarPanelcount = AdditionalDataUtils.getValueOfCodeFromAdditionalData("NO_OF_SOLAR_PANEL");
            if (Utils.isNotEmpty(stepfragmentValueArray.get(MISMATCH_VALUE_LIST))) {
                mismatchUnitId.setText(stepfragmentValueArray.get(count - 1) == null ? ""
                        : stepfragmentValueArray.get(count - 1).toString());
            }
            populateDataForScanning();
        } catch (Exception e) {
            writeLog(TAG + " : populateData()", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        if (requestCode == PANEL_SCAN_REQUEST
                && resultCode == Activity.RESULT_OK) {
            mismatchUnitId.setText(imageData.getStringExtra("barcode_result"));
            AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(), false);
        }
    }


    public WoUnitCustomTO createWoUnitTO() {

        WoUnitCustomTO woUnitTO = new WoUnitCustomTO();
        String temp = mismatchUnitId.getText().toString();
        try {
            if (Utils.isNotEmpty(temp)) {
                Long unitModelID = Long.parseLong(stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID).toString());
                if (Utils.isNotEmpty(unitModelID) && (unitModelID != -1)) {

                    UnitModelCustomTO unitModelCustomTO = ObjectCache.getIdObject(UnitModelCustomTO.class, unitModelID);
                    List<UnitModelIdentifierTO> unitModelIdentifierTOs = unitModelCustomTO.getUnitModelIdentifierTOs();
                    if ((unitModelIdentifierTOs != null) && (unitModelIdentifierTOs.size() > 0)) {
                        UnitModelIdentifierTO unitModelIdentifierTO = unitModelIdentifierTOs.get(0);
                        woUnitTO.setIdUnitIdentifierT(unitModelIdentifierTO.getIdUnitIdentifierT());
                    }
                    woUnitTO.setUnitModel(unitModelCustomTO.getCode());
                }

                woUnitTO.setGiai(temp);
                woUnitTO.setSerialNumber(temp);
                woUnitTO.setPropertyNumberD(temp);
            }
        } catch (Exception e) {
            writeLog(TAG + " : createWoUnitTO()", e);
        }
        return woUnitTO;
    }

    protected void populateDataForScanning() {
        try {
            int numerator = count + 1;
            int denominator = noOfsolarPanelcount;
            mismatchValueTxtv.setText(numerator + "/" + denominator);
            enableNavigator();
        } catch (Exception e) {
            writeLog(TAG + " : populateDataForScanning() ", e);
        }
    }

    private void enableNavigator() {
        try {
            if (noOfsolarPanelcount == 1) {
                rightBtn.setVisibility(View.INVISIBLE);
                leftBtn.setVisibility(View.INVISIBLE);
            } else if (count + 1 == noOfsolarPanelcount) {
                rightBtn.setVisibility(View.INVISIBLE);
                leftBtn.setVisibility(View.VISIBLE);
            } else if (count == 0) {
                leftBtn.setVisibility(View.INVISIBLE);
                rightBtn.setVisibility(View.VISIBLE);
            } else {
                leftBtn.setVisibility(View.VISIBLE);
                rightBtn.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            writeLog(TAG + " : enableNavigator() ", e);
        }
    }

    /**
     * Validate User input
     *
     * @return
     */
    @Override
    public boolean validateUserInput() {
        boolean validateStatus = false;
        if (Utils.isNotEmpty(verificationStatus.get(VERFICATION_BUTTON_STATUS))) {
            if (verificationStatus.get(VERFICATION_BUTTON_STATUS).equals(ConstantsAstSep.FlowPageConstants.YES)) {
                if (batteryInverterVerfication.returnFlagForVerfication()) {
                    applyChangesToModifiableWO();
                    validateStatus = true;
                }
            }
            else
                validateStatus = true;
        }
        return validateStatus;
    }

    public void applyChangesToModifiableWO() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            List<WoUnitCustomTO> woUnitCustomTOs = new ArrayList<>();
            for (Map.Entry<Integer, WoUnitCustomTO> entry : noOfUnitsArrayMap.entrySet()) {
                UnitModelCustomTO unitModelCustomTO = ObjectCache.getIdObject(UnitModelCustomTO.class, Long.valueOf(stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID).toString()));
                WoUnitCustomTO woUnitCustomTO = new WoUnitCustomTO();
                woUnitCustomTO.setIdCase(wo.getIdCase());
                woUnitCustomTO.setGiai(String.valueOf(entry.getValue().getGiai()));
                woUnitCustomTO.setPropertyNumberD(String.valueOf(entry.getValue().getPropertyNumberD()));
                woUnitCustomTO.setPropertyNumberV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woUnitCustomTO.setSerialNumber(String.valueOf(entry.getValue().getSerialNumber()));
                woUnitCustomTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                woUnitCustomTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woUnitCustomTO.setIdWoUnitT(CONSTANTS().WO_UNIT_T__SOLAR_PANEL);
                woUnitCustomTO.setIdUnitIdentifierT(CONSTANTS().UNIT_IDENTIFIER_T__GIAI);
                woUnitCustomTO.setUnitMountedTimestamp(new Date());
                woUnitCustomTO.setUnitModel(unitModelCustomTO.getCode());
                woUnitCustomTOs.add(woUnitCustomTO);
            }
            wo.getWorkorderCustomTO().setWoUnits(woUnitCustomTOs);
        } catch (Exception e) {
            writeLog(TAG + " : applyChangesToModifiableWO() ", e);
        }

    }


    @Override
    public void onClick(View v) {
        try {

            if (v != null) {
                if (Utils.isNotEmpty(mismatchUnitId.getText().toString())) {
                    noOfUnitsArrayMap.put((count - 1), createWoUnitTO());
                }
                if (v.getId() == R.id.leftArrow) {
                    count--;
                } else {
                    if (v.getId() == R.id.rightArrow)
                        count++;
                }
                populateDataForScanning();
                mismatchUnitId.setText(noOfUnitsArrayMap.get((count - 1)) == null ? ""
                        : noOfUnitsArrayMap.get((count - 1)).getGiai());

            }
            if (v.getId() == R.id.batteryInverterVerificationBtn) {
                stepfragmentValueArray.put(MISMATCH_VALUE_LIST, gson.toJson(noOfUnitsArrayMap));
                if (noOfUnitsArrayMap.size() == noOfsolarPanelcount && batteryInverterVerfication.checkDuplicateArrayMapValue(noOfUnitsArrayMap)) {
                    if (SessionState.getInstance().isLoggedInOnline()) {
                        verificationStatus.put(VERFICATION_BUTTON_STATUS, ConstantsAstSep.FlowPageConstants.YES);
                        batteryInverterVerfication.unitVerfication(fragmentView, noOfUnitsArrayMap);
                    } else {
                        batteryInverterVerfication.showVerificationNotPossible(fragmentView);
                        verificationStatus.put(VERFICATION_BUTTON_STATUS, ConstantsAstSep.FlowPageConstants.NO);
                    }
                } else {
                    batteryInverterVerificationBtn.setText(getResources().getString(R.string.verification_failed));
                    msgText.setVisibility(View.VISIBLE);
                    Drawable verificationFailed = getResources().getDrawable(R.drawable.ic_cancel_hollow_24dp);
                    batteryInverterVerificationBtn.setTextColor(getResources().getColor(R.color.colorRed));
                    batteryInverterVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verificationFailed, null, null, null);

                    msgText.setText("Please enter all and valid units");
                    msgText.setTextColor(getResources().getColor(R.color.colorHighlight));
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : onClick() ", e);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!String.valueOf(id).equals("-1")) {
            userSelectedSolarPanelModel = String.valueOf(id);
        }
        stepfragmentValueArray.put(USER_SELECTED_UNIT_MODEL_ID, String.valueOf(id));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

