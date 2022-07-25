package com.capgemini.sesp.ast.android.module.util;

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
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class BatteryInverterVerfication implements NetworkStatusCallbackListener {

    private static String TAG = BatteryInverterVerfication.class.getSimpleName();
    ArrayMap<Integer, WoUnitCustomTO> woUnitCustomTOArrayMap = new ArrayMap<Integer, WoUnitCustomTO>();
    Button onlineVerificationBtn;
    TextView msgTxt;
    View view;
    ArrayMap<String, Boolean> verificationStatusMap = new ArrayMap<>();
    private transient boolean validationSuccessful = false;
    protected static final String VALIDATION_STATUS = "VALIDATION_STATUS";
    Resources resources = ApplicationAstSep.context.getResources();
    List<IncorrectUnitsBean> incorrectUnitsBeanList = new ArrayList<IncorrectUnitsBean>();

    public void unitVerfication(View parentView, ArrayMap<Integer, WoUnitCustomTO> woUnitCustomTOS) {
        this.woUnitCustomTOArrayMap = woUnitCustomTOS;
        initializeView(parentView);
        msgTxt.setVisibility(View.INVISIBLE);
        onlineVerificationBtn.setText(resources.getString(R.string.performing_verification));
        onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorAccent));
        Drawable verification = resources.getDrawable(R.drawable.ic_done_24dp);
        onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verification, null, null, null);
        new FetchUnitStatusThread(woUnitCustomTOArrayMap).execute();
    }
    public void initializeView(View parentview)
    {

        onlineVerificationBtn = parentview.findViewById(R.id.batteryInverterVerificationBtn);
        msgTxt = parentview.findViewById(R.id.msgText);
    }


    @Override
    public void networkStatusChanged(boolean isConnected) {
        if (!isConnected) {
            SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
            msgTxt.setText(resources.getString(R.string.request_timeout_message));
        }
    }

    public void validationFinished(List<UnitInformationTO> unitInformationTO) {
        try {
            validationSuccessful = true;
            List<UnitInformationTO> unitInformationTOFailedList = new ArrayList<UnitInformationTO>();
            Drawable verificationFailed = resources.getDrawable(R.drawable.ic_cancel_hollow_24dp);
            onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorRed));
            onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verificationFailed, null, null, null);
            int stringId = R.string.verification_failed;
            String errorMSg = "";
            if (unitInformationTO != null) {
                if (unitInformationTO.size() == woUnitCustomTOArrayMap.size()) {
                    for (UnitInformationTO unitInformationTO1 : unitInformationTO) {
                        boolean mountable = AndroidUtilsAstSep.CONSTANTS().UNIT_STATUS_T__MOUNTABLE.equals(unitInformationTO1.getUnitStatusTTO().getId());
                        stringId = R.string.verification_ok;
                        Drawable verificationok = resources.getDrawable(R.drawable.ic_done_24dp);
                        onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verificationok, null, null, null);
                        onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorAccent));
                        msgTxt.setVisibility(View.GONE);
                        if (!mountable) {
                            unitInformationTOFailedList.add(unitInformationTO1);
                        }
                    }
                } else {
                    validationSuccessful = false;
                    stringId = R.string.verification_failed;
                    verificationFailed = resources.getDrawable(R.drawable.ic_cancel_hollow_24dp);
                    onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorRed));
                    onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verificationFailed, null, null, null);
                    for (IncorrectUnitsBean incorrectUnitsBean : incorrectUnitsBeanList) {
                        errorMSg = errorMSg + incorrectUnitsBean.getGiai() + ", ";
                    }
                    msgTxt.setVisibility(View.VISIBLE);
                    msgTxt.setText(errorMSg + resources.getString(R.string.no_unit_found));
                    incorrectUnitsBeanList.clear();


                }
                if (unitInformationTOFailedList.size() > 0) {
                    validationSuccessful = false;
                    stringId = R.string.verification_failed;
                    verificationFailed = resources.getDrawable(R.drawable.ic_cancel_hollow_24dp);
                    onlineVerificationBtn.setTextColor(resources.getColor(R.color.colorRed));
                    onlineVerificationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(verificationFailed, null, null, null);
                    for (UnitInformationTO unitInformationTO1 : unitInformationTOFailedList) {
                        errorMSg = errorMSg + unitInformationTO1.getGiai() + ", ";
                    }
                    msgTxt.setVisibility(View.VISIBLE);
                    msgTxt.setText(errorMSg + resources.getString(R.string.unit_is_not_mountable));
                }

            }
            verificationStatusMap.put(VALIDATION_STATUS, validationSuccessful);
            onlineVerificationBtn.setText(stringId);
            onlineVerificationBtn.setEnabled(true);
        } catch (Exception e) {
            writeLog(TAG + ": validationFinished()", e);
        }
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

    public Boolean checkDuplicateArrayMapValue(ArrayMap<Integer, WoUnitCustomTO> arrayMap) {

        if (Utils.isNotEmpty(arrayMap)) {
            for (int i = -1; i < arrayMap.size() - 1; i++) {
                for (int j = i + 1; j < arrayMap.size() - 1; j++) {
                    if (arrayMap.get(i).getGiai().equalsIgnoreCase(arrayMap.get(j).getGiai())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    class IncorrectUnitsBean {
        String unitModel;
        String identifier;
        Long idUnitIdentifierT;
        String giai;

        public String getGiai() {
            return giai;
        }

        public void setGiai(String giai) {
            this.giai = giai;
        }

        public String getUnitModel() {
            return unitModel;
        }

        public void setUnitModel(String unitModel) {
            this.unitModel = unitModel;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public Long getIdUnitIdentifierT() {
            return idUnitIdentifierT;
        }

        public void setIdUnitIdentifierT(Long idUnitIdentifierT) {
            this.idUnitIdentifierT = idUnitIdentifierT;
        }
    }

    public class FetchUnitStatusThread extends AsyncTask<Void, Void, List<UnitInformationTO>> {
        private ArrayMap<Integer, WoUnitCustomTO> woUnitCustomTOArrayMap;

        public FetchUnitStatusThread(final ArrayMap<Integer, WoUnitCustomTO> woUnitCustomTO) {
            this.woUnitCustomTOArrayMap = woUnitCustomTO;
        }

        @Override
        protected List<UnitInformationTO> doInBackground(final Void... params) {


            List<UnitInformationTO> unitInformationTOList = new ArrayList<UnitInformationTO>();
            UnitInformationTO unitInformationTO = null;
            String identifier = null;
            String unitModel = null;
            Long idUnitIdentifierT = null;
            for (Map.Entry<Integer, WoUnitCustomTO> entry : woUnitCustomTOArrayMap.entrySet()) {
                try {
                    unitInformationTO = new UnitInformationTO();
                    idUnitIdentifierT = entry.getValue().getIdUnitIdentifierT();
                    identifier = UnitInstallationUtils.getUnitIdentifierValue(entry.getValue(), idUnitIdentifierT);
                    unitModel = entry.getValue().getUnitModel();
                    Log.i(TAG, "UNIT MODEL IDENTIFIER TYPE ID :: " + idUnitIdentifierT);
                    Log.i(TAG, "UNIT MODEL IDENTIFIER VALUE :: " + identifier);
                    Log.i(TAG, "UNIT MODEL VALUE :: " + unitModel);
                    unitInformationTO = Utils.isNotEmpty(unitModel) ?
                            AndroidUtilsAstSep.getDelegate().getDeviceInfoWithModel(identifier, idUnitIdentifierT, unitModel) :
                            AndroidUtilsAstSep.getDelegate().getDeviceInfo(identifier, idUnitIdentifierT);
                    unitInformationTOList.add(unitInformationTO);
                } catch (Exception e) {
                    IncorrectUnitsBean incorrectUnitsBean = new IncorrectUnitsBean();
                    incorrectUnitsBean.setUnitModel(unitModel);
                    incorrectUnitsBean.setIdentifier(identifier);
                    incorrectUnitsBean.setIdUnitIdentifierT(idUnitIdentifierT);
                    incorrectUnitsBean.setGiai(identifier);
                    incorrectUnitsBeanList.add(incorrectUnitsBean);
                    writeLog(" : doInBackground()", e);
                }
            }
            return unitInformationTOList;
        }

        @Override
        public void onPostExecute(List<UnitInformationTO> unitInformationTO) {
            validationFinished(unitInformationTO);

        }
    }
}
