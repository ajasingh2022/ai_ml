package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class MeterNotAccessibleFragment extends CustomFragment implements View.OnClickListener{

    private transient RadioButton okbuttonmtrntaccess = null;
    private transient RadioButton nobuttonmtrntaccess = null;
    private transient RadioButton unknowbtnmtrntaccess = null;
    private transient RadioButton radiobuttonmtr=null;

    transient AtomicBoolean techAccessible = null;
    transient AtomicBoolean techNotAccessible = null;
    transient AtomicBoolean techUnknown = null;
    private String userSelection="USER_SELECTION";
    private static String TAG = MeterNotAccessibleFragment.class.getSimpleName();

    public RadioGroup getRadiogroupmtr() {
        return radiogroupmtr;
    }

    private transient RadioGroup radiogroupmtr=null;
    private transient String selectedText;

    public MeterNotAccessibleFragment ()
    {
        super();
    }

    public MeterNotAccessibleFragment(String stepIdentifier) {
        super(stepIdentifier);
    }



    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView =  inflater.inflate(R.layout.fragment_meter_not_accessible_layout, null);
        populateView();
        return fragmentView;
    }

    @Override
    protected void populateView() {

        super.populateView();
        okbuttonmtrntaccess = fragmentView.findViewById(R.id.okbuttonmtrntaccess);
        nobuttonmtrntaccess = fragmentView.findViewById(R.id.nobuttonmtrntaccess);
        radiogroupmtr = fragmentView.findViewById(R.id.radioGroupmtrntaccess);

        radiogroupmtr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == group.getChildAt(0).getId())
                    stepfragmentValueArray.put(userSelection,"YES");
                else
                if (checkedId == group.getChildAt(1).getId())
                    stepfragmentValueArray.put(userSelection,"NO");

            }
        });

        //========== Populate previously selected value
        if (!stepfragmentValueArray.isEmpty())
        {
            if ("YES".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(userSelection)))){
                radiogroupmtr.check(radiogroupmtr.getChildAt(0).getId());
            }
            else if ("NO".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(userSelection)))){
                radiogroupmtr.check(radiogroupmtr.getChildAt(1).getId());
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();


        // Pre-fetch the view widgets






    }




    /**
     * User has not selected any option
     * and tries to move forward, user should be prompted
     * to select an option
     */
    public void showPromptUserAction() {
        try{
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
        } catch (Exception e) {
            writeLog(TAG + "  : showPromptUserAction() ", e);
        }
    }

    @Override
    public boolean validateUserInput() {
        if (radiogroupmtr.getCheckedRadioButtonId() == -1)
        return false;
        else
        {
            applyChangesToModifiableWO();
            return true;
        }

    }

    @Override
    public void applyChangesToModifiableWO() {
        try{

        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        // Update access to technician information
        if (wo.getWorkorderCustomTO() != null
                && wo.getWorkorderCustomTO().getWoInst() != null
                && wo.getWorkorderCustomTO().getWoInst().getWoInstTO() != null) {
            wo.getWorkorderCustomTO().getWoInst().getWoInstTO().setAccessibleToTechnicianV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
            wo.getWorkorderCustomTO().getWoInst().
                    getWoInstTO().setAccessibleToTechnicianD((techAccessible != null && techAccessible.get()) ? CONSTANTS().SYSTEM_BOOLEAN_T__TRUE : CONSTANTS().SYSTEM_BOOLEAN_T__FALSE);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
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
}