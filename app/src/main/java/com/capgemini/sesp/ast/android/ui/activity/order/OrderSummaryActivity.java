package com.capgemini.sesp.ast.android.ui.activity.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.dataloader.WorkorderLtoCategoryLoader;
import com.capgemini.sesp.ast.android.module.tsp.MapsActivity;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DownloadWOThread;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.document_download.DocumentListActivityThread;
import com.capgemini.sesp.ast.android.ui.activity.receiver.NetworkStatusMonitorReceiver;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;

import java.util.Collections;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.CASE_TYPE_NULL;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * This activity class is last modified on 23rd July, 2014
 * for SESPSTD-3182
 *
 * @author Capgemini
 * @since 23rd July, 2014
 */

@SuppressLint("InflateParams")
public class OrderSummaryActivity extends AppCompatActivity implements NetworkStatusCallbackListener {
    OrderSummaryCategory allOrders=null;
    public static final String INTENT_KEY_ID_CASE_T = "idCaseT";
    private static final String TAG = OrderSummaryActivity.class.getSimpleName();

    ListView listView;
    protected String helpTitle = "ORDER_SUMMARY";

    private transient OrderSummaryAdapter orderSummaryAdapter = null;
    private ImageView networStateIcon;

    /*
     * Local broadcast receiver used to
     * receive network connectivity information asynchronously
     */
    private transient BroadcastReceiver networkStatusReceiver = null;

    @Override
    public void networkStatusChanged(boolean isConnected) {
        Log.d(OrderSummaryActivity.class.getSimpleName(), "networkStatusChanged called, value = " + isConnected);
        networStateIcon.setVisibility(View.VISIBLE);
        SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
        if (isConnected) {
            networStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_4_bar_white));
        } else {
            networStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_null_black));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_order_summary);

        // Hiding the logo as requested
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);
        
        /*
         * Setting up custom action bar view
		 */
        final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
            ImageButton help_btn = vw.findViewById(R.id.menu_help);
            help_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new HelpDialog(OrderSummaryActivity.this, ConstantsAstSep.HelpDocumentConstant.ORDER_SUMMARY);
                    dialog.show();
                }
            });

        // -- Customizing the action bar ends -----

        final View localeFlag = vw.findViewById(R.id.save_btn);
        localeFlag.setVisibility(View.INVISIBLE);
        networStateIcon = vw.findViewById(R.id.network_state_iv);
        getSupportActionBar().setCustomView(vw, layout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        listView = findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onOrderListItemClick(listView, view, position, id);


            }
        });

        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(getString(R.string.title_activity_order_summary));
        //Removed new workorderLtoCategory loader from on start and implemented it in oncreate to prevent everytime execution
        new WorkorderLtoCategoryLoader(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            writeLog(TAG + " : onCreate()" ,e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_order_summary, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
        Log.d(TAG, "onResume called");
        LanguageHelper.reloadIfLanguageChanged(this);

        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        this.networkStatusReceiver = new NetworkStatusMonitorReceiver(this);
        // Register broadcast receiver here
        registerReceiver(this.networkStatusReceiver, filter);
        } catch (Exception e) {
            writeLog(TAG + " : onResume()" ,e);
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

    //@Override
    public void onOrderListItemClick(ListView l, View v, int position, long id) {
        OrderSummaryCategory category = (OrderSummaryCategory) v.getTag();
        try {
            Intent intent = new Intent(this, ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.ORDER_LIST_ACTIVITY));
            SESPPreferenceUtil.savePreference("SELECTED_ID_CASE_T", category.getIdCaseT());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            writeLog(TAG + " : onOrderListItemClick()", e);
        }

    }


    /**
     * New method added to
     * avoid code duplication
     */
    private void showReload() {
        new UpdateWOThread(this, null).start();
    }

    /**
     * New method added to
     * avoid code duplication
     */
    private void showStatus() {
        try{
        final Intent intent = new Intent(OrderSummaryActivity.this, StatusActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        } catch (Exception e) {
            writeLog(TAG + " : showStatus()" ,e);
        }
    }
    private void showDocumentList(CharSequence itemName) {
        String documentType = itemName.toString();
        String type;
        if (documentType.equalsIgnoreCase("installation")) {
            type = ConstantsAstSep.DocumentDownloadAction.INSTALLATION_DOC_TYPE;
        } else {
            type = ConstantsAstSep.DocumentDownloadAction.SAFETY_DOC_TYPE;
        }
        DocumentListActivityThread thread = new DocumentListActivityThread(this, type);
        thread.start();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CharSequence itemname = item.getTitle();
        int itemId = item.getItemId();
        if (itemId == R.id.menu_reload) {
            showReload();
            return true;
        } else if (itemId == R.id.menu_status) {
            showStatus();
            return true;
        }
        else  if (itemId == R.id.menu_document){
            item.getSubMenu().clearHeader();
            return true;
        }
        else if (itemId == R.id.installation_document) {
            showDocumentList(itemname);
            return true;
        } else if (itemId == R.id.safety_document) {
            showDocumentList(itemname);
            return true;
        }
        else if (itemId == R.id.menu_tsp){
            openMap();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void openMap() {
        if(allOrders.getCount()>0 && AndroidUtilsAstSep.isNetworkAvailable()) {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            startActivity(mapIntent);
        }
    }

    private class UpdateWOThread extends DownloadWOThread {

        public UpdateWOThread(Activity ownerActivity, Class<? extends Activity> nextActivityClass) {
            super(ownerActivity, nextActivityClass);
        }

        @Override
        protected void onPostExecute(boolean successful, Void result) {
            if (successful) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    /**
     * The method is called when all the workorder lto categories are loaded
     * from database (sqlite), time to update the UI here
     */
    public void onLoadFinished(
            final List<OrderSummaryCategory> dataOrderSummaryCategories) {
        try {
            final ListView orderSummaryListView = findViewById(android.R.id.list);
            if (orderSummaryListView != null) {
                //Create list item for all orders in Order Summary Page
                allOrders = new OrderSummaryCategory();
                allOrders.setIdCaseT(CASE_TYPE_NULL);
                for (OrderSummaryCategory orderSummaryCategory : dataOrderSummaryCategories) {
                    allOrders.setNumberOfWoCount(orderSummaryCategory.getCount() + allOrders.getCount());
                    allOrders.setTimeReservationCount(orderSummaryCategory.getTimeReservationCount() + allOrders.getTimeReservationCount());
                }
                dataOrderSummaryCategories.add(allOrders);
                Collections.sort(dataOrderSummaryCategories);
                orderSummaryAdapter = new OrderSummaryAdapter(this, dataOrderSummaryCategories);
                listView.setAdapter(orderSummaryAdapter);
                orderSummaryAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            writeLog(TAG + " : onLoadFinished()", e);
        }
    }


}
