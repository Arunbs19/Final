package com.example.user.afinal.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.afinal.R;
import com.example.user.afinal.Utility.OnSwipeTouchListener;

import java.util.ArrayList;


public class importantFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View view;
    TextView txt1;
    RelativeLayout  rtlf3;
    Spinner spinner1;
    ArrayAdapter<String> sp1adapter;

    private String sp1list[] = { "select", "0", "1", "2"/* , "3", "4" */};





    public importantFragment() {
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

        view = inflater.inflate(R.layout.fragment_important, container, false);
        rtlf3 =(RelativeLayout)view.findViewById(R.id.rtlframe3);
        spinner1 =(Spinner)view.findViewById(R.id.spinner1);
        init();
        rtlf3.setOnTouchListener(new OnSwipeTouchListener() {
            public boolean onSwipeTop() {
                Toast.makeText(getActivity(), "top", Toast.LENGTH_SHORT).show();
                return true;
            }
            public boolean onSwipeRight() {
                Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
                return true;
            }
            public boolean onSwipeLeft() {
                Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();
                return true;
            }
            public boolean onSwipeBottom() {
                Toast.makeText(getActivity(), "bottom", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view;
    }

    private void init() {
        try{

        sp1adapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,sp1list);
        spinner1.setAdapter(sp1adapter);
            spinner1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (parent.getId()){
                        case R.id.spinner1:
                            if (spinner1.getSelectedItemPosition()==2){
                                Toast.makeText(getActivity(),"select",Toast.LENGTH_LONG).show();
                            }
                    }
                }
            });


        }catch (Exception e){
            Log.e("initimportant",e.getMessage().toString());
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner1:
                    if (spinner1.getSelectedItemPosition()==2){
                        Toast.makeText(getActivity(),"select",Toast.LENGTH_LONG).show();
                    }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(),"noselect",Toast.LENGTH_LONG).show();
    }
}
