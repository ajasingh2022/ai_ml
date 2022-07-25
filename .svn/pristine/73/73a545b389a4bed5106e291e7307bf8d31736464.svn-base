package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.CableTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoCableTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMupTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint("ValidFragment")
public class RegisterMultipointInformationPageFragment extends
        CustomFragment implements View.OnClickListener {         //std ui modifications

    Long selectedHeatElementExists = null;
    protected Long selectedIdCableType = null;
    //private Button heatElementYesBtn, heatElementNoBtn = null;
    protected Spinner cableTypeSpinner = null;
    protected transient AlertDialog dialog = null;
    private RadioGroup registerMultipointHeatElementYesNoRadio;
    private static String TAG = RegisterMultipointInformationPageFragment.class.getSimpleName();


    public RegisterMultipointInformationPageFragment() {
        super();
    }

    public RegisterMultipointInformationPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_register_multipoint_information, null);
        initializePageValues();
        initializeUI();
        setupListeners();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * Initialize the UI components
     */
    protected void initializeUI() {
        final View parentView = fragmentView;
        if (null != parentView) {
            //heatElementYesBtn = (Button) parentView.findViewById(R.id.register_multipoint_heat_element_yes);
            //heatElementNoBtn = (Button) parentView.findViewById(R.id.register_multipoint_heat_element_no);
            registerMultipointHeatElementYesNoRadio = parentView.findViewById(R.id.registerMultipointHeatElementYesNo);
            cableTypeSpinner = parentView.findViewById(R.id.cable_type_spinner);
            GuIUtil.setUpSpinner(parentView.getContext(), cableTypeSpinner, CableTTO.class, true, cableTypeSpinner.getOnItemSelectedListener());
        }
    }

    /**
     * Set up Listeners for UI components
     */
    protected void setupListeners() {
        try {

            cableTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedIdCableType = ((DisplayItem) cableTypeSpinner.getSelectedItem()).getId();
                    if (selectedIdCableType == -1L)
                        selectedIdCableType = null;

                    stepfragmentValueArray.put("cableTypeSpinner", selectedIdCableType);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            registerMultipointHeatElementYesNoRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == registerMultipointHeatElementYesNoRadio.getChildAt(0).getId()) {

                        if (selectedHeatElementExists == null || (selectedHeatElementExists != null && selectedHeatElementExists == CONSTANTS().SYSTEM_BOOLEAN_T__FALSE)) {
                            selectedHeatElementExists = CONSTANTS().SYSTEM_BOOLEAN_T__TRUE;
                        }

                    } else if (checkedId == registerMultipointHeatElementYesNoRadio.getChildAt(1).getId()) {

                        if (selectedHeatElementExists == null || (selectedHeatElementExists != null && selectedHeatElementExists == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE)) {
                            selectedHeatElementExists = CONSTANTS().SYSTEM_BOOLEAN_T__FALSE;
                        }

                    }
                    stepfragmentValueArray.put("registerMultipointHeatElementYesNoRadio", group.indexOfChild(group.findViewById(checkedId)));
                }
            });

        } catch (Exception e) {
            writeLog(TAG + "  : setupListeners() ", e);
        }


    }

    public void showPromptUserAction() {

			/*
             *  If status is false then it is coming from flow engine stack rebuild
			 *  and does not require user attention
			 */

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
        try {
            if (selectedIdCableType == null || selectedHeatElementExists == null) {
                status = false;
            }
            else{
                status = true;
                applyChangesToModifiableWO();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : validateUserInput() ", e);
        }
        return status;
    }


    @Override
    public void onClick(View v) {
        if (v != null) {
/*
                if(v.getId() == R.id.register_multipoint_heat_element_yes){
                    if(selectedHeatElementExists ==  null || (selectedHeatElementExists != null && selectedHeatElementExists == CONSTANTS().SYSTEM_BOOLEAN_T__FALSE)){
                        v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        v.setEnabled(true);
                        selectedHeatElementExists = CONSTANTS().SYSTEM_BOOLEAN_T__TRUE;
                        heatElementNoBtn.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                        heatElementNoBtn.setEnabled(false);
                    }
                } else if(v.getId() == R.id.register_multipoint_heat_element_no){
                    if(selectedHeatElementExists ==  null || (selectedHeatElementExists != null && selectedHeatElementExists == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE)){
                        v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        v.setEnabled(true);
                        selectedHeatElementExists = CONSTANTS().SYSTEM_BOOLEAN_T__FALSE;
                        heatElementYesBtn.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                        heatElementYesBtn.setEnabled(false);
                    }
*/
            if (v.getId() == R.id.okButtonYesNoPage && dialog != null) {
                dialog.dismiss();
            }
        }
    }
/*                                                                                                      std ui modifications

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }
            return true;
        }
*/

    /**
     * Populate UI components with data
     */
    protected void populateData() {
        try {
            if (stepfragmentValueArray.get("registerMultipointHeatElementYesNoRadio") != null) {
                registerMultipointHeatElementYesNoRadio.check(registerMultipointHeatElementYesNoRadio.getChildAt(Math.round(Float.parseFloat(String.valueOf(stepfragmentValueArray.get("registerMultipointHeatElementYesNoRadio"))))).getId());

            } else if (selectedHeatElementExists != null && selectedHeatElementExists == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) {
                /*heatElementYesBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                heatElementYesBtn.setEnabled(true);
                heatElementNoBtn.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                heatElementNoBtn.setEnabled(false);*/
                registerMultipointHeatElementYesNoRadio.check(registerMultipointHeatElementYesNoRadio.getChildAt(0).getId());
                //registerMultipointHeatElementYesNoSwitch.setChecked(true);
            } else if (selectedHeatElementExists != null && selectedHeatElementExists == CONSTANTS().SYSTEM_BOOLEAN_T__FALSE) {
                /*heatElementNoBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                heatElementNoBtn.setEnabled(true);

                heatElementYesBtn.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                heatElementYesBtn.setEnabled(false);*/
                registerMultipointHeatElementYesNoRadio.check(registerMultipointHeatElementYesNoRadio.getChildAt(1).getId());
                //registerMultipointHeatElementYesNoSwitch.setChecked(false);

            }

                 if (selectedIdCableType != null) {
                cableTypeSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(cableTypeSpinner,
                        new DisplayItem(ObjectCache.getType(CableTTO.class, selectedIdCableType))));
            }
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }
    }

    @Override
    public void applyChangesToModifiableWO() {
        try {
            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();

            //Set Cable Type
            selectedIdCableType = ((DisplayItem) cableTypeSpinner.getSelectedItem()).getId();
            if (selectedIdCableType != null) {
                WoCableTO feedingWoCable = new WoCableTO();
                feedingWoCable.setIdCase(modifiableWo.getIdCase());
                feedingWoCable.setIdCableTD(selectedIdCableType);
                feedingWoCable.setIdCableTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                modifiableWo.getWorkorderCustomTO().getWoInst().getInstMultipoint().setFeedingWoCable(feedingWoCable);
            }

            //Set HeatElement Flag
            if (selectedHeatElementExists != null) {
                modifiableWo.getWorkorderCustomTO().getWoInst().getInstMultipoint().getWoInstMupTO().setHeatElementExistsD(selectedHeatElementExists);
                modifiableWo.getWorkorderCustomTO().getWoInst().getInstMultipoint().getWoInstMupTO().setHeatElementExistsV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }
    }

    @Override
    public void initializePageValues() {
        try {
            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (selectedHeatElementExists == null) {
                selectedHeatElementExists = TypeDataUtil.getValidOrgDivValue(modifiableWo.getWorkorderCustomTO().getWoInst().getInstMultipoint().getWoInstMupTO(),
                        WoInstMupTO.HEAT_ELEMENT_EXISTS_O);
            }
            if (stepfragmentValueArray != null){
                selectedIdCableType= (long)Double.parseDouble(String.valueOf(stepfragmentValueArray.get("cableTypeSpinner")));
            }

            if (selectedIdCableType == null) {
                if (modifiableWo.getWorkorderCustomTO().getWoInst().getInstMultipoint().getFeedingWoCable() != null)
                    selectedIdCableType = TypeDataUtil.getValidOrgDivValue(modifiableWo.getWorkorderCustomTO().getWoInst().getInstMultipoint().getFeedingWoCable(),
                            WoCableTO.ID_CABLE_T_O);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : initializePageValues() ", e);
        }
    }


}