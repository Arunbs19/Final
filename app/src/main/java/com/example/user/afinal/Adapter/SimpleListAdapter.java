package com.example.user.afinal.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.afinal.Model.SimpleListClass;
import com.example.user.afinal.R;

import java.util.ArrayList;

public class SimpleListAdapter extends ArrayAdapter<SimpleListClass> {
    Context context;
    ViewHolder holder = null;
    int layoutResourceId;
    String srt;
    String id;
    AlertDialog alert;
    int count=0;
    ArrayList<SimpleListClass> offlist = new ArrayList<SimpleListClass>();

    public SimpleListAdapter(Context context, int layoutResourceId,
                             ArrayList<SimpleListClass> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.offlist = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;


        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.txtquestion = (TextView) row.findViewById(R.id.txtquestion);


	/*if (notifications.getNotstatus().equals("0")) {
		row.setBackgroundColor(Color.parseColor("#ffffff"));
	} else {
		row.setBackgroundColor(Color.parseColor("#ededed"));
	}*/



            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final SimpleListClass parameter = offlist.get(position);
        holder.txtquestion.setText(parameter.getQuestion());


        return row;

    }



    static class ViewHolder {
        TextView txtquestion;
    }



}