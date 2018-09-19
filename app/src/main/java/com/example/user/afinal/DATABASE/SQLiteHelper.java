package com.example.user.afinal.DATABASE;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by USER on 10/21/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME = "DemoDataBase";

    //old table
    public static final String TABLE_NAME = "demoTable";
    public static final String KEY_ID = "_id";
    public static final String KEY_Name = "name";
    public static final String KEY_Age = "age";
    public static final String KEY_Address = "address";
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_Name + " TEXT, " + KEY_Age + " TEXT, " + KEY_Address + " TEXT, " + KEY_PASSWORD + " TEXT)";

    //alter table

    public  static  final String KEY_PASSWORD = "password";
    private static final String DATABASE_ALTER_TEAM_TO_V2 = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + KEY_PASSWORD + " TEXT ";

    //1st new table
    public static final String TABLE_NAME2 = "colTable2";
    public static final String COL_ID = "_id";
    public static final String COL_Name = "name";
    public static final String COL_Age = "age";
    public static final String COL_Address = "address";
    public static final String COL_Password = "password";
    private static final String   CREATE_TABLE2 = "CREATE TABLE " + TABLE_NAME2 + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_Name + " TEXT, " + COL_Age + " TEXT, " + COL_Address + " TEXT, " + COL_Password + " TEXT)";

    //2st new table
    public static final String TABLE_NAME3 = "imgTable";
    public static final String COLM_ID = "_id";
    public static final String COLM_Name = "name";
    public static final String COLM_Age = "age";
    public static final String COLM_Address = "address";
    public static final String COLM_Password = "password";
    public static final String COLM_POTO = "poto";
    private static final String   CREATE_TABLE3 = "CREATE TABLE " + TABLE_NAME3 + "(" + COLM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLM_Name + " TEXT, " + COLM_Age + " TEXT, " + COLM_Address + " TEXT, " + COLM_Password + " TEXT, " + COLM_POTO + " BLOB" + ")";


    static final String loginTable="Logindetails";

    static final String collogintype="logintype";
    static final String colusername="username";
    static final String colprofileppic="profilepic";


    private static final String STRING_CREATE = "CREATE TABLE " + loginTable + "(" + collogintype + " TEXT," + colusername + " TEXT, " + colprofileppic + " BLOB" +  ")";
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null,7);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        try {
            database.execSQL(CREATE_TABLE);
            database.execSQL(CREATE_TABLE2);
            database.execSQL(CREATE_TABLE3);
            database.execSQL(STRING_CREATE);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(STRING_CREATE);
          //  db.execSQL(CREATE_TABLE3);
      /*  if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
            db.execSQL(DATABASE_ALTER_TEAM_TO_V2);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
            onCreate(db);

        }*/


    }


}
