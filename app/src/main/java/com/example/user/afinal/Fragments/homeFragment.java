package com.example.user.afinal.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.afinal.Adapter.Whatapplistadapter;
import com.example.user.afinal.Model.BaseMessage;
import com.example.user.afinal.R;

import java.util.ArrayList;


public class homeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    ListView lstcmsq;
    ArrayList<BaseMessage> parameres;
    private View view;
    ViewHolder holder = null;
    Whatapplistadapter  adapter;


    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        initcallback();
        return view;
    }
    private void init(View v) {
       // lstcmsq = (ListView)v.findViewById(android.R.id.list);
        lstcmsq = (ListView)v.findViewById(R.id.whatapplistview);
    }
    private void initcallback() {
        try {
         {
                parameres = new ArrayList<BaseMessage>();
                parameres.add(new BaseMessage("1","arun","01-01-2018"));
                parameres.add(new BaseMessage("2","kumar","01-01-2018"));
                parameres.add(new BaseMessage("3","kumar","01-01-2018"));
                parameres.add(new BaseMessage("4","kumar","01-01-2018"));
                parameres.add(new BaseMessage("5","kumar","01-01-2018"));
                parameres.add(new BaseMessage("6","kumar","01-01-2018"));
                parameres.add(new BaseMessage("7","kumar","01-01-2018"));
                parameres.add(new BaseMessage("8","kumar","01-01-2018"));
                parameres.add(new BaseMessage("9","kumar","01-01-2018"));
                parameres.add(new BaseMessage("10","kumar","01-01-2018"));
                parameres.add(new BaseMessage("11","kumar","01-01-2018"));
                adapter= new Whatapplistadapter(getContext(),R.layout.whatapplistrow,parameres);
                lstcmsq.setAdapter(adapter);
               // setListAdapter(new bsAdapter(getActivity()));
            }
        }catch (Exception ex){
            Log.e("home initcallback",ex.getMessage().toString());
        }
    }

    public class bsAdapter extends BaseAdapter
    {

        //if u want to use this plz change classextend   fragment to listfragment
        Activity cntx;
        public bsAdapter(Activity context)
        {
            // TODO Auto-generated constructor stub
            this.cntx=context;

        }

        public int getCount()
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
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View row=convertView;
            if (row == null) {
                LayoutInflater inflater=cntx.getLayoutInflater();
                row=inflater.inflate(R.layout.whatapplistrow, null);
                holder = new ViewHolder();
                holder.txtid = (TextView) row.findViewById(R.id.textID);
                holder.txtname=(TextView)row.findViewById(R.id.textViewNAME);
                holder.txttime=(TextView)row.findViewById(R.id.textViewtime);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            final BaseMessage parameter = parameres.get(position);
            holder.txtid.setText(parameter.getId());
            holder.txtname.setText(parameter.getName());
            holder.txttime.setText(parameter.getTime());

            return row;
        }

    }
    static class ViewHolder {
        TextView txtid;
        TextView txtname;
        TextView txttime;


    }


}
