package com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.CustomFragment;
import com.google.gson.Gson;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.drozdzynski.library.steppers.SteppersView;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


@SuppressLint("ValidFragment")
public class ScanMismatchedValueFragment extends CustomFragment implements View.OnClickListener {

    private ImageButton mismatchUnitIdBtn;
    private ImageButton leftBtn;
    private ImageButton rightBtn;
    private TextView mismatchValueTxtv;
    private EditText mismatchUnitId;
    private int count = 0;
    private int diff;
    private boolean modifyFlag = false;
    public SteppersView.Config steppersViewConfig;
    private ArrayMap<String, String> scanMismatchArray = new ArrayMap<String, String>();
    protected static final String MISMATCH_VALUE_LIST = "MISMATCH_VALUE_LIST";
    protected static final String DIFFERENCE = "DIFFERENCE";
    Gson gson = new Gson();
    private static final int SOLAR_SCAN_REQUEST = 107;

    @SuppressLint("ValidFragment")
    public ScanMismatchedValueFragment(String stepId) {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        try {
            diff = getActivity().getIntent().getIntExtra(ConstantsAstSep.FlowPageConstants.SCAN_MISMATCH_UNIT_ID, 0);
            count = 1;
            fragmentView = inflater.inflate(R.layout.scan_mismatchedvalue_list, container, false);
            initializeUI();
            initViews();
            populateData();


            try {
                Type type = scanMismatchArray.getClass();
                scanMismatchArray = gson.fromJson(String.valueOf(stepfragmentValueArray.get(MISMATCH_VALUE_LIST)), type);
                if (scanMismatchArray == null) {
                    scanMismatchArray = new ArrayMap<>();
                }
                if (scanMismatchArray.size() != diff) {
                    scanMismatchArray.clear();
                    mismatchUnitId.setText("");
                    stepfragmentValueArray.put(MISMATCH_VALUE_LIST, gson.toJson(scanMismatchArray));
                } else {
                    mismatchUnitId.setText(scanMismatchArray.get("0") == null ? ""
                            : scanMismatchArray.get(String.valueOf("0")));
                }
            } catch (Exception e) {
                writeLog("ScanMismatchedValueFragment : onCreateView()", e);
            }

        } catch (Exception e) {
            writeLog("ScanMismatchedValueFragment : onCreateView()", e);
        }
        return fragmentView;

    }

    /**
     * Validate User input
     *
     * @return
     */
    @Override
    public boolean validateUserInput() {

        if (!AbstractWokOrderActivity.resuming) {
            String temp = mismatchUnitId.getText().toString();
            scanMismatchArray.put(String.valueOf(count - 1), temp);
            stepfragmentValueArray.put(MISMATCH_VALUE_LIST, gson.toJson(scanMismatchArray));
        }
        scanMismatchArray.size();
        // Commenting Because there is no Units Mapped in webservice and no clear user stories.
        /*if(stepfragmentValueArray.size() == scanMismatchArray.size()){
       // if (Utils.isNotEmpty(stepfragmentValueArray.get(MISMATCH_VALUE_LIST))) {
            applyChangesToModifiableWO();
            return true;
        }*/
        return true;
    }

    public void applyChangesToModifiableWO() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            ArrayList<String> valueList = new ArrayList<String>();
            List<WoUnitCustomTO> woUnitCustomTOs = new ArrayList<WoUnitCustomTO>();
            /*if(Utils.isEmpty(scanMismatchArray))
            {
                scanMismatchArray = (ArrayMap<String, String>) stepfragmentValueArray.get(MISMATCH_VALUE_LIST);
            }*/

            for (Map.Entry<String, String> entry : scanMismatchArray.entrySet()) {
                WoUnitCustomTO woUnitCustomTO = new WoUnitCustomTO();
                woUnitCustomTO.setIdCase(wo.getIdCase());
                woUnitCustomTO.setGiai(entry.getValue());
                woUnitCustomTO.setPropertyNumberD(entry.getValue());
                woUnitCustomTO.setPropertyNumberV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woUnitCustomTO.setSerialNumber(entry.getValue());
                woUnitCustomTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
                woUnitCustomTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woUnitCustomTO.setIdWoUnitT(CONSTANTS().WO_UNIT_T__SOLAR_PANEL);
                woUnitCustomTO.setIdUnitIdentifierT(CONSTANTS().UNIT_IDENTIFIER_T__GIAI);
                woUnitCustomTO.setUnitMountedTimestamp(new Date());
                woUnitCustomTO.setUnitModel("SOLAR");

                woUnitCustomTOs.add(woUnitCustomTO);

            }

            wo.getWorkorderCustomTO().setWoUnits(woUnitCustomTOs);
        } catch (Exception e) {
            writeLog("ScanMismatchedValueFragment : applyChangesToModifiableWO()", e);
        }

    }

    /**
     * Initialize UI views
     */
    protected void initViews() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            //If the device does not have built-in camera, cam scanner button will be hidden.
            if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                mismatchUnitIdBtn = fragmentView.findViewById(R.id.mismatchUnitIdBtn);
                mismatchUnitIdBtn.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            writeLog("ScanMismatchedValueFragment : initViews()", e);
        }
    }


    private void populateData() {
        try {
            String numerator = Integer.toString(count);
            int denominator = diff;
            mismatchValueTxtv.setText(numerator + "/" + denominator);

            mismatchUnitId.setText(scanMismatchArray.get(count - 1) == null ? ""
                    : scanMismatchArray.get(String.valueOf(count - 1)));

        } catch (Exception e) {
            writeLog("ScanMismatchedValueFragment : populateData()", e);
        }
    }

    protected void initializeUI() {
        try {
            final View parentView = fragmentView;
            if (parentView != null) {
                mismatchUnitId = parentView.findViewById(R.id.mismatchUnitIdEt);
                mismatchUnitIdBtn = fragmentView.findViewById(R.id.mismatchUnitIdBtn);
                mismatchUnitIdBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getParentFragment();
                        AndroidUtilsAstSep.scanBarCode(ScanMismatchedValueFragment.this, SOLAR_SCAN_REQUEST);
                    }
                });

                leftBtn = parentView.findViewById(R.id.leftArrow);
                rightBtn = parentView.findViewById(R.id.rightArrow);
                mismatchValueTxtv = parentView.findViewById(R.id.mismatchValueTxtv);

                mismatchValueTxtv.setText(String.valueOf(diff));
                enableNavigator();
                leftBtn.setOnClickListener(this);
                rightBtn.setOnClickListener(this);

            }
        } catch (Exception e) {
            writeLog("ScanMismatchedValueFragment : initializeUI()", e);
        }
    }

    private void enableNavigator() {
        try {
            populateData();

            if (diff == 1) {
                rightBtn.setVisibility(View.INVISIBLE);
                leftBtn.setVisibility(View.INVISIBLE);
            } else if (count == diff) {
                rightBtn.setVisibility(View.INVISIBLE);
                leftBtn.setVisibility(View.VISIBLE);
            } else if (count == 1) {
                leftBtn.setVisibility(View.INVISIBLE);
                rightBtn.setVisibility(View.VISIBLE);
            } else {
                leftBtn.setVisibility(View.VISIBLE);
                rightBtn.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            writeLog(TAG + " ScanMismatchedValueFragment : enableNavigator() ", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        if (requestCode == SOLAR_SCAN_REQUEST
                && resultCode == Activity.RESULT_OK) {
            mismatchUnitId.setText(imageData.getStringExtra("barcode_result"));
            AndroidUtilsAstSep.showHideSoftKeyBoard(getActivity(), false);
        }
    }

    @Override
    public void onClick(View v) {
        try {

            if (v != null) {
                String temp = mismatchUnitId.getText().toString();
                scanMismatchArray.put(String.valueOf(count - 1), temp);

                int i = v.getId();
                if (i == R.id.leftArrow) {
                    count--;
                } else {
                    if (i == R.id.rightArrow)
                        count++;
                }
                enableNavigator();
                mismatchUnitId.setText(scanMismatchArray.get(String.valueOf(count - 1)) == null ? ""
                        : scanMismatchArray.get(String.valueOf(count - 1)));

            } else if (v != null && v.getId() == R.id.mismatchUnitIdBtn) {
                AndroidUtilsAstSep.scanBarCode(this, SOLAR_SCAN_REQUEST);
            }
        } catch (Exception e) {
            writeLog(TAG + " ScanMismatchedValueFragment : onClick() ", e);
        }
    }
}







