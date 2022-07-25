package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint("ValidFragment")
public class MeterAccessibilityFragment extends CustomFragment implements  RadioGroup.OnCheckedChangeListener {

    public MeterAccessibilityFragment() {
        super();
    }

    protected transient Dialog dialog = null;
    protected transient RadioGroup techAccessGroup = null;
    protected transient RadioButton techAccessRadioButtonYes = null;
    protected transient RadioButton techAccessRadioButtonNo = null;

    protected transient RadioGroup endCustomerRadioGroup = null;
    protected transient RadioButton endCustomerRadioButtonYes = null;
    protected transient RadioButton endCustomerRadioButtonNo = null;
    private String TAG = MeterAccessibilityFragment.class.getSimpleName();

    protected transient TextView techAccessTxVal = null;
    protected transient TextView endCustomerTxVal = null;

    transient static final String CUSTOMER_SELECTION = "CUSTOMER_SELECTION";
    transient static final String TECHNICIAN_SELECTION = "TECHNICIAN_SELECTION";
    transient static final String ACCESSIBLE = "ACCESSIBLE";
    transient static final String NOT_ACCESSIBLE = "NOT_ACCESSIBLE";

    public MeterAccessibilityFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.meter_installation_check_layout_new, null);
        initializeUI();
        registerListeners();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    protected void initializeUI() {

        //Radio Groups for technician and end customer
        techAccessGroup = fragmentView.findViewById(R.id.techAccessRadioGroup);
        endCustomerRadioGroup = fragmentView.findViewById(R.id.endCustRadioGroup);

        //Radio Button for Accessible to Technician
        techAccessRadioButtonYes = fragmentView.findViewById(R.id.techAccessRadioBtnYes);
        techAccessRadioButtonNo = fragmentView.findViewById(R.id.techAccessRadioBtnNo);

        //Radio button for End Customer Accessible
        endCustomerRadioButtonYes = fragmentView.findViewById(R.id.endCustRadioBtnYes);
        endCustomerRadioButtonNo = fragmentView.findViewById(R.id.endCustRadioBtnNo);
    }

    /**
     * Populate data fom pagepreference to UI
     */
    protected void populateData() {

        WorkorderCustomWrapperTO workorderCustomWrapperTO = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        try{
        if ((String.valueOf(stepfragmentValueArray.get(TECHNICIAN_SELECTION)).equalsIgnoreCase(ACCESSIBLE)||String.valueOf(stepfragmentValueArray.get(TECHNICIAN_SELECTION)).equalsIgnoreCase(NOT_ACCESSIBLE)) &&(String.valueOf(stepfragmentValueArray.get(CUSTOMER_SELECTION)).equalsIgnoreCase(ACCESSIBLE))||(String.valueOf(stepfragmentValueArray.get(CUSTOMER_SELECTION)).equalsIgnoreCase(NOT_ACCESSIBLE)))
        {
            if (ACCESSIBLE.equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(TECHNICIAN_SELECTION)))){
                techAccessGroup.check(techAccessGroup.getChildAt(0).getId());
            }
            else if (NOT_ACCESSIBLE.equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(TECHNICIAN_SELECTION)))){
                techAccessGroup.check(techAccessGroup.getChildAt(1).getId());
            }

            if (ACCESSIBLE.equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(CUSTOMER_SELECTION)))){
                endCustomerRadioGroup.check(endCustomerRadioGroup.getChildAt(0).getId());
            }
            else if (NOT_ACCESSIBLE.equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(CUSTOMER_SELECTION)))){
                endCustomerRadioGroup.check(endCustomerRadioGroup.getChildAt(1).getId());
            }

        }
        else {
            Long isAccessibleToTechnicianOrg = workorderCustomWrapperTO.getWorkorderCustomTO().getWoInst().getWoInstTO().getAccessibleToTechnicianO();
            if (isAccessibleToTechnicianOrg != null)
                //     techAccessTxVal.setText(isAccessibleToTechnicianOrg.equals(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) ? getString(R.string.yes) : getString(R.string.no));

                techAccessGroup.check(isAccessibleToTechnicianOrg.equals(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) ? (R.id.techAccessRadioBtnYes) : (R.id.techAccessRadioBtnNo));

            Long isAccessibleToEndCustomer = workorderCustomWrapperTO.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().getAccessibleToEndCustomer();
            if (isAccessibleToEndCustomer != null)
                //     endCustomerTxVal.setText(isAccessibleToEndCustomer.equals(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) ? getString(R.string.yes) : getString(R.string.no));
                endCustomerRadioGroup.check(isAccessibleToEndCustomer.equals(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) ? (R.id.endCustRadioBtnYes) : (R.id.endCustRadioBtnNo));

        }
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }

    }




    private void registerListeners() {

        if (techAccessGroup != null) {
            techAccessGroup.setOnCheckedChangeListener(this);
        }

        if (endCustomerRadioGroup != null) {
            endCustomerRadioGroup.setOnCheckedChangeListener(this);
        }

    }


    @Override
    public void applyChangesToModifiableWO() {
        try{
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        // Update access to technician information
        Long isAccessibleToTechnician = null;
        // if(Utils.isNotEmpty(equals(stepfragmentValueArray.get(TECHNICIAN_SELECTION)))){
        if(ACCESSIBLE.equals(stepfragmentValueArray.get(TECHNICIAN_SELECTION))) {
            isAccessibleToTechnician = CONSTANTS().SYSTEM_BOOLEAN_T__TRUE;
        } else if (NOT_ACCESSIBLE.equals(stepfragmentValueArray.get(TECHNICIAN_SELECTION))) {
            isAccessibleToTechnician = CONSTANTS().SYSTEM_BOOLEAN_T__FALSE;
        } else {
            isAccessibleToTechnician = null;

        }


        // Update end customer access information
        Long isAccessibleToEndCustomer;
        //  if(Utils.isNotEmpty(equals(stepfragmentValueArray.get(CUSTOMER_SELECTION)))){
        if (ACCESSIBLE.equals(stepfragmentValueArray.get(CUSTOMER_SELECTION))) {
            isAccessibleToEndCustomer = CONSTANTS().SYSTEM_BOOLEAN_T__TRUE;
        } else if(NOT_ACCESSIBLE.equals(stepfragmentValueArray.get(CUSTOMER_SELECTION))) {
            isAccessibleToEndCustomer = CONSTANTS().SYSTEM_BOOLEAN_T__FALSE;
        } else {
            isAccessibleToEndCustomer = null;
        }


        wo.getWorkorderCustomTO().getWoInst().getWoInstTO().setAccessibleToTechnicianV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
        wo.getWorkorderCustomTO().getWoInst().getWoInstTO().setAccessibleToTechnicianD(isAccessibleToTechnician);
        wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().setAccessibleToEndCustomer(isAccessibleToEndCustomer);
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }
    }


    public boolean validateUserInput() {

        if ((Utils.isNotEmpty(stepfragmentValueArray.get(TECHNICIAN_SELECTION))) && (Utils.isNotEmpty(stepfragmentValueArray.get(CUSTOMER_SELECTION)))) {
            if ((ACCESSIBLE.equals(stepfragmentValueArray.get(TECHNICIAN_SELECTION)) || NOT_ACCESSIBLE.equals(stepfragmentValueArray.get(TECHNICIAN_SELECTION))) && (ACCESSIBLE.equals(stepfragmentValueArray.get(CUSTOMER_SELECTION)) || NOT_ACCESSIBLE.equals(stepfragmentValueArray.get(CUSTOMER_SELECTION)))) {
                applyChangesToModifiableWO();
                return true;
            }
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.techAccessRadioBtnYes) {
            stepfragmentValueArray.put(TECHNICIAN_SELECTION, ACCESSIBLE);
        }else if (checkedId == R.id.techAccessRadioBtnNo) {
            stepfragmentValueArray.put(TECHNICIAN_SELECTION, NOT_ACCESSIBLE);
        }
        if (checkedId == R.id.endCustRadioBtnYes) {
            stepfragmentValueArray.put(CUSTOMER_SELECTION,ACCESSIBLE);
        }  else if(checkedId == R.id.endCustRadioBtnNo) {
            stepfragmentValueArray.put(CUSTOMER_SELECTION,NOT_ACCESSIBLE);
        }
    }
}
