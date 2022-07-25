package com.capgemini.sesp.ast.android.ui.activity.material_logistics.register_new_broken_device;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitTTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class RemoveNewBrokenDeviceDialog extends Dialog {
    private static final String TAG = RemoveNewBrokenDeviceDialog.class.getSimpleName();
    private RegisterNewBrokenDeviceActivity activityList;
    private Context context;
    private static final int SCAN_REQUEST = 1;
    Spinner unitTypeSpinner = null;
    Spinner unitModelSpinner = null;
    UnitTypeItemSelectionListener unitTypeItemSelectionListener = null;

    public RemoveNewBrokenDeviceDialog(Context context, RegisterNewBrokenDeviceActivity registerNewBrokenDeviceActivity) {
        super(context);
        try {
            this.context = context;
            activityList = registerNewBrokenDeviceActivity;

            setContentView(R.layout.dialog_add_remove_id);
            TextView addRemoveTxtView = (TextView)findViewById(R.id.add_RemoveID);
            addRemoveTxtView.setText(R.string.remove);
         //   setTitle(R.string.remove);

            unitTypeSpinner = findViewById(R.id.unitTypeSpinner);
            unitModelSpinner = findViewById(R.id.unitModelSpinner);

            EditText identifierEditText = findViewById(R.id.dialogAddRemoveIdentifierEditText);
            identifierEditText.setHint(ObjectCache.getTypeName(UnitIdentifierTTO.class, MaterialLogisticsSettingsActivity.getIdUnitIdentifierT()));

            unitTypeItemSelectionListener = new UnitTypeItemSelectionListener();
            fillunitTypeSpinner(null);

            /** Listeners for button clicks */
            Button removeButton = findViewById(R.id.dialogAddRemoveButton);
            removeButton.setText(context.getString(R.string.remove_and_close));
            removeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    removeButtonClicked(null);
                }
            });
            Button nextButton = findViewById(R.id.dialogNextButton);
            nextButton.setText(context.getString(R.string.remove_more));
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
            writeLog(TAG + ": RemoveNewBrokenDeviceDialog()", e);
        }
    }

    public void scanButtonClicked(View view) {
        try {
            BarcodeListener.barcodeListener = new BarcodeDetector() {
                @Override
                public void scannedCodeItem(String scannedCode) {
                    EditText identifierEditText = findViewById(R.id.dialogAddRemoveIdentifierEditText);
                    identifierEditText.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(scannedCode, true));
                }
            };
            Intent barCodeScanner = new Intent(activityList, MaterialLogisticsDialogBarcodeScanner.class);
            activityList.startActivityForResult(barCodeScanner, SCAN_REQUEST);
        } catch (Exception e) {
            writeLog(TAG + ": scanButtonClicked()", e);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpunitTypeSpinnerListener();
        setUpunitModelSpinnerListener();
    }

    public void removeButtonClicked(View view) {
        try{
        if (activityList.getItemsSize() > 0) {
            BrokenNewDeviceItem itemToRemove = getInput();
            if (activityList.hasItem(itemToRemove) && Utils.isEmpty(itemToRemove.getIdUnitT()) && activityList.getSelectedItemCount(itemToRemove) > 1) {
                Toast.makeText(getContext(), R.string.multiple_device_in_list, Toast.LENGTH_SHORT).show();
            } else if (itemToRemove != null) {
                BrokenNewDeviceItem unitTypeCheckItem = activityList.checkExistsUnitItem(itemToRemove);
                if (Utils.isNotEmpty(unitTypeCheckItem)) {
                    if (activityList.removeItem(unitTypeCheckItem))
                        dismiss();
                } else {
                    Toast.makeText(activityList, activityList.getString(R.string.error_identifier_not_found_in_list), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(context, context.getString(R.string.error_list_empty), Toast.LENGTH_SHORT).show();
        }
        } catch (Exception e) {
            writeLog(TAG + ": removeButtonClicked()", e);
        }
    }

    public void nextButtonClicked(View view) {
        try{
        if (activityList.getItemsSize() > 0) {
            BrokenNewDeviceItem brokenNewDeviceItem = getInput();
            if (brokenNewDeviceItem != null) {
                if (activityList.removeItem(brokenNewDeviceItem)) {
                    EditText identifierEditText = findViewById(R.id.dialogAddRemoveIdentifierEditText);
                    identifierEditText.setText("");
                    if (activityList.getItemsSize() == 0) {
                        dismiss();
                    }
                }
            }
        } else {
            Toast.makeText(context, context.getString(R.string.error_list_empty), Toast.LENGTH_SHORT).show();
        }
        } catch (Exception e) {
            writeLog(TAG + ": nextButtonClicked()", e);
        }
    }

    private BrokenNewDeviceItem getInput() {

        EditText identifierEditText = findViewById(R.id.dialogAddRemoveIdentifierEditText);
        String identifier = identifierEditText.getText().toString();
        final SharedPreferences settingsSP = AndroidUtilsAstSep.getSharedPreferences(ConstantsAstSep.SharedPreferenceType.USER_SETTINGS);
        Long idUnitIdentifierT = settingsSP.getLong(ConstantsAstSep.SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER, -1);
        if (identifier.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.error_identifier_is_missing), Toast.LENGTH_SHORT).show();
            return null;
        }
        UnitTTO unitTTO = null;
        UnitModelTO unitModelTO = null;
        try {
            DisplayItem displayItemUnitType = (DisplayItem) unitTypeSpinner.getSelectedItem();
            if (Utils.isNotEmpty(displayItemUnitType) && displayItemUnitType.getUserObject().getId() != -1L) {
                if (displayItemUnitType.equals(Spinner.INVALID_ROW_ID)) {
                    return null;
                }
                unitTTO = (UnitTTO) displayItemUnitType.getUserObject();
            }

            DisplayItem displayItemUnitModel = (DisplayItem) unitModelSpinner.getSelectedItem();
            if (Utils.isNotEmpty(displayItemUnitModel) && displayItemUnitModel.getUserObject().getId() != -1L) {
                if (displayItemUnitModel.equals(Spinner.INVALID_ROW_ID)) {
                    return null;
                }
                unitModelTO = (UnitModelTO) displayItemUnitModel.getUserObject();
            }
        } catch (Exception e) {
            writeLog(TAG + ": onItemSelected()", e);
        }
        if (Utils.isNotEmpty(unitTTO)) {
            return new BrokenNewDeviceItem(identifier, null, idUnitIdentifierT, unitTTO.getId(), Utils.isNotEmpty(unitModelTO) ? unitModelTO.getId() : null);
        } else {
            return new BrokenNewDeviceItem(identifier, null, idUnitIdentifierT);
        }

    }


    protected void fillunitTypeSpinner(UnitModelCustomTO selectedUnitModelTO) {
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
        unitTypeSpinner.setAdapter(spinnerArrayAdapter);

    }

    protected UnitTTO getUnitTypeInput() {
        UnitTTO unitTypeTO = null;
        try {

            DisplayItem displayItem = (DisplayItem) unitTypeSpinner.getSelectedItem();
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
            DisplayItem displayItem = (DisplayItem) unitModelSpinner.getSelectedItem();
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
        try {
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

            unitModelSpinner.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            writeLog(TAG + ": fillunitModelSpinner()", e);
        }
    }

    /**
     * Set up UI listeners
     */
    protected void setUpunitTypeSpinnerListener() {
        try {
            if (unitTypeSpinner != null && unitTypeItemSelectionListener != null) {
                unitTypeSpinner.setOnItemSelectedListener(unitTypeItemSelectionListener);
            }
        } catch (Exception e) {
            writeLog(TAG + ": setUpunitTypeSpinnerListener()", e);
        }
    }

    private class UnitTypeItemSelectionListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                UnitTTO selectedUnitTypeTO = getUnitTypeInput();
                fillunitModelSpinner(selectedUnitTypeTO);
            } catch (Exception e) {
                writeLog(TAG + ": onItemSelected()", e);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    protected void setUpunitModelSpinnerListener() {
        try {
            if (unitModelSpinner != null && (unitTypeSpinner.getSelectedItemId() == -1 || Utils.isEmpty(unitTypeSpinner.getSelectedItem()))) {
                unitModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            writeLog(TAG + ": setUpunitModelSpinnerListener()", e);
        }
    }

    protected void setRelatedUnitTypeonUnitModel(UnitModelCustomTO selectedUnitModelTO) {
        try {
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
            writeLog(TAG + ": setRelatedUnitTypeonUnitModel()", e);
        }

    }
}
