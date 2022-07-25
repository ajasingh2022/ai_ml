package com.capgemini.sesp.ast.android.ui.wo.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.communication.WorkorderAdditionalProcessingCallbackListener;
import com.capgemini.sesp.ast.android.module.tsp.TspUtil;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.StepFactory;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StepTypeConstants;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.YesNoFragment;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataValueTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoAdditionalDataCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.drozdzynski.library.steppers.SteppersItem;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class TroubleShootSolar extends AbstractWokOrderActivity {

    SteppersItem isLocationAccessible;

    SteppersItem registerNonAccesibility;
    SteppersItem ismeterConnectedToSolarModule;
    SteppersItem registerNonAccessibleReasons;

    SteppersItem registerAccesibility;
    SteppersItem verifySolarPanel;
    SteppersItem verifyInverter;
    SteppersItem verifyBattery;


    SteppersItem verifyInstallationInfo;
    SteppersItem verifyPhaseKey;
    SteppersItem verifyCoordinates;

    SteppersItem canTroubleShootBeerformed;

    SteppersItem registerNotPerformedReasons;

    SteppersItem replaceSolarPanel;
    SteppersItem replaceInverter;
    SteppersItem replaceBattery;
    SteppersItem verifyPowerOutput;
    SteppersItem verifyPowerOutputMeterConnection;
    SteppersItem ismeterConnectinWorking;
    SteppersItem additionalWork;

    private SteppersItem attachPhoto;
    private SteppersItem registerEIComments;
    private SteppersItem registertime;

    ArrayList<SteppersItem> verificationSteps = new ArrayList<>();
    ArrayList<SteppersItem> troubleShootSteps = new ArrayList<>();
    ArrayList<SteppersItem> verifymeasurePointSteps = new ArrayList<>();

    int fromindex, toindex;

    @Override
    public void addAssuredSteps() {
        addToActiveStepIfNotAdded(isLocationAccessible, -1);
        addToActiveStepIfNotAdded(attachPhoto, -1);
        addToActiveStepIfNotAdded(registertime, -1);
        addToActiveStepIfNotAdded(registerEIComments, -1);
    }

    @Override
    public void chooseYesNoResponse(String stepid, String responseCode) {
        toindex = activeSteps.indexOf(attachPhoto);
        ArrayList<SteppersItem> stepsToAdd = new ArrayList<SteppersItem>();

        switch (responseCode){

            case "0": switch (stepid){
                case "isLocationAccessible": fromindex =
                        activeSteps.indexOf(isLocationAccessible) +1; stepsToAdd =
                        verificationSteps;
                        break;
                case "canTroubleshootbePerformed":fromindex =
                        activeSteps.indexOf(canTroubleShootBeerformed)+1; stepsToAdd =
                        troubleShootSteps;
                        break;
                case "ismeterConnectedToSolarModule": fromindex =
                        activeSteps.indexOf(ismeterConnectedToSolarModule)+1; stepsToAdd =
                        verifymeasurePointSteps;
                        toindex = activeSteps.indexOf(canTroubleShootBeerformed);
                        break;
                case "verifyPowerOutputMeterConnection": fromindex =
                        activeSteps.indexOf(verifyPowerOutputMeterConnection) +1;
                        updateAdditionalDataValue(true,"SOLAR_PANEL_CONNECTED_TO_METER");
                        stepsToAdd.add(ismeterConnectinWorking);
                        toindex = activeSteps.indexOf(additionalWork);
                        break;
                case "verifyPowerOutput":fromindex = toindex;
                    updateAdditionalDataValue(true,"INVERTER_OUTPUT_OK");
                break;

                case  "ismeterConnectinWorking":fromindex = toindex;
                updateAdditionalDataValue(true,"METER_CONNECTION_WORKING");
                break;


            }break;
            case "1":switch (stepid){
                case "isLocationAccessible": fromindex =
                        activeSteps.indexOf(isLocationAccessible) +1;
                        stepsToAdd.add(registerNonAccesibility);
                        stepsToAdd.add(registerNonAccessibleReasons);

                    break;
                case "canTroubleshootbePerformed":fromindex =
                        activeSteps.indexOf(canTroubleShootBeerformed)+1;
                        stepsToAdd.add(registerNotPerformedReasons);
                    break;
                case "ismeterConnectedToSolarModule": fromindex =
                        activeSteps.indexOf(ismeterConnectedToSolarModule)+1;
                    toindex = activeSteps.indexOf(canTroubleShootBeerformed);
                    break;
                case "verifyPowerOutputMeterConnection": fromindex =
                        activeSteps.indexOf(verifyPowerOutputMeterConnection) +1;
                    toindex = activeSteps.indexOf(additionalWork);
                    updateAdditionalDataValue(false,"SOLAR_PANEL_CONNECTED_TO_METER");
                    break;
                case "verifyPowerOutput":fromindex = toindex;
                    updateAdditionalDataValue(false,"INVERTER_OUTPUT_OK");
                    break;
                case  "ismeterConnectinWorking":fromindex = toindex;
                    updateAdditionalDataValue(false,"METER_CONNECTION_WORKING");
                    break;

            }break;

        }
        if (fromindex < toindex ) {
            activeSteps.subList(fromindex, toindex).clear();
        }
        if(stepsToAdd != null){
            activeSteps.addAll(fromindex, stepsToAdd);
        }
        getSteppersView().getSteppersAdapter().notifyDataSetChanged();
    }

    private void updateAdditionalDataValue(boolean status, String additionalDataT) {

        List<WoAdditionalDataTTO> woAdditionalDataTTOList =
                ObjectCache.getAllIdObjects(WoAdditionalDataTTO.class);

        WoAdditionalDataTTO mWoAdditionalDataTTO=null;
        for (WoAdditionalDataTTO woAdditionalDataTTO :woAdditionalDataTTOList){

            if(woAdditionalDataTTO.getCode().trim().equals(additionalDataT)){
                mWoAdditionalDataTTO = woAdditionalDataTTO;
                break;
            }
        }
        WoAdditionalDataCustomTO woAdditionalDataCustomTO =
                WorkorderUtils.createAdditionalData(workorderCustomWrapperTO.getIdCase(),status ? "YES":
                "NO",mWoAdditionalDataTTO.getId());
        if (workorderCustomWrapperTO.getWorkorderCustomTO().getAdditionalDataList() == null){

            List<WoAdditionalDataCustomTO> woAdditionalDataCustomTOS = new ArrayList<>();
            workorderCustomWrapperTO.getWorkorderCustomTO().setAdditionalDataList(woAdditionalDataCustomTOS);
        }
        workorderCustomWrapperTO.getWorkorderCustomTO().getAdditionalDataList().add(woAdditionalDataCustomTO);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final WorkorderAdditionalProcessingCallbackListener eventReasonResetProcessor = new WorkorderAdditionalProcessingCallbackListener() {
            @Override
            public void process(WorkorderCustomWrapperTO workOrder, Object... objects) {
                try {
                    WoEventCustomTO woEventCustomTO = WorkorderUtils.getOrCreateWoEventCustomTO(workOrder, AndroidUtilsAstSep.CONSTANTS().WO_EVENT_T__FIELD_VISIT, AndroidUtilsAstSep.CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA);
                    if (workOrder.getWorkorderCustomTO().getWoEvents() != null
                            && woEventCustomTO != null && woEventCustomTO.getResultReasons() != null) {
                        woEventCustomTO.getResultReasons().clear();
                        woEventCustomTO.getWoEventTO().setId(null);
                        woEventCustomTO.getWoEventTO().setIdWoEventResultT(null);
                        woEventCustomTO.getWoEventTO().setEventTimestamp(new Date());
                        List<WoInfoCustomTO> infoList = workOrder.getWorkorderCustomTO().getInfoList();
                        WoInfoCustomTO temp = null;
                        if (infoList != null) {
                            for (WoInfoCustomTO woInfoCustomTO : infoList) {
                                if (woInfoCustomTO.getInfo().getIdInfoT().equals(AndroidUtilsAstSep.CONSTANTS().INFO_T__INFO_TO_UTILITY)) {
                                    temp = woInfoCustomTO;
                                }

                            }
                            if (temp != null)
                                infoList.remove(temp);
                        }
                        workOrder.getWorkorderCustomTO().setInfoList(infoList);
                    }
                } catch (Exception e) {
                    writeLog("WorkOrderEventReasonResetProcessor  :process() ", e);
                }
            }
        };


        StepFactory stepFactory = new StepFactory(this);
        String[] arguments = new String[]{resources.getString(R.string.is_location_accessible),
                resources.getString(R.string.is_location_accessible)+"?"};

        isLocationAccessible =stepFactory.createStep(StepTypeConstants.YESNOSTEP,
                "isLocationAccessible",arguments);
        ((YesNoFragment)isLocationAccessible.getFragment()).setProcessingCallbackListener(eventReasonResetProcessor);


        arguments = new String[]{resources.getString(R.string.accessibility)};
        registerAccesibility = stepFactory.createStep(StepTypeConstants.METERACCESSIBLE,
                "registerAccessibility",arguments);
        verificationSteps.add(registerAccesibility);

        arguments = new String[]{resources.getString(R.string.verify_existing_solar_panels)};
         verifySolarPanel =stepFactory.createStep(StepTypeConstants.VERIFY_EXISTING_SOLAR_PANEL,
                 "verifySolarPanel",arguments);
         //verificationSteps.add(verifySolarPanel);

        arguments = new String[]{resources.getString(R.string.verify_existing_inverters)};
         verifyInverter = stepFactory.createStep(StepTypeConstants.VERIFY_EXISTING_SOLAR_PANEL,
                 "verifyInverter",arguments);
         //verificationSteps.add(verifyInverter);

        arguments = new String[]{resources.getString(R.string.verify_existing_batteries)};
         verifyBattery = stepFactory.createStep(StepTypeConstants.VERIFY_EXISTING_SOLAR_PANEL,"verifyBattery",arguments);
         //verificationSteps.add(verifyBattery);

        arguments = new String[]{resources.getString(R.string.is_meter_connected),
                resources.getString(R.string.is_meter_connected)+"?"};
        ismeterConnectedToSolarModule = stepFactory.createStep(StepTypeConstants.YESNOSTEP,
                "ismeterConnectedToSolarModule",arguments);
        verificationSteps.add(ismeterConnectedToSolarModule);

         {
             arguments = new String[]{resources.getString(R.string.verify_install_info)};
             verifyInstallationInfo =
                     stepFactory.createStep(StepTypeConstants.VERIFYMETERINSTALLINFO,
                             "verifyInstallation",arguments);
             verifymeasurePointSteps.add(verifyInstallationInfo);

             arguments = new String[]{resources.getString(R.string.verify_install_info)};
             verifyPhaseKey = stepFactory.createStep(StepTypeConstants.VERIFYPHASEKEY,
                     "verifyPhaseKey",arguments);
             verifymeasurePointSteps.add(verifyPhaseKey);

             arguments = new String[]{resources.getString(R.string.verify_installation_coordinate_verify_coordinate)};
             verifyCoordinates = stepFactory.createStep(StepTypeConstants.REGISTERCORDS,
                     "verifyCords",arguments);
             verifymeasurePointSteps.add(verifyCoordinates);

         }

        arguments = new String[]{resources.getString(R.string.can_troubleshoot_be_performed),
                resources.getString(R.string.can_troubleshoot_be_performed)+"?"};
        canTroubleShootBeerformed = stepFactory.createStep(StepTypeConstants.YESNOSTEP,
                "canTroubleshootbePerformed",arguments);
        verificationSteps.add(canTroubleShootBeerformed);


        arguments = new String[]{resources.getString(R.string.replace_solar_panels),
                String.valueOf(CONSTANTS().WO_UNIT_T__SOLAR_PANEL)};
        replaceSolarPanel = stepFactory.createStep(StepTypeConstants.REPLACE_UNIT,
                "replaceSolarPanel",arguments);
        troubleShootSteps.add(replaceSolarPanel);

        arguments = new String[]{resources.getString(R.string.replace_inverter),
                String.valueOf(CONSTANTS().WO_UNIT_T__INVERTER)};
        replaceInverter = stepFactory.createStep(StepTypeConstants.REPLACE_UNIT,"replaceInverter"
                ,arguments);
        troubleShootSteps.add(replaceInverter);

        arguments = new String[]{resources.getString(R.string.replace_battery),
                String.valueOf(CONSTANTS().WO_UNIT_T__BATTERY)};
        replaceBattery = stepFactory.createStep(StepTypeConstants.REPLACE_UNIT,"replaceBattery",
                arguments);
        troubleShootSteps.add(replaceBattery);

        arguments = new String[]{resources.getString(R.string.verify_power_status_header),
                resources.getString(R.string.is_inverter_out_put_ok)};//
        verifyPowerOutput = stepFactory.createStep(StepTypeConstants.YESNOSTEP,
                "verifyPowerOutput",arguments);
        troubleShootSteps.add(verifyPowerOutput);

        arguments =  new String[]{resources.getString(R.string.verify_power_status_header),
                resources.getString(R.string.is_solar_panel_to_be_connected_to_meter)};
        verifyPowerOutputMeterConnection = stepFactory.createStep(StepTypeConstants.YESNOSTEP,
                "verifyPowerOutputMeterConnection",arguments);
        troubleShootSteps.add(verifyPowerOutputMeterConnection);

        arguments =  new String[]{resources.getString(R.string.verify_power_status_header),
                resources.getString(R.string.is_meter_connection_working)};
        ismeterConnectinWorking = stepFactory.createStep(StepTypeConstants.YESNOSTEP,
                "ismeterConnectinWorking",arguments);



        arguments = new String[]{resources.getString(R.string.additional_work)};
        additionalWork = stepFactory.createStep(StepTypeConstants.ADDITIONALWORK,"additionalWork"
                ,arguments);
        troubleShootSteps.add(additionalWork);


        arguments = new String[]{resources.getString(R.string.menu_attach_photo)};
        attachPhoto = stepFactory.createStep(StepTypeConstants.ATTACHPHOTO,"attachPhoto",arguments);

        arguments = new String[]{resources.getString(R.string.register_time_title)};
        registertime = stepFactory.createStep(StepTypeConstants.REGISTERTIME,"registertime",
                arguments);

        arguments = new String[]{resources.getString(R.string.register_external_internal_comments)};
        registerEIComments =
                stepFactory.createStep(StepTypeConstants.REGISTERINTERNALEXTERNALCOMMENTS,
                        "registerEIComments",arguments);


        arguments = new String[]{resources.getString(R.string.notaccessible)};
        registerNonAccesibility = stepFactory.createStep(StepTypeConstants.METERNOTACCESSIBLE,
                "registerNonAccesibility",arguments);

        arguments = new String[]{resources.getString(R.string.notaccessible)};
        registerNonAccessibleReasons =
                stepFactory.createStep(StepTypeConstants.NOTACCESSIBLEREASON,
                        "registerNonAccessibleReasons",arguments);

        arguments = new String[]{resources.getString(R.string.not_possible)};
        registerNotPerformedReasons =stepFactory.createStep(StepTypeConstants.CANNOTPERFORMREASON
                ,"registerNotPerformedReasons",arguments);

        stepIdWithStepItem = stepFactory.stepIdWithStepItem;


    }
}
