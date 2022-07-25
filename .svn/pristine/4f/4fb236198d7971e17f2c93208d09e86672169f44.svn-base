package com.capgemini.sesp.ast.android.ui.activity.material_logistics.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.others.AddrRemoveUnitInterface;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitTTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class AddBulkDialog<T extends AppCompatActivity & AddrRemoveUnitInterface> extends Dialog {
    private T activityList;
    private static final String BARCODE_PREFIX = "8004";
    private static final String TAG = AddBulkDialog.class.getSimpleName();
    private RadioButton addingType;
    RadioGroup addingTypeGroup;

    public AddBulkDialog(final T activityList, Long idDomain, boolean showType) {

        super(activityList);
        this.activityList = activityList;
        try {

            setContentView(R.layout.dialog_add_bulk);
            setTitle(R.string.add_bulk_unit);

            final TextView addingTypeTextView = findViewById(R.id.dialogAddBulkAddingTypeTextView);
            addingTypeGroup = findViewById(R.id.radioGroupAddingType);
            if (!showType) {
                addingTypeGroup.setVisibility(View.GONE);
                addingTypeTextView.setVisibility(View.GONE);
            }

            final Spinner unitSpinner = findViewById(R.id.dialogAddBulkBulkUnitSpinner);
            List<UnitModelCustomTO> bulkDevices = new ArrayList<UnitModelCustomTO>();
            for (UnitModelCustomTO unitModelCustomTO : ObjectCache.getAllIdObjects(UnitModelCustomTO.class)) {
                UnitTTO unitTTO = ObjectCache.getType(UnitTTO.class, unitModelCustomTO.getIdUnitT());
                if (unitTTO.getBulkUnit().equals(AndroidUtilsAstSep.CONSTANTS().SYSTEM_BOOLEAN_T__TRUE)) {
                    bulkDevices.add(unitModelCustomTO);
                }
            }

            final List<DisplayItem<UnitModelCustomTO>> unitModelDisplayItems = DisplayItem.getDisplayItems(bulkDevices, idDomain);

            final UnitModelCustomAdapter dataAdapter = new UnitModelCustomAdapter(activityList, android.R.layout.simple_spinner_item, new ArrayList<DisplayItem<UnitModelCustomTO>>(unitModelDisplayItems));
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            unitSpinner.setAdapter(dataAdapter);


            /** Listeners for button clicks */
            Button addButton = findViewById(R.id.dialogAddRemoveButton);
            addButton.setText(activityList.getString(R.string.add_and_close));
            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addButtonClicked(null);
                }
            });
            Button nextButton = findViewById(R.id.dialogNextButton);
            nextButton.setText(activityList.getString(R.string.add_more));
            nextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    nextButtonClicked(null);
                }
            });

            final EditText scanBulkUnit = findViewById(R.id.dialogScanBulkUnitEditText);
            scanBulkUnit.addTextChangedListener(new TextWatcher() {

                List<DisplayItem<UnitModelCustomTO>> unitModelDisplayItemsDynamic;

                @Override
                public void beforeTextChanged(CharSequence scanBulkText, int start, int count, int after) {
                    unitModelDisplayItemsDynamic = dataAdapter.getItemList();
                }

                @Override
                public void onTextChanged(CharSequence scanBulkText, int start, int before, int count) {
                    if (scanBulkText != null
                            && scanBulkText.length() == 20
                            && BARCODE_PREFIX
                            .equalsIgnoreCase(String.valueOf(scanBulkText.subSequence(0, 4)))) {
                        scanBulkText.subSequence(4, scanBulkText.length());
                        scanBulkUnit.setText(scanBulkText);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String scanBulkText = s.toString();
                    unitModelDisplayItemsDynamic.clear();
                    for (DisplayItem<UnitModelCustomTO> unitModelCustomTODisplayItem : unitModelDisplayItems) {
                        if (unitModelCustomTODisplayItem.getName().contains(scanBulkText)) {
                            unitModelDisplayItemsDynamic.add(unitModelCustomTODisplayItem);
                        }
                    }
                    dataAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            writeLog(TAG + " :AddBulkDialog()", e);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addButtonClicked(View view) {
        try{
        UnitItem newItem = getInput();

        if (newItem != null) {
            activityList.addItem(newItem);
            dismiss();
        }
        } catch (Exception e) {
            writeLog(TAG + " :addButtonClicked()", e);
        }
    }

    private Boolean getAddingType() {
        if (addingTypeGroup != null
                && addingTypeGroup.getCheckedRadioButtonId() == R.id.radioButtonAddingTypeOverwrite) {
            return true;
        } else if (addingTypeGroup != null
                && addingTypeGroup.getCheckedRadioButtonId() == R.id.radioButtonAddingTypeAdd) {
            return false;
        } else {
            return null;
        }
    }

    public void nextButtonClicked(View view) {
        try{
        UnitItem newItem = getInput();

        if (newItem != null) {
            activityList.addItem(newItem);
            EditText addBulkAmountEditText = findViewById(R.id.dialogAddBulkAmountEditText);
            addBulkAmountEditText.setText("");
        }
        } catch (Exception e) {
            writeLog(TAG + " :nextButtonClicked()", e);
        }
    }

    private UnitItem getInput() {
        int amount = 0;
        Spinner unitModelSpinner = findViewById(R.id.dialogAddBulkBulkUnitSpinner);
        DisplayItem<UnitModelCustomTO> unitModelDisplayItem = (DisplayItem<UnitModelCustomTO>) unitModelSpinner.getSelectedItem();
        try {
            if (unitModelDisplayItem == null) {
                Toast.makeText(activityList, activityList.getString(R.string.error_unit_model_must_be_selected), Toast.LENGTH_SHORT).show();
                return null;

            }


            EditText amountEditText = findViewById(R.id.dialogAddBulkAmountEditText);
            String amountString = amountEditText.getText().toString();

            if (amountString.isEmpty() || amountString.equalsIgnoreCase("0")) {
                Toast.makeText(activityList, activityList.getString(R.string.error_amount_is_missing), Toast.LENGTH_SHORT).show();
                return null;
            }

            try {
                amount = Integer.parseInt(amountString);
            } catch (NumberFormatException nfe) {
                Toast.makeText(activityList, activityList.getString(R.string.error_invalid_number), Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (Exception e) {
            writeLog(TAG + " :AddBulkDialog()", e);
        }
        return new UnitItem(unitModelDisplayItem.getId(), unitModelDisplayItem.getUserObject(), amount, getAddingType());

    }


    private class UnitModelCustomAdapter extends ArrayAdapter<DisplayItem<UnitModelCustomTO>> {
        List<DisplayItem<UnitModelCustomTO>> itemList;

        public UnitModelCustomAdapter(Context context, int textViewResourceId, List<DisplayItem<UnitModelCustomTO>> itemList) {
            super(context, textViewResourceId, itemList);
            this.itemList = itemList;
        }

        public List<DisplayItem<UnitModelCustomTO>> getItemList() {
            return itemList;
        }
    }

}
