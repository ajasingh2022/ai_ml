package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitDismldReasonTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint("ValidFragment")
public class ChangeMeterReasonPageFragment extends CustomFragment implements View.OnClickListener {

    private ListView changeMeterReasonLv = null;
    private TextView workorderNote, screenHeader = null;
    private transient Dialog dialog = null;
    transient Set<UnitStatusReasonTTO> mSelectedChangeMeterReason = null;
    private transient String mSelectedChangeMeterReasonAtStart = null;
    private String mChangeMeterReason = null;
    private String TAG = ChangeMeterReasonPageFragment.class.getSimpleName();


    public ChangeMeterReasonPageFragment() {
        super();
    }

    public ChangeMeterReasonPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_not_accessible_reason, null);
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

    private void initialize() {
        final View parentView = fragmentView;
        if (parentView != null) {
            workorderNote = parentView.findViewById(R.id.workorderNote);
            workorderNote.setVisibility(View.GONE);
            screenHeader = parentView.findViewById(R.id.screenHeader);
            screenHeader.setText(getString(R.string.reason_for_changing_meter));
            changeMeterReasonLv = parentView.findViewById(R.id.notAccessibleReasonLv);
            changeMeterReasonLv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    private void setupListeners() {

    }

    private void populateData() {
        try {
            List<UnitStatusReasonTTO> changeMeterReasonList = AndroidUtilsAstSep.getChangeMeterReasons();
            if (changeMeterReasonList != null && !changeMeterReasonList.isEmpty()) {
                changeMeterReasonLv.setAdapter(new ChangeMeterReasonAdapter(this, changeMeterReasonList, mSelectedChangeMeterReason));
            }
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }

    }

    @Override
    public void applyChangesToModifiableWO() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if ((mSelectedChangeMeterReason != null) && (mSelectedChangeMeterReason.size() > 0)) {
                WoUnitCustomTO existingMeter = UnitInstallationUtils.getModifiedUnit(
                        wo.getWorkorderCustomTO().getWoUnits(),
                        AndroidUtilsAstSep.CONSTANTS().WO_UNIT_T__METER,
                        AndroidUtilsAstSep.CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);

                if (existingMeter != null) {
                    List<WoUnitDismldReasonTO> woUnitStatusReasonTOList = new ArrayList<WoUnitDismldReasonTO>();
                    for (UnitStatusReasonTTO unitStatusReasonTTO : mSelectedChangeMeterReason) {
                        WoUnitDismldReasonTO woUnitDismldReasonTO = new WoUnitDismldReasonTO();
                        woUnitDismldReasonTO.setIdCase(wo.getIdCase());
                        woUnitDismldReasonTO.setIdWoUnit(existingMeter.getId());
                        woUnitDismldReasonTO.setIdUnitStatusReasonT(unitStatusReasonTTO.getId());
                        woUnitStatusReasonTOList.add(woUnitDismldReasonTO);
                    }
                    existingMeter.setDismantledReasons(woUnitStatusReasonTOList);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :applyChangesToModifiableWO()", e);
        }
    }


    public Map<String, String> getUserChoiceValuesForPage() {
        Map<String, String> userChoice = new ArrayMap<String, String>();
        try {
            String changeMeterReasonCode = getChangeMeterReasonCode();
            if (Utils.isNotEmpty(changeMeterReasonCode)) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.CHANGE_METER_REASON, changeMeterReasonCode);
            }
        } catch (Exception e) {
            writeLog(TAG + " :getUserChoiceValuesForPage()", e);
        }
        return userChoice;
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
    public boolean validateUserInput() {
        boolean status = false;
        try {
            if (!mSelectedChangeMeterReason.isEmpty()) {
                getUserChoiceValuesForPage();
                applyChangesToModifiableWO();
                status = true;
            }
        } catch (Exception e) {
            writeLog(TAG + " :validateUserInput()", e);
        }
        return status;
    }

    @Override
    public void onClick(View v) {
        if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private String getChangeMeterReasonCode() {

        String reasonCode = null;
        try {
            if (mSelectedChangeMeterReason != null) {

                for (UnitStatusReasonTTO unitStatusReasonTTO : mSelectedChangeMeterReason) {
                    reasonCode = String.valueOf(unitStatusReasonTTO.getId());
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :getChangeMeterReasonCode()", e);
        }
        return reasonCode;
    }


    public void initializePageValues() {
        try {
            mSelectedChangeMeterReason = new HashSet<UnitStatusReasonTTO>();
            mChangeMeterReason = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.CHANGE_METER_REASON);

            if (mChangeMeterReason != null) {
                UnitStatusReasonTTO unitStatusReasonTTO = ObjectCache.getType(UnitStatusReasonTTO.class, Long.valueOf(mChangeMeterReason));

                if (unitStatusReasonTTO != null) {
                    mSelectedChangeMeterReason.add(unitStatusReasonTTO);
                } else {
                    Log.d("Test1", "  Received null tempCode " + mChangeMeterReason);
                }
            }

            mSelectedChangeMeterReasonAtStart = mChangeMeterReason;
        } catch (Exception e) {
            writeLog(TAG + " :initializePageValues()", e);
        }
    }

    /**
     * Apply the changes to modifiable work-order copy
     */

    public void applyChanges() {
        try{
        WorkorderCustomWrapperTO woWrapper = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        if (woWrapper != null && woWrapper.getWorkorderCustomTO() != null) {
            final WorkorderCustomTO wo = woWrapper.getWorkorderCustomTO();

            final String userNote = workorderNote.getText().toString();

				/*
                 *  Add work-order info object
				 *  Table: INFO
				 *  	Linked with tables -> INFO_T and INFO_SOURCE_T
				 */

            final WoInfoCustomTO woInfoCustomTO = WorkorderUtils.createInfo(userNote,
                    CONSTANTS().INFO_SOURCE_T__SESP_PDA, CONSTANTS().INFO_T__RESULT_REASON_OTHER);
            if (woInfoCustomTO != null) {
                // Need to compare here
                if (wo.getInfoList() == null) {
                    wo.setInfoList(new ArrayList<WoInfoCustomTO>());
                }

                wo.getInfoList().add(woInfoCustomTO);
            }

				/*
                 * Add Work order event info object
				 * Table: WO_EVENT
				 * Linked Tables: WO_EVENT_T, WO_EVENT_RESULT_T, WO_EVENT_SOURCE_T
				 */
            WorkorderUtils.getOrCreateWoEventCustomTO(woWrapper,
                    CONSTANTS().WO_EVENT_T__FIELD_VISIT, CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA);
            Log.d("NotAccesibleReasonPage",
                    "NotAccessibleResaonPage-applyChanges woWrapper.getWorkorderCustomTO().getWoEvents().size()" + woWrapper.getWorkorderCustomTO().getWoEvents().size());

        }
        } catch (Exception e) {
            writeLog(TAG + " :applyChanges()", e);
        }
    }

    public void addChangeMeterReason(final UnitStatusReasonTTO selectedReason) {
        if (selectedReason != null) {
            mSelectedChangeMeterReason.clear();
            mSelectedChangeMeterReason.add(selectedReason);
        }
    }


    public void removeChangeMeterReason(final UnitStatusReasonTTO selectedReason) {
        if (selectedReason != null) {
            mSelectedChangeMeterReason.remove(selectedReason);
            mSelectedChangeMeterReason.clear();
        }
    }
}
