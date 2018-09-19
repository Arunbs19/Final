package com.example.user.afinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.user.afinal.Activity.DashboardActivity;
import com.example.user.afinal.Activity.LoginActivity;
import com.example.user.afinal.DATABASE.DBAdapter;
import com.example.user.afinal.Model.loginContact;
import com.example.user.afinal.Utility.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by USER on 12/8/2017.
 */

public class SplashScreen extends Activity {
    DBAdapter dbadapter = new DBAdapter(SplashScreen.this);
    loginContact loginperson;

    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_splash_screen);
        dbadapter.open();
        MyApplication.getInstance().setDIRECTORY(MyApplication.DIRECTORY);
        File destination = new File(MyApplication.getDIRECTORY());
        if (destination.isDirectory())
        {
            String[] children = destination.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(destination, children[i]).delete();
            }
        }
        if(!destination.exists())
        {
            destination.mkdirs();
        }

        String log_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File logfile = new File(destination, log_name+".txt");
        if (logfile.exists()) {
            logfile.delete();
        }



        try {
            Runtime.getRuntime().exec("logcat  -v time -f "+logfile.getAbsolutePath());
        } catch (IOException e) {
        Log.e("splashscreen","unable to getruntime log");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        handerdelay();
    }

    private void handerdelay() {
        try {
            loginperson= dbadapter.getlogincontact();
            if (loginperson!=null)
            {
                if(loginperson.getType().equalsIgnoreCase("Registereduser")){
                    MyApplication.getInstance().setLogintype(loginperson.getType());
                    MyApplication.getInstance().setLoginuser(loginperson.getUsername());
                    MyApplication.getInstance().setLoginprofilepic(loginperson.getImage());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashScreen.this, DashboardActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.screenin, R.anim.screenout);
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                }else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.screenin, R.anim.screenout);
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                }


            }else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.screenin, R.anim.screenout);
                        finish();
                    }
                }, SPLASH_TIME_OUT);
            }

        }catch (Exception ex){
Log.e("handledelay",ex.getMessage().toString());
        }
    }
}
