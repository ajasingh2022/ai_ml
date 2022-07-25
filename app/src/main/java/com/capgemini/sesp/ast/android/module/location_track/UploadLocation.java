package com.capgemini.sesp.ast.android.module.location_track;

import android.app.Service;
import android.os.AsyncTask;

import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.skvader.rsp.cft.common.to.cft.table.UserPositionTO;

import java.util.ArrayList;

public class UploadLocation extends AsyncTask<UserPositionTO,Void,Void> {

    Service callingService;
    UploadLocation(Service service) {
        callingService=service;
    }

    @Override
    protected void onPostExecute(Void o) {
        try {
            super.onPostExecute(o);
            callingService.stopSelf();
        } catch (Exception e) {

        }

    }

    @Override
    protected Void doInBackground(UserPositionTO[] userPositionTOS) {
        if (AndroidUtilsAstSep.isNetworkAvailable()) {
            UserPositionTO userPositionTO = userPositionTOS[0];
            ArrayList arrayList = new ArrayList<UserPositionTO>();
            arrayList.add(userPositionTO);
            try {
                AndroidUtilsAstSep.getDelegate().addUserPositions(arrayList);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
