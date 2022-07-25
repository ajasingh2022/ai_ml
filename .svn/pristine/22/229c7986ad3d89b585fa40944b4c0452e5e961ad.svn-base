package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;


/**
 * Created by yhegde on 3/2/2018.
 */

@SuppressLint("ValidFragment")
public class ConfirmDisconnectionServiceLineFragment extends CustomFragment implements View.OnClickListener {


    /**
     * Drawables for quick reference
     */
    private transient Drawable positiveEnabled = null;
    private transient Drawable defaultBtn = null;

    private transient Dialog dialog = null;
    protected boolean isConfirmed = false;

    private Button mOKBtn;
    

    public ConfirmDisconnectionServiceLineFragment() {
        super();
    }

    public ConfirmDisconnectionServiceLineFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_confirm_discon_serviceline, null);
        return fragmentView ;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePageValues();
        initialize();
        setupListeners();
    }


    /**
     * Lookup and initialize all the view widgets
     */
    private void initialize() {
        mOKBtn = fragmentView.findViewById(R.id.btn_ok);
        // Initialize the drawables for quick reference

        if(isConfirmed){
            mOKBtn.setBackgroundResource(R.drawable.ok_enabled);
        }
    }


    private void setupListeners() {
        mOKBtn.setOnClickListener(this);
    }

    public boolean validateUserInput() {
        boolean status = false;
        if (isConfirmed) {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.CONFIRM_DISCONNECTION_OK_BTN, "YES");
            return true;
        }
        return status;
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
    public void initializePageValues() {
        if (stepfragmentValueArray != null) {
            isConfirmed = "YES".equalsIgnoreCase((String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.CONFIRM_DISCONNECTION_OK_BTN));
        }
    }

    @Override
    public void onClick(final View v) {

        if (v.getId() == R.id.btn_ok) {
            if (isConfirmed) {
                mOKBtn.setBackgroundResource(R.drawable.ok_disabled);
                isConfirmed = false;
            } else {
                mOKBtn.setBackgroundResource(R.drawable.ok_enabled);
                isConfirmed = true;
            }
        } else if (v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}