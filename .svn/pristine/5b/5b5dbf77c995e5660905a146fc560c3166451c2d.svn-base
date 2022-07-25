package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.InstAntPlmtTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoCableTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventTechPlanTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstMeasurepointCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstMupCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.getSafeInteger;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by sacrai on 2/22/2018.
 *
 * @author sacrai
 * @since 1st August, 2018
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class RegisterNewExternAntennaFragment extends CustomFragment implements View.OnClickListener {

    // Spinner to hold all possible antenna model types
    private transient Spinner antennaModelSpinner = null;
    private transient Spinner antennaPlacementTypeSpinner = null;

    private transient TextView existingModelTv = null;
    private transient TextView existingTechPlanTv = null;

    private transient EditText cableLengthTv = null;
    private transient EditText directionTv = null;
    private transient EditText signalStrengthTv = null;

    private transient Dialog dialog = null;
    transient String existingModel;
    transient String antenaInTechplan;
    transient String selectedAntennaModel;
    transient String selectedAntennaPlacement;
    transient String cableLength;
    transient String direction;
    transient String signalStrength;
    private transient String selectedAntennaModelAtStart;
    private transient String selectedAntennaPlacementAtStart;
    private transient String cableLengthAtStart;
    private transient String directionAtStart;
    private transient String signalStrengthAtStart;
    private String TAG = RegisterNewExternAntennaFragment.class.getSimpleName();
    transient int userChoice;
    protected transient UnitModelCustomTO selectedAntennaModelObj;
    transient WoUnitCustomTO existingAntennaObj;
    private transient boolean isMeasurepoint = true;
    private transient List<WoUnitCustomTO> abortUnitList;
    private transient WoInstMeasurepointCustomTO abortWoInstMeasurepointCustomTO;
    private transient WoInstMupCustomTO abortWoInstMupCustomTO;


    public RegisterNewExternAntennaFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_register_new_antenna, null);
        initializePageValues();
        initilizeUI();
        return fragmentView;
    }

    @Override
    public void initializePageValues() {
    try{
        final Intent intraPageIntent = getActivity().getIntent();
        userChoice = intraPageIntent.getIntExtra(ConstantsAstSep.FlowPageConstants.USER_SELECTION_EXTERNAL_ANTENA, 0);
        antenaInTechplan = intraPageIntent.getStringExtra(ConstantsAstSep.FlowPageConstants.ANTENNA_MODEL_NAME);
        existingModel= intraPageIntent.getStringExtra(ConstantsAstSep.FlowPageConstants.UNIT_MODEL_NAME);
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        WoInstMepTO woInstMepTo = null;
        WoCableTO woCableTo = null;
        if (wo != null
                && wo.getWorkorderCustomTO() != null
                && wo.getWorkorderCustomTO().getWoInst() != null
                && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null) {
            if (wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO() != null) {
                woInstMepTo = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO();
            }
            if (wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getAntennaWoCable() != null) {
                woCableTo = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getAntennaWoCable();
            }
        }

        final Map<String, String> antelModelNNameMap = UnitInstallationUtils.getExistingModelNames(wo, CONSTANTS().WO_UNIT_T__ANTENNA);
        if (!stepfragmentValueArray.isEmpty()) {
            selectedAntennaModel = String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SELECTED_ANTENNA_MODEL));
            selectedAntennaPlacement = String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SELECTED_ANTENNA_PLACEMENT));
            signalStrength = String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SIGNAL_STRENGTH));
            cableLength = String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.CABLE_LENGTH));
            direction = String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.DIRECTION));
        }
        else
        {
            if (woInstMepTo != null) {
                if ((woInstMepTo.getSignalStrengthV() != null) && (woInstMepTo.getSignalStrengthV().equals(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE))) {
                    if (woInstMepTo.getSignalStrengthD() != null) {
                        signalStrength = woInstMepTo.getSignalStrengthD().toString();
                    }
                } else {
                    if (woInstMepTo.getSignalStrengthO() != null) {
                        signalStrength = woInstMepTo.getSignalStrengthO().toString();
                    }
                }
        }

            if ((woInstMepTo.getAntennaDirectionV() != null) && (woInstMepTo.getAntennaDirectionV().equals(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE))) {
                if (woInstMepTo.getAntennaDirectionD() != null) {
                    direction = woInstMepTo.getAntennaDirectionD().toString();
                }
            } else {
                if (woInstMepTo.getAntennaDirectionO() != null) {
                    direction = woInstMepTo.getAntennaDirectionO().toString();
                }
            }
            Log.d("Direction", "Direction value " + direction);


        if (woCableTo != null) {
            if ((woCableTo.getLengthV() != null) && (woCableTo.getLengthV().equals(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE))) {
                if (woCableTo.getLengthD() != null) {
                    cableLength = String.valueOf(woCableTo.getLengthD());
                }
            } else {
                if (woCableTo.getLengthO() != null) {
                    cableLength = String.valueOf(woCableTo.getLengthO());
                }
            }
        }
        }
       /* existingAntennaObj = UnitInstallationUtils.getUnit(wo,
                CONSTANTS().WO_UNIT_T__ANTENNA,
                CONSTANTS().WO_UNIT_STATUS_T__EXISTING);*/

        selectedAntennaModelAtStart = selectedAntennaModel;
        selectedAntennaPlacementAtStart = selectedAntennaPlacement;
        cableLengthAtStart = cableLength;
        directionAtStart = direction;
        signalStrengthAtStart = signalStrength;
    } catch (Exception e) {
        writeLog(TAG + " : initializePageValues()", e);
    }
    }

    private void initilizeUI() {
        try{
        antennaModelSpinner = fragmentView.findViewById(R.id.antennaModelSpinner);
        antennaPlacementTypeSpinner = fragmentView.findViewById(R.id.antennaPlacementTypeSpinner);
        existingModelTv = fragmentView.findViewById(R.id.existingModelTv);
        existingTechPlanTv = fragmentView.findViewById(R.id.existingTechPlanTv);
        cableLengthTv = fragmentView.findViewById(R.id.cableLengthTv);
        directionTv = fragmentView.findViewById(R.id.directionTv);
        signalStrengthTv = fragmentView.findViewById(R.id.signalStrengthTv);
        antennaModelSpinner.setEnabled(userChoice != 3);
        setUpListener();
        populateData();
        } catch (Exception e) {
            writeLog(TAG + " : initilizeUI()", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Initialize the view objects


    }

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


        applyChangesToModifiableWO();
        return true;
    }

    @Override
    public void applyChangesToModifiableWO() {
        getUserChoiceValuesForPage();
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
         WoInstMepTO woInstMepTo;
        try {
            if (wo != null
                    && wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoInst() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null) {
                if (wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO() == null) {
                    woInstMepTo = new WoInstMepTO();
                    woInstMepTo.setIdCase(wo.getIdCase());
                    wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().setWoInstMepTO(woInstMepTo);
                    }
                    woInstMepTo = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO();
                    if (signalStrength != null && isDouble(signalStrength)) {
                        woInstMepTo.setSignalStrengthV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                        woInstMepTo.setSignalStrengthD(Utils.safeToDouble(signalStrength, Locale.getDefault()));
                    }
                    if (direction != null && isDouble(signalStrength)) {
                        woInstMepTo.setAntennaDirectionV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                        woInstMepTo.setAntennaDirectionD(Utils.safeToLong(direction));
                    }

                    if (userChoice == 1 || userChoice == 2) {//New Antenna or Change Antenna
                        if (existingAntennaObj != null) {
                            try {
                                TypeDataUtil.mapValueIntoDOV(CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED, existingAntennaObj, WoUnitTO.ID_WO_UNIT_STATUS_T_D);
                            } catch (Exception e) {
                                writeLog(TAG + " :applyChangesToModifiableWO()", e);
                            }
                            existingAntennaObj.setUnitDismantledTimestamp(new Date());
                        }

                        WoUnitCustomTO mountedAntennaObj = UnitInstallationUtils.getUnit(wo,
                                CONSTANTS().WO_UNIT_T__ANTENNA,
                                CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                        if (mountedAntennaObj == null) {
                            mountedAntennaObj = new WoUnitCustomTO();
                            List<WoUnitCustomTO> unitList = wo.getWorkorderCustomTO().getWoUnits();
                            if (unitList == null) {
                                unitList = new ArrayList<WoUnitCustomTO>();
                                wo.getWorkorderCustomTO().setWoUnits(unitList);
                            }
                            unitList.add(mountedAntennaObj);
                        }
                        mountedAntennaObj.setIdCase(wo.getIdCase());
                        try {
                            TypeDataUtil.mapValueIntoDOV(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED, mountedAntennaObj, WoUnitTO.ID_WO_UNIT_STATUS_T_D);
                        } catch (Exception e) {
                            writeLog(TAG + " :applyChangesToModifiableWO()", e);
                        }
                        mountedAntennaObj.setIdWoUnitT(CONSTANTS().WO_UNIT_T__ANTENNA);
                        mountedAntennaObj.setIdUnitIdentifierT(CONSTANTS().UNIT_IDENTIFIER_T__NA);
                        mountedAntennaObj.setUnitModel(selectedAntennaModelObj.getCode());
                        mountedAntennaObj.setUnitMountedTimestamp(new Date());
                    }
                }

            if (selectedAntennaModel != null) {
                if (wo != null
                        && wo.getWorkorderCustomTO() != null
                        && wo.getWorkorderCustomTO().getWoEvents() != null) {
                    final List<WoEventCustomTO> eventList = wo.getWorkorderCustomTO().getWoEvents();
                    if (eventList != null && !eventList.isEmpty()) {
                        for (final WoEventCustomTO eventTo : eventList) {
                            if (eventTo != null && eventTo.getTechnicalPlanning() != null) {
                                final WoEventTechPlanTO woEventTechPlanTO = eventTo.getTechnicalPlanning();
                                woEventTechPlanTO.setAntIdUnitModel(Utils.safeToLong(selectedAntennaModel));
                            }
                        }
                    }
                }
            }
            if (selectedAntennaPlacement != null) {
                if (wo != null
                        && wo.getWorkorderCustomTO() != null
                        && wo.getWorkorderCustomTO().getWoInst() != null
                        && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null
                        && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO() != null) {
                    wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().setIdInstAntPlmtTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().setIdInstAntPlmtTD(Utils.safeToLong(selectedAntennaPlacement));
                }
            }
            if (cableLength != null && isDouble(cableLength)) {
                if (wo != null
                        && wo.getWorkorderCustomTO() != null
                        && wo.getWorkorderCustomTO().getWoInst() != null
                        && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null) {
                    if (wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getAntennaWoCable() != null) {
                        wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getAntennaWoCable().setLengthV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                        wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getAntennaWoCable().setLengthD(Utils.safeToDouble(cableLength, Locale.getDefault()));
                    } else {
                        WoCableTO woCableTO = new WoCableTO();
                        woCableTO.setLengthV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                        woCableTO.setLengthD(Utils.safeToDouble(cableLength, Locale.getDefault()));
                        woCableTO.setIdCase(wo.getIdCase());
                        wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().setAntennaWoCable(woCableTO);
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :applyChangesToModifiableWO()", e);
        }
    }

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            //return false;
            writeLog(TAG + " : isDouble()", e);
            return false;
        }

    }

    public void getUserChoiceValuesForPage() {
        if (selectedAntennaModel != null) {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.SELECTED_ANTENNA_MODEL, selectedAntennaModel);
        }
        if (selectedAntennaPlacement != null) {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.SELECTED_ANTENNA_PLACEMENT, selectedAntennaPlacement);
        }
        if (signalStrength!= null){
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.SIGNAL_STRENGTH,signalStrength);
        }
        if (cableLength!= null){
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.CABLE_LENGTH,cableLength);
        }
        if (direction!= null){
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.DIRECTION,direction);
        }
    }

    public boolean validateUserChoice() {
        /*
         *  Ensure following info is provided
         *
         *  1. Cable length
         *  2. Direction
         *  3. Signal Strength
         */
        return cableLengthTv.getText() != null
                & directionTv.getText() != null
                & signalStrengthTv.getText() != null
                & !cableLengthTv.getText().toString().trim().equals("")
                & !directionTv.getText().toString().trim().equals("")
                & !signalStrengthTv.getText().toString().trim().equals("");
    }

    private void setUpListener() {
        if (antennaModelSpinner != null) {
            antennaModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedAntennaModelObj = (UnitModelCustomTO) antennaModelSpinner.getSelectedItem();
                    selectedAntennaModel = String.valueOf(selectedAntennaModelObj.getId().longValue());
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
        }
        if (antennaPlacementTypeSpinner != null) {
            antennaPlacementTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedAntennaPlacement = String.valueOf(((InstAntPlmtTTO) antennaPlacementTypeSpinner.getSelectedItem()).getId().longValue());
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
        }
        if (cableLengthTv != null) {
            cableLengthTv.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (cableLengthTv.getText() != null) {
                       cableLength = String.valueOf(cableLengthTv.getText());
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
        if (signalStrengthTv != null) {
            signalStrengthTv.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (signalStrengthTv.getText() != null) {
                        signalStrength = signalStrengthTv.getText().toString();
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
        if (directionTv != null) {
            directionTv.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (directionTv.getText() != null) {
                        direction = directionTv.getText().toString();
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


    private void populateData() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();

            existingAntennaObj = UnitInstallationUtils.getUnit(wo,
                    CONSTANTS().WO_UNIT_T__ANTENNA,
                    CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
            //Set Existing Model
            if (Utils.isNotEmpty(existingModel)) {
                existingModelTv.setText(existingModel);
            }
            if (Utils.isNotEmpty(antenaInTechplan)) {
                existingTechPlanTv.setText(antenaInTechplan);
            }
            // Get all available antenna model configuration units (not type data)
            List<UnitModelCustomTO> allAntennaModels = UnitInstallationUtils.getUnitModelsOfType(CONSTANTS().UNIT_T__ANTENNA);
            List<UnitModelCustomTO> antennaModels = new ArrayList<UnitModelCustomTO>();

            //filter based on the idDomain
            List<Long> idDomains = WorkorderUtils.getDomainsForWO(wo);
            for (UnitModelCustomTO antennaModel : allAntennaModels) {
                if (antennaModel.getIdDomain() != null &&
                        idDomains.contains(antennaModel.getIdDomain())) {
                    antennaModels.add(antennaModel);
                }
            }

            if (Utils.isNotEmpty(antennaModels)) {
                final CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity());
                adapter.setUnitModelList(antennaModels);
                antennaModelSpinner.setAdapter(adapter);
                if (selectedAntennaModel != null) {
                    int selectedIndex = adapter.getPosition(Long.parseLong(selectedAntennaModel));
                    if (selectedIndex > -1) {
                        antennaModelSpinner.setSelection(selectedIndex);
                    }
                }

                //Existing Antenna selection
                else if (existingAntennaObj != null) {
                    int selectedIndex = adapter.getPosition(ObjectCache.getIdObject(UnitModelCustomTO.class, existingAntennaObj.getUnitModel()).getId());
                    if (selectedIndex > -1) {
                        antennaModelSpinner.setSelection(selectedIndex);
                    }
                }
            }
            // Get all possible antenna placement types (type data)
            final List<InstAntPlmtTTO> antPlmtTList = ObjectCache.getAllTypes(InstAntPlmtTTO.class);
            if (antPlmtTList != null && !antPlmtTList.isEmpty()) {
                final CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity());
                adapter.setAntPlmtTList(antPlmtTList);
                antennaPlacementTypeSpinner.setAdapter(adapter);
                if (selectedAntennaPlacement != null) {
                    int selectedIndex = adapter.getPosition(Long.parseLong(selectedAntennaPlacement));
                    if (selectedIndex > -1) {
                        antennaPlacementTypeSpinner.setSelection(selectedIndex);
                    }
                }
            }

            if (signalStrength != null) {
                signalStrengthTv.setText(signalStrength);
            }
            if (cableLength != null) {
                cableLengthTv.setText(cableLength);
            }
            if (direction != null) {
                directionTv.setText(direction);
            }
        } catch (Exception e) {
            writeLog(TAG + " :populateData()", e);
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
