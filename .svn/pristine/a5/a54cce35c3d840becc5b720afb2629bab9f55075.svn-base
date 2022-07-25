package com.capgemini.sesp.ast.android.ui.activity.material_logistics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.skvader.rsp.ast_sep.common.to.ast.table.StockTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class SiteSelectionActivity extends AppCompatActivity {
	private final String TAG = getClass().getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.site_list);

		List<StockTO> stockTOs = new ArrayList<StockTO>(ObjectCache.getAllIdObjects(StockTO.class));

		Collections.sort(stockTOs, new Comparator<StockTO>() {
			@Override
			public int compare(StockTO lhs, StockTO rhs) {
				if (lhs.getName() != null && rhs.getName() != null) {
					return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
				}
				return -1;
			}
		});

		StockListAdapter siteListAdapter = new StockListAdapter(this,stockTOs);
		ListView siteListView = findViewById(R.id.siteList);
		siteListView.setAdapter(siteListAdapter);
		siteListView.setFastScrollEnabled(true);


		TextView headerText = findViewById(R.id.title_text);
		headerText.setText(R.string.set_location);

		EditText filterSiteEditText = findViewById(R.id.filterSiteEditText);
		filterSiteEditText.addTextChangedListener(new StockTextWatcher(siteListAdapter));
		} catch (Exception e) {
			writeLog("SiteSelectionActivity  : onCreate() ", e);
		}

	}

	private class StockListAdapter extends ArrayAdapter<StockTO> {

		private List<StockTO> stockTOList;

		public StockListAdapter(Context context, List<StockTO> objects) {
			super(context,0, objects);
			this.stockTOList = new ArrayList<StockTO>(objects);
		}

		private class ViewHolder {
			public TextView rowItemText;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			try{
			ViewHolder viewHolder = null;
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
				viewHolder = new ViewHolder();
				viewHolder.rowItemText = convertView.findViewById(android.R.id.text1);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.rowItemText.setText(getItem(position).getName());
			viewHolder.rowItemText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(TAG, " Clicked view " + getItem(position).getName());
					SESPPreferenceUtil.savePreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK, getItem(position).getId());
					Intent intent = new Intent();
					intent.setClass(SiteSelectionActivity.this, SESPMaterialLogisticsSettingsActivity.class);
					finish();
				}
			});
			} catch (Exception e) {
				writeLog("SiteSelectionActivity  : getView() ", e);
			}
			return convertView;
		}

		@Override
		public Filter getFilter() {
			return new Filter() {

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					Log.d(TAG,"*******PERFORM FILTERING*********");
					final FilterResults oReturn = new FilterResults();
					final List<StockTO> filteredStock = new ArrayList<StockTO>(stockTOList);
					if(!TextUtils.isEmpty(constraint)) {
						if(stockTOList != null && !stockTOList.isEmpty()) {
							for(StockTO stockTO : stockTOList) {
								if(!stockTO.getName().contains(constraint)) {
									filteredStock.remove(stockTO);
								}
							}
						}
					}
					oReturn.values = filteredStock;
					Log.d(TAG,"SIZE OF FILTERED STOCK IS : "+filteredStock.size());
					return oReturn;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					Log.d(TAG, "**********PUBLISH RESULTS************");
					StockListAdapter.this.clear();
					StockListAdapter.this.addAll((List<StockTO>)results.values);
					StockListAdapter.this.notifyDataSetChanged();
				}
			};
		}
	}

	private class StockTextWatcher implements TextWatcher {

		private ArrayAdapter<StockTO> siteListAdapter = null;

		public StockTextWatcher(ArrayAdapter<StockTO> siteListAdapter) {
			this.siteListAdapter = siteListAdapter;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//Do nothing
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//Do nothing
		}

		@Override
		public void afterTextChanged(Editable s) {
			Log.d(getClass().getSimpleName(), "Entered text :: "+ s.toString());
			siteListAdapter.getFilter().filter(s.toString());
			siteListAdapter.notifyDataSetChanged();
		}
	}

}
