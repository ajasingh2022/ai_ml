package com.capgemini.sesp.ast.android.ui.activity.material_logistics.manage_pallet.content;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.MaterialLogisticsSettingsActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.CommonListActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.UnitItem;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.manage_pallet.PalletInformationActivity;
import com.capgemini.sesp.ast.android.ui.adapter.UnitAdapter;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelIdentifierTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.PalletLightTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitIdentifierTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class PalletContentActivity extends CommonListActivity {
	private PalletLightTO palletLightTO;
	private List<UnitItem> removeItems;
	static String TAG = PalletContentActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle(R.string.pallet_content);

		removeItems = new ArrayList<UnitItem>();
		displayItems = new ArrayList<UnitItem>();
		this.palletLightTO = (PalletLightTO)getIntent().getExtras().get(PalletInformationActivity.INTENT_PALLET);

		listView = findViewById(android.R.id.list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				onCommonListItemClick(listView,view,position,id);


			}
		});

		new GuiWorker<List<UnitIdentifierTO>>(this) {
			@Override
			protected List<UnitIdentifierTO> runInBackground() throws Exception {
				return AndroidUtilsAstSep.getDelegate().getUnitsOnPallet(palletLightTO.getId());
			}

			@Override
			protected void onPostExecute(boolean successful, List<UnitIdentifierTO> palletContents) {
				if(isSuccessful()) {
					displayItems = new ArrayList<UnitItem>();
					for(UnitIdentifierTO palletContent : palletContents) {
						if(palletContent.getType().equalsIgnoreCase("bulk")) {
							UnitItem unitItem = new UnitItem(ObjectCache.getIdObject(UnitModelCustomTO.class, palletContent.getIdUnitModel()), palletContent.getQuantity().intValue(), null);
							unitItem.setSaved(true);
							displayItems.add(unitItem);
						} else {
							UnitModelCustomTO unitModelCustomTO = ObjectCache.getIdObject(UnitModelCustomTO.class, palletContent.getIdUnitModel());

							Long idUnitIdentifierT = null;
							for(UnitModelIdentifierTO unitModelIdentifierTO : unitModelCustomTO.getUnitModelIdentifierTOs()) {
								if(unitModelIdentifierTO.getIdApplication() == null) {
									idUnitIdentifierT = unitModelIdentifierTO.getIdUnitIdentifierT();
								}
							}
							String identifier = null;
							if(idUnitIdentifierT != null) {
								if (idUnitIdentifierT.equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__GIAI)) {
									identifier = palletContent.getGiai();
								} else if (idUnitIdentifierT.equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO)) {
									identifier = palletContent.getSerialNumber();
								} else if (idUnitIdentifierT.equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO)) {
									identifier = palletContent.getPropertyNumber();
								}
							}
							UnitItem unitItem = new UnitItem(identifier, Utils.isNotEmpty(palletContent.getIdUnitT()) ? palletContent.getIdUnitT() : null, Utils.isNotEmpty(palletContent.getIdUnitModel()) ?
									palletContent.getIdUnitModel() : null, idUnitIdentifierT);
							unitItem.setSaved(true);
							displayItems.add(unitItem);
						}
					}
					adapter = new UnitAdapter(PalletContentActivity.this, displayItems, R.layout.activity_add_remove_list_count_row);
					listView.setAdapter(adapter);
				}
			}
		}.start();

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("PalletContentActivity" , "onresume called");
	}

	public void saveButtonClicked() {
		final List<UnitIdentifierTO> unitsToAdd = createItems(displayItems, false);
		final List<UnitIdentifierTO> unitsToRemove = createItems(removeItems, true);
		new GuiWorker<Void>(this) {
			@Override
			protected Void runInBackground() throws Exception {
				AndroidUtilsAstSep.getDelegate().addRemoveUnitsToPallet(PalletContentActivity.this.palletLightTO.getId(), MaterialLogisticsSettingsActivity.getIdStock(), unitsToAdd, unitsToRemove);
				return null;
			}
			@Override
			protected void onPostExecute(boolean successful, Void result) {
				if(isSuccessful()) {
					new GuiWorker<PalletLightTO>(PalletContentActivity.this) {
						@Override
						protected PalletLightTO runInBackground() throws Exception {
							return AndroidUtilsAstSep.getDelegate().getPallet(palletLightTO.getCode());
						}

						@Override
						protected void onPostExecute(boolean successful, PalletLightTO palletLightTO) {
							if(isSuccessful()) {
								Intent palletInformationIntent = new Intent(ctx, ActivityFactory.getInstance().getActivityClass(ActivityConstants.PALLET_INFORMATION_ACTIVITY));
								palletInformationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								palletInformationIntent.putExtra(PalletInformationActivity.INTENT_PALLET, palletLightTO);
								startActivity(palletInformationIntent);
							}
						}
					}.start();

				}
			}
		}.start();

		
	}

	@Override
	public void removeItem(int position){
		try{
		UnitItem unitItem = displayItems.get(position);
		if(unitItem.isSaved()) {
			removeItems.add(unitItem);
		}
		displayItems.remove(position);
		activityAddRemoveListSaveButton.setVisible(getItemsSize()==0?false:true);
		adapter.notifyDataSetChanged();
		if(displayItems.size()==0)
			showSaveAllItemsDeletedDialog();
		}catch(Exception e){
			writeLog(TAG+"  : removeItem() " ,e);
		}
	}
	
	@Override
	protected void showDeleteAllItemsDialog() {
		try{
		Builder deleteAllBuilder = new Builder(PalletContentActivity.this);
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
		}catch(Exception e){
			writeLog(TAG+"  : showDeleteAllItemsDialog() " ,e);
		}
	}

	protected void showSaveAllItemsDeletedDialog() {
		try{
		hideSoftKeyboard();
		Builder SaveAllItemsDeletedBuilder = new Builder(PalletContentActivity.this);
		SaveAllItemsDeletedBuilder.setTitle(R.string.saving);
		SaveAllItemsDeletedBuilder.setMessage(R.string.save_question);
		SaveAllItemsDeletedBuilder.setIcon(android.R.drawable.ic_menu_save);
		SaveAllItemsDeletedBuilder.setCancelable(false);
		SaveAllItemsDeletedBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveButtonClicked();
			}
		});
		SaveAllItemsDeletedBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				restoreAllDeletedItems();
				deleteAllDialog.dismiss();
			}
		});
		SaveAllItemsDeletedBuilder.create();
		deleteAllDialog = SaveAllItemsDeletedBuilder.show();
		}catch(Exception e){
			writeLog(TAG+"  : showSaveAllItemsDeletedDialog() " ,e);
		}
	}
	protected void restoreAllDeletedItems(){
		try{
		if(removeItems.size()>0) {
			displayItems.clear();
			displayItems.addAll(removeItems);
			removeItems.clear();
			activityAddRemoveListSaveButton.setVisible(getItemsSize()==0?false:true);
			adapter.notifyDataSetChanged();
		}
		}catch(Exception e){
			writeLog(TAG+"  : restoreAllDeletedItems() " ,e);
		}
	}
	/**
	 * Hides the soft keyboard
	 */
	public void hideSoftKeyboard() {

		if(getCurrentFocus()!=null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}
	@Override
	protected void deleteAllItems() {
		try{
		for(UnitItem item : displayItems) {
			if(item.isSaved()) {
				removeItems.add(item);
			}
		}
		displayItems.clear();
		activityAddRemoveListSaveButton.setVisible(getItemsSize()==0?false:true);
		adapter.notifyDataSetChanged();
		showSaveAllItemsDeletedDialog();
		}catch(Exception e){
			writeLog(TAG+"  : deleteAllItems() " ,e);
		}
	}

	@Override
	public boolean removeItem(UnitItem itemToRemove) {
		int i = 0;
		try{
		final UnitItem selectedUnitItem = checkforDuplicateAddedItems(itemToRemove);
		for (UnitItem unitItem : displayItems) {
			if (unitItem.getIdUnitIdentifierT() != null && unitItem.getIdUnitIdentifierT().equals(MaterialLogisticsSettingsActivity.getIdUnitIdentifierT()) && unitItem.getTitle().equals(itemToRemove.getTitle())) {
				if (Utils.isEmpty(selectedUnitItem)) {
					if (unitItem.isSaved()) {
						removeItems.add(unitItem);
					}
					displayItems.remove(unitItem);
					activityAddRemoveListSaveButton.setVisible(getItemsSize()==0?false:true);
					adapter.notifyDataSetChanged();
					if (displayItems.size() == 0)
						showSaveAllItemsDeletedDialog();
					return true;
				} else {
					if (Utils.isNotEmpty(itemToRemove.getIdUnitT()) && unitItem.getIdUnitT().equals(itemToRemove.getIdUnitT())) {
						if (unitItem.isSaved()) {
							removeItems.add(unitItem);
						}
						displayItems.remove(unitItem);
						activityAddRemoveListSaveButton.setVisible(getItemsSize()==0?false:true);
						adapter.notifyDataSetChanged();
						if (displayItems.size() == 0)
							showSaveAllItemsDeletedDialog();
						return true;

					}
				}
			}
			i++;
		}
		Toast.makeText(getApplicationContext(), getString(R.string.error_identifier_not_found_in_list), Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			writeLog(TAG+"  : removeItem() " ,e);
		}
		return false;
	}

	@Override
	public boolean removeItem(String itemToRemove) {
		int i = 0;
		try{
		for (UnitItem unitItem : displayItems) {
			if (unitItem.getIdUnitIdentifierT() != null && unitItem.getIdUnitIdentifierT().equals(MaterialLogisticsSettingsActivity.getIdUnitIdentifierT()) && unitItem.getTitle().equals(itemToRemove)) {
				if(unitItem.isSaved()) {
					removeItems.add(unitItem);
				}
				displayItems.remove(i);
				activityAddRemoveListSaveButton.setVisible(getItemsSize()==0?false:true);
				adapter.notifyDataSetChanged();
				if(displayItems.size()==0)
					showSaveAllItemsDeletedDialog();
				return true;
			}
			i++;
		}
		Toast.makeText(getApplicationContext(), getString(R.string.error_identifier_not_found_in_list), Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			writeLog(TAG+"  : removeItem() " ,e);
		}
		return false;
	}

	protected UnitItem checkforDuplicateAddedItems(final UnitItem newItem){
		UnitItem alreadyExists = null;
		try{
		if(null != newItem && null != newItem.getId()) {
			for (UnitItem unitItem : displayItems) {
				if (null != unitItem && null != unitItem.getId() && unitItem.getId().equals(newItem.getId())) {
					if(Utils.isNotEmpty(unitItem.getIdUnitT()) && unitItem.getIdUnitT().equals(newItem.getIdUnitT())) {
						alreadyExists = unitItem;
						break;
					}
				}
			}
		}
		}catch(Exception e){
			writeLog(TAG+"  : checkforDuplicateAddedItems() " ,e);
		}

		return alreadyExists;
	}

	/**
	 * on click of option menu btn
	 */
	public void headerButtonClicked(View view) {
		if(view.getId() == R.id.option_btn)
			openOptionsMenu();
	}


}