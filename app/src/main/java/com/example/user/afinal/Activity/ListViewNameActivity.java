package com.example.user.afinal.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.afinal.Model.Contact;
import com.example.user.afinal.DATABASE.DBAdapter;
import com.example.user.afinal.DATABASE.SQLiteHelper;
import com.example.user.afinal.Adapter.SQLiteListAdapter;
import com.example.user.afinal.Adapter.datagettingAdapter;
import com.example.user.afinal.R;

import java.io.IOException;
import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

public class ListViewNameActivity extends Activity {

    SQLiteHelper SQLITEHELPER;
    SQLiteDatabase SQLITEDATABASE;
    Cursor cursor ;
    SQLiteListAdapter ListAdapter;

    datagettingAdapter data;

    ArrayList<String> ID_ArrayList = new ArrayList<String>();
    ArrayList<String> NAME_ArrayList = new ArrayList<String>();
    ArrayList<String> Age_ArrayList = new ArrayList<String>();
    ArrayList<String> Address_ArrayList = new ArrayList<String>();
    ArrayList<byte[]> Img_ArrayList = new ArrayList<>();

    ArrayList<Contact> contacts = new ArrayList<Contact>();

    ListView LISTVIEW;
    ArrayList<String> Liste;

    ArrayAdapter<String> adp ;
    AutoCompleteTextView auto;
    EditText edt_search;
    Button reset;
    Button delete;
    Button home;
    ImageView close;
    String str;

    Handler handler;
    private Contact datamodel;


    DBAdapter adb = new DBAdapter(ListViewNameActivity.this);
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_name);
        init();
        initcallback();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public  void init(){
        setContentView(R.layout.activity_list_view_name);
        try {
            SQLITEHELPER = new SQLiteHelper(this);
            SQLITEDATABASE = SQLITEHELPER.getWritableDatabase();
            LISTVIEW = (ListView) findViewById(R.id.listView1);
            auto = (AutoCompleteTextView) findViewById(R.id.ato_name);
            edt_search=(EditText)findViewById(R.id.edt_search);
            home=(Button)findViewById(R.id.btn_Home);
            Liste = new ArrayList<String>();
            dropdw();
            initial();
            initcallback();
            Showrecords();
            //ShowSQLiteDBdata();

            auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    str = (String) parent.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(), " you selected : " + str, Toast.LENGTH_SHORT).show();
                    //showsinglecontact(str);
                    singlerecord(str);

                }
            });
            auto.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Liste.clear();
                    initial();
                }
            });

            reset =(Button)findViewById(R.id.btn_reset);
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*final Animation myAnim = AnimationUtils.loadAnimation(ListViewNameActivity.this, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.6, 30);
                    myAnim.setInterpolator(interpolator);
                    reset.startAnimation(myAnim);*/
                   data.clear();
                    init();
                    auto.requestFocus();

                }
            });

        }catch (Exception e)
        { Log.e("Listview init",e.getMessage().toString());}
    }
    public void initcallback(){
        try {
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  Intent i = new Intent(ListViewNameActivity.this,MapsActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slidrleft,R.anim.slideright);
                }
            });
        }catch (Exception e)
        { Log.e("listview initcallback",e.getMessage().toString());}
    }
    private void dropdw() {
        try {adb.open();
            Liste.clear();
            cursor=adb.getnames3();
            if(cursor.moveToFirst()){
                Liste = new ArrayList<String>();
                do {
                    Liste.add(cursor.getString(0));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            adb.close();

        }catch (Exception e){Log.e("listview dropdw",e.getMessage().toString());}

    }
    private void initial() {
        try { if(Liste.isEmpty()){dropdw();}
            adp = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, Liste);
            adp.setNotifyOnChange(true);
            adp.notifyDataSetInvalidated();
            adp.notifyDataSetChanged();
            auto.setThreshold(1);
            auto.setAdapter(adp);
            auto.setDropDownBackgroundDrawable(new ColorDrawable(getBaseContext().getResources().getColor(R.color.color30lime)));


        }catch (Exception e){Log.e("listview_initial",e.getMessage().toString());}

    }
    public void Showrecords(){
        try {
            edt_search.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    ListViewNameActivity.this.data.getFilter().filter(cs.toString());
                    data.notifyDataSetChanged();




                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub

                }
            });
            adb.open();
                cursor = adb.getall3();
                if (cursor.moveToFirst()) {
                    do {
                        Contact contact = new Contact();
                        contact.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
                        contact.setName(cursor.getString(cursor.getColumnIndex("name")));
                        contact.setAge(cursor.getString(cursor.getColumnIndex("age")));
                        contact.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        contact.setImage(cursor.getBlob(cursor.getColumnIndex("poto")));
                        contacts.add(contact);
                    } while (cursor.moveToNext());
                }


            data = new datagettingAdapter(ListViewNameActivity.this,contacts);

            data.setNotifyOnChange(true);
            data.notifyDataSetInvalidated();
            data.notifyDataSetChanged();
            LISTVIEW.setAdapter(data);
            LISTVIEW.setTextFilterEnabled(true);



        }catch (Exception e){Log.e("listview_Showrecords",e.getMessage().toString());}

    }


    private void singlerecord(String str){
    try{ SQLITEDATABASE = SQLITEHELPER.getReadableDatabase();
        SQLITEDATABASE.isOpen();
        contacts.clear();
        cursor=SQLITEDATABASE.query(SQLITEHELPER.TABLE_NAME3,new String[]{"_id","name","age","address","poto"},SQLITEHELPER.COLM_Name +"=?",new String[]{str},null,null,null,null);
        if(cursor!=null && cursor.moveToFirst())
        {Contact contact = new Contact();
            contact.setID(Integer.parseInt(cursor.getString(0)));
            contact.setName(cursor.getString(1));
            contact.setAge(cursor.getString(2));
            contact.setAddress(cursor.getString(3));
            contact.setImage(cursor.getBlob(4));
            contacts.add(contact);
        }cursor.close();
        data = new datagettingAdapter(ListViewNameActivity.this,contacts);
        LISTVIEW.setAdapter(data);
        data.notifyDataSetChanged();

    }catch (Exception e){Log.e("listview_singlerecord",e.getMessage().toString());}


}

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListViewNameActivity.this,R.style.MyAlertDialogStyle);
        alertDialog.setTitle("Alert!");
        alertDialog.setMessage("you want to go Dashboard page");
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                              finish();
                            Intent i = new Intent(ListViewNameActivity.this,DashboardActivity.class);
                            startActivityForResult(i,500);

                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


}






   /* private void ShowSQLiteDBdata() {
        try {
            adb.open();
            cursor = adb.getall3();
            NAME_ArrayList.clear();
            Age_ArrayList.clear();
            Address_ArrayList.clear();
            Img_ArrayList.clear();
            if (cursor.moveToFirst()) {
                do {
                    ID_ArrayList .add(cursor.getString(cursor.getColumnIndex("_id")));
                    NAME_ArrayList.add(cursor.getString(cursor.getColumnIndex("name")));
                    Age_ArrayList.add(cursor.getString(cursor.getColumnIndex("age")));
                    Address_ArrayList.add(cursor.getString(cursor.getColumnIndex("address")));
                    Img_ArrayList.add(cursor.getBlob(cursor.getColumnIndex("poto")));

                } while (cursor.moveToNext());
            }
            ListAdapter = new SQLiteListAdapter(ListViewNameActivity.this,
                    NAME_ArrayList,
                    Age_ArrayList,
                    Address_ArrayList,
                    ID_ArrayList,
                    Img_ArrayList);

            LISTVIEW.setAdapter(ListAdapter);
        }catch (Exception e){Log.e("listview_DBdata",e.getMessage().toString());}

}


    private void showsinglecontact(String str) {
        try {
            SQLITEDATABASE = SQLITEHELPER.getReadableDatabase();
            SQLITEDATABASE.isOpen();
            NAME_ArrayList.clear();
            Age_ArrayList.clear();
            Address_ArrayList.clear();
            ID_ArrayList.clear();
            Img_ArrayList.clear();
            cursor=SQLITEDATABASE.query(SQLITEHELPER.TABLE_NAME3,new String[]{"_id","name","age","address","poto"},SQLITEHELPER.COLM_Name +"=?",new String[]{str},null,null,null,null);
            if(cursor!=null && cursor.moveToFirst())
            {ID_ArrayList.add(cursor.getString(0));
                NAME_ArrayList.add(cursor.getString(1));
                Age_ArrayList.add(cursor.getString(2));
                Address_ArrayList.add(cursor.getString(3));
                Img_ArrayList.add(cursor.getBlob(4));
            }cursor.close();

            ListAdapter = new SQLiteListAdapter(ListViewNameActivity.this,
                    NAME_ArrayList,
                    Age_ArrayList,
                    Address_ArrayList,
                    ID_ArrayList,
                    Img_ArrayList);
            LISTVIEW.setAdapter(ListAdapter);
            ListAdapter.notifyDataSetChanged();


        }catch (Exception e){Log.e("listview_singlecontact",e.getMessage().toString());}

    }*/






 /*Bundle bundle =getIntent().getExtras();
        String logname =bundle.getString("username");
        String logpassword =bundle.getString("password");
         Toast.makeText(getApplicationContext(),"username" + logname,Toast.LENGTH_SHORT).show();*/


