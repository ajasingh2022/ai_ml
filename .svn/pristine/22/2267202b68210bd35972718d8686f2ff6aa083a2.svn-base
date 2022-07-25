package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.PdaWoResultConfigTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.List;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint("ValidFragment")
public class CommunicationFailureReasonPageFragment extends CustomFragment implements View.OnClickListener {

    private ListView CommunicationFailureReasonLv = null;
    private TextView workorderNote, screenHeader = null;
    private static String TAG = CommunicationFailureReasonPageFragment.class.getSimpleName();
    private TroubleshootNoCommunicatinAdapter mReasonAdapter;
    private String question="";
    private transient Dialog dialog = null;
    transient long selectedReason = -1;
    transient long unselectedReason = -1;
    Set<Long> unselectedReasonIds;
    transient Long caseTypeId;
    private transient String choosenReason;
    private transient long selectedReasonAtStart;
    List<WoEventResultReasonTTO> woEventResultReasonTTOs;
    protected Long pageType;

    public CommunicationFailureReasonPageFragment(String stepId, Long pdaPageType) {
        super(stepId);
        pageType = pdaPageType;
    }

    public CommunicationFailureReasonPageFragment(String stepId, Long pdaPageType, String question) {
        super(stepId);
        this.pageType = pdaPageType;
        this.question=question;
    }

    public CommunicationFailureReasonPageFragment() {
        super();
    }

    public CommunicationFailureReasonPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }


    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_not_accessible_reason, null);
        initializePageValues();
        initialize();
        setupListeners();
        populateData();
        return fragmentView;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initialize() {
        try {
            if (fragmentView != null) {
                workorderNote = fragmentView.findViewById(R.id.workorderNote);
                workorderNote.setVisibility(View.GONE);
                screenHeader = fragmentView.findViewById(R.id.screenHeader);
                if(!question.isEmpty()) {
                    screenHeader.setText(question);
                }
                else
                    screenHeader.setText(getResources().getString(R.string.enter_the_reason_for_the_communication_problem));
                CommunicationFailureReasonLv = fragmentView.findViewById(R.id.notAccessibleReasonLv);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : initialize() ", e);
        }
    }

    private void setupListeners() {

    }

    public void initializePageValues() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            caseTypeId = wo.getIdCaseType();

            if (stepfragmentValueArray != null) {
                choosenReason = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.COMMUNICATION_FAILURE_REASON);
            }
            if (choosenReason != null && !"".equals(choosenReason.trim())) {
                selectedReason = Long.valueOf(choosenReason);
            } else {
                selectedReason = -1;
            }
            unselectedReason = -1;
            selectedReasonAtStart = selectedReason;
        } catch (Exception e) {
            writeLog(TAG + ":initializePageValues()", e);
        }
    }

    private void populateData() {
        try {
            List<WoEventResultReasonTTO> woEventResultReasonTTOList = ObjectCache.getAllTypes(WoEventResultReasonTTO.class);
            List<PdaWoResultConfigTCTO> pdaWoResultConfigTCTOList = ObjectCache.getAllTypes(PdaWoResultConfigTCTO.class);
            woEventResultReasonTTOs = TypeDataUtil.getResultReasonType(caseTypeId, pageType, pdaWoResultConfigTCTOList, woEventResultReasonTTOList);
            CommunicationFailureReasonLv.setOnTouchListener(new ListView.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });
            if (woEventResultReasonTTOs != null && !woEventResultReasonTTOs.isEmpty()) {
                mReasonAdapter = new TroubleshootNoCommunicatinAdapter(getActivity(), this);
                CommunicationFailureReasonLv.setAdapter(mReasonAdapter);
            }
        } catch (Exception e) {
            writeLog(TAG + ":populateData()", e);
        }

    }


    /**
     * User has not selected any option
     * and tries to move forward, user should be prompted
     * to select an option
     */
    @Override
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
                    alertView, getString(R.string.please_select_the_reason_for_no_communication), null);
            dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean validateUserInput() {
        //check if any check box(Reason for not acciable) is checked else false
        if (mReasonAdapter != null && mReasonAdapter.isReasonChecked()) {
            getUserChoiceValuesForPage();
            applyChangesToModifiableWO();
            return true;
        } else {
            return false;
        }
    }

    public void applyChangesToModifiableWO() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            for (WoEventResultReasonTTO woEventResultReasonTTO : woEventResultReasonTTOs) {
                unselectedReasonIds.add(woEventResultReasonTTO.getId());
            }
            WorkorderUtils.addRemoveResultReasons(selectedReason, unselectedReasonIds, wo);
            if (processingCallbackListener != null) {
                processingCallbackListener.process(wo, selectedReason, unselectedReasonIds, pageType);
            }
        } catch (Exception e) {
            writeLog(TAG + ":applyChangesToModifiableWO()", e);
        }
    }

    public void getUserChoiceValuesForPage() {
        try{
            String noCommunicationReasonCodes = getNoCommunicationReasonCodes();
            if (Utils.isNotEmpty(noCommunicationReasonCodes)) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.COMMUNICATION_FAILURE_REASON, noCommunicationReasonCodes);
            }
        } catch (Exception e) {
            writeLog(TAG + " :getUserChoiceValuesForPage()", e);
        }
    }


    private String getNoCommunicationReasonCodes() {
        String reasonCodes = null;
        try {
            if (selectedReason != -1) {
                reasonCodes = String.valueOf(selectedReason);
            }
        } catch (Exception e) {
            writeLog(TAG + ":getNoCommunicationReasonCodes()", e);
        }
        return reasonCodes;
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
}


