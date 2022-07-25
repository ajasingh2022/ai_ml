package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.driver.iface.LabelPrinter;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConnectBTPrinterThread;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.ui.activity.printer.PrintScannedIdActivity;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint("ValidFragment")
public class PrintLabelFragment extends CustomFragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private transient Dialog dialog = null;
    String existingMepNumber;
    protected int LABEL_SCAN_REQUEST = 1003;
    protected EditText incorrectValueEt;
    protected EditText correctValueEt;
    protected Button printLabelBtn;
    protected EditText printTypeEt;
    protected EditText copiesEt;
    protected Button checkStatusBtn;
    protected Button printBtn;
    protected EditText printerStatusEt;
    protected CheckBox labelMountedCb;
    protected CheckBox unabalePrintCb;
    protected LinearLayout scanLayout;
    protected Button scanBtn;
    protected EditText measureCodeTv;
    private String btPrinterMacAddress;
    protected int validationConditonFlag = 0;

    public PrintLabelFragment() {
        super();

    }

    public PrintLabelFragment(String stepIdentifier) {
        super(stepIdentifier);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.print_label_layout, null);
        initializePageValues();
        initViews();
        initializePageValues();
        populateViews();
        setUpListeners();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getBtPrinterMacAddress() == null) {
            setBtPrinterMacAddress(SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.BT_MAC_ADDRESS));
            Log.d(TAG, "Bluetooth Mac address :: " + getBtPrinterMacAddress());
        }


    }


    private void initViews() {
        //getView() changed to fragmentView
        incorrectValueEt = fragmentView.findViewById(R.id.incorrect_value_et);
        correctValueEt = fragmentView.findViewById(R.id.correct_value_et);
        printLabelBtn = fragmentView.findViewById(R.id.print_label_btn);
        printTypeEt = fragmentView.findViewById(R.id.print_type_et);
        copiesEt = fragmentView.findViewById(R.id.copies_et);
        checkStatusBtn =fragmentView.findViewById(R.id.check_status_btn);
        printBtn = fragmentView.findViewById(R.id.print_btn);
        printerStatusEt = fragmentView.findViewById(R.id.printer_status_et);
        labelMountedCb = fragmentView.findViewById(R.id.label_mounted_cb);
        unabalePrintCb = fragmentView.findViewById(R.id.unabale_print_cb);
        scanLayout = fragmentView.findViewById(R.id.scan_layout);
        scanBtn = fragmentView.findViewById(R.id.scan_btn);
        measureCodeTv = fragmentView.findViewById(R.id.measureCodeTx);
        if(labelMountedCb.isChecked()){
            scanLayout.setVisibility(View.VISIBLE);
        }else if(unabalePrintCb.isChecked()){
            scanLayout.setVisibility(View.GONE);
        }
    }

    private void setUpListeners() {
        checkStatusBtn.setOnClickListener(this);
        printBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(this);
        labelMountedCb.setOnCheckedChangeListener(this);
        unabalePrintCb.setOnCheckedChangeListener(this);
    }

    private void populateViews() {
        correctValueEt.setText(existingMepNumber);
        printTypeEt.setText(getString(R.string.measurepoint_id));
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked)
            if (compoundButton.getId() == R.id.label_mounted_cb) {
                scanLayout.setVisibility(View.VISIBLE);
                unabalePrintCb.setChecked(!checked);
            } else if (compoundButton.getId() == R.id.unabale_print_cb) {
                scanLayout.setVisibility(View.GONE);
                labelMountedCb.setChecked(!checked);
            }
    }

    @Override
    public void onClick(View view) {
        try{
        if (view.getId() == R.id.check_status_btn) {
            checkPrinterStatus();
        } else if (view.getId() == R.id.print_btn) {
            if(AndroidUtilsAstSep.validateExistingBtPrinterSettings(getActivity(),getBtPrinterMacAddress())) {
                Intent printScannedId = new Intent(getActivity().getApplicationContext(), PrintScannedIdActivity.class);
                startActivity(printScannedId);
            }
        } else if (view.getId() == R.id.scan_btn) {
            AndroidUtilsAstSep.scanBarCode(this, LABEL_SCAN_REQUEST);
        } else if (view.getId() == R.id.okButtonYesNoPage) {
            dialog.dismiss();
        }
        }catch(Exception e) {
            writeLog("PrintLabelFragment : onClick() " ,e);
        }
    }
    /**
     * Initialize Page values
     */
    public void initializePageValues() {
        super.initializePageValues();
        try{
        WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        existingMepNumber = getMepCode(wo);
        }catch(Exception e) {
            writeLog("PrintLabelFragment : initializePageValues() " ,e);
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
        try{
        if (wo.getWorkorderCustomTO() != null
                && wo.getWorkorderCustomTO().getWoInst() != null
                && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null
                && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO() != null
                && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().getMeasurepointCode() != null) {
            mepCode = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().getMeasurepointCode();
        }
        }catch(Exception e) {
            writeLog("PrintLabelFragment : getMepCode() " ,e);
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


    /**
     * The method would be called
     * to know if user has selected any option (return true)
     * or just has clicked next without any selection (return false)
     *
     * @return boolean
     */
    public boolean validateUserInput() {
        boolean status = true;
        try{
        String enteredMepNumber = measureCodeTv.getText().toString();

        if (!unabalePrintCb.isChecked() && !labelMountedCb.isChecked()) {
            validationConditonFlag = 1;
            status = false;
        } else if(labelMountedCb.isChecked() &&
                (!Utils.isNotEmpty(enteredMepNumber))){
            validationConditonFlag = 1;
            status = false;
        } else if(labelMountedCb.isChecked() &&
                !enteredMepNumber.equals(existingMepNumber)) {
            validationConditonFlag = 2;
            status = false;
        }
        }catch(Exception e) {
            writeLog("PrintLabelFragment : validateUserInput() " ,e);
        }
        return status;
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

        String validationMsg = null;
        switch (validationConditonFlag) {
            case 1 : validationMsg = getString(R.string.one_or_more_mandatory_field_is_missing);
                break;

            case 2: validationMsg = getString(R.string.one_or_more_entries_have_invalid_inputs);
                break;
        }

        final View alertView
                = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.prompt_user_response_layout, null);
        if (alertView != null) {
            final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
            okButton.setOnClickListener(this);

            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                    alertView, validationMsg, null);

            dialog = builder.create();
            dialog.show();
        }
    }

    public void checkPrinterStatus() {
        try{
        if (AndroidUtilsAstSep.validateExistingBtPrinterSettings(getActivity(), getBtPrinterMacAddress())) {
            AndroidUtilsAstSep.turnOnOffBluetooth(true);
            final LabelPrinter labelPrinter = ApplicationAstSep.getDriverManager().getLabelPrinter();
            new ConnectBTPrinterThread(getActivity(), labelPrinter, getBtPrinterMacAddress()).start();
        }
        }catch(Exception e) {
            writeLog("PrintLabelFragment : checkPrinterStatus() " ,e);
        }
    }


    public String getBtPrinterMacAddress() {
        return btPrinterMacAddress;
    }

    public void setBtPrinterMacAddress(String btPrinterMacAddress) {
        this.btPrinterMacAddress = btPrinterMacAddress;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        try{
        if (requestCode == LABEL_SCAN_REQUEST && resultCode == Activity.RESULT_OK) {
            measureCodeTv.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
            AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(), false);
        }
        }catch(Exception e) {
            writeLog("PrintLabelFragment : onActivityResult() " ,e);
        }
    }

}