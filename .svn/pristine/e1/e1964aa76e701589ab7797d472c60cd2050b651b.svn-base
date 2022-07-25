package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.WoInfoCustomUtil;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.adapter.RegisterInternalExternalNotesAdapter;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class RegisterExternalInternalCommentFragment extends CustomFragment {

    protected transient EditText commentToUtilityEditText;
    protected transient EditText commentToInternalEditText;
    protected transient TextView prevRegisteredText;
    protected transient ListView prevRegisteredListView;

    static transient String commentToUtility = "";
    static transient String commentToInternal = "";
    protected transient TextView riskObservationTextView;
    protected transient EditText riskObservationEditText;
    private String TAG = RegisterExternalInternalCommentFragment.class.getSimpleName();

    public RegisterExternalInternalCommentFragment() {
        super();

    }

    public RegisterExternalInternalCommentFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_register_external_internal_comment, null);
        initialize();
        setUpListeners();
        populateData();

        return fragmentView;
    }

    @Override
    public void onResume() {

        super.onResume();
        populateData();


    }

    /**
     * POPULATE INITIAL DATA TO THE UI VIEWS
     */
    private void populateData() {
        WorkorderCustomWrapperTO workOrder = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        try {
            commentToUtility = WoInfoCustomUtil.getInfo(workOrder, CONSTANTS().INFO_T__INFO_TO_UTILITY, true);
            commentToInternal = WoInfoCustomUtil.getInfo(workOrder, CONSTANTS().INFO_T__INFO_TO_OPERATOR, true);
            commentToUtilityEditText.setText(commentToUtility);
            commentToInternalEditText.setText(commentToInternal);
            String riskInfo = getRiskInfo();
            if(Utils.isNotEmpty(riskInfo)){
                riskObservationEditText.setVisibility(View.VISIBLE);
                riskObservationEditText.setText(riskInfo);
                riskObservationTextView.setVisibility(View.VISIBLE);
            }

            List<String> prevInfoList = getPrevInfos();
            if (prevInfoList != null && prevInfoList.size() > 0) {
                final RegisterInternalExternalNotesAdapter adapter = new RegisterInternalExternalNotesAdapter(this.getContext(), prevInfoList);
                prevRegisteredListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                prevRegisteredText.setVisibility(View.VISIBLE);
                prevRegisteredListView.setVisibility(View.VISIBLE);
            } else {
                prevRegisteredText.setVisibility(View.GONE);
                prevRegisteredListView.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }

    }
    /**
     * get Previous Note/comment/info
     * @return
     */
    private String getRiskInfo(){
        String riskInfo = null;
        List<WoInfoCustomTO> currentInfoList = AbstractWokOrderActivity.getWorkorderCustomWrapperTO().getWorkorderCustomTO().getInfoList();
        if(Utils.isNotEmpty(currentInfoList)) {
            for (WoInfoCustomTO woInfoCustomTO : currentInfoList) {
                if (woInfoCustomTO.getInfo().getIdInfoT().equals(CONSTANTS().INFO_T__INFO_RISK_OBSERVATION)) {
                    riskInfo = woInfoCustomTO.getInfo().getText().trim();
                    break;
                }
            }
        }
        return riskInfo;
    }
    /**
     * Initialise View Components
     */
    protected void initialize() {
        try {
            final View parentView = fragmentView;
            if (parentView != null) {
                prevRegisteredText = parentView.findViewById(R.id.prevRegisteredTextView);
                prevRegisteredListView = parentView.findViewById(R.id.prevRegisteredListView);
                commentToUtilityEditText = parentView.findViewById(R.id.commentToUtilityEditText);
                commentToInternalEditText = parentView.findViewById(R.id.commentToInternalEditText);
                riskObservationTextView=(TextView)parentView.findViewById(R.id.riskObservationTextView);;
                riskObservationEditText = (EditText) parentView.findViewById(R.id.riskObservationEditText);

                prevRegisteredListView.setOnTouchListener(new ListView.OnTouchListener() {
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
            }
        } catch (Exception e) {
            writeLog(TAG + "  : initialize() ", e);
        }
    }

    private void setUpListeners() {
        try {
            commentToUtilityEditText.addTextChangedListener(new CommentTextWatcher(commentToUtilityEditText));
            commentToInternalEditText.addTextChangedListener(new CommentTextWatcher(commentToInternalEditText));
        } catch (Exception e) {
            writeLog(TAG + "  : setUpListeners() ", e);
        }
    }

    List<String> getPrevInfos() {

        List<String> prevInfoList = new ArrayList<String>();
        try {
            List<WoInfoCustomTO> currentInfoList = AbstractWokOrderActivity.getWorkorderCustomWrapperTO().getWorkorderCustomTO().getInfoList();
            if (Utils.isNotEmpty(currentInfoList)) {
                for (WoInfoCustomTO woInfoCustomTO : currentInfoList) {
                    if (!woInfoCustomTO.getInfo().getIdInfoT().equals(CONSTANTS().INFO_T__INFO_TO_UTILITY) && (!woInfoCustomTO.getInfo().getIdInfoT().equals(CONSTANTS().INFO_T__INFO_TO_OPERATOR)) && (!woInfoCustomTO.getInfo().getIdInfoT().equals(CONSTANTS().INFO_T__INFO_TO_TECHNICIAN))
                            && (!woInfoCustomTO.getInfo().getIdInfoT().equals(CONSTANTS().INFO_T__INFO_RISK_OBSERVATION))) {
                        prevInfoList.add("" + woInfoCustomTO.getInfo().getText().trim() + "\n");
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getPrevInfos() ", e);
        }
        return prevInfoList;
    }

    @Override
    public void showPromptUserAction() {
        //Do nothing
    }

    @Override
    public boolean validateUserInput() {
        applyChangesToModifiableWO();
        return true;
    }

    public void applyChangesToModifiableWO() {
        try {
            Log.d("COMMENT TO UTILITY ::", commentToUtility);
            Log.d("COMMENT TO OPERATOR :: ", commentToInternal);

            WorkorderCustomWrapperTO workOrder = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (commentToUtility.length() > 0) {
                WoInfoCustomTO woInfoCustomTO =
                        WorkorderUtils.createInfo(commentToUtility,
                                SessionState.getInstance().getCurrentUserUsername(),
                                CONSTANTS().INFO_SOURCE_T__SESP_PDA,
                                CONSTANTS().INFO_T__INFO_TO_UTILITY);
                WorkorderUtils.deleteAndAddWoInfoCustomMTO(workOrder, woInfoCustomTO);
            }

            if (commentToInternal.length() > 0) {
                WoInfoCustomTO woInfoCustomTO =
                        WorkorderUtils.createInfo(commentToInternal,
                                SessionState.getInstance().getCurrentUserUsername(),
                                CONSTANTS().INFO_SOURCE_T__SESP_PDA,
                                CONSTANTS().INFO_T__INFO_TO_OPERATOR);
                WorkorderUtils.deleteAndAddWoInfoCustomMTO(workOrder, woInfoCustomTO);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }
    }

    public void getUserChoiceValuesForPage() {
        try {
            if (Utils.isNotEmpty(commentToUtility)) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.COMMENT_TO_UTILITY, commentToUtility);
            }
            if (Utils.isNotEmpty(commentToInternal)) {
                stepfragmentValueArray.put(ConstantsAstSep.FlowPageConstants.COMMENT_TO_INTERNAL, commentToInternal);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getUserChoiceValuesForPage() ", e);
        }
    }

    class CommentTextWatcher implements TextWatcher {

        private View view;

        CommentTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if (view.getId() == R.id.commentToUtilityEditText) {
                commentToUtility = text;
            } else if (view.getId() == R.id.commentToInternalEditText) {
                commentToInternal = text;
            }
        }
    }

}
