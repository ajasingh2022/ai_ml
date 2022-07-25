package com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.MaterialLogisticsUtils;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.to.BasicDataTO;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTTO;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.StockhandlingConstants;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class MaterialControlRegisterUnitActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    protected UnitInformationTO unitInformationTO = null;
    // protected ImageButton unitInfo;
    // protected ImageButton selectPallet;
    protected RadioGroup actionRadioGroup;
    protected String strUnitId;
    protected TextView unitId;
    protected TextView palletCodeTextView;
    protected BasicDataTO[] buttonDataList;
    protected List<UnitStatusTypeInformationType> unitStatusTypeInformationTypeList;
    protected ActivityFactory activityFactory;
    protected final transient String TAG = getClass().getSimpleName();
    protected String palletCode;
    protected BasicDataTO selectedButtonData;
    protected boolean mIsRestoredToTop;
    protected Button saveBtn;
    protected TextView headerText;

    static MenuItem selectPallet;
    protected LinearLayout reasonForActionLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_material_control_register_unit);
            getSupportActionBar().setTitle(R.string.material_control);
            headerText = findViewById(R.id.title_text);
            unitId = findViewById(R.id.unitIdTextview);
            palletCodeTextView = findViewById(R.id.palletCodeTextView);
            reasonForActionLayout = findViewById(R.id.reasonForActionLayout);
            palletCodeTextView.setVisibility(View.GONE);
            reasonForActionLayout.setVisibility(View.GONE);

            activityFactory = ActivityFactory.getInstance();

        } catch (Exception e) {
            writeLog("MaterialControlRegisterUnitActivity  : onCreate() ", e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_material_control, menu);
        selectPallet = menu.findItem(R.id.select_pallet_btn);
        selectPallet.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        try {
            if (itemId == R.id.select_pallet_btn) {
                selectPalletButtonClicked();
            } else if (itemId == R.id.info_btn) {
                infoButtonClicked();
            }

        } catch (Exception excep) {
            writeLog(TAG + " :onOptionsItemSelected()", excep);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if ((intent.getFlags() | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) > 0) {
            mIsRestoredToTop = true;
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (android.os.Build.VERSION.SDK_INT >= 19 && !isTaskRoot() && mIsRestoredToTop) {
            // 4.4.2 platform issues for FLAG_ACTIVITY_REORDER_TO_FRONT,
            // reordered activity back press will go to home unexpectly,
            // Workaround: move reordered activity current task to front when it's finished.
            ActivityManager tasksManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            tasksManager.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        unitInformationTO = (UnitInformationTO) getIntent().getExtras().get("UnitInformationTO");
        strUnitId = getIntent().getExtras().getString("UnitId");
        palletCode = getIntent().getExtras().getString("PalletCode");
        selectedButtonData = (BasicDataTO) getIntent().getExtras().get("SelectedButton");
        updateTitleBar();
        updateOptionList();
    }

    protected void updateOptionList() {
        try {
            unitStatusTypeInformationTypeList = MaterialLogisticsUtils.createUnitStatusTypeInformationTypes();
            int counter = 0;
            if (unitInformationTO.getUnitModelTO().getCode()
                    .equals(StockhandlingConstants.METER_EXTERNAL)) {

                buttonDataList = new BasicDataTO[2];
                for (UnitStatusTypeInformationType unitStatusTypeInformationType : unitStatusTypeInformationTypeList) {
                    if (unitStatusTypeInformationType.getUnitStatusType()
                            .getCode().equals(StockhandlingConstants.SPECIAL_WASTE)
                            || unitStatusTypeInformationType
                            .getUnitStatusType()
                            .getCode()
                            .equals(StockhandlingConstants.SUSPICIOUS_OLD_METER)) {
                        updateName(unitStatusTypeInformationType);
                        buttonDataList[counter] = unitStatusTypeInformationType
                                .getUnitStatusType();
                        counter++;
                    }
                }

            } else {
                buttonDataList = new BasicDataTO[3];
                for (UnitStatusTypeInformationType unitStatusTypeInformationType : unitStatusTypeInformationTypeList) {

                    if (unitStatusTypeInformationType.getUnitStatusType()
                            .getCode().equals(StockhandlingConstants.CONTROL)
                            || unitStatusTypeInformationType
                            .getUnitStatusType()
                            .getCode()
                            .equals(StockhandlingConstants.MOUNTABLE)
                            || unitStatusTypeInformationType
                            .getUnitStatusType().getCode()
                            .equals(StockhandlingConstants.WASTED)) {
                        updateName(unitStatusTypeInformationType);
                        buttonDataList[counter] = unitStatusTypeInformationType
                                .getUnitStatusType();
                        counter++;
                    }
                }
            }
            if (buttonDataList[0] == null || buttonDataList[1] == null) {
                String errorMessage = getString(R.string.manage_materiel_external_meter_not_supported);
                final View alertView
                        = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.prompt_user_response_layout, null);
                if (alertView != null) {
                    final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
                    final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(this,
                            alertView, errorMessage, null);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ActivityConstants.MATERIAL_CONTROL_INPUT_ACTIVITY));
                            startActivity(goToIntent);
                        }
                    });
                }
            } else {
                actionRadioGroup = findViewById(R.id.action_rg);
                actionRadioGroup.setOnCheckedChangeListener(this);
                if (actionRadioGroup.getChildCount() <= 0) {
                    int i = 0;
                    for (BasicDataTO buttonData : buttonDataList) {
                        RadioButton button = (RadioButton) (LayoutInflater.from(this).inflate(R.layout.action_radio_button, null));
                        RadioGroup.LayoutParams btnParam = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        btnParam.setMargins(0, (int) GuIUtil.convertDpToPixel(10f, this), 0, 0);
                        button.setId(buttonData.getId().intValue());
                        button.setLayoutParams(btnParam);
                        button.setTypeface(button.getTypeface(), Typeface.BOLD);
                        button.setText(buttonData.getInfo());
                        button.setTag(buttonData);
                        actionRadioGroup.addView(button);
                    }
                }
            }
            if (selectedButtonData != null) {
                actionRadioGroup.check(selectedButtonData.getId().intValue());
            }
        } catch (Exception e) {
            writeLog("MaterialControlRegisterUnitActivity  : updateoptionList() ", e);
        }
    }

    public void updateTitleBar() {
        try {
            unitId.setText(ObjectCache.getTypeName(UnitIdentifierTTO.class,
                    SESPPreferenceUtil.getPreference(ConstantsAstSep.SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER)) + ": " + strUnitId);
            //palletCode = SESPPreferenceUtil.getPreferenceString(StockhandlingConstants.MATERIAL_CONTROL_PALLET_CODE);
            if (Utils.isNotEmpty(palletCode)) {
                palletCodeTextView.setVisibility(View.VISIBLE);
                palletCodeTextView.setText(getString(R.string.pallet_code) + ": " + palletCode);
            } else {
                palletCodeTextView.setVisibility(View.GONE);
            }
        } catch (Exception excep) {
            writeLog(TAG + ":updateTitleBar() ", excep);
        }
    }

    /**
     * Update Button Text label
     *
     * @param unitStatusTypeInformationType
     */
    protected void updateName(UnitStatusTypeInformationType unitStatusTypeInformationType) {
        try {
            switch (unitStatusTypeInformationType.getUnitStatusType().getCode()) {
                case StockhandlingConstants.CONTROL:
                    unitStatusTypeInformationType.getUnitStatusType().setInfo(
                            getString(R.string.manage_materiel_to_service));
                    break;
                case StockhandlingConstants.MOUNTABLE:
                    unitStatusTypeInformationType.getUnitStatusType().setInfo(
                            getString(R.string.manage_materiel_to_storage));
                    break;
                case StockhandlingConstants.WASTED:
                    unitStatusTypeInformationType.getUnitStatusType().setInfo(
                            getString(R.string.manage_materiel_to_scrapping));
                    break;
                case StockhandlingConstants.SPECIAL_WASTE:
                    unitStatusTypeInformationType.getUnitStatusType().setInfo(
                            getString(R.string.manage_materiel_mark_as_special_waste));
                    break;
                case StockhandlingConstants.SUSPICIOUS_OLD_METER:
                    unitStatusTypeInformationType.getUnitStatusType().setInfo(
                            getString(R.string.manage_materiel_mark_as_suspicious_old_meter));
                    break;
            }
        } catch (Exception e) {
            writeLog("MaterialControlRegisterUnitActivity  : updateName() ", e);
        }
    }

    public void infoButtonClicked() {
        try {
            Log.d(TAG, "=header unitinfo==button clicked===");
            Intent intent = new Intent(this, ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.DEVICE_INFO_SHOW_ACTIVITY));
            intent.putExtra("UnitInformationTO", unitInformationTO);
            startActivity(intent);
        } catch (Exception excep) {
            writeLog(TAG + ":infoButtonClicked() ", excep);
        }
    }

    public void selectPalletButtonClicked() {
        Log.d(TAG, "=SELECT PALLET BUTTON clicked===");
        try {
            Intent callingIntent = new Intent(this, ActivityFactory.getInstance().getActivityClass(ActivityConstants.MATERIAL_CONTROL_SELECT_PALLET_ACTIVITY));
            callingIntent.putExtra("UnitInformationTO", unitInformationTO);
            callingIntent.putExtra("UnitId", strUnitId);
            callingIntent.putExtra("SelectedButton", selectedButtonData);
            startActivity(callingIntent);
        } catch (Exception excep) {
            writeLog(TAG + ":selectPalletButtonClicked() ", excep);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        try {
            selectedButtonData = (BasicDataTO) group.findViewById(checkedId).getTag();
            Log.d(TAG, "Button clicked : " + selectedButtonData.getCode());
            if (!selectedButtonData.getCode().equals(StockhandlingConstants.CONTROL)) {
                selectPallet.setVisible(false);
                palletCodeTextView.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Pallet code : " + palletCode);
                if (Utils.isEmpty(palletCode)) {
                    selectPalletButtonClicked();
                }
                selectPallet.setVisible(true);
                palletCodeTextView.setVisibility(View.VISIBLE);
            }
            reasonForActionLayout.setVisibility(View.VISIBLE);
            headerText.setText(selectedButtonData.getName());
            fillReasonSpinner();

    } catch(
    Exception e)

    {
        writeLog(TAG + ": onCheckedChanged()", e);
    }

}
    private void getInput() {
        Spinner reasonSpinner = findViewById(R.id.reasonSpinner);
        Long selectedItemId = reasonSpinner.getAdapter().getItemId(reasonSpinner.getSelectedItemPosition());
        if (selectedItemId == -1) {
            Toast.makeText(this, this.getString(R.string.error_reason_is_missing), Toast.LENGTH_SHORT).show();
        } else {
            UnitStatusReasonTTO unitStatusReasonTTO = ObjectCache.getType(UnitStatusReasonTTO.class, selectedItemId);
            Log.d(TAG, "SELECTED ITEM ID ::" + selectedItemId);
            Log.d(TAG, "SELECTED REASON IS ::" + unitStatusReasonTTO.getName());
            List<UnitStatusReasonTTO> listStatusReason = new ArrayList<UnitStatusReasonTTO>();
            listStatusReason.add(unitStatusReasonTTO);
            unitInformationTO.setUnitStatusReasonTTOs(listStatusReason);
        }

    }


    private class MaterialControlReasonAdapter extends ArrayAdapter {
        List<BasicDataTO> basicDataTOs;

        public MaterialControlReasonAdapter(Context context, int textViewResourceId, List<BasicDataTO> basicDataTOs) {
            super(context, textViewResourceId);
            this.basicDataTOs = basicDataTOs;
        }

        public int getCount() {
            return this.basicDataTOs.size();
        }

        public String getItem(int position) {
            return this.basicDataTOs.get(position).getName();
        }

        public long getItemId(int position) {
            BasicDataTO userObject = this.basicDataTOs.get(position);
            return userObject.getId();
        }

        public void remove(BasicDataTO obj) {
            this.basicDataTOs.remove(obj);
        }

    }

    private void fillReasonSpinner() {
        BasicDataTO buttonData = (BasicDataTO) actionRadioGroup.findViewById(actionRadioGroup.getCheckedRadioButtonId()).getTag();
        Spinner reasonSpinner = findViewById(R.id.reasonSpinner);
        List<BasicDataTO> reasonList = null;
        List<UnitStatusTypeInformationType> unitStatusTypeInformationTypes = MaterialLogisticsUtils.createUnitStatusTypeInformationTypes();
        for (UnitStatusTypeInformationType unitStatusTypeInformationType : unitStatusTypeInformationTypes) {
            if (unitStatusTypeInformationType.getUnitStatusType().equals(buttonData)) {
                reasonList = Arrays.asList(unitStatusTypeInformationType.getAvailableUnitStatusReasonTypes());
                break;
            }
        }
        MaterialControlReasonAdapter  spinnerArrayAdapter = new MaterialControlReasonAdapter(this, android.R.layout.simple_spinner_item, reasonList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        reasonSpinner.setAdapter(spinnerArrayAdapter);
    }

 /**
     * Next button is clicked
     */
    public void saveButtonClicked(View view) {
        try {
           /* if (actionRadioGroup.getCheckedRadioButtonId() <= 0) {
                String errorMessage = getString(R.string.atleast_one_option_should_be_selected);
                final View alertView
                        = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.prompt_user_response_layout, null);
                if (alertView != null) {
                    final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
                    final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(this,
                            alertView, errorMessage, getString(R.string.cannot_proceed));
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            } else {*/
                BasicDataTO buttonData = (BasicDataTO) actionRadioGroup.findViewById(actionRadioGroup.getCheckedRadioButtonId()).getTag();
                Log.d(TAG, "Button clicked : " + buttonData.getCode());
               if ((Utils.isNotEmpty(palletCode)
                      || !buttonData.getCode().equals(StockhandlingConstants.CONTROL))) {
                   /* Intent intent = new Intent(this, activityFactory.getActivityClass(ActivityConstants.MATERIAL_CONTROL_SELECT_REASON_ACTIVTY));
                    unitInformationTO.setPalletCode(palletCode);
                    intent.putExtra("UnitInformationTO", unitInformationTO);
                    intent.putExtra("ActionInfo", buttonData);
                    intent.putExtra("UnitId", strUnitId);
                    intent.putExtra("title", buttonData.getName());
                    startActivity(intent);*/
                   Log.d(TAG, "=save unitinfo==button clicked===");
                   getInput();
                   new RegisterMaterialControlUnitSaveThread(this, strUnitId, unitInformationTO, buttonData).start();
                   //SESPPreferenceUtil.savePreference(ConstantsAstSep.StockhandlingConstants.MATERIAL_CONTROL_PALLET_CODE, "");
                   Log.d(TAG, "SAVE BUTTON IS CLICKED");

             } else {

                    String errorMessage = getString(R.string.pallet_is_not_selected);
                    final View alertView
                            = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.prompt_user_response_layout, null);
                    if (alertView != null) {
                        final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
                        final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(this,
                                alertView, errorMessage, null /*getString(R.string.cannot_proceed)*/);
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                }

        } catch (Exception e) {
            writeLog("MaterialControlRegisterUnitActivity  : nextButtonClicked() ", e);
        }
    }
}
