package com.example.tutorial1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        long value = intent.getLongExtra("new value", -10) + 10;
        Toast.makeText(context, "Broadcast Receiver catch an Intent\nValue: " + value, Toast.LENGTH_LONG).show();
    }
}