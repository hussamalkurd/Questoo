package com.quest.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.quest.R;
import com.quest.activity.Drawer_Activity;
import com.quest.activity.WebViewActivity;
import com.quest.helper.OnBackPressedListener;
import com.quest.helper.PracticeTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PracticeTest_Fragment extends Fragment implements OnBackPressedListener {

    ListView listView;
    TextView textView;
    FragmentManager fragmentManager;
    MyAdapter myAdapter;
    ArrayList<PracticeTest> plist;
    String category_id,category,category_name,cat_id,category_image;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.practicetest_main, container, false);
        getActivity().setTitle("Category");

        preferences=getActivity().getSharedPreferences("Report",Context.MODE_PRIVATE);
        editor=preferences.edit();


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

        listView =(ListView) view.findViewById(R.id.practice_list);
        textView = (TextView) view.findViewById(R.id.textView);
        fragmentManager = getActivity().getSupportFragmentManager();


        // Load ads into Banner Ads
        myAdapter=new MyAdapter();
        plist=new ArrayList<>();
        fetchData();

        return view;
    }

    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.frm_drawer,new AdvertisementFragment()).commit();
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
                v = linf.inflate(R.layout.practicetest_item, parent, false);


                final TextView textView_name = (TextView) v.findViewById(R.id.test_name);
                textView_name.setText(plist.get(position).getTest_name());
                Log.e("categoryname",plist.get(position).getTest_name());

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        editor.putString("URL",plist.get(position).getTest_url());
                        editor.commit();
                     startActivity(new Intent(getActivity(), WebViewActivity.class));
                    }
                });

            } catch (Exception e) {

            }
            return v;
        }

    }
    private void fetchData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.merneadan.co.za/QuestoAdmin/process.php?action=fetch_practice_test",
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

                                category=jsonObject.getString("Test_name");
                               // Log.e("category",category);
                                category_image=jsonObject.getString("Type");
                               // Log.e("category_image",category_image);



                                PracticeTest home_item = new PracticeTest(category_id,category,category_image);
                                plist.add(home_item);
                               // Log.e("plisst", String.valueOf(plist));
                               listView.setAdapter(myAdapter);
                               myAdapter.notifyDataSetChanged();
                            }
                            if(listView.getAdapter()==null){
                                listView.setVisibility(View.GONE);
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
}
