package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.module.util.comparators.UnitModelComparator;
import com.capgemini.sesp.ast.android.ui.adapter.UnitModelAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.UnitInstallationUtilsStepper;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelIdentifierTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitMeterCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class ExistingMeterTechPlanPageFragment extends CustomFragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static String TAG = ExistingMeterTechPlanPageFragment.class.getSimpleName();
    protected static final int METER_SCAN_REQUEST = 100;
    protected transient Spinner existingMtrModel = null;
    protected transient UnitModelAdapter adapter = null;
    protected transient TextView existingMeterTv = null;
    protected transient TextView techPlanTv = null;
    protected transient TextView meterIdntfrTv = null;

    protected transient TextView newMeterPropertyTextTv = null;
    protected transient ImageButton scanIdentifierButton = null;
    protected transient ImageButton clearPropValueBtn = null;

    protected transient CheckBox builtInCommCheck = null;
    protected transient CheckBox meterIsSlaveCheck = null;

    protected transient Dialog dialog = null;
    protected transient CheckBox yesNewMeterCb = null;
    protected transient CheckBox keepExistingMeterCb = null;


    public transient String selectedUnitModel;
    public transient String selectedUnitModelAtStart;
    public transient String unitIdentfier;
    public transient String unitIdentfierAtStart;
    public transient Boolean masterMeterSelected = false;
    public transient Boolean masterMeterSelectedAtStart;
    public transient Boolean enforceNewSIM;
    public transient Boolean enforceNewSIMAtStart;
    public transient String meterNumber;
    public transient String meterNumberAtStart;
    public transient String techPlanName;
    public transient String existingMtrNumber;
    public transient Boolean builtInComm = false;
    public transient Boolean builtInCommAtStart;
    public boolean fetchMeterModelWithoutCat = false;
    String nextPage = null;
    protected transient Boolean orgYesNewMeterCb;
    protected transient Boolean orgKeepExistingMeterCb;
    protected transient Boolean modYesNewMeterCb;
    protected transient Boolean modKeepExistingMeterCb;

    protected List<WoUnitCustomTO> abortUnits;
    protected transient WoUnitCustomTO existingCMModel;
    View view;

    public ExistingMeterTechPlanPageFragment(String stepIdentifier, final boolean fetchMeterModelWithoutCat) {
        super(stepIdentifier);
        this.fetchMeterModelWithoutCat = fetchMeterModelWithoutCat;
    }

    public ExistingMeterTechPlanPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }


    public ExistingMeterTechPlanPageFragment() {
        super();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_existing_meter_tech_plan, null);
        initialize();
        initializePageValues();
        populateData();
        setupListeners();
        return fragmentView;
    }
    @Override
    public void onResume() {

        super.onResume();
        WorkorderUtils.unitType = ConstantsAstSep.UnitType.METER;

        // Populate the data for Meter placement type
    }
    public String evaluateNextPage() {
        try {
            final Intent nextPageIntentData = this.getActivity().getIntent();
            final String newUnitProp = getProvidedNewMeterPropNum();
            UnitModelCustomTO selectedModelTo = getSelectedUnitModel();

            if (newUnitProp != null && !newUnitProp.trim().equals("")) {
                nextPageIntentData.putExtra(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE, newUnitProp);
                selectedModelTo = getExistingMeterModel();
                nextPageIntentData.putExtra(ConstantsAstSep.FlowPageConstants.SELECTED_UNIT_MODEL, selectedModelTo.getCode());
            }
            nextPageIntentData.putExtra(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED, builtInComm); //Store built in communication module

            if (modYesNewMeterCb && masterMeterSelected) {
                nextPage = ConstantsAstSep.PageNavConstants.NEXT_PAGE_WITH_MASTER;
            } else {
                nextPage = UnitInstallationUtilsStepper.evaluateUnitInstallationNextPage(selectedModelTo, existingCMModel, nextPageIntentData, nextPageIntentData.getExtras());
            }
        } catch (Exception e) {
            writeLog(TAG + "  : evaluateNextPage() ", e);
        }
        return nextPage;
    }


    public void applyChangesToModifiableWO() {
        try {
            final String newUnitProp = getProvidedNewMeterPropNum();
            UnitModelCustomTO selectedModelTo = getSelectedUnitModel();

            if (newUnitProp != null && !newUnitProp.trim().equals("")) {
                getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE, newUnitProp);
            } else {
                selectedModelTo = getExistingMeterModel();
            }
            getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED, builtInComm);

            if (modYesNewMeterCb && masterMeterSelected) {
                getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.SELECTED_UNIT_MODEL, selectedModelTo.getCode());
            }

            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            //Create new wo unit list
            List<WoUnitCustomTO> unitCustomTos = wo.getWorkorderCustomTO().getWoUnits();
            if (unitCustomTos == null) {
                wo.getWorkorderCustomTO().setWoUnits(new ArrayList<WoUnitCustomTO>());
            }
            if ((modYesNewMeterCb != null && modYesNewMeterCb)
                    && (modKeepExistingMeterCb != null && !modKeepExistingMeterCb)) {
                //Dismantling any existing Meter Unit
                WoUnitCustomTO existingUnitCustomTO = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
                if (existingUnitCustomTO != null) {
                    Log.d(TAG, "DISMANTLING THE EXISTING UNIT " + existingUnitCustomTO.getGiai());
                    existingUnitCustomTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
                    existingUnitCustomTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    existingUnitCustomTO.setUnitDismantledTimestamp(new Date());
                }

                // Attaching new Meter Unit
                WoUnitCustomTO newUnitCustomTO = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                if (newUnitCustomTO == null) {
                    // Create a New Meter
                    // Add the new Meter in the workorder unit list
                    newUnitCustomTO = new WoUnitCustomTO();
                    wo.getWorkorderCustomTO().getWoUnits().add(newUnitCustomTO);
                }

                UnitModelCustomTO unitModelCustomTO = ObjectCache.getIdObject(UnitModelCustomTO.class, Long.valueOf(selectedUnitModel));
                newUnitCustomTO.setUnitModel(unitModelCustomTO.getCode());
                List<UnitModelIdentifierTO> unitModelIdentifierTOs = unitModelCustomTO.getUnitModelIdentifierTOs();
                if ((unitModelIdentifierTOs != null) && (unitModelIdentifierTOs.size() > 0)) {
                    UnitModelIdentifierTO unitModelIdentifierTO = unitModelIdentifierTOs.get(0);
                    newUnitCustomTO.setIdUnitIdentifierT(unitModelIdentifierTO.getIdUnitIdentifierT());
                }
                if (newUnitCustomTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__GIAI.longValue()) {
                    newUnitCustomTO.setGiai(meterNumber);
                } else if (newUnitCustomTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO.longValue()) {
                    newUnitCustomTO.setPropertyNumberV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    newUnitCustomTO.setPropertyNumberD(meterNumber);
                } else if (newUnitCustomTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO.longValue()) {
                    newUnitCustomTO.setSerialNumber(meterNumber);
                }
                newUnitCustomTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                newUnitCustomTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                newUnitCustomTO.setIdCase(wo.getIdCase());
                newUnitCustomTO.setIdWoUnitT(ObjectCache.getIdObject(WoUnitTTO.class, ConstantsAstSep.FlowPageConstants.METER).getId());
                newUnitCustomTO.setUnitMountedTimestamp(new Date());

                newUnitCustomTO.setWoUnitMeter(new WoUnitMeterCustomTO());
            } else {
                //Dismantling any existing Meter Unit
                WoUnitCustomTO existingUnitCustomTO = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
                if (existingUnitCustomTO != null) {
                    Log.d(TAG, "REINSTALLING THE EXISTING UNIT " + existingUnitCustomTO.getGiai());
                    existingUnitCustomTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
                    existingUnitCustomTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                }

                //Detaching the newly added Meter
                WoUnitCustomTO newMeterUnitTO = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                if (newMeterUnitTO != null) {
                    wo.getWorkorderCustomTO().getWoUnits().remove(newMeterUnitTO);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }
    }


    public void initializePageValues() {
        try {
            final WorkorderCustomWrapperTO originalWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (abortUnits == null) {
                abortUnits = originalWo.getWorkorderCustomTO().getWoUnits();
            }
            selectedUnitModel = null;
            unitIdentfier = null;
            masterMeterSelected = false;
            enforceNewSIM = false;
            meterNumber = null;
            if (stepfragmentValueArray != null) {
                orgYesNewMeterCb = "YES".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.YES_NEW_METER_CHECKBOX));
                orgKeepExistingMeterCb = "YES".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.KEEP_EXISTING_METER_CHECKBOX));
                selectedUnitModel = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SELECTED_UNIT_MODEL);
                unitIdentfier = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.UNIT_IDENTIFIER);
                meterNumber = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_NUMBER);
                masterMeterSelected = "YES".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.MASTER_METER_SELECTED));
                enforceNewSIM = "YES".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.ENFORCE_NEW_SIM));
                builtInComm = "YES".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.BUILT_IN_COMMUNICATION));
            }
            techPlanName = WorkorderUtils.getTechnologyPlanName(originalWo);
            if (unitIdentfier == null) {
                if (originalWo != null
                        && originalWo.getWorkorderCustomTO() != null
                        && originalWo.getWorkorderCustomTO().getWoUnits() != null) {
                    WoUnitCustomTO unitMeterCustomTo = null;
                    for (WoUnitCustomTO unitCustomTo : originalWo.getWorkorderCustomTO().getWoUnits()) {
                        if (unitCustomTo != null && unitCustomTo.getWoUnitMeter() != null) {
                            unitMeterCustomTo = unitCustomTo;
                            break;
                        }
                    }
                    if (unitMeterCustomTo != null) {
                        unitIdentfier = UnitInstallationUtils.getUnitIdentifierName(unitMeterCustomTo);
                    }
                }
            }
            existingMtrNumber = getExistingMeterNumber(originalWo);
            existingCMModel = UnitInstallationUtils.getUnit(originalWo,
                    CONSTANTS().WO_UNIT_T__CM,
                    CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
            selectedUnitModelAtStart = selectedUnitModel;
            unitIdentfierAtStart = unitIdentfier;
            masterMeterSelectedAtStart = masterMeterSelected;
            enforceNewSIMAtStart = enforceNewSIM;
            builtInCommAtStart = builtInComm;
            meterNumberAtStart = meterNumber;

            modYesNewMeterCb = orgYesNewMeterCb;
            modKeepExistingMeterCb = orgKeepExistingMeterCb;
        } catch (Exception e) {
            writeLog(TAG + " :initializePageValues()", e);
        }
    }


    public void getUserChoiceValuesForPage() {

        try {
            if (masterMeterSelected != null) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.MASTER_METER_SELECTED, masterMeterSelected ? "YES" : "NO");
            }
            if (enforceNewSIM != null) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.ENFORCE_NEW_SIM, enforceNewSIM ? "YES" : "NO");
            }
            if (builtInComm != null) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.BUILT_IN_COMMUNICATION, builtInComm ? "YES" : "NO");
            }
            if (Utils.isNotEmpty(selectedUnitModel)) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.SELECTED_UNIT_MODEL, selectedUnitModel);
            }
            if (Utils.isNotEmpty(meterNumber)) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.METER_NUMBER, meterNumber);
            }
            if (Utils.isNotEmpty(unitIdentfier)) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.UNIT_IDENTIFIER, unitIdentfier);
            }
        } catch (Exception e) {
            writeLog(TAG + " :getUserChoiceValuesForPage()", e);
        }
    }

    private String getExistingMeterNumber(final WorkorderCustomWrapperTO wo) {
        String meterNumber = null;
        try {
            if (wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoUnits() != null) {
                for (final WoUnitCustomTO unitCustomTo : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (unitCustomTo != null
                            && unitCustomTo.getWoUnitMeter() != null) {
                        if (unitCustomTo.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__GIAI.longValue()) {
                            meterNumber = unitCustomTo.getGiai();
                        } else if (unitCustomTo.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO.longValue()) {
                            if (unitCustomTo.getPropertyNumberV() != 0) {
                                meterNumber = unitCustomTo.getPropertyNumberD();
                            } else {
                                meterNumber = unitCustomTo.getPropertyNumberO();
                            }
                        } else if (unitCustomTo.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO.longValue()) {
                            meterNumber = unitCustomTo.getSerialNumber();
                        }
                        if (meterNumber != null) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getExistingMeterNumber() ", e);
        }
        return meterNumber;
    }

    /**
     * Get Existing meter model
     *
     * @return
     */
    private UnitModelCustomTO getExistingMeterModel() {
        try {
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoUnits() != null) {
                for (final WoUnitCustomTO unitCustomTo : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (unitCustomTo != null
                            && unitCustomTo.getWoUnitMeter() != null) {
                        String unitModel = unitCustomTo.getUnitModel();
                        UnitModelCustomTO unitModelCustomTO = ObjectCache.getIdObject(UnitModelCustomTO.class, unitModel);
                        return unitModelCustomTO;
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getExistingMeterModel() ", e);
        }
        return null;
    }



    protected void initialize() {
        final View parentView = fragmentView;
        if (parentView != null) {
            yesNewMeterCb = parentView.findViewById(R.id.yesNewMeterCb);
            keepExistingMeterCb = parentView.findViewById(R.id.keepExistingMeterCb);

            techPlanTv = parentView.findViewById(R.id.techPlanTv);
            existingMtrModel = parentView.findViewById(R.id.existingMtrModel);
            existingMeterTv = parentView.findViewById(R.id.existingMeterTv);
            newMeterPropertyTextTv = parentView.findViewById(R.id.newMeterPropertyTextTv);
            clearPropValueBtn = parentView.findViewById(R.id.clearPropValueBtn);
            scanIdentifierButton = parentView.findViewById(R.id.scanIdentifierButton);
            builtInCommCheck = parentView.findViewById(R.id.builtInCommCheck);
            meterIsSlaveCheck = parentView.findViewById(R.id.meterIsSlaveCheck);
            meterIdntfrTv = parentView.findViewById(R.id.meterIdntfrTv);
        }
    }

    protected void enableNewMeterOptions(final boolean enable) {
        // Meter model Spinner drop down
        existingMtrModel.setEnabled(enable);

        // New Meter Identifier Value and clear button
        newMeterPropertyTextTv.setEnabled(enable);
        clearPropValueBtn.setEnabled(enable);
        scanIdentifierButton.setEnabled(enable);

        //Built in Communication Module
        //Is Master/Slave Meter
        builtInCommCheck.setEnabled(enable);
        meterIsSlaveCheck.setEnabled(enable);
    }

    public String getProvidedNewMeterPropNum() {
        String value = null;
        if (newMeterPropertyTextTv != null) {
            value = newMeterPropertyTextTv.getText().toString();
        }
        return value;
    }

    UnitModelCustomTO getSelectedUnitModel() {
        return (UnitModelCustomTO) existingMtrModel.getSelectedItem();
    }

    protected void populateData() {
        try {
            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            //Set tech Plan name
            techPlanTv.setText(techPlanName);
            List<UnitModelCustomTO> filteredUnitModelCustomTOs = UnitInstallationUtils.getUnitModelsOfType(CONSTANTS().UNIT_T__METER);
            if (!fetchMeterModelWithoutCat) {
                filteredUnitModelCustomTOs =
                        UnitInstallationUtils.filterUnitModelsOnCategory(
                                filteredUnitModelCustomTOs,
                                WorkorderUtils.getIdMeaCategoryT(modifiableWo)
                        );
            } else {
                filteredUnitModelCustomTOs =
                        UnitInstallationUtils.filterUnitModelsOnCategory(
                                filteredUnitModelCustomTOs,
                                null
                        );
            }

            if (filteredUnitModelCustomTOs != null && !filteredUnitModelCustomTOs.isEmpty()) {
                Collections.sort(filteredUnitModelCustomTOs, new UnitModelComparator(UnitModelComparator.ASCENDING));
                adapter = new UnitModelAdapter(this, filteredUnitModelCustomTOs);
                existingMtrModel.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            if (selectedUnitModel != null) {
                UnitModelCustomTO selUnitModelCustomTO = null;
                for (UnitModelCustomTO unitModelCustomTO : filteredUnitModelCustomTOs) {
                    if ((unitModelCustomTO.getUnitModelMeterTO() != null)
                            && (selectedUnitModel.equalsIgnoreCase(String.valueOf(unitModelCustomTO.getUnitModelMeterTO().getIdUnitModel().longValue())))) {
                        selUnitModelCustomTO = unitModelCustomTO;
                        break;
                    }
                }
                if (selUnitModelCustomTO != null) {
                    int position = adapter.getPosition(selUnitModelCustomTO);
                    if (position > -1) {
                        existingMtrModel.setSelection(position);
                    }
                }
            }
            if (meterIdntfrTv != null) {
                meterIdntfrTv.setText(unitIdentfier);
            }
            existingMeterTv.setText(existingMtrNumber);
            if (meterNumber != null) {
                newMeterPropertyTextTv.setText(meterNumber);
            }
            if (builtInComm != null && builtInComm) {
                builtInCommCheck.setChecked(true);
            }
            if (masterMeterSelected != null && masterMeterSelected) {
                meterIsSlaveCheck.setChecked(true);
            }

            if (modYesNewMeterCb != null) {
                yesNewMeterCb.setChecked(modYesNewMeterCb);
                enableNewMeterOptions(modYesNewMeterCb);
            }
            if (modKeepExistingMeterCb != null) {
                keepExistingMeterCb.setChecked(modKeepExistingMeterCb);
            }
        } catch (Exception e) {
            writeLog(TAG + " :populateData()", e);
        }
    }

    /**
     * User has not selected any option
     * and tries to move forward, user should be prompted
     * to select an option
     */

    public void showPromptUserAction() {
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

    @Override
    public boolean validateUserInput() {
        boolean status = false;
        // Check if either "new meter is selected" or "Keep existing meter" is not selected
        if (!(yesNewMeterCb != null && !yesNewMeterCb.isChecked()
                && keepExistingMeterCb != null && !keepExistingMeterCb.isChecked())) {
            getUserChoiceValuesForPage();
            applyChangesToModifiableWO();
            status = true;
        }
        // Check if either "new meter is selected" but identifier text is blank
        if (yesNewMeterCb.isChecked() && Utils.isEmpty(meterNumber)) {
            status = false;
        }
        return status;
    }

    private void setupListeners() {
        //New Meter checkbox
        if (yesNewMeterCb != null) {
            yesNewMeterCb.setOnCheckedChangeListener(this);
        }
        //Keep existing meter checkbox
        if (keepExistingMeterCb != null) {
            keepExistingMeterCb.setOnCheckedChangeListener(this);
        }
        // Property value clear button
        if (clearPropValueBtn != null) {
            clearPropValueBtn.setOnClickListener(this);
        }
        // Property value scan button
        if (scanIdentifierButton != null) {
            scanIdentifierButton.setOnClickListener(this);
        }
        // Built in communication module
        if (builtInCommCheck != null) {
            builtInCommCheck.setOnCheckedChangeListener(this);
        }

        if (meterIsSlaveCheck != null) {
            meterIsSlaveCheck.setOnCheckedChangeListener(this);
        }
        if (existingMtrModel != null) {
            existingMtrModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    List<UnitModelIdentifierTO> unitModelIdentifierTOs = ((UnitModelCustomTO) existingMtrModel.getSelectedItem()).getUnitModelIdentifierTOs();
                    selectedUnitModel = String.valueOf(((UnitModelCustomTO) existingMtrModel.getSelectedItem()).getId().longValue());
                    String meterIdentiFier = null;
                    Long idUnitIdentifierType = null;
                    if ((unitModelIdentifierTOs != null) && (unitModelIdentifierTOs.size() > 0)) {
                        UnitModelIdentifierTO unitModelIdentifierTO = unitModelIdentifierTOs.get(0);
                        Log.d(TAG, " selected unit model " + selectedUnitModel);
                        idUnitIdentifierType = unitModelIdentifierTO.getIdUnitIdentifierT();
                        meterIdentiFier = ObjectCache.getType(UnitIdentifierTTO.class, idUnitIdentifierType).getCode();
                        if ((meterIdntfrTv != null) && (meterIdentiFier != null)) {
                            meterIdntfrTv.setText(meterIdentiFier);
                            unitIdentfier = meterIdentiFier;
                        }
                    }

                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
        }
        if (newMeterPropertyTextTv != null) {
            newMeterPropertyTextTv.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (newMeterPropertyTextTv.getText() != null) {
                        meterNumber = newMeterPropertyTextTv.getText().toString();
                        if (Utils.isNotEmpty(meterNumber)) {
                            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.METER_NUMBER, meterNumber);
                        }
                    }
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        if (requestCode == METER_SCAN_REQUEST
                && resultCode == Activity.RESULT_OK) {
            newMeterPropertyTextTv.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
            AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(), false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null
                && v.getId() == R.id.clearPropValueBtn
                && newMeterPropertyTextTv != null) {
            newMeterPropertyTextTv.setText("");
        } else if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        } else if (v != null && v.getId() == R.id.scanIdentifierButton) {
            AndroidUtilsAstSep.scanBarCode(this, METER_SCAN_REQUEST);
        }
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView,
                                 final boolean isChecked) {
        if (buttonView != null
                && buttonView.getId() == R.id.meterIsSlaveCheck) {
            masterMeterSelected = !masterMeterSelected;
        } else if (buttonView != null
                && buttonView.getId() == R.id.builtInCommCheck) {
            builtInComm = !builtInComm;
        } else if (buttonView != null && buttonView.getId() == R.id.yesNewMeterCb) {

            yesNewMeterCb.setChecked(isChecked);
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.YES_NEW_METER_CHECKBOX, isChecked ? "YES" : "NO");
            modYesNewMeterCb = isChecked;
            modKeepExistingMeterCb = !isChecked;
            keepExistingMeterCb.setChecked(!isChecked);
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.KEEP_EXISTING_METER_CHECKBOX, isChecked ? "NO" : "YES");
            enableNewMeterOptions(isChecked);
        } else if (buttonView != null && buttonView.getId() == R.id.keepExistingMeterCb) {

            keepExistingMeterCb.setChecked(isChecked);
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.KEEP_EXISTING_METER_CHECKBOX, isChecked ? "YES" : "NO");
            yesNewMeterCb.setChecked(!isChecked);
            modYesNewMeterCb = !isChecked;
            modKeepExistingMeterCb = isChecked;
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.YES_NEW_METER_CHECKBOX, isChecked ? "NO" : "YES");
            enableNewMeterOptions(!isChecked);
        }
    }
}