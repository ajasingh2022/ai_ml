package com.capgemini.sesp.ast.android.ui.activity.material_logistics;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.MaterialLogisticList;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.goods_reception.GoodsReceptionActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_balance.StockBalanceFetchThread;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_taking.StockTakingFetchActiveThread;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.ast_sep.common.to.ast.table.StockTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.UserLoginCustomTO;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class MaterialLogisticsActivity extends AppCompatActivity {

    ListView materialLogisticsList = null;
    UserLoginCustomTO userLoginCustomTO;
    List<StockTO> stockTOs;
    ActivityFactory activityFactory;
    List<MaterialLogisticsItem> itemlist;
    private final int stockType = 10;
    private final int goodsReceptionPos = 4;
    private final static String LOGGED_IN_USER = "LOGGED_IN_USER";
    private final static String TAG = MaterialLogisticsActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_material_logistics);
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
            ImageButton help_btn = vw.findViewById(R.id.menu_help);
            help_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new HelpDialog(MaterialLogisticsActivity.this, ConstantsAstSep.HelpDocumentConstant.MATERIAL_lOGISTICS);
                    dialog.show();
                }
            });

            // -- Customizing the action bar ends -----


            getSupportActionBar().setCustomView(vw);
            getSupportActionBar().setDisplayShowCustomEnabled(true);

            TextView txtTitleBar = findViewById(R.id.title_text);
            txtTitleBar.setText(R.string.title_activity_material_logistics);

        stockTOs = new ArrayList<StockTO>(ObjectCache.getAllIdObjects(StockTO.class));

        if (savedInstanceState != null && savedInstanceState.getSerializable(LOGGED_IN_USER) != null) {
            userLoginCustomTO = (UserLoginCustomTO) savedInstanceState.getSerializable(LOGGED_IN_USER);
        } else {
            userLoginCustomTO = SessionState.getInstance().getCurrentUser();
        }
        activityFactory = ActivityFactory.getInstance();
        if (null != userLoginCustomTO)

            materialLogisticsList = (ListView) findViewById(R.id.materialLogistics);
        materialLogisticsList.setAdapter(new MaterialLogisticsAdapter(this, createItems()));

        materialLogisticsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onLogisticsItemClick(materialLogisticsList, view, position, id);


            }
        });

        updateHeaderSummary();
        } catch (Exception e) {
            writeLog(TAG + " : onCreate() ", e);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (userLoginCustomTO != null) {
            Log.d(TAG, "Saving logged in user in Bundle");
            outState.putSerializable(LOGGED_IN_USER, userLoginCustomTO);
        }
    }

    private List<MaterialLogisticsItem> createItems() {
        int[] itemIconIds = new int[]{
                R.drawable.ic_equalizer_black_24dp,
                R.drawable.ic_move_to_inbox_black_24dp,
                R.drawable.ic_playlist_add_check_24dp,
                R.drawable.ic_local_shipping_black_24dp,
                R.drawable.ic_call_received_black_24dp,
                R.drawable.ic_storage_black_24dp,
                R.drawable.ic_broken_image_black_24dp,
                R.drawable.ic_perm_device_information_black_24dp,
                R.drawable.ic_settings_black_24dp};
        String[] itemNames = getResources().getStringArray(R.array.items_material_logistics_menu);

        List<MaterialLogisticsItem> items = new ArrayList<MaterialLogisticsItem>();
        try {
            for (int i = 0; i < itemNames.length; i++) {
                Integer iconid = null;

                if ((i == MaterialLogisticList.STOCK_BALANCE && userLoginCustomTO.getIdFunctionTs()
                        .contains(AndroidUtilsAstSep.CONSTANTS().FUNCTION_T__PDA_STOCK_BALANCE))
                        || (i == MaterialLogisticList.PALLET_HANDLING && userLoginCustomTO.getIdFunctionTs()
                        .contains(AndroidUtilsAstSep.CONSTANTS().FUNCTION_T__PDA_PALLET_MANAGEMENT))
                        || (i == MaterialLogisticList.MATERIAL_CINTROL && userLoginCustomTO.getIdFunctionTs()
                        .contains(AndroidUtilsAstSep.CONSTANTS().FUNCTION_T__PDA_MATERIEL_CONTROL))
                        || (i == MaterialLogisticList.CAR && userLoginCustomTO.getIdFunctionTs()
                        .contains(AndroidUtilsAstSep.CONSTANTS().FUNCTION_T__PDA_VEHICLE_IN_OUT))
                        || (i == MaterialLogisticList.GOODS_RECEPTION && userLoginCustomTO.getIdFunctionTs()
                        .contains(AndroidUtilsAstSep.CONSTANTS().FUNCTION_T__PDA_GOODS_RECEPTION))
                        || (i == MaterialLogisticList.STOCK_TAKING && userLoginCustomTO.getIdFunctionTs()
                        .contains(AndroidUtilsAstSep.CONSTANTS().FUNCTION_T__PDA_STOCK_TAKING))
                        || (i == MaterialLogisticList.REGISTER_NEW_BROKEN_DEVICE && userLoginCustomTO.getIdFunctionTs()
                        .contains(AndroidUtilsAstSep.CONSTANTS().FUNCTION_T__PDA_NEW_BROKEN_UNIT))
                        || (i == MaterialLogisticList.DEVICE_INFO && userLoginCustomTO.getIdFunctionTs()
                        .contains(AndroidUtilsAstSep.CONSTANTS().FUNCTION_T__PDA_UNIT_INFO))
                        || (i == MaterialLogisticList.MATERIAL_LOGISTICS_SETTINGS)) {

                    if (i < itemIconIds.length) {
                        iconid = itemIconIds[i];
                    }
                    items.add(new MaterialLogisticsItem(itemNames[i], iconid));
                }

            }
        } catch (Exception e) {
            writeLog(TAG + " + createItems()", e);
        }
        return items;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_material_logistics, menu);
        return true;
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        updateHeaderSummary();
        updateListSummary();
    }

    private void updateHeaderSummary() {
        String displayLength = "";
        try {
            Long identifierLength = SESPPreferenceUtil.getIdentifierLengthNullIfOff();
            if (identifierLength != null) {
                displayLength = " (" + getString(R.string.x_characters, identifierLength) + ")";
            }

            TextView identifierTextView = findViewById(R.id.identifier);
            identifierTextView.setText(getText(R.string.identifier) + ": "
                    + ObjectCache.getTypeName(UnitIdentifierTTO.class, SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER))
                    + displayLength);

            TextView locationTextView = findViewById(R.id.location);
            locationTextView.setText(getText(R.string.location) + ": "
                    + ObjectCache.getIdObject(StockTO.class, SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK)).getName());

        } catch (Exception e) {
            writeLog(TAG + " + updateHeaderSummary()", e);
        }

    }

    //	@Override
    public void onLogisticsItemClick(ListView l, View v, int position, long id) {
        try {
            String listItemName = (String) v.getTag();
            if (listItemName != null) {
                String[] itemNames = getResources().getStringArray(R.array.items_material_logistics_menu);
                if (listItemName.equalsIgnoreCase(itemNames[MaterialLogisticList.STOCK_BALANCE])) {
                    Log.d(TAG, " Fetching Stock status");
                    new StockBalanceFetchThread(this).start();
                } else if (listItemName.equalsIgnoreCase(itemNames[MaterialLogisticList.PALLET_HANDLING])) {
                    Intent goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ActivityConstants.SELECT_PALLET_ACTIVITY));
                    startActivity(goToIntent);
                } else if (listItemName.equalsIgnoreCase(itemNames[MaterialLogisticList.MATERIAL_CINTROL])) {
                    Intent goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ActivityConstants.MATERIAL_CONTROL_INPUT_ACTIVITY));
                    startActivity(goToIntent);
                } else if (listItemName.equalsIgnoreCase(itemNames[MaterialLogisticList.CAR])) {
                    Intent goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ActivityConstants.CAR_INPUT_ACTIVITY));
                    startActivity(goToIntent);
                } else if (listItemName.equalsIgnoreCase(itemNames[MaterialLogisticList.GOODS_RECEPTION])) {
                    Intent goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ActivityConstants.GOODS_RECEPTION_ACTIVITY));
                    goToIntent.putExtra(GoodsReceptionActivity.TO_ID_STOCK, MaterialLogisticsSettingsActivity.getIdStock());
                    startActivity(goToIntent);
                } else if (listItemName.equalsIgnoreCase(itemNames[MaterialLogisticList.STOCK_TAKING])) {
                    new StockTakingFetchActiveThread(this).start();
                } else if (listItemName.equalsIgnoreCase(itemNames[MaterialLogisticList.REGISTER_NEW_BROKEN_DEVICE])) {
                    Intent goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ActivityConstants.REGISTER_NEW_BROKEN_DEVICE_ACTIVITY));
                    startActivity(goToIntent);
                } else if (listItemName.equalsIgnoreCase(itemNames[MaterialLogisticList.DEVICE_INFO])) {
                    Intent goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ActivityConstants.DEVICE_INFO_INPUT_ACTIVITY));
                    startActivity(goToIntent);
                } else if (listItemName.equalsIgnoreCase(itemNames[MaterialLogisticList.MATERIAL_LOGISTICS_SETTINGS])) {
                    Intent goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ActivityConstants.MATERIAL_LOGISTICS_SETTINGS));
                    startActivity(goToIntent);
                } else {
                    String[] menuItems = getResources().getStringArray(R.array.items_material_logistics_menu);
                    Toast.makeText(getApplicationContext(), menuItems[position] + " " + getString(R.string.error_not_implemented), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " + onLogisticsItemClick()", e);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ActivityConstants.MAIN_MENU_ACTIVITY));
            goToIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goToIntent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent goToIntent = null;
        try {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_material_logistics_settings) {
                goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ActivityConstants.MATERIAL_LOGISTICS_SETTINGS));
            }

            if (goToIntent != null) {
                startActivity(goToIntent);
                return true;
            }
        } catch (Exception e) {
            writeLog(TAG + " + onOptionsItemSelected()", e);
        }
        return super.onOptionsItemSelected(item);
    }
   
    /**
     * on click of option menu btn
     */
    public void headerButtonClicked(View view) {
        if (view.getId() == R.id.option_btn)
            openOptionsMenu();
    }

    private void updateListSummary() {
        try {
            StockTO tempStockTo = ObjectCache.getIdObject(StockTO.class, SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK));
            itemlist = createItems();
            if (tempStockTo.getIdStockT() == stockType) {
                itemlist.remove(goodsReceptionPos);
                materialLogisticsList.setAdapter(new MaterialLogisticsAdapter(this, itemlist));
            } else {
                materialLogisticsList.setAdapter(new MaterialLogisticsAdapter(this, itemlist));
            }
        } catch (Exception e) {
            writeLog(TAG + " + updateListSummary()", e);
        }
    }

}
