package com.quest.fragment;

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
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quest.R;
import com.quest.activity.Drawer_Activity;
import com.quest.helper.OnBackPressedListener;
import com.quest.helper.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment implements OnBackPressedListener {

    EditText editText_name,editText_contact;
    Button button_next;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    String  name, email, contact, cardno, card_expiry, card_cvv,fetched_email,setname,setcontact;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Profile dashboard");
        sharedPreferences=getActivity().getSharedPreferences("Report", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        email=sharedPreferences.getString("user_email","");
        Log.e("email",email);

        Drawer_Activity.toolbar.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ImageView contact_back=(ImageView)view.findViewById(R.id.contact_back);
        contact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new AdvertisementFragment()).commit();
            }
        });

        editText_name=(EditText)view.findViewById(R.id.pr_name);
        editText_contact=(EditText)view.findViewById(R.id.pr_contact);

        fetchDetail();





        fragmentManager=getActivity().getSupportFragmentManager();

        button_next=(Button)view.findViewById(R.id.pr_nextBtn);

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getActivity(),"comming Soon",Toast.LENGTH_LONG).show();


                setname=editText_name.getText().toString();
                setcontact=editText_contact.getText().toString();

                editor.putString("name",name);
                Log.e("name",name);

                editor.putString("contact",contact);
                Log.e("contact",contact);

                editor.putString("email",fetched_email);
                Log.e("fetched_email",fetched_email);

                editor.putString("cardno",cardno);
                Log.e("cardno",cardno);

                editor.putString("card_expiry",card_expiry);
                Log.e("card_expiry",card_expiry);

                editor.putString("card_cvv",card_cvv);
                Log.e("card_cvv",card_cvv);

                editor.putString("setname",setname);
                Log.e("setname",setname);

                editor.putString("setcontact",setcontact);
                Log.e("setcontact",setcontact);


                editor.commit();


                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new AccountFragment()).commit();

            }
        });

        return view;
    }
    private void fetchDetail() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.FETCH_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.e("fetch_response",response);

                            JSONObject jobj = new JSONObject(response);

                            String status = jobj.getString("result");

                            name=jobj.getString("name");
                            Log.e("name",name);

                            fetched_email=jobj.getString("email");
                            Log.e("email",email);

                            contact=jobj.getString("contact");
                            Log.e("contact",contact);

                            cardno=jobj.getString("cardno");
                            Log.e("cardno",cardno);

                            card_expiry=jobj.getString("card_expiry");
                            Log.e("card_expiry",card_expiry);

                            card_cvv=jobj.getString("card_cvv");
                            Log.e("card_cvv",card_cvv);

                            editText_name.setText(name);
                            editText_contact.setText(contact);


                            if (status.equals("successfully")) {

                                Log.e("successs","success");




                            }

                            else
                            {


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


                params.put("email",email);
                Log.e("email",email);

                Log.e("signup_params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.frm_drawer,new AdvertisementFragment()).commit();
    }
}
