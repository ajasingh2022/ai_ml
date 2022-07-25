package com.capgemini.sesp.ast.android.ui.wo.stepperItem;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.WorkorderAdditionalProcessingCallbackListener;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * Created by yhegde on 2/16/2018.
 */

@SuppressLint({"ValidFragment", "InflateParams"})
public class YesNoFragment extends CustomFragment implements OnClickListener {
    /*
     * Initializing mostly used view objects
     */

    protected transient String question = null;

    public String getYesNoValue() {
        return yesNoValue;
    }

    private static String TAG = YesNoFragment.class.getSimpleName();
    private String yesNoValue;
    RadioGroup radioYesNoGroup;
    private RadioButton radioYesNoButton;
    protected transient TextView questionTextView = null;
    protected WorkorderAdditionalProcessingCallbackListener processingCallbackListener;
    //Default Constructor
    public YesNoFragment() {
        super();
    }

    public YesNoFragment(String stepId, String question) {
        super(stepId);
        this.question = question;
    }

    /**
     * Inject additional work order processing logic.
     * @param processingCallbackListener
     */
    public void setProcessingCallbackListener(WorkorderAdditionalProcessingCallbackListener processingCallbackListener) {
        this.processingCallbackListener = processingCallbackListener;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_yes_no_radio, null);
        initViews();
        setUpListeners();
        populateView();
        return fragmentView;

    }

    @Override
    public void onResume() {

        super.onResume();
    }

    /**
     * Set up listeners
     */
    protected void setUpListeners() {


    }

    public void populateView() {

        super.populateView();
        questionTextView.setText(question);
        try {
            //========== Populate previously selected value
            if (!stepfragmentValueArray.isEmpty()) {
                if ("0".equalsIgnoreCase(stepfragmentValueArray.get("YESNOSTATUS").toString()))
                    radioYesNoGroup.check(R.id.yes);
                else if ("1".equalsIgnoreCase(stepfragmentValueArray.get("YESNOSTATUS").toString()))
                    radioYesNoGroup.check(R.id.no);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : populateView() ", e);
        }
    }


    protected void initViews() {
        questionTextView = fragmentView.findViewById(R.id.question);
        radioYesNoGroup = fragmentView.findViewById(R.id.yesNo);

    }

    public void showPromptUserAction() {
        /*
         *  If status is false then it is coming from flow engine stack rebuild
         *  and does not require user attention
         */

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
                    alertView, getString(R.string.atleast_one_option_should_be_selected), null);
            dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * The method would be called
     * to know if user has selected any option (return true)
     * or just has clicked next without any selection (return false)
     *
     * @return boolean
     */
    @Override
    public boolean validateUserInput() {

           int radioButtonID = radioYesNoGroup.getCheckedRadioButtonId();
           View radioButton = radioYesNoGroup.findViewById(radioButtonID);
           int idx = radioYesNoGroup.indexOfChild(radioButton);
           yesNoValue = String.valueOf(idx);
           int selectedId = radioYesNoGroup.getCheckedRadioButtonId();
           // find the radiobutton by returned id

           radioYesNoButton = fragmentView.findViewById(selectedId);

            if (selectedId == -1)
                return false;
            else {
            	 if(idx ==1)
                     AbstractWokOrderActivity.isPhotoMandatory=false;
                stepfragmentValueArray.put("YESNOSTATUS", yesNoValue);
                applyChangesToModifiableWO();
                return true;
            }

    }

    @Override
    public void applyChangesToModifiableWO() {
        try{
        if (processingCallbackListener != null) {
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            processingCallbackListener.process(wo, stepfragmentValueArray);
        }
        }catch(Exception e) {
            writeLog(TAG+"  : applyChangesToModifiableWO() " ,e);
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

}
