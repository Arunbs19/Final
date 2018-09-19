package com.example.user.afinal.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.afinal.Activity.ChatroomActivity;
import com.example.user.afinal.Model.BaseMessage;
import com.example.user.afinal.R;
import com.example.user.afinal.Utility.MyApplication;

import java.util.ArrayList;

public class Whatapplistadapter extends ArrayAdapter
    {
       /* Activity cntx;
        public Whatapplistadapter(Activity context)
        {
            // TODO Auto-generated constructor stub
            this.cntx=context;

        }*/


        Context context;
        int layoutResourceId;
        ArrayList<BaseMessage> parameres =new ArrayList<BaseMessage>();;


        public Whatapplistadapter(Context context2, int layoutResourceId, ArrayList<BaseMessage> parameres) {
            super(context2,layoutResourceId,parameres);
                    this.context = context2;
                    this.layoutResourceId = layoutResourceId;
                    this.parameres = parameres;

        }

       /* public int getCount()
        {
            // TODO Auto-generated method stub
            return parameres.size();
        }

        public Object getItem(int position)
        {
            // TODO Auto-generated method stub
            return parameres.get(position);
        }

        public long getItemId(int position)
        {
            // TODO Auto-generated method stub
            return parameres.size();
        }*/

        public View getView(final int position, View convertView, ViewGroup parent)
        {
             ViewHolder holder = null;
            View row=convertView;

            if (row == null) {
                LayoutInflater  layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                    //row = layoutInflater.inflate(R.layout.whatapplistrow, null);
                row = layoutInflater.inflate(layoutResourceId, parent, false);

               /* LayoutInflater inflater=cntx.getLayoutInflater();
                row=inflater.inflate(R.layout.whatapplistrow, null);*/
                holder = new ViewHolder();
                holder.txtid = (TextView) row.findViewById(R.id.textID);
                holder.txtname=(TextView)row.findViewById(R.id.textViewNAME);
                holder.txttime=(TextView)row.findViewById(R.id.textViewtime);
                row.setTag(holder);

            } else {
                holder = (ViewHolder) row.getTag();
            }

            final BaseMessage parameter = parameres.get(position);
            if (parameter.getId()!=null)
            holder.txtid.setText(parameter.getId());
            if (parameter.getName()!=null)
            holder.txtname.setText(parameter.getName());
            if (parameter.getTime()!=null)
            holder.txttime.setText(parameter.getTime());
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView txtid = (TextView) v.findViewById(R.id.textID);
                    TextView txtname=(TextView)v.findViewById(R.id.textViewNAME);
                    TextView txttime=(TextView)v.findViewById(R.id.textViewtime);
                    Intent intent = new Intent(context, ChatroomActivity.class);
                    MyApplication.getInstance().setMsgid(txtid.getText().toString());
                    MyApplication.getInstance().setMsgname(txtname.getText().toString());
                    context.startActivity(intent);
                }
            });

            return row;
        }

        static class ViewHolder{
            TextView txtid;
            TextView txtname;
            TextView txttime;

        }

    }