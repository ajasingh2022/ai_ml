package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepPowStatusTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class OldMeterPowerCheckPageFragment extends CustomFragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //  private transient Button meterPoweredNo = null;
    //  private transient Button meterPoweredYes = null;


    private transient Spinner mtrDCTyp = null;
    private transient View meterDcSpinnerLayout = null;

    private transient MeterConnectionTypeAdapter adapter = null;
    private transient Dialog dialog = null;

    transient String meterDisConnectType = null;

    private transient RadioGroup meterpoweredYesNo = null;
    private transient boolean noProduct = true;
    final WoInstMepPowStatusTTO connected
            = ObjectCache.getIdObject(WoInstMepPowStatusTTO.class, CONSTANTS().WO_INST_MEP_POW_STATUS_T__CONNECTED);
    private transient Drawable roundedCornerButtonPositiveEnabled = null;
    private transient Drawable roundedCornerButtonNegativeEnabled = null;
    private boolean ismtrPowered = true;
    private String TAG = OldMeterPowerCheckPageFragment.class.getSimpleName();

    public OldMeterPowerCheckPageFragment() {
        super();
    }

    public OldMeterPowerCheckPageFragment(String stepIdentifier) {
        super(stepIdentifier);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_meter_power_check_layout, null);
        initializePageValues();
        initialize();
        populateData();
        setupListeners();
        return fragmentView;
    }

    public String evaluateNextPage() {
        String nextPage;
        if (!noProduct) {
            nextPage = ConstantsAstSep.FlowPageConstants.NEXT_PAGE_PRODUCT;
        } else {
            nextPage = ConstantsAstSep.FlowPageConstants.NEXT_PAGE;
        }
        return nextPage;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void applyChangesToModifiableWO() {
        try {
            getUserChoiceValuesForPage();
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (wo != null
                    && wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoInst() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO() != null) {
                if (ismtrPowered) {
                    wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().setIdWoInstMepPowStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().setIdWoInstMepPowStatusTD(connected.getId());
                } else {
                    wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().setIdWoInstMepPowStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().setIdWoInstMepPowStatusTD(Long.valueOf(meterDisConnectType));
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }
    }

    public void initializePageValues() {
        try {
            meterDisConnectType = null;
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (stepfragmentValueArray != null) {
                if ("YES".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.OLD_METER_POWERED))) {
                    ismtrPowered = true;
                } else if ("NO".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.OLD_METER_POWERED))) {
                    ismtrPowered = false;
                }
                meterDisConnectType = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.OLD_METER_DISCONNECT_TYPE);
            }
            if (WorkorderUtils.isMeterRegisterExists(wo)
                    || WorkorderUtils.isCollectionProductExist(wo)) {
                noProduct = false;
            }
        } catch (Exception e) {
            writeLog(TAG + "  : initializePageValues() ", e);
        }
    }

    private void initialize() {
        // Initialize the view widgets

        meterpoweredYesNo = fragmentView.findViewById(R.id.meterPoweredYesNo);

        mtrDCTyp = fragmentView.findViewById(R.id.mtrDcTyp);
        meterDcSpinnerLayout = fragmentView.findViewById(R.id.meterDcSpinnerLayout);

    }

    public void getUserChoiceValuesForPage() {

        if (ismtrPowered) {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.OLD_METER_POWERED, "YES");
        } else {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.OLD_METER_POWERED, "NO");
        }
        if (meterDisConnectType != null) {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.OLD_METER_DISCONNECT_TYPE, meterDisConnectType);
        }

    }

    private void setupListeners() {

        meterpoweredYesNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                group.getCheckedRadioButtonId();

                if (group.getCheckedRadioButtonId() == group.getChildAt(0).getId()) {
                    showHideReasonLayout(false);
                    stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.OLD_METER_POWERED, "YES");
                    ismtrPowered = true;
                }
                if (group.getCheckedRadioButtonId() == group.getChildAt(1).getId()) {
                    showHideReasonLayout(true);
                    stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.OLD_METER_POWERED, "NO");
                    ismtrPowered = false;
                }

            }
        });

    }

    private void showHideReasonLayout(final Boolean state) {
        try{
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
        } catch (Exception e) {
            writeLog(TAG + "  : showHideReasonLayout() ", e);
        }
    }

    private void populateData() {
        try{
        // Pre-fetch known disconection types
        final List<WoInstMepPowStatusTTO> ttoList = ObjectCache.getAllIdObjects(WoInstMepPowStatusTTO.class);
        ttoList.remove(connected);
        adapter = new MeterConnectionTypeAdapter(getActivity(), ttoList);
        mtrDCTyp.setAdapter(adapter);
        if (ismtrPowered) {

            meterpoweredYesNo.check(meterpoweredYesNo.getChildAt(0).getId());
            showHideReasonLayout(false);
        } else {

            meterpoweredYesNo.check(meterpoweredYesNo.getChildAt(1).getId());
            showHideReasonLayout(true);
            if (meterDisConnectType != null) {
                WoInstMepPowStatusTTO mepPowStatusTTO = ObjectCache.getType(WoInstMepPowStatusTTO.class, Long.valueOf(meterDisConnectType));
                if (mepPowStatusTTO != null) {
                    mtrDCTyp.setSelection(adapter.selectPowerStatusType(mepPowStatusTTO));
                }
            }
        }
        mtrDCTyp.setOnItemSelectedListener(this);
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
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

            ((TextView) alertView.findViewById(R.id.msg1)).setText(getResources().getString(R.string.select_option));

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


        if (meterpoweredYesNo.getCheckedRadioButtonId() == meterpoweredYesNo.getChildAt(0).getId()) {
            ismtrPowered = true;
        } else if (meterpoweredYesNo.getCheckedRadioButtonId() == meterpoweredYesNo.getChildAt(1).getId()) {
            ismtrPowered = false;
        } else
            return false;

        applyChangesToModifiableWO();
        return true;
    }

    @Override
    public void onClick(View v) {

        if (dialog != null && v.getId() == R.id.okButtonYesNoPage) {
            dialog.dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try{
        WoInstMepPowStatusTTO woInstMepPowStatusTTO = (WoInstMepPowStatusTTO) mtrDCTyp.getSelectedItem();

        if (woInstMepPowStatusTTO != null) {
            meterDisConnectType = String.valueOf(woInstMepPowStatusTTO.getId().longValue());
        }
        } catch (Exception e) {
            writeLog(TAG + "  : onItemSelected() ", e);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
