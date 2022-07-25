package com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.StringUtil;
import com.capgemini.sesp.ast.android.module.util.to.BasicDataTO;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.scanner.BarcodeScanner;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by samdasgu on 11/18/2017.
 */
public class MaterialControlSelectPalletActivity extends AppCompatActivity {

    private UnitInformationTO unitInformationTO = null;
    private EditText palletCode;
    private boolean isSelected = false;
    private static final int SCAN_REQUEST = 3;
    private String strUnitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.dialog_material_control_select_pallet);
        this.palletCode = findViewById(R.id.editPallet);
        this.unitInformationTO = (UnitInformationTO) getIntent().getExtras().get("UnitInformationTO");
        this.strUnitId = getIntent().getExtras().getString("UnitId");
        Button scanPalletCodeButton = findViewById(R.id.scanPalletCodeButton);
        Button savePalletButton = findViewById(R.id.savePalletButton);

        scanPalletCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanPalletButtonClicked();
            }
        });

        savePalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClicked();
            }
        });
        LinearLayout palletSpinnerLayout = findViewById(R.id.selectPalletspinnerScanLayout);
        if (ObjectCache.materialControlPalletList != null && ObjectCache.materialControlPalletList.size() > 0) {
            palletSpinnerLayout.setVisibility(View.VISIBLE);
            fillScannedPalletSpinner();
        } else {
            palletSpinnerLayout.setVisibility(View.GONE);
        }
        PackageManager pm = getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            ImageButton scanIdentifierButton = findViewById(R.id.scanIdentifierButton);
            scanIdentifierButton.setVisibility(View.INVISIBLE);
        }
        } catch (Exception e) {
            writeLog( "MaterialControlSelectPalletActivity: onCreate()", e);
        }
    }

    public void saveButtonClicked() {
        try{
        if((!isSelected && !StringUtil.isNotEmpty(palletCode.getText().toString()))) {
            Toast.makeText(this, getString(R.string.either_select_or_enter_pallect_code), Toast.LENGTH_LONG).show();
        } else {
            if(StringUtil.isNotEmpty(palletCode.getText().toString()) && !ObjectCache.getMaterialControlPallets().contains(palletCode.getText().toString())) {
                ObjectCache.materialControlPalletList.add(palletCode.getText().toString());
            }
            goToRegisterUnitActivity();
        }
        } catch (Exception e) {
            writeLog( "MaterialControlSelectPalletActivity: saveButtonClicked()", e);
        }
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("Material Control");
    }*/

    private void goToRegisterUnitActivity() {
        try{
        Spinner palletSpinner = findViewById(R.id.scannedSpinner);
        //SESPPreferenceUtil.savePreference(StockhandlingConstants.MATERIAL_CONTROL_PALLET_CODE, palletCode.getText().toString());
        Intent callingIntent = new Intent(this, ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.MATERIAL_CONTROL_REGISTER_UNIT_ACTIVITY));
        callingIntent.putExtra("UnitInformationTO", unitInformationTO);
        callingIntent.putExtra("UnitId", strUnitId);
        callingIntent.putExtra("PalletCode", StringUtil.isNotEmpty(palletCode.getText().toString()) ? palletCode.getText().toString() : palletSpinner.getSelectedItem().toString());
        callingIntent.putExtra("SelectedButton", (BasicDataTO) getIntent().getExtras().get("SelectedButton"));
        callingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(callingIntent);
        } catch (Exception e) {
            writeLog( "MaterialControlSelectPalletActivity: goToRegisterUnitActivity()", e);
        }
    }

    public void scanPalletButtonClicked() {
        try{
        Intent barCodeScanner = new Intent(this, BarcodeScanner.class);
        startActivityForResult(barCodeScanner, SCAN_REQUEST);
        } catch (Exception e) {
            writeLog( "MaterialControlSelectPalletActivity: scanPalletButtonClicked()", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            palletCode.setText(data.getStringExtra("barcode_result"));
        }
    }

    private void fillScannedPalletSpinner() {
        try{
        ArrayAdapter<String> palletadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ObjectCache.materialControlPalletList);
        palletadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.scannedSpinner);
        spinner.setAdapter(palletadapter);
        ScannedPalletSpinnerListener actionSpinnerListener = new ScannedPalletSpinnerListener();
        spinner.setOnItemSelectedListener(actionSpinnerListener);
        } catch (Exception e) {
            writeLog( "MaterialControlSelectPalletActivity: fillScannedPalletSpinner()", e);
        }
    }

    private class ScannedPalletSpinnerListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
            isSelected = true;
            unitInformationTO.setPalletCode(ObjectCache.materialControlPalletList.get(position));
            palletCode.setText(ObjectCache.materialControlPalletList.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // Do nothing?
        }
    }
}
