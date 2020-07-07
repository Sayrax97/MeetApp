package com.team4infinity.meetapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.team4infinity.meetapp.models.Event;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

public class MyMessageingService extends FirebaseMessagingService {
    private static final String TAG = "MyMessageingService";
    private static final String CHANNEL_ID = "team.4infinity";
    private static final String CHANNEL_NAME = "Team 4infinity";
    private static final String CHANNEL_DESC = "Team 4infinity Notifications";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(CHANNEL_DESC);
        NotificationManager manager=getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        displayNotification(getApplicationContext(),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getData().get("event"));
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

    }

    private static void displayNotification(Context context,String title,String body,String key) {
        Intent intent=new Intent(context, EventActivity.class);
        intent.putExtra("key",key);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,100,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.meet_app_logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());
    }
}
