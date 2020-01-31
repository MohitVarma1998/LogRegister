package com.example.logregsiterapp.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.activities.AlarmManagerActivity;

public class ReceiveAlarmManager extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 0;


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context, AlarmManagerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,NOTIFICATION_ID,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Simple Title")
                .setContentText("Hello i wake up")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MIN)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }
}
