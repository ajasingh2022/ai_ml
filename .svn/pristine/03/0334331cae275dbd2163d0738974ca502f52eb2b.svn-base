package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.StringUtil;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterInstallationCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterInstallationDM;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;
import static com.skvader.rsp.cft.common.util.Utils.isEmpty;


/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint("ValidFragment")
public class MeterInstallationCheckFragment extends CustomFragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnTouchListener {

    protected LinearLayout mepCodeLinearLayout;
    protected TextView measureCodeTxLbl;
    protected ImageButton scanMepcode;
    protected TextView existingMepCodeTx;
    protected EditText measureCodeTx;
    protected CheckBox missingLblCheck;
    protected TextView missingLabel;
    protected LinearLayout meterNumberLinearLayout;
    protected TextView scnmeternumtx;
    protected ImageButton scanMeterNumber;
    protected TextView meterNumRdOnlyTx;
    protected EditText meterNumTx;
    protected transient Dialog validationDialog = null;

    protected transient boolean mandatoryFieldsMissing;

    protected transient String existingMepNumber = null;
    protected transient String existingMeterNo = null;
    protected transient boolean performValidation;
    protected transient String flowName = null;
    protected transient int nextPageIdentifier = -1;

    protected transient final String USER_INPUT_MEASUREPOINT_CODE = "USER_INPUT_MEASUREPOINT_CODE";
    protected transient final String USER_INPUT_METER_NUMBER = "USER_INPUT_METER_NUMBER";
    protected transient final String USER_INPUT_LABEL_MISSING = "USER_INPUT_LABEL_MISSING";
    static String TAG = MeterInstallationCheckFragment.class.getSimpleName();

    public MeterInstallationCheckFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    protected interface NextPageIdentifier {
        int VERIFY_INSTALLATION_INFO = 1;
        int NOT_POSSIBLE = 2;
        int PRINT_MEASUREPOINT_LABEL = 3;
    }


    //From Page

    public String getNextPageIdentifier() {
        return String.valueOf(nextPageIdentifier);

    }

    public String evaluateNextPage() {
        String nextPage = null;
        try {
            switch (nextPageIdentifier) {
                case NextPageIdentifier.VERIFY_INSTALLATION_INFO:
                    nextPage = ConstantsAstSep.FlowPageConstants.VERIFYINSTALLINFO;
                    break;

                case NextPageIdentifier.NOT_POSSIBLE:
                    nextPage = ConstantsAstSep.FlowPageConstants.NEGATIVE_FLOW_PAGE;
                    break;

                case NextPageIdentifier.PRINT_MEASUREPOINT_LABEL:
                    nextPage = ConstantsAstSep.PageNavConstants.NEXT_PAGE_PRINT_LABEL;
                    break;
            }
        } catch (Exception e) {
            writeLog(TAG + "  : evaluateNextPage() ", e);
        }
        return nextPage;
    }

    /**
     * Apply changes to modifiable workorder
     */
    @Override
    public void applyChangesToModifiableWO() {
        // No implementation required since this is only a validation page
    }

    /**
     * Initialize Page values
     */
    public void initializePageValues() {
        try {
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            existingMepNumber = getMepCode(wo);
            existingMeterNo = getMeterNumber(wo);
        } catch (Exception e) {
            writeLog(TAG + " : initializePageValues() ", e);
        }

    }

    /**
     * Fetches the Measurepoint Code from the work order
     *
     * @param wo
     * @return
     */
    protected String getMepCode(WorkorderCustomWrapperTO wo) {
        String mepCode = null;
        try {
            if (wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoInst() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().getMeasurepointCode() != null) {
                mepCode = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().getMeasurepointCode();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getMepCode() ", e);
        }
        return mepCode;
    }

    /**
     * Get the meter number
     *
     * @param wo
     * @return
     */
    protected String getMeterNumber(WorkorderCustomWrapperTO wo) {
        return UnitInstallationUtils.getExistingMeterNumber(wo);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.meter_accessibility_layout, null);
        initializePageValues();
        initViews();
        populateViews();
        setUpListeners();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    /**
     * Set up UI listeners
     */
    protected void setUpListeners() {
        // Set verification listener for meter number
        if (meterNumTx != null) {
            // Either after each key press or after IME action done
            measureCodeTx.setText(AndroidUtilsAstSep.removePrefixFromInstIfPresent(measureCodeTx.getText().toString(), true));
            meterNumTx.addTextChangedListener(new MepCodeMeterNumberTextWatcher(USER_INPUT_METER_NUMBER, existingMeterNo, meterNumTx));

        }

        // Set verification listener for measurepoint code
        if (measureCodeTx != null) {
            measureCodeTx.setText(AndroidUtilsAstSep.removePrefixFromInstIfPresent(measureCodeTx.getText().toString(), true));
            measureCodeTx.addTextChangedListener(new MepCodeMeterNumberTextWatcher(USER_INPUT_MEASUREPOINT_CODE, existingMepNumber, measureCodeTx));
        }

        // Attach listener for "Label missing check box"
        if (missingLblCheck != null) {
            missingLblCheck.setOnCheckedChangeListener(this);
        }
    }


    protected boolean checkUserInput(final String userProvideValue, final String existingValue, final TextView tv) {
        boolean state = false;
        Log.d(this.getClass().getSimpleName(), "User provided value :" + userProvideValue + " Existing value:" + existingValue);
        // Validate only if "Label is missing" is not selected
        if (tv != null && existingValue != null && userProvideValue != null) {
            if (userProvideValue.equals(existingValue)) {
                state = true;
                // tv.setBackgroundDrawable(genericGreenBackground);
            } else {
                if (StringUtil.isNotEmpty(userProvideValue)) {
                    //   tv.setBackgroundDrawable(genericRedBackground);
                } else {
                    // tv.setBackgroundDrawable(defaultRoundedGreen);
                }
            }
        }
        return state;
    }

    /**
     * Initialize UI views
     */
    protected void initViews() {

        PackageManager pm = getActivity().getPackageManager();
        //If the device does not have built-in camera, cam scanner button will be hidden.
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            scanMepcode = fragmentView.findViewById(R.id.scanMepcode);
            scanMepcode.setVisibility(View.GONE);
            scanMeterNumber = fragmentView.findViewById(R.id.scanMeterNumber);
            scanMeterNumber.setVisibility(View.GONE);
        }

        mepCodeLinearLayout = fragmentView.findViewById(R.id.mepCodeLinearLayout);
        measureCodeTxLbl = fragmentView.findViewById(R.id.measureCodeTxLbl);
        existingMepCodeTx = fragmentView.findViewById(R.id.existingMepCodeTx);
        measureCodeTx = fragmentView.findViewById(R.id.measureCodeTx);
        missingLblCheck = fragmentView.findViewById(R.id.missingLblCheck);
        missingLabel = fragmentView.findViewById(R.id.missingLabel);
        meterNumberLinearLayout = fragmentView.findViewById(R.id.meterNumberLinearLayout);
        scnmeternumtx = fragmentView.findViewById(R.id.scnmeternumtx);
        meterNumRdOnlyTx = fragmentView.findViewById(R.id.meterNumRdOnlyTx);
        meterNumTx = fragmentView.findViewById(R.id.meterNumTx);

        /**
         * Drawable backgrounds
         */
        /*disabledBackground = getActivity().getResources().getDrawable(R.drawable.rounded_corner_disabled);
        genericRedBackground = getActivity().getResources().getDrawable(R.drawable.rounded_corner_light_red_background);
        genericGreenBackground = getActivity().getResources().getDrawable(R.drawable.rounded_corner_light_green_background);
        defaultRoundedGreen = getActivity().getResources().getDrawable(R.drawable.rounded_corner_generic);*/

        /**
         * Hide the existing meter number layout if it is an installation work-order
         */
        flowName = this.getActivity().getClass().getSimpleName();
        if(this.getActivity() instanceof MeterInstallationCT ||
                this.getActivity() instanceof MeterInstallationDM) {
            View meterNumberLinearLayout = fragmentView.findViewById(R.id.meterNumberLinearLayout);
            meterNumberLinearLayout.setVisibility(View.GONE);
        }


        performValidation = true;


    }

    /**
     * Populate the UI fragments with values
     */
    protected void populateViews() {
        try{
        // Set the existing measurepoint code and meter number
        existingMepCodeTx.setText(Utils.isNotEmpty(existingMepNumber) ? existingMepNumber : "");
        meterNumRdOnlyTx.setText(Utils.isNotEmpty(existingMeterNo) ? existingMeterNo : "");

        //Set the user entered measurepoint code and meter number
        if (!stepfragmentValueArray.isEmpty()) {
            measureCodeTx.setText(String.valueOf(Utils.isNotEmpty(stepfragmentValueArray.get(USER_INPUT_MEASUREPOINT_CODE)) ? stepfragmentValueArray.get(USER_INPUT_MEASUREPOINT_CODE) : ""));
            meterNumTx.setText(String.valueOf(Utils.isNotEmpty(stepfragmentValueArray.get(USER_INPUT_METER_NUMBER)) ? stepfragmentValueArray.get(USER_INPUT_METER_NUMBER) : ""));
            checkUserInput((String) stepfragmentValueArray.get(USER_INPUT_MEASUREPOINT_CODE), existingMepNumber, measureCodeTx);
            checkUserInput((String) stepfragmentValueArray.get(USER_INPUT_METER_NUMBER), existingMeterNo, meterNumTx);
        }
        //Set the label missing
        boolean isChecked = "YES".equalsIgnoreCase((String) stepfragmentValueArray.get(USER_INPUT_LABEL_MISSING));
        if (isChecked) {
            missingLblCheck.setChecked(true);
            measureCodeTx.setText("");
            //measureCodeTx.setBackgroundDrawable(disabledBackground);
        }
        measureCodeTx.setFocusable(!isChecked);
        measureCodeTx.setFocusableInTouchMode(!isChecked);
        measureCodeTx.setClickable(!isChecked);
        } catch (Exception e) {
            writeLog(TAG + " : populateViews() ", e);
        }
    }

    /**
     * User has not selected any mandatory option
     * and tries to move forward, user should be prompted
     * to select an option
     */
    @Override
    public void showPromptUserAction() {
            /*
             *  If status is false then it is coming from flow engine stack rebuild
			 *  and does not require user attention
			 */
        if (mandatoryFieldsMissing) {
            final View alertView
                    = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.prompt_user_response_layout, null);
            if (alertView != null) {
                final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
                okButton.setOnClickListener(this);

                final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                        alertView, null, null);
                validationDialog = builder.create();
                validationDialog.show();
            }
        }
    }

    /**
     * Validate User input
     *
     * @return
     */
    @Override
    public boolean validateUserInput() {
        boolean isValidInput;
        if (!AbstractWokOrderActivity.resuming) {
            if ("YES".equals(stepfragmentValueArray.get(USER_INPUT_LABEL_MISSING))) {
                if(!(this.getActivity() instanceof MeterInstallationCT) &&
                        !(this.getActivity() instanceof MeterInstallationDM)){

                    isValidInput = !isEmpty(stepfragmentValueArray.get(USER_INPUT_METER_NUMBER));

                } else {
                    isValidInput = missingLblCheck.isChecked();
                }

            } else {
                if(!(this.getActivity() instanceof MeterInstallationCT) &&
                        !(this.getActivity() instanceof MeterInstallationDM)) {
                    isValidInput = Utils.isNotEmpty(stepfragmentValueArray.get(USER_INPUT_MEASUREPOINT_CODE)) && Utils.isNotEmpty(stepfragmentValueArray.get(USER_INPUT_METER_NUMBER));
                } else {
                    isValidInput = Utils.isNotEmpty(stepfragmentValueArray.get(USER_INPUT_MEASUREPOINT_CODE));
                }
            }
            mandatoryFieldsMissing = !isValidInput;
            if (!mandatoryFieldsMissing) {
                isValidInput = validateData();
            }


            return isValidInput;
        } else return true;
    }

    /**
     * For handling the label missing check change
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView != null && buttonView.getId() == R.id.missingLblCheck) {
            if (measureCodeTx != null) {
                if (isChecked) {
                    nextPageIdentifier = NextPageIdentifier.PRINT_MEASUREPOINT_LABEL;
                    measureCodeTx.setText("");
                    measureCodeTx.setLongClickable(false);
                    existingMepCodeTx.setLongClickable(false);
                    // measureCodeTx.setBackgroundDrawable(disabledBackground);
                    stepfragmentValueArray.put(USER_INPUT_LABEL_MISSING, "YES");
                } else {
                    nextPageIdentifier = NextPageIdentifier.VERIFY_INSTALLATION_INFO;
                    //measureCodeTx.setBackgroundDrawable(defaultRoundedGreen);
                    measureCodeTx.setLongClickable(true);
                    existingMepCodeTx.setLongClickable(true);
                    stepfragmentValueArray.put(USER_INPUT_LABEL_MISSING, "NO");
                }
                measureCodeTx.setFocusable(!isChecked);
                measureCodeTx.setFocusableInTouchMode(!isChecked);
                measureCodeTx.setClickable(!isChecked);
            }

        }
    }

    /**
     * Handle on touch event
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && (v.getId() == R.id.okButtonBtPrint
                || v.getId() == R.id.noButtonBtPrint)) {
            // v.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_button_negative_enabling_disabling));
        } else if (event.getAction() == MotionEvent.ACTION_UP
                && (v.getId() == R.id.okButtonBtPrint
                || v.getId() == R.id.noButtonBtPrint)) {
            //v.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_button_disabled));
            v.performClick();
        }

        return true;
    }

    /**
     * Validate data on the UI for next page evaluation
     *
     * @return
     */
    protected boolean validateData() {
        boolean state = true;
        try{
        boolean meterNumChk = false;
        final StringBuilder builder = new StringBuilder();
        if (performValidation) {
            // Perform validation
            state = state & (checkUserInput(measureCodeTx.getText().toString(), existingMepNumber, measureCodeTx) ||
                    "YES".equals(stepfragmentValueArray.get(USER_INPUT_LABEL_MISSING)));
            if (!state) {
                builder.append(getResources().getString(R.string.wrong_mep_id)).append("\n");
            }
            if(!(this.getActivity() instanceof MeterInstallationCT) &&
                    !(this.getActivity() instanceof MeterInstallationDM)) {
                if ("YES".equals(stepfragmentValueArray.get(USER_INPUT_LABEL_MISSING))) {

                    if (!checkUserInput(meterNumTx.getText().toString(), existingMeterNo, meterNumTx)) {
                        builder.append(getResources().getString(R.string.wrong_meter)).append("\n");
                        state = false;
                        meterNumChk = true;
                    } else {
                        state = true;
                        nextPageIdentifier = NextPageIdentifier.VERIFY_INSTALLATION_INFO;
                    }

                }
                if (!checkUserInput(meterNumTx.getText().toString(), existingMeterNo, meterNumTx)) {
                    if (meterNumChk == false) {
                        builder.append(getResources().getString(R.string.wrong_meter)).append("\n");
                    }
                    state = false;
                } else {
                    if (isEmpty(builder.toString())) {
                        state = true;
                        nextPageIdentifier = NextPageIdentifier.VERIFY_INSTALLATION_INFO;
                    } else {
                        state = false;
                    }
                }

            } else {
                if ("YES".equals(stepfragmentValueArray.get(USER_INPUT_LABEL_MISSING))) {
                    state = true;
                    nextPageIdentifier = NextPageIdentifier.VERIFY_INSTALLATION_INFO;

                } else {

                    if (!checkUserInput(measureCodeTx.getText().toString(), existingMepNumber, measureCodeTx)) {
                        state = false;
                    } else {
                        state = true;
                        nextPageIdentifier = NextPageIdentifier.VERIFY_INSTALLATION_INFO;
                    }

                }
            }
            if (Utils.isNotEmpty(builder.toString())) {
                builder.append(getResources().getString(R.string.do_you_want_to_continue));
                showErrorPopup(builder.toString());
            }

        }
        } catch (Exception e) {
            writeLog(TAG + " : vslidateData() ", e);
        }
        return state;
    }


    /**
     * This method is called if user has provided incorrect measurepoint code
     * and/or meter number and clicks next to proceed with the WO.
     * <p/>
     * <p>
     * As per existing PDA logic, user can still proceed with the WO
     * after providing incorrect measurepoint and/or meter number
     * </p>
     *
     * @param validationErrMsg
     */
    protected void showErrorPopup(final String validationErrMsg) {
        try{
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.popup_ask_bluetooth_print_choice, null);

        if (alertView != null) {

            //Remove all print dialogue box components from Standard

            final TextView errorMsg = alertView.findViewById(R.id.errorMsg);
            final Button okButton = alertView.findViewById(R.id.okButtonBtPrint);
            final Button noButton = alertView.findViewById(R.id.noButtonBtPrint);

            //below portion of code removed to remove the Missing Print Lable pop up


            okButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);

            if (!Utils.safeToString(validationErrMsg).equals("")) {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(validationErrMsg);
            } else {
                errorMsg.setVisibility(View.GONE);
            }

            okButton.setOnClickListener(this);
            noButton.setOnClickListener(this);


            final AlertDialog.Builder builder =
                    GuiController.getCustomAlertDialog(getActivity(), alertView, null, null);
            validationDialog = builder.create();
            validationDialog.setCancelable(false);
            validationDialog.show();
        }
        } catch (Exception e) {
            writeLog(TAG + " : showErrorPopup() ", e);
        }
    }

    /**
     * Handle onClick event
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        try{
        AbstractWokOrderActivity abstractWokOrderActivity = (AbstractWokOrderActivity) this.getActivity();
        // Button for Print Label Functionality - Go with Print Label
        if (v != null && v.getId() == R.id.goWithBtPrint) {
            // Hide the parent dialog
            validationDialog.dismiss();
            nextPageIdentifier = NextPageIdentifier.PRINT_MEASUREPOINT_LABEL;
        } else if (v != null && (v.getId() == R.id.dontGoWithBtPrint || v.getId() == R.id.okButtonBtPrint)) {
            // Hide the parent dialog
            performValidation = false;
            validationDialog.dismiss();
            nextPageIdentifier = NextPageIdentifier.VERIFY_INSTALLATION_INFO;
            validateUserInput();
            abstractWokOrderActivity.meterInstallationCheckResponse(stepIdentifier, evaluateNextPage());
            abstractWokOrderActivity.getSteppersView().nextStep();
            performValidation = true;
        } else if (v != null && v.getId() == R.id.noButtonBtPrint) {
            performValidation = false;
            validationDialog.dismiss();
            nextPageIdentifier = NextPageIdentifier.NOT_POSSIBLE;
            validateUserInput();
            abstractWokOrderActivity.meterInstallationCheckResponse(stepIdentifier, evaluateNextPage());
            abstractWokOrderActivity.getSteppersView().nextStep();
            performValidation = true;
        } // Button for Mandatory message dialog
        else if (v != null && v.getId() == R.id.okButtonYesNoPage) {
            validationDialog.dismiss();
        }
        } catch (Exception e) {
            writeLog(TAG + " : onClick() ", e);
        }
    }


    /**
     * TextWatcher class for handling the validation
     */
    protected class MepCodeMeterNumberTextWatcher implements TextWatcher {

        private String pagePreferenceUserInputKey;
        private String existingValue;
        private EditText editTextComponent;

        public MepCodeMeterNumberTextWatcher(final String pagePreferenceUserInputKey,
                                             final String existingValue,
                                             final EditText editTextComponent) {
            this.pagePreferenceUserInputKey = pagePreferenceUserInputKey;
            this.existingValue = existingValue;
            this.editTextComponent = editTextComponent;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            checkUserInput(s.toString(), existingValue, editTextComponent);
            stepfragmentValueArray.put(pagePreferenceUserInputKey, s.toString());
        }
    }
}