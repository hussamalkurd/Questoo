package com.quest.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.answers.Answers;
import com.quest.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends Activity{
    String username;
   TextView txt_bottom;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Answers());
        setContentView(R.layout.splash);

        SharedPreferences preferences = getSharedPreferences("Report", MODE_PRIVATE);
        username = preferences.getString("user_email", "");
        Log.e("username", username);

        txt_bottom = (TextView) findViewById(R.id.textview);

        FETCHSLOGAN();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (username.equals("")) {

                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), Drawer_Activity.class));
                    finish();
                }
            }
        }, 3000);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.quest.activity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
    private void FETCHSLOGAN() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://www.merneadan.co.za/QuestoAdmin/process.php?action=fetch_slogan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response",response);

                            final JSONObject jobj = new JSONObject(response);

                            String id = jobj.getString("id");

                           String slogan=jobj.getString("slogan");
                                txt_bottom.setText(slogan);


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

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
