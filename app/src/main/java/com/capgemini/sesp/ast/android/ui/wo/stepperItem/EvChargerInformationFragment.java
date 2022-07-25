package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AdditionalDataUtils;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterInstallationCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterInstallationDM;
import com.capgemini.sesp.ast.android.ui.wo.bean.EvChargerListBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitAdditionalDataTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitAdditionalDataTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.to.cft.table.SystemParameterTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvChargerInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class EvChargerInformationFragment extends CustomFragment {
    protected transient EditText editLengthOfCable = null;
    protected transient Spinner typeOfChargerSpinner = null;
    protected transient EditText editNumberOfConnectors = null;
    protected transient RecyclerView addLayout = null;
    protected transient TextView connectorSerials = null;
    protected transient TextView maxLimitLabel = null;
    transient static final String LENGTH_OF_CABLE = "LENGTH_OF_CABLE";
    transient static final String NUMBER_OF_CONNECTORS = "NUMBER_OF_CONNECTORS";
    transient static final String TYPE_OF_CHARGER = "TYPE_OF_CHARGER";
    transient static final String CONNECTOR_DETAILS = "CONNECTOR_DETAILS";
    transient static final String INVALID_EV_ENTRY = "INVALID_EV_ENTRY";
    protected transient Dialog dialog = null;
    Integer selected_connectors = 1;
    SystemParameterTO evMax = null;
    private static String TAG = EvChargerInformationFragment.class.getSimpleName();
    private EvChargerAdapter evChargerAdapter;
    List<EvChargerListBean> evChargerListBeans = new ArrayList<>();
    Gson gson = new Gson();
    Type type = new TypeToken<ArrayList<EvChargerListBean>>() {
    }.getType();

    public EvChargerInformationFragment() {
        super();
    }

    public EvChargerInformationFragment(String stepId) {
        super(stepId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_charger_information, null);
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
            maxLimitLabel = parentView.findViewById(R.id.maxLimitLabel);
            evMax = ObjectCache.getIdObject(SystemParameterTO.class, AndroidUtilsAstSep.CONSTANTS().SYSTEM_PARAMETER__EV_MAX_CONNECTIONS);
            if (evMax != null) {
                maxLimitLabel.setText("(MAXIMUM: " + evMax.getValue() + ")");
            }
            editLengthOfCable = parentView.findViewById(R.id.editLengthOfCable);
            typeOfChargerSpinner = parentView.findViewById(R.id.typeOfChargerSpinner);
            editNumberOfConnectors = parentView.findViewById(R.id.editNoOfConnectors);
            addLayout = parentView.findViewById(R.id.addLayout);
            connectorSerials = parentView.findViewById(R.id.connectorSerials);
            //resumeCheck = 0;
            AbstractWokOrderActivity.evInformationRestore = true;
            if ((this.getActivity() instanceof MeterInstallationDM) || (this.getActivity() instanceof MeterInstallationCT)) {
                if (stepfragmentValueArray != null) {
                    if (stepfragmentValueArray.get(NUMBER_OF_CONNECTORS) == null) {
                        selected_connectors = AdditionalDataUtils.getValueOfCodeFromAdditionalData(NUMBER_OF_CONNECTORS);
                        if (selected_connectors != null)
                            stepfragmentValueArray.put(NUMBER_OF_CONNECTORS, String.valueOf(selected_connectors));
                    }
                    if (stepfragmentValueArray.get(CONNECTOR_DETAILS) == null) {
                        if (stepfragmentValueArray.get(NUMBER_OF_CONNECTORS) != null) {
                            selected_connectors = Integer.parseInt(stepfragmentValueArray.get(NUMBER_OF_CONNECTORS).toString());
                            evChargerListBeans.clear();
                            for (int i = 1; i <= Integer.parseInt(evMax.getValue()); i++) {
                                try {
                                    String code = "EV_CONNECTOR_" + i;
                                    String details = AdditionalDataUtils.getStringValueOfCodeFromAdditionalData(code);
                                    if (details != null) {
                                        String detailsArray[] = details.split(",");
                                        EvChargerListBean evChargerListBean = new EvChargerListBean();
                                        evChargerListBean.setIndex(Integer.parseInt(detailsArray[0]));
                                        String type = detailsArray[1].substring(5);
                                        if (type.equalsIgnoreCase("AC") || type.equalsIgnoreCase("DC")) {
                                            evChargerListBean.setCurrentType(type);
                                        } else {
                                            throw new Exception("Connector type mismatch. Check work order.");
                                        }
                                        String power = detailsArray[2].substring(6);
                                        Double.parseDouble(power);
                                        evChargerListBean.setPower(power);
                                        evChargerListBeans.add(evChargerListBean);
                                    }
                                } catch (Exception e) {
                                    writeLog(TAG + "initializeWidgets(): ", e);
                                }
                            }
                            Gson gson = new Gson();
                            String connectorDetails = gson.toJson(evChargerListBeans, type);
                            stepfragmentValueArray.put(CONNECTOR_DETAILS, connectorDetails);
                        }
                    }
                    if (stepfragmentValueArray.get(TYPE_OF_CHARGER) == null) {
                        String chargerType = AdditionalDataUtils.getStringValueOfCodeFromAdditionalData(TYPE_OF_CHARGER);
                        if (chargerType != null)
                            stepfragmentValueArray.put(TYPE_OF_CHARGER, chargerType);
                    }
                }
            } else {
                AbstractWokOrderActivity.evInformationRestore = false;
                if (stepfragmentValueArray.get(CONNECTOR_DETAILS) != null) {
                    AbstractWokOrderActivity.evInformationRestore = true;
                }
            }
        }
    }

    protected void setupListeners() {
        try {
            typeOfChargerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    stepfragmentValueArray.put(TYPE_OF_CHARGER, typeOfChargerSpinner.getSelectedItem().toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            editNumberOfConnectors.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        selected_connectors = Integer.parseInt(editNumberOfConnectors.getText().toString());
                    } catch (Exception e) {
                        selected_connectors = 1;
                    }
                    if (selected_connectors > Integer.parseInt(evMax.getValue()) || selected_connectors < 1) {
                        if (selected_connectors < 1) {
                            editNumberOfConnectors.setText("");
                        } else {
                            editNumberOfConnectors.setText(editNumberOfConnectors.getText().toString().substring(0, editNumberOfConnectors.getText().toString().length() - 1));
                            editNumberOfConnectors.setSelection(editNumberOfConnectors.getText().toString().length());
                        }
                    } else {
                        Map<Integer, EvChargerListBean> initMap = new TreeMap<>();
                        for (int i = 1; i <= selected_connectors; i++) {
                            initMap.put(i, new EvChargerListBean());
                        }
                        if (AbstractWokOrderActivity.evInformationRestore) {
                            Map<Integer, EvChargerListBean> map = new TreeMap<>();
                            if (stepfragmentValueArray.get(CONNECTOR_DETAILS) != null) {
                                evChargerListBeans.clear();
                                evChargerListBeans = gson.fromJson(stepfragmentValueArray.get(CONNECTOR_DETAILS).toString(), type);
                                for (EvChargerListBean evChargerListBean : evChargerListBeans) {
                                    map.put(evChargerListBean.getIndex(), evChargerListBean);
                                }
                                evChargerAdapter = new EvChargerAdapter(EvChargerInformationFragment.this.getContext(), map);
                            }
                        } else
                            evChargerAdapter = new EvChargerAdapter(EvChargerInformationFragment.this.getContext(), initMap);
                        addLayout.setLayoutManager(new LinearLayoutManager(EvChargerInformationFragment.this.getContext()));
                        addLayout.setAdapter(evChargerAdapter);
                    }


                    stepfragmentValueArray.put(NUMBER_OF_CONNECTORS, Integer.toString(selected_connectors));

                }
            });
            editLengthOfCable.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String lengthOfCable = editLengthOfCable.getText().toString();
                    String fragments[] = lengthOfCable.split("\\.");
                    try {
                        Double.parseDouble(lengthOfCable);
                        if (Double.parseDouble(lengthOfCable) < 0d) {
                            editLengthOfCable.setText("0");
                            editLengthOfCable.setSelection(editLengthOfCable.getText().toString().length());
                        }
                        if (fragments.length > 1) {
                            if (fragments[1].length() > 2) {
                                editLengthOfCable.setText(editLengthOfCable.getText().toString().substring(0, editLengthOfCable.getText().toString().length() - 1));
                                editLengthOfCable.setSelection(editLengthOfCable.getText().toString().length());
                            }
                        }
                    } catch (Exception e) {
                        if (fragments.length == 0) {
                            editLengthOfCable.setText("0.");
                            editLengthOfCable.setSelection(editLengthOfCable.getText().toString().length());
                        } else if (fragments.length == 3) {
                            String correction = fragments[0] + "." + fragments[1] + fragments[2];
                            editLengthOfCable.setText(correction);
                            editLengthOfCable.setSelection(editLengthOfCable.getText().toString().length());
                        } else if (lengthOfCable.endsWith(".")) {
                            editLengthOfCable.setText(editLengthOfCable.getText().toString().substring(0, editLengthOfCable.getText().toString().length() - 1));
                            editLengthOfCable.setSelection(editLengthOfCable.getText().toString().length());
                        }
                    }
                    stepfragmentValueArray.put(LENGTH_OF_CABLE, editLengthOfCable.getText().toString());

                }
            });


        } catch (Exception e) {
            writeLog(TAG + " : setupListeners()", e);
        }

    }

    protected void populateData() {
        Map<Integer, EvChargerListBean> map = new TreeMap<>();
        if (stepfragmentValueArray != null) {
            if (stepfragmentValueArray.get(TYPE_OF_CHARGER) != null) {
                if (stepfragmentValueArray.get(TYPE_OF_CHARGER).toString().equalsIgnoreCase("Untethered")) {
                    typeOfChargerSpinner.setSelection(((ArrayAdapter<String>) typeOfChargerSpinner.getAdapter()).getPosition("Untethered"));
                } else
                    typeOfChargerSpinner.setSelection(((ArrayAdapter<String>) typeOfChargerSpinner.getAdapter()).getPosition("Tethered"));
            }
            if (stepfragmentValueArray.get(LENGTH_OF_CABLE) != null) {
                editLengthOfCable.setText(stepfragmentValueArray.get(LENGTH_OF_CABLE).toString());
            }
            if (stepfragmentValueArray.get(NUMBER_OF_CONNECTORS) != null) {
                selected_connectors = Integer.parseInt(stepfragmentValueArray.get(NUMBER_OF_CONNECTORS).toString());
                editNumberOfConnectors.setText(stepfragmentValueArray.get(NUMBER_OF_CONNECTORS).toString());
            }
            if (stepfragmentValueArray.get(CONNECTOR_DETAILS) != null) {
                evChargerListBeans.clear();
                evChargerListBeans = gson.fromJson(stepfragmentValueArray.get(CONNECTOR_DETAILS).toString(), type);
                for (EvChargerListBean evChargerListBean : evChargerListBeans) {
                    map.put(evChargerListBean.getIndex(), evChargerListBean);
                }
            } else {
                if (!(this.getActivity() instanceof MeterInstallationDM) && !(this.getActivity() instanceof MeterInstallationCT)) {
                    Map<Integer, EvChargerListBean> initMap = new TreeMap<>();
                    for (int i = 1; i <= selected_connectors; i++) {
                        initMap.put(i, new EvChargerListBean());
                    }
                    evChargerAdapter = new EvChargerAdapter(this.getContext(), initMap);
                    addLayout.setLayoutManager(new LinearLayoutManager(this.getContext()));
                    addLayout.setAdapter(evChargerAdapter);
                }
            }
        }
    }

    @Override
    public boolean validateUserInput() {
        boolean validate = true;
        Map<Integer, EvChargerListBean> map = evChargerAdapter.getChargerDetails();
        evChargerListBeans.clear();
        if (map != null) {
            for (Map.Entry<Integer, EvChargerListBean> entry : map.entrySet()) {
                evChargerListBeans.add(entry.getValue());
            }
        }
        try {
            String lengthOfCable = stepfragmentValueArray.get(LENGTH_OF_CABLE).toString();
            Double.parseDouble(lengthOfCable);
            if (lengthOfCable.endsWith(".")) {
                stepfragmentValueArray.put(INVALID_EV_ENTRY, "YES");
                validate = false;
            } else
                validate = true;
            if (validate) {
                for (EvChargerListBean evChargerListBean : evChargerListBeans) {
                    String power = evChargerListBean.getPower();
                    Double.parseDouble(power);
                    if (power.endsWith(".")) {
                        stepfragmentValueArray.put(INVALID_EV_ENTRY, "YES");
                        validate = false;
                        break;
                    } else
                        validate = true;
                }
            }
        } catch (Exception e) {
            stepfragmentValueArray.put(INVALID_EV_ENTRY, "NO");
            validate = false;
        }
        if (validate) {
            Gson gson = new Gson();
            String string = gson.toJson(evChargerListBeans, type);
            if (evChargerListBeans.size() > 0) {
                stepfragmentValueArray.put(CONNECTOR_DETAILS, string);
            }
            applyChangesToModifiableWO();
        }
        return validate;
    }

    @Override
    public void showPromptUserAction() {
        dialog = null;
        if (stepfragmentValueArray.get(INVALID_EV_ENTRY) != null) {
            if (stepfragmentValueArray.get(INVALID_EV_ENTRY).toString().equalsIgnoreCase("NO")) {
                final AlertDialog.Builder builder = GuiController.showErrorDialog(getActivity(),
                        getString(R.string.one_or_more_mandatory_field_is_missing));
                dialog = builder.create();
                dialog.show();
            } else if (stepfragmentValueArray.get(INVALID_EV_ENTRY).toString().equalsIgnoreCase("YES")) {
                final AlertDialog.Builder builder = GuiController.showErrorDialog(getActivity(),
                        getString(R.string.please_enter_a_number_after_decimal));
                dialog = builder.create();
                dialog.show();
            }
        }
    }


    @Override
    public void applyChangesToModifiableWO() {
        try {
            final WorkorderCustomWrapperTO workorderCustomWrapperTO = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            final List<WoUnitCustomTO> woUnitCustomTOs = workorderCustomWrapperTO.getWorkorderCustomTO().getWoUnits();
            WoUnitCustomTO woUnitCustomTO = UnitInstallationUtils.getModifiedUnit(woUnitCustomTOs, CONSTANTS().WO_UNIT_T__EV_CHARGER, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
            List<UnitAdditionalDataTTO> unitAdditionalDataTTOS = ObjectCache.getAllTypes(UnitAdditionalDataTTO.class);
            if (Utils.isEmpty(evChargerListBeans))
                evChargerListBeans = gson.fromJson(stepfragmentValueArray.get(CONNECTOR_DETAILS).toString(), type);
            int counter = evChargerListBeans.size();
            List<UnitAdditionalDataTO> unitAdditionalDataTOs = new ArrayList<>();
            for (UnitAdditionalDataTTO unitAdditionalDataTTO : unitAdditionalDataTTOS) {
                UnitAdditionalDataTO unitAdditionalDataTO = new UnitAdditionalDataTO();
                if ((unitAdditionalDataTTO.getId() == AndroidUtilsAstSep.CONSTANTS().UNIT_ADDITIONAL_DATA_T__CHARGER_TYPE) ||
                        (unitAdditionalDataTTO.getId().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_ADDITIONAL_DATA_T__CHARGER_TYPE))) {
                    unitAdditionalDataTO.setIdUnitAdditionalDataT(unitAdditionalDataTTO.getId());
                    unitAdditionalDataTO.setValue(stepfragmentValueArray.get(TYPE_OF_CHARGER).toString());
                    unitAdditionalDataTOs.add(unitAdditionalDataTO);
                } else if ((unitAdditionalDataTTO.getId() == AndroidUtilsAstSep.CONSTANTS().UNIT_ADDITIONAL_DATA_T__CHARGER_CABLE_LENGTH) ||
                        (unitAdditionalDataTTO.getId().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_ADDITIONAL_DATA_T__CHARGER_CABLE_LENGTH))) {
                    unitAdditionalDataTO.setIdUnitAdditionalDataT(unitAdditionalDataTTO.getId());
                    unitAdditionalDataTO.setValue(stepfragmentValueArray.get(LENGTH_OF_CABLE).toString());
                    unitAdditionalDataTOs.add(unitAdditionalDataTO);
                } else {
                    for (EvChargerListBean evChargerListBean : evChargerListBeans) {
                        String code = evChargerListBean.getCode();
                        if (unitAdditionalDataTTO.getCode().equals(code) && counter > 0) {
                            unitAdditionalDataTO.setIdUnitAdditionalDataT(unitAdditionalDataTTO.getId());
                            unitAdditionalDataTO.setValue(evChargerListBean.getPower());
                            unitAdditionalDataTOs.add(unitAdditionalDataTO);
                            counter--;
                        }
                    }
                }
            }
            woUnitCustomTO.setUnitAdditionalDataTOs(unitAdditionalDataTOs);
        } catch (Exception e) {
            writeLog(TAG + " : applyChangesToModifiableWO() ", e);
        }
    }

}