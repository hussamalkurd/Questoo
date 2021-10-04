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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.quest.R;
import com.quest.activity.Drawer_Activity;
import com.quest.helper.Bean;
import com.quest.helper.OnBackPressedListener;
import com.quest.helper.URL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SubCategoryFragment extends Fragment implements OnBackPressedListener {

    FragmentManager fragmentManager;
    ListView listView;

   MyAdapter myAdapter;
    ArrayList<Bean> plist;
    TextView textView;
    AdView adView;

    String category_id,category_name,user_id;
    String sub_id,sub_category,sub_category_image;
    String subcategory_name,subcategory_id;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sub_category, container, false);
        getActivity().setTitle("SubCategory");

        Drawer_Activity.toolbar.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ImageView contact_back=(ImageView)view.findViewById(R.id.contact_back);
        contact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new QCategoryFragment()).commit();
            }
        });

        listView = (ListView) view.findViewById(R.id.sub_list);
        textView = (TextView) view.findViewById(R.id.sub_text);
        fragmentManager=getActivity().getSupportFragmentManager();
        adView = (AdView)view.findViewById(R.id.adView_sub);

        AdRequest adRequest = new AdRequest.Builder()

                .build();

        // Load ads into Banner Ads
        adView.loadAd(adRequest);


        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Report",Context.MODE_PRIVATE);
        category_id=sharedPreferences.getString("category_id","");
        Log.e("category_id",category_id);
        category_name=sharedPreferences.getString("category_name","");
        Log.e("category_name",category_name);
        user_id=sharedPreferences.getString("user_id","");
        Log.e("user_id",user_id );

        plist = new ArrayList<>();
        myAdapter = new MyAdapter();


        fetchData();


        return view;
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


    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.frm_drawer,new QCategoryFragment()).commit();

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
                v = linf.inflate(R.layout.subcat_item, parent, false);


                final  TextView textView_subcategory=(TextView)v.findViewById(R.id.sub_name);
                final ImageView imageView_sub=(ImageView)v.findViewById(R.id.sub_image);


                Log.e("sub_category",plist.get(position).getCategory());
                textView_subcategory.setText(plist.get(position).getCategory());

                Log.e("sub_image",plist.get(position).getImage());
                Picasso.with(getActivity()).load(plist.get(position).getImage()).into(imageView_sub);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        subcategory_name=plist.get(position).getCategory();
                        subcategory_id=plist.get(position).getId();

                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Report",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("subcategory_id",subcategory_id);
                        editor.putString("subcategory_name",subcategory_name);
                        Log.e("subcategory",subcategory_name);
                        editor.commit();

                        fragmentManager.beginTransaction().replace(R.id.frm_drawer,new myWallet()).commit();
                        getActivity().setTitle("Questions");
                    }
                });




            } catch (Exception e) {

            }
            return v;
        }

    }
    private void fetchData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.FETCH_SUBCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("sub_category_response",response);

                        try {


                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jobj = jsonArray.getJSONObject(i);


                                sub_id = jobj.getString("id");
                                Log.e("sub_id",sub_id);

                                sub_category = jobj.getString("category");
                                Log.e("sub_category",sub_category);

                                sub_category_image = jobj.getString("image");
                                Log.e("sub_category_image", sub_category_image);



                                Bean item = new Bean(sub_id,sub_category,sub_category_image);

                                plist.add(item);
                                Log.e("plist", String.valueOf(plist));
                                listView.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();

                                if(listView.getAdapter()==null){
                                    listView.setVisibility(View.GONE);
                                    textView.setVisibility(View.VISIBLE);

                                }

                                else
                                {

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
                params.put("cat_id",category_id);
                Log.e("subcategory_params", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }




}
