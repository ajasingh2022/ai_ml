package com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_taking;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.MaterialLogisticsSettingsActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.UnitItem;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitTTO;
import com.skvader.rsp.cft.common.util.Utils;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class StockTakingRemoveByIdDialog extends Dialog {
	private StockTakingListActivity activityList;
	private Context context;
	
	 public StockTakingRemoveByIdDialog(Context context, StockTakingListActivity stockTakingEditActivity) {
		 super(context);
		 try{
		 this.context = context;
		 activityList = stockTakingEditActivity;
		
	     setContentView(R.layout.dialog_add_remove_id);
	     setTitle(R.string.remove);

	     EditText identifierEditText = findViewById(R.id.dialogAddRemoveIdentifierEditText);
	     identifierEditText.setHint(ObjectCache.getTypeName(UnitIdentifierTTO.class, MaterialLogisticsSettingsActivity.getIdUnitIdentifierT()));

	     /** Listeners for button clicks */
	     Button removeButton = findViewById(R.id.dialogAddRemoveButton);
	     removeButton.setText(context.getString(R.string.remove_and_close));
	     removeButton.setOnClickListener(new View.OnClickListener() {
	    	 public void onClick(View v) {
	    		 removeButtonClicked(null);
	    	 }
	     });
	     Button nextButton = findViewById(R.id.dialogNextButton);
	     nextButton.setText(context.getString(R.string.remove_more));
	     nextButton.setOnClickListener(new View.OnClickListener() {
	    	 public void onClick(View v) {
	    		 nextButtonClicked(null);
	    	 }
	     });
		 } catch (Exception e) {
			 writeLog("StockTakingRemoveByIdDialog  : StockTakingRemoveByIdDialog() ", e);
		 }
	}

	@Override
	 protected void onCreate(final Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	 }
	 
	public void removeButtonClicked(View view) {
	 	try{
		if (activityList.getItemsSize() > 0){
			UnitItem itemToRemove = getInput();
			if (itemToRemove != null) {
				if (activityList.removeItem(itemToRemove))
					dismiss();
			}
		} else {
			Toast.makeText(context, context.getString(R.string.error_list_empty), Toast.LENGTH_SHORT).show();
		}
		} catch (Exception e) {
			writeLog("StockTakingRemoveByIdDialog  : removeButtonClicked() ", e);
		}
	}

	public void nextButtonClicked(View view) {
	 	try{
		if (activityList.getItemsSize() > 0){
			UnitItem itemToRemove = getInput();
			if (itemToRemove != null) {
				if (activityList.removeItem(itemToRemove)) {
					EditText identifierEditText = findViewById(R.id.dialogAddRemoveIdentifierEditText);
					identifierEditText.setText("");
					if (activityList.getItemsSize() == 0){
						dismiss();
					}
				}
			}
		} else {
			Toast.makeText(context, context.getString(R.string.error_list_empty), Toast.LENGTH_SHORT).show();
		}
		} catch (Exception e) {
			writeLog("StockTakingRemoveByIdDialog  : nextButtonClicked() ", e);
		}
	 }

	 private UnitItem getInput() {

		 EditText identifierEditText = findViewById(R.id.dialogAddRemoveIdentifierEditText);
		 String identifierInput = identifierEditText.getText().toString().trim();
		 UnitTTO unitTTO = null;
		 UnitModelTO unitModelTO = null;

		 Long identifierLengthSetting = SESPPreferenceUtil.getIdentifierLengthNullIfOff();
		 try{
		 if (identifierInput.isEmpty()) {
			 Toast.makeText(activityList, activityList.getString(R.string.error_identifier_is_missing), Toast.LENGTH_SHORT).show();
			 return null;
		 } else if (identifierLengthSetting != null && identifierInput.length() < identifierLengthSetting.intValue()) {
			 Toast.makeText(activityList, activityList.getString(R.string.error_identifier_is_too_short), Toast.LENGTH_SHORT).show();
			 return null;
		 } else if (identifierLengthSetting != null && identifierInput.length() > identifierLengthSetting.intValue()) {
			 Toast.makeText(activityList, activityList.getString(R.string.error_identifier_is_too_long), Toast.LENGTH_SHORT).show();
			 return null;
		 }

		 Spinner unitTypeSpinner = findViewById(R.id.unitTypeSpinner);
		 DisplayItem displayItemUnitType = (DisplayItem) unitTypeSpinner.getSelectedItem();
		 if(Utils.isNotEmpty(displayItemUnitType) && displayItemUnitType.getUserObject().getId()!= -1L) {
			 if (displayItemUnitType.equals(Spinner.INVALID_ROW_ID)) {
				 return null;
			 }
			 unitTTO = (UnitTTO) displayItemUnitType.getUserObject();
		 }

		 Spinner unitModelSpinner = findViewById(R.id.unitModelSpinner);
		 DisplayItem displayItemUnitModel = (DisplayItem) unitModelSpinner.getSelectedItem();
		 if(Utils.isNotEmpty(displayItemUnitModel) && displayItemUnitModel.getUserObject().getId()!= -1L) {
			 if (displayItemUnitModel.equals(Spinner.INVALID_ROW_ID)) {
				 return null;
			 }
			 unitModelTO = (UnitModelTO) displayItemUnitModel.getUserObject();
		 }
		} catch (Exception e) {
			writeLog("StockTakingRemoveByIdDialog  : getInput() ", e);
		}
		 return new UnitItem(identifierInput,Utils.isNotEmpty(unitTTO) ? unitTTO.getId() : null,Utils.isNotEmpty(unitModelTO) ? unitModelTO.getId() : null,SESPPreferenceUtil.getPreference(ConstantsAstSep.SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER));

	 }
}
