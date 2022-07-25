package com.capgemini.sesp.ast.android.module.util;


import android.os.AsyncTask;
import android.util.Log;

import com.skvader.rsp.cft.common.to.cft.table.UserPositionTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by rkumari2 on 08-05-2019.
 */

public class GPSThread extends AsyncTask<String, Void, Void> {
    private transient final String TAG = GPSThread.class.getSimpleName();
    private transient Long idSystemCoordsSystemT = null;
    private transient String xCoordinate = null;
    private transient String yCoordinate = null;

    public GPSThread(Long idSystemCoordsSystemT, String xCoordinate, String yCoordinate) {
        this.idSystemCoordsSystemT = idSystemCoordsSystemT;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    @Override
    protected Void doInBackground(String... strings) {

        final List<UserPositionTO> userPositionTOs = new ArrayList<UserPositionTO>();
        UserPositionTO userPositionTO = new UserPositionTO();
        userPositionTO.setIdSystemCoordSystemT(idSystemCoordsSystemT);
        userPositionTO.setIuTimestamp(new Date());
        userPositionTO.setXCoordinate(xCoordinate);
        userPositionTO.setYCoordinate(yCoordinate);
        userPositionTOs.add(userPositionTO);
        try {
            AndroidUtilsAstSep.getDelegate().addUserPositions(userPositionTOs);
            SESPPreferenceUtil.removeUserLocation();
        } catch (Exception e) {
            SESPPreferenceUtil.savePreference(ConstantsAstSep.SharedPreferenceKeys.GPS_X_COORD, xCoordinate);
            SESPPreferenceUtil.savePreference(ConstantsAstSep.SharedPreferenceKeys.GPS_Y_COORD, yCoordinate);
            SESPPreferenceUtil.savePreference(ConstantsAstSep.SharedPreferenceKeys.GPS_COORD_SYS_TYPE, idSystemCoordsSystemT);
            Log.e(TAG, " Updated in Saved Preferences " + e.getMessage());

        }
        return null;
    }

}
