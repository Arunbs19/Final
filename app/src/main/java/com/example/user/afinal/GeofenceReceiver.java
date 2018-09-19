package com.example.user.afinal;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by USER on 12/8/2017.
 */

public class GeofenceReceiver extends WakefulBroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        //this will update the UI with message
        Log.e("fdmfkm", "GeofenceReceiver called");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.e("GeofenceReceiver", String.valueOf(geofencingEvent.getErrorCode()));

        } else {
          // Log.e("GeofenceReceiver", geofencingEvent.getTriggeringLocation().getLatitude() + ", " +geofencingEvent.getTriggeringLocation().getLongitude());

        }
        ComponentName comp = new ComponentName(context.getPackageName(),
                GeofenceTrasitionService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

    }
}
