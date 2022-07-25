package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler;
import com.skvader.rsp.ast_sep.common.to.ast.table.RegisterTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.TariffTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitMeterRegTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitMeterRegisterCustomTO;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class UnitMeterRegReadAdapter extends BaseAdapter implements View.OnClickListener {

    private transient List<WoUnitMeterRegisterCustomTO> validRegisters = null;
    Activity activity;
    ReadMeterIndiNewMeterFragment readMeterIndiNewMeterFragment;
    static String TAG = UnitMeterRegReadAdapter.class.getSimpleName();

    public UnitMeterRegReadAdapter(final List<WoUnitMeterRegisterCustomTO> validRegisters, ReadMeterIndiNewMeterFragment readMeterIndiNewMeterFragment) {
        this.validRegisters = validRegisters;
        activity=readMeterIndiNewMeterFragment.getActivity();
        this.readMeterIndiNewMeterFragment = readMeterIndiNewMeterFragment ;
    }

    @Override
    public int getCount() {
        int count = 0;
        try{
        if (this.validRegisters != null) {
            count = this.validRegisters.size();
        }
    } catch (NumberFormatException e) {
        writeLog(TAG + " : getCount()", e);
    }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object object = null;
        try{
        if (this.validRegisters != null && position < this.validRegisters.size()) {
            object = this.validRegisters.get(position);
        }
        } catch (NumberFormatException e) {
            writeLog(TAG + " : getItem()", e);
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
            vw = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.read_meter_reading_indiv, parent, false);
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

            Long preDecCount = 1L;
            Long postDecCount = 1L;

            final TextView decIndicationTv = vw.findViewById(R.id.decIndicationTv);
            if (decIndicationTv != null) {
                final StringBuilder digitValidatorBuilder = new StringBuilder();
                digitValidatorBuilder.append('(');
                // Get pre - decimal num digits
                if (registerCustomTo.getWoUnitMeterReg().getNumDigitsV() != null &&
                        registerCustomTo.getWoUnitMeterReg().getNumDigitsV().longValue() == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) {
                    // Get delta value
                    preDecCount = registerCustomTo.getWoUnitMeterReg().getNumDigitsD();
                } else {
                    // Get original value
                    preDecCount = registerCustomTo.getWoUnitMeterReg().getNumDigitsO();
                }
                if(preDecCount == null) {
                    digitValidatorBuilder.append("N/A");
                } else {
                    digitValidatorBuilder.append(preDecCount);
                }

                digitValidatorBuilder.append(',');

                // Get post - decimal num digits
                if (registerCustomTo.getWoUnitMeterReg().getNumDecimalsV() != null
                        && registerCustomTo.getWoUnitMeterReg().getNumDecimalsV().longValue()
                        == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) {
                    // Get delta value
                    postDecCount = registerCustomTo.getWoUnitMeterReg().getNumDecimalsV();
                } else {
                    // Get original value
                    postDecCount = registerCustomTo.getWoUnitMeterReg().getNumDecimalsO();
                }
                if(postDecCount == null) {
                    digitValidatorBuilder.append("N/A");
                } else {
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
            if(preDecCount != null) {
                registerReading.setFilters(new InputFilter[]{new InputFilter.LengthFilter(preDecCount.intValue())});
            }
            registerReading.setOnKeyListener(keyListener);
            registerReading.addTextChangedListener(validator);
            registerReading.setTag(registerCustomTo);
            tariffReading.addTextChangedListener(validator);
            tariffReading.setOnKeyListener(keyListener);
            if(postDecCount != null) {
                tariffReading.setFilters(new InputFilter[]{new InputFilter.LengthFilter(postDecCount.intValue())});
            }
            tariffReading.setTag(registerCustomTo);
            if (readMeterIndiNewMeterFragment.readingMap != null && !readMeterIndiNewMeterFragment.readingMap.isEmpty()) {
                try {
                    Double readVal = readMeterIndiNewMeterFragment.readingMap.get(registerCustomTo.getWoUnitMeterReg());
                    String[] vals = String.valueOf(readVal.doubleValue()).split("\\.");
                    if (vals != null && vals.length == 2) {
                        registerReading.setText(vals[0]);
                        tariffReading.setText(vals[1]);
                    }
                } catch (Exception e) {
                    SespLogHandler.writeLog(TAG,e);
                }
            }
        }
        // Add to the list
        if (readMeterIndiNewMeterFragment.meterReadRegsViewLs == null) {
            readMeterIndiNewMeterFragment.meterReadRegsViewLs = new SoftReference<List<View>>(new ArrayList<View>());
        } else if (readMeterIndiNewMeterFragment.meterReadRegsViewLs != null && readMeterIndiNewMeterFragment.meterReadRegsViewLs.get() != null) {
            readMeterIndiNewMeterFragment.meterReadRegsViewLs.get().add(vw);
        }
    } catch (NumberFormatException e) {
        writeLog(TAG + " : getView()", e);
    }
        return vw;
    }

    class RegisterTariffReadValidator implements TextWatcher {

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
            // Validate the value when the key is released
            if (this.regValueRef != null && !TextUtils.isEmpty(regValueRef.get().getText())
                    && this.tariffValueRef != null && !TextUtils.isEmpty(this.tariffValueRef.get().getText())
                    && unitMeterRegTo != null
                    && this.rootViewRef != null && this.rootViewRef.get() != null) {

                final View rootView = this.rootViewRef.get();
                final View tv = rootView.findViewById(R.id.validationLayout);

                final Double userValue = readMeterIndiNewMeterFragment.getUserProvidedValue(this.regValueRef.get(), this.tariffValueRef.get());
                Log.d(this.getClass().getSimpleName(), "USER PROVIDED VALUES : " + userValue);
                if (unitMeterRegTo.getMinValidReading() != null &&
                        unitMeterRegTo.getMaxValidReading() != null &&
                        !(userValue >= unitMeterRegTo.getMinValidReading()
                                && userValue <= unitMeterRegTo.getMaxValidReading())) {
                    // Show validation error
                    tv.setVisibility(View.VISIBLE);
                    readMeterIndiNewMeterFragment.validDataProvided = false;
                } else {
                    // Hide any validation error that may be displayed previously
                    tv.setVisibility(View.GONE);
                    readMeterIndiNewMeterFragment.validDataProvided = true;
                    readMeterIndiNewMeterFragment.readingMap.put(unitMeterRegTo, userValue);
                }
            }
        }
    }

    class RegisterTariffKeyListener implements View.OnKeyListener {

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
    @Override
    public void onClick(final View v) {
        try{
        if (v != null && v.getTag() != null) {
            // Clear the fields
            final View rootPane = (View) v.getTag();
            // Get reference to register reading and tariff reading EditText views
            final TextView registerReading = rootPane.findViewById(R.id.registerReading);
            final TextView tariffReading = rootPane.findViewById(R.id.tariffReading);


            if (registerReading != null) {
                registerReading.setText("");
            }

            if (tariffReading != null) {
                tariffReading.setText("");
            }
            if( v.getId()==R.id.clearRegValueBtn && registerReading!= null && tariffReading!=null){
                registerReading.setText("");
                tariffReading.setText("");
                readMeterIndiNewMeterFragment.validDataProvided = false;
            }
        }
        } catch (NumberFormatException e) {
            writeLog(TAG + " : onClick()", e);
        }

    }
}