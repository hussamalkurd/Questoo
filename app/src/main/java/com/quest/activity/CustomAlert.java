package com.quest.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.quest.R;


public class CustomAlert extends AppCompatActivity {

    Button button_cancel;

    TextView textView_name,textView_contact;
    FragmentManager fragmentManager;

    SharedPreferences sharedPreferences;

    String user_name,user_id,user_contact,user_email,user_destination_address,user_source_address,regId,message,subcat_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater  = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.activity_custom_alert, null);
        builder.setView(dialogLayout);


        fragmentManager=getSupportFragmentManager();
        final AlertDialog customAlertDialog = builder.create();


        sharedPreferences=getSharedPreferences("Report", Context.MODE_PRIVATE);
        message=sharedPreferences.getString("message","");
        subcat_id=sharedPreferences.getString("subcategory_id","");
        Log.e("subcat_id",subcat_id);

        textView_name=(TextView)dialogLayout.findViewById(R.id.user_name);
        button_cancel=(Button)dialogLayout.findViewById(R.id.button_cancel);

        textView_name.setText(message);


        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                     customAlertDialog.dismiss();
                      CustomAlert.this.finish();
                    // fragmentManager.beginTransaction().replace(R.id.frm_drawer,new myWallet()).commit();

            }
        });

        customAlertDialog.show();

    }


}
