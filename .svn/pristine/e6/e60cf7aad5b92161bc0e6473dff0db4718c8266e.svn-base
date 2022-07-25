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
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.adapter.UnitModelAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.UnitInstallationUtilsStepper;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelIdentifierTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class NewConcentratorTechPlanPageFragment extends CustomFragment
        implements View.OnClickListener {

    String selectedConcModel;
    private Intent nextPageIntentData = new Intent();
    private String orgUnitIdentifierValue;
    String modUnitIdentifierValue;
    private UnitModelCustomTO orgSelectedConcUnitModel;
    UnitModelCustomTO userSelectedConcUnitModel;
    private String orgUnitIdentifierType;
    String modUnitIdentifierType;
    private boolean orgBuildComCheck;
    boolean enforceNewConcentrator;
    transient CheckBox yesNewConcentratorCb = null;
    transient CheckBox keepExistingConcentratorCb = null;
    Boolean modyesNewConcentratorCb;
    Boolean modKeepExistingConcentratorCb;
    private Boolean orgYesNewConcCb;
    private Boolean orgKeepExistingConcCb;
    private static String TAG = NewConcentratorTechPlanPageFragment.class.getSimpleName();
    private static final int CONCENTRATOR_SCAN_REQUEST = 104;
    private transient Spinner existingConcModel = null;
    private transient UnitModelAdapter adapter = null;
    private transient TextView existingConcentratorTv = null;
    private transient TextView techPlanTv = null;
    private transient TextView concentratorIdntfrTv = null;
    private transient TextView newConcentratorUidTv = null;
    private transient ImageButton scanIdentifierButton = null;
    private transient ImageButton clearPropValueBtn = null;
    transient CheckBox builtInCommCheck = null;
    private transient Dialog dialog = null;

    public NewConcentratorTechPlanPageFragment() {
        super();
    }

    public NewConcentratorTechPlanPageFragment(String stepIdentifier, Boolean enforceNewConcentrator) {
        super(stepIdentifier);
        this.enforceNewConcentrator = enforceNewConcentrator;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_new_concentrator_tech_plan, null);
        initializePageValues();
        initializeUI();
        setupListeners();
        populateData();

        WorkorderUtils.unitType = ConstantsAstSep.UnitType.CONCENTRATOR;

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void initializePageValues() {
    try{
        WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        WoUnitCustomTO newConcUnitTO = UnitInstallationUtils.getUnit(modifiableWo, CONSTANTS().WO_UNIT_T__CONCENTRATOR, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
        if (newConcUnitTO != null) { //Set originally selected concentrator model
            orgSelectedConcUnitModel = ObjectCache.getIdObject(UnitModelCustomTO.class, newConcUnitTO.getUnitModel());
            orgUnitIdentifierValue = UnitInstallationUtils.getUnitIdentifierValue(newConcUnitTO);
            orgUnitIdentifierType = UnitInstallationUtils.getUnitIdentifierName(newConcUnitTO);
        }

        if (!stepfragmentValueArray.isEmpty()) {
            if ("YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED)))) {
                orgBuildComCheck = true;
            } else if ("NO".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED)))) {
                orgBuildComCheck = false;
            }
            //Yes New Conc checkbox
            if ("YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.YES_NEW_CONCENTRATOR_CHECKBOX)))) {
                orgYesNewConcCb = true;
            } else if ("NO".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.YES_NEW_CONCENTRATOR_CHECKBOX)))) {
                orgYesNewConcCb = false;
            }
            //Keep existing Conc checkbox
            if ("YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.KEEP_EXISTING_CONCENTRATOR_CHECKBOX)))) {
                orgKeepExistingConcCb = true;
            } else if ("NO".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.KEEP_EXISTING_CONCENTRATOR_CHECKBOX)))) {
                orgKeepExistingConcCb = false;
            }
        }

        if (stepfragmentValueArray.get("userSelectedConcUnitModel") != null) {
            userSelectedConcUnitModel = ObjectCache.getType(UnitModelCustomTO.class, (long) Double.parseDouble(String.valueOf(stepfragmentValueArray.get("userSelectedConcUnitModel"))));
        } else
            userSelectedConcUnitModel = orgSelectedConcUnitModel;


        modUnitIdentifierType = orgUnitIdentifierType;

        if (stepfragmentValueArray.get("modUnitIdentifierValue") != null)

            modUnitIdentifierValue = stepfragmentValueArray.get("modUnitIdentifierValue").toString();

        else
            modUnitIdentifierValue = orgUnitIdentifierValue;

        modyesNewConcentratorCb = orgYesNewConcCb;
        modKeepExistingConcentratorCb = orgKeepExistingConcCb;
    } catch (Exception e) {
        writeLog(TAG + "  : initializePageValues() ", e);
    }
    }

    private void initializeUI() {
        final View parentView = fragmentView;

        if (parentView != null) {
            techPlanTv = parentView.findViewById(R.id.techPlanTv);
            existingConcModel = parentView.findViewById(R.id.existingConcModel);
            existingConcentratorTv = parentView.findViewById(R.id.existingConcentratorTv);
            existingConcentratorTv.setEnabled(false);
            newConcentratorUidTv = parentView.findViewById(R.id.newConcentratorUidTv);
            clearPropValueBtn = parentView.findViewById(R.id.clearPropValueBtn);
            builtInCommCheck = parentView.findViewById(R.id.builtInCommCheck);
            concentratorIdntfrTv = parentView.findViewById(R.id.concentratorIdntfrTv);
            scanIdentifierButton = parentView.findViewById(R.id.scanIdentifierButton);
        }
        techPlanTv.setEnabled(false);
        concentratorIdntfrTv.setEnabled(false);
        existingConcentratorTv.setEnabled(false);

        if (!enforceNewConcentrator) {
            parentView.findViewById(R.id.newOrExistingConcentrator).setVisibility(View.VISIBLE);
            yesNewConcentratorCb = parentView.findViewById(R.id.yesNewConcentratorCb);
            keepExistingConcentratorCb = parentView.findViewById(R.id.keepExistingConcentratorCb);
            enableNewConcOptions(false);
        } else {
            parentView.findViewById(R.id.builtInCommTV).setVisibility(View.GONE);
            parentView.findViewById(R.id.builtInCommCheck).setVisibility(View.GONE);
        }
    }

    private UnitModelCustomTO getSelectedUnitModel() {
        return (UnitModelCustomTO) existingConcModel.getSelectedItem();
    }

    private void populateData() {
        final WorkorderCustomWrapperTO originalWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        // Populate existing concentrator number
        try {
            if (originalWo != null) {
                WoUnitCustomTO existingConcentrator = UnitInstallationUtils.getUnit(originalWo, CONSTANTS().WO_UNIT_T__CONCENTRATOR, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
                final String existingConcentratorNumber = existingConcentrator != null ? UnitInstallationUtils.getUnitIdentifierValue(existingConcentrator) : "";//Get existing Concentrator Number
                final String concentratorIdentifier = existingConcentrator != null ? UnitInstallationUtils.getUnitIdentifierName(existingConcentrator) : "";//Get existing Concentrator Identifier
                final String hardwareCfgModel = WorkorderUtils.getTechnologyPlanName(originalWo);// Get technical planning of the work-order
                if (GuIUtil.notNullnotEmpty(existingConcentratorNumber)) {
                    existingConcentratorTv.setText(existingConcentratorNumber);
                    Log.d("ConcentratorTechPlan", "Existing concentrator number obtained as = " + existingConcentratorNumber);
                }
                if (concentratorIdntfrTv != null) {
                    concentratorIdntfrTv.setText(GuIUtil.displayUnitIdentifierNameText(concentratorIdentifier));
                }
                if (hardwareCfgModel != null) {
                    techPlanTv.setText(hardwareCfgModel);
                }
            }

            // Get all concentrator models
            // =========================================================================================================
            // Fetch all code names for available system type names
            List<UnitModelCustomTO> unitModelCustomTOs = UnitInstallationUtils.getUnitModelsOfType(CONSTANTS().UNIT_T__CONCENTRATOR);
            if (unitModelCustomTOs != null && !unitModelCustomTOs.isEmpty()) {
                adapter = new UnitModelAdapter(this, unitModelCustomTOs);
                existingConcModel.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.d("ConcentartorTechplan", "No unit models exists for unit type = Concentrator");
            }

            // ==========================================================================================================

            //===============================Set the preselected values==============================
            if (userSelectedConcUnitModel != null)
                existingConcModel.setSelection(((UnitModelAdapter) existingConcModel.getAdapter()).getPositionOfItem(userSelectedConcUnitModel));
            if (modUnitIdentifierType != null)
                concentratorIdntfrTv.setText(GuIUtil.displayUnitIdentifierNameText(modUnitIdentifierType));
            if (modUnitIdentifierValue != null)
                newConcentratorUidTv.setText(modUnitIdentifierValue);
            if ("YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED)))) {
                builtInCommCheck.setChecked(true);
            } else {
                builtInCommCheck.setChecked(false);
            }
            if (modyesNewConcentratorCb != null && modyesNewConcentratorCb) {
                yesNewConcentratorCb.setChecked(modyesNewConcentratorCb);
                enableNewConcOptions(modyesNewConcentratorCb);
            }
            if (modKeepExistingConcentratorCb != null && modKeepExistingConcentratorCb) {
                keepExistingConcentratorCb.setChecked(modKeepExistingConcentratorCb);
            }
            //=======================================================================================
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
        try{
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
        } catch (Exception e) {
            writeLog(TAG + "  : showPromptUserAction() ", e);
        }
    }

    @Override
    public boolean validateUserInput() {
        boolean status = false;
        // User is good to proceed if the user has provided new concentrator uid value only
        // Check the identifier value if its provided or not

        try {
            modUnitIdentifierValue =  newConcentratorUidTv.getText().toString();
            if (GuIUtil.notNullnotEmpty(modUnitIdentifierValue) ||
                    (yesNewConcentratorCb != null && yesNewConcentratorCb.isChecked() && GuIUtil.notNullnotEmpty(modUnitIdentifierValue)) ||
                    (keepExistingConcentratorCb != null && keepExistingConcentratorCb.isChecked())) {
                applyChangesToModifiableWO();
                status = true;
            }
        } catch (Exception e) {
            writeLog(TAG + " :validateUserInput()", e);
        }
        return status;
    }

    @Override
    public String evaluateNextPage() {
        String nextPage = null;
        final NewConcentratorTechPlanPageFragment fragment = this;
        try {
            if (fragment != null) {


                if (modUnitIdentifierValue != null)
                    this.getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE, modUnitIdentifierValue);
                this.getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED, fragment.builtInCommCheck.isChecked());

                WoUnitCustomTO parentUnit = UnitInstallationUtils.getUnit(AbstractWokOrderActivity.getWorkorderCustomWrapperTO(),
                        CONSTANTS().WO_UNIT_T__CONCENTRATOR, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                if (parentUnit == null) {
                    //No newly mounted units, retrieve the existing unit
                    parentUnit = UnitInstallationUtils.getUnit(AbstractWokOrderActivity.getWorkorderCustomWrapperTO(),
                            CONSTANTS().WO_UNIT_T__CONCENTRATOR, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
                }
                UnitModelCustomTO selectedUnitModel = ObjectCache.getIdObject(UnitModelCustomTO.class, parentUnit.getUnitModel());

                nextPage = UnitInstallationUtilsStepper.evaluateUnitInstallationNextPage(selectedUnitModel, null,
                        nextPageIntentData, getActivity().getIntent().getExtras());

            }
        } catch (Exception e) {
            writeLog(TAG + " :evaluateNextPage()", e);
        }
        return nextPage;
    }

    @Override
    public void applyChangesToModifiableWO() {
        try {
            if (modUnitIdentifierValue != null)
                this.getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE, modUnitIdentifierValue);
            this.getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED, this.builtInCommCheck.isChecked());

            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();


            if (enforceNewConcentrator || (!enforceNewConcentrator && (modyesNewConcentratorCb != null && modyesNewConcentratorCb)
                    && (modKeepExistingConcentratorCb != null && !modKeepExistingConcentratorCb))) {

            /*
             *  Perform a check that for the provided property number there does not exist any concentrator
             *  in the unit list (if this is not done then there would be possibility of same concentrator getting added multiple times)
             */
                // Create the new wo_unit
                if (modifiableWo.getWorkorderCustomTO().getWoUnits() == null)
                    modifiableWo.getWorkorderCustomTO().setWoUnits(new ArrayList<WoUnitCustomTO>());

                // Attaching new Concentrator Unit
                WoUnitCustomTO newConcUnitTO = UnitInstallationUtils.getUnit(modifiableWo, CONSTANTS().WO_UNIT_T__CONCENTRATOR, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                if (newConcUnitTO == null) {
                    // Create a New Concentrator
                    // Add the new Concentrator in the workorder unit list
                    newConcUnitTO = new WoUnitCustomTO();
                    modifiableWo.getWorkorderCustomTO().getWoUnits().add(newConcUnitTO);
                }


                // set the unit model
                newConcUnitTO.setUnitModel(userSelectedConcUnitModel.getCode());
                List<UnitModelIdentifierTO> unitModelIdentifierTOs = userSelectedConcUnitModel.getUnitModelIdentifierTOs();
                if ((unitModelIdentifierTOs != null) && (unitModelIdentifierTOs.size() > 0)) {
                    UnitModelIdentifierTO unitModelIdentifierTO = unitModelIdentifierTOs.get(0);
                    newConcUnitTO.setIdUnitIdentifierT(unitModelIdentifierTO.getIdUnitIdentifierT());
                    if (unitModelIdentifierTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__GIAI.longValue()) {
                        newConcUnitTO.setGiai(modUnitIdentifierValue);
                    } else if (unitModelIdentifierTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO.longValue()) {
                        newConcUnitTO.setPropertyNumberD(modUnitIdentifierValue);
                        newConcUnitTO.setPropertyNumberV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    } else if (unitModelIdentifierTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO.longValue()) {
                        newConcUnitTO.setSerialNumber(modUnitIdentifierValue);
                    }
                }
                // set unit status type
                newConcUnitTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                newConcUnitTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                newConcUnitTO.setIdCase(modifiableWo.getIdCase());
                newConcUnitTO.setIdWoUnitT(ObjectCache.getIdObject(WoUnitTTO.class, ConstantsAstSep.UnitType.CONCENTRATOR.name()).getId());
                // set unit mounted timestamp
                newConcUnitTO.setUnitMountedTimestamp(new Date());
                if (!enforceNewConcentrator && (modyesNewConcentratorCb != null && modyesNewConcentratorCb)) {
                    UnitInstallationUtils.dismantleExistingUnit(modifiableWo, CONSTANTS().WO_UNIT_T__CONCENTRATOR);
                    //UnitInstallationUtils.installUnit(modifiableWo,CONSTANTS().WO_UNIT_T__CONCENTRATOR, Long.valueOf(selectedConcModel), modUnitIdentifierValue, false);
                }
            } else {

                WoUnitCustomTO newConctUnitTO = UnitInstallationUtils.getUnit(modifiableWo.getWorkorderCustomTO().getWoUnits(), modUnitIdentifierValue);
                if (newConctUnitTO != null) {
                    modifiableWo.getWorkorderCustomTO().getWoUnits().remove(newConctUnitTO);
                    newConctUnitTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__FALSE);
                    newConctUnitTO.setIdUnitIdentifierT(CONSTANTS().SYSTEM_BOOLEAN_T__FALSE);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :applyChangesToModifiableWO()", e);
        }
    }

    private void setupListeners() {
        try {
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
                builtInCommCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView != null
                                && buttonView.getId() == R.id.builtInCommCheck) {
                            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED, isChecked ? "YES" : "NO");
                            Log.d("ConcentratorTechPlan", "Built in communication check clicked");
                        }
                    }
                });
            }

            if (yesNewConcentratorCb != null)
                yesNewConcentratorCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //if(isChecked){
                        // Make the other unchecked
                        yesNewConcentratorCb.setChecked(isChecked);
                        stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.YES_NEW_CONCENTRATOR_CHECKBOX, isChecked ? "YES" : "NO");
                        modyesNewConcentratorCb = isChecked;
                        modKeepExistingConcentratorCb = !isChecked;
                        keepExistingConcentratorCb.setChecked(!isChecked);
                        stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.KEEP_EXISTING_CONCENTRATOR_CHECKBOX, isChecked ? "NO" : "YES");
                        enableNewConcOptions(isChecked);
                        //}
                    }
                });

            if (keepExistingConcentratorCb != null)
                keepExistingConcentratorCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //if(isChecked){
                        // Make the other unchecked
                        keepExistingConcentratorCb.setChecked(isChecked);
                        stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.KEEP_EXISTING_CONCENTRATOR_CHECKBOX, isChecked ? "YES" : "NO");
                        yesNewConcentratorCb.setChecked(!isChecked);
                        modyesNewConcentratorCb = !isChecked;
                        modKeepExistingConcentratorCb = isChecked;
                        stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.YES_NEW_CONCENTRATOR_CHECKBOX, isChecked ? "NO" : "YES");
                        enableNewConcOptions(!isChecked);
                        //}
                    }
                });

            //Concentrator model
            if (existingConcModel != null) {
                existingConcModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    //Display Unit Identifier Type depending on the unit models selected
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            stepfragmentValueArray.put("userSelectedConcUnitModel", getSelectedUnitModel().getId());
                        } catch (Exception e) {
                            writeLog(TAG + " :setupListeners()", e);
                        }

                        //One unit model can have only one Unit Identifier Type
                        userSelectedConcUnitModel = getSelectedUnitModel();
                        if ((userSelectedConcUnitModel.getUnitModelIdentifierTOs() != null) && (userSelectedConcUnitModel.getUnitModelIdentifierTOs().size() > 0)) {
                            UnitModelIdentifierTO unitModelIdentifierTO = userSelectedConcUnitModel.getUnitModelIdentifierTOs().get(0);
                            modUnitIdentifierType = ObjectCache.getTypeCode(UnitIdentifierTTO.class, unitModelIdentifierTO.getIdUnitIdentifierT());
                            if ((concentratorIdntfrTv != null) && (GuIUtil.notNullnotEmpty(modUnitIdentifierType))) {
                                concentratorIdntfrTv.setText(GuIUtil.displayUnitIdentifierNameText(modUnitIdentifierType));
                            }
                        } else {
                            Log.d("ConcentratorTechPlan", "Unit Identifier is not present for the selected Unit Model");
                        }
                        if (((UnitModelCustomTO) existingConcModel.getSelectedItem()).getId() != null)
                            selectedConcModel = String.valueOf(((UnitModelCustomTO) existingConcModel.getSelectedItem()).getId().longValue());
                    }

                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });
            }

            //Identifier value
            if (newConcentratorUidTv != null) {
                newConcentratorUidTv.addTextChangedListener(new TextWatcher() {
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
                        modUnitIdentifierValue = newConcentratorUidTv.getText().toString();
                        stepfragmentValueArray.put("modUnitIdentifierValue", modUnitIdentifierValue);
                    }
                });
            }
        } catch (Exception e) {
            writeLog(TAG + " :setupListeners()", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        try{
        if (requestCode == CONCENTRATOR_SCAN_REQUEST
                && resultCode == Activity.RESULT_OK) {
            newConcentratorUidTv.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
            AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(), false);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : onActivityResult() ", e);
        }
    }

    @Override
    public void onClick(View v) {
        try{
        if (v != null
                && v.getId() == R.id.clearPropValueBtn
                && newConcentratorUidTv != null) {
            newConcentratorUidTv.setText("");
        } else if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        } else if (v != null && v.getId() == R.id.scanIdentifierButton) {
            AndroidUtilsAstSep.scanBarCode(this, CONCENTRATOR_SCAN_REQUEST);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : onClick() ", e);
        }
    }

    private void enableNewConcOptions(final boolean enable) {
        existingConcModel.setEnabled(enable);
        newConcentratorUidTv.setEnabled(enable);
        builtInCommCheck.setEnabled(enable);
        scanIdentifierButton.setEnabled(enable);
        clearPropValueBtn.setEnabled(enable);
    }

}