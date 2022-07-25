package com.capgemini.sesp.ast.android.ui.activity.meter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.StringUtil;
import com.capgemini.sesp.ast.android.module.util.to.CommunicationResultTO;

import java.util.Hashtable;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by aditam on 3/17/2017.
 */
public abstract class AbstractMeterCommunicationTestActivity extends AppCompatActivity implements View.OnClickListener {

    protected Hashtable<String, String> listOfCaseIds = new Hashtable();

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
    protected transient boolean validMep = false;
    protected transient boolean validGiai = false;
    protected int INST_SCAN_REQUEST = 1001;
    protected int GIAI_SCAN_REQUEST = 1002;
    static String TAG = AbstractMeterCommunicationTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_meter_communication_test);

        setCustomActionBar();
        initViews();
        setUpListeners();

    }

    protected void setCustomActionBar() {
        try {
            // Hiding the logo as requested
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            final ActionBar actionBar = getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(false);

        /*
         * Setting up custom action bar view
		 */
            final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);

            // -- Customizing the action bar ends -----

            final View localeFlag = vw.findViewById(R.id.save_btn);
            localeFlag.setVisibility(View.INVISIBLE);
            getActionBar().setCustomView(vw, layout);
            getActionBar().setDisplayShowCustomEnabled(true);

            TextView txtTitleBar = findViewById(R.id.title_text);
            txtTitleBar.setText(getString(R.string.app_name));
        } catch (Exception e) {
            writeLog(TAG + " : setCustomActionBar() ", e);
        }
    }

    /**
     * Initialize views
     */
    protected void initViews() {
        /*roundedCornerButtonDisabled = getResources().getDrawable(R.drawable.rounded_corner_button_disabled);*/
        instCodeEt = findViewById(R.id.inst_code_et);
        meterGiaiEt = findViewById(R.id.meter_giai_et);

        resultEt = findViewById(R.id.result_et);
        performTestBtn = findViewById(R.id.perform_test_btn);
        fetchDataBtn = findViewById(R.id.fetch_meter_reading_btn);
        meterReadingTable = findViewById(R.id.meter_data_table_tl);
        resultLayout = findViewById(R.id.result_ll);
        resultLayout.setVisibility(View.GONE);
        instScanButton = findViewById(R.id.inst_scanIdentifierButton);
        giaiScanButton = findViewById(R.id.giai_scanIdentifierButton);
    }

    @Override
    public void onResume() {
        super.onResume();
        resultLayout.setVisibility(View.GONE);
        enableDisableButton();
    }

    /**
     * Register listeners
     */
    protected void setUpListeners() {
        performTestBtn.setOnClickListener(this);
        fetchDataBtn.setOnClickListener(this);
        instScanButton.setOnClickListener(this);
        giaiScanButton.setOnClickListener(this);
        instCodeEt.addTextChangedListener(new EditTextWatcher(instCodeEt));
        meterGiaiEt.addTextChangedListener(new EditTextWatcher(meterGiaiEt));
    }

    /**
     * Enable or Disable buttons
     */
    public void enableDisableButton() {
        try {
            String instCode = instCodeEt.getText().toString();
            String caseId = listOfCaseIds.get(instCode);
            if (caseId != null) {
                setButtonStatus(false);
            } else {
                setButtonStatus(true);
            }
        } catch (Exception e) {
            writeLog(TAG + " : enableDisableButton() ", e);
        }
    }


    /**
     * Enable/Disable the buttons
     *
     * @param flag
     */
    public void setButtonStatus(boolean flag) {
        try {
            if (flag) {
                GuIUtil.enableDisableButton(performTestBtn, true);
                GuIUtil.enableDisableButton(fetchDataBtn, false);
            } else {
                GuIUtil.enableDisableButton(performTestBtn, false);
                GuIUtil.enableDisableButton(fetchDataBtn, true);
            }
        } catch (Exception e) {
            writeLog(TAG + " : setButtonStatus() ", e);
        }
    }

    /**
     * Click event listener
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        try {
            if (v != null) {
                if (v.getId() == R.id.inst_scanIdentifierButton) {
                    AndroidUtilsAstSep.scanBarCode(this, INST_SCAN_REQUEST);
                } else if (v.getId() == R.id.giai_scanIdentifierButton) {
                    AndroidUtilsAstSep.scanBarCode(this, GIAI_SCAN_REQUEST);
                } else if (SessionState.getInstance().isLoggedInOnline()) {
                    resultLayout.setVisibility(View.GONE);
                    resultEt.setText("");
                    if (v.getId() == R.id.perform_test_btn) {
                        v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        v.setEnabled(true);

                        if (StringUtil.isNotEmpty(instCodeEt.getText().toString()) && StringUtil.isNotEmpty(meterGiaiEt.getText().toString())) {
                            validateMepInfo();
                        } else {
                            resultEt.setText(R.string.valid_inst_giai_required);
                        }

                    } else if (v.getId() == R.id.fetch_meter_reading_btn) {
                        v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        v.setEnabled(true);
                        displayMomentaneousMeterReading();
                    }

                } else {
                    resultEt.setText(getString(R.string.offline_mode_msg));
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : onClick() ", e);
        }

    }


    /**
     * abstract method to override in BST Layer
     */
    protected abstract void displayMomentaneousMeterReading();


    /**
     * abstract method to override in BST Layer
     */
    protected abstract void validateMepInfo();


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        try {
            if (requestCode == INST_SCAN_REQUEST && resultCode == Activity.RESULT_OK) {
                instCodeEt.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
                AndroidUtilsAstSep.showHideSoftKeyBoard(this, false);
            } else if (requestCode == GIAI_SCAN_REQUEST && resultCode == Activity.RESULT_OK) {
                meterGiaiEt.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
                AndroidUtilsAstSep.showHideSoftKeyBoard(this, false);
            }
        } catch (Exception e) {
            writeLog(TAG + " : onActivityResult() ", e);
        }
    }

    private class EditTextWatcher implements TextWatcher {

        private View view;

        private EditTextWatcher(View view) {
            this.view = view;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        /**
         * after Text Changed event
         *
         * @param editable
         */
        @Override
        public void afterTextChanged(Editable editable) {
            try {
                if (view.getId() == R.id.inst_code_et) {
                    instCodeEt.setTextColor(getResources().getColor(android.R.color.black));
                } else if (view.getId() == R.id.meter_giai_et) {
                    meterGiaiEt.setTextColor(getResources().getColor(android.R.color.black));
                }
            } catch (Exception e) {
                writeLog(TAG + " : afterTextChanged() ", e);
            }
        }
    }
}
