package com.capgemini.sesp.ast.android.ui.activity.material_logistics.common;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.others.AddrRemoveUnitInterface;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.device_info.DeviceInfoFetchThread;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_taking.StockTakingListActivity;
import com.capgemini.sesp.ast.android.ui.activity.receiver.DialogFactory;
import com.capgemini.sesp.ast.android.ui.adapter.UnitAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skvader.rsp.ast_sep.common.to.ast.table.StockTO;
import com.skvader.rsp.ast_sep.common.to.custom.AddToStockTO;
import com.skvader.rsp.ast_sep.common.to.custom.AddToStocktakingTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitIdentifierTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public abstract class CommonListActivity extends AppCompatActivity implements AddrRemoveUnitInterface,View.OnClickListener {
    protected List<UnitItem> displayItems;
    protected List<UnitItem> removeItems;
    protected UnitAdapter<?> adapter;
    protected Dialog deleteAllDialog;
    protected ListView listView = null;

    protected int helpTitle;
    protected String helpText;
    public static final String HIDE_LOCATION = "hide_location";
    protected Class activityClass;
    protected MenuItem activityAddRemoveListSaveButton;
    private String TAG = CommonListActivity.class.getSimpleName();

    private boolean fabExpanded = false;
    private boolean clickHandled = false;
    private FloatingActionButton fabSettings;
    private LinearLayout layoutFab1;
    private LinearLayout layoutFab2;
    private LinearLayout layoutFab3;
    private LinearLayout layoutFab4;
    private LinearLayout layoutFab5;
    protected int selectedId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_goods_reception, menu);
        activityAddRemoveListSaveButton=menu.findItem(R.id.activityAddRemoveListSaveButton);
        activityAddRemoveListSaveButton.setVisible(false);
        return true;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_list);

        fabSettings = (FloatingActionButton) this.findViewById(R.id.fabSetting);
        layoutFab1 = (LinearLayout) this.findViewById(R.id.layoutFab1);
        layoutFab2 = (LinearLayout) this.findViewById(R.id.layoutFab2);
        layoutFab3 = (LinearLayout) this.findViewById(R.id.layoutFab3);
        layoutFab4 = (LinearLayout) this.findViewById(R.id.layoutFab4);
        layoutFab5 = (LinearLayout) this.findViewById(R.id.layoutFab5);

        //Only main FAB is visible in the beginning
        closeFabSubMenus();

        fabSettings.setOnClickListener(this);
        layoutFab1.setOnClickListener(this);
        layoutFab2.setOnClickListener(this);
        layoutFab3.setOnClickListener(this);
        layoutFab4.setOnClickListener(this);
        layoutFab5.setOnClickListener(this);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (fabExpanded == true) {
                closeFabSubMenus();
                clickHandled = true;
            } else
                clickHandled = false;
        }
        return true;
    }

    //closes FAB submenus
    protected void closeFabSubMenus(){
        layoutFab1.setVisibility(View.INVISIBLE);
        layoutFab2.setVisibility(View.INVISIBLE);
        layoutFab3.setVisibility(View.INVISIBLE);
        layoutFab4.setVisibility(View.INVISIBLE);
        layoutFab5.setVisibility(View.INVISIBLE);
        fabSettings.setImageResource(R.drawable.ic_add_white_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
    protected void openFabSubMenus(){
        layoutFab1.setVisibility(View.VISIBLE);
        layoutFab2.setVisibility(View.VISIBLE);
        layoutFab3.setVisibility(View.VISIBLE);
        layoutFab4.setVisibility(View.VISIBLE);
        layoutFab5.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fabSettings.setImageResource(R.drawable.ic_clear_white_24dp);
        fabExpanded = true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        closeFabSubMenus();
        final ActivityFactory activityFactory = ActivityFactory.getInstance();
        int itemId = item.getItemId();
        try {
            if (itemId == R.id.menu_help) {
                final Dialog dialog = new HelpDialog(this, helpTitle);
                dialog.show();
            } else if (itemId == R.id.menu_material_logistics_settings) {
                Intent goToIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ConstantsAstSep.ActivityConstants.MATERIAL_LOGISTICS_SETTINGS));
                goToIntent.putExtra(HIDE_LOCATION, true);
                startActivity(goToIntent);
            }
            else if(itemId == R.id.activityAddRemoveListSaveButton)
            {
                saveButtonClicked();
            }
        } catch (Exception excep) {
            writeLog("CommonListActivity : onOptionsItemSelected() ", excep);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        final DialogFactory dialogFactory = DialogFactory.getInstance();
        try {
            selectedId=view.getId();
            if(selectedId == R.id.fabSetting)
            {
                if(!clickHandled)
                {
                    if (fabExpanded == true ){
                        closeFabSubMenus();
                    } else if(!fabExpanded) {
                        openFabSubMenus();
                    }
                }
            }
            else if(selectedId == R.id.layoutFab1) {
                Class<? extends Dialog> dialogClass = dialogFactory.getDialogClass(ConstantsAstSep.DialogKeyConstants.ADD_BULK_DIALOG);
                final Dialog dialog = AndroidUtilsAstSep.newInstance(dialogClass,
                        this,
                        ObjectCache.getIdObject(StockTO.class,
                                SESPPreferenceUtil.getPreference(ConstantsAstSep.SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK)).getIdDomain(),
                        StockTakingListActivity.class.equals(activityClass));
                dialog.show();


            }
            else if(selectedId == R.id.layoutFab2) {
                Class<? extends Dialog> dialogClass = dialogFactory.getDialogClass(ConstantsAstSep.DialogKeyConstants.ADD_SEQUENCE_DIALOG);
                final Dialog dialog = AndroidUtilsAstSep.newInstance(dialogClass, this);
                dialog.show();

            }
            else if(selectedId == R.id.layoutFab3) {
                Class<? extends Dialog> dialogClass = dialogFactory.getDialogClass(ConstantsAstSep.DialogKeyConstants.ADD_BY_ID_DIALOG);
                final Dialog dialog = AndroidUtilsAstSep.newInstance(dialogClass, this);
                dialog.show();
            }
            else if(selectedId == R.id.layoutFab4) {
                if (getItemsSize() > 0) {
                    Class<? extends Dialog> dialogClass = dialogFactory.getDialogClass(ConstantsAstSep.DialogKeyConstants.REMOVE_BY_ID_DIALOG);
                    final Dialog dialog = AndroidUtilsAstSep.newInstance(dialogClass, this);
                    dialog.show();
                } else {
                    Toast.makeText(this, getString(R.string.error_list_empty), Toast.LENGTH_SHORT).show();
                }
            }
            else if(selectedId == R.id.layoutFab5) {
                if (getItemsSize() > 0) {
                    showDeleteAllItemsDialog();
                } else {
                    Toast.makeText(this, getString(R.string.error_list_empty), Toast.LENGTH_SHORT).show();
                }
            }



        } catch (Exception excep) {
            writeLog("CommonListActivity : onClick() ", excep);
        }
    }

    //@Override
    public void onCommonListItemClick(ListView l, View v, int position, long id) {
        closeFabSubMenus();
        String identifier = displayItems.get(position).getId();
        Long idUnitT = displayItems.get(position).getIdUnitT();
        UnitItem unitItem1 = null;
        try {
            if (displayItems.get(position).getType().equals(UnitItem.Type.NORMAL)) {
                for (UnitItem unitItem : displayItems) {
                    if (Utils.isNotEmpty(unitItem.getIdUnitT())) {
                        if (unitItem.getId().equals(identifier) && unitItem.getIdUnitT().equals(idUnitT)) {
                            unitItem1 = unitItem;
                        }
                    } else {
                        if (unitItem.getId() != null && unitItem.getId().equals(identifier)) {
                            unitItem1 = unitItem;
                        }
                    }

                }
            } else if (displayItems.get(position).getType().equals(UnitItem.Type.BULK)) {
                identifier = null;
            }
            if (identifier != null) {
                DeviceInfoFetchThread deviceInfoFetchThread = new DeviceInfoFetchThread(CommonListActivity.this, identifier, unitItem1);
                deviceInfoFetchThread.start();
            }
        } catch (Exception e) {
            writeLog(TAG + " + : onCommonListItemClick()", e);
        }
    }

    protected void showDeleteAllItemsDialog() {
        Builder deleteAllBuilder = new Builder(CommonListActivity.this);
        deleteAllBuilder.setTitle(R.string.delete_all);
        deleteAllBuilder.setMessage(R.string.delete_all_question);
        deleteAllBuilder.setIcon(android.R.drawable.ic_menu_delete);
        deleteAllBuilder.setCancelable(true);
        deleteAllBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllItems();
            }
        });
        deleteAllBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllDialog.dismiss();
            }
        });
        deleteAllBuilder.create();
        deleteAllDialog = deleteAllBuilder.show();
    }

    protected List<UnitIdentifierTO> createItems(List<UnitItem> items, boolean ignoreSaved) {
        List<UnitIdentifierTO> unitIdentifierTOs = new ArrayList<UnitIdentifierTO>();
        for (UnitItem item : items) {
            if (ignoreSaved || !item.isSaved()) {
                switch (item.getType()) {
                    case NORMAL: {
                        UnitIdentifierTO unitIdentifierTO = new UnitIdentifierTO();
                        unitIdentifierTO.setIdUnitIdentifierT(item.getIdUnitIdentifierT());
                        unitIdentifierTO.setType(UnitIdentifierTO.TYPE_NORMAL);
                        if (item.getIdUnitIdentifierT().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__GIAI)) {
                            unitIdentifierTO.setGiai(item.getId());
                        } else if (item.getIdUnitIdentifierT().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO)) {
                            unitIdentifierTO.setSerialNumber(item.getId());
                        } else if (item.getIdUnitIdentifierT().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO)) {
                            unitIdentifierTO.setPropertyNumber(item.getId());
                        }
                        unitIdentifierTO.setIdUnitT(Utils.isNotEmpty(item.getIdUnitT()) ? item.getIdUnitT() : null);
                        unitIdentifierTO.setIdUnitModel(Utils.isNotEmpty(item.getIdUnitModelT()) ? item.getIdUnitModelT() : null);
                        unitIdentifierTOs.add(unitIdentifierTO);
                    }
                    break;
                    case SEQUENCE: {
                        for (String value : AddSequenceDialog.getUnitsInSequence(item.getIdFrom(), item.getIdTo())) {
                            UnitIdentifierTO unitIdentifierTO = new UnitIdentifierTO();
                            unitIdentifierTO.setIdUnitIdentifierT(item.getIdUnitIdentifierT());
                            unitIdentifierTO.setType(UnitIdentifierTO.TYPE_SEQUENCE);
                            if (item.getIdUnitIdentifierT().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__GIAI)) {
                                unitIdentifierTO.setGiai(value);
                            } else if (item.getIdUnitIdentifierT().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO)) {
                                unitIdentifierTO.setSerialNumber(value);
                            } else if (item.getIdUnitIdentifierT().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO)) {
                                unitIdentifierTO.setPropertyNumber(value);
                            }
                            unitIdentifierTOs.add(unitIdentifierTO);
                        }
                    }
                    break;
                    case BULK: {
                        UnitIdentifierTO unitIdentifierTO = new UnitIdentifierTO();
                        unitIdentifierTO.setIdUnitModel(item.getObj().getId());
                        unitIdentifierTO.setType(UnitIdentifierTO.TYPE_BULK);
                        unitIdentifierTO.setQuantity(item.getAmount());
                        unitIdentifierTOs.add(unitIdentifierTO);
                    }
                    break;
                }
            }
        }
        return unitIdentifierTOs;
    }

    public boolean hasItem(final UnitItem unitItem) {
        if (displayItems.contains(unitItem)) {
            for (UnitItem unitItem1 : displayItems) {
                if (Utils.isNotEmpty(unitItem1.getIdUnitT())) {
                    if (Utils.isNotEmpty(unitItem.getIdUnitT()) && unitItem1.getIdUnitT().equals(unitItem.getIdUnitT())) {
                        return true;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean checkExistingSequenceUnit(String idFrom, String idTo) {

        List<String> newUnitList = new ArrayList<>();
        List<String> addedUnitList = new ArrayList<>();
        try {
            newUnitList.addAll(AddSequenceDialog.getUnitsInSequence(idFrom, idTo));

            for (UnitItem item : displayItems) {
                if (item.getIdFrom() != null && item.getIdTo() != null) {
                    addedUnitList.addAll(AddSequenceDialog.getUnitsInSequence(item.getIdFrom(), item.getIdTo()));
                } else {
                    addedUnitList.add(item.getId());
                }
            }
            for (String unit : newUnitList) {
                if (addedUnitList.contains(unit)) {
                    return true;
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " + : checkExistingSequenceUnit()", e);
        }
        return false;
    }

    public int getSelectedItemCount(final UnitItem unitItem) {
        int i = 0;
        try{
        for (UnitItem item : displayItems) {
            if (Utils.isNotEmpty(item.getId()) && Utils.isNotEmpty(unitItem.getId()) && item.getId().equals(unitItem.getId())) {
                i++;
            }
        }
        } catch (Exception e) {
            writeLog(TAG + " : getSelectedItemCount()", e);
        }
        return i;
    }

    public UnitItem checkExistsUnitItem(final UnitItem unitItem) {
        UnitItem returnItem = null;
        List<UnitItem> unitItems = new ArrayList<UnitItem>();
        try{
        for (UnitItem item : displayItems) {
            if (item.getId() != null && item.getId().equals(unitItem.getId())) {
                unitItems.add(item);
            }
        }

        for (UnitItem unitItem1 : unitItems) {
            if (Utils.isNotEmpty(unitItem1.getIdUnitT())) {
                if (unitItem1.getIdUnitT().equals(unitItem.getIdUnitT())) {
                    returnItem = unitItem1;
                } else if (Utils.isEmpty(unitItem.getIdUnitT())) {
                    returnItem = unitItem;
                }
            } else if (Utils.isEmpty(unitItem1.getIdUnitT()) && Utils.isNotEmpty(unitItem.getIdUnitT())) {
                returnItem = null;
            } else {
                returnItem = unitItem;
            }
        }
        } catch (Exception e) {
            writeLog(TAG + "  : checkExistsUnitItem()", e);
        }
        return returnItem;
    }


    public void addItem(final UnitItem newItem) {
        try{
        final UnitItem selectedUnitItem = checkforDuplicateAddedItems(newItem);
        if (null == selectedUnitItem) {
            displayItems.add(newItem);
            adapter.notifyDataSetChanged();
        } else {
            if (newItem.getType().equals(UnitItem.Type.BULK)) {
                Builder questionBuilder = GuiController.showConfirmCancelDialog(this, getString(R.string.error_bulk_unit_already_added), getString(R.string.bulk_unit_already_added_overwrite_question));
                questionBuilder.setPositiveButton(R.string.overwrite,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                findAndDeleteItem(selectedUnitItem); // Will delete the old one which is equal to the new one (except from amount)
                                displayItems.add(newItem);
                                adapter.notifyDataSetChanged();

                                dialog.dismiss(); // Method deprecated, and should not use -1
                            }
                        });
                questionBuilder.setNeutralButton(R.string.add_up,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UnitItem oldItem = findAndDeleteItem(selectedUnitItem); // Will delete the old one which is equal to the new one (except from amount)
                                newItem.setAmount(newItem.getAmount() + oldItem.getAmount());
                                displayItems.add(newItem);
                                adapter.notifyDataSetChanged();

                                dialog.dismiss(); // Method deprecated, and should not use -1
                            }
                        });
                questionBuilder.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Method deprecated, and should not use -1
                            }
                        });
                questionBuilder.show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_device_already_in_list), Toast.LENGTH_SHORT).show();
            }
        }
        activityAddRemoveListSaveButton.setVisible(true);
        } catch (Exception e) {
            writeLog(TAG + " : addItem()", e);
        }
    }

    private UnitItem checkforDuplicateAddedItems(final UnitItem newItem) {
        UnitItem alreadyExists = null;
        List<String> addedSequenceUnits = new ArrayList<>();
        try{
        if (null != newItem && null != newItem.getId()) {
            for (UnitItem unitItem : displayItems) {
                if (unitItem.getType() != null && unitItem.getType().equals(UnitItem.Type.SEQUENCE)) {
                    addedSequenceUnits = AddSequenceDialog.getUnitsInSequence(unitItem.getIdFrom(), unitItem.getIdTo());
                }
                if (null != unitItem && null != unitItem.getId() && unitItem.getId().equals(newItem.getId())) {
                    if (newItem.getType().equals(UnitItem.Type.NORMAL) && Utils.isNotEmpty(unitItem.getIdUnitT()) && unitItem.getIdUnitT().equals(newItem.getIdUnitT())) {
                        alreadyExists = unitItem;
                        break;
                    }
                }
            }
            if (addedSequenceUnits.contains(newItem.getId())) {
                alreadyExists = newItem;
            }
        }
        } catch (Exception e) {
            writeLog(TAG + " : checkforDuplicateAddedItems()", e);
        }
        return alreadyExists;
    }

    protected UnitItem findAndDeleteItem(UnitItem newItem) {
        UnitItem deletedItem = null;
        try{
        for (UnitItem item : displayItems) {
            if (item.equals(newItem)) {
                deletedItem = item;
                displayItems.remove(item);
                break;
            }
        }
        } catch (Exception e) {
            writeLog(TAG + " : findAndDeleteItem()", e);
        }
        return deletedItem;
    }

    public void removeItem(int position) {
        UnitItem unitItem = displayItems.get(position);
        if (unitItem.isSaved()) {
            removeItems.add(unitItem);
        }
        displayItems.remove(position);
        adapter.notifyDataSetChanged();
        if (getItemsSize() == 0)
            activityAddRemoveListSaveButton.setVisible(false);
    }

    protected void deleteAllItems() {
        for (UnitItem item : displayItems) {
            if (item.isSaved()) {
                removeItems.add(item);
            }
        }
        displayItems.clear();
        adapter.notifyDataSetChanged();
        if (getItemsSize() == 0)
            activityAddRemoveListSaveButton.setVisible(false);
    }

    public boolean removeItem(String itemToRemove) {
        int i = 0;
        try{
        for (UnitItem unitItem : displayItems) {
            if (!unitItem.getType().equals(UnitItem.Type.BULK) && // Cannot remove bulk units by deleting by ID
                    unitItem.getIdUnitIdentifierT().equals(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER)) && unitItem.getTitle().equals(itemToRemove)) {
                if (unitItem.isSaved()) {
                    removeItems.add(unitItem);
                }
                displayItems.remove(i);
                adapter.notifyDataSetChanged();
                return true;
            }
            i++;
        }
        if (getItemsSize() == 0)
            activityAddRemoveListSaveButton.setVisible(false);
        Toast.makeText(getApplicationContext(), getString(R.string.error_identifier_not_found_in_list), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            writeLog(TAG + " : removeItem()", e);
        }
        return false;
    }

    public int getItemsSize() {
        return displayItems.size();
    }

    /**
     * @param displayItems Values to be returned
     * @param fromIdStock
     * @param toIdStock
     * @return Values in parameter items as AddToStockTO
     */
    public List<AddToStockTO> getAddToStockValues(List<UnitItem> displayItems, Long fromIdStock, Long toIdStock) {
        List<AddToStockTO> addToStockTOs = new ArrayList<AddToStockTO>();
        AddToStockTO addToStockTO;
        try{
        for (UnitItem item : displayItems) {
            addToStockTO = new AddToStockTO();
            addToStockTO.setFromIdStock(fromIdStock);
            addToStockTO.setToIdStock(toIdStock);
            addToStockTO.setUnitId(item.getId());

            if (item.getType().equals(UnitItem.Type.NORMAL)) {
                addToStockTO.setQuantity(1L);
                addToStockTO.setIdUnitIdentifierT(item.getIdUnitIdentifierT());
                addToStockTO.setIdUnitT(Utils.isNotEmpty(item.getIdUnitT()) ? item.getIdUnitT() : null);
                addToStockTO.setIdUnitModelT(Utils.isNotEmpty(item.getIdUnitModelT()) ? item.getIdUnitModelT() : null);
            } else if (item.getType().equals(UnitItem.Type.SEQUENCE)) {

                List<String> ids = AddSequenceDialog.getUnitsInSequence(item.getIdFrom(), item.getIdTo());

                for (String id : ids) {
                    addToStockTO = new AddToStockTO();
                    addToStockTO.setFromIdStock(fromIdStock);
                    addToStockTO.setToIdStock(toIdStock);
                    addToStockTO.setUnitId(id);
                    addToStockTO.setQuantity(1L);
                    addToStockTO.setIdUnitIdentifierT(item.getIdUnitIdentifierT());
                    addToStockTOs.add(addToStockTO);
                }
                addToStockTO = null;
            } else { // BULK
                addToStockTO.setUnitId(null);
                addToStockTO.setIdUnitModel(item.getObj().getId());
                addToStockTO.setQuantity(item.getAmount());
            }

            if (addToStockTO != null)
                addToStockTOs.add(addToStockTO);
        }
        } catch (Exception e) {
            writeLog(TAG + " : getAddToStockValues()", e);
        }
        return addToStockTOs;
    }

    /**
     * @param displayItems Values to be returned
     * @return Values in parameter items as AddToStockTO
     */
    public List<AddToStocktakingTO> getAddToStocktakingValues(List<UnitItem> displayItems) {
        List<AddToStocktakingTO> addToStocktakingTOs = new ArrayList<AddToStocktakingTO>();
        AddToStocktakingTO addToStocktakingTO;
        try{
        for (UnitItem item : displayItems) {
            addToStocktakingTO = new AddToStocktakingTO();
            addToStocktakingTO.setUnitId(item.getId());

            if (item.getType().equals(UnitItem.Type.NORMAL)) {
                addToStocktakingTO.setQuantity(1L);
                addToStocktakingTO.setIdUnitIdentifierT(item.getIdUnitIdentifierT());
                addToStocktakingTO.setIdUnitT(item.getIdUnitT());
                addToStocktakingTO.setIdUnitModel(item.getIdUnitModelT());
            } else if (item.getType().equals(UnitItem.Type.SEQUENCE)) {

                List<String> ids = AddSequenceDialog.getUnitsInSequence(item.getIdFrom(), item.getIdTo());

                for (String id : ids) {
                    addToStocktakingTO = new AddToStocktakingTO();
                    addToStocktakingTO.setUnitId(id);
                    addToStocktakingTO.setQuantity(1L);
                    addToStocktakingTO.setIdUnitIdentifierT(item.getIdUnitIdentifierT());
                    addToStocktakingTOs.add(addToStocktakingTO);
                }
                addToStocktakingTO = null;
            } else { // BULK
                addToStocktakingTO.setIdUnitModel(item.getObj().getId());
                addToStocktakingTO.setQuantity(item.getAmount());
                addToStocktakingTO.setUnitId(null);
                addToStocktakingTO.setOverwriteExistingPost(item.isOverwriteExisting());
            }

            if (addToStocktakingTO != null)
                addToStocktakingTOs.add(addToStocktakingTO);
        }
        } catch (Exception e) {
            writeLog(TAG + " : getAddToStocktakingValues()", e);
        }
        return addToStocktakingTOs;
    }

    public abstract void saveButtonClicked();
}