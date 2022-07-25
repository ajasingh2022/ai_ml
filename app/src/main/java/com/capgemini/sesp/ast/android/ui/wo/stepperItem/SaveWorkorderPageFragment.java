package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.communication.WorkorderAdditionalProcessingCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.module.util.to.AndroidWOAttachmentBean;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderSummaryActivity;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventFieldVisitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams"})
public class SaveWorkorderPageFragment extends CustomFragment implements View.OnTouchListener, View.OnClickListener {

    private transient Button saveWoButton = null;
    private transient Drawable pressedDrawable = null;
    private transient Drawable activatedDrawable = null;
    private transient String TAG = SaveWorkorderPageFragment.class.getSimpleName();
    WorkorderAdditionalProcessingCallbackListener processingCallbackListener;


    Activity a;

    private transient AtomicBoolean saveStarted = new AtomicBoolean(false);

    private transient final DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();

    public SaveWorkorderPageFragment() {
        super();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

   /* @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback= (MyListener) activity;
    }*/


    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flow_fragment_save_wo, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        saveWoButton = fragmentView.findViewById(R.id.saveWoButton);
            /*pressedDrawable = getActivity().getResources().getDrawable(R.drawable.rounded_corner_button_positive_enabling_disabling);
			activatedDrawable = getActivity().getResources().getDrawable(R.drawable.rounded_corner_button_positive_enabled);*/
        initializePageValues();
        setupListeners();
    }

    public void initializePageValues() {
        try {
            final WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            WoEventCustomTO woEventCustomTO = WorkorderUtils.getOrCreateWoEventCustomTO(modifiableWo, CONSTANTS().WO_EVENT_T__FIELD_VISIT, CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA);

            Log.d(TAG, "--------------EVENT REASONS LOG TRACE----");
            if (woEventCustomTO.getResultReasons() != null) {
                for (WoEventResultReasonTO woEventResultReasonTO : woEventCustomTO.getResultReasons()) {
                    Log.d(TAG, " Reason selected :: " + ObjectCache.getTypeName(WoEventResultReasonTTO.class, woEventResultReasonTO.getIdWoEventResultReasonT()));
                }
            }

            Log.d(TAG, "------------ATTACHMENT LOG TRACE---------------");
            List<AndroidWOAttachmentBean> androidWOAttachmentBeanList = DatabaseHandler.createDatabaseHandler().getAttachment(modifiableWo.getIdCase(), 0L, 0L, null);
            for (AndroidWOAttachmentBean androidWOAttachmentBean : androidWOAttachmentBeanList) {
                Log.d(TAG, androidWOAttachmentBean.toString());
            }
        } catch (Exception ex) {
            writeLog(TAG + "  ::initializePageValues()", ex);
        }
    }

    private void setupListeners() {
        if (saveWoButton != null) {
            saveWoButton.setOnTouchListener(this);
            saveWoButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (!saveStarted.get() && v.getId() == R.id.saveWoButton) {
                saveStarted.set(!saveStarted.get());

                v.setBackgroundDrawable(activatedDrawable);
                //((Button)v).setText(getActivity().getResources().getString(R.string.saving));
                v.setEnabled(false);
                applyChanges();
                //getActivity().finish();
                showOrderSummaryActivity();
            }
        } catch (Exception e) {
            writeLog(TAG + " : onClick()", e);
        }
    }

    private void showOrderSummaryActivity() {
        try {
            AndroidUtilsAstSep.launchExplicitActivity(ApplicationAstSep.context,
                    ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.ORDER_SUMMARY_ACTIVITY), Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            writeLog(TAG + " : showOrderSummaryActivity()", e);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.saveWoButton
                && event.getAction() == MotionEvent.ACTION_DOWN) {
            // Change to transition color
            if (pressedDrawable != null) {
                v.setBackgroundDrawable(pressedDrawable);
            }
        } else if (v.getId() == R.id.saveWoButton
                && event.getAction() == MotionEvent.ACTION_UP) {
            v.performClick();
            v.setBackgroundDrawable(activatedDrawable);
        }
        return true;
    }

    /**
     * ======= SAVE THE WORKORDER VIA WEB SERVICE CALL ============
     */

    public void applyChanges() {
        try {
            final WorkorderCustomWrapperTO modifiableWo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (processingCallbackListener != null) {
                processingCallbackListener.process(modifiableWo);
            }
			/*
			 * fix to set endtime.
			 */
            WoEventFieldVisitCustomTO visitEvent = WorkorderUtils.getWoEventFieldVisitCustomTO(modifiableWo);
            if (null == visitEvent.getWoEventFvTO().getFieldVisitEnd()) {
                visitEvent.getWoEventFvTO().setFieldVisitEnd(new Date());
            }

            WoEventCustomTO woEventCustomTO = WorkorderUtils.getOrCreateWoEventCustomTO(modifiableWo, CONSTANTS().WO_EVENT_T__FIELD_VISIT, CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA);
            woEventCustomTO.getWoEventTO().setEventTimestamp(new Date());

            databaseHandler.markWoInSynchState(modifiableWo.getIdCase(), modifiableWo.getIdCaseType());
            databaseHandler.clearPageVisitsForCase(modifiableWo.getIdCase());
            databaseHandler.saveFinalWOForSync(modifiableWo);
            // End of flow reached - clear flow engine stack
            //FlowEngine.getInstance().releaseCurrentFlow();

            AndroidUtilsAstSep.suggestSaveWorkorder(modifiableWo.getIdCase(), modifiableWo.getFieldVisitID());
        } catch (Exception ex) {
            writeLog(TAG + "  ::applyChanges()", ex);
        }
    }

    @Override
    public void showPromptUserAction() {
        //Do nothing
    }

    @Override
    public boolean validateUserInput() {
        return true;
    }
}