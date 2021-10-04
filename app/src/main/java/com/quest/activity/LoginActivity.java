package com.quest.activity;

import android.Manifest;

import com.facebook.FacebookSdk;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.quest.R;
import com.quest.fragment.InstructionFragment;
import com.quest.helper.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button button_login,button_facebook,button_google;
    EditText editText_email,editText_password;
    TextView textView_newUser,textView_forgetpassword;
    String username,password;
    String useremail,userpassword;
    String name;
    LoginButton loginButton;
    CallbackManager callbackManager;
    String FB_name,FB_email,FB_contact,FB_id,pemail,FB_image,FB_address;
    String id,first_name,email,contact,image,address,facebook_user_id,regId;
    ImageView imageView_hide,imageView_show;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    Dialog dialog3;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
    String personName,kk;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        //FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        //AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//permissions for marshmallow
        if(!hasPermissions(LoginActivity.this, PERMISSIONS)){
            ActivityCompat.requestPermissions((Activity) LoginActivity.this, PERMISSIONS, PERMISSION_ALL);
        }

        editText_email=(EditText)findViewById(R.id.lg_email);
        editText_password=(EditText)findViewById(R.id.lg_password);
        textView_newUser=(TextView)findViewById(R.id.signup);
        textView_forgetpassword=(TextView)findViewById(R.id.forgotpassword);

        button_login=(Button)findViewById(R.id.login_button);
        loginButton = (LoginButton) findViewById(R.id.fb_button);
        button_facebook=(Button)findViewById(R.id.fblogin);
        button_google=(Button)findViewById(R.id.googlelogin);

        imageView_hide=(ImageView)findViewById(R.id.lg_hide);
        imageView_show=(ImageView)findViewById(R.id.lg_show);

        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);


        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.quest", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                 kk= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
               // Toast.makeText(LoginActivity.this,kk,Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("log",".Exception FB.");
            Log.e("log","NameNotFoundException");
            Log.e("log",""+e.getMessage());
            Log.e("log",""+e.getStackTrace());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("log",".Exception FB.");
            Log.e("log","NoSuchAlgorithmException");
            Log.e("log",""+e.getMessage());
            Log.e("log",""+e.getStackTrace());
        }

        imageView_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView_hide.setVisibility(View.GONE);
                editText_password.setTransformationMethod(new PasswordTransformationMethod());
                imageView_show.setVisibility(View.VISIBLE);

            }
        });

        imageView_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView_show.setVisibility(View.GONE);
                imageView_hide.setVisibility(View.VISIBLE);
                editText_password.setTransformationMethod(null);

            }
        });

        textView_newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern p = Pattern.compile(regEx);

                if (editText_email.getText().toString().isEmpty()){
                    editText_email.setError("Required Field");
                    editText_email.requestFocus();
                }else if (!p.matcher(editText_email.getText().toString()).find()){
                    editText_email.setError("Invalid email");
                    editText_email.requestFocus();
                }
                else if (editText_password.getText().toString().isEmpty()){
                    editText_password.setError("Required Field");
                    editText_password.requestFocus();
                }
                else {
                    pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Please wait...");
                    pd.setCancelable(false);
                    pd.show();
                    login();
                }


            }
        });

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        try {

            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {


                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {

                                    setProfileToView(object);

                                }
                            });
                    Bundle parameters = new Bundle();

                    parameters.putString("fields", "id,name,email");

                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getApplicationContext(), "error to Login Facebook", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.toString();
        }

        button_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Comming Soon",Toast.LENGTH_LONG).show();
            }
        });

        //======================================Google login========================================
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
   public void shareImage(String kk) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_TITLE,"xyz");
        i.putExtra(Intent.EXTRA_TEXT,kk);
       // i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
      startActivity(Intent.createChooser(i, "Share Image"));
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());



            first_name = acct.getDisplayName();
            Log.e("personName",first_name);
            email = acct.getEmail();
            Log.e("emailll",email);

            signUp();

            Log.e(TAG, "Name: " + personName + ", email: " + email);



        } else {

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.btn_sign_out:
               // signOut();
                break;

            case R.id.btn_revoke_access:
              //  revokeAccess();
                break;
        }
    }

    /*@Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
*/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
          //  llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
           // llProfileLayout.setVisibility(View.GONE);
        }
    }



    private void setProfileToView(JSONObject jsonObject) {
        try {
            email=jsonObject.getString("email");
            Log.e("",email);

            first_name=jsonObject.getString("name");
             Log.e("first_name",first_name);

            FB_id=jsonObject.getString("id");
             Log.e("FB_id",FB_id);

           /* image=jsonObject.getString("image");
            Log.e("image",image);
*/
      signUp();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
          GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

//====================simple login===============================================================
    private void login() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response",response);
                            pd.dismiss();
                            final JSONObject jobj = new JSONObject(response);

                            String result = jobj.getString("result");

                            if (result.equals("successfully Login")) {


                                id = jobj.getString("id");

                                Log.e("id",id);

                                name = jobj.getString("name");
                                Log.e("name",name);

                                email = jobj.getString("email");
                                Log.e("email",email);

                                contact = jobj.getString("contact");
                                Log.e("contact",contact);


                                //  Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_SHORT).show();


                                SharedPreferences preferences = getSharedPreferences("Report", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putString("user_id", id);
                                Log.e("user_id", id);

                                editor.putString("user_name", name);
                                Log.e("name", name);

                                editor.putString("user_email", email);
                                Log.e("user_email", email);

                                editor.putString("user_contact", contact);
                                Log.e("contact", contact);

                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this,InstructionFragment.class);
                                startActivity(intent);

                            }
                            else
                                {
                                Toast.makeText(getApplicationContext(), "Incorrect Email ID or Password", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                username=editText_email.getText().toString();
                password=editText_password.getText().toString();

                Map<String, String> params = new HashMap<>();
                // params.put("Content-Type", "application/json; charset=utf-8");
                params.put("email",username);
                params.put("password",password);
                Log.e("login_params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    //====================facebook  login===============================================================


    private void signUp() {
        final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                             progressDialog.dismiss();
                            Log.e("sign_response",response);

                            JSONObject jobj = new JSONObject(response);

                            String result = jobj.getString("result");

                            if (result.equals("successfully")) {

                                id = jobj.getString("id");

                                Log.e("id",id);

                                name = jobj.getString("name");
                                Log.e("name",name);

                                email = jobj.getString("email");
                                Log.e("email",email);

                                contact = jobj.getString("contact");
                                Log.e("contact",contact);


                                //  Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_SHORT).show();


                                SharedPreferences preferences = getSharedPreferences("Report", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putString("user_id", id);
                                Log.e("user_id", id);

                                editor.putString("user_name", first_name);
                                Log.e("name", name);

                                editor.putString("user_email", email);
                                Log.e("user_email", email);

                                editor.putString("user_contact", contact);
                                Log.e("contact", contact);

                                editor.commit();


                                Intent intent = new Intent(LoginActivity.this,InstructionFragment.class);
                                startActivity(intent);


                            }
                            else if(result.equals("Email Already Exist")) {

                                Toast.makeText(getApplicationContext(), "Already Exist", Toast.LENGTH_SHORT).show();
                                SharedPreferences preferences =getSharedPreferences("Report",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user_email",email);
                                editor.commit();


                                Intent intent = new Intent(LoginActivity.this, Drawer_Activity.class);
                                startActivity(intent);
                                finish();


                            }
                            else
                            {

                                Toast.makeText(getApplicationContext(), "Invalid Details", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("name",first_name);
                //Log.e("name",name);

                params.put("email",email);
                //Log.e("email",email);

                params.put("contact","");
                //Log.e("contact",contact);

                params.put("password","");
                //Log.e("password",password);

                params.put("cardno","");
                //Log.e("cardno",cardnumber);

                params.put("card_expiry","");
                //Log.e("card_expiry",cardexpiry);

                params.put("card_cvv","");
                //Log.e("card_cvv",cardcvv);

                 Log.e("signup_params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

   // @SuppressLint("NewApi")
/*    class FacebookTask extends AsyncTask<String, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity .this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            try {
                String url = URL.REGISTER;
                HttpPost postrequest = new HttpPost(url);
                ArrayList<NameValuePair> postlist = new ArrayList<NameValuePair>();
                postlist.add(new BasicNameValuePair("user_name",first_name));
                postlist.add(new BasicNameValuePair("email",email));
                postlist.add(new BasicNameValuePair("contact",""));
                Log.e("contact","");

                postlist.add(new BasicNameValuePair("password",""));
                Log.e("password","");

                postlist.add(new BasicNameValuePair("cardno",""));
                Log.e("cardno","");

                postlist.add(new BasicNameValuePair("card_expiry",""));
                Log.e("card_expiry","");

                postlist.add(new BasicNameValuePair("card_cvv",""));
                Log.e("card_cvv","");

                Log.e("postlist", String.valueOf(postlist));

                postrequest.setEntity(new UrlEncodedFormEntity(postlist));
                HttpClient client = new DefaultHttpClient();
                HttpResponse resp = client.execute(postrequest);

                InputStream is = resp.getEntity().getContent();
                while (true) {
                    int i = is.read();
                    if (i == -1)
                        break;
                    data += (char) i;
                }
                is.close();
            } catch (Exception e) {
                data = e.toString();
            }
            return data;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pd.dismiss();

            try {
                JSONObject jobj = new JSONObject(result);
                Log.e("result",result);
                String status = jobj.getString("result");
                Log.e("result",result);

                if (status.equals("successfully")) {
                    id = jobj.getString("id");
                    SharedPreferences preferences =getSharedPreferences("Report",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_id",id);
                    editor.putString("user_name", first_name);
                    editor.putString("user_email", email);
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, Drawer_Activity.class);
                    startActivity(intent);
                    finish();


                } else if(status.equals("Email Already Exist")) {

                    Toast.makeText(getApplicationContext(), "Already Exist", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences =getSharedPreferences("Report",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_email",email);
                     editor.commit();


                    Intent intent = new Intent(LoginActivity.this, Drawer_Activity.class);
                    startActivity(intent);
                    finish();


                }
                else {


                    Toast.makeText(getApplicationContext(), "Already Exist", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences =getSharedPreferences("Report",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_email",email);
                     editor.commit();
                    Intent intent = new Intent(LoginActivity.this, Drawer_Activity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {

            }
        }
    }*/








}
