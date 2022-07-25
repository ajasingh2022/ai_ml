package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;

import com.capgemini.sesp.ast.android.R;

import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * Created by devldas on 9/20/2018.
 */
@SuppressLint("InflateParams")
public class RiskAssessmentFragment extends CustomFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String RISK_COMMENTS = "RISK_COMMENTS";
    private static final String RISK_REPORT_SELECTION = "RISK_REPORT_SELECTION";
    private static final String YES = "YES";
    private static final String NO = "NO";
    private static final String PREVIOUS_RISK_PHOTO_ATTACHMENT_MANDATORY = "PREVIOUS_RISK_PHOTO_ATTACHMENT_MANDATORY";
    private transient Dialog dialog = null;
    private RadioGroup yesOrNo = null;
    private EditText comments = null;
    static String TAG = RiskAssessmentFragment.class.getSimpleName();



    /*@Override
    public String  evaluateNextPage() {
//String nextPage;
        if(YES.equals(stepfragmentValueArray.get(RISK_REPORT_SELECTION))) {
        //  nextPage.setIntraPageIntent(this.getIntraPageIntent().putExtra(RISK_PHOTO_ATTACHMENT_MANDATORY, true));
        }else{
            //nextPage.setIntraPageIntent(this.getIntraPageIntent().putExtra(RISK_PHOTO_ATTACHMENT_MANDATORY, false));
        }
       return nextPage;
    }*/


    public RiskAssessmentFragment() {
        super();
    }


    public RiskAssessmentFragment(String stepId) {
        super(stepId);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        initializePageValues();
        fragmentView = inflater.inflate(R.layout.risk_assessment, container, false);
        initViews();
        setupListeners();
        populateData();
        return fragmentView;
    }

    @Override
    public void applyChangesToModifiableWO() {
        try {
            WorkorderCustomWrapperTO workOrder = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            String comments = (String) stepfragmentValueArray.get(RISK_COMMENTS);

            if (YES.equals(stepfragmentValueArray.get(RISK_REPORT_SELECTION))) {
                AbstractWokOrderActivity.isPhotoMandatory = true;
            } else
                AbstractWokOrderActivity.isPhotoMandatory = false;


            if(Utils.isNotEmpty(comments) && comments.trim().length() > 0){
                WoInfoCustomTO woInfoCustomTO =
                        WorkorderUtils.createInfo(comments,
                                SessionState.getInstance().getCurrentUserUsername(),
                                CONSTANTS().INFO_SOURCE_T__SESP_PDA,
                                CONSTANTS().INFO_T__INFO_RISK_OBSERVATION);
                WorkorderUtils.deleteAndAddWoInfoCustomMTO(workOrder, woInfoCustomTO);
            }
            if((CONSTANTS().SYSTEM_BOOLEAN_T__FALSE).equals(workOrder.getWorkorderCustomTO().getWoInst().getWoInstTO().getAccessibleToTechnicianO()))
            {
                workOrder.getWorkorderCustomTO().getWoInst().getWoInstTO().setAccessibleToTechnicianV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                workOrder.getWorkorderCustomTO().getWoInst().getWoInstTO().setAccessibleToTechnicianD(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
            }
        } catch (Exception e) {
            writeLog(TAG + " : applyChangesToModifiableWO()", e);
        }
    }


    @Override
    public void onResume() {
        super.onResume();


    }


    protected void initViews() {
        yesOrNo = (RadioGroup) fragmentView.findViewById(R.id.yesOrNo);
        comments = (EditText) fragmentView.findViewById(R.id.comments);
        comments.setVisibility(View.GONE);
    }

    protected void populateData() {
        if ("YES".equals(stepfragmentValueArray.get(RISK_REPORT_SELECTION))) {
            yesOrNo.check(R.id.yesBtn);
            comments.setVisibility(View.VISIBLE);
        } else if ("NO".equals(stepfragmentValueArray.get(RISK_REPORT_SELECTION))) {
            yesOrNo.check(R.id.noBtn);
        }
        if (Utils.isNotEmpty(stepfragmentValueArray.get(RISK_COMMENTS))) {
            comments.setText((String) stepfragmentValueArray.get(RISK_COMMENTS));
            comments.setVisibility(View.VISIBLE);
        }
    }

    protected void setupListeners() {
        yesOrNo.setOnCheckedChangeListener(this);
        comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stepfragmentValueArray.put(RISK_COMMENTS, String.valueOf(s));
            }
        });
    }

    @Override
    public boolean validateUserInput() {
        Boolean result = null;
        try {
            result = ((YES.equals(stepfragmentValueArray.get(RISK_REPORT_SELECTION)) && Utils.isNotEmpty(stepfragmentValueArray.get(RISK_COMMENTS)))
                    || (NO.equals(stepfragmentValueArray.get(RISK_REPORT_SELECTION))));
            if (result) {
                applyChangesToModifiableWO();
            }
        } catch (Exception e) {
            writeLog(TAG + " : validateUserInput()", e);
        }
        return result;
    }


    public void showPromptUserAction() {
        /**
         *  If status is false then it is coming from flow engine stack rebuild
         *  and does not require user attention
         */
        final View alertView
                = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.prompt_user_response_layout, null);
        if (alertView != null) {
            final Button okButton = (Button) alertView.findViewById(R.id.okButtonYesNoPage);
            okButton.setOnClickListener(this);

            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                    alertView, null, null);
            dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            if (v.getId() == R.id.okButtonYesNoPage && dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int selectedId = yesOrNo.getCheckedRadioButtonId();
        if (selectedId == R.id.yesBtn) {
            stepfragmentValueArray.put(RISK_REPORT_SELECTION, YES);
            comments.setVisibility(View.VISIBLE);
        } else if (selectedId == R.id.noBtn) {
            stepfragmentValueArray.put(RISK_REPORT_SELECTION, NO);
            comments.setVisibility(View.GONE);
            comments.setText("");
            stepfragmentValueArray.remove(RISK_COMMENTS);
        }
    }

}


