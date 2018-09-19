package com.example.user.afinal;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.example.user.afinal.Activity.BluetoothActivity;
import com.example.user.afinal.Activity.MapsActivity;
import com.example.user.afinal.DATABASE.DBAdapter;

import java.io.Serializable;
import java.util.List;



public class AlarmReceiver extends WakefulBroadcastReceiver {

    private PowerManager.WakeLock screenWakeLock;


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "AlarmReceiver", Toast.LENGTH_SHORT).show();
        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.open();
        dbAdapter.clearalarm();

        //this will update the UI with message
        //BluetoothActivity inst = BluetoothActivity.instance();
        //inst.setAlarmText("Alarm! Wake up! Wake up!");

        if (screenWakeLock == null)
        {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            screenWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,"Tag");
            screenWakeLock.acquire();
        }
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
        if (screenWakeLock != null)
            screenWakeLock.release();
    }
}
