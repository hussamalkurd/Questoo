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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.quest.helper.Bean;
import com.quest.helper.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BalanceFragment extends Fragment {
    String userID,subcategoryId,user_email,balance;
    TextView yourBalance,Amount,ba;
    int intBalance=0;
    ListView listView;
    ArrayList<Bean> plist;
    MyAdapter myAdapter;
    TextView textView;
    String transaction_date,
            amount,
            subcat_id,cr_dr,user_id;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_balance, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();


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


        listView=(ListView)view.findViewById(R.id.list_history);
        textView=(TextView)view.findViewById(R.id.nohistoryavailable);
        getActivity().setTitle("Balance Fragment");
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Report",Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("user_id","");
        Log.e("userid",user_id);

        plist=new ArrayList<>();
        myAdapter=new MyAdapter();

        fetchHistory();

        return view;
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
                params.put("balance",balance);


                Log.e("getbalance_params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private class MyAdapter extends BaseAdapter {
        View v;

        @Override
        public int getCount() {
            return plist.size();
        }

        @Override
        public Object getItem(int position) {
            return plist.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            try {

                LayoutInflater linf = getActivity().getLayoutInflater();
                v = linf.inflate(R.layout.list_history_new, parent, false);


              //  final  TextView textView_subcategory=(TextView)v.findViewById(R.id.subName);
                final  TextView textView_rupee=(TextView)v.findViewById(R.id.rupee);
                final  TextView textView_time=(TextView)v.findViewById(R.id.date);
                final  TextView textView_cr=(TextView)v.findViewById(R.id.cr);

             //   textView_subcategory.setText(plist.get(position).getSubcat_id());


                textView_rupee.setText(plist.get(position).getAmount());


                textView_time.setText(plist.get(position).getTransaction_date());


                textView_cr.setText(plist.get(position).getCr_dr());

            } catch (Exception e) {

            }
            return v;
        }

    }

    private void fetchHistory() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.TRANSACTION_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("history_response",response);



                        try {


                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jobj = jsonArray.getJSONObject(i);


                                transaction_date = jobj.getString("transaction_date");
                                Log.e("transaction_date", transaction_date);

                                amount = jobj.getString("amount");
                                Log.e("amount", amount);

                                subcat_id = jobj.getString("subcat_id");
                                Log.e("subcat_id", subcat_id);

                                cr_dr = jobj.getString("cr_dr");
                                Log.e("cr_dr", cr_dr);


                                Bean item = new Bean(transaction_date, amount, subcat_id, cr_dr);

                                plist.add(item);
                                Log.e("plist", String.valueOf(plist));
                                listView.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();

                                if (listView.getAdapter() == null) {
                                    listView.setVisibility(View.GONE);
                                    textView.setVisibility(View.VISIBLE);
                                } else {

                                }
                            }
                        }
                        catch(JSONException e)
                        {
                            //Toast.makeText(DashboardActivity.this, e.toString(), 5).show();
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
                params.put("user_id",user_id);
                Log.e("history_params", String.valueOf(params));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
