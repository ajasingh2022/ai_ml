package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by sacrai on 01/08/2018.
 *
 * @author sacrai
 * @since 1st August, 2018
 */
@SuppressLint("ValidFragment")
public class ApplianceLoadControlFragment extends CustomFragment implements View.OnClickListener {

    transient final String LOAD_CONTROL_CONNECTED_SELECTION = "LOAD_CONTROL_CONNECTED_SELECTION";
    transient final String LOAD_CONTROL_EXISTS_SELECTION = "LOAD_CONTROL_EXISTS_SELECTION";


    static String TAG = ApplianceLoadControlFragment.class.getSimpleName();
    protected transient RadioGroup loadCtlExistsRadioGroup = null;
    protected transient RadioButton loadCtlExistsRadioButtonYes = null;
    protected transient RadioButton loadCtlExistsRadioButtonNo = null;

    protected transient RadioGroup loadConnectedRadioGroup = null;
    protected transient RadioButton loadConnectedRadioButtonYes = null;
    protected transient RadioButton loadConnectedRadioButtonNo = null;


    protected transient View ctlExistsHiddenView = null;
    View view;

    public int pageIndex = 0;

    public ApplianceLoadControlFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_load_control_new, null);
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
     */
    private void initialize() {

        ctlExistsHiddenView = fragmentView.findViewById(R.id.ctlExistsHiddenView);

        // Appliance load control exists?
        loadCtlExistsRadioGroup = fragmentView.findViewById(R.id.loadExistsRadioGroup);
        loadCtlExistsRadioButtonYes = fragmentView.findViewById(R.id.loadExistsYes);
        loadCtlExistsRadioButtonNo = fragmentView.findViewById(R.id.loadExistsNo);
        // Appliance load control connected?
        loadConnectedRadioGroup = fragmentView.findViewById(R.id.loadConnectionRadioGroup);
        loadConnectedRadioButtonYes = fragmentView.findViewById(R.id.loadConnectionYes);
        loadConnectedRadioButtonNo = fragmentView.findViewById(R.id.loadConnectionNo);


    }

    protected void populateData() {
        try {
            if (Utils.isNotEmpty(stepfragmentValueArray.get(LOAD_CONTROL_EXISTS_SELECTION))) {
                if (ConstantsAstSep.FlowPageConstants.YES.equals(stepfragmentValueArray.get(LOAD_CONTROL_EXISTS_SELECTION))) {
                /*yesLoadCtlExistsBtn.performClick();*/
                    ctlExistsHiddenView.setVisibility(View.VISIBLE);

                    loadCtlExistsRadioGroup.check(R.id.loadExistsYes);
                } else {
                /*noLoadCtlExistsBtn.performClick();*/
                    loadCtlExistsRadioGroup.check(R.id.loadExistsNo);
                    ctlExistsHiddenView.setVisibility(View.GONE);
                }
            }

            if (Utils.isNotEmpty(stepfragmentValueArray.get(LOAD_CONTROL_CONNECTED_SELECTION))) {
                if (ConstantsAstSep.FlowPageConstants.YES.equals(stepfragmentValueArray.get(LOAD_CONTROL_CONNECTED_SELECTION))) {
                /*yesLoadConnectedBtn.performClick();*/
                    loadConnectedRadioGroup.check(R.id.loadConnectionYes);
                } else {
                /*					noLoadConnectedBtn.performClick();*/
                    loadConnectedRadioGroup.check(R.id.loadConnectionNo);
                }
            }

        } catch (Exception e) {
            writeLog(TAG + " + :populateData()", e);
        }
    }

    protected void setupListeners() {

        try {
            loadCtlExistsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.loadExistsYes) {
                        ctlExistsHiddenView.setVisibility(View.VISIBLE);
                        stepfragmentValueArray.put(LOAD_CONTROL_EXISTS_SELECTION, ConstantsAstSep.FlowPageConstants.YES);
                    } else if (checkedId == R.id.loadExistsNo) {
                        ctlExistsHiddenView.setVisibility(View.GONE);
                        stepfragmentValueArray.put(LOAD_CONTROL_EXISTS_SELECTION, ConstantsAstSep.FlowPageConstants.NO);
                        stepfragmentValueArray.put(LOAD_CONTROL_CONNECTED_SELECTION, "");
                    }
                }
            });

            loadConnectedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.loadConnectionYes) {
                        stepfragmentValueArray.put(LOAD_CONTROL_CONNECTED_SELECTION, ConstantsAstSep.FlowPageConstants.YES);
                    } else if (checkedId == R.id.loadConnectionNo) {
                        stepfragmentValueArray.put(LOAD_CONTROL_CONNECTED_SELECTION, ConstantsAstSep.FlowPageConstants.NO);
                    }
                }
            });
        } catch (Exception e) {
            writeLog(TAG + " + :setupListeners()", e);
        }
    }

    @Override
    public boolean validateUserInput() {
        boolean status = false;
        try {
            if (Utils.isNotEmpty(stepfragmentValueArray.get(LOAD_CONTROL_EXISTS_SELECTION))) {
                if (ConstantsAstSep.FlowPageConstants.YES.equals(stepfragmentValueArray.get(LOAD_CONTROL_EXISTS_SELECTION))) {
                    if (Utils.isNotEmpty(stepfragmentValueArray.get(LOAD_CONTROL_CONNECTED_SELECTION))) {
                        status = true;
                    }
                } else {
                    status = true;
                }
            }
            if (status == true)
                applyChangesToModifiableWO();
        } catch (Exception e) {
            writeLog(TAG + " + :validateUserInput()", e);
        }
        return status;
    }

    @Override
    public void onClick(final View v) {
        // Related to load control exists

        if (v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void applyChangesToModifiableWO() {
        //undoChanges();
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            WoInstMepTO woInstMepTO = null;
            if (wo != null
                    && wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoInst() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO() != null) {
                woInstMepTO = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO();
            }
            if (woInstMepTO != null) {
                if (ConstantsAstSep.FlowPageConstants.YES.equals(stepfragmentValueArray.get(LOAD_CONTROL_EXISTS_SELECTION))) {
                    woInstMepTO.setExternalControlExistsV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    woInstMepTO.setExternalControlExistsD(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    if (ConstantsAstSep.FlowPageConstants.YES.equals(stepfragmentValueArray.get(LOAD_CONTROL_CONNECTED_SELECTION))) {
                        woInstMepTO.setExternalControlConnectedV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                        woInstMepTO.setExternalControlConnectedD(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    } else if (ConstantsAstSep.FlowPageConstants.NO.equals(stepfragmentValueArray.get(LOAD_CONTROL_CONNECTED_SELECTION))) {
                        woInstMepTO.setExternalControlConnectedV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                        woInstMepTO.setExternalControlConnectedD(CONSTANTS().SYSTEM_BOOLEAN_T__FALSE);
                    }
                } else if (ConstantsAstSep.FlowPageConstants.NO.equals(stepfragmentValueArray.get(LOAD_CONTROL_EXISTS_SELECTION))) {
                    woInstMepTO.setExternalControlExistsV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    woInstMepTO.setExternalControlExistsD(CONSTANTS().SYSTEM_BOOLEAN_T__FALSE);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " + :applyChangesToModifiableWO()", e);
        }
    }
}
