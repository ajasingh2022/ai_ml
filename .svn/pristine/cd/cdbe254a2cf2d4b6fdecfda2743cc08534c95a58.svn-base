package com.capgemini.sesp.ast.android.ui.activity.material_logistics.manage_pallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.skvader.rsp.ast_sep.common.to.ast.table.PalletBulkUnitTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.PalletStatusTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.PalletTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.PalletTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.StockTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitOwnerTTO;
import com.skvader.rsp.ast_sep.common.to.ast.view.PalletUnitSummaryTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.PalletLightTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;

public class PalletInformationActivity extends AppCompatActivity {
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	public static final String INTENT_PALLET = "pallet";

	static private PalletLightTO palletLightTO;
	protected int SEND_IDENTIFIER_SCAN_REQUEST = 1;
	EditText editText = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_pallet_main);
		findViewById(R.id.option_btn).setVisibility(View.VISIBLE);
		TextView headerText = findViewById(R.id.title_text);
		headerText.setText(R.string.pallet_information);

		View v = findViewById(R.id.title_layout);
		v.setVisibility(View.GONE);

		getSupportActionBar().setTitle(R.string.pallet_information);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		palletLightTO = (PalletLightTO)(getIntent().getExtras().get(INTENT_PALLET));
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	private class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
		}

		@Override
		public Fragment getItem(int i) {
			if(i == 0) {
				return new InformationTab(getApplicationContext());
			} else {
				return new UnitSummaryTab(getApplicationContext());
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.info);
			case 1:
				return getString(R.string.unit_summary);
			}
			return null;
		}
	}

	@SuppressLint({"ValidFragment","InflateParams"})
	public static class UnitSummaryTab extends Fragment {
		public UnitSummaryTab(Context context) {}
		
		@Override
		public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View layout = inflater.inflate(R.layout.activity_manage_pallet_pallet_summary, null);
			TextView header1 = layout.findViewById(R.id.header1);
			header1.setText(R.string.name);
			TextView header2 = layout.findViewById(R.id.header2);
			header2.setText(R.string.count);
			ListView unitModelInformation = layout.findViewById(R.id.pallet_summary_list);
			unitModelInformation.setAdapter(new BaseAdapter() {
				
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					if (convertView == null) {
						convertView = inflater.inflate(R.layout.activity_row_two_column, null);
					}
					TextView leftTextView = convertView.findViewById(R.id.leftColumn);
					TextView rightTextView = convertView.findViewById(R.id.rightColumn);
					if(palletLightTO.getPalletBulkUnitTOs() != null) {
						if(position < palletLightTO.getPalletBulkUnitTOs().size()) {
							PalletBulkUnitTO palletBulkUnitTO = palletLightTO.getPalletBulkUnitTOs().get(position);
							leftTextView.setText(ObjectCache.getIdObjectName(UnitModelCustomTO.class, palletBulkUnitTO.getIdUnitModel()));
							rightTextView.setText(Utils.safeToString(palletBulkUnitTO.getQuantity()));
							return convertView;
						} else {
							position = position - palletLightTO.getPalletBulkUnitTOs().size();
						}
					}
					PalletUnitSummaryTO palletUnitSummaryTO = palletLightTO.getPalletUnitSummarys().get(position);
					leftTextView.setText(ObjectCache.getIdObjectName(UnitModelCustomTO.class, palletUnitSummaryTO.getIdUnitModel()));
					rightTextView.setText(Utils.safeToString(palletUnitSummaryTO.getSumUnits()));
					return convertView;
				}
				
				@Override
				public long getItemId(int position) {
					return position;
				}
				
				@Override
				public Object getItem(int position) {
					return null;
				}
				
				@Override
				public int getCount() {
					return 
						(palletLightTO.getPalletBulkUnitTOs() == null ? 0 : palletLightTO.getPalletBulkUnitTOs().size()) +
						(palletLightTO.getPalletUnitSummarys() == null ? 0 : palletLightTO.getPalletUnitSummarys().size())
					;
				}
			});
			return layout;
		}		
	}
	
	@SuppressLint({"ValidFragment","InflateParams"})
	public static class InformationTab extends Fragment {
		public InformationTab(Context context) {}

		@Override
		public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			final LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.activity_manage_pallet_pallet_info, null);

			ListView palletInfoListView = layout.findViewById(R.id.pallet_info_list);
			palletInfoListView.setAdapter(new BaseAdapter() {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					if (convertView == null) {
						convertView = inflater.inflate(R.layout.activity_row, null);
					}

					TextView nameTextView = convertView.findViewById(R.id.name);
					TextView valueTextView = convertView.findViewById(R.id.value);
					switch(position) {
						case 0:	
							nameTextView.setText(getString(R.string.code));
							valueTextView.setText(palletLightTO.getCode());
							break;
						case 1:	
							nameTextView.setText(getString(R.string.type));
							valueTextView.setText(ObjectCache.getTypeName(PalletTTO.class, palletLightTO.getIdPalletT()));
							break;
						case 2:	
							nameTextView.setText(getString(R.string.status));
							valueTextView.setText(ObjectCache.getTypeName(PalletStatusTTO.class, palletLightTO.getIdPalletStatusT()));
							break;
						case 3:	
							nameTextView.setText(getString(R.string.freigth_number));
							(valueTextView).setText(palletLightTO.getFreightNumber());
							break;
						case 4:	
							nameTextView.setText(getString(R.string.info));
							valueTextView.setText(palletLightTO.getInfo());
							break;
						case 5:	
							nameTextView.setText(getString(R.string.sender_sent_from));
							valueTextView.setText(ObjectCache.getIdObjectName(StockTO.class, palletLightTO.getSendingIdStock()));
							break;
						case 6:
							nameTextView.setText(getString(R.string.destination));
							(valueTextView).setText(ObjectCache.getIdObjectName(StockTO.class, palletLightTO.getDestinationIdStock()));
							break;
						case 7:
							nameTextView.setText(getString(R.string.received_at));
							valueTextView.setText(ObjectCache.getIdObjectName(StockTO.class, palletLightTO.getReceivingIdStock()));
							break;
						case 8:
							nameTextView.setText(getString(R.string.owner));
							valueTextView.setText(ObjectCache.getTypeName(UnitOwnerTTO.class, palletLightTO.getIdUnitOwnerT()));
							break;
					}
					return convertView;
				}
				
				@Override
				public long getItemId(int position) {
					return position;
				}
				
				@Override
				public Object getItem(int position) {
					return null;
				}
				
				@Override
				public int getCount() {
					return 9;
				}
			});
			return layout;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_manage_pallet_handling_action_list, menu);
		return true;
	}
	
	private static final int DIALOG_CODE__SEND_PALLET = 0;
	
	 @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		StockTO currentStockTO = ObjectCache.getIdObject(StockTO.class,
				SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK));
		StockTO sendingStockTO = ObjectCache.getIdObject(StockTO.class, palletLightTO.getSendingIdStock());
		
        int itemId = item.getItemId();
        if (itemId == R.id.add_remove) {
			if(!palletLightTO.getSendingIdStock().equals(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK))) {
        		Toast.makeText(this, getString(R.string.you_are_on_the_wrong_stock_current_X_corret_y, currentStockTO.getName(), sendingStockTO.getName()),Toast.LENGTH_LONG).show();
        		return true;
        	}
        	if(palletLightTO.getDestinationIdStock() != null) {
        		Toast.makeText(this, R.string.the_pallet_is_already_sent,Toast.LENGTH_LONG).show();
        		return true;
        	}
        	
            Intent intent = new Intent(getApplicationContext(), ActivityFactory.getInstance().getActivityClass(ActivityConstants.PALLET_CONTENT_ACTIVITY));
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			intent.putExtra(PalletInformationActivity.INTENT_PALLET, palletLightTO);
			startActivity(intent);
        	return true;
        } else if (itemId == R.id.send_pallet) {
        	if(!palletLightTO.getSendingIdStock().equals(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK))) {
        		Toast.makeText(this, getString(R.string.you_are_on_the_wrong_stock_current_X_corret_y, currentStockTO.getName(), sendingStockTO.getName()),Toast.LENGTH_LONG).show();
        		return true;
        	}
        	if(palletLightTO.getDestinationIdStock() != null) {
        		Toast.makeText(this, R.string.the_pallet_is_already_sent,Toast.LENGTH_LONG).show();
        		return true;
        	}
			Dialog sendPalletDialog = createDialogSendPallet();
			if(sendPalletDialog != null) {
				sendPalletDialog.show();
			}
            return true;
        } else if (itemId == R.id.receive_pallet) {
        	if(!palletLightTO.getIdPalletStatusT().equals(CONSTANTS().PALLET_STATUS_T__SENT)) {
        		Toast.makeText(this, R.string.pallet_is_not_sent,Toast.LENGTH_LONG).show();
        		return true;
        	}
        	if(!palletLightTO.getDestinationIdStock().equals(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK))) {
        		StockTO destinationStockTO = ObjectCache.getIdObject(StockTO.class, palletLightTO.getDestinationIdStock());
        		Toast.makeText(this, getString(R.string.you_are_on_the_wrong_stock_current_X_corret_y, currentStockTO.getName(), destinationStockTO.getName()),Toast.LENGTH_LONG).show();
        		return true;
        	}
        	if(palletLightTO.getReceivingIdStock() != null) {
        		Toast.makeText(this, R.string.the_pallet_is_already_received,Toast.LENGTH_LONG).show();
        		return true;
        	}
            // showAboutDialog();
            createDialogReceivePallet();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	 
	 
	private void createDialogReceivePallet() {
		final StockTO currentStockTO = ObjectCache.getIdObject(StockTO.class, SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK));
    	
    	new GuiWorker<PalletLightTO>(PalletInformationActivity.this) {
			@Override
			protected PalletLightTO runInBackground() throws Exception {
				AndroidUtilsAstSep.getDelegate().receivePallet(
						palletLightTO.getId(), 
						currentStockTO.getId()
				);
				return AndroidUtilsAstSep.getDelegate().getPallet(palletLightTO.getCode());
			}
			
			@Override
			protected void onPostExecute(boolean successful, PalletLightTO palletLightTO) {
				if(successful) {
					Intent palletInformationIntent = new Intent(getApplicationContext(), ActivityFactory.getInstance()
							.getActivityClass(ActivityConstants.PALLET_INFORMATION_ACTIVITY));
					palletInformationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					palletInformationIntent.putExtra(PalletInformationActivity.INTENT_PALLET, palletLightTO);
					startActivity(palletInformationIntent);
				}
			}
			
		}.start();
	}
	 
	 
	private Dialog createDialogSendPallet() {
		final Dialog dialog = new Dialog(this);
    	dialog.setContentView(R.layout.activity_manage_pallet_dialog_send_pallet);
    	dialog.setTitle(R.string.send);
    	dialog.setCancelable(true);
    	
    	final Spinner destinationSpinner = dialog.findViewById(R.id.destinationSpinner);
    	final StockTO currentStockTO = ObjectCache.getIdObject(StockTO.class, SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK));
    	
    	List<DisplayItem<StockTO>> stockList = new ArrayList<DisplayItem<StockTO>>();
    	for(StockTO stockTO : ObjectCache.getAllIdObjects(StockTO.class)) {
    		for(PalletTCTO palletTCTO : ObjectCache.getAllIdObjects(PalletTCTO.class)) {
    			if(palletTCTO.getIdPalletTCT().equals(AndroidUtilsAstSep.CONSTANTS().PALLET_T_C_T__DESTINATION)) {
    				if(palletTCTO.getIdPalletT().equals(palletLightTO.getIdPalletT())) {
    					if(stockTO.getIdStockT().equals(palletTCTO.getIdStockT())) {
    						//The stock is valid to send this pallet to
    						if(stockTO.getId().equals(SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK))) {//Skip this since this is the same stock as we are on
    							break;
    						}
    						
    						if(stockTO.getIdDomain().equals(currentStockTO.getIdDomain())) {
    							DisplayItem<StockTO> displayItem = new DisplayItem<StockTO>(stockTO);
    			        		stockList.add(displayItem);
    						}
    					}
    				}
    			}
    		}
    	}
		if(!(stockList.size()>0)){
			Toast.makeText(this, R.string.current_stock_site_can_not_send_pallet,Toast.LENGTH_LONG).show();
			return null;
		}
		ArrayAdapter<DisplayItem<StockTO>> dataAdapter = new ArrayAdapter<DisplayItem<StockTO>>(this, android.R.layout.simple_spinner_item, stockList);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	destinationSpinner.setAdapter(dataAdapter);
    	
    	editText = dialog.findViewById(R.id.freightNumberEditText);
    	Button sendButton = dialog.findViewById(R.id.sendButton);

		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new GuiWorker<PalletLightTO>(PalletInformationActivity.this) {
					@Override
					protected PalletLightTO runInBackground() throws Exception {
						final Long destinationIdStock = ((DisplayItem<StockTO>)destinationSpinner.getSelectedItem()).getUserObject().getId();
						AndroidUtilsAstSep.getDelegate().sendPallet(
								palletLightTO.getId(), 
								currentStockTO.getId(), 
								destinationIdStock,
								editText.getText().toString()
						);
						Log.d(getClass().getSimpleName(),"Current stock id :: " + currentStockTO.getId());
						Log.d(getClass().getSimpleName(),"Selected destination stock id :: " + destinationIdStock);
						return AndroidUtilsAstSep.getDelegate().getPallet(palletLightTO.getCode());
					}

					@Override
					protected void onPostExecute(boolean successful, PalletLightTO palletLightTO) {
						if (successful) {
							Intent palletInformationIntent = new Intent(getApplicationContext(), ActivityFactory.getInstance()
                                    .getActivityClass(ActivityConstants.PALLET_INFORMATION_ACTIVITY));
							palletInformationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							palletInformationIntent.putExtra(PalletInformationActivity.INTENT_PALLET, palletLightTO);
							startActivity(palletInformationIntent);
							dialog.dismiss();
						}
					}

				}.start();
			}
		});
		final ImageButton dialogIdentifierScanButton = (ImageButton) dialog.findViewById(R.id.dialogIdentifierScanButton);

		dialogIdentifierScanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				AndroidUtilsAstSep.scanBarCode( , SEND_IDENTIFIER_SCAN_REQUEST);
				scanBarCode(SEND_IDENTIFIER_SCAN_REQUEST);
			}
		});
    	return dialog;
	}
	private void scanBarCode(int value){
		AndroidUtilsAstSep.scanBarCode(this , SEND_IDENTIFIER_SCAN_REQUEST);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
		super.onActivityResult(requestCode, resultCode, imageData);
		if (requestCode == SEND_IDENTIFIER_SCAN_REQUEST
				&& resultCode == Activity.RESULT_OK) {
			editText.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
			AndroidUtilsAstSep.showHideSoftKeyBoard(this, false);
		}
	}
	/**
	 * on click of option menu btn
	 */
	public void headerButtonClicked(View view) {
		if(view.getId() == R.id.option_btn)
			openOptionsMenu();
	}
	 
}
