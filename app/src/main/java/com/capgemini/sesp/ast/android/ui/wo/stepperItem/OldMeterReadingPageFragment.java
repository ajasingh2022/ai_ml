package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.RegisterTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.TariffTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitMeterRegReadTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitMeterRegTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitMeterRegisterCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class OldMeterReadingPageFragment extends CustomFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private transient ListView listViewExistingMeterReadReg = null;
    private transient UnitMeterRegReadAdapters adapter = null;
    private transient Dialog dialog = null;
    static String TAG = OldMeterReadingPageFragment.class.getSimpleName();
    private transient CheckBox cantbereadcheckbox = null;
    private transient View collapsibleLayout = null;
    private transient EditText notReadReasonEditText = null;
    private transient SoftReference<List<View>> meterReadRegsViewLs = null;
    transient boolean canNotRead = false;
    private transient boolean canNotReadAtStart = false;
    private String readValues = null;
    private String readValuesAtStart = null;
    String notReadReason = null;
    private String notReadReasonAtStart = null;
    transient List<WoUnitMeterRegisterCustomTO> uniMeterRegisters = null;
    private transient String[] rValues = null;
    transient Map<WoUnitMeterRegTO, Double> readingMap = new ArrayMap<WoUnitMeterRegTO, Double>();
    transient boolean validDataProvided = false;

    protected transient String flowName = null;
    private int registerRadingLengthLimit = 6;

    public OldMeterReadingPageFragment() {
        super();
    }

    public OldMeterReadingPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_read_old_meter_reading, null);
        initialize();
        initializePageValues();
        setupListeners();
        populateData();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

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

    public void applyChangesToModifiableWO() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (canNotRead) {
                WoInfoCustomTO woInfoTO = WorkorderUtils.createInfo(notReadReason, SessionState.getInstance().getCurrentUserUsername(),
                        CONSTANTS().INFO_SOURCE_T__SESP_PDA, CONSTANTS().INFO_T__NO_REG_READ_REASON);
                WorkorderUtils.addWoInfoCustomMTO(wo, woInfoTO);
            } else {
                List<WoUnitMeterRegisterCustomTO> uniMtrRegs = UnitInstallationUtils.getUnitMeterRegisters(wo, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
                if (uniMtrRegs != null)
                    for (WoUnitMeterRegisterCustomTO customTo : uniMtrRegs) {
                        final WoUnitMeterRegTO unitMeterRegTo = customTo.getWoUnitMeterReg();
                        if (unitMeterRegTo != null) {
                            final Double reading = readingMap.get(unitMeterRegTo);

                            if (reading != null) {
                                List<WoUnitMeterRegReadTO> toLs = customTo.getWoUnitMeterRegReads();
                                if (toLs == null) {
                                    toLs = new ArrayList<WoUnitMeterRegReadTO>();
                                } else {
                                    Iterator<WoUnitMeterRegReadTO> unitMtrRegReadItr = toLs.iterator();
                                    while (unitMtrRegReadItr.hasNext()) {
                                        WoUnitMeterRegReadTO tempUnitMtrRegRead = unitMtrRegReadItr.next();
                                        if (tempUnitMtrRegRead.getIdWoUnitMeterReg() != null) {
                                            if (tempUnitMtrRegRead.getIdWoUnitMeterReg().longValue() == unitMeterRegTo.getId().longValue()) {
                                                unitMtrRegReadItr.remove();
                                                break;
                                            }
                                        }
                                    }
                                }
                                final WoUnitMeterRegReadTO meterRegReadTO = new WoUnitMeterRegReadTO();
                                meterRegReadTO.setIdCase(wo.getIdCase());
                                meterRegReadTO.setIdWoUnitMeterReg(unitMeterRegTo.getId());
                                meterRegReadTO.setIdWoUnitMeterRegReadT(CONSTANTS().WO_UNIT_METER_REG_READ_T__DISMANTLED_METER);
                                meterRegReadTO.setReadingValue(reading);
                                meterRegReadTO.setReadingTimestamp(new Date());
                                toLs.add(meterRegReadTO);
                                customTo.setWoUnitMeterRegReads(toLs);
                            }
                        }
                    }
            }
            flowName = this.getActivity().getClass().getSimpleName();
            if (!flowName.equalsIgnoreCase("MeterRemovalCT")
                    && !flowName.equalsIgnoreCase("MeterRemovalDM")) {
                if (wo != null
                        && wo.getWorkorderCustomTO() != null
                        && wo.getWorkorderCustomTO().getWoUnits() != null) {
                    for (WoUnitCustomTO woUnitCustomTo : wo.getWorkorderCustomTO().getWoUnits()) {
                        boolean existingMeter = false;
                        if (woUnitCustomTo.getWoUnitMeter() != null) {
                            woUnitCustomTo.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                            woUnitCustomTo.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
                            woUnitCustomTo.setUnitDismantledTimestamp(new Date());
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : applyCahnagesToModifiableWO()", e);
        }
    }

    public void getUserChoiceValuesForPage() {
        try {
            if (canNotRead) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.CAN_READ, "NO");
            }
            else
            {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.CAN_READ, "YES");
            }
            if (validDataProvided){
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.VALIDDATAPROVIDED,"YES");
            }
            if (notReadReason != null) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.REASON_NOT_READ, notReadReason);
            }
            String readValue = getRegisterReadingAsString();
            if (readValue != null) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.REG_VALUES, readValue);
            }

        } catch (Exception e) {
            writeLog(TAG + " : getUserChoiceValuesForPage()", e);
        }
    }

    private String getRegisterReadingAsString() {
        String readValue = null;
        try{
        if (readingMap != null && !readingMap.isEmpty()) {
            StringBuilder readBuilder = new StringBuilder();
            Iterator<WoUnitMeterRegTO> unitMeterItr = readingMap.keySet().iterator();
            while (unitMeterItr.hasNext()) {
                WoUnitMeterRegTO tempMeterRegTo = unitMeterItr.next();
                Double dReadValue = readingMap.get(tempMeterRegTo);
                if (dReadValue != null) {
                    readBuilder.append(tempMeterRegTo.getId());
                    readBuilder.append(":");
                    readBuilder.append(dReadValue.toString());
                    readBuilder.append(";");
                }
            }
            readValue = readBuilder.toString();
        }
        } catch (Exception e) {
            writeLog(TAG + " : getRegisterReadingAsString()", e);
        }
        return readValue;
    }

    public void initializePageValues() {
        canNotReadAtStart = false;
        readValues = null;
        readValuesAtStart = null;
        notReadReason = null;
        uniMeterRegisters = null;
        rValues = null;
        Map<String, String> meterRegReadMap = new ArrayMap<String, String>();
        if (stepfragmentValueArray != null) {
            if ("NO".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.CAN_READ))) {
                canNotRead = true;
            }
            notReadReason = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.REASON_NOT_READ);
            readValues = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.REG_VALUES);
            if ("YES".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.VALIDDATAPROVIDED))){
                validDataProvided=true;
            }
        }
        if (readValues != null) {
            rValues = readValues.split(";");
            if (rValues != null) {
                for (String tempString : rValues) {
                    String[] meterRegRead = tempString.split(":");
                    if (meterRegRead != null) {
                        meterRegReadMap.put(meterRegRead[0], meterRegRead[1]);
                    }
                }
            }
        }
        //Get register details from original WO
        uniMeterRegisters = UnitInstallationUtils.getUnitMeterRegisters(AbstractWokOrderActivity.getWorkorderCustomWrapperTO(),
                CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
        //Initialize readValues
        if (uniMeterRegisters != null) {
            for (WoUnitMeterRegisterCustomTO woUnitMeterRegisterCustomTO : uniMeterRegisters) {
                WoUnitMeterRegTO woUnitMeterRegTO = woUnitMeterRegisterCustomTO.getWoUnitMeterReg();
                if (woUnitMeterRegTO != null) {
                    if (meterRegReadMap.get(String.valueOf(woUnitMeterRegTO.getId().longValue())) != null) {
                        readingMap.put(woUnitMeterRegTO, Utils.safeToDouble(meterRegReadMap.get(String.valueOf(woUnitMeterRegTO.getId().longValue())), Locale.getDefault()));
                    }
                }
            }
        }
        canNotReadAtStart = canNotRead;
        readValuesAtStart = readValues;
    }

    @Override
    public boolean validateUserInput() {
        Boolean returnValue = false;
        try {
            getUserChoiceValuesForPage();

            if (cantbereadcheckbox.isChecked()&& Utils.isNotEmpty(notReadReasonEditText.getText().toString())) {
                notReadReason = notReadReasonEditText.getText().toString();
                validDataProvided = true;
            }

            if (canNotRead) {
                returnValue = Utils.isNotEmpty(notReadReasonEditText.getText().toString());
            } else {
                returnValue = validDataProvided;
            }

            if (returnValue)
                applyChangesToModifiableWO();
        } catch (Exception e) {
            writeLog(TAG + " : validateUserInput()", e);
        }
        return returnValue;
    }

    /**
     * Perform lookup for the view objects
     */

    private void initialize() {
        final View parentView = fragmentView;
        if (parentView != null) {
            listViewExistingMeterReadReg = parentView.findViewById(R.id.listViewExistingMeterReadReg);
            cantbereadcheckbox = parentView.findViewById(R.id.cantbereadcheckbox);
            collapsibleLayout = parentView.findViewById(R.id.oldMetrReadingReasonLayout);
            notReadReasonEditText = collapsibleLayout.findViewById(R.id.reason);
            notReadReasonEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //do nothing

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //do nothing
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (notReadReasonEditText.getText() != null && Utils.isNotEmpty(notReadReasonEditText.getText().toString())) {
                        notReadReason = notReadReasonEditText.getText().toString();
                        validDataProvided = true;
                    }
                }
            });

        }
    }

    private void populateData() {
        try {
            if (canNotRead) {
                cantbereadcheckbox.setChecked(true);
                collapsibleLayout.setVisibility(View.VISIBLE);
                notReadReasonEditText.setText(notReadReason);

            }
            if (listViewExistingMeterReadReg != null) {
                adapter = new UnitMeterRegReadAdapters(uniMeterRegisters);
                listViewExistingMeterReadReg.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (uniMeterRegisters != null && uniMeterRegisters.size() > 0) {
                    if (!canNotRead) {
                        listViewExistingMeterReadReg.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : populateData()", e);
        }
    }

    private void setupListeners() {
        if (cantbereadcheckbox != null) {
            cantbereadcheckbox.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {

        if (buttonView.getId() == R.id.cantbereadcheckbox
                && isChecked) {
            // Show reason layout
            collapsibleLayout.setVisibility(View.VISIBLE);
            enableDisableReadingTextBoxes(false);
            listViewExistingMeterReadReg.setVisibility(View.GONE);
            canNotRead = true;
        } else if (buttonView.getId() == R.id.cantbereadcheckbox
                && !isChecked) {
            // Hide reason layout
            collapsibleLayout.setVisibility(View.GONE);
            listViewExistingMeterReadReg.setVisibility(View.VISIBLE);
            listViewExistingMeterReadReg.setAdapter(adapter);
            enableDisableReadingTextBoxes(true);
            canNotRead = false;
        }

    }

    private void enableDisableReadingTextBoxes(final boolean enable) {
        if (meterReadRegsViewLs != null
                && meterReadRegsViewLs.get() != null
                && !meterReadRegsViewLs.get().isEmpty()) {

            for (final View vw : meterReadRegsViewLs.get()) {
                final TextView readingTv = vw.findViewById(R.id.registerReading);
                final TextView tariffTv = vw.findViewById(R.id.tariffReading);
                final ImageButton clearButton = vw.findViewById(R.id.clearRegValueBtn);
                if (readingTv != null) {
                    readingTv.setText("");
                    readingTv.setEnabled(enable);
                }
                if (tariffTv != null) {
                    tariffTv.setText("");
                    tariffTv.setEnabled(enable);
                }

                if (clearButton != null) {
                    clearButton.setEnabled(enable);
                }
            }
        }

    }

    /**
     * Adapter class to dynamically
     */

    private class UnitMeterRegReadAdapters extends BaseAdapter implements View.OnClickListener {

        private transient List<WoUnitMeterRegisterCustomTO> validRegisters = null;


        public UnitMeterRegReadAdapters(final List<WoUnitMeterRegisterCustomTO> validRegisters) {
            this.validRegisters = validRegisters;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (this.validRegisters != null) {
                count = this.validRegisters.size();
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            Object object = null;
            if (this.validRegisters != null && position < this.validRegisters.size()) {
                object = this.validRegisters.get(position);
            }
            return object;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vw = convertView;
            try{
            final WoUnitMeterRegisterCustomTO registerCustomTo = (WoUnitMeterRegisterCustomTO) getItem(position);
            if (registerCustomTo != null && vw == null) {
                vw = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.read_meter_reading_indiv, null);
                final TextView registerTariffTypeText = vw.findViewById(R.id.registerTariffTypeText);
                if (registerTariffTypeText != null) {
                    final StringBuilder regTariffLabelBuilder = new StringBuilder();
                    // Get Register type info
                    final RegisterTTO registerTTO
                            = ObjectCache.getIdObject(RegisterTTO.class, registerCustomTo.getWoUnitMeterReg().getIdRegisterT());
                    if (registerTTO != null) {
                        regTariffLabelBuilder.append(registerTTO.getName()).append(',');
                    }
                    // Get Tariff type info
                    final TariffTTO tariffTTO
                            = ObjectCache.getIdObject(TariffTTO.class, registerCustomTo.getWoUnitMeterReg().getIdTariffT());
                    if (tariffTTO != null) {
                        regTariffLabelBuilder.append(tariffTTO.getName());
                    }
                    registerTariffTypeText.setText(regTariffLabelBuilder.toString());
                }

                long preDecCount = 1;
                long postDecCount = 1;

                final TextView decIndicationTv = vw.findViewById(R.id.decIndicationTv);
                if (decIndicationTv != null) {
                    final StringBuilder digitValidatorBuilder = new StringBuilder();
                    digitValidatorBuilder.append('(');
                    // Get pre - decimal num digits
                    if (registerCustomTo.getWoUnitMeterReg().getNumDigitsV() != null &&
                            registerCustomTo.getWoUnitMeterReg().getNumDigitsV().longValue() == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) {
                        // Get delta value
                        preDecCount = registerCustomTo.getWoUnitMeterReg().getNumDigitsD();
                        digitValidatorBuilder.append(preDecCount);

                    } else {
                        // Get original value
                        preDecCount = registerCustomTo.getWoUnitMeterReg().getNumDigitsO();
                        digitValidatorBuilder.append(preDecCount);
                    }

                    digitValidatorBuilder.append(',');

                    // Get post - decimal num digits
                    if (registerCustomTo.getWoUnitMeterReg().getNumDecimalsV() != null
                            && registerCustomTo.getWoUnitMeterReg().getNumDecimalsV().longValue()
                            == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) {
                        // Get delta value
                        postDecCount = registerCustomTo.getWoUnitMeterReg().getNumDecimalsV();
                        digitValidatorBuilder.append(postDecCount);

                    } else {
                        // Get original value
                        postDecCount = registerCustomTo.getWoUnitMeterReg().getNumDecimalsO();
                        digitValidatorBuilder.append(postDecCount);
                    }

                    digitValidatorBuilder.append(')');
                    decIndicationTv.setText(digitValidatorBuilder.toString());
                }

                // Register listener for clear button
                final ImageButton clearRegValueBtn = vw.findViewById(R.id.clearRegValueBtn);
                if (clearRegValueBtn != null) {
                    clearRegValueBtn.setOnClickListener(this);
                    clearRegValueBtn.setTag(vw);
                }

                // Tag the register objects with these views
                vw.setTag(registerCustomTo);

					/*
                     *  Whenever the user will enter reading values at the same time validation would be done
					 *  to check if the reading is insider/outside range values
					 */
                final EditText registerReading = vw.findViewById(R.id.registerReading);
                final EditText tariffReading = vw.findViewById(R.id.tariffReading);

                final RegisterTariffReadValidator validator
                        = new RegisterTariffReadValidator(registerCustomTo.getWoUnitMeterReg(), registerReading, tariffReading, vw);
                final RegisterTariffKeyListener keyListener
                        = new RegisterTariffKeyListener(registerCustomTo.getWoUnitMeterReg(), registerReading, tariffReading, vw);
                registerReading.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) preDecCount)});
                registerReading.setOnKeyListener(keyListener);
                registerReading.addTextChangedListener(validator);
                registerReading.setTag(registerCustomTo);
                tariffReading.addTextChangedListener(validator);
                tariffReading.setOnKeyListener(keyListener);
                tariffReading.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) postDecCount)});
                tariffReading.setTag(registerCustomTo);
                if (readingMap != null && !readingMap.isEmpty()) {
                    Iterator<WoUnitMeterRegTO> itrUnitRegTo = readingMap.keySet().iterator();
                    while (itrUnitRegTo.hasNext()) {
                        WoUnitMeterRegTO tempWoUnitMeterRegTO = itrUnitRegTo.next();
                        if (tempWoUnitMeterRegTO.getId().longValue() == registerCustomTo.getWoUnitMeterReg().getId().longValue()) {
                            Double readVal = readingMap.get(tempWoUnitMeterRegTO);
                            String[] vals = String.valueOf(readVal.doubleValue()).split("\\.");
                            if (vals != null && vals.length == 2) {
                                registerReading.setText(vals[0]);
                                tariffReading.setText(vals[1]);
                            }
                        }
                    }
                }
            }
            // Add to the list
            if (meterReadRegsViewLs == null) {
                meterReadRegsViewLs = new SoftReference<List<View>>(new ArrayList<View>());
            } else if (meterReadRegsViewLs != null && meterReadRegsViewLs.get() != null) {
                meterReadRegsViewLs.get().add(vw);
            }
            } catch (Exception e) {
                writeLog(TAG + " : getView()", e);
            }
            return vw;
        }

        @Override
        public void onClick(final View v) {
            if (v != null) {
                // Clear the fields
                final View rootPane = (View) v.getTag();
                // Get reference to register reading and tariff reading EditText views
                final TextView registerReading = rootPane.findViewById(R.id.registerReading);
                final TextView tariffReading = rootPane.findViewById(R.id.tariffReading);

                if (v.getId() == R.id.clearRegValueBtn && registerReading != null && tariffReading != null) {
                    registerReading.setText("");
                    tariffReading.setText("");
                    validDataProvided=false;
                }
                if (registerReading != null) {
                    registerReading.setText("");
                }

                if (tariffReading != null) {
                    tariffReading.setText("");
                }
            }
        }
    }

    private class RegisterTariffKeyListener implements View.OnKeyListener {

        private transient WoUnitMeterRegTO unitMeterRegTo = null;
        private SoftReference<TextView> regValueRef = null;
        private SoftReference<TextView> tariffValueRef = null;

        // Root view layout -- dont keep hard reference here, let it be garbage collected when necessary
        private SoftReference<View> rootViewRef = null;

        public RegisterTariffKeyListener(final WoUnitMeterRegTO unitMeterRegTo,
                                         final TextView regValue, final TextView tariffValue, final View rootView) {

            if (unitMeterRegTo != null) {
                this.unitMeterRegTo = unitMeterRegTo;
            }

            if (regValue != null) {
                this.regValueRef = new SoftReference<TextView>(regValue);
            }

            if (tariffValue != null) {
                this.tariffValueRef = new SoftReference<TextView>(tariffValue);
            }

            if (rootView != null) {
                this.rootViewRef = new SoftReference<View>(rootView);
            }
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            return false;
        }
    }

    /**
     * This validator will be called whenever the user enters/modifies readings in
     * register or tariff type values
     * <p>
     * <p>
     * This would check if the combined value (register value.tariff value) is within valid
     * ranges or not. If not it would highlight the background of both register and tariff values by red color
     * and will also show a validation text message "Reading outside range" just below the text boxes
     * </p>
     *
     * @author nirmchak
     */

    private class RegisterTariffReadValidator implements TextWatcher {

        private transient WoUnitMeterRegTO unitMeterRegTo = null;
        private SoftReference<EditText> regValueRef = null;
        private SoftReference<EditText> tariffValueRef = null;

        // Root view layout -- dont keep hard reference here, let it be garbage collected when necessary
        private SoftReference<View> rootViewRef = null;

        public RegisterTariffReadValidator(final WoUnitMeterRegTO unitMeterRegTo,
                                           final EditText regValue, final EditText tariffValue, final View rootView) {

            if (unitMeterRegTo != null) {
                this.unitMeterRegTo = unitMeterRegTo;
            }

            if (regValue != null) {
                this.regValueRef = new SoftReference<EditText>(regValue);
            }

            if (tariffValue != null) {
                this.tariffValueRef = new SoftReference<EditText>(tariffValue);
            }

            if (rootView != null) {
                this.rootViewRef = new SoftReference<View>(rootView);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void afterTextChanged(final Editable s) {
            try {
                // Validate the value when the key is released
                if (this.regValueRef != null && !TextUtils.isEmpty(regValueRef.get().getText())
                        && this.tariffValueRef != null && !TextUtils.isEmpty(this.tariffValueRef.get().getText())
                        && unitMeterRegTo != null
                        && this.rootViewRef != null && this.rootViewRef.get() != null) {

                    final View rootView = this.rootViewRef.get();
                    final View tv = rootView.findViewById(R.id.validationLayout);

                    final Double userValue = getUserProvidedValue(this.regValueRef.get(), this.tariffValueRef.get());
                    if (!Utils.equals(this.regValueRef.get().getText().toString().length(), registerRadingLengthLimit)) {
                        // Show validation error
                        tv.setVisibility(View.VISIBLE);
                        validDataProvided = false;
                    } else {
                        // Hide any validation error that may be displayed previously
                        tv.setVisibility(View.GONE);
                        validDataProvided = true;
                        readingMap.put(unitMeterRegTo, userValue);
                    }
                }
            } catch (Exception e) {
                writeLog(TAG + " : getUserProvidedValue()", e);
            }
        }
    }

    /**
     * Combine the provided values for register and tariff
     * separated by decimal point
     *
     * @param regValueTv
     * @param tariffValueTv
     * @return
     */

    private Double getUserProvidedValue(final TextView regValueTv,
                                        final TextView tariffValueTv) {
        final StringBuilder builder = new StringBuilder();

        String beforeDec = regValueTv.getText().toString().trim();
        String afterDec = tariffValueTv.getText().toString().trim();
        try {
            if (beforeDec == null
                    || (beforeDec != null && beforeDec.trim().equals(""))) {
                beforeDec = "0";
            }
            builder.append(beforeDec).append('.');

            if (afterDec == null
                    || (afterDec != null && afterDec.trim().equals(""))) {
                afterDec = "0";
            }

            builder.append(afterDec);
        } catch (Exception e) {
            writeLog(TAG + " : getUserProvidedValue()", e);
        }
        return Utils.safeToDouble(builder.toString(), Locale.getDefault());
    }

    @Override
    public void onClick(final View v) {
        if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
        }
    }
}
