package com.quest.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quest.R;
import com.quest.helper.Bean;
import com.quest.helper.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TotalFragment extends Fragment {

    GridView gridView;
    ArrayList<Bean> plist;
    MyAdapter myAdapter;
    TextView textView;

    ImageView imageView;
    FragmentManager fragmentManager;


    String result,
            amount,
            quiz,user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.total, container, false);
        fragmentManager=getActivity().getSupportFragmentManager();
        gridView=(GridView)view.findViewById(R.id.winhistory);
        textView=(TextView)view.findViewById(R.id.total_amount);
        imageView=(ImageView)view.findViewById(R.id.back);



        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Report",Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("user_id","");
     //   Log.e("userid",user_id);

        plist=new ArrayList<>();
        myAdapter=new MyAdapter();

        fetchWinHistory();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             fragmentManager.beginTransaction().replace(R.id.frm_drawer,new myWallet()).commit();
            }
        });
        return view;
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
                v = linf.inflate(R.layout.list_total, parent, false);


                final  TextView textView_subcategory=(TextView)v.findViewById(R.id.win_amount);

                //Log.e("ssssssssss",plist.get(position).getCategory());
                textView_subcategory.setText(plist.get(position).getCategory());

            } catch (Exception e) {

            }
            return v;
        }

    }

    private void fetchWinHistory() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.GET_WINNING_PRICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("win_response",response);



                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String data=jsonObject.getString("result");
                           // Log.e("data",data);

                            String total=jsonObject.getString("total");
                           // Log.e("total",total);

                            textView.setText(total);


                            JSONArray jsonArray =jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jobj = jsonArray.getJSONObject(i);

                                result=jobj.getString("result");
                              //  Log.e("result",result);

                                amount=jobj.getString("amount");
                             //   Log.e("amount",amount);

                                quiz=jobj.getString("quiz");
                               // Log.e("quiz",quiz);

                                Bean item = new Bean(result,amount,quiz);

                                plist.add(item);
                                Log.e("plist", String.valueOf(plist));
                                gridView.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();




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
                Log.e("win_params", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
