package com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_balance;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.StockBalanceKeys;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.adapter.StockBalanceAdapter;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.ast_sep.common.to.ast.table.StockTO;
import com.skvader.rsp.ast_sep.common.to.custom.BulkSummaryTO;
import com.skvader.rsp.ast_sep.common.to.custom.DeviceSummaryTO;
import com.skvader.rsp.ast_sep.common.to.custom.StockBalanceTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class StockBalanceActivity extends AppCompatActivity {

    ListView stocklist = null;
    private List<StockBalanceRow> list;
    private boolean sortByDesc = false;
    private StockBalanceAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_stock_balance);
      /*      findViewById(R.id.option_btn).setVisibility(View.VISIBLE);
        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(getString(R.string.title_activity_stock_balance));

        View titlebar= findViewById(R.id.title_bar);

        titlebar.setVisibility(View.GONE);

        getSupportActionBar().setTitle(getString(R.string.title_activity_stock_balance));*/
            // Hiding the logo as requested
            getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(false);

            /*
             * Setting up custom action bar view
             */
            final ActionBar.LayoutParams layout =
                    new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
            ImageButton help_btn = vw.findViewById(R.id.menu_help);
            help_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new HelpDialog(StockBalanceActivity.this, ConstantsAstSep.HelpDocumentConstant.PAGE_STOCK_BALANCE);
                    dialog.show();
                }
            });

            // -- Customizing the action bar ends -----


            getSupportActionBar().setCustomView(vw);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            TextView txtTitleBar = findViewById(R.id.title_text);
            txtTitleBar.setText(R.string.title_activity_stock_balance);


        stocklist =findViewById(android.R.id.list);

        TextView locationTextView = findViewById(R.id.location);
        locationTextView.setText(getText(R.string.location) + ": " + ObjectCache.getIdObject(StockTO.class,
                SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK)).getName());

        createSortListeners();
        } catch (Exception e) {
            writeLog(" StockBalanceActivity : onCreate() ", e);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        StockBalanceTO stockBalanceTO = (StockBalanceTO) getIntent().getExtras().get("StockBalanceTO");
        onReloadStockBalance(stockBalanceTO);
    }

    private void createSortListeners() {
        /* Only Name and Status header need listeners since they cannot use android:onClick */
        TextView nameHeader = findViewById(R.id.header1);
        nameHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sortOnNameClicked(null);
            }
        });
        TextView statusHeader = findViewById(R.id.header2);
        statusHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sortOnStatusClicked(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_stock_balance2, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_reload) {
            new StockBalanceFetchThread(this).start();
        }

        return true;
    }

    private class StockBalanceFetchThread extends GuiWorker<StockBalanceTO> {

        public StockBalanceFetchThread(AppCompatActivity ownerActivity) {
            super(ownerActivity, StockBalanceActivity.class);
        }

        @Override
        protected StockBalanceTO runInBackground() throws Exception {
            setMessage(R.string.fetching_stock_balance);
            return AndroidUtilsAstSep.getDelegate().getStockBalance(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK));
        }

        @Override
        protected void onPostExecute(boolean successful, StockBalanceTO stockBalanceTO) {
            if(successful) {
                onReloadStockBalance(stockBalanceTO);
            }
        }

    }

    protected void onReloadStockBalance(StockBalanceTO stockBalanceTO){
        list = parse(stockBalanceTO);
        sortByDesc = false;
        Collections.sort(list, new StockBalanceSort(sortByDesc, StockBalanceKeys.NAME));
        adapter = new StockBalanceAdapter(StockBalanceActivity.this, list, R.layout.stock_balance_row);
        stocklist.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void sortOnNameClicked(View view) {
        sort(StockBalanceKeys.NAME);
    }

    public void sortOnStatusClicked(View view) {
        sort(StockBalanceKeys.STATUS);
    }

    public void sortOnAmountClicked(View view) {
        sort(StockBalanceKeys.AMOUNT);
    }

    private void sort(StockBalanceKeys sortKey) {
        try{
        sortByDesc = !sortByDesc;
        Collections.sort(list, new StockBalanceSort(sortByDesc, sortKey));
        adapter.notifyDataSetChanged();
        } catch (Exception npe){
            writeLog("StockBalanceActivity :sort()",npe );
        }
    }

    private List<StockBalanceRow> parse(StockBalanceTO stockBalanceTO) {
        List<StockBalanceRow> list = new ArrayList<StockBalanceRow>();
        StockBalanceRow stockBalanceRow;

        try {

        for (DeviceSummaryTO deviceSummaryTO : stockBalanceTO.getDeviceSummaryTOs()) {
            stockBalanceRow = new StockBalanceRow();
            stockBalanceRow.setName(deviceSummaryTO.getUnitModelTO().getName());
            stockBalanceRow.setStatus(deviceSummaryTO.getUnitStatusTTO().getName());
            stockBalanceRow.setAmount(deviceSummaryTO.getAmount());
            list.add(stockBalanceRow);
        }

        for (BulkSummaryTO bulkSummaryTO : stockBalanceTO.getBulkSummaryTOs()) {
            stockBalanceRow = new StockBalanceRow();
            stockBalanceRow.setName(bulkSummaryTO.getUnitModelTO() != null ? bulkSummaryTO.getUnitModelTO().getName() : "");
            stockBalanceRow.setStatus(getString(R.string.bulk_unit));
            stockBalanceRow.setAmount(bulkSummaryTO.getAmount());
            list.add(stockBalanceRow);
        }
        } catch (Exception npe){
            writeLog("StockBalanceActivity : parse()  msg: Check DB",npe );
        }

        return list;
    }

    /**
     * on click of option menu btn
     */
    public void headerButtonClicked(View view) {
        if(view.getId() == R.id.option_btn)
            openOptionsMenu();
    }
}
