package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.WorkorderAdditionalProcessingCallbackListener;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstMepElCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by yhegde on 3/2/2018.
 */

@SuppressLint("ValidFragment")
public  class ConfirmCTRatioFragment extends CustomFragment implements View.OnClickListener{


    /**
     * Drawables for quick reference
     */

    protected transient Dialog dialog = null;
    protected transient View ctlExistsHiddenView = null;
    protected Long mMeterConstant = 1l;
    public WorkorderAdditionalProcessingCallbackListener processingCallbackListener;

    protected transient final String USER_CHOICE_YES_NO = "USER_CHOICE_YES_NO";


    protected TextView mMeterContET;
    protected Button mOKBtn;

    protected ConfirmCTRatioFragment() {
        super();
    }
    public ConfirmCTRatioFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

   

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_confirm_ct_ratio, null);
        initViews();
        setupListeners();
        populateView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        
    }
    public void applyChangesToModifiableWO() {
        try{
        if(processingCallbackListener != null) {
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            processingCallbackListener.process(wo, stepfragmentValueArray);
        }
        } catch (Exception e) {
            writeLog(TAG + " :applyChangesToModifiableWO()", e);
        }
    }
    /**
     * Lookup and initialize all the view widgets
     */

    protected void initViews() {
        mMeterContET = fragmentView.findViewById(R.id.meter_constant_et);
        mOKBtn = fragmentView.findViewById(R.id.btn_ok);
        mMeterContET.setEnabled(false);
    }


    protected void populateView() {
        try{
        WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        if (modifiableWo != null
                && modifiableWo.getWorkorderCustomTO() != null
                && modifiableWo.getWorkorderCustomTO().getWoInst() != null
                && modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null
                && modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepElCustomTO() != null) {

          mMeterConstant = TypeDataUtil.getValidOrgDivValue(modifiableWo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepElCustomTO(),
                    WoInstMepElCustomTO.REGISTER_CONSTANT_O);
        }
        if (mMeterConstant == null) {
          mMeterConstant = 1l;
        }
        mMeterContET.setText(mMeterConstant + "");

        if ("YES".equalsIgnoreCase((String)stepfragmentValueArray.get(USER_CHOICE_YES_NO))) {
            mOKBtn.setBackgroundResource(R.drawable.ok_enabled);
        } else if ("NO".equalsIgnoreCase((String)stepfragmentValueArray.get(USER_CHOICE_YES_NO))) {
            mOKBtn.setBackgroundResource(R.drawable.ok_disabled);
        }
    } catch (Exception e) {
        writeLog(TAG + " :populateView()", e);
    }

    }

    protected void setupListeners() {
        if (mOKBtn != null) {
            mOKBtn.setOnClickListener(this);
        }
    }
    @Override
    public boolean validateUserInput() {
        applyChangesToModifiableWO();
        return (Utils.isNotEmpty((String)stepfragmentValueArray.get(USER_CHOICE_YES_NO)) && ("YES".equals(stepfragmentValueArray.get(USER_CHOICE_YES_NO))));
    }

    /**
     * When the user navigates away from this page
     * save the user progress in this method
     */
    public void applyChanges() {
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
        if(v != null) {
            if (v.getId() == R.id.btn_ok) {
                if(Utils.isNotEmpty(stepfragmentValueArray.get(USER_CHOICE_YES_NO))) { // Subsequent clicks

                    Log.i("ConfirmCTRatioFragment on click method","Inside outer if of onclick()");
                    if("YES".equals(stepfragmentValueArray.get(USER_CHOICE_YES_NO))) {
                        Log.i("ConfirmCTRatioFragment on click method","Inside inner if of onclick()");
                      stepfragmentValueArray.put(USER_CHOICE_YES_NO, "NO");
                        mOKBtn.setBackgroundResource(R.drawable.ok_disabled);
                    } else if("NO".equals(stepfragmentValueArray.get(USER_CHOICE_YES_NO))) {
                        Log.i("ConfirmCTRatioFragment on click method","Inside inner else of onclick()");
                      stepfragmentValueArray.put(USER_CHOICE_YES_NO, "YES");
                        mOKBtn.setBackgroundResource(R.drawable.ok_enabled);
                    }
                } else { // First Time click
                    Log.i("ConfirmCTRatioFragment on click method","Inside outer else of onclick()");
                  stepfragmentValueArray.put(USER_CHOICE_YES_NO,"YES");
                    mOKBtn.setBackgroundResource(R.drawable.ok_enabled);
                }
            } else if (v.getId() == R.id.okButtonYesNoPage
                    && dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }
}