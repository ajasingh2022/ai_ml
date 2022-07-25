package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.ui.adapter.UnitModelAdapter;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.UnitInstallationUtilsStepper;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelIdentifierTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class RegisterNewSimCardInfoPageFragment extends CustomFragment implements View.OnClickListener {

    protected transient Dialog dialog = null;

    protected transient CheckBox yesNewSimCb = null;
    protected transient CheckBox keepExistingSimCb = null;
    protected transient CheckBox removeExistingSimCb = null;

    protected transient TextView existingIccReadOnlyTv = null;
    protected transient Spinner ispSpinner = null;
    protected transient UnitModelAdapter adapter = null;
    protected transient EditText iccPropertyTv = null;
    protected transient ImageButton scanIdentifierButton = null;
    protected transient ImageButton clearPropValueBtn = null;

    private transient int SIMCARD_SCAN_REQUEST = 101;
    private String TAG = RegisterNewSimCardInfoPageFragment.class.getSimpleName();
    protected long idUnitType = 0;
    protected transient WoUnitCustomTO existingCMModel;
    protected String userChoice;
    protected String userSelectedSimModel;
    protected String simICCNumber;
    protected String unitIdenValue;

    /**********PAGE PREFERENCE KEYS *****************/
    protected static final String USER_CHOICE_SIMCARD_OPTION = "USER_CHOICE_SIMCARD_OPTION";
    protected static final String USER_SELECTED_SIM_CARD_MODEL = "USER_SELECTED_SIM_CARD_MODEL";
    protected static final String USER_ENTERED_SIM_ICC_NUMBER = "USER_ENTERED_SIM_ICC_NUMBER";
    protected static final String ENFORCE_NEW_SIM_CARD = "ENFORCE_NEW_SIM_CARD";

    public RegisterNewSimCardInfoPageFragment(String stepIdentifier, long idUnitType) {
        super(stepIdentifier);
        this.idUnitType = idUnitType;
    }


    public void initializePageValues() {
        existingCMModel = UnitInstallationUtils.getUnit(AbstractWokOrderActivity.getWorkorderCustomWrapperTO(),
                CONSTANTS().WO_UNIT_T__CM,
                CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_simcard_in_concentrator, null);
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

    protected void initializeUI() {
        final View parentView = fragmentView;
        if (parentView != null) {
            yesNewSimCb = parentView.findViewById(R.id.yesNewSimCb);
            keepExistingSimCb = parentView.findViewById(R.id.keepExistingSimCb);
            removeExistingSimCb = parentView.findViewById(R.id.removeExistingSimCb);

            existingIccReadOnlyTv = parentView.findViewById(R.id.existingIccReadOnlyTv);
            ispSpinner = parentView.findViewById(R.id.ispSpinner);

            iccPropertyTv = parentView.findViewById(R.id.iccPropertyTv);
            clearPropValueBtn = parentView.findViewById(R.id.clearPropValueBtn);
            scanIdentifierButton = parentView.findViewById(R.id.scanIdentifierButton);

        }
    }

    protected void populateData() {
        // Get the value whether new Sim is Required.

        //**** CHECK ****

        try {

            final Intent intent = this.getActivity().getIntent();
            if (intent != null && intent.getExtras() != null) {
                // Get the new meter/concentrator/repeater unit identifier value
                if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_ENTERED_SIM_ICC_NUMBER))) {
                    unitIdenValue = (String) stepfragmentValueArray.get(USER_ENTERED_SIM_ICC_NUMBER);
                }
                final Bundle bundle = intent.getExtras();
                stepfragmentValueArray.put(ENFORCE_NEW_SIM_CARD, bundle.getBoolean(ConstantsAstSep.FlowPageConstants.ENFORCE_NEW_SIM, false) ? "YES" : "NO");
            }

            if (Utils.isNotEmpty(stepfragmentValueArray.get(ENFORCE_NEW_SIM_CARD))
                    && "YES".equals(String.valueOf(stepfragmentValueArray.get(ENFORCE_NEW_SIM_CARD)))) {
                yesNewSimCb.setChecked(true);
                yesNewSimCb.setEnabled(false);
                keepExistingSimCb.setChecked(false);
                keepExistingSimCb.setEnabled(false);
                removeExistingSimCb.setChecked(false);
                removeExistingSimCb.setEnabled(false);
                enableNewSimOptions(true);

            } else { // In other case both will be enabled
                enableNewSimOptions(false);
            }

            // Get existing ICC number
            final String existingICC = UnitInstallationUtils.getICCNumberForSimCard(AbstractWokOrderActivity.getWorkorderCustomWrapperTO(),
                    idUnitType);
            if (Utils.isNotEmpty(existingICC)) {
                existingIccReadOnlyTv.setText(existingICC);
            } else {
                //If there is no existing sim card, then remove existing combobox and label will be hidden
                removeExistingSimCb.setVisibility(View.GONE);
            }

            // Get all ISP Sim Model
            // =========================================================================================================
            // Fetch all code names for available system type names
            List<UnitModelCustomTO> unitSimModelCustomTOs = UnitInstallationUtils.getUnitModelsOfType(CONSTANTS().UNIT_T__SIMCARD);
            GuIUtil.setUpSpinner(getContext(), ispSpinner, unitSimModelCustomTOs, unitSimModelCustomTOs.size() > 1, new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!String.valueOf(id).equals("-1")) {
                        userSelectedSimModel = String.valueOf(id);
                    }
                    stepfragmentValueArray.put(USER_SELECTED_SIM_CARD_MODEL, String.valueOf(id));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            //===============Initialize previous data================

            userChoice = String.valueOf(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION));
            simICCNumber = String.valueOf(stepfragmentValueArray.get(USER_ENTERED_SIM_ICC_NUMBER));
            userSelectedSimModel = String.valueOf(stepfragmentValueArray.get(USER_SELECTED_SIM_CARD_MODEL));

            //===============Populate previous data================
            if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION))) {
                if ("NEW".equals(String.valueOf(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION)))) {
                    yesNewSimCb.setChecked(true);
                    if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_ENTERED_SIM_ICC_NUMBER))) {
                        iccPropertyTv.setText(String.valueOf(stepfragmentValueArray.get(USER_ENTERED_SIM_ICC_NUMBER)));
                        if (null != stepfragmentValueArray.get(USER_SELECTED_SIM_CARD_MODEL))
                            if (GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_SIM_CARD_MODEL)))) {
                                ispSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(ispSpinner, Long.valueOf(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_SIM_CARD_MODEL)))));
                            } else if (unitSimModelCustomTOs.size() == 1) {
                                ispSpinner.setSelection(0);
                            }
                    }
                    enableNewSimOptions(true);
                } else if ("EXISTING".equals(String.valueOf(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION)))) {
                    keepExistingSimCb.setChecked(true);
                    enableNewSimOptions(false);
                } else if ("REMOVE".equals(String.valueOf(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION)))) {
                    removeExistingSimCb.setChecked(true);
                    enableNewSimOptions(false);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : populateData()", e);
        }

    }

    protected void setupListeners() {
        clearPropValueBtn.setOnClickListener(this);
        scanIdentifierButton.setOnClickListener(this);
        yesNewSimCb.setOnClickListener(this);
        keepExistingSimCb.setOnClickListener(this);
        removeExistingSimCb.setOnClickListener(this);

        iccPropertyTv.addTextChangedListener(new TextWatcher() {
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
            }
        });
    }

    @Override
    public boolean validateUserInput() {
        boolean status = false;
        try {
            stepfragmentValueArray.put(USER_ENTERED_SIM_ICC_NUMBER, StringUtil.checkNullString(iccPropertyTv.getText().toString()).trim());

            if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION))) {
                if ("NEW".equals(String.valueOf(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION))) &&
                        GuIUtil.validSpinnerSelection(USER_SELECTED_SIM_CARD_MODEL) &&
                        Utils.isNotEmpty(stepfragmentValueArray.get(USER_ENTERED_SIM_ICC_NUMBER))) {
                    status = true;
                } else if ("EXISTING".equals(String.valueOf(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION))) ||
                        "REMOVE".equals(String.valueOf(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION)))) {
                    status = true;
                }
            }
            if (status)
                applyChangesToModifiableWO();
        } catch (Exception e) {
            writeLog(TAG + " : validateUserInput()", e);
        }
        return status;
    }

    public String evaluateNextPage() {
        String nextPage = null;
        try {
            // Apply the changes and if necessary do validation here
            if (this != null) {
                final Intent intent = getActivity().getIntent();
                intent.putExtra(ConstantsAstSep.FlowPageConstants.SIM_CARD_INSTALLED, true);
                intent.putExtra(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED, intent.getBooleanExtra(ConstantsAstSep.FlowPageConstants.BUILT_IN_CM_MODULE_CHECKED, false));
                //Check for newly mounted units
                WoUnitCustomTO parentUnit = UnitInstallationUtils.getUnit(AbstractWokOrderActivity.getWorkorderCustomWrapperTO(),
                        idUnitType, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                if (parentUnit == null) {
                    //No newly mounted units, retrieve the existing unit
                    parentUnit = UnitInstallationUtils.getUnit(AbstractWokOrderActivity.getWorkorderCustomWrapperTO(),
                            idUnitType, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
                }

                UnitModelCustomTO selectedUnitModel = ObjectCache.getIdObject(UnitModelCustomTO.class, parentUnit.getUnitModel());
                nextPage = UnitInstallationUtilsStepper.evaluateUnitInstallationNextPage(selectedUnitModel, existingCMModel, intent, intent.getExtras());


            }
        } catch (Exception e) {
            writeLog(TAG + "  : evaluateNextPage() ", e);
        }
        return nextPage;
    }

    @Override
    public void applyChangesToModifiableWO() {
        //undoChanges();
        try {
            stepfragmentValueArray.put(USER_ENTERED_SIM_ICC_NUMBER, StringUtil.checkNullString(iccPropertyTv.getText().toString()).trim());

            final WorkorderCustomWrapperTO woModifiable = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            final Intent intent = this.getActivity().getIntent();
            if (intent != null) {
                WoUnitCustomTO parentUnit = null;
                String parentUnitIdentifierValue = null;
                if (Utils.isNotEmpty(intent.getStringExtra(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE))) {
                    parentUnitIdentifierValue = intent.getStringExtra(ConstantsAstSep.FlowPageConstants.NEW_UNIT_IDENTIFIER_VALUE);
                }
                if (parentUnitIdentifierValue != null) {
                    // Get Mounted Unit
                    parentUnit = UnitInstallationUtils.getUnit(woModifiable.getWorkorderCustomTO().getWoUnits(), parentUnitIdentifierValue);
                }

                if (parentUnit == null) {
                    //No new Mounted unit, get existing unit
                    parentUnit = UnitInstallationUtils.getUnit(woModifiable, idUnitType, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
                }
                if (parentUnit != null) {
                    // Associate sim card information along the work-order data
                    if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION)) &&
                            ("NEW".equals(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION)) || "REMOVE".equals(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION)))) {

                        //Dismantle existing SIM card
                        UnitInstallationUtils.dismantleExistingUnit(woModifiable, CONSTANTS().WO_UNIT_T__SIMCARD);

                        if ("NEW".equals(stepfragmentValueArray.get(USER_CHOICE_SIMCARD_OPTION))) {
                            // Attaching new SIM card
                            //  WoUnitCustomTO newSimCardUnitTO = UnitInstallationUtils.getUnit(woModifiable,CONSTANTS().WO_UNIT_T__SIMCARD,CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                            WoUnitCustomTO newSimCardUnitTO = UnitInstallationUtils.getUnit(woModifiable.getWorkorderCustomTO().getWoUnits(), TypeDataUtil.getValidOrgDivValue(parentUnit, WoUnitCustomTO.SIMC_REFERENCE_ID_O));

                            if (newSimCardUnitTO == null) {
                                // Create a New Sim Card
                                // Add the new Sim Card in the workorder unit list
                                newSimCardUnitTO = new WoUnitCustomTO();
                                woModifiable.getWorkorderCustomTO().getWoUnits().add(newSimCardUnitTO);
                            }
                            // set the unit model
                            if (Utils.isNotEmpty(stepfragmentValueArray.get(USER_SELECTED_SIM_CARD_MODEL))) {
                                Long selectedSimModelId = Long.valueOf(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_SIM_CARD_MODEL)));
                                UnitModelCustomTO selectedSimModel = ObjectCache.getIdObject(UnitModelCustomTO.class, selectedSimModelId);
                                newSimCardUnitTO.setUnitModel(selectedSimModel.getCode());
                                List<UnitModelIdentifierTO> unitModelIdentifierTOs = selectedSimModel.getUnitModelIdentifierTOs();
                                if ((unitModelIdentifierTOs != null) && (unitModelIdentifierTOs.size() > 0)) {
                                    UnitModelIdentifierTO unitModelIdentifierTO = unitModelIdentifierTOs.get(0);
                                    newSimCardUnitTO.setIdUnitIdentifierT(unitModelIdentifierTO.getIdUnitIdentifierT());
                                    if (unitModelIdentifierTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__GIAI.longValue()) {
                                        newSimCardUnitTO.setGiai(String.valueOf(stepfragmentValueArray.get(USER_ENTERED_SIM_ICC_NUMBER)));
                                    } else if (unitModelIdentifierTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO.longValue()) {
                                        newSimCardUnitTO.setPropertyNumberD(String.valueOf(stepfragmentValueArray.get(USER_ENTERED_SIM_ICC_NUMBER)));
                                        newSimCardUnitTO.setPropertyNumberV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                                    } else if (unitModelIdentifierTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO.longValue()) {
                                        newSimCardUnitTO.setSerialNumber(String.valueOf(stepfragmentValueArray.get(USER_ENTERED_SIM_ICC_NUMBER)));
                                    }
                                    parentUnit.setSimcIdUnitIdentifierTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                                    parentUnit.setSimcIdUnitIdentifierTD(unitModelIdentifierTO.getIdUnitIdentifierT());
                                }
                            }
                            // set unit status type
                            newSimCardUnitTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                            newSimCardUnitTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                            newSimCardUnitTO.setIdCase(woModifiable.getIdCase());
                            newSimCardUnitTO.setIdWoUnitT(CONSTANTS().WO_UNIT_T__SIMCARD);
                            // set unit mounted timestamp
                            newSimCardUnitTO.setUnitMountedTimestamp(new Date());

                            parentUnit.setSimcReferenceIdD(String.valueOf(stepfragmentValueArray.get(USER_ENTERED_SIM_ICC_NUMBER)));
                            parentUnit.setSimcReferenceIdV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }

    }

    @Override
    public void onClick(View v) {
        try{
        if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        } else if (v != null && v.getId() == R.id.clearPropValueBtn) {
            // Clear the entered ICC number value
            iccPropertyTv.setText("");
        } else if (v != null && v.getId() == R.id.scanIdentifierButton) {
            AndroidUtilsAstSep.scanBarCode(this, SIMCARD_SCAN_REQUEST);
        } else if (v != null &&
                (v.getId() == R.id.yesNewSimCb || v.getId() == R.id.keepExistingSimCb || v.getId() == R.id.removeExistingSimCb)) {
            onCheckboxClicked(v);
        }
        } catch (Exception e) {
            writeLog(TAG + " : onClick()", e);
        }
    }

    protected void enableNewSimOptions(final boolean enable) {
        // ISP Spinner drop down
        ispSpinner.setEnabled(enable);

        // ICC number and clear button
        iccPropertyTv.setEnabled(enable);
        clearPropValueBtn.setEnabled(enable);
        scanIdentifierButton.setEnabled(enable);
        if (!enable) {
            iccPropertyTv.setText("");
        }

    }

    public void onCheckboxClicked(View view) {
        String userSelectedOptionValue = null;
        try {
            int i = view.getId();
            if (i == R.id.yesNewSimCb ) {

                    keepExistingSimCb.setChecked(false);
                    removeExistingSimCb.setChecked(false);
                if(yesNewSimCb.isChecked())
                {
                    enableNewSimOptions(true);
                    userSelectedOptionValue = "NEW";
                }
               else
                {
                    enableNewSimOptions(false);
                    userSelectedOptionValue = "NOTHING";
                }

            } else if (i == R.id.keepExistingSimCb) {
                yesNewSimCb.setChecked(false);
                removeExistingSimCb.setChecked(false);
                enableNewSimOptions(false);
                if(keepExistingSimCb.isChecked())
                    userSelectedOptionValue = "EXISTING";
                else
                    userSelectedOptionValue = "NOTHING";
            } else if (i == R.id.removeExistingSimCb) {
                yesNewSimCb.setChecked(false);
                keepExistingSimCb.setChecked(false);
                enableNewSimOptions(false);
                if(removeExistingSimCb.isChecked())
                    userSelectedOptionValue = "REMOVE";
                else
                    userSelectedOptionValue = "NOTHING";
            }
            userChoice = userSelectedOptionValue;
            stepfragmentValueArray.put(USER_CHOICE_SIMCARD_OPTION, userSelectedOptionValue);
        } catch (Exception e) {
            writeLog(TAG + "  : onCheckboxClicked() ", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        if (requestCode == SIMCARD_SCAN_REQUEST
                && resultCode == Activity.RESULT_OK) {
            iccPropertyTv.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
            AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(), false);
        }
    }
}