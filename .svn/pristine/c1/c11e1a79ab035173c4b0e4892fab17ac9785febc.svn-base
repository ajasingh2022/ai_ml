package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.ClientFuseSizeTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.FuseTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.InstMupFuseBracketTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoFuseTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMupTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstElCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint("ValidFragment")
public class RegisterMultipointInfoFusePageFragment extends CustomFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    protected String TAG = this.getClass().getSimpleName();

    protected InstMupFuseBracketTTO userSelectedFuseBracketType = null;
    protected FuseTTO userSelectedFuseType = null;
    protected ClientFuseSizeTTO userSelectedFuseSizeType = null;

    protected Spinner fuseBracketSpinner = null;
    protected Spinner fuseSizeSpinner = null;
    protected Spinner fuseTypeSpinner = null;
    protected transient AlertDialog dialog = null;

    public RegisterMultipointInfoFusePageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_register_multipoint_information_fuse, null);
        initializePageValues();
        initializeUI();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    /**
     * Initialize the UI components
     */
    protected void initializeUI() {
        final View parentView = fragmentView;
        if (null != parentView) {
            fuseBracketSpinner = parentView.findViewById(R.id.fuse_bracket_spinner);
            fuseTypeSpinner = parentView.findViewById(R.id.fuse_type_spinner);
            fuseSizeSpinner = parentView.findViewById(R.id.fuse_size_spinner);
           GuIUtil.setUpSpinner(getActivity(), fuseBracketSpinner, InstMupFuseBracketTTO.class, true, this);
            GuIUtil.setUpSpinner(getActivity(), fuseTypeSpinner, FuseTTO.class, true, this);
            GuIUtil.setUpSpinner(getActivity(), fuseSizeSpinner, ClientFuseSizeTTO.class, true, this);
        }
    }


    @Override
    public void applyChangesToModifiableWO() {
        try {
            WorkorderCustomWrapperTO woModifiable = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();

            //Set Fuse Bracket
            if (userSelectedFuseBracketType != null && !"".equalsIgnoreCase(userSelectedFuseBracketType.getCode())) {
                woModifiable.getWorkorderCustomTO().getWoInst().getInstMultipoint().getWoInstMupTO().setIdInstMupFuseBracketTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woModifiable.getWorkorderCustomTO().getWoInst().getInstMultipoint().getWoInstMupTO().setIdInstMupFuseBracketTD(userSelectedFuseBracketType.getId());
            }

            //1. Set Fuse Size Type
            //2. Set Fuse Type
            WoInstElCustomTO instElCustomTO = woModifiable.getWorkorderCustomTO().getWoInst().getInstElectrical();
            if (instElCustomTO == null) {
                instElCustomTO = new WoInstElCustomTO();
                woModifiable.getWorkorderCustomTO().getWoInst().setInstElectrical(instElCustomTO);
            }
            WoFuseTO woFuseTO = instElCustomTO.getMainFuse();
            if (woFuseTO == null) {
                woFuseTO = new WoFuseTO();
                woModifiable.getWorkorderCustomTO().getWoInst().getInstElectrical().setMainFuse(woFuseTO);
                woFuseTO.setIdCase(woModifiable.getIdCase());
            }
            if (userSelectedFuseSizeType != null
                    && !"".equalsIgnoreCase(userSelectedFuseSizeType.getCode())) {
                woFuseTO.setFuseSizeV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woFuseTO.setFuseSizeD(userSelectedFuseSizeType.getCode());
            }
            if (userSelectedFuseType != null
                    && !"".equalsIgnoreCase(userSelectedFuseType.getCode())) {
                woFuseTO.setIdFuseTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woFuseTO.setIdFuseTD(userSelectedFuseType.getId());
            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }
    }

    @Override
    public void initializePageValues() {
        try {

            WoInstMupTO woInstMupTO = AbstractWokOrderActivity.workorderCustomWrapperTO.getWorkorderCustomTO().getWoInst().getInstMultipoint().getWoInstMupTO();

            //Get Fuse Bracket
            if (stepfragmentValueArray.get("userSelectedFuseBracketType") != null)
                userSelectedFuseBracketType = ObjectCache.getType(InstMupFuseBracketTTO.class, ((Double) stepfragmentValueArray.get("userSelectedFuseBracketType")).longValue());

            // Get Fuse Size Type
            if (stepfragmentValueArray.get("userSelectedFuseSizeType") != null)
                userSelectedFuseSizeType = ObjectCache.getType(ClientFuseSizeTTO.class, ((Double) stepfragmentValueArray.get("userSelectedFuseSizeType")).longValue());

            // Get Fuse Type
            if (stepfragmentValueArray.get("userSelectedFuseType") != null)
                userSelectedFuseType = ObjectCache.getType(FuseTTO.class, ((Double) stepfragmentValueArray.get("userSelectedFuseType")).longValue());



        } catch (Exception e) {
            writeLog(TAG + "  : initializePageValues() ", e);
        }
    }


    @Override
    public void onClick(View v) {
        if (v != null && v.getId() == R.id.okButtonYesNoPage && dialog != null) {
            dialog.dismiss();
        }
    }

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
        try {
            if (stepfragmentValueArray.get("userSelectedFuseBracketType") == null
                    || stepfragmentValueArray.get("userSelectedFuseType") == null
                    || stepfragmentValueArray.get("userSelectedFuseSizeType") == null) return false;
            applyChangesToModifiableWO();
        } catch (Exception e) {
            writeLog(TAG + "  : validateUserInput() ", e);
        }
        return true;

    }

    /**
     * Populate data in UI
     */
    protected void populateData() {
        try {

            if (userSelectedFuseType != null) { // Select Fuse Type
                Log.d(TAG, "Fuse Type" + userSelectedFuseType.getName());
                fuseTypeSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(fuseTypeSpinner, new DisplayItem(userSelectedFuseType)));
            }

            if (userSelectedFuseBracketType != null) { // Select Fuse Bracket Type
                Log.d(TAG, "Fuse Bracket Type" + userSelectedFuseBracketType.getName());
                fuseBracketSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(fuseBracketSpinner, new DisplayItem(userSelectedFuseBracketType)));
            }
            if (userSelectedFuseSizeType != null) { // Select Fuse Size Type
                Log.d(TAG, "Fuse Size Type" + userSelectedFuseSizeType.getName());
                fuseSizeSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(fuseSizeSpinner, new DisplayItem(userSelectedFuseSizeType)));
            }
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            //Get user selected values
            if (parent.getId() == R.id.fuse_bracket_spinner) {
                if ((!((DisplayItem) fuseBracketSpinner.getSelectedItem()).getId().equals(-1L))) {
                    userSelectedFuseBracketType = (InstMupFuseBracketTTO) ((DisplayItem) fuseBracketSpinner.getSelectedItem()).getUserObject();
                   stepfragmentValueArray.put("userSelectedFuseBracketType", ((DisplayItem) fuseBracketSpinner.getSelectedItem()).getId());
                } else
                    userSelectedFuseBracketType = null;
            } else if (parent.getId() == R.id.fuse_size_spinner) {
                if ((!((DisplayItem) fuseSizeSpinner.getSelectedItem()).getId().equals(-1L))) {
                    userSelectedFuseSizeType = (ClientFuseSizeTTO) ((DisplayItem) fuseSizeSpinner.getSelectedItem()).getUserObject();
                   stepfragmentValueArray.put("userSelectedFuseSizeType", ((DisplayItem) fuseSizeSpinner.getSelectedItem()).getId());
                } else
                    userSelectedFuseSizeType = null;
            } else if (parent.getId() == R.id.fuse_type_spinner) {
                if ((!((DisplayItem) fuseTypeSpinner.getSelectedItem()).getId().equals(-1L))) {
                    userSelectedFuseType = (FuseTTO) ((DisplayItem) fuseTypeSpinner.getSelectedItem()).getUserObject();
                   stepfragmentValueArray.put("userSelectedFuseType", ((DisplayItem) fuseTypeSpinner.getSelectedItem()).getId());
                } else
                    userSelectedFuseType = null;
            }
        } catch (Exception e) {
            writeLog(TAG + "  : onItemSelected() ", e);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}