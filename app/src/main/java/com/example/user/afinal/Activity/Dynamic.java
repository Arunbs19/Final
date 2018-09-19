package com.example.user.afinal.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.user.afinal.Model.DisplayorderClass;
import com.example.user.afinal.Model.DropDownData;
import com.example.user.afinal.Model.Surveyformclass;
import com.example.user.afinal.R;
import com.example.user.afinal.Utility.MyApplication;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Dynamic extends AppCompatActivity {

        ViewFlipper viewflipper;
        Button imgnext;
        Button imgprev;
        ArrayList<DropDownData> Dropdownlist;
        ProgressDialog pDialog;
        ArrayList<String> startaudio;
        ArrayList<String> stopaudio;
        JSONArray quejarray;
        JSONArray formarray;
       // JSONArray gridformarray;
        String description;
        Button btnsubmit;
        JSONObject formobj;
        JSONObject questionobj;
        JSONArray questionarray;
       // String retailerid;
        ImageView mapimgsrc;
        String strimagename = "";
        String strimagepath = "";
        File captureimgfile;
        int resID = 0;
        int uniqueid = 20;
        int radioid = 12000;
        int quessid = 0;
        int isfiltergrid = 0;
        private Calendar cal;
        private int day;
        private int month;
        private int year;
        String locformid = "";
        ArrayList<EditText> listedittext;
        private int TAKE_PHOTO_CODE = 0;
        private Bitmap photo = null;
        ImageView imgsource;
        TextView txtvisitdetails;
        int camerabar = 0;
        public TextView maptextview;
        public static boolean[] checkSelected;
        private boolean expanded;
        private PopupWindow pw;
        JSONArray formsjarray = null;
        int gridformcontrolheight = 0;
        int gridformemptyheight = 0;
        String[] surveyformtitle;
        String[] surveyformids;
        private Uri fileUri;
        String nextformid = "";
        ArrayList<Integer> sessiondisplay;
        int ids = 0;
        String ipaddress;
        String strdependids = "";
        int pagecount = 0;
        int titlecount = 0;
        int tablerowid = 5000;
        String absfilepath = "";
        String btn1code = "", btn2code = "";
        private long totalSize = 0;
        private final int MY_PERMISSIONS_RECORD_AUDIO = 147;
        boolean doubleBackToExitPressedOnce = false;
        private static final int CAMERA_REQUEST = 1888;
        ArrayList<String> imagefiles;
        ArrayList<DisplayorderClass> displayclass;
        ArrayList<Surveyformclass> surveyformclass;

        private static final int REQUEST_EXTERNAL_STORAGE = 1;
        private static String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        private static final String IMAGE_DIRECTORY_NAME = "vizibee";
        /**
         * ATTENTION: This was auto-generated to implement the App Indexing API.
         * See https://g.co/AppIndexing/AndroidStudio for more information.
         */
        JSONArray formsarray;
        private GoogleApiClient client;
        //UserSessionManager session;


        int PERMISSION_REQUEST_COARSE_LOCATION = 100;
        private SQLiteDatabase mDb;
        //AudioRecorder
        private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
        private MediaRecorder myRecorder;
        private MediaPlayer myPlayer;
        private String outputFile = null;
        final Boolean click = true;
        String devicedate;
        String devicedate1;
        String Userid;
        String donetype;
        JSONArray formrule_array;
        int marginleftpx = 0;
        private static int marginleft = 0;
        int marginrightpx = 0;
        private static int marginright = 0;
        ImageView imghome;
        int controlmarginleftpx = 0;
        private static int controlmarginleft = 0;
        int controlmarginrightpx = 0;
        private static int controlmarginright = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        devicedate = sdf.format(c.getTime());

        try {
           // JSONObject jobj = new JSONObject(MyApplication.getInstance().getJsontestobject());
            JSONObject jobj =  MyApplication.getInstance().getJsontestobject();
            JSONObject jsonobj = jobj.getJSONObject("logged_user");

            Userid = jsonobj.getString("logged_user_id");
            formsjarray = new JSONArray(jobj.getString("forms"));
        }catch (Exception ex)
        {}

        init();
        initcallback();
    }
    public void init(){
      try {
          btnsubmit = (Button)findViewById(R.id.btnsubmit);
          btnsubmit.setVisibility(View.INVISIBLE);
          imghome = (ImageView)findViewById(R.id.imghome);
          txtvisitdetails = (TextView) findViewById(R.id.txt_visitdetails);
          //formarray = MyApplication.getInstance().getFinalobj();
          displayclass = new ArrayList<DisplayorderClass>();
          surveyformclass = new ArrayList<Surveyformclass>();
          sessiondisplay = new ArrayList<Integer>();
          viewflipper = (ViewFlipper) findViewById(R.id.view_flipper);
          imgnext = (Button) findViewById(R.id.btnnext);
          imgprev = (Button) findViewById(R.id.btnprevious);
          JSONObject jobj = MyApplication.getInstance().getJsontestobject();
         // retailerid = MyApplication.getInstance().getRetailerid();
          formsjarray = new JSONArray(jobj.getString("forms"));
          surveyformtitle = new String[formsjarray.length()];
          surveyformids = new String[formsjarray.length()];
          sessiondisplay.add(0);

          marginleftpx =  getResources().getDimensionPixelSize(R.dimen.default_size_5dp);
          marginleft = (int)(marginleftpx / getResources().getDisplayMetrics().density);
          marginrightpx =  getResources().getDimensionPixelSize(R.dimen.default_size_5dp);
          marginright = (int)(marginrightpx / getResources().getDisplayMetrics().density);


          controlmarginleftpx =  getResources().getDimensionPixelSize(R.dimen.default_size_5dp);
          controlmarginleft = (int)(controlmarginleftpx / getResources().getDisplayMetrics().density);
          controlmarginrightpx =  getResources().getDimensionPixelSize(R.dimen.default_size_5dp);
          controlmarginright = (int)(controlmarginrightpx / getResources().getDisplayMetrics().density);

          startaudio =new ArrayList<String>();
          stopaudio =new ArrayList<String>();
          try {
              for (int i = 0; i < formsjarray.length(); i++) {
                  {
                      if (!formsjarray.getJSONObject(i).getString("type").toString().equalsIgnoreCase("DEFAULT") && !formsjarray.getJSONObject(i).getString("type").toString().equalsIgnoreCase("SUBMIT") && !formsjarray.getJSONObject(i).getString("type").toString().equalsIgnoreCase("GRID")) {
                          surveyformclass.add(new Surveyformclass(formsjarray.getJSONObject(i).getString("form_id").toString(), formsjarray.getJSONObject(i).getString("title").toString(), formsjarray.getJSONObject(i), formsjarray.getJSONObject(i).getString("type").toString(), new JSONArray(formsjarray.getJSONObject(i).has("form_rules") ? formsjarray.getJSONObject(i).getString("form_rules").toString() : "[]")));
                      } else if (formsjarray.getJSONObject(i).getString("type").toString().equalsIgnoreCase("SUBMIT")) {
                          surveyformclass.add(new Surveyformclass(formsjarray.getJSONObject(i).getString("form_id").toString(), formsjarray.getJSONObject(i).getString("title").toString(), formsjarray.getJSONObject(i), formsjarray.getJSONObject(i).getString("type").toString(), new JSONArray(formsjarray.getJSONObject(i).has("form_rules") ? formsjarray.getJSONObject(i).getString("form_rules").toString() : "[]")));
                      } else if (formsjarray.getJSONObject(i).getString("type").toString().equalsIgnoreCase("GRID")) {

                          surveyformclass.add(new Surveyformclass(formsjarray.getJSONObject(i).getString("form_id").toString(), formsjarray.getJSONObject(i).getString("title").toString(), formsjarray.getJSONObject(i), formsjarray.getJSONObject(i).getString("type").toString(), new JSONArray(formsjarray.getJSONObject(i).has("form_rules") ? formsjarray.getJSONObject(i).getString("form_rules").toString() : "[]")));
                      }
                  }
              }
          }catch (Exception e){
              Log.e("inside init",e.getMessage().toString());
          }
          pagesetup(surveyformclass.get(3).getJsonobj());

      }catch (Exception e){
          Log.e("dynamic init",e.getMessage().toString());
      }
    }
    public void initcallback(){
        try {

            imgnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //   pagesetup(surveyformclass.get(3).getJsonobj());
                 //   viewflipper.showNext();
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Dynamic.this,R.style.MyAlertDialogStyle);
                    // Setting Dialog Title
                    alertDialog.setTitle("GPS is settings");
                    // Setting Dialog Message
                    alertDialog.setMessage(Html.fromHtml("<font color='#ffffff'>GPS is not enabled. Do you want to go to settings menu?</font>"));
                    // On pressing Settings button
                    alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
                    // on pressing cancel button
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                }
            });

        }catch (Exception e){
            Log.e("dynamic initcallback",e.getMessage().toString());
        }
    }
    public void pagesetup(JSONObject jobj) {
        locformid = "";
        String quesid = "";
        try{
            formobj = new JSONObject();
            int subqueid= 1000;
            listedittext = new ArrayList<EditText>();
            locformid = jobj.getString("form_id").toString();
            txtvisitdetails.setText(jobj.getString("title").toString());
            displayclass.add(new DisplayorderClass(jobj.getString("form_id").toString(),jobj.getString("title").toString(),jobj,jobj.getString("type").toString(),new JSONArray(jobj.has("form_rules")?jobj.getString("form_rules").toString():"[]")));
            if (!jobj.getString("type").toString().equalsIgnoreCase("DEFAULT") && !jobj.getString("type").toString().equalsIgnoreCase("GRID"))
            {
                formobj.put("form_id", jobj.getString("form_id").toString());
                description = jobj.getString("description").toString();
                quejarray = new JSONArray(jobj.getString("questions").toString());
                formrule_array = new JSONArray(jobj.has("form_rules")?jobj.getString("form_rules").toString():"[]");
                ScrollView scroll = new ScrollView(this);
                ScrollView.LayoutParams scrollparams = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                scrollparams.setMargins(marginleft, 0, marginright, 0);
                scroll.setLayoutParams(scrollparams);
                scroll.setFocusableInTouchMode(true);
                scroll.setFocusable(true);
                scroll.setVerticalScrollBarEnabled(false);
                scroll.setHorizontalScrollBarEnabled(false);

                LinearLayout li = new LinearLayout(this);
                LinearLayout.LayoutParams liparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                li.setLayoutParams(liparams);
                li.setFocusableInTouchMode(true);
                li.setFocusable(true);
                li.setTag(jobj.getString("form_id").toString());
                li.setOrientation(LinearLayout.VERTICAL);
                li.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                li.requestFocus(View.FOCUS_FORWARD);
                TableLayout tb = new TableLayout(this);
                TableRow row = new TableRow(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int marginbottompx =  getResources().getDimensionPixelSize(R.dimen.title_marginbottom);
                int marginbottom = (int)(marginbottompx / getResources().getDisplayMetrics().density);

                //1rd
                LayoutInflater headinflater = Dynamic.this.getLayoutInflater();
                View view = headinflater.inflate(R.layout.title, null, false);
                view.setLayoutParams(params);
                final TextView txtheader = (TextView) view.findViewById(R.id.txttitle);
                final TextView txtmandatory = (TextView) view.findViewById(R.id.txtmandatory);

                //2nd
                TableRow.LayoutParams params2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LayoutInflater headinflater2 = Dynamic.this.getLayoutInflater();
                View view2 = headinflater.inflate(R.layout.title, null, false);
                view2.setLayoutParams(params2);
                final TextView txtheader2 = (TextView) view2.findViewById(R.id.txttitle);
                final TextView txtmandatory2 = (TextView) view2.findViewById(R.id.txtmandatory);
                txtheader2.setText("arun");
                TextView tv = new TextView(this);
                tv.setText("third element");
                row.addView(view);
                row.addView(view2);
                row.addView(tv);
                tb.addView(row);
               // li.addView(row);
                li.addView(tb);


                //2nd row

                TableRow row2 = new TableRow(this);
                TextView tv2 = new TextView(this);
                tv2.setText("third element");
                row2.addView(tv2);
                li.addView(row2);

                //3rd row
                TableRow row3 = new TableRow(this);
                GridView gv = new GridView(this);
                gv.setNumColumns(2);
                gv.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                gv.setGravity(Gravity.START);
                gv.setScrollingCacheEnabled(false);
                gv.setPadding(0,0,0,0);
                ArrayList<String> list = new ArrayList<String>();
                list.add("one");
                list.add("two");
                list.add("threee");
                list.add("four");
                list.add("five");
                list.add("six");
                list.add("seven");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
                gv.setAdapter(adapter);
                setListViewHeightBasedOnItems(row3, adapter);
                row3.addView(gv);
                li.addView(row3);

                //4th row
                TableRow row4 = new TableRow(this);
                TextView tv4 = new TextView(this);
                tv4.setText("fourth row  element");
                tv4.setTextSize(30);
                row4.addView(tv4);
                li.addView(row4);


               // row.addView(view2);
              //  li.addView(row);
               /* questionarray = new JSONArray();
                for (int j = 0; j < quejarray.length(); j++) {
                    uniqueid++;
                    quesid = quejarray.getJSONObject(j).getString("id").toString();
                    questionobj = new JSONObject();
                    questionobj.put("unique_id", uniqueid);
                    questionobj.put("question_id", quejarray.getJSONObject(j).getString("id").toString());
                    questionobj.put("id", quejarray.getJSONObject(j).has("sub_question_id") ? quejarray.getJSONObject(j).getString("sub_question_id").toString() : quejarray.getJSONObject(j).getString("id").toString());
                    questionobj.put("question_type", quejarray.getJSONObject(j).getString("type").toString());
                    questionobj.put("is_required", quejarray.getJSONObject(j).has("is_required") ? quejarray.getJSONObject(j).getString("is_required").toString() : "0");
                    questionobj.put("dummy_is_required", quejarray.getJSONObject(j).has("is_required") ? quejarray.getJSONObject(j).getString("is_required").toString() : "0");
                    questionobj.put("answer_id", "");
                    questionobj.put("answer", "");
                    questionobj.put("form_id", "");
                    questionobj.put("is_other", "");
                    questionobj.put("visual", "1");
                    questionobj.put("validationtype", "");
                    questionobj.put("validationvalue", "");
                    questionobj.put("parent", "1");
                    questionobj.put("subquestion", quejarray.getJSONObject(j).has("is_sub_question") ? quejarray.getJSONObject(j).getString("is_sub_question").toString() : "0");
                    String subquestionobjid = "";
                    if (quejarray.getJSONObject(j).has("is_sub_question"))
                    {
                        if (quejarray.getJSONObject(j).getString("is_sub_question").toString().equalsIgnoreCase("1")) {
                            JSONArray jarray = new JSONArray(quejarray.getJSONObject(j).getString("values").toString());
                            for (int s = 0; s < jarray.length(); s++) {
                                JSONArray subquearray = new JSONArray(jarray.getJSONObject(s).getString("sub_questions").toString());
                                for (int t = 0; t < subquearray.length(); t++) {
                                    subquestionobjid += subquearray.getJSONObject(t).getString("sub_question_id").toString() + ",";
                                }
                            }
                        }
                    }
                    questionobj.put("subquestionid", subquestionobjid.length()>0?subquestionobjid.substring(0,subquestionobjid.length()-1):subquestionobjid);
                    questionarray.put(questionobj);
                    controls("empty", li, "", "", uniqueid, "", "", "", "", "", "", "", "","",0,"0",uniqueid,0,0,"","","",0,"0","#FFFFFF",0,0,0,"","",1,0);
                    controls("empty", li, "", "", uniqueid, "", "", "", "", "", "", "", "","",0,"0",uniqueid,0,0,"","","",0,"0","#FFFFFF",0,0,0,"","",1,0);
                    if(quejarray.getJSONObject(j).getString("type").toString().equalsIgnoreCase("qmyn")){
                    }else {
                        int iswhitebg = 0;
                        if(quejarray.getJSONObject(j).getString("type").toString().equalsIgnoreCase("qmpi")||quejarray.getJSONObject(j).getString("type").toString().equalsIgnoreCase("qmar")||quejarray.getJSONObject(j).getString("type").toString().equalsIgnoreCase("qmls")||quejarray.getJSONObject(j).getString("type").toString().equalsIgnoreCase("qmbc")||quejarray.getJSONObject(j).getString("type").toString().equalsIgnoreCase("qmgt")||quejarray.getJSONObject(j).getString("type").toString().equalsIgnoreCase("qmir"))
                        {
                            controls("divideline", li, "", "", uniqueid, "", "", "", "", "", "", "", "", "", 0, "0", uniqueid, 0, 1, "", "", "", 0, "0", "#FFFFFF", R.drawable.whiterow_border_leftright, 0,0,"","",1,0);
                            iswhitebg = R.drawable.whiterow_border_leftright;
                        }
                        controls("title", li, quejarray.getJSONObject(j).getString("title").toString(), "", uniqueid, "", quejarray.getJSONObject(j).has("is_required")?quejarray.getJSONObject(j).getString("is_required").toString():"0", "", "", "", "", "", "","",uniqueid,"0",uniqueid,Integer.parseInt(quejarray.getJSONObject(j).getString("id").toString()),0,"","","",0,"0","#FFFFFF",iswhitebg,0,0,"","",1,0);
                       // controls(quejarray.getJSONObject(j).getString("type").toString(), li, "", quejarray.getJSONObject(j).has("values") ? quejarray.getJSONObject(j).getString("values").toString() : "", uniqueid, quejarray.getJSONObject(j).has("target") ? quejarray.getJSONObject(j).getString("target").toString() : "", quejarray.getJSONObject(j).getString("id").toString(), quejarray.getJSONObject(j).has("validation") ? quejarray.getJSONObject(j).getString("validation").toString() : "", quejarray.getJSONObject(j).has("from_lable") ? quejarray.getJSONObject(j).getString("from_lable").toString() : "", quejarray.getJSONObject(j).has("to_lable") ? quejarray.getJSONObject(j).getString("to_lable").toString() : "", quejarray.getJSONObject(j).has("from_to_number") ? quejarray.getJSONObject(j).getString("from_to_number").toString() : "", quejarray.getJSONObject(j).has("row_values") ? quejarray.getJSONObject(j).getString("row_values").toString() : "", quejarray.getJSONObject(j).has("column_values") ? quejarray.getJSONObject(j).getString("column_values").toString() : "",quejarray.getJSONObject(j).has("is_required")?quejarray.getJSONObject(j).getString("is_required").toString():"0",uniqueid,"0",uniqueid,Integer.parseInt(quejarray.getJSONObject(j).getString("id").toString()),0,quejarray.getJSONObject(j).has("image_path")?quejarray.getJSONObject(j).getString("image_path"):"",quejarray.getJSONObject(j).has("is_other")?quejarray.getJSONObject(j).getString("is_other").toString():"",quejarray.getJSONObject(j).has("sub_question_id")?quejarray.getJSONObject(j).getString("sub_question_id").toString():quejarray.getJSONObject(j).getString("id").toString(),0,quejarray.getJSONObject(j).has("decimal")?quejarray.getJSONObject(j).getString("decimal").toString():"0","#FFFFFF",iswhitebg,0,0,"","",1,0);

                    }
                }
                formobj.put("questions", questionarray);
                formarray.put(formobj);
                controls("empty", li, "", "", 0, "", "", "", "", "", "", "", "","",0,"0",uniqueid,0,0,"","","",0,"0","#FFFFFF",0,0,0,"","",1,0);
                controls("empty", li, "", "", 0, "", "", "", "", "", "", "", "","",0,"0",uniqueid,0,0,"","","",0,"0","#FFFFFF",0,0,0,"","",1,0);
                */
                scroll.addView(li);
                viewflipper.addView(scroll);
                int qmtrcount = 0;
            }

        }catch (Exception ex){
            Log.e("pagesetup",ex.getMessage().toString());
        }

    }
    static boolean setListViewHeightBasedOnItems(TableRow row, ArrayAdapter adapter) {
        if (adapter != null) {
            int numberOfItems = adapter.getCount();
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = adapter.getView(itemPos, null, row);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight()+20;
            }
            // Get total height of all item dividers.
           int totalDividersHeight = row.getMeasuredHeight()*(numberOfItems);
            //int totalDividersHeight = 30;
            // Set list height.
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, totalItemsHeight + totalDividersHeight);
            row.setLayoutParams(params);
            row.setVerticalScrollBarEnabled(false);
            row.setPadding(0,5,0,5);
            row.requestLayout();
            return true;
        } else {
            return false;
        }
    }
    public void controls(final String type, final LinearLayout tl, String title, String values, final int id, final String targetid, final String questionid, String validation, String fromlabel, String tolabel, String fromtonumber, String rowvalues, String columnvalues, String isrequired, final int childid, final String subquestion, int subqueuniqueid, int quesid, int grid,String imagepath,String isother,String subquestionid,int isgrid,final String decimallength,String bgcolour,int leftrightbg,int issinglegrid,int gridfilter,String filter1,String filter2,int rowtype,int item_id)
    {
        try{
            tablerowid++;
            TableRow row = new TableRow(this);
            row.setBackgroundColor(Color.parseColor(bgcolour));
            row.setId(tablerowid);
            if(leftrightbg != 0) {
                row.setBackground(getResources().getDrawable(leftrightbg));
            }
            Dropdownlist = new ArrayList<DropDownData>();
            if (type.equalsIgnoreCase("empty")) {
                int headerHeight = (int) getResources().getDimension(R.dimen.default_size_10dp);
                row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, headerHeight));
                TextView txtheader = new TextView(this);
                row.addView(txtheader);

            }
            else if (type.equalsIgnoreCase("title")) {
                int height = (int) getResources().getDimension(R.dimen.title_height);
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int margintoptpx =  getResources().getDimensionPixelSize(R.dimen.title_margintop);
                int margintop = (int)(margintoptpx / getResources().getDisplayMetrics().density);
                int marginbottompx =  getResources().getDimensionPixelSize(R.dimen.title_marginbottom);
                int marginbottom = (int)(marginbottompx / getResources().getDisplayMetrics().density);
                params.setMargins(controlmarginleft, margintop, controlmarginright, marginbottom);
                LayoutInflater headinflater = Dynamic.this.getLayoutInflater();
                View view = headinflater.inflate(R.layout.title, null, false);
                view.setLayoutParams(params);
                final TextView txtheader = (TextView) view.findViewById(R.id.txttitle);
                final TextView txtmandatory = (TextView) view.findViewById(R.id.txtmandatory);
                int paddingleft = (int) getResources().getDimension(R.dimen.title_paddingleft);
                txtheader.setText(title);
                txtmandatory.setPadding(paddingleft, 0, 0, 0);
                if(isgrid == 0){
                    if (questionid.equalsIgnoreCase("1")) {
                        txtmandatory.setVisibility(View.VISIBLE);
                    }else
                        txtmandatory.setVisibility(View.GONE);
                }
                else
                {
                    txtmandatory.setVisibility(View.GONE);
                }

                row.addView(view);


            }

            TableRow.LayoutParams params = new TableRow.LayoutParams(1, 1);
            LayoutInflater filterheadinflater = Dynamic.this.getLayoutInflater();
            View views = filterheadinflater.inflate(R.layout.rowtype, null, false);
            views.setLayoutParams(params);
            final TextView txtrowtype = (TextView) views.findViewById(R.id.txttablerowtype);
            txtrowtype.setText(String.valueOf(rowtype));
            txtrowtype.setTag(String.valueOf(item_id));
            row.addView(views);
            tl.addView(row);

        }catch (Exception e)
        {
            Log.e("Control ex",e.getMessage().toString());
        }
    }
    }
