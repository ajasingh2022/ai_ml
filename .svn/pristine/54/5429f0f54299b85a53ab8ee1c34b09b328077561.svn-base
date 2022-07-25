package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.StringUtil;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.ui.adapter.UnitModelAdapter;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.UnitInstallationUtilsStepper;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class RegisterNewCommModulePageFragment extends CustomFragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final int CM_SCAN_REQUEST = 102;

    protected transient CheckBox yesNewCM = null;
    protected transient CheckBox keepExistingCM = null;
    protected transient CheckBox removeExistingCM = null;

    protected transient TextView existingCMReadOnlyTv = null;
    protected transient TextView techPlanTv = null;
    protected transient TextView cmIdntfrTv = null;
    protected transient Spinner cmModelSpinner = null;
    protected transient UnitModelAdapter adapter = null;
    protected transient EditText serialNumberTv = null;
    protected transient ImageButton clearPropValueBtn = null;
    protected transient ImageButton scanIdentifierButton = null;
    protected transient LinearLayout removeExistingCMLayout = null;
    private String TAG = RegisterNewCommModulePageFragment.class.getSimpleName();
    protected Long idWoUnitType;
    protected WoUnitCustomTO cmWoUnitCustomTO;
    protected WoUnitCustomTO holderWoUnitCustomTO;
    protected static final String USER_SELECTED_CHECKBOX_OPTION = "USER_SELECTED_CHECKBOX_OPTION";
    protected static final String USER_SELECTED_UNIT_MODEL_ID = "USER_SELECTED_UNIT_MODEL_ID";
    protected static final String USER_ENTERED_CM_IDENTIFIER = "USER_ENTERED_CM_IDENTIFIER";
    Boolean enforceNewCM;

    public RegisterNewCommModulePageFragment() {
        super();
    }

    public RegisterNewCommModulePageFragment(String stepIdentifier, Long idWoUnitType) {
        super(stepIdentifier);
        this.idWoUnitType = idWoUnitType;
    }

    protected WoUnitCustomTO getExistingCommunicationModule(WoUnitCustomTO holderWoUnitCustomTO) {
        try {
            String cmReferenceId =
                    TypeDataUtil.getOrgOrDiv(
                            holderWoUnitCustomTO.getCmReferenceIdO(),
                            holderWoUnitCustomTO.getCmReferenceIdD(),
                            holderWoUnitCustomTO.getCmReferenceIdV()
                    );
            if (cmReferenceId != null) {
                WoUnitCustomTO woUnitCustomMTO =
                        UnitInstallationUtils.getUnit(AbstractWokOrderActivity.getWorkorderCustomWrapperTO().getWorkorderCustomTO().getWoUnits(),
                                cmReferenceId);
                return woUnitCustomMTO;
            }
        } catch (Exception e) {
            writeLog(TAG + " : getExistingCommunicationModule()", e);
        }
        return null;
    }


    public void initializePageValues() {
        try{
            holderWoUnitCustomTO = UnitInstallationUtils.getUnit(AbstractWokOrderActivity.getWorkorderCustomWrapperTO(),idWoUnitType,CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
       // holderWoUnitCustomTO = UnitInstallationUtils.getCurrentWoUnit(AbstractWokOrderActivity.getWorkorderCustomWrapperTO(), idWoUnitType);
            if(Utils.isNotEmpty(holderWoUnitCustomTO))
           cmWoUnitCustomTO = getExistingCommunicationModule(holderWoUnitCustomTO);
        enforceNewCM = getActivity().getIntent().getExtras().getBoolean(ConstantsAstSep.FlowPageConstants.ENFORCE_NEW_CM);
        } catch (Exception e) {
            writeLog(TAG + " : initializePageValues()", e);
        }
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_communication_module, null);
        initializePageValues();
        initializeUI();
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

    public String evaluateNextPage() {
        final Intent intent = this.getActivity().getIntent();
        final Bundle bundle = new Bundle();
        // Apply the changes and if necessary do validation here
        String nextPage = null;
        try {
            if (yesNewCM.isChecked() || enforceNewCM) {
                bundle.putString(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE, (String) stepfragmentValueArray.get(USER_ENTERED_CM_IDENTIFIER));
                if (GuIUtil.validSpinnerSelection((String) stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID))) {
                    UnitModelCustomTO modSelectedCmUnitModel = ObjectCache.getIdObject(UnitModelCustomTO.class, Long.valueOf((String) stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID)));
                    nextPage = UnitInstallationUtilsStepper.evaluateUnitInstallationNextPage(modSelectedCmUnitModel, cmWoUnitCustomTO,
                            intent, bundle);
                }
            }
            else {
                // Go to online verification
                nextPage = ConstantsAstSep.PageNavConstants.NEXT_PAGE_UNIT_VERIFICATION;
            }
        } catch (Exception e) {
            writeLog(TAG + " :evaluateNextPage()", e);
        }
        return nextPage;
    }

    protected void initializeUI() {
        final View parentView = fragmentView;
        if (parentView != null) {
            yesNewCM = parentView.findViewById(R.id.yesNewCM);
            keepExistingCM = parentView.findViewById(R.id.keepExistingCM);
            removeExistingCM = parentView.findViewById(R.id.removeExistingCM);

            existingCMReadOnlyTv = parentView.findViewById(R.id.existingCMReadOnlyTv);
            techPlanTv = parentView.findViewById(R.id.techPlanTv);
            cmIdntfrTv = parentView.findViewById(R.id.cmIdntfrTv);
            cmModelSpinner = parentView.findViewById(R.id.cmModelSpinner);
            serialNumberTv = parentView.findViewById(R.id.serialNumberTv);
            clearPropValueBtn = parentView.findViewById(R.id.clearPropValueBtn);
            scanIdentifierButton = parentView.findViewById(R.id.scanIdentifierButton);

            //removeExistingCMLayout = (LinearLayout) parentView.findViewById(R.id.removeExistingCMLayout);
        }
    }

    /**
     * Populate Data in UI
     */
    protected void populateData() {
        try{
        //Enable disable Remove Checkbox
        if (cmWoUnitCustomTO == null || enforceNewCM) {
            //removeExistingCMLayout.setVisibility(View.GONE);
            removeExistingCM.setVisibility(View.GONE);
        }

        //Enforce new CM in unit
        if (cmWoUnitCustomTO == null && enforceNewCM) {
            yesNewCM.setChecked(true);
            yesNewCM.setEnabled(false);
            keepExistingCM.setChecked(false);
            keepExistingCM.setEnabled(false);
            enableNewCMOptions(true);
            stepfragmentValueArray.put(USER_SELECTED_CHECKBOX_OPTION, "NEW");
        } else {// In other case both will be enabled
            enableNewCMOptions(false);
        }

        //Populate data for existing unit
        if (cmWoUnitCustomTO != null) {
            String identifier = cmWoUnitCustomTO.getGiai();
            String unitModel = cmWoUnitCustomTO.getUnitModel();
            if (unitModel != null && unitModel.length() != 0) {
                identifier += " " + unitModel;
            }
            existingCMReadOnlyTv.setText(identifier);
        }


        //Fill Spinner for CM Models
        List<UnitModelCustomTO> unitCMModelCustomTOs = UnitInstallationUtils.getUnitModelsOfType(CONSTANTS().UNIT_T__CM);
        GuIUtil.setUpSpinner(getActivity(), cmModelSpinner, unitCMModelCustomTOs, true, this);

        //===========================Set original values========================
        if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION)) && "NEW".equals(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION))) {
            yesNewCM.setChecked(true);
            enableNewCMOptions(true);
        } else if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION)) && "EXISTING".equals(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION))) {
            keepExistingCM.setChecked(true);
            enableNewCMOptions(false);
        } else if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION)) && "REMOVE".equals(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION))) {
            removeExistingCM.setChecked(true);
            enableNewCMOptions(false);
        }

        if (stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID) != null) {
            if (GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID)))) {
                Long selectedUnitModelId = Long.parseLong((String) stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID));
                cmModelSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(cmModelSpinner, selectedUnitModelId));
            }
        }

        if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_ENTERED_CM_IDENTIFIER))) {
            serialNumberTv.setText((CharSequence) stepfragmentValueArray.get(USER_ENTERED_CM_IDENTIFIER));
        }
        } catch (Exception e) {
            writeLog(TAG + " : populateData()", e);
        }
    }

    private void setupListeners() {
        clearPropValueBtn.setOnClickListener(this);
        scanIdentifierButton.setOnClickListener(this);
        yesNewCM.setOnClickListener(this);
        keepExistingCM.setOnClickListener(this);
        removeExistingCM.setOnClickListener(this);


    }

    @Override
    public boolean validateUserInput() {
        stepfragmentValueArray.put(USER_ENTERED_CM_IDENTIFIER, StringUtil.checkNullString(serialNumberTv.getText().toString()).trim());
        boolean status = false;
        if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION))) {
            if ("NEW".equals(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION)) &&
                    GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID))) &&
                    Utils.isNotEmpty(stepfragmentValueArray.get(USER_ENTERED_CM_IDENTIFIER))) {
                status = true;
            } else if ("EXISTING".equals(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION)) ||
                    "REMOVE".equals(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION))) {
                status = true;
            }
        }
        if (status)
            applyChangesToModifiableWO();
        return status;
    }

    @Override
    public void applyChangesToModifiableWO() {
        try {
            cmWoUnitCustomTO = getExistingCommunicationModule(holderWoUnitCustomTO);
            WorkorderCustomWrapperTO workorder = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION)) &&
                    ("NEW".equals(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION)) || "EXISTING".equals(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION)))) {
                    getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE, (String) stepfragmentValueArray.get(USER_ENTERED_CM_IDENTIFIER));
                    if (cmWoUnitCustomTO != null) {
                    try {
                     UnitInstallationUtils.updateOrCreateUnit(
                                new ArrayList<WoUnitCustomTO>(),
                                cmWoUnitCustomTO,
                                null,
                                null,
                                CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED,
                                cmWoUnitCustomTO.getIdWoUnitT(),
                                false,
                                true,
                                workorder.getIdCase());

                          } catch (Exception e) {
                        writeLog(TAG + " :applyChangesToModifiableWO()", e);
                    }
                }

                if ("NEW".equals(stepfragmentValueArray.get(USER_SELECTED_CHECKBOX_OPTION))) {
                    if (GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID)))) {
                        UnitModelCustomTO selectedCmUnitModel = ObjectCache.getIdObject(UnitModelCustomTO.class, Long.valueOf(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_UNIT_MODEL_ID))));
                        UnitTTO unitTTO = ObjectCache.getType(UnitTTO.class, selectedCmUnitModel.getIdUnitT());
                        WoUnitTTO woUnitTTO = ObjectCache.getType(WoUnitTTO.class, unitTTO.getCode());
                        //add new
                        List<WoUnitCustomTO> newUnits = new ArrayList<WoUnitCustomTO>();
                           WoUnitCustomTO theNewUnit = UnitInstallationUtils.getUnit(workorder, woUnitTTO.getId(), CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                        try {
                            if (workorder.getWorkorderCustomTO().getWoUnits().contains(theNewUnit))
                                workorder.getWorkorderCustomTO().getWoUnits().remove(theNewUnit);
                            theNewUnit = UnitInstallationUtils.updateOrCreateUnit(
                                    newUnits,
                                    theNewUnit,
                                    selectedCmUnitModel.getCode(),
                                    String.valueOf(stepfragmentValueArray.get(USER_ENTERED_CM_IDENTIFIER)),
                                    CONSTANTS().WO_UNIT_STATUS_T__MOUNTED,
                                    woUnitTTO.getId(),
                                    true,
                                    false,
                                    workorder.getIdCase()
                            );
                        } catch (Exception e) {
                            writeLog(this.getClass().getSimpleName() + ":applyChangesToModifiableWO() :Error in mounting the unit", e);
                        }

                        WoUnitCustomTO woUnitCustomMTO = UnitInstallationUtils.getCurrentWoUnit(workorder, idWoUnitType);

                        woUnitCustomMTO.setCmIdUnitIdentifierTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                        woUnitCustomMTO.setCmIdUnitIdentifierTD(CONSTANTS().UNIT_IDENTIFIER_T__GIAI);

                        woUnitCustomMTO.setCmReferenceIdV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                        woUnitCustomMTO.setCmReferenceIdD(String.valueOf(stepfragmentValueArray.get(USER_ENTERED_CM_IDENTIFIER)));


                        List<WoUnitCustomTO> unitsArrayOrg = workorder.getWorkorderCustomTO().getWoUnits();
                        if (unitsArrayOrg == null) {
                            unitsArrayOrg = new ArrayList<WoUnitCustomTO>();
                        }
                        unitsArrayOrg.add(theNewUnit);
                        workorder.getWorkorderCustomTO().setWoUnits(unitsArrayOrg);
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :applyChangesToModifiableWO()", e);
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        } else if (v != null && v.getId() == R.id.clearPropValueBtn) {
            // Clear the entered Identifier
            serialNumberTv.setText("");
        } else if (v != null && v.getId() == R.id.scanIdentifierButton) {
            AndroidUtilsAstSep.scanBarCode(this, CM_SCAN_REQUEST);
        } else if (v != null &&
                (v.getId() == R.id.yesNewCM || v.getId() == R.id.keepExistingCM || v.getId() == R.id.removeExistingCM)) {
            onCheckboxClicked(v);
        }
    }

    /**
     * Check box clicked handler
     *
     * @param view
     */
    public void onCheckboxClicked(View view) {
        String userSelectedOptionValue = null;
        int i = view.getId();
        try {
            if (i == R.id.yesNewCM) {
                keepExistingCM.setChecked(false);
                removeExistingCM.setChecked(false);
                enableNewCMOptions(true);
                userSelectedOptionValue = "NEW";

            } else if (i == R.id.keepExistingCM) {
                yesNewCM.setChecked(false);
                removeExistingCM.setChecked(false);
                enableNewCMOptions(false);
                userSelectedOptionValue = "EXISTING";

            } else if (i == R.id.removeExistingCM) {
                yesNewCM.setChecked(false);
                keepExistingCM.setChecked(false);
                enableNewCMOptions(false);
                userSelectedOptionValue = "REMOVE";

            }
            stepfragmentValueArray.put(USER_SELECTED_CHECKBOX_OPTION, userSelectedOptionValue);
        } catch (Exception e) {
            writeLog(TAG + " :onCheckboxClicked()", e);
        }
    }

    private void enableNewCMOptions(final boolean enable) {
        // CM Model Spinner drop down
        cmModelSpinner.setEnabled(enable);
        // Serial Number and clear button
        serialNumberTv.setEnabled(enable);
        clearPropValueBtn.setEnabled(enable);
        scanIdentifierButton.setEnabled(enable);
        if (!enable) {
            serialNumberTv.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        if (requestCode == CM_SCAN_REQUEST
                && resultCode == Activity.RESULT_OK) {
            serialNumberTv.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
            AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(), false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        stepfragmentValueArray.put(USER_SELECTED_UNIT_MODEL_ID, String.valueOf(id));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }
}