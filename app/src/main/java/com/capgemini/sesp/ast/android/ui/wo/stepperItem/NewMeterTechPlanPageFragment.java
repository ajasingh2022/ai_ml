package com.capgemini.sesp.ast.android.ui.wo.stepperItem;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.StringUtil;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.adapter.UnitModelAdapter;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.UnitInstallationUtilsStepper;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelIdentifierTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by sacrai on 2/08/2018.
 *
 * @author sacrai
 * @since 2nd August, 2018
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class NewMeterTechPlanPageFragment extends CustomFragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    protected static final int METER_SCAN_REQUEST = 100;
    protected transient Spinner existingMtrModel = null;
    protected transient UnitModelAdapter adapter = null;
    protected transient TextView existingMeterTv = null;
    protected transient TextView techPlanTv = null;
    protected transient TextView meterIdntfrTv = null;
    private static String TAG = NewMeterTechPlanPageFragment.class.getSimpleName();
    protected transient EditText newMeterPropertyTextTv = null;
    protected transient ImageButton scanIdentifierButton = null;
    protected transient ImageButton clearPropValueBtn = null;

    protected transient CheckBox builtInCommCheck = null;
    protected transient CheckBox meterIsSlaveCheck = null;
    protected boolean fetchMeterModelWithoutCat = false;
    static final String UNIT_MODEL_SELECTED = "UNIT_MODEL_SELECTED";
    static final String UNIT_IDENTIFIER = "UNIT_IDENTIFIER";
    static final String BUILT_IN_COMM_MODULE = "BUILT_IN_COMM_MODULE";
    static final String IS_METER_SLAVE = "IS_METER_SLAVE";
   // View view;


    public NewMeterTechPlanPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_new_meter_tech_plan, null);

        WorkorderUtils.unitType = ConstantsAstSep.UnitType.METER;
        initialize();
        setupListeners();
        populateData();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        {

        }
    }

    protected void initialize() {
        try{
        final View parentView = fragmentView;
        if (parentView != null) {
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
        } catch (Exception e) {
            writeLog(TAG + "  : initialize() ", e);
        }
    }

    protected void populateData() {
        try {
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();

            //Technical Planning
            techPlanTv.setText(WorkorderUtils.getTechnologyPlanName(wo));

            //Existing Meter
            WoUnitCustomTO existingDevice = null;
            existingDevice = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
            if (existingDevice == null)
                existingDevice = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER_EXTERNAL, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
            if (existingDevice == null)
                existingDevice = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
            if (existingDevice == null)
                existingDevice = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER_EXTERNAL, CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);


            if (existingDevice != null) {
                String identifier = existingDevice.getGiai();
                String unitModel = existingDevice.getUnitModel();
                if (unitModel != null && unitModel.length() != 0) {
                    identifier += " " + unitModel;
                }
                existingMeterTv.setText(identifier);
            }

            //Meter Model - Spinner
            List<UnitModelCustomTO> filteredUnitModelCustomTOs = UnitInstallationUtils.getUnitModelsOfType(CONSTANTS().UNIT_T__METER);
            if (!fetchMeterModelWithoutCat) {
                filteredUnitModelCustomTOs =
                        UnitInstallationUtils.filterUnitModelsOnCategory(
                                filteredUnitModelCustomTOs,
                                WorkorderUtils.getIdMeaCategoryT(wo)
                        );
            } else {
                filteredUnitModelCustomTOs =
                        UnitInstallationUtils.filterUnitModelsOnCategory(
                                filteredUnitModelCustomTOs,
                                null
                        );
            }
            GuIUtil.setUpSpinner(getContext(), existingMtrModel, filteredUnitModelCustomTOs, true, this);


            //========== Populate the previous values ================

            if (!stepfragmentValueArray.isEmpty()) {

                if (GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED)))) {
                    Long selectedDeviceModelId = Long.valueOf(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED)));
                    existingMtrModel.setSelection(GuIUtil.getPositionOfItemInSpinner(existingMtrModel, selectedDeviceModelId));
                }

                if (Utils.isNotEmpty(stepfragmentValueArray.get(UNIT_IDENTIFIER))) {
                    newMeterPropertyTextTv.setText((CharSequence) stepfragmentValueArray.get(UNIT_IDENTIFIER));
                }

                if (Utils.isNotEmpty(stepfragmentValueArray.get(BUILT_IN_COMM_MODULE))) {
                    builtInCommCheck.setChecked("YES".equals(stepfragmentValueArray.get(BUILT_IN_COMM_MODULE)));
                }

                if (Utils.isNotEmpty(stepfragmentValueArray.get(IS_METER_SLAVE))) {
                    meterIsSlaveCheck.setChecked("YES".equals(stepfragmentValueArray.get(IS_METER_SLAVE)));
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :populateData()", e);
        }
    }

    @Override
    public boolean validateUserInput() {
        boolean status = false;
        try {
            if (GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED))) &&
                    Utils.isNotEmpty(stepfragmentValueArray.get(UNIT_IDENTIFIER))) {
                status = true;
                applyChangesToModifiableWO();
            }
        } catch (Exception e) {
            writeLog(TAG + " :validateUserInput()", e);
        }
        return status;
    }

    public String evaluateNextPage() {
        String nextStepIdentifier = null;
        final NewMeterTechPlanPageFragment fragment = this;
        try {
            if (fragment != null) {
                final String newUnitProp = (String) stepfragmentValueArray.get(UNIT_IDENTIFIER);
                final UnitModelCustomTO selectedModelTo = ObjectCache.getIdObject(UnitModelCustomTO.class, Long.valueOf(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED))));


                if (ConstantsAstSep.FlowPageConstants.YES.equals(stepfragmentValueArray.get(IS_METER_SLAVE))) {
                    return ConstantsAstSep.PageNavConstants.NEXT_PAGE_WITH_MASTER;
                } else {
                    nextStepIdentifier = UnitInstallationUtilsStepper.evaluateUnitInstallationNextPage(selectedModelTo, null,
                            getActivity().getIntent(), getActivity().getIntent().getExtras());
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :evaluateNextPage()", e);
        }
        return nextStepIdentifier;
    }

    public void applyChangesToModifiableWO() {
        //undoChanges();
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();

        final String newUnitProp = (String) stepfragmentValueArray.get(UNIT_IDENTIFIER);
        final UnitModelCustomTO selectedModelTo = ObjectCache.getIdObject(UnitModelCustomTO.class, Long.valueOf(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED))));
        try {
            if (Utils.isNotEmpty(newUnitProp)) {
                getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE, newUnitProp);
                getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.SELECTED_UNIT_MODEL, selectedModelTo.getCode());
            }

            UnitInstallationUtils.dismantleExistingUnit(wo, CONSTANTS().WO_UNIT_T__METER);
            UnitInstallationUtils.installUnit(
                    wo,
                    CONSTANTS().WO_UNIT_T__METER,
                    Long.valueOf(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED))),
                    String.valueOf(stepfragmentValueArray.get(UNIT_IDENTIFIER)),
                    ConstantsAstSep.FlowPageConstants.YES.equals(stepfragmentValueArray.get(IS_METER_SLAVE))
            );
        } catch (Exception e) {
            writeLog(TAG + " :applyChangesToModifiableWO()", e);
        }
    }

    protected void setupListeners() {
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
            builtInCommCheck.setOnClickListener(this);
        }

        //Is Meter Slave
        if (meterIsSlaveCheck != null) {
            meterIsSlaveCheck.setOnClickListener(this);
        }

        // New Meter identifier value
        if (newMeterPropertyTextTv != null) {
            newMeterPropertyTextTv.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    stepfragmentValueArray.put(UNIT_IDENTIFIER, StringUtil.checkNullString(s.toString()));
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
    public void onClick(View v) {
        try{
        if (v != null
                && v.getId() == R.id.clearPropValueBtn
                && newMeterPropertyTextTv != null) {
            newMeterPropertyTextTv.setText("");
        } else if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
        } else if (v != null && v.getId() == R.id.scanIdentifierButton) {
            AndroidUtilsAstSep.scanBarCode(this, METER_SCAN_REQUEST);
        } else if (v != null && v.getId() == R.id.meterIsSlaveCheck) {
            stepfragmentValueArray.put(IS_METER_SLAVE, meterIsSlaveCheck.isChecked() ? ConstantsAstSep.FlowPageConstants.YES : ConstantsAstSep.FlowPageConstants.NO);
        } else if (v != null && v.getId() == R.id.builtInCommCheck) {
            stepfragmentValueArray.put(BUILT_IN_COMM_MODULE, builtInCommCheck.isChecked() ? ConstantsAstSep.FlowPageConstants.YES : ConstantsAstSep.FlowPageConstants.NO);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : onClick() ", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        try{
        if (requestCode == METER_SCAN_REQUEST
                && resultCode == Activity.RESULT_OK) {
            newMeterPropertyTextTv.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
            AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(), false);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : onActivityResult() ", e);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try{
        if (id != -1) {
            UnitModelCustomTO selectedUnitModel = ObjectCache.getIdObject(UnitModelCustomTO.class, id);
            List<UnitModelIdentifierTO> unitModelIdentifierTOs = selectedUnitModel.getUnitModelIdentifierTOs();
            String meterIdentiFier = null;
            Long idUnitIdentifierType = null;
            if ((unitModelIdentifierTOs != null) && (unitModelIdentifierTOs.size() > 0)) {
                UnitModelIdentifierTO unitModelIdentifierTO = unitModelIdentifierTOs.get(0);
                Log.d(this.getClass().getSimpleName(), " selected unit model " + selectedUnitModel.getName());
                idUnitIdentifierType = unitModelIdentifierTO.getIdUnitIdentifierT();
                meterIdentiFier = ObjectCache.getType(UnitIdentifierTTO.class, idUnitIdentifierType).getCode();
                if ((meterIdntfrTv != null) && (meterIdentiFier != null)) {
                    meterIdntfrTv.setText(meterIdentiFier);
                }
            }
        }
        stepfragmentValueArray.put(UNIT_MODEL_SELECTED, String.valueOf(id));
        } catch (Exception e) {
            writeLog(TAG + "  : onItemSelected() ", e);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }
}