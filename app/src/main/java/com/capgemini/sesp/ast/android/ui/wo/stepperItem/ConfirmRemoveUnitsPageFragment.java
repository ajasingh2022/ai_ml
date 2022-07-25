package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitDismldReasonTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by yhegde on 3/2/2018.
 */

@SuppressLint({"ValidFragment", "InflateParams"})
public class ConfirmRemoveUnitsPageFragment extends CustomFragment implements View.OnClickListener {

    List<LinearLayout> unitLayoutList = new ArrayList<LinearLayout>();
    private View mView;
    private static String TAG = ConfirmRemoveUnitsPageFragment.class.getSimpleName();
    private transient Dialog dialog = null;
    private int mCheckBoxCount = 0;
    Map<String, String> userSelection = new ArrayMap<String, String>();

    public ConfirmRemoveUnitsPageFragment() {
        super();

    }

    public ConfirmRemoveUnitsPageFragment(String stepIdentifier) {
        super(stepIdentifier);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.confirm_remove_units_layout, container, false);
        initializeValues();
        setData();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void initializeValues() {
        try {
            mCheckBoxCount =0;
            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (stepfragmentValueArray != null) {
                for (int i = 0; i < modifiableWo.getWorkorderCustomTO().getWoUnits().size(); i++) {
                    WoUnitCustomTO unit = modifiableWo.getWorkorderCustomTO().getWoUnits().get(i);
                    if (!TextUtils.isEmpty(unit.getGiai())) {
                        userSelection.put(unit.getGiai(), (String) stepfragmentValueArray.get(unit.getGiai()));
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":initializeValues()", e);
        }
    }

    private void setData() {
        try {
            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            for (int i = 0; i < modifiableWo.getWorkorderCustomTO().getWoUnits().size(); i++) {
                LinearLayout unitLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.unit_removal_layout, null);
                ((LinearLayout) fragmentView.findViewById(R.id.removed_units_container)).addView(unitLayout);
                unitLayoutList.add(unitLayout);

                WoUnitCustomTO unit = modifiableWo.getWorkorderCustomTO().getWoUnits().get(i);

                if (!TextUtils.isEmpty(unit.getIdWoUnitT() + "")) {
                    ((TextView) unitLayout.findViewById(R.id.text_value_UnitType)).setText(ObjectCache.getTypeName(WoUnitTTO.class, unit.getIdWoUnitT()) + "");
                }

                if (!TextUtils.isEmpty(unit.getUnitModel())) {
                    ((TextView) unitLayout.findViewById(R.id.text_value_UnitModel)).setText(unit.getUnitModel());
                }
                CheckBox cb = unitLayout.findViewById(R.id.checkBoxUnitIsRemoved);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            userSelection.put((String) buttonView.getTag(), "YES");
                            mCheckBoxCount++;
                        } else {
                            if (userSelection.containsKey(buttonView.getTag())) {
                                userSelection.remove(buttonView.getTag());
                            }
                            mCheckBoxCount--;
                        }

                    }
                });
                if (!TextUtils.isEmpty(unit.getGiai())) {
                    ((TextView) unitLayout.findViewById(R.id.text_value_Giai)).setText(unit.getGiai());
                    cb.setTag(unit.getGiai());
                    if (userSelection.size() > 0) {
                        if ("YES".equals(userSelection.get(unit.getGiai()))) {
                            cb.setChecked(true);
                        }
                    }
                }


            }
        } catch (Exception e) {
            writeLog(TAG + ":setData()", e);
        }

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

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void applyChangesToModifiableWO() {
        try {
            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            for (WoUnitCustomTO unit : modifiableWo.getWorkorderCustomTO().getWoUnits()) {

                //changed according to order
                unit.setDismantledReasons(new ArrayList<WoUnitDismldReasonTO>());
                WoUnitDismldReasonTO woUnitStatusReasonTO = new WoUnitDismldReasonTO();
                woUnitStatusReasonTO.setIdUnitStatusReasonT(CONSTANTS().UNIT_STATUS_REASON_T__04);
                unit.getDismantledReasons().add(woUnitStatusReasonTO);

                //dismount the unit.
                unit.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
                unit.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);

                unit.setUnitDismantledTimestamp(new Date());
            }
        } catch (Exception e) {
            writeLog(TAG + ":applyChangesToModifiableWO()", e);
        }
    }


    public boolean validateUserInput() {
        try {
            for (Map.Entry<String, String> entry : userSelection.entrySet()) {
                System.out.println(entry.getKey() + "/" + entry.getValue());
                stepfragmentValueArray.put(entry.getKey(), entry.getValue());
            }


            WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (mCheckBoxCount == modifiableWo.getWorkorderCustomTO().getWoUnits().size()) {
                applyChangesToModifiableWO();
                return true;
            }
        } catch (Exception e) {
            writeLog(TAG + " :validateUserInput()", e);
        }
        return false;
    }
}