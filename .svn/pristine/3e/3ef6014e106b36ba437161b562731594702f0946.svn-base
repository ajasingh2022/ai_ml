package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AbortUtil;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.ClientFuseSizeTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.InstMepMeterPlmtTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoFuseTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstElTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstMeasurepointCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstMepElCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by svadera on 2/21/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class VerifyMeterInstallInfoFragment extends CustomFragment {

    protected AbortUtil abortUtil = new AbortUtil();
    transient static final String METER_PLACEMENT = "METER_PLACEMENT";
    transient static final String FUSE_SIZE = "FUSE_SIZE";
    transient static final String PLINTH_NUMBER = "PLINTH_NUMBER";
    private static String TAG = VerifyMeterInstallInfoFragment.class.getSimpleName();
    /*
     * Widgets for METER PLACEMENT
     */
    protected transient TextView existingMeterPlacementTv = null;
    protected transient Spinner placementTypeSpinner = null;
    /*
     * Widgets for PLINT NUMBER
     */
    protected transient TextView existingPlinthNumber = null;
    protected transient TextView editPlinthNumber = null;
    /*
     * Widgets for FUSE AMPERE
     */
    protected transient TextView existingFuseAmp = null;
    protected transient Spinner fuseAmpSpinner = null;

    public VerifyMeterInstallInfoFragment() {
        super();
    }

    public VerifyMeterInstallInfoFragment(String stepId) {
        super(stepId);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
       fragmentView = inflater.inflate(R.layout.flow_fragment_verify_install_info_new, null);
        initializeWidgets(fragmentView);
        setupListeners();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    protected void initializeWidgets(View parentView) {

        if (parentView != null) {
                /*
                 * Widgets for METER PLACEMENT
				 */

            placementTypeSpinner = parentView.findViewById(R.id.placementTypeSpinner);
            existingMeterPlacementTv = (TextView) parentView.findViewById(R.id.existingMeterPlacementTv);
                /*
                 * Widgets for PLINT NUMBER
				 */
            editPlinthNumber = parentView.findViewById(R.id.editPlinthNumber);
            existingPlinthNumber = (TextView) parentView.findViewById(R.id.existingPlinthNumber);
                /*
                 * Widgets for FUSE AMPERE
				 */
            existingFuseAmp = (TextView) parentView.findViewById(R.id.existingFuseAmp);
            fuseAmpSpinner = parentView.findViewById(R.id.fuseAmpSpinner);
        }
    }

    private int getPostitonMeterPlacementSpinner(String locationid, ArrayList<InstMepMeterPlmtTTO> arrayList) {
        int position = 0;
        int i;
        try {
            arrayList.iterator();
            for (i = 0; i < arrayList.size() - 1; i++) {

                InstMepMeterPlmtTTO locationVal = arrayList.get(i);
                if (locationVal.getName().equals(locationid)) {
                    position = i;
                    break;
                } else {
                    position = -1;
                }
                arrayList.iterator();
            }
        } catch (Exception e) {
            writeLog(TAG + ": getPostitonMeterPlacementSpinner()", e);
        }
        return position;
    }

    private int getPostitonFuse(String locationid, ArrayList<ClientFuseSizeTTO> arrayList) {
        int position = 0;
        int i;
        try {
            arrayList.iterator();
            for (i = 0; i < arrayList.size() - 1; i++) {

                ClientFuseSizeTTO locationVal = arrayList.get(i);
                if (locationVal.getName().equals(locationid)) {
                    position = i;
                    break;
                } else {
                    position = -1;
                }
                arrayList.iterator();
            }
        } catch (Exception e) {
            writeLog(TAG + ": getPostitonFuse()", e);
        }
        return position;
    }

    protected void populateData() {
        try {
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            WoInstMeasurepointCustomTO woMepTo = null;
            String fuseSizeOrg = null;
            Long meterPlacementOrg = null;
            String plinthNumberOrg = null;
            WoFuseTO woFuseTO = null;
            if (wo != null
                    && wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoInst() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null) {
                woMepTo = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint();
            }

            if (wo != null
                    && wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoInst() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstElectrical() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstElectrical().getMainFuse() != null) {
                woFuseTO = wo.getWorkorderCustomTO().getWoInst().getInstElectrical().getMainFuse();
            }

            //Meter Placement
            final List<InstMepMeterPlmtTTO> meterPlmtTto = TypeDataUtil.filterTypeDataListByDomain(ObjectCache.getAllTypes(InstMepMeterPlmtTTO.class), WorkorderUtils.getDomainsForWO(wo));
            if (meterPlmtTto != null && !meterPlmtTto.isEmpty()) {
                GuIUtil.setUpSpinner(getContext(), placementTypeSpinner, meterPlmtTto, true, placementTypeSpinner.getOnItemSelectedListener());
            }

            if (woMepTo != null
                    && woMepTo.getWoInstMepTO() != null) {
                final WoInstMepTO woInstMepTo = woMepTo.getWoInstMepTO();
                if (woInstMepTo != null) {
                    meterPlacementOrg = woInstMepTo.getIdInstMepMeterPlmtTO();
                    if (null != meterPlacementOrg) {
                        InstMepMeterPlmtTTO plmtTto = ObjectCache.getIdObject(InstMepMeterPlmtTTO.class, meterPlacementOrg);
                        if (plmtTto != null) {
                            existingMeterPlacementTv.setText(plmtTto.getName());
                            int placementTypeSpinnerValue = getPostitonMeterPlacementSpinner(plmtTto.getName(), (ArrayList) meterPlmtTto) + 1;
                            placementTypeSpinner.setSelection(placementTypeSpinnerValue);


                        }
                    }
                }
            }

            //Plinth Number
            if (woMepTo != null
                    && woMepTo.getWoInstMepElCustomTO() != null) {
                plinthNumberOrg = woMepTo.getWoInstMepElCustomTO().getPlintNumberO();
                if (null != plinthNumberOrg) {
                    existingPlinthNumber.setText(plinthNumberOrg);
                    editPlinthNumber.setText(plinthNumberOrg);
                }
            }

            //Fuse Size
            final List<ClientFuseSizeTTO> clientFuseSizeTto = TypeDataUtil.filterTypeDataListByDomain(ObjectCache.getAllTypes(ClientFuseSizeTTO.class), WorkorderUtils.getDomainsForWO(wo));
            if (clientFuseSizeTto != null && !clientFuseSizeTto.isEmpty()) {
                GuIUtil.setUpSpinner(getContext(), fuseAmpSpinner, clientFuseSizeTto, true, fuseAmpSpinner.getOnItemSelectedListener());
            }

            if (woFuseTO != null) {
                fuseSizeOrg = woFuseTO.getFuseSizeO();
                existingFuseAmp.setText(fuseSizeOrg);
                int fuseSpinnerValue = getPostitonFuse(fuseSizeOrg, (ArrayList) clientFuseSizeTto) + 1;
                fuseAmpSpinner.setSelection(fuseSpinnerValue);
            }

            if (!stepfragmentValueArray.isEmpty()) {
                if (stepfragmentValueArray.get(METER_PLACEMENT) != null) {
                    placementTypeSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(placementTypeSpinner, Long.parseLong(stepfragmentValueArray.get(METER_PLACEMENT).toString())));
                }
                if (stepfragmentValueArray.get(PLINTH_NUMBER) != null) {
                    editPlinthNumber.setText(stepfragmentValueArray.get(PLINTH_NUMBER).toString());
                }
                if (stepfragmentValueArray.get(FUSE_SIZE) != null) {
                    fuseAmpSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(fuseAmpSpinner, Long.parseLong(stepfragmentValueArray.get(FUSE_SIZE).toString())));
                }
            }

        } catch (Exception e) {
            writeLog(TAG + ": populateData()", e);
        }
    }

    @Override
    public boolean validateUserInput() {
        stepfragmentValueArray.put(PLINTH_NUMBER, editPlinthNumber.getText().toString());
        boolean status = false;
        if ((Utils.isNotEmpty(stepfragmentValueArray.equals(placementTypeSpinner))) && (Utils.isNotEmpty(stepfragmentValueArray.equals(editPlinthNumber))) && (Utils.isNotEmpty(stepfragmentValueArray.equals(fuseAmpSpinner)))) {
            status = true;
            applyChangesToModifiableWO();
        }
        return status;
    }


    @Override
    public void applyChangesToModifiableWO() {
        //undoChanges();
        WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        try {
            WorkorderUtils.saveWoInstMepTO(wo,
                    Long.valueOf((String) stepfragmentValueArray.get(METER_PLACEMENT)),
                    null,//idWoInstMepPowStatusTD,
                    null,//signalStrength,
                    null,//externalControlExists,
                    null,//externalControlConnected,
                    null,//accessibleToEndCustomer,
                    null//direction
            );

            WorkorderUtils.saveWoInstMepElTO(wo,
                    null,//idCurrentTransformerPrimary,
                    null,//idCurrentTransformerSecondary,
                    (String) stepfragmentValueArray.get(PLINTH_NUMBER),
                    null,//idPlintControl,
                    null,//idFuseSocketControl,
                    null //meterConstant
            );
            WorkorderUtils.saveInstElectricalMainFuse(
                    wo,
                    (String) stepfragmentValueArray.get(FUSE_SIZE),
                    null, //idFusePhysicalSizeTD,
                    null //idFuseT
            );
        } catch (Exception e) {
            writeLog(TAG + ": applyChangesToModifiableWO()", e);
        }


    }


    public void undoChanges() {
        try {
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            wo.getWorkorderCustomTO().getWoInst().setWoInstTO((WoInstTO) abortUtil.getRestoreTOs(WoInstTO.class));
            wo.getWorkorderCustomTO().getWoInst().getInstElectrical().setWoInstElTO((WoInstElTO) abortUtil.getRestoreTOs(WoInstElTO.class));
            wo.getWorkorderCustomTO().getWoInst().getInstElectrical().setMainFuse((WoFuseTO) abortUtil.getRestoreTOs(WoFuseTO.class));
            wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().setWoInstMepElCustomTO((WoInstMepElCustomTO) abortUtil.getRestoreTOs(WoInstMepElCustomTO.class));
            wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().setWoInstMepTO((WoInstMepTO) abortUtil.getRestoreTOs(WoInstMepTO.class));
        } catch (Exception e) {
            writeLog(TAG + ": undoChanges()", e);
        }
    }


    protected void setupListeners() {
        try {
            placementTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    stepfragmentValueArray.put(METER_PLACEMENT, String.valueOf(id));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            fuseAmpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    stepfragmentValueArray.put(FUSE_SIZE, String.valueOf(id));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception e) {
            writeLog(TAG + " : setupListeners()", e);
        }

    }


}
