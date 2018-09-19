package com.example.user.afinal;


import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import android.text.TextUtils;
import android.util.Log;

import com.example.user.afinal.Activity.MapsActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 07/04/2018.
 */

public class GeofenceTrasitionService extends IntentService{
    private static final String TAG = GeofenceTrasitionService.class.getSimpleName();
    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    String geofenceTransitionDetails;
    public static final String PARAM_IN_MSG = "imsg";
    private ResultReceiver mResultReceiver;
    boolean isIntentServiceRunning;


    public GeofenceTrasitionService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
     //   mResultReceiver = intent.getParcelableExtra("RECEIVER");
   /*     // Extract the receiver passed into the service
        ResultReceiver rec = intent.getParcelableExtra("RECEIVER");
        // Extract additional values from the bundle
        String val = intent.getStringExtra("RECEIVER");
        // To send a message to the Activity, create a pass a Bundle
        Bundle bundle = new Bundle();
        bundle.putString("resultValue", "My Result Value. Passed in: " + val);
        // Here we call send passing a resultCode and the bundle of extras
        rec.send(Activity.RESULT_OK, bundle);*/



        // Retrieve the Geofencing intent
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
       // String val = intent.getStringExtra("distance");
         double distance = intent.getDoubleExtra("distance",0.1);
        DecimalFormat df = new DecimalFormat("#.##");
        double p = Double.parseDouble(df.format(distance));

        if (distance>5){
            sendNotification(String.valueOf(p)+"Km  Out Of Region");
        }else {
            sendNotification("Inside The Region Of  "+String.valueOf(p)+"Km");

        }

        // Handling errors
        if ( geofencingEvent.hasError() ) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode() );
            Log.e( TAG, errorMsg );
            return;
        }

        // Retrieve GeofenceTrasition
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type

      /*  if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT )
        {
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            // Create a detail message with Geofences received
             geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences );
            // Send notification details as a String
            sendNotification( geofenceTransitionDetails );
            sendToActivity("Sending to activity test");
        }else {sendNotification("error");}*/

        //  Log.i(TAG, geofenceTransitionDetails);


        //2nd method
        /* if (LocationClient.hasError(intent)) {
            //todo error process
        } else {
            int transitionType = LocationClient.getGeofenceTransition(intent);
            if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
                List<Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);

                for (Geofence geofence : triggerList) {
                    generateNotification(geofence.getRequestId(), "address you defined");
                }
            }
        }*/

    }

    // Create a detail message with Geofences received
    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER )
            status = "Entering ";
        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT )
            status = "Exiting ";
        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }

    // Send a notification
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String msg ) {
        Log.i(TAG, "sendNotification: " + msg );

        // Intent to start the main Activity
       // Intent notificationIntent = MapsActivity.makeNotificationIntent(getApplicationContext(), msg);
        Intent notificationIntent = new Intent(getApplicationContext(), MapsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MapsActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
       // PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this,MapsActivity.class), 0);
        // Creating and sending Notification
        NotificationManager notificatioMng = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificatioMng.notify(GEOFENCE_NOTIFICATION_ID, createNotification(msg, notificationPendingIntent));
    }

    // Create a notification
    private Notification createNotification(String msg, PendingIntent notificationPendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_refresh)
                .setColor(Color.RED)
                //.setContentTitle(msg)
               // .setContentText("Geofence Notification!")
                .setContentTitle("Distance")
                .setContentText(msg )
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder.build();
    }

    // Handle errors
    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isIntentServiceRunning = false;
        Log.d("INTENET SERVICE", "ON DESTROY");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isIntentServiceRunning = true;
        Log.d("INTENET SERVICE", "ON CREATE");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("INTENET SERVICE", "ON START");
        isIntentServiceRunning = true;
    }

    public void sendToActivity(String string){
        Bundle b=new Bundle();
        b.putString("FROM_GEOFENCE_KEY", string);
        mResultReceiver.send(0, b);
    }
}
