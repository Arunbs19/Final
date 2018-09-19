package com.example.user.afinal.Utility;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

/**
 * Created by USER on 12/8/2017.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;
    Location location = null;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;
    double distanceinkm;
    float distanceFrom_in_Km;


    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
        LatLng ourlocation = new LatLng(13.029737,80.248162);
        LatLng fixedlat = new LatLng( 13.031842921944303,80.25030735880136);
        double fixedlatitude = fixedlat.latitude;
        double fixedlongitude = fixedlat.longitude;
        Location fixedlocation = new Location(locationManager.GPS_PROVIDER);
        fixedlocation.setLatitude(fixedlatitude);
        fixedlocation.setLongitude(fixedlongitude);
        if (location!=null){
            double distance = location.distanceTo(fixedlocation);
            double kmdistance=distance * 0.001;
            distanceinkm=distance * 0.001;
            MyApplication.getInstance().setDistance(kmdistance);


        }else
        distanceinkm  =  distance(ourlocation.latitude,ourlocation.longitude,fixedlatitude,fixedlongitude);
        distanceFrom_in_Km = distanceFrom_in_Km(latitude,longitude,fixedlatitude,fixedlongitude);


    }



    private float distanceFrom_in_Km(double lat1, double lng1, double lat2, double lng2) {


        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        MyApplication.getInstance().setDistance(dist);
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
            }
            else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("network", "network enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location!=null){
                            MyApplication.getInstance().setOwnlocation(location);
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                } if (isGPSEnabled){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    Log.d("network","GPS enabled");
                    if (locationManager!=null){
                        location  = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location!=null){
                            MyApplication.getInstance().setOwnlocation(location);
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();}
        return location;
    }
    public void stopUsingGPS(){
        if (locationManager!=null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
    public boolean checkgps(){
        if(isGPSEnabled){
            return  true;}else {
            return false ;
        }
    }
    public double getLatitude(){
        if (location!=null){
            latitude = location.getLatitude();
        }return latitude;
    }
    public double getLongitude(){
        if (location!=null){
            longitude=location.getLongitude();
        }return longitude;
    }
    public double getdistance(){
            DecimalFormat df = new DecimalFormat("#.##");
            System.out.print(df.format(distanceinkm));
            distanceinkm = Double.parseDouble(df.format(distanceinkm));
            return distanceinkm;

    }
    public  double getdistances(String latitude,String longitude){

        Location fixedlocation = new Location(locationManager.GPS_PROVIDER);
        fixedlocation.setLatitude(Double.parseDouble(latitude));
        fixedlocation.setLongitude(Double.parseDouble(longitude));
        if (location!=null){
            double distance = location.distanceTo(fixedlocation);
            double kmdistance=distance * 0.001;
            distanceinkm=distance * 0.001;
            DecimalFormat df = new DecimalFormat("#.##");
            System.out.print(df.format(distanceinkm));
            distanceinkm = Double.parseDouble(df.format(distanceinkm));
            return distanceinkm;

        }

        DecimalFormat df = new DecimalFormat("#.##");
        System.out.print(df.format(distanceinkm));
        distanceinkm = Double.parseDouble(df.format(distanceinkm));
        return distanceinkm;

    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS is setting");
        alertDialog.setMessage("GPS is not enabled.Do you want to go Setting?");
        alertDialog.setPositiveButton("setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                mContext.startActivity(i);
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
