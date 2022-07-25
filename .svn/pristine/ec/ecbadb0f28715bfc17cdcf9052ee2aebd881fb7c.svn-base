package com.capgemini.sesp.ast.android.module.util;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.communication.AndroidWebserviceClientAstSepImpl;
import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.PropertyConstants;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceType;
import com.capgemini.sesp.ast.android.ui.activity.scanner.BarcodeScanner;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.service.AsyncResponse;
import com.capgemini.sesp.ast.android.ui.service.CheckServerConnectionService;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.PdaWoResultConfigTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.DatabaseConstantsAstSepAndroid;
import com.skvader.rsp.ast_sep.webservice_client.port.AndroidWebserviceClientAstSep;
import com.skvader.rsp.ast_sep.webservice_client.port.ForgotPasswordWebserviceClient;
import com.skvader.rsp.cft.common.to.cft.table.DomainRelationTO;
import com.skvader.rsp.cft.common.util.Utils;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.DomainCustomTO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.StockhandlingConstants;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


public class AndroidUtilsAstSep {

    private static final String TAG = "AndroidUtilsAstSep";
    private static Properties props = null;
    public static Boolean forgotPassword = false;
    public static SharedPreferences getSharedPreferences(SharedPreferenceType type) {
        return ApplicationAstSep.context.getSharedPreferences(type.name(), Context.MODE_PRIVATE);
    }

    public static String hashPassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(password.getBytes());

        } catch (NoSuchAlgorithmException e) {
            writeLog(TAG + " :hashPassword()", e);
        }
        return new String(md.digest());
    }


    public static AndroidWebserviceClientAstSep getDelegate() {
        forgotPassword=false;
        return new AndroidWebserviceClientAstSepImpl();
    }
    // changes done for reset password functionality
    public static ForgotPasswordWebserviceClient getDelegateForForgotPassword() {
        forgotPassword = true;
        return new AndroidWebserviceClientAstSepImpl();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            writeLog(TAG + " :getLocalIpAddress() ", ex);
        }
        return null;
    }

    public static DatabaseConstantsAstSepAndroid CONSTANTS() {
        return ObjectCache.getAllIdObjects(DatabaseConstantsAstSepAndroid.class).get(0);
    }

    public static Set<Long> getVisibleDomains(Long idDomain) {
        Set<Long> idDomains = new HashSet<Long>();
        try {
            if (idDomain == null) {
                return idDomains;
            }
            DomainCustomTO domainCustomTO = ObjectCache.getIdObject(DomainCustomTO.class, idDomain);
            for (DomainRelationTO domainRelationTO : domainCustomTO.getDomainRelationTOs()) {
                if (domainRelationTO.getIdDomain().equals(idDomain)) {
                    idDomains.add(domainRelationTO.getVisibleIdDomain());
                }
            }
        } catch (Exception ex) {
            writeLog(TAG + " :getVisibleDomains() ", ex);
        }
        return idDomains;
    }

    public static URL getResource(String fileName) {
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource(fileName);
            if (url == null) {
                url = Thread.currentThread().getContextClassLoader().getResource(fileName);
            }
            return url;
        } catch (Exception e) {
            writeLog(TAG + " :getResource() ", e);
            return null;
        }
    }

    public static String getDatabaseInfo() {
        SharedPreferences p = null;
        try {
            p = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.PERSISTENT);
        } catch (Exception e) {
            writeLog(TAG + " : getDatabaseInfo()", e);
        }
        return p.getString(SharedPreferenceKeys.DATABASE_INFO, "");
    }

    /**
     * The method would return SESP
     * specific image directory
     *
     * @param appName {@link String}
     * @return {@link String}
     */

    public static String getAppImageFolder(final String appName) {
        String status = null;
        try {
            if (appName != null && !"".equals(appName)) {
                File file = null;
                // If SD Card is available
                if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(), appName);
                    status = file.getAbsolutePath();
                    file.mkdirs();
                } else {
                    // Use the private data directory
                    file = new File(Environment.getDataDirectory(), appName);
                    status = file.getAbsolutePath();
                    file.mkdirs();
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : getAppImageFolder()", e);
        }
        return status;
    }

    /**
     * The method would return SESP
     * specific temporary image directory
     * for specific case ID
     *
     * @param appName
     * @param idCase
     * @return
     */


    public static String getAppImageFolder(final String appName,final Long idCase) {
        String status = null;
        String caseStatus = null;
        if (appName != null && !"".equals(appName)) {
            File file = null;
            // If SD Card is available
            if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory(), appName);
                status = file.getAbsolutePath();
                file.mkdirs();
            } else {
                // Use the private data directory
                file = new File(Environment.getDataDirectory(), appName);
                status = file.getAbsolutePath();
                file.mkdirs();
            }

            File caseIdFolder = null;

            if(status != null){
                caseIdFolder = new File(file, idCase.toString());
                caseStatus = caseIdFolder.getAbsolutePath();
                caseIdFolder.mkdirs();

            }
        }
        return caseStatus;
    }


    /**
     * Method required for filtering reasons
     *
     * @param idCaseT
     * @param idPdaPageT
     * @param pdaWoResultConfigMobileBeans
     * @param woEventResultReasonTypes
     * @return
     */
    public static List<WoEventResultReasonTTO> getResultReason(
            Long idCaseT,
            Long idPdaPageT,
            List<PdaWoResultConfigTCTO> pdaWoResultConfigMobileBeans,
            List<WoEventResultReasonTTO> woEventResultReasonTypes) {

        ArrayList<WoEventResultReasonTTO> filteredReasons = new ArrayList<WoEventResultReasonTTO>();
        try {
            for (int i = 0; i < pdaWoResultConfigMobileBeans.size(); i++) {

                for (int j = 0; j < woEventResultReasonTypes.size(); j++) {

                    Long idWoEventResultReasonT = pdaWoResultConfigMobileBeans.get(i).getIdWoEventResultReasonT();

                    if ((idWoEventResultReasonT).equals(woEventResultReasonTypes.get(j).getId())) {

                        if ((pdaWoResultConfigMobileBeans.get(i).getIdPdaPageT()).equals(idPdaPageT) &&
                                (pdaWoResultConfigMobileBeans.get(i).getIdCaseT()).equals(idCaseT)) {

                            filteredReasons.add(woEventResultReasonTypes.get(j));

                        }

                    }

                }

            }
        } catch (Exception e) {
            writeLog(TAG + " : getResultReason()", e);
        }

        return filteredReasons;
    }


    public static List<UnitStatusReasonTTO> getChangeMeterReasons() {


        List<UnitStatusReasonTTO> dismantledReasons = null;
        try {
            List<UnitStatusReasonTTO> reasons = ObjectCache.getAllTypes(UnitStatusReasonTTO.class);
            List<UnitStatusReasonTCTO> reasonsConnect = ObjectCache.getAllTypes(UnitStatusReasonTCTO.class);

            ArrayList<Long> currentReasonId = new ArrayList<Long>();
            ArrayList<UnitStatusReasonTTO> currentReasonType = new ArrayList<UnitStatusReasonTTO>();

            for (int i = 0; i < reasonsConnect.size(); i++) {

                UnitStatusReasonTCTO temp = reasonsConnect.get(i);

                if (temp.getIdUnitStatusT().equals(AndroidUtilsAstSep.CONSTANTS().UNIT_STATUS_T__DISMANTLED)) {
                    currentReasonId.add(temp.getIdUnitStatusReasonT());
                }
            }

            for (int i = 0; i < reasons.size(); i++) {

                if (currentReasonId.contains(reasons.get(i).getId())) {
                    currentReasonType.add(reasons.get(i));
                }
            }

            dismantledReasons = new ArrayList<UnitStatusReasonTTO>();

            for (int i = 0; i < currentReasonType.size(); i++) {
                dismantledReasons.add(currentReasonType.get(i));
            }

        } catch (Exception e) {
            writeLog(TAG + " : getChangeMeterReasons()", e);
        }
        return dismantledReasons;
    }

    /**
     * Generate an image file name to be resided inside
     * the given folder and as per current date to avoid conflict
     * with any existing file name
     *
     * @param dir    {@link String} The directory where the image file would be placed
     * @param format {@link String} the image file extension format.. if not specified default is jpg
     * @return String, absolute file name
     */

    public static String generateImageFileName(final String dir, final String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        final StringBuilder builder = new StringBuilder(dir);
        try {
            builder.append(File.separator);
            builder.append(sdf.format(new Date()));
            if (format == null) {
                builder.append(ConstantsAstSep.OrderHandlerConstants.DEFAULT_IMAGE_FORMAT);
            } else {
                builder.append(format);
            }
        } catch (Exception e) {
            writeLog(TAG + " : generateImageFileName()", e);
        }
        return builder.toString();
    }

    /**
     * @param callerClass {@link Class}
     * @return boolean
     */

    public static boolean isCallerOfType(final Class<?> callerClass) {
        boolean status = false;
        // Check the call stack position for the root caller class
        try {
            final StackTraceElement[] callStackArray = Thread.currentThread().getStackTrace();
            if (callStackArray != null
                    && callStackArray.length >= 3
                    && callerClass != null
                    && callStackArray[3].getClass().equals(callerClass)) {
                status = true;
            }
        } catch (Exception e) {
            writeLog(TAG + " : isCallerOfType()", e);
        }
        return status;
    }

    /**
     * Add to shared preference object
     *
     * @param prefName
     * @param key
     * @param value
     * @param mode
     * @return
     */
    public static boolean addToSharedPreference(final String prefName,
                                                final String key, final String value, final int mode) {
        boolean status = false;
        try {
            if (!"".equals(prefName)
                    && !"".equals(key)
                    && value != null) {
                final SharedPreferences preferences
                        = ApplicationAstSep.context.getSharedPreferences(prefName, mode);
                status = preferences.edit().putString(key, value).commit();
            }
        } catch (Exception e) {
            writeLog(TAG + " : addToSharedPreference()", e);
        }
        return status;
    }


    /**
     * Commonly used utility method to retrieve string values from
     * shared preferences.
     *
     * @param prefName String
     * @param key      String
     * @param mode     int Should be derived from type Context
     * @param defValue defValue- if not required provide null
     * @return String value
     */
    public static String getStringSharedPref(final String prefName,
                                             final String key, final int mode, final String defValue) {
        String value = null;
        try {
            if (!"".equals(prefName)
                    && !"".equals(key)) {
                final SharedPreferences preferences
                        = ApplicationAstSep.context.getSharedPreferences(prefName, mode);
                value = preferences.getString(key, defValue);
            }
        } catch (Exception e) {
            writeLog(TAG + " : getStringSharedPref()", e);
        }
        return value;
    }


    /**
     * @param activity
     * @return
     */

    public static Intent takeSnapshot(final Activity activity) {
        Intent camIntent = null;
        try {
            Long idCase = null;
            if (AbstractWokOrderActivity.getWorkorderCustomWrapperTO() != null){
                idCase= AbstractWokOrderActivity.getWorkorderCustomWrapperTO().getIdCase();
            }
            if (activity != null) {
                camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Need to check if the device has an inbuilt cam app
                // Most devices have so the else should be executed rarely
                final ComponentName imageHandler = camIntent.resolveActivity(activity.getPackageManager());
                if (imageHandler != null) {
                    Log.d("takeSnapshot", "Suitable image handler found, launching activity=" + imageHandler.getPackageName());
                    // The device has at least 1 in built camera app to handle the intent
                    //camIntReqCode = 1;

                    final String photoFile = AndroidUtilsAstSep.generateImageFileName(
                            AndroidUtilsAstSep.getAppImageFolder(ConstantsAstSep.OrderHandlerConstants.DEFAULT_CASE_IMAGE_DIR_NAME,idCase),
                            null);

                    AndroidUtilsAstSep.addToSharedPreference(ConstantsAstSep.OrderHandlerConstants.SESPWOIMAGEPREFNAME,
                            ConstantsAstSep.OrderHandlerConstants.IMAGEFILEINTENTNAME, photoFile, Context.MODE_PRIVATE);

                    Log.d("takeSnapshot", "Saving photo file = " + photoFile);

                    final Uri picUri = Uri.fromFile(new File(photoFile));
                    camIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                    camIntent.putExtra(ConstantsAstSep.OrderHandlerConstants.IMAGEFILEINTENTNAME, photoFile);
                    //activity.startActivityForResult(camIntent, requestCode);
                } else {
                    // Need to start indigenous camera app
                    Log.d("takeSnapshot", "No suitable image handler found");
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : takeSnapshot()", e);
        }
        return camIntent;
    }


    public static Intent getWoImageGalleryIntent(final Activity activity) {
        String imageFile = null;
        String imageDir = null;
        Intent imageActivity = null;
        Long idCase = null;
        if (AbstractWokOrderActivity.getWorkorderCustomWrapperTO() != null){
            idCase= AbstractWokOrderActivity.getWorkorderCustomWrapperTO().getIdCase();
        }
        final List<String> publicImageDirs = new ArrayList<String>();
        try {
            if (activity != null) {

                // Get the image path
                imageFile = AndroidUtilsAstSep.getStringSharedPref(ConstantsAstSep.OrderHandlerConstants.SESPWOIMAGEPREFNAME,
                        ConstantsAstSep.OrderHandlerConstants.IMAGEFILEINTENTNAME, Context.MODE_PRIVATE, null);
                Log.d("getWoImageGalleryIntent", "Receiving photo file name =" + imageFile);
                Log.d("getWoImageGalleryIntent", "Image Directory=" + imageDir);
               // imageDir = AndroidUtilsAstSep.getAppImageFolder(ConstantsAstSep.OrderHandlerConstants.DEFAULT_IMAGE_DIR_NAME);
                imageDir = AndroidUtilsAstSep.getAppImageFolder(ConstantsAstSep.OrderHandlerConstants.DEFAULT_CASE_IMAGE_DIR_NAME,idCase);
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    if (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) != null) {
                        publicImageDirs.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
                    }
                    if (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) != null) {
                        publicImageDirs.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
                    }
                }

                imageActivity = new Intent(); // new Intent(getApplicationContext(),ImageActivity.class);
                imageActivity.setAction(ConstantsAstSep.OrderHandlerConstants.IMAGE_ACTIVITY_INTENT_ACTION);
                // Pass the image file name and SESP specific image directory
                imageActivity.putExtra(ConstantsAstSep.OrderHandlerConstants.LAST_WO_IMAGE, imageFile);
                imageActivity.putExtra(ConstantsAstSep.OrderHandlerConstants.DEFAULT_IMAGE_DIR, imageDir);
                imageActivity.putExtra(ConstantsAstSep.OrderHandlerConstants.PUBLIC_IMAGE_DIRS, publicImageDirs.toArray(new String[]{}));
                final ComponentName imageHandler = imageActivity.resolveActivity(activity.getPackageManager());
                if (imageHandler == null) {
                    Toast.makeText(activity,
                            "No image handler defined for intent action=SESP_WO_IMAGE_ACTIVITY",
                            Toast.LENGTH_LONG).show();
                    imageActivity = null;
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :onActivityResult()", e);
        }

        return imageActivity;

    }

    /**
     * Utility method to check if GPS is enabled
     *
     * @return boolean
     */

    public static boolean isGPSEnabled() {
        boolean status = false;
        try {
            final Context appContext = ApplicationAstSep.context;
            if (appContext != null) {
                final LocationManager locationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
                status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch (Exception e) {
            writeLog(TAG + " :isGPSEnabled()", e);
        }
        return status;
    }


    /**
     * Utility method to downgrade the provided image resource
     * as per target height and width
     * <p/>
     * <p><b>
     * This method must NOT be called from main thread
     * </b></p>
     *
     * @param imageFilePath {@link String}
     * @param targetHeight  {@link int}
     * @param targetWidth   {@link int}
     * @return Bitmap
     */

    public static Bitmap decodeBitmap(final String imageFilePath, final int targetHeight,
                                      final int targetWidth) {
        Bitmap bitmap = null;
        try {
            if (!"".equals(Utils.safeToString(imageFilePath))) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeFile(imageFilePath, options);

                int inSampleSize = 1;

                if (options.outHeight > targetHeight || options.outWidth > targetWidth) {

                    final int halfHeight = options.outHeight / 2;
                    final int halfWidth = options.outWidth / 2;

                    // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                    // height and width larger than the requested height and width.
                    while ((halfHeight / inSampleSize) > targetHeight
                            && (halfWidth / inSampleSize) > targetWidth) {
                        inSampleSize *= 2;
                    }
                }

                options.inJustDecodeBounds = false;
                options.inSampleSize = inSampleSize;

                bitmap = BitmapFactory.decodeFile(imageFilePath, options);
                Log.d("BitmapWorkerTask", "decoding completed " + imageFilePath);
            }
        } catch (Exception e) {
            writeLog(TAG + " :decodeBitmap()", e);
        }
        return bitmap;
    }

    /**
     * Utility method to downgrade the provided image resource
     * as per target height and width
     * <p/>
     * <p><b>
     * This method must NOT be called from main thread
     * </b></p>
     *
     * @param res          {@link String}
     * @param targetHeight {@link int}
     * @param targetWidth  {@link int}
     * @return Bitmap
     */

    public static Bitmap decodeBitmap(final Resources res, final int imageResourceId,
                                      final int targetHeight, final int targetWidth) {
        Bitmap target = null;
        try {
            if (res != null) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeResource(res, imageResourceId, options);

                int inSampleSize = 1;

                if (options.outHeight > targetHeight || options.outWidth > targetWidth) {

                    final int halfHeight = options.outHeight / 2;
                    final int halfWidth = options.outWidth / 2;

                    // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                    // height and width larger than the requested height and width.
                    while ((halfHeight / inSampleSize) > targetHeight
                            && (halfWidth / inSampleSize) > targetWidth) {
                        inSampleSize *= 2;
                    }
                }

                options.inJustDecodeBounds = false;
                options.inSampleSize = inSampleSize;

                target = BitmapFactory.decodeResource(res, imageResourceId, options);
                Log.d("BitmapWorkerTask", "decoding completed for resource id" + imageResourceId);
            }
        } catch (Exception e) {
            writeLog(TAG + " :decodeBitmap()", e);
        }
        return target;
    }


    /**
     * Returns the available storage space
     * on sd-card in mega bytes
     *
     * @return Return available Diskspace
     */

    public static Long getFreeDiskSpaceInMB() {
        Long size = null;
        try {
            if (Environment.getExternalStorageDirectory() != null) {
                final StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
                size = ((long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize()) / (1024 * 1024);
            }
        } catch (Exception e) {
            writeLog(TAG + " :getFreeDiskSpaceInMB()", e);
        }
        return size;
    }

    /**
     * Utility method to check current bluetooth status
     *
     * @return Bluetooth status of device,
     * NULL= bluetooth is not available,
     * TRUE = bluetooth is turned on currently,
     * FALSE = bluetooth is turned off now
     */

    public static Boolean getBluetoothStatus() {
        Boolean status = null;
        try {
            final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter != null) {
                Log.d(TAG, "Default bluetooth adapter IS FOUND");
                // Blue-tooth is either turned off or on
                status = adapter.isEnabled();
            } else {
                Log.d(TAG, "Default bluetooth adapter not found");
            }
        } catch (Exception e) {
            writeLog(TAG + " :getBluetoothStatus()", e);
        }
        return status;
    }


    /**
     * Utility method to turn blue-tooth on/off
     *
     * @param enableFlag
     */
    public static BluetoothAdapter turnOnOffBluetooth(final boolean enableFlag) {
        final Boolean currentStatus = getBluetoothStatus();
        BluetoothAdapter adapter = null;
        try {
            if (currentStatus != null) {
                adapter = BluetoothAdapter.getDefaultAdapter();
                if (enableFlag) {
                    Log.d(TAG, "Turning ON bluetooth");
                    if (!adapter.isEnabled())
                    adapter.enable();
                    Log.d(TAG, "Bluetooth adapter is turned ON");
                } else {
                    Log.d(TAG, "Turning OFF bluetooth");
                    if (adapter.isEnabled())
                    adapter.disable();
                    Log.d(TAG, "Bluetooth adapter is turned OFF");
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

                writeLog(TAG + " :turnOnOffBluetooth() ", e);
            }
        } catch (Exception e) {
            writeLog(TAG + " :turnOnOffBluetooth()", e);
        }
        return adapter;
    }

    /**
     *
     */

    public static void searchBluetoothDevices(final BluetoothAdapter adapter) {
        //final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            if (adapter != null) {
                Log.d("searchBluetoothDevices", "Searching bluetooth discovery");
                adapter.startDiscovery();
            } else {
                Log.e("searchBluetoothDevices", "ELSE BLOCK in searchBluetoothDevices");
            }
        } catch (Exception e) {
            writeLog(TAG + " :searchBluetoothDevices()", e);
        }
    }


    /**
     * Utility methods related to workorder save
     *
     * @param workorderCustomWrapperTO {@link WorkorderCustomWrapperTO}
     * @return fieldVisitId (String)
     */
    public static String generateFieldVisitID(final WorkorderCustomWrapperTO workorderCustomWrapperTO) {
        final StringBuilder builder = new StringBuilder();
        try {
            if (workorderCustomWrapperTO != null) {
                builder.append(":id:").append(workorderCustomWrapperTO.getIdCase());
                builder.append(":timestamp:").append(new Date().getTime());
                builder.append(":");
            }

        } catch (Exception e) {
            writeLog(TAG + " :searchBluetoothDevices()", e);
        }
        return builder.toString();
    }

    public static void setFieldVisitID(final WorkorderCustomWrapperTO workorderCustomWrapperTO) {
        try {
            final StringBuilder builder = new StringBuilder();

            if (workorderCustomWrapperTO != null) {
                builder.append(":id:").append(workorderCustomWrapperTO.getIdCase());
                builder.append(":timestamp:").append(new Date().getTime());
                builder.append(":");
            }
            workorderCustomWrapperTO.setFieldVisitID(builder.toString());
        } catch (Exception e) {
            writeLog(TAG + " :setFieldVisitID()", e);
        }
    }

    /**
     * The method takes the original workorder and
     * performes a deep cloning and returns the copy
     *
     * @param source   {@link WorkorderCustomWrapperTO}
     * @param srcClass {@link Class}
     * @return T
     */

    public static <T> T getDeepCopy(final T source, final Class<T> srcClass) {
        Log.d(TAG, "getDeepCopy getting called " + new Date());
        T clone = null;
        try {
            if (source != null) {
                clone = CommunicationHelper.JSONMAPPER.readValue(CommunicationHelper.JSONMAPPER.writeValueAsString(source), srcClass);
            }
        } catch (final Exception ex) {
            writeLog(TAG + " :returning data from getDeepCopy() ", ex);
            //  Log.e(TAG, "Error during deep copy -" + ex.getMessage(), ex);
        }
        Log.d(TAG, "returning data from getDeepCopy " + new Date());
        return clone;
    }

    /**
     * This method is useful to add an empty pojo in
     * the type data selection spinner
     *
     * @param cls
     * @return
     */

    public static <T> T instantiateEmptyTypeObject(final Class<T> cls) {
        T val = null;
        try {
            if (cls != null) {
                val = cls.newInstance();
            }
        } catch (final Exception ex) {
            writeLog(TAG + " :instantiateEmptyTypeObject() ", ex);
        }
        return val;
    }


    /**
     * Utility method to return
     * the display metrics containing various information
     * of the realtime display screen of the device
     *
     * @return {@link DisplayMetrics}
     */

    public static DisplayMetrics getScreenDisplayMetrics() {
        DisplayMetrics displayMetrics = null;

        try {
            final WindowManager windowManager = (WindowManager) ApplicationAstSep.context.getSystemService(Context.WINDOW_SERVICE);
            displayMetrics = new DisplayMetrics();

            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        } catch (final Exception ex) {
            writeLog(TAG + " :getScreenDisplayMetrics() ", ex);
        }
        return displayMetrics;
    }

    /**
     * Utility method to extract integer from string/object
     *
     * @param object
     * @return
     */

    public static Integer getSafeInteger(final String object) {
        Integer val = null;
        try {
            if (object != null && !"".equals(object)) {
                val = Integer.parseInt(object.toString());
            }
        } catch (final Exception ex) {
            writeLog(TAG + " :getSafeInteger() ", ex);
        }

        return val;
    }


    /**
     * Utility method to be used across the application
     * to show/hide the soft key board where appropriate.
     * <p/>
     * Passing parameter showKeyBoard = false will hide the keyboard if visible now
     * (If not visible already would not do anything)
     * and vice versa
     */

    public static void showHideSoftKeyBoard(final Activity activity,
                                            final boolean showKeyBoard) {
        try {
            if (activity != null) {
                if (showKeyBoard) {
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                } else {
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        } catch (final Exception ex) {
            writeLog(TAG + " :showHideSoftKeyBoard() ", ex);
        }
    }

    /**
     * Call the intent service (dynamically decided based on intent action)
     * to transmit work-order data to web service
     *
     * @param caseId (Case id of work-order)
     */

    public static void suggestSaveWorkorder(final long caseId, String fieldVisitId) {

        Log.d(TAG, "AndroidUtilsAstSep - suggestSaveWorkorder method is called, case id=" + caseId);
        Log.d(TAG, "Session State - Workorder result " + SessionState.getInstance().getCurrentUserPassword());
        try {
            if (fieldVisitId == null) {
                fieldVisitId = DatabaseHandler.createDatabaseHandler().getFinalWOForSync(caseId, ApplicationAstSep.workOrderClass).getFieldVisitID();
            }
            AsyncTask<Void, Void, Boolean> workOrderSyncService = null;

            workOrderSyncService = (AsyncTask<Void, Void, Boolean>) AndroidUtilsAstSep.newInstance(ApplicationAstSep.workOrderSyncServiceClass, caseId, fieldVisitId);
            Log.i(TAG, "Work order sync instance class : " + workOrderSyncService.getClass().toString());
            Log.i(TAG, "Case Id : " + caseId + ", Field Visit Id : " + fieldVisitId);
            workOrderSyncService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            writeLog(TAG + " :suggestSaveWorkorder() ", e);
        }
    }


    public static void loadConfigProperties() {
        try {
            final String serverUrl = ApplicationAstSep.getPropertyValue(PropertyConstants.KEY_SERVER_URL, null);
            Log.i("StartupActivity", "StartupActivity server_url=" + serverUrl);
            CommunicationHelper.setDefaultServerAddress(serverUrl);

            final String sslCertName = ApplicationAstSep.getPropertyValue(PropertyConstants.KEY_SSL_CERT_NAME, null);
            Log.i("StartupActivity", "StartupActivity ssl_certificate_name=" + sslCertName);
            CommunicationHelper.setSslCertName(sslCertName);

            final String sslPort = ApplicationAstSep.getPropertyValue(PropertyConstants.KEY_SSL_PORT, null);
            Log.i("StartupActivity", "StartupActivity ssl_port=" + sslPort);
            CommunicationHelper.setSslPort(sslPort);
            //Load auto update properties
            final String autoUpdate = ApplicationAstSep.getPropertyValue(PropertyConstants.KEY_AUTO_UPDATE, null);
            if (autoUpdate != null) {
                SESPPreferenceUtil.savePreference(SharedPreferenceKeys.AUTO_UPDATE, autoUpdate);
            }
            final String versionCheckURL = ApplicationAstSep.getPropertyValue(PropertyConstants.KEY_VERSION_CHECK_URL, null);
            if (versionCheckURL != null) {
                SESPPreferenceUtil.savePreference(SharedPreferenceKeys.VERSION_CHECK_URL, versionCheckURL);
            }
            final String downloadURL = ApplicationAstSep.getPropertyValue(PropertyConstants.KEY_DOWNLOAD_URL, null);
            if (downloadURL != null) {
                SESPPreferenceUtil.savePreference(SharedPreferenceKeys.DOWNLOAD_URL, downloadURL);
            }
        } catch (final Exception e) {
            writeLog(TAG + ": AndroidUtilsAstSep()", e);
        }
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = null;
        NetworkInfo networkInfo = null;
        try {
            cm = (ConnectivityManager) ApplicationAstSep.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {
            writeLog(TAG + ":isNetworkAvailable()  ", e);
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static void downloadWorkordersIfNeeded(final Activity parentActivity,
                                                  final Class<? extends Activity> nextActivityClass, final Integer flag) {
        try {
            if (parentActivity != null && nextActivityClass != null) {
                new DownloadWOThread(parentActivity, nextActivityClass, flag).start();
                SESPPreferenceUtil.setWoDownloadTime();
            }
        } catch (Exception e) {
            writeLog(TAG + " :downloadWorkordersIfNeeded() ", e);
        }
    }


    /**
     * Utility method to be used to launch an explicit
     * activity with optional intent flags
     * <p/>
     * <p>
     * As this belongs to an explicit intent hence does
     * not require the intent resolution.
     * </p>
     *
     * @param context
     * @param activityClass
     * @param flag
     */

    public static void launchExplicitActivity(final Context context,
                                              final Class<? extends Activity> activityClass, final Integer flag) {

        Log.d("AndroidUtilsAstSep", "launchExplicitActivity is called");
        try {
            if (context != null && activityClass != null) {
                final Intent launchIntent = new Intent(context, activityClass);
                if (flag != null) {
                    launchIntent.addFlags(flag);
                }
                Log.d("AndroidUtilsAstSep", "Launching activity with intent class =" + activityClass.getName());
                context.startActivity(launchIntent);
            }
        } catch (Exception e) {
            writeLog(TAG + " :launchExplicitActivity() ", e);
        }
    }

    /**
     * Launch an implicit intent with action only
     * may or may not be tied to SESP application only
     * <p/>
     * As this is implicit intent hence the intent requires
     * resolution. If the target package is not found the method
     * would return false to caller to indicate that no activity is launched.
     *
     * @param context
     * @param action
     * @param flag
     */

    public static boolean launchImplicitActivity(final Context context,
                                                 final String action, final Integer flag) {
        boolean found = false;
        try {
            Log.d("AndroidUtilsAstSep", "launchImplicitActivity is called");

            if (context != null && action != null && !"".equals(action)) {
                ResolveInfo resolveInfo = null;

                final Intent launchIntent = new Intent(action);
                if (flag != null) {
                    launchIntent.addFlags(flag);
                    // Check for availability
                    resolveInfo = context.getPackageManager().resolveActivity(launchIntent, flag);
                } else {
                    resolveInfo = context.getPackageManager().resolveActivity(launchIntent, 0);
                }

                if (resolveInfo != null) {
                    Log.d("AndroidUtilsAstSep", "Launching activity with intent action =" + action);
                    found = true;
                    context.startActivity(launchIntent);
                } else {
                    Log.d("AndroidUtilsAstSep", "Implicit activity with intent action =" + action + " not found, not launched");
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :launchImplicitActivity() ", e);
        }
        return found;
    }

    /**
     * Getting SESP map tile source
     *
     * @return
     */
    public static String[] getTileSourceURL() {
        String[] tileSourceArray = null;
        try {
            ArrayList<String> mapTiles = new ArrayList<String>();
            int i = 1;
            while (true) {
                if (TextUtils.isEmpty(ApplicationAstSep.getPropertyValue(PropertyConstants.KEY_TILE_SOURCE_FACTORY + i, null))) {
                    break;
                }
                mapTiles.add(ApplicationAstSep.getPropertyValue(PropertyConstants.KEY_TILE_SOURCE_FACTORY + i++, null));
            }
            tileSourceArray = new String[mapTiles.size()];
            mapTiles.toArray(tileSourceArray);
        } catch (Exception e) {
            writeLog(TAG + " :getTileSourceURL() ", e);
        }
        return tileSourceArray;
    }

    /**
     * Requests the location by all three providers
     *
     * @param context
     * @param listener
     * @throws Exception
     */
    public static void requestLocationByProviders(Context context, LocationListener listener, boolean requestNewLoc) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                requestLocation(LocationManager.GPS_PROVIDER, locationManager, listener, requestNewLoc);
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                requestLocation(LocationManager.NETWORK_PROVIDER, locationManager, listener, requestNewLoc);
            }
            if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                requestLocation(LocationManager.PASSIVE_PROVIDER, locationManager, listener, requestNewLoc);
            }

            Log.d(TAG,
                    "Location tracking started with min time millis=" + ConstantsAstSep.OrderHandlerConstants.MINTIMEMILLISFORLOCUPDATE);
            Log.d(TAG,
                    "Location tracking started with min distance meter=" + ConstantsAstSep.OrderHandlerConstants.MINDISTANCEFLOATFORLOCUPDATE);
        } catch (Exception e) {
            writeLog(TAG + " :requestLocationByProviders() ", e);
        }
    }


    /**
     * Location request reusable method
     *
     * @param provider
     * @param locationManager
     * @param listener
     * @return
     */
    private static void requestLocation(String provider, LocationManager locationManager, LocationListener listener, boolean requestNewLoc) {
        Location loc;
        try {
            if (!requestNewLoc) {
                loc = locationManager.getLastKnownLocation(provider);
                if (loc != null) {
                    listener.onLocationChanged(loc);
                }
            }
            locationManager.requestLocationUpdates(provider,
                    ConstantsAstSep.OrderHandlerConstants.MINTIMEMILLISFORLOCUPDATE,
                    ConstantsAstSep.OrderHandlerConstants.MINDISTANCEFLOATFORLOCUPDATE,
                    listener);
        } catch (Exception e) {
            writeLog(TAG + " :requestLocation() ", e);
        }
    }

    /**
     * Checkes if time period is elapsed
     * Returns true if the time difference is greater than @differenceMinut
     *
     * @param oldTimeString
     * @param currentTimeString
     * @param differenceMinut
     * @return
     */
    public static boolean isElapsedTime(String oldTimeString, String currentTimeString, long differenceMinut) {
        Log.d(TAG, "Last Work order downloaded time :" + oldTimeString);
        Log.d(TAG, "Current time : " + currentTimeString);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        Date oldTime;
        Date currentTime;
        try {
            oldTime = simpleDateFormat.parse(oldTimeString);
            currentTime = simpleDateFormat.parse(currentTimeString);
        } catch (ParseException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
        //milliseconds
        long elapsedDays = 0;
        long elapsedHours = 0;
        long elapsedMinutes = 0;
        try {
            long different = currentTime.getTime() - oldTime.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            elapsedDays = different / daysInMilli;
            different = different % daysInMilli;
            elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;
            elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;
            long elapsedSeconds = different / secondsInMilli;
            Log.d(TAG, "Elapsed time : " +
                    elapsedDays + " days, " +
                    elapsedHours + " hours, " +
                    elapsedMinutes + " minutes, " +
                    elapsedSeconds + " seconds.");
        } catch (Exception e) {
            writeLog(TAG + " :requestLocation() ", e);
        }
        return elapsedDays >= 1 || elapsedHours >= 1 || elapsedMinutes > differenceMinut;
    }

    /**
     * Returns the current date in dd/M/yyyy hh:mm:ss format
     *
     * @return
     */
    public static String getCurrentDate() {
        String currentTime = null;
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
            currentTime = df.format(c.getTime());
        } catch (Exception e) {
            writeLog(TAG + " :requestLocation() ", e);
        }
        return currentTime;
    }

    /**
     * Create a copy of a bean.
     *
     * @param to
     * @param from
     */
    public static void copyBean(Object to, Object from) {
        Method[] m = from.getClass().getMethods();
        String methodname;
        Object value;
        try {
            for (int i = 0; i < m.length; i++) {
                if (m[i].getName().toLowerCase().startsWith("get")) {
                    methodname = m[i].getName().substring(3);
                    methodname = "set" + methodname;
                    try {
                        value = m[i].invoke(from);
                        Method to_method = to.getClass().getMethod(
                                methodname,
                                m[i].getReturnType());
                        to_method.invoke(to, value);
                    } catch (Exception e) {
                        writeLog(TAG + " : copyBean() : ", e);

                    }

                }
            }
        }catch(Exception e){
                writeLog(TAG + " :copyBean() : ", e);
            }
        }


    /**
     * Returns a copy of an object
     *
     * @param orig
     * @return
     */
    public static Object copyObject(Object orig) {
        Object obj = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;

        ObjectInputStream in = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (Exception e) {
            writeLog(TAG + " :copyObject()", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    writeLog(TAG + " :copyObjectByStream()", e);
                }

                try {
                    in.close();
                } catch (IOException e) {
                    writeLog(TAG + " :copyObject()", e);
                }
            }
        }
        return obj;
    }

    /**
     * method to remove 8004 prefix from meters if present
     *
     * @param meterGiai
     * @param enableUnitBarcodeValidation
     * @return meter giai without 8004 if it was previously present
     */
    public static String removePrefixFromGiaiIfPresent(String meterGiai, boolean enableUnitBarcodeValidation) {
        try {
            final int prefixLength = StockhandlingConstants.GIAI_BARCODE_PREFIX.length();
            if (enableUnitBarcodeValidation && (meterGiai != null && meterGiai.length() > prefixLength
                    && StockhandlingConstants.GIAI_BARCODE_PREFIX.equalsIgnoreCase(meterGiai.substring(0, prefixLength)))) {
                meterGiai = meterGiai.substring(prefixLength);

            }
        } catch (Exception e) {
            writeLog(TAG + " :removePrefixFromGiaiIfPresent()", e);
        }
        return meterGiai;
    }

    public static String removePrefixFromInstIfPresent(String instId, boolean enableUnitBarcodeValidation) {
        try {
            final int prefixLength = StockhandlingConstants.INST_BARCODE_PREFIX.length();
            if (enableUnitBarcodeValidation && (instId != null && instId.length() > prefixLength
                    && StockhandlingConstants.INST_BARCODE_PREFIX.equalsIgnoreCase(instId.substring(0, prefixLength)))) {
                instId = instId.substring(prefixLength);

            }
        } catch (Exception e) {
            writeLog(TAG + " :removePrefixFromInstIfPresent()", e);
        }
        return instId;
    }

    /**
     * Method for scanning bar code
     *
     * @param fragment
     * @param requestCode
     */
    public static void scanBarCode(final Fragment fragment, final int requestCode) {
        try {
            Intent barCodeScanner = new Intent(fragment.getActivity(), BarcodeScanner.class);
            fragment.startActivityForResult(barCodeScanner, requestCode);
        } catch (Exception e) {
            writeLog(TAG + " :scanBarCode()", e);
        }
    }

    /**
     * Scan the bar code
     *
     * @param activity
     * @param requestCode
     */
    public static void scanBarCode(final Activity activity, final int requestCode) {
        try {
            Intent barCodeScanner = new Intent(activity, BarcodeScanner.class);
            activity.startActivityForResult(barCodeScanner, requestCode);
        } catch (Exception e) {
            writeLog(TAG + " :scanBarCode()", e);
        }
    }


    /**
     * Creates a new instance for clazz using it's constructor matching the given args.
     * As opposed to the <code>clazz.getConstructor(...).newInstance(args)</code> method
     * this method considers also constructors with matching super-type parameters
     * (as we know it from normal method or constructor invocations).
     *
     * @param clazz
     * @param args
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static <T> T newInstance(Class<? extends T> clazz, Object... args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return findConstructor(clazz, args).newInstance(args);
    }

    /**
     * @param clazz
     * @param args
     * @return
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> findConstructor(Class<T> clazz, Object... args) throws NoSuchMethodException {
        Constructor<T> matchingConstructor = null;
        try {
            Constructor<T>[] constructors = (Constructor<T>[]) clazz.getConstructors();
            for (Constructor<T> constructor : constructors) {
                if (constructor.getParameterTypes().length != args.length) {
                    continue;
                }
                Class<?>[] formalTypes = constructor.getParameterTypes();
                for (int i = 0; i < formalTypes.length; i++) {
                    if (!formalTypes[i].isInstance(args[i])) {
                        continue;
                    }
                }
                if (matchingConstructor != null) // already found one ... so there is more than one ...
                {
                    throw new NoSuchMethodException("Multiple constructors found for: " + printArgs(clazz, args) + " --> " + matchingConstructor + " --> " + constructor);
                }
                matchingConstructor = constructor;
            }
            if (matchingConstructor == null) {
                throw new NoSuchMethodException("No constructor found for: " + printArgs(clazz, args));
            }
        } catch (Exception e) {
            writeLog(TAG + " :findConstructor()", e);
        }
        return matchingConstructor;
    }

    private static String printArgs(Class<?> clazz, Object... args) {
        StringBuilder msg = new StringBuilder();
        try {
            msg.append("new ");
            msg.append(clazz.getName());
            msg.append("(");
            for (int i = 0; i < args.length; i++) {
                if (i > 0) {
                    msg.append(", ");
                }
                msg.append(args[i] == null ? "null" : args[i].getClass().getName());
            }
            msg.append(")");
        } catch (Exception e) {
            writeLog(TAG + " :printArgs()", e);
        }
        return msg.toString();
    }

    /**
     * CONVERT List<Long> to delimited string
     *
     * @param idList
     * @param delimiter
     * @return
     */
    public static String convertLongListToDelimitedString(List<Long> idList, String delimiter) {
        StringBuffer delimitedString = new StringBuffer();
        try {
            for (Long id : idList) {
                if (id != null && !id.toString().isEmpty()) {
                    if (delimitedString.length() != 0) {
                        delimitedString.append(delimiter);
                    }
                    delimitedString.append(id.toString());
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :convertLongListToDelimitedString()", e);
        }
        return delimitedString.toString();
    }

    /**
     * CONVERT delimited string to List of Long
     *
     * @param delimitedString
     * @param delimiter
     * @return
     */
    public static List<Long> convertDelimitedStringToLongList(String delimitedString, String delimiter) {
        String[] idArrayString = delimitedString.split(delimiter);
        List<Long> idList = new ArrayList<Long>(idArrayString.length);
        try {
            for (String idString : idArrayString) {
                idList.add(Long.valueOf(idString));
            }
        } catch (Exception e) {
            writeLog(TAG + " :convertDelimitedStringToLongList()", e);
        }
        return idList;
    }

    public static boolean validateExistingBtPrinterSettings(Context context, String printerMacAddress) {
        boolean existingBtPrinterSettings = false;
        try {
            if (printerMacAddress == null || "".equals(printerMacAddress)) {
                final View alertView
                        = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.prompt_user_response_layout, null);
                final Dialog dialog = GuiController.getCustomAlertDialog(context,
                        alertView, context.getString(R.string.print_not_configured), "ERROR")
                        .create();
                if (alertView != null) {
                    final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            } else {
                existingBtPrinterSettings = true;
            }
        } catch (Exception e) {
            writeLog(TAG + " :validateExistingBtPrinterSettings()", e);
        }
        return existingBtPrinterSettings;
    }


    public static void navigateTo(Context context, String latitude, String longitude) {
        try {
            String uri = "geo:0,0?q=" + latitude + "," + longitude;
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        } catch (Exception e) {
            writeLog(TAG + " :navigateTo()", e);
        }
    }

    public static void checkServerConnection(final NetworkStatusCallbackListener listener) {
        try {
            CheckServerConnectionService checkServerConnectionService = new CheckServerConnectionService(new AsyncResponse<Boolean>() {
                @Override
                public void processFinish(Boolean output) {
                    if (!output) {
                        // Increment the failed connection status for that session.
                        SessionState.getInstance().incrementFailedConnectionsCounter();
                    }
                    if (listener != null) {
                        listener.networkStatusChanged(output);
                    }

                    if(output && SessionState.getInstance().isLoggedIn()) {
                        List<Long> caseIdsToBeSynched = DatabaseHandler.createDatabaseHandler().getCaseIdsForSync();

                        for (Long caseId : caseIdsToBeSynched) {
                            Log.d("synchronizing", "Syncing work order, case Id : " + caseId);
                            AndroidUtilsAstSep.suggestSaveWorkorder(caseId, null);
                        }
                    }
                }
            });
            checkServerConnectionService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            writeLog(TAG + " :checkServerConnection()", e);
        }
    }

    /**
     * Convert String to double
     *
     * @param str
     * @return
     */
    public static Double convertStringToDouble(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        return Double.valueOf(str.replace(',', '.'));
    }

    /**
     * Load properties file and return value of the key
     *
     * @param key
     * @param context
     * @return
     * @throws IOException
     */
    public static String getProperty(String key, Context context) throws IOException {
        Properties properties = new Properties();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(PropertyConstants.KEY_PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            writeLog(TAG + " :getProperty()", e);
        }
        return properties.getProperty(key);
    }

    public static boolean isJavaWebStart() {
        return System.getProperty("webstart") != null && System.getProperty("webstart").equals("true");
    }

    public static synchronized String getProperty(String wanted) {
        if (isJavaWebStart()) {
            return System.getProperty(wanted);
        }
        if (props == null) {
            props = new Properties();
            FileInputStream f = null;
            try {
                f = new FileInputStream(System.getProperty("app.prop"));
                props.load(f);
            } catch (Exception e) {
                writeLog(TAG + " :getProperty()", e);
            } finally {
                if (f != null) {
                    try {
                        f.close();
                    } catch (IOException e) {
                        writeLog(TAG + " :getProperty()", e);
                    }
                }
            }
        }
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            System.out.println(key + " => " + value);
            Log.d(TAG, "Tapas:****** " + key + " = " + value);
        }
        return props.getProperty(wanted);
    }

    /*
     * Handheld / model / device checks
	 */
    public static boolean isHandheldMode() {
        /*
        // for time being this code is commented
        try {
            if (System.getProperty("handheld") == null
                    || System.getProperty("handheld").equals("yes")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
        */
        return false;
    }

    public static String getProperty(String wanted, String def) {
        String res = getProperty(wanted);
        if (res != null)
            return res;
        else
            return def;
    }

    /**
     * Check if APP is running on any Emulator?
     *
     * @return
     */
    public static boolean isEmulator() {
        boolean emulatorFlag = false;
        try {
            Log.d(TAG, "Fingerprint:" + Build.FINGERPRINT + " Device:" + Build.FINGERPRINT + "  Brand:" + Build.BRAND + "  Manufacturer:" + Build.MANUFACTURER + "  Model:" + Build.MODEL + " Brand:" + Build.BRAND);
            emulatorFlag = Build.FINGERPRINT.contains("generic")
                    || Build.FINGERPRINT.contains("samsung/victorltectc/victorlte:4.4.2/KOT49H/3.8.117.0927:user/release-keys")
                    || Build.FINGERPRINT.contains("unknown")
                    || Build.DEVICE.contains("prototd")
                    || Build.DEVICE.contains("victorlte")
                    || Build.BRAND.contains("Coolpad")
                    || Build.BRAND.contains("samsung")
                    || Build.BRAND.contains("Huawei")
                    || Build.MANUFACTURER.contains("samsung")
                    || Build.MANUFACTURER.contains("Coolpad")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("SM-G3589W")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || (Build.BRAND.contains("generic") && Build.DEVICE.contains("generic"));
        } catch (Exception e) {
            writeLog(TAG + " :isEmulator()", e);
        }
        return emulatorFlag;
    }

}
