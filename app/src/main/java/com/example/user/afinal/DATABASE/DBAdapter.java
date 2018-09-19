package com.example.user.afinal.DATABASE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.user.afinal.Adapter.SQLiteListAdapter;
import com.example.user.afinal.Model.Contact;
import com.example.user.afinal.Model.loginContact;
import com.example.user.afinal.Utility.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 11/15/2017.
 */

public class DBAdapter {
    private SQLiteDatabase database;
    private SQLiteOpenHelper helper;
    private SQLiteListAdapter Sqadapter;


    public static String DATABASE_NAME = "DemoDataBase";
    public static final String TABLE_NAME = "demoTable";
    public static final String KEY_ID = "_id";
    public static final String KEY_Name = "name";
    public static final String KEY_Age = "age";
    public static final String KEY_Address = "address";
//ALTER
    public  static  final String KEY_PASSWORD = "password";
///NEW TABLE1
    public static final String TABLE_NAME2 = "colTable2";
    public static final String COL_ID = "_id";
    public static final String COL_Name = "name";
    public static final String COL_Age = "age";
    public static final String COL_Address = "address";
    public static final String COL_Password = "password";

    ///newtable2
    public static final String TABLE_NAME3 = "imgTable";
    public static final String COLM_ID = "_id";
    public static final String COLM_Name = "name";
    public static final String COLM_Age = "age";
    public static final String COLM_Address = "address";
    public static final String COLM_Password = "password";
    public static final String COLM_POTO = "poto";


    //logintable
    static final String loginTable="Logindetails";
    static final String collogintype="logintype";
    static final String colusername="username";
    static final String colprofileppic="profilepic";


    public DBAdapter(Context context) {
        helper = new SQLiteHelper(context);


    }
    public DBAdapter open() throws SQLException {

        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
    }

    public long insertdata(String name, String age, String address,String password) {

        ContentValues insertvalue = new ContentValues();
        insertvalue.put(KEY_Name, name);
        insertvalue.put(KEY_Age, age);
        insertvalue.put(KEY_Address, address);
        insertvalue.put(KEY_PASSWORD, password);
        return database.insert(TABLE_NAME, null, insertvalue);
    }
    public long insertdata2(String name, String age, String address,String password) {

        ContentValues insertvalue = new ContentValues();
        insertvalue.put(COL_Name, name);
        insertvalue.put(COL_Age, age);
        insertvalue.put(COL_Address, address);
        insertvalue.put(COL_Password,password);
        return database.insert(TABLE_NAME2, null, insertvalue);
    }
    public long insertdata3(String name, String age, String address,String password,byte[] photo ){
        ContentValues insertvalue = new ContentValues();
        insertvalue.put(COLM_Name,name);
        insertvalue.put(COLM_Age,age);
        insertvalue.put(COLM_Address,address);
        insertvalue.put(COLM_Password,password);
        if(photo==null){
            insertvalue.putNull(COLM_POTO);
        }else{
            insertvalue.put(COLM_POTO,photo);
        }
        return database.insert(TABLE_NAME3,null,insertvalue);
    }

    public void clearalarm(){
        database.delete(TABLE_NAME, null,null);
    }
    public  long insertalarm1(long milli){
    ContentValues cv = new ContentValues();
    cv.put("name", String.valueOf(milli));
    return database.insert(TABLE_NAME, null, cv);


}
    public long getalarm1(){

        Cursor cursor = database.rawQuery("SELECT name FROM demoTable",null);
        cursor.moveToLast();
      //return   Long.parseLong(cursor.getString(cursor.getColumnIndex("name")));
        return Long.parseLong(cursor.getString(0));
    }

    public boolean deleterow(long id) throws SQLException {
        return database.delete(TABLE_NAME, KEY_ID + "=" + id, null) > 0;
       // String table = TABLE_NAME;
        //String whereClause = "_id=?";
        //String[] whereArgs = new String[]{Integer.toString(id)};
        //return database.delete(table, whereClause, whereArgs) > 0;
    }
    public boolean deleterow3(long id) throws SQLException {
        return database.delete(TABLE_NAME3, COLM_ID + "=" + id, null) > 0;}


    public Cursor getall() {
        return database.rawQuery("SELECT * FROM demoTable", null);
    }
    public Cursor getall2() {
        return database.rawQuery("SELECT * FROM colTable2", null);
    }
    public Cursor getall3() {
        return database.rawQuery("SELECT * FROM imgTable", null);
    }
    public  Cursor getnames() throws SQLException{return database.rawQuery("SELECT name FROM demotable",null);}
    public  Cursor getnames3() throws SQLException{return database.rawQuery("SELECT name FROM imgTable",null);}

    public boolean Login(String username,String password) throws SQLException {
        Cursor mCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME3 + " WHERE " + COLM_Name + "=?" +  " AND " + COLM_Password + "=?", new String[]{username,password});
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                int id =0;
                byte[] profilepic = null;
                mCursor.moveToFirst();
                id = mCursor.getInt(mCursor.getColumnIndex("_id"));
                profilepic = mCursor.getBlob(mCursor.getColumnIndex("poto"));
                MyApplication.getInstance().setId(id);
                MyApplication.getInstance().setLoginprofilepic(profilepic);
                return true;
            }
        }
        return false;
    }

    public long insertlogindata(String logintype,String username,byte[] profilepic)
    {
        try {
            database.delete(loginTable,null,null);
            ContentValues initialValues = new ContentValues();
            initialValues.put(collogintype, logintype);
            initialValues.put(colusername, username);
            if(profilepic==null){
                initialValues.putNull(colprofileppic);
            }else{
                initialValues.put(colprofileppic, profilepic);
            }
            return database.insert(loginTable, null, initialValues);

        }catch (SQLiteException e){
            if (e.getMessage().contains("no such table")){
                Log.e("DBAdapter", "Creating table " + loginTable + "because it doesn't exist!" );
            }else{Log.e("DBAdapter",e.getMessage().toString() );}
            return database.insert(loginTable, null, null);
        }


    }
    public  void deletelogindata(){
        database.delete(loginTable,null,null);
    }
//not using
    public String[] getlogindata() {
        String[] logindetails = null;
        Cursor c = database.rawQuery("SELECT * FROM Logindetails", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        logindetails = new String[3];
                        logindetails[0] = c.getString(c.getColumnIndex("logintype"));
                        logindetails[1] = c.getString(c.getColumnIndex("username"));
                        logindetails[2] = new String(c.getBlob(c.getColumnIndex("profilepic")));

                    } while (c.moveToNext());
                }
            }return logindetails;

    }

    public  loginContact getlogincontact(){
        loginContact contact = null;
        Cursor c = database.rawQuery("SELECT * FROM Logindetails", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                 contact = new loginContact( c.getString(c.getColumnIndex("logintype")),c.getString(c.getColumnIndex("username")),c.getBlob(c.getColumnIndex("profilepic")));
                } while (c.moveToNext());
            }


        }
        return contact;
    }



    public Boolean DeletedatafromSQl(String str) {

        final String whereClause = KEY_Name + "=?";
        final String whereArgs[] = {str};
        int affectedRows = database.delete(TABLE_NAME, whereClause, whereArgs);
        return affectedRows > 0;
    }
    public List<Contact> getAllContacts(){
        List<Contact> contactList = new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME3;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
                contact.setName(cursor.getString(cursor.getColumnIndex("name")));
                contact.setAge(cursor.getString(cursor.getColumnIndex("age")));
                contact.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                contact.setImage(cursor.getBlob(cursor.getColumnIndex("poto")));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;

    }
}