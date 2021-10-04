package com.quest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * Created by CISS31 on 7/15/2017.
 */

public class NotificationBrodcastreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      // Toast.makeText(context,"brodcast", Toast.LENGTH_SHORT).show();
        Intent newAct = new Intent(context,CustomAlert.class);
        newAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(newAct);
    }
}
