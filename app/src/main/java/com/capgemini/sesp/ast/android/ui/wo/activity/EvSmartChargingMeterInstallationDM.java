package com.capgemini.sesp.ast.android.ui.wo.activity;

import android.os.Bundle;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StepTypeConstants;

import java.util.ArrayList;

import me.drozdzynski.library.steppers.SteppersItem;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class EvSmartChargingMeterInstallationDM extends MeterInstallationDM {

    private static String TAG = EvSmartChargingMeterInstallationDM.class.getSimpleName();
    SteppersItem registerNewEvCharger;
    SteppersItem evChargerInformation;
    SteppersItem installChargerYesNo;
    ArrayList<SteppersItem> changeCharger = new ArrayList<>();

    @Override
    public void onlineVerificationPageFragmentResponse(String steppersItemIdentifier, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(onlineVerification) + 1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();
            activeSteps.add(fromindex, installChargerYesNo);
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : onlineVerificationPageFragmentResponse() ", e);
        }

    }

    @Override
    public void chooseYesNoResponse(String stepIdentifier, String responseCode) {
        try {
            toindex = activeSteps.indexOf(attachPhoto);
            if (stepIdentifier.equalsIgnoreCase("InstallEvChargerYesNo")) {
                fromindex = activeSteps.indexOf(installChargerYesNo) + 1;
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();

                if (responseCode.equalsIgnoreCase("0")) {
                    activeSteps.addAll(fromindex, changeCharger);
                    activeSteps.addAll(fromindex + changeCharger.size(), previoustoLast);
                } else if (responseCode.equalsIgnoreCase("1")) {
                    activeSteps.addAll(fromindex, previoustoLast);

                }
            } else if (stepIdentifier.equalsIgnoreCase("ChooseYesNo")) {

                fromindex = activeSteps.indexOf(isMeterLocationaccessible) + 1;

                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();

                if (responseCode.equalsIgnoreCase("0"))
                    activeSteps.addAll(fromindex, yesflow);
                if (responseCode.equalsIgnoreCase("1"))
                    activeSteps.addAll(fromindex, noFlow);
            }

            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : chooseYesNoResponse() ", e);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);


            String[] arguments = new String[]{resources.getString(R.string.register_new_ev_charger), String.valueOf(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_T__EV_CHARGER)};
            registerNewEvCharger = stepFactory.createStep(StepTypeConstants.REGISTERNEWEVCHARGERINFO, "REGISTERNEWEVCHARGERINFO", arguments);

            arguments = new String[]{resources.getString(R.string.ev_charger_information)};
            evChargerInformation = stepFactory.createStep(StepTypeConstants.EVCHARGERINFORMATION, "EVCHARGERINFORMATION", arguments);

            arguments = new String[]{resources.getString(R.string.install_ev_charger), resources.getString(R.string.install_ev_charger)};
            installChargerYesNo = stepFactory.createStep(StepTypeConstants.YESNOSTEP, "InstallEvChargerYesNo", arguments);


            //Add all initilized steps to Arraymap so that it can be accessed by Id

            stepIdWithStepItem = stepFactory.stepIdWithStepItem;

            yesflow.clear();
            noFlow.clear();
            yesflow.add(riskAssessment);
            yesflow.add(meterAccesibility);
            yesflow.add(meterInstallationCheck);
            noFlow.add(meterNotAccessible);
            noFlow.add(notAccessibleReason);

            meterInstallationCheckPositive.clear();
            meterInstallationChecknegative.clear();
            meterInstallationCheckPositive.add(verifyInstallInfo);
            meterInstallationCheckPositive.add(verifyPhaseKey);
            meterInstallationCheckPositive.add(verifyInstallationCoordinates);
            meterInstallationCheckPositive.add(canDMMeterBeInstalledYesNoPage);
            meterInstallationChecknegative.add(cannotPerformReasons);


            previoustoLast.clear();
            changeCharger.clear();
            changeCharger.add(registerNewEvCharger);
            changeCharger.add(evChargerInformation);
            previoustoLast.add(readMeterIndi);
            previoustoLast.add(registerExtAntenna);
            previoustoLast.add(registernewexternalAntenna);
            previoustoLast.add(applicationloadControl);
            previoustoLast.add(newMeterPowerCheck);
            previoustoLast.add(additionalWork);
            previoustoLast.add(qualityControl);

        } catch (Exception e) {
            writeLog(TAG + "  : onCreate() ", e);
        }
    }
}
