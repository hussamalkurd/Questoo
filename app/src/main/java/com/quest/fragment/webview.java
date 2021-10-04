package com.quest.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quest.R;
import com.quest.activity.Drawer_Activity;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class webview extends AppCompatActivity {

    private WebView web;
    ProgressDialog dialog;
    SharedPreferences SM;
    String userID, amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        web=(WebView)findViewById(R.id.webView);

        dialog = new ProgressDialog(webview.this);
        dialog.setTitle("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        SM = getSharedPreferences("Report", MODE_PRIVATE);
        String MobileNo = SM.getString("user_contact", "");
        Log.e("mobile",MobileNo);
        userID = SM.getString("user_id", "");
        Log.e("mobile",MobileNo);
        amt = SM.getString("amt", "");


        String url = "http://payfast.mywindowsapp.in/order.aspx?amount="+amt+"&mobile="+MobileNo;

        web.postUrl(url, null );
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                view.loadUrl(url);

                Log.e("url", url);
                return true;
            }



            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                   Log.e("webwiewresponse",url);
                if (url.contains("s=f")) {

                    Log.e("webView", "fail");

                    Intent intent = new Intent(webview.this, Drawer_Activity.class);

                    startActivity(intent);


                }else if(url.contains("s=s")){


                    addMoney(amt, userID);
                    Log.e("webView", "success");
                }

                dialog.dismiss();
               Log.e("url", url);

            }
        });






    }

   /* private void addMoney(final String amt, final String userID) {


        dialog.show();

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
               "http://www.merneadan.co.za/QuestoAdmin/process.php?action=wallet_moneyadd", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {


                    JSONObject job = new JSONObject(response);

                    String status = job.getString("status");


                    if (status.equals("success") ) {

                        dialog.dismiss();

                        String msg = job.getString("msg");

                        Toast.makeText(webview.this,msg, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(webview.this, Drawer_Activity.class);
                        startActivity(intent);
                        finish();


                    }

                    else{

                        String msg = job.getString("msg");
//
                        Toast.makeText(webview.this,msg, Toast.LENGTH_LONG).show();

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

                        Toast.makeText(webview.this, status, Toast.LENGTH_SHORT).show();

                    }
                    catch (JSONException e) {
                        e.printStackTrace();

                    }}
                dialog.dismiss();
*//*

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
*//*

            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_id", userID);
                params.put("amount", amt);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(webview.this);
        requestQueue.add(strReq);
    }*/

    private void addMoney(final String amt, final String userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://merneadan.co.za/QuestoAdmin/process.php?action=wallet_moneyadd",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject job = new JSONObject(response);

                            String status = job.getString("status");


                            if (status.equals("success") ) {

                                dialog.dismiss();

                                String msg = job.getString("msg");

                                Toast.makeText(webview.this,msg, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(webview.this, Drawer_Activity.class);
                                startActivity(intent);
                                finish();


                            }

                            else{

                                String msg = job.getString("msg");
//
                                Toast.makeText(webview.this,msg, Toast.LENGTH_LONG).show();

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
                }, new Response.ErrorListener(){

        @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                // params.put("Content-Type", "application/json; charset=utf-8");
                params.put("user_id",userID);
                params.put("amount", amt);

                Log.e("addmoney_params", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
