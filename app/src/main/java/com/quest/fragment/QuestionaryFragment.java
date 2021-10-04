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
        import android.webkit.WebView;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
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
        import com.quest.helper.OnBackPressedListener;
        import com.quest.helper.URL;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;


public class QuestionaryFragment extends Fragment implements OnBackPressedListener{
    Button button_next;
    FragmentManager fragmentManager;
    ListView listView;
    MyAdapter myAdapter;
    ArrayList<Bean> plist;
    TextView textView,textView_pageno;
    String question_id,question,option_one,option_two,option_three,option_four,option,id,choose_answer;
    String category_id,category_name;
    String questionss, option_1, option_2,option_3,option_4;
    Button nextBtn, prevBtn;
    Paginator p = new Paginator();
    private int totalPages =p.getTotalPages();
    private int currentPage = 0;
    int lastpage=0;
    String   subcat_id, selected_question_id, user_email,answer, fetched_answer;

    String fetched_question_id,fetched_question,answer_one, answer_two,answer_three,answer_four,previous_answer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    WebView textView_question;
    RadioGroup radioGroup;
    RadioButton radioButton_one;
    RadioButton radioButton_two;
    RadioButton radioButton_three;
    RadioButton radioButton_four;
    LinearLayout lay_noitem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.frg_question, container, false);
        getActivity().setTitle("Question");

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

        listView = (ListView) view.findViewById(R.id.list);
        lay_noitem=(LinearLayout)view.findViewById(R.id.lay_noitem);
        textView = (TextView) view.findViewById(R.id.text);
        textView_pageno=(TextView)view.findViewById(R.id.number);

        nextBtn = (Button)view.findViewById(R.id.nextBtn);
        prevBtn = (Button)view.findViewById(R.id.prevBtn);

        fragmentManager=getActivity().getSupportFragmentManager();

        sharedPreferences=getActivity().getSharedPreferences("Report",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        category_id=sharedPreferences.getString("subcategory_id","");
        Log.e("subcategory_id",category_id);
        category_name=sharedPreferences.getString("category_name","");
        Log.e("subcategory_name",category_name);


        user_email=sharedPreferences.getString("user_email","");
        Log.e("user_email",user_email);




        plist = new ArrayList<>();



        fetchData();




        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (answer.equals("")){
                 Toast.makeText(getActivity(),"Please Select Answer",Toast.LENGTH_SHORT).show();
              }else {
                  insertAnswer();
              }


            }
        });


        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage-=1;



                myAdapter=new MyAdapter(getActivity(),new Paginator().getCurrentSpacecrafts(currentPage));
                listView.setAdapter(myAdapter);
                textView_pageno.setText((currentPage)+"/"+(lastpage-1));

                toggleButtons();


            }
        });


        return view;
    }


    private void toggleButtons()
    {

        if(currentPage==lastpage)
        {
            nextBtn.setEnabled(false);
            nextBtn.setVisibility(View.GONE);
            prevBtn.setVisibility(View.VISIBLE);
            prevBtn.setEnabled(true);
            fragmentManager.beginTransaction().replace(R.id.frm_drawer,new ThankYouFragment()).commit();
        }
        else if(currentPage==0)
        {
            nextBtn.setVisibility(View.VISIBLE);


            textView_pageno.setText((currentPage)+"/"+(lastpage-1));


            prevBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        }

        else
        if(currentPage>=1 && currentPage < new Paginator().getTotalPages() )
        {
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
            textView_pageno.setText((currentPage)+"/"+(lastpage-1));

            prevBtn.setVisibility(View.VISIBLE);
        }
        else if(currentPage<1){
            prevBtn.setVisibility(View.GONE);
            fragmentManager.beginTransaction().replace(R.id.frm_drawer,new myWallet()).commit();
        }

    }
    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.frm_drawer,new SubCategoryFragment()).commit();
    }


    private class MyAdapter extends BaseAdapter {
        private List<Bean> spacecraftss;
        Context mcontext;

        public MyAdapter(Context c,List<Bean> spacecrafts){
            this.mcontext=c;
            this.spacecraftss=spacecrafts;

        }

        @Override
        public int getCount() {
            return spacecraftss.size();
        }

        @Override
        public Object getItem(int i) {
            return spacecraftss.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            Bean ad = spacecraftss.get(i);
            LayoutInflater linf = getActivity().getLayoutInflater();
            View v = linf.inflate(R.layout.fragment_questionary, viewGroup, false);

            textView_question = (WebView) v.findViewById(R.id.question);
            radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
            radioButton_one = (RadioButton) v.findViewById(R.id.radio_one);
            radioButton_two = (RadioButton) v.findViewById(R.id.radio_two);
            radioButton_three = (RadioButton) v.findViewById(R.id.radio_three);
            radioButton_four = (RadioButton) v.findViewById(R.id.radio_four);


            questionss = ad.getQuestion();
         //   Log.e("qqqqqqqqqqq", questionss);
            option_1 = ad.getOption_one();


            option_2 = ad.getOption_two();


            option_3 = ad.getOption_three();


            option_4 = ad.getOption_four();

            selected_question_id = ad.getQuestion_id();
           Log.e("selected_question_id", selected_question_id);

            textView_question.loadData(questionss,"text/html", "utf-8");

            if (option_1.equals("")){
                radioButton_one.setVisibility(View.GONE);
            }else {
                radioButton_one.setVisibility(View.VISIBLE);
                radioButton_one.setText(option_1);
            }
            if (option_2.equals("")){
                radioButton_two.setVisibility(View.GONE);
            }else {
                radioButton_two.setVisibility(View.VISIBLE);
                radioButton_two.setText(option_2);
            }
            if (option_3.equals("")){
                radioButton_three.setVisibility(View.GONE);
            }else {
                radioButton_three.setVisibility(View.VISIBLE);
                radioButton_three.setText(option_3);
            }
            if (option_4.equals("")){
                radioButton_four.setVisibility(View.GONE);
            }else {
                radioButton_four.setVisibility(View.VISIBLE);
                radioButton_four.setText(option_4);
            }


            int selected_answer=spacecraftss.get(i).getAnswer();

            answer=spacecraftss.get(i).getChooseanswer();
            Log.e("aaaaaaaaaaa",answer);
         //   Toast.makeText(getActivity(),chooseanswer,Toast.LENGTH_LONG).show();

            if(selected_answer==1||answer.equalsIgnoreCase(option_1)){
                radioButton_one.setChecked(true);
            } else if(selected_answer==2||answer.equalsIgnoreCase(option_2)){
                radioButton_two.setChecked(true);
            } else if(selected_answer==3||answer.equalsIgnoreCase(option_3)){
                radioButton_three.setChecked(true);
            } else if(selected_answer==4||answer.equalsIgnoreCase(option_4)){
                radioButton_four.setChecked(true);
            } else{
                radioButton_one.setChecked(false);
                radioButton_two.setChecked(false);
                radioButton_three.setChecked(false);
                radioButton_four.setChecked(false);
            }

          radioButton_one.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  spacecraftss.get(i).setAnswer(1);
                  answer=option_1;
                  editor.putString("option", option_1);
                  Log.e("option_1", option_1);
                  editor.commit();
              }
          });
           radioButton_two.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   spacecraftss.get(i).setAnswer(2);
                   answer=option_2;
                   editor.putString("option", option_2);
                   Log.e("option_2", option_2);
                   editor.commit();
               }
           });
          radioButton_three.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  spacecraftss.get(i).setAnswer(3);
                  answer=option_3;
                  editor.putString("option", option_3);
                  Log.e("option_3", option_3);
                  editor.commit();
              }
          });

            radioButton_four.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spacecraftss.get(i).setAnswer(4);
                    answer=option_4;
                    editor.putString("option", option_4);
                    Log.e("option_4", option_4);
                    editor.commit();
                }
            });




            return v;
        }

    }

    private void fetchData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, com.quest.helper.URL.FETCH_QUESTION_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.e("questionresponse",response);

                        try {


                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jobj = jsonArray.getJSONObject(i);


                                question_id = jobj.getString("id");
                                //Log.e("question_id",question_id);

                                question = jobj.getString("question");
                               // Log.e("question",question);

                                option_one = jobj.getString("option1");
                                //Log.e("option_one", option_one);

                                option_two = jobj.getString("option2");
                                //Log.e("option_two", option_two);

                                option_three = jobj.getString("option3");
                                //Log.e("option_three", option_three);

                                option_four = jobj.getString("option4");
                                //Log.e("option_four", option_four);

                                choose_answer=jobj.getString("answer");
                                //Log.e("answer",choose_answer);


                                Bean item = new Bean(question_id,question,option_one,option_two,option_three,option_four,choose_answer);

                                plist.add(item);
                                //Log.e("plist", String.valueOf(plist));


                                myAdapter=new MyAdapter(getActivity(),new Paginator().getCurrentSpacecrafts(currentPage));
                                listView.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();

                                lastpage=new Paginator().getTotalPages();

                                lastpage=plist.size();

                                if (currentPage==0){

                                    textView_pageno.setText((currentPage)+"/"+(lastpage-1));
                                }



                            }
                            if (listView.getAdapter()==null){
                                lay_noitem.setVisibility(View.VISIBLE);
                            }


                        }
                        catch(JSONException e)
                        {

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
                params.put("cat_id",category_id);
                params.put("user_email",user_email);
                //Log.e("questionparams", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    public class Paginator {


        private int TOTAL_NUM_ITEMS=0;
        private int ITEMS_PER_PAGE=1;

        public Paginator() {
            try
            {
                TOTAL_NUM_ITEMS= plist.size();
                ITEMS_PER_PAGE=1;

            }catch (Exception e)
            {

            }

        }

        /*
        TOTAL NUMBER OF PAGES
         */
        public int getTotalPages()
        {
            return TOTAL_NUM_ITEMS/ITEMS_PER_PAGE;
        }

        /*
        CURRENT PAGE SPACECRAFTS LIST
         */
        public List<Bean> getCurrentSpacecrafts(int currentPage)
        {
            int startItem=currentPage*ITEMS_PER_PAGE;

            List<Bean> currentSpacecrafts=new ArrayList<>();
            try
            {
                //currentSpacecrafts=new RushSearch().limit(ITEMS_PER_PAGE).offset(startItem).find(Item.class);
                for (int j = 0; j < ITEMS_PER_PAGE; j++) {
                    currentSpacecrafts.add(j, plist.get(startItem));
                    startItem = startItem + 1;
                }
                //currentSpacecrafts.add(plist.get(startItem));

            }catch (Exception e)
            {
                e.printStackTrace();
            }


            return currentSpacecrafts;
        }

    }

    private void insertAnswer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.INSERT_ANSWER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response",response);

                            JSONObject jobj = new JSONObject(response);

                            String result = jobj.getString("result");

                            if (result.equals("successfully")) {
                                if (currentPage==lastpage){

                                    myAdapter=new MyAdapter(getActivity(),new Paginator().getCurrentSpacecrafts(currentPage));
                                    listView.setAdapter(myAdapter);
                                    toggleButtons();
                                    nextBtn.setEnabled(false);

                                    fragmentManager.beginTransaction().replace(R.id.frm_drawer,new ThankYouFragment()).commit();

                                }
                                else {

                                    currentPage+=1;
                                    myAdapter=new MyAdapter(getActivity(),new Paginator().getCurrentSpacecrafts(currentPage));
                                    listView.setAdapter(myAdapter);
                                    textView_pageno.setText((currentPage+1)+"/"+(lastpage-1));
                                    // insertAnswer();
                                    toggleButtons();
                                }


                            }

                            else{



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

                params.put("subcat_id",category_id);

                params.put("user_email",user_email);

                params.put("question_id",selected_question_id);

                params.put("answer",answer);

                Log.e("answer_params", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}
