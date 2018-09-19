package com.example.user.afinal.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.afinal.ComplexPreferences;
import com.example.user.afinal.DATABASE.DBAdapter;
import com.example.user.afinal.Model.loginContact;
import com.example.user.afinal.R;
import com.example.user.afinal.Utility.GPSTracker;
import com.example.user.afinal.Utility.Keystore;
import com.example.user.afinal.Utility.MyApplication;
import com.example.user.afinal.Utility.OnSwipeTouchListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DashboardActivity extends Activity {
    SimpleSideDrawer slide_me;
    ImageView img_menu;
    public static boolean checkDay = false;
    View view;
    RelativeLayout rtl;
    DBAdapter adb = new DBAdapter(DashboardActivity.this);
    String[] logindetails;
    loginContact contact = new loginContact();
    GPSTracker gps;
    Button button2;
    private Keystore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      // requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        slide_me = new SimpleSideDrawer(this);
        slide_me.setLeftBehindContentView(R.layout.slidemenu);
        adb.open();
        gps = new GPSTracker(this);
        init();
        initcallback();
        double distance = MyApplication.getInstance().getDistance();
        double  getdistance = gps.getdistance();
        if (getdistance>=5){
            Toast.makeText(getApplicationContext(),"Out of region "+getdistance+" km",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"Inside the region "+getdistance+" km",Toast.LENGTH_SHORT).show();
        }
        initmenu();
        googleplaycheck();
       
    }

    private void googleplaycheck() {
        try {
            int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            switch (resultCode) {
                case ConnectionResult.SUCCESS:
                    Log.d("googleplaycheck", "Google Play Services is ready to go!");
                    break;
                default:
                    showPlayServicesError(resultCode);
                    return;
            }

        }catch (Exception ex){
            Log.e("dashbrd googleplaycheck" ,ex.getMessage().toString());
        }
    }
    private void showPlayServicesError(int errorCode) {
        GoogleApiAvailability.getInstance().showErrorDialogFragment(this, errorCode, 10,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                });
    }

    public void init(){


        Location mylocation = gps.getLocation();
        rtl = (RelativeLayout)findViewById(R.id.linear11);
        button2 = (Button)findViewById(R.id.button2);
        store = Keystore.getInstance(getApplicationContext());//Creates or Gets our key pairs.  You MUST have access to current context!

        int int_var = 1;
        String string_var = new String("test");

        store.putInt("key name to store int value",int_var);
        store.put("key name to store string value",string_var);
       /* rtl.setOnTouchListener(new OnSwipeTouchListener() {
            public boolean onSwipeTop() {
                Toast.makeText(getApplicationContext(), "top", Toast.LENGTH_SHORT).show();
                return true;
            }
            public boolean onSwipeRight() {
                Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
                slide_me.toggleLeftDrawer();
                return true;
            }
            public boolean onSwipeLeft() {
                Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
                return true;
            }
            public boolean onSwipeBottom() {
                Toast.makeText(getApplicationContext(), "bottom", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
        img_menu =(ImageView) findViewById(R.id.img_menu);
    }
    public void initcallback()  {
        //   logindetails = adb.getlogindata();
        try {
            JSONObject newtestjson = new JSONObject(loadJSONFromAsset());
            MyApplication.getInstance().setJsontestobject(newtestjson);
        } catch (JSONException e) {
        Log.e("convertstring to json",e.getMessage().toString());
        }



        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }

        Log.e("Mobile version" ,"OS: " + builder.toString());

        img_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                slide_me.toggleLeftDrawer();
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = store.get("myapplication");
                MyApplication obj = gson.fromJson(json, MyApplication.class);
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(MyApplication.getInstance().getPintent());

                //2nd method
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(), "object_prefs", 0);
                MyApplication myApplication = complexPreferences.getObject("object_value", MyApplication.class);
            }
        });


    }
    public void initmenu(){
        RelativeLayout lin_Dashboard = (RelativeLayout)findViewById(R.id.lin_Dashboard);
        RelativeLayout lin_Listview = (RelativeLayout)findViewById(R.id.lin_Listview);
        RelativeLayout lin_maps = (RelativeLayout)findViewById(R.id.lin_maps);
        RelativeLayout lin_whatapp = (RelativeLayout)findViewById(R.id.lin_whatapp);
        RelativeLayout lin_registerstion = (RelativeLayout)findViewById(R.id.lin_registerstion);
        RelativeLayout lin_mapaddress = (RelativeLayout)findViewById(R.id.lin_mapaddress);
        RelativeLayout lin_bluetooth = (RelativeLayout)findViewById(R.id.lin_bluetooth);
        RelativeLayout lin_gallery = (RelativeLayout)findViewById(R.id.lin_gallery);
        RelativeLayout lin_scanner = (RelativeLayout)findViewById(R.id.lin_scanner);
        RelativeLayout lin_dynamic = (RelativeLayout)findViewById(R.id.lin_dynamic);
        ImageView imglogout = (ImageView)findViewById(R.id.imglogout);
        ImageView imgloginpic = (ImageView)findViewById(R.id.imgloginpic);
        TextView txtusername = (TextView)findViewById(R.id.txtusername) ;
        if (MyApplication.getInstance().getLogintype().equalsIgnoreCase("Registereduser")){
            txtusername.setText( MyApplication.getInstance().getId()+ " "+MyApplication.getInstance().getLoginuser());
            if (MyApplication.getInstance().getLoginprofilepic()!=null){
            imgloginpic.setImageBitmap(convertToBitmap(MyApplication.getInstance().getLoginprofilepic()));}
        }else{
            txtusername.setText(MyApplication.getInstance().getLoginemail());
            if (MyApplication.getInstance().getLoginprofilepic()!=null){
                imgloginpic.setImageBitmap(convertToBitmap(MyApplication.getInstance().getLoginprofilepic()));
            }

        }

        // txtusername.setText(logindetails[1]);
       // byte[] profilepic =logindetails[2].getBytes();
       // imgloginpic.setImageBitmap(convertToBitmap(profilepic));

        lin_Dashboard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,DashboardActivity.class);
                startActivityForResult(i,500);


            }
        });

        imglogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyApplication.getInstance().setLogout(true);
                adb.deletelogindata();
                Intent DashboardActivityIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                DashboardActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(DashboardActivityIntent);
                finish();

            }
        });

        lin_Listview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(DashboardActivity.this,ListViewNameActivity.class);
                startActivityForResult(i,500);



            }
        });

        lin_maps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(DashboardActivity.this,MapsActivity.class);
                startActivityForResult(i,500);



            }
        });

        lin_whatapp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(DashboardActivity.this,WhatappActivity.class);
                startActivityForResult(i,500);



            }
        });

        lin_registerstion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(DashboardActivity.this,RegisterActivity.class);
                startActivityForResult(i,500);



            }
        });
        lin_mapaddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              /* Intent i = new Intent(DashboardActivity.this,MapAddress.class);
                startActivityForResult(i,500);*/

            }
        });
        lin_bluetooth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(DashboardActivity.this,BluetoothActivity.class);
                startActivityForResult(i,500);



            }
        });
        lin_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(DashboardActivity.this,GalleryActivity.class);
                startActivityForResult(i,500);

            }
        });
        lin_scanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               Intent i = new Intent(DashboardActivity.this,ScannerActivity.class);
                startActivityForResult(i,500);



            }
        });
        lin_dynamic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(DashboardActivity.this,Dynamic.class);
                startActivityForResult(i,500);



            }
        });
    }

    private Bitmap convertToBitmap(byte[] b) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("testdata.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(DashboardActivity.this,"Please click LOGOUT ",Toast.LENGTH_LONG).show();
    }
}
