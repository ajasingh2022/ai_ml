package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.activity.flow.SESPFlowActivity;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTO;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.isNetworkAvailable;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint({"ValidFragment","InflateParams"})
public class OnlineVerificationPageFragment extends CustomFragment implements View.OnClickListener {

    private transient Dialog alertDialog = null;
    private transient LinearLayout onlineVerificationView = null;
    private transient LinearLayout noUnitsMountedLayout = null;
    private transient Dialog dialog = null;
    private List<UnitViewComponents> unitViews;

    protected transient ArrayMap<String,Boolean> verificationStatusMap = null;
    protected transient ArrayMap<String,Boolean> verificationStatusLatest = null;

    protected transient List<WoUnitCustomTO> unitsToVerify = null;
    protected transient boolean noMeter = true;
    private transient boolean noProduct=true;
    public OnlineVerificationPageFragment(){
        super();
    }

    public OnlineVerificationPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }


    public String evaluateNextPage() {
        String nextPage;
        if(noMeter){
            nextPage = ConstantsAstSep.FlowPageConstants.NEXT_PAGE_NO_METER;
        }
        else if(!noProduct){
            nextPage=ConstantsAstSep.FlowPageConstants.NEXT_PAGE_PRODUCT;
        }
        else{
            nextPage = ConstantsAstSep.FlowPageConstants.NEXT_PAGE;
        }
        if(nextPage != null) {
        }
        return nextPage;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
   fragmentView = inflater.inflate(R.layout.flow_fragment_online_verification, null);
        initializePageValues();
        initializeUI();
        populateData();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    protected void initializeUI(){
        onlineVerificationView = fragmentView.findViewById(R.id.onlineVerificationView);
        noUnitsMountedLayout = fragmentView.findViewById(R.id.noUnitsMountedLayout);
        onlineVerificationView.removeAllViews();
    }
    public void initializePageValues() {
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        unitsToVerify = UnitInstallationUtils.getUnits(wo, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
        verificationStatusMap = new ArrayMap<String, Boolean>();
        verificationStatusLatest = new ArrayMap<String, Boolean>();
        noMeter = true;
        if(WorkorderUtils.isCollectionProductExist(wo)){
            noProduct=false;
        }
        Iterator<WoUnitCustomTO> woUnitCustomTOIterator = unitsToVerify.iterator();
        while (woUnitCustomTOIterator.hasNext()) {
            WoUnitCustomTO tempUnit = woUnitCustomTOIterator.next();
            if(CONSTANTS().WO_UNIT_T__ANTENNA.equals(tempUnit.getIdWoUnitT())){
                woUnitCustomTOIterator.remove();
            }
        }
        for (WoUnitCustomTO tempUnitCustomTo : unitsToVerify) {
            if(tempUnitCustomTo.getIdWoUnitT().equals(CONSTANTS().WO_UNIT_T__METER)) {
                noMeter = false;
            }
            if ("YES".equalsIgnoreCase((String)stepfragmentValueArray.get(getUnitIdentifierValue(tempUnitCustomTo)))) {
                verificationStatusMap.put(getUnitIdentifierValue(tempUnitCustomTo), new Boolean(true));
            }else if("NO".equalsIgnoreCase((String)stepfragmentValueArray.get(getUnitIdentifierValue(tempUnitCustomTo)))){
                verificationStatusMap.put(getUnitIdentifierValue(tempUnitCustomTo), new Boolean(false));
            }
        }

        verificationStatusLatest = new ArrayMap(verificationStatusMap);
    }
    protected String getUnitIdentifierValue(WoUnitCustomTO unitCustomTo) {
        return UnitInstallationUtils.getUnitIdentifierValue(unitCustomTo);
    }
    protected void populateData(){
        try{
        //Dynamically load Unit Verification Page
        if(unitsToVerify == null || unitsToVerify.isEmpty()) {
            noUnitsMountedLayout.setVisibility(View.VISIBLE);
        } else {

            unitViews = new ArrayList<UnitViewComponents>();
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Iterator<WoUnitCustomTO> woUnitCustomTOIterator = unitsToVerify.iterator();
            while (woUnitCustomTOIterator.hasNext()) {
                WoUnitCustomTO tempUnit = woUnitCustomTOIterator.next();
                if (CONSTANTS().WO_UNIT_T__ANTENNA.equals(tempUnit.getIdWoUnitT())) {
                    continue;
                }
                View view = inflater.inflate(R.layout.layout_unit_verification, null);
                onlineVerificationView.addView(view);
                boolean verificationStatus = false;
                if (verificationStatusMap.get(getUnitIdentifierValue(tempUnit)) != null &&
                        verificationStatusMap.get(getUnitIdentifierValue(tempUnit)) == true) {
                    verificationStatus = true;
                }
                UnitViewComponents unitView = new UnitViewComponents(view, tempUnit, verificationStatus);
                if (CONSTANTS().WO_UNIT_T__METER.equals(tempUnit.getIdWoUnitT())) {
                    unitView.unitTypeTv.setText(getResources().getString(R.string.meter));
                } else if (CONSTANTS().WO_UNIT_T__CONCENTRATOR.equals(tempUnit.getIdWoUnitT())) {
                    unitView.unitTypeTv.setText(getResources().getString(R.string.concentrator));
                } else if (CONSTANTS().WO_UNIT_T__REPEATER.equals(tempUnit.getIdWoUnitT())) {
                    unitView.unitTypeTv.setText(getResources().getString(R.string.repeater));
                } else if (CONSTANTS().WO_UNIT_T__SIMCARD.equals(tempUnit.getIdWoUnitT())) {
                    unitView.unitTypeTv.setText(getResources().getString(R.string.sim_card));
                } else if (CONSTANTS().WO_UNIT_T__CM.equals(tempUnit.getIdWoUnitT())) {
                    unitView.unitTypeTv.setText(getResources().getString(R.string.com_module));
                } else if (CONSTANTS().WO_UNIT_T__CURRENT_TRAFO.equals(tempUnit.getIdWoUnitT())) {
                    unitView.unitTypeTv.setText(getResources().getString(R.string.current_tranformer));
                } else if (CONSTANTS().WO_UNIT_T__VOLTAGE_TRAFO.equals(tempUnit.getIdWoUnitT())) {
                    unitView.unitTypeTv.setText(getResources().getString(R.string.voltage_tranformer));
                } else if (CONSTANTS().WO_UNIT_T__MODEM.equals(tempUnit.getIdWoUnitT())) {
                    unitView.unitTypeTv.setText(getResources().getString(R.string.modem));
                } else if (CONSTANTS().WO_UNIT_T__TERMINAL.equals(tempUnit.getIdWoUnitT())) {
                    unitView.unitTypeTv.setText(getResources().getString(R.string.terminal));
                }
                unitView.newUnitNumTv.setText(getUnitIdentifierValue(tempUnit));
                ((SESPFlowActivity) getActivity()).regForNetStatChangeInfo(unitView);
                unitViews.add(unitView);
            }
        }
        } catch ( Exception e){
            writeLog("OnlineVerificationPageFragment : populateData()", e);
        }
    }
    public Map<String, String> getUserChoiceValuesForPage() {
        Map<String,String> userChoiceMap = new ArrayMap<String, String>();
        try{
        if(unitsToVerify != null){
            for (WoUnitCustomTO tempUnitCustomTo : unitsToVerify) {
                if(verificationStatusLatest.get(getUnitIdentifierValue(tempUnitCustomTo)) != null){
                    if(verificationStatusLatest.get(getUnitIdentifierValue(tempUnitCustomTo))){
                        userChoiceMap.put(getUnitIdentifierValue(tempUnitCustomTo),"YES");
                    }else{
                        userChoiceMap.put(getUnitIdentifierValue(tempUnitCustomTo),"NO");
                    }
                }
            }
        }
        } catch ( Exception e){
            writeLog("OnlineVerificationPageFragment : getUserChoiceValuesForPage()", e);
        }
        return userChoiceMap;
    }
    @Override
    public void onStop(){
        super.onStop();
        if(unitViews != null && !unitViews.isEmpty()) {
            for (UnitViewComponents unitViewComponent : unitViews)
                ((SESPFlowActivity) getActivity()).unregForNetStatChangeInfo(unitViewComponent);
        }
    }

    @Override
    public void onClick(final View v) {
        if (v!=null
                && v.getId()==R.id.okButtonYesNoPage
                && dialog!=null){
            dialog.dismiss();
        }
    }

    /**
     * Validate whether user has selected
     * a choice
     *
     * @return boolean
     */
    public boolean validateUserInput(){

        getUserChoiceValuesForPage();
        boolean status = false;
        if(unitViews != null && !unitViews.isEmpty())
            for(UnitViewComponents unitView : unitViews) {
                status = unitView.skipped || (!unitView.verificationInProgress && unitView.validationSuccessful);
                if(!status) break;
            }
        return status;
    }
    @Override
    public void onStart() {
        super.onStart();
        initializeUI();
        populateData();
    }


    /**
     * User has not selected any option
     * and tries to move forward, user should be prompted
     * to select an option
     *
     */
    public void showPromptUserAction(){
			/*
			 *  If status is false then it is coming from flow engine stack rebuild
			 *  and does not require user attention
			 */
        final View alertView
                = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.prompt_user_response_layout, null);
        if(alertView!=null){
            final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
            okButton.setOnClickListener(this);

            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                    alertView, getResources().getString(R.string.one_or_more_units_have_invalid_status), null);
            dialog = builder.create();
            dialog.show();
        }
    }

    private class UnitViewComponents implements View.OnClickListener, View.OnTouchListener,
            NetworkStatusCallbackListener {

        private transient TextView skippableOrNotTV = null;
        private transient TextView newUnitNumTv = null;
        private transient TextView unitTypeTv = null;
        private transient Button onlineVerificationBtn = null;
        private transient Drawable roundedCornerButtonPositiveEnabled = null;
        private transient Drawable roundedCornerButtonDisabled = null;
        private transient boolean verificationInProgress =  false;
        private transient boolean validationSuccessful = false;
        final Resources resources = ApplicationAstSep.context.getResources();
        private transient boolean skipped = false;
        private WoUnitCustomTO woUnitTO = null;


        public UnitViewComponents(View parentView,WoUnitCustomTO woUnitTO,boolean validationrslt) {
            // Initialise UI view components
            newUnitNumTv = parentView.findViewById(R.id.newUnitNumTv);
            unitTypeTv = parentView.findViewById(R.id.unitTypeTv);
            validationSuccessful=validationrslt;
            onlineVerificationBtn = parentView.findViewById(R.id.onlineVerificationBtn);

            skippableOrNotTV = parentView.findViewById(R.id.skippableornot);

            skippableOrNotTV.setText("You can continue without verification since the app is in offline mode");
            skippableOrNotTV.setTextColor(getResources().getColor(R.color.colorAccent));
            if(SessionState.getInstance().isLoggedInOnline()) {
                skippableOrNotTV.setText("Mandatory to Perform");
                skippableOrNotTV.setTextColor(getResources().getColor(R.color.colorHighlight));
            }


            skipped = !SessionState.getInstance().isLoggedInOnline();

            //Set up Listeners
            onlineVerificationBtn.setOnClickListener(this);
            onlineVerificationBtn.setOnTouchListener(this);
            networkStatusChanged(isNetworkAvailable());
            this.woUnitTO = woUnitTO;
        }

        @Override
        public void onClick(View v) {
            try{
            if(v!=null && v.getId()==R.id.onlineVerificationBtn){
                skipped = false;
                if(!verificationInProgress){
                    if(AndroidUtilsAstSep.isNetworkAvailable()){
                        verificationInProgress = true;
                        //Toast.makeText(getContext(), getString(R.string.performing_verification), Toast.LENGTH_SHORT).show();
                        onlineVerificationBtn.setText(resources.getString(R.string.performing_verification));
                        onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorAccent));
                        Drawable verification= resources.getDrawable(R.drawable.ic_done_24dp);
                        onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verification,null,null,null);

                        new FetchUnitStatusThread(getActivity(), woUnitTO).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        showOnlineVerificationNotPossible();
                    }

                }
            }  else if (v!=null && v.getId()==R.id.closeOfflineWoWarningBtn){

                alertDialog.dismiss();
            }
            } catch ( Exception e){
                writeLog("OnlineVerificationPageFragment : onClick()", e);
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                if(v.getId()==R.id.onlineVerificationBtn){

                    //onlineVerificationBtn.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                    //onlineVerificationBtn.setEnabled(false);

                }
            } else if (event.getAction()==MotionEvent.ACTION_UP){
                v.performClick();
            }
            return true;
        }

        /**
         * Whenever there is change in network connection status
         * this method would be called and the online verification
         * enable/disable functionality would be triggered.
         *
         */
        @Override
        public void networkStatusChanged(boolean isConnected) {
            try{
            if(!isConnected){
                SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
                // Callback received -- network connectivity is lost
                onlineVerificationBtn.setEnabled(false);
                // Faded white text to indicate its disabled
                // onlineVerificationBtn.setTextColor(getResources().getColor(android.R.color.white));
                onlineVerificationBtn.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                // onlineVerificationBtn.setText(resources.getString(R.string.perform_verification));
                skippableOrNotTV.setText("You can continue without verification since the app is in offline mode");
                skipped = true;
                //cancelOnlineVerBtn.setTextColor(getResources().getColor(android.R.color.black));
            } else {
                SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
                onlineVerificationBtn.setEnabled(true);
                //  onlineVerificationBtn.setTextColor(getResources().getColor(android.R.color.black));
                skippableOrNotTV.setText("Mandatory to Perform");
                skipped = false;
                //cancelOnlineVerBtn.setTextColor(getResources().getColor(android.R.color.white));
            }
            } catch ( Exception e){
                writeLog("OnlineVerificationPageFragment : networkStatusChanged()", e);
            }
        }

        /**
         * User is offline hence online verification is not possible.
         *
         * Show informative pop up message and let the user continue with the flow
         *
         */
        private void showOnlineVerificationNotPossible(){
            try{
            final View titleView = getActivity().getLayoutInflater().inflate(R.layout.layout_warning_unfinished_wo_title, null);
            final View bodyView = getActivity().getLayoutInflater().inflate(R.layout.warning_offline_wo_start_confirmation, null);
            ((TextView)bodyView.findViewById(R.id.msgText)).setText(resources.getString(R.string.not_connected_online_verification_nt_possble));

            final ImageButton continueButton = bodyView.findViewById(R.id.continueOfflineStartWoButton);
            final Button closeOfflinePopupBtn = bodyView.findViewById(R.id.closeOfflineWoWarningBtn);
            closeOfflinePopupBtn.setText(resources.getString(R.string.ok));
            continueButton.setVisibility(View.GONE);

            // When clicked on reset button the warning dialog will be closed simply
            if(closeOfflinePopupBtn!=null){
                closeOfflinePopupBtn.setOnClickListener(this);
            }

            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),bodyView,null,null);

            if(builder!=null){
                builder.setCustomTitle(titleView);
                alertDialog=builder.create();
                alertDialog.show();
            } } catch ( Exception e){
                writeLog("OnlineVerificationPageFragment : showOnlineVerificationNotPossible()", e);
            }
        }

        public void validationFinished(UnitInformationTO unitInformationTO){
            int stringId = R.string.verification_failed;
            Drawable verificationFailed = resources.getDrawable(R.drawable.ic_cancel_hollow_24dp);
            onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorRed));
            onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verificationFailed,null,null,null);
            validationSuccessful=false;
            if(unitInformationTO != null) {
                boolean mountable = true;
                mountable &= AndroidUtilsAstSep.CONSTANTS().UNIT_STATUS_T__MOUNTABLE.equals(unitInformationTO.getUnitStatusTTO().getId());

                if(mountable &&
                        (Utils.isEmpty(woUnitTO.getUnitModel()) || unitInformationTO.getUnitModelTO().getCode().equalsIgnoreCase(woUnitTO.getUnitModel()))) {
                    stringId = R.string.verification_ok;
                    //onlineVerificationBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    onlineVerificationBtn.setEnabled(true);
                    Drawable verificationok= resources.getDrawable(R.drawable.ic_done_24dp);
                    onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verificationok,null,null,null);
                    onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorAccent));
                    validationSuccessful = true;

                } else if(!mountable) {
                    stringId = R.string.unit_is_not_mountable;
                    //onlineVerificationBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    onlineVerificationBtn.setEnabled(true);
                } else if(Utils.isNotEmpty(woUnitTO.getUnitModel())
                        && !unitInformationTO.getUnitModelTO().getCode().equalsIgnoreCase(woUnitTO.getUnitModel())) {
                    stringId = R.string.unit_model_mismatch;
                    //onlineVerificationBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    onlineVerificationBtn.setEnabled(true);
                }
                verificationStatusLatest.put(getUnitIdentifierValue(woUnitTO), validationSuccessful);
            } else {
                //onlineVerificationBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                onlineVerificationBtn.setEnabled(true);
            }

            Log.i("validationFinished","value of string is "+getResources().getString(stringId));
            //Toast.makeText(getContext(),getResources().getString(stringId),Toast.LENGTH_SHORT).show();
            onlineVerificationBtn.setText(getResources().getString(stringId));

            onlineVerificationBtn.setEnabled(true);
            verificationInProgress=false;
        }

        private class FetchUnitStatusThread extends AsyncTask<Void, Void, UnitInformationTO> {
            private WoUnitTO woUnitTO;

            public FetchUnitStatusThread(final Context context, final WoUnitTO woUnitTO){
                this.woUnitTO = woUnitTO;
            }

            @Override
            protected UnitInformationTO doInBackground(final Void... params) {
                UnitInformationTO unitInformationTO = null;
                try{
                Long idUnitIdentifierT = woUnitTO.getIdUnitIdentifierT();
                String identifier = UnitInstallationUtils.getUnitIdentifierValue(woUnitTO, idUnitIdentifierT);
                String unitModel = woUnitTO.getUnitModel();
                Log.i(TAG, "UNIT MODEL IDENTIFIER TYPE ID :: " + idUnitIdentifierT);
                Log.i(TAG, "UNIT MODEL IDENTIFIER VALUE :: " + identifier);
                Log.i(TAG, "UNIT MODEL VALUE :: " + unitModel);
                    unitInformationTO = Utils.isNotEmpty(unitModel) ?
                            AndroidUtilsAstSep.getDelegate().getDeviceInfoWithModel(identifier, idUnitIdentifierT, unitModel) :
                            AndroidUtilsAstSep.getDelegate().getDeviceInfo(identifier, idUnitIdentifierT);
                } catch ( Exception e){
                   writeLog("OnlineVerificationPageFragment : doInBackground()", e);
                }
                return unitInformationTO;
            }

            @Override
            public void onPostExecute(final UnitInformationTO unitInformationTO){
                UnitViewComponents.this.validationFinished(unitInformationTO);
            }

        }
    }

}