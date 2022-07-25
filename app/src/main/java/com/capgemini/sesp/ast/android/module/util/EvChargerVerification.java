package com.capgemini.sesp.ast.android.module.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.OnlineVerificationPageFragment;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitAdditionalDataTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTO;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class EvChargerVerification implements NetworkStatusCallbackListener {
    protected transient ArrayMap<String,Boolean> verificationStatusMap = null;
    protected transient ArrayMap<String,Boolean> verificationStatusLatest = null;
    private boolean validationSuccessful = false;
    private static String TAG = EvChargerVerification.class.getSimpleName();
     WoUnitCustomTO woUnitCustomTO;
    Button onlineVerificationBtn;
    TextView msgTxt;
    private WoUnitCustomTO woUnitTO = null;
    Resources resources = ApplicationAstSep.context.getResources();
    View view;
    protected static final String VALIDATION_STATUS = "VALIDATION_STATUS";
    private boolean verificationInProgress;
    public UnitInformationTO unitInformationTO;
    public EvChargerVerification(UnitInformationTO unitInformationTO) {
        this.unitInformationTO = unitInformationTO;

    }

    @Override
    public void networkStatusChanged(boolean isConnected) {
        if (!isConnected) {
            SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
            msgTxt.setText(resources.getString(R.string.request_timeout_message));
        }
    }
    protected String getUnitIdentifierValue(WoUnitCustomTO unitCustomTo) {
        return UnitInstallationUtils.getUnitIdentifierValue(unitCustomTo);
    }
    public void initializeView(View parentview)
    {
        onlineVerificationBtn = parentview.findViewById(R.id.evChargerVerificationBtn);
        msgTxt = parentview.findViewById(R.id.msgText);
    }

    public void unitVerfication(View parentView, WoUnitCustomTO woUnitCustomTO) {
        this.woUnitCustomTO = woUnitCustomTO;
        this.woUnitTO = woUnitCustomTO;

        initializeView(parentView);
        msgTxt.setVisibility(View.INVISIBLE);
        onlineVerificationBtn.setText(resources.getString(R.string.performing_verification));
        onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorAccent));
        Drawable verification = resources.getDrawable(R.drawable.ic_done_24dp);
        onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verification, null, null, null);
        verificationStatusMap = new ArrayMap<>();
        verificationStatusLatest = new ArrayMap<>();
        new FetchUnitStatusThread(this.woUnitCustomTO).execute();
    }

    public void validationFinished(UnitInformationTO unitInformationTO){
        int stringId = R.string.verification_failed;
        Drawable verificationFailed = resources.getDrawable(R.drawable.ic_cancel_hollow_24dp);
        onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorRed));
        onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verificationFailed,null,null,null);
        validationSuccessful = false;
        if(unitInformationTO != null) {
            boolean mountable = true;
            mountable &= AndroidUtilsAstSep.CONSTANTS().UNIT_STATUS_T__MOUNTABLE.equals(unitInformationTO.getUnitStatusTTO().getId());

            if(mountable &&
                    (Utils.isEmpty(woUnitTO.getUnitModel()) || unitInformationTO.getUnitModelTO().getCode().equalsIgnoreCase(woUnitTO.getUnitModel()))) {
                stringId = R.string.verification_ok;
                onlineVerificationBtn.setEnabled(true);
                Drawable verificationok= resources.getDrawable(R.drawable.ic_done_24dp);
                onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verificationok,null,null,null);
                onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorAccent));
                validationSuccessful = true;
                verificationStatusMap.put(VALIDATION_STATUS,true);

            } else if(!mountable) {
                stringId = R.string.unit_is_not_mountable;
                onlineVerificationBtn.setEnabled(true);
            } else if(Utils.isNotEmpty(woUnitTO.getUnitModel())
                    && !unitInformationTO.getUnitModelTO().getCode().equalsIgnoreCase(woUnitTO.getUnitModel())) {
                stringId = R.string.unit_model_mismatch;
                onlineVerificationBtn.setEnabled(true);
            }
            verificationStatusLatest.put(getUnitIdentifierValue(woUnitTO), validationSuccessful);
        } else {
            onlineVerificationBtn.setEnabled(true);
        }

        Log.i("validationFinished","value of string is "+resources.getString(stringId));
        onlineVerificationBtn.setText(resources.getString(stringId));

        onlineVerificationBtn.setEnabled(true);
        verificationInProgress = false;



    }

    public void showVerificationNotPossible(View parentView) {
        try {
            initializeView(parentView);
            onlineVerificationBtn.setText(resources.getString(R.string.perform_verification));
            onlineVerificationBtn.setBackgroundColor(resources.getColor(R.color.colorDisable));
            msgTxt.setVisibility(View.VISIBLE);
            msgTxt.setText(resources.getString(R.string.you_can_continue_without_verification_since_the_app_is_in_offline_mode));
            msgTxt.setTextColor(resources.getColor(R.color.colorAccent));
        } catch (Exception e) {
            writeLog("OnlineVerificationPageFragment : showOnlineVerificationNotPossible()", e);
        }
    }

    public boolean returnFlagForVerfication() {
        boolean userChoiceMap = false;
        try {
            if (Utils.isNotEmpty(verificationStatusMap.get(VALIDATION_STATUS))) {
                if (verificationStatusMap.containsValue(false)) {
                    userChoiceMap = false;
                    verificationStatusMap.remove(VALIDATION_STATUS);
                } else
                    userChoiceMap = true;
                verificationStatusMap.remove(VALIDATION_STATUS);
            }
        } catch (Exception e) {
            writeLog(TAG + " : returnFlagForVerfication()", e);
        }
        return userChoiceMap;
    }

    private class FetchUnitStatusThread extends AsyncTask<Void, Void, UnitInformationTO> {
        private WoUnitTO woUnitTO;

        public FetchUnitStatusThread(final WoUnitTO woUnitTO){
            this.woUnitTO = woUnitTO;
        }

        @Override
        protected UnitInformationTO doInBackground(final Void... params) {
            unitInformationTO = null;
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
            EvChargerVerification.this.validationFinished(unitInformationTO);
        }

    }
}
