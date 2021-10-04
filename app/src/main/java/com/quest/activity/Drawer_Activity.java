package com.quest.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;



import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quest.R;
import com.quest.fragment.AdvertisementFragment;
import com.quest.fragment.PracticeTest_Fragment;
import com.quest.fragment.ProfileFragment;
import com.quest.fragment.myWallet;
import com.quest.helper.OnBackPressedListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Drawer_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    String regId,email;
    ImageView imageView;
    public static Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    InterstitialAd mInterstitialAd;
    LinearLayout adParentLyout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_);
        FacebookSdk.sdkInitialize(getApplicationContext());
           toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        adParentLyout=(LinearLayout)findViewById(R.id.adParentLyout);
        imageView=(ImageView)findViewById(R.id.image_back);
          fragmentManager=getSupportFragmentManager();


        SharedPreferences sharedPreferences=getSharedPreferences("Report",MODE_PRIVATE);
        email=sharedPreferences.getString("user_email","");
        Log.e("email",email);


        regId = FirebaseInstanceId.getInstance().getToken();
      //  Log.e(regId,regId);
        imageView=(ImageView)findViewById(R.id.image_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        new AddTask().execute();

        fragmentManager.beginTransaction().replace(R.id.frm_drawer,new AdvertisementFragment()).commit();
        setTitle("Category");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
       toggle.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/*
       Handler handler=new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(Drawer_Activity.this,"handler",Toast.LENGTH_SHORT).show();
               showMainAds();
           }
       },1000);*/


    }
   /* private void initializerAds() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(PreferenceManager.getUnitInterstitialAdID(this));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                //AppHelper.LaunchActivity(MainActivity.this, SettingsActivity.class);
            }
        });

        requestNewInterstitial();
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }*/
    private void showMainAds() {
        if (ShowBannerAds(this)) {
            adParentLyout.setVisibility(View.VISIBLE);
            if (getUnitBannerAdsID(this) != null) {
                AdView mAdView = new AdView(this);
                mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(getUnitBannerAdsID(this));
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                if (mAdView.getAdSize() != null || mAdView.getAdUnitId() != null)
                    mAdView.loadAd(adRequest);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                adParentLyout.addView(mAdView, params);
            }
        } else {
            adParentLyout.setVisibility(View.GONE);
        }
    }
    public static boolean ShowBannerAds(Context mContext) {
       // mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return false;
    }
    public static String getUnitBannerAdsID(Context mContext) {
        String banner_id="ca-app-pub-3863931134581674~9570086736";
        return banner_id;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            if (fragmentList != null) {

                //TODO: Perform your logic to pass back press here
                for (Fragment fragment : fragmentList) {
                    if (fragment instanceof OnBackPressedListener) {
                        ((OnBackPressedListener) fragment).onBackPressed();
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            fragmentManager.beginTransaction().replace(R.id.frm_drawer,new AdvertisementFragment()).commit();
        /*    Intent intent=new Intent(Drawer_Activity.this,Drawer_Activity.class);
            startActivity(intent);
*/
            // Handle the camera action
        } else if (id == R.id.profile) {
            fragmentManager.beginTransaction().replace(R.id.frm_drawer,new ProfileFragment()).commit();


        }else if (id==R.id.practicetest){
            fragmentManager.beginTransaction().replace(R.id.frm_drawer,new PracticeTest_Fragment()).commit();
        }
        else if (id == R.id.logout) {

            androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(Drawer_Activity.this);
            alertDialogBuilder.setTitle("Always");
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            SharedPreferences sharedPreferences =getSharedPreferences("Report",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("user_email","");
                            editor.putString("user_id","");
                            editor.remove("subcategory_id");
                            editor.putString("subcategory_id","");
                            editor.commit();
                            LoginManager.getInstance().logOut();

                            startActivity(new Intent(Drawer_Activity.this, LoginActivity.class));

                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();



        }
     else if (id == R.id.wallet) {
        fragmentManager.beginTransaction().replace(R.id.frm_drawer,new myWallet()).commit();}


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class AddTask extends AsyncTask<String,Void,String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd=new ProgressDialog(Drawer_Activity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(true);
            // pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //code to do background process (Thread Job)
            String data="";

            String url="http://www.merneadan.co.za/QuestoAdmin/process.php?action=update_device_id";

            try{
                HttpPost postrequest=new HttpPost(url);
                ArrayList<NameValuePair> postlist=new ArrayList<NameValuePair>();
                postlist.add(new BasicNameValuePair("email",email));

                postlist.add(new BasicNameValuePair("device_id",regId));

                postrequest.setEntity(new UrlEncodedFormEntity(postlist));
                Log.e("postlist", String.valueOf(postlist));


                //HttpGet getReq=new HttpGet(url);
                HttpClient client=new DefaultHttpClient();
                HttpResponse resp = client.execute(postrequest);

                InputStream is =resp.getEntity().getContent();


                while(true)
                {
                    int i = is.read();
                    if(i==-1)
                        break;
                    data+=(char)i;
                }
                is.close();
            }
            catch(Exception e)
            {
                data=e.toString();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // pd.dismiss();

            try{

                JSONObject jobj=new JSONObject(result);
                String status = jobj.getString("result");
                Log.e("status",result);
                if (status.equals("updated successfully")){
                   // Toast.makeText(getApplicationContext(),"Update device id successfully",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"try again",Toast.LENGTH_LONG).show();
                }
            }
            catch(JSONException e)
            {

            }
        }


    }
}
