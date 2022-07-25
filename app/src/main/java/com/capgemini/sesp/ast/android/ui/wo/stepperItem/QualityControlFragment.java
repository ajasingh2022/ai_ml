package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
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
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.ast_sep.common.to.ast.table.FieldVisitQualityTTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint({"ValidFragment","InflateParams"})
public class QualityControlFragment extends CustomFragment implements View.OnClickListener {
    static  String TAG = QualityControlFragment.class.getSimpleName();
    private transient ListView qualityListView = null;
    private transient QualityControlTypesAdapter adapter = null;
    
    private transient Dialog dialog = null;
    private transient  String selectedQualityControl;
    private transient  String selectedQualityControlAtStart;
    transient  Set<FieldVisitQualityTTO> selectedQControl;
    public QualityControlFragment(){
        super();
    }

    public QualityControlFragment(String stepIdentifier) {
        super(stepIdentifier);
    }


    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_quality_control, null);
        initializePageValues();
        initialize();
        populateData();
        return fragmentView;
    }

    @Override
    public void onResume(){
        super.onResume();

    }
    public void initializePageValues() {
        try{
        selectedQualityControl = null;
        selectedQControl = new HashSet<FieldVisitQualityTTO>();

            selectedQualityControl = (String)stepfragmentValueArray.get(ConstantsAstSep.FlowPageConstants.QUALITY_CONTROL);
            if(selectedQualityControl != null){
                String[] selQcs = selectedQualityControl.split(";");
                if((selQcs != null) && (selQcs.length > 0)){
                    for(String tempQc:selQcs){
                        FieldVisitQualityTTO fieldVisitQualityTTO = ObjectCache.getType(FieldVisitQualityTTO.class,Long.valueOf(tempQc));
                        if(fieldVisitQualityTTO != null){
                            selectedQControl.add(fieldVisitQualityTTO);
                        }
                    }
                }
            }

        selectedQualityControlAtStart = selectedQualityControl;
        } catch (Exception e) {
            writeLog(TAG + " : initializePageValues()", e);
        }
    }

    private void initialize(){
        qualityListView = fragmentView.findViewById(R.id.qualityListView);
    }

    private void populateData(){
        try{
        List<FieldVisitQualityTTO> qualityTTOs = ObjectCache.getAllTypes(FieldVisitQualityTTO.class);
        qualityListView.setOnTouchListener(new ListView.OnTouchListener() {
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
        if(qualityTTOs!=null && !qualityTTOs.isEmpty()){
            adapter = new QualityControlTypesAdapter(getActivity(), qualityTTOs, selectedQControl);
            qualityListView.setAdapter(adapter);
        }
        } catch (Exception e) {
            writeLog(TAG + " : populateData()", e);
        }
    }



    /**
     * User has not selected any option
     * and tries to move forward, user should be prompted
     * to select an option
     *
     */

    public void showPromptUserAction(){
        final View alertView
                = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.prompt_user_response_layout, null);
        if(alertView!=null){
            final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
            okButton.setOnClickListener(this);

            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                    alertView, null, null);
            dialog = builder.create();
            dialog.show();
        }
    }


    public boolean validateUserInput() {
        //check if any check box(Reason for not acciable) is checked else false
        if (adapter != null && adapter.isQualityChecked()) {
            stepfragmentValueArray = getUserChoiceValuesForPage();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if(v!=null
                && v.getId()==R.id.okButtonYesNoPage
                && dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
    }

    public void addQualityEnsured(final FieldVisitQualityTTO qualityTTO){
        if(qualityTTO!=null){
            selectedQControl.add(qualityTTO);
        }
    }

    public void removeQualityEnsured(final FieldVisitQualityTTO qualityTTO){
        if(qualityTTO!=null){
            selectedQControl.remove(qualityTTO);
        }
    }
    public ArrayMap<String, Object> getUserChoiceValuesForPage() {
        ArrayMap<String,Object> userChoice = new ArrayMap<>();
        try{
        String qualityControls = getQualityControl();
        if(Utils.isNotEmpty(qualityControls)){
            userChoice.put(ConstantsAstSep.FlowPageConstants.QUALITY_CONTROL, qualityControls);
        }
        } catch (Exception e) {
            writeLog(TAG + " : getUserChoiceValuesForPage()", e);
        }
        return userChoice;
    }
    private String getQualityControl(){
        String qcPerformed = null;
        try{
        if(selectedQControl != null){
            StringBuilder qcPerformedBuilder = new StringBuilder();
            for(FieldVisitQualityTTO fieldVisitQualityTTO:selectedQControl){
                qcPerformedBuilder.append(fieldVisitQualityTTO.getId());
                qcPerformedBuilder.append(";");
            }
            qcPerformed = qcPerformedBuilder.toString();
        }
        } catch (Exception e) {
            writeLog(TAG + " : getQualityControl()", e);
        }
        return qcPerformed;
    }
    /**
     * Adapter class to dynamically load all field visit quality types and show
     *
     * @author nirmchak
     *
     */

    private class QualityControlTypesAdapter extends BaseAdapter implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private transient SoftReference<Context> contextRef = null;
        private transient List<FieldVisitQualityTTO> qualityTTOs = null;
        private transient Set<FieldVisitQualityTTO> selectedQualityTTOs = null;

        public QualityControlTypesAdapter(final Context context,
                                          final List<FieldVisitQualityTTO> qualityTTOs, final Set<FieldVisitQualityTTO> selQualityTTOs){
            if(context!=null){
                this.contextRef = new SoftReference<Context>(context);
            }
            this.qualityTTOs = qualityTTOs;
            this.selectedQualityTTOs = selQualityTTOs;
        }

        @Override
        public int getCount() {
            int count = 0;
            if(this.qualityTTOs!=null){
                count = this.qualityTTOs.size();
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            FieldVisitQualityTTO to = null;
            if(this.qualityTTOs!=null
                    && this.qualityTTOs.size()>position
                    && position>=0){
                to = this.qualityTTOs.get(position);
            }
            return to;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View vw = convertView;
            if(vw==null
                    && this.contextRef!=null
                    && this.contextRef.get()!=null){
                final LayoutInflater inflater
                        = (LayoutInflater) this.contextRef.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final FieldVisitQualityTTO to = (FieldVisitQualityTTO)getItem(position);
                vw = inflater.inflate(R.layout.wo_generic_cb_tv_layout, null);
                if(to!=null){
                    ((TextView)vw.findViewById(R.id.textView)).setText(to.getName());
                }
                vw.setOnClickListener(this);

                final CheckBox checkBox = vw.findViewById(R.id.checkBox);
                if(checkBox!=null){
                    checkBox.setOnCheckedChangeListener(this);
                    checkBox.setTag(to);
                    for(FieldVisitQualityTTO fieldVisitQualityTTO:selectedQualityTTOs){
                        if(fieldVisitQualityTTO.getId().longValue()==to.getId().longValue()){
                            checkBox.setChecked(true);
                        }
                    }
                }
                // Tag the to
                vw.setTag(to);
            }
            return vw;
        }

        @Override
        public void onClick(View v) {
            if(v!=null){
                final CheckBox cb = v.findViewById(R.id.checkBox);
                // check if it is already selected and invert the selection
                if(cb!=null){
                    cb.setChecked(!cb.isChecked());
                    if(cb.isChecked()){
                        addQualityEnsured((FieldVisitQualityTTO) v.getTag());
                    } else {
                        removeQualityEnsured((FieldVisitQualityTTO) v.getTag());
                    }
                }
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if(buttonView!=null){
                if(buttonView.isChecked()){
                    addQualityEnsured((FieldVisitQualityTTO) buttonView.getTag());
                } else {
                    removeQualityEnsured((FieldVisitQualityTTO) buttonView.getTag());
                }
            }
        }

        /**
         * this will return whether the user has selected any reason
         *
         * @return
         */
        public boolean isQualityChecked() {
            return !selectedQualityTTOs.isEmpty();
        }

    }
}