package com.quest.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.sdk.InMobiSdk;

import com.quest.R;
import com.quest.activity.Drawer_Activity;
import com.quest.helper.PlacementId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dipak on 2/24/2018.
 */

public class AdvertisementFragment extends Fragment  {

    ImageView advImageView;

    LocationManager mlocManager;

    String advertisUrl, StringID;
    String adv_id ,type, image, app_id, ad_until_id;
    static final int REQUEST_LOCATION = 1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    ProgressDialog dialog;
    List<modelDetail> advertismentList = new ArrayList<modelDetail>();
    RelativeLayout adContainer;
    private InMobiBanner mBannerAd;
    public static final int BANNER_WIDTH = 320;
    public static final int BANNER_HEIGHT = 50;
    AdView mAdView ;
    private YouTubePlayer YPlayer;
    YouTubePlayer.OnInitializedListener mOnInitalized;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    FrameLayout youtubeFrameLayout;
    FragmentManager fragmentManager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
   LinearLayout lay_quiz,lay_wallet,lay_profile;
   TextView textmarguee;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MobileAds.initialize(getActivity(),getString(R.string.banner_home_APPID) );

        InMobiSdk.init(getActivity(), "e082c061860e4e93bc8e1f2f3856faba");
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);

        View mView = inflater.inflate(R.layout.advertiser_fragment, container, false);
        Drawer_Activity.toolbar.setVisibility(View.VISIBLE);
        getActivity().setTitle("Advertisement");
        fragmentManager=getActivity().getSupportFragmentManager();
        setHasOptionsMenu(true);

        textmarguee=(TextView)mView.findViewById(R.id.textmarguee);
        textmarguee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textmarguee.setText("Winner will announced soon...");
        textmarguee.setSelected(true);
        textmarguee.setSingleLine(true);

        fetchMarqueeText();

        lay_quiz=(LinearLayout)mView.findViewById(R.id.menu_quiz);
        lay_wallet=(LinearLayout)mView.findViewById(R.id.menu_wallet);
        lay_profile=(LinearLayout)mView.findViewById(R.id.menu_profile);

        lay_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new QCategoryFragment()).commit();
            }
        });
        lay_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new myWallet()).commit();
            }
        });
        lay_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.frm_drawer,new ProfileFragment()).commit();
            }
        });

        mAdView = (AdView)mView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.e("ADS", "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e("ADS", "onAdFailedToLoad"+i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.e("ADS", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.e("ADS", "onAdOpened");
                googleClick();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e("ADS", "onAdLoaded");

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.e("ADS", "onAdClicked");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.e("ADS", "onAdImpression");
            }
        });

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Please wait...");
        dialog.setCancelable(false);

        adContainer = (RelativeLayout) mView.findViewById(R.id.ad_container);

        setupBannerAd();

        mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        initGoogleAPIClient();
        checkPermissions();

        SharedPreferences prefs = getActivity().getSharedPreferences("userrecord", MODE_PRIVATE);

        StringID = prefs.getString("UserId", "");
        app_id = prefs.getString("app_id", "");
        ad_until_id = prefs.getString("ad_until_id", "");

        Log.e("UserId1", StringID);
        Log.e("app_id", app_id);
        Log.e("ad_until_id", ad_until_id);

        advImageView = (ImageView) mView.findViewById(R.id.advertisementImage);
       /* mYoutubeplay = (YouTubePlayerView)mView.findViewById(R.id.youtubePlay);
        getLocation();

        mOnInitalized = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {


                youTubePlayer.loadVideo(image);
                Log.e("newvideoUrl", image);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };*/

        youtubeFrameLayout = (FrameLayout)  mView.findViewById(R.id.youtubePlay);
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtubePlay, youTubePlayerFragment).commit();

        getLocation();

        return mView;

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
                            textmarguee.setText(winner);
                            textmarguee.setSelected(true);
                            textmarguee.setSingleLine(true);

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

    private void googleClick() {

        String advonClick = "http://www.merneadan.co.za/api/add_googleadd_click/"+"?user_id="+"670";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, advonClick, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String status =  response.getString("status");

                    if (status.equals("ok") ) {

                        Log.e("Click123 ", "success123");



                    }else {
                        String error = response.getString("error");
                        Log.e("error",error );
                        dialog.dismiss();
                        Log.e("Click ", "unSuccess");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: " , error.getMessage());
/*
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
*/
                // hide the progress dialog
                dialog.dismiss();

            }
        });

        RequestQueue q = Volley.newRequestQueue(getActivity());
        q.add(jsonObjectRequest);


    }


    void getLocation(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else {
            Location location =mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location != null){
                double latti = location.getLatitude();
                double longi = location.getLongitude();

                Log.e("latti",latti+"" );
                Log.e("longi", longi+"");

                advertisUrl   = "http://www.merneadan.co.za/api/"+"get_nearby_advertis/?lat=" + latti+ "&long=" + longi + "&user_id="+"670";//+StringID301
                Log.e("advertisUrl", advertisUrl);

               advertis(advertisUrl);

            }
            else {
                Log.e("Location", "Not Available");
            }
        }

    }

    private void advertis(String advertisUrl) {

        dialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, advertisUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e("responseadvertise", response.toString());
                    String status = response.getString("status");

                    Log.e("status", status);

                    if (status.equals("ok")) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONArray array = response.getJSONArray("advertising");
                            JSONObject prod = array.getJSONObject(i);

                            final modelDetail getSet = new modelDetail();

                            String title = prod.getString("title");
                            final String website = prod.getString("website");
                            image = prod.getString("image");
                            type = prod.getString("type");
                            adv_id = prod.getString("id");
                           // rewardImage = prod.getString("reward");
                           // rewardTitle = prod.getString("reward_name");

                            String newWebsite = website.replace("\\", "");
                            String newimage = image.replace("\\", "");

                            getSet.setWebsite(newWebsite);
                            getSet.setTitle(title);
                            getSet.setImage(newimage);
                            getSet.setId(adv_id);


                            Log.e("Title", title);
                            Log.e("type", type);
                            Log.e("image", image);
                            Log.e("adv_id", adv_id);
                            Log.e("newimage", newimage);
                            Log.e("newWebsite", newWebsite);
                          //  Log.e("rewardImage", rewardImage);
                         //   Log.e("rewardTitle", rewardTitle);
                            advertismentList.add(getSet);


                            final Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                int a = 0;

                                public void run() {
                                    if(type.equals("image")){
                                 //        mYoutubeplay.setVisibility(View.GONE);
                                        advImageView.setVisibility(View.VISIBLE);
                                        final modelDetail getSet = advertismentList.get(a);
                                        String imageUrl = getSet.getImage();

                                        final String websiteUrl = getSet.getWebsite();
                                        Log.e("imageUrl",imageUrl );
                                        Picasso.with(getActivity())
                                                .load(imageUrl)
                                                // .placeholder(R.drawable.logo)
                                                //    .error(R.drawable.logo)
                                                .into(advImageView);

                                        advImageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                onclickData();
                                                Log.e("Condiction",websiteUrl.isEmpty()+"" );
                                                if(websiteUrl.isEmpty()){
                                                    Toast.makeText(getActivity(), "No URL Found", Toast.LENGTH_SHORT).show();

                                                }else {
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.setPackage("com.android.chrome");
                                                    try {
                                                        startActivity(intent);
                                                    } catch (ActivityNotFoundException ex) {
                                                        // Chrome browser presumably not installed so allow user to choose instead
                                                        intent.setPackage(null);
                                                        startActivity(intent);
                                                    }}


                                            }
                                        });}
                                    else {


                                        final String vedioURL = image.replace("v=", "");


                                        youtubeFrameLayout.setVisibility(View.VISIBLE);
                                        advImageView.setVisibility(View.GONE);

                                        youTubePlayerFragment.initialize("AIzaSyCwQqMQzCIaVofPSjEuh3sao6eUpyxdcts", new YouTubePlayer.OnInitializedListener() {

                                            @Override
                                            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                                                if (!b) {
                                                    YPlayer = youTubePlayer;
                                                    YPlayer.setFullscreen(true);
                                                    YPlayer.loadVideo(vedioURL);
                                                    YPlayer.play();
                                                }
                                            }

                                            @Override
                                            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                                                // TODO Auto-generated method stub

                                            }
                                        });





                                        //   mYoutubeplay.initialize(youTubeConfig.getApiKey(), mOnInitalized);




                                     //   Toast.makeText(advertisement.this, "Vedio will play", Toast.LENGTH_SHORT).show();

                                    }

                                    a++;
                                    if (a > advertismentList.size() - 1) {
                                        a = 0;
                                    }
                                    handler.postDelayed(this, 60000);  //for interval...
                                }
                            };
                            handler.postDelayed(runnable, 10); //for initial delay..


                            //     imageListview.setAdapter(adaptor);
                            dialog.dismiss();
                        }
                    } else {
                        String error = response.getString("error");
                        Log.e("erroradvertise", error);
                        dialog.dismiss();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: ", error.getMessage());
             /*   Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
             */   // hide the progress dialog
                dialog.dismiss();

            }
        });

        RequestQueue q = Volley.newRequestQueue(getActivity());
        q.add(jsonObjectRequest);


    }

    private void onclickData() {

        String advonClick = "http://www.merneadan.co.za/api/add_advertise_click/"+"?user_id="+StringID +"&adv_id="+adv_id;//+StringID301

        Log.e("advonClick", advonClick);

        dialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, advonClick, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String status =  response.getString("status");

                    if (status.equals("ok") ) {

                        Log.e("Click ", "success");



                    }else {
                        String error = response.getString("error");
                        Log.e("error",error );
                        dialog.dismiss();
                        Log.e("Click ", "unSuccess");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: " , error.getMessage());
/*
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
*/
                // hide the progress dialog
                dialog.dismiss();

            }
        });

        RequestQueue q = Volley.newRequestQueue(getActivity());
        q.add(jsonObjectRequest);



    }

    private void initGoogleAPIClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkPermissions() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();

            else
                showSettingDialog();
            //   advertis();
        } else
            showSettingDialog();
        // advertis();

    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    private void showSettingDialog() {


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("GPS", "SUCCESS");
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        // updateGPSStatus("GPS is Enabled in your device");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        Log.e("GPS", "RESOLUTION_REQUIRED");       // a dialog.

                        try {
                            Log.e("GPS", "REQUEST_CHECK_SETTINGS");       // a dialog.

                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("GPS", "SETTINGS_CHANGE_UNAVAILABLE");
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getLocation();
                                //Do something after 100ms
                            }
                        }, 10000);



                        Log.e("Settings", "Result OK");
                        // updateGPSStatus("GPS is Enabled in your device");
                        //startLocationUpdates();
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        //  updateGPSStatus("GPS is Disabled in your device");
                        break;
                }
                break;
        }
    }

    private void setupBannerAd() {
        mBannerAd = new InMobiBanner(getActivity(), PlacementId.YOUR_PLACEMENT_ID);

        mBannerAd.setAnimationType(InMobiBanner.AnimationType.ROTATE_HORIZONTAL_AXIS);
        mBannerAd.setListener(new InMobiBanner.BannerAdListener() {
            @Override
            public void onAdLoadSucceeded(InMobiBanner inMobiBanner) {
//                Log.e(TAG, "onAdLoadSucceeded");
            }

            @Override
            public void onAdLoadFailed(InMobiBanner inMobiBanner,
                                       InMobiAdRequestStatus inMobiAdRequestStatus) {
//                Log.e(TAG, "Banner ad failed to load with error: " +
//                        inMobiAdRequestStatus.getMessage());
            }

            @Override
            public void onAdDisplayed(InMobiBanner inMobiBanner) {
//                Log.e(TAG, "onAdDisplayed");
            }

            @Override
            public void onAdDismissed(InMobiBanner inMobiBanner) {
//                Log.e(TAG, "onAdDismissed");
            }

            @Override
            public void onAdInteraction(InMobiBanner inMobiBanner, Map<Object, Object> map) {
//                Log.e(TAG, "onAdInteraction");
                googleClick();

            }

            @Override
            public void onUserLeftApplication(InMobiBanner inMobiBanner) {
//                Log.e(TAG, "onUserLeftApplication");
            }

            @Override
            public void onAdRewardActionCompleted(InMobiBanner inMobiBanner, Map<Object, Object> map) {
//                Log.e(TAG, "onAdRewardActionCompleted");
            }
        });
        setBannerLayoutParams();
        adContainer.addView(mBannerAd);
        mBannerAd.load();

    }
    private void setBannerLayoutParams() {
        int width = toPixelUnits(BANNER_WIDTH);
        int height = toPixelUnits(BANNER_HEIGHT);
        RelativeLayout.LayoutParams bannerLayoutParams = new RelativeLayout.LayoutParams(width, height);
        bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bannerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mBannerAd.setLayoutParams(bannerLayoutParams);
    }
    private int toPixelUnits(int dipUnit) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dipUnit * density);
    }
}
