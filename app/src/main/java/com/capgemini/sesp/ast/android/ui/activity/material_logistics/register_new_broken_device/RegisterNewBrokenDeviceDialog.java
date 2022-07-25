package com.capgemini.sesp.ast.android.ui.activity.material_logistics.register_new_broken_device;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SespSpinnerAdapter;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.MaterialLogisticsSettingsActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.BarcodeDetector;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.BarcodeListener;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.MaterialLogisticsDialogBarcodeScanner;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitTTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class RegisterNewBrokenDeviceDialog extends Dialog {

    private static final String TAG = RegisterNewBrokenDeviceDialog.class.getSimpleName();
    protected RegisterNewBrokenDeviceActivity brokenList;
    protected Context mContext;
    Spinner unitTypeCustomSpinner = null;
    Spinner unitModelCustomSpinner = null;
    private static final int SCAN_REQUEST = 1;
    UnitTypeItemSelectionListener unitTypeItemSelectionListener = null;

    public RegisterNewBrokenDeviceDialog(Context context, RegisterNewBrokenDeviceActivity registerNewBrokenDeviceActivity) {
        super(context);
        try {
            mContext = context;
            brokenList = registerNewBrokenDeviceActivity;

            setContentView(R.layout.dialog_register_new_broken_device);
            setTitle(R.string.add);
            final SharedPreferences settingsSP = AndroidUtilsAstSep.getSharedPreferences(ConstantsAstSep.SharedPreferenceType.USER_SETTINGS);

            EditText identifierEditText = findViewById(R.id.dialogBrokenDeviceIdentifierEditText);
            identifierEditText.setHint(ObjectCache.getTypeName(UnitIdentifierTTO.class, settingsSP.getLong(ConstantsAstSep.SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER, -1)));
            identifierEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int characterCount) {
                    if (settingsSP.getBoolean(ConstantsAstSep.SharedPreferenceKeys.MATERIAL_LOGISTICS_IDENTFIER_LENGTH_USED, false)) {
                        Long identifierLengthSetting = settingsSP.getLong(ConstantsAstSep.SharedPreferenceKeys.MATERIAL_LOGISTICS_GIAI_LENGTH, -1);
                        if (identifierLengthSetting != null) { // Setting is on
                            if (characterCount == identifierLengthSetting.intValue()) {
                                nextButtonClicked(null);
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
            });


            fillReasonSpinner();
            unitTypeItemSelectionListener = new UnitTypeItemSelectionListener();
            fillunitTypeSpinner(null);
            /** Listeners for button clicks */
            Button addButton = findViewById(R.id.dialogAddRemoveButton);
            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addButtonClicked(null);
                }
            });
            Button nextButton = findViewById(R.id.dialogNextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    nextButtonClicked(null);
                }
            });
            Button scanButton = findViewById(R.id.dialogAddRemoveIdentifierScanButton);
            scanButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    scanButtonClicked(v);
                }
            });
        } catch (Exception e) {
            writeLog(TAG + ": RegisterNewBrokenDeviceDialog()", e);
        }
    }

    public void scanButtonClicked(View view) {
        try {
            BarcodeListener.barcodeListener = new BarcodeDetector() {
                @Override
                public void scannedCodeItem(String scannedCode) {
                    EditText identifierEditText = findViewById(R.id.dialogBrokenDeviceIdentifierEditText);
                    identifierEditText.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(scannedCode, true));
                }
            };
            Intent barCodeScanner = new Intent(getContext(), MaterialLogisticsDialogBarcodeScanner.class);
            brokenList.startActivityForResult(barCodeScanner, SCAN_REQUEST);
        } catch (Exception e) {
            writeLog(TAG + ": scanButtonClicked()", e);
        }
    }

    protected void fillReasonSpinner() {
        try {
            Spinner reasonSpinner = findViewById(R.id.dialogBrokenDeviceReasonSpinner);
            List<DisplayItem> spinnerList = new ArrayList<DisplayItem>();
            List<UnitStatusReasonTTO> unitStatusReasonTTOs = ObjectCache.getAllTypes(UnitStatusReasonTTO.class);
            for (UnitStatusReasonTTO unitStatusReasonTTO : unitStatusReasonTTOs) {
                spinnerList.add(new DisplayItem(unitStatusReasonTTO));
            }
            SespSpinnerAdapter spinnerArrayAdapter = new SespSpinnerAdapter(mContext, android.R.layout.simple_spinner_item, spinnerList);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            reasonSpinner.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            writeLog(TAG + ": fillReasonSpinner()", e);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpunitTypeSpinnerListener();
        setUpunitModelSpinnerListener();
    }

    public void addButtonClicked(View view) {
        BrokenNewDeviceItem newItem = getInput();
        if (newItem != null) {
            brokenList.addItem(newItem);
            dismiss();
        }
    }

    protected void fillunitTypeSpinner(UnitModelCustomTO selectedUnitModelTO) {
        unitTypeCustomSpinner = findViewById(R.id.registerBrokenDeviceUnitTypeSpinner);
        List<DisplayItem<UnitTTO>> spinnerList = null;
        List<UnitTTO> unitTypeCustomTTOs = ObjectCache.getAllTypes(UnitTTO.class);
        DisplayItem<UnitTTO> blankObject = null;
        if (Utils.isEmpty(selectedUnitModelTO) || selectedUnitModelTO.getId() == -1) {
            spinnerList = DisplayItem.getDisplayItems(unitTypeCustomTTOs, null);
            try {
                blankObject = AndroidUtilsAstSep.newInstance(spinnerList.get(0).getClass(),
                        spinnerList.get(0).getUserObject().getClass().newInstance());
                blankObject.setName("-" + getContext().getString(R.string.select) + "-");
                blankObject.getUserObject().setId(-1L);
                blankObject.getUserObject().setName("");
                spinnerList.add(0, blankObject);
            } catch (Exception e) {
                writeLog(TAG + ": fillunitTypeSpinner()", e);
            }
        } else {
            for (UnitTTO unitType : unitTypeCustomTTOs) {
                if (unitType.getId() == selectedUnitModelTO.getIdUnitT()) {
                    spinnerList.add(new DisplayItem(unitType));
                }
            }
            try {
                blankObject = AndroidUtilsAstSep.newInstance(spinnerList.get(0).getClass(),
                        spinnerList.get(0).getUserObject().getClass().newInstance());
                blankObject.setName("-" + getContext().getString(R.string.select) + "-");
                blankObject.getUserObject().setId(-1L);
                blankObject.getUserObject().setName("");
                spinnerList.add(0, blankObject);
            } catch (Exception e) {
                writeLog(TAG + ": fillunitTypeSpinner()", e);
            }

        }

        SespSpinnerAdapter spinnerArrayAdapter = new SespSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, spinnerList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitTypeCustomSpinner.setAdapter(spinnerArrayAdapter);

    }

    protected UnitTTO getUnitTypeInput() {
        UnitTTO unitTypeTO = null;
        try {
            DisplayItem displayItem = (DisplayItem) unitTypeCustomSpinner.getSelectedItem();
            if (displayItem.equals(Spinner.INVALID_ROW_ID)) {
                Toast.makeText(getContext(), getContext().getString(R.string.error_reason_is_missing), Toast.LENGTH_SHORT).show();
                return null;
            }
            unitTypeTO = (UnitTTO) displayItem.getUserObject();
        } catch (Exception e) {
            writeLog(TAG + ": getUnitTypeInput()", e);
        }
        return unitTypeTO;
    }

    protected UnitModelCustomTO getUnitModelInput() {
        UnitModelCustomTO unitModelTO = null;
        try {
            DisplayItem displayItem = (DisplayItem) unitModelCustomSpinner.getSelectedItem();
            if (displayItem.equals(Spinner.INVALID_ROW_ID)) {
                Toast.makeText(getContext(), getContext().getString(R.string.error_reason_is_missing), Toast.LENGTH_SHORT).show();
                return null;
            }
            unitModelTO = (UnitModelCustomTO) displayItem.getUserObject();
        } catch (Exception e) {
            writeLog(TAG + ": getUnitModelInput()", e);
        }
        return unitModelTO;
    }

    protected void fillunitModelSpinner(UnitTTO unitTypeTo) {
        unitModelCustomSpinner = findViewById(R.id.registerBrokenDeviceUnitModelSpinner);
        List<DisplayItem<UnitModelCustomTO>> spinnerList = new ArrayList<DisplayItem<UnitModelCustomTO>>();
        List<UnitModelCustomTO> unitModelCustomTTOs = ObjectCache.getAllIdObjects(UnitModelCustomTO.class);
        if (Utils.isEmpty(unitTypeTo) || unitTypeTo.getId() == -1) {
            spinnerList = DisplayItem.getDisplayItems(unitModelCustomTTOs, null);
            DisplayItem<UnitModelCustomTO> blankObject = null;
            try {
                blankObject = AndroidUtilsAstSep.newInstance(spinnerList.get(0).getClass(),
                        spinnerList.get(0).getUserObject().getClass().newInstance());
                blankObject.setName("-" + getContext().getString(R.string.select) + "-");
                blankObject.getUserObject().setId(-1L);
                blankObject.getUserObject().setName("");
                spinnerList.add(0, blankObject);
            } catch (Exception e) {
                writeLog(TAG + ": fillunitModelSpinner()", e);
            }
        } else {
            for (UnitModelCustomTO unitModel : unitModelCustomTTOs) {
                if (unitModel.getIdUnitT() == unitTypeTo.getId()) {
                    spinnerList.add(new DisplayItem(unitModel));
                }
            }
            DisplayItem<UnitModelCustomTO> blankObject = null;
            try {
                blankObject = AndroidUtilsAstSep.newInstance(spinnerList.get(0).getClass(),
                        spinnerList.get(0).getUserObject().getClass().newInstance());
                blankObject.setName("-" + getContext().getString(R.string.select) + "-");
                blankObject.getUserObject().setId(-1L);
                blankObject.getUserObject().setName("");
                spinnerList.add(0, blankObject);
            } catch (Exception e) {
                writeLog(TAG + ": fillunitModelSpinner()", e);
            }
        }

        SespSpinnerAdapter spinnerArrayAdapter = new SespSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, spinnerList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitModelCustomSpinner.setAdapter(spinnerArrayAdapter);
    }

    protected void setUpunitTypeSpinnerListener() {
        if (unitTypeCustomSpinner != null && unitTypeItemSelectionListener != null) {
            unitTypeCustomSpinner.setOnItemSelectedListener(unitTypeItemSelectionListener);
        }
    }

    private class UnitTypeItemSelectionListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            UnitTTO selectedUnitTypeTO = getUnitTypeInput();
            fillunitModelSpinner(selectedUnitTypeTO);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    protected void setUpunitModelSpinnerListener() {
        try {
            unitModelCustomSpinner = findViewById(R.id.registerBrokenDeviceUnitModelSpinner);
            unitTypeCustomSpinner = findViewById(R.id.registerBrokenDeviceUnitTypeSpinner);
            if (unitModelCustomSpinner != null && (unitTypeCustomSpinner.getSelectedItemId() == -1 || Utils.isEmpty(unitTypeCustomSpinner.getSelectedItem()))) {
                unitModelCustomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        UnitModelCustomTO selectedUnitModelTO = getUnitModelInput();

                        if (selectedUnitModelTO.getId() != -1) {
                            setRelatedUnitTypeonUnitModel(selectedUnitModelTO);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        } catch (Exception e) {
            writeLog(TAG + " : setUpunitModelSpinnerListener()", e);
        }
    }

    protected void setRelatedUnitTypeonUnitModel(UnitModelCustomTO selectedUnitModelTO) {
        try {
            Spinner unitTypeSpinner = findViewById(R.id.registerBrokenDeviceUnitTypeSpinner);
            UnitTTO unitSelected = null;
            List<UnitTTO> unitTypeCustomTTOs = ObjectCache.getAllTypes(UnitTTO.class);
            for (UnitTTO unitType : unitTypeCustomTTOs) {
                if (unitType.getId() == selectedUnitModelTO.getIdUnitT()) {
                    unitSelected = unitType;
                }
            }
            unitTypeSpinner.setOnItemSelectedListener(null);
            Log.d("AddByIdDialog", "Item position :: " + ((SespSpinnerAdapter) unitTypeSpinner.getAdapter()).getItemPosition(unitSelected.getId()));
            unitTypeSpinner.setSelection(((SespSpinnerAdapter) unitTypeSpinner.getAdapter()).getItemPosition(unitSelected.getId()), true);
            unitTypeSpinner.setOnItemSelectedListener(unitTypeItemSelectionListener);
        } catch (Exception e) {
            writeLog(TAG + " : setRelatedUnitTypeonUnitModel()", e);
        }
    }

    public void nextButtonClicked(View view) {
        try {
            BrokenNewDeviceItem newItem = getInput();
            if (newItem != null) {
                brokenList.addItem(newItem);
                EditText identifierEditText = findViewById(R.id.dialogBrokenDeviceIdentifierEditText);
                identifierEditText.setText("");
            }
        } catch (Exception e) {
            writeLog(TAG + " : nextButtonClicked()", e);
        }
    }

    private BrokenNewDeviceItem getInput() {
        UnitTTO unitTTO = null;
        UnitModelTO unitModelTO = null;
        SharedPreferences settingsSP = null;
        try {
            settingsSP = AndroidUtilsAstSep.getSharedPreferences(ConstantsAstSep.SharedPreferenceType.USER_SETTINGS);
        } catch (Exception e) {
            writeLog(TAG + " : BrokenNewDeviceItem()", e);
        }


        Spinner reasonSpinner = findViewById(R.id.dialogBrokenDeviceReasonSpinner);
        DisplayItem displayItem = (DisplayItem) reasonSpinner.getSelectedItem();
        if (displayItem.equals(Spinner.INVALID_ROW_ID)) {
            Toast.makeText(mContext, mContext.getString(R.string.error_reason_is_missing), Toast.LENGTH_SHORT).show();
            return null;
        }
        UnitStatusReasonTTO unitStatusReasonTTO = (UnitStatusReasonTTO) displayItem.getUserObject();

        EditText identifierEditText = findViewById(R.id.dialogBrokenDeviceIdentifierEditText);
        String identifierInput = identifierEditText.getText().toString().trim();
        Long identifierLengthSetting = MaterialLogisticsSettingsActivity.getLengthOfCurrentIdentifierWithNullIfOff();
        Long idUnitIdentifierT = settingsSP.getLong(ConstantsAstSep.SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER, -1);
        try {
            if (identifierInput.isEmpty()) {
                Toast.makeText(mContext, mContext.getString(R.string.error_identifier_is_missing), Toast.LENGTH_SHORT).show();
                return null;
            } else if (identifierLengthSetting != null && identifierInput.length() < identifierLengthSetting.intValue()) {
                Toast.makeText(mContext, mContext.getString(R.string.error_identifier_is_too_short), Toast.LENGTH_SHORT).show();
                return null;
            } else if (identifierLengthSetting != null && identifierInput.length() > identifierLengthSetting.intValue()) {
                Toast.makeText(mContext, mContext.getString(R.string.error_identifier_is_too_long), Toast.LENGTH_SHORT).show();
                return null;
            }
            Spinner unitTypeSpinner = findViewById(R.id.registerBrokenDeviceUnitTypeSpinner);
            DisplayItem displayItemUnitType = (DisplayItem) unitTypeSpinner.getSelectedItem();
            if (Utils.isNotEmpty(displayItemUnitType) && displayItemUnitType.getUserObject().getId() != -1L) {
                if (displayItemUnitType.equals(Spinner.INVALID_ROW_ID)) {
                    return null;
                }
                unitTTO = (UnitTTO) displayItemUnitType.getUserObject();
            }

            Spinner unitModelSpinner = findViewById(R.id.registerBrokenDeviceUnitModelSpinner);
            DisplayItem displayItemUnitModel = (DisplayItem) unitModelSpinner.getSelectedItem();
            if (Utils.isNotEmpty(displayItemUnitModel) && displayItemUnitModel.getUserObject().getId() != -1L) {
                if (displayItemUnitModel.equals(Spinner.INVALID_ROW_ID)) {
                    return null;
                }
                unitModelTO = (UnitModelTO) displayItemUnitModel.getUserObject();
            }
        } catch (Exception e) {
            writeLog(TAG + " : BrokenNewDeviceItem()", e);
        }
        if (Utils.isNotEmpty(unitTTO)) {
            return new BrokenNewDeviceItem(identifierInput, unitStatusReasonTTO, idUnitIdentifierT, unitTTO.getId(), Utils.isNotEmpty(unitModelTO) ? unitModelTO.getId() : null);
        } else {
            return new BrokenNewDeviceItem(identifierInput, unitStatusReasonTTO, idUnitIdentifierT);
        }

    }
}
