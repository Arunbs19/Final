package com.example.user.afinal;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.Toast;

import com.example.user.afinal.DATABASE.DBAdapter;

import java.util.Calendar;

public class OnBootReceiver extends BroadcastReceiver {
@Override
public void onReceive(Context ctxt, Intent intent) {
    DBAdapter dbAdapter = new DBAdapter(ctxt);
    dbAdapter.open();

    if (intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")){
        ComponentName receiver = new ComponentName(ctxt, OnBootReceiver.class);
        PackageManager pm = ctxt.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        Toast.makeText(ctxt,"BOOT_COMPLETED",Toast.LENGTH_SHORT).show();
        AlarmManager alarmMgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarm = new Intent(ctxt, AlarmReceiver.class);
        Calendar alarmtime = Calendar.getInstance();
        long currenttime = Calendar.getInstance().getTimeInMillis();
        if (dbAdapter.getalarm1()!=0){
            if (currenttime<dbAdapter.getalarm1()){
             /*   AlarmManager am = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
                Intent i= new Intent(AlarmClock.ACTION_SET_ALARM);
                PendingIntent pi = PendingIntent.getBroadcast(ctxt, 0, i, 0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
               // am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                am.cancel(pi);*/
                alarmtime.setTimeInMillis(dbAdapter.getalarm1());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(ctxt, 0, intentAlarm, 0);
                alarmMgr.set(AlarmManager.RTC_WAKEUP,alarmtime.getTimeInMillis(),pendingIntent);
                Toast.makeText(ctxt,"Alarm retained"+alarmtime.getTimeInMillis(),Toast.LENGTH_SHORT).show();

                try {
                    Intent cancelalarm = new Intent(AlarmClock.ACTION_DISMISS_ALARM);
                    cancelalarm.putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE,AlarmClock.ALARM_SEARCH_MODE_TIME);
                    cancelalarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                    cancelalarm.putExtra(AlarmClock.EXTRA_HOUR,alarmtime.get(Calendar.HOUR_OF_DAY));
                    cancelalarm.putExtra(AlarmClock.EXTRA_MINUTES,alarmtime.get(Calendar.MINUTE)-3);
                    ctxt.startActivity(cancelalarm);

                } catch (ActivityNotFoundException e) {
                    Log.e("OnBOOTReceiver",e.getMessage().toString());
                }
                //   cancelalarm.putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE,AlarmClock.ALARM_SEARCH_MODE_LABEL);
                //   cancelalarm.putExtra(AlarmClock.ALARM_SEARCH_MODE_LABEL,"BOOTALARM");

            }
        }
        //time.setTimeInMillis(System.currentTimeMillis());
        // time.add(Calendar.SECOND, 10);
       // alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),10000,pendingIntent);*/
    }
    if (intent.getAction().equalsIgnoreCase("android.intent.action.ACTION_SHUTDOWN")){
        Calendar time = Calendar.getInstance();
        if (dbAdapter.getalarm1()!=0){
            time.setTimeInMillis(dbAdapter.getalarm1());
            int alarmHour = time.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
            int alarmminute = time.get(Calendar.MINUTE);
            Intent openNewAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
            openNewAlarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            openNewAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            openNewAlarm.putExtra(AlarmClock.EXTRA_HOUR, alarmHour);
            openNewAlarm.putExtra(AlarmClock.EXTRA_MESSAGE,"BOOTALARM");
            openNewAlarm.putExtra(AlarmClock.EXTRA_MINUTES,alarmminute-3);
            ctxt.startActivity(openNewAlarm);
            Toast.makeText(ctxt,"Alarm Saved"+time.getTimeInMillis(),Toast.LENGTH_SHORT).show();
        }
    }
}
}