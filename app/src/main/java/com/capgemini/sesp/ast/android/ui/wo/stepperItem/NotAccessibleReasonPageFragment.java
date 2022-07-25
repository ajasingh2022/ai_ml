package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.PdaWoResultConfigTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint("ValidFragment")
public class NotAccessibleReasonPageFragment extends CustomFragment implements View.OnClickListener {

    protected transient ListView mReasonLv = null;
    protected transient TextView mWorkorderNote = null;
    protected transient TextView mScreenHeader = null;
    protected transient LinearLayout workOrderNoteLayout = null;
    private String TAG = NotAccessibleReasonPageFragment.class.getSimpleName();

    protected transient Dialog mDialog = null;
    protected transient ReasonAdapter mReasonAdapter;
    protected transient Long caseTypeId;
    protected transient String choosenReason;
    protected transient String note;
    protected transient String noteAtStart;
    protected transient List<WoEventResultReasonTTO> eventResultReasons = new ArrayList<WoEventResultReasonTTO>();
    protected static transient long selectedReason = -1;
    protected transient long selectedReasonAtStart;
    protected static transient long unselectedReason = -1;
    protected static transient Set<Long> unselectedReasonIds = null;
    protected static transient View parentView = null;
    protected static transient Long pdaPageType;

    public NotAccessibleReasonPageFragment(String stepId, Long pdaPageType) {
        super(stepId);
        NotAccessibleReasonPageFragment.pdaPageType = pdaPageType;
    }


    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_not_accessible_reason, null);
        initializePageValues();
        initializeUI();
        populateData();
        return fragmentView;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void initializePageValues() {
        try{

        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        caseTypeId = wo.getIdCaseType();

        choosenReason = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.CHOSEN_REASON);
        note = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SAVED_NOTE);

        if (choosenReason != null && !"".equals(choosenReason.trim())) {
            selectedReason = Long.valueOf(choosenReason);
        } else {
            selectedReason = -1;
        }
        unselectedReason = -1;
        selectedReasonAtStart = selectedReason;
        noteAtStart = note;
        } catch (Exception e) {
            writeLog(TAG + "  : initializePageValues() ", e);
        }
    }

    /**
     * Initialize UI widgets
     */
    protected void initializeUI() {
        try{
        parentView = fragmentView;
        if (parentView != null) {
            mWorkorderNote = parentView.findViewById(R.id.workorderNote);
            mReasonLv = parentView.findViewById(R.id.notAccessibleReasonLv);
            mScreenHeader = parentView.findViewById(R.id.screenHeader);
            workOrderNoteLayout = parentView.findViewById(R.id.work_order_note_layout);
            mWorkorderNote.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void afterTextChanged(Editable s) {
                    if (mWorkorderNote.getText() != null) {
                        note = mWorkorderNote.getText().toString();
                    }
                }
            });
            mScreenHeader.setText(getResources().getString(R.string.enter_the_reason_for_not_accessibility));

        }
        } catch (Exception e) {
            writeLog(TAG + "  : initializeUI() ", e);
        }
    }

    public void saveUserChoiceValuesForPage() {
        try{
        if (note != null) {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.SAVED_NOTE, note);
        }
        String rsCodes = getNotAccessibleReasonCodes();
        if (Utils.isNotEmpty(rsCodes)) {
            stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.CHOSEN_REASON, rsCodes);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : saveUserChoiceValuesForPage() ", e);
        }
    }

    /**
     * Populate UI widget with initial data
     */
    protected void populateData() {
        try {
            List<WoEventResultReasonTTO> woEventResultReasonTTOList = ObjectCache.getAllTypes(WoEventResultReasonTTO.class);
            Log.d(this.getClass().getSimpleName(), "SIZE OF woEventResultReasonTTOList = " + woEventResultReasonTTOList.size());
            List<PdaWoResultConfigTCTO> pdaWoResultConfigTCTOList = ObjectCache.getAllTypes(PdaWoResultConfigTCTO.class);
            Log.d(this.getClass().getSimpleName(), "SIZE OF pdaWoResultConfigTCTOList = " + pdaWoResultConfigTCTOList.size());
            eventResultReasons = TypeDataUtil.getResultReasonType(caseTypeId, pdaPageType, pdaWoResultConfigTCTOList, woEventResultReasonTTOList);
            mReasonLv.setOnTouchListener(new ListView.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });
            if (Utils.isNotEmpty(eventResultReasons)) {
                mReasonAdapter = new ReasonAdapter(getActivity(), eventResultReasons);
                mReasonLv.setAdapter(mReasonAdapter);
            }

            mWorkorderNote.setText(Utils.isNotEmpty(note) ? note : "");
        } catch (Exception e) {
            writeLog("NotAccessibleReasonPageFragment : populateData()", e);
        }
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
            mDialog = builder.create();
            mDialog.show();
        }
    }

    public void applyChangesToModifiableWO() {
        try{
        saveUserChoiceValuesForPage();
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        if (Utils.isNotEmpty(note)) {
            final WoInfoCustomTO woInfoCustomTO = WorkorderUtils.createInfo(note,
                    CONSTANTS().INFO_SOURCE_T__SESP_PDA, CONSTANTS().INFO_T__RESULT_REASON_OTHER);
            if (woInfoCustomTO != null) {
                WorkorderUtils.addWoInfoCustomMTO(wo, woInfoCustomTO);
            }
        }
        //Changes to event reason list
        if (selectedReason != -1) {
            for (WoEventResultReasonTTO woEventResultReasonTTO : eventResultReasons) {
                unselectedReasonIds.add(woEventResultReasonTTO.getId());
            }
            unselectedReasonIds.remove(selectedReason);
            WorkorderUtils.addRemoveResultReasons(selectedReason, unselectedReasonIds, wo);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }

    }

    private String getNotAccessibleReasonCodes() {
        String reasonCodes = null;
        try{
        if (selectedReason != -1) {
            reasonCodes = String.valueOf(selectedReason);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : getNotAccessibleReasonCodes() ", e);
        }
        return reasonCodes;
    }


    public boolean validateUserInput() {
        //check if any check box(Reason for not acciable) is checked else false
        if (mReasonAdapter != null && mReasonAdapter.isReasonChecked()) {

            applyChangesToModifiableWO();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null
                && v.getId() == R.id.okButtonYesNoPage
                && mDialog != null) {
            mDialog.dismiss();
        }
    }

    class ReasonAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

        private final Context context;
        private List<WoEventResultReasonTTO> valueList = null;

        public ReasonAdapter(Context context, List<WoEventResultReasonTTO> objects) {
            super();
            this.context = context;
            this.valueList = objects;
            unselectedReasonIds = new HashSet<Long>();
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView != null) {
                final int position = (Integer) buttonView.getTag();
                if (isChecked) {
                    selectedReason = getItem(position).getId();
                    unselectedReasonIds.remove(selectedReason);
                } else {
                    unselectedReason = getItem(position).getId();
                    unselectedReasonIds.add(unselectedReason);
                    if (unselectedReasonIds.size() == valueList.size()) selectedReason = -1;
                }
                notifyDataSetChanged();
            }
        }

        private class ViewHolder {
            TextView text;
            CheckBox checkbox;
        }

        @Override
        public int getCount() {
            int count = 0;
            try{
            if (this.valueList != null) {
                count = this.valueList.size();
            }
            } catch (Exception e) {
                writeLog(TAG + "  : getCount() ", e);
            }
            return count;
        }

        @Override
        public WoEventResultReasonTTO getItem(int position) {
            WoEventResultReasonTTO obj = null;
            try{
            if (this.valueList != null && this.valueList.size() > position) {
                obj = this.valueList.get(position);
            }
            } catch (Exception e) {
                writeLog(TAG + "  : getItem() ", e);
            }
            return obj;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            try{
            ViewHolder holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final WoEventResultReasonTTO listItem = valueList.get(position);
            //If holder not exist then locate all view from UI file.
            if (convertView == null) {
                // inflate UI from XML file
                convertView = inflater.inflate(R.layout.activity_cannot_be_preformed_row, parent, false);
                // get all UI view
                holder.text = convertView.findViewById(R.id.activityCannotBePerformedTextview);
                holder.checkbox = convertView.findViewById(R.id.activityCannotBePerformedCheckbox);

                // set tag for holder
                convertView.setTag(holder);
            } else {
                // if holder created, get tag from view
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(listItem.getName());
            holder.checkbox.setTag(position);
            holder.checkbox.setChecked(listItem.getId() == selectedReason);
            holder.checkbox.setOnCheckedChangeListener(this);
            } catch (Exception e) {
                writeLog(TAG + "  : getView() ", e);
            }
            return convertView;
        }

        /**
         * this will return whether the user has selected any reason
         *
         * @return
         */
        public boolean isReasonChecked() {
            return selectedReason != -1;
        }

    }

}