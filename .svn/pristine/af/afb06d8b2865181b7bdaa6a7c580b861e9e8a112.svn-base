package com.capgemini.sesp.ast.android.ui.activity.order;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.cache.WorkorderCache;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO.SortByInstCode;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.MeterChangeConstants;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.OrderListKeys;
import com.capgemini.sesp.ast.android.module.util.GuiDate;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.ui.activity.navigation.ShowWorkOrdersInMap;
import com.capgemini.sesp.ast.android.ui.activity.receiver.NetworkStatusMonitorReceiver;
import com.capgemini.sesp.ast.android.ui.adapter.WorkOrderCategoryAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.to.cft.table.PlanningPeriodTO;
import com.skvader.rsp.cft.common.util.Utils;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.CaseTCustomTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.CASE_TYPE_NULL;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


@SuppressLint("InflateParams")
public class OrderListActivity extends AppCompatActivity implements NetworkStatusCallbackListener {
    private final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = OrderListActivity.class.getSimpleName();
    /**
     * The {@link androidx.core.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link androidx.core.app.FragmentStatePagerAdapter}.
     */
    // SectionsPagerAdapter mSectionsPagerAdapter;

    /*
     * Local broadcast receiver used to
     * receive network connectivity information asynchronously
     */
    private transient BroadcastReceiver networkStatusReceiver = null;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    protected List<WorkorderLiteTO> workorderList;
    private List<WorkorderLiteTO> filteredWo;
    private List<WorkorderLiteTO> advSearchList;

    public List<WorkorderLiteTO> getWorkorderList() {
        return workorderList;
    }

    private int currentposition = 0;

    /**
     * Adapter sections for the respective categories
     */
    private transient WorkOrderCategoryAdapter workOrderMeterAdapter = null;
    private transient WorkOrderCategoryAdapter workOrderCustomerAdapter = null;
    private transient WorkOrderCategoryAdapter workOrderAddressAdapter = null;
    private transient WorkOrderCategoryAdapter workOrderTimeReservationAdapter = null;
    private transient WorkOrderCategoryAdapter workOrderSLAAdapter = null;
    private transient WorkOrderCategoryAdapter workOrderKeyAdapter = null;

    private OrderSummaryListAdapter mAdapter;
    protected RecyclerView recyclerView;

    private Long idCaseT;
    private Long ppIdFilter;
    private LinearLayout filterLayout;

    private EditText timeReservationStart;
    private EditText timeReservationEnd;
    private Spinner spinnerPlan;
    private ArrayAdapter<String> planAdapter;
    private CheckBox filterCheckBox;
    //private RelativeLayout filterPopup;
    private Animation slideAnim = null;

    private TextView orderListTitle;
    private ImageView networStateIcon;

    private List<OrderSummary> orderSummary;

    private List<String> itemTitles;
    private int sortChoice = 0;

    protected DividerItemDecoration dividerItemDecoration;

    @Override
    public void networkStatusChanged(boolean isConnected) {
        try {
            Log.d(OrderListActivity.class.getSimpleName(), "networkStatusChanged called, value = " + isConnected);
            networStateIcon.setVisibility(View.VISIBLE);
            SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
            if (isConnected) {
                networStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_4_bar_white));
            } else {
                networStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_null_black));
            }
        } catch (Exception e) {
            writeLog(TAG + ": networkStatusChanged()", e);
        }
    }
    //private transient PopupWindow changeStatusPopUp = null;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.order_list);

            getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(false);


        /*
         * Setting up custom action bar view
		 */
            final ActionBar.LayoutParams layout =
                    new ActionBar.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
            ImageButton help_btn = vw.findViewById(R.id.menu_help);
            help_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new HelpDialog(OrderListActivity.this, ConstantsAstSep.HelpDocumentConstant.PAGE_ORDER_LIST);
                    dialog.show();
                }
            });
            TextView title = vw.findViewById(R.id.title_text);
            title.setText(getResources().getString(R.string.title_order_list));

            // -- Customizing the action bar ends -----

            final View localeFlag = vw.findViewById(R.id.save_btn);
            if (localeFlag != null) {
                localeFlag.setVisibility(View.INVISIBLE);
            }
            networStateIcon = vw.findViewById(R.id.network_state_iv);
            getSupportActionBar().setCustomView(vw, layout);
            getSupportActionBar().setDisplayShowCustomEnabled(true);


            idCaseT = SESPPreferenceUtil.getPreference("SELECTED_ID_CASE_T");
            try {
                workorderList = (!CASE_TYPE_NULL.equals(this.idCaseT)) ? WorkorderCache.getWorkOrdersByType(idCaseT) : WorkorderCache.getWorkordersLite(true);
            } catch (Exception e) {
                workorderList = new ArrayList<WorkorderLiteTO>();
                GuiController.showErrorDialog(this, getString(R.string.error_cannot_update_wo));
            }
            final Map<Long, String> planningPeriodMap = new ArrayMap<Long, String>();
            if (!workorderList.isEmpty()) {
                for (WorkorderLiteTO woLiteTo : workorderList) {
                    if (woLiteTo.getIdPlanningPeriod() != null) {
                        PlanningPeriodTO planningPeriodTO = ObjectCache.getIdObject(PlanningPeriodTO.class, woLiteTo.getIdPlanningPeriod());
                        if (planningPeriodTO != null) {
                            planningPeriodMap.put(woLiteTo.getIdPlanningPeriod(), planningPeriodTO.getName());
                        }
                    }
                }
            }


            int size = getWorkorderList().size();


            final ImageView cancel = findViewById(R.id.cancel);
            final ImageView cancelTROrders = findViewById(R.id.cancel_orders);
            final ImageView slideFilterLayout = findViewById(R.id.swap);
            final ImageView slidetimeReservedOrdersLayout = findViewById(R.id.swap_orders);

            itemTitles = new ArrayList<String>();
            itemTitles.add(getString(R.string.installation_code));
            itemTitles.add(getString(R.string.identifier) + "/" + getString(R.string.value));
            itemTitles.add(getString(R.string.model));
            itemTitles.add(getString(R.string.name));
            itemTitles.add(getString(R.string.address));
            itemTitles.add(getString(R.string.time_reservation));
            itemTitles.add(getString(R.string.deadline));
            itemTitles.add(getString(R.string.relative_time));
            itemTitles.add(getString(R.string.key_number));
            itemTitles.add(getString(R.string.key_info));
            itemTitles.add(getString(R.string.wo_number));

            orderListTitle = findViewById(R.id.order_list_title);
            recyclerView = findViewById(R.id.recyclerview);
            dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            populateData(this.getWorkorderList(), getItemTitles());

            final OrderListClickListener listener = new OrderListClickListener(this);
            cancel.setOnClickListener(listener);
            cancelTROrders.setOnClickListener(listener);
            slideFilterLayout.setOnClickListener(listener);
            slidetimeReservedOrdersLayout.setOnClickListener(listener);


            //((TextView) findViewById(R.id.title_text)).setText(getResources().getStringArray(R.array.order_list_pages)[0]);
            //View view=layoutManager.inflate(R.layout.order_list,null);
            filterLayout = findViewById(R.id.filter_layout);
            timeReservationStart = findViewById(R.id.from);
            timeReservationEnd = findViewById(R.id.to);
            spinnerPlan = findViewById(R.id.plan);
            filterCheckBox = findViewById(R.id.time_reserved_orders);

            String[] planAdapterArray = getResources().getStringArray(R.array.plan);
            int sizeFinal = planAdapterArray.length;
            if (!planningPeriodMap.isEmpty()) {
                sizeFinal = sizeFinal + planningPeriodMap.values().size();
            }
            String[] finalPPAdapter = new String[sizeFinal];
            finalPPAdapter[0] = planAdapterArray[0];
            List<String> ppList = new ArrayList<String>();
            int counter = 1;
            if (!planningPeriodMap.isEmpty()) {
                for (String ppValue : planningPeriodMap.values()) {
                    finalPPAdapter[counter] = ppValue;
                    counter++;
                }
            }
            planAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, finalPPAdapter);
            planAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPlan.setAdapter(planAdapter);
            spinnerPlan.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    ppIdFilter = null;
                    if (!planningPeriodMap.isEmpty()) {
                        Iterator<Long> ppMapItr = planningPeriodMap.keySet().iterator();
                        while (ppMapItr.hasNext()) {
                            Long itrValue = ppMapItr.next();
                            if (((String) spinnerPlan.getSelectedItem()).equalsIgnoreCase(planningPeriodMap.get(itrValue))) {
                                ppIdFilter = itrValue;
                                break;
                            }
                        }
                    }
                    if (ppIdFilter != null) {
                        if (filterCheckBox.isChecked()) {
                            workorderList = WorkorderCache.getPlannedWorkOrdersByType(ppIdFilter, idCaseT, true);
                        } else {
                            workorderList = WorkorderCache.getPlannedWorkOrdersByType(ppIdFilter, idCaseT, false);
                        }
                    } else {
                        if (filterCheckBox.isChecked()) {
                            workorderList = WorkorderCache.getWorkOrdersByTimeReserved(idCaseT);
                        } else {
                            workorderList = WorkorderCache.getWorkOrdersByType(idCaseT);
                        }
                    }

                    populateData(workorderList, itemTitles);
                    mAdapter.notifyDataSetChanged();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    populateData(getWorkorderList(), itemTitles);
                }
            });

            filterCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (ppIdFilter != null) {
                        if (filterCheckBox.isChecked()) {
                            workorderList = WorkorderCache.getPlannedWorkOrdersByType(ppIdFilter, idCaseT, true);
                        } else {
                            workorderList = WorkorderCache.getPlannedWorkOrdersByType(ppIdFilter, idCaseT, false);
                        }
                    } else {
                        if (filterCheckBox.isChecked()) {
                            workorderList = WorkorderCache.getWorkOrdersByTimeReserved(idCaseT);
                        } else {
                            workorderList = WorkorderCache.getWorkOrdersByType(idCaseT);
                        }
                    }

                    populateData(workorderList, itemTitles);
                    mAdapter.notifyDataSetChanged();

                }
            });
        } catch (Exception e) {
            writeLog(TAG + ": onCreate()", e);
        }
    }


    public void populateData(List<WorkorderLiteTO> workOrders, List<String> itemTitles) {

        String timeReservationImage;
        String startedImage;
        String assignedImage;
        String keyImage;
        String rowtitle;
        String temp;
        String priority;
        List<OrderSummaryItems> orderItemTitle = null;
        orderSummary = new ArrayList<OrderSummary>();
        List<String> orderInfo;

        try {
            for (WorkorderLiteTO wo : workOrders) {

                orderInfo = getWoInfo(wo);
                temp = orderInfo.set(0, orderInfo.get(sortChoice));
                orderInfo.set(sortChoice, temp);
                rowtitle = orderInfo.get(0);

                List<OrderSummaryItems> orderSummaryItems = new ArrayList<>();

                for (int i = 1; i <= 10; i++) {
                    orderSummaryItems.add(new OrderSummaryItems(itemTitles.get(i), orderInfo.get(i)));
                    orderSummaryItems.get(i - 1).setWorkOrder(wo);
                }

                if (wo.isStarted()) {
                    startedImage = "visible";
                } else {
                    startedImage = "invisible";
                }

                if (wo.isAssigned()) {
                    assignedImage = "team";
                } else {
                    assignedImage = "user";
                }

                if (wo.getKeyInfo() != null || wo.getKeyNumber() != null) {
                    keyImage = "visible";
                } else {
                    keyImage = "invisible";
                }
                if (wo.getPriorityShortName() == null) {
                    priority = "L";
                } else
                    priority = wo.getPriorityShortName().toString();
                if (wo.getTimeReservationStart() != null) {
                    timeReservationImage = "visible";

                    long timeLeft = wo.getTimeReservationStart().getTime() - System.currentTimeMillis();
                    if (timeLeft < ConstantsAstSep.THRESHOLD_TIME_RESERAVTION_WARNING) {
                        timeReservationImage += " ic_clock_red";
                    } else {
                        timeReservationImage += " ic_time_reservation";
                    }
                } else {
                    timeReservationImage = "invisible";
                }


                orderSummary.add(new OrderSummary(rowtitle, orderSummaryItems, timeReservationImage, startedImage, assignedImage, keyImage, priority));

            }
            mAdapter = new OrderSummaryListAdapter(this, orderSummary);
            recyclerView.setAdapter(mAdapter);


            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }


    }

    public List<String> getWoInfo(WorkorderLiteTO wo) {
        WoUnitTO meterWoUnitTO;
        List<String> orderInfo = null;
        String identifierValue = "";
        String identifier = null;
        String model = null;
        String name;
        String address = null;
        String timeReservation = null;
        String deadline = null;
        String relativeTime = null;
        String keyNumber;
        String keyInfo;
        String workorderId;
        meterWoUnitTO = wo.getMeterWoUnitTO();
        try {
            if (meterWoUnitTO != null) {

                if (meterWoUnitTO.getIdUnitIdentifierT().equals(CONSTANTS().UNIT_IDENTIFIER_T__GIAI)) {
                    identifierValue = meterWoUnitTO.getGiai();
                } else if (meterWoUnitTO.getIdUnitIdentifierT().equals(CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO)) {
                    identifierValue = meterWoUnitTO.getSerialNumber();
                } else if (meterWoUnitTO.getIdUnitIdentifierT().equals(CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO)) {
                    identifierValue = TypeDataUtil.getValidOrgDivValue(meterWoUnitTO, WoUnitTO.PROPERTY_NUMBER_D);
                }
                // Populate meter number
                identifier = ObjectCache.getTypeName(UnitIdentifierTTO.class, meterWoUnitTO.getIdUnitIdentifierT()) + "/" + identifierValue;
                model = ObjectCache.getIdObjectName(UnitModelCustomTO.class, meterWoUnitTO.getUnitModel());
            }
            name = wo.getCustomerName();
            String postcodeCity[] = {""};
            if (Utils.isNotEmpty(wo.getAddressCity()))
                postcodeCity = wo.getAddressCity().split(" ");
            if (Utils.isNotEmpty(wo.getAddressStreet()) && postcodeCity.length != 0) {
                address = wo.getAddressStreet() + ", " + postcodeCity[postcodeCity.length - 1];
            }
            if (wo.getTimeReservationStart() != null) {
                GuiDate timeresStart = GuiDate.toGuiDateUsersTimesZone(wo.getTimeReservationStart(), true);
                timeReservation = GuiDate.formatSameDateAndTime(wo.getTimeReservationStart(), wo.getTimeReservationEnd());
            }
            if (wo.getSlaDeadline() != null) {
                GuiDate slaDeadline = GuiDate.toGuiDateUsersTimesZone(wo.getSlaDeadline(), true);
                deadline = slaDeadline.toString();
                relativeTime = slaDeadline.getRelativeTime();
            }

            keyInfo = wo.getKeyInfo();
            keyNumber = wo.getKeyNumber();
            workorderId = wo.getId().toString();
            orderInfo = new ArrayList<String>();
            orderInfo.add(wo.getInstCode() == null ? "" : wo.getInstCode());
            orderInfo.add(identifier == null ? "" : identifier);
            orderInfo.add(model == null ? "" : model);
            orderInfo.add(name == null ? "" : name);
            orderInfo.add(address == null ? "" : address);
            orderInfo.add(timeReservation == null ? "" : timeReservation);
            orderInfo.add(deadline == null ? "" : deadline);
            orderInfo.add(relativeTime == null ? "" : relativeTime);
            orderInfo.add(keyNumber == null ? "" : keyNumber);
            orderInfo.add(keyInfo == null ? "" : keyInfo);
            orderInfo.add(workorderId == null ? "" : workorderId);
        } catch (Exception e) {
            writeLog(TAG + ": getWoInfo()", e);
        }
        return orderInfo;


    }

    public List<String> sortItemTitleList(int swapPosition) {
        List<String> tempList = new ArrayList<>();
        try {
            tempList.addAll(getItemTitles());
            String temp;
            temp = tempList.set(0, tempList.get(swapPosition));
            tempList.set(swapPosition, temp);
        } catch (Exception e) {
            writeLog(TAG + ": sortItemTitleList()", e);
        }
        return tempList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.d(TAG, "onResume called");
            LanguageHelper.reloadIfLanguageChanged(this);
            final IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

            this.networkStatusReceiver = new NetworkStatusMonitorReceiver(this);
            // Register broadcast receiver here
            registerReceiver(this.networkStatusReceiver, filter);
        } catch (Exception e) {
            writeLog(TAG + ": onResume()", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // De-register the broadcast receiver here
        if (this.networkStatusReceiver != null) {
            unregisterReceiver(this.networkStatusReceiver);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.search_view_menu_item, menu);
            MenuItem searchViewItem = menu.findItem(R.id.action_search);
            final SearchView searchViewAndroidActionBar =
                    (SearchView) searchViewItem.getActionView();
            searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchViewAndroidActionBar.clearFocus();
                    List<WorkorderLiteTO> workOrders = getWorkorderList();
                    List<WorkorderLiteTO> filteredWo = new ArrayList<>();
                    List<String> orderinfo;
                    for (WorkorderLiteTO wo : workOrders) {
                        orderinfo = getWoInfo(wo);
                        if (orderinfo.contains(query) || wo.getInstCode().toLowerCase().contains(query.toLowerCase())) {
                            filteredWo.add(wo);
                            continue;
                        }
                    }
                    populateData(filteredWo, getItemTitles());
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            searchViewItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    populateData(getWorkorderList(), getItemTitles());
                    return true;
                }
            });
       /* searchViewAndroidActionBar.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                populateData(getWorkorderList());
                return true;
            }
        });*/
            getMenuInflater().inflate(R.menu.order_list, menu);
        } catch (Exception e) {
            writeLog(TAG + ": onCreateOptionsMenu()", e);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.menu_filter) {
            if (findViewById(R.id.filter_popup).getVisibility() == View.GONE) {
                findViewById(R.id.filter_popup).setVisibility(View.VISIBLE);
                slideUP(filterLayout);
            }
            return true;
        } else if (itemId == R.id.menu_adv_search) {
            Intent searchIntent = new Intent(getApplicationContext(), AdvancedSearchActivity.class);
            startActivityForResult(searchIntent, 1);
            return true;
        } else if (itemId == R.id.map) {
            if (AndroidUtilsAstSep.isNetworkAvailable()) {
                Intent showWosInMap = new Intent(this, ShowWorkOrdersInMap.class);
                Bundle bundle = new Bundle();
                ArrayList woIds = new ArrayList<String>();

                for (WorkorderLiteTO workorderLiteTO : workorderList) {
                    woIds.add(workorderLiteTO.getId().toString());
                }

                bundle.putStringArrayList("WO_LIST", woIds);
                bundle.putLong("CASE_T", idCaseT);
                showWosInMap.putExtras(bundle);
                startActivity(showWosInMap);
            } else
                Toast.makeText(this, this.getString(R.string.turn_on_internet), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> searchCriteria;
        List<String> woInfo;
        advSearchList = new ArrayList<WorkorderLiteTO>();
        Log.i("OrderListActivity", "inside onActivityResult()");
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                searchCriteria = data.getStringArrayListExtra("SearchCriteriaList");
                for (WorkorderLiteTO wo : workorderList) {
                    if (!searchCriteria.get(0).isEmpty()) {
                        if (wo.getInstCode().toLowerCase().contains(searchCriteria.get(0).toLowerCase()))
                            advSearchList.add(wo);
                    }
                }
                for (WorkorderLiteTO wo : workorderList) {
                    woInfo = getWoInfo(wo);
                    if (!searchCriteria.get(1).isEmpty() && !woInfo.get(2).isEmpty()) {
                        if (woInfo.get(2).toLowerCase().contains(searchCriteria.get(1).toLowerCase()))
                            advSearchList.add(wo);
                    }
                }
                for (WorkorderLiteTO wo : workorderList) {
                    woInfo = getWoInfo(wo);
                    if (!searchCriteria.get(2).isEmpty() && !woInfo.get(4).isEmpty()) {
                        if (woInfo.get(4).toLowerCase().contains(searchCriteria.get(2).toLowerCase()))
                            advSearchList.add(wo);
                    }
                }
                for (WorkorderLiteTO wo : workorderList) {
                    woInfo = getWoInfo(wo);
                    if (!searchCriteria.get(3).isEmpty() && !woInfo.get(6).isEmpty() && !woInfo.get(7).isEmpty()) {
                        if (woInfo.get(6).toLowerCase().contains(searchCriteria.get(3).toLowerCase()) || woInfo.get(7).toLowerCase().contains(searchCriteria.get(3).toLowerCase()))
                            advSearchList.add(wo);
                    }
                }
                Set uniqueData = new HashSet();
                uniqueData.addAll(advSearchList);
                advSearchList = new ArrayList<>();
                advSearchList.addAll(uniqueData);
                populateData(advSearchList, sortItemTitleList(sortChoice));
            } else if (resultCode == RESULT_CANCELED) {
                populateData(getWorkorderList(), sortItemTitleList(sortChoice));
            }
        }
    }

    public void sortWO(View view) {
        AlertDialog levelDialog;
        final CharSequence[] items = {getResources().getString(R.string.installation_code), "Identifier/value", getResources().getString(R.string.model), getResources().getString(R.string.name), getResources().getString(R.string.address), getResources().getString(R.string.time_reservation), getResources().getString(R.string.deadline), getResources().getString(R.string.relative_time), getResources().getString(R.string.key_no), getResources().getString(R.string.key_info), getResources().getString(R.string.wo_number)};
        List<WorkorderLiteTO> wo = getWorkorderList();
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sort_title);
        builder.setSingleChoiceItems(items, sortChoice, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                sortChoice = item;
                switch (item) {
                    case 0:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.INST_CODE));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.installation_code));
                        break;
                    case 1:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.METER_SRNO));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.identifier) + "/" + getResources().getString(R.string.value));
                        break;
                    case 2:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.METER_MODEL));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.model));
                        break;
                    case 3:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.CUST_NAME));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.name));
                        break;
                    case 4:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.ADDRESS));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.address));
                        break;
                    case 5:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.TIME_RESERVATION));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.time_reservation));
                        break;
                    case 6:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.SLA_DEADLINE));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.sla_deadline));
                        break;
                    case 7:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.SLA_DEADLINE));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.relative_time));
                        break;
                    case 8:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.KEY_NUMBER));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.key_number));
                        break;
                    case 9:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.KEY_INFO));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.key_info));
                        break;
                    case 10:
                        try {
                            Collections.sort(wo, new SortByInstCode(false, OrderListKeys.WO_NUMBER));
                        } catch (NullPointerException e) {
                            writeLog(TAG + ":sortWO  :Some values are missing ", e);
                        }
                        orderListTitle.setText(getResources().getString(R.string.wo_number));
                        break;
                }
                populateData(wo, sortItemTitleList(sortChoice));
                dialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();

    }

    public void slideUP(LinearLayout layout) {
        if (layout != null) {
            layout.setVisibility(View.VISIBLE);
            final Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            setSlideAnim(anim);
            layout.startAnimation(anim);
        }
    }

    public void slideDown(final LinearLayout layout, final boolean isClosed) {

        final Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.slide_down);
        setSlideAnim(anim);
        layout.startAnimation(anim);
        layout.setLayoutAnimationListener(null);

        anim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                //Do nothing
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setVisibility(View.GONE);
                if (isClosed) {
                    findViewById(R.id.filter_popup).setVisibility(View.GONE);
                }
            }
        });
    }


    public OnItemClickListener getListViewItemClickListener() {
        return listViewItemClicked;
    }

    private OnItemClickListener listViewItemClicked = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position,
                                long id) {
            final WorkorderLiteTO workOrder = workorderList.get(position);
            if (workOrder != null) {
                if (workOrder.getTimeReservationStart() != null) {
                    timeReservationStart.setText(workOrder
                            .getTimeReservationStart().toString());
                } else {
                    timeReservationStart.setText("");

                }

                if (workOrder.getTimeReservationEnd() != null) {
                    timeReservationEnd.setText(workOrder
                            .getTimeReservationEnd().toString());
                } else {
                    timeReservationEnd.setText("");

                }

                //final CaseTTO caseT = ObjectCache.getIdObject(CaseTTO.class, workOrder.getIdCaseT());
                final CaseTCustomTO caseT = ObjectCache.getIdObject(CaseTCustomTO.class, workOrder.getIdCaseT());

                if (caseT != null) {
                    // Single if block to handle all type of work-orders
                    final Intent intent = new Intent();
                    intent.setAction(caseT.getCode());

                    // Check if user has defined an activity to handle/display this case type
                    final ComponentName caseHandlerComp = intent.resolveActivity(getPackageManager());
                    if (caseHandlerComp != null) {
                        intent.putExtra(MeterChangeConstants.ID_CASE, workOrder.getId());
                        intent.putExtra(MeterChangeConstants.ALLOW_START_FLOW, true);
                        startActivity(intent);
                    } else {
                        // Show message to user that for this case type no component is defined
                        // To be implemented
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_not_implemented), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };


    public List<String> getItemTitles() {
        return itemTitles;
    }

    public void setItemTitles(List<String> itemTitles) {
        this.itemTitles = itemTitles;
    }

    public WorkOrderCategoryAdapter getWorkOrderMeterAdapter() {
        return workOrderMeterAdapter;
    }

    public void setWorkOrderMeterAdapter(
            final WorkOrderCategoryAdapter workOrderMeterAdapter) {
        this.workOrderMeterAdapter = workOrderMeterAdapter;
    }

    public WorkOrderCategoryAdapter getWorkOrderCustomerAdapter() {
        return workOrderCustomerAdapter;
    }

    public void setWorkOrderCustomerAdapter(
            final WorkOrderCategoryAdapter workOrderCustomerAdapter) {
        this.workOrderCustomerAdapter = workOrderCustomerAdapter;
    }

    public WorkOrderCategoryAdapter getWorkOrderAddressAdapter() {
        return workOrderAddressAdapter;
    }

    public void setWorkOrderAddressAdapter(
            final WorkOrderCategoryAdapter workOrderAddressAdapter) {
        this.workOrderAddressAdapter = workOrderAddressAdapter;
    }

    public WorkOrderCategoryAdapter getWorkOrderTimeReservationAdapter() {
        return workOrderTimeReservationAdapter;
    }

    public void setWorkOrderTimeReservationAdapter(
            final WorkOrderCategoryAdapter workOrderTimeReservationAdapter) {
        this.workOrderTimeReservationAdapter = workOrderTimeReservationAdapter;
    }

    public WorkOrderCategoryAdapter getWorkOrderSLAAdapter() {
        return workOrderSLAAdapter;
    }

    public void setWorkOrderSLAAdapter(final WorkOrderCategoryAdapter workOrderSLAAdapter) {
        this.workOrderSLAAdapter = workOrderSLAAdapter;
    }

    public WorkOrderCategoryAdapter getWorkOrderKeyAdapter() {
        return workOrderKeyAdapter;
    }

    public void setWorkOrderKeyAdapter(final WorkOrderCategoryAdapter workOrderKeyAdapter) {
        this.workOrderKeyAdapter = workOrderKeyAdapter;
    }


    public Animation getSlideAnim() {
        return slideAnim;
    }

    public void setSlideAnim(final Animation slideAnim) {
        this.slideAnim = slideAnim;
    }

}
