package com.example.user.afinal.Activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.user.afinal.Adapter.Default_DropdownCustomAdapter;
import com.example.user.afinal.Adapter.MultiCheckListCustomAdapter;
import com.example.user.afinal.Model.ListClass;
import com.example.user.afinal.Model.SimpleListClass;
import com.example.user.afinal.Model.Surveyformclass;
import com.example.user.afinal.R;
import com.example.user.afinal.Utility.MyApplication;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScannerActivity extends AppCompatActivity {
    private Button buttonScan;
    private TextView textViewName, textViewAddress;
  //  private IntentIntegrator qrScan;
    int SCANNER_REQUEST_CODE = 1000;
    LinearLayout modifylayout;
    int marginleftpx =  0;
    int marginleft = 0;
    int marginrightpx =  0;
    int marginright = 0;
    EditText Uiedit;
    ArrayList<String> Dropdownlist ;
    JSONArray formsjarray;
    JSONObject formsjobject;
    JSONArray quesarray;
    JSONObject quesoobj;
    int uniqueid = 20;
    String Userid ;
    ArrayList<Surveyformclass> surveyformclass;
    int controlmarginleftpx =  0;
    private static int controlmarginleft = 0;
    int controlmarginrightpx =  0;
    private static int controlmarginright = 0;
    String locformid="";
    ViewFlipper viewflipper;
    Button imgnext;
    Button imgprev;
    int gridformcontrolheight = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        init();
        initcallback();

    }

    private void init() {
        try {

            surveyformclass = new ArrayList<Surveyformclass>();
            buttonScan = (Button) findViewById(R.id.buttonScan);
            textViewName = (TextView) findViewById(R.id.textViewName);
            textViewAddress = (TextView) findViewById(R.id.textViewAddress);
            modifylayout =(LinearLayout)findViewById(R.id.linear11);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            marginleftpx =  getResources().getDimensionPixelSize(R.dimen.default_size_10dp);
            marginleft = (int)(marginleftpx / getResources().getDisplayMetrics().density);
            marginrightpx =  getResources().getDimensionPixelSize(R.dimen.default_size_10dp);
            marginright = (int)(marginrightpx / getResources().getDisplayMetrics().density);

            viewflipper = (ViewFlipper) findViewById(R.id.view_flipper);
            imgnext = (Button) findViewById(R.id.btnnext);
            imgprev = (Button) findViewById(R.id.btnprevious);
            // JSONObject jobj = new JSONObject(MyApplication.getInstance().getJsontestobject());
            JSONObject jobj =  MyApplication.getInstance().getJsontestobject();
            JSONObject jsonobj = jobj.getJSONObject("logged_user");
            Userid = jsonobj.getString("logged_user_id");
            JSONArray formjsonarray = new JSONArray(jobj.getString("forms"));
            formsjarray =new JSONArray(jobj.getString("forms"));
            MyApplication.getInstance().setJsonformarray(formjsonarray);
            for (int i =0;i<formjsonarray.length();i++){
               if (formjsonarray.getJSONObject(i).getString("form_id").equalsIgnoreCase("41")){
                formsjobject = formjsonarray.getJSONObject(i);
                    surveyformclass.add(new Surveyformclass(formsjarray.getJSONObject(i).getString("form_id").toString(), formsjarray.getJSONObject(i).getString("title").toString(), formsjarray.getJSONObject(i), formsjarray.getJSONObject(i).getString("type").toString(), new JSONArray(formsjarray.getJSONObject(i).has("form_rules") ? formsjarray.getJSONObject(i).getString("form_rules").toString() : "[]")));
                    quesarray= formsjobject.getJSONArray("questions");

               }
            }

            formsjarray = new JSONArray(jobj.getString("forms"));

         //  qrScan = new IntentIntegrator(this);
            buttonScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  using Zxing jar*/
                  //  qrScan.initiateScan();

                    //using none
                   /* Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage("com.mypackage.app");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, SCANNER_REQUEST_CODE);*/

                    //using Barcodescanner app

                    /*Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,RSS_14,UPC_A,UPC_E,QR_CODE");
                    startActivityForResult(intent, 0);
*/
                }
            });
        }catch (Exception ex){
            Log.e("scanner init ",ex.getMessage().toString());
        }
    }
    private void initcallback() {
        try {
            pagesetup(surveyformclass.get(0).getJsonobj());
            imgnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pagesetup(surveyformclass.get(0).getJsonobj());
                    viewflipper.showNext();
                }
            });


            //Uicontrol("empty",modifylayout,"","",0,"","");
          /*  Uicontrol("title",modifylayout,"TITLE","",0,"","1");
            Uicontrol("editbox",modifylayout,"","",0,"","");
            Uicontrol("editbox",modifylayout,"","",0,"","");
          //  Uicontrol("dividerline",modifylayout,"","",0,"","");
            Uicontrol("qdropdown",modifylayout,"","",0,"","");
            Uicontrol("qimage",modifylayout,"","",0,"","");*/

        }catch (Exception ex){
            Log.e("scanner initcall",ex.getMessage().toString());
        }
    }
    public void pagesetup(JSONObject jobj){
    locformid = "";
    String quesid = "";
    try {
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
        int iswhitebg = 0;
        for (int j = 0;j<quesarray.length();j++){
         //   if (quesarray.getJSONObject(j).getString("type").equalsIgnoreCase("qmgc")){
            uniqueid++;
            iswhitebg = 0;
               // Uicontrol("qmrb",modifylayout,"",quesarray.getJSONObject(j).getString("values").toString(),0,"","");
            controls("title", li, quesarray.getJSONObject(j).getString("title").toString(), "", uniqueid, "", quesarray.getJSONObject(j).has("is_required")?quesarray.getJSONObject(j).getString("is_required").toString():"0", "", "", "", "", "", "","",uniqueid,"0",uniqueid,Integer.parseInt(quesarray.getJSONObject(j).getString("id").toString()),0,"","","",0,"0","#00FFFFFF",iswhitebg,0,0,"","",1,0);
            //controls("divideline", modifylayout, "", "", uniqueid, "", "", "", "", "", "", "", "", "", 0, "0", uniqueid, 0, 1, "", "", "", 0, "0", "#FFFFFF", R.drawable.whiterow_border_leftright, 0,0,"","",1,0);
               // iswhitebg = R.drawable.whiterow_border_leftright;
            iswhitebg = 0 ;
                controls(quesarray.getJSONObject(j).getString("type").toString(), li, "", quesarray.getJSONObject(j).has("values") ? quesarray.getJSONObject(j).getString("values").toString() : "", uniqueid, quesarray.getJSONObject(j).has("target") ? quesarray.getJSONObject(j).getString("target").toString() : "", quesarray.getJSONObject(j).getString("id").toString(), quesarray.getJSONObject(j).has("validation") ? quesarray.getJSONObject(j).getString("validation").toString() : "", quesarray.getJSONObject(j).has("from_lable") ? quesarray.getJSONObject(j).getString("from_lable").toString() : "", quesarray.getJSONObject(j).has("to_lable") ? quesarray.getJSONObject(j).getString("to_lable").toString() : "", quesarray.getJSONObject(j).has("from_to_number") ? quesarray.getJSONObject(j).getString("from_to_number").toString() : "", quesarray.getJSONObject(j).has("row_values") ? quesarray.getJSONObject(j).getString("row_values").toString() : "", quesarray.getJSONObject(j).has("column_values") ? quesarray.getJSONObject(j).getString("column_values").toString() : "",quesarray.getJSONObject(j).has("is_required")?quesarray.getJSONObject(j).getString("is_required").toString():"0",uniqueid,"0",uniqueid,Integer.parseInt(quesarray.getJSONObject(j).getString("id").toString()),0,quesarray.getJSONObject(j).has("image_path")?quesarray.getJSONObject(j).getString("image_path"):"",quesarray.getJSONObject(j).has("is_other")?quesarray.getJSONObject(j).getString("is_other").toString():"",quesarray.getJSONObject(j).has("sub_question_id")?quesarray.getJSONObject(j).getString("sub_question_id").toString():quesarray.getJSONObject(j).getString("id").toString(),0,quesarray.getJSONObject(j).has("decimal")?quesarray.getJSONObject(j).getString("decimal").toString():"0","#FFFFFF",iswhitebg,0,0,"","",1,0);

       //     }
        }
        scroll.addView(li);
        viewflipper.addView(scroll);
       // modifylayout.addView(scroll);
      //  viewflipper.addView(scroll);

    }catch (Exception ex){
        Log.e("pagesetup exp",ex.getMessage().toString() );
    }
}
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void Uicontrol(String type, LinearLayout tl, String title, String values, final int id, final String targetid, final String questionid) {
        try {
            TableRow row = new TableRow(this);
            int height = (int) getResources().getDimension(R.dimen.default_size_10dp);
            int titheight = (int) getResources().getDimension(R.dimen.default_btn_size_30dp);
            int margintoptpx =  getResources().getDimensionPixelSize(R.dimen.default_size_10dp  );
            int margintop = (int)(margintoptpx / getResources().getDisplayMetrics().density);
            int paddingleft = (int) getResources().getDimension(R.dimen.default_size_5dp);
            int paddingright = (int) getResources().getDimension(R.dimen.default_btn_size_30dp);
            int titpaddingleft = (int) getResources().getDimension(R.dimen.default_btn_size_30dp);
            int edtpaddingleft = (int) getResources().getDimension(R.dimen.default_size_15dp);
            int paddingtop = (int) getResources().getDimension(R.dimen.default_size_5dp);
            int paddingbottom = (int) getResources().getDimension(R.dimen.default_size_5dp);
            int rbpaddingtop = (int) getResources().getDimension(R.dimen.default_size_2dp);
            int rbpaddingleft = (int) getResources().getDimension(R.dimen.default_size_10dp);
            int rbpaddingright = (int) getResources().getDimension(R.dimen.default_size_2dp);
            int rbpaddingbottom = (int) getResources().getDimension(R.dimen.default_size_2dp);
            controlmarginleftpx =  getResources().getDimensionPixelSize(R.dimen.default_size_10dp);
            controlmarginleft = (int)(controlmarginleftpx / getResources().getDisplayMetrics().density);
            controlmarginrightpx =  getResources().getDimensionPixelSize(R.dimen.default_size_10dp);
            controlmarginright = (int)(controlmarginrightpx / getResources().getDisplayMetrics().density);

            Dropdownlist = new ArrayList<String>();

            if(type.equalsIgnoreCase("empty")){
                row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                TextView txtheader = new TextView(this);
                txtheader.setText("empty ui control");
                int px = getResources().getDimensionPixelSize(R.dimen.default_size_1dp);
                float size = px / getResources().getDisplayMetrics().density;
                txtheader.setTextSize(100);
                row.addView(txtheader);


            }
            else if (type.equalsIgnoreCase("title")) {
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,titheight);
                params.setMargins(marginleft, margintop, marginright, 0);
                row.setLayoutParams(params);
                LayoutInflater headinflater = ScannerActivity.this.getLayoutInflater();
                View view = headinflater.inflate(R.layout.title, null, false);
                final TextView txtheader = (TextView) view.findViewById(R.id.txttitle);
                final TextView txtmandatory = (TextView) view.findViewById(R.id.txtmandatory);
                txtheader.setText(title);
                txtmandatory.setPadding(titpaddingleft, 0, 0, 0);
                if (questionid.equalsIgnoreCase("1")) {
                    txtmandatory.setVisibility(View.VISIBLE);
                }else
                    txtmandatory.setVisibility(View.GONE);

                row.addView(view);

            }
            else if(type.equalsIgnoreCase("editbox")){

                android.widget.TableRow.LayoutParams params = new android.widget.TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titheight);
                android.widget.TableRow.LayoutParams qmdtsparams = new android.widget.TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titheight);
                params.setMargins(marginleft, 0, marginright, 0);
                row.setLayoutParams(params);
                row.setGravity(Gravity.CENTER);
                LayoutInflater headinflater = ScannerActivity.this.getLayoutInflater();
                View view = headinflater.inflate(R.layout.uiedittext, null, false);
                Uiedit = (EditText) view.findViewById(R.id.edtui);
                params.setMargins(marginleft, margintop, marginright, 0);
                view.setLayoutParams(qmdtsparams);
                Uiedit.setPadding(edtpaddingleft,0,0,0);
                Uiedit.setSingleLine(true);
                int px = getResources().getDimensionPixelSize(R.dimen.default_size_15dp);
                float size = px / getResources().getDisplayMetrics().density;
                Uiedit.setTextSize(size);
                row.addView(view);
            }
            else if (type.equalsIgnoreCase("dividerline")) {
                row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView txtheader = new TextView(this);
                int dividerlineHeight = (int) getResources().getDimension(R.dimen.default_btn_size_30dp);
                txtheader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerlineHeight));
                txtheader.setBackgroundColor(Color.parseColor("000000"));
                txtheader.setText("arun");
                row.addView(txtheader);
            }
            else if (type.equalsIgnoreCase("qdropdown")) {

                android.widget.TableRow.LayoutParams params = new android.widget.TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titheight);
                android.widget.TableRow.LayoutParams qmdtparams = new android.widget.TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titheight);
                params.setMargins(marginleft, 0, marginright, 0);
                row.setLayoutParams(params);
                row.setGravity(Gravity.CENTER);
                Dropdownlist.add("select");
                Dropdownlist.add("test1");
                final Default_DropdownCustomAdapter adapter = new Default_DropdownCustomAdapter(ScannerActivity.this, Dropdownlist);
                LayoutInflater headinflater = ScannerActivity.this.getLayoutInflater();
                View view = headinflater.inflate(R.layout.spinner, null, false);
                final Spinner spn = (Spinner) view.findViewById(R.id.spinner);
                spn.setPadding(paddingleft,0,0,0);
                view.setLayoutParams(qmdtparams);
                spn.setId(id);
                spn.setAdapter(adapter);
                spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3)
                    {
                        try
                        {
                            if(arg1!= null) {
                                View view = (View) arg1.getParent();
                                if (view != null) {
                                    TextView txtid = (TextView) view.findViewById(R.id.txt_id);
                                    TextView txtname = (TextView) view.findViewById(R.id.txt_name);
                                    TextView txt_queid = (TextView) view.findViewById(R.id.txt_queid);
                                   {
                                        Default_DropdownCustomAdapter adapters = null;
                                        Dropdownlist = new ArrayList<String>();
                                        Dropdownlist.clear();
                                        Dropdownlist.add("select");
                                        adapters = new Default_DropdownCustomAdapter(ScannerActivity.this, Dropdownlist);
                                        //Spinner spnnext = (Spinner) findViewById(resID);
                                       // spnnext.setAdapter(adapters);
                                    }
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.e(" scanner qdropdwon",ex.getMessage().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                });
                row.addView(view);


            }
            else if (type.equalsIgnoreCase("qimage")) {
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(marginleft, 0, marginright, 0);
                row.setGravity(Gravity.CENTER);
               // row.setTag(validation);
                int imgwidth = (int) getResources().getDimension(R.dimen.default_btn_size_90dp);
                int imgheight = (int) getResources().getDimension(R.dimen.default_btn_size_90dp);
                TableRow.LayoutParams imgparams = new TableRow.LayoutParams(imgwidth, imgheight);
                final ImageView img = new ImageView(this);
                img.setLayoutParams(imgparams);
                img.setTag(id);
                img.setId(id);
                img.setBackground(getResources().getDrawable(R.drawable.under_construction));
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        img.requestFocus();
                      //  String imgurl =  ((TableRow)view.getParent()).getTag().toString();
                       if(img.getBackground() != null) {
                            final Dialog ViewDialog = new Dialog(ScannerActivity.this);
                            ViewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            ViewDialog.setContentView(R.layout.alert_image_dialoge);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            ViewDialog.setCanceledOnTouchOutside(false);
                            lp.copyFrom(ViewDialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                            ViewDialog.show();
                            ViewDialog.getWindow().setAttributes(lp);
                            ImageView imgs = (ImageView) ViewDialog.findViewById(R.id.img);
                            imgs.setImageDrawable(img.getBackground());
                            Button close = (Button) ViewDialog.findViewById(R.id.btnclose);
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ViewDialog.dismiss();
                                }
                            });


                            ViewDialog.show();
                       }
                    }
                });

                row.addView(img);
            }
            else if (type.equalsIgnoreCase("qmrb")) {
                int radioid = 2018;
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(controlmarginleft, 0, controlmarginright, 0);
                JSONArray valuearray = new JSONArray(values);
                final RadioGroup radiogroup = new RadioGroup(this);
                radiogroup.setOrientation(RadioGroup.VERTICAL);
                radiogroup.setLayoutParams(params);
                radiogroup.setTag(id);
                radiogroup.setId(id);
                radiogroup.requestFocus();
                for (int i = 0; i < valuearray.length(); i++) {
                    String subids="";
                    RadioButton radiobtn = new RadioButton(this);
                    radiobtn.setText(valuearray.getJSONObject(i).getString("title").toString());
                    radiobtn.setPadding(rbpaddingleft,rbpaddingtop,rbpaddingbottom,rbpaddingright);
                    radiobtn.setId(radioid);
                    TypedValue outxValue = new TypedValue();
                    getResources().getValue(R.dimen.default_size_1dp, outxValue, true);
                    float xvalue = outxValue.getFloat();
                    TypedValue outyValue = new TypedValue();
                    getResources().getValue(R.dimen.default_size_1dp, outyValue, true);
                    float yvalue = outyValue.getFloat();
                    radiobtn.setTag(valuearray.getJSONObject(i).getString("code").toString()+"~"+subids);
                    int px = getResources().getDimensionPixelSize(R.dimen.default_size_15dp);
                    float size = px / getResources().getDisplayMetrics().density;
                    radiobtn.setTextSize(size);
                    radiobtn.setCompoundDrawablePadding(50);
                    radiobtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    radiogroup.addView(radiobtn);
                    radioid++;
                }



                radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup arg0, int selectedId) {
                        selectedId = radiogroup.getCheckedRadioButtonId();
                        RadioButton radiobutton = (RadioButton) findViewById(selectedId);
                        if (radiobutton != null)
                        {
                            String[] valuess = radiobutton.getTag().toString().split("~");
                            String[] ids = valuess.length > 1 ? valuess[1].toString().split(",") : null;
                            String[] dependids;
                            String strdependids = "";
                            for (int i = 0; i < radiogroup.getChildCount(); i++) {
                                View v = radiogroup.getChildAt(i);
                                if (v instanceof RadioButton) {
                                    String[] childid = ((RadioButton) v).getTag().toString().split("~");
                                    if (childid.length > 1)
                                        strdependids += childid[1].substring(0, childid[1].length() - 1) + ",";

                                }
                            }

                            strdependids = strdependids.substring(0, strdependids.length() > 0 ? strdependids.length() - 1 : 0);
                            dependids = strdependids.equalsIgnoreCase("") ? null : strdependids.split(",");
                            if (dependids != null) {
                            }
                            if (ids != null)
                                try {
                                    TableRow vparent = (TableRow) radiogroup.getParent();
                                RelativeLayout rellayout = (RelativeLayout)vparent.findViewById(R.id.rowtypelayout);
                                TextView txtrowtype = (TextView)rellayout.findViewById(R.id.txttablerowtype);
                                String itemid = txtrowtype.getTag().toString();

                                //Page Rule
                                JSONArray questionsarray = null;
                                String questionid = "";
                                int questionposition = 0;




                            }catch(Exception ex)
                            {
                                Log.e("Json Exception",ex.getMessage().toString());
                            }
                        }
                    }



                });
                row.addView(radiogroup);
            }

            tl.addView(row);

        }catch (Exception ex){
            Log.e("UIcontrol scanner",ex.getMessage().toString());
        }
    }
    public void controls(final String type, final LinearLayout tl, String title, String values, final int id, final String targetid, final String questionid, String validation, String fromlabel, String tolabel, String fromtonumber, String rowvalues, String columnvalues, String isrequired, final int childid, final String subquestion, int subqueuniqueid, int quesid, int grid,String imagepath,String isother,String subquestionid,int isgrid,final String decimallength,String bgcolour,int leftrightbg,int issinglegrid,int gridfilter,String filter1,String filter2,int rowtype,int item_id) {
        try {
            TableRow row = new TableRow(this);
            row.setBackgroundColor(Color.parseColor(bgcolour));
           // row.setId(tablerowid);
            if(leftrightbg != 0) {
                row.setBackground(getResources().getDrawable(leftrightbg));
            }
            int height = (int) getResources().getDimension(R.dimen.default_btn_size_30dp);
            int titheight = (int) getResources().getDimension(R.dimen.default_btn_size_30dp);
            int margintoptpx =  getResources().getDimensionPixelSize(R.dimen.default_size_10dp  );
            int margintop = (int)(margintoptpx / getResources().getDisplayMetrics().density);
            int paddingleft = (int) getResources().getDimension(R.dimen.default_size_5dp);
            int paddingright = (int) getResources().getDimension(R.dimen.default_btn_size_30dp);
            int titpaddingleft = (int) getResources().getDimension(R.dimen.default_btn_size_30dp);
            int edtpaddingleft = (int) getResources().getDimension(R.dimen.default_size_15dp);
            int paddingtop = (int) getResources().getDimension(R.dimen.default_size_5dp);
            int paddingbottom = (int) getResources().getDimension(R.dimen.default_size_5dp);
            int rbpaddingtop = (int) getResources().getDimension(R.dimen.default_size_2dp);
            int rbpaddingleft = (int) getResources().getDimension(R.dimen.default_size_10dp);
            int rbpaddingright = (int) getResources().getDimension(R.dimen.default_size_2dp);
            int rbpaddingbottom = (int) getResources().getDimension(R.dimen.default_size_2dp);
            if (type.equalsIgnoreCase("title")) {
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
              /*  int marginbottompx =  getResources().getDimensionPixelSize(R.dimen.title_marginbottom);
                int marginbottom = (int)(marginbottompx / getResources().getDisplayMetrics().density);
                params.setMargins(controlmarginleft, margintop, controlmarginright, marginbottom);*/
                LayoutInflater headinflater = ScannerActivity.this.getLayoutInflater();
                View view = headinflater.inflate(R.layout.title, null, false);
                view.setLayoutParams(params);
                final TextView txtheader = (TextView) view.findViewById(R.id.txttitle);
                final TextView txtmandatory = (TextView) view.findViewById(R.id.txtmandatory);
                txtheader.setText(title);
                txtmandatory.setPadding(paddingleft, 0, 0, 0);
                if(isgrid == 0){
                    if (questionid.equalsIgnoreCase("1")) {
                        txtmandatory.setVisibility(View.VISIBLE);
                    }else
                        txtmandatory.setVisibility(View.GONE);
                }else
                {
                    txtmandatory.setVisibility(View.GONE);
                }

                row.addView(view);
            }
            else if (type.equalsIgnoreCase("qmda")) {

                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height+20);
                TableRow.LayoutParams qmdaparams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                gridformcontrolheight = height;
                params.setMargins(controlmarginleft, 0, controlmarginright, 0);
                row.setBackground(getResources().getDrawable(R.drawable.editable));
                LayoutInflater headinflater = ScannerActivity.this.getLayoutInflater();
                View view = headinflater.inflate(R.layout.datepicker, null, false);
                view.setLayoutParams(params);
                final TextView readonlytext = (TextView) view.findViewById(R.id.txtdatepicker);
            //  TextView readonlytext = new TextView(this);
                readonlytext.setPadding(paddingleft,paddingtop,paddingbottom,paddingright);
                int px = getResources().getDimensionPixelSize(R.dimen.default_size_5dp);
                float size = px / getResources().getDisplayMetrics().density;
                readonlytext.setTextSize(size);
                readonlytext.setTag(id);
                readonlytext.setId(id);
                readonlytext.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                readonlytext.setTextColor(Color.parseColor("#000000"));
                readonlytext.setSingleLine(true);

                final JSONObject validationobj = new JSONObject(validation);
                readonlytext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      /*  InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(readonlytext.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                readonlytext.setText(dayOfMonth+"-"+(monthOfYear + 1) + "-"+ year);
                                try {
                                    for (int k = 0; k < formarray.length(); k++) {
                                        JSONArray questionsarray = new JSONArray(formarray.getJSONObject(k).getString("questions"));
                                        for (int l = 0; l < questionsarray.length(); l++) {
                                            if (questionsarray.getJSONObject(l).getString("unique_id").toString().equalsIgnoreCase(readonlytext.getTag().toString())) {
                                                questionsarray.getJSONObject(l).put("answer_id", readonlytext.getText().toString());
                                                questionsarray.getJSONObject(l).put("answer", readonlytext.getText().toString());
                                                questionsarray.getJSONObject(l).put("is_other", "");
                                            }
                                        }
                                        formarray.getJSONObject(k).put("questions", questionsarray);
                                    }
                                    // TODO: 3/15/2018  arun
                                    TableRow vparent = (TableRow) readonlytext.getParent().getParent();
                                    RelativeLayout rellayout = (RelativeLayout)vparent.findViewById(R.id.rowtypelayout);
                                    TextView txtrowtype = (TextView)rellayout.findViewById(R.id.txttablerowtype);
                                    String itemid = txtrowtype.getTag().toString();
                                    //Page Rule
                                    JSONArray questionsarray = null;
                                    String questionid = "";
                                    int questionposition = 0;
                                    for (int k = 0; k < formarray.length(); k++) {
                                        if(formarray.getJSONObject(k).getString("form_id").toString().equalsIgnoreCase(locformid)) {
                                            questionsarray = new JSONArray(formarray.getJSONObject(k).getString("questions"));
                                            questionposition = k;
                                        }
                                    }

                                    for(int l=0;l<questionsarray.length();l++)
                                    {
                                        if(questionsarray.getJSONObject(l).getString("unique_id").toString().equalsIgnoreCase(readonlytext.getTag().toString()))
                                            questionid = questionsarray.getJSONObject(l).getString("id").toString();
                                    }

                                    if(formrule_array.length()>0)
                                    {
                                        for(int p=0;p<formrule_array.length();p++)
                                        {
                                            String method = formrule_array.getJSONObject(p).getString("method").toString();
                                            JSONArray condition_array = new JSONArray(formrule_array.getJSONObject(p).getString("condition").toString());
                                            JSONArray show_array = new JSONArray(formrule_array.getJSONObject(p).getString("show").toString());
                                            JSONArray hide_array = new JSONArray(formrule_array.getJSONObject(p).getString("hide").toString());
                                            int conditioncount=0;
                                            for(int j=0;j<condition_array.length();j++)
                                            {
                                                for(int m=0;m<hide_array.length();m++) {
                                                    for(int n=0;n<questionsarray.length();n++)
                                                    {
                                                        if(condition_array.getJSONObject(j).getString("question_id").toString().equalsIgnoreCase(questionid))
                                                        {
                                                            if (hide_array.getJSONObject(m).getString("question_id").toString().equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString())) {
                                                                if (!itemid.equalsIgnoreCase("0") ? questionsarray.getJSONObject(n).getString("item_id").toString().equalsIgnoreCase(itemid) : hide_array.getJSONObject(m).getString("question_id").toString().equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString())) {
                                                                    if (questionsarray.getJSONObject(n).getString("parent").toString().equalsIgnoreCase("1")&& questionsarray.getJSONObject(n).getString("visual").toString().equalsIgnoreCase("0")) {
                                                                        if (questionsarray.getJSONObject(n).getString("dummy_is_required").toString().equalsIgnoreCase("1"))
                                                                            questionsarray.getJSONObject(n).put("is_required", "1");
                                                                        else
                                                                            questionsarray.getJSONObject(n).put("is_required", "0");
                                                                        questionsarray.getJSONObject(n).put("answer_id", "");
                                                                        questionsarray.getJSONObject(n).put("answer", "");
                                                                        questionsarray.getJSONObject(n).put("is_other", "");
                                                                        questionsarray.getJSONObject(n).put("visual", "1");
                                                                        showcontrol(questionsarray.getJSONObject(n).getString("unique_id").toString(), questionsarray.getJSONObject(n).getString("question_type").toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    formarray.getJSONObject(questionposition).put("questions", questionsarray);
                                                }

                                                for(int l=0;l<questionsarray.length();l++) {
                                                    if (questionsarray.getJSONObject(l).getString("unique_id").toString().equalsIgnoreCase(readonlytext.getTag().toString()))
                                                    {
                                                        if (condition_array.getJSONObject(j).getString("question_id").equalsIgnoreCase(questionid)) {
                                                            if (condition_array.getJSONObject(j).getString("condition_type").equalsIgnoreCase("isEmpty")) {
                                                                if (questionsarray.getJSONObject(l).getString("answer").toString().trim().equalsIgnoreCase("")) {
                                                                    conditioncount++;
                                                                }
                                                            } else if (condition_array.getJSONObject(j).getString("condition_type").equalsIgnoreCase("isNotEmpty")) {
                                                                if (!questionsarray.getJSONObject(l).getString("answer").toString().trim().equalsIgnoreCase("")) {
                                                                    conditioncount++;
                                                                }
                                                            }else if (condition_array.getJSONObject(j).getString("condition_type").equalsIgnoreCase("Is")) {
                                                                JSONObject conditionvalueobj = new JSONObject(condition_array.getJSONObject(j).getString("condition_value").toString());
                                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                                                Date selecteddate = sdf.parse(questionsarray.getJSONObject(l).getString("answer").toString().trim());
                                                                Date conditiondate = sdf.parse(conditionvalueobj.getString("condition").toString());
                                                                if (selecteddate.equals(conditiondate)) {
                                                                    conditioncount++;
                                                                }
                                                            } else if (condition_array.getJSONObject(j).getString("condition_type").equalsIgnoreCase("isNot")) {
                                                                JSONObject conditionvalueobj = new JSONObject(condition_array.getJSONObject(j).getString("condition_value").toString());
                                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                                                Date selecteddate = sdf.parse(questionsarray.getJSONObject(l).getString("answer").toString().trim());
                                                                Date conditiondate = sdf.parse(conditionvalueobj.getString("condition").toString());
                                                                if (!selecteddate.equals(conditiondate)) {
                                                                    conditioncount++;
                                                                }
                                                            } else if (condition_array.getJSONObject(j).getString("condition_type").equalsIgnoreCase("Less Than")) {
                                                                JSONObject conditionvalueobj = new JSONObject(condition_array.getJSONObject(j).getString("condition_value").toString());
                                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                                                Date selecteddate = sdf.parse(questionsarray.getJSONObject(l).getString("answer").toString().trim());
                                                                Date conditiondate = sdf.parse(conditionvalueobj.getString("condition").toString());
                                                                if (selecteddate.before(conditiondate)) {
                                                                    conditioncount++;
                                                                }
                                                            } else if (condition_array.getJSONObject(j).getString("condition_type").equalsIgnoreCase("Greater Than")) {
                                                                JSONObject conditionvalueobj = new JSONObject(condition_array.getJSONObject(j).getString("condition_value").toString());
                                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                                                Date selecteddate = sdf.parse(questionsarray.getJSONObject(l).getString("answer").toString().trim());
                                                                Date conditiondate = sdf.parse(conditionvalueobj.getString("condition").toString());
                                                                if (selecteddate.after(conditiondate)) {
                                                                    conditioncount++;
                                                                }
                                                            } else if (condition_array.getJSONObject(j).getString("condition_type").equalsIgnoreCase("Between")) {
                                                                JSONObject conditionvalueobj = new JSONObject(condition_array.getJSONObject(j).getString("condition_value").toString());
                                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                                                Date selecteddate = sdf.parse(questionsarray.getJSONObject(l).getString("answer").toString().trim());
                                                                Date conditiondatefrom = sdf.parse(conditionvalueobj.getString("condition").toString());
                                                                Date conditiondateto = sdf.parse(conditionvalueobj.getString("condition_between").toString());

                                                                if (selecteddate.after(conditiondatefrom)&&selecteddate.before(conditiondateto)) {
                                                                    conditioncount++;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if(method.equalsIgnoreCase("1")){
                                                if(conditioncount >0)
                                                {
                                                    for(int m=0;m<show_array.length();m++) {
                                                        for(int n=0;n<questionsarray.length();n++)
                                                        {
                                                            if(show_array.getJSONObject(m).getString("question_id").toString().equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString()))
                                                            {
                                                                if (!itemid.equalsIgnoreCase("0")?questionsarray.getJSONObject(n).getString("item_id").toString().equalsIgnoreCase(itemid):show_array.getJSONObject(m).getString("question_id").toString().equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString())) {
                                                                    if (questionsarray.getJSONObject(n).getString("visual").toString().equalsIgnoreCase("0")) {
                                                                        if (questionsarray.getJSONObject(n).getString("dummy_is_required").toString().equalsIgnoreCase("1"))
                                                                            questionsarray.getJSONObject(n).put("is_required", "1");
                                                                        else
                                                                            questionsarray.getJSONObject(n).put("is_required", "0");
                                                                        questionsarray.getJSONObject(n).put("answer_id", "");
                                                                        questionsarray.getJSONObject(n).put("answer", "");
                                                                        questionsarray.getJSONObject(n).put("answered", "yes");
                                                                        questionsarray.getJSONObject(n).put("visual", "1");
                                                                        showcontrol(questionsarray.getJSONObject(n).getString("unique_id").toString(), questionsarray.getJSONObject(n).getString("question_type").toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        formarray.getJSONObject(questionposition).put("questions", questionsarray);
                                                    }
                                                    for(int m=0;m<hide_array.length();m++) {
                                                        String issubquestion = "";
                                                        String subqueids="";
                                                        for(int n=0;n<questionsarray.length();n++)
                                                        {

                                                            if(hide_array.getJSONObject(m).getString("question_id").toString().equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString()))
                                                            {
                                                                if (!itemid.equalsIgnoreCase("0")?questionsarray.getJSONObject(n).getString("item_id").toString().equalsIgnoreCase(itemid):hide_array.getJSONObject(m).getString("question_id").toString().equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString())) {
                                                                    if (questionsarray.getJSONObject(n).getString("parent").toString().equalsIgnoreCase("1")) {
                                                                        questionsarray.getJSONObject(n).put("dummy_is_required", questionsarray.getJSONObject(n).has("dummy_is_required") ? questionsarray.getJSONObject(n).getString("dummy_is_required").toString() : questionsarray.getJSONObject(n).getString("is_required").toString());
                                                                        questionsarray.getJSONObject(n).put("is_required", "0");
                                                                        questionsarray.getJSONObject(n).put("answer_id", "");
                                                                        questionsarray.getJSONObject(n).put("answer", "");
                                                                        questionsarray.getJSONObject(n).put("is_other", "");
                                                                        questionsarray.getJSONObject(n).put("visual", "0");
                                                                        issubquestion = questionsarray.getJSONObject(n).getString("subquestion").toString();
                                                                        subqueids =questionsarray.getJSONObject(n).getString("subquestionid").toString();
                                                                        hiddencontrol(questionsarray.getJSONObject(n).getString("unique_id").toString(), questionsarray.getJSONObject(n).getString("question_type").toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(issubquestion.equalsIgnoreCase("1"))
                                                        {
                                                            String[] subquearray = subqueids.split(",");
                                                            for(int k=0;k<subquearray.length;k++)
                                                            {
                                                                for(int n=0;n<questionsarray.length();n++) {
                                                                    if (!itemid.equalsIgnoreCase("0") ? questionsarray.getJSONObject(n).getString("item_id").toString().equalsIgnoreCase(itemid) : subquearray[k].equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString())) {
                                                                        questionsarray.getJSONObject(n).put("dummy_is_required", questionsarray.getJSONObject(n).has("dummy_is_required") ? questionsarray.getJSONObject(n).getString("dummy_is_required").toString() : questionsarray.getJSONObject(n).getString("is_required").toString());
                                                                        questionsarray.getJSONObject(n).put("is_required", "0");
                                                                        questionsarray.getJSONObject(n).put("answer_id", "");
                                                                        questionsarray.getJSONObject(n).put("answer", "");
                                                                        questionsarray.getJSONObject(n).put("is_other", "");
                                                                        questionsarray.getJSONObject(n).put("visual", "0");
                                                                        hiddencontrol(questionsarray.getJSONObject(n).getString("unique_id").toString(), questionsarray.getJSONObject(n).getString("question_type").toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        formarray.getJSONObject(questionposition).put("questions", questionsarray);

                                                    }
                                                }
                                            }else
                                            {
                                                if(condition_array.length()==conditioncount)
                                                {
                                                    for(int m=0;m<show_array.length();m++) {
                                                        for(int n=0;n<questionsarray.length();n++)
                                                        {
                                                            if(show_array.getJSONObject(m).getString("question_id").toString().equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString()))
                                                            {
                                                                if (!itemid.equalsIgnoreCase("0")?questionsarray.getJSONObject(n).getString("item_id").toString().equalsIgnoreCase(itemid):show_array.getJSONObject(m).getString("question_id").toString().equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString())) {
                                                                    if (questionsarray.getJSONObject(n).getString("visual").toString().equalsIgnoreCase("0")) {
                                                                        if (questionsarray.getJSONObject(n).getString("dummy_is_required").toString().equalsIgnoreCase("1"))
                                                                            questionsarray.getJSONObject(n).put("is_required", "1");
                                                                        else
                                                                            questionsarray.getJSONObject(n).put("is_required", "0");
                                                                        questionsarray.getJSONObject(n).put("answer_id", "");
                                                                        questionsarray.getJSONObject(n).put("answer", "");
                                                                        questionsarray.getJSONObject(n).put("answered", "yes");
                                                                        questionsarray.getJSONObject(n).put("visual", "1");
                                                                        showcontrol(questionsarray.getJSONObject(n).getString("unique_id").toString(), questionsarray.getJSONObject(n).getString("question_type").toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        formarray.getJSONObject(questionposition).put("questions", questionsarray);
                                                    }
                                                    for(int m=0;m<hide_array.length();m++)
                                                    {
                                                        String issubquestion = "";
                                                        String subqueids="";
                                                        for(int n=0;n<questionsarray.length();n++)
                                                        {

                                                            if(hide_array.getJSONObject(m).getString("question_id").toString().equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString()))
                                                            {
                                                                if (!itemid.equalsIgnoreCase("0")?questionsarray.getJSONObject(n).getString("item_id").toString().equalsIgnoreCase(itemid):hide_array.getJSONObject(m).getString("question_id").toString().equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString())) {
                                                                    if (questionsarray.getJSONObject(n).getString("parent").toString().equalsIgnoreCase("1")) {
                                                                        questionsarray.getJSONObject(n).put("dummy_is_required", questionsarray.getJSONObject(n).has("dummy_is_required") ? questionsarray.getJSONObject(n).getString("dummy_is_required").toString() : questionsarray.getJSONObject(n).getString("is_required").toString());
                                                                        questionsarray.getJSONObject(n).put("is_required", "0");
                                                                        questionsarray.getJSONObject(n).put("answer_id", "");
                                                                        questionsarray.getJSONObject(n).put("answer", "");
                                                                        questionsarray.getJSONObject(n).put("is_other", "");
                                                                        questionsarray.getJSONObject(n).put("visual", "0");
                                                                        issubquestion = questionsarray.getJSONObject(n).getString("subquestion").toString();
                                                                        subqueids =questionsarray.getJSONObject(n).getString("subquestionid").toString();
                                                                        hiddencontrol(questionsarray.getJSONObject(n).getString("unique_id").toString(), questionsarray.getJSONObject(n).getString("question_type").toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(issubquestion.equalsIgnoreCase("1"))
                                                        {
                                                            String[] subquearray = subqueids.split(",");
                                                            for(int k=0;k<subquearray.length;k++)
                                                            {
                                                                for(int n=0;n<questionsarray.length();n++) {
                                                                    if (!itemid.equalsIgnoreCase("0") ? questionsarray.getJSONObject(n).getString("item_id").toString().equalsIgnoreCase(itemid) : subquearray[k].equalsIgnoreCase(questionsarray.getJSONObject(n).getString("id").toString())) {
                                                                        questionsarray.getJSONObject(n).put("dummy_is_required", questionsarray.getJSONObject(n).has("dummy_is_required") ? questionsarray.getJSONObject(n).getString("dummy_is_required").toString() : questionsarray.getJSONObject(n).getString("is_required").toString());
                                                                        questionsarray.getJSONObject(n).put("is_required", "0");
                                                                        questionsarray.getJSONObject(n).put("answer_id", "");
                                                                        questionsarray.getJSONObject(n).put("answer", "");
                                                                        questionsarray.getJSONObject(n).put("is_other", "");
                                                                        questionsarray.getJSONObject(n).put("visual", "0");
                                                                        hiddencontrol(questionsarray.getJSONObject(n).getString("unique_id").toString(), questionsarray.getJSONObject(n).getString("question_type").toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        formarray.getJSONObject(questionposition).put("questions", questionsarray);

                                                    }
                                                }
                                            }
                                        }

                                    }


                                } catch (Exception ex) {
                                    Log.e("Json exception", ex.getMessage().toString());
                                }

                                view.setMaxDate(System.currentTimeMillis());
                            }
                        };
                    //    DatePickerDialog dpDialog = new DatePickerDialog(ScannerActivity.this, listener, year, month, day);
                        try {
                            if (validationobj.getString("values").toString().equalsIgnoreCase("0"))
                                dpDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+ (1000 * 60 * 60));
                        } catch (Exception ex) {
                            Log.e("Datepicker EX", ex.getMessage().toString());
                        }
                        dpDialog.show();*/
                    }
                });
                row.addView(view);
            }
             else if (type.equalsIgnoreCase("qmrb"))     {
                int radioid = 2018;
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(controlmarginleft, 0, controlmarginright, 0);
                JSONArray valuearray = new JSONArray(values);
                final RadioGroup radiogroup = new RadioGroup(this);
                radiogroup.setOrientation(RadioGroup.VERTICAL);

                radiogroup.setLayoutParams(params);
                radiogroup.setTag(id);
                radiogroup.setId(id);
                radiogroup.requestFocus();
                for (int i = 0; i < valuearray.length(); i++) {
                    String subids="";
                    RadioButton radiobtn = new RadioButton(this);
                    radiobtn.setText(valuearray.getJSONObject(i).getString("title").toString());
                    // radiobtn.setText("arun");
                    radiobtn.setPadding(rbpaddingleft,rbpaddingtop,rbpaddingbottom,rbpaddingright);
                    radiobtn.setId(radioid);
                    TypedValue outxValue = new TypedValue();
                    getResources().getValue(R.dimen.default_size_1dp, outxValue, true);
                    float xvalue = outxValue.getFloat();
                    TypedValue outyValue = new TypedValue();
                    getResources().getValue(R.dimen.default_size_1dp, outyValue, true);
                    float yvalue = outyValue.getFloat();
                    radiobtn.setTag(valuearray.getJSONObject(i).getString("code").toString()+"~"+subids);
                    int px = getResources().getDimensionPixelSize(R.dimen.default_size_15dp);
                    float size = px / getResources().getDisplayMetrics().density;
                    radiobtn.setTextSize(size);
                    radiobtn.setCompoundDrawablePadding(50);
                    radiobtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    radiogroup.addView(radiobtn);
                    radioid++;
                }



                radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup arg0, int selectedId) {
                        selectedId = radiogroup.getCheckedRadioButtonId();
                        RadioButton radiobutton = (RadioButton) findViewById(selectedId);
                        if (radiobutton != null)
                        {
                            String[] valuess = radiobutton.getTag().toString().split("~");
                            String[] ids = valuess.length > 1 ? valuess[1].toString().split(",") : null;
                            String[] dependids;
                            String strdependids = "";

                            for (int i = 0; i < radiogroup.getChildCount(); i++) {
                                View v = radiogroup.getChildAt(i);
                                if (v instanceof RadioButton) {
                                    String[] childid = ((RadioButton) v).getTag().toString().split("~");
                                    if (childid.length > 1)
                                        strdependids += childid[1].substring(0, childid[1].length() - 1) + ",";

                                }
                            }

                            strdependids = strdependids.substring(0, strdependids.length() > 0 ? strdependids.length() - 1 : 0);
                            dependids = strdependids.equalsIgnoreCase("") ? null : strdependids.split(",");
                            if (dependids != null) {
                            }
                            if (ids != null)





                                try {

                                    TableRow vparent = (TableRow) radiogroup.getParent();
                                    RelativeLayout rellayout = (RelativeLayout)vparent.findViewById(R.id.rowtypelayout);
                                    TextView txtrowtype = (TextView)rellayout.findViewById(R.id.txttablerowtype);
                                    String itemid = txtrowtype.getTag().toString();

                                    //Page Rule
                                    JSONArray questionsarray = null;
                                    String questionid = "";
                                    int questionposition = 0;




                                }catch(Exception ex)
                                {
                                    Log.e("Json Exception",ex.getMessage().toString());
                                }
                        }
                    }



                });
                row.addView(radiogroup);
            }
             else if (type.equalsIgnoreCase("divideline")) {
                 TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                 row.setBackgroundColor(Color.parseColor("#DCDCDC"));
                /*row.setId(id);*/
                 row.setLayoutParams(params);
             }
            else if (type.equalsIgnoreCase("qmgm")) {
                 TableRow.LayoutParams paramss = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                 paramss.setMargins(controlmarginleft, 0, controlmarginright, 0);
                 row.setGravity(Gravity.CENTER);
                 ListView lstv = new ListView(this);
                 lstv.setLayoutParams(paramss);
                 if(bgcolour.equalsIgnoreCase("#FFFFFF"))
                     lstv.setBackgroundResource(R.drawable.editable);
                 else
                     lstv.setBackgroundResource(R.drawable.editable);

                 lstv.setTag(id);
                 lstv.setId(id);
                 ArrayList<ListClass> rowvaluelist = new ArrayList<ListClass>();
                 ArrayList<SimpleListClass> columnvaluelist = new ArrayList<SimpleListClass>();
                 JSONArray colvalarray = new JSONArray(columnvalues);
                 for (int i = 0; i < colvalarray.length(); i++) {
                     columnvaluelist.add(new SimpleListClass(colvalarray.getJSONObject(i).getString("code"), colvalarray.getJSONObject(i).getString("title")));
                 }
                 JSONArray rowvalarray = new JSONArray(rowvalues);
                 for (int i = 0; i < rowvalarray.length(); i++) {
                     rowvaluelist.add(new ListClass(rowvalarray.getJSONObject(i).getString("code"), rowvalarray.getJSONObject(i).getString("title"), columnvaluelist, lstv.getTag().toString()));
                 }
                 MultiCheckListCustomAdapter adapter = new MultiCheckListCustomAdapter(ScannerActivity.this, R.layout.multicheck_list_row, rowvaluelist,bgcolour);
                 lstv.setAdapter(adapter);
                 setMultiListViewHeightBasedOnItems(row, adapter);
                 row.addView(lstv);
             }
            else if(type.equalsIgnoreCase("qmtn")){
                 TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height+20);
                 TableRow.LayoutParams qmtnparams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                 row.setBackground(getResources().getDrawable(R.drawable.editable));
                 gridformcontrolheight = height;
                 params.setMargins(controlmarginleft, 0, controlmarginright, 0);
                 row.setTag(isgrid);
                 final JSONObject validationobj = new JSONObject(validation);
                 final EditText numbertext = new EditText(this);

                 numbertext.setLayoutParams(params);
                 numbertext.setTag(id);
                 numbertext.setId(id);
                 numbertext.setBackgroundColor(Color.parseColor(bgcolour));
                 numbertext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                 numbertext.setSingleLine(true);
                 row.addView(numbertext);
             }
            else if (type.equalsIgnoreCase("qmgc")) {
                TableRow.LayoutParams paramss = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                paramss.setMargins(controlmarginleft, 0, controlmarginright, 0);
                row.setGravity(Gravity.CENTER);
                ListView lstv = new ListView(this);
                if(bgcolour.equalsIgnoreCase("#FFFFFF"))
                    lstv.setBackgroundResource(R.drawable.editable);
                else
                    lstv.setBackgroundResource(R.drawable.spinner_bg);
                lstv.setLayoutParams(paramss);
                //lstv.setNestedScrollingEnabled(true);
                lstv.setTag(id);
                lstv.setId(id);
                ArrayList<ListClass> rowvaluelist = new ArrayList<ListClass>();
                ArrayList<SimpleListClass> columnvaluelist = new ArrayList<SimpleListClass>();
                JSONArray colvalarray = new JSONArray(columnvalues);
                for (int i = 0; i < colvalarray.length(); i++)
                {
                    columnvaluelist.add(new SimpleListClass(colvalarray.getJSONObject(i).getString("code"), colvalarray.getJSONObject(i).getString("title")));
                }
                JSONArray rowvalarray = new JSONArray(rowvalues);
                for (int i = 0; i < rowvalarray.length(); i++)
                {
                    rowvaluelist.add(new ListClass(rowvalarray.getJSONObject(i).getString("code"), rowvalarray.getJSONObject(i).getString("title"), columnvaluelist, lstv.getTag().toString()));
                }
                MultiCheckListCustomAdapter adapter = new MultiCheckListCustomAdapter(ScannerActivity.this, R.layout.multicheck_list_row, rowvaluelist,bgcolour);
                lstv.setAdapter(adapter);
                setMultiListViewHeightBasedOnItems(row, adapter);
                row.addView(lstv);
            }
            if(gridfilter == 1) {
                TableRow.LayoutParams params = new TableRow.LayoutParams(1, 1);
                LayoutInflater filterheadinflater = ScannerActivity.this.getLayoutInflater();
                View view = filterheadinflater.inflate(R.layout.filterid, null, false);
                view.setLayoutParams(params);
                final TextView txtfilter1 = (TextView) view.findViewById(R.id.txtfilter1);
                final TextView txtfilter2 = (TextView) view.findViewById(R.id.txtfilter2);
                if (!filter1.equalsIgnoreCase(",")) {
                    String[] filer1array = filter1.split(",");
                    txtfilter1.setTag(filer1array[0]);
                    txtfilter1.setText(filer1array[1]);
                }
                if (!filter1.equalsIgnoreCase(",")) {
                    String[] filer2array = filter2.split(",");
                    txtfilter2.setTag(filer2array[0]);
                    txtfilter2.setText(filer2array[1]);
                }

                row.addView(view);
            }
            TableRow.LayoutParams params = new TableRow.LayoutParams(1,1);
            LayoutInflater filterheadinflater = ScannerActivity.this.getLayoutInflater();
            View views = filterheadinflater.inflate(R.layout.rowtype, null, false);
            views.setLayoutParams(params);
            final TextView txtrowtype = (TextView) views.findViewById(R.id.txttablerowtype);
            txtrowtype.setText(String.valueOf(rowtype));
            txtrowtype.setTag(String.valueOf(item_id));
            row.addView(views);
            tl.addView(row);

        }catch (Exception e){
        Log.e("control",e.getMessage().toString());
        }
    }
    static boolean setMultiListViewHeightBasedOnItems(TableRow row, MultiCheckListCustomAdapter adapter) {
        if (adapter != null) {
            int numberOfItems = adapter.getCount();
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = adapter.getView(itemPos, null, row);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }
            // Get total height of all item dividers.
            int totalDividersHeight = row.getMeasuredHeight() * (numberOfItems - 1);
            // Set list height.
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, totalItemsHeight + totalDividersHeight);

            row.setLayoutParams(params);
            row.requestLayout();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//using barcode reader app
       /* super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            final String result = data.getStringExtra("SCAN_RESULT");
        }*/

        //using zxing jar

      /*  IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

*/
        /*if (requestCode == SCANNER_REQUEST_CODE) {
            // Handle scan intent
            if (resultCode == Activity.RESULT_OK) {
                // Handle successful scan
                String contents = data.getStringExtra("SCAN_RESULT");
                String formatName = data.getStringExtra("SCAN_RESULT_FORMAT");
                byte[] rawBytes = data.getByteArrayExtra("SCAN_RESULT_BYTES");
                int intentOrientation = data.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
                Integer orientation = (intentOrientation == Integer.MIN_VALUE) ? null : intentOrientation;
                String errorCorrectionLevel = data.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");

            }else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
            }
        } else {
            // Handle other intents
        }*/
    }
}
