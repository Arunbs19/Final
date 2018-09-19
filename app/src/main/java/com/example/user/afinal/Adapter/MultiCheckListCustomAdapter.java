package com.example.user.afinal.Adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.afinal.Model.ListClass;
import com.example.user.afinal.R;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MultiCheckListCustomAdapter extends ArrayAdapter<ListClass> {
    Context context;
    ViewHolder holder = null;
    int layoutResourceId;
    String srt;
    static SimpleListAdapter adapter = null;
    String id;
    String colour;
    AlertDialog alert;
    int count=0;
    JSONArray valuesarray;
    ArrayList<ListClass> offlist = new ArrayList<ListClass>();
   // JSONArray formarray = MyApplication.getInstance().getFinalobj();
    JSONArray ansarray = new JSONArray();

    public MultiCheckListCustomAdapter(Context context, int layoutResourceId,
                                       ArrayList<ListClass> data, String colour) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.offlist = data;
        this.colour = colour;
        valuesarray = new JSONArray();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.txtquestion = (TextView) row.findViewById(R.id.txtquestion);
            holder.txtquestionid = (TextView) row.findViewById(R.id.txtquestionid);
            holder.checkboxgroup=(LinearLayout) row.findViewById(R.id.cbgroup);

            final ListClass parameter = offlist.get(position);
            holder.txtquestion.setText(parameter.getQuestion());
            holder.txtquestionid.setText(parameter.getQuestionid());
            holder.checkboxgroup.setTag(Integer.parseInt(parameter.getUniqueid()));

            try {
                JSONObject jobj = new JSONObject();
                jobj.put("row_id", parameter.getQuestionid());
                jobj.put("answer_id", "");
                jobj.put("answer","");
                valuesarray.put(jobj);

            }catch(Exception ex){
                Log.e("Json Exception",ex.getMessage().toString());
            }


            for(int i = 0;i<parameter.getQuestionlist().size();i++) {
                CheckBox cb = new CheckBox(context);
                cb.setTag(parameter.getQuestionlist().get(i).getQuestionid());
                cb.setText(parameter.getQuestionlist().get(i).getQuestion());
                cb.setId(Integer.parseInt(parameter.getQuestionlist().get(i).getQuestionid()));
                cb.setPadding(12,2,2,10);
                int px = context.getResources().getDimensionPixelSize(R.dimen.default_size_15dp);
                float size = px / context.getResources().getDisplayMetrics().density;
                cb.setTextSize(size);
                TypedValue outxValue = new TypedValue();
                context.getResources().getValue(R.dimen.default_size_1dp, outxValue, true);
                float xvalue = outxValue.getFloat();
                TypedValue outyValue = new TypedValue();
                context.getResources().getValue(R.dimen.default_size_1dp, outyValue, true);
                float yvalue = outyValue.getFloat();
                /*cb.setScaleX(xvalue);
                cb.setScaleY(yvalue);*/
                cb.setButtonDrawable(R.drawable.checkbtn_icon);
                cb.setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        LinearLayout li = (LinearLayout) buttonView.getParent();
                        LinearLayout liparent = (LinearLayout) li.getParent();
                        TextView txtquestionid = (TextView) liparent.findViewById(R.id.txtquestionid);
                       /* if(buttonView.isChecked()){
                        // TODO Auto-generated method stub

                        try {

                            for (int k = 0; k < formarray.length(); k++) {
                                JSONArray questionsarray = new JSONArray(formarray.getJSONObject(k).getString("questions"));
                                for (int l = 0; l < questionsarray.length(); l++) {
                                    if (questionsarray.getJSONObject(l).getString("unique_id").toString().equalsIgnoreCase(li.getTag().toString())) {
                                        JSONArray valuesarray = new JSONArray(questionsarray.getJSONObject(l).getString("answer").toString());
                                        for (int m = 0; m < valuesarray.length(); m++) {
                                            if (valuesarray.getJSONObject(m).getString("row_id").toString().equalsIgnoreCase(txtquestionid.getText().toString())) {
                                                if(valuesarray.getJSONObject(m).getString("answer").toString().equalsIgnoreCase(""))
                                                    ansarray = new JSONArray("[]");
                                                else
                                                    ansarray = new JSONArray(valuesarray.getJSONObject(m).getString("answer").toString());

                                                JSONObject jobj = new JSONObject();
                                                jobj.put("answer_id", buttonView.getTag().toString());
                                                jobj.put("answer", buttonView.getText().toString());
                                                ansarray.put(jobj);

                                                valuesarray.getJSONObject(m).put("answer_id", "");
                                                valuesarray.getJSONObject(m).put("answer", ansarray);
                                            }
                                        }
                                        questionsarray.getJSONObject(l).put("answer", valuesarray);
                                    }
                                }
                                formarray.getJSONObject(k).put("questions", questionsarray);
                            }
                        } catch (Exception ex) {
                            Log.e("Json exception", ex.getMessage().toString());
                        }
                    }
                        else {
                            try {

                                for (int k = 0; k < formarray.length(); k++) {
                                    JSONArray questionsarray = new JSONArray(formarray.getJSONObject(k).getString("questions"));
                                    for (int l = 0; l < questionsarray.length(); l++) {
                                        if (questionsarray.getJSONObject(l).getString("unique_id").toString().equalsIgnoreCase(li.getTag().toString())) {
                                            JSONArray valuesarray = new JSONArray(questionsarray.getJSONObject(l).getString("answer").toString());
                                            for (int m = 0; m < valuesarray.length(); m++) {
                                                if (valuesarray.getJSONObject(m).getString("row_id").toString().equalsIgnoreCase(txtquestionid.getText().toString())) {
                                                    if(valuesarray.getJSONObject(m).getString("answer").toString().equalsIgnoreCase(""))
                                                        ansarray = new JSONArray("[]");
                                                    else
                                                        ansarray = new JSONArray(valuesarray.getJSONObject(m).getString("answer").toString());

                                                    for (int i = 0; i < ansarray.length(); i++) {
                                                        if (ansarray.getJSONObject(i).getString("answer_id").equalsIgnoreCase(buttonView.getTag().toString()))
                                                            ansarray.remove(i);
                                                    }
                                                    valuesarray.getJSONObject(m).put("answer_id", "");
                                                    valuesarray.getJSONObject(m).put("answer", ansarray);
                                                }
                                            }
                                            questionsarray.getJSONObject(l).put("answer", valuesarray);
                                        }
                                    }
                                    formarray.getJSONObject(k).put("questions", questionsarray);
                                }
                            }catch (Exception ex)
                            {
                                Log.e("Json Exception",ex.getMessage().toString());
                            }
                        }*/
                    }
                });
                holder.checkboxgroup.addView(cb);

            }
            for(int i=offlist.size();i<valuesarray.length();i++)
            {
                valuesarray.remove(i);
            }
           /* try {
                for (int k = 0; k < formarray.length(); k++) {
                    JSONArray questionsarray = new JSONArray(formarray.getJSONObject(k).getString("questions"));
                    for (int l = 0; l < questionsarray.length(); l++) {
                        if (questionsarray.getJSONObject(l).getString("unique_id").toString().equalsIgnoreCase(holder.checkboxgroup.getTag().toString())) {
                            questionsarray.getJSONObject(l).put("answer_id", "");
                            questionsarray.getJSONObject(l).put("answer", valuesarray);
                        }
                    }
                    formarray.getJSONObject(k).put("questions",questionsarray);
                }
            }catch(Exception ex){
                Log.e("Json exception",ex.getMessage().toString());
            }*/
            setRadioViewHeightBasedOnItems(holder.checkboxgroup);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }




        return row;

    }



    static class ViewHolder {
        TextView txtquestion;
        TextView txtquestionid;
        LinearLayout checkboxgroup;
    }

     static boolean setRadioViewHeightBasedOnItems(LinearLayout radiogroup) {


        if (adapter != null) {

            int numberOfItems = adapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = adapter.getView(itemPos, null, radiogroup);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = radiogroup.getMeasuredHeight()*
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = radiogroup.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            radiogroup.setLayoutParams(params);
            radiogroup.requestLayout();

            return true;

        } else {
            return false;
        }

    }


}