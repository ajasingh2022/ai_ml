/**
 * @Copyright Capgemini
 */
package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventFvActTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventFvActTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventFieldVisitActivityCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.FlowPageConstants;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * Flow page that indicates additional work types
 * associated with <b>Meter change directly measured roll-out flow </b>
 * <p>
 * <p>
 * The additional work types are dynamically retrieved from table <b>WO_EVENT_FV_ACT_T</b>
 * </p>
 * <p><b>
 * Flow page implemented for meter change directly measured rollout
 * </p></b>
 *
 * @author Capgemini
 * @version 1.0
 * @since 12th March, 2015
 */
@SuppressLint("InflateParams")
public class AdditionalWorkPageFragment extends CustomFragment {

    private transient String additionalWork;
    transient List<WoEventFvActTTO> selectedAddWorkTypes;


    public AdditionalWorkPageFragment() {
        super();
    }

    static String TAG = AdditionalWorkPageFragment.class.getSimpleName();

    public AdditionalWorkPageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_additional_work, null);
        initialize();
        initializePageValues();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Constructor containing page title and only possible next page
     *
     * @param pageTitle String
     * @param nextPage FlowPage
     */

    /**
     * Returns the display implementing fragment
     *
     * @return {@link Fragment}
     */
    @Override
    public void applyChangesToModifiableWO() {
        try {
            //undoChanges();
            getUserChoiceValuesForPage();
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            final WoEventCustomTO woEventCustomTO = WorkorderUtils.getOrCreateWoEventCustomTO(wo, CONSTANTS().WO_EVENT_T__FIELD_VISIT, CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA);
            final List<WoEventFieldVisitActivityCustomTO> activities = new ArrayList<WoEventFieldVisitActivityCustomTO>();
            for (final WoEventFvActTTO tto : selectedAddWorkTypes) {
                final WoEventFieldVisitActivityCustomTO to = new WoEventFieldVisitActivityCustomTO();
                final WoEventFvActTO woEventFvActTo = new WoEventFvActTO();
                woEventFvActTo.setIdCase(wo.getIdCase());
                woEventFvActTo.setIdWoEventFvActT(tto.getId());
                if (woEventCustomTO.getFieldVisit() != null
                        && woEventCustomTO.getFieldVisit().getWoEventFvTO() != null
                        && woEventCustomTO.getFieldVisit().getWoEventFvTO().getId() != null) {
                    woEventFvActTo.setIdWoEventFv(woEventCustomTO.getFieldVisit().getWoEventFvTO().getId());
                }
                to.setWoEventFvActTO(woEventFvActTo);
                activities.add(to);
            }
            if (woEventCustomTO.getFieldVisit() != null) {
                woEventCustomTO.getFieldVisit().setActivities(activities);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : applyChangesToModifiableWO() ", e);
        }
    }

    public void setAbortStructure() {
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        //abortUtil.backupTOs(wo.getWorkorderCustomTO().getWoEvents(), WoEventCustomTO.class);
    }


    public void undoChanges() {
        final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        //wo.getWorkorderCustomTO().setWoEvents((List<WoEventCustomTO>)abortUtil.getRestoreTOs(WoEventCustomTO.class));

    }

    private String getSelectedAdditionWork() {
        String additionalWork = null;
        try{
        if (selectedAddWorkTypes != null) {
            StringBuilder selAddWorkBuilder = new StringBuilder();
            for (WoEventFvActTTO woEventFvActTTO : selectedAddWorkTypes) {
                selAddWorkBuilder.append(woEventFvActTTO.getId());
                selAddWorkBuilder.append(";");
            }
            additionalWork = selAddWorkBuilder.toString();
        }
        } catch (Exception e) {
            writeLog(TAG + "  : getSelectedAdditionWork() ", e);
        }
        return additionalWork;
    }

    public void initializePageValues() {
        try{
        additionalWork = null;
        selectedAddWorkTypes = new ArrayList<WoEventFvActTTO>();
        if (!stepfragmentValueArray.isEmpty()) {
            additionalWork = String.valueOf(stepfragmentValueArray.get(FlowPageConstants.ADDITIONAL_WORK));
            if (additionalWork != null) {
                String[] selAdnWork = additionalWork.split(";");
                if ((selAdnWork != null) && (selAdnWork.length > 0)) {
                    for (String tempAddWork : selAdnWork) {
                        WoEventFvActTTO woEventFvActTTO = ObjectCache.getType(WoEventFvActTTO.class, Long.valueOf(tempAddWork));
                        if (woEventFvActTTO != null && !selectedAddWorkTypes.contains(woEventFvActTTO)) {
                            selectedAddWorkTypes.add(woEventFvActTTO);
                        }
                    }
                }

            }
        }
        Log.d(this.getClass().getSimpleName(), "Additional work at start " + additionalWork);
        } catch (Exception e) {
            writeLog(TAG + "  : initializePageValues() ", e);
        }
    }


    public Map<String, Object> getUserChoiceValuesForPage() {
        try {
            String additionalWorks = getSelectedAdditionWork();
            if (Utils.isNotEmpty(additionalWorks)) {
                stepfragmentValueArray.put(FlowPageConstants.ADDITIONAL_WORK, additionalWorks);
            }
        } catch (Exception e) {
            writeLog(TAG + " : getUserChoiceValuesForPage()", e);
        }
        return stepfragmentValueArray;
    }

    /**
     * UI implementation fragment
     * <p>
     * The type of additional works here are retrieved dynamically
     * based on the table WO_EVENT_FV_ACT_T
     */
    protected transient AddWorkTypesAdapter adapter = null;
    protected transient ListView addWorkListView = null;




    /*
         * Lookup and initialize all the view widgets
    */
    protected void initialize() {
        addWorkListView = fragmentView.findViewById(R.id.addWorkListView);

        addWorkListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }
        });
    }

    public void addWorkCompleted(final WoEventFvActTTO actTTO) {
        if (actTTO != null && !selectedAddWorkTypes.contains(actTTO)) {
            selectedAddWorkTypes.add(actTTO);
        }
    }

    public void removeWorkCompleted(final WoEventFvActTTO actTTO) {
        if (actTTO != null && selectedAddWorkTypes.contains(actTTO)) {
            selectedAddWorkTypes.remove(actTTO);
        }
    }

    protected void populateData() {
        // Get all field visit additional works
        try {
            final List<WoEventFvActTTO> addWorkTypes = ObjectCache.getAllTypes(WoEventFvActTTO.class);
            if (addWorkTypes != null && !addWorkTypes.isEmpty()) {
                adapter = new AddWorkTypesAdapter(getActivity(), addWorkTypes);
                addWorkListView.setAdapter(adapter);
            }
        } catch (Exception e) {
            writeLog("AdditionalWorkPageFragment : populateData()", e);
        }
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
/*

      The adapter class that displays each additional work
      by inflating custom view

      @author nirmchak
*/

    private class AddWorkTypesAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

        private transient SoftReference<Context> contextRef = null;
        private transient List<WoEventFvActTTO> addWorkTypes = null;

        public AddWorkTypesAdapter(final Context context,
                                   final List<WoEventFvActTTO> addWorkTypes) {
            if (context != null) {
                this.contextRef = new SoftReference<Context>(context);
            }
            this.addWorkTypes = addWorkTypes;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (this.addWorkTypes != null) {
                count = this.addWorkTypes.size();
            }
            return count;
        }

        @Override
        public WoEventFvActTTO getItem(int position) {
            WoEventFvActTTO to = null;
            if (this.addWorkTypes != null
                    && this.addWorkTypes.size() > position
                    && position >= 0) {
                to = this.addWorkTypes.get(position);
            }
            return to;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        private class ViewHolder {
            TextView text;
            CheckBox checkBox;
        }


        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            final ViewHolder holder = new ViewHolder();
            final WoEventFvActTTO to = getItem(position);
            final LayoutInflater inflater = (LayoutInflater) this.contextRef.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vw = inflater.inflate(R.layout.wo_generic_cb_tv_layout, parent, false);
            if (to != null) {
                holder.text = vw.findViewById(R.id.textView);
                holder.checkBox = vw.findViewById(R.id.checkBox);
                holder.checkBox.setOnCheckedChangeListener(this);
            }
            if (holder.text != null) {
                holder.text.setText(to.getName());
            }
            if (holder.checkBox != null) {
                holder.checkBox.setTag(to);
                if (selectedAddWorkTypes.contains(to)) {
                    holder.checkBox.setChecked(true);
                }
            }
            return vw;
        }

        @Override
        public void onCheckedChanged(final CompoundButton buttonView,
                                     final boolean isChecked) {
            if (buttonView != null) {
                if (buttonView.isChecked()) {
                    addWorkCompleted((WoEventFvActTTO) buttonView.getTag());
                } else {
                    removeWorkCompleted((WoEventFvActTTO) buttonView.getTag());
                }
            }
        }

    }

}
