package com.capgemini.sesp.ast.android.ui.wo.stepperItem;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.WorkorderAdditionalProcessingCallbackListener;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.google.gson.Gson;

import me.drozdzynski.library.steppers.SteppersItem;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


public abstract class CustomFragment extends Fragment {

    protected  ArrayMap<String,Object> stepfragmentValueArray = new ArrayMap<>();
    protected SteppersItem steppersItem;

    public String getStepIdentifier() {
        return stepIdentifier;
    }

    public void setStepIdentifier(String stepIdentifier) {
        this.stepIdentifier = stepIdentifier;
    }

    protected String stepIdentifier;
    protected String stepFragmentValues = null;
    protected static Dialog dialog = null;
    protected static String caseId;
    protected View fragmentView;
    public String TAG ;

    public WorkorderAdditionalProcessingCallbackListener getProcessingCallbackListener() {
        return processingCallbackListener;
    }

    public void setProcessingCallbackListener(WorkorderAdditionalProcessingCallbackListener processingCallbackListener) {
        this.processingCallbackListener = processingCallbackListener;
    }

    public WorkorderAdditionalProcessingCallbackListener processingCallbackListener;


    public CustomFragment() {
        super();
        TAG = this.getClass().getSimpleName();
    }


    public CustomFragment(String stepIdentifier){
        super();
        this.stepIdentifier=stepIdentifier;
        TAG = this.getClass().getSimpleName();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            caseId = String.valueOf(AbstractWokOrderActivity.workorderCustomWrapperTO.getIdCase());
            DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
            stepFragmentValues = databaseHandler.getStepStatus(caseId, stepIdentifier);
            if (!stepFragmentValues.equalsIgnoreCase("")) {
                Gson gson = new Gson();
                stepfragmentValueArray = gson.fromJson(stepFragmentValues, stepfragmentValueArray.getClass());
            }
        }catch(Exception e)
        {
            writeLog(TAG + "  : onAttach() " ,e);
        }


    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        int i =10;
        return null;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("N","M");
    }
    public void onStart() {
        super.onStart();
        Log.d("m","n");
    }

    public void onResume(){
        super.onResume();
        Log.d("m","n");
    }

    public void onPause(){
        super.onPause();
        //persistDataForWorkOrderResume();
        Log.d("m","n");
    }
    public void onStop(){
        super.onStop();
        Log.d("m","n");
    }

    public void onDestroyView(){
        super.onDestroyView();
        Log.d("m","n");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("m","n");
    }

    public void onDetach(){
        super.onDetach();
        Log.d("m","n");
    }


    protected void populateView()
    {
        DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
        //databaseHandler.getStepStatus(caseId,stepIdentifier);

    }

    public void showPromptUserAction() {
        final AlertDialog.Builder builder = GuiController.showErrorDialog(getActivity(),
                getString(R.string.one_or_more_mandatory_field_is_missing));
        dialog = builder.create();
        dialog.show();
    }

    public boolean validateUserInput() {
        return true;
    }

    public void persistDataForWorkOrderResume(){
        try{
        DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
        Gson gson = new Gson();
        stepFragmentValues = gson.toJson(stepfragmentValueArray);
        databaseHandler.saveStepStatus(caseId,stepIdentifier,stepFragmentValues,
                AbstractWokOrderActivity.getWorkorderCustomWrapperTO());
        } catch (Exception e) {
            writeLog(TAG + "  : persistDataForWorkOrderResume() ", e);
        }

    }

    public void applyChangesToModifiableWO() {
        //Fragments will implement this method on Requirement
    }

    public void initializePageValues() {

    }

    public  String evaluateNextPage(){
        return null;
    }
}
