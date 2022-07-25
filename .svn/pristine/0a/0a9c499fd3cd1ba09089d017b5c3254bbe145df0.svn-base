package com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SespSpinnerAdapter;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.UnitItem;
import com.capgemini.sesp.ast.android.ui.activity.scanner.BarcodeScanner;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitTTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class MaterialControlInputActivity extends AppCompatActivity {
    private static final String TAG = MaterialControlInputActivity.class.getSimpleName();
    EditText searchUnitId;
    private static final int SCAN_REQUEST = 2;
    Spinner unitTypeCustomSpinner = null;
    Spinner unitModelCustomSpinner = null;
    UnitTypeItemSelectionListener unitTypeItemSelectionListener = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_control_input);
     //   getSupportActionBar().setTitle(R.string.material_control);
            /*
             * Setting up custom action bar view
             */
            getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(false);

            final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
            ImageButton help_btn = vw.findViewById(R.id.menu_help);
            help_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new HelpDialog(MaterialControlInputActivity.this, ConstantsAstSep.HelpDocumentConstant.PAGE_MATERIAL_CONTROL);
                    dialog.show();
                }
            });
            getSupportActionBar().setCustomView(vw);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            TextView txtTitleBar = findViewById(R.id.title_text);
            txtTitleBar.setText(R.string.material_control);
            // -- Customizing the action bar ends -----

        unitTypeCustomSpinner = findViewById(R.id.materialControlUnitTypeSpinner);
        unitModelCustomSpinner = findViewById(R.id.materialControlUnitModelSpinner);

        searchUnitId = findViewById(R.id.unitidEditText);
        searchUnitId.setHint(ObjectCache.getTypeName(UnitIdentifierTTO.class,
                SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER)));
        PackageManager pm = getPackageManager();

        Button scanButton = findViewById(R.id.scanIdentifierButton);
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                SESPPreferenceUtil.getPreferenceBoolean(SharedPreferenceKeys.ENABLE_BT_SCANNING)) {
            //FloatingActionButton scanButton = (FloatingActionButton) findViewById(R.id.scanIdentifierButton);
            scanButton.setVisibility(View.INVISIBLE);
        }

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scanIdentifierButtonClicked(v);
            }
        });
        } catch (Exception e) {
            writeLog(TAG + ": onCreate()", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{

        unitTypeItemSelectionListener = new UnitTypeItemSelectionListener();
        filldeviceInfounitTypeSpinner(null);
        setUpMaterialControlUnitTypeSpinnerListener();
        setMaterialControlUnitModelSpinnerListener();
        } catch (Exception e) {
            writeLog(TAG + ": onResume()", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            Intent goToIntent = new Intent(getApplicationContext(), ActivityFactory.getInstance().getActivityClass(ActivityConstants.MATERIAL_LOGISTICS_ACTIVITY));
            goToIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goToIntent);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    public void scanIdentifierButtonClicked(View view) {
        try{
        Intent barCodeScanner = new Intent(getApplicationContext(), BarcodeScanner.class);
        startActivityForResult(barCodeScanner, SCAN_REQUEST);
        } catch (Exception e) {
            writeLog(TAG + ": scanIdentifierButtonClicked()", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            searchUnitId = findViewById(R.id.unitidEditText);
            searchUnitId.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(data.getStringExtra("barcode_result"), true));
        }
    }

    public void searchButtonClicked(View view) {
        /* Validation before sending request to web service */
        String unitId = searchUnitId.getText().toString();
        UnitTTO unitTTO = null;
        UnitModelTO unitModelTO = null;

        if (unitId.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_identifier_is_missing), Toast.LENGTH_SHORT).show();
        } else {

            DisplayItem displayItemUnitType = (DisplayItem) unitTypeCustomSpinner.getSelectedItem();
            if (Utils.isNotEmpty(displayItemUnitType) && displayItemUnitType.getUserObject().getId() != -1L) {
                if (displayItemUnitType.equals(Spinner.INVALID_ROW_ID)) {
                    Toast.makeText(this, "Unit_Type_Missing", Toast.LENGTH_SHORT).show();
                }
                unitTTO = (UnitTTO) displayItemUnitType.getUserObject();
            }

            DisplayItem displayItemUnitModel = (DisplayItem) unitModelCustomSpinner.getSelectedItem();
            if (Utils.isNotEmpty(displayItemUnitModel) && displayItemUnitModel.getUserObject().getId() != -1L) {
                if (displayItemUnitModel.equals(Spinner.INVALID_ROW_ID)) {
                    Toast.makeText(this, "Unit_Model_Missing", Toast.LENGTH_SHORT).show();
                }
                unitModelTO = (UnitModelTO) displayItemUnitModel.getUserObject();
            }

        }


        if (TextUtils.isEmpty(unitId)) {
            Toast.makeText(this, R.string.error_missing_UnitId, Toast.LENGTH_SHORT).show();
            return;
        } else {

            if (Utils.isNotEmpty(unitTTO)) {
                UnitItem unitItem = new UnitItem(unitId, Utils.isNotEmpty(unitTTO) ? unitTTO.getId() : null, Utils.isNotEmpty(unitModelTO) ? unitModelTO.getId() : null, SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER));
                new MaterialControlInfoFetchThread(this, unitId, unitItem).start();
            } else {
                new MaterialControlInfoFetchThread(this, unitId).start();
            }


        }
    }


    ///////////////////////////

    private class UnitTypeItemSelectionListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            UnitTTO selectedUnitTypeTO = getUnitTypeInput();
            filldeviceInfounitModelSpinner(selectedUnitTypeTO);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    protected void setMaterialControlUnitModelSpinnerListener() {
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
    }

    protected void setRelatedUnitTypeonUnitModel(UnitModelCustomTO selectedUnitModelTO) {
        try{
        UnitTTO unitSelected = null;
        List<UnitTTO> unitTypeCustomTTOs = ObjectCache.getAllTypes(UnitTTO.class);
        for (UnitTTO unitType : unitTypeCustomTTOs) {
            if (unitType.getId() == selectedUnitModelTO.getIdUnitT()) {
                unitSelected = unitType;
            }
        }
        unitTypeCustomSpinner.setOnItemSelectedListener(null);
        Log.d("AddByIdDialog", "Item position :: " + ((SespSpinnerAdapter) unitTypeCustomSpinner.getAdapter()).getItemPosition(unitSelected.getId()));
        unitTypeCustomSpinner.setSelection(((SespSpinnerAdapter) unitTypeCustomSpinner.getAdapter()).getItemPosition(unitSelected.getId()), true);
        unitTypeCustomSpinner.setOnItemSelectedListener(unitTypeItemSelectionListener);
        } catch (Exception e) {
            writeLog(TAG + ": setRelatedUnitTypeonUnitModel()", e);
        }
    }

    protected void setUpMaterialControlUnitTypeSpinnerListener() {
        if (unitTypeCustomSpinner != null && unitTypeItemSelectionListener != null) {
            unitTypeCustomSpinner.setOnItemSelectedListener(unitTypeItemSelectionListener);
        }
    }


    protected UnitModelCustomTO getUnitModelInput() {
        UnitModelCustomTO unitModelTO = null;
        try {
            DisplayItem displayItem = (DisplayItem) unitModelCustomSpinner.getSelectedItem();
            if (displayItem.equals(Spinner.INVALID_ROW_ID)) {
                Toast.makeText(this, getString(R.string.error_reason_is_missing), Toast.LENGTH_SHORT).show();
                return null;
            }
            unitModelTO = (UnitModelCustomTO) displayItem.getUserObject();
        } catch (Exception e) {
            writeLog(TAG + ": getUnitModelInput()", e);
        }
        return unitModelTO;
    }

    protected UnitTTO getUnitTypeInput() {
        UnitTTO unitTypeTO = null;
        try {
            DisplayItem displayItem = (DisplayItem) unitTypeCustomSpinner.getSelectedItem();
            if (displayItem.equals(Spinner.INVALID_ROW_ID)) {
                Toast.makeText(this, getString(R.string.error_reason_is_missing), Toast.LENGTH_SHORT).show();
                return null;
            }
            unitTypeTO = (UnitTTO) displayItem.getUserObject();
        } catch (Exception e) {
            writeLog(TAG + ": getUnitTypeInput()", e);
        }
        return unitTypeTO;
    }


    protected void filldeviceInfounitModelSpinner(UnitTTO unitTypeTo) {
        List<DisplayItem<UnitModelCustomTO>> spinnerList = new ArrayList<DisplayItem<UnitModelCustomTO>>();
        List<UnitModelCustomTO> unitModelCustomTTOs = ObjectCache.getAllIdObjects(UnitModelCustomTO.class);
        if (Utils.isEmpty(unitTypeTo) || unitTypeTo.getId() == -1) {
            spinnerList = DisplayItem.getDisplayItems(unitModelCustomTTOs, null);
            DisplayItem<UnitModelCustomTO> blankObject = null;
            try {
                blankObject = AndroidUtilsAstSep.newInstance(spinnerList.get(0).getClass(),
                        spinnerList.get(0).getUserObject().getClass().newInstance());
                blankObject.setName("-" + getString(R.string.select) + "-");
                blankObject.getUserObject().setId(-1L);
                blankObject.getUserObject().setName("");
                spinnerList.add(0, blankObject);
            } catch (Exception e) {
                writeLog(TAG + ": filldeviceInfounitModelSpinner()", e);
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
                blankObject.setName("-" + getString(R.string.select) + "-");
                blankObject.getUserObject().setId(-1L);
                blankObject.getUserObject().setName("");
                spinnerList.add(0, blankObject);
            } catch (Exception e) {
                writeLog(TAG + ": filldeviceInfounitModelSpinner()", e);
            }
        }

        SespSpinnerAdapter spinnerArrayAdapter = new SespSpinnerAdapter(this, android.R.layout.simple_spinner_item, spinnerList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitModelCustomSpinner.setAdapter(spinnerArrayAdapter);
    }

    protected void filldeviceInfounitTypeSpinner(UnitModelCustomTO selectedUnitModelTO) {
        List<DisplayItem<UnitTTO>> spinnerList = null;
        List<UnitTTO> unitTypeCustomTTOs = ObjectCache.getAllTypes(UnitTTO.class);
        DisplayItem<UnitTTO> blankObject = null;
        if (Utils.isEmpty(selectedUnitModelTO) || selectedUnitModelTO.getId() == -1) {
            spinnerList = DisplayItem.getDisplayItems(unitTypeCustomTTOs, null);
            try {
                blankObject = AndroidUtilsAstSep.newInstance(spinnerList.get(0).getClass(),
                        spinnerList.get(0).getUserObject().getClass().newInstance());
                blankObject.setName("-" + getString(R.string.select) + "-");
                blankObject.getUserObject().setId(-1L);
                blankObject.getUserObject().setName("");
                spinnerList.add(0, blankObject);
            } catch (Exception e) {
                writeLog(TAG + ": filldeviceInfounitTypeSpinner()", e);
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
                blankObject.setName("-" + getString(R.string.select) + "-");
                blankObject.getUserObject().setId(-1L);
                blankObject.getUserObject().setName("");
                spinnerList.add(0, blankObject);
            } catch (Exception e) {
                writeLog(TAG + ": filldeviceInfounitTypeSpinner()", e);
            }

        }

        SespSpinnerAdapter spinnerArrayAdapter = new SespSpinnerAdapter(this, android.R.layout.simple_spinner_item, spinnerList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitTypeCustomSpinner.setAdapter(spinnerArrayAdapter);

    }

}
