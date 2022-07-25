package com.capgemini.sesp.ast.android.ui.activity.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.cache.WorkorderCache;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.MeterChangeConstants;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.OrderHandlerConstants;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.activity.navigation.MapNavigation;
import com.capgemini.sesp.ast.android.ui.activity.navigation.MapsActivity;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderListActivity;
import com.capgemini.sesp.ast.android.ui.activity.receiver.NetworkStatusMonitorReceiver;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.activity.ConcentratorInstallation;
import com.capgemini.sesp.ast.android.ui.wo.activity.EvSmartChargingMeterInstallationCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.EvSmartChargingMeterInstallationDM;
import com.capgemini.sesp.ast.android.ui.wo.activity.EvTroubleshootMeasurePointDM;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterChangeCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterChangeCTRollout;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterChangeDM;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterChangeDMRollout;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterInstallationCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterInstallationDM;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterRemovalCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterRemovalDM;
import com.capgemini.sesp.ast.android.ui.wo.activity.RepeaterInstallation;
import com.capgemini.sesp.ast.android.ui.wo.activity.SolarPanelInstallation;
import com.capgemini.sesp.ast.android.ui.wo.activity.TroubleShootSolar;
import com.capgemini.sesp.ast.android.ui.wo.activity.TroubleshootMeasurePointCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.TroubleshootMeasurePointDM;
import com.capgemini.sesp.ast.android.ui.wo.activity.TroubleshootMultipointConcentrator;
import com.capgemini.sesp.ast.android.ui.wo.activity.TroubleshootMultipointRepeater;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.CaseTCustomTO;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.LockWorkorderCustomTO;

import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * All workorder handler would extend this class
 * <p/>
 * <p>
 * This abstract class would also provide easy to use implementation
 * facilities such as handling the task when the work-order start button is clicked,
 * image capture icon is clicked etc.
 * </p>
 *
 * @author Capgemini
 * @version 1.0
 */
@SuppressLint("InflateParams")
@SuppressWarnings("deprecation")
public abstract class AbstractOrderDetailsActivity extends AppCompatActivity implements DialogInterface.OnClickListener, OnClickListener, NetworkStatusCallbackListener {
    private boolean allowStartFlow = false;
    protected WorkorderCustomWrapperTO wo = null;
    protected transient int camIntReqCode = -1;
    protected transient Class<?> implClass = null;

    private transient Dialog alertDialog = null;

    private transient boolean proceedInOffline = false;

    protected Date startOrderTimeDate = null;
    protected Date startTravelTimeDate = null;
    protected Long travelTimeInMinutes = null;
    private ProgressDialog progressDialog = null;
    private LinearLayout footerLayout;
    public static final String INTENT_KEY_ID_CASE_T = "idCaseT";


    // Log tag used for this class
    private transient final String TAG = "AbstractOrderDetails";

    /*
     * Local broadcast receiver used to
     * receive network connectivity information asynchronously
     */
    private transient BroadcastReceiver networkStatusReceiver = null;
    private ImageView networStateIcon;
    protected boolean continueFlag;

    @Override
    public void networkStatusChanged(boolean isConnected) {
        networStateIcon.setVisibility(View.VISIBLE);
        SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
        if (isConnected) {
            networStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_4_bar_white));
        } else {
            networStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_null_black));
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display_order_details);
            startTravelTimeDate = new Date();
            footerLayout = findViewById(R.id.footer_layout);
            fetchDBandSetUI(getIntent());

            setCustomActionBar();
        } catch (Exception e) {
            writeLog(TAG + " : onCreate() ", e);
        }
    }

    private void setCustomActionBar() {
        // Hiding the logo as requested
        try {
            getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(false);

        /*
         * Setting up custom action bar view
		 */
            final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
            ImageButton help_btn = vw.findViewById(R.id.menu_help);		
            help_btn.setOnClickListener(new View.OnClickListener() {		
                @Override		
                public void onClick(View v) {		
                    final Dialog dialog = new HelpDialog(AbstractOrderDetailsActivity.this, ConstantsAstSep.HelpDocumentConstant.PAGE_ORDER_INFO);		
                    dialog.show();		
                }		
            });
            TextView title = vw.findViewById(R.id.title_text);
            title.setText(getResources().getString(R.string.order_information));

            // -- Customizing the action bar ends -----

            final View localeFlag = vw.findViewById(R.id.save_btn);
            localeFlag.setVisibility(View.INVISIBLE);
            networStateIcon = vw.findViewById(R.id.network_state_iv);
            getSupportActionBar().setCustomView(vw, layout);
            getSupportActionBar().setDisplayShowCustomEnabled(true);

        } catch (Exception e) {
            writeLog(TAG + " : setCustomActionBar() ", e);
        }
    }


    private void fetchDBandSetUI(final Intent intent) {
        //Start an async task to fetch Db contents and set data in onPostexecute method
        try {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    showProgressDialog();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    if (intent.getExtras() != null) {
                        Long idCase = intent.getExtras().getLong(MeterChangeConstants.ID_CASE);
                        allowStartFlow = intent.getExtras().getBoolean(MeterChangeConstants.ALLOW_START_FLOW);
                        wo = WorkorderCache.getWorkorderByCaseId(idCase, ApplicationAstSep.workOrderClass);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    progressDialog.dismiss();
                    setUpView();
                    ImageButton btn = findViewById(R.id.startButton);
                    if (!allowStartFlow) {

                        btn.setVisibility(View.GONE);
                    } else {
                        btn.setVisibility(View.VISIBLE);
                    }
                    ImageButton unlockbtn = findViewById(R.id.unlockButton);
                    if ((!SessionState.getInstance().isLoggedInOnline())
                            || (wo.getLockUserName() == null)
                            || (!SessionState.getInstance().getCurrentUserUsername().equalsIgnoreCase(wo.getLockUserName())) || (!allowStartFlow)) {

                        unlockbtn.setVisibility(View.INVISIBLE);
                    } else {
                        unlockbtn.setVisibility(View.VISIBLE);
                    }
                    if (!SessionState.getInstance().isLoggedInOnline() && allowStartFlow) {
                        AlertDialog dialog = GuiController.showConfirmDialog(AbstractOrderDetailsActivity.this,
                                getResources().getString(R.string.start_order_warning),
                                getResources().getString(R.string.warning)
                        );
                        dialog.setButton(getResources().getString(R.string.continue2), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }
                        );
                        dialog.setButton2(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                }
                        );
                        if (wo.getLockUserName() == null) {
                            dialog.setMessage(getResources().getString(R.string.work_order_lock_message));
                            dialog.setTitle(getResources().getString(R.string.work_order_lock));
                            dialog.show();
                        }
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            writeLog(TAG + " fetchDBandSetUI() ", e);
        }
    }

    private void showProgressDialog() {
        try {
            progressDialog = new ProgressDialog(AbstractOrderDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
            // Load the custom title view
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customTitleLayout = inflater.inflate(R.layout.progress_bar_custom_layout, null);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCustomTitle(customTitleLayout);
            ((TextView) customTitleLayout.findViewById(R.id.progressVwId)).setText(R.string.loading);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        } catch (Exception e) {
            writeLog(TAG + "  : showProgressDialog() ", e);
        }
    }


    /**
     * This method would be called by android OS
     * when the user has taken the image and clicked on yes sign
     * or "save" button
     *
     * @param requestCode {@link Integer}
     * @param resultCode  {@link Integer}
     * @param imageData   {@link Intent}
     */

      @Override
    public void onActivityResult(final int requestCode,
                                 final int resultCode, final Intent imageData) {
          /*
           * Check the request code first
           *
           * If it happens that the request code is different then
           * either android has restarted the activity or its following a UI configuration change
           */
          if (requestCode == this.camIntReqCode
                  && resultCode == RESULT_OK) {
              final Intent imageIntent = AndroidUtilsAstSep.getWoImageGalleryIntent(this);
              if (imageIntent != null) {
                  // Launch the image handler activity
                  startActivity(imageIntent);
              }
          }
      }

    public WorkorderCustomWrapperTO getWorkorder() {
        return wo;
    }

    protected abstract void setUpView();


    /**
     * Once the workorder is locked and STARTED work order event is added
     * then only proceed else show error and retry option
     *
     * @param wo
     */

    private void saveWorkorderToCache(final WorkorderCustomWrapperTO wo) {
        try {
            WorkorderCache.saveWorkorderToCache(wo);
        } catch (Exception e) {
            writeLog(TAG + "  : saveWorkorderToCache() ", e);
        }
    }


    /**
     * Once the workorder is locked and STARTED work order event is added
     * then only proceed else show error and retry option
     *
     * @param wo
     * @param pageIndex
     */

    private void launchFlowActivity(final WorkorderCustomWrapperTO wo,
                                    final Integer pageIndex) {
        try {
            Intent flowActivityIntent;


                startOrderTimeDate = new Date();
                SESPPreferenceUtil.savePreference(ConstantsAstSep.SharedPreferenceKeys.START_ORDER_TIME, startOrderTimeDate.getTime());
                SESPPreferenceUtil.savePreference(ConstantsAstSep.SharedPreferenceKeys.START_TRAVEL_TIME, startTravelTimeDate.getTime());

                startOrderTimeDate = new Date(SESPPreferenceUtil.getPreference(ConstantsAstSep.SharedPreferenceKeys.START_ORDER_TIME));
                startTravelTimeDate = new Date(SESPPreferenceUtil.getPreference(ConstantsAstSep.SharedPreferenceKeys.START_TRAVEL_TIME));


                travelTimeInMinutes = new Long((startOrderTimeDate.getTime() - startTravelTimeDate.getTime()) / 1000 / 60);
                WorkorderUtils.setFieldVisit(wo, startTravelTimeDate, null, travelTimeInMinutes, null);

                String caseId = String.valueOf(wo.getId());
                DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
                ArrayMap<String, Object> savedState = databaseHandler.checkStatusforWorkOrder(caseId);
                ArrayList<String> activeStepIdentifiers = new ArrayList<>();
                int presentStep = (Integer) savedState.get("PRESENT_STEP");


                if (!"0".equalsIgnoreCase(String.valueOf(savedState.get("PRESENT_STEP")))) {

                    AlertDialog dialog = null;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Want To Reset Or Continue")
                            .setTitle("Confirm");
                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            continueFlag = true;
                            dialog.dismiss();
                            postSelection();
                        }
                    });
                    builder.setNegativeButton("Reset", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            continueFlag = false;
                            dialog.dismiss();
                            postSelection();
                        }
                    });

                    dialog = builder.create();
                    dialog.show();

                } else
                    postSelection();

        } catch (Exception e) {
            writeLog(TAG + "  : launchFlowActivity() ", e);
        }

    }

    protected void postSelection() {


        Long caseTHandlerT = (Long) ObjectCache.getIdObject(CaseTCustomTO.class,wo.getIdCaseType()).getIdCaseTHandlerT();

        try {
            Intent flowActivityIntent;

            if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__METER_INSTALLATION_DM_FIELD) {
                    AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                    flowActivityIntent = new Intent(this, MeterInstallationDM.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__METER_REMOVAL_CT_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, MeterRemovalCT.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__TS_MUP_REPEATER_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, TroubleshootMultipointRepeater.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__METER_INSTALLATION_CT_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, MeterInstallationCT.class); //change
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__METER_REMOVAL_DM_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, MeterRemovalDM.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__METER_CHANGE_CT_ROLLOUT_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, MeterChangeCTRollout.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__METER_CHANGE_DM_ROLLOUT_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, MeterChangeDMRollout.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__METER_CHANGE_CT_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, MeterChangeCT.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__METER_CHANGE_DM_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, MeterChangeDM.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__CONC_INSTALLATION_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, ConcentratorInstallation.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__TS_MEP_CT_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, TroubleshootMeasurePointCT.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__TS_MEP_DM_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, TroubleshootMeasurePointDM.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__REPEATER_INSTALLATION_FIELD) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, RepeaterInstallation.class);
            }
            //caseTHandlerT values greater than 127 should be compared with .equals()
            else if ((caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__SOLAR_PANEL_INSTALLATION_FIELD) ||
            (caseTHandlerT.equals(AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__SOLAR_PANEL_INSTALLATION_FIELD))) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, SolarPanelInstallation.class);
            }
            /*EV METER*/
            else if(((caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__EV_CHARGING_STATION_DM_FIELD)) ||
                    caseTHandlerT.equals(AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__EV_CHARGING_STATION_DM_FIELD)) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, EvSmartChargingMeterInstallationDM.class);
            }
            else if(((caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__EV_CHARGING_STATION_CT_FIELD)) ||
                    caseTHandlerT.equals(AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__EV_CHARGING_STATION_CT_FIELD)) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, EvSmartChargingMeterInstallationCT.class);
            }
            else if(((caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__EV_TS_CHARGING_STATION_DM_FIELD)) ||
                    caseTHandlerT.equals(AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__EV_TS_CHARGING_STATION_DM_FIELD)) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, EvTroubleshootMeasurePointDM.class);
            }
            else if(((caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__EV_TS_CHARGING_STATION_CT_FIELD)) ||
                    caseTHandlerT.equals(AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__EV_TS_CHARGING_STATION_CT_FIELD)) {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this, EvTroubleshootMeasurePointDM.class);
            }
            else if (caseTHandlerT == AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__TS_SP_INST_FIELD
                    || caseTHandlerT.equals(AndroidUtilsAstSep.CONSTANTS().CASE_T_HANDLER_T__TS_SP_INST_FIELD)){
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this,TroubleShootSolar.class);
            }
                else {
                AbstractWokOrderActivity.setWorkorderCustomWrapperTO(wo);
                flowActivityIntent = new Intent(this,TroubleshootMultipointConcentrator.class);
            }

//            flowActivityIntent.putExtra(OrderHandlerConstants.WORKORDER_DATA, wo);
            flowActivityIntent.putExtra("ContinueFlag", continueFlag);

            if (implClass != null) {
                flowActivityIntent.putExtra(OrderHandlerConstants.ORDERDETAILSACTIVITYCLASS, implClass);
            }

            startActivity(flowActivityIntent);
        } catch (Exception e) {
            writeLog(TAG + "  : postSelection() ", e);
        }
    }

    /**
     * The method will show the user that due to some error
     * the workorder cannot be locked, Does he/she want to retry?
     *
     * @param to {@link WorkorderCustomWrapperTO}
     * @param ex {@link Exception}
     */

    private void retryWorkorderLock(final WorkorderCustomWrapperTO to, final Exception ex, boolean lockFlag) {

        try {
            if (alertDialog != null) {
                final TextView tv = alertDialog.findViewById(R.id.lockUnlockWo);
                if (tv != null) {
                    if (lockFlag) {
                        tv.setText(getResources().getString(R.string.unable_to_lock_tech_err));
                    } else {
                        tv.setText(getResources().getString(R.string.unable_to_lock_tech_err));
                    }
                }
                // Enable the retry button
                final Button retryButton = alertDialog.findViewById(R.id.retryButton);
                if (retryButton != null) {
                    retryButton.setEnabled(true);
                }
            }

        } catch (Exception e) {
            writeLog(TAG + "  : retryWorkorderLock() ", e);
        }

    }

    /**
     * The workorder is already locked by a different user
     * Show this message to user
     *
     * @param to
     * @param lockingUserName
     */

    private void showWoDifferentUserLocked(final WorkorderCustomWrapperTO to,
                                           final String lockingUserName, final boolean lockFlag) {
        try {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }

            if (lockFlag) {
                final View vw
                        = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.locking_wo, null);

                if (vw != null) {
                    final Button cancelButton = vw.findViewById(R.id.cancelButton);
                    final Button retryButton = vw.findViewById(R.id.retryButton);

                    if (cancelButton != null && retryButton != null) {
                        retryButton.setEnabled(false);
                        cancelButton.setOnClickListener(this);
                    }

                    final TextView lockUnlockWoTv = vw.findViewById(R.id.lockUnlockWo);
                    if (lockUnlockWoTv != null && lockingUserName != null) {
                        lockUnlockWoTv.setText("Workorder already locked by " + lockingUserName);
                    }

                    final AlertDialog.Builder builder
                            = GuiController.getCustomAlertDialog(this, vw, null, null);

                    if (builder != null) {
                        alertDialog = builder.create();
                        alertDialog.setCancelable(true);
                        alertDialog.show();
                    }
                }
            } else {
                finish();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : showWoDifferentUserLocked() ", e);
        }
    }

    /**
     * @param wo {@link WorkorderCustomWrapperTO}
     */

    private void showPopupAndLockWo(final WorkorderCustomWrapperTO wo, boolean lockFlag,
                                    boolean offlineMode) {
        try {
            if (offlineMode) {
                // No possibility of locking workorder
                launchFlowActivity(wo, null);
                allowStartFlow = false;
            } else {
                // Connected to network
                new WoLockUnLockThread(this, wo, lockFlag).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            writeLog(TAG + "  : showPopupAndLockWo() ", e);
        }
    }

    /**
     * Method added to lock/unlock workorder
     *
     * @param wo   {@link WorkorderCustomWrapperTO}
     * @param lock {@link Boolean}
     */

    private LockWorkorderCustomTO lockUnlockWo(final WorkorderCustomWrapperTO wo, final boolean lock) throws Exception {
        // Show loading popup here and try to lock the case/workorder
        LockWorkorderCustomTO customTO = null;
        try {
            if (wo != null) {
                final LockWorkorderCustomTO to = new LockWorkorderCustomTO(SessionState.getInstance().getCurrentUser());
                to.setIdCase(wo.getIdCase());
                if (lock) {
                    customTO = AndroidUtilsAstSep.getDelegate().lockWorkorder(to, lock);
                } else {
                    AndroidUtilsAstSep.getDelegate().lockWorkorder(to, lock);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : lockUnlockWo() ", e);
        }
        return customTO;
    }

    public void startOrderButtonClicked(final View view) {
        try {
                proceedWithLaunchflow();

        } catch (Exception e) {
            writeLog(TAG + "  : startOrderButtonClicked() ", e);
        }


    }


    private void proceedWithLaunchflow() {
        try {
            if (SessionState.getInstance().isLoggedInOnline()) {
                if (wo.getLockUserName() != null) {
                    if (!wo.getLockUserName().equalsIgnoreCase(SessionState.getInstance().getCurrentUserUsername())) {
                        final View alertView
                                = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.wo_already_locked_diff_user_alert, null);
                        TextView lockUsertv = alertView.findViewById(R.id.lockUserTv);
                        lockUsertv.setText(wo.getLockUserName());
                        if (alertView != null) {

                            final Button okButton = alertView.findViewById(R.id.okButton);
                            okButton.setOnClickListener(this);

                            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(this,
                                    alertView, null, null);
                            alertDialog = builder.create();
                            alertDialog.show();
                        }
                    } else {
                        allowStartFlow = false;
                        launchFlowActivity(wo, null);
                    }
                } else {
                    showPopupAndLockWo(wo, true, false);
                }
            } else if (!proceedInOffline) {
                getOfflineWoStartConfirmation(wo);
            } else {
                proceedInOffline = false;
                proceedWithLaunchflow();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : proceedWithLaunchflow() ", e);
        }
    }

    public void unlockButtonClicked(final View view) {
        try {
            final boolean netAvailble = AndroidUtilsAstSep.isNetworkAvailable();
            if (netAvailble) {
                showPopupAndLockWo(wo, false, false);
            }
        } catch (Exception exc) {
            writeLog(TAG + " : WoLockUnLockThread()", exc);
        }
    }

    /**
     * Displaying map on click of button
     */
    public void mapButtonClicked(final View vw) {
        try {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            getCustomerLatLong(mapIntent);
            startActivity(mapIntent);
        } catch (Exception e) {
            writeLog(TAG + ": mapButtonClicked()", e);
        }
    }

    /**
     * Display the map with route between current location and customer location
     */
    public void displayNavigation(final View vw) {
        try {
            if (AndroidUtilsAstSep.isNetworkAvailable()) {

                Intent mapRouteIntent = new Intent(this, MapNavigation.class);
                getCustomerLatLong(mapRouteIntent);
                startActivity(mapRouteIntent);

            } else
                Toast.makeText(this, this.getString(R.string.turn_on_internet), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            writeLog(TAG + ": displayNavigation()", e);
        }
    }

    /**
     * Fetching customer location latitude and longitude and starting the activity
     *
     * @param intent
     */
    private void getCustomerLatLong(Intent intent) {
        try {
            // Existing x coordinate
            if (Utils.isNotEmpty(wo.getWgs84Latitude())) {
                intent.putExtra("customerLatitude", Double.parseDouble(wo.getWgs84Latitude()));
            }
            // Existing y coordinate
            if (Utils.isNotEmpty(wo.getWgs86Longitude())) {
                intent.putExtra("customerLongitude", Double.parseDouble(wo.getWgs86Longitude()));
            }
        } catch (Exception e) {
            writeLog(TAG + ": getCustomerLatLong()", e);
        }
    }


    /**
     * Method is called alerting the user that
     * he/she is not connected to network and opting to start the work-order
     *
     * @param woWrapper
     */

    private void getOfflineWoStartConfirmation(final WorkorderCustomWrapperTO woWrapper) {

        try {
            final View titleView = getLayoutInflater().inflate(R.layout.layout_warning_unfinished_wo_title, null);

            final View bodyView = getLayoutInflater().inflate(R.layout.warning_offline_wo_start_confirmation, null);

            final Button continueButton = bodyView.findViewById(R.id.continueOfflineStartWoButton);
            final Button closeOfflinePopupBtn = bodyView.findViewById(R.id.closeOfflineWoWarningBtn);

            // When clicked on continue button the workorder would be started in offline mode
            if (continueButton != null) {
                continueButton.setOnClickListener(this);
            }

            // When clicked on reset button the warning dialog will be closed simply
            if (closeOfflinePopupBtn != null) {
                closeOfflinePopupBtn.setOnClickListener(this);
            }

            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(this, bodyView, null, null);

            if (builder != null) {
                builder.setCustomTitle(titleView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        } catch (Exception e) {
            writeLog(TAG + ": getOfflineWoStartConfirmation()", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        this.networkStatusReceiver = new NetworkStatusMonitorReceiver(this);
        // Register broadcast receiver here
        registerReceiver(this.networkStatusReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // De-register the broadcast receiver here
        if (this.networkStatusReceiver != null) {
            unregisterReceiver(this.networkStatusReceiver);
        }
    }

    @Override
    public void onClick(final View vw) {
        try{
        if (vw != null && vw.getId() == R.id.continueButton) {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            proceedWithLaunchflow();
        } else if (vw != null && vw.getId() == R.id.resetButton) {
            DatabaseHandler.createDatabaseHandler().clearPageVisitsForCase(wo.getIdCase());
            DatabaseHandler.createDatabaseHandler().removeAttchment(wo.getIdCase(), wo.getIdCaseType(), "", 0, null);
            //FlowEngine.getInstance().getVisitedFlowPages().clear();
            //FlowEngine.getInstance().getPagePreferences().clear();
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            startOrderButtonClicked(null);
        } else if (vw != null && vw.getId() == R.id.closeOfflineWoWarningBtn) {
            // User has selected to back out from proceeding with the WO in offline mode
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            proceedInOffline = false;
        } else if (vw != null && vw.getId() == R.id.continueOfflineStartWoButton) {
            // User has opted to continue with the workorder even if in offline mode
            proceedInOffline = true;
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            showPopupAndLockWo(wo, true, true);
        } else if (vw != null && vw.getId() == R.id.okButton) {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        }
        } catch (Exception e) {
            writeLog(TAG + " onClick() ", e);
        }
    }

    /**
     *
     */


    @Override
    public void onBackPressed() {
        try{
        if (allowStartFlow) {
            final AlertDialog.Builder builder =
                    GuiController.getCustomAlertDialog(this, R.layout.wo_return_warning,
                            getResources().getString(R.string.start_order_warning),
                            getResources().getString(R.string.warning));

            final Context currentContext = this;
            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //showOrderSummaryActivity();
                    showOrderListActivity();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.create().show();
        } else {
            finish();
        }
        } catch (Exception e) {
            writeLog(TAG + " onBackPressed() ", e);
        }
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        try{
        if (!SessionState.getInstance().isLoggedInOnline()) {
            dialog.dismiss();
            AlertDialog dialog2 = GuiController.showConfirmDialog(AbstractOrderDetailsActivity.this,
                    getResources().getString(R.string.start_order_warning),
                    getResources().getString(R.string.warning));
            dialog2.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            dialog2.setButton2(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            dialog2.setMessage(getResources().getString(R.string.start_order_offline_warning));
            dialog2.setTitle(getResources().getString(R.string.warning));
            dialog2.show();
        } else
            finish();
        } catch (Exception e) {
            writeLog(TAG + " onClick() ", e);
        }
    }


    /**
     * The async task to call web service to lock/unlock the workorder
     *
     * @author nirmchak
     */

    private class WoLockUnLockThread extends AsyncTask<Void, Void, LockWorkorderCustomTO> {

        //private transient SoftReference<Context> contextRef = null;
        private transient boolean running = true;
        private transient boolean lockFlag = false;
        private transient WorkorderCustomWrapperTO wo = null;
        private transient Exception wsCallException = null;
        private transient Context mContext;

        public WoLockUnLockThread(final Context ctx, final WorkorderCustomWrapperTO wo, final boolean lockFlag) {
            this.lockFlag = lockFlag;
            this.wo = wo;
            this.mContext = ctx;
        }

        @Override
        protected void onPreExecute() {
            try {
                final View vw
                        = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.locking_wo, null);

                if (vw != null) {
                    final Button cancelButton = vw.findViewById(R.id.cancelButton);
                    final Button retryButton = vw.findViewById(R.id.retryButton);

                    if (cancelButton != null && retryButton != null) {
                        cancelButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // cancel locking workorder
                                if (alertDialog != null) {
                                    alertDialog.dismiss();
                                }
                                cancel(true);
                            }
                        });
                        retryButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Retry button is called
                                // Close the existing popup first
                                if (alertDialog != null) {
                                    alertDialog.dismiss();
                                }
                                showPopupAndLockWo(wo, true, true);
                            }
                        });
                        retryButton.setEnabled(false);
                    }

                    if (!lockFlag) {
                        // Show unlocking message
                        ((TextView) vw.findViewById(R.id.lockUnlockWo)).setText(getResources().getString(R.string.unlocking_workorder));
                    }

                    final AlertDialog.Builder builder
                            = GuiController.getCustomAlertDialog(mContext, vw, null, null);

                    if (builder != null) {
                        alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                }
            } catch (Exception e) {
                writeLog(TAG + "  onPreExecute() ", e);
            }
        }

        @Override
        protected void onCancelled(final LockWorkorderCustomTO customTO) {
            try {
                lockUnlockWo(wo, false);
            } catch (final Exception e1) {
                wsCallException = e1;
                writeLog(TAG + " :onCancelled()  : WoLockUnLockThread()", e1);
            }
        }


        @Override
        protected LockWorkorderCustomTO doInBackground(final Void... params) {

            try {
                while (!isCancelled()) {
                    try {
                        LockWorkorderCustomTO customTO = null;
                        customTO = lockUnlockWo(wo, this.lockFlag);
                        return customTO;
                    } catch (final Exception e1) {
                        wsCallException = e1;
                        writeLog(TAG + " :doInBackground()  : WoLockUnLockThread()", e1);
                    }
                }
            } catch (Exception e) {
                writeLog(TAG + "  onPostExecute() ", e);
            }
            return null;
        }


        @Override
        public void onPostExecute(final LockWorkorderCustomTO customTO) {
            try {
                if (wsCallException != null) {
                    retryWorkorderLock(wo, wsCallException, lockFlag);
                }
                if (this.lockFlag) {
                    if (customTO != null
                            && customTO.getLockedByUserName() != null
                            && !SessionState.getInstance().getCurrentUserUsername().equalsIgnoreCase(customTO.getLockedByUserName())) {
                        showWoDifferentUserLocked(wo, customTO.getLockedByUserName(), this.lockFlag);
                    } else {
                        wo.setLockUserName(customTO.getLockedByUserName());
                        saveWorkorderToCache(wo);
                        allowStartFlow = false;
                        alertDialog.dismiss();
                        launchFlowActivity(wo, null);
                    }
                } else {
                    wo.setLockUserName(null);
                    saveWorkorderToCache(wo);
                    showOrderSummaryActivity();
                }

            } catch (Exception e) {
                writeLog(TAG + "  onPostExecute() ", e);
            }
        }
    }


    /**
     * Once the current work-order is completed
     * go back to the order summary page
     */

    private void showOrderSummaryActivity() {
        try {
            AndroidUtilsAstSep.launchExplicitActivity(ApplicationAstSep.context,
                    ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.ORDER_SUMMARY_ACTIVITY), Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            writeLog(TAG + "  showOrderSummaryActivity() ", e);
        }
    }

    /**
     * It will order list activity
     * Once press back button from Start Activity page
     */
    protected void showOrderListActivity() {
        try {
            Intent intent = new Intent(this, OrderListActivity.class);
            intent.putExtra(INTENT_KEY_ID_CASE_T, wo.getIdCaseType());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            writeLog(TAG + "  showOrderListActivity() ", e);
        }
    }

    @Override
    public File getFilesDir() {
        return super.getFilesDir();
    }

    public void hideFooterLayout() {
        footerLayout.setVisibility(View.GONE);
    }

    public void showFooterLayout() {
        footerLayout.setVisibility(View.VISIBLE);
    }


}
