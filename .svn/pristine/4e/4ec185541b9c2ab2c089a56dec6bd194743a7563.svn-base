package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.GuIUtil;
import com.capgemini.sesp.ast.android.module.util.StringUtil;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.PhaseTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstElCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by svadera on 2/21/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class VerifyPhaseKeyFragment extends CustomFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    /*
     * Following widgets are present in the fragment layout
     *
     * Widgets for PHASE
     */
    protected transient Long existingPhaseValue;
    protected transient TextView existingPhaseValueTv = null;
    protected transient Spinner numOfPhasesSpinner = null;

    /*
     * Widgets for KEY INFO
     */
    protected transient TextView existingKeyInfoTv = null;
    protected transient EditText editKeyInfo = null;

    /*
     * Widgets for KEY NUMBER
     */
    protected transient TextView existingKeyNumberTv = null;
    protected transient EditText editKeyNumber = null;

    /*
     * Widgets for FEED LINE
     */
    protected transient TextView existingFeedLineTv = null;
    protected transient EditText editFeedLine = null;
    private static String TAG = VerifyPhaseKeyFragment.class.getSimpleName();
    protected transient static final String KEY_INFO = "KEY_INFO";
    protected transient static final String KEY_NUMBER = "KEY_NUMBER";
    protected transient static final String PHASE_TYPE = "PHASE_TYPE";
    protected transient static final String FEED_LINE = "FEED_LINE";
    protected transient static final String KEY_INFO_SELECTION = "KEY_INFO_SELECTION";
    protected transient static final String KEY_NUMBER_SELECTION = "KEY_NUMBER_SELECTION";
    protected transient static final String PHASE_TYPE_SELECTION = "PHASE_TYPE_SELECTION";
    protected transient static final String FEED_LINE_SELECTION = "FEED_LINE_SELECTION";
    protected transient static final String OK = "OK";
    protected transient static final String REMARK = "REMARK";

    public VerifyPhaseKeyFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    public VerifyPhaseKeyFragment() {
        super();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_phase_key_feedline_steppper, null);


        initializeWidgets(fragmentView);
        populateData();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    protected void initializeWidgets(View parentView) {
        try{
        if (parentView != null) {
            existingPhaseValueTv = (TextView) parentView.findViewById(R.id.existingPhaseValueTv);
            // Initialize widgets for PHASE
            numOfPhasesSpinner = parentView.findViewById(R.id.numOfPhasesSpinner);

            // Initialize widgets for KEY INFO
            existingKeyInfoTv = (TextView) parentView.findViewById(R.id.existingKeyInfoTv);
            editKeyInfo = parentView.findViewById(R.id.editKeyInfo);

            // Initialize widgets for KEY NUMBER
            existingKeyNumberTv = (TextView) parentView.findViewById(R.id.existingKeyNumberTv);
            editKeyNumber = parentView.findViewById(R.id.editKeyNumber);

            // Initialize widgets for FEED LINE
            existingFeedLineTv = (TextView) parentView.findViewById(R.id.existingFeedLineTv);
            editFeedLine = parentView.findViewById(R.id.editFeedLine);

        }
        } catch (Exception e) {
            writeLog(TAG + " : initializeWidgets()", e);
        }
    }

    /**
     * Populate UI Data
     */
    protected void populateData() {
        try{
        WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        Long phaseTypeOrg = null;
        String keyInfoOrg = null;
        String keyNumberOrg = null;
        Long feedLinesOrg = null;
        WoInstElCustomTO instElCustomTO = null;
        WoInstTO woInstTO = null;

        if (wo != null
                && wo.getWorkorderCustomTO() != null
                && wo.getWorkorderCustomTO().getWoInst() != null) {
            woInstTO = wo.getWorkorderCustomTO().getWoInst().getWoInstTO();
        }

        if (wo != null
                && wo.getWorkorderCustomTO() != null
                && wo.getWorkorderCustomTO().getWoInst() != null
                && wo.getWorkorderCustomTO().getWoInst().getInstElectrical() != null
                && wo.getWorkorderCustomTO().getWoInst().getInstElectrical().getMainFuse() != null) {
            instElCustomTO = wo.getWorkorderCustomTO().getWoInst().getInstElectrical();
        }

        //Number of Phases
        final List<PhaseTTO> phaseTTOList = TypeDataUtil.filterTypeDataListByDomain(ObjectCache.getAllTypes(PhaseTTO.class), WorkorderUtils.getDomainsForWO(wo));
        if (Utils.isNotEmpty(phaseTTOList)) {
            GuIUtil.setUpSpinner(getContext(), numOfPhasesSpinner, phaseTTOList, true, this);
        }
        if (instElCustomTO != null) {
            phaseTypeOrg = instElCustomTO.getWoInstElTO().getIdPhaseTO();
            if (phaseTypeOrg != null) {
                existingPhaseValueTv.setText(ObjectCache.getTypeName(PhaseTTO.class,phaseTypeOrg));
                existingPhaseValue = phaseTypeOrg;
            }
        }
        numOfPhasesSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(numOfPhasesSpinner, existingPhaseValue));

        //Key Info
        if (woInstTO != null) {
            keyInfoOrg = woInstTO.getKeyInfoO();
            if (null != keyInfoOrg) {
                existingKeyInfoTv.setText(keyInfoOrg);
                editKeyInfo.setText(keyInfoOrg);
            }
        }

        //Key Number
        if (woInstTO != null) {
            keyNumberOrg = woInstTO.getKeyNumberO();
            if (null != keyNumberOrg) {
                existingKeyNumberTv.setText(keyNumberOrg);
                editKeyNumber.setText(keyNumberOrg);
            }
        }

        //Feed Lines
        if (instElCustomTO != null) {
            feedLinesOrg = instElCustomTO.getWoInstElTO().getFeedingLinesO();
            if (feedLinesOrg != null) {
                existingFeedLineTv.setText(String.valueOf(feedLinesOrg));
                editFeedLine.setText(String.valueOf(feedLinesOrg));
            }
        }

        if (!stepfragmentValueArray.isEmpty()) {
            if (stepfragmentValueArray.get(KEY_INFO) != null) {
                editKeyInfo.setText(stepfragmentValueArray.get(KEY_INFO).toString());
            }
            if (stepfragmentValueArray.get(KEY_NUMBER) != null) {
                editKeyNumber.setText(stepfragmentValueArray.get(KEY_NUMBER).toString());
            }
            if (stepfragmentValueArray.get(FEED_LINE) != null) {
                editFeedLine.setText(stepfragmentValueArray.get(FEED_LINE).toString());
            }
            if (stepfragmentValueArray.get(PHASE_TYPE) != null) {
                numOfPhasesSpinner.setSelection(GuIUtil.getPositionOfItemInSpinner(numOfPhasesSpinner, Long.parseLong(stepfragmentValueArray.get(PHASE_TYPE).toString())));
            }

        }
        } catch (Exception e) {
            writeLog(TAG + " : populateData()", e);
        }

    }

    public void applyChangesToModifiableWO() {

        WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        try {
            //Number of Phases
            if (GuIUtil.validSpinnerSelection((String) stepfragmentValueArray.get(PHASE_TYPE))) {
                WorkorderUtils.saveWoInstElTO(
                        wo,
                        Long.valueOf((String) stepfragmentValueArray.get(PHASE_TYPE)), //Phase Type
                        null,  //idInstElNsT
                        null   //feedingLines
                );
            }

            //Key Info
            if (Utils.isNotEmpty((String) stepfragmentValueArray.get(KEY_INFO))) {
                WorkorderUtils.saveWoInstTO(wo,
                        null,// xCoordinate
                        null,// yCoordinate
                        null,// idSystemCoordSystemT
                        (String) stepfragmentValueArray.get(KEY_INFO),// keyInfo
                        null,// keyNumber
                        null// accessibleToTechnician
                );
            }

            //Key Number
            if (Utils.isNotEmpty((String) stepfragmentValueArray.get(KEY_NUMBER))) {
                WorkorderUtils.saveWoInstTO(wo,
                        null,// xCoordinate
                        null,// yCoordinate
                        null,// idSystemCoordSystemT
                        null,// keyInfo
                        (String) stepfragmentValueArray.get(KEY_NUMBER),// keyNumber
                        null// accessibleToTechnician
                );
            }

            //Feeding Line
            if (Utils.isNotEmpty((String) stepfragmentValueArray.get(FEED_LINE))) {
                WorkorderUtils.saveWoInstElTO(
                        wo,
                        null,  //Phase Type
                        null,  //idInstElNsT
                        Long.valueOf((String) stepfragmentValueArray.get(FEED_LINE))   //feedingLines
                );
            }

        } catch (Exception e) {
            writeLog(TAG + " : applyChangesToModifiableWO()", e);
        }
    }

    @Override
    public boolean validateUserInput() {
        boolean status = false;
        try {
            String value = new String();
            value = editKeyInfo.getText().toString();
            stepfragmentValueArray.put(KEY_INFO, value);
            value = editFeedLine.getText().toString();
            stepfragmentValueArray.put(FEED_LINE, value);
            value = editKeyNumber.getText().toString();
            stepfragmentValueArray.put(KEY_NUMBER, value);

            if (

                // Is number of phases selected
                    (GuIUtil.validSpinnerSelection((String) stepfragmentValueArray.get(PHASE_TYPE)))
                            // Is key info entered
                            && (Utils.isNotEmpty((String) stepfragmentValueArray.get(KEY_INFO)))
                            // Is key number entered
                            && (Utils.isNotEmpty((String) stepfragmentValueArray.get(KEY_NUMBER)))) {
                applyChangesToModifiableWO();

                status = true;
            }
        } catch (Exception e) {
            writeLog(TAG + " : afterTextChanged()", e);
        }

        return status;
    }


    @Override
    public void onClick(View v) {

    }


    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p/>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       try{
        int viewId = parent.getId();
        if (viewId == R.id.numOfPhasesSpinner) {
            stepfragmentValueArray.put(PHASE_TYPE, String.valueOf(id));

        }
       } catch (Exception e) {
           writeLog(TAG + " : onItemSelected()", e);
       }
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    class VerifyPhaseKeyFeedTextWatcher implements TextWatcher {

        private EditText editText;

        public VerifyPhaseKeyFeedTextWatcher(final EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                String text = s.toString();
                if (editText.getId() == R.id.editKeyInfo) {
                    stepfragmentValueArray.put(KEY_INFO, StringUtil.checkNullString(text));
                } else if (editText.getId() == R.id.editKeyNumber) {
                    stepfragmentValueArray.put(KEY_NUMBER, StringUtil.checkNullString(text));
                } else if (editText.getId() == R.id.editFeedLine) {
                    stepfragmentValueArray.put(FEED_LINE, StringUtil.checkNullString(text));
                }

            } catch (Exception e) {
                writeLog(TAG + " : afterTextChanged()", e);
            }
        }
    }

}
