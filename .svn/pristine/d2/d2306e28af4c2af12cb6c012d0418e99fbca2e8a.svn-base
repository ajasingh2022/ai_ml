package com.capgemini.sesp.ast.android.ui.activity.material_logistics.goods_reception;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.CommonListActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.UnitItem;
import com.capgemini.sesp.ast.android.ui.adapter.UnitAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.ast_sep.common.to.custom.AddToStockTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class GoodsReceptionActivity extends CommonListActivity {

    public static final String TO_ID_STOCK = "toIdStock";
    public static final String FROM_ID_STOCK = "fromIdStock";
    private Long toIdStock = null;
    private Long fromIdStock = null;
    private enum MODE {CAR, GOODS}

    private MODE mode;


    private Button receive;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     try{
       /* ImageButton optionButton = findViewById(R.id.option_btn);
        optionButton.setVisibility(View.VISIBLE);*/
        listView = findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onCommonListItemClick(listView, view, position, id);


            }
        });
        displayItems = new ArrayList<UnitItem>();
        adapter = new UnitAdapter<GoodsReceptionActivity>(GoodsReceptionActivity.this, displayItems, R.layout.activity_add_remove_list_count_row);
        listView.setAdapter(adapter);

       /* TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(R.string.title_activity_goods_reception);*/

} catch (Exception e) {
    writeLog( "GoodsReceptionActivity : onCreate()" ,e);
}

     }

    @Override
    public void onResume() {
        super.onResume();
        toIdStock = getIntent().getLongExtra(GoodsReceptionActivity.TO_ID_STOCK, -1L);
        toIdStock = (toIdStock.equals(-1L)) ? null : toIdStock;
        fromIdStock = getIntent().getLongExtra(GoodsReceptionActivity.FROM_ID_STOCK, -1L);
        fromIdStock = (fromIdStock.equals(-1L)) ? null : fromIdStock;
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);
        final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
        ImageButton help_btn = vw.findViewById(R.id.menu_help);
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new HelpDialog(GoodsReceptionActivity.this, ConstantsAstSep.HelpDocumentConstant.PAGE_GOODS_RECEPTION);
                dialog.show();
            }
        });

        TextView txtTitleBar = vw.findViewById(R.id.title_text);
        if (fromIdStock == null) {
            // Goods Reception
            txtTitleBar.setText(R.string.title_activity_goods_reception);
            getSupportActionBar().setTitle(R.string.title_activity_goods_reception);
            mode = MODE.GOODS;
        } else {
            //Car In/Out
            txtTitleBar.setText(R.string.title_car_in_out);
            getSupportActionBar().setTitle(R.string.title_car_in_out);
            help_btn.setVisibility(View.GONE);
            mode = MODE.CAR;


        }
        getSupportActionBar().setCustomView(vw);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        //System.out.println("RESUME: " + mode);
    }


    @Override
    public boolean removeItem(UnitItem itemToRemove) {
        final UnitItem selectedUnitItem = checkforDuplicateAddedItems(itemToRemove);
        int i = 0;
        if (hasItem(itemToRemove) && Utils.isEmpty(itemToRemove.getIdUnitT()) && getSelectedItemCount(itemToRemove) > 1) {
            Toast.makeText(getApplicationContext(), getString(R.string.multiple_device_in_list), Toast.LENGTH_SHORT).show();
            return false;
        }
        for (UnitItem unitItem : displayItems) {
            if (!unitItem.getType().equals(UnitItem.Type.BULK) && // Cannot remove bulk units by deleting by ID
                    unitItem.getIdUnitIdentifierT().equals(SESPPreferenceUtil.getPreference(ConstantsAstSep.SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER)) && unitItem.getTitle().equals(itemToRemove.getTitle())) {

                if (Utils.isEmpty(selectedUnitItem)) {
                    if (unitItem.isSaved()) {
                        removeItems.add(unitItem);
                    }
                    displayItems.remove(i);
                    activityAddRemoveListSaveButton.setVisible(getItemsSize()==0?false:true);
                    adapter.notifyDataSetChanged();
                    return true;
                } else {

                    if (Utils.isNotEmpty(itemToRemove.getIdUnitT()) && Utils.isNotEmpty(unitItem.getIdUnitT()) && unitItem.getIdUnitT().equals(itemToRemove.getIdUnitT())) {
                        if (unitItem.isSaved()) {
                            removeItems.add(unitItem);
                        }
                        displayItems.remove(i);
                        activityAddRemoveListSaveButton.setVisible(getItemsSize()==0?false:true);
                        adapter.notifyDataSetChanged();
                        return true;

                    }
                }
            }
            i++;
        }
        if (getItemsSize() == 0)
            receive.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), getString(R.string.error_identifier_not_found_in_list), Toast.LENGTH_SHORT).show();
        return false;
    }

    private UnitItem checkforDuplicateAddedItems(final UnitItem newItem) {
        UnitItem alreadyExists = null;
        if (null != newItem && null != newItem.getId()) {
            for (UnitItem unitItem : displayItems) {
                if (null != unitItem && null != unitItem.getId() && unitItem.getId().equals(newItem.getId())) {
                    if (Utils.isNotEmpty(unitItem.getIdUnitT()) && unitItem.getIdUnitT().equals(newItem.getIdUnitT())) {
                        alreadyExists = unitItem;
                        break;
                    }
                }
            }
        }
        return alreadyExists;
    }

    public void saveButtonClicked() {
        new GuiWorker<Void>(this) {
            @Override
            protected Void runInBackground() throws Exception {
                List<AddToStockTO> addToStockTOs = getAddToStockValues(displayItems, fromIdStock, toIdStock);

                if (mode.equals(MODE.GOODS)) {
                    if (selectedId == R.id.layoutFab1) {
                        AndroidUtilsAstSep.getDelegate().addToStock(addToStockTOs, new Date());
                    } else {
                        AndroidUtilsAstSep.getDelegate().changeUnitStatusAndChangeStock(addToStockTOs, new Date());
                    }
                } else if (mode.equals(MODE.CAR)) {
                    AndroidUtilsAstSep.getDelegate().addToStock(addToStockTOs, new Date());
                }
                return null;
            }

            @Override
            protected void onPostExecute(boolean successful, Void result) {
                if (isSuccessful()) {
                    displayItems.clear();
                    adapter.notifyDataSetChanged();

                    String message = "This screen has a bug";

                    if (mode.equals(MODE.GOODS)) {
                        message = getString(R.string.goods_reception_completed);
                    } else if (mode.equals(MODE.CAR)) {
                        message = getString(R.string.change_stock_completed);
                    }

                    Builder builder = GuiController.showInfoDialog(GoodsReceptionActivity.this, message);
                    builder.show();
                }
            }
        }.start();
    }

    /**
     * on click of option menu btn
     */
    public void headerButtonClicked(View view) {
        if (view.getId() == R.id.option_btn)
            openOptionsMenu();
    }

}
