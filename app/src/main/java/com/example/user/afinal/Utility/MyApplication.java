package com.example.user.afinal.Utility;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.GeofencingClient;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by USER on 1/11/2018.
 */

public class MyApplication extends android.app.Application {
    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    private GoogleApiClient mGoogleApiClient;
    String msgid;
    String msgname;
    RecyclerView recycleview;

    int id;
    String loginuser;
    byte[] loginprofilepic;
    String logintype;
    String loginemail;
    boolean logout;
    String testdata;
    JSONObject jsontestobject;
    JSONArray jsonformarray;
    TextView bartext;
    Location ownlocation;
    GeofencingClient mLocationclient;
    double distance;
    PendingIntent pintent;

    public  static  String  DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/B.S.A/";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public String getMsgname() {
        return msgname;
    }

    public void setMsgname(String msgname) {
        this.msgname = msgname;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public RecyclerView getRecycleview() {
        return recycleview;
    }

    public void setRecycleview(RecyclerView recycleview) {
        this.recycleview = recycleview;
    }

    public static String getDIRECTORY() {
        return DIRECTORY;
    }

    public static void setDIRECTORY(String DIRECTORY) {
        MyApplication.DIRECTORY = DIRECTORY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginuser() {
        return loginuser;
    }

    public void setLoginuser(String loginuser) {
        this.loginuser = loginuser;
    }

    public byte[] getLoginprofilepic() {
        return loginprofilepic;
    }

    public void setLoginprofilepic(byte[] loginprofilepic) {
        this.loginprofilepic = loginprofilepic;
    }

    public String getLogintype() {
        return logintype;
    }

    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    public String getLoginemail() {
        return loginemail;
    }

    public void setLoginemail(String loginemail) {
        this.loginemail = loginemail;
    }

    public boolean isLogout() {
        return logout;
    }

    public void setLogout(boolean logout) {
        this.logout = logout;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    public String getTestdata(String s) {
        return testdata;
    }

    public void setTestdata(String testdata) {
        this.testdata = testdata;
    }

    public JSONObject getJsontestobject() {
        return jsontestobject;
    }

    public void setJsontestobject(JSONObject jsontestobject) {
        this.jsontestobject = jsontestobject;
    }

    public JSONArray getJsonformarray() {
        return jsonformarray;
    }

    public void setJsonformarray(JSONArray jsonformarray) {
        this.jsonformarray = jsonformarray;
    }

    public TextView getBartext() {
        return bartext;
    }

    public void setBartext(TextView bartext) {
        this.bartext = bartext;
    }

    public Location getOwnlocation() {
        return ownlocation;
    }

    public void setOwnlocation(Location ownlocation) {
        this.ownlocation = ownlocation;
    }

    public GeofencingClient getmLocationclient() {
        return mLocationclient;
    }

    public void setmLocationclient(GeofencingClient mLocationclient) {
        this.mLocationclient = mLocationclient;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public PendingIntent getPintent() {
        return pintent;
    }

    public void setPintent(PendingIntent pintent) {
        this.pintent = pintent;
    }
}
