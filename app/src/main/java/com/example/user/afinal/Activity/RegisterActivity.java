package com.example.user.afinal.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.afinal.DATABASE.DBAdapter;
import com.example.user.afinal.DATABASE.SQLiteHelper;
import com.example.user.afinal.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class RegisterActivity extends Activity
{

    EditText GetName;
    AutoCompleteTextView GetAge;
    EditText GetAddress;
    EditText Getpass;
    ImageView imageViewLoad;
    Button LoadImage;
    Button Submit;
    Button ShowValues;
    public static final String TABLE_NAME="demoTable";
    public static final String TABLE_NAME2 = "colTable2";
    public static final String TABLE_NAME3 = "imgTable";
    SQLiteHelper sqlitehelper;

    SQLiteDatabase SQLITEDATABASE;
    String Name,Age,Address,RPassword;
    private byte[] photo;
    Boolean CheckEditTextEmpty;

    private EditText dobedt;
    private DatePickerDialog dobPickDiaglog;

    private static int IMG_RESULT=1;
    String ImageDecode;

    Intent intent;
    String[] FILE;
    private Bitmap bp;
  static  final    String pref_username ="PREF_USERNAME";
    static  final  String pref_age ="PREF_AGE";

    SharedPreferences pref;
    SharedPreferences.Editor sEdit;


    DBAdapter adb = new DBAdapter(RegisterActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        initcallback();
    }
    public  void init(){
        try {
            adb.open();
            sqlitehelper = new SQLiteHelper(this);
            SQLITEDATABASE = sqlitehelper.getWritableDatabase();
            GetName = (EditText)findViewById(R.id.edt_name);
            GetAge = (AutoCompleteTextView)findViewById(R.id.edt_age);
            GetAddress = (EditText)findViewById(R.id.edt_address);
            Getpass =(EditText)findViewById(R.id.edt_pass1);
            Submit = (Button)findViewById(R.id.btn_save);
            imageViewLoad=(ImageView)findViewById(R.id.imageView1);
            LoadImage=(Button)findViewById(R.id.btn_image);

            String str[]={
                    "01","02","03","04","05","06","07","08","09","10",
                    "11","12","13","14","15","16","17","18","19","20",
                    "21","22","23","24","25","26","27","28","29","30",
                    "31","32","33","34","35","36","37","38","39","40",
                    "41","42","43","44","45","46","47","48","49","50",
                    "51","52","53","54","55","56","57","58","59","60",
                    "61","62","63","64","65","66","67","68","69","70",
                    "71","72","73","74","75","76","77","78","79","80",
                    "81","82","83","84","85","86","87","88","89","90",
                    "91","92","93","94","95","96","97","98","99","100"};


            ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line,str);
            GetAge.setThreshold(1);
            GetAge.setAdapter(adp);
            GetAge.setDropDownBackgroundDrawable(new ColorDrawable(getBaseContext().getResources().getColor(R.color.color10pink)));
            findViewsById();
            setaDateTimeField();


            pref = PreferenceManager.getDefaultSharedPreferences(this);

                sEdit = pref.edit();

        }catch (Exception e){
            Log.e("register init",e.getMessage().toString());
        }



    }
    public  void initcallback(){
        try{
            LoadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(intent, IMG_RESULT);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 2);
                }
            });

            Submit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SubmitData2SQLiteDB();

                }
            });

        }catch (Exception e){
            Log.e("register initcallback",e.getMessage().toString());
        }


    }


    public  void SubmitData2SQLiteDB()
            {
                try {
                    Name = GetName.getText().toString();
                    Age = GetAge.getText().toString();
                    Address = GetAddress.getText().toString();
                    RPassword =Getpass.getText().toString();
                    if(imageViewLoad.getDrawable()!=null){  photo = profileImage(bp);}
                    CheckEditTextIsEmptyOrNot( Name,Age,Address,RPassword);

                    sEdit.putString("tusername",Name);
                    sEdit.putString("tage",Age);
                    sEdit.commit();

if (!DashboardActivity.checkDay)
                    Toast.makeText(RegisterActivity.this,pref.getString("tusername",null) + pref.getString("tage",null), Toast.LENGTH_LONG).show();
                    if(CheckEditTextEmpty == true)
                    {
                        if(RPassword.length()>4){
                            //adb.insertdata(Name,Age,Address,RPassword);
                            // adb.insertdata2(Name,Age,Address,RPassword);
                            adb.insertdata3(Name,Age,Address,RPassword,photo);
                            Toast.makeText(RegisterActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();
                            ClearEditTextAfterDoneTask();
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slidrleft,R.anim.slideright);
                            finish();

                        }else {Toast.makeText(RegisterActivity.this,"password must contain 5 digit", Toast.LENGTH_LONG).show();}

                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,"Please Fill  the Field", Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e)
                {
                    Log.e("register submitSQLiteDb",e.getMessage().toString());
                }

            }

                    public void CheckEditTextIsEmptyOrNot(String Name,String Age,String Address,String RPassword) {
                        if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Age) || TextUtils.isEmpty(Address)||TextUtils.isEmpty(RPassword)){

                            CheckEditTextEmpty = false;
                        }
                        else {
                            CheckEditTextEmpty = true;
                        }
                         }

    public void ClearEditTextAfterDoneTask() {
        GetName.getText().clear();
        GetAge.getText().clear();
        GetAddress.getText().clear();
        Getpass.getText().clear();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 2:
                if(resultCode == RESULT_OK){
                    Uri choosenImage = data.getData();
                    if(choosenImage !=null){
                        bp=decodeUri(choosenImage, 400);
                        imageViewLoad.setImageBitmap(bp);
                    }
                }
        }
    }

    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b){
        try {ByteArrayOutputStream bos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 0, bos);
            return bos.toByteArray();

        }catch (Exception e){Log.e("register profileImage",e.getMessage().toString());
        return null;}

    }
    private void getValues(){
        Name = GetName.getText().toString();
        Age = GetAge.getText().toString();
        Address = GetAddress.getText().toString();
        RPassword =Getpass.getText().toString();
        photo = profileImage(bp);

    }
    private void findViewsById()
    {
        dobedt=(EditText) findViewById(R.id.edt_dob);
        dobedt.setInputType(InputType.TYPE_NULL);
        dobedt.requestFocus();
    }
    private void setaDateTimeField()
    {
        try {
            dobedt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dobPickDiaglog.show();
                }
            });
            Calendar newCalender = Calendar.getInstance();
            dobPickDiaglog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year,monthOfYear, dayOfMonth);
                    dobedt.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));

        }catch (Exception e){Log.e("register DataTimeField",e.getMessage().toString());}

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this,DashboardActivity.class);
        startActivityForResult(i,500);
    }
}

/*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == IMG_RESULT && resultCode == RESULT_OK && null != data) {
                Uri URI = data.getData();
                String[] FILE = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(URI, FILE, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();

                imageViewLoad.setImageBitmap(BitmapFactory.decodeFile(ImageDecode));

            }
        } catch (Exception e) {
            Toast.makeText(this,"try again",Toast.LENGTH_LONG).show();
        }
    }*/
