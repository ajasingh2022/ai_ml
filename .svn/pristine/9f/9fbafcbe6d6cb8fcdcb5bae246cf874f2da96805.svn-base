package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.capgemini.sesp.ast.android.module.util.comparators.UnitModelComparator;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.UnitInstallationUtilsStepper;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.Collections;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by sacrai on 2/08/2018.
 * @author sacrai
 * @since 2nd August, 2018
 */
@SuppressLint("ValidFragment")
public class MasterMeterInfoPageFragment extends CustomFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    protected static final int METER_SCAN_REQUEST = 101;
    protected transient ImageButton clearPropValueBtn = null;
    protected transient TextView masterMeterPropertyNumTv = null;
    protected transient Spinner masterMeterUnitModelSpinner = null;
    protected transient ImageButton scanIdentifierButton;
    static final String UNIT_MODEL_SELECTED = "UNIT_MODEL_SELECTED";
    static final String UNIT_IDENTIFIER_ENTERED = "UNIT_IDENTIFIER_ENTERED";
    public int pageIndex = 0;
    private static String TAG = MasterMeterInfoPageFragment.class.getSimpleName();
    View view;

    public MasterMeterInfoPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.choose_master_meter_layout, null);
        initialize();
        setupListeners();
        populateData();


        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    /**
     * Lookup and initialize all the view widgets
     *
     */
    protected void initialize(){
        final View parentView = fragmentView;
        if(parentView!=null){
            clearPropValueBtn = parentView.findViewById(R.id.clearPropValueBtn);
            masterMeterPropertyNumTv = parentView.findViewById(R.id.masterMeterPropertyNumTv);
            masterMeterUnitModelSpinner = parentView.findViewById(R.id.masterMeterUnitModelSpinner);
            scanIdentifierButton = parentView.findViewById(R.id.scanIdentifierButton);
        }
    }

    /**
     * Retrieve all the unit model info from the dynamic cache
     *
     */
    protected void populateData(){
        // Fetch unit model list for master meter
        try{
        List<UnitModelCustomTO> filteredUnitModelCustomTOs = UnitInstallationUtils.getUnitModelsOfType(CONSTANTS().UNIT_T__METER);
        filteredUnitModelCustomTOs =
                UnitInstallationUtils.filterUnitModelsOnCategory(
                        filteredUnitModelCustomTOs,
                        null
                );
        if (filteredUnitModelCustomTOs != null && !filteredUnitModelCustomTOs.isEmpty()) {
            Collections.sort(filteredUnitModelCustomTOs, new UnitModelComparator(UnitModelComparator.ASCENDING));
            GuIUtil.setUpSpinner(getContext(), masterMeterUnitModelSpinner, filteredUnitModelCustomTOs, true, this);
        }

           if (stepfragmentValueArray.isEmpty())
               masterMeterPropertyNumTv.setText("");
           else
               masterMeterPropertyNumTv.setText(String.valueOf(stepfragmentValueArray.get(UNIT_IDENTIFIER_ENTERED)));

        //Populate data
        if (!stepfragmentValueArray.isEmpty())
            if(GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED)))) {
                masterMeterUnitModelSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(masterMeterUnitModelSpinner, Long.parseLong(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED)))));
            }
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }
    }

    public String evaluateNextPage() {
        String nextPage = null;
        try{
        if(this !=null){
            if(true){
                final Intent intraPageIntent = getActivity().getIntent();
                if(intraPageIntent!=null){
                    final String selectedModelCode = intraPageIntent.getStringExtra(ConstantsAstSep.FlowPageConstants.SELECTED_UNIT_MODEL);
                    if(Utils.isNotEmpty(selectedModelCode)){
                        final UnitModelCustomTO selectedModel = ObjectCache.getIdObject(UnitModelCustomTO.class, selectedModelCode);
                        nextPage = UnitInstallationUtilsStepper.evaluateUnitInstallationNextPage(selectedModel, null, intraPageIntent, intraPageIntent.getExtras());
                    }
                }
            }
        }
        } catch (Exception e) {
            writeLog(TAG + "  : evaluateNextPage() ", e);
        }
        return nextPage;
    }

    /**
     * Set up listeners
     */
    protected void setupListeners(){
        if(clearPropValueBtn!=null){
            clearPropValueBtn.setOnClickListener(this);
        }
        //Add onclick listener for Scan button
        if(scanIdentifierButton != null) {
            scanIdentifierButton.setOnClickListener(this);
        }
        masterMeterPropertyNumTv.addTextChangedListener(new TextWatcher() {
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
                stepfragmentValueArray.put(UNIT_IDENTIFIER_ENTERED, s.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v !=null && v.getId()==R.id.clearPropValueBtn
                && masterMeterPropertyNumTv!=null){
            masterMeterPropertyNumTv.setText("");
        } else if(v!=null
                && v.getId()==R.id.okButtonYesNoPage
                && dialog!=null){
            dialog.dismiss();
            dialog = null;
        } else if(v!=null && v.getId()==R.id.scanIdentifierButton) {
            AndroidUtilsAstSep.scanBarCode(this, METER_SCAN_REQUEST);
        }
    }

    @Override
    public boolean validateUserInput() {
        boolean status = false;
        if(GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED))) &&
                Utils.isNotEmpty(stepfragmentValueArray.get(UNIT_IDENTIFIER_ENTERED))){
            status = true;
            applyChangesToModifiableWO();
        }
        return status;
    }

    @Override
    public void applyChangesToModifiableWO() {
        //undoChanges();
        try{
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        // Find the new unit meter with the above match
        final WoUnitCustomTO newUnitTo = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
        if(newUnitTo!=null){
            // Set the master meter reference number (varchar field in db)
            newUnitTo.setMasterReferenceIdD(StringUtil.checkNullString(String.valueOf(stepfragmentValueArray.get(UNIT_IDENTIFIER_ENTERED))));
            newUnitTo.setMasterReferenceIdV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);

            // Set master meter unit model name
            final UnitModelCustomTO masterMeterUnitModel = ObjectCache.getIdObject(UnitModelCustomTO.class, Long.valueOf(String.valueOf(stepfragmentValueArray.get(UNIT_MODEL_SELECTED))));
            if(masterMeterUnitModel != null){
                newUnitTo.setUnitModelName(masterMeterUnitModel.getCode());
                newUnitTo.setMasterIdUnitIdentTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                if(masterMeterUnitModel.getUnitModelIdentifierTOs() != null
                        && !masterMeterUnitModel.getUnitModelIdentifierTOs().isEmpty()) {
                    newUnitTo.setMasterIdUnitIdentTD(masterMeterUnitModel.getUnitModelIdentifierTOs().get(0).getIdUnitIdentifierT());
                }
            }
        }
        } catch (Exception e) {
            writeLog(TAG + " : getExistingMeterModel() ", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        if (requestCode == METER_SCAN_REQUEST
                && resultCode == Activity.RESULT_OK) {
            masterMeterPropertyNumTv.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"),true));
            AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(),false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        stepfragmentValueArray.put(UNIT_MODEL_SELECTED, String.valueOf(id));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }
}