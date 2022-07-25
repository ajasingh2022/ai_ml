package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.BarcodeDetector;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.BarcodeListener;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.MaterialLogisticsDialogBarcodeScanner;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.adapter.UnitEntryItem;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;

import java.util.ArrayList;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

class AddItemDialog extends Dialog {

    private EditText existingUnitModel;
    private UnitEntryItem unitEntryItem;

    interface CommunicateFragment{
        void updateUI();
    }

    private static final int SCAN_REQUEST = 1;
    String TAG = this.getClass().getSimpleName();
    AutoCompleteTextView existingUnit;
    EditText newUnit;
    Button addAndClose;
    Button addMore;
    RecyclerView.Adapter adapter;
    TextView errorMessage;
    ArrayList unitEntryItems;
    ArrayMap existingIdentifierUnitMapUpdated;
    Context mContext;
    CommunicateFragment communicateFragment;
     AddItemDialog(@NonNull Activity context, RecyclerView.Adapter unitEntryAdapter,
                   ArrayList<UnitEntryItem> unitEntryItems, ArrayMap<String, WoUnitCustomTO> existingIdentifierUnitMapUpdated, UnitEntryItem unitEntryItem) {
        super(context);
        adapter = unitEntryAdapter;
        this.unitEntryItems = unitEntryItems;
        this.existingIdentifierUnitMapUpdated = existingIdentifierUnitMapUpdated;
        mContext = context;
        this.unitEntryItem = unitEntryItem;
    }

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         setContentView(R.layout.unit_entry_dialog_view);

         existingUnit = findViewById(R.id.existingUnit);
         existingUnitModel = findViewById(R.id.unitModel);
         newUnit = findViewById(R.id.newUnit);

         if (unitEntryItem !=null){
             existingUnit.setText(unitEntryItem.getExistingUnit());
             existingUnitModel.setText(unitEntryItem.getModel());
             newUnit.setText(unitEntryItem.getNewUnit());
             unitEntryItems.remove(unitEntryItem);
         }


         ((EditText)existingUnit).addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(Editable s) {
                 String userValue = existingUnit.getText().toString();
                 if(existingIdentifierUnitMapUpdated.containsKey(userValue)){
                     WoUnitCustomTO woUnitCustomTO =
                             (WoUnitCustomTO) existingIdentifierUnitMapUpdated.get(userValue);
                     existingUnitModel.setText(woUnitCustomTO.getUnitModel());

                 }

             }
         });

         setAutoFillValues();

         errorMessage = findViewById(R.id.errorMessage);
         errorMessage.setVisibility(View.INVISIBLE);

         addAndClose = findViewById(R.id.addAndClose);
         addAndClose.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(validInput()){
                     updateUIAndData();
                     AddItemDialog.this.dismiss();
                 }
                 else {
                     AddItemDialog.this
                             .getWindow()
                             .getDecorView()
                             .animate()
                             .translationX(16f)
                             .setInterpolator(new CycleInterpolator(7f));

                 }
             }
         });
         addMore = findViewById(R.id.addMore);
         addMore.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (validInput()){
                     updateUIAndData();
                 }
                 else {
                     AddItemDialog.this
                             .getWindow()
                             .getDecorView()
                             .animate()
                             .translationX(16f)
                             .setInterpolator(new CycleInterpolator(7f));
                 }
             }
         });

         ImageButton scanExisting = findViewById(R.id.scanExisting);
         scanExisting.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 scanButtonClicked(existingUnit);
             }
         });
         ImageButton scanNew = findViewById(R.id.scanNew);
         scanNew.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 scanButtonClicked(newUnit);
             }
         });

     }

    private void setAutoFillValues() {
        String[] autoItems =
                (String[]) existingIdentifierUnitMapUpdated.keySet().toArray(new String[existingIdentifierUnitMapUpdated.keySet().toArray().length]);;

        existingUnit.setAdapter(new ArrayAdapter<String>(mContext,
                R.layout.single_choice_text_view,autoItems));
    }

    public void scanButtonClicked(View view) {
        try{
            BarcodeListener.barcodeListener = new BarcodeDetector() {
                @Override
                public void scannedCodeItem(String scannedCode) {
                    EditText identifierEditText = (EditText) view;
                    identifierEditText.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(scannedCode, true));
                }
            };
            Intent barCodeScanner = new Intent(mContext,
                    MaterialLogisticsDialogBarcodeScanner.class);
            ((Activity)mContext).startActivityForResult(barCodeScanner, SCAN_REQUEST);
        } catch (Exception e) {
            writeLog(TAG + " : scanButtonClicked()", e);
        }
    }

    private void updateUIAndData() {

         unitEntryItem = new UnitEntryItem();
         unitEntryItem.setExistingUnit(existingUnit.getText().toString());
         unitEntryItem.setNewUnit(newUnit.getText().toString());
         unitEntryItem.setModel(
                 ((WoUnitCustomTO)existingIdentifierUnitMapUpdated.get(unitEntryItem.getExistingUnit())).getUnitModel());
         unitEntryItem.setVerificationStatus(UnitEntryItem.VerificationStatus.NOT_DONE);

         existingIdentifierUnitMapUpdated.remove(unitEntryItem.getExistingUnit());
         unitEntryItems.add(unitEntryItem);
         adapter.notifyDataSetChanged();

         existingUnit.setText("");
         newUnit.setText("");
         setAutoFillValues();
         errorMessage.setVisibility(View.INVISIBLE);
         communicateFragment.updateUI();

    }

    private boolean validInput() {

         if (existingIdentifierUnitMapUpdated.containsKey(existingUnit.getText().toString())){
             if (!newUnit.getText().toString().trim().equals( "")){
                 return true;
             }
         }

         errorMessage.setVisibility(View.VISIBLE);
         return false;
    }


}
