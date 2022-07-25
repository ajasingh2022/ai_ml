package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.CurrentTransformerPriTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.CurrentTransformerSecTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstMeasurepointCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

@SuppressLint("ValidFragment")
public class VerifyCurrentTransfermerFragment extends CustomFragment implements View.OnClickListener {

    private transient CheckBox mManualCheckbox = null;
    private transient EditText mManualEditText = null;

    private transient Spinner mPrimarySpinner = null;
    private transient Spinner mSecondarySpinner = null;

    private transient LinearLayout primaryLayout;
    private transient LinearLayout secondaryLayout;

    private transient Dialog dialog = null;
    protected boolean mIsChangeManually;

    protected int mPriSpinnerIndex;
    protected int mSecSpinnerIndex;

    protected String mRatio;
    private static String TAG = VerifyCurrentTransfermerFragment.class.getSimpleName();
    private Long mMeterConstant = 1l;
    private TextView mMeterConstantEt;

    public VerifyCurrentTransfermerFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    public VerifyCurrentTransfermerFragment() {
        super();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_verify_ct_stepper, null);
        initializePageValues();
        initialize();
        setupListeners();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "INSIDE onResume");
        restoreViewState();
    }

    private void restoreViewState() {
        Log.i(TAG, "INSIDE restoreViewState");
        mManualCheckbox.setChecked(mIsChangeManually);
        Log.i(TAG, "VALUE OF mIsChangeManually :: " + mIsChangeManually);
    }


    @Override
    public void applyChangesToModifiableWO() {
        applyChanges();
    }


    public void initializePageValues() {
        mIsChangeManually = false;
        mPriSpinnerIndex = -1;
        mSecSpinnerIndex = -1;

        if (stepfragmentValueArray != null) {

            if ("YES".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.MANUAL_CHANGE_CT_PARAMS))) {
                mIsChangeManually = true;
            }

            try {
                if (stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.PRIMARY_VALUE_INDEX) != null) {
                    mPriSpinnerIndex = Integer.parseInt((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.PRIMARY_VALUE_INDEX));
                }
                if (stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SECONDARY_VALUE_INDEX) != null) {
                    mSecSpinnerIndex = Integer.parseInt((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SECONDARY_VALUE_INDEX));
                }
                if (stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.RATIO_VALUE) != null) {
                    mRatio = stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.RATIO_VALUE).toString();
                }
            } catch (NumberFormatException e) {
                writeLog(TAG + " : initializePageValues()", e);
            }
        }
    }

    /**
     * Primary Coupling
     *
     * @return
     */
    protected CurrentTransformerPriTTO getCouplingPrimary() {
        try{
        final WorkorderCustomWrapperTO workorder = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        WoInstMeasurepointCustomTO woInstMeasurepointCustomTO = workorder.getWorkorderCustomTO().getWoInst().getInstMeasurepoint();
        if (woInstMeasurepointCustomTO != null && woInstMeasurepointCustomTO.getWoInstMepElCustomTO() != null) {
            Long id = TypeDataUtil.getOrgOrDiv(
                    woInstMeasurepointCustomTO.getWoInstMepElCustomTO().getIdCurrentTransformerPTO(),
                    woInstMeasurepointCustomTO.getWoInstMepElCustomTO().getIdCurrentTransformerPTD(),
                    woInstMeasurepointCustomTO.getWoInstMepElCustomTO().getIdCurrentTransformerPTV());

            if (id != null) {
                CurrentTransformerPriTTO currentTransformerPrimaryTTO =
                        ObjectCache.getType(CurrentTransformerPriTTO.class, id);
                return currentTransformerPrimaryTTO;
            }
        }
        } catch (Exception e) {
            writeLog(TAG + " : getCouplingPrimary()", e);
        }
        return null;
    }

    /**
     * Secondary coupling
     *
     * @return
     */
    protected CurrentTransformerSecTTO getCouplingSecondary() {
        try {
            final WorkorderCustomWrapperTO workorder = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            WoInstMeasurepointCustomTO woInstMeasurepointCustomTO = workorder.getWorkorderCustomTO().getWoInst().getInstMeasurepoint();
            if (woInstMeasurepointCustomTO != null && woInstMeasurepointCustomTO.getWoInstMepElCustomTO() != null) {
                Long id = TypeDataUtil.getOrgOrDiv(
                        woInstMeasurepointCustomTO.getWoInstMepElCustomTO().getIdCurrentTransformerSTO(),
                        woInstMeasurepointCustomTO.getWoInstMepElCustomTO().getIdCurrentTransformerSTD(),
                        woInstMeasurepointCustomTO.getWoInstMepElCustomTO().getIdCurrentTransformerSTV());

                if (id != null) {
                    CurrentTransformerSecTTO currentTransformerSecondaryTTO =
                            ObjectCache.getType(CurrentTransformerSecTTO.class, id);
                    return currentTransformerSecondaryTTO;
                }
            }
        } catch (NumberFormatException e) {
            writeLog(TAG + " :getCouplingSecondary()", e);
        }
        return null;
    }


    public ArrayMap<String, Object> getUserChoiceValuesForPage() {
        try {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.MANUAL_CHANGE_CT_PARAMS, mIsChangeManually ? "YES" : "NO");
            //stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.RATIO_VALUE, mRatio);
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.RATIO_VALUE,  mManualEditText.getText().toString());
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.PRIMARY_VALUE_INDEX, mPriSpinnerIndex + "");
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.SECONDARY_VALUE_INDEX, mSecSpinnerIndex + "");
        } catch (NumberFormatException e) {
            writeLog(TAG + " : getUserChoiceValuesForPage()", e);
        }
        return stepfragmentValueArray;
    }

    /**
     * Lookup and initialize all the view widgets
     */
    private void initialize() {
        // Initialize the view widgets

        try {
            mManualCheckbox = fragmentView.findViewById(R.id.manual_change_cb);
            mMeterConstantEt = fragmentView.findViewById(R.id.meter_constant_value_et);
            mManualEditText = fragmentView.findViewById(R.id.manual_meter_constant_et);
            mManualEditText.setEnabled(false);
            primaryLayout = fragmentView.findViewById(R.id.primary_layout);
            secondaryLayout=fragmentView.findViewById(R.id.secondary_layout);
            mPrimarySpinner = fragmentView.findViewById(R.id.primary_current_spinner);
            mSecondarySpinner = fragmentView.findViewById(R.id.secondary_current_spinner);
        } catch (NumberFormatException e) {
            writeLog(TAG + " : initialize()", e);
        }

    }


    private void populateData() {
        try {
            final List<CurrentTransformerPriTTO> ctPrittoList = ObjectCache.getAllTypes(CurrentTransformerPriTTO.class);
            final List<CurrentTransformerSecTTO> ctSecttoList = ObjectCache.getAllTypes(CurrentTransformerSecTTO.class);
            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (modifiableWo != null
                    && modifiableWo.getWorkorderCustomTO() != null
                    && modifiableWo.getWorkorderCustomTO().getWoInst() != null
                    && modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null
                    && modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepElCustomTO() != null) {
                mMeterConstant = modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepElCustomTO().getRegisterConstantO();
            }
            if(ctPrittoList.get(0) != null )
                ctPrittoList.add(0,null);
            if(ctSecttoList.get(0)!=null)
                ctSecttoList.add(0,null);
            CurrentAmpsAdapter primaryAdapter = new CurrentAmpsAdapter<CurrentTransformerPriTTO>(getActivity(), ctPrittoList, true);
            CurrentAmpsAdapter secondaryAdapter = new CurrentAmpsAdapter<CurrentTransformerSecTTO>(getActivity(), ctSecttoList, false);
            mPrimarySpinner.setAdapter(primaryAdapter);
            mSecondarySpinner.setAdapter(secondaryAdapter);

            if (mMeterConstant != null) {
                mMeterConstantEt.setText(mMeterConstant + "");
            }

            //TODO use the static method in AndroidUtilsAstSep to set the spinner adapter


            if (getCouplingPrimary() != null &&
                    getCouplingSecondary() != null) {

                if (getCouplingPrimary().getName() != null) {
                    int spinnerPosition = primaryAdapter.getPosition(getCouplingPrimary().getName());
                    mPrimarySpinner.setSelection(spinnerPosition);
                }
                if (getCouplingSecondary().getName() != null) {
                    int spinnerPosition = secondaryAdapter.getPosition(getCouplingSecondary().getName());
                    mSecondarySpinner.setSelection(spinnerPosition);
                }

            }
            mManualCheckbox.setChecked(mIsChangeManually);
           if(Utils.isNotEmpty(mRatio))
                mManualEditText.setText(mRatio);
            else
                mManualEditText.setText("");

            if (mPriSpinnerIndex > -1) {
                mPrimarySpinner.setSelection(mPriSpinnerIndex);
            }
            if (mSecSpinnerIndex > -1) {
                mSecondarySpinner.setSelection(mSecSpinnerIndex);
            }
            updatemManualEditText();
        } catch (NumberFormatException e) {
            writeLog(TAG + " : populateData()", e);
        }

    }


    private void setupListeners() {
        try{
        mManualCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mIsChangeManually = true;
                    mManualEditText.setEnabled(true);
                    primaryLayout.setVisibility(View.GONE);
                    secondaryLayout.setVisibility(View.GONE);
                } else {
                    mIsChangeManually = false;
                    mManualEditText.setEnabled(false);
                    primaryLayout.setVisibility(View.VISIBLE);
                    secondaryLayout.setVisibility(View.VISIBLE);
                }

            }
        });


        mPrimarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPriSpinnerIndex = position;
                updatemManualEditText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSecondarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSecSpinnerIndex = position;
                updatemManualEditText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.i(TAG, "SET UP LISTENERS");
        } catch (Exception e) {
            writeLog(TAG + " : setUpListeners()", e);
        }
    }

    private void updatemManualEditText() {
        int primaryAmps, secondaryAmps;
        try {
            if(mPrimarySpinner.getSelectedItem()!=null && mSecondarySpinner.getSelectedItem()!=null && !mIsChangeManually )
            {
                primaryAmps = Integer.parseInt(((CurrentTransformerPriTTO) mPrimarySpinner.getSelectedItem()).getName().replace("A", ""));
                secondaryAmps = Integer.parseInt(((CurrentTransformerSecTTO) mSecondarySpinner.getSelectedItem()).getName().replace("A", ""));
                mManualEditText.setText("" + primaryAmps / secondaryAmps);
                mRatio = String.valueOf(primaryAmps / secondaryAmps);
            }
            else if (mIsChangeManually){
                mManualEditText.setText(mRatio);
            }
            else
                mManualEditText.setText("");


        } catch (Exception e) {
            writeLog(TAG + " : updatemManualEditText()", e);
        }

    }

    public boolean validateUserInput() {
        Boolean returnValue = null;
        getUserChoiceValuesForPage();
        try {
            if (!mIsChangeManually) {
                returnValue = true;

            } else {
                returnValue = !TextUtils.isEmpty(mManualEditText.getText());
            }
            if (returnValue)
                applyChangesToModifiableWO();
        } catch (NumberFormatException e) {
            writeLog(TAG + " :  validateUserInput()", e);
        }
        return returnValue;
    }

    /**
     * When the user navigates away from this page
     * save the user progress in this method
     */
    public void applyChanges() {
        try{
        WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        if (modifiableWo != null
                && modifiableWo.getWorkorderCustomTO() != null
                && modifiableWo.getWorkorderCustomTO().getWoInst() != null
                && modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null
                && modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepElCustomTO() != null) {
            try {
                if(mIsChangeManually || (mManualEditText.getText().toString()!="" && mManualEditText.getText().toString()!=mMeterConstantEt.getText().toString()))
                {

                    mRatio=mManualEditText.getText().toString();
                    modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepElCustomTO().setRegisterConstantV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepElCustomTO().setRegisterConstantD(Long.parseLong(mRatio));
                }
                else
                {
                    modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepElCustomTO().setRegisterConstantV(CONSTANTS().SYSTEM_BOOLEAN_T__FALSE);
                }
            } catch (NumberFormatException e) {
                writeLog(TAG + " : applyChanges()", e);
            }
        }
        } catch (Exception e) {
            writeLog(TAG + " : applyChanges()", e);
        }
    }

    /**
     * User has not selected any option
     * and tries to move forward, user should be prompted
     * to select an option
     */

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
    public void onClick(final View v) {
        if (v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}