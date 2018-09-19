package com.example.user.afinal.Utility;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by USER on 12/12/2017.
 */

public class GeocodingLocation {
    private static final String TAG = "GeocodingLocation";

    public static void getAddressFromLocation(final String locationAddress,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                String latitude = null;
                String longitude = null;
                String details = null;

                try {
                    List addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        //sb.append(address.getLatitude()).append("\n");
                       // sb.append(address.getLongitude()).append("\n");
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getAdminArea()).append("\n");
                        sb.append(address.getCountryName()).append("\n");
                       // sb.append(address.getPostalCode());
                        result = sb.toString();
                        double lat = address.getLatitude();
                        double lng = address.getLongitude();
                        latitude =Double.toString(lat);
                        longitude =Double.toString(lng);


                    }else {Toast.makeText(context,"unable to connect Geocoder",Toast.LENGTH_SHORT).show();}
                } catch (IOException e) {
                   //Toast.makeText(context,"unable to connect Geocoder",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    try {Message message = Message.obtain();
                        message.setTarget(handler);
                        if (result != null) {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            //result = "Address: " + locationAddress + "\nLatitude and Longitude :\n" + result;
                            result = "Address: " + locationAddress + "\n" + result;
                            bundle.putString("address", result);
                            bundle.putString("latitude",latitude);
                            bundle.putString("longitude",longitude);
                            message.setData(bundle);
                        } else {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            result = "Address: " + locationAddress +
                                    "\n Unable to get Latitude and Longitude for this address location.";
                            bundle.putString("address", result);
                            message.setData(bundle);
                        }
                        message.sendToTarget();

                    }catch (Exception e) {Log.d("GEOcoding",e.getMessage().toString());}

                }
            }
        };
        thread.start();
    }
}
