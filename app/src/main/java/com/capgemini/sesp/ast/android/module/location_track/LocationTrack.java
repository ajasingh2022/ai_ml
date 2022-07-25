package com.capgemini.sesp.ast.android.module.location_track;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.CacheController;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.skvader.rsp.cft.common.to.cft.table.SystemParameterTO;
import com.skvader.rsp.cft.common.to.cft.table.UserPositionTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class LocationTrack extends Service {

    private static final int NOTIFICATION_ID = 1000;
    private static final int ENABLE_LOCATION_REQUEST_CODE = 101;
    SessionState sessionState=SessionState.getInstance();
    private static final String CHANNEL_ID = "notification_fetching_location" ;
    private FusedLocationProviderClient fusedLocationClient;
    Location updatedLocation = null;
    public static final String ACTION_INVOKE_SOURCE_FCM = "ACTION_INVOKE_SOURCE_FCM";
    public static final String ACTION_INVOKE_SOURCE_ALARM = "ACTION_INVOKE_SOURCE_ALARM";
    public static final String ACTION_INVOKE_LOCATION_ENABLE = "ACTION_INVOKE_LOCATION_ENABLE";
    String action;
    private  String startingHour,startingMinute,weekends,endingHour,endingMinute,
            durationBetween;

    private void startFetching() throws Exception{
        startingHour = null;
        startingMinute = null;
        weekends = null;
        endingHour = null;
        endingMinute = null;
        durationBetween = null;
        DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
        List<SystemParameterTO> systemParameterTOS =
                databaseHandler.getAll(SystemParameterTO.class);

        for (SystemParameterTO systemParameterTO:systemParameterTOS){
            if (startingMinute == null ||startingHour==null ||weekends==null
                    ||endingHour==null ||endingMinute == null || durationBetween == null){
                switch (systemParameterTO.getCode()){
                    case "STARTING_MINUTE":startingMinute = systemParameterTO.getValue();break;
                    case "STARTING_HOUR":startingHour = systemParameterTO.getValue();break;
                    case "NON_WORKING_DAYS_OF_WEEK":weekends = systemParameterTO.getValue();break;
                    case "ENDING_MINUTE":endingMinute = systemParameterTO.getValue();break;
                    case "ENDING_HOUR":endingHour=systemParameterTO.getValue();break;
                    case "IN_BETWEEN_DURATION":durationBetween=systemParameterTO.getValue();break;
                }

            }
            else {
                break;
            }
        }


        if (canStartFetching())
        {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setNumUpdates(1);
            locationRequest.setInterval(10);
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {

                        if (updatedLocation == null) {
                            updatedLocation = location;
                            continue;
                        }
                        if (updatedLocation.getTime() < location.getTime()) {
                            updatedLocation = location;
                        }
                    }
                    Log.d("FromFusedLocationUpdate", updatedLocation.toString());
                    uploadLocation(updatedLocation);
                }
            };

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(this).checkLocationSettings(
                            builder.build());

            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);

                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        fusedLocationClient =
                                LocationServices.getFusedLocationProviderClient(LocationTrack.this);
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                        switch (action) {
                            case ACTION_INVOKE_SOURCE_ALARM:
                                setNextCall();
                                break;
                        }

                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    updateNotificationForLocationEnable();

                                } catch (Exception e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                switch (action) {
                                    case ACTION_INVOKE_SOURCE_ALARM:
                                        setNextCall();
                                        break;
                                }
                                LocationTrack.this.stopSelf();
                                break;
                        }
                    }
                }
            });
        }


        else {
            switch (action){
                case ACTION_INVOKE_SOURCE_ALARM: setNextCall();break;
            }
            this.stopSelf();
        }
    }

    private boolean canStartFetching() {

        Calendar currentTime=Calendar.getInstance();
        if (startingHour == null || startingMinute == null || endingHour == null
                ||endingMinute == null || weekends == null || durationBetween == null){
            return false;
        }
        if(!sessionState.isLoggedIn()){
            if (isWeekend(currentTime.get(Calendar.DAY_OF_WEEK)) || !isWorkingHours(currentTime))
                return false;
            else
                return !SESPPreferenceUtil.getPreferenceBoolean(ConstantsAstSep.SharedPreferenceKeys.USER_LOGGED_OUT);
        }


        return true;
    }

    private boolean isWorkingHours(Calendar currentTime) {

        if( (currentTime.getTimeInMillis() >=getStartingTimeOfDay(currentTime).getTimeInMillis())
                && currentTime.getTimeInMillis() <=getEndingTimeOfDay(currentTime).getTimeInMillis()){
            return true;

        }
        return false;
    }

    private  void uploadLocation(Location updatedLocation) {
        Location locationToReturn = updatedLocation;


        if (locationToReturn!= null) {

            UserPositionTO userPositionTO = new UserPositionTO();
            userPositionTO.setXCoordinate(String.valueOf(locationToReturn.getLatitude()));
            userPositionTO.setYCoordinate(String.valueOf(locationToReturn.getLongitude()));
            userPositionTO.setIuTimestamp(new Date());

            List<UserPositionTO> userPositionTOS = new ArrayList<>();
            userPositionTOS.add(userPositionTO);
            CallWebServiceInBackGround callWebServiceInBackGround =
                    new CallWebServiceInBackGround();
            callWebServiceInBackGround.returnValue = new CallWebServiceInBackGround.ReturnValue() {
                @Override
                public Void onReturnResult(Object o) {
                    LocationTrack.this.stopSelf();
                    return null;
                }
            };
            callWebServiceInBackGround.execute("addUserPositions",List.class,userPositionTOS);

        }

    }

    private void updateNotificationForLocationEnable(){

        Intent intent = new Intent(this, LocationTrack.class);
        intent.setAction(LocationTrack.ACTION_INVOKE_LOCATION_ENABLE);
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

        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+100,
                pendingIntent);
        setNextCall();
        this.stopSelf();
    }

    private Calendar getStartingTimeOfDay(Calendar arg){

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(arg.getTimeInMillis());
        start.set(Calendar.HOUR_OF_DAY,
                Integer.parseInt(startingHour));
        start.set(Calendar.MINUTE,
                Integer.parseInt(startingMinute));

        return start;
    }

    private Calendar getEndingTimeOfDay(Calendar arg){

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(arg.getTimeInMillis());
        end.set(Calendar.HOUR_OF_DAY,
                Integer.parseInt(endingHour));
        end.set(Calendar.MINUTE,
                Integer.parseInt(endingMinute));

        return end;
    }



    private void setNextCall() {

        Calendar start,end;
        Calendar currentTime = Calendar.getInstance();
        start = getStartingTimeOfDay(currentTime);
        end = getEndingTimeOfDay(currentTime);

        Long nextCallTime =
                currentTime.getTimeInMillis() + Long.parseLong(durationBetween);

        if (!canStartFetching()) {
            if (nextCallTime < start.getTimeInMillis()) {
                nextCallTime = start.getTimeInMillis();
            }

            if (nextCallTime > end.getTimeInMillis()) {
                start.add(Calendar.DAY_OF_YEAR, 1);
                while (isWeekend(start.get(Calendar.DAY_OF_WEEK))) {
                    start.add(Calendar.DAY_OF_YEAR, 1);
                }
                nextCallTime = start.getTimeInMillis();
            }
        }


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
        alarmManager.set(AlarmManager.RTC_WAKEUP,nextCallTime,
                pendingIntent);
    }

    private boolean isWeekend(int dayOfWeek) {
        String weekend = weekends;
        String[] weekends = weekend.split(",");

        for (int i = 0;i<weekends.length;i++){
            int calenderReference =0;
            switch (weekends[i].toUpperCase()){
                case "SUNDAY": calenderReference = Calendar.SUNDAY;break;
                case "MONDAY": calenderReference = Calendar.MONDAY;break;
                case "TUESDAY": calenderReference = Calendar.TUESDAY;break;
                case"WEDNESDAY": calenderReference = Calendar.WEDNESDAY;break;
                case "THURSDAY": calenderReference = Calendar.THURSDAY;break;
                case "FRIDAY": calenderReference = Calendar.FRIDAY;break;
                case "SATURDAY": calenderReference = Calendar.SATURDAY;break;

            }
            if (calenderReference == dayOfWeek){
                return  true;
            }
        }
        return false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        action = intent.getAction();

        if (action.equals(ACTION_INVOKE_LOCATION_ENABLE)){
            this.startForeground(NOTIFICATION_ID,
                    createNotification(2,
                            getResources().getString(R.string.enable_location_service)));
        }
    else {
            this.startForeground(NOTIFICATION_ID,
                    createNotification(1, getResources().getString(R.string.fetching_location)));
            try {
                startFetching();
            } catch (Exception e) {
                this.stopSelf();
            }
        }
        return START_NOT_STICKY;
    }

    private Notification createNotification(int constant,String message){
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                ENABLE_LOCATION_REQUEST_CODE,
                new Intent(this, AskLocationPermissionActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Location Update";
            NotificationChannel chan = new NotificationChannel(CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
            notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_gps_not_fixed_black_24dp)
                    .setContentTitle(message)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCategory(Notification.CATEGORY_SERVICE);

        }
        else {
            notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID);
            notificationBuilder
                    .setContentTitle(getResources().getString(R.string.sesp_title))
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_gps_not_fixed_black_24dp);
        }

        if (constant == 2){
             return notificationBuilder.addAction(R.drawable.arrow_right_selected,"ok",
                    pendingIntent).setOngoing(false).build();
        }
        return notificationBuilder.build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
