package com.quest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CvvEditText;
import com.braintreepayments.cardform.view.ExpirationDateEditText;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.quest.R;
import com.quest.helper.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity{
    TextView textView_already_login;
    Button button_signup;
    EditText editText_name,editText_contact,editText_email;
           EditText editText_password;
    String id,name,contact,email,password,cardnumber,cardexpiry,cardcvv;
    ImageView imageView_hide,imageView_show;
    ProgressDialog progressDialog;
    private SupportedCardTypesView mSupportedCardTypesView;
    private static final CardType[] SUPPORTED_CARD_TYPES = { CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
            CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY };
    CardEditText editText_cardno;
    ExpirationDateEditText editText_expiry;
    CvvEditText editText_cvv;
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textView_already_login=(TextView)findViewById(R.id.already_login);
        button_signup=(Button)findViewById(R.id.userbtn_signup);

        editText_name=(EditText)findViewById(R.id.sg_name);
        editText_contact = (EditText)findViewById(R.id.sg_contact);
        editText_email=(EditText)findViewById(R.id.sg_email);
        editText_password=(EditText)findViewById(R.id.sg_password);
        editText_cardno=(CardEditText)findViewById(R.id.sg_carddetail);
        editText_expiry=(ExpirationDateEditText)findViewById(R.id.sg_cardexpiry);
        editText_cvv=(CvvEditText)findViewById(R.id.sg_cvc);
        mSupportedCardTypesView = findViewById(R.id.supported_card_types1);
        mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);


        imageView_hide=(ImageView)findViewById(R.id.hide);
        imageView_show=(ImageView)findViewById(R.id.show);


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

        editText_cardno.setOnCardTypeChangedListener(new CardEditText.OnCardTypeChangedListener() {
            @Override
            public void onCardTypeChanged(CardType cardType) {
               String card_type=cardType.toString();
                //  Toast.makeText(getActivity(),card_type, Toast.LENGTH_SHORT).show();
            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pattern p = Pattern.compile(regEx);
                if (editText_name.getText().toString().isEmpty()){
                    editText_name.setError("Required Field");
                    editText_name.requestFocus();
                }
                else  if (editText_contact.getText().toString().isEmpty()){
                    editText_contact.setError("Required Field");
                    editText_contact.requestFocus();
                }
                else  if (editText_email.getText().toString().isEmpty()){
                    editText_email.setError("Required Field");
                    editText_email.requestFocus();
                }
                else if (!p.matcher(editText_email.getText().toString()).find()){
                    editText_email.setError("Invalid email");
                    editText_email.requestFocus();
                }
                else  if (editText_password.getText().toString().isEmpty()){
                    editText_password.setError("Required Field");
                    editText_password.requestFocus();
                }
               /* else  if (editText_cardno.getText().toString().isEmpty()){
                    editText_cardno.setError("Required Field");
                    editText_cardno.requestFocus();
                }
                else  if (editText_expiry.getText().toString().isEmpty()){
                    editText_expiry.setError("Required Field");
                    editText_expiry.requestFocus();
                }
                else  if (editText_cvv.getText().toString().isEmpty()){
                    editText_cvv.setError("Required Field");
                    editText_cvv.requestFocus();
                }*/
                else {

                    progressDialog = new ProgressDialog(SignUpActivity.this);
                    progressDialog.setMessage(" Please Wiat...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();


                    signUp();
                }

            }
        });

        textView_already_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();

            }
        });
    }

    private void signUp() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.e("sign_response",response);
                           progressDialog.dismiss();
                            JSONObject jobj = new JSONObject(response);

                            String result = jobj.getString("result");

                            if (result.equals("successfully")) {


                                id = jobj.getString("id");


                                Toast.makeText(getApplicationContext(), "Register  successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                startActivity(intent);

                            }
                            else if(result.equals("Email Already Exist")) {
                                Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_SHORT).show();
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

                name=editText_name.getText().toString();
                contact=editText_contact.getText().toString();
                email=editText_email.getText().toString();
                password=editText_password.getText().toString();
                cardnumber=editText_cardno.getText().toString();
                cardexpiry=editText_expiry.getText().toString();
                cardcvv=editText_cvv.getText().toString();

                Map<String, String> params = new HashMap<>();

                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("name",name);
                //Log.e("name",name);

                params.put("email",email);
                //Log.e("email",email);

                params.put("contact",contact);
                //Log.e("contact",contact);

                params.put("password",password);
                //Log.e("password",password);

                params.put("cardno",cardnumber);
                //Log.e("cardno",cardnumber);

                params.put("card_expiry",cardexpiry);
                //Log.e("card_expiry",cardexpiry);

                params.put("card_cvv",cardcvv);
                //Log.e("card_cvv",cardcvv);

               // Log.e("signup_params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
