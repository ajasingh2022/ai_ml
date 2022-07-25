package com.capgemini.sesp.ast.android.ui.wo;

import android.app.Activity;
import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;

import com.capgemini.sesp.ast.android.ui.wo.stepperItem.AdditionalWorkPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.ApplianceLoadControlFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.CannotPerformReasonPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.ChangeMeterReasonPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.CommunicationFailureReasonPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.ConfirmCTRatioFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.ConfirmDisconnectionServiceLineFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.ConfirmNetStationInstallationLocationPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.ConfirmRemoveUnitsPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.EvChargerInformationFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.ExistingMeterTechPlanPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.MasterMeterInfoPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.MeterAccessibilityFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.MeterInstallationCheckFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.MeterNotAccessibleFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.MeterPowerCheckPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.NewConcentratorTechPlanPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.NewMeterTechPlanPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.NewRepeaterTechPlanPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.NotAccessibleReasonPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.OldMeterChangeYesNoPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.OldMeterPowerCheckPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.OldMeterReadingPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.OnlineVerificationPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.PrintLabelFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.QualityControlFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.ReadMeterIndiNewMeterFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterCordsFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterExternAntennaAsPerTechPlanFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterExternalInternalCommentFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterMultipointInfoFusePageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterMultipointInformationPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterNewCommModulePageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterNewEvChargerInfoFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterNewExternAntennaFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterNewSimCardInfoPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterProdMeterReadingPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RegisterTimePageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.ReplaceUnitFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.RiskAssessmentFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.SaveWorkorderPageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StandardWoAttachFileFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StandardWoAttachImageFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StepTypeConstants;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.VerifyCurrentTransfermerFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.VerifyMeterInstallInfoFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.VerifyPhaseKeyFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.YesNoFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.EditableInfoFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.NewBatteryFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.NewInverterFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.PanelInstallationFragement;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.ScanMismatchedValueFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.SolarCheckListFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.VerifyExistingSolarPanels;

import me.drozdzynski.library.steppers.SteppersItem;
import me.drozdzynski.library.steppers.interfaces.OnClickContinue;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class StepFactory {

    public interface ActivityConnectingInterface {
        void chooseYesNoResponse(String stepid, String responseCode);

        void meterAccesibiltyResponse(String stepId, String responseCode);

        void meterInstallationCheckResponse(String stepId, String responseCode);

        void registerCordsFragmentResponse(String stepId, String responseCode);

        void oldMeterChangeYesNoFragmentResponse(String stepId, String responseCode);

        void newMeterTechPlanPageFragmentResponse(String stepId, String responseCode);

        void registerExternAntennaAsPerTechPlanFragmentResponse(String stepId, String responseCode);

        void MasterMeterInfoPageFragmentResponse(String steppersItemIdentifier, String responseCode);

        void registerNewSimCardInfoPageFragmentResponse(String steppersItemIdentifier, String responseCode);

        void onlineVerificationPageFragmentResponse(String steppersItemIdentifier, String responseCode);

        void oldMeterPowerCheckPageFragmentResponse(String steppersItemIdentifier, String responseCode);

        void registerNewCommunicationModulePageFragmentResponse(String steppersItemIdentifier, String responseCode);

        void newConcentratorTechPlanPageFragmentResponse(String steppersItemIdentifier, String responseCode);

        void newRepeaterTechPlanPageFragmentResponse(String steppersItemIdentifier, String responseCode);

        void registerNewEvChargerInfoFragmentResponse(String steppersItemIdentifier, String responseCode);



    }


    Activity activity;
    ActivityConnectingInterface activityConnectingInterface;
    AbstractWokOrderActivity abstractWokOrderActivity;
    public ArrayMap<String, SteppersItem> stepIdWithStepItem;


    public StepFactory(Context context) {

        activity = (Activity) context;
        activityConnectingInterface = (ActivityConnectingInterface) activity;
        abstractWokOrderActivity = (AbstractWokOrderActivity) activity;
        stepIdWithStepItem = new ArrayMap<>();

    }

    public SteppersItem createStep(String stepType, String stepId, String[] argv) {
        String stepLabel = argv[0];
        final SteppersItem steppersItem = new SteppersItem();
        steppersItem.setLabel(stepLabel);
        steppersItem.setSteppersItemIdentifier(stepId);
        try {

            if (stepType == StepTypeConstants.CONFIRMNETSTATIONINSTALLATIONLOCATION) {
                ConfirmNetStationInstallationLocationPageFragment confirmNetStationInstallationLocationPageFragment;
                confirmNetStationInstallationLocationPageFragment = new ConfirmNetStationInstallationLocationPageFragment(stepId);
                steppersItem.setFragment(confirmNetStationInstallationLocationPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = confirmNetStationInstallationLocationPageFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            confirmNetStationInstallationLocationPageFragment.showPromptUserAction();
                        }

                    }
                });
            } else if (stepType == StepTypeConstants.REGISTERMULTIPOINTINFO) {
                RegisterMultipointInformationPageFragment registerMultipointInformationPageFragment;
                registerMultipointInformationPageFragment = new RegisterMultipointInformationPageFragment(stepId);
                steppersItem.setFragment(registerMultipointInformationPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = registerMultipointInformationPageFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            registerMultipointInformationPageFragment.showPromptUserAction();
                        }

                    }
                });
            } else if (stepType == StepTypeConstants.RISKASSESSMENT) {
                RiskAssessmentFragment riskAssessmentFragment;
                riskAssessmentFragment = new RiskAssessmentFragment(stepId);
                steppersItem.setFragment(riskAssessmentFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = riskAssessmentFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            riskAssessmentFragment.showPromptUserAction();
                        }

                    }
                });
            } else if (stepType == StepTypeConstants.REGISTERMULTIPOINTFUSEINFO) {
                RegisterMultipointInfoFusePageFragment registerMultipointInfoFusePageFragment;
                registerMultipointInfoFusePageFragment = new RegisterMultipointInfoFusePageFragment(stepId);
                steppersItem.setFragment(registerMultipointInfoFusePageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = registerMultipointInfoFusePageFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            registerMultipointInfoFusePageFragment.showPromptUserAction();
                        }

                    }
                });
            } else if (stepType == StepTypeConstants.NEWCONCENTRATORTECHPLAN) {
                NewConcentratorTechPlanPageFragment newConcentratorTechPlanPageFragment = new NewConcentratorTechPlanPageFragment(stepId, Boolean.parseBoolean(argv[1]));
                steppersItem.setFragment(newConcentratorTechPlanPageFragment);

                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {

                        boolean validationStatus = newConcentratorTechPlanPageFragment.validateUserInput();
                        if (validationStatus) {
                            activityConnectingInterface.newConcentratorTechPlanPageFragmentResponse(steppersItem.getSteppersItemIdentifier(), newConcentratorTechPlanPageFragment.evaluateNextPage());
                            steppersItem.setDone(true);
                            //meterInstallationCheckFragment.persistDataForWorkOrderResume();
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            newConcentratorTechPlanPageFragment.showPromptUserAction();
                        }


                    }

                });

            } else if (stepType == StepTypeConstants.NEWREPEATERTECHPLAN) {
                NewRepeaterTechPlanPageFragment newRepeaterTechPlanPageFragment = new NewRepeaterTechPlanPageFragment(stepId, Boolean.parseBoolean(argv[1]));
                steppersItem.setFragment(newRepeaterTechPlanPageFragment);

                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {

                        boolean validationStatus = newRepeaterTechPlanPageFragment.validateUserInput();
                        if (validationStatus) {
                            activityConnectingInterface.newRepeaterTechPlanPageFragmentResponse(steppersItem.getSteppersItemIdentifier(), newRepeaterTechPlanPageFragment.evaluateNextPage());
                            steppersItem.setDone(true);
                            //meterInstallationCheckFragment.persistDataForWorkOrderResume();
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            newRepeaterTechPlanPageFragment.showPromptUserAction();
                        }


                    }

                });

            } else if (stepType == StepTypeConstants.EXISTINGMETERTECHPLAN) {
                ExistingMeterTechPlanPageFragment existingMeterTechPlanPageFragment;
                existingMeterTechPlanPageFragment = new ExistingMeterTechPlanPageFragment(stepId);
                steppersItem.setFragment(existingMeterTechPlanPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {

                        boolean validationStatus = existingMeterTechPlanPageFragment.validateUserInput();
                        if (validationStatus) {
                            activityConnectingInterface.newMeterTechPlanPageFragmentResponse(steppersItem.getSteppersItemIdentifier(), existingMeterTechPlanPageFragment.evaluateNextPage());
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            existingMeterTechPlanPageFragment.showPromptUserAction();
                        }


                    }
                });
            } else if (stepType == StepTypeConstants.COMMUNICATIONFAILUREREASON) {
                final CommunicationFailureReasonPageFragment communicationFailureReasonPageFragment;
                if (argv.length == 3)
                    communicationFailureReasonPageFragment = new CommunicationFailureReasonPageFragment(stepId, Long.parseLong(argv[1]), argv[2]);
                else
                    communicationFailureReasonPageFragment = new CommunicationFailureReasonPageFragment(stepId, Long.parseLong(argv[1]));
                steppersItem.setFragment(communicationFailureReasonPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationStatus = communicationFailureReasonPageFragment.validateUserInput();
                        if (validationStatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            communicationFailureReasonPageFragment.showPromptUserAction();
                        }


                    }
                });
            } else if (stepType == StepTypeConstants.CHANGEMETERREASON) {
                ChangeMeterReasonPageFragment changeMeterReasonPageFragment = new ChangeMeterReasonPageFragment(stepId);
                steppersItem.setFragment(changeMeterReasonPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (changeMeterReasonPageFragment.validateUserInput()) {
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else changeMeterReasonPageFragment.showPromptUserAction();
                    }
                });

            }
            //1
            else if (stepType == StepTypeConstants.YESNOSTEP) {
                final YesNoFragment yesNoFragment;
                if (argv.length == 2) {
                    yesNoFragment = new YesNoFragment(stepId, argv[1]);
                } else {
                    yesNoFragment = new YesNoFragment();
                }
                steppersItem.setFragment(yesNoFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationStatus = yesNoFragment.validateUserInput();
                        if (validationStatus) {
                            String yesNoValue = yesNoFragment.getYesNoValue();
                            //String yesNoValue = String.valueOf(yesNoFragment.getRadioYesNoGroup().getCheckedRadioButtonId());
                            activityConnectingInterface.chooseYesNoResponse(steppersItem.getSteppersItemIdentifier(), yesNoValue);
                            steppersItem.setDone(true);
                            //yesNoFragment.persistDataForWorkOrderResume();
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            yesNoFragment.showPromptUserAction();
                        }


                    }
                });

            }

            //2
            else if (stepType == StepTypeConstants.METERINSTALLATIONCHECK) {
                MeterInstallationCheckFragment meterInstallationCheckFragment = new MeterInstallationCheckFragment(stepId);
                steppersItem.setFragment(meterInstallationCheckFragment);

                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {

                        boolean validationStatus = meterInstallationCheckFragment.validateUserInput();
                        if (validationStatus) {
                            activityConnectingInterface.meterInstallationCheckResponse(steppersItem.getSteppersItemIdentifier(), meterInstallationCheckFragment.evaluateNextPage());
                            steppersItem.setDone(true);
                            //meterInstallationCheckFragment.persistDataForWorkOrderResume();
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            meterInstallationCheckFragment.showPromptUserAction();
                        }


                    }

                });

            }

            //3
            else if (stepType == StepTypeConstants.METERPOWERCHECKPAGE) {
                MeterPowerCheckPageFragment meterPowerCheckPageFragment = new MeterPowerCheckPageFragment(stepId);
                steppersItem.setFragment(meterPowerCheckPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (meterPowerCheckPageFragment.validateUserInput()) {
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else meterPowerCheckPageFragment.showPromptUserAction();
                    }
                });

            } else if (stepType == StepTypeConstants.PRINTLABEL) {
                PrintLabelFragment printLabelFragment = new PrintLabelFragment(stepId);
                steppersItem.setFragment(printLabelFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (printLabelFragment.validateUserInput()) {
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else printLabelFragment.showPromptUserAction();
                    }
                });

            } else if (stepType == StepTypeConstants.METERNOTACCESSIBLE) {
                final MeterNotAccessibleFragment meterNotAccessibleFragment;
                meterNotAccessibleFragment = new MeterNotAccessibleFragment(stepId);
                steppersItem.setFragment(meterNotAccessibleFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationStatus = meterNotAccessibleFragment.validateUserInput();
                        if (validationStatus) {
                            String yesNoValue = String.valueOf(meterNotAccessibleFragment.getRadiogroupmtr().getCheckedRadioButtonId());
                            activityConnectingInterface.meterAccesibiltyResponse(steppersItem.getSteppersItemIdentifier(), yesNoValue);
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            meterNotAccessibleFragment.showPromptUserAction();
                        }


                    }
                });

            }

            //4
            else if (stepType == StepTypeConstants.REGISTERCORDS) {
                RegisterCordsFragment registerCordsFragment;
                registerCordsFragment = new RegisterCordsFragment(stepId);
                steppersItem.setFragment(registerCordsFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {

                        boolean validationStatus = registerCordsFragment.validateUserInput();
                        if (validationStatus) {
                            activityConnectingInterface.registerCordsFragmentResponse(steppersItem.getSteppersItemIdentifier(), registerCordsFragment.evaluateNextPage());
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            registerCordsFragment.showPromptUserAction();
                        }
                    }
                });
            }

            //5
            else if (stepType == StepTypeConstants.OLDMETERCHANGEYESNO) {
                OldMeterChangeYesNoPageFragment oldmeterchangeyesNo;
                oldmeterchangeyesNo = new OldMeterChangeYesNoPageFragment(stepId);

                steppersItem.setFragment(oldmeterchangeyesNo);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationStatus = oldmeterchangeyesNo.validateUserInput();
                        if (validationStatus) {
                            activityConnectingInterface.oldMeterChangeYesNoFragmentResponse(steppersItem.getSteppersItemIdentifier(), oldmeterchangeyesNo.evaluateNextPage());
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            oldmeterchangeyesNo.showPromptUserAction();
                        }

                    }
                });
            }
            //6

            else if (stepType == StepTypeConstants.REGISTEREXTERNANTENNAASPERTECHNICIANPLAN) {
                final RegisterExternAntennaAsPerTechPlanFragment registerExternAntennaAsPerTechPlanFragment;
                registerExternAntennaAsPerTechPlanFragment = new RegisterExternAntennaAsPerTechPlanFragment(stepId);
                steppersItem.setFragment(registerExternAntennaAsPerTechPlanFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = registerExternAntennaAsPerTechPlanFragment.validateUserInput();
                        if (validationstatus) {
                            activityConnectingInterface.registerExternAntennaAsPerTechPlanFragmentResponse(steppersItem.getSteppersItemIdentifier(), registerExternAntennaAsPerTechPlanFragment.evaluateNextPage());
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            registerExternAntennaAsPerTechPlanFragment.showPromptUserAction();
                        }
                    }
                });

            } else if (stepType == StepTypeConstants.NEWMETERTECHPLAN) {
                NewMeterTechPlanPageFragment newtmetertechplanPage;
                newtmetertechplanPage = new NewMeterTechPlanPageFragment(stepId);
                steppersItem.setFragment(newtmetertechplanPage);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {

                        boolean validationStatus = newtmetertechplanPage.validateUserInput();
                        if (validationStatus) {
                            activityConnectingInterface.newMeterTechPlanPageFragmentResponse(steppersItem.getSteppersItemIdentifier(), newtmetertechplanPage.evaluateNextPage());
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            newtmetertechplanPage.showPromptUserAction();
                        }


                    }
                });
            } else if (stepType == StepTypeConstants.NOTACCESSIBLEREASON) {
                NotAccessibleReasonPageFragment notAccessibleReasonPageFragment;
                notAccessibleReasonPageFragment = new NotAccessibleReasonPageFragment(stepId, CONSTANTS().PDA_PAGE_T__NOT_ACCESSIBLE);
                steppersItem.setFragment(notAccessibleReasonPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = notAccessibleReasonPageFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            notAccessibleReasonPageFragment.showPromptUserAction();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.CANNOTPERFORMREASON) {
                CannotPerformReasonPageFragment cannotPerformReasonPageFragment;
                if (argv.length == 2) {
                    cannotPerformReasonPageFragment = new CannotPerformReasonPageFragment(stepId, CONSTANTS().PDA_PAGE_T__NOT_POSSIBLE, argv[1]);
                } else {
                    cannotPerformReasonPageFragment = new CannotPerformReasonPageFragment(stepId, CONSTANTS().PDA_PAGE_T__NOT_POSSIBLE);
                }
                // cannotPerformReasonPageFragment = new CannotPerformReasonPageFragment(stepId, CONSTANTS().PDA_PAGE_T__NOT_POSSIBLE);
                steppersItem.setFragment(cannotPerformReasonPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = cannotPerformReasonPageFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            cannotPerformReasonPageFragment.showPromptUserAction();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.VERIFYPHASEKEY) {
                final VerifyPhaseKeyFragment verifyPhaseKeyFragment;
                verifyPhaseKeyFragment = new VerifyPhaseKeyFragment(stepId);
                steppersItem.setFragment(verifyPhaseKeyFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = verifyPhaseKeyFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            verifyPhaseKeyFragment.showPromptUserAction();
                        }
                    }
                });

            } else if (stepType == StepTypeConstants.CONFORMDISCONNECTIONSERVICELINE) {
                final ConfirmDisconnectionServiceLineFragment confirmDisconnectionServiceLine;
                confirmDisconnectionServiceLine = new ConfirmDisconnectionServiceLineFragment(stepId);
                steppersItem.setFragment(confirmDisconnectionServiceLine);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = confirmDisconnectionServiceLine.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            confirmDisconnectionServiceLine.showPromptUserAction();
                        }
                    }
                });

            } else if (stepType == StepTypeConstants.CONFORMREMOVEDUNITS) {
                final ConfirmRemoveUnitsPageFragment confirmRemoveUnitsPageFragment;
                confirmRemoveUnitsPageFragment = new ConfirmRemoveUnitsPageFragment(stepId);
                steppersItem.setFragment(confirmRemoveUnitsPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = confirmRemoveUnitsPageFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            confirmRemoveUnitsPageFragment.showPromptUserAction();
                        }
                    }
                });

            } else if (stepType == StepTypeConstants.VERIFYCURRENTTRANSFORMER) {
                final VerifyCurrentTransfermerFragment verifyCurrentTransfermerFragment;
                verifyCurrentTransfermerFragment = new VerifyCurrentTransfermerFragment(stepId);
                steppersItem.setFragment(verifyCurrentTransfermerFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = verifyCurrentTransfermerFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            verifyCurrentTransfermerFragment.showPromptUserAction();
                        }
                    }
                });

            } else if (stepType == StepTypeConstants.METERACCESSIBLE) {
                MeterAccessibilityFragment meterAccessibilityFragment;
                meterAccessibilityFragment = new MeterAccessibilityFragment(stepId);
                steppersItem.setFragment(meterAccessibilityFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = meterAccessibilityFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            //meterAccessibilityFragment.persistDataForWorkOrderResume();
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            meterAccessibilityFragment.showPromptUserAction();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.ONLINEVERIFICATION) {
                final OnlineVerificationPageFragment onlineVerificationPageFragment;
                onlineVerificationPageFragment = new OnlineVerificationPageFragment(stepId);
                steppersItem.setFragment(onlineVerificationPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = onlineVerificationPageFragment.validateUserInput();
                        if (validationstatus) {
                            activityConnectingInterface.onlineVerificationPageFragmentResponse(steppersItem.getSteppersItemIdentifier(), onlineVerificationPageFragment.evaluateNextPage());
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            onlineVerificationPageFragment.showPromptUserAction();
                        }
                    }
                });

            }

            else if (stepType == StepTypeConstants.QUALITYCONTROL) {
                QualityControlFragment qualityControlFragment;
                qualityControlFragment = new QualityControlFragment(stepId);
                steppersItem.setFragment(qualityControlFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = qualityControlFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            qualityControlFragment.showPromptUserAction();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.ATTACHFILE) {
                StandardWoAttachFileFragment standardWoAttachFileFragment;
                standardWoAttachFileFragment = new StandardWoAttachFileFragment(stepId);
                steppersItem.setFragment(standardWoAttachFileFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = standardWoAttachFileFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            standardWoAttachFileFragment.showPromptUserAction();
                        }

                    }
                });
            } else if (stepType == StepTypeConstants.REGISTERNEWSIMCARDINFOPAGE) {
                RegisterNewSimCardInfoPageFragment registerNewSimCardInfoPageFragment;
                registerNewSimCardInfoPageFragment = new RegisterNewSimCardInfoPageFragment(stepId, Long.parseLong(argv[1]));
                steppersItem.setFragment(registerNewSimCardInfoPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = registerNewSimCardInfoPageFragment.validateUserInput();
                        if (validationstatus) {
                            activityConnectingInterface.registerNewSimCardInfoPageFragmentResponse(steppersItem.getSteppersItemIdentifier(), registerNewSimCardInfoPageFragment.evaluateNextPage());
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            registerNewSimCardInfoPageFragment.showPromptUserAction();
                        }

                    }
                });
            } else if (stepType == StepTypeConstants.SAVEWORKORDER) {
                SaveWorkorderPageFragment saveWorkorderPageFragment;
                saveWorkorderPageFragment = new SaveWorkorderPageFragment();
                steppersItem.setFragment(saveWorkorderPageFragment);
            } else if (stepType == StepTypeConstants.ADDITIONALWORK) {
                AdditionalWorkPageFragment additionalWorkPageFragment = new AdditionalWorkPageFragment(stepId);
                steppersItem.setFragment(additionalWorkPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (additionalWorkPageFragment.validateUserInput()) {
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            additionalWorkPageFragment.showPromptUserAction();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.READMETERINDINEWMETER) {

                ReadMeterIndiNewMeterFragment readMeterIndiNewMeterFragment = new ReadMeterIndiNewMeterFragment(stepId, Long.parseLong(argv[1]), Boolean.parseBoolean(argv[2]));
                steppersItem.setFragment(readMeterIndiNewMeterFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (readMeterIndiNewMeterFragment.validateUserInput()) {
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else
                            readMeterIndiNewMeterFragment.showPromptUserAction();
                    }
                });

            } else if (stepType == StepTypeConstants.OLDMETERPOWERCHECK) {

                OldMeterPowerCheckPageFragment oldMeterPowerCheckPageFragment = new OldMeterPowerCheckPageFragment(stepId);
                steppersItem.setFragment(oldMeterPowerCheckPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (oldMeterPowerCheckPageFragment.validateUserInput()) {
                            activityConnectingInterface.oldMeterPowerCheckPageFragmentResponse(steppersItem.getSteppersItemIdentifier(), oldMeterPowerCheckPageFragment.evaluateNextPage());
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else
                            oldMeterPowerCheckPageFragment.showPromptUserAction();
                    }
                });

            } else if (stepType == StepTypeConstants.APPLIANCELOADCONTROL) {
                ApplianceLoadControlFragment applianceLoadControlFragment = new ApplianceLoadControlFragment(stepId);
                steppersItem.setFragment(applianceLoadControlFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (applianceLoadControlFragment.validateUserInput()) {
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else applianceLoadControlFragment.showPromptUserAction();
                    }
                });
            } else if (stepType == StepTypeConstants.REGISTERNEWCOMMUNICATIONMODULE) {
                RegisterNewCommModulePageFragment registerNewCommModulePageFragment;
                registerNewCommModulePageFragment = new RegisterNewCommModulePageFragment(stepId, Long.parseLong(argv[1]));
                steppersItem.setFragment(registerNewCommModulePageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (registerNewCommModulePageFragment.validateUserInput()) {
                            activityConnectingInterface.registerNewCommunicationModulePageFragmentResponse(steppersItem.getSteppersItemIdentifier(), registerNewCommModulePageFragment.evaluateNextPage());
                            abstractWokOrderActivity.getSteppersView().nextStep();

                        } else
                            registerNewCommModulePageFragment.showPromptUserAction();
                    }
                });
            } else if (stepType == StepTypeConstants.REGISTERNEWEXTERNALANTENNA) {
                RegisterNewExternAntennaFragment registerNewExternalAntenna;
                registerNewExternalAntenna = new RegisterNewExternAntennaFragment(stepId);
                steppersItem.setFragment(registerNewExternalAntenna);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (registerNewExternalAntenna.validateUserInput()) {
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.CONFORMCTRATIO) {
                ConfirmCTRatioFragment confirmCTRatioFragment;
                confirmCTRatioFragment = new ConfirmCTRatioFragment(stepId);
                steppersItem.setFragment(confirmCTRatioFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (confirmCTRatioFragment.validateUserInput()) {
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            confirmCTRatioFragment.showPromptUserAction();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.REGISTERPRODMETERREADING) {
                RegisterProdMeterReadingPageFragment registerprodmeterReading;
                registerprodmeterReading = new RegisterProdMeterReadingPageFragment(stepId);
                steppersItem.setFragment(registerprodmeterReading);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        if (registerprodmeterReading.validateUserInput())
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        else
                            registerprodmeterReading.showPromptUserAction();
                    }
                });
            } else if (stepType == StepTypeConstants.MASTERMETERINFOPAGE) {
                MasterMeterInfoPageFragment mastermeterinfopage;
                mastermeterinfopage = new MasterMeterInfoPageFragment(stepId);
                steppersItem.setFragment(mastermeterinfopage);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = mastermeterinfopage.validateUserInput();
                        if (validationstatus) {
                            activityConnectingInterface.MasterMeterInfoPageFragmentResponse(steppersItem.getSteppersItemIdentifier(), mastermeterinfopage.evaluateNextPage());
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            mastermeterinfopage.showPromptUserAction();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.ATTACHPHOTO) {
                final StandardWoAttachImageFragment standardWoAttachImageFragment;
                standardWoAttachImageFragment = new StandardWoAttachImageFragment(stepId);
                steppersItem.setFragment(standardWoAttachImageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = standardWoAttachImageFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            standardWoAttachImageFragment.showPromptUserAction();
                        }
                    }
                });

            } else if (stepType == StepTypeConstants.REGISTERTIME) {
                RegisterTimePageFragment registerTimePageFragment;
                registerTimePageFragment = new RegisterTimePageFragment(stepId);
                steppersItem.setFragment(registerTimePageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = registerTimePageFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            registerTimePageFragment.showPromptUserAction();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.REGISTERINTERNALEXTERNALCOMMENTS) {
                RegisterExternalInternalCommentFragment registerExternalInternalCommentFragment;
                registerExternalInternalCommentFragment = new RegisterExternalInternalCommentFragment(stepId);
                steppersItem.setFragment(registerExternalInternalCommentFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = registerExternalInternalCommentFragment.validateUserInput();
                        steppersItem.setDone(true);
                        abstractWokOrderActivity.getSteppersView().nextStep();

                    }
                });
            } else if (stepType == StepTypeConstants.VERIFYMETERINSTALLINFO) {
                final VerifyMeterInstallInfoFragment verifyMeterInstallInfoFragment;
                verifyMeterInstallInfoFragment = new VerifyMeterInstallInfoFragment(stepId);
                steppersItem.setFragment(verifyMeterInstallInfoFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = verifyMeterInstallInfoFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            verifyMeterInstallInfoFragment.showPromptUserAction();
                        }
                    }
                });

            } else if (stepType == StepTypeConstants.READMETERINDICATION) {
                OldMeterReadingPageFragment oldMeterReadingPageFragment = new OldMeterReadingPageFragment(stepId);
                steppersItem.setFragment(oldMeterReadingPageFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = oldMeterReadingPageFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            oldMeterReadingPageFragment.showPromptUserAction();
                        }
                    }
                });


            } else if (stepType == StepTypeConstants.PANELINSTALLATION) {
                PanelInstallationFragement panelInstallationFragement = new PanelInstallationFragement(stepId);
                steppersItem.setFragment(panelInstallationFragement);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = panelInstallationFragement.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            panelInstallationFragement.showPromptUserAction();
                        }
                    }
                });
            }else if (stepType == StepTypeConstants.SCANMISMATCHEDVALUE) {
                ScanMismatchedValueFragment scanMismatchedValueFragment = new ScanMismatchedValueFragment(stepId);
                steppersItem.setFragment(scanMismatchedValueFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = scanMismatchedValueFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            scanMismatchedValueFragment.showPromptUserAction();
                        }
                    }
                });
            }else if (stepType == StepTypeConstants.VERIFYBATTERY) {
                NewBatteryFragment batteryFragment = new NewBatteryFragment(stepId);
                steppersItem.setFragment(batteryFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = batteryFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            batteryFragment.showPromptUserAction();
                        }
                    }
                });
            }else if (stepType == StepTypeConstants.VERIFYINVERTER) {
                NewInverterFragment newInverterFragment = new NewInverterFragment(stepId);
                steppersItem.setFragment(newInverterFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = newInverterFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            newInverterFragment.showPromptUserAction();
                        }
                    }
                });
            }else if (stepType == StepTypeConstants.SOLARCHECKLIST) {
                SolarCheckListFragment solarCheckListFragment = new SolarCheckListFragment(stepId);
                steppersItem.setFragment(solarCheckListFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = solarCheckListFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            solarCheckListFragment.showPromptUserAction();
                        }
                    }
                });
            }else if (stepType == StepTypeConstants.EDITABLEINFO) {
                EditableInfoFragment editableInfoFragment = new EditableInfoFragment(stepId);
                steppersItem.setFragment(editableInfoFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = editableInfoFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            editableInfoFragment.showPromptUserAction();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.REGISTERNEWEVCHARGERINFO) {
                RegisterNewEvChargerInfoFragment registerNewEvChargerInfoFragment = new RegisterNewEvChargerInfoFragment(stepId);
                steppersItem.setFragment(registerNewEvChargerInfoFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = registerNewEvChargerInfoFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            registerNewEvChargerInfoFragment.showPromptUserAction();
                        }
                    }
                });
            } else if (stepType == StepTypeConstants.EVCHARGERINFORMATION) {
                final EvChargerInformationFragment evChargerInformationFragment;
                evChargerInformationFragment = new EvChargerInformationFragment(stepId);
                steppersItem.setFragment(evChargerInformationFragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = evChargerInformationFragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            evChargerInformationFragment.showPromptUserAction();
                        }
                    }
                });

            }
            else if (stepType == StepTypeConstants.VERIFY_EXISTING_SOLAR_PANEL){
                VerifyExistingSolarPanels fragment =
                        new VerifyExistingSolarPanels(stepId);
                steppersItem.setFragment(fragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = fragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            fragment.showPromptUserAction();
                        }

                    }
                });
            }

            else if (stepType == StepTypeConstants.REPLACE_UNIT){
                ReplaceUnitFragment fragment =
                        new ReplaceUnitFragment(stepId,Long.parseLong(argv[1]));
                steppersItem.setFragment(fragment);
                steppersItem.setOnClickContinue(new OnClickContinue() {
                    @Override
                    public void onClick() {
                        boolean validationstatus = fragment.validateUserInput();
                        if (validationstatus) {
                            steppersItem.setDone(true);
                            abstractWokOrderActivity.getSteppersView().nextStep();
                        } else {
                            fragment.showPromptUserAction();
                        }

                    }
                });
            }

            if (steppersItem.getFragment() == null) {
                Log.d(this.getClass().getSimpleName(), "Fragment for step is missing");
            }

            stepIdWithStepItem.put(steppersItem.getSteppersItemIdentifier(), steppersItem);
        } catch (Exception e) {
            writeLog("StepFactory  : evaluateUnitInstallationNextPage() ", e);
        }
        return steppersItem;

    }


}
