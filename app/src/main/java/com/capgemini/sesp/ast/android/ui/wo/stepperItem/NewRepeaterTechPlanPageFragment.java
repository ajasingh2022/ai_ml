package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

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
import com.capgemini.sesp.ast.android.module.util.StringUtil;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.adapter.UnitModelAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.UnitInstallationUtilsStepper;
import com.google.android.material.textfield.TextInputLayout;
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

public class NewRepeaterTechPlanPageFragment extends CustomFragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private transient Spinner existingRepeatModel = null;
    private transient UnitModelAdapter adapter = null;
    private transient TextView existingRepeaterTv = null;
    private transient TextView techPlanTv = null;
    private transient TextView repeaterIdntfrTv = null;
    private transient TextView newRepeaterUidTv = null;
    private transient ImageButton clearPropValueBtn = null;
    private transient ImageButton scanIdentifierButton = null;
    protected transient CheckBox builtInCommCheck = null;
    private transient Dialog dialog = null;

    private transient TextView header = null;
    private transient TextInputLayout existingRepeater = null;
    private transient TextView repeaterModel = null;

    private transient int SIMCARD_SCAN_REQUEST = 101;
    private transient CheckBox yesNewRepeaterCb = null;
    private transient CheckBox keepExistingRepeaterCb = null;
    protected Intent nextPageIntentData = new Intent();
    protected UnitModelCustomTO userSelectedRepeatUnitModel;
    protected boolean enforceNewRepeater;
    private String TAG = NewRepeaterTechPlanPageFragment.class.getSimpleName();
    static final String UNIT_MODEL_SELECTED = "UNIT_MODEL_SELECTED";
    static final String UNIT_IDENTIFIER = "UNIT_IDENTIFIER";
    static final String BUILT_IN_COMM_MODULE = "BUILT_IN_COMM_MODULE";
    static final String YES_NEW_REPEATER_CHECKBOX = "YES_NEW_REPEATER_CHECKBOX";
    static final String KEEP_EXISTING_REPEATER_CHECKBOX = "KEEP_EXISTING_REPEATER_CHECKBOX";
    static final String ORG_UNIT_IDENTIFIER_TYPE = "ORG_UNIT_IDENTIFIER_TYPE";
    static final String ORG_UNIT_IDENTIFIER_VALUE = "ORG_UNIT_IDENTIFIER_VALUE";
    static final String ORG_SELECTED_REPEATER_UNIT_MODEL = "ORG_SELECTED_REPEATER_UNIT_MODEL";
    static final String MOD_UNIT_IDENTIFIER_TYPE = "MOD_UNIT_IDENTIFIER_TYPE";
    static final String MOD_UNIT_IDENTIFIER = "MOD_UNIT_IDENTIFIER";
    static final String USER_SELECTED_REPEATER_UNIT_MODEL = "USER_SELECTED_REPEATER_UNIT_MODEL";

    public NewRepeaterTechPlanPageFragment() {
        super();
    }


    public NewRepeaterTechPlanPageFragment(String stepIdentifier, Boolean enforceNewRepeater) {
        super(stepIdentifier);
        this.enforceNewRepeater = enforceNewRepeater;
    }


    @Override
    public void initializePageValues() {
        try {
            super.initializePageValues();
            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            WoUnitCustomTO newRepeatUnitTO = UnitInstallationUtils.getUnit(modifiableWo, CONSTANTS().WO_UNIT_T__REPEATER, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
            if (newRepeatUnitTO != null) { //Set originally selected Repeater model
                stepfragmentValueArray.put(ORG_SELECTED_REPEATER_UNIT_MODEL, newRepeatUnitTO.getUnitModel());
                stepfragmentValueArray.put(ORG_UNIT_IDENTIFIER_VALUE, UnitInstallationUtils.getUnitIdentifierValue(newRepeatUnitTO));
                stepfragmentValueArray.put(ORG_UNIT_IDENTIFIER_TYPE, UnitInstallationUtils.getUnitIdentifierName(newRepeatUnitTO));
            }
        } catch (Exception e) {
            writeLog(TAG + "  : initializePageValues() ", e);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_new_concentrator_tech_plan, null);
        initializePageValues();
        WorkorderUtils.unitType = ConstantsAstSep.UnitType.REPEATER;
        initViews();
        setupListeners();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void initViews() {
        final View parentView = fragmentView;
        if (parentView != null) {
            techPlanTv = parentView.findViewById(R.id.techPlanTv);
            existingRepeatModel = parentView.findViewById(R.id.existingConcModel);
            existingRepeaterTv = parentView.findViewById(R.id.existingConcentratorTv);
            existingRepeaterTv.setEnabled(false);
            newRepeaterUidTv = parentView.findViewById(R.id.newConcentratorUidTv);
            clearPropValueBtn = parentView.findViewById(R.id.clearPropValueBtn);
            scanIdentifierButton = parentView.findViewById(R.id.scanIdentifierButton);
            builtInCommCheck = parentView.findViewById(R.id.builtInCommCheck);
            repeaterIdntfrTv = parentView.findViewById(R.id.concentratorIdntfrTv);

            header = parentView.findViewById(R.id.header);
            header.setText(getResources().getString(R.string.new_repeater_header));

            existingRepeater = parentView.findViewById(R.id.existingConcRepeaterTv);
            existingRepeater.setHint(getResources().getString(R.string.existing_repeater));

            repeaterModel = parentView.findViewById(R.id.model);
            repeaterModel.setText(getResources().getString(R.string.repeater_model));

            if (!enforceNewRepeater) {
                parentView.findViewById(R.id.newOrExistingConcentrator).setVisibility(View.VISIBLE);
                yesNewRepeaterCb = parentView.findViewById(R.id.yesNewConcentratorCb);
                keepExistingRepeaterCb = parentView.findViewById(R.id.keepExistingConcentratorCb);
                enableNewRepeaterOptions(false);
            }
        }
        techPlanTv.setEnabled(false);
        repeaterIdntfrTv.setEnabled(false);
    }

    private UnitModelCustomTO getSelectedUnitModel() {
        return (UnitModelCustomTO) existingRepeatModel.getSelectedItem();
    }

    private void populateData() {
        final WorkorderCustomWrapperTO originalWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        // Populate existing Repeater number
        try {
            if (originalWo != null) {
                WoUnitCustomTO existingRepeater = UnitInstallationUtils.getUnit(originalWo, CONSTANTS().WO_UNIT_T__REPEATER, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
                final String existingRepeaterNumber = existingRepeater != null ? UnitInstallationUtils.getUnitIdentifierValue(existingRepeater) : "";//Get existing Repeater Number
                final String repeaterIdentifier = existingRepeater != null ? UnitInstallationUtils.getUnitIdentifierName(existingRepeater) : "";//Get existing Repeater Identifier
                final String hardwareCfgModel = WorkorderUtils.getTechnologyPlanName(originalWo);// Get technical planning of the work-order
                if (GuIUtil.notNullnotEmpty(existingRepeaterNumber)) {
                    existingRepeaterTv.setText(existingRepeaterNumber);
                    Log.d(TAG, "Existing Repeater number obtained as = " + existingRepeaterNumber);
                }
                if (repeaterIdntfrTv != null) {
                    repeaterIdntfrTv.setText(GuIUtil.displayUnitIdentifierNameText(repeaterIdentifier));
                }
                if (hardwareCfgModel != null) {
                    techPlanTv.setText(hardwareCfgModel);
                }
            }

            // Get all repeater models
            // =========================================================================================================
            // Fetch all code names for available system type names
            List<UnitModelCustomTO> unitModelCustomTOs = UnitInstallationUtils.getUnitModelsOfType(CONSTANTS().UNIT_T__REPEATER);
            if (unitModelCustomTOs != null && !unitModelCustomTOs.isEmpty()) {
                adapter = new UnitModelAdapter(this, unitModelCustomTOs);
                existingRepeatModel.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.d(TAG, "No unit models exists for unit type = Repeater");
            }

            // ==========================================================================================================

            //===============================Set the preselected values==============================
            if (stepfragmentValueArray.get(UNIT_MODEL_SELECTED) != null) {

                existingRepeatModel.setSelection(GuIUtil.getPositionOfItemInSpinner(existingRepeatModel, Long.parseLong(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED)))));
            }
            if (stepfragmentValueArray.get("userSelectedRepeatUnitModel") != null) {
                userSelectedRepeatUnitModel = ObjectCache.getType(UnitModelCustomTO.class, (long) Double.parseDouble(String.valueOf(stepfragmentValueArray.get("userSelectedRepeatUnitModel"))));
                if (userSelectedRepeatUnitModel != null) {
                    existingRepeatModel.setSelection(((UnitModelAdapter) existingRepeatModel.getAdapter()).getPositionOfItem(userSelectedRepeatUnitModel));
                }
            }
            if (stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER_TYPE) != null)
                repeaterIdntfrTv.setText(GuIUtil.displayUnitIdentifierNameText(String.valueOf(stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER_TYPE))));
            if (stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER) != null)
                newRepeaterUidTv.setText(String.valueOf(stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER)));
            if ("YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(BUILT_IN_COMM_MODULE)))) {
                builtInCommCheck.setChecked(true);
            }
            if (stepfragmentValueArray.get(YES_NEW_REPEATER_CHECKBOX) != null && "YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(YES_NEW_REPEATER_CHECKBOX)))) {
                yesNewRepeaterCb.setChecked(true);
                enableNewRepeaterOptions(true);
            }
            if (stepfragmentValueArray.get(KEEP_EXISTING_REPEATER_CHECKBOX) != null && "YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(KEEP_EXISTING_REPEATER_CHECKBOX)))) {
                keepExistingRepeaterCb.setChecked(true);
            }
            //=======================================================================================
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }

    }


    public void showPromptUserAction() {
        try {
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
        // User is good to proceed if the user has provided new repeater uid value only
        // Check the identifier value if its provided or not
        try {
            if (null != (stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER)) ||
                    (yesNewRepeaterCb != null && yesNewRepeaterCb.isChecked() && GuIUtil.notNullnotEmpty((String) (stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER)))) ||
                    (keepExistingRepeaterCb != null && keepExistingRepeaterCb.isChecked())) {
                status = true;

                applyChangesToModifiableWO();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : validateUserInput() ", e);
        }
        return status;
    }

    @Override
    public String evaluateNextPage() {
        String nextPage = null;
        try {
            if (this != null) {

                getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE, String.valueOf(stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER)));
                getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED, this.builtInCommCheck.isChecked());
                UnitModelCustomTO selectedUnitModel = ObjectCache.getIdObject(UnitModelCustomTO.class, String.valueOf(stepfragmentValueArray.get(USER_SELECTED_REPEATER_UNIT_MODEL)));
                nextPage = UnitInstallationUtilsStepper.evaluateUnitInstallationNextPage(selectedUnitModel, null,
                        getActivity().getIntent(), getActivity().getIntent().getExtras());
            }
        } catch (Exception e) {
            writeLog(TAG + "  : evaluateNextPage() ", e);
        }
        return nextPage;
    }


    @Override
    public void applyChangesToModifiableWO() {
        try {
            getUserChoiceValuesForPage();

            getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE, String.valueOf(stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER)));
            getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED, this.builtInCommCheck.isChecked());

            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        /*
         *  Perform a check that for the provided property number there does not exist any Repeater
         *  in the unit list (if this is not done then there would be possibility of same Repeater getting added multiple times)
         */
            if (enforceNewRepeater || (!enforceNewRepeater && (stepfragmentValueArray.get(YES_NEW_REPEATER_CHECKBOX) != null && "YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(YES_NEW_REPEATER_CHECKBOX))))
                    && (stepfragmentValueArray.get(KEEP_EXISTING_REPEATER_CHECKBOX) != null && "NO".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(KEEP_EXISTING_REPEATER_CHECKBOX)))))) {
                // Create the new wo_unit
                if (modifiableWo.getWorkorderCustomTO().getWoUnits() == null)
                    modifiableWo.getWorkorderCustomTO().setWoUnits(new ArrayList<WoUnitCustomTO>());

                // Attaching new Repeater Unit
                WoUnitCustomTO newRepeatUnitTO = UnitInstallationUtils.getUnit(modifiableWo, CONSTANTS().WO_UNIT_T__REPEATER, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                if (newRepeatUnitTO == null) {
                    // Create a New Repeater
                    // Add the new Repeater in the workorder unit list
                    newRepeatUnitTO = new WoUnitCustomTO();
                    modifiableWo.getWorkorderCustomTO().getWoUnits().add(newRepeatUnitTO);
                }

                // set the unit model
                newRepeatUnitTO.setUnitModel(userSelectedRepeatUnitModel.getCode());
                List<UnitModelIdentifierTO> unitModelIdentifierTOs = userSelectedRepeatUnitModel.getUnitModelIdentifierTOs();
                if ((unitModelIdentifierTOs != null) && (unitModelIdentifierTOs.size() > 0)) {
                    UnitModelIdentifierTO unitModelIdentifierTO = unitModelIdentifierTOs.get(0);
                    newRepeatUnitTO.setIdUnitIdentifierT(unitModelIdentifierTO.getIdUnitIdentifierT());
                    if (unitModelIdentifierTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__GIAI.longValue()) {
                        newRepeatUnitTO.setGiai(String.valueOf(stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER)));
                    } else if (unitModelIdentifierTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO.longValue()) {
                        newRepeatUnitTO.setPropertyNumberD(String.valueOf(stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER)));
                        newRepeatUnitTO.setPropertyNumberV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    } else if (unitModelIdentifierTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO.longValue()) {
                        newRepeatUnitTO.setSerialNumber(String.valueOf(stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER)));
                    }
                }
                // set unit status type
                newRepeatUnitTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                newRepeatUnitTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                newRepeatUnitTO.setIdCase(modifiableWo.getIdCase());
                newRepeatUnitTO.setIdWoUnitT(ObjectCache.getIdObject(WoUnitTTO.class, ConstantsAstSep.UnitType.REPEATER.name()).getId());
                // set unit mounted timestamp
                newRepeatUnitTO.setUnitMountedTimestamp(new Date());
                if (!enforceNewRepeater && (stepfragmentValueArray.get(YES_NEW_REPEATER_CHECKBOX) != null && "YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(YES_NEW_REPEATER_CHECKBOX))))) {
                    UnitInstallationUtils.dismantleExistingUnit(modifiableWo, CONSTANTS().WO_UNIT_T__REPEATER);
                }
            } else {
                WoUnitCustomTO newRepeaterUnitTO = UnitInstallationUtils.getUnit(modifiableWo.getWorkorderCustomTO().getWoUnits(), String.valueOf(stepfragmentValueArray.get(UNIT_IDENTIFIER)));
                if (newRepeaterUnitTO != null) {
                    modifiableWo.getWorkorderCustomTO().getWoUnits().remove(newRepeaterUnitTO);
                    newRepeaterUnitTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__FALSE);
                    newRepeaterUnitTO.setIdUnitIdentifierT(CONSTANTS().SYSTEM_BOOLEAN_T__FALSE);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }
    }


    public void getUserChoiceValuesForPage() {
        try {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.ENFORCE_NEW_REPEATER, enforceNewRepeater ? "YES" : "NO");
        } catch (Exception e) {
            writeLog(TAG + "  : getUserChoiceValuesForPage() ", e);
        }
    }

    private void setupListeners() {
        try {
            scanIdentifierButton.setOnClickListener(this);

            // Property value clear button
            if (clearPropValueBtn != null) {
                clearPropValueBtn.setOnClickListener(this);
            }

            // Built in communication module
            if (builtInCommCheck != null) {
                builtInCommCheck.setOnCheckedChangeListener(this);
            }

            if (yesNewRepeaterCb != null)
                yesNewRepeaterCb.setOnCheckedChangeListener(this);

            if (keepExistingRepeaterCb != null)
                keepExistingRepeaterCb.setOnCheckedChangeListener(this);

            //Repeater model
            if (existingRepeatModel != null) {
                existingRepeatModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    //Display Unit Identifier Type depending on the unit models selected
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                        //One unit model can have only one Unit Identifier Type
                        stepfragmentValueArray.put("userSelectedRepeatUnitModel", getSelectedUnitModel().getId());
                        userSelectedRepeatUnitModel = getSelectedUnitModel();
                        stepfragmentValueArray.put(USER_SELECTED_REPEATER_UNIT_MODEL, userSelectedRepeatUnitModel.getCode());
                        if ((userSelectedRepeatUnitModel.getUnitModelIdentifierTOs() != null) && (userSelectedRepeatUnitModel.getUnitModelIdentifierTOs().size() > 0)) {
                            UnitModelIdentifierTO unitModelIdentifierTO = userSelectedRepeatUnitModel.getUnitModelIdentifierTOs().get(0);
                            stepfragmentValueArray.put(MOD_UNIT_IDENTIFIER_TYPE, ObjectCache.getTypeCode(UnitIdentifierTTO.class, unitModelIdentifierTO.getIdUnitIdentifierT()));
                            if ((repeaterIdntfrTv != null) && (null != (stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER_TYPE)))) {
                                repeaterIdntfrTv.setText(GuIUtil.displayUnitIdentifierNameText(String.valueOf(stepfragmentValueArray.get(MOD_UNIT_IDENTIFIER_TYPE))));
                            }
                        } else {
                            Log.d(TAG, "Unit Identifier is not present for the selected Unit Model");
                        }
                        stepfragmentValueArray.put(UNIT_MODEL_SELECTED, String.valueOf(id));
                    }

                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });
            }

            //Identifier value
            if (newRepeaterUidTv != null) {
                newRepeaterUidTv.addTextChangedListener(new TextWatcher() {
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
                        stepfragmentValueArray.put(MOD_UNIT_IDENTIFIER, StringUtil.checkNullString(s.toString()).trim());
                    }
                });
            }
        } catch (Exception e) {
            writeLog(TAG + "  : setUPListeners() ", e);
        }
    }

    private void enableNewRepeaterOptions(final boolean enable) {
        newRepeaterUidTv.setEnabled(enable);
        existingRepeatModel.setEnabled(enable);
        builtInCommCheck.setEnabled(enable);
        scanIdentifierButton.setEnabled(enable);
        clearPropValueBtn.setEnabled(enable);
        if (!enable)
        {
            newRepeaterUidTv.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v != null
                    && v.getId() == R.id.clearPropValueBtn
                    && newRepeaterUidTv != null) {
                newRepeaterUidTv.setText("");
            } else if (v != null
                    && v.getId() == R.id.okButtonYesNoPage
                    && dialog != null) {
                dialog.dismiss();
                dialog = null;
            } else if (v != null && v.getId() == R.id.scanIdentifierButton) {
                AndroidUtilsAstSep.scanBarCode(this, SIMCARD_SCAN_REQUEST);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : onClick() ", e);
        }
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView,
                                 final boolean isChecked) {
        try {
            if (buttonView != null
                    && buttonView.getId() == R.id.builtInCommCheck) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED, isChecked ? "YES" : "NO");
                stepfragmentValueArray.put(BUILT_IN_COMM_MODULE, isChecked ? "YES" : "NO");
                Log.d(TAG, "Built in communication check clicked");
            }

            if (buttonView != null && buttonView.getId() == R.id.yesNewConcentratorCb) {
                // Make the other unchecked
                yesNewRepeaterCb.setChecked(isChecked);
                stepfragmentValueArray.put(YES_NEW_REPEATER_CHECKBOX, isChecked ? "YES" : "NO");
                keepExistingRepeaterCb.setChecked(!isChecked);
                stepfragmentValueArray.put(KEEP_EXISTING_REPEATER_CHECKBOX, isChecked ? "NO" : "YES");
                enableNewRepeaterOptions(isChecked);
            } else if (buttonView != null && buttonView.getId() == R.id.keepExistingConcentratorCb) {
                // Make the other unchecked
                keepExistingRepeaterCb.setChecked(isChecked);
                stepfragmentValueArray.put(KEEP_EXISTING_REPEATER_CHECKBOX, isChecked ? "YES" : "NO");
                yesNewRepeaterCb.setChecked(!isChecked);
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.YES_NEW_REPEATER_CHECKBOX, isChecked ? "NO" : "YES");
                enableNewRepeaterOptions(!isChecked);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : onCheckedChanged() ", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        try {
            if (requestCode == SIMCARD_SCAN_REQUEST
                    && resultCode == Activity.RESULT_OK) {
                newRepeaterUidTv.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
                AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(), false);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : onActivityResult() ", e);
        }
    }
}