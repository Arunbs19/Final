package com.example.user.afinal.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by USER on 12/8/2017.
 */

public class ConnectionDetector {
    private Context _context;
    public ConnectionDetector(Context context){
        this._context=context;
    }
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity =(ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity!=null){
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info !=null)
            for (int i =0;i<info.length;i++)
                if (info[i].getState() ==NetworkInfo.State.CONNECTED)
                {return  true;}
    }Toast.makeText(_context,"no networkconnection",Toast.LENGTH_SHORT).show();
    return false;

    }

}
