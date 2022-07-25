package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepPowStatusTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class MeterPowerCheckPageFragment extends CustomFragment
        implements OnClickListener {                                        //On touch Listener Removed for std UI modifications

    private transient RadioGroup meterpoweredYesNo = null;
    private transient TextView questiontextView;
    private transient Spinner mtrDCTyp = null;
    private transient View meterDcSpinnerLayout = null;

    // Made this public to resolve //


    private transient MeterConnectionTypeAdapter adapter = null;
    private transient Dialog dialog = null;

    private transient Drawable roundedCornerButtonPositiveEnabled = null;
    private transient Drawable roundedCornerButtonNegativeEnabled = null;

    final WoInstMepPowStatusTTO connected
            = ObjectCache.getIdObject(WoInstMepPowStatusTTO.class, CONSTANTS().WO_INST_MEP_POW_STATUS_T__CONNECTED);
    private transient boolean mtrPoweredYesAtStart = false;
    private transient boolean mtrPoweredNoAtStart = false;

    transient String meterDisConnectType = null;
    View view;
    private boolean ismtrPowered = true;

    public MeterPowerCheckPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_meter_power_check_layout, null);
        initializePageValues();
        initialize();
        setupListeners();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void initializePageValues() {
        meterDisConnectType = null;

            if("YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_POWERED)))){
                ismtrPowered = true;
            }else if("NO".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_POWERED)))){
                ismtrPowered = false;
                meterDisConnectType = String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_DISCONNECT_TYPE));
            }
        mtrPoweredYesAtStart = ismtrPowered;

    }

    private void initialize() {

        meterpoweredYesNo = fragmentView.findViewById(R.id.meterPoweredYesNo);
        questiontextView = fragmentView.findViewById(R.id.questionTextView);
        mtrDCTyp = fragmentView.findViewById(R.id.mtrDcTyp);
        meterDcSpinnerLayout = fragmentView.findViewById(R.id.meterDcSpinnerLayout);

    }


    private void setupListeners() {

        mtrDCTyp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WoInstMepPowStatusTTO woInstMepPowStatusTTO = (WoInstMepPowStatusTTO)mtrDCTyp.getSelectedItem();
                if(woInstMepPowStatusTTO != null){
                    meterDisConnectType = String.valueOf(woInstMepPowStatusTTO.getId().longValue());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        meterpoweredYesNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                group.getCheckedRadioButtonId() ;

                if (group.getCheckedRadioButtonId() == group.getChildAt(0).getId()){
                    showHideReasonLayout(false);
                    stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.METER_POWERED,"YES");
                    ismtrPowered = true;

                }
                if (group.getCheckedRadioButtonId() == group.getChildAt(1).getId()){
                    meterDcSpinnerLayout.setVisibility(View.VISIBLE);
                    showHideReasonLayout(true);
                    stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.METER_POWERED,"NO");
                    ismtrPowered = false;
                }

            }
        });

    }

    private void showHideReasonLayout(final Boolean state) {
        if (state != null) {
            if (state) {
                meterDcSpinnerLayout.setVisibility(View.VISIBLE);
            } else {
                meterDcSpinnerLayout.setVisibility(View.GONE);
            }
        } else {
            if (meterDcSpinnerLayout != null) {
                if (meterDcSpinnerLayout.getVisibility() == View.GONE) {
                    meterDcSpinnerLayout.setVisibility(View.VISIBLE);
                } else {
                    meterDcSpinnerLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    private void populateData() {
        questiontextView.setVisibility(View.GONE);
        // Pre-fetch known disconection types
        final List<WoInstMepPowStatusTTO> ttoList = ObjectCache.getAllIdObjects(WoInstMepPowStatusTTO.class);
        ttoList.remove(connected);
        adapter = new MeterConnectionTypeAdapter(getActivity(), ttoList);
        mtrDCTyp.setAdapter(adapter);
        if (!ismtrPowered) {
            meterpoweredYesNo.check(meterpoweredYesNo.getChildAt(1).getId());
            showHideReasonLayout(true);
            if(meterDisConnectType != null){
                try {
                    WoInstMepPowStatusTTO mepPowStatusTTO = ObjectCache.getType(WoInstMepPowStatusTTO.class,Long.valueOf(meterDisConnectType));
                    if(mepPowStatusTTO != null){
                        mtrDCTyp.setSelection(adapter.selectPowerStatusType(mepPowStatusTTO));
                    }
                }catch (NumberFormatException e){
                    Log.d(this.getClass().getSimpleName(),"Dont do anything");
                }

            }
        }
        else  {
            meterpoweredYesNo.check(meterpoweredYesNo.getChildAt(0).getId());
            showHideReasonLayout(false);
        }

    }

    /**
     * User has not selected any option
     * and tries to move forward, user should be prompted
     * to select an option
     */

    public void showPromptUserAction() {
        final View alertView
                = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.prompt_user_response_layout, null);

        if (alertView != null) {

            ((TextView) alertView.findViewById(R.id.msg1)).setText(getResources().getString(R.string.select_option));
            //((TextView)alertView.findViewById(R.id.msg2)).setText("");

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

        if (meterpoweredYesNo.getCheckedRadioButtonId() == meterpoweredYesNo.getChildAt(0).getId())
        {
            ismtrPowered = true;
        }
        else if (meterpoweredYesNo.getCheckedRadioButtonId() == meterpoweredYesNo.getChildAt(1).getId())
        {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.METER_DISCONNECT_TYPE,meterDisConnectType);
            ismtrPowered = false;

        }
        else
            return false;

        applyChangesToModifiableWO();
        return true;
    }


    @Override
    public void applyChangesToModifiableWO() {
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        WoInstMepTO woInstMepTO = null;
        if (wo != null
                && wo.getWorkorderCustomTO() != null
                && wo.getWorkorderCustomTO().getWoInst() != null
                && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint()!= null
                && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO() != null) {
            woInstMepTO =  wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO();
        }

        if(ismtrPowered){
            if (woInstMepTO != null) {
                woInstMepTO.setIdWoInstMepPowStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woInstMepTO.setIdWoInstMepPowStatusTD(connected.getId());
            }
        }else if (!ismtrPowered){
            if (woInstMepTO != null) {
                woInstMepTO.setIdWoInstMepPowStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woInstMepTO.setIdWoInstMepPowStatusTD(Long.valueOf(meterDisConnectType));
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (dialog != null && v.getId() == R.id.okButtonYesNoPage) {
            dialog.dismiss();
        }
    }




}


