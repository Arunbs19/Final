package com.example.user.afinal.Activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.user.afinal.AlarmReceiver;
import com.example.user.afinal.ComplexPreferences;
import com.example.user.afinal.DATABASE.DBAdapter;
import com.example.user.afinal.R;
import com.example.user.afinal.Utility.GPSTracker;
import com.example.user.afinal.Utility.Keystore;
import com.example.user.afinal.Utility.MyApplication;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

public class BluetoothActivity extends AppCompatActivity {
    EditText message;
    Button printbtn;

    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;

    //11/04/2018
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static BluetoothActivity inst;
    private TextView alarmTextView;
    GPSTracker gps;
    DatePicker datepicker;
    DBAdapter dbAdapter = new DBAdapter(BluetoothActivity.this);
    Button btnreboot;
    Keystore store;


    public static BluetoothActivity instance(){
        return inst;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        //gps = new GPSTracker(this);
        dbAdapter.open();
        store = Keystore.getInstance(getApplicationContext());//Creates or Gets our key pairs.  You MUST have access to current context!
        init();
        alarm();
        initcallback();
        turnGPSOn();

    }

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }


        String  beforeEnable = Settings.Secure.getString (getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        String newSet = String.format ("%s,%s",
                beforeEnable,
                LocationManager.GPS_PROVIDER);
        try {
            Settings.Secure.putString (getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED,
                    newSet);
        } catch(Exception e) {}
    }

    private void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    public void alarm(){
        try {
            alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
            alarmTextView = (TextView) findViewById(R.id.alarmText);
            datepicker = (DatePicker)findViewById(R.id.alarmdatepicker);
            ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);

            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmToggle.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (((ToggleButton) view).isChecked()) {
                        Log.d("MyActivity", "Alarm On");

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_MONTH,datepicker.getDayOfMonth());
                            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
                            Intent myIntent = new Intent(BluetoothActivity.this, AlarmReceiver.class);
                            //myIntent.putExtra("distance",gps.getdistance());
                            //startService(myIntent);
                            pendingIntent = PendingIntent.getBroadcast(BluetoothActivity.this, 0, myIntent, 0);
                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
                            MyApplication.getInstance().setPintent(pendingIntent);


                            //2nd method
                       /* try {
                            Gson gson = new Gson();
                            MyApplication myApplication = MyApplication.getInstance();
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(), "object_prefs", 0);
                            complexPreferences.clearObject();
                            complexPreferences.putObject("object_value", myApplication);
                            complexPreferences.commit();
                        }
                        catch (StackOverflowError e){
                            Log.e("Bluetootoohactvy","stachoverflow");
                        }

                            dbAdapter.clearalarm();
                            dbAdapter.insertalarm1(calendar.getTimeInMillis());
                            Toast.makeText(getApplicationContext(),String.valueOf(dbAdapter.getalarm1()), Toast.LENGTH_SHORT).show();

                        //27/04/2018
                            Intent openNewAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                            openNewAlarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            openNewAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                            openNewAlarm.putExtra(AlarmClock.EXTRA_HOUR, alarmTimePicker.getCurrentHour());
                            openNewAlarm.putExtra(AlarmClock.EXTRA_MINUTES,  alarmTimePicker.getCurrentMinute()-1);
                            startActivity(openNewAlarm);*/

                    } else {
                        alarmManager.cancel(pendingIntent);
                        setAlarmText("");
                        Log.d("MyActivity", "Alarm Off");
                    }
                }
            });
        }catch (Exception ex){

        }
    }

   /* public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            Log.d("MyActivity", "Alarm On");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Intent myIntent = new Intent(BluetoothActivity.this, AlarmReceiver.class);
            myIntent.putExtra("distance",gps.getdistance());
            pendingIntent = PendingIntent.getBroadcast(BluetoothActivity.this, 0, myIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            setAlarmText("");
            Log.d("MyActivity", "Alarm Off");
        }
    }
*/
    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

    private void init() {
        try {
            message = (EditText) findViewById(R.id.message);
            printbtn = (Button) findViewById(R.id.printButton);

        } catch (Exception ex) {
            Log.e("bluetooth init", ex.getMessage().toString());
        }

    }

    private void initcallback() {
        try {
            printbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    connect();
                }
            });

        } catch (Exception ex) {
            Log.e("bluetooth initcall", ex.getMessage().toString());
        }
    }

    protected void connect() {
        if (btsocket == null) {
            Intent BTIntent = new Intent(getApplicationContext(),BTDeviceList.class);
          this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        } else {

            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btoutputstream = opstream;
            print_bt();

        }

    }

    private void print_bt() {
try { try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    e.printStackTrace();
}
    byte[] printformat = { 0x1B, 0*21, FONT_TYPE };
    btoutputstream.write(printformat);
    String msg = message.getText().toString();
    btoutputstream.write(msg.getBytes());
    btoutputstream.write(0x0D);
    btoutputstream.write(0x0D);
    btoutputstream.write(0x0D);
    btoutputstream.flush();

}catch (IOException e) {
    e.printStackTrace();
}




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = BTDeviceList.getSocket();
            if(btsocket != null){
                print_bt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                btoutputstream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}