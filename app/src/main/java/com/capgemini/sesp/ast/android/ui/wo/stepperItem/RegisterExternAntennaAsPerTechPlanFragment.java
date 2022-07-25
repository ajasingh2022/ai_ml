package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class RegisterExternAntennaAsPerTechPlanFragment extends CustomFragment implements CompoundButton.OnCheckedChangeListener {

    private transient LinearLayout newExternAntennaLayout = null;
    private transient CheckBox newExtrnAntennaCb = null;
    private transient LinearLayout changeExternAntennaLayout = null;
    private transient CheckBox changeExtrnAntennaCb = null;
    private transient LinearLayout updateExternAntennaLayout = null;
    private transient CheckBox updateExtrnAntennaCb = null;

    private transient TextView existingTechPlanAntennaTv = null;
    private transient TextView existingModelTv = null;
    static String TAG = RegisterExternAntennaAsPerTechPlanFragment.class.getSimpleName();
    transient String existingModel;
    transient String antenaInTechplan;
    transient int antennaChoice;// 1 - NEW ANTENNA; 2 - CHANGE ANTENNA; 3 - UPDATE ANTENNA
    private transient int antennaChoiceAtStart;
    transient WoUnitCustomTO existingAntenna;

    public RegisterExternAntennaAsPerTechPlanFragment() {
        super();
    }

    public RegisterExternAntennaAsPerTechPlanFragment(String stepIdentifier) {
        super(stepIdentifier);
    }


    public String evaluateNextPage() {
        String nextPage = null;
        try {
            if (antennaChoice == 1 || antennaChoice == 2 || antennaChoice == 3) {
                getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.USER_SELECTION_EXTERNAL_ANTENA, antennaChoice);
                getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.ANTENNA_MODEL_NAME, antenaInTechplan);
                getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.UNIT_MODEL_NAME, existingModel);
                nextPage = ConstantsAstSep.FlowPageConstants.NEXT_PAGE_NEW_EXTERNAL_ANTENNA;
            } else {
                nextPage = ConstantsAstSep.FlowPageConstants.NEXT_PAGE;
            }
        } catch (Exception e) {
            writeLog(TAG + "  : evaluateNextPage() ", e);
        }
        return nextPage;
        //return String.valueOf(antennaChoice);
    }

    public void initializePageValues() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            existingAntenna = UnitInstallationUtils.getUnit(wo,
                    CONSTANTS().WO_UNIT_T__ANTENNA,
                    CONSTANTS().WO_UNIT_STATUS_T__EXISTING);

            String userSelectedExternAntennaChoice = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.USER_SELECTION_EXTERNAL_ANTENA);
            antennaChoice = Utils.isNotEmpty(userSelectedExternAntennaChoice) ? Integer.parseInt(userSelectedExternAntennaChoice) : -1;

            final Map<String, String> antelModelNNameMap = UnitInstallationUtils.getExistingModelNames(wo, CONSTANTS().WO_UNIT_T__ANTENNA);
            existingModel = antelModelNNameMap.get(ConstantsAstSep.FlowPageConstants.UNIT_MODEL_NAME);
            antenaInTechplan = antelModelNNameMap.get(ConstantsAstSep.FlowPageConstants.ANTENNA_MODEL_NAME);
            antennaChoiceAtStart = antennaChoice;
        } catch (Exception e) {
            writeLog(TAG + "  : initializePageValues() ", e);
        }
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_register_existing_antenna, null);
        initializePageValues();
        initialize();
        populateData();
        return fragmentView;
    }

    public void getUserChoiceValuesForPage() {
        try {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.USER_SELECTION_EXTERNAL_ANTENA, String.valueOf(antennaChoice));
        } catch (Exception e) {
            writeLog(TAG + "  : getUserChoiceValuesForPage() ", e);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void initialize() {
        try{
        final View parentView = fragmentView;
        if (parentView != null) {
            newExtrnAntennaCb = parentView.findViewById(R.id.newExtrnAntennaCb);
            newExtrnAntennaCb.setOnCheckedChangeListener(this);

            changeExtrnAntennaCb = parentView.findViewById(R.id.changeExtrnAntennaCb);
            changeExtrnAntennaCb.setOnCheckedChangeListener(this);

            updateExtrnAntennaCb = parentView.findViewById(R.id.updateExtrnAntennaCb);
            updateExtrnAntennaCb.setOnCheckedChangeListener(this);

            existingTechPlanAntennaTv = parentView.findViewById(R.id.existingTechPlanAntennaTv);
            existingModelTv = parentView.findViewById(R.id.existingModelTv);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : initialize() ", e);
        }
    }

    private void populateData() {
        if (existingAntenna != null) {

            newExtrnAntennaCb.setEnabled(false);

            changeExtrnAntennaCb.setEnabled(true);

            updateExtrnAntennaCb.setEnabled(true);

        }

        switch (antennaChoice) {
            case 1:
                newExtrnAntennaCb.setChecked(true);
                break;
            case 2:
                changeExtrnAntennaCb.setChecked(true);
                break;
            case 3:
                updateExtrnAntennaCb.setChecked(true);
                break;
        }

        if (antenaInTechplan != null) {
            existingTechPlanAntennaTv.setText(antenaInTechplan);
        }
        if (existingModel != null) {
            existingModelTv.setText(existingModel);
        }
    }

    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.newExtrnAntennaCb) {
                antennaChoice = 1;
            } else if (buttonView.getId() == R.id.changeExtrnAntennaCb) {
                antennaChoice = 2;
                updateExtrnAntennaCb.setChecked(false);
            } else if (buttonView.getId() == R.id.updateExtrnAntennaCb) {
                antennaChoice = 3;
                changeExtrnAntennaCb.setChecked(false);
            }
        }
        if (!isChecked) {
            if (!newExtrnAntennaCb.isChecked() && !changeExtrnAntennaCb.isChecked() && !updateExtrnAntennaCb.isChecked()) {
                antennaChoice = 0;
            }
        }
    }

    @Override
    public void showPromptUserAction() {
        //Do nothing
    }


    public boolean validateUserInput() {
        getUserChoiceValuesForPage();

        return true;
    }

    @Override
    public void applyChangesToModifiableWO() {
        super.applyChangesToModifiableWO();
        if (antennaChoice == 1 || antennaChoice == 2 || antennaChoice == 3) {
            getActivity().getIntent().putExtra(ConstantsAstSep.FlowPageConstants.USER_SELECTION_EXTERNAL_ANTENA, antennaChoice);
        }
        else
        {
            getActivity().getIntent().removeExtra(ConstantsAstSep.FlowPageConstants.USER_SELECTION_EXTERNAL_ANTENA);
        }
    }
}