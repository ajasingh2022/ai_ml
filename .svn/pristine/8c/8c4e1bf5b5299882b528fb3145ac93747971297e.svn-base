package com.capgemini.sesp.ast.android.ui.wo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.WorkorderCache;
import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.communication.LocationStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.communication.WorkflowImageCaptureCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.common.AddCommentActivity;
import com.capgemini.sesp.ast.android.ui.activity.flow.SESPFlowActivity;
import com.capgemini.sesp.ast.android.ui.activity.login.LogoutActivity;
import com.capgemini.sesp.ast.android.ui.activity.receiver.NetworkStatusMonitorReceiver;
import com.capgemini.sesp.ast.android.ui.activity.scanner.BarcodeScanner;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.CustomFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.OnlineVerificationPageFragment;
import com.google.gson.Gson;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventFieldVisitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.drozdzynski.library.steppers.SteppersItem;
import me.drozdzynski.library.steppers.SteppersView;
import me.drozdzynski.library.steppers.interfaces.OnCancelAction;
import me.drozdzynski.library.steppers.interfaces.OnChangeStepAction;
import me.drozdzynski.library.steppers.interfaces.OnFinishAction;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.OrderHandlerConstants.ORDERDETAILSACTIVITYCLASS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public abstract class AbstractWokOrderActivity extends AppCompatActivity implements SteppersView.NotifyActivityListener, StepFactory.ActivityConnectingInterface, SESPFlowActivity, NetworkStatusCallbackListener, View.OnLongClickListener {

    //Location Related Variables
    private String TAG = AbstractWokOrderActivity.class.getSimpleName();
    private transient Set<NetworkStatusCallbackListener> netStatReceivers = null;
    private transient SoftReference<Set<LocationStatusCallbackListener>> loctionReceivers = null;
    private transient ArrayMap<String, Boolean> locTrackStarted = new ArrayMap<>();

    //Scanning Related
    private static final int MEP_SCAN_REQUEST = 7;
    private static final int MET_SCAN_REQUEST = 8;
    private transient Class<?> launcherOrderDetailsCls = null;
    protected transient Set<WorkflowImageCaptureCallbackListener> imageListeners = null;

    public static boolean revisiting;
    public static boolean resuming = false;
    public static boolean clickedOnStepperItem;
    private ImageView networStateIcon;
    private SteppersView steppersView;
    public static boolean isPhotoMandatory = false;
    public static WorkorderCustomWrapperTO workorderCustomWrapperTO;
    public SteppersView.Config steppersViewConfig;
    protected ArrayList<String> activeStepIdentifiers = null;
    public ArrayList<SteppersItem> activeSteps;
    public ArrayMap<String, SteppersItem> stepIdWithStepItem = new ArrayMap<>();
    public StepFactory stepFactory = new StepFactory(this);
    protected final Resources resources = ApplicationAstSep.context.getResources();
    private transient final String WIFI_STATUS_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    private transient final String MOBILE_DATA_STATUS_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private transient BroadcastReceiver networkStatusReceiver = null;
    public static int camIntReqCode;
    public static int currentPosition = 0,previousPos = 0;
    public WorkorderCustomWrapperTO workorderCustomWrapperTOUntouched;
    private Bundle bundleAtBegining;
    private boolean needToResumeWO = false;
    public static boolean evInformationRestore = false;

    public SteppersView getSteppersView() {
        return steppersView;
    }

    public abstract void addAssuredSteps();

    public static WorkorderCustomWrapperTO getWorkorderCustomWrapperTO() {
        return workorderCustomWrapperTO;
    }

    public static void setWorkorderCustomWrapperTO(WorkorderCustomWrapperTO workorderCustomWrapperTO) {
        AbstractWokOrderActivity.workorderCustomWrapperTO = workorderCustomWrapperTO;
    }

    public void setSteppersView(SteppersView steppersView) {
        this.steppersView = steppersView;
    }


    public ArrayList<String> getActiveStepIdentifiers() {
        return activeStepIdentifiers;
    }

    public void setActiveStepIdentifiers(ArrayList<String> activeStepIdentifiers) {
        this.activeStepIdentifiers = activeStepIdentifiers;
    }

    public ArrayList<SteppersItem> getActiveSteps() {
        return activeSteps;
    }

    public void setActiveSteps(ArrayList<SteppersItem> activeSteps) {
        this.activeSteps = activeSteps;
    }

    public void scanMEPButtonClicked(View view) {
        try {
            Intent barCodeScanner = new Intent(this, BarcodeScanner.class);
            startActivityForResult(barCodeScanner, MEP_SCAN_REQUEST);
        } catch (Exception e) {
            writeLog(TAG + "  : scanMEPButtonClicked() ", e);
        }
    }



    public void scanMeterNumber(View view) {
        try {
            Intent barCodeScanner = new Intent(this, BarcodeScanner.class);
            startActivityForResult(barCodeScanner, MET_SCAN_REQUEST);
        } catch (Exception e) {
            writeLog(TAG + "  : scanMeterNumber() ", e);
        }

    }

    @Override
    public void chooseYesNoResponse(String stepid, String responseCode) {

    }

    @Override
    public void meterAccesibiltyResponse(String stepId, String responseCode) {

    }

    @Override
    public void newConcentratorTechPlanPageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    public void meterInstallationCheckResponse(String stepId, String responseCode) {

    }

    @Override
    public void registerCordsFragmentResponse(String stepId, String responseCode) {

    }

    @Override
    public void oldMeterChangeYesNoFragmentResponse(String stepId, String responseCode) {

    }

    @Override
    public void newMeterTechPlanPageFragmentResponse(String stepId, String responseCode) {

    }

    @Override
    public void registerExternAntennaAsPerTechPlanFragmentResponse(String stepId, String responseCode) {

    }

    @Override
    public void MasterMeterInfoPageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    public void registerNewSimCardInfoPageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    public void onlineVerificationPageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    public void oldMeterPowerCheckPageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    public void registerNewCommunicationModulePageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }

    @Override
    public void newRepeaterTechPlanPageFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }
    @Override
    public void registerNewEvChargerInfoFragmentResponse(String steppersItemIdentifier, String responseCode) {

    }



    @Override
    public void onClickItem(int position) {
        /*if (activeSteps.get(position).isDone())
            steppersView.setActiveItem(position);*/

        try {
            if (position != currentPosition)
                if (activeSteps.get(position).isDone()) {
                    revisiting = true;
                    clickedOnStepperItem = true;
                    steppersView.setActiveItem(position);
                }
        } catch (Exception ex) {
            writeLog(TAG + ":onClickItem", ex);
        }

    }

    @Override
    public void onLongClickItem(int position) {

    }

    protected void buildSteps() {
        try {
            steppersView.setConfig(steppersViewConfig);
            steppersView.setItems(activeSteps);
            steppersView.build();
        } catch (Exception ex) {
            writeLog(TAG + ":buildSteps", ex);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            currentPosition = 0;
            previousPos = 0;
            workorderCustomWrapperTO=AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            Long idCase = workorderCustomWrapperTO.getIdCase();

            //Set Field Visitid if not set
            if(workorderCustomWrapperTO.getFieldVisitID() == null || workorderCustomWrapperTO.getFieldVisitID().trim().equals("")){
                AndroidUtilsAstSep.setFieldVisitID(workorderCustomWrapperTO);
                WorkorderCache.saveWorkorderToCache(workorderCustomWrapperTO);
            }

            workorderCustomWrapperTOUntouched = new WorkorderCustomWrapperTO();
            Gson gson = new Gson();
            workorderCustomWrapperTOUntouched = gson.fromJson(gson.toJson(workorderCustomWrapperTO), WorkorderCustomWrapperTO.class);
            needToResumeWO = true;
            setContentView(R.layout.activity_abstract_workorder);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            final View activityRootView = findViewById(R.id.activityRoot);



            /**
             * Specify the custom action bar
             */
            // Hiding the logo as requested
            getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(false);

            /*
             * Setting up custom action bar view
             */
            final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
            final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vw = layoutManager.inflate(R.layout.flow_engine_activity_custom_action_bar_layout, null);
            networStateIcon = vw.findViewById(R.id.network_state_iv);
            TextView heading = vw.findViewById(R.id.title_text);
            heading.setText(String.format("%s-%s", getResources().getString(R.string.work_order), workorderCustomWrapperTO.getIdCase().toString()));//Todo:need  to define string resource
            getSupportActionBar().setCustomView(vw, layout);
            getSupportActionBar().setDisplayShowCustomEnabled(true);


            steppersView = findViewById(R.id.steppersView);
            //LinearLayout loadingScreen = findViewById(R.id.progressbar_view);
            //loadingScreen.setVisibility(View.GONE);
            steppersView.setEnabled(false);
            steppersViewConfig = new SteppersView.Config();
            steppersViewConfig.setOnFinishAction(new OnFinishAction() {
                @Override
                public void onFinish() {
                    CustomFragment lastFragment = (CustomFragment) getSupportFragmentManager().findFragmentByTag(activeSteps.get(currentPosition).getSteppersItemIdentifier());
                    if (!lastFragment.validateUserInput()) {
                        lastFragment.showPromptUserAction();
                    } else {
                        applyChanges();
                        showOrderSummaryActivity();
                        AbstractWokOrderActivity.this.finish();
                    }
                }
            });


            steppersViewConfig.setOnCancelAction(new OnCancelAction() {
                @Override
                public void onCancel() {

                    getSteppersView().getSteppersAdapter().maxReachedStep = currentPosition;
                    undoWO();
                }
            });

            steppersViewConfig.setOnChangeStepAction(new OnChangeStepAction() {
                @Override
                public void onChangeStep(int position, SteppersItem activeStep) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(activityRootView.getWindowToken(), 0);
                    steppersView.setEnabled(false);
                    activeStep.setDone(true);
                    List<Fragment> activeFragments = new ArrayList<>();
                    if (previousPos != currentPosition)
                        previousPos = new Integer(currentPosition);
                    currentPosition = position;
                    //When User revisits a completed step and clicks on continue
                    if ((!resuming) && (position > previousPos) &&(!clickedOnStepperItem)) {
                        if (position != 0) {
                            ((CustomFragment) activeSteps.get(position - 1).getFragment()).persistDataForWorkOrderResume();
                            saveWorkOrderStateForResume();
                        }

                    }
                    clickedOnStepperItem = false;
                    steppersView.getRecyclerView().smoothScrollToPosition(position);
                    steppersView.setEnabled(true);

                }
            });

            steppersViewConfig.setFragmentManager(getSupportFragmentManager());
            activeSteps = new ArrayList<>();
            // getResources().getDrawable(R.drawable.ic_radio_button_unchecked_24dp);
            if (getIntent().getExtras().get(ORDERDETAILSACTIVITYCLASS) != null) {
                // Initiating class
                launcherOrderDetailsCls = (Class<?>) getIntent().getExtras().get(ORDERDETAILSACTIVITYCLASS);
            }
        } catch (Exception ex) {
            writeLog(TAG + ":onCreate", ex);
        }

    }

    private void undoWO() {
        try {
            List<Fragment> activeFragments = new ArrayList<>();
            resuming = true;
            workorderCustomWrapperTO = AndroidUtilsAstSep.getDeepCopy(workorderCustomWrapperTOUntouched, WorkorderCustomWrapperTO.class);
            //replace bundle with null as at first step it will be null
            Bundle newBundle = new Bundle();
            getIntent().replaceExtras(newBundle);
            for (int i = 0; i < getActiveSteps().size(); i++) {
                activeFragments.add(getActiveSteps().get(i).getFragment());
                if (i > currentPosition) {
                    SteppersItem steppersItem = getActiveSteps().get(i);
                    steppersItem.setDone(false);
                    Fragment fragment = steppersViewConfig.getFragmentManager().findFragmentByTag(steppersItem.getSteppersItemIdentifier());
                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().detach(fragment).commitNow();
                        getSupportFragmentManager().beginTransaction().remove(fragment).commitNow();
                    }
                } else if (i == currentPosition) {
                    if (currentPosition == 0){
                        workorderCustomWrapperTO =
                                AndroidUtilsAstSep.getDeepCopy(workorderCustomWrapperTOUntouched,
                                        WorkorderCustomWrapperTO.class);
                    }else {
                        workorderCustomWrapperTO = CommunicationHelper.JSONMAPPER.readValue(
                                DatabaseHandler.createDatabaseHandler().
                                        getStepWO(workorderCustomWrapperTOUntouched.getIdCase().toString(),
                                                activeSteps.get(currentPosition - 1).getSteppersItemIdentifier()),
                                WorkorderCustomWrapperTO.class);
                    }
                    SteppersItem steppersItem = getActiveSteps().get(i);
                    getActiveSteps().remove(steppersItem);
                    getActiveSteps().add(currentPosition,steppersItem);
                    steppersView.getSteppersAdapter().notifyDataSetChanged();
                    Fragment fragment = steppersViewConfig.getFragmentManager().findFragmentByTag(steppersItem.getSteppersItemIdentifier());
                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().detach(fragment).commitNow();
                        getSupportFragmentManager().beginTransaction().add(fragment, steppersItem.getSteppersItemIdentifier()).commitNow();
                    }
                } else if (i < currentPosition) {
                    SteppersItem steppersItem = getActiveSteps().get(i);
                    CustomFragment currentFragment = (CustomFragment) steppersViewConfig.getFragmentManager().findFragmentByTag(steppersItem.getSteppersItemIdentifier());
                    //currentFragment.applyChangesToModifiableWO();
                }
            }

            //Remove all fragments except active fragments
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (int i = 0; i < fragments.size(); i++) {
                CustomFragment customFragment = (CustomFragment) fragments.get(i);
                if (activeFragments.contains(customFragment)) {

                } else {
                    stepIdWithStepItem.get(customFragment.getStepIdentifier()).setDone(false);
                    getSupportFragmentManager().beginTransaction().remove(fragments.get(i)).commitNow();
                }
            }
            getSupportFragmentManager().executePendingTransactions();
            resuming = false;
        } catch (Exception ex) {
            writeLog(TAG + ":undoWo", ex);
        }
    }

    private void undoWoandRedoIt(List<Fragment> activeFragments, int position) {
        try {
            {
                resuming = true;
                revisiting = false;
                workorderCustomWrapperTO = AndroidUtilsAstSep.getDeepCopy(workorderCustomWrapperTOUntouched, WorkorderCustomWrapperTO.class);
                //replace bundle with null as at first step it will be null
                Bundle newBundle = new Bundle();
                getIntent().replaceExtras(newBundle);
                //if step is lesser than position applychangesToModifiableWo
                // if step is greater than position remove it from fragmntTransaction
                for (int i = 0; i < getActiveSteps().size(); i++) {
                    activeFragments.add(getActiveSteps().get(i).getFragment());
                    if (i > position) {
                        SteppersItem steppersItem = getActiveSteps().get(i);
                        steppersItem.setDone(false);
                        Fragment fragment = steppersViewConfig.getFragmentManager().findFragmentByTag(steppersItem.getSteppersItemIdentifier());
                        if (fragment != null) {
                            getSupportFragmentManager().beginTransaction().detach(fragment).commitNow();
                            getSupportFragmentManager().beginTransaction().remove(fragment).commitNow();
                        }
                    } else if (i == position) {
                        SteppersItem steppersItem = getActiveSteps().get(i);
                        Fragment fragment = steppersViewConfig.getFragmentManager().findFragmentByTag(steppersItem.getSteppersItemIdentifier());
                        if (fragment != null) {
                            getSupportFragmentManager().beginTransaction().detach(fragment).commitNow();
                            getSupportFragmentManager().beginTransaction().add(fragment, steppersItem.getSteppersItemIdentifier()).commitNow();
                        }
                    } else if (i < position) {
                        SteppersItem steppersItem = getActiveSteps().get(i);
                        CustomFragment currentFragment = (CustomFragment) steppersViewConfig.getFragmentManager().findFragmentByTag(steppersItem.getSteppersItemIdentifier());
                        currentFragment.applyChangesToModifiableWO();

                    }
                }
                //Remove all fragments except active fragments
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for (int i = 0; i < fragments.size(); i++) {
                    CustomFragment customFragment = (CustomFragment) fragments.get(i);
                    if (activeFragments.contains(customFragment)) {

                    } else {
                        stepIdWithStepItem.get(customFragment.getStepIdentifier()).setDone(false);
                        getSupportFragmentManager().beginTransaction().remove(fragments.get(i)).commitNow();
                    }
                }


                getSupportFragmentManager().executePendingTransactions();
            }
            resuming = false;
            steppersView.getSteppersAdapter().changeToStep(position - 1);
            activeSteps.get(position - 1).getOnClickContinue().onClick();
        } catch (Exception ex) {
            writeLog(TAG + ":undoWoandRedoIt", ex);
        }

    }


    @Override
    public void onActivityResult(final int requestCode,
                                 final int resultCode, final Intent imageData) {
        try {
            if (requestCode == camIntReqCode
                    && resultCode == RESULT_OK) {

                String photoFileLocation = AndroidUtilsAstSep.getStringSharedPref(ConstantsAstSep.OrderHandlerConstants.SESPWOIMAGEPREFNAME,
                        ConstantsAstSep.OrderHandlerConstants.IMAGEFILEINTENTNAME, Context.MODE_PRIVATE, "");

                Log.d("PHOTO", "PHOTO FILE LOCATION :: " + photoFileLocation);
                Log.d("PHOTO", "COMPRESSING FILE");
                //Compressing photos
                if (Utils.isNotEmpty(photoFileLocation)) {
                    final Uri picUri = Uri.fromFile(new File(photoFileLocation));
                    Bitmap bm = null;
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        // bm = Media.getBitmap(mContext.getContentResolver(), imageLoc);
                        bm = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(picUri), null, options);
                        FileOutputStream out = new FileOutputStream(photoFileLocation);
                        bm.compress(Bitmap.CompressFormat.JPEG, 20, out);
                        bm.recycle();
                    } catch (Exception e) {
                        writeLog(TAG + "onActivityResult : PHOTO", e);
                    }
                }

                // Callback methods to be executed
                if (this.imageListeners != null
                        && this.imageListeners != null) {
                    final Intent imageInfo = AndroidUtilsAstSep.getWoImageGalleryIntent(this);

                    try {
                        if (imageInfo != null) {
                            for (final WorkflowImageCaptureCallbackListener callback : this.imageListeners) {
                                callback.onImageTaken(imageInfo);
                            }
                        } else {
                            Log.d("PHOTO", "ImageInfo is null");
                        }
                    } catch (final Exception ex) {
                        writeLog(TAG + "onActivityResult : PHOTO", ex);
                    }
                } else {
                    Log.d("PHOTO", "imageListeners is NULL");
                }
            } else if (requestCode == MEP_SCAN_REQUEST && resultCode == Activity.RESULT_OK && imageData != null) {
                EditText measureCodeTx = findViewById(R.id.measureCodeTx);
                measureCodeTx.setText(imageData.getStringExtra("barcode_result"));
            } else if (requestCode == MET_SCAN_REQUEST && resultCode == Activity.RESULT_OK && imageData != null) {
                EditText meterNumTx = findViewById(R.id.meterNumTx);
                meterNumTx.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(imageData.getStringExtra("barcode_result"), true));
            } else {
                Log.d("PHOTO", "ELSE block is called");
                super.onActivityResult(requestCode, resultCode, imageData);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : onActivityResult() ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.exit) + "?");
            builder.setMessage(getString(R.string.wo_progress_saved_msg))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AbstractWokOrderActivity.this.finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }catch (Exception e)
        {
            writeLog(TAG + "  : onBackPressed() ", e);
        }

    }

    public void applyChanges() {
        Log.d("SavingWorkOrder", "SaveWorkorderPage-applyChanges method is called");
        try {
            WorkorderCustomWrapperTO modifiableWo = new WorkorderCustomWrapperTO();
            modifiableWo = workorderCustomWrapperTO;
            DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();

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

            Log.d("SaveWorkOrder", "applyChanges method, calling intent service to upload photos and attachments for workorder " + modifiableWo.getIdCase());
            AndroidUtilsAstSep.suggestSaveWorkorder(modifiableWo.getIdCase(), modifiableWo.getFieldVisitID());
            databaseHandler.deleteStatusforWorkOrder(String.valueOf(modifiableWo.getId()));
        } catch (Exception e) {
            writeLog(TAG + "  : applyChanges() ", e);
        }
    }

    protected void showOrderSummaryActivity() {
        try {
            AndroidUtilsAstSep.launchExplicitActivity(ApplicationAstSep.context,
                    ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.ORDER_SUMMARY_ACTIVITY), Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            writeLog(TAG + "  : showOrderSummaryActivity() ", e);
        }
    }

    protected void addToActiveStepIfNotAdded(SteppersItem steppersItem, int index) {
        try {
            if (index == -1) {
                if (!activeSteps.contains(steppersItem)) {
                    activeSteps.add(steppersItem);
                }
            } else if (!activeSteps.contains(steppersItem)) {
                activeSteps.add(index, steppersItem);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : addToActiveStepIfNotAdded() ", e);
        }

    }


    public boolean ifPossibleResumeWorkOrder() {
        needToResumeWO = false;
        String caseId = String.valueOf(workorderCustomWrapperTO.getId());
        DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
        ArrayMap<String, Object> savedState = databaseHandler.checkStatusforWorkOrder(caseId);
        activeStepIdentifiers = new ArrayList<>();
        boolean returnvalue = false;
        try {
            int presentStep = (Integer) savedState.get("PRESENT_STEP");

            buildSteps();
            if (getIntent().getExtras().get("ContinueFlag").toString().equalsIgnoreCase("true")) {
                //resume work order
                if (!"0".equalsIgnoreCase(String.valueOf(savedState.get("PRESENT_STEP")))) {
                    resuming = true;
                    activeStepIdentifiers = getActiveStepIdentifiersFromJSON(String.valueOf(savedState.get("STEP_IDENTIFIERS")));
                    steppersView.getSteppersAdapter().changeToStep(resumeWorkOrderFromSavedState(presentStep));
                    steppersView.getRecyclerView().smoothScrollToPosition(presentStep);
                    returnvalue = true;
                    resuming = false;


                }
            } else if (getIntent().getExtras().get("ContinueFlag").toString().equalsIgnoreCase("false")) {
                //delete all saved values for work order
                databaseHandler.deleteStatusforWorkOrder(caseId);
                returnvalue = false;
            }

            steppersView.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            addAssuredSteps();
        } catch (Exception e) {
            writeLog(TAG + "  : ifPossibleResumeWorkOrder() ", e);
        }
        return returnvalue;

    }


    private ArrayList<String> getActiveStepIdentifiersFromJSON(String jsonString) {

        Gson gson = new Gson();
        ArrayList<String> activeStepIdentifiers = new ArrayList<>();
        try {
            StringBuilder stringBuilder = new StringBuilder(jsonString);
            stringBuilder.deleteCharAt(0);
            stringBuilder.deleteCharAt(jsonString.length() - 2);
            jsonString = stringBuilder.toString();
            activeStepIdentifiers = gson.fromJson(jsonString, activeStepIdentifiers.getClass());
        } catch (Exception e) {
            writeLog(TAG + ":ArrayList() ", e);
        }
        return activeStepIdentifiers;

    }

    @Override
    public void onPause() {
        super.onPause();
        // De-register the broadcast receiver here
        try {
            if (this.networkStatusReceiver != null) {
                unregisterReceiver(this.networkStatusReceiver);
                unregForNetStatChangeInfo(null);
            }

            if (this.loctionReceivers != null) {
                unregForLocationChangeInfo(null, null);
            }
        } catch (Exception e) {
            writeLog(TAG + ":onPause() ", e);
        }
        //saveWorkOrderStateForResume();

    }


    /* @param callBack {@link NetworkStatusCallbackListener
    }
     * @see
    StandardFlowActivity#unregForNetStatChangeInfo(NetworkStatusCallbackListener)
     */
    @Override
    public void regForNetStatChangeInfo(final NetworkStatusCallbackListener callBack) {
        try {
            if (callBack != null) {
                if (netStatReceivers == null) {
                    netStatReceivers
                            = new HashSet<NetworkStatusCallbackListener>();
                }

                if (netStatReceivers != null) {
                    netStatReceivers.add(callBack);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : regForNetStatChangeInfo() ", e);
        }
    }

    /**
     * @param callBack {@link LocationStatusCallbackListener}
     */
    @Override
    public void regForLocationChangeInfo(final LocationStatusCallbackListener callBack,
                                         final String provider) {
        try {
            if (callBack != null && !Utils.safeToString(provider).equals("")) {
                if (loctionReceivers == null) {
                    loctionReceivers
                            = new SoftReference<Set<LocationStatusCallbackListener>>(new HashSet<LocationStatusCallbackListener>());
                }

                boolean shouldRequestNewLoc = false;
                AndroidUtilsAstSep.requestLocationByProviders(this, this, shouldRequestNewLoc);

                if (loctionReceivers != null && loctionReceivers.get() != null && callBack != null) {
                    loctionReceivers.get().add(callBack);
                }

                locTrackStarted.put(provider, true);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : regForLocationChangeInfo() ", e);
        }
    }


    /**
     * The method performs opposite of regForNetStatChangeInfo
     * <p>
     * The fragment should manually unregister if it registers earlier
     * else the framework would pro-actively unregister it to help it to be GCed
     * </p>
     *
     * @param callBack {@link LocationStatusCallbackListener}
     */

    @Override
    public void unregForLocationChangeInfo(final LocationStatusCallbackListener callBack, final String provider) {
        try {
            if (loctionReceivers != null && loctionReceivers.get() != null) {
                // Stop the listener
                final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.removeUpdates(this);


                if (callBack != null) {
                    loctionReceivers.get().remove(callBack);
                    if (loctionReceivers.get().isEmpty()) {
                        loctionReceivers.clear();
                        loctionReceivers = null;
                    }

                    if (!"".equals(Utils.safeToString(provider))) {
                        locTrackStarted.remove(provider);
                    }
                } else {
                    loctionReceivers.clear();
                    loctionReceivers = null;
                    locTrackStarted.clear();
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : unregForLocationChangeInfo() ", e);
        }
    }

    @Override
    public void unregForNetStatChangeInfo(final NetworkStatusCallbackListener callBack) {
        if (netStatReceivers != null) {
            if (callBack != null) {
                netStatReceivers.remove(callBack);
            } else {
                netStatReceivers.clear();
                netStatReceivers = null;
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (needToResumeWO)
            ifPossibleResumeWorkOrder();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(MOBILE_DATA_STATUS_ACTION);
        filter.addAction(WIFI_STATUS_ACTION);

        this.networkStatusReceiver = new NetworkStatusMonitorReceiver(this);
        // Register broadcast receiver here
        registerReceiver(this.networkStatusReceiver, filter);
        steppersView.getRecyclerView().setHasFixedSize(false);
    }

    @Override
    public void takeSnapshot() {
        camIntReqCode = 1;
        final Intent imageIntent = AndroidUtilsAstSep.takeSnapshot(this);
        try {
            if (imageIntent != null) {
                if (!activeSteps.isEmpty() && workorderCustomWrapperTO != null) {
                    final WorkorderCustomWrapperTO wo = workorderCustomWrapperTO;
                    Log.d("ImageCapture", "Placing work order case id=" + wo.getIdCase());
                    Log.d("ImageCapture", "Placing work order case type id=" + wo.getIdCaseType());
                    Log.d("ImageCapture", "Placing work order field visit id=" + wo.getFieldVisitID());
                    imageIntent.putExtra(ConstantsAstSep.OrderHandlerConstants.WORKORDER_CASE_ID, wo.getIdCase());
                    imageIntent.putExtra(ConstantsAstSep.OrderHandlerConstants.WORKORDER_CASE_TYPE_ID, wo.getIdCaseType());
                    imageIntent.putExtra(ConstantsAstSep.OrderHandlerConstants.FIELD_VISIT_ID, wo.getFieldVisitID());
                } else {
                    Log.d("ImageCapture", "work order is null");
                }

                startActivityForResult(imageIntent, camIntReqCode);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : takeSnapshot() ", e);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void regForImageCaptureInfo(final WorkflowImageCaptureCallbackListener callBack) {
        try {
            Log.d("regForImageCaptureInfo-StandardFlowActivity", "regForImageCaptureInfo-StandardFlowActivity is called");
            if (this.imageListeners == null) {
                Log.d("regForImageCaptureInfo-StandardFlowActivity", "Initializing imagelistener list");
                this.imageListeners
                        = new HashSet<WorkflowImageCaptureCallbackListener>(new HashSet<WorkflowImageCaptureCallbackListener>());
            }

            if (callBack != null) {
                this.imageListeners.add(callBack);
                Log.d("regForImageCaptureInfo", "Image taken callback registered=" + callBack.toString());
                Log.d("regForImageCaptureInfo", "Listener Size=" + this.imageListeners.size());
            }
        } catch (Exception e) {
            writeLog(TAG + "  : regForImageCaptureInfo() ", e);
        }
    }

    @Override
    public void unregForImageCaptureInfo(WorkflowImageCaptureCallbackListener callBack) {

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (loctionReceivers != null
                    && loctionReceivers.get() != null
                    && !loctionReceivers.get().isEmpty()) {

                for (final LocationStatusCallbackListener caller : loctionReceivers.get()) {
                    if (caller != null) {
                        try {
                            caller.onLocationChanged(location);
                        } catch (final Exception ex) {
                            writeLog(TAG + "onLocationChanged", ex);
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : onLocationChanged() ", e);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_logout) {
            showLogoutActivity();
            return true;
        } else if (itemId == R.id.menu_home) {
            showMainMenu();
            return true;
        } else if (itemId == R.id.menu_attach_photo) {
            takeSnapshot();
            return true;
        } else if (itemId == R.id.menu_add_comments) {
            showComments();
            return true;
        } else if (itemId == R.id.menu_info) {
            navigateToOrderDetails(false, null);
            return true;
        }
        return true;
    }

    public void navigateToOrderDetails(boolean allowStartFlow, Long idCase) {
        try {
            if (launcherOrderDetailsCls != null) {
                Log.d("navigateToOrderDetails", "Launcher class is=" + launcherOrderDetailsCls);

                final Intent intent = new Intent(getApplicationContext(), launcherOrderDetailsCls);
                intent.putExtra(ConstantsAstSep.MeterChangeConstants.ALLOW_START_FLOW, allowStartFlow);
                if (idCase != null) {
                    intent.putExtra(ConstantsAstSep.MeterChangeConstants.ID_CASE, idCase);
                } else if (!activeSteps.isEmpty() && workorderCustomWrapperTO != null) {
                    final WorkorderCustomWrapperTO wo = workorderCustomWrapperTO;

                    intent.putExtra(ConstantsAstSep.MeterChangeConstants.ID_CASE, wo.getIdCase());
                }
                intent.putExtra(ConstantsAstSep.MeterChangeConstants.INTENT_FROM, AbstractWokOrderActivity.this.getClass().getSimpleName());
                startActivity(intent);
            } else {
                Log.d("navigateToOrderDetails", "launcher class is null");
            }
        } catch (Exception e) {
            writeLog(TAG + "  : navigateToOrderDetails() ", e);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void showMainMenu() {
        try {
            AlertDialog.Builder builder = GuiController.showInfoDialog(this, getString(R.string.wo_progress_saved_msg));
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final Intent goToIntent = new Intent(AbstractWokOrderActivity.this, ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.MAIN_MENU_ACTIVITY));
                    goToIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goToIntent);
                }
            });
            builder.show();

        } catch (Exception e) {
            writeLog(TAG + "  : showMainMenu() ", e);
        }
    }

    private void showComments() {
        try {
            final Intent commentIntent = new Intent(this, AddCommentActivity.class);
            // Pass the case id for this workorder


            if (!activeSteps.isEmpty() && workorderCustomWrapperTO != null) {
                final WorkorderCustomWrapperTO wo = workorderCustomWrapperTO;
                commentIntent.putExtra(ConstantsAstSep.OrderHandlerConstants.WORKORDER_CASE_ID,
                        wo.getId());
                commentIntent.putExtra(ConstantsAstSep.OrderHandlerConstants.WORKORDER_CASE_TYPE_ID,
                        wo.getIdCaseType());

                startActivity(commentIntent);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : showComments() ", e);
        }
    }

    protected void showLogoutActivity() {
        try {
            final Intent goToIntent = new Intent(this, LogoutActivity.class);
            startActivity(goToIntent);
        } catch (final Exception ex) {
            writeLog(TAG + "showLogoutActivity", ex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void networkStatusChanged(boolean isConnected) {
        try {
            //Set network state icon in the Action bar
            networStateIcon.setVisibility(View.VISIBLE);
            SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
            if (isConnected) {
                networStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_4_bar_white));
            } else {
                networStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_null_black));
            }

            if (netStatReceivers != null
                    && !netStatReceivers.isEmpty()) {
                // Can be implemented using JMX also
                for (final NetworkStatusCallbackListener listener : netStatReceivers) {
                    if (listener != null) {
                        /*
                         *  Need to ensure that if there is any error
                         *  while processing this by 1 fragment (any project specific code)
                         *  this does not interrupt the others
                         */
                        try {
                            listener.networkStatusChanged(isConnected);
                        } catch (final Exception ex) {
                            writeLog(TAG + "networkStatusChanged", ex);
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            writeLog(TAG + "networkStatusChanged", ex);
        }

    }

    public void saveWorkOrderStateForResume() {
        try {
            activeStepIdentifiers = new ArrayList<>();
            String stepId = null;
            for (int i = 0; i < activeSteps.size(); i++) {
                stepId = activeSteps.get(i).getSteppersItemIdentifier();
                activeStepIdentifiers.add(i, stepId);
            }
            Gson gson = new Gson();
            String value = "'" + gson.toJson(activeStepIdentifiers) + "'";
            DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
            databaseHandler.updateStatusforWorkOrder(workorderCustomWrapperTO.getIdCase(), currentPosition, value);
        } catch (Exception e) {
            writeLog(TAG + "  : saveWorkOrderStateForResume() ", e);
        }
    }

    public int resumeWorkOrderFromSavedState(int presentStep) {
        //Add all previously completed steps to activeStep, fragments to fragmentmanager, applychangesToModifiableWO() from all completed steps.
        try {
            SteppersItem stepItem;


            for (String s : activeStepIdentifiers) {
                stepItem = stepIdWithStepItem.get(s);
                if (stepItem == null) {
                    Toast.makeText(getApplicationContext(), "For Developer :You have specified wrong step id while adding to arraymap", Toast.LENGTH_SHORT);
                }
                activeSteps.add(stepItem);

            }
            getSteppersView().getSteppersAdapter().notifyDataSetChanged();

            for (int i = 0; i < presentStep; i++) {
                CustomFragment fragment = (CustomFragment) activeSteps.get(i).getFragment();
                steppersViewConfig.getFragmentManager().beginTransaction().add(fragment, activeSteps.get(i).getSteppersItemIdentifier()).commitNow();
                getSteppersView().getSteppersAdapter().changeToStep(i);
                activeSteps.get(i).setDone(true);
                steppersViewConfig.getFragmentManager().executePendingTransactions();

            }

            workorderCustomWrapperTO =
                    CommunicationHelper.JSONMAPPER.readValue(
                    DatabaseHandler.createDatabaseHandler().getStepWO(workorderCustomWrapperTOUntouched.getIdCase().toString(),
                            activeSteps.get(presentStep-1).getSteppersItemIdentifier()),
                            WorkorderCustomWrapperTO.class);
        } catch (Exception e) {
            writeLog(TAG + "  : resumeWorkOrderFromSavedState() ", e);
        }
        return presentStep;
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == findViewById(R.id.textViewLabel)) {
            int i = 0;
        }
        return false;
    }

    public static class SharedDataConstants {
        public static final String UNIT_INFORMATION_TO = "UNIT_INFORMATION_TO";
    }
}
