package com.capgemini.sesp.ast.android.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import androidx.fragment.app.Fragment;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.to.CommunicationResultTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.Hashtable;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public abstract class AbstractCommunicationTestFragment extends Fragment implements View.OnClickListener {

	protected WorkorderCustomWrapperTO wo;
	protected transient CommunicationResultTO communicationResultTO = null;
	protected EditText instCodeEt;
	protected EditText meterGiaiEt;
	protected EditText resultEt;
	protected Button performTestBtn;
	protected Button fetchDataBtn;
	protected TableLayout meterReadingTable;
	protected LinearLayout resultLayout;
	protected transient Drawable roundedCornerButtonDisabled = null;
	protected ImageButton instScanButton;
	protected ImageButton giaiScanButton;


	public static Hashtable<String, String> listOfCaseIds = new Hashtable();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_meter_communication_test, null);

		initViews(root);
		setUpListeners();
		setWorkorderFields(root, getWorkorder());
		return root;
	}


	@Override
	public void onResume() {
		super.onResume();
		enableDisableButton();
	}

	/**
	 * Initialize the UI widgets
	 */
	protected void initViews(ViewGroup root) {
		/*roundedCornerButtonDisabled = getResources().getDrawable(R.drawable.rounded_corner_button_disabled);*/
		instCodeEt = root.findViewById(R.id.inst_code_et);
		meterGiaiEt = root.findViewById(R.id.meter_giai_et);

		resultEt = root.findViewById(R.id.result_et);
		performTestBtn = root.findViewById(R.id.perform_test_btn);
		fetchDataBtn = root.findViewById(R.id.fetch_meter_reading_btn);
		meterReadingTable = root.findViewById(R.id.meter_data_table_tl);
		resultLayout = root.findViewById(R.id.result_ll);
		instScanButton = root.findViewById(R.id.inst_scanIdentifierButton);
		giaiScanButton = root.findViewById(R.id.giai_scanIdentifierButton);
		resultLayout.setVisibility(View.GONE);
		instScanButton.setVisibility(View.GONE);
		giaiScanButton.setVisibility(View.GONE);
		instCodeEt.setEnabled(false);
		meterGiaiEt.setEnabled(false);
	}

	public void enableDisableButton() {
		try {
			String mepCode = getWorkorder().getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().getMeasurepointCode();
			String caseId = listOfCaseIds.get(mepCode);
			if (caseId != null) {
			setButtonStatus(false);
		} else {
			setButtonStatus(true);
		}
		}catch(Exception e) {
			writeLog("AbstractCommunicationTestFragment :enableDisableButton()", e);
		}

	}

	/**
	 * Enable/Disable the buttons
	 * @param flag
	 */
	public void setButtonStatus(boolean flag) {
		if(flag){
			GuIUtil.enableDisableButton(performTestBtn, true);
			GuIUtil.enableDisableButton(fetchDataBtn, false);
		} else {
			GuIUtil.enableDisableButton(performTestBtn, false);
			GuIUtil.enableDisableButton(fetchDataBtn, true);
		}
	}

	protected void setWorkorderFields(ViewGroup root, WorkorderCustomWrapperTO wo) {

	}

	/**
	 * Register listeners
	 */
	protected void setUpListeners() {
		performTestBtn.setOnClickListener(this);
		fetchDataBtn.setOnClickListener(this);
	}

	/**
	 * Click event listener
	 *
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		if(SessionState.getInstance().isLoggedInOnline()) {
			resultLayout.setVisibility(View.GONE);
			resultEt.setText("");
			if (v != null) {
				if (v.getId() == R.id.perform_test_btn) {

					v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
					v.setEnabled(true);
					performCommunicationTest();
				} else if (v.getId() == R.id.fetch_meter_reading_btn) {
					v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
					v.setEnabled(true);
					displayMomentaneousMeterReading();
				}
			}
		} else {
			resultEt.setText(getString(R.string.offline_mode_msg));
		}
	}


	/**
	 * abstract method to override in BST Layer
	 */
	protected  abstract void displayMomentaneousMeterReading();


	/**
	 * abstract method to override in BST Layer
	 */
	protected  abstract void performCommunicationTest();


	public void setWorkorder(WorkorderCustomWrapperTO wo){
		this.wo = wo;
	}

	public WorkorderCustomWrapperTO getWorkorder() {
		return this.wo;
	}


}