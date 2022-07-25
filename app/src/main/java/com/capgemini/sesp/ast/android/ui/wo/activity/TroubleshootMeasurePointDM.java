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
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.FlowPageConstants.NEXT_PAGE_PRODUCT;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class TroubleshootMeasurePointDM extends AbstractWokOrderActivity {

    SteppersItem isMeterLocationaccessible;
    SteppersItem meterNotAccessible;
    SteppersItem meterInstallationCheck;
    SteppersItem applicationloadControl;
    SteppersItem attachPhoto;
    SteppersItem existingMeterTechPlanning;
    SteppersItem masterMeterInfo;
    SteppersItem newCmModule;
    SteppersItem oldMeterChangableYesNoPage;
    SteppersItem cannotPerformReasons;
    SteppersItem changeMaterYesNO;
    SteppersItem oldMeterPowerCheck;
    SteppersItem additionalWork;
    SteppersItem registerEIComments;
    SteppersItem simCardInMeter;
    SteppersItem onlineVerification;
    SteppersItem readMeterIndi;
    SteppersItem readOldMeterIndi;
    SteppersItem registerProdMeterReading;
    SteppersItem registerOldProdMeterReading;
    SteppersItem registerExtAntenna;
    SteppersItem registernewexternalAntenna;
    SteppersItem qualityControl;
    SteppersItem changeMeterReason;
    SteppersItem simCardCmModule;
    SteppersItem newMeterPowerCheck;
    SteppersItem verifyInstallInfo;
    SteppersItem verifyPhaseKey;
    SteppersItem communicationFailureReason;
    SteppersItem verifyInstallationCoordinates;
    SteppersItem verifyCurrentTransformer;
    SteppersItem registertime;
    SteppersItem meterAccesibility;
    SteppersItem notAccessibleReason;
    SteppersItem riskAssessment;
    ArrayList<SteppersItem> yesflow = new ArrayList<>();
    ArrayList<SteppersItem> noFlow = new ArrayList<>();
    ArrayList<SteppersItem> meterInstallationCheckPositive = new ArrayList<>();
    ArrayList<SteppersItem> meterInstallationChecknegative = new ArrayList<>();
    ArrayList<SteppersItem> previoustoLast = new ArrayList<>();
    ArrayList<SteppersItem> troubleshootPositiveFlow = new ArrayList<>();
    ArrayList<SteppersItem> changeMeterPositiveFlow = new ArrayList<>();
    private static String TAG = TroubleshootMeasurePointDM.class.getSimpleName();
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
            toindex = activeSteps.indexOf(attachPhoto);
            if (stepIdentifier.equalsIgnoreCase("ChooseYesNo")) {
                fromindex = activeSteps.indexOf(isMeterLocationaccessible) + 1;

                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();

                if (responseCode.equalsIgnoreCase("0"))
                    activeSteps.addAll(fromindex, yesflow);
                if (responseCode.equalsIgnoreCase("1"))
                    activeSteps.addAll(fromindex, noFlow);
            } else {
                fromindex = activeSteps.indexOf(changeMaterYesNO) + 1;
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();

                if (responseCode.equalsIgnoreCase("0"))
                    activeSteps.add(fromindex, existingMeterTechPlanning);
                else if (responseCode.equalsIgnoreCase("1")) {

                    activeSteps.addAll(fromindex, previoustoLast);

                }
            }

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
            }


            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : meterInstallationCheckResponse() ", e);
        }

    }


    @Override
    public void oldMeterChangeYesNoFragmentResponse(String stepId, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(oldMeterChangableYesNoPage) + 1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();


            if ("YES".equalsIgnoreCase(responseCode)) {
                activeSteps.addAll(fromindex, troubleshootPositiveFlow);
                //activeSteps.addAll(activeSteps.indexOf(oldMeterChangableYesNoPage)+1,canDMMeterBeInstalledYes);
                //existingMeterTechPlanningPage
            } else if ("NO".equalsIgnoreCase(responseCode)) {
                //ConnectPhoto
            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : oldMeterChangeYesNoFragmentResponse() ", e);
        }
    }

    @Override
    public void registerCordsFragmentResponse(String stepId, String responseCode) {

    }

    @Override
    public void newMeterTechPlanPageFragmentResponse(String stepId, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(existingMeterTechPlanning) + 1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();

            if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_WITHOUT_MASTER) || responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_UNIT_VERIFICATION)) {
                activeSteps.add(fromindex, onlineVerification);
                //onlineVerificationpage
            } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_WITH_MASTER)) {

                activeSteps.add(fromindex, masterMeterInfo);
                //,masterMeterInfoPage
            } else if (responseCode.equalsIgnoreCase((ConstantsAstSep.PageNavConstants.NEXT_PAGE_SIM_CARD))) {

                activeSteps.add(fromindex, simCardInMeter);
                //simCardInMeterPage
            } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_CM_MODULE)) {
                activeSteps.add(fromindex, newCmModule);
                //newCmModulepage
            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : newMeterTechPlanPageFragmentResponse() ", e);
        }


    }

    @Override
    public void registerExternAntennaAsPerTechPlanFragmentResponse(String stepId, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(registerExtAntenna) + 1;
            toindex = activeSteps.indexOf(attachPhoto);


            if (responseCode.equalsIgnoreCase(ConstantsAstSep.FlowPageConstants.NEXT_PAGE_NEW_EXTERNAL_ANTENNA)) {
            /*Bundle bundle = new Bundle();
            bundle.putInt(ConstantsAstSep.FlowPageConstants.USER_SELECTION_EXTERNAL_ANTENA, Integer.parseInt(responseCode));*/
                if (!activeSteps.get(fromindex).equals(registernewexternalAntenna))
                    activeSteps.add(fromindex, registernewexternalAntenna);
                //NewExternalAntenna
            } else {
                if (activeSteps.contains(registernewexternalAntenna))
                    activeSteps.remove(registernewexternalAntenna);
            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : registerExternAntennaAsPerTechPlanFragmentResponse() ", e);
        }


    }

    @Override
    public void MasterMeterInfoPageFragmentResponse(String steppersItemIdentifier, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(masterMeterInfo) + 1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();

            if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE) || responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_UNIT_VERIFICATION)) {
                activeSteps.add(fromindex, onlineVerification);
            } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_SIM_CARD)) {
                activeSteps.add(fromindex, simCardInMeter);
            } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_CM_MODULE)) {

                activeSteps.add(fromindex, newCmModule);
            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : MasterMeterInfoPageFragmentResponse() ", e);
        }
    }

    @Override
    public void registerNewSimCardInfoPageFragmentResponse(String steppersItemIdentifier, String responseCode) {
        try {
            if (steppersItemIdentifier == "simCardInMeter") {
                fromindex = activeSteps.indexOf(simCardInMeter) + 1;
                toindex = activeSteps.indexOf(attachPhoto);
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();

                if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE) || responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_UNIT_VERIFICATION)) {
                    addToActiveStepIfNotAdded(onlineVerification, fromindex);
                    //activeSteps.add(onlineVerification);
                    //, onlineVerificationPage

                } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_CM_MODULE)) {

                    addToActiveStepIfNotAdded(newCmModule, fromindex);
                    //activeSteps.add(newCmModule);
                    //newcmModule
                } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_WITH_MASTER)) {

                    addToActiveStepIfNotAdded(masterMeterInfo, fromindex);
                    //activeSteps.add(newCmModule);
                    //newcmModule
                }
            } else if (steppersItemIdentifier == "simCardInCMModule") {
                fromindex = activeSteps.indexOf(simCardCmModule) + 1;
                toindex = activeSteps.indexOf(attachPhoto);
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();

                if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE) || responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_UNIT_VERIFICATION)) {
                    addToActiveStepIfNotAdded(onlineVerification, fromindex);
                    //activeSteps.add(onlineVerification);
                    //, onlineVerificationPage

                }
            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : registerNewSimCardInfoPageFragmentResponse() ", e);
        }


    }

    @Override
    public void onlineVerificationPageFragmentResponse(String steppersItemIdentifier, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(onlineVerification) + 1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();

            ArrayList<SteppersItem> changeMeterPositiveFlowTemp = new ArrayList<>(changeMeterPositiveFlow) ;

            if (responseCode.equalsIgnoreCase(NEXT_PAGE_PRODUCT)) {
                changeMeterPositiveFlowTemp.remove(0);
                activeSteps.addAll(fromindex, changeMeterPositiveFlowTemp);
                activeSteps.addAll(activeSteps.indexOf(changeMeterReason)+1,previoustoLast);
            } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.FlowPageConstants.NEXT_PAGE_NO_METER))
                activeSteps.addAll(fromindex, previoustoLast);
            else{
                activeSteps.addAll(fromindex,changeMeterPositiveFlowTemp);
                activeSteps.addAll(activeSteps.indexOf(changeMeterReason)+1,previoustoLast);
            }

            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : onlineVerificationPageFragmentResponse() ", e);
        }

    }

    @Override
    public void oldMeterPowerCheckPageFragmentResponse(String steppersItemIdentifier, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(oldMeterPowerCheck)+1;
            toindex = activeSteps.indexOf(attachPhoto);
            if (ConstantsAstSep.FlowPageConstants.NEXT_PAGE_PRODUCT.equalsIgnoreCase(responseCode)) {
                if (activeSteps.contains(registerOldProdMeterReading))
                    activeSteps.remove(registerOldProdMeterReading);
            }
            else
            if (!activeSteps.contains(registerOldProdMeterReading))
                activeSteps.add(fromindex,registerOldProdMeterReading);
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : oldMeterPowerCheckPageFragmentResponse() ", e);
        }
    }

    @Override
    public void registerNewCommunicationModulePageFragmentResponse(String steppersItemIdentifier, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(newCmModule) + 1;
            if (activeSteps.contains(simCardCmModule))
                activeSteps.remove(simCardCmModule);
            if (activeSteps.contains(onlineVerification))
                activeSteps.remove(onlineVerification);
            if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_SIM_CARD)) {
                activeSteps.add(fromindex, simCardCmModule);
            } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_UNIT_VERIFICATION)) {
                activeSteps.add(fromindex, onlineVerification);
                //onlineVerificationpage
            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : registerNewCommunicationModulePageFragmentResponse() ", e);
        }
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

        arguments = new String[]{resources.getString(R.string.register_cords)};
        verifyInstallationCoordinates = stepFactory.createStep(StepTypeConstants.REGISTERCORDS, "verifyInstallationCoordinates", arguments);

        arguments = new String[]{resources.getString(R.string.can_troubleshoot_be_performed)};
        oldMeterChangableYesNoPage = stepFactory.createStep(StepTypeConstants.OLDMETERCHANGEYESNO, "oldMeterChangableYesNoPage", arguments);

        arguments = new String[]{resources.getString(R.string.sim_card_in_com_module), String.valueOf(CONSTANTS().WO_UNIT_T__CM)};
        simCardCmModule = stepFactory.createStep(StepTypeConstants.REGISTERNEWSIMCARDINFOPAGE, "simCardInCMModule", arguments);

        arguments = new String[]{resources.getString(R.string.not_possible)};
        cannotPerformReasons = stepFactory.createStep(StepTypeConstants.CANNOTPERFORMREASON, "cannotPerformReasons", arguments);

        arguments = new String[]{resources.getString(R.string.existing_meter)};
        existingMeterTechPlanning = stepFactory.createStep(StepTypeConstants.EXISTINGMETERTECHPLAN, "EXISTINGMETERTECHPLAN", arguments);

        arguments = new String[]{resources.getString(R.string.register_new_cm_header), String.valueOf(CONSTANTS().WO_UNIT_T__METER)};
        newCmModule = stepFactory.createStep(StepTypeConstants.REGISTERNEWCOMMUNICATIONMODULE, "newCmModule", arguments);

        arguments = new String[]{resources.getString(R.string.online_verification)};
        onlineVerification = stepFactory.createStep(StepTypeConstants.ONLINEVERIFICATION, "onlineVerification", arguments);

        arguments = new String[]{resources.getString(R.string.master_meter)};
        masterMeterInfo = stepFactory.createStep(StepTypeConstants.MASTERMETERINFOPAGE, "masterMeterInfo", arguments);

        arguments = new String[]{resources.getString(R.string.sim_card_in_meter), String.valueOf(CONSTANTS().WO_UNIT_T__METER)};
        simCardInMeter = stepFactory.createStep(StepTypeConstants.REGISTERNEWSIMCARDINFOPAGE, "simCardInMeter", arguments);

        arguments = new String[]{resources.getString(R.string.read_mtr_indic_new_mtr), String.valueOf(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_METER_REG_READ_T__MOUNTED_METER), String.valueOf(false)};
        readMeterIndi = stepFactory.createStep(StepTypeConstants.READMETERINDINEWMETER, "READMETERINDI", arguments);

        arguments = new String[]{resources.getString(R.string.read_mtr_indic), String.valueOf(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_METER_REG_READ_T__DISMANTLED_METER), String.valueOf(true)};
        readOldMeterIndi = stepFactory.createStep(StepTypeConstants.READMETERINDINEWMETER, "readOldMeterIndic", arguments);

        arguments = new String[]{resources.getString(R.string.verify_current_transformer)};
        verifyCurrentTransformer = stepFactory.createStep(StepTypeConstants.VERIFYCURRENTTRANSFORMER, "verifyCurrentTransformer", arguments);

        arguments = new String[]{resources.getString(R.string.reason_for_changing_meter)};
        changeMeterReason = stepFactory.createStep(StepTypeConstants.CHANGEMETERREASON, "CHANGEMETERREASON", arguments);

        arguments = new String[]{resources.getString(R.string.change_meter),resources.getString(R.string.change_meter)};
        changeMaterYesNO = stepFactory.createStep(StepTypeConstants.YESNOSTEP, "ChangeMeterYesNo", arguments);

        arguments = new String[]{resources.getString(R.string.reg_prod_meter_reading_title)};
        registerProdMeterReading = stepFactory.createStep(StepTypeConstants.REGISTERPRODMETERREADING, "REGISTERPRODMETERREADING", arguments);

        arguments = new String[]{resources.getString(R.string.reg_prod_meter_reading_title)};
        registerOldProdMeterReading = stepFactory.createStep(StepTypeConstants.REGISTERPRODMETERREADING, "REGISTEROLDPRODMETERREADING", arguments);

        arguments = new String[]{resources.getString(R.string.reason_for_no_communication), String.valueOf(CONSTANTS().PDA_PAGE_T__NO_COMMUNICATION)};
        communicationFailureReason = stepFactory.createStep(StepTypeConstants.COMMUNICATIONFAILUREREASON, "COMMUNICATIONFAILUREREASON", arguments);


        arguments = new String[]{resources.getString(R.string.register_external_antenna)};
        registerExtAntenna = stepFactory.createStep(StepTypeConstants.REGISTEREXTERNANTENNAASPERTECHNICIANPLAN, "registerExtAntenna", arguments);


        arguments = new String[]{resources.getString(R.string.register_new_external_antenna)};
        registernewexternalAntenna = stepFactory.createStep(StepTypeConstants.REGISTERNEWEXTERNALANTENNA, "RegisterNewExternalAntenna", arguments);

        arguments = new String[]{resources.getString(R.string.app_load_control)};
        applicationloadControl = stepFactory.createStep(StepTypeConstants.APPLIANCELOADCONTROL, "ApplicationLoadControl", arguments);


        arguments = new String[]{resources.getString(R.string.verify_power_status_header)};
        oldMeterPowerCheck = stepFactory.createStep(StepTypeConstants.OLDMETERPOWERCHECK, "oldMeterPowerCheck", arguments);

        arguments = new String[]{resources.getString(R.string.additional_work)};
        additionalWork = stepFactory.createStep(StepTypeConstants.ADDITIONALWORK, "additionalWork", arguments);

        arguments = new String[]{resources.getString(R.string.confirm_quality_control)};
        qualityControl = stepFactory.createStep(StepTypeConstants.QUALITYCONTROL, "qualityControl", arguments);

        arguments = new String[]{resources.getString(R.string.is_meter_powered)};
        newMeterPowerCheck = stepFactory.createStep(StepTypeConstants.METERPOWERCHECKPAGE, "newMeterPowerCheck", arguments);

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
        meterInstallationCheckPositive.add(verifyInstallationCoordinates);
        meterInstallationCheckPositive.add(oldMeterPowerCheck);
        meterInstallationCheckPositive.add(registerOldProdMeterReading);
        meterInstallationCheckPositive.add(readOldMeterIndi);
        meterInstallationCheckPositive.add(oldMeterChangableYesNoPage);

        meterInstallationChecknegative.add(cannotPerformReasons);

        troubleshootPositiveFlow.add(communicationFailureReason);
        troubleshootPositiveFlow.add(changeMaterYesNO);

        changeMeterPositiveFlow.add(registerProdMeterReading);
        changeMeterPositiveFlow.add(readMeterIndi);
        changeMeterPositiveFlow.add(changeMeterReason);

        previoustoLast.add(registerExtAntenna);
        previoustoLast.add(registernewexternalAntenna);
        previoustoLast.add(newMeterPowerCheck);
        previoustoLast.add(additionalWork);
    } catch (Exception e) {
        writeLog(TAG + "  : onCreate() ", e);
    }

    }


}
