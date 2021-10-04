package com.quest.fragment;

import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.quest.R;
import com.quest.activity.CheckoutActivityJava;
import com.quest.activity.Drawer_Activity;
import com.quest.activity.SendMoney;
import com.quest.helper.Bean;
import com.quest.helper.OnBackPressedListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wdl-android on 01-05-2018.
 */

public class myWallet extends Fragment implements OnBackPressedListener{

    ImageView imageBalance;
    ArrayList<Bean> plist;
    SharedPreferences SM;
    EditText edtMobileNo;
    ProgressDialog dialog;
    String userID,subcategoryId,user_email;
    TextView yourBalance,Amount,ba;
    LinearLayout addBalance;
    EditText edtAmount, edtOpeator;
    Dialog dialog1, dialog2,dialog3,dialog4 ;
    TextView History, Transaction ;
    View lineFrature, lineLatest;

    LinearLayout quick_pay,Received,history,send_money,bal;

    Button stripeBt;
    List<modelDetail> cityList = new ArrayList<modelDetail>();
    int intBalance=0;
    String operator;
    AdView adView;
    WebView webView;
    FragmentManager fragmentManager;
    EditText amtRecharge;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View mView = inflater.inflate(R.layout.fragment_mywallet, container, false);
        getActivity().setTitle("Wallet");

        fragmentManager = getActivity().getSupportFragmentManager();

        Drawer_Activity.toolbar.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ImageView contact_back=(ImageView)mView.findViewById(R.id.contact_back);
        contact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new AdvertisementFragment()).commit();
            }
        });


        SM = getActivity().getSharedPreferences("Report", MODE_PRIVATE);

        String MobileNo = SM.getString("mobile", "");
        userID = SM.getString("user_id", "");
        Log.e("userId", userID);
        subcategoryId = SM.getString("subcategory_id", "");
        Log.e("subcategory_id", subcategoryId);

        user_email=SM.getString("user_email","");


        edtMobileNo = (EditText) mView.findViewById(R.id.MobileNo);
        yourBalance = (TextView) mView.findViewById(R.id.yourBalance);
        Amount = (TextView) mView.findViewById(R.id.balance);
        addBalance = (LinearLayout) mView.findViewById(R.id.addBalance);
        History = (TextView) mView.findViewById(R.id.txtFeatured);
        Transaction = (TextView) mView.findViewById(R.id.txtLatest);
        lineFrature = (View) mView.findViewById(R.id.lineFeatured);
        lineLatest = (View) mView.findViewById(R.id.lineLatest);
        quick_pay = (LinearLayout) mView.findViewById(R.id.quick_pay);
        Received = (LinearLayout) mView.findViewById(R.id.received);
        history = (LinearLayout) mView.findViewById(R.id.history);
        send_money = (LinearLayout) mView.findViewById(R.id.SendMoney);
        imageBalance = mView.findViewById(R.id.image_balance);
        stripeBt = mView.findViewById(R.id.stripe_button);
//        sharedPreferences=getActivity().getSharedPreferences("Report", Context.MODE_PRIVATE);
//        amount=sharedPreferences.getString("balance","");
//        Log.e("balance",amount);
        //ba.setText(amount);

        adView = (AdView) mView.findViewById(R.id.adView_foryou);

        AdRequest adRequest = new AdRequest.Builder()

                .build();

        // Load ads into Banner Ads
        adView.loadAd(adRequest);


        edtMobileNo.setText(MobileNo);

        SharedPreferences sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences("userrecord", getActivity().MODE_PRIVATE).edit();


        stripeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheckoutActivityJava.class);
                getActivity().startActivity(intent);
            }
        });

        send_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog2 = new Dialog(getActivity());
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setCancelable(false);
                dialog2.setContentView(R.layout.sendmoney);
                final Window window = dialog2.getWindow();
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);


                Button btn_send, btnCancel;
                final TextView numberMobile;

                numberMobile = dialog2.findViewById(R.id.number);
                btn_send = dialog2.findViewById(R.id.btnSendMoney);
                btnCancel = dialog2.findViewById(R.id.btnCancel);

                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String number = numberMobile.getText().toString();

                        if(number.isEmpty()){
                            Toast.makeText(getActivity(),
                                    "Please Enter Mobile Number" , Toast.LENGTH_SHORT).show();

                        }else {


                            SharedPreferences.Editor editor1 = getActivity().getSharedPreferences("userrecord", getActivity().MODE_PRIVATE).edit();
                            editor1.putString("number", number);
                            editor1.commit();

                            Intent intent = new Intent(getActivity(), SendMoney.class);
                            intent.putExtra("mobileNumber", number);
                            intent.putExtra("balance", intBalance);
                            getActivity().startActivity(intent);

                            //  paymentMethod();
                            dialog2.dismiss();
                            //  addMoney(amt);
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog2.dismiss();
                    }
                });



                dialog2.show();

            }
        });


        quick_pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (subcategoryId.equals("")){
                    fragmentManager.beginTransaction().replace(R.id.frm_drawer,new QCategoryFragment()).commit();
                }
                else {
                    dialog4 = new Dialog(getContext());
                    dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog4.setCancelable(false);
                    dialog4.setContentView(R.layout.paymoney);
                    final Window window = dialog4.getWindow();
                    window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    Button btnRecharge, btnCancel;

                    btnRecharge = (Button) dialog4.findViewById(R.id.proceedBtn);
                    amtRecharge = (EditText) dialog4.findViewById(R.id.rechareAmt);

                    FETCHPayment();
                    btnCancel = dialog4.findViewById(R.id.cancelBtn);

                    btnRecharge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                             String amount = amtRecharge.getText().toString();
                            Log.e("amount", amount);
                            try {
                                int intAmaount = Integer.parseInt(amount);


                                if (intBalance < intAmaount) {

                                    Toast.makeText(getActivity(), "Your Balance is low", Toast.LENGTH_SHORT).show();
                                } else {
                                    fragmentManager.beginTransaction().replace(R.id.frm_drawer,new QuestionaryFragment()).commit();
                                    getRecharge(amount);
                                    dialog4.dismiss();
                                    dialog3.dismiss();

                                }

                            } catch (Exception e) {

                            }

                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog4.dismiss();
                        }
                    });


                    dialog4.show();

                }
            }
        });
        addBalance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dialog1 = new Dialog(getActivity());
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setCancelable(false);
                dialog1.setContentView(R.layout.add_balance_dailog);
                final Window window = dialog1.getWindow();
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);


                Button btn_add, btnCancel;




                edtAmount = dialog1.findViewById(R.id.amount);
                btn_add = dialog1.findViewById(R.id.addMoney);
                btnCancel = dialog1.findViewById(R.id.btnCancel);


                   btn_add.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       String amt = edtAmount.getText().toString();

                       if(amt.isEmpty()){
                           Toast.makeText(getActivity(),
                                   "Please Enter Amount" , Toast.LENGTH_SHORT).show();

                       }
                       else {


                           SharedPreferences.Editor editor1 = getActivity().getSharedPreferences("Report", getActivity().MODE_PRIVATE).edit();
                           editor1.putString("amt", amt);
                           editor1.commit();

                           Intent intent = new Intent(getActivity(), webview.class);

                           getActivity().startActivity(intent);

                           dialog1.dismiss();

                   }
                   }
               });


                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog1.dismiss();
                    }
                });
                dialog1.show();
            }
        });
            Received.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.beginTransaction().replace(R.id.frm_drawer,new TotalFragment()).commit();
                    getActivity().setTitle("Rewards");
                }
            });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new HistoryFragment()).commit();
                getActivity().setTitle("Transaction History");
            }
        });
//        imageBalance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new BalanceFragment()).commit();
//                getActivity().setTitle("Balance Fragment");
//            }
//        });

      getBalance();



        return mView;
    }


    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


    private void getRecharge( final String amount) {


//        dialog.show();



        StringRequest strReq = new StringRequest(Request.Method.POST,"http://www.merneadan.co.za/QuestoAdmin/process.php?action=wallet_mobile_recharge", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                  //  dialog3.dismiss();
                  //  dialog.dismiss();
                    JSONObject job = new JSONObject(response);
                    Log.e("response",response);

                    String status = job.getString("status");
                    Log.e("msg", status);

                   /* String url=job.getString("url");
                    Log.e("url", url);

*/
                    if (status.equals("success") ) {

                        String msg = job.getString("msg");

                        Log.e("msg", msg);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(myWallet.this).attach(myWallet.this).commit();

                        SendCounter();

                    }

                    else{
                        String msg = job.getString("msg");
                        Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
                        String errorMsg = job.getString("error");
                        Log.e("Json Error for register",msg);

                    }
                } catch (JSONException e) {
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

                        Toast.makeText(getActivity(), status, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }}

            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_id", userID);
                params.put("amount", amount);
                params.put("subcat_id", subcategoryId);
                Log.e("param", params+"");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);

    }

    private void getBalance() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.merneadan.co.za/QuestoAdmin/process.php?action=wallet_getbalance",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response",response);

                            JSONObject jobj = new JSONObject(response);
                            String result = jobj.getString("status");
                            if (result.equals("success")) {
                                String balance =  jobj.getString("balance");
                                intBalance = Integer.parseInt(balance);
                                String msg =  jobj.getString("msg");
                                yourBalance.setText(msg+":");
                                Amount.setText("R"+" "+balance);
                                Log.e("balance", balance);
                            }
                            else
                            {
                                String msg =  jobj.getString("msg");
                                Toast.makeText(getActivity(),
                                        msg , Toast.LENGTH_SHORT).show();
                                String error = jobj.getString("error");
                                Log.e("error",error );
                                //   dialog.dismiss();
                                //    getHistory();
                                Toast.makeText(getActivity(), "Invalid Login", Toast.LENGTH_SHORT).show();
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
                // params.put("Content-Type", "application/json; charset=utf-8");
                params.put("id",userID);

                Log.e("getbalance_params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void FETCHPayment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.merneadan.co.za/QuestoAdmin/process.php?action=fetch_payment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response",response);

                            final JSONObject jobj = new JSONObject(response);

                            String id = jobj.getString("id");

                                String payment=jobj.getString("payment");

                                amtRecharge.setText(payment);

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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void SendCounter() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.merneadan.co.za/QuestoAdmin/process.php?action=insert_counter",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response",response);

                            final JSONObject jobj = new JSONObject(response);

                            String status = jobj.getString("result");
                            if (status.equals("successfull updated")){
                                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new QuestionaryFragment()).commit();
                            }else {
                                Toast.makeText(getActivity(),"Something went Wrong",Toast.LENGTH_SHORT).show();
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
                params.put("email",user_email);
                params.put("subcat_id",subcategoryId);
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
