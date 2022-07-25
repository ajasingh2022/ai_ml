package com.capgemini.sesp.ast.android.ui.service;

/**
 * Created by rkumari2 on 10-10-2018.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.GPSThread;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.skvader.rsp.cft.common.util.AndroidAuthenticationTO;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class AndroidStatisticsSyncService extends Service {
    private static final String TAG = AndroidStatisticsSyncService.class.getSimpleName();

    public AndroidStatisticsSyncService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart(Intent intent, int startid) {
        synchStatics();
    }

    private void synchStatics() {

        final AndroidAuthenticationTO androidAuthenticationTO = new AndroidAuthenticationTO();
        Long idSystemCoordsSystemT = SESPPreferenceUtil.getPreference(ConstantsAstSep.SharedPreferenceKeys.GPS_COORD_SYS_TYPE);
        String xCoordinate = SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.GPS_X_COORD);
        String yCoordinate = SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.GPS_Y_COORD);
        //Set Android device IP address.
        try {
            androidAuthenticationTO.setIpAddress(AndroidUtilsAstSep.getLocalIpAddress());
            androidAuthenticationTO.setOperatingSystem("Android:" + Build.VERSION.RELEASE);
            // Set Device Id
            androidAuthenticationTO.setDeviceSerialNumber(Build.SERIAL != Build.UNKNOWN ? Build.SERIAL : Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        } catch (Exception e) {
            writeLog(TAG + "  : onStart() ", e);
        }

        final int noOfUnsycWorkorders = DatabaseHandler.createDatabaseHandler().getCaseIdsForSync().size();
        final int noOfUnsycPhotos = DatabaseHandler.createDatabaseHandler().getNumberOfPhotosToBeSynched();
        List<WorkorderLiteTO> list = DatabaseHandler.createDatabaseHandler().getAllWorkorderLiteTos();
        final int totalWoCount = list.size();
        try {
            if ((idSystemCoordsSystemT != null) && (!xCoordinate.equals("")) && (!yCoordinate.equals(""))) {
                new GPSThread(idSystemCoordsSystemT, xCoordinate, yCoordinate).execute();
            }
        } catch (Exception e) {
            writeLog(TAG + ":onStart ::User Position Could not be updated: ", e);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AndroidUtilsAstSep.getDelegate().addAndroidStatistics(androidAuthenticationTO, totalWoCount, noOfUnsycWorkorders, noOfUnsycPhotos);
                } catch (Exception e) {
                    writeLog(TAG + ":onStart ::Android Statistics could not be updated from Statistics Service: ", e);
                }
            }
        }).start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchStatics();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
    }
}