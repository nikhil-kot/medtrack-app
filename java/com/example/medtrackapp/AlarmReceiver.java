/*
AlarmReceiver
This class creates the push notification that is sent to the user. This class creates the message associated with the push notification and sends it with the notification.
Version 1 and 6/06/2020
Sean Rhee
All of the imports below the package statement are dependencies.
 */

package com.example.medtrackapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder notificBuilder = new
                NotificationCompat.Builder(context, "notify")
                .setSmallIcon(R.drawable.baseline_person_black_18dp)
                .setContentTitle("Take meds")
                .setContentText("Take medicine")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, notificBuilder.build());
    }
}
