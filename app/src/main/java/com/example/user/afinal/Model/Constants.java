package com.example.user.afinal.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Constants {
 
    public static final String GEOFENCE_ID_STAN_UNI = "STAN_UNI";
    public static final float GEOFENCE_RADIUS_IN_METERS = 100;
    public  static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 100;
 
    /**
     * Map for storing information about stanford university in the Stanford.
     */
    public static final HashMap<String, LatLng> AREA_LANDMARKS = new HashMap<String, LatLng>();
    public static final HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<String, LatLng>();

    static {
        // stanford university.

        AREA_LANDMARKS.put(GEOFENCE_ID_STAN_UNI, new LatLng(13.029737,80.248162));


        BAY_AREA_LANDMARKS.put("My home", new LatLng(  13.029737, 80.248162));

        // Googleplex.
        BAY_AREA_LANDMARKS.put("aljwadah school", new LatLng( 13.031842921944303,80.25030735880136));
    }
}