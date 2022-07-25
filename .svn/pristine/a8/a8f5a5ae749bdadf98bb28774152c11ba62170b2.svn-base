package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class RegisterProdMeterReadingPageFragment extends CustomFragment implements View.OnClickListener {
    private transient Spinner prdmeterreadingspinner = null;
    private transient MeterReadIndicAdapter adapter = null;
    private transient Dialog dialog = null;
    transient String noOfOldMeterReading;
    private transient String noOfOldMeterReadingAtStart;
    private String TAG = RegisterProdMeterReadingPageFragment.class.getSimpleName();
    private transient SoftReference<Context> contextRef = null;
    private transient List<String> readingCounts = null;


    public RegisterProdMeterReadingPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_register_prodmeter_reading, null);
        initialize();
        populateData();
        return fragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void initialize() {
        try {
            noOfOldMeterReading = String.valueOf(stepfragmentValueArray.get("noOfOldMeterReading"));
            prdmeterreadingspinner = fragmentView.findViewById(R.id.prdmeterreadingspinner);
            adapter = new MeterReadIndicAdapter(getActivity(), getDummyList());
        } catch (Exception e) {
            writeLog(TAG + " : initialize()", e);
        }
    }

    private void populateData() {
        try {
            if (prdmeterreadingspinner != null
                    && adapter != null) {
                prdmeterreadingspinner.setAdapter(adapter);
                if (Utils.isNotEmpty(noOfOldMeterReading) && !noOfOldMeterReading.equalsIgnoreCase("null")) {
                    Log.d("Test1", "No of old readings " + noOfOldMeterReading);
                    int selectedIndex = Integer.parseInt(noOfOldMeterReading) - 1;
                    prdmeterreadingspinner.setSelection(selectedIndex);
                } else {
                    prdmeterreadingspinner.setSelection(0);
                }
                prdmeterreadingspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        noOfOldMeterReading = (String) prdmeterreadingspinner.getSelectedItem();
                        stepfragmentValueArray.put("noOfOldMeterReading", noOfOldMeterReading);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });
            }
        } catch (Exception e) {
            writeLog(TAG + " : populateData()", e);
        }
    }

    /**
     * The business logic for this page is not clarified unfortunately
     * hence this is kept just as a prototype
     *
     * @return
     */

    private List<String> getDummyList() {
        final List<String> val = new ArrayList<String>();
        //val.add("");
        try {
            for (int i = 1; i <= 10; i++) {
                val.add(String.valueOf(i));
            }
        } catch (Exception e) {
            writeLog(TAG + " : getDummyList()", e);
        }
        return val;
    }

    /**
     * User has not selected any option
     * and tries to move forward, user should be prompted
     * to select an option
     */

    public void showPromptUserAction() {
        /*
         *  If status is false then it is coming from flow engine stack rebuild
         *  and does not require user attention
         */

        final View alertView
                = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.prompt_user_response_layout, null);
        if (alertView != null) {
            final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
            okButton.setOnClickListener(this);

            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                    alertView, null, null);
            dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean validateUserInput() {
        return Utils.isNotEmpty(noOfOldMeterReading);
    }

    @Override
    public void onClick(View v) {
        if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        final String val = (String) prdmeterreadingspinner.getSelectedItem();
        if (val != null) {
            noOfOldMeterReading = val;
        }

    }
}

