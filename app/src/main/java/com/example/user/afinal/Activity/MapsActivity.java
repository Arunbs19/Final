package com.example.user.afinal.Activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.afinal.DownloadResultReceiver;
import com.example.user.afinal.GeofenceReceiver;
import com.example.user.afinal.Model.Constants;
import com.example.user.afinal.Utility.AlertDialogManager;
import com.example.user.afinal.Utility.ConnectionDetector;
import com.example.user.afinal.Utility.DirectionsJSONParser;
import com.example.user.afinal.Utility.GPSTracker;
import com.example.user.afinal.Utility.GeocodingLocation;
import com.example.user.afinal.R;
import com.example.user.afinal.GeofenceTrasitionService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, LocationListener, ResultCallback<Status>
{
    //from 6/04/2018
    PendingIntent pendingIntent;
    SharedPreferences sharedPreferences;
    int locationCount = 0;

    //from 7/04/2018

    private TextView textLat, textLong;
    private Location lastLocation;
    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;
    int REQ_PERMISSION = 100;
    private Marker locationMarker;
    private Marker geoFenceMarker;
    private static final long GEO_DURATION = 60 * 60 * 1000;//in milliseconds
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 500; // in meters   --1609  -1.6km
    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    private Circle geoFenceLimits;
    //10/04/2018
    protected ArrayList<Geofence> mGeofenceList;
    GeofencingClient mGeofencingClient;
    //13/04/2014
    protected ResultReceiver mResultReceiver;
    private static MapsActivity inst;
    AlarmManager alarmManager;
    Location circlelocation;
    //16/04/2018
    boolean iscirclelocation = false;
    double  getdistance;


    public static MapsActivity instance(){
        return inst;
    }


    private static final String TAG = MapsActivity.class.getSimpleName();
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private List<LatLng> polyLineList;
    private Marker marker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition;
    private int index, next;
    private LatLng sydney;
    private Button button;
    private EditText destinationEditText;
    private String destination;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, greyPolyLine;


    LocationRequest mLocationRequest;
    public  GoogleApiClient mGoogleApiClient;

    ArrayList markerpoints = new ArrayList();
    ArrayList latlonglist = new ArrayList();
    LatLng latLng;

    Boolean isInternetPresent = false;
    AlertDialogManager alert;
    ConnectionDetector cd;
    GPSTracker gps;
    com.example.user.afinal.InfoWindowCustom windw;

    LatLng orig = null;
    LatLng destinat = null;
    EditText Destination;
    Button Find;
    TextView locationTv;
    Marker currLocationMarker;
    Location mLastLocation;
    LocationManager locationManager;

    String locationAddress;
    String slatitude;
    String slongitude;
    Double dlatitude = null;
    Double dlongitude = null;
    Location Start_loc = new Location(" ");
    Location End_loc = new Location(" ");
    private static DecimalFormat df2 = new DecimalFormat("#.00");

    RadioButton rbDriving;
    RadioButton rbBiCycling;
    RadioButton rbWalking;
    RadioGroup rgModes;
    TextView tvdistance;
    int mMode = 0;
    final int MODE_DRIVING = 0;
    final int MODE_BICYCLING = 1;
    final int MODE_WALKING = 2;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
      //  moveTaskToBack(true);
        cd = new ConnectionDetector(getApplicationContext());
        alert = new AlertDialogManager();
        gps = new GPSTracker(this);
        Location dummy = gps.getLocation();
        windw = new com.example.user.afinal.InfoWindowCustom(this);
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
        //    alert.showAlertDialog(MapsActivity.this, "Internet Connection Error", "Please connect to working Internet connection and come back", false);
            Toast.makeText(getApplicationContext(), "Internet Connection Error", Toast.LENGTH_SHORT).show();

        }
        autoGPSON();
        init();
        textLat = (TextView) findViewById(R.id.lat);
        textLong = (TextView) findViewById(R.id.lon);


        mGeofenceList = new ArrayList<Geofence>();
        populateGeofenceList();

        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }
        // task2();
        //checkLocationPermission();



    }

    private void autoGPSON() {
        String[] cmds = {"cd /system/bin" ,"settings put secure location_providers_allowed +gps"};
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            for (String tmpCmd : cmds) {
                os.writeBytes(tmpCmd + "\n");
            }
            os.writeBytes("exit\n");
            os.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void populateGeofenceList() {
      /*  FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("roads").child("Name").child("locations");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(dataSnapshot.getKey())
                        .setCircularRegion(
                                (Double) dataSnapshot.child("lat").getValue(),
                                (Double) dataSnapshot.child("lang").getValue(),200
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        )
                        .build());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        for (Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)


                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build());
        }



    }

    private void init() {
        try {
           // FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
            Destination = (EditText) findViewById(R.id.edt_dest);
            locationTv = (TextView) findViewById(R.id.latlongLocation);
            Find = (Button) findViewById(R.id.btn_find);
            rbDriving = (RadioButton) findViewById(R.id.rb_driving);
            rbBiCycling = (RadioButton) findViewById(R.id.rb_bicycling);
            rbWalking = (RadioButton) findViewById(R.id.rb_walking);
            rgModes = (RadioGroup) findViewById(R.id.rg_modes);
            tvdistance = (TextView) findViewById(R.id.txt3);

            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            polyLineList = new ArrayList<>();
            button = (Button) findViewById(R.id.destination_button);
            destinationEditText = (EditText) findViewById(R.id.edittext_place);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    destination = destinationEditText.getText().toString();
                    destination = destination.replace(" ", "+");
                    Log.d(TAG, destination);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            });

                    if (gps.canGetLocation()) {
                Log.d("GPS Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
            } else {
                // Can't get user's current location
                //alert.showAlertDialog(MapsActivity.this, "GPS Status","Couldn't get location information. Please enable GPS",false);
                //gps.showSettingsAlert();
                //init();
                // stop executing code by return
                // return;
            }


            rgModes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                   /* if (latlonglist.size() >= 2) {

                        LatLng origin = (LatLng) latlonglist.get(0);
                        LatLng dest = (LatLng) latlonglist.get(1);
                        String url = getDirectionsUrl(origin, dest);
                        DownloadTask downoadTask = new DownloadTask();
                        downoadTask.execute(url);
                    }*/
                }
            });

            Find.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String address = Destination.getText().toString();
                    if (isInternetPresent != false) {
                        GeocodingLocation locationAddress = new GeocodingLocation();
                        locationAddress.getAddressFromLocation(address,
                                getApplicationContext(), new GeocoderHandler());
                    } else {
                        Toast.makeText(getApplicationContext(), "without internet you cannot find location", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            circlelocation = new Location(LocationManager.NETWORK_PROVIDER);
            circlelocation.setLatitude(13.031842921944303);
            circlelocation.setLongitude(80.25030735880136);

        } catch (Exception d) {
            Log.d("mapsacivity_init", d.getMessage().toString());
        }

    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if (status.isSuccess()) {
            drawGeofence();
        } else {


            Toast.makeText(getApplicationContext(), "onResult fail", Toast.LENGTH_SHORT).show();
        }
    }

    // Draw Geofence circle on GoogleMap

    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");
        if (geoFenceLimits != null)
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(GEOFENCE_RADIUS);
        geoFenceLimits = mMap.addCircle(circleOptions);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick(" + latLng + ")");
        iscirclelocation = true;
        markerForGeofence(latLng);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClickListener: " + marker.getPosition());
        return false;
    }

    // Create a Location Marker
    private void markerLocation(LatLng latLng) {
        Log.i(TAG, "markerLocation(" + latLng + ")");
        String title = latLng.latitude + ", " + latLng.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("our fixed location");
        if (mMap != null) {
            // Remove the anterior marker
            if (locationMarker != null)
                locationMarker.remove();
            locationMarker = mMap.addMarker(markerOptions);
            float zoom = 14f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            mMap.animateCamera(cameraUpdate);
        }
    }

    // Create a marker for the geofence creation
    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence(" + latLng + ")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if (mMap != null) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();
            circlelocation.setLatitude(latLng.latitude);
            circlelocation.setLongitude(latLng.longitude);
            geoFenceMarker = mMap.addMarker(markerOptions);
            double distance = lastLocation.distanceTo(circlelocation);
            getdistance  = distance * 0.001;
            startGeofence();
        }
    }

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
               // .addGeofence(geofence)
                //by listmethod
                .addGeofences(mGeofenceList)
                .build();


    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;
        Intent intent = new Intent(MapsActivity.this, GeofenceTrasitionService.class);
        intent.putExtra("distance",getdistance);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.MINUTE, Calendar.MINUTE );
        // startService(intent);
        // Intent intent = new Intent("com.example.geofence.ACTION_RECEIVE_GEOFENCE");
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        return PendingIntent.getService(MapsActivity.this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //by me
     /* Intent myIntent = new Intent(MapsActivity.this, GeofenceReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MapsActivity.this, 0, myIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MapsActivity.this, 0, myIntent, 0);
        return pendingIntent;*/

    }



    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission()) {
            try {
                pendingIntent = createGeofencePendingIntent();

                //1st method
              /*  if (mGoogleApiClient.isConnected())
                LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);*/
//2nd method
                mGeofencingClient.addGeofences(request, createGeofencePendingIntent())
                        .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("geofencing success", "successfully added");
                                drawGeofence();
                               // Toast.makeText(getApplicationContext(), "geofencing success", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("geofencing failure", "failed in added");
                      //  Toast.makeText(getApplicationContext(), "geofencing failure", Toast.LENGTH_SHORT).show();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        //    Toast.makeText(MapsActivity.this, "Location alter has been added", Toast.LENGTH_SHORT).show();
                        } else {
                         //   Toast.makeText(MapsActivity.this, "Location alter could not be added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (SecurityException securityException) {
                Log.e("addgeofence", securityException.getMessage().toString());
            }
        }
    }
    //Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");
        if (geoFenceMarker != null) {
            //2nd method
            mGeofencingClient = LocationServices.getGeofencingClient(MapsActivity.this);
            //1st method
            Geofence geofence = createGeofence(geoFenceMarker.getPosition(), GEOFENCE_RADIUS);
            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            addGeofence(geofenceRequest);
           // initreceiver();
            Log.e("Built gefencing request: ",String.valueOf(geofenceRequest.getGeofences().toString()));
            Log.e("Geofence initial trigger: " , String.valueOf(geofenceRequest.getInitialTrigger()));

        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    private  void initreceiver(){
   /* mResultReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Check result code and/or resultData and take necessary action
            if(resultCode == 0){
              String resultstr =  resultData.getString("FROM_GEOFENCE_KEY");
                Toast.makeText(getApplicationContext(),resultstr,Toast.LENGTH_SHORT).show();
            }
        }
    };*/

        DownloadResultReceiver mreceiver = new DownloadResultReceiver(new Handler());
        mreceiver.setReceiver(new DownloadResultReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    String resultValue = resultData.getString("resultValue");
                    Toast.makeText(MapsActivity.this, resultValue, Toast.LENGTH_SHORT).show();
                }
            }
        });

    // if(mResultReceiver != null){
    //   mAddGeofencesButton.setText("Add Geofences NOT NULL TEST");
    //}
    Intent intent = new Intent(this, GeofenceTrasitionService.class);
    intent.putExtra("RECEIVER", mreceiver);
        startService(intent);
   // getApplicationContext().startService(intent);
}

    public double distanceGeoPoints(Marker geoPoint01, Marker geoPoint02) {
        double lat1 = geoPoint01.getPosition().latitude / 1E6;
        double lng1 = geoPoint01.getPosition().longitude / 1E6;
        double lat2 = geoPoint02.getPosition().latitude / 1E6;
        double lng2 = geoPoint02.getPosition().longitude / 1E6;
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        //   double dist = earthRadius * c;

        //  int meterConversion = 1609;
        //   double geopointDistance = dist * meterConversion;

        //   return geopointDistance;
        double temp = 6371 * c;
        temp = temp * 0.621;
        return temp;
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    slatitude = bundle.getString("latitude");
                    slongitude = bundle.getString("longitude");
                    bundle.clear();
                    setDestinationlocation();
                    break;
                default:
                    locationAddress = null;
                    slatitude = null;
                    slongitude = null;
            }
            locationTv.setText(locationAddress);
        }

    }

    private void setDestinationlocation() {
        try {
            if (slatitude != null && slongitude != null)
                dlatitude = Double.parseDouble(slatitude);
            dlongitude = Double.parseDouble(slongitude);
            End_loc.setLatitude(dlatitude);
            End_loc.setLongitude(dlongitude);
            double distance = Start_loc.distanceTo(End_loc) / 1000;

            Toast.makeText(getApplicationContext(), "Distance" + Double.toString(distance) + "km", Toast.LENGTH_SHORT).show();
            destinat = new LatLng(Double.parseDouble(slatitude), Double.parseDouble(slongitude));
            String fdistance = String.format(" %.2f km", distance);
            //tvdistance.setText(fdistance);
            latlonglist.add(1, destinat);
            mMap.addMarker(new MarkerOptions()
                    .position(destinat)
                    .title("destination")
                    .snippet(locationAddress + "\n" + "distance" + fdistance)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
            mMap.setInfoWindowAdapter(windw);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinat, 18));
            getdirection();
        } catch (Exception e) {
            Log.d("mapAtvy_setDestination", e.getMessage().toString());
        }
    }

    private void getdirection() {
        try {
            if (latlonglist.size() >= 2) {
                LatLng origin = (LatLng) latlonglist.get(0);
                LatLng dest = (LatLng) latlonglist.get(1);
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downoadTask = new DownloadTask();
                downoadTask.execute(url);

            }

        } catch (Exception e)

        {
            Log.d("mapAvty_getdirection", e.getMessage().toString());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch ( item.getItemId() ) {
            case R.id.geofence: {
                startGeofence();
                return true;
            }
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onmapready()");
        mMap = googleMap;
        sydney = new LatLng(13.029737, 80.248162);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        final double latitude = 13.029737;
        double longitude = 80.248162;
        /*  mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        sydney = new LatLng(13.029737, 80.248162);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Home"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(googleMap.getCameraPosition().target)
                .zoom(17)
                .bearing(30)
                .tilt(45)
                .build()));
       */
        //route car map
        try {
        String requestUrl = null;
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&"
                    + "transit_routing_preference=less_driving&"
                    + "origin=" + latitude + "," + longitude + "&"
                    + "destination=" + destination + "&"
                    + "key=" + getResources().getString(R.string.google_directions_key);
            Log.d(TAG, requestUrl);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    requestUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response + "");
                            try {
                                JSONArray jsonArray = response.getJSONArray("routes");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject route = jsonArray.getJSONObject(i);
                                    JSONObject poly = route.getJSONObject("overview_polyline");
                                    String polyline = poly.getString("points");
                                    polyLineList = decodePoly(polyline);
                                    Log.d(TAG, polyLineList + "");
                                }
                                //Adjusting bounds
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (LatLng latLng : polyLineList) {
                                    builder.include(latLng);
                                }
                                LatLngBounds bounds = builder.build();
                                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                                mMap.animateCamera(mCameraUpdate);

                                polylineOptions = new PolylineOptions();
                                polylineOptions.color(Color.GRAY);
                                polylineOptions.width(5);
                                polylineOptions.startCap(new SquareCap());
                                polylineOptions.endCap(new SquareCap());
                                polylineOptions.jointType(ROUND);
                                polylineOptions.addAll(polyLineList);
                                greyPolyLine = mMap.addPolyline(polylineOptions);

                                blackPolylineOptions = new PolylineOptions();
                                blackPolylineOptions.width(5);
                                blackPolylineOptions.color(Color.BLACK);
                                blackPolylineOptions.startCap(new SquareCap());
                                blackPolylineOptions.endCap(new SquareCap());
                                blackPolylineOptions.jointType(ROUND);
                                blackPolyline = mMap.addPolyline(blackPolylineOptions);

                                mMap.addMarker(new MarkerOptions()
                                        .position(polyLineList.get(polyLineList.size() - 1)));

                                ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                                polylineAnimator.setDuration(2000);
                                polylineAnimator.setInterpolator(new LinearInterpolator());
                                polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        List<LatLng> points = greyPolyLine.getPoints();
                                        int percentValue = (int) valueAnimator.getAnimatedValue();
                                        int size = points.size();
                                        int newPoints = (int) (size * (percentValue / 100.0f));
                                        List<LatLng> p = points.subList(0, newPoints);
                                        blackPolyline.setPoints(p);
                                    }
                                });
                                polylineAnimator.start();
                                marker = mMap.addMarker(new MarkerOptions().position(sydney)
                                        .flat(true)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));
                                handler = new Handler();
                                index = -1;
                                next = 1;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (index < polyLineList.size() - 1) {
                                            index++;
                                            next = index + 1;
                                        }
                                        if (index < polyLineList.size() - 1) {
                                            startPosition = polyLineList.get(index);
                                            endPosition = polyLineList.get(next);
                                        }
                                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                                        valueAnimator.setDuration(3000);
                                        valueAnimator.setInterpolator(new LinearInterpolator());
                                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                                v = valueAnimator.getAnimatedFraction();
                                                lng = v * endPosition.longitude + (1 - v)
                                                        * startPosition.longitude;
                                                lat = v * endPosition.latitude + (1 - v)
                                                        * startPosition.latitude;
                                                LatLng newPos = new LatLng(lat, lng);
                                                marker.setPosition(newPos);
                                                marker.setAnchor(0.5f, 0.5f);
                                                marker.setRotation(getBearing(startPosition, newPos));
                                                mMap.moveCamera(CameraUpdateFactory
                                                        .newCameraPosition
                                                                (new CameraPosition.Builder()
                                                                        .target(newPos)
                                                                        .zoom(15.5f)
                                                                        .build()));
                                            }
                                        });
                                        valueAnimator.start();
                                        handler.postDelayed(this, 3000);
                                    }
                                }, 3000);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, error + "");
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {e.printStackTrace();}

       /* try {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    // Incrementing location count
                    locationCount++;

                    // Drawing marker on the map
                    drawMarker(point);

                    // Drawing circle on the map
                    drawCircle(point);
                    Intent proximityIntent = new Intent(".afinal.Activity.ProximityActivity");

                    // Passing latitude to the PendingActivity
                    proximityIntent.putExtra("lat",point.latitude);

                    // Passing longitude to the PendingActivity
                    proximityIntent.putExtra("lng", point.longitude);

                    // Creating a pending intent which will be invoked by LocationManager when the specified region is
                    // entered or exited
                    pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent,Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Setting proximity alert
                    // The pending intent will be invoked when the device enters or exits the region 20 meters
                    // away from the marked point
                    // The -1 indicates that, the monitor will not be expired
                    locationManager.addProximityAlert(point.latitude, point.longitude, 20, -1, pendingIntent);

                    *//** Opening the editor object to write data to sharedPreferences *//*
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Storing the latitude for the i-th location
                    editor.putString("lat"+ Integer.toString((locationCount-1)), Double.toString(point.latitude));

                    // Storing the longitude for the i-th location
                    editor.putString("lng"+ Integer.toString((locationCount-1)), Double.toString(point.longitude));

                    // Storing the count of locations or marker count
                    editor.putInt("locationCount", locationCount);

                    *//** Storing the zoom level to the shared preferences *//*
                    editor.putString("zoom", Float.toString(googleMap.getCameraPosition().zoom));

                    *//** Saving the values stored in the shared preferences *//*
                    editor.commit();

                    Toast.makeText(getBaseContext(), "Proximity Alert is added", Toast.LENGTH_SHORT).show();

                }
            });

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng point) {
                    Intent proximityIntent = new Intent("in.wptrafficanalyzer.activity.proximity");

                    pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent,Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Removing the proximity alert
                    locationManager.removeProximityAlert(pendingIntent);

                    // Removing the marker and circle from the Google Map
                    googleMap.clear();

                    // Opening the editor object to delete data from sharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Clearing the editor
                    editor.clear();

                    // Committing the changes
                    editor.commit();

                    Toast.makeText(getBaseContext(), "Proximity Alert is removed", Toast.LENGTH_LONG).show();

                }
            });





        }catch (Exception ex2){}*/

      /*  try {
            *//*   mMap = googleMap;
       // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager!=null){
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location!=null){
                onLocationChanged(location);

            }
           else  {
                Toast.makeText(getApplicationContext(), "GPS location:latitude:" + gps.getLatitude() + "&longitude: " + gps.getLongitude(), Toast.LENGTH_SHORT).show();
                orig = new LatLng(13.029737, 80.248162);
                latlonglist.add(0, orig);
                Start_loc.setLatitude(gps.getLatitude());
                Start_loc.setLongitude(gps.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(orig);
                markerOptions.title("GPS location");
                markerOptions.snippet("you are here");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mMap.addMarker(markerOptions);
                mMap.setInfoWindowAdapter(windw);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(orig));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

            }

        }
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000,0,this);*//*
        }catch ( Exception old){}*/


    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    protected synchronized void buildGoogleApiClient() {
       // Toast.makeText(this, "buildgoogleApiclient", Toast.LENGTH_LONG).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();




    }

    @Override
    protected void onStart() {
        super.onStart();
        // Call GoogleApiClient connection when starting the Activity
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnect GoogleApiClient when stopping Activity
        mGoogleApiClient.disconnect();

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected()");
       // Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        { mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(100);
            mLocationRequest.getFastestInterval();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                 LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, (com.google.android.gms.location.LocationListener) this);
            }
             if (mGoogleApiClient.isConnected())
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation!=null){
                onLocationChanged(lastLocation);
            }else {
                getLastKnownLocation();
            }
           /* PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                @Override
                public void onResult(LocationSettingsResult result) {
                       final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied.
                            // You can initialize location requests here.
                            int permissionLocation = ContextCompat
                                    .checkSelfPermission(MapsActivity.this,
                                            Manifest.permission.ACCESS_FINE_LOCATION);
                            if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                lastLocation = LocationServices.FusedLocationApi
                                        .getLastLocation(mGoogleApiClient);
                            }
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied.
                            // But could be fixed by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                // Ask to turn on GPS automatically
                                status.startResolutionForResult(MapsActivity.this,
                                        0);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied.
                            // However, we have no way
                            // to fix the
                            // settings so we won't show the dialog.
                            // finish();
                            break;
                    }
                }
            });*/

        }
    }

    private void getLastKnownLocation(){
        if ( checkPermission() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation==null){
                Location ourfixedlocation = new Location(LocationManager.GPS_PROVIDER);
                ourfixedlocation.setLatitude(13.029737);
                ourfixedlocation.setLongitude(80.248162);
                lastLocation = ourfixedlocation;
                onLocationChanged(lastLocation);
            }
            if ( lastLocation != null ) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
                markerForGeofence(new LatLng(circlelocation.getLatitude(),circlelocation.getLongitude()));


            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        }
        else
            askPermission();



    }

    // Start location Updates
    private void startLocationUpdates(){
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if ( checkPermission() )
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,locationRequest, (com.google.android.gms.location.LocationListener) this);

        }
           // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(), "onLocation change", Toast.LENGTH_SHORT).show();
        Log.d("OnLocationchange", "location");
        mLastLocation = location;
        lastLocation=location;
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        double loclat = location.getLatitude();
        double loclong = location.getLongitude();
        orig = new LatLng(loclat,loclong);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(orig);
        markerOptions.title("your Location");
  /*{       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            List<String> providerList = locationManager.getAllProviders();
            if (null != location && null != providerList && providerList.size() > 0) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddress = geocoder.getFromLocation(loclat, loclong, 1);
                    if (null != listAddress && listAddress.size() > 0) {
                        String subLocality = listAddress.get(0).getSubLocality();
                        String local = listAddress.get(0).getLocality();
                        String state = listAddress.get(0).getAdminArea();
                        String country = listAddress.get(0).getCountryName();
                        markerOptions.snippet(subLocality + ",\n" + local + ",\n" + state + ",\n" + country);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        currLocationMarker = mMap.addMarker(markerOptions);
        mMap.setInfoWindowAdapter(windw);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(orig));
        float zoom = 14f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(orig, zoom);
        mMap.animateCamera(cameraUpdate);
       // mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        latlonglist.add(0, orig);
        Start_loc.setLatitude(location.getLatitude());
        Start_loc.setLongitude(location.getLongitude());
/*  {       boolean proximity_entering = getIntent().getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
            double lat = getIntent().getDoubleExtra("lat", 0);
            double lng = getIntent().getDoubleExtra("lng", 0);
            String strLocation = Double.toString(lat) + "," + Double.toString(lng);
            if (proximity_entering) {
                Toast.makeText(getBaseContext(), "Entering the region", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getBaseContext(), "Exiting the region", Toast.LENGTH_LONG).show();
}
        }*/
        if (!iscirclelocation)
        markerForGeofence(new LatLng(circlelocation.getLatitude(),circlelocation.getLongitude()));
        double distance = location.distanceTo(circlelocation);
        getdistance  = distance * 0.001;
        DecimalFormat df = new DecimalFormat("#.##");
        if (getdistance>=5){
            Toast.makeText(getApplicationContext(),"Out of region "+df.format(getdistance)+" km",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"inside the  region "+df.format(getdistance)+" km",Toast.LENGTH_SHORT).show();
        }
    }

    private  class DownloadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... url) {
            String data =" ";
            try {
                data = downloadUrl(url[0]);
                Log.d("Background Task data",data.toString());
            }catch (Exception e){
                Log.d("Background Task",e.toString());}
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }


    }

    private class ParserTask extends AsyncTask<String,Integer,List<List<HashMap<String,String>>>>{
        @Override
        protected List<List<HashMap<String,String>>> doInBackground(String... jsonData) {
            JSONObject jobject;
            List<List<HashMap<String,String>>> routes = null;
            try {
                jobject = new JSONObject(jsonData[0]);
                Log.d("parserTask",jsonData[0].toString());
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jobject);
                Log.d("parserTask","exectuting routes");
                Log.d("parserTask",routes.toString());
            }catch (Exception e){
                Log.d("parserTask",e.toString());
                e.printStackTrace();}
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String,String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            String distance = "";
            String duration = "";
            MarkerOptions markerOptions = new MarkerOptions();
            for (int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions =new PolylineOptions();
                List<HashMap<String,String>> path = result.get(i);
                for (int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);

                }
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.geodesic(true);

                if(mMode==MODE_DRIVING)
                    lineOptions.color(Color.CYAN);
                else if(mMode==MODE_BICYCLING)
                    lineOptions.color(Color.GREEN);
                else if(mMode==MODE_WALKING)
                    lineOptions.color(Color.BLUE);
                }
            if (lineOptions!=null){
                tvdistance.setText("Distance:"+distance +",Duration:" +duration);
                mMap.addPolyline(lineOptions);}
            else {Log.d("onpostExecute","withoutpolylines drawn");}
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){
        String str_origin = "origin=" + origin.latitude + "," +origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," +dest.longitude;

        String sensor = "sensor=false";
        String mode = "mode-driving";
        if(rbDriving.isChecked()){
            mode = "mode=driving";
            mMode = 0 ;
        }else if(rbBiCycling.isChecked()){
            mode = "mode=bicycling";
            mMode = 1;
        }else if(rbWalking.isChecked()){
            mode = "mode=walking";
            mMode = 2;
        }
        String key ="AIzaSyCKKYWKC57cv9LQRaHzXSaLjdkfl_Nj2zs";

        //String parameter = str_origin + "&" +str_dest + "&" + sensor + "&" +mode;
        String parameter = str_origin + "&" +str_dest + "&" + sensor + "&" +mode + "&" +key;
        String output = "json";

        String url ="https://maps.googleapis.com/maps/api/directions/" + output +"?" +parameter;
        //String url ="https://maps.googleapis.com/maps/api/directions/" + output +"?" +key;

        return url;

    }

    // Write location coordinates on UI
    private void writeActualLocation(Location location) {
        textLat.setText( "Lat: " + location.getLatitude() );
        textLong.setText( "Long: " + location.getLongitude() );
       // markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));

    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }


    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)
    {  Log.d(TAG, "onRequestPermissionsResult()");
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
          /*  case 99: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }return;
            }*/
            case 100: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
    }
    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(20);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
       // circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);
    }
    private void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding InfoWindow title
        markerOptions.title("Location Coordinates");

        // Adding InfoWindow contents
        markerOptions.snippet(Double.toString(point.latitude) + "," + Double.toString(point.longitude));

        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);

    }

    public boolean checkLocationPermission1() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("location_permission")
                        .setMessage("_permission")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    public boolean checkLocationPermission2() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    private  void  task2() {
    try {
        boolean proximity_entering = getIntent().getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, true);

        double lat = getIntent().getDoubleExtra("lat", 0);

        double lng = getIntent().getDoubleExtra("lng", 0);

        String strLocation = Double.toString(lat) + "," + Double.toString(lng);

        if (proximity_entering) {
            Toast.makeText(getBaseContext(), "Entering the region", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getBaseContext(), "Exiting the region", Toast.LENGTH_LONG).show();

        }


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,this,requestCode);
            dialog.show();
        }else {


            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Opening the sharedPreferences object
            sharedPreferences = getSharedPreferences("location", 0);

            // Getting number of locations already stored
            locationCount = sharedPreferences.getInt("locationCount", 0);

            // Getting stored zoom level if exists else return 0
            String zoom = sharedPreferences.getString("zoom", "0");

            // If locations are already saved
            if(locationCount!=0){

                String lat2 = "";
                String lng2 = "";

                // Iterating through all the locations stored
                for(int i=0;i<locationCount;i++){

                    // Getting the latitude of the i-th location
                    lat2 = sharedPreferences.getString("lat"+i,"0");

                    // Getting the longitude of the i-th location
                    lng2 = sharedPreferences.getString("lng"+i,"0");

                    // Drawing marker on the map
                    drawMarker(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)));

                    // Drawing circle on the map
                    drawCircle(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)));
                }

                // Moving CameraPosition to last clicked position
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2))));

                // Setting the zoom level in the map on last position  is clicked
                mMap.animateCamera(CameraUpdateFactory.zoomTo(Float.parseFloat(zoom)));
            }



        }
    } catch (Exception ex) {}
}
    public void locationCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(MapsActivity.this,DashboardActivity.class);
        startActivityForResult(i,500);
    }

}