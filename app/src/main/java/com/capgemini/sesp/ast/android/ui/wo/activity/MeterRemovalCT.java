package com.capgemini.sesp.ast.android.ui.wo.activity;

import android.os.Bundle;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.WorkorderAdditionalProcessingCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.WorkOrderEventReasonResetProcessor;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StepTypeConstants;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.YesNoFragment;

import java.util.ArrayList;

import me.drozdzynski.library.steppers.SteppersItem;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class MeterRemovalCT extends AbstractWokOrderActivity {

    SteppersItem isMeterLocationaccessible;
    SteppersItem meterNotAccessible;
    SteppersItem meterInstallationCheck;
    SteppersItem attachPhoto;
    SteppersItem newMeterTechPlanning;
    SteppersItem conformRemovedUnits;
    SteppersItem conformDisconnectionServiceLine;
    SteppersItem masterMeterInfo;
    SteppersItem oldMeterPowerCheck;
    SteppersItem canDMMeterBeInstalledYesNoPage;
    SteppersItem cannotPerformReasons;
    SteppersItem newMeterPowerCheck;
    SteppersItem additionalWork;
    SteppersItem readMeterIndication;
    SteppersItem registerEIComments;
    SteppersItem simCardInMeter;
    SteppersItem onlineVerification;
    SteppersItem readMeterIndi;
    SteppersItem registerProdMeterReading;
    SteppersItem registerExtAntenna;
    SteppersItem registernewexternalAntenna;
    SteppersItem conformCtRatio;
    SteppersItem qualityControl;
    SteppersItem verifyInstallInfo;
    SteppersItem verifyPhaseKey;
    SteppersItem verifyCurrentTransformer;
    SteppersItem verifyInstallationCoordinates;
    SteppersItem registertime;
    SteppersItem meterAccesibility;
    SteppersItem notAccessibleReason;
    SteppersItem riskAssessment;
    ArrayList<SteppersItem> yesflow = new ArrayList<>();
    ArrayList<SteppersItem> noFlow = new ArrayList<>();
    ArrayList<SteppersItem> meterInstallationCheckPositive = new ArrayList<>();
    ArrayList<SteppersItem> meterInstallationChecknegative = new ArrayList<>();
    ArrayList<SteppersItem> previoustoLast = new ArrayList<>();
    private static String TAG = MeterRemovalCT.class.getSimpleName();
    int fromindex, toindex;


    @Override
    public void addAssuredSteps() {
        try {
            addToActiveStepIfNotAdded(isMeterLocationaccessible, -1);
            addToActiveStepIfNotAdded(attachPhoto, -1);
            addToActiveStepIfNotAdded(registertime, -1);
            addToActiveStepIfNotAdded(registerEIComments, -1);
        } catch (Exception e) {
            writeLog(TAG + "  : addAssuredSteps() ", e);
        }

    }

    @Override
    public void chooseYesNoResponse(String stepIdentifier, String responseCode) {

        try {
            fromindex = activeSteps.indexOf(isMeterLocationaccessible) + 1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();

            if (responseCode.equalsIgnoreCase("0"))
                activeSteps.addAll(fromindex, yesflow);
            if (responseCode.equalsIgnoreCase("1"))
                activeSteps.addAll(fromindex, noFlow);

            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : chooseYesNoResponse() ", e);
        }


    }

    @Override
    public void meterInstallationCheckResponse(String stepId, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(meterInstallationCheck) + 1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();

            if (ConstantsAstSep.FlowPageConstants.VERIFYINSTALLINFO.equalsIgnoreCase(responseCode)) {
                activeSteps.addAll(fromindex, meterInstallationCheckPositive);
                //add ths fragment to flow verifyInstallInfoPage
            } else if (ConstantsAstSep.FlowPageConstants.NEGATIVE_FLOW_PAGE.equalsIgnoreCase(responseCode)) {
                activeSteps.addAll(fromindex, meterInstallationChecknegative);
                //add this fragment cannotPerformActivityReasonPage
            } else if ("3".equalsIgnoreCase(responseCode)) {
                //dont do anything

            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : meterInstallationCheckResponse() ", e);
        }

    }

    @Override
    public void registerCordsFragmentResponse(String stepId, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(oldMeterPowerCheck)+1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (ConstantsAstSep.FlowPageConstants.NEXT_PAGE_PRODUCT.equalsIgnoreCase(responseCode)) {
                if (activeSteps.contains(registerProdMeterReading))
                    activeSteps.remove(registerProdMeterReading);
            }
            else
            if (!activeSteps.contains(registerProdMeterReading))
                activeSteps.add(fromindex,registerProdMeterReading);
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : oldMeterPowerCheckPageFragmentResponse() ", e);
        }

    }

    @Override
    public void oldMeterChangeYesNoFragmentResponse(String stepId, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(canDMMeterBeInstalledYesNoPage) + 1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();


            if ("YES".equalsIgnoreCase(responseCode)) {
                activeSteps.addAll(fromindex, previoustoLast);
                //activeSteps.addAll(activeSteps.indexOf(canDMMeterBeInstalledYesNoPage)+1,canDMMeterBeInstalledYes);
                //newMeterTechPlanningPage
            } else if ("NO".equalsIgnoreCase(responseCode)) {
                //ConnectPhoto
            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : oldMeterChangeYesNoFragmentResponse() ", e);
        }
    }

    @Override
    public void newMeterTechPlanPageFragmentResponse(String stepId, String responseCode) {

    }

    @Override
    public void registerExternAntennaAsPerTechPlanFragmentResponse(String stepId, String responseCode) {

    }

    @Override
    public void MasterMeterInfoPageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    public void registerNewSimCardInfoPageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    public void onlineVerificationPageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    public void oldMeterPowerCheckPageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    public void registerNewCommunicationModulePageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);

        String[] arguments = new String[]{resources.getString(R.string.meter_accessibility), resources.getString(R.string.ismeterlocaccessible)};
        isMeterLocationaccessible = stepFactory.createStep(StepTypeConstants.YESNOSTEP, "ChooseYesNo", arguments);
            WorkorderAdditionalProcessingCallbackListener eventReasonResetProcessor =
                    new WorkOrderEventReasonResetProcessor();
            ((YesNoFragment) isMeterLocationaccessible.getFragment()).setProcessingCallbackListener(eventReasonResetProcessor);

        arguments = new String[]{resources.getString(R.string.accessibility)};
        meterAccesibility = stepFactory.createStep(StepTypeConstants.METERACCESSIBLE, "MeterAccessible", arguments);

        arguments = new String[]{resources.getString(R.string.info_text_view_measure_point)};
        meterInstallationCheck = stepFactory.createStep(StepTypeConstants.METERINSTALLATIONCHECK, "meterInstallationCheck", arguments);

        arguments = new String[]{resources.getString(R.string.accessibility)};
        meterNotAccessible = stepFactory.createStep(StepTypeConstants.METERNOTACCESSIBLE, "meterNotAccessible", arguments);

        arguments = new String[]{resources.getString(R.string.notaccessible)};
        notAccessibleReason = stepFactory.createStep(StepTypeConstants.NOTACCESSIBLEREASON, "notAccessibleReason", arguments);

        arguments = new String[]{resources.getString(R.string.verify_install_info)};
        verifyInstallInfo = stepFactory.createStep(StepTypeConstants.VERIFYMETERINSTALLINFO, "verifyInstallInfo", arguments);

        arguments = new String[]{resources.getString(R.string.verify_install_info)};
        verifyPhaseKey = stepFactory.createStep(StepTypeConstants.VERIFYPHASEKEY, "verifyPhaseKey", arguments);

        arguments = new String[]{resources.getString(R.string.verify_current_transformer)};
        verifyCurrentTransformer = stepFactory.createStep(StepTypeConstants.VERIFYCURRENTTRANSFORMER, "verifyCurrentTransformer", arguments);

        arguments = new String[]{resources.getString(R.string.register_cords)};
        verifyInstallationCoordinates = stepFactory.createStep(StepTypeConstants.REGISTERCORDS, "verifyInstallationCoordinates", arguments);

        arguments = new String[]{resources.getString(R.string.can_meter_be_removed)};
        canDMMeterBeInstalledYesNoPage = stepFactory.createStep(StepTypeConstants.OLDMETERCHANGEYESNO, "canDMMeterBeInstalledYesNoPage", arguments);

        arguments = new String[]{resources.getString(R.string.not_possible)};
        cannotPerformReasons = stepFactory.createStep(StepTypeConstants.CANNOTPERFORMREASON, "cannotPerformReasons", arguments);

        arguments = new String[]{resources.getString(R.string.register_new_meter_header)};
        newMeterTechPlanning = stepFactory.createStep(StepTypeConstants.NEWMETERTECHPLAN, "newMeterTechPlanning", arguments);

        arguments = new String[]{resources.getString(R.string.online_verification)};
        onlineVerification = stepFactory.createStep(StepTypeConstants.ONLINEVERIFICATION, "onlineVerification", arguments);

        arguments = new String[]{resources.getString(R.string.master_meter)};
        masterMeterInfo = stepFactory.createStep(StepTypeConstants.MASTERMETERINFOPAGE, "masterMeterInfo", arguments);

        arguments = new String[]{resources.getString(R.string.sim_card_in_meter), String.valueOf(CONSTANTS().WO_UNIT_T__METER)};
        simCardInMeter = stepFactory.createStep(StepTypeConstants.REGISTERNEWSIMCARDINFOPAGE, "simCardInMeter", arguments);

        arguments = new String[]{resources.getString(R.string.read_mtr_indic_new_mtr), String.valueOf(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_METER_REG_READ_T__MOUNTED_METER), String.valueOf(false)};
        readMeterIndi = stepFactory.createStep(StepTypeConstants.READMETERINDINEWMETER, "READMETERINDI", arguments);

        arguments = new String[]{resources.getString(R.string.reg_prod_meter_reading_title)};
        registerProdMeterReading = stepFactory.createStep(StepTypeConstants.REGISTERPRODMETERREADING, "REGISTERPRODMETERREADING", arguments);

        arguments = new String[]{resources.getString(R.string.register_Scaning)};
        readMeterIndication = stepFactory.createStep(StepTypeConstants.READMETERINDICATION, "readMeterIndication", arguments);

        arguments = new String[]{resources.getString(R.string.register_external_antenna)};
        registerExtAntenna = stepFactory.createStep(StepTypeConstants.REGISTEREXTERNANTENNAASPERTECHNICIANPLAN, "registerExtAntenna", arguments);

        arguments = new String[]{resources.getString(R.string.register_new_external_antenna)};
        registernewexternalAntenna = stepFactory.createStep(StepTypeConstants.REGISTERNEWEXTERNALANTENNA, "RegisterNewExternalAntenna", arguments);

        arguments = new String[]{resources.getString(R.string.confirm_ct_ratio)};
        conformCtRatio = stepFactory.createStep(StepTypeConstants.CONFORMCTRATIO, "conformCtRatio", arguments);

        arguments = new String[]{resources.getString(R.string.is_old_meter_powered)};
        oldMeterPowerCheck = stepFactory.createStep(StepTypeConstants.OLDMETERPOWERCHECK, "oldMeterPowerCheck", arguments);

        arguments = new String[]{resources.getString(R.string.confirm_removed_units)};
        conformRemovedUnits = stepFactory.createStep(StepTypeConstants.CONFORMREMOVEDUNITS, "conformRemovedUnits", arguments);

        arguments = new String[]{resources.getString(R.string.confirm_discon_service_line)};
        conformDisconnectionServiceLine = stepFactory.createStep(StepTypeConstants.CONFORMDISCONNECTIONSERVICELINE, "conformDisconnectionServiceLine", arguments);

        arguments = new String[]{resources.getString(R.string.is_meter_powered)};
        newMeterPowerCheck = stepFactory.createStep(StepTypeConstants.METERPOWERCHECKPAGE, "newMeterPowerCheck", arguments);

        arguments = new String[]{resources.getString(R.string.additional_work)};
        additionalWork = stepFactory.createStep(StepTypeConstants.ADDITIONALWORK, "additionalWork", arguments);

        arguments = new String[]{resources.getString(R.string.confirm_quality_control)};
        qualityControl = stepFactory.createStep(StepTypeConstants.QUALITYCONTROL, "qualityControl", arguments);

        arguments = new String[]{resources.getString(R.string.risk_assessment_for_your_safety)};
        riskAssessment = stepFactory.createStep(StepTypeConstants.RISKASSESSMENT,"riskAssessment",arguments);

        //Sure steps which will at last 3

        // arguments = new String[]{resources.getString(R.string.meter_accessibility), resources.getString(R.string.ismeterlocaccessible)};
        arguments = new String[]{resources.getString(R.string.connect_photos)};
        attachPhoto = stepFactory.createStep(StepTypeConstants.ATTACHPHOTO, "attachPhoto", arguments);

        arguments = new String[]{resources.getString(R.string.register_time_title)};
        registertime = stepFactory.createStep(StepTypeConstants.REGISTERTIME, "registerTime", arguments);

        arguments = new String[]{resources.getString(R.string.register_external_internal_comments)};
        registerEIComments = stepFactory.createStep(StepTypeConstants.REGISTERINTERNALEXTERNALCOMMENTS, "EIComments", arguments);


        //Add all initilized steps to Arraymap so that it can be accessed by Id
        stepIdWithStepItem = stepFactory.stepIdWithStepItem;

        yesflow.add(riskAssessment);
        yesflow.add(meterAccesibility);
        yesflow.add(meterInstallationCheck);
        noFlow.add(meterNotAccessible);
        noFlow.add(notAccessibleReason);

        meterInstallationCheckPositive.add(verifyInstallInfo);
        meterInstallationCheckPositive.add(verifyPhaseKey);
        meterInstallationCheckPositive.add(verifyCurrentTransformer);
        meterInstallationCheckPositive.add(verifyInstallationCoordinates);
        meterInstallationCheckPositive.add(registerProdMeterReading);
        meterInstallationCheckPositive.add(readMeterIndication);
        meterInstallationCheckPositive.add(canDMMeterBeInstalledYesNoPage);

        meterInstallationChecknegative.add(cannotPerformReasons);


        previoustoLast.add(oldMeterPowerCheck);
        previoustoLast.add(conformRemovedUnits);
        previoustoLast.add(conformDisconnectionServiceLine);
        previoustoLast.add(additionalWork);
        } catch (Exception e) {
            writeLog(TAG + "  : onCreate() ", e);
        }

    }


}

