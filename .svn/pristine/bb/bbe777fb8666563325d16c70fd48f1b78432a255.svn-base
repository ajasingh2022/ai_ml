package com.capgemini.sesp.ast.android.ui.activity.material_logistics.car;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.goods_reception.GoodsReceptionActivity;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.ast_sep.common.to.ast.table.StockTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class CarInputActivity extends AppCompatActivity {

    AutoCompleteTextView fromAutoCompleteTextView;
    AutoCompleteTextView toAutoCompleteTextView;
    private TextView fromStockSiteTV;
    private TextView toStockSiteTV;
    ImageView fromDownArrowIV;
    ImageView toDownArrowIV;
    Button buttonNext = null;

    ArrayList<DisplayItem<StockTO>> items;
    ArrayList<StockTO> stockTOs;
    DisplayItem<StockTO> toBeSelected = null;

    static String TAG = CarInputActivity.class.getSimpleName();

    boolean isFromVehicle = false;
    boolean isToVehicle = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_car_input);
          //  getSupportActionBar().setTitle(R.string.title_car_in_out);



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
                final Dialog dialog = new HelpDialog(CarInputActivity.this, ConstantsAstSep.HelpDocumentConstant.PAGE_CAR);
                    					dialog.show();
                   		}
		});
			// -- Customizing the action bar ends -----
            getSupportActionBar().setCustomView(vw);
            	getSupportActionBar().setDisplayShowCustomEnabled(true);
            	TextView headerText = findViewById(R.id.title_text);
            	headerText.setText(R.string.title_car_in_out);


            fromAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.fromAutoCompleteTextView);
            toAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.toAutoCompleteTextView);
            fromStockSiteTV = findViewById(R.id.fromStockSiteTextView);
            toStockSiteTV = findViewById(R.id.toStockSiteTextView);
            fromDownArrowIV = (ImageView)findViewById(R.id.downArrowFrom);
            toDownArrowIV = (ImageView) findViewById(R.id.downArrowTo);
            buttonNext = (Button) findViewById(R.id.nextButton);

            stockTOs = new ArrayList<StockTO>(ObjectCache.getAllIdObjects(StockTO.class));
            items = new ArrayList<DisplayItem<StockTO>>();
            StockTO settingStockTO = ObjectCache.getIdObject(StockTO.class, SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK));
            int count = 0;
            for (int i = 0; i < stockTOs.size(); i++) {
                StockTO item = stockTOs.get(i);
                if (item.getIdDomain().equals(settingStockTO.getIdDomain())) {
                    if (item.getActive().equals(AndroidUtilsAstSep.CONSTANTS().SYSTEM_BOOLEAN_T__TRUE)) {
                        if (item.getName().equals(settingStockTO.getName())) {
                            toBeSelected = new DisplayItem<StockTO>(item);
                            items.add(toBeSelected);
                        } else {
                            items.add(new DisplayItem<StockTO>(item));
                        }
                        count++;
                        Log.d("", "====Matching=====" + count + ":" + item.getIdDomain());
                    }
                } else {
                    Log.d("", "===not =Matching=====" + item.getIdDomain());
                }
            }
            final ArrayAdapter<DisplayItem<StockTO>> fromArrayAdapter = new ArrayAdapter<DisplayItem<StockTO>>(this, android.R.layout.simple_dropdown_item_1line, items);
            final ArrayAdapter<DisplayItem<StockTO>> toArrayAdapter = new ArrayAdapter<DisplayItem<StockTO>>(this, android.R.layout.simple_dropdown_item_1line, items);

            fromAutoCompleteTextView.setAdapter(fromArrayAdapter);
            toAutoCompleteTextView.setAdapter(toArrayAdapter);

            fromAutoCompleteTextView.setText(toBeSelected.getName());
            fromStockSiteTV.setText(toBeSelected.getName());

            isFromVehicle = items.get(items.indexOf(toBeSelected)).getUserObject().getIdStockT().equals(AndroidUtilsAstSep.CONSTANTS().STOCK_T__SERVICE_VEHICLE);
            if (!isToVehicle && ! items.get(items.indexOf(toBeSelected)).getUserObject().getIdStockT().equals(AndroidUtilsAstSep.CONSTANTS().STOCK_T__SERVICE_VEHICLE)) {
                buttonNext.setEnabled(false);
                buttonNext.setBackgroundColor(getResources().getColor(R.color.colorDisable));

            } else {
                buttonNext.setEnabled(true);
                buttonNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            toAutoCompleteTextView.setText(items.get(0).getName());
            toStockSiteTV.setText(items.get(0).getName());

            isToVehicle = items.get(0).getUserObject().getIdStockT().equals(AndroidUtilsAstSep.CONSTANTS().STOCK_T__SERVICE_VEHICLE);
            if (!isFromVehicle && ! items.get(0).getUserObject().getIdStockT().equals(AndroidUtilsAstSep.CONSTANTS().STOCK_T__SERVICE_VEHICLE)) {
                buttonNext.setEnabled(false);
                buttonNext.setBackgroundColor(getResources().getColor(R.color.colorDisable));
            } else {
                buttonNext.setEnabled(true);
                buttonNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }

            buttonNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Code here executes on main thread after user presses button
                    nextButtonClicked(v);
                }
            });

            fromAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    fromDownArrowIV.setImageResource(R.drawable.ic_arrow_down);
                   fromAutoCompleteTextView.setText(parent.getItemAtPosition(position).toString());
                    fromStockSiteTV.setText(parent.getItemAtPosition(position).toString());
                    isFromVehicle = items.get(items.indexOf(parent.getItemAtPosition(position))).getUserObject().getIdStockT().equals(AndroidUtilsAstSep.CONSTANTS().STOCK_T__SERVICE_VEHICLE);
                    if (!isToVehicle && ! items.get(items.indexOf(parent.getItemAtPosition(position))).getUserObject().getIdStockT().equals(AndroidUtilsAstSep.CONSTANTS().STOCK_T__SERVICE_VEHICLE)) {
                        buttonNext.setEnabled(false);
                        buttonNext.setBackgroundColor(getResources().getColor(R.color.colorDisable));

                    } else {
                        buttonNext.setEnabled(true);
                        buttonNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                }
            });

            fromAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AutoCompleteTextView)v).showDropDown();
                    fromDownArrowIV.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
            });

            toAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AutoCompleteTextView)v).showDropDown();
                    toDownArrowIV.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
            });

            toAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    toDownArrowIV.setImageResource(R.drawable.ic_arrow_down);
                    toAutoCompleteTextView.setText(parent.getItemAtPosition(position).toString());
                    toStockSiteTV.setText(parent.getItemAtPosition(position).toString());
                    isToVehicle = items.get(items.indexOf(parent.getItemAtPosition(position))).getUserObject().getIdStockT().equals(AndroidUtilsAstSep.CONSTANTS().STOCK_T__SERVICE_VEHICLE);
                    if (!isFromVehicle && ! items.get(items.indexOf(parent.getItemAtPosition(position))).getUserObject().getIdStockT().equals(AndroidUtilsAstSep.CONSTANTS().STOCK_T__SERVICE_VEHICLE)) {
                        buttonNext.setEnabled(false);
                        buttonNext.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                    } else {
                        buttonNext.setEnabled(true);
                        buttonNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                }
            });
        }catch(Exception e)
        {
            writeLog(TAG + " :onCreate()", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        buttonNext = (Button) findViewById(R.id.nextButton);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                nextButtonClicked(v);
            }
        });
    }

    private void nextButtonClicked(View v) {
        /* Validation before sending request to web service */
        try{
            DisplayItem<StockTO> fromStockTODisplayItem = (DisplayItem<StockTO>)fromAutoCompleteTextView.getAdapter().getItem(0);
            DisplayItem<StockTO> toStockTODisplayItem = (DisplayItem<StockTO>)toAutoCompleteTextView.getAdapter().getItem(0);
            Intent goToIntent = new Intent(getApplicationContext(), ActivityFactory.getInstance().getActivityClass(ActivityConstants.GOODS_RECEPTION_ACTIVITY));
            if(Utils.isNotEmpty(toStockTODisplayItem) && Utils.isNotEmpty(fromStockTODisplayItem)) {
                goToIntent.putExtra(GoodsReceptionActivity.TO_ID_STOCK, toStockTODisplayItem.getUserObject().getId());
                goToIntent.putExtra(GoodsReceptionActivity.FROM_ID_STOCK, fromStockTODisplayItem.getUserObject().getId());
                startActivity(goToIntent);
            }else{
                buttonNext.setEnabled(false);
                buttonNext.setBackgroundColor(getResources().getColor(R.color.colorDisable));

            }
        }catch(Exception e) {
            writeLog(TAG +" :nextButtonClicked()", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            Intent goToIntent = new Intent(getApplicationContext(), ActivityFactory.getInstance().getActivityClass(ActivityConstants.MATERIAL_LOGISTICS_ACTIVITY));
            goToIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goToIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

