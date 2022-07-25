package com.capgemini.sesp.ast.android.ui.activity.dashboard;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.location_track.CallWebServiceInBackGround;
import com.capgemini.sesp.ast.android.module.location_track.LocationTrack;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.ui.activity.USBSerialCommunication.UsbSerialCommunicationActivity;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.login.LoginActivity;
import com.capgemini.sesp.ast.android.ui.activity.login.LogoutActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_list.MaterialListActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.MaterialLogisticsActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.SESPMaterialLogisticsSettingsActivity;
import com.capgemini.sesp.ast.android.ui.activity.navigation.NavigationListActivity;
import com.capgemini.sesp.ast.android.ui.activity.receiver.NetworkStatusMonitorReceiver;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.capgemini.sesp.ast.android.ui.service.DownloadTypeDataIntentService;
import com.capgemini.sesp.ast.android.ui.service.DownloadWOIntentService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skvader.rsp.ast_sep.common.mobile.bean.FCMTokenTO;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, NetworkStatusCallbackListener {

    static final String EXTRA_MAP = "map";
    public static final String TAG = DashboardActivity.class.getSimpleName();
    private static boolean dialogShowed = false;
    private ImageView networStateIcon;
    public static Boolean downloadFinished = false;
    Dialog dialog = null;
    TextView textViewForTypeDataAndWorkOrder;
    private ProgressBar downloadSpinner;
    public  static Boolean typeDataDownloadFinished = false;
    public  static Boolean woDownloadFinished = false;

    /*
     * Local broadcast receiver used to
     * receive network connectivity information asynchronously
     */
    private transient BroadcastReceiver networkStatusReceiver = null;

    /**
     * The popup window needs to be referred
     * outside from its parent method. Hence
     * moved to instance scope
     */


    static final LauncherIcon[] ICONS = {
            new LauncherIcon(R.drawable.ic_dashboard_wo_24dp, "WorkOrder", "ic_work_order_image.png"),
            new LauncherIcon(R.drawable.ic_dashboard_navigation_24dp, "Navigation", "navigation.png"),
            new LauncherIcon(R.drawable.ic_dashboard_materiallist_24dp, "Material List", "ic_material_list_image.png"),
            new LauncherIcon(R.drawable.ic_dashboard_shipping_24dp, "Material Logistics", "ic_material_logistics_image.png"),
            new LauncherIcon(R.drawable.ic_dashboard_printer_24dp, "Printer", "ic_printer_image.png"),
            new LauncherIcon(R.drawable.ic_dashboard_comm_24dp, " Communication", "ic_communication_image.png")};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_dashboard);

            // Hiding the logo as requested
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
                    final Dialog dialog = new HelpDialog(DashboardActivity.this,ConstantsAstSep.HelpDocumentConstant.MAIN_MENU);
                    dialog.show();
                }
            });

            // -- Customizing the action bar ends -----

            final View localeFlag = vw.findViewById(R.id.save_btn);

            if (localeFlag != null) {
                localeFlag.setVisibility(View.INVISIBLE);
            }

            networStateIcon = vw.findViewById(R.id.network_state_iv);
            getSupportActionBar().setCustomView(vw);
            getSupportActionBar().setDisplayShowCustomEnabled(true);


            TextView txtTitleBar = findViewById(R.id.title_text);
            txtTitleBar.setText(R.string.title_activity_main_menu);

            SESPPreferenceUtil.savePreference(ConstantsAstSep.SharedPreferenceKeys.USER_LOGGED_OUT,false);
            if (!SessionState.getInstance().isLoggedInOnline() && !dialogShowed) {
                AlertDialog.Builder builder = GuiController.showInfoDialog(this, getString(R.string.offline_login), getString(R.string.offline_mode_warning));
                builder.show();
                dialogShowed = true;
            }

            FirebaseApp.initializeApp(this);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            String token = task.getResult().getToken();
                            // Log and toast
                            String msg =  token;
                            Log.d(TAG, msg);

                            if (SESPPreferenceUtil.getPreferenceString("FCM_KEY").equals(token)){
                                return;
                            }
                            FCMTokenTO fcmTokenTO = new FCMTokenTO();
                            fcmTokenTO.setIdUser(SessionState.getInstance().getCurrentUser().getId());
                            fcmTokenTO.setToken(token);
                            fcmTokenTO.setDeviceSerialId(Build.SERIAL != Build.UNKNOWN ? Build.SERIAL : Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                            CallWebServiceInBackGround callWebServiceInBackGround =
                                    new CallWebServiceInBackGround();
                            callWebServiceInBackGround.returnValue = new CallWebServiceInBackGround.ReturnValue() {
                                @Override
                                public Void onReturnResult(Object o) {
                                    if (!(o instanceof Exception))
                                        SESPPreferenceUtil.savePreference("FCM_KEY",token);
                                    return null;
                                }
                            };
                            callWebServiceInBackGround.execute("updateFCMToken",
                                    FCMTokenTO.class,fcmTokenTO);
                        }
                    });

            downloadSpinner=(ProgressBar)findViewById(R.id.downloadProgressBar);
            downloadSpinner.setVisibility(View.VISIBLE);
            //  Initialising the Grid View
            GridView dashboardGridview = findViewById(R.id.dashboard_grid);
            dashboardGridview.setAdapter(new ImageAdapter(this));
            dashboardGridview.setOnItemClickListener(this);
            textViewForTypeDataAndWorkOrder = (TextView) findViewById(R.id.textViewForTypeDataAndWO);
            //  Hack to disable GridView scrolling
            dashboardGridview.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return event.getAction() == MotionEvent.ACTION_MOVE;
                }
            });
        } catch (Exception e) {
            writeLog(TAG + " :onCreate()", e);
        }
    }

    /**
     * Method to perform close/cleanup
     * operation when the navigation back button is clicked
     * by user
     */


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.d("DashboardActivity", "onResume is called");
            final IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            this.networkStatusReceiver = new NetworkStatusMonitorReceiver(this);
            // Register broadcast receiver here
            registerReceiver(this.networkStatusReceiver, filter);
            LanguageHelper.reloadIfLanguageChanged(this);
            //AndroidUtilsAstSep.turnOnOffBluetooth(false);
            if (SESPPreferenceUtil.getForcedOfflineStatus()) {
                SharedPreferences downloadFinishedPreference = AndroidUtilsAstSep.getSharedPreferences(ConstantsAstSep.SharedPreferenceType.PERSISTENT);
                typeDataDownloadFinished= downloadFinishedPreference.getBoolean(ConstantsAstSep.SharedPreferenceKeys.DOWNLOAD_FINISHED,false);
                woDownloadFinished=typeDataDownloadFinished;
                if (typeDataDownloadFinished )   {
                    textViewForTypeDataAndWorkOrder.setVisibility(View.GONE);
                    downloadSpinner.setVisibility(View.GONE);
                }
            } else if (typeDataDownloadFinished && woDownloadFinished){
                textViewForTypeDataAndWorkOrder.setVisibility(View.GONE);
                downloadSpinner.setVisibility(View.GONE);
            }
            else {
                IntentFilter intentFilter1 = new IntentFilter(DownloadTypeDataIntentService.ACTION);
                LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver1, intentFilter1);
                IntentFilter intentFilter2 = new IntentFilter(DownloadWOIntentService.ACTION);
                LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver2, intentFilter2);
            }
        } catch (Exception e) {
            writeLog(TAG + " :onResume()", e);
        }

    }
    private BroadcastReceiver testReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);

            if (resultCode == RESULT_OK) {
                typeDataDownloadFinished = true;
                if (woDownloadFinished){
                    Toast.makeText(DashboardActivity.this, com.capgemini.sesp.ast.android.R.string.work_order_and_type_data_downloaded_successfully, Toast.LENGTH_SHORT).show();
                    downloadSpinner.setVisibility(View.GONE);
                    textViewForTypeDataAndWorkOrder.setVisibility(View.GONE);
                    setLocationScheduler();
                }
                LocalBroadcastManager.getInstance(DashboardActivity.this).unregisterReceiver(testReceiver1);

            }
        }
    };
    private BroadcastReceiver testReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);

            if (resultCode == RESULT_OK) {
                woDownloadFinished = true;
                if (typeDataDownloadFinished){
                    Toast.makeText(DashboardActivity.this, com.capgemini.sesp.ast.android.R.string.work_order_and_type_data_downloaded_successfully, Toast.LENGTH_SHORT).show();
                    downloadSpinner.setVisibility(View.GONE);
                    textViewForTypeDataAndWorkOrder.setVisibility(View.GONE);
                }
                LocalBroadcastManager.getInstance(DashboardActivity.this).unregisterReceiver(testReceiver2);

            }
        }
    };
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("DashboardActivity", "onPause is called");
        // De-register the broadcast receiver here
        if (this.networkStatusReceiver != null) {
            unregisterReceiver(this.networkStatusReceiver);
        }
    }

    private void setLocationScheduler(){
        Intent intent = new Intent(this, LocationTrack.class);
        intent.setAction(LocationTrack.ACTION_INVOKE_SOURCE_ALARM);
        AlarmManager alarmManager =
                (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            pendingIntent =
                    PendingIntent.getForegroundService(this, ConstantsAstSep.LOCATION_TRACK_PENDING_REQUEST_ID
                            , intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
        }

        else {
            pendingIntent =
                    PendingIntent.getService(this, ConstantsAstSep.LOCATION_TRACK_PENDING_REQUEST_ID
                            , intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
        }

        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+20000,
                pendingIntent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == ConstantsAstSep.DialogCodes.LOGOUT) {
            return new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle(R.string.menu_logout)
                    .setMessage(R.string.logout_question)
                    .setIcon(R.drawable.ic_menu_logout)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SessionState.getInstance().clearSession();
                                    Intent intent = new Intent(
                                            DashboardActivity.this,
                                            LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                    .setNegativeButton(R.string.no,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeDialog(ConstantsAstSep.DialogCodes.LOGOUT);
                                }
                            }).create();
        }
        return super.onCreateDialog(id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }


    /**
     * The following code to display logout activity
     * is re-factored to a new method to avoid code duplication
     */

    private void showLogoutActivity() {
        try {
            final Intent goToIntent = new Intent(this, LogoutActivity.class);
            startActivity(goToIntent);
        } catch (Exception e) {
            writeLog(TAG + " :showLogoutActivity()", e);
        }
    }


    /**
     * The following code to display settings activity
     * is re-factored to a new method to avoid code duplication
     */

    private void showSettingsActivity() {
        try {
            final Intent intent = new Intent(DashboardActivity.this, MainMenuSettingsActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            writeLog(TAG + " : showSettingsActivity()", e);
        }
    }

    /**
     * The following code to display about activity
     * is re-factored to a new method to avoid code duplication
     */

    private void showAboutActivity() {
        try {
            final Intent intent = new Intent(DashboardActivity.this, MainMenuAboutActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            writeLog(TAG + "showAboutActivity()", e);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent;
        if (typeDataDownloadFinished == false || woDownloadFinished == false) {

            final View alertView
                    = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.prompt_user_response_layout, null);
            if (alertView != null) {
                final Button okButton = (Button) alertView.findViewById(R.id.okButtonYesNoPage);
                okButton.setOnClickListener(this);

                final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(this,
                        alertView, getResources().getString(R.string.download_Type_data_work_order_progress), null);
                dialog = builder.create();
                dialog.show();
            }
        } else
            switch (position) {

                case ConstantsAstSep.MainMenuItems.ORDER_SUMMARY:   //  Order Summary
                    Intent goToIntent = new Intent(getApplicationContext(), ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.ORDER_SUMMARY_ACTIVITY));
                    startActivity(goToIntent);
                    break;
                case ConstantsAstSep.MainMenuItems.MATERIAL_LIST:   //  Material List
                    intent = new Intent(getApplicationContext(), MaterialListActivity.class);
                    startActivity(intent);
                    break;
                case ConstantsAstSep.MainMenuItems.MATERIAL_LOGISTIC:   //  Material Logistics
                    if (SESPPreferenceUtil.isPreferencesSaved(position) &&
                            SessionState.getInstance().isLoggedInOnline()) {
                        Intent materiaLogisticslIntent = new Intent(getApplicationContext(), MaterialLogisticsActivity.class);
                        startActivity(materiaLogisticslIntent);
                    } else if (!SessionState.getInstance().isLoggedInOnline()) {
                        GuiController.showErrorDialog(this, getString(R.string.error), getString(R.string.not_connected_material_logistic_not_accessible)).show();
                    } else {
                        Intent mlSettingsIntent = new Intent(getApplicationContext(), SESPMaterialLogisticsSettingsActivity.class);
                        startActivity(mlSettingsIntent);
                    }
                    break;
                case ConstantsAstSep.MainMenuItems.NAVIGATION:      //  Navigation
                    intent = new Intent(getApplicationContext(), NavigationListActivity.class);
                    startActivity(intent);
                    break;
                case ConstantsAstSep.MainMenuItems.PRINTER:         //  Print Menu
                    ActivityFactory activityFactory = ActivityFactory.getInstance();
                    Intent btPrinterIntent = new Intent(getApplicationContext(), ActivityFactory.getInstance().getActivityClass(ConstantsAstSep.ActivityConstants.PRINT_MENU_ACTIVITY));
                    startActivity(btPrinterIntent);

                    break;
                case ConstantsAstSep.MainMenuItems.SERIAL_COMMUNICATION_TEST:   //  Serial Communication
                    Intent usbSerialTestIntent = new Intent(getApplicationContext(), UsbSerialCommunicationActivity.class);
                    startActivity(usbSerialTestIntent);
                    break;

                case ConstantsAstSep.MainMenuItems.TEST_METER_COMMUNICATION:    //  Meter Communication Test
                    activityFactory = ActivityFactory.getInstance();
                    Intent meterCommunicationTestIntent = new Intent(getApplicationContext(), activityFactory.getActivityClass(ConstantsAstSep.ActivityConstants.METER_COMMUNICATION_ACTIVITY));
                    startActivity(meterCommunicationTestIntent);
                    break;

                default:
                    String[] menuItems = getResources().getStringArray(R.array.items_main_menu);
                    Toast.makeText(getApplicationContext(), menuItems[position] + " " + getString(R.string.error_not_implemented), Toast.LENGTH_SHORT).show();
                    break;
            }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // you cannot go back to login screen
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_logout) {
            showLogoutActivity();
            return true;
        } else if (itemId == R.id.menu_settings) {
            showSettingsActivity();
            return true;
        } else if (itemId == R.id.menu_about) {
            showAboutActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void networkStatusChanged(boolean isConnected) {
        try {
            Log.d("DashboardActivity", "networkStatusChanged called, value = " + isConnected);
            networStateIcon.setVisibility(View.VISIBLE);
            SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
            if (isConnected) {
                networStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_4_bar_white));
            } else {
                networStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_null_black));
            }
        } catch (Exception e) {
            writeLog(TAG + " :networkStatusChanged()", e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("DashboardActivity", "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DashboardActivity", "onDestroy called");
    }

    static class LauncherIcon {
        final String text;
        final int imgId;
        final String icon;

        public LauncherIcon(int imgId, String text, String icon) {
            super();
            this.imgId = imgId;
            this.text = text;
            this.icon = icon;
        }

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            if (v.getId() == R.id.okButtonYesNoPage && dialog != null) {
                dialog.dismiss();
            }
        }

    }

    static class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            return ICONS.length;
        }

        @Override
        public LauncherIcon getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        static class ViewHolder {
            public ImageView icon;
            public TextView text;
        }

        // Create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            try {
                ViewHolder holder;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);

                    v = vi.inflate(R.layout.dashboard_icon, null);
                    holder = new ViewHolder();
                    holder.text = v.findViewById(R.id.dashboard_icon_text);
                    holder.icon = v.findViewById(R.id.dashboard_icon_img);
                    v.setTag(holder);
                } else {
                    holder = (ViewHolder) v.getTag();
                }

                holder.icon.setImageResource(ICONS[position].imgId);
                holder.text.setText(ICONS[position].text);
            } catch (Exception e) {
                writeLog(TAG + " :getView()", e);
            }
            return v;
        }
    }
}