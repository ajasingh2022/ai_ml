package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.PdaWoResultConfigTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.FlowPageConstants.NOT_POSSIBLE_REASON;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by sacrai on 2/08/2018.
 *
 * @author sacrai
 * @since 2nd August, 2018
 */
@SuppressLint("ValidFragment")
public class OldMeterChangeYesNoPageFragment extends CustomFragment
        implements View.OnClickListener {


    RadioGroup oldMeterChangeYesNoRG = null;
    protected transient View collapsibleLayout = null;

    protected transient ListView notPssbleLv = null;
    protected transient TextView noteOldMeterChangeYesNoPage = null;
    protected transient Dialog dialog = null;
    private transient String notPossibleReason = null;
    private transient String notPossibleReasonAtStart = null;
    transient Set<WoEventResultReasonTTO> selectedReasons = null;
    private transient String[] npReasonCode = null;
    private String TAG = OldMeterChangeYesNoPageFragment.class.getSimpleName();

    public OldMeterChangeYesNoPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    public void initializePageValues() {
        try {
            selectedReasons = new HashSet<WoEventResultReasonTTO>();
            if (AbstractWokOrderActivity.getWorkorderCustomWrapperTO() != null) {


                if (stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE) != null) {
                    if (stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE).toString().equalsIgnoreCase(ConstantsAstSep.FlowPageConstants.NO)) {
                        if (stepfragmentValueArray.get(NOT_POSSIBLE_REASON) != null)
                            notPossibleReason = (String) stepfragmentValueArray.get(NOT_POSSIBLE_REASON);
                        npReasonCode = notPossibleReason.split(";");
                        if ((npReasonCode != null) && (npReasonCode.length > 0)) {
                            for (String tempReasoneId : npReasonCode) {
                                WoEventResultReasonTTO woEventResultReasonTTO = ObjectCache.getType(WoEventResultReasonTTO.class, Long.parseLong(tempReasoneId));
                                if (woEventResultReasonTTO != null) {
                                    selectedReasons.add(woEventResultReasonTTO);
                                }
                            }
                        }


                    }

                }
            }
            notPossibleReasonAtStart = notPossibleReason;
        } catch (Exception e) {
            writeLog(TAG + "  : initializePageValues() ", e);
        }
    }

    private String getNotPossibleReasonCodes() {
        String reasoneCodes = null;
        try {
            if (selectedReasons != null) {
                StringBuilder reasonCodeBuilder = new StringBuilder();
                for (WoEventResultReasonTTO woEventResultReasonTTO : selectedReasons) {
                    reasonCodeBuilder.append(woEventResultReasonTTO.getId());
                    reasonCodeBuilder.append(";");
                }
                reasoneCodes = reasonCodeBuilder.toString();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getNotPossibleReasonCodes() ", e);
        }
        return reasoneCodes;
    }

    public String evaluateNextPage() {
        String value = null;
        try {
            stepfragmentValueArray.put(NOT_POSSIBLE_REASON, getNotPossibleReasonCodes());
            value = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE);
        } catch (Exception e) {
            writeLog(TAG + "  : evaluateNextPage() ", e);
        }
        return value;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_old_meter_change_yes_no, null);
        {
            initializePageValues();
            initialize();
            populateData();
            setupListeners();
        }
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        {

        }
    }

    protected void initialize() {
        // lookup the view objects and store in instance level variables
        collapsibleLayout = fragmentView.findViewById(R.id.oldMeterChangeYesNoLayout);
        noteOldMeterChangeYesNoPage = fragmentView.findViewById(R.id.noteOldMeterChangeYesNoPage);

        oldMeterChangeYesNoRG = fragmentView.findViewById(R.id.oldMeterChangeYesNo);
        oldMeterChangeYesNoRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes) {
                    showHideReasonLayout(false);
                    stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE, ConstantsAstSep.FlowPageConstants.YES);
                } else if (checkedId == R.id.no) {
                    showHideReasonLayout(true);
                    stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE, ConstantsAstSep.FlowPageConstants.NO);
                }
            }
        });

        notPssbleLv = fragmentView.findViewById(R.id.notPssbleLv);

        //Call this method to set listview height dynamically inside scrollView
        GuIUtil.setListViewHeightBasedOnChildren(notPssbleLv);

        // Setting on Touch Listener for handling the touch inside ScrollView
        notPssbleLv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    protected void populateData() {
        try {
            String floName = this.getActivity().getClass().getSimpleName();

            // added for ui modification (switch) - start
            if (Utils.isNotEmpty(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE))) {

                if (ConstantsAstSep.FlowPageConstants.NO.equals(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE))) {
                    oldMeterChangeYesNoRG.check(oldMeterChangeYesNoRG.getChildAt(1).getId());
                    showHideReasonLayout(true);
                } else if (ConstantsAstSep.FlowPageConstants.YES.equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE)))) {
                    oldMeterChangeYesNoRG.check(oldMeterChangeYesNoRG.getChildAt(0).getId());
                    showHideReasonLayout(false);
                }
            }


            // Get data for all not accessible reason types
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            List<PdaWoResultConfigTCTO> pdaWoResultConfigTCTOs = ObjectCache.getAllTypes(PdaWoResultConfigTCTO.class);
            Collections.sort(pdaWoResultConfigTCTOs, new Comparator<PdaWoResultConfigTCTO>() {
                @Override
                public int compare(PdaWoResultConfigTCTO object1, PdaWoResultConfigTCTO object2) {
                    if (object1 == null || object2 == null)
                        return -1;

                    if (object1.getId() != null && object2.getId() != null) {
                        return object1.getId().compareTo(object2.getId());
                    }

                    //keep existing order
                    return -1;
                }
            });
            final List<WoEventResultReasonTTO> reasonTypeList = AndroidUtilsAstSep.getResultReason(wo.getIdCaseType(), CONSTANTS().PDA_PAGE_T__NOT_POSSIBLE,
                    pdaWoResultConfigTCTOs, ObjectCache.getAllTypes(WoEventResultReasonTTO.class));

            if (reasonTypeList != null && !reasonTypeList.isEmpty()) {
                notPssbleLv.setAdapter(new OldMeterNotReplaceableAdapter(this, reasonTypeList, selectedReasons));
                //Call this method to set listview height dynamically inside scrollView
                GuIUtil.setListViewHeightBasedOnChildren(notPssbleLv, 4);
            }


            //Note
            if (Utils.isNotEmpty(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.NOT_POSSIBLE_NOTE))) {
                noteOldMeterChangeYesNoPage.setText((CharSequence) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.NOT_POSSIBLE_NOTE));
            }
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }
    }

    protected void setupListeners() {


        if (noteOldMeterChangeYesNoPage != null) {
            noteOldMeterChangeYesNoPage.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void afterTextChanged(Editable s) {
                    stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.NOT_POSSIBLE_NOTE, s.toString());
                }
            });
        }
    }

    private void showHideReasonLayout(final Boolean state) {
        if (state != null) {
            if (state) {
                collapsibleLayout.setVisibility(View.VISIBLE);
            } else {
                collapsibleLayout.setVisibility(View.GONE);
            }
        } else {
            if (collapsibleLayout != null) {
                if (collapsibleLayout.getVisibility() == View.GONE) {
                    collapsibleLayout.setVisibility(View.VISIBLE);
                } else {
                    collapsibleLayout.setVisibility(View.GONE);
                }
            }
        }
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

    /**
     * User has not selected any option
     * and tries to move forward, user should be prompted
     * to select an option
     */

    public void showPromptUserAction() {
        try {
            final View alertView
                    = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.prompt_user_response_layout, null);

            if (alertView != null) {

                ((TextView) alertView.findViewById(R.id.msg1)).setText(getResources().getString(R.string.select_option));

                final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
                okButton.setOnClickListener(this);

                final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                        alertView, null, null);
                dialog = builder.create();
                dialog.show();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : showPromptUserAction() ", e);
        }
    }

    @Override
    public boolean validateUserInput() {
        boolean status = false;
        try {
            if (ConstantsAstSep.FlowPageConstants.YES.equals(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE))) {
                status = true;
            } else if (ConstantsAstSep.FlowPageConstants.NO.equals(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE))
                    && !selectedReasons.isEmpty()) {
                // Check if user has selected at least 1 reason
                status = true;
            }
            if (status)
                applyChangesToModifiableWO();
        } catch (Exception e) {
            writeLog(TAG + "  : validateUserInput() ", e);
        }
        return status;
    }

    @Override
    public void applyChangesToModifiableWO() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            //undoChanges();
            if (processingCallbackListener != null) {
                processingCallbackListener.process(wo);
            }
            if (ConstantsAstSep.FlowPageConstants.NO.equals(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.METER_CHANGE_POSSIBLE)))) {
                if (Utils.isNotEmpty(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.NOT_POSSIBLE_NOTE))) {
                    WoInfoCustomTO woInfoTO = WorkorderUtils.createInfo(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.NOT_POSSIBLE_NOTE)), SessionState.getInstance().getCurrentUserUsername(),
                            CONSTANTS().INFO_SOURCE_T__SESP_PDA, CONSTANTS().INFO_T__RESULT_REASON_OTHER);
                    WorkorderUtils.deleteAndAddWoInfoCustomMTO(wo, woInfoTO);
                }
                if (Utils.isNotEmpty(selectedReasons)) {
                    WorkorderUtils.addEventResultReasons(wo, selectedReasons, true);
                }

            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }
    }

    /**
     * Add the currently selected reason to the reason map
     *
     * @param selectedReason
     */

    public void addNotPossibleReason(final WoEventResultReasonTTO selectedReason) {
        if (selectedReason != null) {
            selectedReasons.add(selectedReason);
        }
    }

    /**
     * Remove the currently selected reason from the map
     *
     * @param selectedReason
     */


    public void removeNotPossibleReason(final WoEventResultReasonTTO selectedReason) {
        if (selectedReason != null) {
            selectedReasons.remove(selectedReason);
        }
    }


}
