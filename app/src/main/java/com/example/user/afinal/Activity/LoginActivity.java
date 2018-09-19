package com.example.user.afinal.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;

import android.database.Cursor;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.afinal.DATABASE.DBAdapter;
import com.example.user.afinal.DATABASE.SQLiteHelper;
import com.example.user.afinal.Adapter.SQLiteListAdapter;
import com.example.user.afinal.R;
import com.example.user.afinal.Utility.MyApplication;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends Activity  {


    // private UserLoginTask mAuthTask = null;

     AutoCompleteTextView username;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private  Button submit;
    private TextView register;
    private View mLoginformview;
    TextView login;
    ImageView imggooglesignin;
    int RC_SIGN_IN =1000;
    GoogleApiClient mGoogleApiClient;
    ProgressDialog mProgressDialog;
    TextView txtgooglesignin;


    SQLiteHelper SQLITEHELPER;
    SQLiteDatabase SQLITEDATABASE;
    Cursor cursor;
    SQLiteListAdapter ListAdapter;
    ArrayList<String> roww = new ArrayList<String>();

    final String sql_userlogin="SELECT * FROM imgTable WHERE name=?";
    final String Sql_validlogin="SELECT * FROM demoTable WHERE name=? AND password=?";
    final String SQl_validlogin3 ="SELECT * FROM imgTable WHERE name=? AND password=?";

    DBAdapter adb = new DBAdapter(LoginActivity.this);
    GoogleSignInOptions gso;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_login);
        init();
        initcallback();



         }


    @Override
    public void onStart() {

        if (MyApplication.getInstance().isLogout()&&MyApplication.getInstance().getLogintype().equalsIgnoreCase("googlelogin")){
            mGoogleApiClient = MyApplication.getInstance().getmGoogleApiClient();
            mGoogleApiClient.clearDefaultAccountAndReconnect();
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient=null;
            }

        }
       gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        if (mGoogleApiClient==null){
            buildGoogleApiClient();
        }
        super.onStart();
         //to do one time login
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {

            Log.d("TAG", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            try {
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });



            }catch (Exception ex){
                Log.e("onstart Login",ex.getMessage().toString());
            }

        }
    }

    public void  init(){
        try {
            login = (TextView)findViewById(R.id.heading);
            username =(AutoCompleteTextView)findViewById(R.id.usname);

            mPasswordView =(EditText)findViewById(R.id.password);
            register =(TextView)findViewById(R.id.textView2);
            TextInputLayout layout1 = (TextInputLayout) findViewById(R.id.txt1);
            layout1.setHintAnimationEnabled(false);
            TextInputLayout layout2 = (TextInputLayout) findViewById(R.id.txt2);
            layout2.setHintAnimationEnabled(false);
            mLoginformview =findViewById(R.id.login_form);
            submit = (Button) findViewById(R.id.email_sign_in_button);
            imggooglesignin=(ImageView)findViewById(R.id.imggooglesignin);
            txtgooglesignin=(TextView)findViewById(R.id.txtgooglesignin);
            adb.open();
            final String my_var;



            SQLITEHELPER = new SQLiteHelper(this);
            SQLITEDATABASE = SQLITEHELPER.getWritableDatabase();
            roww = new ArrayList<String>();

            dropdw();
            final ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, roww);
            username.setThreshold(1);
            username.setAdapter(adp);
            username.setDropDownBackgroundDrawable(new ColorDrawable(getBaseContext().getResources().getColor(R.color.color10blue)));
            mPasswordView.setText("bsarun");


        }catch (Exception e){
            Log.e("login activity init",e.getMessage().toString());
        }

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()){
            Toast.makeText(LoginActivity.this, "GoogleApiclient is connected in buildGoogle", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }

    }
    public  void initcallback(){
        try {
            imggooglesignin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                   //Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                    MyApplication.getInstance().setLogout(false);
                }
            });

            txtgooglesignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                    if (mGoogleApiClient.isConnected()) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                       /* Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        // updateUI(false);
                                        mGoogleApiClient.disconnect();
                                        mGoogleApiClient.connect();

                                    }
                                });*/
                    }

                }
            });
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                    if (id==R.id.login || id==EditorInfo.IME_NULL){
                        if( attemptLogin()==true)  {
                            transist();
                            return true;
                        }
                    }
                    return false;
                }
            });

            submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Animation myAnim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.simplebounce);
                    submit.startAnimation(myAnim);
                    if( attemptLogin()==true)  {
                        transist();

                    }
                }
            });


            register.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Animation myAnim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.simplebounce);
                    register.startAnimation(myAnim);
                    Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slideright,R.anim.slidrleft);

                }
            });


        }catch (Exception e){
            Log.e("login initcallback",e.getMessage().toString());
        }

    }


    private void handleSignInResult(GoogleSignInResult result)  {
        try {
            Log.d("TAG", "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfolly, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                if (acct.getDisplayName()!=null) {
                    txtgooglesignin.setText(acct.getDisplayName() + "," + acct.getEmail() + "," + acct.getPhotoUrl());

              if (!MyApplication.getInstance().isLogout())  {
                  if(acct.getPhotoUrl() != null)
                      new LoadProfileImage().execute(acct.getPhotoUrl().toString());
                  if (MyApplication.getInstance().getLoginprofilepic()!=null)
                      adb.insertlogindata("googlelogin",acct.getDisplayName(),MyApplication.getInstance().getLoginprofilepic());
                  else
                      adb.insertlogindata("googlelogin",acct.getDisplayName(),null);
                  MyApplication.getInstance().setmGoogleApiClient(mGoogleApiClient);
                  Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                  MyApplication.getInstance().setLoginuser(acct.getDisplayName());
                  MyApplication.getInstance().setLoginemail(acct.getEmail());
                  MyApplication.getInstance().setLogintype("googlelogin");
                  startActivity(i);
                  overridePendingTransition(R.anim.slideright, R.anim.slidrleft);
              }


                }

                 if(acct.getPhotoUrl() != null)
                   new LoadProfileImage().execute(acct.getPhotoUrl().toString());

            } else {
                // Signed out, show unauthenticated UI.

                Toast.makeText(getApplicationContext(),"  handleSignInResult not  found",Toast.LENGTH_SHORT).show();
            }


        }catch (Exception ex){
            Log.e("handleSignResult ",ex.getMessage().toString());
        }


    }



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getApplicationContext());
            mProgressDialog.setMessage("loading ");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }

    }

    private void transist(){
        try {
             String user = username.getText().toString();
             String password = mPasswordView.getText().toString();

             boolean validUser = adb.Login(user,password);
             if(validUser == true) {

                 Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                 adb.insertlogindata("registereduser",user,MyApplication.getInstance().getLoginprofilepic());
                 MyApplication.getInstance().setLoginuser(user);
                 MyApplication.getInstance().setLogintype("Registereduser");
                 startActivity(i);
                 overridePendingTransition(R.anim.slideright, R.anim.slidrleft);
                 final Animation myAnim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.bounce);
                 submit.startAnimation(myAnim);
                 ClearEditTextAfterDoneTask();
             }
             else {Toast.makeText(getApplicationContext(), "Invalid login", Toast.LENGTH_SHORT).show();
             }

         }catch (Exception e){
             Log.e(" login transist",e.getMessage().toString());
         }

    }

    private  boolean attemptLogin() {

         try {
             username.setError(null);
             mPasswordView.setError(null);
             String user = username.getText().toString();
             String password = mPasswordView.getText().toString();
             boolean cancel = false;
             View focusView = null;
             // Check for a valid email address.
             if (TextUtils.isEmpty(user)) {
                 username.setError(getString(R.string.error_field_required));
                 focusView = username;
                 cancel = true;
             } else if (!isEmailValid(user)) {
                 username.setError(getString(R.string.error_invalid_username));
                 focusView = username;
                 cancel = true;
             } else if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
                 mPasswordView.setError(getString(R.string.error_invalid_password));
                 focusView = mPasswordView;
                 cancel = true;
             }else{return true;
             }return false;

         }catch (Exception e){
             Log.e("login attemptLogin",e.getMessage().toString());
             return  false;
         }

    }

    private boolean isEmailValid(String user) {
        //TODO: Replace this with your own logic
        Cursor cs = SQLITEDATABASE.rawQuery(sql_userlogin, new String[]{user});
        if (cs.getCount() > 0) {
            Toast.makeText(LoginActivity.this, "valid userid"  , Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    private void dropdw() {
        SQLITEDATABASE = SQLITEHELPER.getReadableDatabase();
        SQLITEDATABASE.isOpen();
        cursor = SQLITEDATABASE.rawQuery("SELECT name FROM imgTable", null);
        while (cursor.moveToNext()) {
            roww.add(cursor.getString(cursor.getColumnIndex("name")));
       }cursor.close();
    }

    private boolean vaildLogin(String user,String password){
        cursor = SQLITEDATABASE.rawQuery(Sql_validlogin, new String[]{user,password});
        if (cursor.getCount()>0) {
            Toast.makeText(LoginActivity.this, "valid Login" , Toast.LENGTH_SHORT).show();
            return true;
        }cursor.close();
        return false;
    }
    private  boolean validLogin3(String user,String password){
        SQLITEDATABASE=SQLITEHELPER.getReadableDatabase();
        SQLITEDATABASE.isOpen();
        Cursor cs = SQLITEDATABASE.rawQuery(SQl_validlogin3,new String[]{user,password});
        if (cs.getCount()>0){
            Toast.makeText(LoginActivity.this, "valid Login" , Toast.LENGTH_SHORT).show();
            return true;
        }else {
            return false;
        }
    }
    public void ClearEditTextAfterDoneTask() {
        username.getText().clear();
        mPasswordView.getText().clear();
        mPasswordView.clearFocus();


    }
    private static final int TIME_INTERVAL = 3000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            finish();
            super.onBackPressed();
            }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        Button button = (Button)findViewById(R.id.button);
        login = (TextView)findViewById(R.id.heading);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.4, 20);
                myAnim.setInterpolator(interpolator);
                login.startAnimation(myAnim);

            }
        });
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        login.startAnimation(myAnim);
       // button.startAnimation(myAnim);

        super.onResume();
    }



    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                MyApplication.getInstance().setLoginprofilepic(profileImage(mIcon11));
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) {



                Bitmap resized = Bitmap.createScaledBitmap(result,200,200, true);
               // MyApplication.getInstance().setLoginprofilepic(profileImage(resized));


            }
        }
    }
    private byte[] profileImage(Bitmap b){
        try {ByteArrayOutputStream bos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 0, bos);
            return bos.toByteArray();

        }catch (Exception e){Log.e("register profileImage",e.getMessage().toString());
            return null;}

    }
}

