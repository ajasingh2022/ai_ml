package com.capgemini.sesp.ast.android.ui.wo.activity;

import android.os.Bundle;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.WorkorderAdditionalProcessingCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StepTypeConstants;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.YesNoFragment;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.drozdzynski.library.steppers.SteppersItem;


import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nagravi on 14-06-2019.
 */

public class SolarPanelInstallation extends AbstractWokOrderActivity {
    private static String TAG = SolarPanelInstallation.class.getSimpleName();
    private SteppersItem isMeterLocationaccessible;
    private SteppersItem riskAssessment;
    private SteppersItem attachPhoto;
    private SteppersItem registertime;
    private SteppersItem verifyInstallationCoordinates;
    private SteppersItem panelInstallation;
    private SteppersItem registerEIComments;
    private SteppersItem solarCheckList;
    private SteppersItem editableInfoFragment;
    private SteppersItem notAccessibleReason;
    private SteppersItem verifyBattery;
    private SteppersItem verifyInverter;
    private SteppersItem cannotPerformReasons;


    ArrayList<SteppersItem> yesflow = new ArrayList<>();
    ArrayList<SteppersItem> noFlow = new ArrayList<>();
    private int fromindex;
    private int toindex;


    @Override
    public void addAssuredSteps() {
        addToActiveStepIfNotAdded(isMeterLocationaccessible, -1);
        addToActiveStepIfNotAdded(attachPhoto, -1);
        addToActiveStepIfNotAdded(registertime, -1);
        addToActiveStepIfNotAdded(registerEIComments, -1);
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
            writeLog(TAG + "  : addAssuredSteps() ", e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
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
            String[] arguments = new String[]{resources.getString(R.string.location_accessible), resources.getString(R.string.is_location_accessible)};
            isMeterLocationaccessible = stepFactory.createStep(StepTypeConstants.YESNOSTEP, "ChooseYesNo", arguments);
            ((YesNoFragment)isMeterLocationaccessible.getFragment()).setProcessingCallbackListener(eventReasonResetProcessor);

            arguments = new String[]{resources.getString(R.string.risk_assessment_for_your_safety)};
            riskAssessment = stepFactory.createStep(StepTypeConstants.RISKASSESSMENT, "riskAssessment", arguments);

            arguments = new String[]{resources.getString(R.string.register_cords)};
            verifyInstallationCoordinates = stepFactory.createStep(StepTypeConstants.REGISTERCORDS, "verifyInstallationCoordinates", arguments);


            arguments = new String[]{resources.getString(R.string.new_panel)};
            panelInstallation = stepFactory.createStep(StepTypeConstants.PANELINSTALLATION, "panelInstallation", arguments);

            arguments = new String[]{resources.getString(R.string.check_list)};
            solarCheckList = stepFactory.createStep(StepTypeConstants.SOLARCHECKLIST, "solarCheckList", arguments);

            arguments = new String[]{resources.getString(R.string.editable_information)};
            editableInfoFragment = stepFactory.createStep(StepTypeConstants.EDITABLEINFO, "editableInfoFragment", arguments);

            arguments = new String[]{resources.getString(R.string.new_battery)};
            verifyBattery = stepFactory.createStep(StepTypeConstants.VERIFYBATTERY,"verifyBattery",arguments);

            arguments = new String[]{resources.getString(R.string.new_inverter)};
            verifyInverter = stepFactory.createStep(StepTypeConstants.VERIFYINVERTER,"verifyInverter",arguments);

            arguments = new String[]{resources.getString(R.string.notaccessible)};
            notAccessibleReason = stepFactory.createStep(StepTypeConstants.NOTACCESSIBLEREASON, "notAccessibleReason", arguments);
            arguments = new String[]{resources.getString(R.string.connect_photos)};
            attachPhoto = stepFactory.createStep(StepTypeConstants.ATTACHPHOTO, "attachPhoto", arguments);

            arguments = new String[]{resources.getString(R.string.register_time_title)};
            registertime = stepFactory.createStep(StepTypeConstants.REGISTERTIME, "registerTime", arguments);

            arguments = new String[]{resources.getString(R.string.register_external_internal_comments)};
            registerEIComments = stepFactory.createStep(StepTypeConstants.REGISTERINTERNALEXTERNALCOMMENTS, "EIComments", arguments);

            stepIdWithStepItem = stepFactory.stepIdWithStepItem;

            yesflow.add(riskAssessment);
            yesflow.add(verifyInstallationCoordinates);
            yesflow.add(panelInstallation);
            yesflow.add(verifyBattery);
            yesflow.add(verifyInverter);
            yesflow.add(solarCheckList);
            yesflow.add(editableInfoFragment);
            noFlow.add(notAccessibleReason);

        } catch (Exception e) {
            writeLog(TAG + "  : onCreate() ", e);
        }

    }
}
