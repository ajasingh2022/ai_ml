package com.capgemini.sesp.ast.android.ui.activity.printer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.driver.iface.LabelPrinter;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SespSpinnerAdapter;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.cft.common.to.custom.base.NameInterfaceTO;
import com.skvader.rsp.cft.common.util.Utils;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.CaseTCustomTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class SearchOrdersToPrintActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private List<WorkorderLiteTO> assignedWOs = null;
    private Spinner spinnerOrderType = null;
    private ListView orderTypeLv = null;
    private CheckBox chooseAllcb = null;
    private transient Set<WorkorderLiteTO> mSelectedWorkorderLiteTO = new HashSet<WorkorderLiteTO>();
    static String TAG = SearchOrdersToPrintActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_search_orders_to_print);

        // Hiding the logo as requested
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);

        /*
         * Setting up custom action bar view
		 */
        final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);

        // -- Customizing the action bar ends -----

        final View localeFlag = vw.findViewById(R.id.save_btn);
        if (localeFlag != null) {
            localeFlag.setVisibility(View.INVISIBLE);
        }

        getSupportActionBar().setCustomView(vw, layout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(R.string.title_activity_search_orders_to_print);

        spinnerOrderType = findViewById(R.id.spinnerOrderType);
        orderTypeLv = findViewById(R.id.orderTypeLv);
        chooseAllcb = findViewById(R.id.chooseAllcb);
        } catch (Exception e) {
            writeLog(TAG + " : onCreate()", e);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (assignedWOs == null) {
                assignedWOs = DatabaseHandler.createDatabaseHandler().getAllWorkorderLiteTos();
            }
            List<DisplayItem> spinnerOrderTypeList = new ArrayList<DisplayItem>();
            for (WorkorderLiteTO workorderLiteTO : assignedWOs) {
                DisplayItem displayItem = new DisplayItem(ObjectCache.getIdObject(CaseTCustomTO.class, workorderLiteTO.getIdCaseT()));
                if (!spinnerOrderTypeList.contains(displayItem)) {
                    spinnerOrderTypeList.add(displayItem);
                }
            }
            //Set Display Item object for Select all option
            NameInterfaceTO selectAllOption = new NameInterfaceTO() {
                private Long id;
                private String name;

                @Override
                public String getName() {
                    return this.name;
                }

                @Override
                public void setName(String name) {
                    this.name = name;
                }

                @Override
                public Long getId() {
                    return this.id;
                }

                @Override
                public void setId(Long id) {
                    this.id = id;
                }
            };
            selectAllOption.setId(-1L);
            selectAllOption.setName(getApplicationContext().getResources().getString(R.string.select_all));
            spinnerOrderTypeList.add(spinnerOrderTypeList.size(), new DisplayItem(null, null, selectAllOption));
            SespSpinnerAdapter spinnerArrayAdapter = new SespSpinnerAdapter(this, android.R.layout.simple_spinner_item, spinnerOrderTypeList);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerOrderType.setAdapter(spinnerArrayAdapter);
            spinnerOrderType.setOnItemSelectedListener(this);
            spinnerOrderType.setSelection(0, true);
            chooseAllcb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<WorkorderLiteTO> valueList = ((CustomOrderListAdapter) orderTypeLv.getAdapter()).getValueList();
                    if (Utils.isNotEmpty(valueList)) {
                        if (chooseAllcb.isChecked()) {
                            for (WorkorderLiteTO workorderLiteTO : valueList) {
                                addWorkorderLiteTO(workorderLiteTO);
                            }
                        } else {
                            for (WorkorderLiteTO workorderLiteTO : valueList) {
                                removeWorkorderLiteTO(workorderLiteTO);
                            }
                        }
                    }
                    ((CustomOrderListAdapter) orderTypeLv.getAdapter()).notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            writeLog(TAG + " : onResume()", e);
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            View v = spinnerOrderType.getSelectedView();
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            Long caseTypeId = spinnerOrderType.getSelectedItemId();
            List<WorkorderLiteTO> workorderLiteTOs = new ArrayList<WorkorderLiteTO>();
            for (WorkorderLiteTO workorderLiteTO : assignedWOs) {
                if (workorderLiteTO.getIdCaseT().equals(caseTypeId)) {
                    workorderLiteTOs.add(workorderLiteTO);
                }
            }
            if (caseTypeId == -1L) {
                orderTypeLv.setAdapter(new CustomOrderListAdapter(assignedWOs, this));
            } else {
                orderTypeLv.setAdapter(new CustomOrderListAdapter(workorderLiteTOs, this));
            }
        } catch (Exception e) {
            writeLog(TAG + " : onItemSelected()", e);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * Add the currently selected work order to the set
     *
     * @param selectedWorkorderLiteTO
     */

    public void addWorkorderLiteTO(final WorkorderLiteTO selectedWorkorderLiteTO) {
        if (selectedWorkorderLiteTO != null) {
            mSelectedWorkorderLiteTO.add(selectedWorkorderLiteTO);
        }
    }

    /**
     * Remove the currently selected work order from the set
     *
     * @param selectedWorkorderLiteTO
     */
    public void removeWorkorderLiteTO(final WorkorderLiteTO selectedWorkorderLiteTO) {
        if (selectedWorkorderLiteTO != null) {
            mSelectedWorkorderLiteTO.remove(selectedWorkorderLiteTO);
        }
    }

    public void printWorkOrder(View view) {
        try {
            if (Utils.isNotEmpty(mSelectedWorkorderLiteTO)) {
                AndroidUtilsAstSep.turnOnOffBluetooth(true);
                final LabelPrinter labelPrinter = ApplicationAstSep.getDriverManager().getLabelPrinter();
                new PrintWorkOrderThread(this, labelPrinter).start();

            } else {
                final View alertView
                        = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.prompt_user_response_layout, null);
                final Dialog dialog = GuiController.getCustomAlertDialog(this,
                        alertView, getString(R.string.no_item_selected), "ERROR")
                        .create();
                if (alertView != null) {
                    final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : printWorkOrder()", e);
        }
    }

    private class CustomOrderListAdapter extends BaseAdapter {

        private transient List<WorkorderLiteTO> valueList = null;
        private final Context context;

        public CustomOrderListAdapter(final List<WorkorderLiteTO> values, final Context context) {
            super();
            this.valueList = values;
            this.context = context;
        }

        @Override
        public int getCount() {
            int count = 0;
            try {
                if (this.valueList != null) {
                    count = this.valueList.size();
                }
            } catch (Exception e) {
                writeLog(TAG +" : WorkorderLiteTO()", e);
            }
            return count;
        }

        @Override
        public WorkorderLiteTO getItem(int position) {
            WorkorderLiteTO obj = null;
            try {
                if (this.valueList != null && this.valueList.size() > position) {
                    obj = this.valueList.get(position);
                }
            } catch (Exception e) {
                writeLog(TAG + " : WorkorderLiteTO()", e);
            }
            return obj;
        }

        @Override
        public long getItemId(int i) {
            return valueList.get(i).getId();
        }

        public List<WorkorderLiteTO> getValueList() {
            return valueList;
        }

        private class ViewHolder {
            TextView text;
            CheckBox checkbox;
            TextView instAddress;
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            try {
                ViewHolder holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final WorkorderLiteTO listItem = valueList.get(position);
                //If holder not exist then locate all view from UI file.
                if (convertView == null) {
                    // inflate UI from XML file
                    convertView = inflater.inflate(R.layout.select_orders_to_print_list_row, parent, false);
                    // get all UI view
                    holder.text = convertView.findViewById(R.id.instCodeTv);
                    holder.checkbox = convertView.findViewById(R.id.selOrdtoPrntCb);
                    holder.instAddress = convertView.findViewById(R.id.instAddressTv);

                    // set tag for holder
                    convertView.setTag(holder);
                } else {
                    // if holder created, get tag from view
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.text.setText(listItem.getInstCode());
                holder.instAddress.setText(listItem.getAddressStreet());
                holder.checkbox.setTag(position);
                holder.checkbox.setChecked(mSelectedWorkorderLiteTO.contains(listItem));
                holder.checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox cb = (CheckBox) view;
                        int position = (Integer) cb.getTag();
                        Log.d("CustomOrderListAdapter", "Checkbox position : " + position);
                        WorkorderLiteTO workorderLiteTO = getItem(position);
                        if (cb.isChecked()) {
                            addWorkorderLiteTO(workorderLiteTO);
                        } else {
                            removeWorkorderLiteTO(workorderLiteTO);
                        }
                    }
                });
            } catch (Exception e) {
                writeLog(TAG + " : getView()", e);
            }
            return convertView;
        }
    }

    private class PrintWorkOrderThread extends GuiWorker<String> {
        LabelPrinter labelPrinter = null;

        public PrintWorkOrderThread(Activity ownerActivity, LabelPrinter labelPrinter) {
            super(ownerActivity, PrintScannedIdActivity.class);
            this.labelPrinter = labelPrinter;
        }

        @Override
        protected String runInBackground() throws Exception {
            setMessage(R.string.check_printer_status);
            String infoMessage = null;
            try {
                String deviceStatus = labelPrinter.checkDeviceStatus(SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.BT_MAC_ADDRESS));
                if ("Bluetooth not supported on device.".equals(deviceStatus)) {
                    infoMessage = getString(R.string.bluetooth_not_supported_on_device);
                } else if ("Bluetooth on device not enabled.".equals(deviceStatus)) {
                    infoMessage = getString(R.string.bluetooth_on_device_not_enabled);
                } else {
                    setMessage(R.string.opening_connection);
                }

                if (labelPrinter.openConnection()) {
                    setMessage(R.string.sending_data_for_printing);
                    Thread.sleep(1500);
                    try {
                        for (WorkorderLiteTO workorderLiteTO : mSelectedWorkorderLiteTO) {
                            labelPrinter.printBarCode("SESP", workorderLiteTO.getInstCode(), "");
                        }
                        infoMessage = getString(R.string.data_sent_to_printer);
                    } catch (Exception e) {
                        writeLog("SearchOrdersToPrintActivity : runInBackground()", e);
                        infoMessage = getString(R.string.error_in_printing);
                    }
                } else {
                    infoMessage = getString(R.string.printer_connected_fail);
                }
            } catch (Exception ex) {
                writeLog(TAG + " : runInBackground()", ex);
                infoMessage = getString(R.string.printer_connected_fail);
            } finally {
                labelPrinter.closeConnection();
            }
            return infoMessage;
        }

        @Override
        protected void onPostExecute(final boolean successful, final String infoToUser) {
            try {
                if (infoToUser != null) {
                    AlertDialog.Builder infoDialog = GuiController.showInfoDialog(SearchOrdersToPrintActivity.this, infoToUser);
                    infoDialog.setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    infoDialog.show();
                }
            } catch (Exception e) {
                writeLog(TAG + " : onPostExecute()", e);
            }
        }
    }
}
