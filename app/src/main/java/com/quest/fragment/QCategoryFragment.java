package com.quest.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.quest.R;
import com.quest.activity.Drawer_Activity;
import com.quest.helper.Bean;
import com.quest.helper.OnBackPressedListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class QCategoryFragment extends Fragment implements OnBackPressedListener {

    GridView gridView;
    TextView textView;
    FragmentManager fragmentManager;
    MyAdapter myAdapter;
    ArrayList<Bean> plist;
    String category_id,category,category_name,cat_id,category_image;
    AdView adView;
     TextView textmarguee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_qcategory, container, false);
        getActivity().setTitle("Category");

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

        gridView =(GridView) view.findViewById(R.id.gridview);
        textView = (TextView) view.findViewById(R.id.textView);
        fragmentManager = getActivity().getSupportFragmentManager();
        textmarguee=(TextView)view.findViewById(R.id.textmarguee);
        textmarguee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textmarguee.setText("Winner will announced soon...");
        textmarguee.setSelected(true);
        textmarguee.setSingleLine(true);

        fetchMarqueeText();

        adView = (AdView)view.findViewById(R.id.adView_category);

        AdRequest adRequest = new AdRequest.Builder()

                .build();

        // Load ads into Banner Ads
        adView.loadAd(adRequest);
        myAdapter=new MyAdapter();
        plist=new ArrayList<>();
        fetchData();

        return view;
    }

    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.frm_drawer,new AdvertisementFragment()).commit();
        /*Intent intent=new Intent(getActivity(), Drawer_Activity.class);
        startActivity(intent);*/
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
                v = linf.inflate(R.layout.category_items, parent, false);


                final TextView textView_category_name = (TextView) v.findViewById(R.id.category_name);
                textView_category_name.setText(plist.get(position).getCategory());
                Log.e("categoryname",plist.get(position).getCategory());
                  final CircleImageView circleImageView=(CircleImageView)v.findViewById(R.id.cat_image);

                Log.e("categoryname",plist.get(position).getImage());
                Picasso.with(getActivity()).load(plist.get(position).getImage()).into(circleImageView);



                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        category_name=plist.get(position).getCategory();
                        Log.e("category_nameeee",category_name);
                        cat_id=plist.get(position).getId();
                        Log.e("cat_iddd",cat_id);

                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Report", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("category_id",cat_id);
                        editor.putString("category_name",category_name);
                        editor.commit();
                        fragmentManager.beginTransaction().replace(R.id.frm_drawer,new SubCategoryFragment()).commit();
                        getActivity().setTitle("Subcategory");

                    }
                });



            } catch (Exception e) {

            }
            return v;
        }

    }
    private void fetchData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, com.quest.helper.URL.FETCH_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("category response",response);
                        try {

                            JSONArray jsonArray=new JSONArray(response);

                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                category_id=jsonObject.getString("id");
                               // Log.e("ccategory_id",category_id);

                                category=jsonObject.getString("category");
                               // Log.e("category",category);
                                category_image=jsonObject.getString("image");
                               // Log.e("category_image",category_image);



                                Bean home_item = new Bean(category_id,category,category_image);
                                plist.add(home_item);
                               // Log.e("plisst", String.valueOf(plist));
                               gridView.setAdapter(myAdapter);
                               myAdapter.notifyDataSetChanged();
                            }
                            if(gridView.getAdapter()==null){
                                gridView.setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);

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
                // params.put("Content-Type", "application/json; charset=utf-8");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void fetchMarqueeText() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.merneadan.co.za/QuestoAdmin/process.php?action=fetch_winner",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("category response",response);
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            String winner=jsonObject.getString("winner");
                            if (winner.equals("")){
                                textmarguee.setText("Winner will announced soon...");

                            }else {
                                textmarguee.setText(winner);
                                textmarguee.setSelected(true);
                                textmarguee.setSingleLine(true);
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

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
