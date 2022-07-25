package com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_taking;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.CommonListActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.UnitItem;
import com.capgemini.sesp.ast.android.ui.adapter.UnitAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.ast_sep.common.to.custom.AddToStocktakingTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class StockTakingListActivity extends CommonListActivity {

	private Long idStockTaking;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		/*findViewById(R.id.option_btn).setVisibility(View.VISIBLE);
        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(R.string.title_activity_stock_taking);
        View v= findViewById(R.id.title_bar);
        v.setVisibility(View.GONE);
		getSupportActionBar().setTitle(R.string.title_activity_stock_taking);
*/
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
				final Dialog dialog = new HelpDialog(StockTakingListActivity.this, ConstantsAstSep.HelpDocumentConstant.PAGE_STOCK_TAKING);
				dialog.show();
			}
		});

		// -- Customizing the action bar ends -----


		getSupportActionBar().setCustomView(vw);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		TextView txtTitleBar = findViewById(R.id.title_text);
		txtTitleBar.setText(R.string.title_activity_stock_taking);



		listView = findViewById(android.R.id.list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				onCommonListItemClick(listView,view,position,id);


			}
		});

        displayItems = new ArrayList<UnitItem>();
        adapter = new UnitAdapter(this, displayItems, R.layout.activity_add_remove_list_count_row);
        listView.setAdapter(adapter);

    	helpText = getString(R.string.help_stock_taking_list);
		activityClass = StockTakingListActivity.class;
    }

	@Override
	protected void onResume() {
		super.onResume();
		idStockTaking = getIntent().getLongExtra(StockTakingActivity.ID_STOCKTAKING, -1L);
	}

	public void saveButtonClicked() {

    	new GuiWorker<Void>(this) {
			@Override
			protected Void runInBackground() throws Exception {
				List<AddToStocktakingTO> addToStocktakingTOs = getAddToStocktakingValues(displayItems);
				
				AndroidUtilsAstSep.getDelegate().addToStocktaking(idStockTaking, addToStocktakingTOs, new Date());
				return null;
			}
			
			@Override
			protected void onPostExecute(boolean successful, Void result) {
				if(isSuccessful()) {
					displayItems.clear();
					adapter.notifyDataSetChanged();
					Builder builder = GuiController.showInfoDialog(StockTakingListActivity.this, getString(R.string.stock_taking_completed));
					builder.show();
				}
			}
		}.start();

    }
	/**
	 * on click of option menu btn
	 */
	public void headerButtonClicked(View view) {
		if(view.getId() == R.id.option_btn)
			openOptionsMenu();
	}

	@Override
	public boolean removeItem(UnitItem itemToRemove) {
		displayItems.remove(itemToRemove);
		adapter.notifyDataSetChanged();
		activityAddRemoveListSaveButton.setVisible(getItemsSize()==0?false:true);
		return true;
	}
}
