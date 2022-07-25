package com.capgemini.sesp.ast.android.ui.wo.activity;

import android.os.Bundle;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StepTypeConstants;

import java.util.ArrayList;

import me.drozdzynski.library.steppers.SteppersItem;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.FlowPageConstants.NEXT_PAGE_PRODUCT;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class EvTroubleshootMeasurePointCT extends TroubleshootMeasurePointCT {
    private static String TAG = EvTroubleshootMeasurePointCT.class.getSimpleName();
    SteppersItem registerNewEvCharger;
    SteppersItem evChargerInformation;
    SteppersItem changeChargerYesNo;
    SteppersItem troubleshootReasons;
    ArrayList<SteppersItem> changeCharger = new ArrayList<>();

    @Override
    public void onlineVerificationPageFragmentResponse(String steppersItemIdentifier, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(onlineVerification) + 1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();

            ArrayList<SteppersItem> changeMeterPositiveFlowTemp = new ArrayList<>(changeMeterPositiveFlow);
            if (responseCode.equalsIgnoreCase(NEXT_PAGE_PRODUCT)) {
                activeSteps.addAll(fromindex, changeMeterPositiveFlowTemp);
                activeSteps.add(activeSteps.indexOf(changeMeterReason)+1,changeChargerYesNo);
            } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.FlowPageConstants.NEXT_PAGE_NO_METER))
                activeSteps.add(fromindex, changeChargerYesNo);
            else{
                activeSteps.addAll(fromindex,changeMeterPositiveFlowTemp);
                activeSteps.add(activeSteps.indexOf(changeMeterReason)+1,changeChargerYesNo);
            }


            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : onlineVerificationPageFragmentResponse() ", e);
        }

    }

    @Override
    public void chooseYesNoResponse(String stepIdentifier, String responseCode) {
        try {
            toindex = activeSteps.indexOf(attachPhoto);
            if (stepIdentifier.equalsIgnoreCase("ChooseYesNo")) {
                fromindex = activeSteps.indexOf(isMeterLocationaccessible) + 1;

                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();

                if (responseCode.equalsIgnoreCase("0"))
                    activeSteps.addAll(fromindex, yesflow);
                if (responseCode.equalsIgnoreCase("1"))
                    activeSteps.addAll(fromindex, noFlow);
            } else if (stepIdentifier.equalsIgnoreCase("ChangeMeterYesNo")) {
                fromindex = activeSteps.indexOf(changeMaterYesNO) + 1;
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();

                if (responseCode.equalsIgnoreCase("0"))
                    activeSteps.add(fromindex, existingMeterTechPlanning);
                else if (responseCode.equalsIgnoreCase("1")) {

                    activeSteps.add(fromindex, changeChargerYesNo);

                }
            } else if (stepIdentifier.equalsIgnoreCase("ChangeEvChargerYesNo")) {
                fromindex = activeSteps.indexOf(changeChargerYesNo) + 1;
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();

                if (responseCode.equalsIgnoreCase("0")) {
                    activeSteps.addAll(fromindex, changeCharger);
                    activeSteps.addAll(fromindex + changeCharger.size(), previoustoLast);
                } else if (responseCode.equalsIgnoreCase("1")) {
                    activeSteps.addAll(fromindex, previoustoLast);

                }
            }

            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : chooseYesNoResponse() ", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] arguments = new String[]{resources.getString(R.string.register_new_ev_charger), String.valueOf(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_T__EV_CHARGER)};
        registerNewEvCharger = stepFactory.createStep(StepTypeConstants.REGISTERNEWEVCHARGERINFO, "REGISTERNEWEVCHARGERINFO", arguments);

        arguments = new String[]{resources.getString(R.string.ev_charger_information)};
        evChargerInformation = stepFactory.createStep(StepTypeConstants.EVCHARGERINFORMATION, "EVCHARGERINFORMATION", arguments);

        arguments = new String[]{resources.getString(R.string.change_ev_charger), resources.getString(R.string.change_ev_charger)};
        changeChargerYesNo = stepFactory.createStep(StepTypeConstants.YESNOSTEP, "ChangeEvChargerYesNo", arguments);

        arguments = new String[]{resources.getString(R.string.reason_for_troubleshoot), String.valueOf(CONSTANTS().PDA_PAGE_T__NO_COMMUNICATION),
                resources.getString(R.string.enter_the_reason_for_troubleshoot)};
        troubleshootReasons = stepFactory.createStep(StepTypeConstants.COMMUNICATIONFAILUREREASON, "COMMUNICATIONFAILUREREASON", arguments);

        stepIdWithStepItem = stepFactory.stepIdWithStepItem;


        yesflow.clear();
        noFlow.clear();
        meterInstallationCheckPositive.clear();
        meterInstallationChecknegative.clear();
        troubleshootPositiveFlow.clear();
        changeMeterPositiveFlow.clear();
        previoustoLast.clear();
        yesflow.add(riskAssessment);
        yesflow.add(meterAccesibility);
        yesflow.add(meterInstallationCheck);
        noFlow.add(meterNotAccessible);
        noFlow.add(notAccessibleReason);


        meterInstallationCheckPositive.add(verifyInstallInfo);
        meterInstallationCheckPositive.add(verifyPhaseKey);
        meterInstallationCheckPositive.add(verifyCurrentTransformer);
        meterInstallationCheckPositive.add(verifyInstallationCoordinates);
        meterInstallationCheckPositive.add(oldMeterPowerCheck);
        meterInstallationCheckPositive.add(registerOldProdMeterReading);
        meterInstallationCheckPositive.add(readOldMeterIndi);
        meterInstallationCheckPositive.add(oldMeterChangableYesNoPage);

        meterInstallationChecknegative.add(cannotPerformReasons);

        troubleshootPositiveFlow.add(troubleshootReasons);
        troubleshootPositiveFlow.add(changeMaterYesNO);

        //changeMeterPositiveFlow.add(registerProdMeterReading);
        changeMeterPositiveFlow.add(readMeterIndi);
        changeMeterPositiveFlow.add(changeMeterReason);

        changeCharger.add(registerNewEvCharger);
        changeCharger.add(evChargerInformation);

        previoustoLast.add(registerExtAntenna);
        previoustoLast.add(registernewexternalAntenna);
        previoustoLast.add(newMeterPowerCheck);
        previoustoLast.add(additionalWork);
    }
}
