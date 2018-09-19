package com.example.user.afinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by USER on 12/15/2017.
 */

public class InfoWindowCustom  implements GoogleMap.InfoWindowAdapter {
    Context context;
    LayoutInflater inflater;
    public InfoWindowCustom(Context context) {
        this.context = context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =inflater.inflate(R.layout.echo_info_window,null);
        TextView title =(TextView)v.findViewById(R.id.info_window_title);
        TextView subtitle = (TextView)v.findViewById(R.id.info_window_subtitle);
        title.setText(marker.getTitle());
        subtitle.setText(marker.getSnippet());
        return v;
    }
}
