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


public class AccountFragment extends Fragment {
    EditText editText_cardno,editText_date,editText_cvv;
    Button button_account_detail;
    FragmentManager fragmentManager;
    //String id;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String cardno,expiry,cvv,setname,setcontact,cardnumber,cardexpiry,cardcvv,email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view=inflater.inflate(R.layout.fragment_account, container, false);

        Drawer_Activity.toolbar.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ImageView contact_back=(ImageView)view.findViewById(R.id.contact_back);
        contact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new ProfileFragment()).commit();
            }
        });

            fragmentManager=getActivity().getSupportFragmentManager();
            editText_cardno=(EditText)view.findViewById(R.id.pr_carddetail);
            editText_date=(EditText)view.findViewById(R.id.pr_cardexpiry);
            editText_cvv=(EditText)view.findViewById(R.id.pr_cvc);
            sharedPreferences=getActivity().getSharedPreferences("Report",Context.MODE_PRIVATE);


            email=sharedPreferences.getString("user_email","");

            cardno=sharedPreferences.getString("cardno","");
          //  Log.e("cardno",cardno);

            expiry=sharedPreferences.getString("card_expiry","");
          //  Log.e("expiry",expiry);

            cvv=sharedPreferences.getString("card_cvv","");
          //  Log.e("cvv",cvv);

            setname=sharedPreferences.getString("setname","");
           //   Log.e("setname",setname);

            setcontact=sharedPreferences.getString("setcontact","");
             // Log.e("setcontact",setcontact);

            editText_cardno.setText(cardno);
            editText_date.setText(expiry);
            editText_cvv.setText(cvv);


            button_account_detail=(Button)view.findViewById(R.id.save_account_detail);

            button_account_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UpdateDetails();

                }
            });


            return view;
    }

    private void UpdateDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.UPDATE_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("update_response",response);

                            JSONObject jobj = new JSONObject(response);

                            String result = jobj.getString("result");

                            if (result.equals("updated successfully")) {

                                Toast.makeText(getActivity(), "Update successfull", Toast.LENGTH_SHORT).show();
                                //id = jobj.getString("id");
                                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new QCategoryFragment()).commit();





                            }

                            else
                            {

                                Toast.makeText(getActivity(), "Invalid try", Toast.LENGTH_SHORT).show();

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

                cardnumber=editText_cardno.getText().toString();
                cardexpiry=editText_date.getText().toString();
                cardcvv=editText_cvv.getText().toString();

                Map<String, String> params = new HashMap<>();

                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("name",setname);


                params.put("email",email);


                params.put("contact",setcontact);



                params.put("cardno",cardnumber);


                params.put("card_expiry",cardexpiry);


                params.put("card_cvv",cardcvv);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
