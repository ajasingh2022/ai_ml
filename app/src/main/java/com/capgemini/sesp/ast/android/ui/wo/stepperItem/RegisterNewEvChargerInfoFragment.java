package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.EvChargerVerification;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.adapter.UnitModelAdapter;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterInstallationCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterInstallationDM;
import com.google.gson.Gson;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelIdentifierTO;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterNewEvChargerInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class RegisterNewEvChargerInfoFragment extends CustomFragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    protected transient UnitModelAdapter adapter = null;
    private static String TAG = RegisterNewEvChargerInfoFragment.class.getSimpleName();
    protected transient TextView evTechPlanTv = null;
    private transient TextView existingEvChargerModelGiai;
    private transient TextView existingEvChargerModelLabel;
    Button evChargerVerificationButton = null;
    TextView msgText = null;
    String userSelectedbatteryModel = null;
    private ImageButton scanEvChargerUnit;
    private int count = 1;
    private int noOfBatterycount;
    private String existingGiai;
    private EditText editEvChargerUnit;
    private static final int BATTERY_SCAN_REQUEST = 108;
    Gson gson = new Gson();
    private WoUnitCustomTO woUnitCustomto = null;
    protected static final String USER_ENTERED_CHARGER = "USER_ENTERED_CHARGER";
    protected static final String USER_SELECTED_UNIT_MODEL_ID = "USER_SELECTED_UNIT_MODEL_ID";
    protected static final String VERFICATION_BUTTON_STATUS = "VERFICATION_BUTTON_STATUS";
    protected static final String USER_ENTERED_UNIQUE_ID = "USER_ENTERED_UNIQUE_ID";
    EvChargerVerification evChargerVerification = new EvChargerVerification(new UnitInformationTO());
    ArrayMap<String, String> verificationStatus = new ArrayMap<>();
    private Spinner existingEvModel = null;
    private String flowName;

    public RegisterNewEvChargerInfoFragment(String stepId) {
        super(stepId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_register_new_ev_charger_info, null);
        initViews();
        initializeUI();
        populateData();
        return fragmentView;
    }

    protected void initViews() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            //If the device does not have built-in camera, cam scanner button will be hidden.
            if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                scanEvChargerUnit = fragmentView.findViewById(R.id.scanEvChargerUnit);
                scanEvChargerUnit.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            writeLog(TAG + " : initViews()", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        if (requestCode == BATTERY_SCAN_REQUEST
                && resultCode == Activity.RESULT_OK) {
            editEvChargerUnit.setText(imageData.getStringExtra("barcode_result"));
            AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(), false);
        }
    }

    protected void initializeUI() {
        try {
            final View parentView = fragmentView;
            if (parentView != null) {
                existingEvModel = parentView.findViewById(R.id.existingEvModel);
                evTechPlanTv = parentView.findViewById(R.id.evTechPlanTv);
                existingEvChargerModelGiai = parentView.findViewById(R.id.existingEvChargerGiai);
                existingEvChargerModelLabel = parentView.findViewById(R.id.existingEvChargerLabel);
                editEvChargerUnit = parentView.findViewById(R.id.editEvChargerUnit);
                scanEvChargerUnit = parentView.findViewById(R.id.scanEvChargerUnit);
                evChargerVerificationButton = parentView.findViewById(R.id.evChargerVerificationBtn);
                msgText = parentView.findViewById(R.id.msgText);
                scanEvChargerUnit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AndroidUtilsAstSep.scanBarCode(RegisterNewEvChargerInfoFragment.this, BATTERY_SCAN_REQUEST);
                    }
                });
                editEvChargerUnit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        verificationStatus.remove(VERFICATION_BUTTON_STATUS);
                        stepfragmentValueArray.put(USER_ENTERED_UNIQUE_ID, editEvChargerUnit.getText().toString());
                    }
                });
                evChargerVerificationButton.setOnClickListener(this);
                msgText.setVisibility(View.VISIBLE);
                msgText.setText(getResources().getString(R.string.you_can_continue_without_verification_since_the_app_is_in_offline_mode));
                msgText.setTextColor(getResources().getColor(R.color.colorAccent));
                if (SessionState.getInstance().isLoggedInOnline()) {
                    msgText.setText(getResources().getString(R.string.mandatory_to_perform));
                    msgText.setTextColor(getResources().getColor(R.color.colorHighlight));
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : initializeUI() ", e);
        }
    }


    protected void populateData() {
        try {

            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            //Technical Planning
            evTechPlanTv.setText(WorkorderUtils.getTechnologyPlanName(wo));
            //Fill Spinner for Inverter Models
            List<UnitModelCustomTO> unitCMModelCustomTOs = UnitInstallationUtils.getUnitModelsOfType(CONSTANTS().UNIT_T__EV_CHARGER);
            GuIUtil.setUpSpinner(getContext(), existingEvModel, unitCMModelCustomTOs, true, this);

            if (stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID) != null) {
                if (GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID)))) {
                    Long selectedUnitModelId = Long.parseLong((String) stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID));
                    existingEvModel.setSelection(GuIUtil.getPositionOfItemInSpinner(existingEvModel, selectedUnitModelId));
                }
            }
            if (stepfragmentValueArray.get(USER_ENTERED_UNIQUE_ID) != null) {
                editEvChargerUnit.setText(stepfragmentValueArray.get(USER_ENTERED_UNIQUE_ID).toString());
            }
            try {
                userSelectedbatteryModel = stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID).toString();
            } catch (Exception e) {
                writeLog(TAG + " : populateData()", e);
            }
            noOfBatterycount = 1;
            if (!(this.getActivity() instanceof MeterInstallationDM) && !(this.getActivity() instanceof MeterInstallationCT)) {
                existingEvChargerModelLabel.setVisibility(View.VISIBLE);
                existingEvChargerModelGiai.setVisibility(View.VISIBLE);
                WoUnitCustomTO unitCustomTO = UnitInstallationUtils.getUnit(wo, AndroidUtilsAstSep.CONSTANTS().WO_UNIT_T__EV_CHARGER,
                        AndroidUtilsAstSep.CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
                if (unitCustomTO != null)
                    existingGiai = unitCustomTO.getGiai();
                if (existingGiai != null)
                    existingEvChargerModelGiai.setText(existingGiai);
                else
                    existingEvChargerModelGiai.setText(getResources().getString(R.string.no_existing_ev_charger));
            } else {
                existingEvChargerModelGiai.setVisibility(View.GONE);
                existingEvChargerModelLabel.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            writeLog(TAG + " : populateData()", e);
        }
    }

    public WoUnitCustomTO createWoUnitTO() {

        WoUnitCustomTO woUnitTO = new WoUnitCustomTO();
        String temp = editEvChargerUnit.getText().toString();
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
                // woUnitTO.setSerialNumber(temp);
                // woUnitTO.setPropertyNumberD(temp);
            }
        } catch (Exception e) {
            writeLog(TAG + " : createWoUnitTO()", e);
        }
        return woUnitTO;
    }

    @Override
    public boolean validateUserInput() {
        boolean validateStatus = false;
        if (!AbstractWokOrderActivity.resuming) {
            if (Utils.isNotEmpty(verificationStatus.get(VERFICATION_BUTTON_STATUS))) {
                if (verificationStatus.get(VERFICATION_BUTTON_STATUS).equals(ConstantsAstSep.FlowPageConstants.YES)) {
                    if (evChargerVerification.returnFlagForVerfication()) {
                        applyChangesToModifiableWO();
                        validateStatus = true;
                    }
                } else
                    validateStatus = true;
            }
        } else
            validateStatus = true;
        return validateStatus;
    }

    public void applyChangesToModifiableWO() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            List<WoUnitCustomTO> woUnitCustomTOs = new ArrayList<WoUnitCustomTO>();
            if (!(this.getActivity() instanceof MeterInstallationDM) && !(this.getActivity() instanceof MeterInstallationCT)) {
                UnitInstallationUtils.dismantleExistingUnit(wo, AndroidUtilsAstSep.CONSTANTS().WO_UNIT_T__EV_CHARGER); //dismantle the installed unit
            }
            UnitModelCustomTO unitModelCustomTO = ObjectCache.getIdObject(UnitModelCustomTO.class, Long.valueOf(stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID).toString()));
            WoUnitCustomTO woUnitCustomTO = new WoUnitCustomTO();
            woUnitCustomTO.setIdCase(wo.getIdCase());
            unitModelCustomTO.getUnitModelIdentifierTOs();
            woUnitCustomTO.setGiai(String.valueOf(woUnitCustomto.getGiai()));
            woUnitCustomTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
            woUnitCustomTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
            woUnitCustomTO.setIdWoUnitT(CONSTANTS().WO_UNIT_T__EV_CHARGER);
            woUnitCustomTO.setIdUnitIdentifierT(CONSTANTS().UNIT_IDENTIFIER_T__GIAI);
            woUnitCustomTO.setUnitMountedTimestamp(new Date());
            woUnitCustomTO.setUnitModel(unitModelCustomTO.getCode());
            woUnitCustomTOs.add(woUnitCustomTO);
            wo.getWorkorderCustomTO().getWoUnits().add(woUnitCustomTO);
        } catch (Exception e) {
            writeLog(TAG + " : applyChangesToModifiableWO() ", e);
        }

    }


    @Override
    public void onClick(View v) {
        try {

            if (v != null) {
                if (Utils.isNotEmpty(editEvChargerUnit.getText().toString())) {
                    woUnitCustomto = createWoUnitTO();
                }
                editEvChargerUnit.setText(woUnitCustomto == null ? ""
                        : woUnitCustomto.getGiai());

            }
            if (v.getId() == R.id.evChargerVerificationBtn) {
                stepfragmentValueArray.put(USER_ENTERED_CHARGER, gson.toJson(woUnitCustomto));
                if (woUnitCustomto != null) {
                    WoUnitCustomTO tempCustomTO = woUnitCustomto;
                    if (SessionState.getInstance().isLoggedInOnline()) {
                        verificationStatus.put(VERFICATION_BUTTON_STATUS, ConstantsAstSep.FlowPageConstants.YES);
                        evChargerVerification.unitVerfication(fragmentView, tempCustomTO);
                    } else {
                        evChargerVerification.showVerificationNotPossible(fragmentView);
                        verificationStatus.put(VERFICATION_BUTTON_STATUS, ConstantsAstSep.FlowPageConstants.NO);
                    }
                } else {
                    evChargerVerificationButton.setText(getResources().getString(R.string.verification_failed));
                    msgText.setVisibility(View.VISIBLE);
                    Drawable verificationFailed = getResources().getDrawable(R.drawable.ic_cancel_hollow_24dp);
                    evChargerVerificationButton.setTextColor(getResources().getColor(R.color.colorRed));
                    evChargerVerificationButton.setCompoundDrawablesRelativeWithIntrinsicBounds(verificationFailed, null, null, null);

                    msgText.setText(getResources().getString(R.string.please_enter_all_and_valid_units));
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
            userSelectedbatteryModel = String.valueOf(id);
        }
        stepfragmentValueArray.put(USER_SELECTED_UNIT_MODEL_ID, String.valueOf(id));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
