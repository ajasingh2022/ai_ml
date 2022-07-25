package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.InstElNsTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.InstMupConcPlmtTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by yhegde on 2/16/2018.
 */

@SuppressLint("ValidFragment")
public class ConfirmNetStationInstallationLocationPageFragment extends CustomFragment implements  View.OnClickListener,AdapterView.OnItemSelectedListener {

    protected static final String USER_SELECTED_REMARK_VALUE = "USER_SELECTED_REMARK_VALUE";
    protected static final String USER_SELECTED_PLACEMENT_VALUE = "USER_SELECTED_PLACEMENT_VALUE";
    private static String TAG = ConfirmNetStationInstallationLocationPageFragment.class.getSimpleName();
    protected transient TextView tvNetStationType = null;
    protected transient Spinner spnRemarkSpinner = null;
    protected transient Spinner spnPlacementSpinner = null;


    public ConfirmNetStationInstallationLocationPageFragment(String stepId) {
        super(stepId);}

    public ConfirmNetStationInstallationLocationPageFragment() {
        super();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_confirm_installation_location, null);
        initializeUI();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    /**
     * Initialize the UI components
     */
    protected void initializeUI() {
        final View parentView = fragmentView;
        if(null != parentView){
            tvNetStationType = parentView.findViewById(R.id.NetStationType);
            spnRemarkSpinner = parentView.findViewById(R.id.remarkSpinner);
            spnPlacementSpinner = parentView.findViewById(R.id.placementSpinner);
        }
    }

    /**
     * Populate UI components with data
     */
    protected void populateData() {
        try{
        WorkorderCustomWrapperTO woModifiable = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        //Set NetStation Type
        if(woModifiable.getWorkorderCustomTO().getWoInst().getInstElectrical().getWoInstElTO().getIdInstElNsTO() != null) {
            tvNetStationType.setText(ObjectCache.getTypeName(InstElNsTTO.class,
                    woModifiable.getWorkorderCustomTO().getWoInst().getInstElectrical().getWoInstElTO().getIdInstElNsTO()));
        }

        GuIUtil.setUpSpinner(getActivity(), spnRemarkSpinner, InstElNsTTO.class,true, this);

        if (stepfragmentValueArray.get(USER_SELECTED_REMARK_VALUE)!= null)

            if (GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_REMARK_VALUE)))) {
                spnRemarkSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(spnRemarkSpinner,
                        Long.valueOf(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_REMARK_VALUE)))));
            }


        GuIUtil.setUpSpinner(getActivity(), spnPlacementSpinner, TypeDataUtil.filterTypeDataListByDomain(ObjectCache.getAllTypes(InstMupConcPlmtTTO.class),
                WorkorderUtils.getDomainsForWO(woModifiable)), true, this);

        if (stepfragmentValueArray.get(USER_SELECTED_PLACEMENT_VALUE) != null)
        if(GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_PLACEMENT_VALUE)))) {
            spnPlacementSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(spnPlacementSpinner,
                    Long.valueOf(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_PLACEMENT_VALUE)))));
        }
        } catch (Exception e) {
            writeLog(TAG + ":populateData()", e);
        }
    }

    /**
     * Save the user selected values into modifiable WO object
     */
    @Override
    public void applyChangesToModifiableWO() {
        try{
        WorkorderCustomWrapperTO woModifiable = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        if(GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_REMARK_VALUE)))) {
            woModifiable.getWorkorderCustomTO().getWoInst().getInstElectrical().getWoInstElTO().setIdInstElNsTD(Long.valueOf(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_REMARK_VALUE))));
            woModifiable.getWorkorderCustomTO().getWoInst().getInstElectrical().getWoInstElTO().setIdInstElNsTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
        }

        if(GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_PLACEMENT_VALUE)))) {
            woModifiable.getWorkorderCustomTO().getWoInst().getInstMultipoint().getWoInstMupTO().setIdInstMupConcPlmtTD(Long.valueOf(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_PLACEMENT_VALUE))));
            woModifiable.getWorkorderCustomTO().getWoInst().getInstMultipoint().getWoInstMupTO().setIdInstMupConcPlmtTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
        }
        } catch (Exception e) {
            writeLog(TAG + ":applyChangesToModifiableWO()", e);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Get user selected values from spinner

        if(parent.getId() == R.id.remarkSpinner) {
            stepfragmentValueArray.put(USER_SELECTED_REMARK_VALUE, String.valueOf(id));
        }
        else if(parent.getId() == R.id.placementSpinner) {
            stepfragmentValueArray.put(USER_SELECTED_PLACEMENT_VALUE, String.valueOf(id));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

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
                    alertView, null, null);
            dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v != null){
            if (v.getId() == R.id.okButtonYesNoPage && dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public boolean validateUserInput() {
        if(!GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_REMARK_VALUE)))
                || !GuIUtil.validSpinnerSelection(String.valueOf(stepfragmentValueArray.get(USER_SELECTED_PLACEMENT_VALUE)))){
            return false;
        }
        else
            applyChangesToModifiableWO();
        return true;
    }
}
