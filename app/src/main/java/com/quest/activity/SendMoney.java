package com.quest.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.quest.R;
import com.quest.helper.URL;
import com.quest.helper.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendMoney extends AppCompatActivity {

    Toolbar toolbar;
    ProgressDialog dialog;
    TextView name, mobile,balanceAmt;
    String userid;
    Button btnPay;
    EditText amount;
    SharedPreferences SM;
    String userID;
    String SenderUserid;
    AdView mAdView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar);
        setTitle("SEND MONEY");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SM = getSharedPreferences("Report", MODE_PRIVATE);

        userID = SM.getString("user_id", "");
        final String MobileNo = SM.getString("user_contact", "");
          Log.e("Mobileno",MobileNo);


        //user_contact


        Intent intent = getIntent();
        final String number = intent.getStringExtra("mobileNumber");
        final int balance = intent.getIntExtra("balance", 0);

        dialog = new ProgressDialog(SendMoney.this);
        dialog.setTitle("Please wait...");
        dialog.setCancelable(false);

        name = (TextView) findViewById(R.id.name);
        mobile = (TextView) findViewById(R.id.mobileNo);
        balanceAmt = (TextView) findViewById(R.id.balanceAmt);
        btnPay = (Button) findViewById(R.id.pay);
        amount = (EditText) findViewById(R.id.edtAmount);
        mobile.setText(number);

        balanceAmt.setText("Payable balance " + balance+"");

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if(amount.getText().toString().isEmpty()){
                    Toast.makeText(SendMoney.this,
                            "Please Enter Amount" , Toast.LENGTH_SHORT).show();

                }else{

                    moneydeduct(amount.getText().toString(), balance, userID, number, MobileNo);


                }







            }
        });



        MobileAds.initialize(this,getString(R.string.banner_home_APPID) );
        mAdView = (AdView)findViewById(R.id.adView_forsend);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.e("ADS", "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e("ADS", "onAdFailedToLoad"+i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.e("ADS", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.e("ADS", "onAdOpened");
             //   googleClick();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e("ADS", "onAdLoaded");

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.e("ADS", "onAdClicked");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.e("ADS", "onAdImpression");
            }
        });




        checkuser(number);






    }

   /* private void googleClick() {

        String advonClick = appconfig.googleAddClick+"?user_id="+userID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, advonClick, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String status =  response.getString("status");

                    if (status.equals("ok") ) {

                        Log.e("Click123 ", "success123");



                    }else {
                        String error = response.getString("error");
                        Log.e("error",error );
                        dialog.dismiss();
                        Log.e("Click ", "unSuccess");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: " , error.getMessage());
*//*
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
*//*
                // hide the progress dialog
                dialog.dismiss();

            }
        });

        RequestQueue q = Volley.newRequestQueue(this);
        q.add(jsonObjectRequest);


    }*/


    private void moneydeduct(final String send, final int balance, final String userID, final String number, final String mobileNo) {



        dialog.show();

/*

        StringRequest strReq = new StringRequest(Request.Method.POST,
                appconfig.moneydeduct, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject job = new JSONObject(response);

                    String status = job.getString("status");
                    Log.e("status", status);

                    if (status.equals("success") ) {

                        String msg = job.getString("msg");

                        Log.e("msg", msg);

                        moneysend(SenderUserid,send, mobileNo);

                      //  dialog.dismiss();

                    }

                    else{

                        String msg = job.getString("msg");
//
                        Toast.makeText(SendMoney.this,msg, Toast.LENGTH_LONG).show();

                        String errorMsg = job.getString("error");
                        Log.e("Json Error for register",msg);
                        dialog.dismiss();

//                    }

                    }
                } catch (JSONException e) {
                    dialog.dismiss();

                    e.printStackTrace();

                    Log.e("Json Error", e.getMessage());
                }
            }
        }, new Response.ErrorListener()

        {

            @Override
            public void onErrorResponse (VolleyError error){
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(jsonError);
                        String status =  obj.getString("error");

                        Log.e("error", status);

                        Toast.makeText(SendMoney.this, status, Toast.LENGTH_SHORT).show();



                    } catch (JSONException e) {
                        e.printStackTrace();



                    }}
                dialog.dismiss();
*/
/*

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
*//*


            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_id",userID);
                params.put("amount", send+"");
                params.put("total", balance+"");
                params.put("receiver", number);
                Log.e("param", params+"");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(SendMoney.this);
        requestQueue.add(strReq);
*/


        VolleyMultipartRequest volleyMultipartRequest1 = new VolleyMultipartRequest(Request.Method.POST, URL.moneydeduct,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        String resultResponse = new String(response.data);
                        Log.e("resultResponse", resultResponse);

                        try {

                            JSONObject job = new JSONObject(resultResponse);


                            String status = job.getString("status");
                            Log.e("status", status);

                            if (status.equals("success") ) {

                                String msg = job.getString("msg");

                                Log.e("msgsuccess", msg);

                                moneysend(SenderUserid,send, mobileNo);

                                //  dialog.dismiss();

                            }

                            else{

                                String msg = job.getString("msg");
//
                                Toast.makeText(SendMoney.this,msg, Toast.LENGTH_LONG).show();

                                String errorMsg = job.getString("error");
                                Log.e("Json Error for register",msg);
                                dialog.dismiss();

//                    }

                            }
                            dialog.dismiss();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error Json", e.getMessage());
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(jsonError);
                                String status =  obj.getString("message");

                                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

                                Log.e("error ", status);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }



                        dialog.dismiss();
                    }


                })

        {




            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_id",userID);
                params.put("amount", send+"");
                params.put("total", balance+"");
                params.put("receiver", number);
                Log.e("param", params+"");
                return params;
            }




        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        volleyMultipartRequest1.setRetryPolicy(retryPolicy);

        Volley.newRequestQueue(SendMoney.this).add(volleyMultipartRequest1);



    }

    private void moneysend(final String senderUserid, final String send, final String mobileNo) {


     //   dialog.show();


/*

        StringRequest strReq = new StringRequest(Request.Method.POST,
                appconfig.moneysend, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject job = new JSONObject(response);

                    String status = job.getString("status");


                    if (status.equals("success") ) {

                        String msg = job.getString("msg");

                        Toast.makeText(SendMoney.this,msg, Toast.LENGTH_LONG).show();


                            finish();

                        dialog.dismiss();


                    }

                    else{

                        String msg = job.getString("msg");
//
                        Toast.makeText(SendMoney.this,msg, Toast.LENGTH_LONG).show();

                        String errorMsg = job.getString("error");
                        Log.e("Json Error for register",errorMsg);
                        dialog.dismiss();

//                    }

                    }
                } catch (JSONException e) {
                    dialog.dismiss();

                    e.printStackTrace();

                    Log.e("Json Error", e.getMessage());
                }
            }
        }, new Response.ErrorListener()

        {

            @Override
            public void onErrorResponse (VolleyError error){
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(jsonError);
                        String status =  obj.getString("error");

                        Log.e("error", status);

                        Toast.makeText(SendMoney.this, status, Toast.LENGTH_SHORT).show();



                    } catch (JSONException e) {
                        e.printStackTrace();



                    }}
                dialog.dismiss();
*/
/*

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
*//*


            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_id", senderUserid);
                params.put("amount", send);
                params.put("receiver", mobileNo);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(SendMoney.this);
        requestQueue.add(strReq);
*/



        VolleyMultipartRequest volleyMultipartRequest1 = new VolleyMultipartRequest(Request.Method.POST, URL.moneysend,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        String resultResponse = new String(response.data);
                        Log.e("resultResponse", resultResponse);

                        try {

                            JSONObject job = new JSONObject(resultResponse);

                            String status = job.getString("status");


                            if (status.equals("success") ) {

                                String msg = job.getString("msg");
                                Log.e("moneysuccess",msg);

                                Toast.makeText(SendMoney.this,msg, Toast.LENGTH_LONG).show();

                                dialog.dismiss();

                                startActivity(new Intent(SendMoney.this,Drawer_Activity.class));
                                finish();


                            }

                            else{

                                String msg = job.getString("msg");
//
                                Toast.makeText(SendMoney.this,msg, Toast.LENGTH_LONG).show();

                                String errorMsg = job.getString("error");
                                Log.e("Json Error for register",errorMsg);
                                dialog.dismiss();

//                    }

                            }
                        } catch (JSONException e) {
                            dialog.dismiss();

                            e.printStackTrace();

                            Log.e("Json Error", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener()

        {

            @Override
            public void onErrorResponse (VolleyError error){
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(jsonError);
                        String status =  obj.getString("error");

                        Log.e("error", status);

                        Toast.makeText(SendMoney.this, status, Toast.LENGTH_SHORT).show();



                    } catch (JSONException e) {
                        e.printStackTrace();



                    }}
                dialog.dismiss();
/*

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
*/

            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_id", senderUserid);
                params.put("amount", send);
                params.put("receiver", mobileNo);

                return params;
            }



        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        volleyMultipartRequest1.setRetryPolicy(retryPolicy);

        Volley.newRequestQueue(SendMoney.this).add(volleyMultipartRequest1);



    }

   /* private void checkuser(final String number) {

        String getBal = URL.checkuser+number;



        dialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getBal, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String status =  response.getString("status");

                    if (status.equals("success") ) {


                        userid =  response.getString("userid");
                        String firstname =  response.getString("firstname");
                        String lastname =  response.getString("lastname");

                        if(firstname.equals("null")){


                            final AlertDialog.Builder alertbox = new AlertDialog.Builder(SendMoney.this);

                            alertbox.setTitle("Mobile Number "+ number+ " does not exist!");
                            alertbox.setCancelable(false);

                            alertbox.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {

                                            arg0.dismiss();
                                            finish();

                                        }
                                    });

                            alertbox.show();


                        }else {


                            name.setText(firstname + " " + lastname);
                            SenderUserid =  response.getString("userid");

                        }

                        dialog.dismiss();

                    }
                    else {
                        String msg =  response.getString("msg");
                        Toast.makeText(SendMoney.this,
                                msg , Toast.LENGTH_SHORT).show();

                        String error = response.getString("error");
                        Log.e("error",error );
                        dialog.dismiss();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: " , error.getMessage());
             *//*   Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
             *//*   // hide the progress dialog
                dialog.dismiss();

            }
        });



        RequestQueue q = Volley.newRequestQueue(SendMoney.this);
        q.add(jsonObjectRequest);







    }*/


    private void checkuser(final String number) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.checkuser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String status =  jsonObject.getString("status");

                            if (status.equals("success") ) {


                                userid =  jsonObject.getString("userid");
                                String firstname =  jsonObject.getString("firstname");
                                String lastname =  jsonObject.getString("lastname");

                                if(firstname.equals("null")){


                                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(SendMoney.this);

                                    alertbox.setTitle("Mobile Number "+ number+ " does not exist!");
                                    alertbox.setCancelable(false);

                                    alertbox.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface arg0,
                                                                    int arg1) {

                                                    arg0.dismiss();
                                                    finish();

                                                }
                                            });

                                    alertbox.show();


                                }
                                else if (userid.equals(userID)){
                                    Log.e("userid,userID",userid+","+userID);

                                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(SendMoney.this);

                                    alertbox.setTitle("You can't send money to yourself!");
                                    alertbox.setCancelable(false);

                                    alertbox.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface arg0,
                                                                    int arg1) {

                                                    arg0.dismiss();
                                                    finish();

                                                }
                                            });

                                    alertbox.show();

                                }
                                else {


                                    name.setText(firstname + " " + lastname);
                                    SenderUserid =  jsonObject.getString("userid");

                                }

                                dialog.dismiss();

                            }
                            else {
                                String msg =  jsonObject.getString("msg");
                                Toast.makeText(SendMoney.this,
                                        msg , Toast.LENGTH_SHORT).show();

                                String error = jsonObject.getString("error");
                                Log.e("error",error );
                                dialog.dismiss();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
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

             //   params.put("Content-Type", "application/json; charset=utf-8");
                params.put("contact",number);
                Log.e("checkuserparams",params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
