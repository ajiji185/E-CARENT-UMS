package com.tryapp.myapplication3.e_carrentums;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "com.tryapp.myapplication3.e_carrentums.channel";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a notification for the foreground service
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.e_carent_ums_1)
                .setContentTitle("Accepted requesr")
                .setContentText("Your request has been accepted")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Start the foreground service and show the notification
        startForeground(NOTIFICATION_ID, builder.build());

        // Return the flag indicating that the service should persist
        // even if the calling component is destroyed
        return START_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
