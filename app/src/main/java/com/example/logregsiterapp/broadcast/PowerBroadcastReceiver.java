package com.example.logregsiterapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.logregsiterapp.utils.Actions;


public class PowerBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = PowerBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Actions.POWER_CONNECTED_ACTION)) {
            Log.d(TAG, "connected ");
        } else if (intent.getAction().equalsIgnoreCase(Actions.POWRE_DISCONNECTED_ACTION)) {
            Log.d(TAG, "disconnected");
        } else if (intent.getAction().equalsIgnoreCase(Actions.BATTERY_OKAY)) {
            Log.d(TAG, "okay");
        } else if (intent.getAction().equalsIgnoreCase(Actions.BATTERY_CHANGED)) {
            Log.d(TAG, "changed");
        } else if (intent.getAction().equalsIgnoreCase(Actions.BATTERY_LOW)) {
            Log.d(TAG, "low");
        }
    }
}
