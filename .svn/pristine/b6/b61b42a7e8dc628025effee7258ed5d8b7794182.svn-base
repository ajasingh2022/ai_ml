package com.capgemini.sesp.ast.android.ui.wo.activity;

import android.os.Bundle;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.WorkorderAdditionalProcessingCallbackListener;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.WorkOrderEventReasonResetProcessor;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StepTypeConstants;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.YesNoFragment;

import java.util.ArrayList;

import me.drozdzynski.library.steppers.SteppersItem;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.PageNavConstants.NEXT_PAGE_UNIT_VERIFICATION;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class TroubleshootMultipointRepeater extends AbstractWokOrderActivity {

    private SteppersItem confirmNetStationInstallationLocation;
    private SteppersItem verifyInstallationCoordinates;
    private SteppersItem registerMultiPointInfo;
    private SteppersItem canTroubleshootPerformed;
    private SteppersItem registerMultipointInfoFuse;
    private int fromindex;
    private int toindex;
    ArrayList<SteppersItem> yesflow = new ArrayList<>();
    ArrayList<SteppersItem> noFlow = new ArrayList<>();
    private SteppersItem newRepeaterTechPlan;
    private SteppersItem cannotPerformActivityReason;
    private SteppersItem attachPhoto;
    private SteppersItem registertime, registerEIComments;
    private SteppersItem onlineVerification;
    private SteppersItem simCardinRepeater;
    private SteppersItem newCmModule;
    private SteppersItem registerExternalAntenna;
    private SteppersItem registerNewExternalAntenna;
    private SteppersItem additionalWork;
    private SteppersItem simCardInCMModule;
    private SteppersItem attachFile;
    private SteppersItem updateMultipointInformation;
    private SteppersItem riskAssessment;
    private SteppersItem replaceDevice;
    private static String TAG = TroubleshootMultipointRepeater.class.getSimpleName();

    @Override
    public void addAssuredSteps() {
        try {
            addToActiveStepIfNotAdded(confirmNetStationInstallationLocation, -1);
            addToActiveStepIfNotAdded(riskAssessment, -1);
            addToActiveStepIfNotAdded(verifyInstallationCoordinates, -1);
            addToActiveStepIfNotAdded(canTroubleshootPerformed, -1);
            addToActiveStepIfNotAdded(additionalWork, -1);
            addToActiveStepIfNotAdded(attachFile, -1);
            addToActiveStepIfNotAdded(attachPhoto, -1);
            addToActiveStepIfNotAdded(registertime, -1);
            addToActiveStepIfNotAdded(registerEIComments, -1);
        } catch (Exception e) {
            writeLog(TAG + "  : addAssuredSteps() ", e);
        }

    }

    @Override
    public void chooseYesNoResponse(String stepid, String responseCode) {
        try {
            if (stepid == "ChooseYesNo") {
                fromindex = activeSteps.indexOf(canTroubleshootPerformed) + 1;
                toindex = activeSteps.indexOf(attachPhoto);
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();


                if (responseCode.equalsIgnoreCase("0")) {
                    activeSteps.add(fromindex, updateMultipointInformation);
                    addToActiveStepIfNotAdded(additionalWork, activeSteps.indexOf(attachPhoto));
                    addToActiveStepIfNotAdded(attachFile, activeSteps.indexOf(attachPhoto));

                }
                if (responseCode.equalsIgnoreCase("1"))
                    activeSteps.addAll(fromindex, noFlow);
            } else if (stepid == "updateMultipointInformation") {
                fromindex = activeSteps.indexOf(updateMultipointInformation) + 1;
                toindex = activeSteps.indexOf(additionalWork);
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();


                if (responseCode.equalsIgnoreCase("0")) {
                    activeSteps.addAll(fromindex, yesflow);

                }
                if (responseCode.equalsIgnoreCase("1"))
                    activeSteps.add(fromindex, replaceDevice);///Need to verify

            } else if (stepid == "replaceDevice") {
                fromindex = activeSteps.indexOf(replaceDevice) + 1;
                toindex = activeSteps.indexOf(additionalWork);
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();


                if (responseCode.equalsIgnoreCase("0")) {
                    activeSteps.add(fromindex, newRepeaterTechPlan);


                }
                if (responseCode.equalsIgnoreCase("1"))
                    activeSteps.add(fromindex, registerExternalAntenna);///Need to verify

            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : chooseYesNoResponse() ", e);
        }

    }

    @Override
    public void newRepeaterTechPlanPageFragmentResponse(String steppersItemIdentifier, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(newRepeaterTechPlan) + 1;
            toindex = activeSteps.indexOf(additionalWork);

            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();

            if (responseCode.equalsIgnoreCase(NEXT_PAGE_UNIT_VERIFICATION)) {
                //onlineVerification
                activeSteps.add(fromindex, onlineVerification);
            } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_SIM_CARD)) {
                //simCardinRepeater
                activeSteps.add(fromindex, simCardinRepeater);

            } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_CM_MODULE)) {
                //newCmModule
                activeSteps.add(fromindex, newCmModule);
            }

            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : newRepeaterTechPlanPageFragmentResponse() ", e);
        }

    }


    @Override
    public void onlineVerificationPageFragmentResponse(String stepid, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(onlineVerification) + 1;
            toindex = activeSteps.indexOf(additionalWork);

            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();

            if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_NO_METER)) {
                activeSteps.add(fromindex, registerExternalAntenna);
            }

            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : onlineVerificationPageFragmentResponse() ", e);
        }
    }


    @Override
    public void registerNewSimCardInfoPageFragmentResponse(String steppersItemIdentifier, String responseCode) {
        try {
            if (steppersItemIdentifier.equalsIgnoreCase("simCardinRepeater")) {
                fromindex = activeSteps.indexOf(simCardinRepeater) + 1;
                toindex = activeSteps.indexOf(additionalWork);
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();

                if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE) || responseCode.equalsIgnoreCase(NEXT_PAGE_UNIT_VERIFICATION)) {
                    addToActiveStepIfNotAdded(onlineVerification, fromindex);
                } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_CM_MODULE)) {

                    addToActiveStepIfNotAdded(newCmModule, fromindex);

                }
            } else if (steppersItemIdentifier.equalsIgnoreCase("simCardInCMModule")) {
                fromindex = activeSteps.indexOf(simCardInCMModule) + 1;
                toindex = activeSteps.indexOf(additionalWork);
                if (fromindex < toindex)
                    activeSteps.subList(fromindex, toindex).clear();
                if (responseCode.equalsIgnoreCase(NEXT_PAGE_UNIT_VERIFICATION)) {
                    activeSteps.add(fromindex, onlineVerification);
                }
            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : registerNewSimCardInfoPageFragmentResponse() ", e);
        }


    }

    @Override
    public void registerNewCommunicationModulePageFragmentResponse(String steppersItemIdentifier, String responseCode) {

        try {
            fromindex = activeSteps.indexOf(newCmModule) + 1;
            toindex = activeSteps.indexOf(additionalWork);
            if (fromindex < toindex)
                activeSteps.subList(fromindex, toindex).clear();

            if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE) || responseCode.equalsIgnoreCase(NEXT_PAGE_UNIT_VERIFICATION)) {

                addToActiveStepIfNotAdded(onlineVerification, fromindex);
            } else if (responseCode.equalsIgnoreCase(ConstantsAstSep.PageNavConstants.NEXT_PAGE_SIM_CARD)) {

                addToActiveStepIfNotAdded(simCardInCMModule, fromindex);

            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : registerNewCommunicationModulePageFragmentResponse() ", e);
        }
    }

    @Override
    public void registerExternAntennaAsPerTechPlanFragmentResponse(String stepId, String responseCode) {
        try {
            fromindex = activeSteps.indexOf(registerExternalAntenna) + 1;
            toindex = activeSteps.indexOf(additionalWork);


            if (responseCode.equalsIgnoreCase(ConstantsAstSep.FlowPageConstants.NEXT_PAGE_NEW_EXTERNAL_ANTENNA)) {

                if (!activeSteps.get(fromindex).equals(registerNewExternalAntenna))
                    activeSteps.add(fromindex, registerNewExternalAntenna);
            } else {
                if (activeSteps.contains(registerNewExternalAntenna))
                    activeSteps.remove(registerNewExternalAntenna);
            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            writeLog(TAG + "  : registerExternAntennaAsPerTechPlanFragmentResponse() ", e);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);

        final WorkorderAdditionalProcessingCallbackListener eventReasonResetProcessor = new WorkOrderEventReasonResetProcessor();

        String[] arguments = new String[]{resources.getString(R.string.confirm_installation_location)};
        confirmNetStationInstallationLocation = stepFactory.createStep(StepTypeConstants.CONFIRMNETSTATIONINSTALLATIONLOCATION, "confirmNetStationInstallationLocation", arguments);

        arguments = new String[]{resources.getString(R.string.register_cords)};
        verifyInstallationCoordinates = stepFactory.createStep(StepTypeConstants.REGISTERCORDS, "verifyInstallationCoordinates", arguments);

        arguments = new String[]{resources.getString(R.string.can_troubleshoot_be_performed), resources.getString(R.string.can_troubleshoot_be_performed) + "?"};
        canTroubleshootPerformed = stepFactory.createStep(StepTypeConstants.YESNOSTEP, "ChooseYesNo", arguments);
        ((YesNoFragment) canTroubleshootPerformed.getFragment()).setProcessingCallbackListener(eventReasonResetProcessor);

        arguments = new String[]{resources.getString(R.string.update_multipoint_information), resources.getString(R.string.update_multipoint_information)};
        updateMultipointInformation = stepFactory.createStep(StepTypeConstants.YESNOSTEP, "updateMultipointInformation", arguments);
        ((YesNoFragment) updateMultipointInformation.getFragment()).setProcessingCallbackListener(eventReasonResetProcessor);

        arguments = new String[]{resources.getString(R.string.not_possible)};
        cannotPerformActivityReason = stepFactory.createStep(StepTypeConstants.CANNOTPERFORMREASON, "cannotPerformActivityReason", arguments);

        arguments = new String[]{resources.getString(R.string.equipment)};
        registerMultiPointInfo = stepFactory.createStep(StepTypeConstants.REGISTERMULTIPOINTINFO, "registerMultiPointInfo", arguments);

        arguments = new String[]{resources.getString(R.string.fuse)};
        registerMultipointInfoFuse = stepFactory.createStep(StepTypeConstants.REGISTERMULTIPOINTFUSEINFO, "registerMultipointInfoFuse", arguments);

        arguments = new String[]{resources.getString(R.string.replace_device), resources.getString(R.string.replace_device)};
        replaceDevice = stepFactory.createStep(StepTypeConstants.YESNOSTEP, "replaceDevice", arguments);
        ((YesNoFragment) replaceDevice.getFragment()).setProcessingCallbackListener(eventReasonResetProcessor);

        arguments = new String[]{resources.getString(R.string.register_new_repeater_header), "false"};
        newRepeaterTechPlan = stepFactory.createStep(StepTypeConstants.NEWREPEATERTECHPLAN, "newRepeaterTechPlan", arguments);

        arguments = new String[]{resources.getString(R.string.online_verification)};
        onlineVerification = stepFactory.createStep(StepTypeConstants.ONLINEVERIFICATION, "onlineVerification", arguments);

        arguments = new String[]{resources.getString(R.string.sim_card_in_repeater), String.valueOf(CONSTANTS().WO_UNIT_T__REPEATER)};
        simCardinRepeater = stepFactory.createStep(StepTypeConstants.REGISTERNEWSIMCARDINFOPAGE, "simCardinRepeater", arguments);

        arguments = new String[]{resources.getString(R.string.register_new_cm_header), String.valueOf(CONSTANTS().WO_UNIT_T__REPEATER)};
        newCmModule = stepFactory.createStep(StepTypeConstants.REGISTERNEWCOMMUNICATIONMODULE, "newCmModule", arguments);

        /*final FlowPage simCardInCMModulePage = new RegisterNewSimCardInfoPage(resources.getString(R.string.sim_card_in_com_module),CONSTANTS().WO_UNIT_T__CM);*/

        arguments = new String[]{resources.getString(R.string.sim_card_in_com_module), String.valueOf(CONSTANTS().WO_UNIT_T__CM)};
        simCardInCMModule = stepFactory.createStep(StepTypeConstants.REGISTERNEWSIMCARDINFOPAGE, "simCardInCMModule", arguments);

        arguments = new String[]{resources.getString(R.string.register_external_antenna)};
        registerExternalAntenna = stepFactory.createStep(StepTypeConstants.REGISTEREXTERNANTENNAASPERTECHNICIANPLAN, "registerExternalAntenna", arguments);

        arguments = new String[]{resources.getString(R.string.register_new_external_antenna)};
        registerNewExternalAntenna = stepFactory.createStep(StepTypeConstants.REGISTERNEWEXTERNALANTENNA, "registerNewExternalAntenna", arguments);

        arguments = new String[]{resources.getString(R.string.risk_assessment_for_your_safety)};
        riskAssessment = stepFactory.createStep(StepTypeConstants.RISKASSESSMENT,"riskAssessment",arguments);

        // arguments = new String[]{};


        //Last 5 steps

        arguments = new String[]{resources.getString(R.string.additional_work)};
        additionalWork = stepFactory.createStep(StepTypeConstants.ADDITIONALWORK, "additionalWork", arguments);

        arguments = new String[]{resources.getString(R.string.attachments)};
        attachFile = stepFactory.createStep(StepTypeConstants.ATTACHFILE, "attachFile", arguments);

        arguments = new String[]{resources.getString(R.string.connect_photos)};
        attachPhoto = stepFactory.createStep(StepTypeConstants.ATTACHPHOTO, "attachPhoto", arguments);

        arguments = new String[]{resources.getString(R.string.register_time_title)};
        registertime = stepFactory.createStep(StepTypeConstants.REGISTERTIME, "registerTime", arguments);

        arguments = new String[]{resources.getString(R.string.register_external_internal_comments)};
        registerEIComments = stepFactory.createStep(StepTypeConstants.REGISTERINTERNALEXTERNALCOMMENTS, "EIComments", arguments);

        stepIdWithStepItem = stepFactory.stepIdWithStepItem;

        yesflow.add(registerMultiPointInfo);
        yesflow.add(registerMultipointInfoFuse);
        yesflow.add(replaceDevice);
        noFlow.add(cannotPerformActivityReason);
    } catch (Exception e) {
        writeLog(TAG + "  : onCreate() ", e);
    }
    }


}
