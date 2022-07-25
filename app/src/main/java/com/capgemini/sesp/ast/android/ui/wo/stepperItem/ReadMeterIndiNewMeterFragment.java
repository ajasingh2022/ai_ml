package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.ProductTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepCollectionTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitMeterRegReadTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitMeterRegTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitMeterCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitMeterRegisterCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.ProductTCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class ReadMeterIndiNewMeterFragment extends CustomFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    private transient Dialog mDialog = null;
    private transient ListView unitMeterRegReadLv = null;
    private transient LinearLayout userChoiceLayout = null;
    private transient CheckBox cantBeReadCb = null;
    private transient View cantBeReadHiddenView = null;
    private EditText mReasonEt;
    private transient UnitMeterRegReadAdapter adapter = null;
    transient SoftReference<List<View>> meterReadRegsViewLs = null;
    transient boolean canNotRead = false;
    private transient boolean canNotReadAtStart = false;
    private String readValues = null;
    private String readValuesAtStart = null;
    String notReadReason = null;
    private String notReadReasonAtStart = null;
    private transient String[] rValues = null;
    transient Map<WoUnitMeterRegTO, Double> readingMap = null;
    transient boolean validDataProvided = false;
    private static String TAG = ReadMeterIndiNewMeterFragment.class.getSimpleName();
    private transient Long readingTypeId;
    private transient boolean oldMeterRegRead;
    transient List<WoUnitMeterRegisterCustomTO> woUnitMeterRegisterCustomTOList;
    private transient WoUnitCustomTO woUnitCustomTO;
    private boolean isRegTariffMissingDismantledMeter = false;


    public ReadMeterIndiNewMeterFragment(String stepIdentifier, Long readingTypeId, boolean oldMeterRegRead) {
        super(stepIdentifier);
        this.readingTypeId = readingTypeId;
        this.oldMeterRegRead = oldMeterRegRead;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_newmeter_indic, null);
        initializePageValues();
        initialize();
        setUpListeners();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initialize() {
        if (fragmentView != null) {
            unitMeterRegReadLv = fragmentView.findViewById(R.id.unitMeterRegReadLv);
            cantBeReadCb = fragmentView.findViewById(R.id.cantBeReadCb);
            cantBeReadHiddenView = fragmentView.findViewById(R.id.cantBeReadHiddenView);
            mReasonEt = fragmentView.findViewById(R.id.reasonEt);
            userChoiceLayout = fragmentView.findViewById(R.id.userchoice);
            mReasonEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //do nothing
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //do nothing
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mReasonEt.getText() != null) {
                        notReadReason = mReasonEt.getText().toString();
                    }
                }
            });
        }
    }

    public void initializePageValues() {
        try {
            //Set abort structure
            WorkorderCustomWrapperTO workorder = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();

            List<WoUnitCustomTO> woUnitMTOList = workorder.getWorkorderCustomTO().getWoUnits();
            readingMap =new ArrayMap<WoUnitMeterRegTO, Double>();

            for (WoUnitCustomTO woUnitTO : woUnitMTOList) {
                Long status;
                try {
                    status = TypeDataUtil.getValidOrgDivValue(woUnitTO, WoUnitTO.ID_WO_UNIT_STATUS_T_D);
                    if (readingTypeId.equals(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_METER_REG_READ_T__DISMANTLED_METER)) {
                        if (woUnitTO.getIdWoUnitT().equals(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_T__METER)
                                && ((status.equals(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_STATUS_T__EXISTING))
                                || status.equals(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED))) {
                            woUnitCustomTO = woUnitTO;
                        }
                    } else {
                        if (woUnitTO.getIdWoUnitT().equals(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_T__METER)
                                && status.equals(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_STATUS_T__MOUNTED)) {
                            woUnitCustomTO = woUnitTO;
                        }
                    }
                } catch (Exception e) {
                    writeLog(TAG + " : initializePageValues()", e);
                }
            }

            preInitInterface(workorder, woUnitCustomTO);

            List<String> meterRegReadList = new ArrayList<>();
            if (!stepfragmentValueArray.isEmpty()) {
                if ("NO".equalsIgnoreCase(String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.CAN_READ)))) {
                    canNotRead = true;
                    notReadReason = String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.REASON_NOT_READ));
                    readValues = "";
                } else {
                    canNotRead = false;
                    notReadReason = "";
                    readValues = String.valueOf(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.REG_VALUES));
                }
            }
            if (readValues != null && !"".equals(readValues)) {
                rValues = readValues.split(";");
                if (rValues != null) {
                    for (String tempString : rValues) {
                        String[] meterRegRead = tempString.split(":");
                        if (meterRegRead != null) {
                            meterRegReadList.add(meterRegRead[1]);
                        }
                    }
                }
            }


            //Initialize readValues
            if (woUnitMeterRegisterCustomTOList != null) {
                for (int i=0;i<woUnitMeterRegisterCustomTOList.size();i++) {

                    WoUnitMeterRegisterCustomTO woUnitMeterRegisterCustomTO= woUnitMeterRegisterCustomTOList.get(i);
                    WoUnitMeterRegTO woUnitMeterRegTO = woUnitMeterRegisterCustomTO.getWoUnitMeterReg();

                    if (woUnitMeterRegTO != null && meterRegReadList.size() >i) {
                            readingMap.put(woUnitMeterRegTO, Utils.safeToDouble(meterRegReadList.get(i), Locale.getDefault()));
                    }

                    if (woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads() == null) {
                        woUnitMeterRegisterCustomTO.setWoUnitMeterRegReads(new ArrayList<WoUnitMeterRegReadTO>());
                    }
                    WoUnitMeterRegReadTO woUnitMeterRegReadTO = new WoUnitMeterRegReadTO();
                    woUnitMeterRegReadTO.setIdCase(workorder.getIdCase());
                    woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads().add(woUnitMeterRegReadTO);
                }
            }

            canNotReadAtStart = canNotRead;
            readValuesAtStart = readValues;
        } catch (Exception e) {
            writeLog(TAG + " : initializePageValues()", e);
        }
    }

    private void preInitInterface(WorkorderCustomWrapperTO workorderTO, WoUnitCustomTO woUnitCustomTO) {
        try {
            Long idProductT = null;
            if ((woUnitCustomTO != null
                    && woUnitCustomTO.getWoUnitMeter() != null
                    && woUnitCustomTO.getWoUnitMeter().getMeterRegisters() != null)) {
                initializeWoUnitMeterRegisterCustomTOList(workorderTO.getIdCase(), woUnitCustomTO, null);
            } else {

                List<WoInstMepCollectionTO> woInstMepCollectionTOs = workorderTO.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getCollection();

                if (woInstMepCollectionTOs != null && woInstMepCollectionTOs.size() > 0) {
                    WoInstMepCollectionTO woInstMepCollectionTO = woInstMepCollectionTOs.get(0);
                    idProductT = woInstMepCollectionTO.getIdProductT();
                }

                if (idProductT != null) {
                    initializeWoUnitMeterRegisterCustomTOList(workorderTO.getIdCase(), woUnitCustomTO, idProductT);
                } else if (readingTypeId.equals(AndroidUtilsAstSep.CONSTANTS().WO_UNIT_METER_REG_READ_T__DISMANTLED_METER)) {
                    isRegTariffMissingDismantledMeter = true;
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : preInitInterface()", e);
        }
    }

    public void saveUserChoiceValuesForPage() {
        try {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.CAN_READ, canNotRead ? "NO" : "YES");
            if (notReadReason != null) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.REASON_NOT_READ, notReadReason);
            }
            String readValue = getRegisterReadingAsString();
            if (readValue != null) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.REG_VALUES, readValue);
            }
        } catch (Exception e) {
            writeLog(TAG + " : saveUserChoiceValuesForPage()", e);
        }

    }

    private String getRegisterReadingAsString() {
        String readValue = null;
        try {
            if (readingMap != null && !readingMap.isEmpty()) {
                StringBuilder readBuilder = new StringBuilder();
                Iterator<WoUnitMeterRegTO> unitMeterItr = readingMap.keySet().iterator();
                while (unitMeterItr.hasNext()) {
                    WoUnitMeterRegTO tempMeterRegTo = unitMeterItr.next();
                    Double dReadValue = readingMap.get(tempMeterRegTo);
                    if (dReadValue != null) {
                        readBuilder.append(tempMeterRegTo.getId());
                        readBuilder.append(":");
                        readBuilder.append(dReadValue.toString());
                        readBuilder.append(";");
                    }
                }
                readValue = readBuilder.toString();
            }
        } catch (Exception e) {
            writeLog(TAG + " : getRegisterReadingAsString()", e);
        }
        return readValue;
    }

    private void initializeWoUnitMeterRegisterCustomTOList(Long IdCase,
                                                           WoUnitCustomTO woUnitCustomTO, Long idProduct) {
        try {
            if (woUnitCustomTO != null && woUnitCustomTO.getWoUnitMeter() != null
                    && woUnitCustomTO.getWoUnitMeter().getMeterRegisters() != null) {
                try {
                    woUnitMeterRegisterCustomTOList = new ArrayList<WoUnitMeterRegisterCustomTO>();
                    for (int i = 0; i < woUnitCustomTO.getWoUnitMeter().getMeterRegisters().size(); i++) {
                        WoUnitMeterRegisterCustomTO woUnitMeterRegisterCustomTO = new WoUnitMeterRegisterCustomTO();
                        WoUnitMeterRegTO woUnitMeterReg = new WoUnitMeterRegTO();
                        woUnitMeterRegisterCustomTO.setWoUnitMeterReg(woUnitMeterReg);

                        AndroidUtilsAstSep.copyBean(woUnitMeterReg, woUnitCustomTO.getWoUnitMeter().getMeterRegisters().get(i).getWoUnitMeterReg());
                        woUnitMeterRegisterCustomTOList.add(woUnitMeterRegisterCustomTO);
                    }
                } catch (Exception e) {
                    writeLog(TAG + " : initializeWoUnitMeterRegisterCustomTOList()", e);
                }
            } else {

                List<ProductTCTO> registerTariffsToShow = new ArrayList<ProductTCTO>();
                // Number of Tariffs
                if (idProduct != null) {
                    ProductTCustomTO productTCustomTO = ObjectCache.getIdObject(ProductTCustomTO.class, idProduct);
                    List<ProductTCTO> productTCTOs = productTCustomTO.getProductTCTOs();
                    for (ProductTCTO productTCTOObj : productTCTOs) {
                        if (productTCTOObj.getIdProductT() != null &&
                                productTCTOObj.getIdProductT().equals(idProduct)) {
                            registerTariffsToShow.add(productTCTOObj);
                        }
                    }
                }

                int currentReg = 0;

                woUnitMeterRegisterCustomTOList = new ArrayList<WoUnitMeterRegisterCustomTO>();
                for (int i = 0; i < registerTariffsToShow.size(); i++) {
                    ProductTCTO aRegisterTariffCombination = registerTariffsToShow.get(i);
                    woUnitMeterRegisterCustomTOList.add(currentReg++, createUnitMeterRegisterCustomMTO(IdCase,
                            aRegisterTariffCombination.getIdRegisterT(),
                            aRegisterTariffCombination.getIdTariffT()));
                }
            }

        } catch (Exception e) {
            writeLog(TAG + " : initializeWoUnitMeterRegisterCustomTOList()", e);
        }
    }

    private WoUnitMeterRegisterCustomTO createUnitMeterRegisterCustomMTO(Long idCase, Long idRegisterT, Long idTariffT) {
        WoUnitMeterRegisterCustomTO woUnitMeterRegisterCustomMTO = null;
        try {
            WoUnitMeterRegTO woUnitMeterRegMTO = new WoUnitMeterRegTO();
            woUnitMeterRegMTO.setIdCase(idCase);
            woUnitMeterRegMTO.setIdRegisterT(idRegisterT);
            woUnitMeterRegMTO.setIdTariffT(idTariffT);
            woUnitMeterRegisterCustomMTO = new WoUnitMeterRegisterCustomTO();
            woUnitMeterRegisterCustomMTO.setWoUnitMeterReg(woUnitMeterRegMTO);
        } catch (Exception e) {
            writeLog(TAG + " : createUnitMeterRegisterCustomMTO()", e);
        }
        return woUnitMeterRegisterCustomMTO;
    }

    private void populateData() {
        try {
            if (canNotRead) {
                cantBeReadCb.setChecked(true);
                cantBeReadHiddenView.setVisibility(View.VISIBLE);
                mReasonEt.setText(notReadReason);

            } else {
                cantBeReadCb.setChecked(false);
            }
            if (unitMeterRegReadLv != null) {
                if ((woUnitMeterRegisterCustomTOList != null) && (!woUnitMeterRegisterCustomTOList.isEmpty())) {
                    adapter = new UnitMeterRegReadAdapter(woUnitMeterRegisterCustomTOList, this);
                    unitMeterRegReadLv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (woUnitMeterRegisterCustomTOList != null && woUnitMeterRegisterCustomTOList.size() > 0) {
                        if (!canNotRead) {
                            unitMeterRegReadLv.setVisibility(View.VISIBLE);
                        }
                    }
                    userChoiceLayout.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ": populateData()", e);
        }

    }

    private void setUpListeners() {
        if (cantBeReadCb != null) {
            cantBeReadCb.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        if (buttonView != null
                && buttonView.getId() == R.id.cantBeReadCb
                && cantBeReadHiddenView != null) {
            if (isChecked) {
                cantBeReadHiddenView.setVisibility(View.VISIBLE);
                enableDisableReadingTextBoxes(false);
                unitMeterRegReadLv.setVisibility(View.GONE);
                canNotRead = true;
            } else {
                cantBeReadHiddenView.setVisibility(View.GONE);
                if (woUnitMeterRegisterCustomTOList != null && woUnitMeterRegisterCustomTOList.size() > 0) {
                    unitMeterRegReadLv.setVisibility(View.VISIBLE);
                    enableDisableReadingTextBoxes(true);
                }
                canNotRead = false;
            }
        }
    }

    private void enableDisableReadingTextBoxes(final boolean enable) {
        if (meterReadRegsViewLs != null
                && meterReadRegsViewLs.get() != null
                && !meterReadRegsViewLs.get().isEmpty()) {

            for (final View vw : meterReadRegsViewLs.get()) {
                final TextView readingTv = vw.findViewById(R.id.registerReading);
                final TextView tariffTv = vw.findViewById(R.id.tariffReading);
                final ImageButton clearButton = vw.findViewById(R.id.clearRegValueBtn);

                if (readingTv != null) {
                    readingTv.setText("");
                    readingTv.setEnabled(enable);
                }
                if (tariffTv != null) {
                    tariffTv.setText("");
                    tariffTv.setEnabled(enable);
                }

                if (clearButton != null) {
                    clearButton.setEnabled(enable);
                }
            }
        }

    }


    /**
     * User has not selected any option
     * and tries to move forward, user should be prompted
     * to select an option
     */
    public void showPromptUserAction() {
        /*
         *  If status is false then it is coming from flow engine stack rebuild
         *  and does not require user attention
         */
        final View alertView
                = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.prompt_user_response_layout, null);
        if (alertView != null) {
            final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
            okButton.setOnClickListener(this);

            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                    alertView, null, null);
            mDialog = builder.create();
            mDialog.show();
        }
    }

    @Override
    public boolean validateUserInput() {
        boolean returnValue = false;
        if (!AbstractWokOrderActivity.resuming) {
            if (canNotRead) {
                Log.d(this.getClass().getSimpleName(), "Test notReadreason " + mReasonEt.getText().toString());
                returnValue = Utils.isNotEmpty(mReasonEt.getText().toString());
            } else {

                returnValue = validDataProvided;
            }

        } else
            returnValue = true;
        if (returnValue) {

            applyChangesToModifiableWO();
        }
        return returnValue;

    }

    @Override
    public void onClick(View v) {
        if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && mDialog != null) {
            mDialog.dismiss();
        }
    }


    @Override
    public void applyChangesToModifiableWO() {
        try {
            saveUserChoiceValuesForPage();
            WoUnitCustomTO theMeter;
            WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (oldMeterRegRead) {
                //INITIAL
                theMeter = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
            } else if (isRegTariffMissingDismantledMeter) {
                theMeter = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
            } else {
                //MOUNTED
                theMeter = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
            }

            WoUnitMeterCustomTO woUnitMeterCustomTO = theMeter.getWoUnitMeter();
            if (woUnitMeterCustomTO == null) {
                woUnitMeterCustomTO = new WoUnitMeterCustomTO();
                theMeter.setWoUnitMeter(woUnitMeterCustomTO);
            }

            woUnitMeterCustomTO.setMeterRegisters(woUnitMeterRegisterCustomTOList);

            if (!isRegTariffMissingDismantledMeter) { //If there is no product then there is no more readings.
                woUnitMeterCustomTO.setMeterRegisters(getAllRegistersWithRegReads(oldMeterRegRead));
            }

            if (isRegTariffMissingDismantledMeter && notReadReason != null && notReadReason.length() != 0) {
                WoInfoCustomTO woInfoCustomTO = WorkorderUtils.createInfo(notReadReason,
                        SessionState.getInstance().getCurrentUserUsername(),
                        CONSTANTS().INFO_SOURCE_T__SESP_PDA,
                        CONSTANTS().INFO_T__NO_REG_READ_REASON);
                WorkorderUtils.deleteAndAddWoInfoCustomMTO(wo, woInfoCustomTO);
            }
        } catch (Exception e) {
            writeLog(TAG + " : applyChangesToModifiableWO()", e);
        }

    }

    private List<WoUnitMeterRegisterCustomTO> getAllRegistersWithRegReads(boolean meterDismantled) {
        Long idWoUnitMeterRegReadT = null;
        try {
            if (canNotRead) {
                idWoUnitMeterRegReadT = CONSTANTS().WO_UNIT_METER_REG_READ_T__READING_NOT_POSSIBLE;
            } else if (meterDismantled) {
                idWoUnitMeterRegReadT = CONSTANTS().WO_UNIT_METER_REG_READ_T__DISMANTLED_METER;
            } else if (!meterDismantled) {
                idWoUnitMeterRegReadT = CONSTANTS().WO_UNIT_METER_REG_READ_T__MOUNTED_METER;
            }

            if (woUnitMeterRegisterCustomTOList != null) {
                for (WoUnitMeterRegisterCustomTO woUnitMeterRegisterCustomTO : woUnitMeterRegisterCustomTOList) {

                    if (woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads() != null) {
                        for (int j = 0; j < woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads().size(); j++) {
                            if (canNotRead) {
                                woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads().get(j).setReadingValue(new Double(0));
                                woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads().get(j).setReadingTimestamp(new Date());
                                woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads().get(j).setIdWoUnitMeterRegReadT(idWoUnitMeterRegReadT);
                            } else if (!canNotRead) {
                                woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads().get(j).setReadingValue(Double.valueOf(readingMap.get(woUnitMeterRegisterCustomTO.getWoUnitMeterReg())));
                                woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads().get(j).setReadingTimestamp(new Date());
                                woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads().get(j).setIdWoUnitMeterRegReadT(idWoUnitMeterRegReadT);
                            } else if (meterDismantled) {
                                woUnitMeterRegisterCustomTO.getWoUnitMeterRegReads().get(j).setIdWoUnitMeterRegReadT(idWoUnitMeterRegReadT);
                            }
                        }
                        //woUnitMeterRegisterCustomTO.getWoUnitMeterReg().setId(null);
                    }

                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : getAllRegistersWithRegReads()", e);
        }
        return woUnitMeterRegisterCustomTOList;
    }


    /**
     * Combine the provided values for register and tariff
     * separated by decimal point
     *
     * @param regValueTv
     * @param tariffValueTv
     * @return
     */

    static Double getUserProvidedValue(final TextView regValueTv,
                                       final TextView tariffValueTv) {
        final StringBuilder builder = new StringBuilder();
        try {
            String beforeDec = regValueTv.getText().toString().trim();
            String afterDec = tariffValueTv.getText().toString().trim();

            if (beforeDec == null
                    || (beforeDec != null && beforeDec.trim().equals(""))) {
                beforeDec = "0";
            }
            builder.append(beforeDec).append('.');

            if (afterDec == null
                    || (afterDec != null && afterDec.trim().equals(""))) {
                afterDec = "0";
            }

            builder.append(afterDec);
        } catch (Exception e) {
            writeLog(TAG + " : getUserProvidedValue()", e);
        }
        return Utils.safeToDouble(builder.toString(), Locale.getDefault());
    }


}