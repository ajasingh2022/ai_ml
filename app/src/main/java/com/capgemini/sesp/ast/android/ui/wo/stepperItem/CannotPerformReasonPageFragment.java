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
import android.widget.ListView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AbortUtil;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.PdaWoResultConfigTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
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
public class CannotPerformReasonPageFragment extends CustomFragment implements View.OnClickListener {

    protected transient ListView mReasonLv = null;
    protected transient TextView mWorkorderNote = null;
    protected transient TextView notPossibleTitleTextView;
    protected transient Dialog mDialog = null;
    protected CannotBePreformedAdapter mReasonAdapter;
     static String TAG = CannotPerformReasonPageFragment.class.getSimpleName();
    protected transient String question=null;
    protected transient Long caseTypeId;
    protected transient String choosenReason;
    protected AbortUtil abortUtil = new AbortUtil();
    protected transient List<WoEventResultReasonTTO> eventResultReasons = new ArrayList<WoEventResultReasonTTO>();
    protected transient Set<Long> selectedReason = new HashSet<Long>();
    protected Long pageType;

    public CannotPerformReasonPageFragment(String stepId, Long pdaPageType) {
        super(stepId);
        pageType = pdaPageType;

    }

    public CannotPerformReasonPageFragment(String stepId, Long pdaPageType, String question) {
        super(stepId);
        pageType = pdaPageType;
        this.question=question;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.cannot_perform_activity_fragment, null);
        initializeUI();
        initializePageValues();
        populateData();

        return fragmentView;
    }

    public void initializePageValues() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            caseTypeId = wo.getIdCaseType();
            if(!question.isEmpty()) {
                notPossibleTitleTextView.setText(question);
            }
            if (stepfragmentValueArray != null) {
                choosenReason = (String) stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.CHOSEN_REASON);
            }
            if (choosenReason != null) {
                String[] npReasonCode = choosenReason.split(";");
                if (Utils.isNotEmpty(npReasonCode)) {
                    for (String tempReasoneId : npReasonCode) {
                        selectedReason.add(Long.valueOf(tempReasoneId));
                    }
                }
            } else {
                selectedReason.clear();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : initializePageValues() ", e);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("CannotPerformReasonPageFragment: ", "Inside onResume() If statement");


    }

    @Override
    public void applyChangesToModifiableWO() {
        //undoChanges();
        try {
            getUserChoiceValuesForPage();
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (Utils.isNotEmpty(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SAVED_NOTE))) {
                final WoInfoCustomTO woInfoCustomTO = WorkorderUtils.createInfo(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SAVED_NOTE).toString(),
                        CONSTANTS().INFO_SOURCE_T__SESP_PDA, CONSTANTS().INFO_T__RESULT_REASON_OTHER);
                if (woInfoCustomTO != null) {
                    WorkorderUtils.deleteAndAddWoInfoCustomMTO(wo, woInfoCustomTO);
                }
            }

            for (Long selReason : selectedReason) {
                WorkorderUtils.addRemoveResultReasons(selReason, null, wo);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }

    }

    public void undoChanges() {
        final WorkorderCustomWrapperTO workorder;
        try {
            workorder = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            workorder.getWorkorderCustomTO().setInfoList((List<WoInfoCustomTO>) abortUtil.getRestoreTOs(WoInfoCustomTO.class));
            workorder.getWorkorderCustomTO().setWoEvents((List<WoEventCustomTO>) abortUtil.getRestoreTOs(WoEventCustomTO.class));
        } catch (Exception e) {
            writeLog(TAG + "  : undoChanges() ", e);
        }

    }

    /**
     * INitializes the UI components
     */
    protected void initializeUI() {
        final View parentView = fragmentView;
        if (parentView != null) {
            mWorkorderNote = parentView.findViewById(R.id.workorderNote);
            if (stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SAVED_NOTE) != null)
            mWorkorderNote.setText(stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.SAVED_NOTE).toString());
            mReasonLv = parentView.findViewById(R.id.notAccessibleReasonLv);
            notPossibleTitleTextView =parentView.findViewById(R.id.not_possible_tv);
            mWorkorderNote.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.SAVED_NOTE, s.toString());
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void afterTextChanged(Editable s) {
                    stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.SAVED_NOTE, s.toString());

                }
            });
        }
    }

    /**
     * Populate list data to adapters
     */
    protected void populateData() {
        try {

            Log.i("CannotPerformReasonPageFragment", "Inside populate()");
            List<WoEventResultReasonTTO> woEventResultReasonTTOList = ObjectCache.getAllTypes(WoEventResultReasonTTO.class);
            List<PdaWoResultConfigTCTO> pdaWoResultConfigTCTOList = ObjectCache.getAllTypes(PdaWoResultConfigTCTO.class);
            Log.i("CannotPerformReasonPageFragment : ", "Indide Populate : woEventResultReasonTTOList-> " + woEventResultReasonTTOList);
            Log.i("CannotPerformReasonPageFragment : ", " Indide Populate : pdaWoResultConfigTCTOList-> " + pdaWoResultConfigTCTOList);

            eventResultReasons = TypeDataUtil.getResultReasonType(caseTypeId, pageType, pdaWoResultConfigTCTOList, woEventResultReasonTTOList);
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
                Log.i("CannotPerformReasonPageFragment", "Inside populate() inside if--> setAdapter()");
                mReasonAdapter = new CannotBePreformedAdapter(getActivity(), eventResultReasons, selectedReason);
                //   mReasonAdapter = new CannotBePreformedAdapter(getActivity(), eventResultReasons);
                mReasonLv.setAdapter(mReasonAdapter);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
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
                    alertView, getString(R.string.atleast_one_option_should_be_selected), null);
            mDialog = builder.create();
            mDialog.show();
        }
    }

    public void getUserChoiceValuesForPage() {
        try {
            String rsCodes = getNotPossibleReasonCodes();
            if (Utils.isNotEmpty(rsCodes)) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.CHOSEN_REASON, rsCodes);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getUserChoiceValuesForPage() ", e);
        }

    }

    protected String getNotPossibleReasonCodes() {
        String reasonCodes = null;
        try {
            if (selectedReason != null) {
                StringBuilder reasonCodeBuilder = new StringBuilder();
                for (Long selreason : selectedReason) {
                    reasonCodeBuilder.append(String.valueOf(selreason.longValue()));
                    reasonCodeBuilder.append(";");
                }
                reasonCodes = reasonCodeBuilder.toString();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getUserChoiceValuesForPage() ", e);
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
}

class CannotBePreformedAdapter extends BaseAdapter {

    protected final Context context;
    protected List<WoEventResultReasonTTO> valueList = null;
    protected Set<Long> selectedValues = null;
    protected transient Set<Long> unselectedReasonIds;

    public CannotBePreformedAdapter(Context context, List<WoEventResultReasonTTO> objects, Set<Long> selectedValues) {
        super();
        this.context = context;
        this.valueList = objects;
        this.selectedValues = selectedValues;
        unselectedReasonIds = new HashSet<Long>();
        for (WoEventResultReasonTTO woEventResultReasonTTO : valueList) {
            unselectedReasonIds.add(woEventResultReasonTTO.getId());
        }
        if (Utils.isNotEmpty(selectedValues)) {
            for (Long selectedId : selectedValues) {
                unselectedReasonIds.remove(selectedId);
            }
        }

    }

    protected class ViewHolder {
        TextView text;
        CheckBox checkbox;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (this.valueList != null) {
            count = this.valueList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object obj = null;
        if (this.valueList != null && this.valueList.size() > position) {
            obj = this.valueList.get(position);
        }
        return obj;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        final WoEventResultReasonTTO listItem = valueList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_cannot_be_preformed_row, parent, false);
        holder.text = rowView.findViewById(R.id.activityCannotBePerformedTextview);
        holder.checkbox = rowView.findViewById(R.id.activityCannotBePerformedCheckbox);
        holder.text.setText(listItem.getName());
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!holder.checkbox.isChecked()) {
                    if (isReasonSelected(selectedValues, listItem.getId())) {
                        selectedValues.remove(listItem.getId());
                        unselectedReasonIds.add(listItem.getId());
                    }
                }
                if (holder.checkbox.isChecked()) {
                    if (!isReasonSelected(selectedValues, listItem.getId())) {
                        selectedValues.add(listItem.getId());
                        unselectedReasonIds.remove(listItem.getId());
                    }
                }
            }
        });
        if (isReasonSelected(selectedValues, listItem.getId())) {
            holder.checkbox.setChecked(true);
        }
        return rowView;
    }


    protected boolean isReasonSelected(Set<Long> selectedReasons, Long reasonCode) {
        boolean selected = false;
        for (Long reason : selectedReasons) {
            if (reason.longValue() == reasonCode.longValue()) {
                selected = true;
                break;
            }
        }
        return selected;
    }

    /**
     * this will return whether the user has selected any reason
     *
     * @return
     */
    public boolean isReasonChecked() {
        return selectedValues != null && !selectedValues.isEmpty();
    }

}