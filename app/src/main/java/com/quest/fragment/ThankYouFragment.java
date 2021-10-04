package com.quest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.quest.helper.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ThankYouFragment extends Fragment implements View.OnClickListener {
    FragmentManager fragmentManager;
    Button button_play,button_gotowallet;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
     String subcategory_id,user_email;
     TextView txt_correct,txt_wrong,txt_msg;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_thank_you, container, false);
        getActivity().setTitle("Thank You");
        fragmentManager=getActivity().getSupportFragmentManager();
        button_play=(Button)view.findViewById(R.id.playaagain);
        button_gotowallet=(Button)view.findViewById(R.id.walletgo);
        Drawer_Activity.toolbar.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ImageView contact_back=(ImageView)view.findViewById(R.id.contact_back);
        txt_correct=(TextView) view.findViewById(R.id.correct_ans);
        txt_wrong=(TextView) view.findViewById(R.id.wrong_ans);
        txt_msg=(TextView)view.findViewById(R.id.txt_msg);

        sharedPreferences=getActivity().getSharedPreferences("Report",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        subcategory_id=sharedPreferences.getString("subcategory_id","");
        user_email=sharedPreferences.getString("user_email","");

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FetchAnswer();

        contact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("PLAYBUTTON","CONTACT");
                editor.commit();
                DeleteAnswer();
            }
        });

        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("PLAYBUTTON","PLAY");
                editor.commit();
                DeleteAnswer();
            }
        });

        button_gotowallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("PLAYBUTTON","WALLET");
                editor.commit();
                DeleteAnswer();
            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {

     fragmentManager.beginTransaction().replace(R.id.frm_drawer,new QuestionaryFragment()).commit();
    }
    private void FetchAnswer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.FETCHANSWERCOUNTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response",response);
                           progressDialog.dismiss();
                            JSONObject jobj = new JSONObject(response);

                            String total_answer = jobj.getString("total_answer");
                            String Right_answer = jobj.getString("Right_answer");
                            String Wrong_answer = jobj.getString("Wrong_answer");

                            Log.e("totaltotal_answer",total_answer);

                            if (total_answer.equals(Right_answer)){

                                txt_correct.setText(Right_answer+"/"+total_answer);
                                txt_wrong.setText(Wrong_answer +"/"+total_answer);
                                txt_msg.setText("Congratulations! Your name will be added to the draw for the winnings for the day");
                            }
                            else {

                                txt_correct.setText(Right_answer+"/"+total_answer);
                                txt_wrong.setText(Wrong_answer+"/"+total_answer);
                                txt_msg.setText("You Tried Well! Butter luck next time");
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

              /*  answer=sharedPreferences.getString("option","");
                Log.e("answer",answer);
*/

                Map<String, String> params = new HashMap<>();

                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("scat_id",subcategory_id);

                params.put("user_email",user_email);

                Log.e("answer_fech", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void DeleteAnswer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.DELETEANSWER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response",response);
                            progressDialog.dismiss();
                            JSONObject jobj = new JSONObject(response);

                            String status = jobj.getString("result");

                            Log.e("totaltotal_answer",status);

                            if (status.equals("success")){
                                editor.remove("subcategory_id");
                                editor.putString("subcategory_id","");
                                editor.commit();
                                if (sharedPreferences.getString("PLAYBUTTON","").equals("CONTACT")){
                                    fragmentManager.beginTransaction().replace(R.id.frm_drawer,new AdvertisementFragment()).commit();
                                }
                                else if (sharedPreferences.getString("PLAYBUTTON","").equals("PLAY")){
                                    fragmentManager.beginTransaction().replace(R.id.frm_drawer,new QCategoryFragment()).commit();
                                }
                                else if (sharedPreferences.getString("PLAYBUTTON","").equals("WALLET")){
                                    fragmentManager.beginTransaction().replace(R.id.frm_drawer,new myWallet()).commit();
                                }
                                else {
                                    fragmentManager.beginTransaction().replace(R.id.frm_drawer,new AdvertisementFragment()).commit();
                                }
                            }
                            else {

                                Toast.makeText(getActivity(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
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

              /*  answer=sharedPreferences.getString("option","");
                Log.e("answer",answer);
*/

                Map<String, String> params = new HashMap<>();

                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("sub_cat_id",subcategory_id);

                params.put("user_email",user_email);

                Log.e("answer_fech", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
