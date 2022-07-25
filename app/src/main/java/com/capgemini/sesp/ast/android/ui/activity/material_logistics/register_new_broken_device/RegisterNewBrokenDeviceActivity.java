package com.capgemini.sesp.ast.android.ui.activity.material_logistics.register_new_broken_device;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.UnitItem;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.device_info.DeviceInfoFetchThread;
import com.capgemini.sesp.ast.android.ui.activity.receiver.DialogFactory;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class RegisterNewBrokenDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    public ListView brokenDeviceList = null;
    private static List<BrokenNewDeviceItem> items;
    private static BrokenNewDeviceAdapter adapter;
    private Dialog deleteAllDialog;
    public static final String HIDE_LOCATION = "hide_location";
    static MenuItem activityAddRemoveListSaveButton;
    static String TAG = RegisterNewBrokenDeviceActivity.class.getSimpleName();
    private boolean clickHandled = false;
    private boolean fabExpanded = false;
    private FloatingActionButton fabSettings;
    private LinearLayout layoutFab1;
    private LinearLayout layoutFab2;
    private LinearLayout layoutFab3;
    private LinearLayout layoutFab4;
    private LinearLayout layoutFab5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_add_remove_list);

        //View v = findViewById(R.id.title_bar);
       // v.setVisibility(View.GONE);

      //  getSupportActionBar().setTitle(R.string.title_activity_register_new_broken_device);
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
                    final Dialog dialog = new HelpDialog(RegisterNewBrokenDeviceActivity.this,ConstantsAstSep.HelpDocumentConstant.PAGE_REGISTER_NEW_BROKEN_DEVICE);
                    dialog.show();
                }
            });
            getSupportActionBar().setCustomView(vw);
            getSupportActionBar().setDisplayShowCustomEnabled(true);


            TextView txtTitleBar = findViewById(R.id.title_text);
            txtTitleBar.setText(R.string.title_activity_register_new_broken_device);
            // -- Customizing the action bar ends -----


            fabSettings = (FloatingActionButton) this.findViewById(R.id.fabSetting);

        layoutFab1 = (LinearLayout) this.findViewById(R.id.layoutFab1);
        layoutFab2 = (LinearLayout) this.findViewById(R.id.layoutFab2);
        layoutFab3 = (LinearLayout) this.findViewById(R.id.layoutFab3);
        layoutFab4 = (LinearLayout) this.findViewById(R.id.layoutFab4);
        layoutFab5 = (LinearLayout) this.findViewById(R.id.layoutFab5);

        layoutFab1.setVisibility(View.GONE);
        layoutFab2.setVisibility(View.GONE);

        layoutFab3.setOnClickListener(this);
        layoutFab4.setOnClickListener(this);
        layoutFab5.setOnClickListener(this);
        //Only main FAB is visible in the beginning
        closeFabSubMenus();
        fabSettings.setOnClickListener(this);

        brokenDeviceList = findViewById(android.R.id.list);
        items = new ArrayList<BrokenNewDeviceItem>();
        adapter = new BrokenNewDeviceAdapter(this, items, R.layout.activity_add_remove_list_row);

        brokenDeviceList.setAdapter(adapter);
        brokenDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onDeviceListItemClick(brokenDeviceList, view, position, id);


            }
        });
        } catch (Exception e) {
            writeLog( TAG + " : onCreate()", e);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (fabExpanded == true) {
                closeFabSubMenus();
                clickHandled = true;
            } else
                clickHandled = false;
        }
        return true;
    }

    //closes FAB submenus
    protected void closeFabSubMenus() {
        layoutFab3.setVisibility(View.INVISIBLE);
        layoutFab4.setVisibility(View.INVISIBLE);
        layoutFab5.setVisibility(View.INVISIBLE);
        fabSettings.setImageResource(R.drawable.ic_add_white_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
    protected void openFabSubMenus() {
        layoutFab3.setVisibility(View.VISIBLE);
        layoutFab4.setVisibility(View.VISIBLE);
        layoutFab5.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fabSettings.setImageResource(R.drawable.ic_clear_white_24dp);
        fabExpanded = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_register_new_broken_device, menu);
        activityAddRemoveListSaveButton = menu.findItem(R.id.activityAddRemoveListSaveButton);
        activityAddRemoveListSaveButton.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        try {
            if (itemId == R.id.menu_material_logistics_settings) {
                Intent goToIntent = new Intent(getApplicationContext(), ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.MATERIAL_LOGISTICS_SETTINGS));
                goToIntent.putExtra(HIDE_LOCATION, true);
                startActivity(goToIntent);
            } /*else if (itemId == R.id.menu_help) {
                final Dialog dialog = new HelpDialog(this, getString(R.string.register_new_broken_device));
                dialog.show();
            }*/ else if(itemId == R.id.activityAddRemoveListSaveButton)
            {
                saveButtonClicked();
            }
        } catch (Exception excep) {
            writeLog(TAG + " :onOptionsItemSelected()", excep);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        final DialogFactory dialogFactory = DialogFactory.getInstance();
        try {
            if (view.getId() == R.id.fabSetting) {
                if (!clickHandled) {
                    if (fabExpanded == true) {
                        closeFabSubMenus();
                    } else if (!fabExpanded) {
                        openFabSubMenus();
                    }
                }
            } else if (view.getId() == R.id.layoutFab3) {
                Class<? extends Dialog> dialogClass = dialogFactory.getDialogClass(ConstantsAstSep.DialogKeyConstants.BROKEN_ADD_BY_ID_DIALOG);
                final Dialog dialog = AndroidUtilsAstSep.newInstance(dialogClass, this, this);
                dialog.show();
            } else if (view.getId() == R.id.layoutFab5) {
                if (getItemsSize() > 0) {
                    showDeleteAllItemsDialog();
                } else {
                    Toast.makeText(this, getString(R.string.error_list_empty), Toast.LENGTH_SHORT).show();
                }
            } else if (view.getId() == R.id.layoutFab4) {
                if (getItemsSize() > 0) {
                    Class<? extends Dialog> dialogClass = dialogFactory.getDialogClass(ConstantsAstSep.DialogKeyConstants.BROKEN_REMOVE_BY_ID_DIALOG);
                    final Dialog dialog = AndroidUtilsAstSep.newInstance(dialogClass, this, this);
                    dialog.show();
                } else {
                    Toast.makeText(this, getString(R.string.error_list_empty), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception excep) {
            writeLog(TAG + " :onClick()", excep);
        }

    }

    //@Override
    public void onDeviceListItemClick(ListView l, View v, int position, long id) {
        // Loading screen
        try {
            closeFabSubMenus();
            String identifier = items.get(position).getIdentifier();
            Long idUnitT = items.get(position).getIdUnitT();
            BrokenNewDeviceItem brokenNewDeviceItem = null;
            for (BrokenNewDeviceItem unitItem : items) {
                if (Utils.isNotEmpty(unitItem.getIdUnitT())) {
                    if (unitItem.getIdentifier().equals(identifier) && unitItem.getIdUnitT().equals(idUnitT)) {
                        brokenNewDeviceItem = unitItem;
                    }
                } else {
                    if (unitItem.getIdentifier().equals(identifier)) {
                        brokenNewDeviceItem = unitItem;
                    }
                }

            }
            UnitItem unitItem = new UnitItem(brokenNewDeviceItem.getIdentifier(), brokenNewDeviceItem.getIdUnitT(), brokenNewDeviceItem.getIdUnitModelT(), brokenNewDeviceItem.getIdUnitIdentifierT());
            DeviceInfoFetchThread fetchingDeviceThread = new DeviceInfoFetchThread(RegisterNewBrokenDeviceActivity.this, items.get(position).getIdentifier(), unitItem);
            fetchingDeviceThread.start();
        } catch (Exception e) {
            writeLog(TAG + " :onDeviceListItemClick()", e);
        }
    }

    private void showDeleteAllItemsDialog() {
        try {
            Builder deleteAllBuilder = new Builder(RegisterNewBrokenDeviceActivity.this);
            deleteAllBuilder.setTitle(R.string.delete_all);
            deleteAllBuilder.setMessage(R.string.delete_all_question);
            deleteAllBuilder.setIcon(android.R.drawable.ic_menu_delete);
            deleteAllBuilder.setCancelable(true);
            deleteAllBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeAllItems();
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
        } catch (Exception e) {
            writeLog(TAG + " :showDeleteAllItemsDialog()", e);
        }
    }

    public void saveButtonClicked() {
        new RegisterNewBrokenDeviceSaveThread(this, items).start();
    }

    public void addItem(BrokenNewDeviceItem newItem) {
        try {
            BrokenNewDeviceItem brokenNewDeviceItem = checkforDuplicateAddedItems(newItem);
            if (items.contains(newItem) && Utils.isEmpty(newItem.getIdUnitT())) {
                Toast.makeText(getApplicationContext(), getString(R.string.multiple_device_in_list), Toast.LENGTH_SHORT).show();
            } else if (Utils.isEmpty(brokenNewDeviceItem)) {
                items.add(newItem);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_device_already_in_list), Toast.LENGTH_SHORT).show();
            }
            activityAddRemoveListSaveButton.setVisible(true);
        } catch (Exception e) {
            writeLog(TAG + " :addItem()", e);
        }
    }

    private BrokenNewDeviceItem checkforDuplicateAddedItems(final BrokenNewDeviceItem newItem) {
        BrokenNewDeviceItem alreadyExists = null;
        try {
            if (null != newItem && null != newItem.getIdentifier()) {
                for (BrokenNewDeviceItem unitItem : items) {
                    if (null != unitItem && null != unitItem.getIdentifier() && unitItem.getIdentifier().equals(newItem.getIdentifier())) {
                        if (Utils.isNotEmpty(unitItem.getIdUnitT()) && unitItem.getIdUnitT().equals(newItem.getIdUnitT())) {
                            alreadyExists = unitItem;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :checkforDuplicateAddedItems()", e);
        }
        return alreadyExists;
    }

    public static void removeItem(int position) {
        items.remove(position);
        adapter.notifyDataSetChanged();
        if (items.size() == 0)
            activityAddRemoveListSaveButton.setVisible(false);
    }

    protected void removeAllItems() {
        items.clear();
        adapter.notifyDataSetChanged();
        activityAddRemoveListSaveButton.setVisible(getItemsSize() == 0 ? false : true);
    }

    public boolean hasItem(final BrokenNewDeviceItem unitItem) {
        if (items.contains(unitItem)) {
            for (BrokenNewDeviceItem unitItem1 : items) {
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

    public int getSelectedItemCount(final BrokenNewDeviceItem unitItem) {
        int i = 0;
        try {
            for (BrokenNewDeviceItem item : items) {
                if (item.getIdentifier().equals(unitItem.getIdentifier())) {
                    i++;
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :getSelectedItemCount()", e);
        }
        return i;
    }

    public BrokenNewDeviceItem checkExistsUnitItem(final BrokenNewDeviceItem unitItem) {
        BrokenNewDeviceItem returnItem = null;
        try {
            List<BrokenNewDeviceItem> unitItems = new ArrayList<BrokenNewDeviceItem>();
            for (BrokenNewDeviceItem item : items) {
                if (item.getIdentifier().equals(unitItem.getIdentifier())) {
                    unitItems.add(item);
                }
            }

            for (BrokenNewDeviceItem unitItem1 : unitItems) {
                if (Utils.isNotEmpty(unitItem1.getIdUnitT())) {
                    if (unitItem1.getIdUnitT().equals(unitItem.getIdUnitT())) {
                        returnItem = unitItem1;
                    } else if (Utils.isEmpty(unitItem.getIdUnitT())) {
                        returnItem = unitItem;
                    }/*else{
                    returnItem = null;
				}*/
                } else if (Utils.isEmpty(unitItem1.getIdUnitT()) && Utils.isNotEmpty(unitItem.getIdUnitT())) {
                    returnItem = null;
                } else {
                    returnItem = unitItem;
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :checkExistsUnitItem()", e);
        }
        return returnItem;
    }


    public boolean removeItem(BrokenNewDeviceItem itemToRemove) {
        int i = 0;
        BrokenNewDeviceItem brokenNewDeviceItemCheck = checkforDuplicateAddedItems(itemToRemove);
        try {
            if (Utils.isNotEmpty(brokenNewDeviceItemCheck)) {
                for (BrokenNewDeviceItem brokenNewDeviceItem : items) {
                    if (brokenNewDeviceItem.getIdentifier().equals(itemToRemove.getIdentifier())) {
                        if (Utils.isNotEmpty(itemToRemove.getIdUnitT()) && brokenNewDeviceItem.getIdUnitT().equals(itemToRemove.getIdUnitT())) {
                            items.remove(brokenNewDeviceItem);
                            activityAddRemoveListSaveButton.setVisible(getItemsSize() == 0 ? false : true);
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                    }
                    i++;
                }
            } else {
                for (BrokenNewDeviceItem brokenNewDeviceItem : items) {
                    if (brokenNewDeviceItem.getIdentifier().equals(itemToRemove.getIdentifier())) {
                        items.remove(brokenNewDeviceItem);
                        activityAddRemoveListSaveButton.setVisible(getItemsSize() == 0 ? false : true);
                        adapter.notifyDataSetChanged();
                        return true;
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            writeLog("BrokenNewDeviceAdapter :buildView()", e);
        }
        Toast.makeText(getApplicationContext(), getString(R.string.error_identifier_not_found_in_list), Toast.LENGTH_SHORT).show();
        activityAddRemoveListSaveButton.setVisible(getItemsSize() == 0 ? false : true);
        return false;
    }

    public int getItemsSize() {
        return items.size();
    }

    /**
     * on click of option menu btn
     */
    public void headerButtonClicked(View view) {
        if (view.getId() == R.id.option_btn)
            openOptionsMenu();
    }
}
