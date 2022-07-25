package com.capgemini.sesp.ast.android.module.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import androidx.multidex.MultiDexApplication;

import com.capgemini.sesp.ast.android.module.driver.DriverManager;
import com.capgemini.sesp.ast.android.module.util.exception.SespExceptionHandler;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteManager;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteType;
import com.capgemini.sesp.ast.android.module.util.task.AndroidInitAstSepExecuteTask;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.common.AddCommentActivity;
import com.capgemini.sesp.ast.android.ui.activity.dashboard.DashboardActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.MaterialLogisticsActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.SESPMaterialLogisticsSettingsActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.car.CarInputActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.AddBulkDialog;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.AddByIdDialog;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.AddSequenceDialog;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.CommonListActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.RemoveByIdDialog;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.device_info.DeviceInfoInputActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.device_info.DeviceInfoShowActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.goods_reception.GoodsReceptionActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.manage_pallet.PalletInformationActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.manage_pallet.SelectPalletActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.manage_pallet.content.PalletContentActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control.MaterialControlInputActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control.MaterialControlRegisterUnitActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control.MaterialControlSelectPalletActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.material_control.MaterialControlSelectReasonActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.register_new_broken_device.RegisterNewBrokenDeviceActivity;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.register_new_broken_device.RegisterNewBrokenDeviceDialog;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.register_new_broken_device.RemoveNewBrokenDeviceDialog;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderListActivity;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderSummaryActivity;
import com.capgemini.sesp.ast.android.ui.activity.printer.PrintMenuActivity;
import com.capgemini.sesp.ast.android.ui.activity.receiver.DialogFactory;
import com.capgemini.sesp.ast.android.ui.wo.activity.EvSmartChargingMeterInstallationCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.EvSmartChargingMeterInstallationDM;
import com.capgemini.sesp.ast.android.ui.wo.activity.EvTroubleshootMeasurePointCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.EvTroubleshootMeasurePointDM;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterChangeCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterChangeCTRollout;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterChangeDM;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterChangeDMRollout;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterInstallationCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterInstallationDM;
import com.capgemini.sesp.ast.android.ui.wo.activity.MeterRemovalCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.SolarPanelInstallation;
import com.capgemini.sesp.ast.android.ui.wo.activity.TroubleshootMeasurePointCT;
import com.capgemini.sesp.ast.android.ui.wo.activity.TroubleshootMeasurePointDM;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.DialogKeyConstants;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class ApplicationAstSep extends MultiDexApplication {
    public static final Date STARTUP_TIME = new Date();
    public static Context context;
    private transient static Properties properties = null;
    public static Class workOrderClass;
    public static Class<? extends AsyncTask> workOrderSyncServiceClass;
    private static DriverManager driverManager;
    //private Activity currentActivity;

    public static String getPropertyValue(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            ApplicationAstSep.context = getApplicationContext();
            ExecuteManager.getInstance().registerTask(new AndroidInitAstSepExecuteTask());
            ExecuteManager.getInstance().executeSafe(ExecuteType.AST_APPLICATION_STARTED);
            final AssetManager assetManager = ApplicationAstSep.context.getResources().getAssets();

            final InputStream inputStream = assetManager.open(ConstantsAstSep.PropertyConstants.KEY_PROPERTIES_FILE);
            properties = new Properties();
            properties.load(inputStream);
        } catch (final Exception e) {
            writeLog("ApplicationAstSep" + " :onCreate()", e);
        }

        //TODO Uncomment this , only for debuging comment this
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new SespExceptionHandler(context));
        initialize();
    }

    public synchronized static DriverManager getDriverManager() {
        if (driverManager == null) {
            driverManager = new DriverManager();
        }
        return driverManager;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    protected void initialize() {
        try {
            getDriverManager().initLabelPrinter(getPropertyValue("printer_model", "Datamax O'neil MF4T Bluetooth Printer"));

            //Load Activity factory instance
            ActivityFactory activityFactory = ActivityFactory.getInstance();
            activityFactory.addActivity(ActivityConstants.SELECT_PALLET_ACTIVITY, SelectPalletActivity.class);
            activityFactory.addActivity(ActivityConstants.MATERIAL_CONTROL_INPUT_ACTIVITY, MaterialControlInputActivity.class);
            activityFactory.addActivity(ActivityConstants.CAR_INPUT_ACTIVITY, CarInputActivity.class);
            activityFactory.addActivity(ActivityConstants.GOODS_RECEPTION_ACTIVITY, GoodsReceptionActivity.class);
            activityFactory.addActivity(ActivityConstants.REGISTER_NEW_BROKEN_DEVICE_ACTIVITY, RegisterNewBrokenDeviceActivity.class);
            activityFactory.addActivity(ActivityConstants.DEVICE_INFO_INPUT_ACTIVITY, DeviceInfoInputActivity.class);
            activityFactory.addActivity(ActivityConstants.MATERIAL_LOGISTICS_SETTINGS, SESPMaterialLogisticsSettingsActivity.class);
            activityFactory.addActivity(ActivityConstants.ORDER_LIST_ACTIVITY, OrderListActivity.class);
            activityFactory.addActivity(ActivityConstants.ORDER_SUMMARY_ACTIVITY, OrderSummaryActivity.class);
            activityFactory.addActivity(ActivityConstants.MAIN_MENU_ACTIVITY, DashboardActivity.class);
            activityFactory.addActivity(ActivityConstants.PALLET_INFORMATION_ACTIVITY, PalletInformationActivity.class);
            activityFactory.addActivity(ActivityConstants.PALLET_CONTENT_ACTIVITY, PalletContentActivity.class);
            activityFactory.addActivity(ActivityConstants.COMMON_LIST_ACTIVITY, CommonListActivity.class);
            activityFactory.addActivity(ActivityConstants.MATERIAL_LOGISTICS_ACTIVITY, MaterialLogisticsActivity.class);
            activityFactory.addActivity(ActivityConstants.MATERIAL_CONTROL_REGISTER_UNIT_ACTIVITY, MaterialControlRegisterUnitActivity.class);
            activityFactory.addActivity(ActivityConstants.ADD_COMMENT_ACTIVITY, AddCommentActivity.class);
            activityFactory.addActivity(ActivityConstants.MATERIAL_CONTROL_SELECT_REASON_ACTIVTY, MaterialControlSelectReasonActivity.class);
            activityFactory.addActivity(ActivityConstants.MATERIAL_CONTROL_SELECT_PALLET_ACTIVITY, MaterialControlSelectPalletActivity.class);
            activityFactory.addActivity(ActivityConstants.PRINT_MENU_ACTIVITY, PrintMenuActivity.class);
            activityFactory.addActivity(ActivityConstants.METER_CHANGE_DM_ROLLOUT, MeterChangeDMRollout.class);
            activityFactory.addActivity(ActivityConstants.METER_CHANGE_CT, MeterChangeCT.class);
            activityFactory.addActivity(ActivityConstants.TROUBLESHOOT_MEASUREPOINT_CT, TroubleshootMeasurePointCT.class);
            activityFactory.addActivity(ActivityConstants.TROUBLESHOOT_MEASUREPOINT_DM, TroubleshootMeasurePointDM.class);
            activityFactory.addActivity(ActivityConstants.METER_CHANGE_DM, MeterChangeDM.class);
            activityFactory.addActivity(ActivityConstants.METER_CHANGE_CT_ROLLOUT, MeterChangeCTRollout.class);
            activityFactory.addActivity(ActivityConstants.METER_INSTALLATION_DM, MeterInstallationDM.class);
            //EV Implementation
            activityFactory.addActivity(ActivityConstants.EV_SMART_CHARGING_METER_INSTALLATION_DM, EvSmartChargingMeterInstallationDM.class);
            activityFactory.addActivity(ActivityConstants.EV_SMART_CHARGING_METER_INSTALLATION_CT, EvSmartChargingMeterInstallationCT.class);
            activityFactory.addActivity(ActivityConstants.EV_TROUBLESHOOT_MEASURE_POINT_DM, EvTroubleshootMeasurePointDM.class);
            activityFactory.addActivity(ActivityConstants.EV_TROUBLESHOOT_MEASURE_POINT_CT, EvTroubleshootMeasurePointCT.class);
            
            activityFactory.addActivity(ActivityConstants.SOLAR_PANEL_INSTALLATION, SolarPanelInstallation.class);
          //activityFactory.addActivity(ActivityConstants.TROUBLESHOOT_SOLAR, TroubleShootSolar.class);
            activityFactory.addActivity(ActivityConstants.METER_REMOVAL_CT, MeterRemovalCT.class);
            activityFactory.addActivity(ActivityConstants.METER_INSTALLATION_CT, MeterInstallationCT.class);
            activityFactory.addActivity(ActivityConstants.DEVICE_INFO_SHOW_ACTIVITY, DeviceInfoShowActivity.class);


            //Load Dialog Factory instance
            DialogFactory dialogFactory = DialogFactory.getInstance();
            dialogFactory.addDialog(DialogKeyConstants.ADD_BY_ID_DIALOG, AddByIdDialog.class);
            dialogFactory.addDialog(DialogKeyConstants.ADD_SEQUENCE_DIALOG, AddSequenceDialog.class);
            dialogFactory.addDialog(DialogKeyConstants.ADD_BULK_DIALOG, AddBulkDialog.class);
            dialogFactory.addDialog(DialogKeyConstants.REMOVE_BY_ID_DIALOG, RemoveByIdDialog.class);
            dialogFactory.addDialog(DialogKeyConstants.BROKEN_ADD_BY_ID_DIALOG, RegisterNewBrokenDeviceDialog.class);
            dialogFactory.addDialog(DialogKeyConstants.BROKEN_REMOVE_BY_ID_DIALOG, RemoveNewBrokenDeviceDialog.class);
        } catch (Exception e) {
            writeLog("ApplicationAstSep" + " :initialize()", e);
        }
    }

}
