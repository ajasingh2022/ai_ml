package com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.MaterialLogisticsUtils;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.to.BasicDataTO;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.device_info.DeviceInfoShowActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTTO;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaterialControlSelectReasonActivity extends AppCompatActivity {

	private String strUnitId;
	private String strTitle;
	private TextView unitId;
	private UnitInformationTO unitInformationTO;
	private BasicDataTO actionInfo;
	private ImageButton unitInfo;
	private final transient String TAG = getClass().getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_material_control_select_reason);

		strTitle = getIntent().getExtras().getString("title");
		unitInformationTO = (UnitInformationTO) getIntent().getExtras().get("UnitInformationTO");
		strUnitId = getIntent().getExtras().getString("UnitId");
		actionInfo = (BasicDataTO)getIntent().getExtras().get("ActionInfo");

		unitId = findViewById(R.id.unitIdTextview);
		unitId.setText(ObjectCache.getTypeName(UnitIdentifierTTO.class,
				SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER)) + ": " + strUnitId);

		unitInfo = findViewById(R.id.info_btn);
		unitInfo.setVisibility(View.VISIBLE);

		TextView headerText = findViewById(R.id.title_text);
		if (strTitle != null)
			headerText.setText(strTitle);
		TextView palletCodeTextView = findViewById(R.id.palletCodeTextView);
		ImageButton selectPalletBtn = findViewById(R.id.select_pallet_btn);
		/*if(!unitInformationTO.getPalletCode().isEmpty())
		{
			palletCodeTextView.setVisibility(View.VISIBLE);
			palletCodeTextView.setText(getString(R.string.pallet_code)+": "+unitInformationTO.getPalletCode());
		}
		else
		{*/
			palletCodeTextView.setVisibility(View.GONE);

		//}
		selectPalletBtn.setVisibility(View.GONE);
		fillReasonSpinner();
	}

	private void fillReasonSpinner() {
		Spinner reasonSpinner = findViewById(R.id.reasonSpinner);
		List<BasicDataTO> reasonList = null;
		List<UnitStatusTypeInformationType> unitStatusTypeInformationTypes = MaterialLogisticsUtils.createUnitStatusTypeInformationTypes();
		for (UnitStatusTypeInformationType unitStatusTypeInformationType : unitStatusTypeInformationTypes) {
			if(unitStatusTypeInformationType.getUnitStatusType().equals(actionInfo)) {
				reasonList = Arrays.asList(unitStatusTypeInformationType.getAvailableUnitStatusReasonTypes());
				break;
			}
		}
		MaterialControlReasonAdapter spinnerArrayAdapter = new MaterialControlReasonAdapter(this, android.R.layout.simple_spinner_item, reasonList);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		reasonSpinner.setAdapter(spinnerArrayAdapter);
	}

	public void infoButtonClicked(View view) {
		Log.d(TAG, "=header unitinfo==button clicked===");
		Intent intent = new Intent(this, ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.DEVICE_INFO_SHOW_ACTIVITY));
		intent.putExtra("UnitInformationTO", unitInformationTO);
		startActivity(intent);
	}

	private void getInput() {
		Spinner reasonSpinner = findViewById(R.id.reasonSpinner);

		Long selectedItemId = reasonSpinner.getAdapter().getItemId(reasonSpinner.getSelectedItemPosition());
		if (selectedItemId == -1) {
			Toast.makeText(this, this.getString(R.string.error_reason_is_missing), Toast.LENGTH_SHORT).show();
		} else {
			UnitStatusReasonTTO unitStatusReasonTTO = ObjectCache.getType(UnitStatusReasonTTO.class, selectedItemId);
			Log.d(TAG,"SELECTED ITEM ID ::" + selectedItemId);
			Log.d(TAG, "SELECTED REASON IS ::"+unitStatusReasonTTO.getName());
			List<UnitStatusReasonTTO> listStatusReason = new ArrayList<UnitStatusReasonTTO>();
			listStatusReason.add(unitStatusReasonTTO);
			unitInformationTO.setUnitStatusReasonTTOs(listStatusReason);
		}

	}

	public void saveButtonClicked(View view) {
		Log.d(TAG, "=save unitinfo==button clicked===");
		getInput();
		new RegisterMaterialControlUnitSaveThread(this, strUnitId, unitInformationTO, actionInfo).start();
		//SESPPreferenceUtil.savePreference(ConstantsAstSep.StockhandlingConstants.MATERIAL_CONTROL_PALLET_CODE, "");
		Log.d(TAG, "SAVE BUTTON IS CLICKED");
	}

	private class MaterialControlReasonAdapter extends ArrayAdapter {
		List<BasicDataTO> basicDataTOs;

		public MaterialControlReasonAdapter(Context context, int textViewResourceId, List<BasicDataTO> basicDataTOs) {
			super(context, textViewResourceId);
			this.basicDataTOs = basicDataTOs;
		}

		public int getCount() {
			return this.basicDataTOs.size();
		}

		public String getItem(int position) {
			return this.basicDataTOs.get(position).getName();
		}

		public long getItemId(int position) {
			BasicDataTO userObject = this.basicDataTOs.get(position);
			return userObject.getId();
		}

		public void remove(BasicDataTO obj) {
			this.basicDataTOs.remove(obj);
		}

	}

}
