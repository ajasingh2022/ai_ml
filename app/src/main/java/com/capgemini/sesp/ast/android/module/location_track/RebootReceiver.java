package com.capgemini.sesp.ast.android.module.location_track;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;

public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent i) {

        /*if (i.getAction().equals("android.intent.action.BOOT_COMPLETED")) {*/

            Intent intent = new Intent(context, LocationTrack.class);
            intent.setAction(LocationTrack.ACTION_INVOKE_SOURCE_ALARM);
            AlarmManager alarmManager =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent ;
            if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {

                        pendingIntent = PendingIntent.getForegroundService(context,
                                ConstantsAstSep.LOCATION_TRACK_PENDING_REQUEST_ID,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
            }
            else {
                    pendingIntent = PendingIntent.getService(context,
                            ConstantsAstSep.LOCATION_TRACK_PENDING_REQUEST_ID,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            }
            if (pendingIntent != null && alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20000,
                    pendingIntent);
    }
}
