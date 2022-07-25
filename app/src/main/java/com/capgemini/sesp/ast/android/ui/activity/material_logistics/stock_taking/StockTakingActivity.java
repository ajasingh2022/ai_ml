package com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_taking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuiDate;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.ast_sep.common.to.ast.table.StocktakingTO;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class StockTakingActivity extends AppCompatActivity {

    private static boolean isActiveStockFetched = false;
    private StocktakingTO stocktakingTO;
    public static final String ID_STOCKTAKING = "ID_STOCKTAKING";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_taking);

/*
        View v = findViewById(R.id.title_bar);
        v.setVisibility(View.GONE);*/

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
        ImageButton help_btn = vw.findViewById(R.id.menu_help);
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new HelpDialog(StockTakingActivity.this, ConstantsAstSep.HelpDocumentConstant.PAGE_STOCK_TAKING);
                dialog.show();
            }
        });

        // -- Customizing the action bar ends -----


        getSupportActionBar().setCustomView(vw);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(R.string.title_activity_stock_taking);
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        stocktakingTO = (StocktakingTO) getIntent().getExtras().get("StocktakingTO");
        if (stocktakingTO != null) {
            EditText activeStockTakingEditText = findViewById(R.id.activityStockTakingActiveStockTakingEditText);
            /*activeStockTakingEditText.setType(RspEditText.Type.OUTPUT);*/
            activeStockTakingEditText.setText(stocktakingTO.getName());

            EditText startTimestampEditText = findViewById(R.id.activityStockTakingStartTimestampEditText);
            /*startTimestampEditText.setType(RspEditText.Type.OUTPUT);*/
            startTimestampEditText.setText(GuiDate.toGuiDateUsersTimesZone(stocktakingTO.getStartTimestamp(), true).toString());

            EditText endTimestampEditText = findViewById(R.id.activityStockTakingEndTimestampEditText);
            /*endTimestampEditText.setType(RspEditText.Type.OUTPUT);*/
            endTimestampEditText.setFocusable(false);
            endTimestampEditText.setText(GuiDate.toGuiDateUsersTimesZone(stocktakingTO.getEndTimestamp(), true).toString());

            EditText informationEditText = findViewById(R.id.activityStockTakingInfoEditText);
            /*informationEditText.setType(RspEditText.Type.OUTPUT);*/
            informationEditText.setFocusable(false);
            informationEditText.setText(stocktakingTO.getInfo());
            informationEditText.setHint("");
        }
        } catch (Exception e) {
            writeLog("StockTakingActivity  : onResume() ", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_stock_taking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_reload) {
            new StockTakingFetchActiveThread(this).start();
        }

        return super.onOptionsItemSelected(item);
    }

    public void nextButtonClicked(View view) {
        try{
        Intent goToIntent = new Intent(getApplicationContext(), StockTakingListActivity.class);
        goToIntent.putExtra(ID_STOCKTAKING,stocktakingTO.getId());
        startActivity(goToIntent);
        } catch (Exception e) {
            writeLog("StockTakingActivity  : nextButtonClicked() ", e);
        }
    }

    public static boolean isActiveStockFetched() {
        return isActiveStockFetched;
    }

    public static void setActiveStockFetched(boolean isActiveStockFetched) {
        StockTakingActivity.isActiveStockFetched = isActiveStockFetched;
    }


    /**
     * on click of option menu btn
     */
    public void headerButtonClicked(View view) {
        if(view.getId() == R.id.option_btn)
            openOptionsMenu();
    }
}
