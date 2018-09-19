package com.example.user.afinal;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.user.afinal.Activity.BluetoothActivity;
import com.example.user.afinal.Model.Constants;
import com.example.user.afinal.Utility.GPSTracker;
/*import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;*/

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by USER on 12/8/2017.
 */

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;
    private static final String TAG = "GeoIntentService";
    GPSTracker gps;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

       gps = new GPSTracker(this);
        double distance =gps.getdistance();
        DecimalFormat df = new DecimalFormat("#.##");
        double p = Double.parseDouble(df.format(distance));

        if (distance>5){
            sendNotification("Out Of Region",String.valueOf(p));
        }else {
            sendNotification("Inside The Region",String.valueOf(p));

        }
        //sendNotification(String.valueOf(gps.getdistance()));
         //double distance = intent.getDoubleExtra("distance",0.1);
        //sendNotification(String.valueOf(distance));
        // sendNotification("Wake Up! Wake Up!");
    }

    private void sendNotification(String title,String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this,BluetoothActivity.class), 0);
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle(title).setSmallIcon(R.drawable.ic_refresh)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setAutoCancel(true)
                .setContentText(msg+" km");
        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");

    }

}
